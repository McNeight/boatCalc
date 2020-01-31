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

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import geom.Point;
import geom.XZCompare;
import geom.xzArea;

// TODO: Auto-generated Javadoc
/**
 * The Class rscFoil.
 */
public class rscFoil implements Cloneable {

  /** The bl. */
  public static int BL = 3;

  /** The br. */
  public static int BR = 2;

  /** The tl. */
  public static int TL = 0;

  /** The tr. */
  public static int TR = 1;

  /** The use. */
  public boolean use;

  /** The ang. */
  private double ang;

  /** The area. */
  private double area;

  /** The base. */
  private double base;

  /** The changed. */
  private boolean changed;

  /** The c X. */
  private double cX;

  /** The c Y. */
  private double cY;

  /** The is CB. */
  private boolean isCB;

  /** The max X. */
  private double maxX;

  /** The max Y. */
  private double maxY;

  /** The min X. */
  private double minX;

  /** The min Y. */
  private double minY;

  /** The p. */
  private double[][] p;

  /** The sp. */
  private SortedSet<Point> sp;

  /** The wet area. */
  private double wetArea;

  /** The wet X. */
  private double wetX;

  /** The wet Y. */
  private double wetY;

  /**
   * Instantiates a new rsc foil.
   */
  public rscFoil() {
    this.p = new double[2][4];
    this.use = false;
    this.changed = true;
  }

  /**
   * Adds the wet pt.
   *
   * @param p the p
   */
  public void addWetPt(final Point p) {
    this.sp.add(p);
  }

  /**
   * Clone.
   *
   * @return the object
   */
  @Override
  public Object clone() {

    try {
      final rscFoil f = (rscFoil) super.clone();
      f.p = new double[2][4];
      f.p[0] = this.p[0].clone();
      f.p[1] = this.p[1].clone();
      f.changed = true;
      return f;
    } catch (final CloneNotSupportedException e) {
      throw new InternalError(e.toString());
    }

  } // end clone

  /**
   * Gets the angle.
   *
   * @return the angle
   */
  public double getAngle() {
    return this.ang;
  }

  /**
   * Gets the area.
   *
   * @return the area
   */
  public double getArea() {
    if (this.changed) {
      this.setFoil();
    }
    return this.area;
  }

  /**
   * Gets the area X.
   *
   * @return the area X
   */
  public double getAreaX() {
    if (this.changed) {
      this.setFoil();
    }
    return this.cX;
  }

  /**
   * Gets the area Y.
   *
   * @return the area Y
   */
  public double getAreaY() {
    if (this.changed) {
      this.setFoil();
    }
    return this.cY;
  }

  /**
   * Gets the base.
   *
   * @return the base
   */
  public double getBase() {
    return this.base;
  }

  /**
   * Gets the max X.
   *
   * @return the max X
   */
  public double getMaxX() {
    if (this.changed) {
      this.setFoil();
    }
    return this.maxX;
  }

  /**
   * Gets the max Y.
   *
   * @return the max Y
   */
  public double getMaxY() {
    if (this.changed) {
      this.setFoil();
    }
    return this.maxY;
  }

  /**
   * Gets the min X.
   *
   * @return the min X
   */
  public double getMinX() {
    if (this.changed) {
      this.setFoil();
    }
    return this.minX;
  }

  /**
   * Gets the min Y.
   *
   * @return the min Y
   */
  public double getMinY() {
    if (this.changed) {
      this.setFoil();
    }
    return this.minY;
  }

  /**
   * Gets the param X.
   *
   * @param i the i
   * @return the param X
   */
  public double getParamX(final int i) {
    return this.p[0][i];
  }

  /**
   * Gets the param Y.
   *
   * @param i the i
   * @return the param Y
   */
  public double getParamY(final int i) {
    return this.p[1][i];
  }

  /**
   * Gets the wet area.
   *
   * @return the wet area
   */
  public double getWetArea() {
    if (this.changed) {
      this.setFoil();
    }
    return this.wetArea;
  }

  /**
   * Gets the wet pts.
   *
   * @return the wet pts
   */
  public SortedSet<Point> getWetPts() {
    if (this.changed) {
      this.setFoil();
    }
    return this.sp;
  }

  /**
   * Gets the wet X.
   *
   * @return the wet X
   */
  public double getWetX() {
    if (this.changed) {
      this.setFoil();
    }
    return this.wetX;
  }

  /**
   * Gets the wet Y.
   *
   * @return the wet Y
   */
  public double getWetY() {
    if (this.changed) {
      this.setFoil();
    }
    return this.wetY;
  }

  /**
   * Checks if is cb.
   *
   * @return true, if is cb
   */
  public boolean isCB() {
    return this.isCB;
  }

  /**
   * Checks if is rs.
   *
   * @return true, if is rs
   */
  public boolean isRS() {
    return !this.isCB;
  }

  /**
   * Sets the angle.
   *
   * @param a the new angle
   */
  public void setAngle(final double a) {
    this.ang = a;
  }

