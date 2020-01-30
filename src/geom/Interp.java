package geom;

import java.util.ArrayList;

public class Interp {
  double[] x;
  double[] y;

  public Interp(final ArrayList<Double> ax, final ArrayList<Double> ay) {
    this.x = new double[ax.size()];
    this.y = new double[ay.size()];
    int i;
    for (i = 0; i < ax.size(); i++) {
      this.x[i] = (ax.get(i));
      this.y[i] = (ay.get(i));
    }
  }

  public Interp(final int n) {
    this.x = new double[n];
    this.y = new double[n];
  }

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

  public double getX(final int i) {
    return this.x[i];
  }

  public double getY(final int i) {
    return this.y[i];
  }

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

  public void setX(final int i, final double v) {
    this.x[i] = v;
  }

  public void setXY(final int i, final double v, final double w) {
    this.x[i] = v;
    this.y[i] = w;
  }

  public void setY(final int i, final double v) {
    this.y[i] = v;
  }

  public int size() {
    return this.x.length;
  }


} // end CLASS Interp
