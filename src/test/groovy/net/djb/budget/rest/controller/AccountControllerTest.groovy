package net.djb.budget.rest.controller;

import net.djb.budget.rest.service.AccountService;

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

class AccountControllerTest extends Specification {

	AccountController controller;
	AccountService accountService;
	MockMvc mockMvc;

	def setup(){
		controller = new AccountController();

		accountService = Mock(AccountService);
		controller.accountService = accountService;

		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	def "GET /accounts"() {
		expect:
		MvcResult result = mockMvc.perform(get("/accounts"))
				.andExpect(status().isOk())
				.andReturn();
	}

}

