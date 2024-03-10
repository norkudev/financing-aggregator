package lv.norkudev.financingaggregator.service;

import lv.norkudev.financingaggregator.banks.fast.FastBankService;
import lv.norkudev.financingaggregator.banks.solid.ApplicationRequest;
import lv.norkudev.financingaggregator.banks.solid.SolidBankService;
import lv.norkudev.financingaggregator.model.ApplicationOffer;
import lv.norkudev.financingaggregator.model.SubmitApplication;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class AggregationServiceTest {

    private MockWebServer solidbank;
    private MockWebServer fastbank;

    @Autowired
    private AggregationService aggregationService;
    @Autowired
    private SolidBankService solidBankService;
    @Autowired
    private FastBankService fastBankService;

    @BeforeEach
    void setUp() {
        solidbank = new MockWebServer();
        solidBankService.setSolidBankClient(WebClient.builder()
                        .baseUrl(solidbank.url("/api/SolidBank").toString())
                .build());
        fastbank = new MockWebServer();
        fastBankService.setFastBankClient(WebClient.builder()
                .baseUrl(fastbank.url("/api/FastBank").toString())
                .build());
    }

    @AfterEach
    void tearDown() throws IOException {
        solidbank.shutdown();
        fastbank.shutdown();
    }

    @Test
    public void testBothOffers() {
        solidbank.enqueue(new MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\n" +
                        "\"id\": \"1d00bf22-24a1-4ee1-907d-230b35397ba9\",\n" +
                        "\"status\": \"DRAFT\",\n" +
                        "\"offer\": null\n" +
                        "}")
        );
        solidbank.enqueue(new MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\n" +
                        "\"id\": \"5fb16219-1d4f-400b-b316-058fd4bb79ba\",\n" +
                        "\"status\": \"PROCESSED\",\n" +
                        "\"offer\": {\n" +
                        "\"monthlyPaymentAmount\": 883.33,\n" +
                        "\"totalRepaymentAmount\": 5300.00,\n" +
                        "\"numberOfPayments\": 6,\n" +
                        "\"annualPercentageRate\": 10.11,\n" +
                        "\"firstRepaymentDate\": \"2024-04-08\"\n" +
                        "}\n" +
                        "}")
        );

        fastbank.enqueue(new MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\n" +
                        "\"id\": \"5743e234-0fa8-4976-8a03-fe5a2397de8f\",\n" +
                        "\"status\": \"DRAFT\",\n" +
                        "\"offer\": null\n" +
                        "}")
        );
        fastbank.enqueue(new MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\n" +
                        "\"id\": \"5743e234-0fa8-4976-8a03-fe5a2397de8f\",\n" +
                        "\"status\": \"PROCESSED\",\n" +
                        "\"offer\": {\n" +
                        "\"monthlyPaymentAmount\": 1750.00,\n" +
                        "\"totalRepaymentAmount\": 5250.00,\n" +
                        "\"numberOfPayments\": 3,\n" +
                        "\"annualPercentageRate\": 10.20,\n" +
                        "\"firstRepaymentDate\": \"2024-04-08\"\n" +
                        "}\n" +
                        "}")
        );

        StepVerifier.create(aggregationService.submitApplication(createSubmitApplication()))
                .expectNext(List.of(createExpectedFastBankOffer(), createExpectedSolidBankOffer()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testOneOnlyResponds() {
        solidbank.enqueue(new MockResponse().setResponseCode(400));

        fastbank.enqueue(new MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\n" +
                        "\"id\": \"5743e234-0fa8-4976-8a03-fe5a2397de8f\",\n" +
                        "\"status\": \"DRAFT\",\n" +
                        "\"offer\": null\n" +
                        "}")
        );
        fastbank.enqueue(new MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\n" +
                        "\"id\": \"5743e234-0fa8-4976-8a03-fe5a2397de8f\",\n" +
                        "\"status\": \"PROCESSED\",\n" +
                        "\"offer\": {\n" +
                        "\"monthlyPaymentAmount\": 1750.00,\n" +
                        "\"totalRepaymentAmount\": 5250.00,\n" +
                        "\"numberOfPayments\": 3,\n" +
                        "\"annualPercentageRate\": 10.20,\n" +
                        "\"firstRepaymentDate\": \"2024-04-08\"\n" +
                        "}\n" +
                        "}")
        );

        StepVerifier.create(aggregationService.submitApplication(createSubmitApplication()))
                .expectNext(List.of(createExpectedFastBankOffer()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testNoneResponds() {
        solidbank.enqueue(new MockResponse().setResponseCode(400));

        fastbank.enqueue(new MockResponse().setResponseCode(400));

        StepVerifier.create(aggregationService.submitApplication(createSubmitApplication()))
                .expectNext(Collections.emptyList())
                .expectComplete()
                .verify();
    }

    private ApplicationOffer createExpectedFastBankOffer() {
        return new ApplicationOffer(
                "FAST_BANK",
                BigDecimal.valueOf(1750.00).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(5250.00).setScale(2, RoundingMode.HALF_UP),
                3,
                BigDecimal.valueOf(10.20).setScale(2, RoundingMode.HALF_UP),
                "2024-04-08"
        );
    }

    private ApplicationOffer createExpectedSolidBankOffer() {
        return new ApplicationOffer(
                "SOLID_BANK",
                BigDecimal.valueOf(883.33),
                BigDecimal.valueOf(5300.00).setScale(2, RoundingMode.HALF_UP),
                6,
                BigDecimal.valueOf(10.11),
                "2024-04-08"
        );
    }

    private SubmitApplication createSubmitApplication() {
        return new SubmitApplication(
                "+37122222222",
                "test@test.com",
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(300),
                BigDecimal.valueOf(0),
                1,
                ApplicationRequest.MaritalStatusEnum.SINGLE,
                true,
                BigDecimal.valueOf(5000)
        );
    }
}
