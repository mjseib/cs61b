package list;

public class LockDListNode extends DListNode {

    protected boolean isLocked;

    LockDListNode(Object i, DListNode prev, DListNode next) {
	super(i, prev, next);
	isLocked = false;
    }
}