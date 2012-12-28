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

package adrianton.melc.production;

import java.util.ArrayList;
import java.util.List;

public class FirstEl {
	private final ArrayList<Token> list;
	
	FirstEl(ArrayList<Token> list) {
		this.list = list;
	}
	
	FirstEl concat(FirstEl that, int k) {
		if(list.size() >= k) return this;
		
		List<Token> toAdd = that.list.subList(0, Math.min(that.list.size(), k - list.size()));
		ArrayList<Token> arRet = new ArrayList<Token>();
		arRet.addAll(list);
		arRet.addAll(toAdd);
		
		return new FirstEl(arRet);
	}
	
	boolean match(ArrayList<Token> string, int index) {
		for(int i=0;i<list.size();i++)
			if(!string.get(index + i).match(list.get(i)))
				return false;
				
		return true;
	}
	
	public boolean equals(Object that) {
		if(that instanceof FirstEl) {
			FirstEl conv = (FirstEl)that;
			return list.equals(conv.list);
		}
		return false;
	}
	
	public int hashCode() {
		return list.hashCode();
	}

	public int size() {
		return list.size();
	}
	
	static FirstEl Empty() {
		return new FirstEl(new ArrayList<Token>());
	}
	
	static FirstEl get(Token token) {
		ArrayList<Token> tmp = new ArrayList<Token>();
		tmp.add(token);
		return new FirstEl(tmp);
	}
	
	public String toCode() {
		String ret = ""; //TODO: use StringBuilder

		if(!list.isEmpty()) {
			ret += list.get(0).toNewCode();
			for(int i = 1; i < list.size(); i++)
				ret += ", " + list.get(i).toNewCode();
		}
		
		ret = "{" + ret + "}";
		
		return ret;
	}
	
	public String toString() {
		String ret = ""; //TODO: use StringBuilder
		
		for(Token t: list)
			ret += " " + t;
		
		ret = "(" + ret.substring(1) + ")";
		return ret;
	}
}