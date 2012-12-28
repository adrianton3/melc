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

import java.util.regex.Pattern;

public class RTermNum implements RToken {
	final String cont;
	
	RTermNum() {
		this.cont = null;
	}
	
	RTermNum(String cont) {
		this.cont = cont;
	}
	
	@Override
	public boolean match(String s) {
		Pattern p = Pattern.compile("-?[0-9]+");
		return p.matcher(s).matches();
	}

	@Override
	public boolean match(RToken token) {
		return token instanceof RTermNum;
	}

	static boolean match2(String s) {
		Pattern p = Pattern.compile("-?[0-9]+");
		return p.matcher(s).matches();
	}
	
	public String toString() {
		return "NUM:" + cont;
	}
}
