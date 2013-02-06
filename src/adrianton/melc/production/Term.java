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

import java.util.Set;

public class Term implements Production, Token {
	final String cont;
	
	public Term(String cont) {
		this.cont = cont;
	}
	
	@Override
	public void add(Production p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRecursive() {
		return false;
	}

	@Override
	public void walk(Set<String> visited, Set<String> isNull, Set<String> isNotNull) throws AlreadyBeenHereException {
		isNotNull.add(getName());
	}
	
	@Override
	public FirstSet first(int k) {
		return FirstSet.get(FirstEl.get(this));
	}

	@Override
	public boolean match(Token that) {
		if(that instanceof Term) {
			Term tmp = (Term)that;
			return this.cont.equals(tmp.cont);
		}
		return false;
	}
	
	public String getProcCode() {
		throw new UnsupportedOperationException("terminals don't generate code");
	}
	
	public String toNewCode() {
		return "new RTerm(\"" + cont + "\")";
	}
	
	public String getStubCode() {
		throw new UnsupportedOperationException("terminals don't generate stub code");
	}
	
	public String getProcNameCode() {
		throw new UnsupportedOperationException("terminals don't have a proc name");
	}
	
	public String getName() {
		return "Term(" + cont + ")";
	}
	
	public String toString() {
		return toStr(); //"Term(" + cont + ")";
	}

	@Override
	public String toStr() {
		return "\"" + cont + "\"";
	}
	
	@Override
	public boolean equals(Object that) {
		if(that instanceof Term) {
			Term conv = (Term)that;
			return cont.equals(conv.cont);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 31 * cont.hashCode();
	}
}