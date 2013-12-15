import java.util.Set;
import java.util.TreeSet;

import p2pmpi.mpi.MPI;

public class FrontEnd extends Node {	
	protected Set<Integer> iKnownMessages = new TreeSet<Integer>();
	
	protected TimeStamp ts;
	
	protected QueryResponse queryRcvBuf[] = new QueryResponse[1];
	protected boolean bWaitingForResponse = false; 
	protected int iIdCounter = 0;

	public FrontEnd(int rank, int[] frontEndIds, int[] replicationManagerIds) {
		super(rank, "FE", frontEndIds, replicationManagerIds);
		log("initialized");
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
		MPI.COMM_WORLD.Send(sndBuf, 0, 1, MPI.OBJECT, who, ProtocolMessage.QUERY);
		MPI.COMM_WORLD.Recv(queryRcvBuf, 0, 1, MPI.OBJECT, who, ProtocolMessage.QUERY_RESPONSE);		
		QueryResponse r = queryRcvBuf[0];
		
		iKnownMessages.add(r.getMessage().getId());
		for(Message msg : r.getAnswers()) {
			iKnownMessages.add(msg.getId());			
		}
		logResponse(r);
	}
	
	protected void sendMessage() {
		if(Math.random() > 0.25)
			return;
		
		Message m = new Message(++iIdCounter, rank, randomMessage(), "Hello from FE " + rank);
		Update u = new Update(ts, m);
		
		logUpdate(u);
		
		Update sndBuf[] = new Update[1];
		sndBuf[0] = u;		
		
		int who = randomNeighbour();
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
		int index = (int)(Math.round(Math.random() * frontEndIds.length-1));
		return frontEndIds[index];
	}
	
	protected void logUpdate(Update u) {
		String str = "";
		str += "Sending update with ts " + u.getTimeStamp() + " and message " + u.getMessage().getId();
		System.out.println(str);
	}
	
	protected void logResponse(QueryResponse r) {
		String str = "";
		str += "Received QueryResponse with ts " + r.getTimeStamp() + " and " + r.getAnswers().size() + " answers";
		System.out.println(str);

	}
}
