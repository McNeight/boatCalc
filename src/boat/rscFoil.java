package boat;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import geom.Point;
import geom.XZCompare;
import geom.xzArea;

public class rscFoil implements Cloneable {

  public static int BL = 3;
  public static int BR = 2;
  public static int TL = 0;

  public static int TR = 1;
  private double ang;

  private double area;
  private double base;
  private boolean changed;

  private double cX;
  private double cY;
  private boolean isCB;
  private double maxX;

  private double maxY;
  private double minX;
  private double minY;

  private double[][] p;
  private SortedSet sp;

  public boolean use;
  private double wetArea;
  private double wetX;
  private double wetY;

  public rscFoil() {
    this.p = new double[2][4];
    this.use = false;
    this.changed = true;
  }

  public void addWetPt(final Object p) {
    this.sp.add(p);
  }

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

  public double getAngle() {
    return this.ang;
  }

  public double getArea() {
    if (this.changed) {
      this.setFoil();
    }
    return this.area;
  }

  public double getAreaX() {
    if (this.changed) {
      this.setFoil();
    }
    return this.cX;
  }

  public double getAreaY() {
    if (this.changed) {
      this.setFoil();
    }
    return this.cY;
  }

  public double getBase() {
    return this.base;
  }

  public double getMaxX() {
    if (this.changed) {
      this.setFoil();
    }
    return this.maxX;
  }

  public double getMaxY() {
    if (this.changed) {
      this.setFoil();
    }
    return this.maxY;
  }

  public double getMinX() {
    if (this.changed) {
      this.setFoil();
    }
    return this.minX;
  }

  public double getMinY() {
    if (this.changed) {
      this.setFoil();
    }
    return this.minY;
  }

  public double getParamX(final int i) {
    return this.p[0][i];
  }

  public double getParamY(final int i) {
    return this.p[1][i];
  }

  public double getWetArea() {
    if (this.changed) {
      this.setFoil();
    }
    return this.wetArea;
  }

  public SortedSet getWetPts() {
    if (this.changed) {
      this.setFoil();
    }
    return this.sp;
  }

  public double getWetX() {
    if (this.changed) {
      this.setFoil();
    }
    return this.wetX;
  }

  public double getWetY() {
    if (this.changed) {
      this.setFoil();
    }
    return this.wetY;
  }

  public boolean isCB() {
    return this.isCB;
  }

  public boolean isRS() {
    return !this.isCB;
  }

  public void setAngle(final double a) {
    this.ang = a;
  }

  public void setBase(final double b) {
    this.base = b;
  }

  public void setCB(final boolean b) {
    this.isCB = b;
  }

  public void setDir(final int i) {
    this.changed = true;
  }

  public void setFoil() {

    final ArrayList al = new ArrayList();
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

    final ArrayList wp = new ArrayList();

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
      final Point p = (Point) wp.get(i);
      tx = tx + p.x;
      tz = tz + p.z;
    }
    tx = tx / Math.max(wp.size(), 1);
    tz = tz / Math.max(wp.size(), 1);

    final XZCompare xzComp = new XZCompare();
    xzComp.setAdj(tx, tz);
    this.sp = new TreeSet(xzComp);
    for (int i = 0; i < wp.size(); i++) {
      this.sp.add(wp.get(i));
    }

    final xzArea xzWA = new xzArea(wp);
    this.wetArea = xzWA.getArea();
    this.wetX = xzWA.getAreaX();
    this.wetY = xzWA.getAreaZ();


    this.changed = false;

  }// end setFoil

  public void setParamX(final int i, final double x) {
    this.p[0][i] = x;
    this.changed = true;
  }

  public void setParamXY(final int i, final double x, final double y) {
    this.p[0][i] = x;
    this.p[1][i] = y;
    this.changed = true;
  }

  public void setParamY(final int i, final double y) {
    this.p[1][i] = y;
    this.changed = true;
  }

  public void setUse(final boolean b) {
    this.use = b;
    this.changed = true;
  }

  public void setWetPts(final XZCompare xzComp) {
    this.sp = new TreeSet(xzComp);
  }

  public void setX(final double x) {
    this.p[0][0] = x;
    this.changed = true;
  }

  public void setY(final double x) {
    this.p[1][0] = x;
    this.changed = true;
  }


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
