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

public class RTerm implements RToken {
	final String cont;
	
	RTerm(String cont) {
		this.cont = cont;
	}
	
	@Override
	public boolean match(String s) {
		return cont.equals(s);
	}

	@Override
	public boolean match(RToken token) {
		if(token instanceof RTerm) {
			RTerm conv = (RTerm)token;
			return cont.equals(conv.cont);
		}
		return false;
	}
	
	static boolean match2(String s) {
		return s.charAt(0) == '"' && s.charAt(s.length()-1) == '"';
	}
	
	public String toString() {
		return "\"" + cont + "\"";
	}
}
