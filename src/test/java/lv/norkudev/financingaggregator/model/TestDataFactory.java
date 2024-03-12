package lv.norkudev.financingaggregator.model;

import lv.norkudev.financingaggregator.banks.solid.ApplicationRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Component
public class TestDataFactory {

    public SubmitApplication createSubmitApplication() {
        return new SubmitApplication(
                UUID.randomUUID(),
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

    public ApplicationOffer createExpectedFastBankOffer() {
        return new ApplicationOffer(
                "FAST_BANK",
                BigDecimal.valueOf(1750.00).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(5250.00).setScale(2, RoundingMode.HALF_UP),
                3,
                BigDecimal.valueOf(10.20).setScale(2, RoundingMode.HALF_UP),
                "2024-04-08"
        );
    }

    public ApplicationOffer createExpectedSolidBankOffer() {
        return new ApplicationOffer(
                "SOLID_BANK",
                BigDecimal.valueOf(883.33),
                BigDecimal.valueOf(5300.00).setScale(2, RoundingMode.HALF_UP),
                6,
                BigDecimal.valueOf(10.11),
                "2024-04-08"
        );
    }

}
