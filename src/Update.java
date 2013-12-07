
public class Update {
	protected int iAuthor = 0;
	protected int iAnswerTo = 0;
	protected String strTitle = "";
	
	public Update(int iAuthor, int iAnswerTo, String strTitle) {
		this.iAuthor = iAuthor;
		this.iAnswerTo = iAnswerTo;
		this.strTitle = strTitle;
	}
	
	public Update(int iAuthor, String strTitle) {
		this(iAuthor, 0, strTitle);
	}
	
	public String toString() {
		String str = "" + iAuthor;
		if(this.iAnswerTo != 0)
			str += " (answering to message " + iAnswerTo + ")";
		str = str + ": \"" + strTitle + "\"";
		return str;
 	}
}
