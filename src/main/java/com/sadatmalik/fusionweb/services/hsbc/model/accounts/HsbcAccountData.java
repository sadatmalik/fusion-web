package com.sadatmalik.fusionweb.services.hsbc.model.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Object representation of the OB API Json response for Hsbc account data.
 *
 * @author sadatmalik
 */
@Getter
@Setter
@ToString
public class HsbcAccountData {
    @JsonProperty("Account")
    private List<HsbcAccount> accounts;
}
