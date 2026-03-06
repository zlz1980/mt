package org.jts.pkg;

public class PkgErrRes {
    private PkgResHead head;
    private PkgErr error;

    public PkgResHead getHead() {
        return head;
    }

    public void setHead(PkgResHead head) {
        this.head = head;
    }

    public PkgErr getError() {
        return error;
    }

    public void setError(PkgErr error) {
        this.error = error;
    }
}
