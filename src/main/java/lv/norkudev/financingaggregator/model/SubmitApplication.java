package lv.norkudev.financingaggregator.model;

import lv.norkudev.financingaggregator.banks.solid.ApplicationRequest;

import java.math.BigDecimal;
import java.util.UUID;

public record SubmitApplication(
        UUID submitterUuid,
        String phone,
        String email,
        BigDecimal monthlyIncome,
        BigDecimal monthlyExpenses,
        BigDecimal monthlyCreditLiabilities,
        Integer dependents,
        ApplicationRequest.MaritalStatusEnum maritalStatus,
        boolean agreeToBeScored,
        BigDecimal amount
        ) {

}
