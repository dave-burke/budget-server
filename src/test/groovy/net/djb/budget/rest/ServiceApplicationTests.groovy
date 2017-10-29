package net.djb.budget.rest

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner)
@SpringBootTest
class ServiceApplicationTests {

	@Test
	void contextLoads() {}

	//@Test
	//void calcBalances() {
		//Map<String, Long> results = balances.calcBalances(LocalDateTime.now())

		//assertNotNull(results)

		//results.each { (k, v) ->
			//System.out.println(k + ": " + v);
		//});

		//assertEquals(new Long(0L), balances.calcBalance(LocalDateTime.now()));
		//assertEquals(new Long(75L), balances.calcBalance("assets:checking", LocalDateTime.now()));
		//assertEquals(5, balances.calcBalances("assets:checking", LocalDateTime.now()).size());
	//}

}
