/**
 *
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class wlPanel extends JPanel {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  /**
   *
   */
  private final boatCalc boatCalc;
  Dimension d;
  int[][] iComp;
  int[][] iCur;

  public wlPanel(boatCalc boatCalc, final int x, final int y) {
    this.boatCalc = boatCalc;
    this.d = new Dimension(x, y);
    this.iComp = new int[2][this.boatCalc.hull.NDIV + 1];
    this.iCur = new int[2][this.boatCalc.hull.NDIV + 1];


  }// end constructor

  @Override
  public Dimension getPreferredSize() {
    return this.d;
  }

  @Override
  protected void paintComponent(final Graphics g) {
    super.paintComponent(g);
    final double sinone = Math.sin(Math.toRadians(1.0));


    final double mx = this.getWidth();
    final double my = this.getHeight();
    final int ix = (int) mx;
    final int iy = (int) my;
    final int xb = 100;
    final int yb = (int) my / 2;

    g.clearRect(0, 0, ix, iy);

    final double rx = (mx - 200.0) / (this.boatCalc.hull.gx_max - this.boatCalc.hull.gx_min);
    final double ry = (0.8 * my) / (this.boatCalc.hull.gy_max - this.boatCalc.hull.gy_min);
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
        g.drawLine(iu, this.iComp[0][i - 1], iv, this.iComp[0][i]);
        iu = iv;
      }

      iu = xb + (int) (r
          * (this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][0] - this.boatCalc.hull.gx_min));
      for (int i = 1; i <= this.boatCalc.hull.NDIV; i++) {
        iv = xb + (int) (r
            * (this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i] - this.boatCalc.hull.gx_min));
        g.drawLine(iu, this.iComp[1][i - 1], iv, this.iComp[1][i]);
        iu = iv;
      }
      g.setColor(Color.black);
    }

    double x = this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][0];
    double y = this.boatCalc.hull.vWL[0][0];
    iu = xb + (int) (r * (x - this.boatCalc.hull.gx_min));
    iv = yb - (int) (r * y);

    this.iCur[0][0] = iv;
    for (int i = 1; i <= this.boatCalc.hull.NDIV; i++) {
      x = this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i];
      y = this.boatCalc.hull.vWL[0][i];
      iw = xb + (int) (r * (x - this.boatCalc.hull.gx_min));
      iz = yb - (int) (r * y);
      g.drawLine(iu, iv, iw, iz);
      iu = iw;
      iv = iz;
      this.iCur[0][i] = iv;
    }

    x = this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][0];
    y = this.boatCalc.hull.vWL[1][0];
    iu = xb + (int) (r * (x - this.boatCalc.hull.gx_min));
    iv = yb - (int) (r * y);

    this.iCur[1][0] = iv;
    for (int i = 1; i <= this.boatCalc.hull.NDIV; i++) {
      x = this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i];
      y = this.boatCalc.hull.vWL[1][i];
      iw = xb + (int) (r * (x - this.boatCalc.hull.gx_min));
      iz = yb - (int) (r * y);
      g.drawLine(iu, iv, iw, iz);
      iu = iw;
      iv = iz;
      this.iCur[1][i] = iv;
    }


    // compute

    double wlCur = this.boatCalc.hull.vWL[1][0] - this.boatCalc.hull.vWL[0][0];
    double wlX = wlCur * this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][0];
    double wlY = wlCur * 0.5 * (this.boatCalc.hull.vWL[1][0] + this.boatCalc.hull.vWL[0][0]);
    double wlSum = wlCur;
    double wlArea = 0;
    double wlLast = wlCur;
    double wlMax = wlCur;
    double wlXMax = this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][0];

    for (int i = 1; i <= this.boatCalc.hull.NDIV; i++) {
      wlCur = this.boatCalc.hull.vWL[1][i] - this.boatCalc.hull.vWL[0][i];
      if (wlCur > wlMax) {
        wlMax = wlCur;
        wlXMax = this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i];
      }
      wlX = wlX + (wlCur * this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i]);
      wlY = wlY + (wlCur * 0.5 * (this.boatCalc.hull.vWL[1][i] + this.boatCalc.hull.vWL[0][i]));
      wlSum = wlSum + wlCur;
      wlArea = wlArea + ((this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i]
          - this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i - 1]) * 0.5 * (wlCur + wlLast));
      wlLast = wlCur;
    }

    int il = 25;
    g.drawString("Waterplane Area", 10, il);
    g.drawString(this.boatCalc.bcf.DF2d.format(this.boatCalc.hull.units.coefArea() * wlArea)
        + this.boatCalc.hull.units.lblArea(), 125, il);
    il += 20;
    g.drawString("Max WL Beam:", 10, il);
    g.drawString(this.boatCalc.bcf.DF1d.format(wlMax), 125, il);
    il += 15;
    g.drawString("   @Station:", 10, il);
    g.drawString(this.boatCalc.bcf.DF1d.format(wlXMax), 125, il);
    il += 20;

    if (wlSum > 0) {
      wlX = wlX / wlSum;
      wlY = wlY / wlSum;
      g.drawString("CoA @ Station:", 10, il);
      g.drawString(this.boatCalc.bcf.DF1d.format(wlX), 125, il);
      il += 15;
      g.drawString("   Breadth:", 10, il);
      g.drawString(this.boatCalc.bcf.DF1d.format(wlY), 125, il);
      il += 20;
      iw = xb + (int) (r * (wlX - this.boatCalc.hull.gx_min));
      iz = yb - (int) (r * wlY);
      g.setColor(Color.red);
      g.drawArc(iw - 5, iz - 5, 10, 10, 0, 360);
      g.setColor(Color.black);


      wlLast = this.boatCalc.hull.vWL[1][0] - this.boatCalc.hull.vWL[0][0];
      double d, w, h, xm;
      double mp = 0;
      for (int i = 1; i <= this.boatCalc.hull.NDIV; i++) {
        d = this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i]
            - this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i - 1]; // delta x
        wlCur = this.boatCalc.hull.vWL[1][i] - this.boatCalc.hull.vWL[0][i];
        w = 0.5 * (wlCur + wlLast); // average width
        xm = 0.5 * (this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i]
            + this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i - 1]); // average x
        h = sinone * (xm - wlX); // height
        mp = mp + (this.boatCalc.hull.units.Vol2Wgt() * d * w * h * (xm - wlX));
        wlLast = wlCur;
      }
      il += 75;

      final double ppi =
          wlArea * this.boatCalc.hull.units.coefPPI() * this.boatCalc.hull.units.Vol2Wgt();
      g.drawString("Imersion:", 10, il);
      g.drawString(this.boatCalc.bcf.DF1d.format(ppi) + this.boatCalc.hull.units.lblPPI(), 125, il);
      il += 20;

      g.drawString("Moment to ", 10, il);
      il += 15;
      g.drawString("pitch 1 deg:", 10, il);
      g.drawString(this.boatCalc.bcf.DF1d.format(mp) + " " + this.boatCalc.hull.units.lblMom(), 125,
          il);
      il += 20;
      il = 25;

      final int ic = ix - 200;
      il = 25;
      g.drawString("Waterplane Coef.:", ic, il);
      g.drawString(
          this.boatCalc.bcf.DF2d.format(
              wlArea / (wlMax * (this.boatCalc.hull.lwlRight - this.boatCalc.hull.lwlLeft))),
          ic + 150, il);

      // half-angle computation
      int i1, i2;
      double a1, a2;
      il += 20;
      g.drawString("Half Angles (avg):", ic, il);

      // left
      if (this.boatCalc.hull.NDIV > 10) {
        i1 = 1;
        i2 = 5;
      } else {
        i1 = 1;
        i2 = 2;
      }
      a1 = Math.atan2(this.boatCalc.hull.vWL[1][i2] - this.boatCalc.hull.vWL[1][i1],
          (this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i2]
              - this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i1]));
      a2 = Math.atan2(this.boatCalc.hull.vWL[0][i2] - this.boatCalc.hull.vWL[0][i1],
          (this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i2]
              - this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i1]));
      il += 15;
      g.drawString("Left -", ic + 50, il);
      g.drawString(this.boatCalc.bcf.DF1d.format(Math.toDegrees(a1 - a2)), ic + 150, il);

      // right
      if (this.boatCalc.hull.NDIV > 10) {
        i1 = this.boatCalc.hull.NDIV - 1;
        i2 = this.boatCalc.hull.NDIV - 4;
      } else {
        i1 = this.boatCalc.hull.NDIV - 1;
        i2 = this.boatCalc.hull.NDIV - 2;
      }
      a1 = Math.atan2(this.boatCalc.hull.vWL[1][i2] - this.boatCalc.hull.vWL[1][i1],
          (this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i1]
              - this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i2]));
      a2 = Math.atan2(this.boatCalc.hull.vWL[0][i2] - this.boatCalc.hull.vWL[0][i1],
          (this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i1]
              - this.boatCalc.hull.vDisp[this.boatCalc.hull.CX][i2]));
      il += 15;
      g.drawString("Right -", ic + 50, il);
      g.drawString(this.boatCalc.bcf.DF1d.format(Math.toDegrees(a1 - a2)), ic + 150, il);
      il += 15;

    }

  } // end paintComponent
} // end wlPanel
