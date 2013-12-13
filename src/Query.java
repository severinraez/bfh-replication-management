
public class Query extends AtomicProtocolMessage {
	protected int iSender;
	protected int iMessage;

	public Query(TimeStamp ts) {
		super(ts);
	}

	public int getSender() {
		return iSender;
	}

	public void setSender(int iSender) {
		this.iSender = iSender;
	}

	public int getMessage() {
		return iMessage;
	}

	public void setMessage(int iMessage) {
		this.iMessage = iMessage;
	}
}
