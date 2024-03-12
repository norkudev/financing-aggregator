package lv.norkudev.financingaggregator.service;

import lv.norkudev.financingaggregator.banks.fast.FastBankService;
import lv.norkudev.financingaggregator.banks.solid.SolidBankService;
import lv.norkudev.financingaggregator.cache.ApplicationOfferCacheService;
import lv.norkudev.financingaggregator.model.ApplicationOffer;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lv.norkudev.financingaggregator.model.SubmitApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class AggregationService {

    private static final Comparator<ApplicationOffer> OFFER_COMPARATOR = Comparator.comparing(ApplicationOffer::api);

    private final FastBankService fastBankService;
    private final SolidBankService solidBankService;
    private final ApplicationOfferCacheService applicationOfferCacheService;
    private final ConcurrentMap<UUID, Mono<List<ApplicationOffer>>> submittedApplications = new ConcurrentHashMap<>();

    public Mono<List<ApplicationOffer>> submitApplication(SubmitApplication request) {
        var submitterUuid = request.submitterUuid();

        var submissionAlreadyInProgress = submittedApplications.get(submitterUuid);
        if (submissionAlreadyInProgress != null) {
            return submissionAlreadyInProgress;
        }

        var cachedOffers = getApplicationOffers(submitterUuid);
        return cachedOffers.switchIfEmpty(Mono.defer(() -> {
            var submitRequest = Flux.merge(fastBankService.submitApplication(request), solidBankService.submitApplication(request))
                    .sort(OFFER_COMPARATOR)
                    .collectList()
                    .doOnNext(applicationOffers -> applicationOfferCacheService.cacheOffers(request.submitterUuid(), applicationOffers))
                    .doFinally(signalType -> submittedApplications.remove(submitterUuid));
            submittedApplications.put(submitterUuid, submitRequest);
            return submitRequest;
        }));
    }

    public Mono<List<ApplicationOffer>> getApplicationOffers(UUID submitterUuid) {
        return applicationOfferCacheService.getCachedOffers(submitterUuid)
                .map(list -> list.stream().sorted(OFFER_COMPARATOR).toList());
    }

}
