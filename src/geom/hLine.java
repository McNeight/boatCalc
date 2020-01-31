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

// TODO: Auto-generated Javadoc
/**
 * The Class hLine.
 */
public class hLine {

  /** The h points. */
  public Point[] hPoints;

  /** The h XY. */
  public Interp hXY;

  /** The h XZ. */
  public Interp hXZ;

  /** The valid. */
  public boolean valid;

  /**
   * Instantiates a new h line.
   *
   * @param p the p
   * @param b the b
   */
  // constructor
  public hLine(final Point[] p, final double b) {
    int i, j = -1;
    int n;
    final Point basePoint = new Point(0, 0, b);
    this.valid = false;
    // count valid points
    n = 0;
    for (i = 0; i < p.length; i++) {
      if (p[i].valid) {
        n++;
      }
    }

    this.hPoints = new Point[n];
    this.hXY = new Interp(n);
    this.hXZ = new Interp(n);

    for (i = 0; i < p.length; i++) {
      if (p[i].valid) {
        j++;
        this.hPoints[j] = p[i].plus(basePoint);
        this.hXY.setXY(j, p[i].x, this.hPoints[j].y);
        this.hXZ.setXY(j, p[i].x, this.hPoints[j].z);
        this.valid = true;
      }
    }

  } // end constructor

  /**
   * Max.
   *
   * @param D the d
   * @return the double
   */
  public double max(final String D) {
    int i;
    double t_max = -1000000.0;
    for (i = 0; i < this.hPoints.length; i++) {
      if (this.hPoints[i].valid) {
        if (0 == D.compareTo("X")) {
          t_max = Math.max(t_max, this.hPoints[i].x);
        }
        if (0 == D.compareTo("Y")) {
          t_max = Math.max(t_max, this.hPoints[i].y);
        }
        if (0 == D.compareTo("Z")) {
          t_max = Math.max(t_max, this.hPoints[i].z);
        }
      }
    }
    return t_max;
  } // end max

  /**
   * Min.
   *
   * @param D the d
   * @return the double
   */
  public double min(final String D) {
    int i;
    double t_min = +1000000.0;
    for (i = 0; i < this.hPoints.length; i++) {
      if (this.hPoints[i].valid) {
        if (0 == D.compareTo("X")) {
          t_min = Math.min(t_min, this.hPoints[i].x);
        }
        if (0 == D.compareTo("Y")) {
          t_min = Math.min(t_min, this.hPoints[i].y);
        }
        if (0 == D.compareTo("Z")) {
          t_min = Math.min(t_min, this.hPoints[i].z);
        }
      }
    }
    return t_min;
  } // end min


}
