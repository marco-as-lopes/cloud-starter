package org.camunda.msl.dto;

public class TokenRequest {

    private String grant_type;
    private String audience;
    private String client_id;
    private String client_secret;

    public TokenRequest(String grant_type, String audience, String client_id,String client_secret)
    {
        this.audience = audience;
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.grant_type = grant_type;
    }
}
