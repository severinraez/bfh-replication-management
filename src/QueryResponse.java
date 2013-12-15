import java.util.List;


public class QueryResponse extends AtomicProtocolMessage {
	private static final long serialVersionUID = -6188515720547675082L;
	protected Message msg;
	protected List<Message> answers;

	public QueryResponse(TimeStamp ts) {
		super(ts);
	}
	
	public Message getMessage() {
		return msg;
	}
	
	public List<Message> getAnswers() {
		return answers;
	}
	
	public void setMessageAndAnswers(Message msg, List<Message> answers) {
		this.msg = msg;
		this.answers = answers;
	}
	
	public String toString() {
		return "QueryResponse @" + tsTime + " with " + answers.size() + " answers";		
	}
}
