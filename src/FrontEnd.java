import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import p2pmpi.mpi.MPI;

public class FrontEnd extends Node {
	protected Set<Integer> iKnownMessages = new TreeSet<Integer>();

	protected TimeStamp ts = new TimeStamp();
	protected Random rand = new Random();

	protected QueryResponse queryRcvBuf[] = new QueryResponse[1];
	protected boolean bWaitingForResponse = false;
	protected int iIdCounter = 0;

	public FrontEnd(int rank, int[] frontEndIds, int[] replicationManagerIds) {
		super(rank, "FE", frontEndIds, replicationManagerIds);
		iKnownMessages.add(0);
	}

	public void work() {
		while (true) {
			query();
			sendMessage();

			try {
				java.lang.Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

	protected void query() {
		if (Math.random() > 0.25)
			return;

		Query sndBuf[] = new Query[1];

		Query q = new Query(ts);
		q.setMessage(randomMessage());
		q.setSender(rank);

		sndBuf[0] = q;

		int who = randomNeighbour();
		log("NETWORK: Querying " + who + ": " + q);
		MPI.COMM_WORLD.Send(sndBuf, 0, 1, MPI.OBJECT, who,
				ProtocolMessage.QUERY);
		MPI.COMM_WORLD.Recv(queryRcvBuf, 0, 1, MPI.OBJECT, who,
				ProtocolMessage.QUERY_RESPONSE);
		QueryResponse r = queryRcvBuf[0];
		log("NETWORK: Received " + r + " for query " + q);

		if (r.getMessage() != null) {
			iKnownMessages.add(r.getMessage().getId());
		}
		if (r.getAnswers() != null) {
			for (Message msg : r.getAnswers()) {
				iKnownMessages.add(msg.getId());
			}
		}
		
		ts = TimeStamp.max(ts,  r.getTimeStamp());
	}

	protected void sendMessage() {
		if (Math.random() > 0.25)
			return;

		int answerToId = randomMessage(); 		
		int id = ++iIdCounter + rank * 10000;
		Message m = new Message(id, rank, answerToId,
				"Hello #" + id + " from FE " + rank + ", an answer to " + answerToId, ts);
		Update u = new Update(ts, m);

		Update sndBuf[] = new Update[1];
		sndBuf[0] = u;

		int who = randomNeighbour();
		log("NETWORK: Sending " + u + " to " + who);
		MPI.COMM_WORLD.Send(sndBuf, 0, 1, MPI.OBJECT, who,
				ProtocolMessage.UPDATE);
	}

	protected int randomMessage() {
		int index = rand.nextInt(iKnownMessages.size());
		
		int curIndex = 0;
		for (int messageId : iKnownMessages) {
			if (curIndex++ == index)
				return messageId;
		}
		return 0;
	}

	protected int randomNeighbour() {
		int index = (int) (Math.round(Math.random()
				* (replicationManagerIds.length - 1)));
		return replicationManagerIds[index];
	}

	protected void log(String msg) {
		super.log(msg + " (my ts: " + ts + ", I know " + iKnownMessages.size() + " messages)");
	}

}
