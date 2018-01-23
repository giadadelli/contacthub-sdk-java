package it.contactlab.hub.sdk.java.internal.api;

import it.contactlab.hub.sdk.java.Auth;
import it.contactlab.hub.sdk.java.ClientData;
import it.contactlab.hub.sdk.java.exceptions.ApiException;
import it.contactlab.hub.sdk.java.exceptions.ContactHubException;
import it.contactlab.hub.sdk.java.exceptions.HttpException;
import it.contactlab.hub.sdk.java.exceptions.ServerException;
import it.contactlab.hub.sdk.java.internal.gson.ContactHubGson;
import it.contactlab.hub.sdk.java.internal.http.Request;
import it.contactlab.hub.sdk.java.models.AsyncPaginated;
import it.contactlab.hub.sdk.java.models.Customer;
import it.contactlab.hub.sdk.java.models.GetCustomersOptions;
import it.contactlab.hub.sdk.java.models.Paged;
import it.contactlab.hub.sdk.java.models.Paginated;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class CustomerApi {

  private static Gson gson = ContactHubGson.getInstance();

  private static Paged<Customer> getPaged(Auth auth, ClientData clientData, GetCustomersOptions options)
      throws ApiException, ServerException, HttpException {
    Map<String, Object> queryString = new HashMap<>();

    final String endpoint = "/customers";

    queryString.put("nodeId", auth.nodeId);

    options.page().ifPresent(page -> queryString.put("page", page));

    options.pageSize().ifPresent(pageSize -> queryString.put("size", pageSize));

    options.externalId().ifPresent(id -> queryString.put("externalId", id));

    if (!options.fields().isEmpty()) {
      queryString.put("fields", String.join(",", options.fields()));
    }

    options.query().ifPresent(query -> queryString.put("query", gson.toJson(query).toString()));

    options.sort().ifPresent(sortField -> {
      queryString.put("sort",
          sortField + options.direction().map(dir -> "," + dir).orElse(""));
    });

    String response = Request.doGet(auth, clientData, endpoint, queryString);

    Type paginatedCustomerType = new TypeToken<Paged<Customer>>(){}.getType();
    Paged<Customer> pagedCustomers = gson.fromJson(response, paginatedCustomerType);

    return pagedCustomers;
  }

  /**
   * Async version of get.
   *
   * @param auth       A ContactHub Auth object.
   * @param options    An instance of {@link GetCustomersOptions}.
   * @return           A {@link CompletionStage} of {@link AsyncPaginated} {@link Customer} objects.
   */
  public static CompletionStage<AsyncPaginated<Customer>> asyncGet(
      Auth auth, ClientData clientData, GetCustomersOptions options) {

    Function<Integer, CompletionStage<AsyncPaginated<Customer>>>
        requestFunction = (Integer pageNumber) ->
            asyncGet(auth, clientData, options.withPage(pageNumber));

    return CompletableFuture.supplyAsync(() -> {
      try {
        Paged<Customer> pagedCustomers = getPaged(auth, clientData, options);

        return new AsyncPaginated<Customer>(pagedCustomers, requestFunction);
      } catch (ContactHubException ex) {
        throw new CompletionException(ex);
      }
    });
  }

  /**
   * Retrieves all the Customers for a Node, with options
   *
   * @param auth       A ContactHub Auth object.
 * @param clientData 
   * @param options    An instance of {@link GetCustomersOptions}.
   * @return           A {@link Paginated} list of matching Customer objects.
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public static Paginated<Customer> get(Auth auth, ClientData clientData, GetCustomersOptions options)
      throws ApiException, ServerException, HttpException {

    Paged<Customer> pagedCustomers = getPaged(auth, clientData, options);

    Function<Integer, Paginated<Customer>> requestFunction = (Integer pageNumber) -> {
      try {
        return get(auth, clientData, options.withPage(pageNumber));
      } catch (ContactHubException exception) {
        throw new RuntimeException(exception);
      }
    };

    return new Paginated<Customer>(pagedCustomers, requestFunction);
  }

  /**
   * Retrieves a Customer by id.
   *
   * @param auth A ContactHub Auth object.
 * @param clientData 
   * @param id   The Customer id.
   * @return     A Customer object.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public static Customer getById(Auth auth, ClientData clientData, String id)
      throws ApiException, ServerException, HttpException {
    String endpoint = "/customers/" + id;
    String response = Request.doGet(auth, clientData, endpoint);

    return gson.fromJson(response, Customer.class);
  }

  /**
   * Adds a new Customer.
   *
   * @param auth     A ContactHub Auth object.
 * @param clientData 
   * @param customer The Customer object.
   * @return         The stored Customer object, including its id.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public static Customer add(Auth auth, ClientData clientData, Customer customer)
      throws ApiException, ServerException, HttpException {
    String endpoint = "/customers";
    Customer expectedCustomer = customer.withNodeId(auth.nodeId);
    String payload = gson.toJson(expectedCustomer);
    String response = Request.doPost(auth, clientData, endpoint, payload);

    return gson.fromJson(response, Customer.class);
  }

  /**
   * Deletes a Customer.
   *
   * @param auth       A ContactHub Auth object.
 * @param clientData 
   * @param customerId The id of the Customer to delete.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public static void delete(Auth auth, ClientData clientData, String customerId)
      throws ApiException, ServerException, HttpException {
    String endpoint = "/customers/" + customerId;
    String response = Request.doDelete(auth, clientData, endpoint);
  }

  /**
   * Updates a Customer.
   *
   * @param auth     A ContactHub Auth object.
 * @param clientData 
   * @param customer The Customer object.
   * @return         The updated Customer object
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public static Customer update(Auth auth, ClientData clientData, Customer customer)
      throws ApiException, ServerException, HttpException {
    String endpoint = "/customers/" + customer.id().get();
    Customer expectedCustomer = customer.withNodeId(auth.nodeId);
    String payload = gson.toJson(expectedCustomer);
    String response = Request.doPut(auth, clientData, endpoint, payload);

    return gson.fromJson(response.toString(), Customer.class);
  }

  /**
   * Patches a Customer.
   *
   * @param auth          A ContactHub Auth object.
 * @param clientData 
   * @param customerId    The id of the Customer to patch.
   * @param patchCustomer The CustomerPatch object.
   * @return              The updated Customer object
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public static Customer patch(Auth auth, ClientData clientData, String customerId, Customer patchCustomer)
      throws ApiException, ServerException, HttpException {
    String endpoint = "/customers/" + customerId;
    String payload = gson.toJson(patchCustomer);
    String response = Request.doPatch(auth, clientData, endpoint, payload);

    return gson.fromJson(response, Customer.class);
  }

}
