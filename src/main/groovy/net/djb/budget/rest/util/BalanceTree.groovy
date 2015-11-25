package net.djb.budget.rest.util;

class BalanceTree {

	private static final String ROOT_NODE_NAME = "_checksum";
	private static final String ACCOUNT_PATH_SEPARATOR = ":";
	
	String name;
	Long balance = 0;
	private Map<String, BalanceTree> childNodes = new HashMap<>();

	public BalanceTree(){
		this.name = ROOT_NODE_NAME;
	}

	public BalanceTree(Map<String, Long> balances){
		this();
		balances.each { path, balance ->
			this.addBalance(path, balance);
		}
	}

	private BalanceTree(String name){
		this.name = name;
	}

	public void addBalance(String path, balance){
		if(this.name != ROOT_NODE_NAME){
			throw new UnsupportedOperationException("Paths must only be added to the root");
		}
		if(path == null){
			throw new IllegalArgumentException("Path cannot be null");
		}
		if(path == ""){
			throw new IllegalArgumentException("Path cannot be empty");
		}
		if(path.contains(ROOT_NODE_NAME)){
			throw new IllegalArgumentException("${ROOT_NODE_NAME} may not be used as part of the path");
		}
		addBalanceWithQueue(stringToQueue(path), balance);
	}

	private void addBalanceWithQueue(Queue path, balance){
		String nextName = path.poll();
		if(nextName == null){
			if(childNodes.size() > 0){
				throw new IllegalArgumentException("Path must refer to a leaf node");
			}
		} else {
			BalanceTree child = childNodes.get(nextName);
			if(child == null){
				child = new BalanceTree(nextName);
				childNodes.put(nextName, child);
			}
			child.addBalanceWithQueue(path, balance);
		}
		this.balance += balance;
	}

	public Long getBalance(String path){
		getBalanceByQueue(stringToQueue(path));
	}

	private Long getBalanceByQueue(Queue path){
		String nextName = path.poll();
		if(nextName == null){
			return this.balance;
		} else {
			return childNodes.get(nextName).getBalanceByQueue(path);
		}
	}

	private Queue stringToQueue(String path){
		return path.split(ACCOUNT_PATH_SEPARATOR).collect(new LinkedList(), {it});
	}

	public Map<String, BalanceTree> getSubaccounts(){
		return Collections.unmodifiableMap(childNodes);
	}

	public String prettyPrint(){
		return prettyPrintIndented(0);
	}

	private String prettyPrintIndented(int depth){
		String margin = "\t" * depth;
		String header = "${margin}${name}: ${balance}";
		String subtree = childNodes.values().collect { it.prettyPrintIndented(depth + 1) }.join();
		return "${header}\n${subtree}"
	}

}

