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
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Iterator;
import javax.swing.JPanel;
import geom.Point;

// TODO: Auto-generated Javadoc
/**
 * The Class stnBody.
 */
public class stnBody extends JPanel {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The boat calc. */
  private final boatCalc boatCalc;

  /** The d. */
  Dimension d;

  /** The title. */
  String title = "Station Area";

  /**
   * Instantiates a new stn body.
   *
   * @param boatCalc the boat calc
   * @param x the x
   * @param y the y
   */
  public stnBody(final boatCalc boatCalc, final int x, final int y) {
    this.boatCalc = boatCalc;
    this.d = new Dimension(x, y);
    this.setBackground(Color.white);
  }

  /**
   * Gets the preferred size.
   *
   * @return the preferred size
   */
  @Override
  public Dimension getPreferredSize() {
    return this.d;
  }

  /**
   * Sets the title.
   *
   * @param s the new title
   */
  public void setTitle(final String s) {
    this.title = s;
  }

  /**
   * Paint component.
   *
   * @param g the g
   */
  @Override
  protected void paintComponent(final Graphics g) {
    super.paintComponent(g);
    Math.sin(Math.toRadians(this.boatCalc.hull.angHeel));
    Math.cos(Math.toRadians(this.boatCalc.hull.angHeel));

    final double mx = this.getWidth();
    final double my = this.getHeight();
    final int ix = (int) mx;
    final int iy = (int) my;
    final int xb = ix / 2;
    final int yb = iy / 2;
    Point p1, p2;
    int u, v, w, z;
    final double r = (0.60 * xb) / this.boatCalc.hull.gy_max;
    g.clearRect(0, 0, ix, iy);
    int il = 15;
    g.drawString(this.title, il, 10);
    // draw axes
    g.setColor(Color.red);
    g.drawLine(xb, 5, xb, iy - 5);
    g.drawLine(5, yb, ix - 5, yb);
    g.setColor(Color.black);

    final double x = this.boatCalc.hull.lwlLeft + ((0.01 * this.boatCalc.dispStn.iPct)
        * (this.boatCalc.hull.lwlRight - this.boatCalc.hull.lwlLeft));
    double hsArea = 0;
    final double ty = 0, tz = 0;

    // draw station
    final Iterator<Point> si = this.boatCalc.hull.getStation(x, this.boatCalc.hull.angHeel);
    p1 = si.next();
    final Point p0 = new Point(p1);
    while (si.hasNext()) {
      p2 = si.next();
      u = xb + (int) (r * p1.y);
      v = yb - (int) (r * p1.z);
      w = xb + (int) (r * p2.y);
      z = yb - (int) (r * p2.z);
      g.drawLine(u, v, w, z);

      g.setColor(Color.red);
      hsArea = hsArea + this.boatCalc.hull.TriArea(ty, tz, p1.y, p1.z, p2.y, p2.z);
      u = xb + (int) (r * ty);
      v = yb - (int) (r * tz);
      w = xb + (int) (r * p1.y);
      z = yb - (int) (r * p1.z);
      g.drawLine(u, v, w, z);
      u = xb + (int) (r * p2.y);
      v = yb - (int) (r * p2.z);
      g.drawLine(u, v, w, z);
      w = xb + (int) (r * ty);
      z = yb - (int) (r * tz);
      g.drawLine(u, v, w, z);
      g.setColor(Color.black);

      p1 = p2;
    }
    p2 = p0;
    hsArea = hsArea + this.boatCalc.hull.TriArea(ty, tz, p1.y, p1.z, p2.y, p2.z);
    g.setColor(Color.red);
    u = xb + (int) (r * ty);
    v = yb - (int) (r * tz);
    w = xb + (int) (r * p1.y);
    z = yb - (int) (r * p1.z);
    g.drawLine(u, v, w, z);
    u = xb + (int) (r * p2.y);
    v = yb - (int) (r * p2.z);
    g.drawLine(u, v, w, z);
    w = xb + (int) (r * ty);
    z = yb - (int) (r * tz);
    g.drawLine(u, v, w, z);
    g.setColor(Color.black);



    final double[] rVals = this.boatCalc.hull.getArea(x, this.boatCalc.hull.angHeel, true);

    il += 25;
    g.drawString("Station (%): " + this.boatCalc.bcf.DF0d.format(this.boatCalc.dispStn.iPct), 10,
        il);
    il += 20;
    g.drawString("          @: " + this.boatCalc.bcf.DF0d.format(x), 10, il);
    il += 20;
    g.drawString("Displaced Area: "
        + this.boatCalc.bcf.DF1d
            .format(this.boatCalc.hull.units.coefArea() * rVals[this.boatCalc.hull.SAREA])
        + this.boatCalc.hull.units.lblArea(), 10, il);
    il += 20;
    g.drawString("    Total Area: "
        + this.boatCalc.bcf.DF1d.format(this.boatCalc.hull.units.coefArea() * hsArea)
        + this.boatCalc.hull.units.lblArea(), 10, il);

    g.setColor(Color.blue);
    final Iterator<double[]> di = this.boatCalc.hull.DispTri.iterator();
    while (di.hasNext()) {
      final double[] tri = di.next();
      u = xb + (int) (r * tri[0]);
      v = yb - (int) (r * tri[1]);
      w = xb + (int) (r * tri[2]);
      z = yb - (int) (r * tri[3]);
      g.drawLine(u, v, w, z);
      u = xb + (int) (r * tri[4]);
      v = yb - (int) (r * tri[5]);
      g.drawLine(u, v, w, z);
      w = xb + (int) (r * tri[0]);
      z = yb - (int) (r * tri[1]);
      g.drawLine(u, v, w, z);
    }

  }// end paint
}// end hdBody
