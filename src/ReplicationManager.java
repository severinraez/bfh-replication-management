import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import p2pmpi.mpi.MPI;
import p2pmpi.mpi.Request;
import p2pmpi.mpi.Status;

public class ReplicationManager extends Node {
	protected static String strType = "RM";
	protected SortedMap<TimeStamp, Update> mapWorkLog = new TreeMap<TimeStamp, Update>();
	protected List<Query> listQueries = new LinkedList<Query>();
	protected Set<TimeStamp> doneUpdates = new TreeSet<TimeStamp>();
	//ATTENTION: (0) - TS blocks everything!
	protected Set<TimeStamp> knownMessages = new TreeSet<TimeStamp>();
	protected TimeStamp tsValue = new TimeStamp();
	protected TimeStamp tsReplica = new TimeStamp();
	protected Threads threads = new Threads();

	protected Request reqGossip, reqQuery, reqUpdate;

	// receive buffers
	protected Gossip rcvGossip[] = new Gossip[1];
	protected Query rcvQuery[] = new Query[1];
	protected Update rcvUpdate[] = new Update[1];

	public ReplicationManager(int rank, int frontEndIds[],
			int replicationManagerIds[]) {
		super(rank, "RM", frontEndIds, replicationManagerIds);
	}

	public void work() {
		initRequests();
		while (true) {
			checkNetwork();
			processUpdates();
			answerQueries();
			gossip();

			try {
				java.lang.Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}

	private void initRequests() {
		initGossipRequest();
		initQueryRequest();
		initUpdateRequest();
	}

	private void initGossipRequest() {
		reqGossip = MPI.COMM_WORLD.Irecv(rcvGossip, 0, 1, MPI.OBJECT,
				MPI.ANY_SOURCE, ProtocolMessage.GOSSIP);
	}

	private void initQueryRequest() {
		reqQuery = MPI.COMM_WORLD.Irecv(rcvQuery, 0, 1, MPI.OBJECT,
				MPI.ANY_SOURCE, ProtocolMessage.QUERY);
	}

	private void initUpdateRequest() {
		reqUpdate = MPI.COMM_WORLD.Irecv(rcvUpdate, 0, 1, MPI.OBJECT,
				MPI.ANY_SOURCE, ProtocolMessage.UPDATE);
	}

	private void checkNetwork() {
		Status s;
		s = reqGossip.Test();
		if (s != null) {
			Gossip g = rcvGossip[0];
			int added = 0;
			for (Map.Entry<TimeStamp, Update> e : g.getUpdates().entrySet()) {
				Update u = e.getValue();
				TimeStamp ts = e.getKey();
				if (!knownMessages.contains(ts)) { // unknown
																	// update
					mapWorkLog.put(ts, u);
					knownMessages.add(ts);
					added++;
				}
			}
			tsReplica = TimeStamp.max(tsReplica, g.getTimeStamp());
			// log("NETWORK: got gossip msg, added " + added + " out of " +
			// g.getUpdates().size() + " updates to work log");
			initGossipRequest();
		}
		s = reqQuery.Test();
		if (s != null) {
			log("NETWORK: got query msg");
			Query q = rcvQuery[0];
			listQueries.add(q);
			initQueryRequest();
		}
		s = reqUpdate.Test();
		if (s != null) {
			log("NETWORK: got update msg");
			Update u = rcvUpdate[0];
			tsReplica.setComponent(rank, tsReplica.getComponent(rank) + 1);

			// give the update an unique identifier
			TimeStamp ts = new TimeStamp(u.getTimeStamp());
			ts.setComponent(rank, tsReplica.getComponent(rank));
			knownMessages.add(ts);

			mapWorkLog.put(ts, u);
			initUpdateRequest();
		}
	}

	private void processUpdates() {
		for (Map.Entry<TimeStamp, Update> entry : mapWorkLog.entrySet()) {
			Update u = entry.getValue();
			TimeStamp ts = entry.getKey();
			//log("worklog: " + mapWorkLog.toString());
			//log("doneupdates: " + doneUpdates.toString());
			if (doneUpdates.contains(ts)) // don't apply updates
										  // twice
				continue;

			if (u.getTimeStamp().compareTo(tsValue) <= 0) {
				// we are ready to apply the message

				log("processing " + u);

				try {
					threads.addMessage(u.getMessage());
					log("APP: added message from " + u);
					log("APP: my data is now\n" + threads);
				} catch (Exception e) {
					log("APP: Could not add message " + u.getMessage().getId());
					e.printStackTrace();
				}

				doneUpdates.add(ts);

				// adjust our value timestamp to reflect added updates
				TimeStamp t = TimeStamp.max(tsValue, ts);
				tsValue = t;
			}
		}
	}

	private void answerQueries() {
		List<Query> done = new LinkedList<Query>();
		for (Query q : listQueries) {
			if (q.getTimeStamp().compareTo(tsValue) <= 0) { // solvable query
				QueryResponse r = new QueryResponse(tsValue);
				Thread result = threads.findMessage(q.getMessage());

				if (result != null)
					r.setMessageAndAnswers(result.getMessage(),
							result.getAnswers());

				log("answering " + q);

				QueryResponse sndBuf[] = new QueryResponse[1];
				sndBuf[0] = r;
				MPI.COMM_WORLD.Isend(sndBuf, 0, 1, MPI.OBJECT, q.getSender(),
						ProtocolMessage.QUERY_RESPONSE);

				done.add(q);
			}
		}

		// remove the answered queries from the worklog
		for (Query q : done) {
			listQueries.remove(q);
		}
	}

	private void gossip() {
		if (mapWorkLog.isEmpty())
			return;

		// ...and prepare a gossip message including them
		Gossip gossipMessage = new Gossip(mapWorkLog, tsReplica);

		for (int rank : replicationManagerIds) {
			// log("NETWORK: gossipping to " + rank);
			Gossip sndBuf[] = new Gossip[1];
			sndBuf[0] = gossipMessage;
			MPI.COMM_WORLD.Isend(sndBuf, 0, 1, MPI.OBJECT, rank,
					ProtocolMessage.GOSSIP);
		}
	}

	protected void log(String msg) {
		super.log(msg + " (my tsValue: " + tsValue + " my tsReplica: "
				+ tsReplica + " worklog size: " + mapWorkLog.size() + ")");
	}
}
