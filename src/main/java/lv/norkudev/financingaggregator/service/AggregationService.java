package lv.norkudev.financingaggregator.service;

import lv.norkudev.financingaggregator.banks.fast.FastBankService;
import lv.norkudev.financingaggregator.banks.solid.SolidBankService;
import lv.norkudev.financingaggregator.model.ApplicationOffer;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lv.norkudev.financingaggregator.model.SubmitApplication;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AggregationService {

    private final FastBankService fastBankService;
    private final SolidBankService solidBankService;

    public Mono<List<ApplicationOffer>> submitApplication(SubmitApplication request) {
        Mono<ApplicationOffer> fastBankResponse = fastBankService.submitApplication(request);
        Mono<ApplicationOffer> solidBankResponse = solidBankService.submitApplication(request);

        return Mono.zip(fastBankResponse, solidBankResponse)
                .map(this::toResponseArray)
                .defaultIfEmpty(Collections.emptyList());
    }

    private List<ApplicationOffer> toResponseArray(Tuple2<ApplicationOffer, ApplicationOffer>
                                                       responses) {
        final var offers = new ArrayList<ApplicationOffer>();
        var t1 = responses.getT1();
        Optional.ofNullable(t1)
                .ifPresent(offers::add);
        var t2 = responses.getT2();
        Optional.ofNullable(t2)
                .ifPresent(offers::add);

        return offers;
    }

}
