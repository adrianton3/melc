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

import adrianton.melc.ParserGenerator;
import adrianton.melc.Statics;
import adrianton.melc.grammar.Grammar;

public class Test {
	static void testParser() { 
		final String source1 = "[ ( [ [ + ] ] ) ]";
		final String source2 = "( ( ds + sd ) - 9 ) + 4";
		final String source = "( 15 + 3 * 2 ) * 7 + 5";
		
		final SimpleTokenizer tokenizer = new SimpleTokenizer();
		final ArrayList<RToken> token = tokenizer.tokenize(source);
		
		System.out.println(token);
		
		final ASTBuilderBase astb = new ASTBuilder(token);
		final Node tree = astb.getTree();
		
		System.out.println(tree);
	}
	
	static void testBuilder() {
		final String grStr1 =
				"a -s> aa ab \"+\" . " +
				"aa -c> \"(\" a \")\" . " + 
				"ab -c> \"[\" a \"]\" .";
		final String grStr2 =
				"exp -s> fac aux . " +
				"aux -c> fac pm fac . " +
				"pm -s> \"+\" \"-\" . " +
				"fac -s> _ID _NUM par . " +
				"par -c> \"(\" exp \")\" .";
		final String grStr3 =
				"e -s> ex t . " +
				"ex -c> t \"+\" e . " +
				"t -s> tx f . " +
				"tx -c> f \"*\" t . " +
				"f -s> _NUM _ID par . " +
				"par -c> \"(\" e \")\" .";
		final String grStr =
				"e -c> t eop . " +
				"eop -o> eo . " +
				"eo -c> \"+\" t eop . " + 
				"t -c> f top . " +
				"top -o> to . " +
				"to -c> \"*\" f top . " +
				"f -s> #num par . " +
				"par -c> \"(\" e \")\" .";

		final Grammar grammar = Grammar.fromString(grStr);
		
		final String fName = "src/adrianton/melc/testparser/ASTBuilder.java";
		final String content = ParserGenerator.getParser(grammar,2,"adrianton.melc.testparser");
		
		Statics.toFile(fName,content);
	}
	
	public static void main(String[] args) {
		//testBuilder();
		testParser();
	}
}
