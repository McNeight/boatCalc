/**
 *
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Iterator;
import javax.swing.JPanel;
import geom.Point;

public class hdBody extends JPanel {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  /**
   *
   */
  private final boatCalc boatCalc;
  Dimension d;
  String title;
  boolean type;

  public hdBody(boatCalc boatCalc, final int x, final int y) {
    this.boatCalc = boatCalc;
    this.d = new Dimension(x, y);
    this.setBackground(Color.white);
  }

  @Override
  public Dimension getPreferredSize() {
    return this.d;
  }

  @Override
  protected void paintComponent(final Graphics g) {
    super.paintComponent(g);
    final double sinang = Math.sin(Math.toRadians(this.boatCalc.hull.angHeel));
    final double cosang = Math.cos(Math.toRadians(this.boatCalc.hull.angHeel));

    final double mx = this.getWidth();
    final double my = this.getHeight();
    final int ix = (int) mx;
    final int iy = (int) my;
    final int xb = ix / 2;
    final int yb = iy / 2;
    Point p1, p2;
    int u, v, w, z;
    final double r = (0.85 * xb) / this.boatCalc.hull.gy_max;
    g.clearRect(0, 0, ix, iy);
    g.drawString(this.title, 10, 10);

    // draw axes
    g.setColor(Color.red);
    g.drawLine(xb, 5, xb, iy - 5);
    g.drawLine(5, yb, ix - 5, yb);
    g.setColor(Color.black);

    int jLow, jHigh;
    if (this.type) {
      jLow = 0;
      jHigh = this.boatCalc.hull.Stations.length / 2;
    } else {
      jLow = this.boatCalc.hull.Stations.length / 2;
      jHigh = this.boatCalc.hull.Stations.length - 1;
    }
    for (int j = jLow; j <= jHigh; j++) {
      // draw station
      final Iterator<Point> si = this.boatCalc.hull.getStation(j, this.boatCalc.hull.angHeel);
      p1 = si.next();
      while (si.hasNext()) {
        p2 = si.next();
        u = xb + (int) (r * p1.y);
        v = yb - (int) (r * p1.z);
        w = xb + (int) (r * p2.y);
        z = yb - (int) (r * p2.z);
        g.drawLine(u, v, w, z);
        p1 = p2;
      }
    }

    final int jm = this.boatCalc.hull.Stations.length / 2;
    final boolean bDraw = (this.type && (this.boatCalc.hull.CX <= this.boatCalc.hull.Stations[jm]))
        || (!this.type && (this.boatCalc.hull.CX >= this.boatCalc.hull.Stations[jm]));
    // put circle on computed CoG
    g.setColor(Color.red);
    u = xb + (int) (r * this.boatCalc.hull.hVals[this.boatCalc.hull.CY]);
    v = yb - (int) (r * this.boatCalc.hull.hVals[this.boatCalc.hull.CZ]);
    if (bDraw) {
      g.drawArc(u - 5, v - 5, 10, 10, 0, 360);
    }

    g.setColor(Color.blue);
    double rtw = 0;
    double rty = 0;
    double rtz = 0;
    for (int j = 0; j < 10; j++) {
      if (this.boatCalc.hull.wgtWgt[j] > 0) {
        final double ry = (cosang * this.boatCalc.hull.wgtY[j])
            - (sinang * (this.boatCalc.hull.wgtZ[j] + this.boatCalc.hull.base));
        final double rz = (sinang * this.boatCalc.hull.wgtY[j])
            + (cosang * (this.boatCalc.hull.wgtZ[j] + this.boatCalc.hull.base));
        if ((this.type && (this.boatCalc.hull.wgtX[j] <= this.boatCalc.hull.Stations[jm]))
            || (!this.type && (this.boatCalc.hull.wgtX[j] >= this.boatCalc.hull.Stations[jm]))) {
          w = xb + (int) (r * ry);
          z = yb - (int) (r * rz);
          g.fillRect(w - 3, z - 3, 6, 6);
        }
        rtw = rtw + this.boatCalc.hull.wgtWgt[j];
        rty = rty + (this.boatCalc.hull.wgtWgt[j] * ry);
        rtz = rtz + (this.boatCalc.hull.wgtWgt[j] * rz);
      }
    }
    if (bDraw && (rtw > 0)) {
      rty = rty / rtw;
      rtz = rtz / rtw;
      w = xb + (int) (r * rty);
      z = yb - (int) (r * rtz);
      g.drawArc(w - 5, z - 5, 10, 10, 0, 360);

      g.drawLine(w, z, w, v);
      g.setColor(Color.red);
      g.drawLine(u, v, w, v);
    }

  }// end paint

  public void setTitle(final String s) {
    this.title = s;
  }

  public void setType(final boolean b) {
    this.type = b;
  }
}// end hdBody
