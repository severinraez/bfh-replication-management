import java.util.SortedMap;
import java.util.TreeMap;

public class ReplicationManager extends Node {
	protected static String strType = "RM";
	protected SortedMap<TimeStamp, Message> mpUpdateLog = new TreeMap<TimeStamp, Message>();
	protected TimeStamp tsValue = new TimeStamp();
	protected TimeStamp tsReplica = new TimeStamp();
	protected Threads threads = new Threads();

	public ReplicationManager(int rank, int[] is) {
		super(rank);
		log("initialized");
	}

	public void work() {	
	}
}
