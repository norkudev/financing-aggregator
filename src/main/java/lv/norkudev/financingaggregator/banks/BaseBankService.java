package lv.norkudev.financingaggregator.banks;

import lv.norkudev.financingaggregator.model.ApplicationOffer;
import lv.norkudev.financingaggregator.model.SubmitApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

public abstract class BaseBankService<T, U> {
    protected int retryAfterSeconds;

    @Value("${banks.retry-after-seconds:5}")
    public void setRetryAfterSeconds(int retryAfterSeconds) {
        this.retryAfterSeconds = retryAfterSeconds;
    }

    protected abstract WebClient getClient();
    protected abstract BaseBankMapper<T, U> getMapper();
    public abstract Mono<ApplicationOffer> submitApplication(SubmitApplication submitApplication);
    protected abstract Mono<T> getLatestOffer(UUID id);

    protected WebClient.ResponseSpec retrieve(SubmitApplication submitApplication) {
        var request = getMapper().toApplicationRequest(submitApplication);
        return getClient().post()
                .uri("/applications")
                .body(BodyInserters.fromValue(request))
                .retrieve();
    }

    protected WebClient.ResponseSpec retrieve(UUID id) {
        return getClient().get()
                .uri("/applications/" + id)
                .retrieve();
    }

}
