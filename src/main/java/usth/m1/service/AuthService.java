package usth.m1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import usth.m1.model.OAuthTokenResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class AuthService {
    @Inject
    ObjectMapper objectMapper;

    @ConfigProperty(name = "sentinel.auth.url")
    String tokenUrl;

    @ConfigProperty(name = "sentinel.client.id")
    String clientId;

    @ConfigProperty(name = "sentinel.client.secret")
    String clientSecret;

    public Uni<String> getAccessToken() {
        String form = "grant_type=client_credentials" +
                "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();

        return Uni.createFrom().item(() -> {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = null;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (response.statusCode() == 200) {
                OAuthTokenResponse token = null;
                try {
                    token = objectMapper.readValue(response.body(), OAuthTokenResponse.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                return token.getAccess_token();
            } else {
                throw new RuntimeException("Failed to get access token: " + response.body());
            }
        });
    }
}

