package it.contactlab.hub.sdk.java;

import it.contactlab.hub.sdk.java.Auth;
import it.contactlab.hub.sdk.java.exceptions.ContactHubException;
import it.contactlab.hub.sdk.java.internal.api.CustomerApi;
import it.contactlab.hub.sdk.java.internal.api.EducationApi;
import it.contactlab.hub.sdk.java.internal.api.EventApi;
import it.contactlab.hub.sdk.java.internal.api.JobApi;
import it.contactlab.hub.sdk.java.internal.api.LikeApi;
import it.contactlab.hub.sdk.java.internal.api.QueryApi;
import it.contactlab.hub.sdk.java.internal.api.SessionApi;
import it.contactlab.hub.sdk.java.internal.api.TagApi;
import it.contactlab.hub.sdk.java.models.AsyncPaginated;
import it.contactlab.hub.sdk.java.models.Customer;
import it.contactlab.hub.sdk.java.models.Education;
import it.contactlab.hub.sdk.java.models.Event;
import it.contactlab.hub.sdk.java.models.EventFilters;
import it.contactlab.hub.sdk.java.models.GetCustomersOptions;
import it.contactlab.hub.sdk.java.models.Job;
import it.contactlab.hub.sdk.java.models.Like;
import it.contactlab.hub.sdk.java.queries.Operator;
import it.contactlab.hub.sdk.java.queries.QueryContainer;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

/**
 * ContactHub Java SDK (Async version).
 */

public class AsyncContactHub {

  public Auth auth;
  private final ClientData clientData;

  public AsyncContactHub(Auth auth) {
    this(auth, null);
  }
  
  public AsyncContactHub(Auth auth, ClientData clientData) {
      this.auth = auth;
      this.clientData = clientData;
  }

  @FunctionalInterface
  private interface ThrowingSupplier<T> {
    T get() throws ContactHubException;
  }

  @FunctionalInterface
  private interface ThrowingRunnable {
    void run() throws ContactHubException;
  }

