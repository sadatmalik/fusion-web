package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.AccountData;
import com.sadatmalik.fusionweb.model.AccountList;
import com.sadatmalik.fusionweb.oauth.HsbcUserAccessToken;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HsbcApiService {

    private static final Logger logger = LoggerFactory.getLogger(HsbcApiService.class);

    private static final String ACCOUNT_INFO_URL = "https://sandbox.hsbc.com/psd2/obie/v3.1/accounts";

    @Autowired
    private RestTemplate restTemplate;

    // curl -v -X GET --cert qwac.der --cert-type DER --key server.key
    // -H "x-fapi-financial-id: test"
    // -H "Authorization: Bearer 4501825f-096a-4959-bde7-dff93a5fe6ba"
    // -H "Cache-Control: no-cache"
    // "https://sandbox.hsbc.com/psd2/obie/v3.1/accounts"
    public List<Account> getUserAccounts(HsbcUserAccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-fapi-financial-id", "test");
        headers.setBearerAuth(accessToken.getAccessToken());
        headers.setCacheControl(CacheControl.noCache());

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<AccountList> response =
                restTemplate.exchange(ACCOUNT_INFO_URL, HttpMethod.GET, request, AccountList.class);

        logger.debug("User Accounts ---------" + response.getBody());

        return response.getBody().getData().getAccounts();
    }
}
