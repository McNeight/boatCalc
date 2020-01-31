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

import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import geom.Line;
import geom.Point;
import geom.YZCompare;
import geom.ZCompare;
import geom.hLine;
import geom.rawLine;
import util.bcFormat;
import util.bcUnits;

// TODO: Auto-generated Javadoc
/**
 * The Class Hull.
 */
public class Hull {

  /** The ang heel. */
  public double angHeel = 0.0;

  /** The base. */
  public double base;

  /** The bcf. */
  public bcFormat bcf;

  /** The b changed. */
  public boolean bChanged;

  /** The board. */
  public Centerboard board;

  /** The boatname. */
  public String boatname;

  /** The b stems. */
  public boolean bStems[];

  /** The cx. */
  public int CX = 1;

  /** The cy. */
  public int CY = 2;

  /** The cz. */
  public int CZ = 3;

  /** The designer. */
  public String designer;

  /** The disp. */
  public int DISP = 4;

  /** The Disp tri. */
  public Set<double[]> DispTri;

  /** The gx max. */
  public double gx_max;

  /** The gx min. */
  // derived data
  public double gx_min;

  /** The gy max. */
  public double gy_max;

  /** The gy min. */
  public double gy_min;

  /** The gz max. */
  public double gz_max;

  /** The gz min. */
  public double gz_min;

  /** The h lines. */
  public hLine[] hLines;

  /** The h vals. */
  public double[] hVals;

  /** The lwl left. */
  public double lwlLeft;

  /** The lwl right. */
  public double lwlRight;

  /** The ndiv. */
  // displacement curve data
  public int NDIV = 100;

  /** The Offsets. */
  public java.util.List<rawLine> Offsets;

  /** The rig. */
  public Rig rig;

  /** The rudder. */
  public Rudder rudder;

  /** The sarea. */
  // Constants
  public int SAREA = 0;

  /** The s lines. */
  public hLine[] sLines;

  /** The Stations. */
  public double[] Stations;

  /** The units. */
  public bcUnits units;

  /** The valid. */
  // basic data
  public boolean valid;

  /** The v disp. */
  public double[][] vDisp;


  /** The v wet. */
  public double[] vWet;

  /** The v WL. */
  public double[][] vWL;

  /** The wgt lbl. */
  public String wgtLbl[];

  /** The wgt wgt. */
  public double wgtWgt[];

  /** The wgt X. */
  public double wgtX[];

  /** The wgt Y. */
  public double wgtY[];

  /** The wgt Z. */
  public double wgtZ[];

  /** The dx. */
  double dx;

  /** The narea. */
  int NAREA = 8;

  /** The wetted. */
  int WETTED = 7;

  /** The wl left. */
  int WL_LEFT = 5;


  /** The wl right. */
  int WL_RIGHT = 6;

  /**
   * Instantiates a new hull.
   */
  // minimal constructor
  public Hull() {
    this.bcf = new bcFormat();

    this.wgtLbl = new String[10];
    this.wgtWgt = new double[10];
    this.wgtX = new double[10];
    this.wgtY = new double[10];
    this.wgtZ = new double[10];
    this.units = new bcUnits();
    this.bStems = new boolean[2];
    this.bStems[0] = true;
    this.bStems[1] = true;
    this.rig = new Rig();
    this.rudder = new Rudder();
    this.board = new Centerboard();
    this.valid = false;
  } // end constructor

