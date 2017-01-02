package it.contactlab.hub.sdk.java.sync.test.integration;

import it.contactlab.hub.sdk.java.sync.ContactHub
import it.contactlab.hub.sdk.java.Auth
import it.contactlab.hub.sdk.java.models._, base._
import it.contactlab.hub.sdk.java.exceptions._

import org.scalatest.FeatureSpec
import org.scalatest.Matchers._
import org.scalatest.GivenWhenThen
import org.scalatest.BeforeAndAfter

import scala.collection.JavaConversions._

class EducationSpec extends FeatureSpec with GivenWhenThen with BeforeAndAfter {

  val auth = new Auth(
    "97841617075b4b5f8ea88c30a8d2aec7647b7181df2c483fa78138c8d58aed4d",
    "40b6195f-e4f7-4f95-b10e-75268d850988",
    "854f0791-c120-4e4a-9264-6dd197cb922c"
  )

  val ch = new ContactHub(auth)
  val customerId = "0533ef33-81b1-4993-9025-18edaec976d3"

  val education1 = Education.builder.id("education1").schoolName("foo").build
  val education2 = Education.builder.id("education2").schoolName("bar").build

  before {
    ch.patchCustomer(customerId, Customer.builder
      .base(BaseProperties.builder
        .educations(Seq(education1, education2))
        .build)
      .build)
  }

  feature("managing educations") {
    scenario("adding a new education", Integration) {
      Given("a known customer")
      val cid = customerId

      When("I add a new education")
      val newEducation = Education.builder.id("education3").schoolName("baz").build
      val updated = ch.addEducation(cid, newEducation)

      Then("The new education is present")
      updated.base.get.educations.get should contain (newEducation)

      And("The old ones are still there")
      updated.base.get.educations.get should contain (education1)
    }

    scenario("updating a education", Integration) {
      Given("a known customer")
      val cid = customerId

      When("I update an existing education")
      val newEducation = Education.builder.id("education2").schoolName("baz").build
      val updated = ch.updateEducation(cid, newEducation)

      Then("The matching education is updated")
      updated.base.get.educations.get should contain (newEducation)

      And("The other educations are still there")
      updated.base.get.educations.get should contain (education1)
    }

    scenario("removing a education", Integration) {
      Given("a known customer")
      val cid = customerId

      When("I remove an existing education")
      val updated = ch.removeEducation(cid, "education1")

      Then("The matching education is removed")
      updated.base.get.educations.get should not contain (education1)
    }
  }
}