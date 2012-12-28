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

public class TermId implements Production, Token {
	final String name;
	
	public TermId(String name) {
		this.name = name;
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
	public FirstSet first(int k) {
		return FirstSet.get(FirstEl.get(this));
	}
	
	@Override
	public boolean match(Token that) {
		return that instanceof TermId;
	}

	public String getProcCode() {
		throw new UnsupportedOperationException("terminals don't generate code");
	}
	
	public String toNewCode() {
		return "new RTermId()";
	}
	
	public String getStubCode() {
		throw new UnsupportedOperationException("terminals don't generate stub code");
	}
	
	public String getProcNameCode() {
		return "_id"; 
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "_" + name;
	}

	@Override
	public String toStr() {
		return toString();
	}
	
	@Override
	public boolean equals(Object that) { //consider a dedicated eq function for the set structure
		return that instanceof TermId;
	}
	
	@Override
	public int hashCode() {
		return 41;
	}
}