  /**
   * Calc disp.
   */
  public void calcDisp() {
    this.setLWL();
    this.dx = (this.lwlRight - this.lwlLeft) / (this.NDIV);

    this.vDisp = new double[4][this.NDIV + 1];
    this.vWL = new double[2][this.NDIV + 1];
    this.vWet = new double[this.NDIV + 1];

    this.hVals = new double[this.NAREA];
    for (int i = 0; i < this.NAREA; i++) {
      this.hVals[i] = 0;
    }

    double[] rVals = new double[4];
    for (int i = 0; i <= this.NDIV; i++) {
      final double x = this.lwlLeft + (this.dx * i);
      rVals = this.getArea(x, this.angHeel, false);
      this.vDisp[this.CX][i] = x;
      this.vDisp[this.CY][i] = rVals[this.CY];
      this.vDisp[this.CZ][i] = rVals[this.CZ];
      this.vDisp[this.SAREA][i] = rVals[this.SAREA];
      this.vWL[0][i] = rVals[this.WL_LEFT];
      this.vWL[1][i] = rVals[this.WL_RIGHT];
      this.vWet[i] = rVals[this.WETTED];
    }

    // accumulate data
    for (int i = 0; i <= this.NDIV; i++) {
      this.hVals[this.SAREA] = this.hVals[this.SAREA] + this.vDisp[this.SAREA][i];
      this.hVals[this.CX] =
          this.hVals[this.CX] + (this.vDisp[this.SAREA][i] * this.vDisp[this.CX][i]);
      this.hVals[this.CY] =
          this.hVals[this.CY] + (this.vDisp[this.SAREA][i] * this.vDisp[this.CY][i]);
      this.hVals[this.CZ] =
          this.hVals[this.CZ] + (this.vDisp[this.SAREA][i] * this.vDisp[this.CZ][i]);
      if (i > 0) {
        this.hVals[this.DISP] = this.hVals[this.DISP]
            + (this.dx * 0.5 * (this.vDisp[this.SAREA][i] + this.vDisp[this.SAREA][i - 1]));
      }
    }
    if (this.hVals[this.SAREA] > 0) {
      this.hVals[this.CX] = this.hVals[this.CX] / this.hVals[this.SAREA];
      this.hVals[this.CY] = this.hVals[this.CY] / this.hVals[this.SAREA];
      this.hVals[this.CZ] = this.hVals[this.CZ] / this.hVals[this.SAREA];
    }
  }// end calc Disp


  /**
   * Gets the area.
   *
   * @param x the x
   * @param ang the ang
   * @param trisave the trisave
   * @return the area
   */
  public double[] getArea(final double x, final double ang, final boolean trisave) {
    final SortedSet<Point> ss = this.getStnSet(x, ang);
    final Iterator<Point> si = ss.iterator();
    Point p1, p2;
    double t1y, t1z, t2y, t2z, t3y, t3z;
    double area, cy, cz;
    double wlRight = 0;
    double wlLeft = 0;
    double wetted = 0;
    t3y = 0;
    t3z = 0;

    if (trisave) {
      this.DispTri = new HashSet<>();
    }

    final double[] rArea = new double[this.NAREA];
    for (int i = 0; i < this.NAREA; i++) {
      rArea[i] = 0;
    }

    if (ss.size() < 2) {
      return rArea;
    }
    if (!si.hasNext()) {
      return rArea;
    }

    p1 = ss.last();

    while (si.hasNext()) {

      p2 = si.next();

      t1y = p1.y;
      t1z = p1.z;
      t2y = p2.y;
      t2z = p2.z;
      if ((t1z > 0) && (t2z > 0)) {
        t1y = 0;
        t1z = 0;
        t2y = 0;
        t2z = 0;
      } else if ((t1z > 0) && (t2z <= 0)) {
        t1y = t2y + (((t1y - t2y) * (0 - t2z)) / (t1z - t2z));
        t1z = 0;
      } else if ((t1z <= 0) && (t2z > 0)) {
        t2y = t1y + (((t2y - t1y) * (0 - t1z)) / (t2z - t1z));
        t2z = 0;
      }

      cy = (t1y + t2y + t3y) / 3;
      cz = (t1z + t2z + t3z) / 3;
      area = this.TriArea(t1y, t1z, t2y, t2z, t3y, t3z);

      if (trisave) {
        final double[] tri = {t1y, t1z, t2y, t2z, t3y, t3z};
        this.DispTri.add(tri);
      }

      rArea[this.CY] = rArea[this.CY] + (area * cy);
      rArea[this.CZ] = rArea[this.CZ] + (area * cz);
      rArea[this.SAREA] = rArea[this.SAREA] + area;


      if ((t1z == 0) && (t1y < 0)) {
        wlLeft = t1y;
      } else if ((t1z == 0) && (t1y > 0)) {
        wlRight = t1y;
      } else if ((t2z == 0) && (t2y < 0)) {
        wlLeft = t2y;
      } else if ((t2z == 0) && (t2y > 0)) {
        wlRight = t2y;
      }

      wetted = wetted + Math.pow(Math.pow(t2y - t1y, 2) + Math.pow(t2z - t1z, 2), 0.5);

      p1 = p2;
    }
    if (rArea[this.SAREA] > 0) {
      rArea[this.CY] = rArea[this.CY] / rArea[this.SAREA];
      rArea[this.CZ] = rArea[this.CZ] / rArea[this.SAREA];
    }

    rArea[this.WL_LEFT] = wlLeft;
    rArea[this.WL_RIGHT] = wlRight;
    rArea[this.WETTED] = wetted;

    return rArea;
  } // end getArea (station=x)


