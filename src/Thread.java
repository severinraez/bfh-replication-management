import java.util.List;
import java.util.Vector;


public class Thread {
	protected Message msg;
	protected List<Thread> children = new Vector<Thread>();
		
	public Thread(Message msg) {
		this.msg = msg;
	}
	
	public void addChild(Thread t) {
		children.add(t);
	}
	
	public Thread find(int iMessageId) {
		if(iMessageId == msg.getId()) { //are we the searched message?
			return this;
		}
		else { //search in children
			Thread result = null;
			for(Thread child : children) {
				result = child.find(iMessageId);
				if(result != null)
					return result; //found
			}
		}
		return null; //not found
	}
}
