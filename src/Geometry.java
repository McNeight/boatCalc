/* Geometric Utilities */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

class Dir extends Point {
  public Dir() {}

  public Dir(final double x, final double y, final double z) {
    this.setDir(x, y, z);
  }

  public Dir(final Point p) {
    this.setDir(p.x, p.y, p.z);
  }

  @Override
  public double getX() {
    return this.x;
  }

  @Override
  public double getY() {
    return this.y;
  }

  @Override
  public double getZ() {
    return this.z;
  }

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


class Interp {
  double[] x;
  double[] y;

  public Interp(final ArrayList ax, final ArrayList ay) {
    this.x = new double[ax.size()];
    this.y = new double[ay.size()];
    int i;
    for (i = 0; i < ax.size(); i++) {
      this.x[i] = ((Double) ax.get(i));
      this.y[i] = ((Double) ay.get(i));
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


class Line extends Object {
  public int length;
  public Point[] points;

  public Line(final int n) {
    this.points = new Point[n];
    this.length = this.points.length;
  }

  public Line(final Point[] p) {
    this.points = p;
    this.length = this.points.length;
  }

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

  public Point getPoint(final int i) {
    return this.points[i];
  }

  public void setPoint(final Point p, final int i) {
    this.points[i] = p;
  }

  public boolean valid(final int i) {
    return this.points[i].valid;
  }

  public double valX(final int i) {
    return this.points[i].x;
  }

  public double valY(final int i) {
    return this.points[i].y;
  }

  public double valZ(final int i) {
    return this.points[i].z;
  }
} // end class Line


class Point extends Object {
  public boolean valid;
  public double x, y, z;

  public Point() {
    this.valid = true;
  }

  public Point(final boolean b) {
    this.setX(0);
    this.setY(0);
    this.setZ(0);
    this.valid = b;
  }

  public Point(final double x, final double y, final double z) {
    this.setX(x);
    this.setY(y);
    this.setZ(z);
    this.valid = true;
  }

  public Point(final double x, final double y, final double z, final boolean v) {
    this.setX(x);
    this.setY(y);
    this.setZ(z);
    this.valid = v;
  }

  public Point(final Point p) {
    this.setX(p.x);
    this.setY(p.y);
    this.setZ(p.z);
    this.valid = true;
  }

  public double angleXZ() {
    return Math.atan2(this.x, -this.z);
  }

  public double angleXZ(final double xAdj, final double zAdj) {
    return Math.atan2(this.x - xAdj, zAdj - this.z);
  }

  public double angleYZ() {
    return Math.atan2(this.y, -this.z);
  }

  public double angleYZ(final double yAdj, final double zAdj) {
    return Math.atan2(this.y - yAdj, zAdj - this.z);
  }

  public boolean equals(final Point p) {
    return ((p.x == this.x) && (p.y == this.y) && (p.z == this.z) && (p.valid == this.valid));
  }

  public double getX() {
    return this.x;
  }

  public double getY() {
    return this.y;
  }

  public double getZ() {
    return this.z;
  }

  public Point minus(final Point p) {
    return this.plus(-1, p);
  }

  public Point plus(final double a, final Point p) {
    final Point r = new Point();
    r.x = this.x + (a * p.x);
    r.y = this.y + (a * p.y);
    r.z = this.z + (a * p.z);
    return r;
  }

  public Point plus(final Point p) {
    final Point r = new Point();
    r.x = this.x + p.x;
    r.y = this.y + p.y;
    r.z = this.z + p.z;
    return r;
  }

  public void setX(final double x) {
    this.x = x;
  }

  public void setY(final double y) {
    this.y = y;
  }

  public void setZ(final double z) {
    this.z = z;
  }
} // end class Point


class XCompare implements Comparator {
  @Override
  public int compare(final Object o1, final Object o2) {
    final Point p1 = (Point) o1;
    final Point p2 = (Point) o2;
    return this.sgn(p1.getX() - p2.getX());
  }

  @Override
  public boolean equals(final Object o) {
    return o.equals(this);
  }

  private int sgn(final double a) {
    if (a > 0) {
      return 1;
    } else if (a < 0) {
      return -1;
    } else {
      return 0;
    }
  }
} // end class XCompare


class xzArea {
  private double area;
  private double cx, cz;
  private double tx, tz;

  public xzArea(final ArrayList a) {
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
      final Point p = (Point) a.get(i);
      this.tx = this.tx + p.x;
      this.tz = this.tz + p.z;
    }
    this.tx = this.tx / a.size();
    this.tz = this.tz / a.size();

    final XZCompare xzComp = new XZCompare();
    xzComp.setAdj(this.tx, this.tz);
    final SortedSet ss = new TreeSet(xzComp);
    for (int i = 0; i < a.size(); i++) {
      ss.add(a.get(i));
    }
    final Iterator si = ss.iterator();
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

  public double getArea() {
    return this.area;
  }

  public double getAreaX() {
    return this.cx;
  }

  public double getAreaZ() {
    return this.cz;
  }

  public double getMidX() {
    return this.tx;
  }

  public double getMidZ() {
    return this.tz;
  }

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


class XZCompare implements Comparator {
  double xAdj = 0;
  double zAdj = 0;

  @Override
  public int compare(final Object o1, final Object o2) {
    final Point p1 = (Point) o1;
    final Point p2 = (Point) o2;
    return this.sgn(p1.angleXZ(this.xAdj, this.zAdj) - p2.angleXZ(this.xAdj, this.zAdj));
  }

  @Override
  public boolean equals(final Object o) {
    return o.equals(this);
  }

  public void setAdj(final double x, final double z) {
    this.xAdj = x;
    this.zAdj = z;
  }

  private int sgn(final double a) {
    if (a > 0) {
      return 1;
    } else if (a < 0) {
      return -1;
    } else {
      return 0;
    }
  }
} // end class XZCompare


class YCompare implements Comparator {
  @Override
  public int compare(final Object o1, final Object o2) {
    final Point p1 = (Point) o1;
    final Point p2 = (Point) o2;
    return this.sgn(p1.getY() - p2.getY());
  }

  @Override
  public boolean equals(final Object o) {
    return o.equals(this);
  }

  private int sgn(final double a) {
    if (a > 0) {
      return 1;
    } else if (a < 0) {
      return -1;
    } else {
      return 0;
    }
  }
} // end class YCompare


class YZCompare implements Comparator {
  double yAdj = 0;
  double zAdj = 0;

  @Override
  public int compare(final Object o1, final Object o2) {
    final Point p1 = (Point) o1;
    final Point p2 = (Point) o2;
    return this.sgn(p1.angleYZ(this.yAdj, this.zAdj) - p2.angleYZ(this.yAdj, this.zAdj));
  }

  @Override
  public boolean equals(final Object o) {
    return o.equals(this);
  }

  public void setAdj(final double y, final double z) {
    this.yAdj = y;
    this.zAdj = z;
  }

  private int sgn(final double a) {
    if (a > 0) {
      return 1;
    } else if (a < 0) {
      return -1;
    } else {
      return 0;
    }
  }
} // end class YZCompare



class ZCompare implements Comparator {
  @Override
  public int compare(final Object o1, final Object o2) {
    final Point p1 = (Point) o1;
    final Point p2 = (Point) o2;
    return this.sgn(p1.getZ() - p2.getZ());
  }

  @Override
  public boolean equals(final Object o) {
    return o.equals(this);
  }

  private int sgn(final double a) {
    if (a > 0) {
      return 1;
    } else if (a < 0) {
      return -1;
    } else {
      return 0;
    }
  }
} // end class ZCompare


