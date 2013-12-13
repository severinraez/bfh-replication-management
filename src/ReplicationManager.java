import java.util.Set;
import java.util.TreeSet;

import p2pmpi.mpi.MPI;
import p2pmpi.mpi.Request;
import p2pmpi.mpi.Status;

public class ReplicationManager extends Node {
	protected static String strType = "RM";
	protected Set<AtomicProtocolMessage> setWorkLog = new TreeSet<AtomicProtocolMessage>();
	protected TimeStamp tsValue = new TimeStamp();
	protected TimeStamp tsGossipped = new TimeStamp();
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
		Status s;
		s = reqGossip.Test(); 
		if(s != null) {
			Gossip g = rcvGossip[0];			
			setWorkLog.addAll(g.getUpdates());
			initGossipRequest();
		}
		s = reqQuery.Test();
		if(s != null) {
			Query q = rcvQuery[0];
			
			setWorkLog.add(q);
			initQueryRequest();
		}
		s = reqUpdate.Test();
		if(s != null) {
			Update u = rcvUpdate[0];
			setWorkLog.add(u);
			initUpdateRequest();
		}
	}

	private void processUpdates() {
		for(AtomicProtocolMessage msg : setWorkLog) {
			if(msg instanceof Update && msg.getTimeStamp().compareTo(tsValue) > 0) { //newer update
				Update u = (Update)msg;				
				
				try {
					threads.addMessage(u.getMessage());
				} catch (Exception e) {
					log("Could not add message " + u.getMessage().getId());
					e.printStackTrace();
				}						
				
				TimeStamp t = TimeStamp.max(tsValue, u.getTimeStamp());
				t.setComponent(rank, t.getComponent(rank) + 1);
				
				tsValue = t;
			}
		}
	}

	private void answerQueries() {
		Set<Query> done = new TreeSet<Query>();
		for(AtomicProtocolMessage msg : setWorkLog) {
			if(msg instanceof Query && msg.getTimeStamp().compareTo(tsValue) < 0) { //solvable query
				Query q = (Query)msg;
				
				QueryResponse r = new QueryResponse(tsValue);
				//TODO: send response
				
				done.add(q);
			}
		}
		setWorkLog.removeAll(done);
	}
}
