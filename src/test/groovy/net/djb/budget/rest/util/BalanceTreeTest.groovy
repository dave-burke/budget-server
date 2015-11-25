package net.djb.budget.rest.util;

import spock.lang.Specification;

class BalanceTreeTest extends Specification {
	
	def "stringToQueue splits paths onto a queue"(){
		given:
		String path = "path:to:account";

		when:
		Queue result = new BalanceTree().stringToQueue(path);

		then:
		result.size() == 3;
	}

	def "Balances can be initialized via constructor"() {
		given:
		Map init = ["checking":1,"savings":2]

		when:
		BalanceTree balances = new BalanceTree(init);

		then:
		balances.getBalance("checking") == 1;
		balances.getBalance("savings") == 2;
		balances.getBalance() == 3;
	}

	def "addPath keeps balances up to date"() {
		given:
		BalanceTree balances = new BalanceTree();

		when:
		balances.addBalance("assets:checking:bills", 40);
		balances.addBalance("assets:checking:save", 20);
		balances.addBalance("assets:checking:misc", 10);
		balances.addBalance("assets:checking:fun", 5);
		balances.addBalance("assets:savings:save", 75);
		balances.addBalance("assets:savings:fun", 25);
		balances.addBalance("expenses:bills", 20);
		balances.addBalance("expenses:gum", 5);
		balances.addBalance("equity:init", -200);
		
		then:
		balances.getBalance() == 0;
		balances.getBalance("assets") == 175;
		balances.getBalance("assets:checking") == 75;
		balances.getBalance("assets:savings") == 100;
		balances.getBalance("assets:checking:bills") == 40;
		println balances.prettyPrint();
	}

	def "addPath can't be called on child nodes"() {
		given:
		BalanceTree root = new BalanceTree();
		root.addBalance("assets", 1);
		
		when:
		root.subaccounts[0].addBalance("checking", 1);

		then:
		thrown(UnsupportedOperationException);
		root.childNodes.get("assets").childNodes.get("checking") == null;
	}

	def "addPath will not add balances to non-leaf nodes"(){
		given:
		BalanceTree balances = new BalanceTree();
		balances.addBalance("assets:checking:bills", 1);

		when:
		balances.addBalance("assets:checking", 1);

		then:
		thrown(IllegalArgumentException)
		balances.getBalance("assets") == 1;
	}

	def "addPath will not add balances for an empty path"(){
		given:
		BalanceTree balances = new BalanceTree();

		when:
		balances.addBalance("", 1);

		then:
		thrown(IllegalArgumentException)
	}

	def "addPath will not add balances for a null path"(){
		given:
		BalanceTree balances = new BalanceTree();

		when:
		balances.addBalance(null, 1);

		then:
		thrown(IllegalArgumentException)
	}

	def "addPath will not add balances for paths containing embeded root node names"(){
		given:
		BalanceTree balances = new BalanceTree();

		when:
		balances.addBalance(BalanceTree.ROOT_NODE_NAME, 1);

		then:
		thrown(IllegalArgumentException);
	}

}

