import java.util.List;
import java.util.Vector;

public class Gossip extends ProtocolMessage {
	protected List<Update> lstUpdates = new Vector<Update>();
	
	public Gossip(List<Update> updates) {
		lstUpdates = updates;
	}
	
	public List<Update> getUpdates() {
		return lstUpdates;
	}
}
