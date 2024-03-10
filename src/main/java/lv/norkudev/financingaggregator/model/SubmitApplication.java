package lv.norkudev.financingaggregator.model;

import lv.norkudev.financingaggregator.banks.solid.ApplicationRequest;

import java.math.BigDecimal;

public record SubmitApplication(
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
