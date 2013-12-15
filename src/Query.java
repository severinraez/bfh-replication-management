
public class Query extends AtomicProtocolMessage {
	private static final long serialVersionUID = -7956088787462804951L;
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
	
	public String toString() {
		return "query @" + tsTime + " msg " + iMessage + ", sender " + iSender;
	}
}
