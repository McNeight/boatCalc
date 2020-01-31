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
 * The Class Point.
 */
public class Point extends Object {

  /** The valid. */
  public boolean valid;

  /** The z. */
  public double x, y, z;

  /**
   * Instantiates a new point.
   */
  public Point() {
    this.valid = true;
  }

  /**
   * Instantiates a new point.
   *
   * @param b the b
   */
  public Point(final boolean b) {
    this.setX(0);
    this.setY(0);
    this.setZ(0);
    this.valid = b;
  }

  /**
   * Instantiates a new point.
   *
   * @param x the x
   * @param y the y
   * @param z the z
   */
  public Point(final double x, final double y, final double z) {
    this.setX(x);
    this.setY(y);
    this.setZ(z);
    this.valid = true;
  }

  /**
   * Instantiates a new point.
   *
   * @param x the x
   * @param y the y
   * @param z the z
   * @param v the v
   */
  public Point(final double x, final double y, final double z, final boolean v) {
    this.setX(x);
    this.setY(y);
    this.setZ(z);
    this.valid = v;
  }

  /**
   * Instantiates a new point.
   *
   * @param p the p
   */
  public Point(final Point p) {
    this.setX(p.x);
    this.setY(p.y);
    this.setZ(p.z);
    this.valid = true;
  }

  /**
   * Angle XZ.
   *
   * @return the double
   */
  public double angleXZ() {
    return Math.atan2(this.x, -this.z);
  }

  /**
   * Angle XZ.
   *
   * @param xAdj the x adj
   * @param zAdj the z adj
   * @return the double
   */
  public double angleXZ(final double xAdj, final double zAdj) {
    return Math.atan2(this.x - xAdj, zAdj - this.z);
  }

  /**
   * Angle YZ.
   *
   * @return the double
   */
  public double angleYZ() {
    return Math.atan2(this.y, -this.z);
  }

  /**
   * Angle YZ.
   *
   * @param yAdj the y adj
   * @param zAdj the z adj
   * @return the double
   */
  public double angleYZ(final double yAdj, final double zAdj) {
    return Math.atan2(this.y - yAdj, zAdj - this.z);
  }

  /**
   * Equals.
   *
   * @param p the p
   * @return true, if successful
   */
  public boolean equals(final Point p) {
    return ((p.x == this.x) && (p.y == this.y) && (p.z == this.z) && (p.valid == this.valid));
  }

  /**
   * Gets the x.
   *
   * @return the x
   */
  public double getX() {
    return this.x;
  }

  /**
   * Gets the y.
   *
   * @return the y
   */
  public double getY() {
    return this.y;
  }

  /**
   * Gets the z.
   *
   * @return the z
   */
  public double getZ() {
    return this.z;
  }

  /**
   * Minus.
   *
   * @param p the p
   * @return the point
   */
  public Point minus(final Point p) {
    return this.plus(-1, p);
  }

  /**
   * Plus.
   *
   * @param a the a
   * @param p the p
   * @return the point
   */
  public Point plus(final double a, final Point p) {
    final Point r = new Point();
    r.x = this.x + (a * p.x);
    r.y = this.y + (a * p.y);
    r.z = this.z + (a * p.z);
    return r;
  }

  /**
   * Plus.
   *
   * @param p the p
   * @return the point
   */
  public Point plus(final Point p) {
    final Point r = new Point();
    r.x = this.x + p.x;
    r.y = this.y + p.y;
    r.z = this.z + p.z;
    return r;
  }

  /**
   * Sets the x.
   *
   * @param x the new x
   */
  public void setX(final double x) {
    this.x = x;
  }

  /**
   * Sets the y.
   *
   * @param y the new y
   */
  public void setY(final double y) {
    this.y = y;
  }

  /**
   * Sets the z.
   *
   * @param z the new z
   */
  public void setZ(final double z) {
    this.z = z;
  }
} // end class Point
