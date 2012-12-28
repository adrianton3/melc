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

package adrianton.melc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import adrianton.melc.grammar.Grammar;
import adrianton.melc.production.ConcatProd;
import adrianton.melc.production.FirstSet;
import adrianton.melc.production.Production;
import adrianton.melc.testparser.Node;
import adrianton.melc.testparser.RTermId;
import adrianton.melc.testparser.RToken;

public class ParserGenerator {
	private static HashMap<String, FirstSet> firstSets(Grammar grammar, int k) {
		HashMap<String, FirstSet> map = new HashMap<String, FirstSet>();

		ArrayList<Production> ar = grammar.getProd();
		for(Production p : ar)
			map.put(p.getName(), p.first(k));

		return map;
	}

	private static HashMap<String, String> procedures(Grammar grammar) {
		HashMap<String, String> map = new HashMap<String, String>();

		ArrayList<Production> ar = grammar.getProd();
		for(Production p : ar)
			map.put(p.getName(), p.getProcCode());

		return map;
	}
	
	private static HashMap<String, String> stubs(Grammar grammar) {
		HashMap<String, String> map = new HashMap<String, String>();

		ArrayList<Production> ar = grammar.getProd();
		for(Production p : ar)
			map.put(p.getName(), p.getStubCode());

		return map;
	}

	private static String proceduresCode(Grammar grammar) {
		HashMap<String, String> procs = procedures(grammar);

		StringBuilder ret = new StringBuilder();

		for(String s : procs.values()) {
			ret.append(s);
			ret.append("\n\n");
		}

		return ret.toString();
	}

	private static String firstSetsCode(Grammar grammar, int k) {
		HashMap<String, FirstSet> firstSets = firstSets(grammar, k);

		StringBuilder ret = new StringBuilder();

		ret.append("RToken _ID = new RTermId();\n");
		ret.append("RToken _NUM = new RTermNum();\n");
		
		for(Entry<String, FirstSet> en: firstSets.entrySet())
			ret.append(en.getValue().toCode(en.getKey() + "FirstSet"));

		return ret.toString();
	}
	
	private static String stubsCode(Grammar grammar) {
		HashMap<String, String> stubs = stubs(grammar);

		StringBuilder ret = new StringBuilder();

		for(String s : stubs.values()) {
			ret.append(s);
			ret.append("\n\n");
		}

		return ret.toString();
	}
	
	public static String getParser(Grammar grammar, int k, String pack) {
		StringBuilder sb = new StringBuilder();
		sb.append("package " + pack + ";\n\n");
		sb.append("import java.util.ArrayList;\n\n");
		sb.append("class ASTBuilder extends ASTBuilderBase {\n");
		
		sb.append(
				"ASTBuilder(ArrayList<RToken> token) {\n" +
				"super(token);\n" +
				"}\n\n");
		
		sb.append(
				"Node getTree() {\n" +
				" return " + grammar.getStart() + "();\n" + 
				"}\n\n");
		
		sb.append(firstSetsCode(grammar,k));
		sb.append("\n");
		sb.append(proceduresCode(grammar));
		sb.append("}\n\n");
		sb.append(stubsCode(grammar));

		return sb.toString();
	}
}