import java.util.List;
import java.util.Vector;


public class Threads {
	protected List<Thread> rootThreads = new Vector<Thread>();
	
	public void addMessage(Message msg) throws Exception {
		Thread t = new Thread(msg);
		if(msg.isAnswer()) {
			Thread parent = findMessage(msg.getAnswerToId());
			if(parent != null)
				parent.addChild(t);
			else
				throw new Exception("could not find the thread " + msg.getAnswerToId());
		}
		else {
			rootThreads.add(t);
		}
	}
	
	public Thread findMessage(int id) {
		Thread result = null;
		for(Thread candidate : rootThreads) {				
			result = candidate.find(id);
			if(result != null)
				return result;		
		}
		return null;
	}
	
	public String toString() {
		String str = "";
		for(Thread t : rootThreads) {
			str += t;
		}
		return str;
	}
}
