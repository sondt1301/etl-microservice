package usth.m1.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import usth.m1.proxy.PingProxy;

@ApplicationScoped
public class ConnectionTestService {
    @Inject
    @RestClient
    PingProxy pingProxy;

    @Inject
    AuthService authService;

    public Uni<JsonObject> testConnection() {
        return authService.getAccessToken()
                .onItem().transform(token -> "Bearer " + token)
                .flatMap(bearerToken -> pingProxy.ping(bearerToken));
    }
}
