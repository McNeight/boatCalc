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
package geom;

import java.util.Comparator;

// TODO: Auto-generated Javadoc
/**
 * The Class XZCompare.
 */
public class XZCompare implements Comparator<Point> {

  /** The x adj. */
  double xAdj = 0;

  /** The z adj. */
  double zAdj = 0;

  /**
   * Compare.
   *
   * @param p1 the p 1
   * @param p2 the p 2
   * @return the int
   */
  @Override
  public int compare(final Point p1, final Point p2) {
    return this.sgn(p1.angleXZ(this.xAdj, this.zAdj) - p2.angleXZ(this.xAdj, this.zAdj));
  }

  /**
   * Equals.
   *
   * @param o the o
   * @return true, if successful
   */
  @Override
  public boolean equals(final Object o) {
    return o.equals(this);
  }

  /**
   * Sets the adj.
   *
   * @param x the x
   * @param z the z
   */
  public void setAdj(final double x, final double z) {
    this.xAdj = x;
    this.zAdj = z;
  }

  /**
   * Sgn.
   *
   * @param a the a
   * @return the int
   */
  private int sgn(final double a) {
    if (a > 0) {
      return 1;
    } else if (a < 0) {
      return -1;
    } else {
      return 0;
    }
  }
} // end class XZCompare
