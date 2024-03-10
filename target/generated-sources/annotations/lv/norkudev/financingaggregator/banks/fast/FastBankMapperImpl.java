package lv.norkudev.financingaggregator.banks.fast;

import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import lv.norkudev.financingaggregator.model.ApplicationOffer;
import lv.norkudev.financingaggregator.model.SubmitApplication;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-10T18:49:34+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Azul Systems, Inc.)"
)
@Component
public class FastBankMapperImpl implements FastBankMapper {

    @Override
    public ApplicationRequest toApplicationRequest(SubmitApplication submitApplication) {
        if ( submitApplication == null ) {
            return null;
        }

        ApplicationRequest applicationRequest = new ApplicationRequest();

        applicationRequest.setPhoneNumber( submitApplication.phone() );
        applicationRequest.setMonthlyIncomeAmount( submitApplication.monthlyIncome() );
        applicationRequest.setAgreeToDataSharing( submitApplication.agreeToBeScored() );
        applicationRequest.setEmail( submitApplication.email() );
        applicationRequest.setMonthlyCreditLiabilities( submitApplication.monthlyCreditLiabilities() );
        applicationRequest.setDependents( submitApplication.dependents() );
        applicationRequest.setAmount( submitApplication.amount() );

        return applicationRequest;
    }

    @Override
    public ApplicationOffer toApplicationOffer(Application application) {
        if ( application == null ) {
            return null;
        }

        BigDecimal monthlyPaymentAmount = null;
        BigDecimal totalRepaymentAmount = null;
        Integer numberOfPayments = null;
        BigDecimal annualPercentageRate = null;
        String firstRepaymentDate = null;

        monthlyPaymentAmount = applicationOfferMonthlyPaymentAmount( application );
        totalRepaymentAmount = applicationOfferTotalRepaymentAmount( application );
        numberOfPayments = applicationOfferNumberOfPayments( application );
        annualPercentageRate = applicationOfferAnnualPercentageRate( application );
        firstRepaymentDate = applicationOfferFirstRepaymentDate( application );

        String api = "FAST_BANK";

        ApplicationOffer applicationOffer = new ApplicationOffer( api, monthlyPaymentAmount, totalRepaymentAmount, numberOfPayments, annualPercentageRate, firstRepaymentDate );

        return applicationOffer;
    }

    private BigDecimal applicationOfferMonthlyPaymentAmount(Application application) {
        if ( application == null ) {
            return null;
        }
        Offer offer = application.getOffer();
        if ( offer == null ) {
            return null;
        }
        BigDecimal monthlyPaymentAmount = offer.getMonthlyPaymentAmount();
        if ( monthlyPaymentAmount == null ) {
            return null;
        }
        return monthlyPaymentAmount;
    }

    private BigDecimal applicationOfferTotalRepaymentAmount(Application application) {
        if ( application == null ) {
            return null;
        }
        Offer offer = application.getOffer();
        if ( offer == null ) {
            return null;
        }
        BigDecimal totalRepaymentAmount = offer.getTotalRepaymentAmount();
        if ( totalRepaymentAmount == null ) {
            return null;
        }
        return totalRepaymentAmount;
    }

    private Integer applicationOfferNumberOfPayments(Application application) {
        if ( application == null ) {
            return null;
        }
        Offer offer = application.getOffer();
        if ( offer == null ) {
            return null;
        }
        Integer numberOfPayments = offer.getNumberOfPayments();
        if ( numberOfPayments == null ) {
            return null;
        }
        return numberOfPayments;
    }

    private BigDecimal applicationOfferAnnualPercentageRate(Application application) {
        if ( application == null ) {
            return null;
        }
        Offer offer = application.getOffer();
        if ( offer == null ) {
            return null;
        }
        BigDecimal annualPercentageRate = offer.getAnnualPercentageRate();
        if ( annualPercentageRate == null ) {
            return null;
        }
        return annualPercentageRate;
    }

    private String applicationOfferFirstRepaymentDate(Application application) {
        if ( application == null ) {
            return null;
        }
        Offer offer = application.getOffer();
        if ( offer == null ) {
            return null;
        }
        String firstRepaymentDate = offer.getFirstRepaymentDate();
        if ( firstRepaymentDate == null ) {
            return null;
        }
        return firstRepaymentDate;
    }
}
