package it.contactlab.hub.sdk.java.exceptions;

import it.contactlab.hub.sdk.java.internal.gson.ContactHubGson;
import it.contactlab.hub.sdk.java.models.ApiErrorResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ApiException extends ContactHubException {

  private final int statusCode;
  private final ApiErrorResponse apiErrorResponse;

  private static Gson gson = ContactHubGson.getInstance();

  /**
   * ApiException is thrown when the Contacthub API returns an error with
   * a JSON-serialized error message.
   */
  public ApiException(int statusCode, ApiErrorResponse apiErrorResponse) {
    super("ContactHub API returned an error: " + statusCode + " " + apiErrorResponse);

    this.statusCode = statusCode;
    this.apiErrorResponse = apiErrorResponse;
  }

  /**
   * Static helper function to deserialize an HTTP responseBody into an instance
   * of {@link ApiErrorResponse}.
   */
  public static ApiErrorResponse parseApiErrorResponse(
      int statusCode, String responseBody
  ) throws ServerException {

    if (responseBody == null || responseBody.trim().equals("")) {
      throw new ServerException(statusCode, responseBody);
    }

    try {
      return gson.fromJson(responseBody, ApiErrorResponse.class);
    } catch (JsonSyntaxException jsonException) {
      throw new ServerException(statusCode, responseBody);
    }
  }

  public int getStatusCode() {
    return statusCode;
  }

  public ApiErrorResponse getApiErrorResponse() {
    return apiErrorResponse;
  }
}
