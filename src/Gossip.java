import java.util.List;
import java.util.Vector;

public class Gossip extends ProtocolMessage {
	private static final long serialVersionUID = 963051547778207777L;
	protected List<Update> lstUpdates = new Vector<Update>();
	protected TimeStamp ts;
	
	public Gossip(List<Update> updates, TimeStamp ts) {
		lstUpdates = updates;
		this.ts = ts;
	}
	
	public List<Update> getUpdates() {
		return lstUpdates;
	}
	
	public void setTimeStamp(TimeStamp ts) {
		this.ts = ts;
	}
	
	public TimeStamp getTimeStamp() {
		return ts;
	}
}
