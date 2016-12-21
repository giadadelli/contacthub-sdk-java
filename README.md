# ContactHUB Java SDK

## Private repository for beta

Until this library reaches 1.0, it's only available from a private Maven
repository at https://buildo-private-maven.appspot.com/

If you have a valid account for this repository, add it to your build tool
configuration.

### sbt

Add the following lines to your `build.sbt`:

```scala
resolvers += "buildo private maven" at "https://buildo-private-maven.appspot.com/"
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
```

Then create a file '~/.ivy2/.credentials' with the following content:

```
realm=
host=buildo-private-maven.appspot.com
user=user
password=pass
```


### Gradle

Add this to your project's `build.gradle`:

```
repositories {
  mavenCentral()
  maven {
    credentials {
      username "user"
      password "pass"
    }
    url "https://buildo-private-maven.appspot.com"
  }
}
```

Then add your credentials to `~/.gradle/gradle.properties`:

```
buildoMavenUser=user
buildoMavenPassword=pass
```

### Maven

Add this to your project's `pom.xml`:

```xml
  <repositories>
    <repository>
      <id>buildo-private-maven.appspot.com</id>
      <url>https://buildo-private-maven.appspot.com</url>
    </repository>
  </repositories>
```

Then add your credentials to `~/.m2/settings.xml`:

```xml
<settings>
  <servers>
    <server>
      <id>buildo-private-maven.appspot.com</id>
      <configuration>
        <httpHeaders>
          <property>
            <name>Authorization</name>
            <!-- Encode the string "user:pass" using https://www.base64encode.org/ -->
            <value>Basic dXNlcjpwYXNz</value>
          </property>
        </httpHeaders>
      </configuration>
    </server>
  </servers>
</settings>
```


## Adding this library to your project's dependencies

### Maven

```xml
<dependency>
  <groupId>it.contactlab.hub</groupId>
  <artifactId>sdk-java</artifactId>
  <version>0.2.1</version>
</dependency>
```

### Gradle
```
dependencies {
  compile 'it.contactlab.hub:sdk-java:0.2.1
}
```


## Importing this library in your project

```java
import it.contactlab.hub.sdk.java.sync.ContactHub;
```

We also provide a separate `async` implementation where all the methods return a
Java8 [CompletionStage&lt;T&gt;](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletionStage.html).

If you wish the use the Async api, import from the `async` package instead:

```java
import it.contactlab.hub.sdk.java.async.ContactHub;
```

All the methods documented below are available in both packages.


## Authenticating

Find your token, workspaceId and nodeId in the ContactHub dashboard. Then
instantiate a new ContactHub object like this:

```java
Auth auth = new Auth(token, workspaceId, nodeId);

ContactHub ch = new ContactHub(auth);
```


## Customer API

### getCustomer

Retrieve a customer by its id.

```java
Customer customer = ch.getCustomer("a-valid-customer-id");
```

### getCustomers

Retrieve all the Customers in a Node.

```java
List<Customer> customers = ch.getCustomers();
```

### getCustomerByExternalId

Retrieve all the Customers matching the given external id.

```java
List<Customer> customers = ch.getCustomerByExternalId("an-external-id");
```

Please note you can have more than one customer with the same external id,
unless you include "external id" in the "matching policies" for your workspace.

### addCustomer

Add a new Customer. This method returns a new Customer object including the id that was
assigned to the new customer by the API.

```java
Customer newCustomer = addCustomer(Customer customer)
```

If the "Customer uniqueness" configuration for your workspace is set to "Merge"
and the new customer data matches an existing customer according to the
"matching policies" for your workspace, this method will return the merged
Customer data.

To create a `Customer` instance that this method requires as its argument, use the
builder provided by the `Customer` object.

```java
Customer.builder()
        .base(...)
        .extended(...)
        // ...
        .build();
```

### deleteCustomer

Delete the Customer with the specified id.

```java
deleteCustomer("an-existing-id");
```

### updateCustomer

Update an existing Customer by replacing all of its data with the data provided.

```java
Customer updatedCustomer = updateCustomer(Customer newCustomer);
```

This method will fail if the id of `newCustomer` doesn't match an existing
customer.

### patchCustomer

Update an existing Customer (identified by `id`) by merging its existing data
with the data provided.

```java
Customer updatedCustomer = patchCustomer(String id, Customer newCustomerData);
```

Customer properties that were already set and are included in the new data will
be overwritten.

Customer properties that were already set and are not specified will be left
untouched.

The merge is performed API-side. If you want more control on how the data is
merged, retrieve the customer with `getCustomer()`, update the properties
locally and overwrite the whole customer with `updateCustomer()`.


## Examples

Check the [example](example/) folder for working examples you can download and
try out.

### Running the example with sbt

```sh
sbt example/run
```

### Running th eexample with Gradle

```sh
cd example
gradle run
```

### Running the example with Maven

```sh
cd example
mvn compile
mvn exec:java
```


## Contributing to this library

Despite this being a Java library, we use [sbt](http://www.scala-sbt.org/) as
the build tool.

`sbt compile` will compile all the Java sources to `target/classes`

`sbt package` will package the compiled files in a JAR file under `target`

`sbt doc` will generate HTML JavaDoc in `target/api`

`sbt packageDoc` will package the javadoc files in a JAR file under `target`


### Immutables

This library uses [Immutables](http://immutables.github.io/) to generate
high-quality, immutable implementations of the domain models.

This dramatically reduces Java boilerplate code. For example, only an
`AbstractCustomer` model is present in the `src` folder. When you compile it,
the `Customer` implementation will be automatically generated by Immutables and
saved as `target/classes/.../Customer.java`.
