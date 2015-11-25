package net.djb.budget.rest;

import java.util.*;
import java.time.*;
import net.djb.budget.rest.data.repo.BalanceRepository;
import net.djb.budget.rest.data.schema.*;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServiceApplication.class)
public class ServiceApplicationTests {

	@Autowired BalanceRepository balances;

	@Test
	public void contextLoads() {
	}

	@Test
	public void calcBalances() {
		Map<String, Long> results = balances.calcBalances(LocalDateTime.now());

		assertNotNull(results);

		results.forEach((k, v) -> {
			System.out.println(k + ": " + v);
		});

		assertEquals(new Long(0L), balances.calcBalance(LocalDateTime.now()));
		assertEquals(new Long(75L), balances.calcBalance("assets/checking", LocalDateTime.now()));
		assertEquals(5, balances.calcBalances("assets/checking", LocalDateTime.now()).size());
	}

}
