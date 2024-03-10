package lv.norkudev.financingaggregator.banks.solid;

import lombok.RequiredArgsConstructor;
import lv.norkudev.financingaggregator.model.ApplicationOffer;
import lv.norkudev.financingaggregator.model.SubmitApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SolidBankService {

    private final SolidBankMapper mapper;

    private WebClient solidBankClient;
    private int retryAfterSeconds;

    @Value("${banks.retry-after-seconds:5}")
    public void setRetryAfterSeconds(int retryAfterSeconds) {
        this.retryAfterSeconds = retryAfterSeconds;
    }

    @Autowired
    public void setSolidBankClient(WebClient solidBankClient) {
        this.solidBankClient = solidBankClient;
    }

    public Mono<ApplicationOffer> submitApplication(SubmitApplication submitApplication) {
        var request = mapper.toApplicationRequest(submitApplication);
        return solidBankClient.post()
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
        return solidBankClient.get()
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
