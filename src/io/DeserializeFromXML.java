/* @formatter:off
 *
 * boatCalc
 * Copyright (C) 2004 Peter H. Vanderwaart
 * Copyright (C) 2020 Neil McNeight
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 *
 * @formatter:on
 */
package io;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import boat.Hull;

// TODO: Auto-generated Javadoc
/**
 * The Class DeserializeFromXML.
 */
public final class DeserializeFromXML {

  /**
   * Instantiates a new deserialize to XML.
   *
   * @param filename the filename
   * @param h the h
   */
  public DeserializeFromXML(final String filename, Hull h) {
    super();

    XMLDecoder decoder = null;
    try {
      decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filename)));
    } catch (final FileNotFoundException e) {
      System.out.println("ERROR: File " + filename + " not found");
    }
    h = (Hull) decoder.readObject();
    decoder.close();
  }
}
