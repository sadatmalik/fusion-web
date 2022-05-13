package com.sadatmalik.fusionweb.messaging.events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The UserTokenChangeEvent class declares three data elements:
 *
 * 1. action — the action that triggered the event. Gives the message consumer more context on
 * how it should process an event.
 *
 * 2. accessToken — this is the access token associated with the event.
 *
 * 3. traceId — this is the trace ID of the service call that triggered the event. Helps with
 * tracking and debugging the flow of messages through our services.
 *
 * @author sadatmalik
 */
@Getter
@Setter
@ToString
public class UserTokenChangeEvent {
    private String type;
    private String action;
    private String accessToken;
    private String traceId;

    public UserTokenChangeEvent(String type,
                                String action, String accessToken,
                                String traceId) {
        this.type = type;
        this.action = action;
        this.accessToken = accessToken;
        this.traceId = traceId;
    }

}
