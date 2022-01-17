package com.sadatmalik.fusionweb.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HsbcUserAccessToken {
    // {"access_token":"d13a4f6c-ce35-4b3b-aadd-0ee14563a04e",
    @JsonProperty("access_token")
    String accessToken;

    // "expires_in":3599,
    @JsonProperty("expires_in")
    int secondsToExpire;

    // "token_type":"Bearer",
    @JsonProperty("token_type")
    String tokenType;

    // "refresh_token":"701861fa-3182-440c-9af0-d9ff285b51f6"}
    @JsonProperty("refresh_token")
    String refreshToken;
}
