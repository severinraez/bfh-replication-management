import java.util.SortedMap;
import java.util.TreeMap;

import p2pmpi.mpi.MPI;
import p2pmpi.mpi.Request;

public class ReplicationManager extends Node {
	protected static String strType = "RM";
	protected SortedMap<TimeStamp, Message> mpUpdateLog = new TreeMap<TimeStamp, Message>();
	protected TimeStamp tsValue = new TimeStamp();
	protected TimeStamp tsReplica = new TimeStamp();
	protected Threads threads = new Threads();
	
	protected Request reqGossip, reqQuery, reqUpdate;
	
	//receive buffers
	protected Gossip rcvGossip[] = new Gossip[1];
	protected Query rcvQuery[] = new Query[1];
	protected Update rcvUpdate[] = new Update[1];
	

	public ReplicationManager(int rank, int[] is) {
		super(rank);
		log("initialized");
	}

	public void work() {
		initRequests();
		while(true) {
			checkNetwork();
			processUpdates();
			answerQueries();
		}
	}
	
	private void initRequests() {
		initGossipRequest();
		initQueryRequest();
		initUpdateRequest();		
	}
	
	private void initGossipRequest() {
		reqGossip = MPI.COMM_WORLD.Irecv(rcvGossip, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, ProtocolMessage.GOSSIP);		
	}

	private void initQueryRequest() {
		reqQuery = MPI.COMM_WORLD.Irecv(rcvQuery, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, ProtocolMessage.QUERY);
	}

	private void initUpdateRequest() {
		reqUpdate = MPI.COMM_WORLD.Irecv(rcvUpdate, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, ProtocolMessage.UPDATE);
	}


	private void checkNetwork() {
		if(reqGossip.Test() != null) {
			Gossip g = rcvGossip[0];
			
			initGossipRequest();
		}
		if(reqQuery.Test() != null) {
			Query q = rcvQuery[0];
			
			initQueryRequest();
		}

		if(reqUpdate.Test() != null) {
			Update u = rcvUpdate[0];
			
			initUpdateRequest();
		}
	}

	private void processUpdates() {
	}

	private void answerQueries() {
	}
}
