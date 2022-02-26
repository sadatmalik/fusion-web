package com.sadatmalik.fusionweb.oauth.hsbc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public final class HsbcClientAccessToken {
    // {"access_token":"68678f2d-e0f0-460e-8bee-c810d1a267dd",
    @JsonProperty("access_token")
    String accessToken;

    // "expires_in":3599,
    @JsonProperty("expires_in")
    int secondsToExpire;

    // "token_type":"Bearer",
    @JsonProperty("token_type")
    String tokenType;

    // "scope":"accounts"}
    String scope;

}