  /**
   * Sets the base.
   *
   * @param b the new base
   */
  public void setBase(final double b) {
    this.base = b;
  }

  /**
   * Sets the cb.
   *
   * @param b the new cb
   */
  public void setCB(final boolean b) {
    this.isCB = b;
  }

  /**
   * Sets the dir.
   *
   * @param i the new dir
   */
  public void setDir(final int i) {
    this.changed = true;
  }

  /**
   * Sets the foil.
   */
  public void setFoil() {

    final ArrayList<Point> al = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      al.add(new Point(this.p[0][i], 0, this.p[1][i]));
    }
    final xzArea xzA = new xzArea(al);
    this.area = xzA.getArea();
    this.cX = xzA.getAreaX();
    this.cY = xzA.getAreaZ();

    this.maxX = xzA.getMidX();
    this.minX = xzA.getMidX();
    this.maxY = xzA.getMidZ();
    this.minY = xzA.getMidZ();

    final ArrayList<Point> wp = new ArrayList<>();

    for (int i = 0; i < 4; i++) {
      this.maxX = Math.max(this.maxX, this.p[0][i]);
      this.minX = Math.min(this.minX, this.p[0][i]);
      this.maxY = Math.max(this.maxY, this.p[1][i]);
      this.minY = Math.min(this.minY, this.p[1][i]);
      // find points below waterline
      if ((this.p[1][i] + this.base) < 0) {
        wp.add(new Point(this.p[0][i], 0, this.p[1][i] + this.base));
      }
    }

    // find line intersections with waterline
    double wx, wy;
    final double tb = -this.base;

    for (int i = 0; i < 4; i++) {
      int j = i + 1;
      if (j == 4) {
        j = 0;
      }

      if ((this.p[1][i] == tb) && (this.p[1][j] == tb)) {
        wp.add(new Point(this.p[0][i], 0, this.p[1][i] + this.base));
        wp.add(new Point(this.p[0][j], 0, this.p[1][j] + this.base));
      } else if ((this.p[1][i] <= tb) && (this.p[1][j] >= tb)) {
        final double f = (tb - this.p[1][i]) / (this.p[1][j] - this.p[1][i]);
        wx = this.p[0][i] + (f * (this.p[0][j] - this.p[0][i]));
        wy = this.p[1][i] + (f * (this.p[1][j] - this.p[1][i]));
        wp.add(new Point(wx, 0, wy + this.base));
      } else if ((this.p[1][j] <= tb) && (this.p[1][i] >= tb)) {
        final double f = (tb - this.p[1][i]) / (this.p[1][j] - this.p[1][i]);
        wx = this.p[0][i] + (f * (this.p[0][j] - this.p[0][i]));
        wy = this.p[1][i] + (f * (this.p[1][j] - this.p[1][i]));
        wp.add(new Point(wx, 0, wy + this.base));
      }
    }

    double tx = 0;
    double tz = 0;
    for (int i = 0; i < wp.size(); i++) {
      final Point p = wp.get(i);
      tx = tx + p.x;
      tz = tz + p.z;
    }
    tx = tx / Math.max(wp.size(), 1);
    tz = tz / Math.max(wp.size(), 1);

    final XZCompare xzComp = new XZCompare();
    xzComp.setAdj(tx, tz);
    this.sp = new TreeSet<>(xzComp);
    for (int i = 0; i < wp.size(); i++) {
      this.sp.add(wp.get(i));
    }

    final xzArea xzWA = new xzArea(wp);
    this.wetArea = xzWA.getArea();
    this.wetX = xzWA.getAreaX();
    this.wetY = xzWA.getAreaZ();


    this.changed = false;

  }// end setFoil

  /**
   * Sets the param X.
   *
   * @param i the i
   * @param x the x
   */
  public void setParamX(final int i, final double x) {
    this.p[0][i] = x;
    this.changed = true;
  }

  /**
   * Sets the param XY.
   *
   * @param i the i
   * @param x the x
   * @param y the y
   */
  public void setParamXY(final int i, final double x, final double y) {
    this.p[0][i] = x;
    this.p[1][i] = y;
    this.changed = true;
  }

  /**
   * Sets the param Y.
   *
   * @param i the i
   * @param y the y
   */
  public void setParamY(final int i, final double y) {
    this.p[1][i] = y;
    this.changed = true;
  }

  /**
   * Sets the use.
   *
   * @param b the new use
   */
  public void setUse(final boolean b) {
    this.use = b;
    this.changed = true;
  }

  /**
   * Sets the wet pts.
   *
   * @param xzComp the new wet pts
   */
  public void setWetPts(final XZCompare xzComp) {
    this.sp = new TreeSet<>(xzComp);
  }

  /**
   * Sets the x.
   *
   * @param x the new x
   */
  public void setX(final double x) {
    this.p[0][0] = x;
    this.changed = true;
  }

  /**
   * Sets the y.
   *
   * @param x the new y
   */
  public void setY(final double x) {
    this.p[1][0] = x;
    this.changed = true;
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
  public final double TriArea(final double x1, final double y1, final double x2, final double y2,
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

}// end rscFoil
