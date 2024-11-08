package org.example.controllers;

import com.auth0.IdentityVerificationException;
import com.auth0.Tokens;
import io.javalin.http.Context;
import jakarta.servlet.http.HttpServletRequest;
import org.example.utils.AuthenticationControllerProvider;
import org.example.jakarta2javax.JavaxRequest;
import org.example.jakarta2javax.JavaxResponse;
import org.example.utils.PrettyProperties;

public class LoginController {

  public static void handleLogin(Context ctx) {
    String redirectUrl = PrettyProperties.getInstance().propertyFromName("auth0_redirectUrl");
    String authorizeUrl = AuthenticationControllerProvider.getInstance()
        .buildAuthorizeUrl(new JavaxRequest(ctx.req()), new JavaxResponse(ctx.res()), redirectUrl)
        .withScope("openid email profile user_id")
        .build();
    ctx.redirect(authorizeUrl);
  }

  public static void auth0Callback(Context ctx) {
    try {
      // authentication complete; the tokens can be stored as needed
      Tokens tokens = AuthenticationControllerProvider
          .getInstance()
          .handle(new JavaxRequest(ctx.req()), new JavaxResponse(ctx.res()));
      System.out.println(tokens.getAccessToken());
      System.out.println(tokens.getIdToken());
      ctx.redirect("/profile");
    } catch (IdentityVerificationException e) {
      // handle authentication error
      e.printStackTrace();
      ctx.result("Error en autenticacion");
    }
  }

  public static void handleLogout(Context ctx) {
    HttpServletRequest request = ctx.req();
    if (request.getSession() != null) {
      request.getSession().invalidate();
    }

    String returnUrl = String.format("%s://%s", request.getScheme(), request.getServerName());
    if ((request.getScheme().equals("http") && request.getServerPort() != 80) || (request.getScheme().equals("https") && request.getServerPort() != 443)) {
      returnUrl += ":" + request.getServerPort();
    }
    returnUrl += "/login";

    // Build logout URL like:
    // https://{YOUR-DOMAIN}/v2/logout?client_id={YOUR-CLIENT-ID}&returnTo=http://localhost:3000/login
    String logoutUrl = String.format(
        "https://%s/v2/logout?client_id=%s&returnTo=%s",
        PrettyProperties.getInstance().propertyFromName("auth0_domain"),
        PrettyProperties.getInstance().propertyFromName("auth0_clientId"),
        returnUrl
    );
    ctx.redirect(logoutUrl);
  }

}
