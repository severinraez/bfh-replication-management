
public abstract class Node {
	protected static String strType = null;
	protected static int rank = 0;
	
	public Node(int rank) {
		this.rank = rank;
	}

	protected void log(String msg) {
		System.out.println(strType + " " + rank + ": " + msg);
	}
	
	public abstract void work();
}
