package net.djb.budget.rest.controller;

import net.djb.budget.rest.service.BalanceService;

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

class BalanceControllerTest extends Specification {

	BalanceController controller;
	BalanceService balanceService;
	MockMvc mockMvc;

	def setup(){
		controller = new BalanceController();

		balanceService = Mock(BalanceService);
		controller.balanceService = balanceService;

		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	def "GET /balance"() {
		expect:
		MvcResult result = mockMvc.perform(get("/balance"))
				.andExpect(status().isOk())
				.andReturn();
	}

	def "GET /balance/account"() {
		expect:
		MvcResult result = mockMvc.perform(get("/balance/account"))
				.andExpect(status().isOk())
				.andReturn();
	}

	def "GET /balance/account.name"() {
		expect:
		MvcResult result = mockMvc.perform(get("/balance/account.name"))
				.andExpect(status().isOk())
				.andReturn();
	}

	def "GET /balances/account?asOf=2015-01-01T00:00"() {
		expect:
		MvcResult result = mockMvc.perform(get("/balances/account?asOf=2015-01-01T00:00"))
				.andExpect(status().isOk())
				.andReturn();
	}

	def "GET /balance/account?asOf=2015-01-01T00:00"() {
		expect:
		MvcResult result = mockMvc.perform(get("/balance/account?asOf=2015-01-01T00:00"))
				.andExpect(status().isOk())
				.andReturn();
	}

	def "GET /balances/account"() {
		expect:
		MvcResult result = mockMvc.perform(get("/balances/account"))
				.andExpect(status().isOk())
				.andReturn();
	}

	def "GET /balances?asOf=2015-01-01T00:00"() {
		expect:
		MvcResult result = mockMvc.perform(get("/balances?asOf=2015-01-01T00:00"))
				.andExpect(status().isOk())
				.andReturn();
	}

	def "GET /balance?asOf=2015-01-01T00:00"() {
		expect:
		MvcResult result = mockMvc.perform(get("/balance?asOf=2015-01-01T00:00"))
				.andExpect(status().isOk())
				.andReturn();
	}

	def "GET /balances"() {
		expect:
		MvcResult result = mockMvc.perform(get("/balances"))
				.andExpect(status().isOk())
				.andReturn();
	}
}

