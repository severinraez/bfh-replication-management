public class TimeStamp implements Comparable<TimeStamp> {
	protected final static int REPLICATION_MANAGERS = 7;

	protected int ts[] = new int[REPLICATION_MANAGERS];
	
	public TimeStamp() {
		for(int i = 0; i < REPLICATION_MANAGERS; i++) {
			ts[i] = 0;
		}
	}
	
	public TimeStamp(TimeStamp src) {
		for(int i = 0; i < REPLICATION_MANAGERS; i++) {
			ts[i] = src.getComponent(i);
		}
	}

	public int getComponent(int index) {
		return ts[index];
	}

	public void setComponent(int index, int value) {
		ts[index] = value;
	}

	@Override
	public int compareTo(TimeStamp other) {
		boolean bBigger = true;
		boolean bEqual = true;
		for (int i = 0; i < REPLICATION_MANAGERS; i++) {
			if (ts[i] < other.getComponent(i)) {
				bBigger = false;
				bEqual = false;
			} else if (ts[i] > other.getComponent(i)) {
				bEqual = false;
			}
		}
		if (bEqual)
			return 0;
		else if (bBigger)
			return 1;
		else
			return -1;
	}

	public static TimeStamp max(TimeStamp a, TimeStamp b) {
		TimeStamp result = new TimeStamp();

		int iA, iB;
		for (int i = 0; i < REPLICATION_MANAGERS; i++) {
			iA = a.getComponent(i);
			iB = b.getComponent(i);

			result.setComponent(i, Math.max(iA, iB));
		}

		return result;
	}
	
	public String toString() {
		String str = "";
		for(int i = 0; i < REPLICATION_MANAGERS; i++) {
			if(i > 0)
				str += ", ";
			str += ts[i];
		}
		return str;
	}
}
