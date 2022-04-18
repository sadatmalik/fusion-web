package com.sadatmalik.fusionweb.oauth.hsbc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Instances of this type will be generated via the Rest Api calls to the Open
 * Banking Api and subsequent Json response containing the required property
 * values.
 *
 * The type represents a user access token - which is the access token at the
 * individual authentication level. Its creation marks the end statge of the OAuth
 * flow sequence, and it needs be sent with every subsequent Rest Api call on behalf
 * of the authentication.
 *
 * @author sadatmalik
 */

@Slf4j
@Getter
@Setter
@ToString
@NoArgsConstructor
public final class HsbcUserAccessToken {
    private long createdAt = System.currentTimeMillis(); // @todo have a look at Instant class

    private static HsbcUserAccessToken currentToken;

    // {"access_token":"d13a4f6c-ce35-4b3b-aadd-0ee14563a04e",
    @JsonProperty("access_token")
    private String accessToken;

    // "expires_in":3599,
    @JsonProperty("expires_in")
    private int secondsToExpire;

    // "token_type":"Bearer",
    @JsonProperty("token_type")
    private String tokenType;

    // "refresh_token":"701861fa-3182-440c-9af0-d9ff285b51f6"}
    @JsonProperty("refresh_token")
    private String refreshToken;

    private long getRefreshThresholdInSeconds() {
        // refresh at 1 minute less than the expiry time
        return secondsToExpire - 60;
    }

    public boolean isExpiring() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - createdAt;
        long threshold = getRefreshThresholdInSeconds() * 1000;

        log.debug("Token elapsed=" + elapsedTime + ", refresh threshold=" + threshold);

        if ( elapsedTime > threshold ) {
            log.debug("Time to refresh token");
            return true;
        }
        return false;
    }

    public static void setCurrentToken(HsbcUserAccessToken token) {
        currentToken = token;
    }

    public static HsbcUserAccessToken current() {
        return currentToken;
    }
}
