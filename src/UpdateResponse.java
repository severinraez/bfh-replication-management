
public class UpdateResponse extends ProtocolMessage {
	private static final long serialVersionUID = 521362456659584407L;

	protected TimeStamp ts;
	public UpdateResponse(TimeStamp ts) {
		super();
		
		this.ts = ts;
	}
}
