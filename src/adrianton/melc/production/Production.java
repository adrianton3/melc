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

public interface Production {
	void add(Production p);
	boolean isRecursive();
	void walk(Set<String> visited, Set<String> isNull, Set<String> isNotNull) throws AlreadyBeenHereException;
	FirstSet first(int k);
	String toStr(); //???
	String getMatchCode();
	String getName();
	String getProcCode();
	String getStubCode();
	String getProcNameCode();
}