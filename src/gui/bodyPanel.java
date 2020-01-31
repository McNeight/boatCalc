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
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.JPanel;
import geom.Point;
import geom.YZCompare;

// TODO: Auto-generated Javadoc
/**
 * The Class bodyPanel.
 */
public class bodyPanel extends JPanel {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The boat calc. */
  private final boatCalc boatCalc;

  /** The d. */
  Dimension d;

  /**
   * Instantiates a new body panel.
   *
   * @param boatCalc the boat calc
   * @param x the x
   * @param y the y
   */
  public bodyPanel(final boatCalc boatCalc, final int x, final int y) {
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
   * Paint component.
   *
   * @param g the g
   */
  @Override
  protected void paintComponent(final Graphics g) {
    super.paintComponent(g);
    final Font bigFont = new Font("Serif", Font.PLAIN, 12);
    new Font("SansSerif", Font.PLAIN, 10);
    g.setFont(bigFont);

    final double my = this.getWidth();
    final double mz = this.getHeight();
    int ix = (int) my;
    int iy = (int) mz;
    g.clearRect(0, 0, ix, iy);
    g.drawString("Body", 10, 10);
    if (!this.boatCalc.hull.valid) {
      return;
    }

    Set<Point> s;
    Iterator<Point> si;
    Point p1, p2;
    YZCompare yzComp;
    final double[] stn = this.boatCalc.hull.Stations;

    int iw, iz;

    final int py = 2;
    final int pz = 2;
    final int pw = (int) my;
    final int ph = (int) mz - 4;

    int iy_min = py + 5;
    final int iy_max = (py + pw) - 25;
    final int iz_min = (pz + ph) - 5;
    final int iz_max = pz + 5;

    g.clearRect(py, pz, pw, ph);
    g.drawString("Body", 10, 12);

    // note: what would usually be gy_min is replaced by -gy_max

    double ry, rz, r;
    ry = (iy_max - iy_min) / (2 * this.boatCalc.hull.gy_max);
    rz = (iz_max - iz_min) / (this.boatCalc.hull.gz_max - this.boatCalc.hull.gz_min);
    r = Math.min(Math.abs(ry), Math.abs(rz));

    iy_min = (int) ((((my)) / 2.0) - (r * this.boatCalc.hull.gy_max));

    g.setColor(Color.red);
    ix = iy_min + (int) (r * (0 + this.boatCalc.hull.gy_max));
    iy = iz_min - (int) (r * (this.boatCalc.hull.gz_max - this.boatCalc.hull.gz_min));
    iw = iy_min + (int) (r * (0 + this.boatCalc.hull.gy_max));
    iz = iz_min - (int) (r * (this.boatCalc.hull.gz_min - this.boatCalc.hull.gz_min));
    g.drawLine(ix, iy, iw, iz);

    g.setColor(Color.blue);
    ix = iy_min + (int) (r * (2 * this.boatCalc.hull.gy_max));
    iy = iz_min - (int) (r * (0 - this.boatCalc.hull.gz_min));
    iw = iy_min + (int) (r * (this.boatCalc.hull.gy_min - this.boatCalc.hull.gy_min));
    iz = iz_min - (int) (r * (0 - this.boatCalc.hull.gz_min));
    g.drawLine(ix, iy, iw, iz);
    g.setColor(Color.black);

    int j;
    final int jm = stn.length / 2;
    for (j = 0; j < stn.length; j++) {

      s = new HashSet<>();

      double zmin = +1000000.0;
      double zmax = -1000000.0;

      for (int iHL = 0; iHL < this.boatCalc.hull.hLines.length; iHL++) {
        final double tx = stn[j];
        final double x_min = this.boatCalc.hull.hLines[iHL].min("X");
        final double x_max = this.boatCalc.hull.hLines[iHL].max("X");
        if ((x_min <= tx) && (tx <= x_max)) {
          final double ty = this.boatCalc.hull.hLines[iHL].hXY.interp4P(tx);
          final double tz = this.boatCalc.hull.hLines[iHL].hXZ.interp4P(tx);
          if (j <= jm) {
            s.add(new Point(tx, ty, tz));
          }
          if (j >= jm) {
            s.add(new Point(tx, -ty, tz));
          }
          zmin = Math.min(zmin, tz);
          zmax = Math.max(zmax, tz);
        }
      }


      yzComp = new YZCompare();
      yzComp.setAdj(0, zmax - (0.3 * (zmax - zmin)));
      final SortedSet<Point> ts = new TreeSet<>(yzComp);
      si = s.iterator();
      while (si.hasNext()) {
        ts.add(si.next());
      }
      si = ts.iterator();
      p1 = si.next();

      if (p1.z <= 0) {
        ix = iy_min + (int) (r * (0 + this.boatCalc.hull.gy_max));
        iy = iz_min - (int) (r * (p1.z - this.boatCalc.hull.gz_min));
        iw = iy_min + (int) (r * (p1.y + this.boatCalc.hull.gy_max));
        iz = iz_min - (int) (r * (p1.z - this.boatCalc.hull.gz_min));
        g.drawLine(ix, iy, iw, iz);
      }

      while (si.hasNext()) {
        p2 = si.next();
        ix = iy_min + (int) (r * (p1.y + this.boatCalc.hull.gy_max));
        iy = iz_min - (int) (r * (p1.z - this.boatCalc.hull.gz_min));
        iw = iy_min + (int) (r * (p2.y + this.boatCalc.hull.gy_max));
        iz = iz_min - (int) (r * (p2.z - this.boatCalc.hull.gz_min));
        g.drawLine(ix, iy, iw, iz);
        p1 = p2;
      }
      if (p1.z <= 0) {
        ix = iy_min + (int) (r * (p1.y + this.boatCalc.hull.gy_max));
        iy = iz_min - (int) (r * (p1.z - this.boatCalc.hull.gz_min));
        iw = iy_min + (int) (r * (0 + this.boatCalc.hull.gy_max));
        iz = iz_min - (int) (r * (p1.z - this.boatCalc.hull.gz_min));
        g.drawLine(ix, iy, iw, iz);
      }
    }



  }// end paint
}// end bodyPanel