  /**
   * Gets the station.
   *
   * @param tx the tx
   * @param ang the ang
   * @return the station
   */
  public Iterator<Point> getStation(final double tx, final double ang) {
    final SortedSet<Point> ts = this.getStnSet(tx, ang);
    return ts.iterator();
  }


  /**
   * Gets the station.
   *
   * @param i the i
   * @param ang the ang
   * @return the station
   */
  public Iterator<Point> getStation(final int i, final double ang) {
    final double tx = this.Stations[i];
    return this.getStation(tx, ang);
  }

  /**
   * Gets the stn set.
   *
   * @param tx the tx
   * @param ang the ang
   * @return the stn set
   */
  public SortedSet<Point> getStnSet(final double tx, final double ang) {
    final double sinang = Math.sin(Math.toRadians(ang));
    final double cosang = Math.cos(Math.toRadians(ang));
    double ty, tz;

    final Set<Point> s = new HashSet<>();

    double z_min = +1000000.0;
    double z_max = -1000000.0;

    for (int iHL = 0; iHL < this.hLines.length; iHL++) {
      final double x_min = this.hLines[iHL].min("X");
      final double x_max = this.hLines[iHL].max("X");
      if ((x_min <= tx) && (tx <= x_max)) {
        final double y = this.hLines[iHL].hXY.interp4P(tx);
        final double z = this.hLines[iHL].hXZ.interp4P(tx);
        z_min = Math.min(z_min, z);
        z_max = Math.max(z_max, z);

        // point on first side
        ty = (cosang * y) - (sinang * z);
        tz = (sinang * y) + (cosang * z);
        s.add(new Point(tx, ty, tz));
        // point for opposite side of boat
        ty = (cosang * (-y)) - (sinang * z);
        tz = (sinang * (-y)) + (cosang * z);
        s.add(new Point(tx, ty, tz));
      }
    }

    // left stem
    if (this.bStems[0]) {
      final double x_min = this.sLines[0].min("X");
      final double x_max = this.sLines[0].max("X");

      if ((x_min <= tx) && (tx <= x_max)) {
        final double y = this.sLines[0].hXY.leftLinear(tx);
        final double z = this.sLines[0].hXZ.leftLinear(tx);
        // point on first side
        ty = (cosang * y) - (sinang * z);
        tz = (sinang * y) + (cosang * z);
        s.add(new Point(tx, ty, tz));
        // point for opposite side of boat
        ty = (cosang * (-y)) - (sinang * z);
        tz = (sinang * (-y)) + (cosang * z);
        s.add(new Point(tx, ty, tz));
      }
    }

    // right stem
    if (this.bStems[1]) {
      final double x_min = this.sLines[1].min("X");
      final double x_max = this.sLines[1].max("X");

      if ((x_min <= tx) && (tx <= x_max)) {
        final double y = this.sLines[1].hXY.rightLinear(tx);
        final double z = this.sLines[1].hXZ.rightLinear(tx);
        // point on first side
        ty = (cosang * y) - (sinang * z);
        tz = (sinang * y) + (cosang * z);
        s.add(new Point(tx, ty, tz));
        // point for opposite side of boat
        ty = (cosang * (-y)) - (sinang * z);
        tz = (sinang * (-y)) + (cosang * z);
        s.add(new Point(tx, ty, tz));
      }
    }

    final YZCompare yzComp2 = new YZCompare();
    ty = -sinang * (z_min + (0.8 * (z_max - z_min)));
    tz = cosang * (z_min + (0.8 * (z_max - z_min)));
    yzComp2.setAdj(ty, tz);

    final Iterator<Point> si = s.iterator();
    final SortedSet<Point> ts = new TreeSet<>(yzComp2);
    while (si.hasNext()) {
      ts.add(si.next());
    }
    return ts;

  }// end getStation

  /**
   * New wgts.
   */
  public void newWgts() {
    this.wgtLbl = new String[10];
    this.wgtWgt = new double[10];
    this.wgtX = new double[10];
    this.wgtY = new double[10];
    this.wgtZ = new double[10];
    this.wgtLbl[0] = "Hull";
    this.wgtLbl[1] = "Ballast";
    this.wgtLbl[2] = "Engine";
    this.wgtLbl[3] = "Accomodation";
    this.wgtLbl[4] = "Crew";
    this.wgtLbl[5] = "Stores/Gear";
    this.wgtLbl[6] = "";
    this.wgtLbl[7] = "";
    this.wgtLbl[8] = "";
    this.wgtLbl[9] = "";

  }



