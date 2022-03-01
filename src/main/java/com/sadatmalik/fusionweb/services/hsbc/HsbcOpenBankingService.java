package com.sadatmalik.fusionweb.services.hsbc;

import com.sadatmalik.fusionweb.services.OpenBankingService;

public interface HsbcOpenBankingService extends OpenBankingService {
    String ACCOUNT_INFO_URL = "https://sandbox.hsbc.com/psd2/obie/v3.1/accounts";
}
