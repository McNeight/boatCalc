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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

// TODO: Auto-generated Javadoc
/**
 * The Class xzArea.
 */
public class xzArea {

  /** The area. */
  private double area;

  /** The cz. */
  private double cx, cz;

  /** The tz. */
  private double tx, tz;

  /**
   * Instantiates a new xz area.
   *
   * @param a the a
   */
  public xzArea(final ArrayList<Point> a) {
    double ta;
    this.tx = 0;
    this.tz = 0;

    if (a.size() == 0) {
      this.area = 0;
      this.cx = 0;
      this.cz = 0;
      return;
    }

    for (int i = 0; i < a.size(); i++) {
      final Point p = a.get(i);
      this.tx = this.tx + p.x;
      this.tz = this.tz + p.z;
    }
    this.tx = this.tx / a.size();
    this.tz = this.tz / a.size();

    final XZCompare xzComp = new XZCompare();
    xzComp.setAdj(this.tx, this.tz);
    final SortedSet<Point> ss = new TreeSet<>(xzComp);
    for (int i = 0; i < a.size(); i++) {
      ss.add(a.get(i));
    }
    final Iterator<?> si = ss.iterator();
    final Point p0 = (Point) si.next();
    Point p1 = new Point(p0);
    while (si.hasNext()) {
      final Point p2 = (Point) si.next();
      ta = this.TriArea(p1.x, p1.z, p2.x, p2.z, this.tx, this.tz);
      this.area = this.area + ta;
      this.cx = this.cx + ((ta * (p1.x + p2.x + this.tx)) / 3.0);
      this.cz = this.cz + ((ta * (p1.z + p2.z + this.tz)) / 3.0);
      p1 = p2;
    }
    ta = this.TriArea(p1.x, p1.z, p0.x, p0.z, this.tx, this.tz);
    this.area = this.area + ta;
    this.cx = this.cx + ((ta * (p1.x + p0.x + this.tx)) / 3.0);
    this.cz = this.cz + ((ta * (p1.z + p0.z + this.tz)) / 3.0);

    this.cx = this.cx / this.area;
    this.cz = this.cz / this.area;

  }// end constructor

  /**
   * Gets the area.
   *
   * @return the area
   */
  public double getArea() {
    return this.area;
  }

  /**
   * Gets the area X.
   *
   * @return the area X
   */
  public double getAreaX() {
    return this.cx;
  }

  /**
   * Gets the area Z.
   *
   * @return the area Z
   */
  public double getAreaZ() {
    return this.cz;
  }

  /**
   * Gets the mid X.
   *
   * @return the mid X
   */
  public double getMidX() {
    return this.tx;
  }

  /**
   * Gets the mid Z.
   *
   * @return the mid Z
   */
  public double getMidZ() {
    return this.tz;
  }

  /**
   * Tri area.
   *
   * @param x1 the x 1
   * @param y1 the y 1
   * @param x2 the x 2
   * @param y2 the y 2
   * @param x3 the x 3
   * @param y3 the y 3
   * @return the double
   */
  public double TriArea(final double x1, final double y1, final double x2, final double y2,
      final double x3, final double y3) {
    double a;
    if ((x1 <= x2) && (x1 <= x3)) {
      a = 0.5 * Math.abs(((x2 - x1) * (y3 - y1)) - ((x3 - x1) * (y2 - y1)));
    } else if ((x2 <= x1) && (x2 <= x3)) {
      a = 0.5 * Math.abs(((x3 - x2) * (y1 - y2)) - ((x1 - x2) * (y3 - y2)));
    } else {
      a = 0.5 * Math.abs(((x1 - x3) * (y2 - y3)) - ((x2 - x3) * (y1 - y3)));
    }
    return a;
  }

}// end xzArea
