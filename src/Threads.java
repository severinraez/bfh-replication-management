import java.util.List;
import java.util.Vector;


public class Threads {
	protected List<Thread> rootThreads = new Vector<Thread>();
	
	public void addMessage(Message msg) throws Exception {
		Thread t = new Thread(msg);
		if(msg.isAnswer()) {
			Thread parent = null;
			for(Thread candidate : rootThreads) {				
				parent = candidate.find(msg.getAnswerToId());
				if(parent != null) {
					parent.addChild(t);					
					break;
				}							
			}			
			throw new Exception("could not find the thread " + msg.getAnswerToId());
		}
		else {
			rootThreads.add(t);
		}
	}
}
