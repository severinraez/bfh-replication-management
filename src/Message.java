import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 8767794265934522414L;
	protected int iId = 0;
	protected int iAuthor = 0;
	protected int iAnswerTo = 0;
	protected String strTitle = "";
	protected TimeStamp tsPrev;
	
	public Message(int iId, int iAuthor, int iAnswerTo, String strTitle, TimeStamp tsPrev) {
		this.iId = iId;
		this.iAuthor = iAuthor;
		this.iAnswerTo = iAnswerTo;
		this.strTitle = strTitle;
		this.tsPrev = tsPrev;
	}
	
	public Message(int iId, int iAuthor, String strTitle, TimeStamp tsPrev) {		
		this(iId, iAuthor, 0, strTitle, tsPrev);
	}
	
	public String toString() {
		String str = "" + iAuthor;
		if(this.iAnswerTo != 0)
			str += " (answering to message " + iAnswerTo + ")";
		str = str + ": \"" + strTitle + "\"";
		return str;
 	}
	
	public boolean isAnswer() {
		return iAnswerTo != 0;
	}
	
	public int getId() {
		return iId;
	}
	
	public int getAnswerToId() {
		return iAnswerTo;
	}
	
	public TimeStamp getTimeStamp() {
		return tsPrev;
	}
}
