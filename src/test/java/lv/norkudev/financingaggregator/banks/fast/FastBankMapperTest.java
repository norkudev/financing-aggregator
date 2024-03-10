package lv.norkudev.financingaggregator.banks.fast;

import lv.norkudev.financingaggregator.banks.solid.ApplicationRequest;
import lv.norkudev.financingaggregator.model.SubmitApplication;
import lv.norkudev.financingaggregator.model.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {FastBankMapperImpl.class, TestDataFactory.class})
public class FastBankMapperTest {

    @Autowired
    private FastBankMapper fastBankMapper;

    @Autowired
    private TestDataFactory testDataFactory;

    @Test
    public void testApplicationOfferMapping() {
        var applicationOffer = fastBankMapper.toApplicationOffer(createApplication());
        assertThat(applicationOffer).isNotNull();
        assertThat(applicationOffer).usingRecursiveComparison()
                .isEqualTo(testDataFactory.createExpectedFastBankOffer());
    }

    @Test
    public void testApplicationRequestMapping() {
        var applicationRequest = fastBankMapper.toApplicationRequest(testDataFactory.createSubmitApplication());
        assertThat(applicationRequest).isNotNull();
        assertThat(applicationRequest.getPhoneNumber()).isEqualTo("+37122222222");
        assertThat(applicationRequest.getEmail()).isEqualTo("test@test.com");
        assertThat(applicationRequest.getMonthlyIncomeAmount()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(applicationRequest.getMonthlyCreditLiabilities()).isEqualTo(BigDecimal.valueOf(0));
        assertThat(applicationRequest.getDependents()).isEqualTo(1);
        assertThat(applicationRequest.isAgreeToDataSharing()).isTrue();
        assertThat(applicationRequest.getAmount()).isEqualTo(BigDecimal.valueOf(5000));
    }

    private Application createApplication()  {
        var application = new Application();
        application.setId(UUID.randomUUID().toString());
        application.setStatus(Application.StatusEnum.PROCESSED);
        application.setOffer(createOffer());
        return application;
    }

    private Offer createOffer() {
        var offer = new Offer();
        offer.setAnnualPercentageRate(BigDecimal.valueOf(10.20).setScale(2, RoundingMode.HALF_UP));
        offer.setNumberOfPayments(3);
        offer.setFirstRepaymentDate("2024-04-08");
        offer.setTotalRepaymentAmount(BigDecimal.valueOf(5250.00).setScale(2, RoundingMode.HALF_UP));
        offer.setMonthlyPaymentAmount(BigDecimal.valueOf(1750.00).setScale(2, RoundingMode.HALF_UP));
        return offer;
    }

}
