package geom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class xzArea {
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
