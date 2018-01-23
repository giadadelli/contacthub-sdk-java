package it.contactlab.hub.sdk.java.internal.api;

import it.contactlab.hub.sdk.java.Auth;
import it.contactlab.hub.sdk.java.ClientData;
import it.contactlab.hub.sdk.java.exceptions.ApiException;
import it.contactlab.hub.sdk.java.exceptions.HttpException;
import it.contactlab.hub.sdk.java.exceptions.ServerException;
import it.contactlab.hub.sdk.java.internal.gson.ContactHubGson;
import it.contactlab.hub.sdk.java.internal.http.Request;
import it.contactlab.hub.sdk.java.models.Education;

import com.google.gson.Gson;

public class EducationApi {

  private static Gson gson = ContactHubGson.getInstance();

  /**
   * Add a new Education to a Customer.
 * @param clientData 
   */
  public static Education add(Auth auth, ClientData clientData, String customerId, Education education)
      throws ApiException, ServerException, HttpException {

    String endpoint = "/customers/" + customerId + "/educations";
    String payload = gson.toJson(education);
    String response = Request.doPost(auth, clientData, endpoint, payload);

    return gson.fromJson(response, Education.class);
  }

  /**
   * Update an existing Education.
 * @param clientData 
   */
  public static Education update(Auth auth, ClientData clientData, String customerId, Education education)
      throws ApiException, ServerException, HttpException {

    String endpoint = "/customers/" + customerId + "/educations/" + education.id();
    String payload = gson.toJson(education);
    String response = Request.doPut(auth, clientData, endpoint, payload);

    return gson.fromJson(response, Education.class);
  }

  /**
   * Remove a tag from a Customer.
 * @param clientData 
   */
  public static void remove(Auth auth, ClientData clientData, String customerId, String educationId)
      throws ApiException, ServerException, HttpException {

    String endpoint = "/customers/" + customerId + "/educations/" + educationId;
    String response = Request.doDelete(auth, clientData, endpoint);
  }

}
