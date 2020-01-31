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
 * The Class hdPanel.
 */
public class hdPanel extends JPanel {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The boat calc. */
  private final boatCalc boatCalc;

  /** The b comp. */
  boolean bComp = false;

  /** The d. */
  Dimension d;

  /** The i comp. */
  int[][] iComp;

  /** The i cur. */
  int[][] iCur;

  /** The x comp. */
  double xComp = 0;

  /** The x cur. */
  double xCur = 0;

  /** The y comp. */
  double yComp = 0;

  /** The y cur. */
  double yCur = 0;

  /**
   * Instantiates a new hd panel.
   *
   * @param boatCalc the boat calc
   * @param x the x
   * @param y the y
   */
  public hdPanel(final boatCalc boatCalc, final int x, final int y) {
    this.boatCalc = boatCalc;
    this.d = new Dimension(x, y);
    // setBackground(Color.lightGray) ;
    this.iComp = new int[2][this.boatCalc.hull.NDIV + 1];
    this.iCur = new int[2][this.boatCalc.hull.NDIV + 1];
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

    final double mx = this.getWidth();
    final double my = this.getHeight();
    final int ix = (int) mx;
    final int iy = (int) my;
    final int xb = 100;
    final int yb = (int) my - 50;

    g.clearRect(0, 0, ix, iy);

    final double rx = (mx - 200.0) / (this.boatCalc.hull.gx_max - this.boatCalc.hull.gx_min);
    final double ry = (0.8 * my) / (this.boatCalc.hull.gy_max * this.boatCalc.hull.gy_max);
    int iu, iv, iw, iz;
    iu = xb + (int) (rx * (this.boatCalc.hull.gx_min - this.boatCalc.hull.gx_min));
    iv = yb + (int) (ry * 0);
    iw = xb + (int) (rx * (this.boatCalc.hull.gx_max - this.boatCalc.hull.gx_min));
    iz = yb + (int) (ry * 0);

    // draw horicontal axis
    g.setColor(Color.red);
    g.drawLine(iu, iv, iw, iz);
    g.setColor(Color.black);

    // draw basic area curve

    double maxArea = 0;
    double maxStn = 0;
    final double leftLWL = this.boatCalc.hull.lwlLeft;
    final double rightLWL = this.boatCalc.hull.lwlRight;

    double x = this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][0];
    double y = this.boatCalc.hull.vDisp[this.boatCalc.hull.SAREA][0];

    iu = xb + (int) (rx * (x - this.boatCalc.hull.gx_min));
    iv = yb - (int) (ry * y);

    if (this.bComp) {
      g.setColor(Color.blue);
      for (int i = 1; i <= this.boatCalc.hull.NDIV; i++) {
        g.drawLine(this.iComp[0][i - 1], this.iComp[1][i - 1], this.iComp[0][i], this.iComp[1][i]);
      }
      g.setColor(Color.cyan);
      iu = xb + (int) (rx * (this.xComp - this.boatCalc.hull.gx_min));
      iv = yb - (int) (ry * 0);
      iw = xb + (int) (rx * (this.xComp - this.boatCalc.hull.gx_min));
      iz = yb - (int) (ry * this.yComp);
      g.drawLine(iu, iv, iw, iz);
      g.setColor(Color.black);
    }

