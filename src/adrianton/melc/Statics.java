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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Statics {
	public static String capitalize(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	public static void toFile(String fName, String content) {
		try {
			FileWriter writer;
			writer = new FileWriter(fName);

			BufferedWriter out = new BufferedWriter(writer);

			out.write(content);

			out.close();
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
