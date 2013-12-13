import java.util.List;

import p2pmpi.mpi.MPI;

public class FrontEnd extends Node {
	protected static String strType = "FE";
	protected List<Integer> iKnownMessages;
	
	protected TimeStamp ts;
	
	protected QueryResponse queryRcvBuf[] = new QueryResponse[1];
	protected boolean bWaitingForResponse = false; 
	protected int iIdCounter = 0;

	public FrontEnd(int rank, int[] is) {
		super(rank);
		log("initialized");
	}

	public void work() {
		while(true) {
			query();
			sendMessage();			
		}
	}
	
	protected void query() {
		//TODO: randomize
			
		Query sndBuf[] = new Query[1];
			
		Query q = new Query(ts);
		q.setMessage(randomMessage());			
		q.setSender(rank);
			
		sndBuf[0] = q;
			
		int who = randomNeighbour();
		MPI.COMM_WORLD.Send(sndBuf, 0, 1, MPI.OBJECT, who, ProtocolMessage.QUERY);
		MPI.COMM_WORLD.Recv(queryRcvBuf, 0, 1, MPI.OBJECT, who, ProtocolMessage.QUERY_RESPONSE);		
		QueryResponse r = queryRcvBuf[0];
		
		//TODO: add ids of known messages, display
	}
	
	protected void sendMessage() {
		//TODO: randomize
		Message m = new Message(++iIdCounter, rank, randomMessage(), "Hello from FE " + rank);
		Update u = new Update(ts, m);
		
		Update sndBuf[] = new Update[1];
		sndBuf[0] = u;		
		
		int who = randomNeighbour();
		MPI.COMM_WORLD.Send(sndBuf, 0, 1, MPI.OBJECT, who, ProtocolMessage.UPDATE);
	}
	
	protected int randomMessage() {
		return 0;
	}
	
	protected int randomNeighbour() {
		return 0;
	}
}
