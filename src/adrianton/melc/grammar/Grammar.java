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

package adrianton.melc.grammar;

import java.util.ArrayList;
import java.util.HashMap;

import adrianton.melc.grammar.token.GTermId;
import adrianton.melc.grammar.token.GTermNum;
import adrianton.melc.grammar.token.ProdIdentifier;
import adrianton.melc.grammar.token.EndProdSym;
import adrianton.melc.grammar.token.ProdSym;
import adrianton.melc.grammar.token.GTerm;
import adrianton.melc.grammar.token.GToken;
import adrianton.melc.production.ConcatProd;
import adrianton.melc.production.ListProd;
import adrianton.melc.production.OptionalProd;
import adrianton.melc.production.Production;
import adrianton.melc.production.SwitchProd;
import adrianton.melc.production.Term;
import adrianton.melc.production.TermId;
import adrianton.melc.production.TermNum;

public class Grammar {
	private ArrayList<Production> prod;
	private final String start; 
	
	private Grammar(ArrayList<Production> prod, String start) {
		this.prod = prod;
		this.start = start;
	}
	
	private static ArrayList<GToken> tokenize(String s) {
		ArrayList<GToken> ret = new ArrayList<GToken>();
		//TODO: make proper tokenizer
		String[] ar = s.split(" ");
		for(int i=0;i<ar.length;i++) {
			if(ProdIdentifier.match(ar[i])) ret.add(new ProdIdentifier(ar[i]));
			else if(GTermId.match(ar[i])) ret.add(new GTermId(ar[i]));
			else if(GTermNum.match(ar[i])) ret.add(new GTermNum(ar[i]));
			else if(ProdSym.match(ar[i])) ret.add(new ProdSym(ar[i]));
			else if(GTerm.match(ar[i])) ret.add(new GTerm(ar[i]));
			else if(EndProdSym.match(ar[i])) ret.add(new EndProdSym());
		}
		
		return ret;
	}
	
	private static Grammar toGrammar(ArrayList<GToken> token) {
		String start = "";
		
		//identify the starting production
		//TODO: rewrite this as a separate function
		int i = 0;
		while(i < token.size()) {
			if(token.get(i) instanceof ProdIdentifier && token.get(i+1) instanceof ProdSym)
				{ start = ((ProdIdentifier)token.get(i)).getName(); break; } 
			else
				i++;
		}
		
		//step 1: create the grammar's structure
		//TODO: rewrite this as a separate function
		HashMap<String,Production> map = new HashMap<String,Production>();
		
		i = 0;
		while(i < token.size()) {
			if(token.get(i) instanceof ProdIdentifier && token.get(i+1) instanceof ProdSym) {
				ProdSym prodSym = (ProdSym)token.get(i+1);
				Production tmp = null;
				
				ProdIdentifier conv = (ProdIdentifier)token.get(i);
				String tmpName = conv.name;
				
				if(prodSym.isConcat) tmp = new ConcatProd(tmpName);
				else if(prodSym.isSwitch) tmp = new SwitchProd(tmpName);
				else if(prodSym.isList) tmp = new ListProd(tmpName);
				else if(prodSym.isOptional) tmp = new OptionalProd(tmpName);
				else throw new RuntimeException("tokenizer: unknown token");
				
				map.put(tmpName, tmp);
			}
			i++;
		}
		
		//step 2: "connect" grammar "nodes" (interpret the grammar as a graph)
		//TODO: rewrite this as a separate function
		i = 0;
		String curProdName = null;
		
		while(i < token.size()) {
			if(token.get(i) instanceof ProdIdentifier && token.get(i+1) instanceof ProdSym) {
				ProdIdentifier conv = (ProdIdentifier)token.get(i);
				curProdName = conv.name;
			}
			else {
				Production toAdd = null;
				
				if(token.get(i) instanceof ProdIdentifier) {
					ProdIdentifier conv = (ProdIdentifier)token.get(i);
					toAdd = map.get(conv.name);
				}
				else if(token.get(i) instanceof GTermId) {
					GTermId conv = (GTermId)token.get(i);
					toAdd = new TermId(conv.name);
				}
				else if(token.get(i) instanceof GTermNum) {
					GTermNum conv = (GTermNum)token.get(i);
					toAdd = new TermNum(conv.name);
				}
				else if(token.get(i) instanceof GTerm) {
					GTerm conv = (GTerm)token.get(i);
					toAdd = new Term(conv.cont);
				}

				if(toAdd != null)
					map.get(curProdName).add(toAdd);
			}
			
			i++;
		}
		
		ArrayList<Production> ar = new ArrayList<Production>();
		ar.addAll(map.values());
		return new Grammar(ar,start);
	}

	public static Grammar testGrammar() {
		Production block   = new ConcatProd("block");
		Production varList = new ListProd("varList");
		Production var     = new ConcatProd("var");
		Production conList = new ListProd("conList");
		Production con     = new ConcatProd("con");
		Production state   = new ConcatProd("state");
		
		block.add(varList); block.add(conList); block.add(state);
		
		varList.add(var);
		var.add(new TermId("id")); var.add(new Term(","));
		
		conList.add(con);
		con.add(new TermId("id")); con.add(new Term("=")); con.add(new TermNum("nr")); con.add(new Term(","));
		
		state.add(new Term("a")); state.add(new Term("b"));
		
		ArrayList<Production> ar = new ArrayList<Production>();
		ar.add(block);
		ar.add(varList);
		ar.add(var);
		ar.add(conList);
		ar.add(con);
		ar.add(state);
		
		return new Grammar(ar,"block");
	}
	
	public static Grammar fromString(String s) {
		ArrayList<GToken> token = tokenize(s);
		Grammar grammar = toGrammar(token);
		return grammar;
	}
	
	public String toString() {
		String ret = "Grammar(\n"; //TODO: use StringBuilder
		
		for(Production p: prod)
			ret += " " + p + "\n";
		
		ret += ")";
		return ret;
	}

	public ArrayList<Production> getProd() {
		return prod;
	}

	public Production getProd(int i) {
		return prod.get(i);
	}
	
	public String getStart() {
		return start;
	}
}
