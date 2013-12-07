public class TimeStamp implements Comparable {
	protected final static int REPLICATION_MANAGERS = 7;
	
	protected int ts[] = new int[REPLICATION_MANAGERS];

	public int getComponent(int index) {
		return ts[index];
	}
	
	public void setComponent(int index, int value) {
		ts[index] = value;
	}
	
	@Override
	public int compareTo(Object other) {
		if(other instanceof TimeStamp) {
			TimeStamp ots = (TimeStamp)other;
			boolean bBigger = true;
			boolean bEqual = true;
			for(int i = 0; i < REPLICATION_MANAGERS; i++) {
				if(ts[i] < ots.getComponent(i)) {
					bBigger = false;
					bEqual = false;
				}
				else if(ts[i] > ots.getComponent(i)) {
					bEqual = false;
				}
			}
			if(bEqual)
				return 0;
			else if(bBigger)
				return 1;
			else
				return -1;						
		}
		else {
			return 0;			
		}
	}
	
	public static TimeStamp max(TimeStamp a, TimeStamp b) {
		TimeStamp result = new TimeStamp();
		
		int iA, iB;
		for(int i = 0; i < REPLICATION_MANAGERS; i++) {
			iA = a.getComponent(i);
			iB = b.getComponent(i);
			
			result.setComponent(i, Math.max(iA, iB));
		}
		
		return result;
	}
}
