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
import geom.Point;
import geom.XZCompare;

// TODO: Auto-generated Javadoc
/**
 * The Class Centerboard.
 */
public class Centerboard implements Cloneable {

  /** The board. */
  public rscFoil board;

  /** The valid. */
  public boolean valid = false;

  /** The angle. */
  private double angle;

  /** The changed. */
  private boolean changed;

  /** The hx max. */
  private double hx_min, hx_max;

  /** The min hull. */
  private double[] minHull;

  /** The pivot. */
  private final Point pivot;

  /** The r CB. */
  private final double[][] rCB;

  /**
   * Instantiates a new centerboard.
   */
  public Centerboard() {
    this.board = new rscFoil();
    this.board.setCB(true);
    this.pivot = new Point(0, 0, 0);
    this.rCB = new double[2][4];
    this.angle = 0;
    this.changed = true;
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
      final Centerboard r = (Centerboard) super.clone();
      r.board = (rscFoil) this.board.clone();
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
    if (this.board.use) {
      a = a + this.board.getArea();
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
    if (this.board.use) {
      x = x + (this.board.getArea() * this.board.getAreaX());
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
    if (this.board.use) {
      x = x + (this.board.getArea() * this.board.getAreaY());
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
    if (this.board.use) {
      x = Math.max(x, this.board.getMaxX());
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
    if (this.board.use) {
      x = Math.max(x, this.board.getMaxY());
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
    if (this.board.use) {
      x = Math.min(x, this.board.getMinX());
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
    if (this.board.use) {
      x = Math.min(x, this.board.getMinY());
    }
    return x;
  }

  /**
   * Gets the pivot angle.
   *
   * @return the pivot angle
   */
  public double getPivotAngle() {
    return this.angle;
  }

  /**
   * Gets the pivot X.
   *
   * @return the pivot X
   */
  public double getPivotX() {
    return this.pivot.x;
  }

  /**
   * Gets the pivot Z.
   *
   * @return the pivot Z
   */
  public double getPivotZ() {
    return this.pivot.z;
  }

  /**
   * Gets the rx.
   *
   * @param i the i
   * @return the rx
   */
  public double getRX(final int i) {
    if (this.changed) {
      this.setCB();
    }
    return this.rCB[0][i];
  }

  /**
   * Gets the rz.
   *
   * @param i the i
   * @return the rz
   */
  public double getRZ(final int i) {
    if (this.changed) {
      this.setCB();
    }
    return this.rCB[1][i];
  }

  /**
   * Sets the base.
   *
   * @param b the new base
   */
  public void setBase(final double b) {
    this.board.setBase(b);
    this.changed = true;
  }

  /**
   * Sets the min hull.
   *
   * @param mH the m H
   * @param xmin the xmin
   * @param xmax the xmax
   * @param incs the incs
   */
  public void setMinHull(final double[] mH, final double xmin, final double xmax, final int incs) {
    this.minHull = mH;
    this.hx_min = xmin;
    this.hx_max = xmax;
    this.changed = true;
  }

  /**
   * Sets the pivot angle.
   *
   * @param x the new pivot angle
   */
  public void setPivotAngle(final double x) {
    this.angle = x;
    this.changed = true;
  }

  /**
   * Sets the pivot X.
   *
   * @param x the new pivot X
   */
  public void setPivotX(final double x) {
    this.pivot.x = x;
    this.changed = true;
  }

  /**
   * Sets the pivot Z.
   *
   * @param x the new pivot Z
   */
  public void setPivotZ(final double x) {
    this.pivot.z = x;
    this.changed = true;
  }

  /**
   * Sets the RX.
   *
   * @param i the i
   * @param x the x
   */
  public void setRX(final int i, final double x) {
    this.rCB[0][i] = x;
    this.changed = true;
  }

  /**
   * Sets the RZ.
   *
   * @param i the i
   * @param x the x
   */
  public void setRZ(final int i, final double x) {
    this.rCB[1][i] = x;
    this.changed = true;
  }

  /**
   * Sets the CB.
   */
  private void setCB() {
    final double sinang = Math.sin(Math.toRadians(this.angle));
    final double cosang = Math.cos(Math.toRadians(this.angle));
    final double xp = this.pivot.x;
    final double yp = this.pivot.z + this.board.getBase();
    for (int i = 0; i < 4; i++) {
      final double x = this.board.getParamX(i);
      final double y = this.board.getParamY(i) + this.board.getBase();
      this.rCB[0][i] = (xp + (cosang * (x - xp))) - (sinang * (y - yp));
      this.rCB[1][i] = yp + (sinang * (x - xp)) + (cosang * (y - yp));
    }

    // computed wetted points
    // step one; circulate perimeter including mid-points
    // save points below keel or at keel
    // note max and min of points at keel

    final ArrayList<Point> wp = new ArrayList<>();
    // set up "prior" point
    final double ox = this.rCB[0][3];
    double tt = (100.0 * (ox - this.hx_min)) / (this.hx_max - this.hx_min);
    Math.min(Math.max((int) tt, 0), 99);

    for (int i = 0; i < 4; i++) {
      int k = i + 1;
      if (k == 1) {
        k = 0;
      }
      for (int j = 0; j <= 1; j++) {
        // calc location
        final double tx = this.rCB[0][i] + (j * 0.5 * (this.rCB[0][j] - this.rCB[0][i]));
        final double ty = this.rCB[1][i] + (j * 0.5 * (this.rCB[1][j] - this.rCB[1][i]));
        // find hull depth
        tt = (100.0 * (tx - this.hx_min)) / (this.hx_max - this.hx_min);
        final int ti = Math.min(Math.max((int) tt, 0), 99);
        if (tx < this.minHull[ti]) {
          wp.add(new Point(tx, 0, ty));
        }
      }
    }

    // skip adding additional points for now

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
    this.board.setWetPts(xzComp);
    // board.sp = new TreeSet(xzComp);
    for (int i = 0; i < wp.size(); i++) {
      this.board.addWetPt(wp.get(i));
    }

    this.changed = false;
  }

} // end Centerboard
