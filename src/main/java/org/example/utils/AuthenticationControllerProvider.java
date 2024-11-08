package org.example.utils;

import com.auth0.AuthenticationController;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import lombok.Getter;

@Getter
public class AuthenticationControllerProvider {

  private static AuthenticationController authenticationController = null;

  public static AuthenticationController getInstance() {
    if (authenticationController == null) {
      String domain = PrettyProperties.getInstance().propertyFromName("auth0_domain");
      String clientId = PrettyProperties.getInstance().propertyFromName("auth0_clientId");
      String clientSecret = PrettyProperties.getInstance().propertyFromName("auth0_clientSecret");
      JwkProvider jwkProvider = new JwkProviderBuilder(domain).build();
      authenticationController = AuthenticationController.newBuilder(domain, clientId, clientSecret)
          .withJwkProvider(jwkProvider)
          .build();
    }
    return authenticationController;
  }
}
