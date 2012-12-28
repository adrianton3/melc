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

import adrianton.melc.Statics;


public class ListProd implements Production {
	private final String name;
	private Production prod;
	
	public ListProd(String name) {
		this.name = name;
	}
	
	@Override
	public void add(Production p) {
		if(prod != null) throw new RuntimeException("List production nonterminal already set");
		prod = p;
	}
	
	@Override
	public boolean isRecursive() {
		throw new UnsupportedOperationException("isRecursive not yet implemented");
	}

	@Override
	public FirstSet first(int k) {
		FirstSet ret = FirstSet.get(FirstEl.Empty());
		
		int lk = 1;
		boolean nok = true;
		
		while(nok) {
			FirstSet t = prod.first(k);
			
			int i = 1;
			while(!t.allComplete(k) && i < lk) {
				int kRem = k - t.kComplete();
				t = t.concat(prod.first(kRem), k);
				i++;
			}
			
			ret = ret.union(t);
			
			nok = !t.allComplete(k);
			lk++;
		}
		
		return ret;
	}
	
	public String getProcCode() {
		String ret = Statics.capitalize(getName()) + " " + getName() +"() {\n";
		
		//TODO: treat separately if terminal
		
		ret +=
				" LinkedList<" + Statics.capitalize(prod.getName()) + "> list = " + 
				"new LinkedList<" + Statics.capitalize(prod.getName()) + ">();\n" +
				" while(match(" + prod.getName() + "FirstSet))\n" +
				"  list.add(" + prod.getName() + "());\n" + 
				" return new " + Statics.capitalize(prod.getName()) + "(list);\n"+
				"}";
		 
		return ret;
	}
	
	public String getStubCode() {
		StringBuilder ret = new StringBuilder(); 
		
		ret.append("class " + Statics.capitalize(getName()) + " implements Node {\n");
		ret.append(" final ArrayList<" + Statics.capitalize(prod.getName()) + "> list;\n\n");
		//constructor
		ret.append(
				" " + Statics.capitalize(getName()) + 
				"(ArrayList<" + Statics.capitalize(prod.getName()) + "> list) {\n");
		ret.append(" this.list = list;\n"); //constructor body
		ret.append(" }\n\n"); //constructor
		
		//toString
		ret.append(" public String toString() {\n");
		ret.append(
				"  String s = \"\";\n" +
				"  for(int i=0;i<list.size();i++)\n" +
				"   s += \"[\" + i + \"]: \" + list.get(i) + \"\\n\";\n");
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
		return name + " -l> { " + prod.toStr() + " }";
	}
	
	public String toStr() {
		return name;
	}
}