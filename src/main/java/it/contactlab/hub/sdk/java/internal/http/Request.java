package it.contactlab.hub.sdk.java.internal.http;

import it.contactlab.hub.sdk.java.Auth;
import it.contactlab.hub.sdk.java.ClientData;
import it.contactlab.hub.sdk.java.exceptions.ApiException;
import it.contactlab.hub.sdk.java.exceptions.HttpException;
import it.contactlab.hub.sdk.java.exceptions.ServerException;
import it.contactlab.hub.sdk.java.models.ApiErrorResponse;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Request {

  /**
   * Sends a generic GET request and returns the response String.
   */
  public static String doGet(
      Auth auth, ClientData clientData, String endpoint, Map<String, Object> queryString
  ) throws ApiException, ServerException, HttpException {
    String url = auth.apiUrl + "/workspaces/" + auth.workspaceId + endpoint;

    try {
      HttpResponse<String> response = Unirest
          .get(url)
          .headers(headersNoContent(auth, clientData))
          .queryString(queryString)
          .asString();

      if (response.getStatus() >= 400) {
        ApiErrorResponse error = ApiException.parseApiErrorResponse(
            response.getStatus(), response.getBody());
        throw new ApiException(response.getStatus(), error);
      }

      return response.getBody().toString();
    } catch (UnirestException ex) {
      throw new HttpException(ex);
    }
  }

  public static String doGet(Auth auth, ClientData clientData, String endpoint)
      throws ApiException, ServerException, HttpException {
    return doGet(auth, clientData, endpoint, Collections.emptyMap());
  }

  /**
   * Sends a generic POST request and returns the response String.
   */
  public static String doPost(Auth auth, ClientData clientData, String endpoint, String payload)
      throws ApiException, ServerException, HttpException {
    try {
      String url = auth.apiUrl + "/workspaces/" + auth.workspaceId + endpoint;

      HttpResponse<String> response = Unirest
          .post(url)
          .headers(headersWithContent(auth, clientData))
          .body(payload)
          .asString();

      if (response.getStatus() >= 400) {
        ApiErrorResponse error = ApiException.parseApiErrorResponse(
            response.getStatus(), response.getBody());
        throw new ApiException(response.getStatus(), error);
      }

      return response.getBody().toString();
    } catch (UnirestException ex) {
      throw new HttpException(ex);
    }
  }

  /**
   * Sends a generic DELETE request and returns the response String.
   */
  public static String doDelete(Auth auth, ClientData clientData, String endpoint)
      throws ApiException, ServerException, HttpException {
    try {
      String url = auth.apiUrl + "/workspaces/" + auth.workspaceId + endpoint;

      HttpResponse<String> response = Unirest
          .delete(url)
          .headers(headersNoContent(auth, clientData))
          .asString();

      if (response.getStatus() >= 400) {
        ApiErrorResponse error = ApiException.parseApiErrorResponse(
            response.getStatus(), response.getBody());
        throw new ApiException(response.getStatus(), error);
      }

      return response.getBody().toString();
    } catch (UnirestException ex) {
      throw new HttpException(ex);
    }
  }

  /**
   * Sends a generic PUT request and returns the response String.
   */
  public static String doPut(Auth auth, ClientData clientData, String endpoint, String payload)
      throws ApiException, ServerException, HttpException {
    try {
      String url = auth.apiUrl + "/workspaces/" + auth.workspaceId + endpoint;

      HttpResponse<String> response = Unirest
          .put(url)
          .headers(headersWithContent(auth, clientData))
          .body(payload)
          .asString();

      if (response.getStatus() >= 400) {
        ApiErrorResponse error = ApiException.parseApiErrorResponse(
            response.getStatus(), response.getBody());
        throw new ApiException(response.getStatus(), error);
      }

      return response.getBody().toString();
    } catch (UnirestException ex) {
      throw new HttpException(ex);
    }
  }

  /**
   * Sends a generic PATCH request and returns the response String.
   */
  public static String doPatch(Auth auth, ClientData clientData, String endpoint, String payload)
      throws ApiException, ServerException, HttpException {
    try {
      String url = auth.apiUrl + "/workspaces/" + auth.workspaceId + endpoint;

      HttpResponse<String> response = Unirest
          .patch(url)
          .headers(headersWithContent(auth, clientData))
          .body(payload)
          .asString();

      if (response.getStatus() >= 400) {
        ApiErrorResponse error = ApiException.parseApiErrorResponse(
            response.getStatus(), response.getBody());
        throw new ApiException(response.getStatus(), error);
      }

      return response.getBody().toString();
    } catch (UnirestException ex) {
      throw new HttpException(ex);
    }
  }

  private static Map<String, String> headersNoContent(Auth auth, ClientData clientData) {
    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + auth.token);
    if (clientData != null) {
      headers.put("Contactlab-Tracing-ID", clientData.correlationId);
    }
    return headers;
  }
    
  private static Map<String, String> headersWithContent(Auth auth, ClientData clientData) {
    Map<String, String> headers = headersNoContent(auth, clientData);
    headers.put("Content-Type", "application/json");
    return headers;
  }
  
}
