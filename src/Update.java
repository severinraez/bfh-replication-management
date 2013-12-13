
public class Update extends AtomicProtocolMessage {	
	protected Message msgData;
	
	public Update(TimeStamp ts, Message data) {
		super(ts);
		msgData = data;
	}
	
	public Message getMessage() {
		return msgData;
	}
}
