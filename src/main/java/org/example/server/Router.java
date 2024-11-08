package org.example.server;

import io.javalin.Javalin;
import org.example.controllers.LoginController;

public class Router {
  /**
   * Inicio de App.
   */
  public static void init(Javalin app) {
    app.get("/", ctx -> ctx.result("Proof of Concept SSO Auth0"));

    app.get("/login/callback", LoginController::auth0Callback);

    app.get("/login", LoginController::handleLogin);

    app.get("/logout", LoginController::handleLogout);

    app.get("/profile", ctx -> ctx.result("Profile"));

  }
}

