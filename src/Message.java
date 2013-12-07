
public class Message {
	protected int iId = 0;
	protected int iAuthor = 0;
	protected int iAnswerTo = 0;
	protected String strTitle = "";
	
	public Message(int iId, int iAuthor, int iAnswerTo, String strTitle) {
		this.iId = iId;
		this.iAuthor = iAuthor;
		this.iAnswerTo = iAnswerTo;
		this.strTitle = strTitle;
	}
	
	public Message(int iId, int iAuthor, String strTitle) {		
		this(iId, iAuthor, 0, strTitle);
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
}
