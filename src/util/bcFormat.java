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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

// TODO: Auto-generated Javadoc
/**
 * The Class bcFormat.
 */
public class bcFormat {

  /** The DF 0 d. */
  public DecimalFormat DF0d;

  /** The DF 1 d. */
  public DecimalFormat DF1d;

  /** The DF 2 d. */
  public DecimalFormat DF2d;

  /** The DF 3 d. */
  public DecimalFormat DF3d;

  /** The DF 4 d. */
  public DecimalFormat DF4d;

  /**
   * Instantiates a new bc format.
   */
  public bcFormat() {
    final Locale l = new Locale("en", "US");
    // Locale l = new Locale("fr","FR");
    final DecimalFormatSymbols dfs = new DecimalFormatSymbols(l);
    this.DF0d = new DecimalFormat("###,###,###", dfs);
    this.DF1d = new DecimalFormat("###,###,###.0", dfs);
    this.DF2d = new DecimalFormat("###,###,###.00", dfs);
    this.DF3d = new DecimalFormat("###,###,###.000", dfs);
    this.DF4d = new DecimalFormat("###,###,###.0000", dfs);
  }// end constructor
}// enc bcFormat