  /**
   * Sets the lines.
   */
  public void setLines() {
    ListIterator<rawLine> l;
    final int n = this.Offsets.size();
    int i = 0;

    this.hLines = new hLine[n];

    l = this.Offsets.listIterator();
    while (l.hasNext()) {
      final rawLine rL = l.next();
      final Line ln = rL.ln;
      this.hLines[i] = new hLine(ln.points, this.base);
      if (i == 0) {
        this.gx_min = this.hLines[i].min("X");
        this.gx_max = this.hLines[i].max("X");
        this.gy_min = this.hLines[i].min("Y");
        this.gy_max = this.hLines[i].max("Y");
        this.gz_min = this.hLines[i].min("Z");
        this.gz_max = this.hLines[i].max("Z");
      } else {
        this.gx_min = Math.min(this.gx_min, this.hLines[i].min("X"));
        this.gx_max = Math.max(this.gx_max, this.hLines[i].max("X"));
        this.gy_min = Math.min(this.gy_min, this.hLines[i].min("Y"));
        this.gy_max = Math.max(this.gy_max, this.hLines[i].max("Y"));
        this.gz_min = Math.min(this.gz_min, this.hLines[i].min("Z"));
        this.gz_max = Math.max(this.gz_max, this.hLines[i].max("Z"));
      }
      i++;
    }

    // build stem/stern lines
    final ZCompare zComp = new ZCompare();
    final SortedSet<Point> sLow = new TreeSet<>(zComp);
    final SortedSet<Point> sHigh = new TreeSet<>(zComp);
    Iterator<Point> si;
    Line ln;
    Point p;
    final int m = this.Stations.length - 1;
    l = this.Offsets.listIterator();
    while (l.hasNext()) {
      final rawLine rL = l.next();
      ln = rL.ln;
      p = ln.getPoint(0);
      if (p.valid) {
        sLow.add(p);
      }
      p = ln.getPoint(m);
      if (p.valid) {
        sHigh.add(p);
      }
    }
    this.sLines = new hLine[2];

    ln = new Line(sLow.size());
    i = 0;
    si = sLow.iterator();
    while (si.hasNext()) {
      ln.setPoint(si.next(), i);
      i++;
    }
    this.sLines[0] = new hLine(ln.points, this.base);

    ln = new Line(sHigh.size());
    i = 0;
    si = sHigh.iterator();
    while (si.hasNext()) {
      ln.setPoint(si.next(), i);
      i++;
    }
    this.sLines[1] = new hLine(ln.points, this.base);

  } // end setLines

  /**
   * Sets the LWL.
   */
  public void setLWL() {
    double[] rVals;
    double x, y;

    // find left end
    int i = 0;
    x = this.Stations[i];
    rVals = this.getArea(x, this.angHeel, false);
    y = rVals[this.SAREA];
    if (y > 0) {
      this.lwlLeft = x;
    } else {
      while ((y == 0) && (i < (this.Stations.length - 1))) {
        i++;
        x = this.Stations[i];
        rVals = this.getArea(x, this.angHeel, false);
        y = rVals[this.SAREA];
      }
      if (i >= (this.Stations.length - 1)) {
        this.lwlLeft = this.Stations[0];
      } else {
        double xl, xr;
        xl = this.Stations[i - 1];
        xr = this.Stations[i];
        for (int j = 0; j <= 10; j++) {
          x = 0.5 * (xl + xr);
          rVals = this.getArea(x, this.angHeel, false);
          y = rVals[this.SAREA];
          if (y > 0) {
            xr = x;
          } else {
            xl = x;
          }
        }
        this.lwlLeft = x;
      }
    }
    // find right end
    i = this.Stations.length - 1;
    x = this.Stations[i];
    rVals = this.getArea(x, this.angHeel, false);
    y = rVals[this.SAREA];
    if (y > 0) {
      this.lwlRight = x;
    } else {
      while ((y == 0) && (i > 0)) {
        i--;
        x = this.Stations[i];
        rVals = this.getArea(x, this.angHeel, false);
        y = rVals[this.SAREA];
      }
      if (i <= 0) {
        this.lwlRight = this.Stations[this.Stations.length - 1];
      } else {
        double xl, xr;
        xl = this.Stations[i];
        xr = this.Stations[i + 1];
        for (int j = 0; j <= 10; j++) {
          x = 0.5 * (xl + xr);
          rVals = this.getArea(x, this.angHeel, false);
          y = rVals[this.SAREA];
          if (y > 0) {
            xl = x;
          } else {
            xr = x;
          }
        }
        this.lwlRight = x;
      }
    }
  }// end setLWL

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



} // end class Hull


