package com.sadatmalik.fusionweb.services.hsbc;

import com.sadatmalik.fusionweb.services.OpenBankingService;

/**
 * An interface intended to encapsulate the required methods that any service
 * interacting with the Hsbc OB Api must expose to clients.
 *
 * The interface also serves as the central repository for all Api related Urls,
 * which it maintains as String constants.
 *
 * The interface extends the OpenBankingService interface.
 *
 * @see OpenBankingService
 * @author sadatmalik
 */
public interface HsbcOpenBankingService extends OpenBankingService {
    String ACCOUNT_INFO_URL = "https://sandbox.hsbc.com/psd2/obie/v3.1/accounts";
}
