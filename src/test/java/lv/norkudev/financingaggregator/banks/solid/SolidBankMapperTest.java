package lv.norkudev.financingaggregator.banks.solid;

import lv.norkudev.financingaggregator.model.SubmitApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SolidBankMapperImpl.class)
public class SolidBankMapperTest {

    @Autowired
    private SolidBankMapper solidBankMapper;

    @Test
    public void testApplicationOfferMapping() {
        var applicationOffer = solidBankMapper.toApplicationOffer(createApplication());
        assertThat(applicationOffer).isNotNull();
        assertThat(applicationOffer.api()).isEqualTo("FAST_BANK");
        assertThat(applicationOffer.annualPercentageRate()).isEqualTo(BigDecimal.valueOf(10.26));
        assertThat(applicationOffer.numberOfPayments()).isEqualTo(12);
        assertThat(applicationOffer.firstRepaymentDate()).isEqualTo("2024-04-08");
        assertThat(applicationOffer.totalRepaymentAmount()).isEqualTo(BigDecimal.valueOf(6300.00));
        assertThat(applicationOffer.monthlyPaymentAmount()).isEqualTo(BigDecimal.valueOf(525.00));
    }

    @Test
    public void testApplicationRequestMapping() {
        var applicationRequest = solidBankMapper.toApplicationRequest(createSubmitApplication());
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
        offer.setAnnualPercentageRate(BigDecimal.valueOf(10.26));
        offer.setNumberOfPayments(12);
        offer.setFirstRepaymentDate("2024-04-08");
        offer.setTotalRepaymentAmount(BigDecimal.valueOf(6300.00));
        offer.setMonthlyPaymentAmount(BigDecimal.valueOf(525.00));
        offer.setMonthlyPaymentAmount(BigDecimal.valueOf(525.00));
        return offer;
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
