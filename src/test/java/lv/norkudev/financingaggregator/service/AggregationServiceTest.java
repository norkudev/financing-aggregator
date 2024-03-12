package lv.norkudev.financingaggregator.service;

import lv.norkudev.financingaggregator.banks.fast.FastBankService;
import lv.norkudev.financingaggregator.banks.solid.SolidBankService;
import lv.norkudev.financingaggregator.model.TestDataFactory;
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
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;

import java.io.IOException;
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
    @Autowired
    private TestDataFactory testDataFactory;

    static {
        GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7.2.4-alpine")).withExposedPorts(6379);
        redis.start();
        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.port", redis.getMappedPort(6379).toString());
    }


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
                .setBody("""
                        {
                        "id": "1d00bf22-24a1-4ee1-907d-230b35397ba9",
                        "status": "DRAFT",
                        "offer": null
                        }""")
        );
        solidbank.enqueue(new MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        {
                        "id": "5fb16219-1d4f-400b-b316-058fd4bb79ba",
                        "status": "PROCESSED",
                        "offer": {
                        "monthlyPaymentAmount": 883.33,
                        "totalRepaymentAmount": 5300.00,
                        "numberOfPayments": 6,
                        "annualPercentageRate": 10.11,
                        "firstRepaymentDate": "2024-04-08"
                        }
                        }""")
        );

        fastbank.enqueue(new MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        {
                        "id": "5743e234-0fa8-4976-8a03-fe5a2397de8f",
                        "status": "DRAFT",
                        "offer": null
                        }""")
        );
        fastbank.enqueue(new MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        {
                        "id": "5743e234-0fa8-4976-8a03-fe5a2397de8f",
                        "status": "PROCESSED",
                        "offer": {
                        "monthlyPaymentAmount": 1750.00,
                        "totalRepaymentAmount": 5250.00,
                        "numberOfPayments": 3,
                        "annualPercentageRate": 10.20,
                        "firstRepaymentDate": "2024-04-08"
                        }
                        }""")
        );

        var submitApplication = testDataFactory.createSubmitApplication();

        StepVerifier.create(aggregationService.submitApplication(submitApplication))
                .expectNext(List.of(testDataFactory.createExpectedFastBankOffer(), testDataFactory.createExpectedSolidBankOffer()))
                .expectComplete()
                .verify();

        StepVerifier.create(aggregationService.submitApplication(submitApplication))
                .expectNext(List.of(testDataFactory.createExpectedFastBankOffer(), testDataFactory.createExpectedSolidBankOffer()))
                .expectComplete()
                .verify();

        StepVerifier.create(aggregationService.getApplicationOffers(submitApplication.submitterUuid()))
                .expectNext(List.of(testDataFactory.createExpectedFastBankOffer(), testDataFactory.createExpectedSolidBankOffer()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testOneOnlyResponds() {
        solidbank.enqueue(new MockResponse().setResponseCode(400));

        fastbank.enqueue(new MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        {
                        "id": "5743e234-0fa8-4976-8a03-fe5a2397de8f",
                        "status": "DRAFT",
                        "offer": null
                        }""")
        );
        fastbank.enqueue(new MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        {
                        "id": "5743e234-0fa8-4976-8a03-fe5a2397de8f",
                        "status": "PROCESSED",
                        "offer": {
                        "monthlyPaymentAmount": 1750.00,
                        "totalRepaymentAmount": 5250.00,
                        "numberOfPayments": 3,
                        "annualPercentageRate": 10.20,
                        "firstRepaymentDate": "2024-04-08"
                        }
                        }""")
        );

        var submitApplication = testDataFactory.createSubmitApplication();

        StepVerifier.create(aggregationService.submitApplication(submitApplication))
                .expectNext(List.of(testDataFactory.createExpectedFastBankOffer()))
                .expectComplete()
                .verify();

        StepVerifier.create(aggregationService.submitApplication(submitApplication))
                .expectNext(List.of(testDataFactory.createExpectedFastBankOffer()))
                .expectComplete()
                .verify();

        StepVerifier.create(aggregationService.getApplicationOffers(submitApplication.submitterUuid()))
                .expectNext(List.of(testDataFactory.createExpectedFastBankOffer()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testNoneResponds() {
        solidbank.enqueue(new MockResponse().setResponseCode(400));
        fastbank.enqueue(new MockResponse().setResponseCode(400));

        StepVerifier.create(aggregationService.submitApplication(testDataFactory.createSubmitApplication()))
                .expectNext(Collections.emptyList())
                .expectComplete()
                .verify();
    }
}
