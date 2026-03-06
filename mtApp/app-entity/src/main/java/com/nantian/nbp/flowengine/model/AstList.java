package com.nantian.nbp.flowengine.model;

import java.util.LinkedList;

public class AstList extends LinkedList<AstModel> {
    public AstList() {
        super();
    }

    public AstList(AstList astList) {
        super(astList);
    }
}