package usth.m1.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import usth.m1.model.ULakeLoginRequest;
import usth.m1.model.ULakeLoginResponse;
import usth.m1.proxy.ULakeAuthProxy;

@ApplicationScoped
public class ULakeAuthService {

    @Inject
    @RestClient
    ULakeAuthProxy uLakeAuthProxy;

    @ConfigProperty(name = "ulake.admin.username")
    String adminUsername;

    @ConfigProperty(name = "ulake.admin.password")
    String adminPassword;


    public Uni<String> getAccessToken() {
        ULakeLoginRequest request = new ULakeLoginRequest(adminUsername, adminPassword);
        return uLakeAuthProxy.uLakeLogin(request).onItem().transform(ULakeLoginResponse::resp);
    }
}
