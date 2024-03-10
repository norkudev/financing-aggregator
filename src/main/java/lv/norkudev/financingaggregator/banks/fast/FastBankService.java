package lv.norkudev.financingaggregator.banks.fast;

import lv.norkudev.financingaggregator.banks.BaseBankMapper;
import lv.norkudev.financingaggregator.banks.BaseBankService;
import lv.norkudev.financingaggregator.model.ApplicationOffer;
import lv.norkudev.financingaggregator.model.SubmitApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FastBankService extends BaseBankService<Application, ApplicationRequest> {

    private final FastBankMapper mapper;

    private WebClient fastBankClient;

    @Autowired
    public void setFastBankClient(WebClient fastBankClient) {
        this.fastBankClient = fastBankClient;
    }

    @Override
    public Mono<ApplicationOffer> submitApplication(SubmitApplication submitApplication) {
        return retrieve(submitApplication)
                .bodyToMono(Application.class)
                .flatMap(application -> getLatestOffer(UUID.fromString(application.getId())))
                .filter(application -> application.getOffer() != null)
                .map(mapper::toApplicationOffer)
                .onErrorResume(throwable -> Mono.empty());
    }

    @Override
    protected Mono<Application> getLatestOffer(UUID id) {
        return retrieve(id)
                .bodyToMono(Application.class)
                .flatMap(application -> {
                    if (application.getStatus() == Application.StatusEnum.PROCESSED) {
                        return Mono.just(application);
                    } else {
                        return Mono.delay(Duration.ofSeconds(retryAfterSeconds))
                                .then(getLatestOffer(UUID.fromString(application.getId())));
                    }
                });
    }

    @Override
    protected WebClient getClient() {
        return fastBankClient;
    }

    @Override
    protected BaseBankMapper<Application, ApplicationRequest> getMapper() {
        return mapper;
    }
}
