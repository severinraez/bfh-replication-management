
public class Query extends AtomicProtocolMessage {
	protected int iSender;

	public Query(TimeStamp ts) {
		super(ts);
	}

	public int getSender() {
		return iSender;
	}

	public void setSender(int iSender) {
		this.iSender = iSender;
	}
}
