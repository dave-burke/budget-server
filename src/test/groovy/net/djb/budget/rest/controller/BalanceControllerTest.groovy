package net.djb.budget.rest.controller;

import net.djb.budget.rest.service.BalanceService;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spock.lang.Specification;
import spock.lang.Unroll;
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

	@Unroll("GET #url")
	def "GET url"() {
		expect:
		MvcResult result = mockMvc.perform(get(url))
				.andExpect(status().isOk())
				.andReturn();
		where:
		url | _
		"/balance" | _
		"/balance/account" | _
		"/balance/account:name" | _
		"/balance/account?asOf=2015-01-01T00:00" | _
		"/balance?asOf=2015-01-01T00:00" | _
		"/balances" | _
		"/balances/account" | _
		"/balances/account?asOf=2015-01-01T00:00" | _
		"/balances?asOf=2015-01-01T00:00" | _
	}

}

