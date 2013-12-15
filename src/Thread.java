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
	
	public Message getMessage() {
		return msg;
	}
	
	public List<Message> getAnswers() {
		List<Message> result = new Vector<Message>();
		for(Thread t : children) {
			result.add(t.getMessage());			
		}
		return result;
	}
	
	public String toString() {
		return _toString(0);
	}
	
	protected String _toString(int level) {
		String str = "";	
		for(int i = 0; i < level; i++) {
			str += " ";
		}
		str += msg + "\n";
	    for(Thread t : children) {
	    	t._toString(level + 4);
	    }
	    return str;
	}
}
