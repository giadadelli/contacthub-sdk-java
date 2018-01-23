package it.contactlab.hub.sdk.java.internal.api;

import it.contactlab.hub.sdk.java.Auth;
import it.contactlab.hub.sdk.java.ClientData;
import it.contactlab.hub.sdk.java.exceptions.ApiException;
import it.contactlab.hub.sdk.java.exceptions.HttpException;
import it.contactlab.hub.sdk.java.exceptions.ServerException;
import it.contactlab.hub.sdk.java.internal.gson.ContactHubGson;
import it.contactlab.hub.sdk.java.internal.http.Request;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.UUID;

public class SessionApi {

  private static Gson gson = ContactHubGson.getInstance();

  private static String baseUrl = "https://api.contactlab.it/hub/v1";

  /**
   * Generate a new SessionId.
   */
  public static String generate() {
    return UUID.randomUUID().toString();
  }

  /**
   * Reconcile a SessionId with a Customer.
 * @param clientData 
   */
  public static void reconcile(Auth auth, ClientData clientData, String customerId, String sessionId)
      throws ApiException, ServerException, HttpException {
    String endpoint = "/customers/" + customerId + "/sessions";
    JsonObject session = new JsonObject();
    session.addProperty("value", sessionId);

    String payload = gson.toJson(session);
    Request.doPost(auth, clientData, endpoint, payload);
  }

}
