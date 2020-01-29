package boat;

import java.util.ArrayList;
import geom.Point;
import geom.XZCompare;

public class Centerboard implements Cloneable {
  private double angle;
  public rscFoil board;
  private boolean changed;
  private double hx_min, hx_max;
  private double[] minHull;
  private final Point pivot;

  private final double[][] rCB;
  public boolean valid = false;

  public Centerboard() {
    this.board = new rscFoil();
    this.board.setCB(true);
    this.pivot = new Point(0, 0, 0);
    this.rCB = new double[2][4];
    this.angle = 0;
    this.changed = true;
    this.valid = true;
  }

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

  public double getArea() {
    double a = 0;
    if (this.board.use) {
      a = a + this.board.getArea();
    }
    return a;
  }

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

  public double getMaxX() {
    double x = 0;
    if (this.board.use) {
      x = Math.max(x, this.board.getMaxX());
    }
    return x;
  }

  public double getMaxY() {
    double x = 0;
    if (this.board.use) {
      x = Math.max(x, this.board.getMaxY());
    }
    return x;
  }

  public double getMinX() {
    double x = 1000000;
    if (this.board.use) {
      x = Math.min(x, this.board.getMinX());
    }
    return x;
  }

  public double getMinY() {
    double x = 1000000;
    if (this.board.use) {
      x = Math.min(x, this.board.getMinY());
    }
    return x;
  }

  public double getPivotAngle() {
    return this.angle;
  }

  public double getPivotX() {
    return this.pivot.x;
  }

  public double getPivotZ() {
    return this.pivot.z;
  }

  public double getRX(final int i) {
    if (this.changed) {
      this.setCB();
    }
    return this.rCB[0][i];
  }

  public double getRZ(final int i) {
    if (this.changed) {
      this.setCB();
    }
    return this.rCB[1][i];
  }

  public void setBase(final double b) {
    this.board.setBase(b);
    this.changed = true;
  }

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

    final ArrayList wp = new ArrayList();
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
      final Point p = (Point) wp.get(i);
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

  public void setMinHull(final double[] mH, final double xmin, final double xmax, final int incs) {
    this.minHull = mH;
    this.hx_min = xmin;
    this.hx_max = xmax;
    this.changed = true;
  }

  public void setPivotAngle(final double x) {
    this.angle = x;
    this.changed = true;
  }

  public void setPivotX(final double x) {
    this.pivot.x = x;
    this.changed = true;
  }

  public void setPivotZ(final double x) {
    this.pivot.z = x;
    this.changed = true;
  }

  public void setRX(final int i, final double x) {
    this.rCB[0][i] = x;
    this.changed = true;
  }

  public void setRZ(final int i, final double x) {
    this.rCB[1][i] = x;
    this.changed = true;
  }

} // end Centerboard
