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

public class SimpleTokenizer {
	ArrayList<RToken> tokenize(String s) {
		ArrayList<RToken> ret = new ArrayList<RToken>();
		
		String[] ar = s.split(" ");
		for(int i=0;i<ar.length;i++) {
			if(RTermId.match2(ar[i])) ret.add(new RTermId(ar[i]));
			else if(RTermNum.match2(ar[i])) ret.add(new RTermNum(ar[i]));
			else ret.add(new RTerm(ar[i]));
		}
		
		ret.add(new REnd());
		
		return ret;
	}
}
