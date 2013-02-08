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
import java.util.HashSet;
import java.util.Set;

import adrianton.melc.Statics;

public class SwitchProd implements Production {
	private final String name;
	private ArrayList<Production> prod = new ArrayList<Production>();
	
	public SwitchProd(String name) {
		this.name = name;
	}
	
	@Override
	public void add(Production p) {
		prod.add(p);
	}
	
	@Override
	public boolean isRecursive() {
		try { walk(new HashSet<String>(), new HashSet<String>(), new HashSet<String>()); }
		catch(AlreadyBeenHereException ex) { return true; }
		return false;
	}
	
	@Override
	public void walk(Set<String> visited, Set<String> isNull, Set<String> isNotNull) throws AlreadyBeenHereException {
		if(visited.contains(name)) throw new AlreadyBeenHereException();
		visited.add(name);
		
		boolean imNull = false;
		
		for(Production p: prod) {
			p.walk(visited, isNull, isNotNull);
			if(isNull.contains(p.getName())) imNull = true;
		}
		
		if(imNull) isNull.add(name); 
		else isNotNull.add(name);
	}
	
	@Override
	public FirstSet first(int k) {
		FirstSet ret = FirstSet.Empty();
		
		for(Production p: prod) {
			ret = ret.union(p.first(k));
			//we need to verify if their intersection contains something more interesting than ("")	
			//if(!FirstSet.disjoint(oRet, pfs, ret)) throw new FirstSetConflictException();
		}
		
		return ret;
	}

	public String getProcCode() {
		String ret = "Node " + getName() +"() {\n";

		Production firstProd = prod.get(0);
		
		if(firstProd instanceof Token)
			ret += 
					" if(match(" + firstProd.getMatchCode() + ")) " +
					"return next();\n";
		else
			ret += 
					" if(match(" + firstProd.getMatchCode() + ")) " +
					"return " + firstProd.getName() + "();\n";
		
		for(int i=1;i<prod.size();i++) {
			Production nowProd = prod.get(i);
			
			if(nowProd instanceof Token)
				ret += 
						" else if(match(" + nowProd.getMatchCode() + ")) " +
						"return next();\n";
			else
				ret += 
						" else if(match(" + nowProd.getMatchCode() + ")) "+
						"return " + nowProd.getName() + "();\n";
		}

		ret += 
				" else throw new RuntimeException(\"" + getName() + ": parsing error\");\n" + 
				"}";
		
		return ret;
	}
	
	public String getStubCode() {
		StringBuilder ret = new StringBuilder(); 
		
		ret.append("class " + Statics.capitalize(getName()) + " implements Node {\n");
		ret.append(" final Node node;\n\n");
		//constructor
		ret.append(
				" " + Statics.capitalize(getName()) + 
				"(Node node) {\n");
		ret.append("  this.node = node;\n"); //constructor body
		ret.append(" }\n\n"); //constructor
		
		//toString
		ret.append(" public String toString() {\n");
		ret.append("  return node.toString();\n");
		ret.append(" }\n"); //toString
		
		ret.append("}"); //class
		
		return ret.toString();
	}
	
	public String getProcNameCode() {
		return name; 
	}
	
	@Override 
	public String getMatchCode() {
		return name + "FirstSet";
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		String ret = name + " -s> "; //TODO: use StringBuilder
		
		ret += prod.get(0).toStr();
		for(int i=1;i<prod.size();i++)
			ret += " | " + prod.get(i).toStr();
		
		return ret;
	}
	
	public String toStr() {
		return name;
	}
}
