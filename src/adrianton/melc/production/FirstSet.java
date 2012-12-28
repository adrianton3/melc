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

import java.util.HashSet;

public class FirstSet {
	private final HashSet<FirstEl> set;
	
	FirstSet(HashSet<FirstEl> set) {
		this.set = set;
	}
	
	FirstSet union(FirstSet that) {
		HashSet<FirstEl> retSet = new HashSet<FirstEl>();
		
		retSet.addAll(set);
		retSet.addAll(that.set);

		return new FirstSet(retSet);
	}
	
	FirstSet concat(FirstSet that, int k) {
		HashSet<FirstEl> retSet = new HashSet<FirstEl>();
		
		for(FirstEl elThis: set)
			for(FirstEl elThat: that.set)
				retSet.add(elThis.concat(elThat, k));
		
		return new FirstSet(retSet);
	}

	int kComplete() {
		int min = 1000;
		
		for(FirstEl el: set)
			if(el.size() < min)
				min = el.size();
		
		return min;
	}
	
	boolean allComplete(int k) {
		for(FirstEl el: set)
			if(el.size() < k)
				return false;
		
		return true;
	}
	
	public static FirstSet Empty() {
		return new FirstSet(new HashSet<FirstEl>());
	}
	
	public static FirstSet get(FirstEl el) {
		HashSet<FirstEl> tmp = new HashSet<FirstEl>();
		tmp.add(el);
		return new FirstSet(tmp);
	}
	
	public String toCode(String name) {
		String ret = "RToken[][] " + name + " = ";
		
		String list = "";
		for(FirstEl el: set)
			list += ", " + el.toCode();
		
		ret = ret + "{" + list.substring(2) + "};\n";
		return ret;
	}
	
	public String toString() {
		String ret = ""; //TODO: use StringBuilder
		
		for(FirstEl el: set)
			ret += ", " + el;
		
		ret = "{" + ret.substring(2) + "}";
		
		return ret;
	}
}