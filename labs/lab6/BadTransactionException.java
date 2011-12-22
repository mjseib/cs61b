/* BadTransactionException.java */

public class BadTransactionException extends Exception {
    
    public int transAmt;

    /**
     *  Creates an exception object for a bad transaction number
     **/
    public BadTransactionException(int badTransAmt) {
	super("Invalid transaction amt: " + badTransAmt);
	
	transAmt = badTransAmt;
    }
}