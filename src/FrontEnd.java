import java.util.Set;
import java.util.TreeSet;

import p2pmpi.mpi.MPI;

public class FrontEnd extends Node {	
	protected Set<Integer> iKnownMessages = new TreeSet<Integer>();
	
	protected TimeStamp ts = new TimeStamp();
	
	protected QueryResponse queryRcvBuf[] = new QueryResponse[1];
	protected boolean bWaitingForResponse = false; 
	protected int iIdCounter = 0;

	public FrontEnd(int rank, int[] frontEndIds, int[] replicationManagerIds) {
		super(rank, "FE", frontEndIds, replicationManagerIds);
		iKnownMessages.add(0);
	}

	public void work() {
		while(true) {
			query();
			sendMessage();
			
			try {
				java.lang.Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
	
	protected void query() {
		if(Math.random() > 0.25)
			return;
					
		Query sndBuf[] = new Query[1];
			
		Query q = new Query(ts);
		q.setMessage(randomMessage());			
		q.setSender(rank);
			
		sndBuf[0] = q;
			
		int who = randomNeighbour();
		log("NETWORK: Querying " + who + ": " + q);
		MPI.COMM_WORLD.Send(sndBuf, 0, 1, MPI.OBJECT, who, ProtocolMessage.QUERY);
		MPI.COMM_WORLD.Recv(queryRcvBuf, 0, 1, MPI.OBJECT, who, ProtocolMessage.QUERY_RESPONSE);		
		QueryResponse r = queryRcvBuf[0];
		log("NETWORK: Received " + r + " for query " + q);
		
		iKnownMessages.add(r.getMessage().getId());
		for(Message msg : r.getAnswers()) {
			iKnownMessages.add(msg.getId());			
		}
	}
	
	protected void sendMessage() {
		if(Math.random() > 0.25)
			return;
		
		Message m = new Message(++iIdCounter, rank, randomMessage(), "Hello from FE " + rank);
		Update u = new Update(ts, m);
			
		Update sndBuf[] = new Update[1];
		sndBuf[0] = u;		
		
		int who = randomNeighbour();
		log("NETWORK: Sending " + u + " to " + who);
		MPI.COMM_WORLD.Send(sndBuf, 0, 1, MPI.OBJECT, who, ProtocolMessage.UPDATE);
	}
	
	protected int randomMessage() {
		int index = (int)(Math.round(Math.random() * iKnownMessages.size()));;
		int curIndex = 0;
		for(int messageId : iKnownMessages) {
			if(curIndex++ == index)
				return messageId;
		}
		return 0;
	}
	
	protected int randomNeighbour() {
		int index = (int)(Math.round(Math.random() * (replicationManagerIds.length-1)));
		return replicationManagerIds[index];
	}
	
	protected void log(String msg) {
		super.log(msg + " (my ts: " + ts + ")");
	}

}
