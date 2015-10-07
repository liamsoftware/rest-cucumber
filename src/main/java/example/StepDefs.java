package example;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefs {
   private Belly belly = new Belly();

   @Given("^I have (\\d+) cukes in my belly$")
   public void I_have_cukes_in_my_belly(int cukes) {
      Belly belly = new Belly();
      belly.eat(cukes);
   }

   @When("^I wait (\\d+) hour$")
   public void i_wait_hour(int cukes) {
      belly.waitAnHour(cukes);
   }

   @Then("^my belly should growl$")
   public void my_belly_should_growl() {
      belly.growl();
   }

   @Given("^I'm logged in as a member$")
   public void i_m_logged_in_as_a_member() {

   }

   @When("^I click \"(.*?)\"$")
   public void i_click(String arg1) {
   }

   @Then("^I should see the provider search form$")
   public void i_should_see_the_provider_search_form() {

   }

   @Given("^no available providers$")
   public void no_available_providers() {

   }

   @Given("^the following providers:$")
   public void the_following_providers(DataTable arg1) {

   }

   @When("^I search for a provider with the criteria:$")
   public void i_search_for_a_provider_with_the_criteria(DataTable arg1) {

   }

   @Then("^the results should include only provider Smith$")
   public void the_results_should_include_only_provider_Smith() {

   }
}