    x = this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][0];
    y = this.boatCalc.hull.vDisp[this.boatCalc.hull.SAREA][0];
    iu = xb + (int) (rx * (x - this.boatCalc.hull.gx_min));
    iv = yb - (int) (ry * y);

    this.iCur[0][0] = iu;
    this.iCur[1][0] = iv;
    for (int i = 1; i <= this.boatCalc.hull.NDIV; i++) {
      x = this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i];
      y = this.boatCalc.hull.vDisp[this.boatCalc.hull.SAREA][i];
      iw = xb + (int) (rx * (x - this.boatCalc.hull.gx_min));
      iz = yb - (int) (ry * y);
      g.drawLine(iu, iv, iw, iz);
      iu = iw;
      iv = iz;
      this.iCur[0][i] = iu;
      this.iCur[1][i] = iv;

      if (this.boatCalc.hull.vDisp[this.boatCalc.hull.SAREA][i] > maxArea) {
        maxArea = this.boatCalc.hull.vDisp[this.boatCalc.hull.SAREA][i];
        maxStn = x;
      }
    }
    this.xCur = maxStn;
    this.yCur = maxArea;

    g.setColor(Color.red);
    iu = xb + (int) (rx * (maxStn - this.boatCalc.hull.gx_min));
    iv = yb - (int) (ry * 0);
    iw = xb + (int) (rx * (maxStn - this.boatCalc.hull.gx_min));
    iz = yb - (int) (ry * maxArea);
    g.drawLine(iu, iv, iw, iz);

    final double Cp =
        this.boatCalc.hull.hVals[this.boatCalc.hull.DISP] / ((rightLWL - leftLWL) * maxArea);

    g.setColor(Color.black);

    int il = 25;
    g.drawString("Baseline Offset", 10, il);
    g.drawString(this.boatCalc.bcf.DF2d.format(this.boatCalc.hull.base), 125, il);
    il += 15;
    g.drawString("Angle of Heel", 10, il);
    g.drawString(this.boatCalc.bcf.DF0d.format(this.boatCalc.hull.angHeel), 125, il);
    il += 20;

    g.drawString("LWL - minimum", 10, il);
    g.drawString(this.boatCalc.bcf.DF1d.format(leftLWL), 125, il);
    il += 15;
    g.drawString("    - maximum", 10, il);
    g.drawString(this.boatCalc.bcf.DF1d.format(rightLWL), 125, il);
    il += 15;
    g.drawString("    - length", 10, il);
    g.drawString(this.boatCalc.bcf.DF1d.format(rightLWL - leftLWL), 125, il);
    il += 20;
    g.drawString("Max Section - Area", 10, il);
    g.drawString(this.boatCalc.bcf.DF2d.format(this.boatCalc.hull.units.coefArea() * maxArea)
        + this.boatCalc.hull.units.lblArea(), 125, il);
    il += 15;
    g.drawString("            @ Station", 10, il);
    g.drawString(this.boatCalc.bcf.DF1d.format(maxStn), 125, il);

    il = 25;
    final int ic = ix - 200;
    g.drawString("Displacement", ic, il);
    g.drawString(this.boatCalc.bcf.DF1d.format(
        this.boatCalc.hull.units.Vol2Wgt() * this.boatCalc.hull.hVals[this.boatCalc.hull.DISP])
        + this.boatCalc.hull.units.lblWgt(), ic + 115, il);
    il += 15;
    g.drawString("CoB - Station", ic, il);
    g.drawString(this.boatCalc.bcf.DF1d.format(this.boatCalc.hull.hVals[this.boatCalc.hull.CX]),
        ic + 115, il);
    il += 15;
    g.drawString("    - Lateral", ic, il);
    g.drawString(this.boatCalc.bcf.DF1d.format(this.boatCalc.hull.hVals[this.boatCalc.hull.CY]),
        ic + 115, il);
    il += 15;
    g.drawString("    - Height", ic, il);
    g.drawString(this.boatCalc.bcf.DF1d.format(this.boatCalc.hull.hVals[this.boatCalc.hull.CZ]),
        ic + 115, il);

    il += 20;
    g.drawString("Prismatic Coeff", ic, il);
    g.drawString(this.boatCalc.bcf.DF3d.format(Cp), ic + 115, il);

    double num, denom;
    String dlVal, dlLbl;

    if (this.boatCalc.hull.units.UNITS == 0) {
      num = this.boatCalc.hull.units.Vol2Ton() * this.boatCalc.hull.hVals[this.boatCalc.hull.DISP];
      denom = Math.pow((0.01 * (rightLWL - leftLWL)) / 12.0, 3.0);
      dlVal = this.boatCalc.bcf.DF0d.format(num / denom);
      dlLbl = "Disp/Length Ratio:";
    } else if (this.boatCalc.hull.units.UNITS == 1) {
      num = this.boatCalc.hull.units.Vol2Ton() * this.boatCalc.hull.hVals[this.boatCalc.hull.DISP];
      denom = Math.pow(0.01 * (rightLWL - leftLWL), 3.0);
      dlVal = this.boatCalc.bcf.DF0d.format(num / denom);
      dlLbl = "Disp/Length Ratio:";
    } else {
      num = rightLWL - leftLWL;
      denom = Math.pow(this.boatCalc.hull.hVals[this.boatCalc.hull.DISP], 0.33333);
      dlVal = this.boatCalc.bcf.DF2d.format(num / denom);
      dlLbl = "Length/Disp ratio:";
    }
    il += 20;
    g.drawString(dlLbl, ic, il);
    g.drawString(dlVal, ic + 115, il);



  }// end paint

}// end hdPanel
