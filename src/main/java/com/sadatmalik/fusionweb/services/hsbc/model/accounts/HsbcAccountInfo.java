package com.sadatmalik.fusionweb.services.hsbc.model.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Object representation used in the mapping of the OB API Json response for
 * Hsbc account data.
 *
 * @author sadatmalik
 */
@Getter
@Setter
@ToString
public class HsbcAccountInfo {
    @JsonProperty("SchemeName")
    private String scheme; // @todo make this an enum

    @JsonProperty("Identification")
    private String identifier;

    @JsonProperty("Name")
    private String name;
}
