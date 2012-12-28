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

package adrianton.melc.grammar.token;

public class ProdSym implements GToken {
	public final String cont;
	public final boolean isConcat, isOptional, isList, isSwitch;
	
	//TODO: put the symbols in some array
	public ProdSym(String cont) {
		this.cont = cont;
		this.isConcat   = this.cont.equals("-c>");
		this.isOptional = this.cont.equals("-o>");
		this.isList     = this.cont.equals("-l>");
		this.isSwitch   = this.cont.equals("-s>");
	}
	
	public static boolean match(String s) {
		return 
				s.equals("-c>") ||
				s.equals("-o>") ||
				s.equals("-l>") ||
				s.equals("-s>");
	}
	
	public String toString() {
		return "$" + cont;
	}
}