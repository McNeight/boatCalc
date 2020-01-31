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
 * The Class Line.
 */
public class Line extends Object {

  /** The length. */
  public int length;

  /** The points. */
  public Point[] points;

  /**
   * Instantiates a new line.
   *
   * @param n the n
   */
  public Line(final int n) {
    this.points = new Point[n];
    this.length = this.points.length;
  }

  /**
   * Instantiates a new line.
   *
   * @param p the p
   */
  public Line(final Point[] p) {
    this.points = p;
    this.length = this.points.length;
  }

  /**
   * Count valid.
   *
   * @return the int
   */
  public int countValid() {
    int i, n;
    n = 0;
    for (i = 0; i < this.points.length; i++) {
      if (this.points[i].valid) {
        n++;
      }
    }
    return n;
  }// end countValid

  /**
   * Gets the point.
   *
   * @param i the i
   * @return the point
   */
  public Point getPoint(final int i) {
    return this.points[i];
  }

  /**
   * Sets the point.
   *
   * @param p the p
   * @param i the i
   */
  public void setPoint(final Point p, final int i) {
    this.points[i] = p;
  }

  /**
   * Valid.
   *
   * @param i the i
   * @return true, if successful
   */
  public boolean valid(final int i) {
    return this.points[i].valid;
  }

  /**
   * Val X.
   *
   * @param i the i
   * @return the double
   */
  public double valX(final int i) {
    return this.points[i].x;
  }

  /**
   * Val Y.
   *
   * @param i the i
   * @return the double
   */
  public double valY(final int i) {
    return this.points[i].y;
  }

  /**
   * Val Z.
   *
   * @param i the i
   * @return the double
   */
  public double valZ(final int i) {
    return this.points[i].z;
  }
} // end class Line
