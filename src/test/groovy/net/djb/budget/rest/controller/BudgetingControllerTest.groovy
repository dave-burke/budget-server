package net.djb.budget.rest.controller;

import net.djb.budget.rest.data.repo.*;
import net.djb.budget.rest.service.*;
import net.djb.budget.rest.data.schema.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spock.lang.Specification;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class BudgetingControllerTest extends Specification {

	BudgetingController controller;
	BudgetingService budgetingService;
	MockMvc mockMvc;

	def setup(){
		controller = new BudgetingController();

		budgetingService = Mock(BudgetingService);
		controller.budgetingService = budgetingService;

		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	def "GET /expenses"() {
		when:
		MvcResult result = mockMvc.perform(get("/expenses"))
				.andExpect(status().isOk())
				.andReturn();
		then:
		1 * budgetingService.listFixedExpenses({it != null});
	}

	def "GET /income"() {
		when:
		MvcResult result = mockMvc.perform(get("/income"))
				.andExpect(status().isOk())
				.andReturn();
		then:
		1 * budgetingService.listFixedIncome({it != null});
	}
	def "GET /income/1/budget"() {
		given:
		def id = 1;

		when:
		MvcResult result = mockMvc.perform(get("/income/${id}/budget"))
				.andExpect(status().isOk())
				.andReturn();
		then:
		1 * budgetingService.buildBudget(id, {it != null});
	}

}

