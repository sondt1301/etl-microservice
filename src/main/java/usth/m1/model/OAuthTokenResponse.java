package usth.m1.model;

public record OAuthTokenResponse (
    String access_token,
    String token_type,
    int expires_in
) {}

