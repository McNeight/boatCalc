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
package boat;


// TODO: Auto-generated Javadoc
/**
 * The Class Rudder.
 */
public class Rudder implements Cloneable {

  /** The dir. */
  public int dir;

  /** The rudder. */
  public rscFoil rudder;

  /** The skeg. */
  public rscFoil skeg;

  /** The valid. */
  public boolean valid = false;

  /**
   * Instantiates a new rudder.
   */
  public Rudder() {
    this.rudder = new rscFoil();
    this.skeg = new rscFoil();
    this.dir = 1;
    this.valid = true;
  }

  /**
   * Clone.
   *
   * @return the object
   */
  @Override
  public Object clone() {

    try {
      final Rudder r = (Rudder) super.clone();

      r.rudder = (rscFoil) this.rudder.clone();
      r.skeg = (rscFoil) this.skeg.clone();
      return r;
    } catch (final CloneNotSupportedException e) {
      throw new InternalError(e.toString());
    }

  } // end clone

  /**
   * Gets the area.
   *
   * @return the area
   */
  public double getArea() {
    double a = 0;
    if (this.rudder.use) {
      a = a + this.rudder.getArea();
    }
    if (this.skeg.use) {
      a = a + this.skeg.getArea();
    }
    return a;
  }

  /**
   * Gets the area X.
   *
   * @return the area X
   */
  public double getAreaX() {
    double x = 0;
    if (this.rudder.use) {
      x = x + (this.rudder.getArea() * this.rudder.getAreaX());
    }
    if (this.skeg.use) {
      x = x + (this.skeg.getArea() * this.skeg.getAreaX());
    }
    final double a = this.getArea();
    if (a > 0) {
      x = x / a;
    }
    return x;
  }

  /**
   * Gets the area Y.
   *
   * @return the area Y
   */
  public double getAreaY() {
    double x = 0;
    if (this.rudder.use) {
      x = x + (this.rudder.getArea() * this.rudder.getAreaY());
    }
    if (this.skeg.use) {
      x = x + (this.skeg.getArea() * this.skeg.getAreaY());
    }
    final double a = this.getArea();
    if (a > 0) {
      x = x / a;
    }
    return x;
  }

  /**
   * Gets the max X.
   *
   * @return the max X
   */
  public double getMaxX() {
    double x = 0;
    if (this.rudder.use) {
      x = Math.max(x, this.rudder.getMaxX());
    }
    if (this.skeg.use) {
      x = Math.max(x, this.skeg.getMaxX());
    }
    return x;
  }

  /**
   * Gets the max Y.
   *
   * @return the max Y
   */
  public double getMaxY() {
    double x = 0;
    if (this.rudder.use) {
      x = Math.max(x, this.rudder.getMaxY());
    }
    if (this.skeg.use) {
      x = Math.max(x, this.skeg.getMaxY());
    }
    return x;
  }

  /**
   * Gets the min X.
   *
   * @return the min X
   */
  public double getMinX() {
    double x = 1000000;
    if (this.rudder.use) {
      x = Math.min(x, this.rudder.getMinX());
    }
    if (this.skeg.use) {
      x = Math.min(x, this.skeg.getMinX());
    }
    return x;
  }

  /**
   * Gets the min Y.
   *
   * @return the min Y
   */
  public double getMinY() {
    double x = 1000000;
    if (this.rudder.use) {
      x = Math.min(x, this.rudder.getMinY());
    }
    if (this.skeg.use) {
      x = Math.min(x, this.skeg.getMinY());
    }
    return x;
  }

  /**
   * Sets the base.
   *
   * @param b the new base
   */
  public void setBase(final double b) {
    this.rudder.setBase(b);
    this.skeg.setBase(b);
  }

} // end Rudder
