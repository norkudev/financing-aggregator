package lv.norkudev.financingaggregator.service;

import lv.norkudev.financingaggregator.banks.fast.FastBankService;
import lv.norkudev.financingaggregator.banks.solid.SolidBankService;
import lv.norkudev.financingaggregator.model.ApplicationOffer;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lv.norkudev.financingaggregator.model.SubmitApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AggregationService {

    private final FastBankService fastBankService;
    private final SolidBankService solidBankService;

    public Mono<List<ApplicationOffer>> submitApplication(SubmitApplication request) {
        return Flux.merge(fastBankService.submitApplication(request), solidBankService.submitApplication(request))
                .sort(Comparator.comparing(ApplicationOffer::api))
                .collectList();
    }

}
