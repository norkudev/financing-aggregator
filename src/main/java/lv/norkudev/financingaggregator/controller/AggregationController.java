package lv.norkudev.financingaggregator.controller;

import lombok.RequiredArgsConstructor;
import lv.norkudev.financingaggregator.model.ApplicationOffer;
import lv.norkudev.financingaggregator.model.SubmitApplication;
import lv.norkudev.financingaggregator.service.AggregationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController("/v1")
@RequiredArgsConstructor
public class AggregationController {

    private final AggregationService aggregationService;

    @PostMapping("/applications")
    public Mono<List<ApplicationOffer>> submitApplication(@RequestBody SubmitApplication submitApplication) {
        return aggregationService.submitApplication(submitApplication);
    }

}
