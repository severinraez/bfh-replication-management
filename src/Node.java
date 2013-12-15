
public abstract class Node {
	protected static String strType = null;
	protected int rank = 0;
	protected int frontEndIds[];
	protected int replicationManagerIds[];
	
	public Node(int rank, String type, int [] frontEndIds, int [] replicationManagerIds) {
		this.rank = rank;
		strType = type;
		this.frontEndIds = frontEndIds;
		this.replicationManagerIds = replicationManagerIds;
	}

	protected void log(String msg) {
		System.out.println(strType + " " + rank + ": " + msg);
	}
	
	public abstract void work();
}
