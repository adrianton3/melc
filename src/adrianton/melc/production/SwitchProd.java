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
		throw new UnsupportedOperationException("isRecursive not yet implemented");
	}
	
	@Override
	public FirstSet first(int k) {
		FirstSet ret = FirstSet.Empty();
		
		for(int i=0;i<prod.size();i++)
			ret = ret.union(prod.get(i).first(k));
		
		return ret;
	}

	public String getProcCode() {
		String ret = "Node " + getName() +"() {\n";

		Production firstProd = prod.get(0);
		
		if(firstProd instanceof Token)
			ret += 
					" if(match(" + firstProd.toStr() + ")) " +
					"return next();\n";
		else
			ret += 
					" if(match(" + firstProd.getName() + "FirstSet)) " +
					"return " + firstProd.getName() + "();\n";
		
		for(int i=1;i<prod.size();i++) {
			Production nowProd = prod.get(i);
			
			if(nowProd instanceof Token)
				ret += 
						" else if(match(" + nowProd.toStr() + ")) " +
						"return next();\n";
			else
				ret += 
						" else if(match(" + nowProd.getName() + "FirstSet)) "+
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
