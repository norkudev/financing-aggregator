package lv.norkudev.financingaggregator.banks.fast;

import lv.norkudev.financingaggregator.model.ApplicationOffer;
import lv.norkudev.financingaggregator.model.SubmitApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FastBankMapper {

    String FAST_BANK = "FAST_BANK";

    @Mapping(target = "phoneNumber", source = "phone")
    @Mapping(target = "monthlyIncomeAmount", source = "monthlyIncome")
    @Mapping(target = "agreeToDataSharing", source = "agreeToBeScored")
    ApplicationRequest toApplicationRequest(SubmitApplication submitApplication);

    @Mapping(target = "api", constant = FAST_BANK)
    @Mapping(target = "monthlyPaymentAmount", source = "offer.monthlyPaymentAmount")
    @Mapping(target = "totalRepaymentAmount", source = "offer.totalRepaymentAmount")
    @Mapping(target = "numberOfPayments", source = "offer.numberOfPayments")
    @Mapping(target = "annualPercentageRate", source = "offer.annualPercentageRate")
    @Mapping(target = "firstRepaymentDate", source = "offer.firstRepaymentDate")
    ApplicationOffer toApplicationOffer(Application application);

}