  private <T> CompletionStage<T> wrapAsync(ThrowingSupplier<T> supplier) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return supplier.get();
      } catch (ContactHubException exception) {
        throw new CompletionException(exception);
      }
    });
  }

  private CompletionStage<Void> wrapAsync(ThrowingRunnable runnable) {
    return CompletableFuture.runAsync(() -> {
      try {
        runnable.run();
      } catch (ContactHubException exception) {
        throw new CompletionException(exception);
      }
    });
  }

  /**
   * Generate a new SessionId.
   *
   * @return A new randomly-generated Session id.
   */
  public String createSessionId() {
    return SessionApi.generate();
  }

  /**
   * Reconcile a SessionId with a Customer.
   *
   * @param  customerId A Customer id.
   * @param  sessionId  A session id that will be associated with the Customer.
   * @return            A {@link CompletionStage}.
   */
  public CompletionStage<Void> addCustomerSession(String customerId, String sessionId) {
    return wrapAsync(() -> SessionApi.reconcile(this.auth, this.clientData, customerId, sessionId));
  }

  /**
   * Retrieves a Customer by id.
   *
   * @param id A Customer id.
   * @return   A {@link CompletionStage} of {@link Customer}.
   */
  public CompletionStage<Customer> getCustomer(String id) {
    return wrapAsync(() -> CustomerApi.getById(this.auth, this.clientData, id));
  }

  /**
   * Retrieve all the Customers of a Node.
   *
   * @return A {@link CompletionStage} of {@link AsyncPaginated} {@link Customer} objects.
   */
  public CompletionStage<AsyncPaginated<Customer>> getCustomers() {
    return CustomerApi.asyncGet(this.auth, this.clientData, GetCustomersOptions.builder().build());
  }

  /**
   * Retrieve all the Customers of a Node, filtered and ordered with 'options'
   *
   * @param options An instance of {@link GetCustomersOptions}.
   * @return        A {@link CompletionStage} of {@link AsyncPaginated} {@link Customer} objects.
   */
  public CompletionStage<AsyncPaginated<Customer>> getCustomers(GetCustomersOptions options) {
    return CustomerApi.asyncGet(this.auth, this.clientData, options);
  }

  /**
   * Retrieves Customers by external id.
   *
   * @param externalId A Customer external id.
   * @return           A {@link CompletionStage} of {@link AsyncPaginated} {@link Customer} objects.
   */
  public CompletionStage<AsyncPaginated<Customer>> getCustomerByExternalId(String externalId) {
    GetCustomersOptions options = GetCustomersOptions.builder()
                                  .externalId(externalId).build();
    return CustomerApi.asyncGet(this.auth, this.clientData, options);
  }

  /**
   * Adds a new Customer.
   *
   * @param customer The {@link Customer} to create.
   * @return         A {@link CompletionStage} of {@link Customer}.
   */
  public CompletionStage<Customer> addCustomer(Customer customer) {
    return wrapAsync(() -> CustomerApi.add(this.auth, this.clientData, customer));
  }

  /**
   * Deletes a Customer.
   *
   * @param id A Customer id.
   * @return   A {@link CompletionStage}
   */
  public CompletionStage<Void> deleteCustomer(String id) {
    return wrapAsync(() -> CustomerApi.delete(this.auth, this.clientData, id));
  }

  /**
   * Updates an existing Customer.
   *
   * @param customer The {@link Customer} to update.
   * @return         A {@link CompletionStage} of {@link Customer}.
   */
  public CompletionStage<Customer> updateCustomer(Customer customer) {
    return wrapAsync(() -> CustomerApi.update(this.auth, this.clientData, customer));
  }


  /**
   * Patches an existing Customer.
   *
   * @param customerId    The id of the Customer to update.
   * @param patchCustomer The {@link Customer} object, containing all the values to patch.
   * @return              A {@link CompletionStage} of {@link Customer}.
   */
  public CompletionStage<Customer> patchCustomer(String customerId, Customer patchCustomer) {
    return wrapAsync(() -> CustomerApi.patch(this.auth, this.clientData, customerId, patchCustomer));
  }

  /**
   * Adds a {@link Like} to an existing Customer.
   *
   * <p>If the Like is already present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param like       The Like to be added.
   * @return           The Like object that was persisted by the API.
   */
  public CompletionStage<Like> addLike(String customerId, Like like) {
    return wrapAsync(() -> LikeApi.add(this.auth, this.clientData, customerId, like));
  }

  /**
   * Update an existing {@link Like} for an existing Customer.
   *
   * <p>If the Like is already present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param like       The updated Like object.
   * @return           The Like object that was persisted by the API.
   */

  public CompletionStage<Like> updateLike(String customerId, Like like) {
    return wrapAsync(() -> LikeApi.update(this.auth, this.clientData, customerId, like));
  }

  /**
   * Removes a {@link Like} from an existing Customer.
   *
   * <p>If the Like is not present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param likeId     The id of the Like to be removed.
   * @return           A {@link CompletionStage}.
   */
  public CompletionStage<Void> removeLike(String customerId, String likeId) {
    return wrapAsync(() -> LikeApi.remove(this.auth, this.clientData, customerId, likeId));
  }

  /**
   * Adds a {@link Job} to an existing Customer.
   *
   * <p>If the Job is already present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param job        The Job to be added.
   * @return           The Job object that was persisted by the API.
   */
  public CompletionStage<Job> addJob(String customerId, Job job) {
    return wrapAsync(() -> JobApi.add(this.auth, this.clientData, customerId, job));
  }

  /**
   * Update an existing {@link Job} for an existing Customer.
   *
   * <p>If the Job is already present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param job        The updated Job object.
   * @return           The Job object that was persisted by the API.
   */

  public CompletionStage<Job> updateJob(String customerId, Job job) {
    return wrapAsync(() -> JobApi.update(this.auth, this.clientData, customerId, job));
  }

  /**
   * Removes a {@link Job} from an existing Customer.
   *
   * <p>If the Job is not present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param jobId      The id of the Job to be removed.
   * @return           A {@link CompletionStage}.
   */
  public CompletionStage<Void> removeJob(String customerId, String jobId) {
    return wrapAsync(() -> JobApi.remove(this.auth, this.clientData, customerId, jobId));
  }

  /**
   * Adds a {@link Education} to an existing Customer.
   *
   * <p>If the Education is already present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param education  The Education to be added.
   * @return           The Education object that was persisted by the API.
   */
  public CompletionStage<Education> addEducation(String customerId, Education education) {
    return wrapAsync(() -> EducationApi.add(this.auth, this.clientData, customerId, education));
  }

  /**
   * Update an existing {@link Education} for an existing Customer.
   *
   * <p>If the Education is already present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param education  The updated Education object.
   * @return           The Education object that was persisted by the API.
   */

  public CompletionStage<Education> updateEducation(String customerId, Education education) {
    return wrapAsync(() -> EducationApi.update(this.auth, this.clientData, customerId, education));
  }

  /**
   * Removes a {@link Education} from an existing Customer.
   *
   * <p>If the Education is not present, nothing will be done.</p>
   *
   * @param customerId  The id of the Customer.
   * @param educationId The id of the Education to be removed.
   * @return            A {@link CompletionStage}.
   */
  public CompletionStage<Void> removeEducation(String customerId, String educationId) {
    return wrapAsync(() -> EducationApi.remove(this.auth, this.clientData, customerId, educationId));
  }

  /**
   * Adds a tag to an existing Customer.
   *
   * <p>If the tag is already present, nothing will be done.
   *
   * @param customerId The id of the Customer.
   * @param tag        The tag to be added.
   * @return           The full Customer object after the update.
   */
  public CompletionStage<Customer> addTag(String customerId, String tag) {
    return wrapAsync(() -> TagApi.add(this.auth, this.clientData, customerId, tag));
  }

  /**
   * Removes a tag from an existing Customer.
   *
   * <p>If the tag is not present, nothing will be done.
   *
   * @param customerId The id of the Customer.
   * @param tag        The tag to be removed.
   * @return           The full Customer object after the update.
   */
  public CompletionStage<Customer> removeTag(String customerId, String tag) {
    return wrapAsync(() -> TagApi.remove(this.auth, this.clientData, customerId, tag));
  }

  /**
   * Adds a new Event.
   *
   * @param newEvent The {@link Event} to create.
   * @return         A {@link CompletionStage}.
   */
  public CompletionStage<Void> addEvent(Event newEvent) {
    return wrapAsync(() -> EventApi.add(this.auth, this.clientData, newEvent));
  }

  /**
   * Retrieves an Event.
   *
   * @param id The id of the event
   * @return   An {@link Event}.
   */
  public CompletionStage<Event> getEvent(String id) {
    return wrapAsync(() -> EventApi.getById(this.auth, this.clientData, id));
  }

  /**
   * Retrieves all the Events for a Customer.
   *
   * @param customerId The id of a Customer with some Events.
   * @return           A {@link CompletionStage} of {@link AsyncPaginated} {@link Event} objects.
   */
  public CompletionStage<AsyncPaginated<Event>> getEvents(String customerId) {
    return EventApi.asyncGet(this.auth, this.clientData, customerId, EventFilters.builder().build());
  }

  /**
   * Retrieves all the Events for a Customer, with filters.
   *
   * @param customerId The id of a Customer with some Events.
   * @param filters    An instance of {@link EventFilters}.
   * @return           A {@link CompletionStage} of {@link AsyncPaginated} {@link Event} objects.
   */
  public CompletionStage<AsyncPaginated<Event>> getEvents(
      String customerId, EventFilters filters
  ) {
    return EventApi.asyncGet(this.auth, this.clientData, customerId, filters);
  }

  /**
   * Returns a {@link QueryContainer} based on some simple arguments.
   */
  public QueryContainer createQuery(String attribute, Operator operator, Object value) {
    return QueryApi.createQuery(attribute, operator, Optional.of(value));
  }

  /**
   * Overloaded version of createQuery for operators that do not require a value.
   */
  public QueryContainer createQuery(String attribute, Operator operator) {
    return QueryApi.createQuery(attribute, operator, Optional.empty());
  }
}
