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
 * The Class Dir.
 */
class Dir extends Point {

  /**
   * Instantiates a new dir.
   */
  public Dir() {}

  /**
   * Instantiates a new dir.
   *
   * @param x the x
   * @param y the y
   * @param z the z
   */
  public Dir(final double x, final double y, final double z) {
    this.setDir(x, y, z);
  }

  /**
   * Instantiates a new dir.
   *
   * @param p the p
   */
  public Dir(final Point p) {
    this.setDir(p.x, p.y, p.z);
  }

  /**
   * Gets the x.
   *
   * @return the x
   */
  @Override
  public double getX() {
    return this.x;
  }

  /**
   * Gets the y.
   *
   * @return the y
   */
  @Override
  public double getY() {
    return this.y;
  }

  /**
   * Gets the z.
   *
   * @return the z
   */
  @Override
  public double getZ() {
    return this.z;
  }

  /**
   * Sets the dir.
   *
   * @param x the x
   * @param y the y
   * @param z the z
   */
  public void setDir(final double x, final double y, final double z) {
    final double l = Math.sqrt((x * x) + (y * y) + (z * z));
    if (l > 0) {
      this.setX(x / l);
      this.setY(y / l);
      this.setZ(z / l);
    } else {
      this.setX(0);
      this.setY(0);
      this.setZ(0);
    }
  }
} // end class Dir
