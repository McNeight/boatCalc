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

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import boat.Hull;

// TODO: Auto-generated Javadoc
/**
 * The Class SerializeToXML.
 */
public final class SerializeToXML {

  /**
   * Instantiates a new serialize to XML.
   *
   * @param filename the filename
   * @param h the h
   */
  public SerializeToXML(final String filename, final Hull h) {
    super();

    XMLEncoder encoder = null;
    try {
      encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filename)));
    } catch (final FileNotFoundException fileNotFound) {
      System.out.println("ERROR: While Creating or Opening the File " + filename);
    }
    encoder.writeObject(h);
    encoder.close();
  }
}
