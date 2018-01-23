package it.contactlab.hub.sdk.java;

import it.contactlab.hub.sdk.java.exceptions.ApiException;
import it.contactlab.hub.sdk.java.exceptions.HttpException;
import it.contactlab.hub.sdk.java.exceptions.ServerException;
import it.contactlab.hub.sdk.java.internal.api.CustomerApi;
import it.contactlab.hub.sdk.java.internal.api.EducationApi;
import it.contactlab.hub.sdk.java.internal.api.EventApi;
import it.contactlab.hub.sdk.java.internal.api.JobApi;
import it.contactlab.hub.sdk.java.internal.api.LikeApi;
import it.contactlab.hub.sdk.java.internal.api.QueryApi;
import it.contactlab.hub.sdk.java.internal.api.SessionApi;
import it.contactlab.hub.sdk.java.internal.api.TagApi;
import it.contactlab.hub.sdk.java.models.Customer;
import it.contactlab.hub.sdk.java.models.Education;
import it.contactlab.hub.sdk.java.models.Event;
import it.contactlab.hub.sdk.java.models.EventFilters;
import it.contactlab.hub.sdk.java.models.GetCustomersOptions;
import it.contactlab.hub.sdk.java.models.Job;
import it.contactlab.hub.sdk.java.models.Like;
import it.contactlab.hub.sdk.java.models.Paginated;
import it.contactlab.hub.sdk.java.queries.Operator;
import it.contactlab.hub.sdk.java.queries.QueryContainer;

import java.util.Optional;

/**
 * ContactHub Java SDK (Sync version).
 */
public class ContactHub {

  private final Auth auth;
  private final ClientData clientData;

  public ContactHub(Auth auth) {
      this(auth, null);
  }

