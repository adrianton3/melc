/*
 * Copyright 2012 Adrian Toncean
 * 
 * This file is part of Melc.
 *
 * Melc is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Melc is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Melc. If not, see <http://www.gnu.org/licenses/>.
 */

package adrianton.melc.testparser;

import java.util.ArrayList;

public abstract class ASTBuilderBase {
	private final ArrayList<RToken> token;
	private int pointer;
	protected RToken cur;
	
	ASTBuilderBase(ArrayList<RToken> token) {
		this.token = token;
		pointer = -1;
		adv();
	}
	
	abstract Node getTree();
	
	final protected boolean match(String s) {
		return cur.match(s);
	}
	
	final protected boolean match(RToken t) {
		return cur.match(t);
	}
	
	final protected boolean match(RToken[] t) {
		for(int i=0;i<t.length;i++)
			if(!token.get(pointer + i).match(t[i])) return false;
		return true;
	}
	
	final protected boolean match(RToken[][] t) {
		for(int i=0;i<t.length;i++)
			if(match(t[i])) return true;
		return false;
	}
	
	final protected void expect(String s) {
		if(cur.match(s)) adv();
		else throw new RuntimeException("Token did not meet expectations");
	}
	
	final private void adv() {
		if(pointer >= token.size()) throw new RuntimeException("No more tokens to process");
		
		pointer++;
		cur = token.get(pointer);
	}
	
	final protected RToken next() {
		adv();
		return token.get(pointer-1);
	}
	
	Node _id() {
		//TODO: expect identifier
		adv();
		return new Id(((RTermId)cur).cont);
	}
	
	Node _num() {
		//TODO: expect number
		adv();
		return new Num(((RTermNum)cur).cont);
	}
}

class Id implements Node {
	public final String cont;
	
	Id(String cont) {
		this.cont = cont;
	}
}

class Num implements Node {
	public final String cont;
	
	Num(String cont) {
		this.cont = cont;
	}
}