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
package util;
/*
 * Copyright (c) 2003 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * -Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * -Redistribution in binary form must reproduct the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL EXPRESS OR IMPLIED
 * CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS
 * SHALL NOT BE LIABLE FOR ANY DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT
 * WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS
 * OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF SUN
 * HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or intended for use in the design,
 * construction, operation or maintenance of any nuclear facility.
 */

/*
 * @(#)ExampleFileFilter.java 1.14 03/01/23
 */


import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.filechooser.FileFilter;

/**
 * A convenience implementation of FileFilter that filters out all files except for those type
 * extensions that it knows about.
 *
 * Extensions are of the type ".foo", which is typically found on Windows and Unix boxes, but not on
 * Macinthosh. Case is ignored.
 *
 * Example - create a new filter that filerts out all files but gif and jpg image files:
 *
 * JFileChooser chooser = new JFileChooser(); ExampleFileFilter filter = new ExampleFileFilter( new
 * String{"gif", "jpg"}, "JPEG &amp; GIF Images") chooser.addChoosableFileFilter(filter);
 * chooser.showOpenDialog(this);
 *
 * @version 1.14 01/23/03
 * @author Jeff Dinkins
 */
public class bcFileFilter extends FileFilter {

  private String description = null;
  private Hashtable<String, bcFileFilter> filters = null;
  private String fullDescription = null;
  private boolean useExtensionsInDescription = true;

  /**
   * Creates a file filter. If no filters are added, then all files are accepted.
   *
   * @see #addExtension
   */
  public bcFileFilter() {
    this.filters = new Hashtable<>();
  }

  /**
   * Creates a file filter that accepts files with the given extension. Example: new
   * ExampleFileFilter("jpg");
   *
   * @see #addExtension
   */
  public bcFileFilter(final String extension) {
    this(extension, null);
  }

  /**
   * Creates a file filter that accepts the given file type. Example: new ExampleFileFilter("jpg",
   * "JPEG Image Images");
   *
   * Note that the "." before the extension is not needed. If provided, it will be ignored.
   *
   * @see #addExtension
   */
  public bcFileFilter(final String extension, final String description) {
    this();
    if (extension != null) {
      this.addExtension(extension);
    }
    if (description != null) {
      this.setDescription(description);
    }
  }

  /**
   * Creates a file filter from the given string array. Example: new ExampleFileFilter(String
   * {"gif", "jpg"});
   *
   * Note that the "." before the extension is not needed adn will be ignored.
   *
   * @see #addExtension
   */
  public bcFileFilter(final String[] filters) {
    this(filters, null);
  }

  /**
   * Creates a file filter from the given string array and description. Example: new
   * ExampleFileFilter(String {"gif", "jpg"}, "Gif and JPG Images");
   *
   * Note that the "." before the extension is not needed and will be ignored.
   *
   * @see #addExtension
   */
  public bcFileFilter(final String[] filters, final String description) {
    this();
    for (int i = 0; i < filters.length; i++) {
      // add filters one by one
      this.addExtension(filters[i]);
    }
    if (description != null) {
      this.setDescription(description);
    }
  }

  /**
   * Return true if this file should be shown in the directory pane, false if it shouldn't.
   *
   * Files that begin with "." are ignored.
   *
   * @see #getExtension
   * @see bcFileFilter#accepts
   */
  @Override
  public boolean accept(final File f) {
    if (f != null) {
      if (f.isDirectory()) {
        return true;
      }
      final String extension = this.getExtension(f);
      if ((extension != null) && (this.filters.get(this.getExtension(f)) != null)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Adds a filetype "dot" extension to filter against.
   *
   * For example: the following code will create a filter that filters out all files except those
   * that end in ".jpg" and ".tif":
   *
   * ExampleFileFilter filter = new ExampleFileFilter(); filter.addExtension("jpg");
   * filter.addExtension("tif");
   *
   * Note that the "." before the extension is not needed and will be ignored.
   */
  public void addExtension(final String extension) {
    if (this.filters == null) {
      this.filters = new Hashtable<>(5);
    }
    this.filters.put(extension.toLowerCase(), this);
    this.fullDescription = null;
  }

  /**
   * Returns the human readable description of this filter. For example: "JPEG and GIF Image Files
   * (*.jpg, *.gif)"
   *
   * @see setDescription
   * @see setExtensionListInDescription
   * @see isExtensionListInDescription
   * @see FileFilter#getDescription
   */
  @Override
  public String getDescription() {
    if (this.fullDescription == null) {
      if ((this.description == null) || this.isExtensionListInDescription()) {
        this.fullDescription = this.description == null ? "(" : this.description + " (";
        // build the description from the extension list
        final Enumeration<String> extensions = this.filters.keys();
        if (extensions != null) {
          this.fullDescription += "." + extensions.nextElement();
          while (extensions.hasMoreElements()) {
            this.fullDescription += ", ." + extensions.nextElement();
          }
        }
        this.fullDescription += ")";
      } else {
        this.fullDescription = this.description;
      }
    }
    return this.fullDescription;
  }


  /**
   * Return the extension portion of the file's name .
   *
   * @see #getExtension
   * @see FileFilter#accept
   */
  public String getExtension(final File f) {
    if (f != null) {
      final String filename = f.getName();
      final int i = filename.lastIndexOf('.');
      if ((i > 0) && (i < (filename.length() - 1))) {
        return filename.substring(i + 1).toLowerCase();
      }
    }
    return null;
  }

  /**
   * Returns whether the extension list (.jpg, .gif, etc) should show up in the human readable
   * description.
   *
   * Only relevent if a description was provided in the constructor or using setDescription();
   *
   * @see getDescription
   * @see setDescription
   * @see setExtensionListInDescription
   */
  public boolean isExtensionListInDescription() {
    return this.useExtensionsInDescription;
  }

  /**
   * Sets the human readable description of this filter. For example: filter.setDescription("Gif and
   * JPG Images");
   *
   * @see setDescription
   * @see setExtensionListInDescription
   * @see isExtensionListInDescription
   */
  public void setDescription(final String description) {
    this.description = description;
    this.fullDescription = null;
  }

  /**
   * Determines whether the extension list (.jpg, .gif, etc) should show up in the human readable
   * description.
   *
   * Only relevent if a description was provided in the constructor or using setDescription();
   *
   * @see getDescription
   * @see setDescription
   * @see isExtensionListInDescription
   */
  public void setExtensionListInDescription(final boolean b) {
    this.useExtensionsInDescription = b;
    this.fullDescription = null;
  }
}
