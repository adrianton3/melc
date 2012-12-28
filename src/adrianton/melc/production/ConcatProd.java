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

public class ConcatProd implements Production {
	private final String name;
	private ArrayList<Production> prod = new ArrayList<Production>();
	
	public ConcatProd(String name) {
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
		FirstSet ret;
	
		int i = 0;
		ret = prod.get(i).first(k);
		i++;
		
		while(!ret.allComplete(k) && i < prod.size()) {
			int kRem = k - ret.kComplete();
			ret = ret.concat(prod.get(i).first(kRem), k);
			i++;
		}
		
		return ret;
	}
	
	public String getProcCode() {
		String ret = "Node " + getName() +"() {\n";
		
		ArrayList<String> paramList = new ArrayList<String>();
		
		for(int i=0;i<prod.size();i++) {
			Production cProd = prod.get(i);
			
			if(cProd instanceof Token) {
				if(cProd instanceof Term)
					ret += " expect(" + cProd.toStr() + ");\n";
				else {
					String tmpParamName = cProd.getName() + i;
					ret += "Node" + " " + tmpParamName + " = " + cProd.getProcNameCode() + "();\n";
					paramList.add(tmpParamName);
				}
			}
			else {
				String tmpParamName = cProd.getName() + i;
				
				ret += 
						" " + "Node" /*Statics.capitalize(prod.get(i).getName())*/ + " " + 
						tmpParamName + " = "/*" = (" + Statics.capitalize(prod.get(i).getName()) + ") "*/ + 
						cProd.getName() + "();\n";
				
				paramList.add(tmpParamName); 
			}
		}
		
		String paramListStr = "";
		
		if(paramList.size() > 0) {
			paramListStr = paramList.get(0);
			for(int i=1;i<paramList.size();i++)
				paramListStr += "," + paramList.get(i);
		}
			
		ret += 
				" return new " + Statics.capitalize(getName()) + "(" + paramListStr + ");\n" + 
				"}";
		
		return ret;
	}

	public String getStubCode() {
		StringBuilder ret = new StringBuilder();

		//class definition
		ret.append("class " + Statics.capitalize(getName()) + " implements Node {\n");
		
		ArrayList<String> fieldList = new ArrayList<String>();
		//class fields
		for(int i=0;i<prod.size();i++) {
			Production cProd = prod.get(i);
		
			if(cProd instanceof Token) {
				if(!(cProd instanceof Term)) {
					String tmpFieldName = cProd.getName() + i;
					ret.append(
							" final " +
							"Node" /*Statics.capitalize(prod.get(i).getName())*/ + " " + tmpFieldName + ";\n");
					fieldList.add(tmpFieldName);
				}
			}
			else {
				String tmpFieldName = cProd.getName() + i;
				ret.append(
						" final " +
						"Node" /*Statics.capitalize(prod.get(i).getName())*/ + " " + tmpFieldName + ";\n");
				fieldList.add(tmpFieldName);
			}
		}
		ret.append("\n");
		
		//constructor parameters
		String fieldListStr = "";
		if(fieldList.size() > 0) {
			fieldListStr = "Node " + fieldList.get(0);
			for(int i=1;i<fieldList.size();i++)
				fieldListStr += ", Node " + fieldList.get(i);
		}
		
		//constructor definition
		ret.append(" " + Statics.capitalize(getName()) + "(" + fieldListStr + ") {\n");
		
		//fields initialization
		for(int i=0;i<fieldList.size();i++)
			ret.append("  this." + fieldList.get(i) + " = " + fieldList.get(i) + ";\n");
		ret.append(" }\n\n"); //constructor
		
		//toString
		ret.append(" public String toString() {\n");
		ret.append("  return \"" + getName() + "(\" +\n");
		for(int i=0;i<fieldList.size();i++)
			ret.append("   \"" + fieldList.get(i) + ": \" + " + fieldList.get(i) + ".toString() + \", \" +\n");
		
		ret.append("   \")\";\n");
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
		String ret = name + " -c> "; //TODO: use StringBuilder
		
		for(Production p: prod)
			ret += p.toStr() + " ";
		
		return ret;
	}
	
	public String toStr() {
		return name;
	}
}