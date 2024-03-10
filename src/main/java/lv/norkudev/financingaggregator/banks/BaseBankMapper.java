package lv.norkudev.financingaggregator.banks;

import lv.norkudev.financingaggregator.model.ApplicationOffer;
import lv.norkudev.financingaggregator.model.SubmitApplication;

public interface BaseBankMapper<T, U> {

    U toApplicationRequest(SubmitApplication submitApplication);

    ApplicationOffer toApplicationOffer(T application);

}
