package it.contactlab.hub.sdk.java;

import it.contactlab.hub.sdk.java.exceptions.ContactHubException;
import it.contactlab.hub.sdk.java.exceptions.HttpException;
import it.contactlab.hub.sdk.java.exceptions.ServerException;
import it.contactlab.hub.sdk.java.internal.api.CustomerApi;
import it.contactlab.hub.sdk.java.internal.api.EducationApi;
import it.contactlab.hub.sdk.java.internal.api.EventApi;
import it.contactlab.hub.sdk.java.internal.api.JobApi;
import it.contactlab.hub.sdk.java.internal.api.LikeApi;
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

/**
 * ContactHub Java SDK (Sync version).
 */
public class ContactHub {

  private final Auth auth;

  public ContactHub(Auth auth) {
    this.auth = auth;
  }

  /**
   * Generate a new SessionId.
   */
  public String createSessionId() {
    return SessionApi.generate();
  }

  /**
   * Reconcile a SessionId with a Customer.
   */
  public boolean addCustomerSession(String customerId, String sessionId)
      throws ContactHubException, ServerException, HttpException {
    return SessionApi.reconcile(this.auth, customerId, sessionId);
  }

  /**
   * Retrieves a Customer by id.
   *
   * @param id A Customer id.
   * @return   A {@link Customer}.
   */
  public Customer getCustomer(String id)
      throws ContactHubException, ServerException, HttpException {
    return CustomerApi.get(this.auth, id);
  }

  /**
   * Retrieve all the Customers of a Node.
   *
   * @return     A {@link Paginated} List of {@link Customer} objects.
   */
  public Paginated<Customer> getCustomers()
      throws ContactHubException, ServerException, HttpException {
    return CustomerApi.get(this.auth);
  }

  /**
   * Retrieve all the Customers of a Node, filtered and ordered with 'options'
   *
   * @param options An instance of {@link GetCustomersOptions}.
   * @return        A {@link Paginated} List of {@link Customer} objects.
   */
  public Paginated<Customer> getCustomers(GetCustomersOptions options)
      throws ContactHubException, ServerException, HttpException {
    return CustomerApi.get(this.auth, options);
  }

  /**
   * Retrieves Customers by external id.
   *
   * @param externalId A Customer external id.
   * @return           A {@link Paginated} List of {@link Customer} objects.
   */
  public Paginated<Customer> getCustomerByExternalId(String externalId)
      throws ContactHubException, ServerException, HttpException {
    GetCustomersOptions options = GetCustomersOptions.builder()
                                  .externalId(externalId).build();
    return CustomerApi.get(this.auth, options);
  }

  /**
   * Adds a new Customer.
   *
   * @param customer The {@link Customer} to create.
   * @return         A newly created {@link Customer}.
   */
  public Customer addCustomer(Customer customer)
      throws ContactHubException, ServerException, HttpException {
    return CustomerApi.add(this.auth, customer);
  }

  /**
   * Deletes a Customer.
   *
   * @param id A Customer id.
   * @return   Whether the Customer was successfully deleted.
   */
  public boolean deleteCustomer(String id)
      throws ContactHubException, ServerException, HttpException {
    return CustomerApi.delete(this.auth, id);
  }

  /**
   * Updates an existing Customer.
   *
   * @param customer The {@link Customer} to update.
   * @return         An updated {@link Customer}.
   */
  public Customer updateCustomer(Customer customer)
      throws ContactHubException, ServerException, HttpException {
    return CustomerApi.update(this.auth, customer);
  }


