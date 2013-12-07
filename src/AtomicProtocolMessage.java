
public abstract class AtomicProtocolMessage extends ProtocolMessage implements Comparable<AtomicProtocolMessage> {
	protected TimeStamp tsTime;
	public TimeStamp getTimeStamp() {
		return tsTime;
	}
	
	public AtomicProtocolMessage(TimeStamp ts) {
		this.tsTime = ts;
	}
	
	public int compareTo(AtomicProtocolMessage other) {
		return tsTime.compareTo(other.getTimeStamp());
	}
}
