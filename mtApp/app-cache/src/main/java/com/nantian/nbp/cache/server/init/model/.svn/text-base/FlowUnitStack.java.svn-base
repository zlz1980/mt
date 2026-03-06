/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.cache.server.init.model;

import com.nantian.nbp.flowengine.model.AstModel;

import java.util.LinkedList;

public class FlowUnitStack {
	
	private final LinkedList<AstModel> stackList = new LinkedList<>();
	private int size=0;
	private final LinkedList<Boolean> matchList = new LinkedList<>();
	
	public void push(AstModel model){
		stackList.push(model);
		matchList.push(false);
		size++;
	}
	
	public AstModel pop(){
		size--;
		matchList.pop();
		return stackList.pop();
	}
	
	public boolean isEmpty(){
        return size <= 0;
	} 
	
	public boolean isLastMatch(){
        return matchList.getFirst();
	}
	
	public void setLastMatch(Boolean flag){
		matchList.set(0, flag);
	}

}
