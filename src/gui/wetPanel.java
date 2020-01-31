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
import javax.swing.JPanel;

// TODO: Auto-generated Javadoc
/**
 * The Class wetPanel.
 */
public class wetPanel extends JPanel {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The boat calc. */
  private final boatCalc boatCalc;

  /** The d. */
  Dimension d;

  /** The i comp. */
  int[] iComp;

  /** The i cur. */
  int[] iCur;

  /**
   * Instantiates a new wet panel.
   *
   * @param boatCalc the boat calc
   * @param x the x
   * @param y the y
   */
  public wetPanel(final boatCalc boatCalc, final int x, final int y) {
    this.boatCalc = boatCalc;
    this.d = new Dimension(x, y);
    this.iComp = new int[this.boatCalc.hull.NDIV + 1];
    this.iCur = new int[this.boatCalc.hull.NDIV + 1];


  }// end constructor

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

    final double mx = this.getWidth();
    final double my = this.getHeight();
    final int ix = (int) mx;
    final int iy = (int) my;
    final int xb = 100;
    final int yb = (int) my - 50;

    g.clearRect(0, 0, ix, iy);

    final double rx = (mx - 200.0) / (this.boatCalc.hull.gx_max - this.boatCalc.hull.gx_min);
    final double ry = my / (this.boatCalc.hull.gy_max - this.boatCalc.hull.gy_min);
    final double r = Math.min(rx, ry);
    int iu, iv, iw, iz;
    iu = xb + (int) (r * (this.boatCalc.hull.gx_min - this.boatCalc.hull.gx_min));
    iv = yb + (int) (r * 0);
    iw = xb + (int) (r * (this.boatCalc.hull.gx_max - this.boatCalc.hull.gx_min));
    iz = yb + (int) (r * 0);

    // draw axis
    g.setColor(Color.red);
    g.drawLine(iu, iv, iw, iz);
    g.setColor(Color.black);

    if (this.boatCalc.disp.bComp) {
      g.setColor(Color.blue);
      iu = xb + (int) (r
          * (this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][0] - this.boatCalc.hull.gx_min));
      for (int i = 1; i <= this.boatCalc.hull.NDIV; i++) {
        iv = xb + (int) (r
            * (this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i] - this.boatCalc.hull.gx_min));
        g.drawLine(iu, this.iComp[i - 1], iv, this.iComp[i]);
        iu = iv;
      }
      g.setColor(Color.black);
    }

    double x = this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][0];
    double y = this.boatCalc.hull.vWet[0];
    iu = xb + (int) (r * (x - this.boatCalc.hull.gx_min));
    iv = yb - (int) (r * y);

    this.iCur[0] = iv;
    for (int i = 1; i <= this.boatCalc.hull.NDIV; i++) {
      x = this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i];
      y = this.boatCalc.hull.vWet[i];
      iw = xb + (int) (r * (x - this.boatCalc.hull.gx_min));
      iz = yb - (int) (r * y);
      g.drawLine(iu, iv, iw, iz);
      iu = iw;
      iv = iz;
      this.iCur[i] = iv;
    }

    // compute

    double wetX = this.boatCalc.hull.vWet[0] * this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][0];
    double wetSum = this.boatCalc.hull.vWet[0];
    double wetArea = 0;
    for (int i = 1; i <= this.boatCalc.hull.NDIV; i++) {
      wetX =
          wetX + (this.boatCalc.hull.vWet[i] * this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i]);
      wetSum = wetSum + this.boatCalc.hull.vWet[i];
      wetArea = wetArea + ((this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i]
          - this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i - 1]) * 0.5
          * (this.boatCalc.hull.vWet[i] + this.boatCalc.hull.vWet[i - 1]));
    }


    int il = 25;
    g.drawString("Wetted Surface", 10, il);
    g.drawString(this.boatCalc.bcf.DF1d.format(this.boatCalc.hull.units.coefArea() * wetArea)
        + this.boatCalc.hull.units.lblArea(), 125, il);
    il += 20;
    if (wetSum > 0) {
      wetX = wetX / wetSum;
      g.drawString("Ctr of Area @ Stn: ", 10, il);
      g.drawString(this.boatCalc.bcf.DF1d.format(wetX), 125, il);
    }


  } // end paintComponent
} // end wetPanel
