package usth.m1.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import usth.m1.model.OAuthTokenResponse;
import usth.m1.proxy.AuthProxy;

@ApplicationScoped
public class AuthService {
    @Inject
    @RestClient
    AuthProxy authProxy;

    @ConfigProperty(name = "sentinel.client.id")
    String clientId;

    @ConfigProperty(name = "sentinel.client.secret")
    String clientSecret;

    public Uni<String> getAccessToken() {

        return authProxy.getToken("client_credentials", clientId, clientSecret)
                .onItem().transform(OAuthTokenResponse::access_token);

    }
}

