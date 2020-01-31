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
 * The Class Rig.
 */
public class Rig implements Cloneable {

  /** The dir. */
  public int dir;

  /** The jib. */
  public Sail jib;

  /** The main. */
  public Sail main;

  /** The mizzen. */
  public Sail mizzen;

  /** The valid. */
  public boolean valid;

  /**
   * Instantiates a new rig.
   */
  public Rig() {
    this.main = new Sail();
    this.jib = new Sail();
    this.mizzen = new Sail();
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
      final Rig r = (Rig) super.clone();
      // r.main = new Sail();
      // r.jib = new Sail();
      // r.mizzen = new Sail();

      r.main = (Sail) this.main.clone();
      r.jib = (Sail) this.jib.clone();
      r.mizzen = (Sail) this.mizzen.clone();
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
    if (this.main.use) {
      a = a + this.main.getArea();
    }
    if (this.jib.use) {
      a = a + this.jib.getArea();
    }
    if (this.mizzen.use) {
      a = a + this.mizzen.getArea();
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
    if (this.main.use) {
      x = x + (this.main.getArea() * this.main.getAreaX());
    }
    if (this.jib.use) {
      x = x + (this.jib.getArea() * this.jib.getAreaX());
    }
    if (this.mizzen.use) {
      x = x + (this.mizzen.getArea() * this.mizzen.getAreaX());
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
    if (this.main.use) {
      x = x + (this.main.getArea() * this.main.getAreaY());
    }
    if (this.jib.use) {
      x = x + (this.jib.getArea() * this.jib.getAreaY());
    }
    if (this.mizzen.use) {
      x = x + (this.mizzen.getArea() * this.mizzen.getAreaY());
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
    if (this.main.use) {
      x = Math.max(x, this.main.getMaxX());
    }
    if (this.jib.use) {
      x = Math.max(x, this.jib.getMaxX());
    }
    if (this.mizzen.use) {
      x = Math.max(x, this.mizzen.getMaxX());
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
    if (this.main.use) {
      x = Math.max(x, this.main.getMaxY());
    }
    if (this.jib.use) {
      x = Math.max(x, this.jib.getMaxY());
    }
    if (this.mizzen.use) {
      x = Math.max(x, this.mizzen.getMaxY());
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
    if (this.main.use) {
      x = Math.min(x, this.main.getMinX());
    }
    if (this.jib.use) {
      x = Math.min(x, this.jib.getMinX());
    }
    if (this.mizzen.use) {
      x = Math.min(x, this.mizzen.getMinX());
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
    if (this.main.use) {
      x = Math.min(x, this.main.getMinY());
    }
    if (this.jib.use) {
      x = Math.min(x, this.jib.getMinY());
    }
    if (this.mizzen.use) {
      x = Math.min(x, this.mizzen.getMinY());
    }
    return x;
  }

} // end class Rig
