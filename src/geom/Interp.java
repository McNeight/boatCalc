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

// TODO: Auto-generated Javadoc
/**
 * The Class Interp.
 */
public class Interp {

  /** The x. */
  double[] x;

  /** The y. */
  double[] y;

  /**
   * Instantiates a new interp.
   *
   * @param ax the ax
   * @param ay the ay
   */
  public Interp(final ArrayList<Double> ax, final ArrayList<Double> ay) {
    this.x = new double[ax.size()];
    this.y = new double[ay.size()];
    int i;
    for (i = 0; i < ax.size(); i++) {
      this.x[i] = (ax.get(i));
      this.y[i] = (ay.get(i));
    }
  }

  /**
   * Instantiates a new interp.
   *
   * @param n the n
   */
  public Interp(final int n) {
    this.x = new double[n];
    this.y = new double[n];
  }

  /**
   * Instantiates a new interp.
   *
   * @param l the l
   * @param X the x
   * @param Y the y
   */
  public Interp(final Line l, final String X, final String Y) {
    int i, n;
    // count valid points
    n = 0;
    for (i = 0; i < l.length; i++) {
      if (l.valid(i)) {
        n++;
      }
    }
    this.x = new double[n];
    this.y = new double[n];
    // move data
    n = 0;
    for (i = 0; i < l.length; i++) {
      if (l.valid(i)) {
        if (0 == X.compareTo("X")) {
          this.x[n] = l.valX(i);
        }
        if (0 == X.compareTo("Y")) {
          this.x[n] = l.valY(i);
        }
        if (0 == X.compareTo("Z")) {
          this.x[n] = l.valZ(i);
        }
        if (0 == Y.compareTo("X")) {
          this.y[n] = l.valX(i);
        }
        if (0 == Y.compareTo("Y")) {
          this.y[n] = l.valY(i);
        }
        if (0 == Y.compareTo("Z")) {
          this.y[n] = l.valZ(i);
        }
        n++;
      }
    }
  }// end constuctor from Line

  /**
   * Gets the x.
   *
   * @param i the i
   * @return the x
   */
  public double getX(final int i) {
    return this.x[i];
  }

  /**
   * Gets the y.
   *
   * @param i the i
   * @return the y
   */
  public double getY(final int i) {
    return this.y[i];
  }

  /**
   * Interp 4 P.
   *
   * @param v the v
   * @return the double
   */
  public double interp4P(final double v) {
    double t;
    double p;
    int i, j;
    int imin, imax;
    i = 0;
    while ((this.x[i] < v) && (i < (this.x.length - 1))) {
      i++;
    }
    if (i <= 1) {
      imin = 0;
      imax = Math.min(2, this.x.length - 1);
    } else if (i > (this.x.length - 2)) {
      imin = Math.max(0, this.x.length - 3);
      imax = this.x.length - 1;
    } else {
      imin = Math.max(0, i - 2);
      imax = Math.min(imin + 3, this.x.length - 1);
    }
    t = 0;
    for (i = imin; i <= imax; i++) {
      p = 1;
      for (j = imin; j <= imax; j++) {
        if (i == j) {
          p = p * this.y[i];
        } else {
          p = (p * (v - this.x[j])) / (this.x[i] - this.x[j]);
        }
      }
      t = t + p;
    }
    return t;
  }

  /**
   * Interp LG.
   *
   * @param v the v
   * @return the double
   */
  public double interpLG(final double v) {
    double t;
    double p;
    int i, j;
    t = 0;
    for (i = 0; i < this.x.length; i++) {
      p = 1;
      for (j = 0; j < this.x.length; j++) {
        if (i == j) {
          p = p * this.y[i];
        } else {
          p = (p * (v - this.x[j])) / (this.x[i] - this.x[j]);
        }
      }
      t = t + p;
    }
    return t;
  }

