package lv.norkudev.financingaggregator.banks.solid;

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
@ContextConfiguration(classes = {SolidBankMapperImpl.class, TestDataFactory.class})
public class SolidBankMapperTest {

    @Autowired
    private SolidBankMapper solidBankMapper;

    @Autowired
    private TestDataFactory testDataFactory;

    @Test
    public void testApplicationOfferMapping() {
        var applicationOffer = solidBankMapper.toApplicationOffer(createApplication());
        assertThat(applicationOffer).isNotNull();
        assertThat(applicationOffer).usingRecursiveComparison()
                .isEqualTo(testDataFactory.createExpectedSolidBankOffer());
    }

    @Test
    public void testApplicationRequestMapping() {
        var applicationRequest = solidBankMapper.toApplicationRequest(testDataFactory.createSubmitApplication());
        assertThat(applicationRequest).isNotNull();
        assertThat(applicationRequest.getPhone()).isEqualTo("+37122222222");
        assertThat(applicationRequest.getEmail()).isEqualTo("test@test.com");
        assertThat(applicationRequest.getMonthlyIncome()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(applicationRequest.getMonthlyExpenses()).isEqualTo(BigDecimal.valueOf(300));
        assertThat(applicationRequest.getMaritalStatus()).isEqualTo(ApplicationRequest.MaritalStatusEnum.SINGLE);
        assertThat(applicationRequest.isAgreeToBeScored()).isTrue();
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
        offer.setAnnualPercentageRate(BigDecimal.valueOf(10.11).setScale(2, RoundingMode.HALF_UP));
        offer.setNumberOfPayments(6);
        offer.setFirstRepaymentDate("2024-04-08");
        offer.setTotalRepaymentAmount(BigDecimal.valueOf(5300.00).setScale(2, RoundingMode.HALF_UP));
        offer.setMonthlyPaymentAmount(BigDecimal.valueOf(883.33).setScale(2, RoundingMode.HALF_UP));
        return offer;
    }

}
