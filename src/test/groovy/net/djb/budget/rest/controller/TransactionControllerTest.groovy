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
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class TransactionControllerTest extends Specification {

	TransactionController controller;
	TransactionService transactionService;
	MockMvc mockMvc;

	def setup(){
		controller = new TransactionController();

		transactionService = Mock(TransactionService);
		controller.transactionService = transactionService;

		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	def "GET /transactions"() {
		expect:
		MvcResult result = mockMvc.perform(get("/transactions"))
				.andExpect(status().isOk())
				.andReturn();
	}

	def "POST /transactions"() {
		setup:
		byte[] content = new ObjectMapper().writeValueAsBytes(new Transaction());

		expect:
		MvcResult result = mockMvc.perform(post("/transactions")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content))
				.andExpect(status().isCreated())
				.andReturn();
	}

	def "PUT /transactions/1"() {
		setup:
		byte[] content = new ObjectMapper().writeValueAsBytes(new Transaction());

		expect:
		MvcResult result = mockMvc.perform(put("/transactions/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content))
				.andExpect(status().isOk())
				.andReturn();
	}

	def "DELETE /transactions/1"() {
		expect:
		MvcResult result = mockMvc.perform(delete("/transactions/1"))
				.andExpect(status().isOk())
				.andReturn();
	}

}

