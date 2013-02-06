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
import java.util.Set;

import adrianton.melc.Statics;

public class OptionalProd implements Production {
	private final String name;
	private Production prod;
	
	public OptionalProd(String name) {
		this.name = name;
	}
	
	@Override
	public void add(Production p) {
		if(prod != null) throw new RuntimeException("Optional production nonterminal already set");
		prod = p;
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
		isNull.add(name);
		prod.walk(visited, isNull, isNotNull);
	}

	@Override
	public FirstSet first(int k) {
		FirstSet ret = FirstSet.Empty();
		
		HashSet<FirstEl> tmp = new HashSet<FirstEl>();
		tmp.add(FirstEl.Empty());
		
		ret = ret.union(new FirstSet(tmp));
		ret = ret.union(prod.first(k));
		
		return ret;
	}
	
	public String getProcCode() {
		String ret = Statics.capitalize(getName()) + " " + getName() + "() {\n";
		
		if(prod instanceof Token)
			ret +=
					" if(match(" + prod.getName() + "FirstSet))\n" + 
					"  return new " + Statics.capitalize(getName()) + "(next());\n" +
					" else return new " + Statics.capitalize(getName()) + "(null);\n" +
					"}";
		else
			ret +=
					" if(match(" + prod.getName() + "FirstSet))\n" + 
					"  return new " + Statics.capitalize(getName()) + "(" + prod.getName() + "());\n" +
					" else return new " + Statics.capitalize(getName()) + "(null);\n" +
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
				"(Node" /*+ Statics.capitalize(prod.getName())*/ + " node) {\n");
		ret.append("  this.node = node;\n"); //constructor body
		ret.append(" }\n\n"); //constructor
		
		//toString
		ret.append(" public String toString() {\n");
		ret.append("  return \"\" + node;\n");
		ret.append(" }\n"); //toString
		
		ret.append("}"); //class
		
		return ret.toString();
	}
	
	public String getProcNameCode() {
		return name; 
	}
	
	@Override
	public String getName() {
		return name;
	}

	public String toString() {
		return name + " -o> [ " + prod.toStr() + " ]";
	}
	
	public String toStr() {
		return name;
	}
}