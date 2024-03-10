package lv.norkudev.financingaggregator.banks.solid;

import lv.norkudev.financingaggregator.banks.BaseBankMapper;
import lv.norkudev.financingaggregator.model.ApplicationOffer;
import lv.norkudev.financingaggregator.model.SubmitApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SolidBankMapper extends BaseBankMapper<Application, ApplicationRequest> {

    String SOLID_BANK = "SOLID_BANK";

    ApplicationRequest toApplicationRequest(SubmitApplication submitApplication);

    @Mapping(target = "api", constant = SOLID_BANK)
    @Mapping(target = "monthlyPaymentAmount", source = "offer.monthlyPaymentAmount")
    @Mapping(target = "totalRepaymentAmount", source = "offer.totalRepaymentAmount")
    @Mapping(target = "numberOfPayments", source = "offer.numberOfPayments")
    @Mapping(target = "annualPercentageRate", source = "offer.annualPercentageRate")
    @Mapping(target = "firstRepaymentDate", source = "offer.firstRepaymentDate")
    ApplicationOffer toApplicationOffer(Application application);

}
