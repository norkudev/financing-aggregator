package lv.norkudev.financingaggregator.banks.fast;

import lv.norkudev.financingaggregator.model.ApplicationOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lv.norkudev.financingaggregator.model.SubmitApplication;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FastBankService {

    private final FastBankMapper mapper;

    private WebClient fastBankClient;
    private int retryAfterSeconds;

    @Value("${banks.retry-after-seconds:5}")
    public void setRetryAfterSeconds(int retryAfterSeconds) {
        this.retryAfterSeconds = retryAfterSeconds;
    }

    @Autowired
    public void setFastBankClient(WebClient fastBankClient) {
        this.fastBankClient = fastBankClient;
    }

    public Mono<ApplicationOffer> submitApplication(SubmitApplication submitApplication) {
        var request = mapper.toApplicationRequest(submitApplication);
        return fastBankClient.post()
                .uri("/applications")
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(Application.class)
                .flatMap(application -> getLatestOffer(UUID.fromString(application.getId())))
                .filter(application -> application.getOffer() != null)
                .map(mapper::toApplicationOffer)
                .onErrorResume(Exception.class, ex -> Mono.justOrEmpty(Optional.empty()));
    }

    private Mono<Application> getLatestOffer(UUID id) {
        return fastBankClient.get()
                .uri("/applications/" + id)
                .retrieve()
                .bodyToMono(Application.class)
                .flatMap(application -> {
                    if (application.getStatus() == Application.StatusEnum.PROCESSED) {
                        return Mono.just(application);
                    } else  {
                        return Mono.delay(Duration.ofSeconds(retryAfterSeconds))
                                .then(getLatestOffer(UUID.fromString(application.getId())));
                    }
                });
    }

}
