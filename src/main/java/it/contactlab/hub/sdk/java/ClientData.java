package it.contactlab.hub.sdk.java;

public class ClientData {

  public String correlationId;

  /**
   * Creates a new ClientData instance with a custom API url.
   *
   * @param correlationId       The correlation id
   */
  public ClientData(
      String correlationId
  ) {
    if (correlationId == null || correlationId.trim().equals("")) {
      throw new IllegalArgumentException("correlationId cannot be null or empty");
    }

    this.correlationId = correlationId;
  }

}
