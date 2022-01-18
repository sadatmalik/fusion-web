package com.sadatmalik.fusionweb.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HsbcUserAccessToken {

    private static final Logger logger = LoggerFactory.getLogger(HsbcUserAccessToken.class);
    private long createdAt = System.currentTimeMillis();

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

        logger.debug("Token elapsed=" + elapsedTime + ", refresh threshold=" + threshold);

        if ( elapsedTime > threshold ) {
            logger.debug("Time to refresh token");
            return true;
        }
        return false;
    }
}
