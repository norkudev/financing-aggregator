package lv.norkudev.financingaggregator.model;

import lv.norkudev.financingaggregator.banks.solid.ApplicationRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TestDataFactory {

    public SubmitApplication createSubmitApplication() {
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
