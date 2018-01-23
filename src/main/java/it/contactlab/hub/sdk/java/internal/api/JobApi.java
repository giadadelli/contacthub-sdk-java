package it.contactlab.hub.sdk.java.internal.api;

import it.contactlab.hub.sdk.java.Auth;
import it.contactlab.hub.sdk.java.ClientData;
import it.contactlab.hub.sdk.java.exceptions.ApiException;
import it.contactlab.hub.sdk.java.exceptions.HttpException;
import it.contactlab.hub.sdk.java.exceptions.ServerException;
import it.contactlab.hub.sdk.java.internal.gson.ContactHubGson;
import it.contactlab.hub.sdk.java.internal.http.Request;
import it.contactlab.hub.sdk.java.models.Job;

import com.google.gson.Gson;

public class JobApi {

  private static Gson gson = ContactHubGson.getInstance();

  /**
   * Add a new Job to a Customer.
 * @param clientData 
   */
  public static Job add(Auth auth, ClientData clientData, String customerId, Job job)
      throws ApiException, ServerException, HttpException {

    String endpoint = "/customers/" + customerId + "/jobs";
    String payload = gson.toJson(job);
    String response = Request.doPost(auth, clientData, endpoint, payload);

    return gson.fromJson(response, Job.class);
  }

  /**
   * Update an existing Job.
 * @param clientData 
   */
  public static Job update(Auth auth, ClientData clientData, String customerId, Job job)
      throws ApiException, ServerException, HttpException {

    String endpoint = "/customers/" + customerId + "/jobs/" + job.id();
    String payload = gson.toJson(job);
    String response = Request.doPut(auth, clientData, endpoint, payload);

    return gson.fromJson(response, Job.class);
  }

  /**
   * Remove a tag from a Customer.
 * @param clientData 
   */
  public static void remove(Auth auth, ClientData clientData, String customerId, String jobId)
      throws ApiException, ServerException, HttpException {

    String endpoint = "/customers/" + customerId + "/jobs/" + jobId;
    String response = Request.doDelete(auth, clientData, endpoint);
  }

}