  /**
   * Patches an existing Customer.
   *
   * @param customerId    The id of the Customer to update.
   * @param patchCustomer The {@link Customer} object, containing all the values to patch.
   * @return              An updated {@link Customer}.
   */
  public Customer patchCustomer(String customerId, Customer patchCustomer)
      throws ContactHubException, ServerException, HttpException {
    return CustomerApi.patch(this.auth, customerId, patchCustomer);
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
  public Like addLike(String customerId, Like like)
      throws ContactHubException, ServerException, HttpException {
    return LikeApi.add(this.auth, customerId, like);
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

  public Like updateLike(String customerId, Like like)
      throws ContactHubException, ServerException, HttpException {
    return LikeApi.update(this.auth, customerId, like);
  }

  /**
   * Removes a {@link Like} from an existing Customer.
   *
   * <p>If the Like is not present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param likeId     The id of the Like to be removed.
   * @return           true if the removal was successful.
   */
  public boolean removeLike(String customerId, String likeId)
      throws ContactHubException, ServerException, HttpException {
    return LikeApi.remove(this.auth, customerId, likeId);
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
  public Job addJob(String customerId, Job job)
      throws ContactHubException, ServerException, HttpException {
    return JobApi.add(this.auth, customerId, job);
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

  public Job updateJob(String customerId, Job job)
      throws ContactHubException, ServerException, HttpException {
    return JobApi.update(this.auth, customerId, job);
  }

  /**
   * Removes a {@link Job} from an existing Customer.
   *
   * <p>If the Job is not present, nothing will be done.</p>
   *
   * @param customerId The id of the Customer.
   * @param jobId      The id of the Job to be removed.
   * @return           true if the removal was successful.
   */
  public boolean removeJob(String customerId, String jobId)
      throws ContactHubException, ServerException, HttpException {
    return JobApi.remove(this.auth, customerId, jobId);
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
  public Education addEducation(String customerId, Education education)
      throws ContactHubException, ServerException, HttpException {
    return EducationApi.add(this.auth, customerId, education);
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

  public Education updateEducation(String customerId, Education education)
      throws ContactHubException, ServerException, HttpException {
    return EducationApi.update(this.auth, customerId, education);
  }

  /**
   * Removes a {@link Education} from an existing Customer.
   *
   * <p>If the Education is not present, nothing will be done.</p>
   *
   * @param customerId  The id of the Customer.
   * @param educationId The id of the Education to be removed.
   * @return            true if the removal was successful.
   */
  public boolean removeEducation(String customerId, String educationId)
      throws ContactHubException, ServerException, HttpException {
    return EducationApi.remove(this.auth, customerId, educationId);
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
  public Customer addTag(String customerId, String tag)
      throws ContactHubException, ServerException, HttpException {
    return TagApi.add(this.auth, customerId, tag);
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
  public Customer removeTag(String customerId, String tag)
      throws ContactHubException, ServerException, HttpException {
    return TagApi.remove(this.auth, customerId, tag);
  }

  /**
   * Adds a new Event.
   *
   * @param newEvent The {@link Event} to create.
   * @return Whether the Event was successfully queued for insertion.
   */
  public boolean addEvent(Event newEvent)
      throws ContactHubException, ServerException, HttpException {
    return EventApi.add(this.auth, newEvent);
  }

  /**
   * Retrieves an Event.
   *
   * @param id The id of the event
   * @return   An {@link Event}.
   */
  public Event getEvent(String id)
      throws ContactHubException, ServerException, HttpException {
    return EventApi.get(this.auth, id);
  }

  /**
   * Retrieves all the Events for a Customer.
   *
   * @param customerId The id of a Customer with some Events.
   * @return A {@link Paginated} List of {@link Event} objects.
   */
  public Paginated<Event> getEvents(String customerId)
      throws ContactHubException, ServerException, HttpException {
    return EventApi.getByCustomer(this.auth, customerId);
  }

  /**
   * Retrieves all the Events for a Customer, with filters.
   *
   * @param customerId The id of a Customer with some Events.
   * @param filters    An instance of {@link EventFilters}.
   * @return A {@link Paginated} List of {@link Event} objects.
   */
  public Paginated<Event> getEvents(String customerId, EventFilters filters)
      throws ContactHubException, ServerException, HttpException {
    return EventApi.getByCustomer(this.auth, customerId, filters);
  }

}