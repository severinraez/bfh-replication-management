
public class Update extends AtomicProtocolMessage {	
	private static final long serialVersionUID = -4742469755126081830L;
	protected Message msgData;

	public Update(TimeStamp ts, Message data) {
		super(ts);
		msgData = data;
	}
	
	public Message getMessage() {
		return msgData;
	}
	
	public String toString() {
		String desc = "update @" + tsTime + ", msg " + msgData.getId();
		if(msgData.getAnswerToId() != 0) {
			desc += " answer to " + msgData.getAnswerToId(); 
		}
		return desc;
	}
}
