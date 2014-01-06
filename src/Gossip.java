import java.util.Map;
import java.util.TreeMap;

public class Gossip extends ProtocolMessage {
	private static final long serialVersionUID = 963051547778207777L;
	protected Map<TimeStamp, Update> lstUpdates = new TreeMap<TimeStamp, Update>();
	protected TimeStamp ts;
	
	public Gossip(Map<TimeStamp, Update> updates, TimeStamp ts) {
		lstUpdates = updates;
		this.ts = ts;
	}
	
	public Map<TimeStamp, Update> getUpdates() {
		return lstUpdates;
	}
	
	public void setTimeStamp(TimeStamp ts) {
		this.ts = ts;
	}
	
	public TimeStamp getTimeStamp() {
		return ts;
	}
}