  /**
   * Left linear.
   *
   * @param v the v
   * @return the double
   */
  public double leftLinear(final double v) {
    final int m = this.x.length - 2;
    for (int i = 0; i <= m; i++) {
      if ((this.x[i + 1] == v) && (v == this.x[i])) {
        return (this.y[i]);
      }
      if ((this.x[i] <= v) && (v <= this.x[i + 1])) {
        return (this.y[i]
            + (((this.y[i + 1] - this.y[i]) * (v - this.x[i])) / (this.x[i + 1] - this.x[i])));
      }
      if ((this.x[i] >= v) && (v >= this.x[i + 1])) {
        return (this.y[i]
            + (((this.y[i + 1] - this.y[i]) * (v - this.x[i])) / (this.x[i + 1] - this.x[i])));
      }
    }
    return this.y[this.x.length - 1];
  }

  /**
   * Left zero.
   *
   * @return the double
   */
  public double leftZero() {
    if (this.y[0] >= this.y[1]) {
      return this.x[0];
    }
    double x1 = this.x[0];
    double y1 = this.y[0];
    double x2 = this.x[1];
    double y2 = this.y[1];
    double x0 = 0;
    double y0 = 1000000;
    int i = 0;
    while ((Math.abs(y0) > 0.001) && (i < 10)) {
      x0 = x2 - ((y2 * (x2 - x1)) / (y2 - y1));
      y0 = this.interp4P(x0);
      x1 = x2;
      y1 = y2;
      x2 = x0;
      y2 = y0;
      i++;
    }
    return x0;
  } // end leftZero


  /**
   * Right linear.
   *
   * @param v the v
   * @return the double
   */
  public double rightLinear(final double v) {
    for (int i = this.x.length - 1; i >= 1; i--) {
      if ((this.x[i - 1] == v) && (v == this.x[i])) {
        return (this.y[i]);
      }
      if ((this.x[i - 1] <= v) && (v <= this.x[i])) {
        return (this.y[i - 1]
            + (((this.y[i] - this.y[i - 1]) * (v - this.x[i - 1])) / (this.x[i] - this.x[i - 1])));
      }
      if ((this.x[i - 1] >= v) && (v >= this.x[i])) {
        return (this.y[i - 1]
            + (((this.y[i] - this.y[i - 1]) * (v - this.x[i - 1])) / (this.x[i] - this.x[i - 1])));
      }
    }
    return this.y[0];
  }

  /**
   * Right zero.
   *
   * @return the double
   */
  public double rightZero() {
    final int n = this.x.length;
    if (this.y[n - 2] <= this.y[n - 1]) {
      return this.x[n - 1];
    }
    double x1 = this.x[n - 2];
    double y1 = this.y[n - 2];
    double x2 = this.x[n - 1];
    double y2 = this.y[n - 1];
    double x0 = 0;
    double y0 = 1000000;
    int i = 0;
    while ((Math.abs(y0) > 0.001) && (i < 10)) {
      x0 = x2 - ((y2 * (x2 - x1)) / (y2 - y1));
      y0 = this.interp4P(x0);
      x1 = x2;
      y1 = y2;
      x2 = x0;
      y2 = y0;
      i++;
    }
    return x0;
  } // end rightZero

  /**
   * Sets the X.
   *
   * @param i the i
   * @param v the v
   */
  public void setX(final int i, final double v) {
    this.x[i] = v;
  }

  /**
   * Sets the XY.
   *
   * @param i the i
   * @param v the v
   * @param w the w
   */
  public void setXY(final int i, final double v, final double w) {
    this.x[i] = v;
    this.y[i] = w;
  }

  /**
   * Sets the Y.
   *
   * @param i the i
   * @param v the v
   */
  public void setY(final int i, final double v) {
    this.y[i] = v;
  }

  /**
   * Size.
   *
   * @return the int
   */
  public int size() {
    return this.x.length;
  }


} // end CLASS Interp
