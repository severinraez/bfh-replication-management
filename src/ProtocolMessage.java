import java.io.Serializable;

public class ProtocolMessage implements Serializable {
	private static final long serialVersionUID = 1710127430092980812L;
	public static final int GOSSIP = 1;
	public static final int QUERY = 2;
	public static final int UPDATE = 3;
	public static final int QUERY_RESPONSE = 4;
	public static final int UPDATE_RESPONSE = 5;
}
