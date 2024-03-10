package lv.norkudev.financingaggregator.model;

import java.math.BigDecimal;

public record ApplicationOffer(
        String api,
        BigDecimal monthlyPaymentAmount,
        BigDecimal totalRepaymentAmount,
        Integer numberOfPayments,
        BigDecimal annualPercentageRate,
        String firstRepaymentDate) {
}
