package lv.norkudev.financingaggregator.controller;

import lombok.RequiredArgsConstructor;
import lv.norkudev.financingaggregator.model.ApplicationOffer;
import lv.norkudev.financingaggregator.model.SubmitApplication;
import lv.norkudev.financingaggregator.service.AggregationService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/applications")
@RequiredArgsConstructor
public class AggregationController {

    private final AggregationService aggregationService;

    @PostMapping
    public Mono<List<ApplicationOffer>> submitApplication(@RequestBody SubmitApplication submitApplication) {
        return aggregationService.submitApplication(submitApplication);
    }

    @GetMapping("/{submitterUuid}")
    public Mono<List<ApplicationOffer>> getApplicationOffers(@PathVariable UUID submitterUuid) {
        return aggregationService.getApplicationOffers(submitterUuid);
    }

}