  public ContactHub(Auth auth, ClientData clientData) {
    this.auth = auth;
    this.clientData = clientData;
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
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public void addCustomerSession(String customerId, String sessionId)
      throws ApiException, ServerException, HttpException {
    SessionApi.reconcile(this.auth, this.clientData, customerId, sessionId);
  }

  /**
   * Retrieves a Customer by id.
   *
   * @param id A Customer id.
   * @return   A {@link Customer}.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public Customer getCustomer(String id)
      throws ApiException, ServerException, HttpException {
    return CustomerApi.getById(this.auth, this.clientData, id);
  }

  /**
   * Retrieve all the Customers of a Node.
   *
   * @return     A {@link Paginated} List of {@link Customer} objects.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public Paginated<Customer> getCustomers()
      throws ApiException, ServerException, HttpException {
    return CustomerApi.get(this.auth, this.clientData, GetCustomersOptions.builder().build());
  }

  /**
   * Retrieve all the Customers of a Node, filtered and ordered with 'options'
   *
   * @param options An instance of {@link GetCustomersOptions}.
   * @return        A {@link Paginated} List of {@link Customer} objects.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public Paginated<Customer> getCustomers(GetCustomersOptions options)
      throws ApiException, ServerException, HttpException {
    return CustomerApi.get(this.auth, this.clientData, options);
  }

  /**
   * Retrieves Customers by external id.
   *
   * @param externalId A Customer external id.
   * @return           A {@link Paginated} List of {@link Customer} objects.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public Paginated<Customer> getCustomerByExternalId(String externalId)
      throws ApiException, ServerException, HttpException {
    GetCustomersOptions options = GetCustomersOptions.builder()
                                  .externalId(externalId).build();
    return CustomerApi.get(this.auth, this.clientData, options);
  }

  /**
   * Adds a new Customer.
   *
   * @param customer The {@link Customer} to create.
   * @return         A newly created {@link Customer}.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public Customer addCustomer(Customer customer)
      throws ApiException, ServerException, HttpException {
    return CustomerApi.add(this.auth, this.clientData, customer);
  }

  /**
   * Deletes a Customer.
   *
   * @param id A Customer id.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public void deleteCustomer(String id)
      throws ApiException, ServerException, HttpException {
    CustomerApi.delete(this.auth, this.clientData, id);
  }

  /**
   * Updates an existing Customer.
   *
   * @param customer The {@link Customer} to update.
   * @return         An updated {@link Customer}.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public Customer updateCustomer(Customer customer)
      throws ApiException, ServerException, HttpException {
    return CustomerApi.update(this.auth, this.clientData, customer);
  }


  /**
   * Patches an existing Customer.
   *
   * @param customerId    The id of the Customer to update.
   * @param patchCustomer The {@link Customer} object, containing all the values to patch.
   * @return              An updated {@link Customer}.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public Customer patchCustomer(String customerId, Customer patchCustomer)
      throws ApiException, ServerException, HttpException {
    return CustomerApi.patch(this.auth, this.clientData, customerId, patchCustomer);
  }

  /**
   * Adds a {@link Like} to an existing Customer.
   *
   * <p>If the Like is already present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param like       The Like to be added.
   * @return           The Like object that was persisted by the API.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public Like addLike(String customerId, Like like)
      throws ApiException, ServerException, HttpException {
    return LikeApi.add(this.auth, this.clientData, customerId, like);
  }

  /**
   * Update an existing {@link Like} for an existing Customer.
   *
   * <p>If the Like is already present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param like       The updated Like object.
   * @return           The Like object that was persisted by the API.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */

  public Like updateLike(String customerId, Like like)
      throws ApiException, ServerException, HttpException {
    return LikeApi.update(this.auth, this.clientData, customerId, like);
  }

  /**
   * Removes a {@link Like} from an existing Customer.
   *
   * <p>If the Like is not present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param likeId     The id of the Like to be removed.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public void removeLike(String customerId, String likeId)
      throws ApiException, ServerException, HttpException {
    LikeApi.remove(this.auth, this.clientData, customerId, likeId);
  }

  /**
   * Adds a {@link Job} to an existing Customer.
   *
   * <p>If the Job is already present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param job        The Job to be added.
   * @return           The Job object that was persisted by the API.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public Job addJob(String customerId, Job job)
      throws ApiException, ServerException, HttpException {
    return JobApi.add(this.auth, this.clientData, customerId, job);
  }

  /**
   * Update an existing {@link Job} for an existing Customer.
   *
   * <p>If the Job is already present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param job        The updated Job object.
   * @return           The Job object that was persisted by the API.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */

  public Job updateJob(String customerId, Job job)
      throws ApiException, ServerException, HttpException {
    return JobApi.update(this.auth, this.clientData, customerId, job);
  }

  /**
   * Removes a {@link Job} from an existing Customer.
   *
   * <p>If the Job is not present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param jobId      The id of the Job to be removed.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public void removeJob(String customerId, String jobId)
      throws ApiException, ServerException, HttpException {
    JobApi.remove(this.auth, this.clientData, customerId, jobId);
  }

  /**
   * Adds a {@link Education} to an existing Customer.
   *
   * <p>If the Education is already present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param education  The Education to be added.
   * @return           The Education object that was persisted by the API.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public Education addEducation(String customerId, Education education)
      throws ApiException, ServerException, HttpException {
    return EducationApi.add(this.auth, this.clientData, customerId, education);
  }

  /**
   * Update an existing {@link Education} for an existing Customer.
   *
   * <p>If the Education is already present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param education  The updated Education object.
   * @return           The Education object that was persisted by the API.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */

  public Education updateEducation(String customerId, Education education)
      throws ApiException, ServerException, HttpException {
    return EducationApi.update(this.auth, this.clientData, customerId, education);
  }

  /**
   * Removes a {@link Education} from an existing Customer.
   *
   * <p>If the Education is not present, nothing will be done.</p>
   *
   * @param customerId  The id of the Customer.
   * @param educationId The id of the Education to be removed.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public void removeEducation(String customerId, String educationId)
      throws ApiException, ServerException, HttpException {
    EducationApi.remove(this.auth, this.clientData, customerId, educationId);
  }

  /**
   * Adds a tag to an existing Customer.
   *
   * <p>If the tag is already present, nothing will be done.
   *
   * @param customerId The id of the Customer.
   * @param tag        The tag to be added.
   * @return           The full Customer object after the update.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public Customer addTag(String customerId, String tag)
      throws ApiException, ServerException, HttpException {
    return TagApi.add(this.auth, this.clientData, customerId, tag);
  }

  /**
   * Removes a tag from an existing Customer.
   *
   * <p>If the tag is not present, nothing will be done.
   *
   * @param customerId The id of the Customer.
   * @param tag        The tag to be removed.
   * @return           The full Customer object after the update.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public Customer removeTag(String customerId, String tag)
      throws ApiException, ServerException, HttpException {
    return TagApi.remove(this.auth, this.clientData, customerId, tag);
  }

  /**
   * Adds a new Event.
   *
   * @param newEvent The {@link Event} to create.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public void addEvent(Event newEvent)
      throws ApiException, ServerException, HttpException {
    EventApi.add(this.auth, this.clientData, newEvent);
  }

  /**
   * Retrieves an Event.
   *
   * @param id The id of the event
   * @return   An {@link Event}.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public Event getEvent(String id)
      throws ApiException, ServerException, HttpException {
    return EventApi.getById(this.auth, this.clientData, id);
  }

  /**
   * Retrieves all the Events for a Customer.
   *
   * @param customerId The id of a Customer with some Events.
   * @return A {@link Paginated} List of {@link Event} objects.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public Paginated<Event> getEvents(String customerId)
      throws ApiException, ServerException, HttpException {
    return EventApi.get(this.auth, this.clientData, customerId, EventFilters.builder().build());
  }

  /**
   * Retrieves all the Events for a Customer, with filters.
   *
   * @param customerId The id of a Customer with some Events.
   * @param filters    An instance of {@link EventFilters}.
   * @return A {@link Paginated} List of {@link Event} objects.
   *
   * @throws ApiException    if the API returns an error.
   * @throws ServerException if the API returns an unexpected response.
   * @throws HttpException   if the API request cannot be completed.
   */
  public Paginated<Event> getEvents(String customerId, EventFilters filters)
      throws ApiException, ServerException, HttpException {
    return EventApi.get(this.auth, this.clientData, customerId, filters);
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
