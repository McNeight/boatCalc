/**
 *
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

public class planPanel extends JPanel {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  /**
   *
   */
  private final boatCalc boatCalc;
  Dimension d;

  public planPanel(boatCalc boatCalc, final int x, final int y) {
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
    final Font bigFont = new Font("Serif", Font.PLAIN, 12);
    final Font lilFont = new Font("SansSerif", Font.PLAIN, 10);
    g.setFont(bigFont);

    final double mx = this.getWidth();
    final double my = this.getHeight();
    int px = 0;
    int py = 2;
    int pw = (int) mx;
    int ph = ((int) my / 2) - 4;

    g.clearRect(px, py, pw, ph);
    g.drawString("Plan", px + 10, py + 15);

    g.drawString(
        "Length: "
            + this.boatCalc.bcf.DF1d.format(this.boatCalc.hull.gx_max - this.boatCalc.hull.gx_min),
        pw - 100, py + 15);
    g.drawString("Beam: " + this.boatCalc.bcf.DF1d.format(2.0 * this.boatCalc.hull.gy_max),
        pw - 100, py + 30);
    g.drawString(
        "Depth: "
            + this.boatCalc.bcf.DF1d.format(this.boatCalc.hull.gz_max - this.boatCalc.hull.gz_min),
        pw - 100, py + 45);

    g.clearRect(0, ((int) my / 2) + 4, (int) mx, ((int) my / 2) - 4);
    g.drawString("Profile", 10, ((int) my / 2) + 15);

    if (!this.boatCalc.hull.valid) {
      return;
    }

    int ix, iy, iw, iz;
    int iHL;
    double x, y, x_min, x_max;
    // Line ch;
    // Iterator listLine;

    final double[] stn = this.boatCalc.hull.Stations;

    g.setFont(lilFont);

    // draw plan view
    int ix_min = px + 100;
    int ix_max = (px + pw) - 25;
    int iy_min = (py + ph) - 15;
    int iy_max = py + 5;
    final int n = stn.length;
    final int n1 = n - 1;

    double rx, ry, r;
    rx = (ix_max - ix_min) / (this.boatCalc.hull.gx_max - this.boatCalc.hull.gx_min);
    ry = Math.abs(iy_max - iy_min)
        / Math.max((this.boatCalc.hull.gy_max - this.boatCalc.hull.gy_min),
            (this.boatCalc.hull.gz_max - this.boatCalc.hull.gz_min));
    r = Math.min(Math.abs(rx), Math.abs(ry));

    ix = ix_min + (int) (r * (stn[0] - this.boatCalc.hull.gx_min));
    iy = iy_min;
    iw = ix_min + (int) (r * (stn[n1] - this.boatCalc.hull.gx_min));
    iz = iy_min;
    g.setColor(Color.blue);
    g.drawLine(ix, iy, iw, iz);
    g.drawString("0.0", ix - 25, iy);
    for (int ic = 0; ic < stn.length; ic++) {
      ix = ix_min + (int) (r * (stn[ic] - this.boatCalc.hull.gx_min));
      g.drawLine(ix, iy - 5, ix, iy + 5);
      g.drawString(this.boatCalc.bcf.DF2d.format(stn[ic]), ix + 1, iy + 12);
    }

    // draw vertical axis
    double vmin, vmax;
    double vinc = 12.0;
    if (this.boatCalc.hull.units.UNITS == 1) {
      vinc = 1.0;
    } else if (this.boatCalc.hull.units.UNITS == 2) {
      vinc = 10.0;
    } else if (this.boatCalc.hull.units.UNITS == 3) {
      vinc = 0.1;
    }
    vmax = vinc * Math.ceil(this.boatCalc.hull.gy_max / vinc);
    while ((vmax / vinc) > 4.0) {
      vinc = 2.0 * vinc;
    }
    ix = ix_min + (int) (r * (stn[0] - this.boatCalc.hull.gx_min));
    iy = iy_min - (int) (r * (0));
    iw = ix;
    iz = iy_min - (int) (r * (vmax - 0));
    g.drawLine(ix, iy, iw, iz);
    ix = ix - 5;
    for (double vidx = vinc; vidx <= vmax; vidx += vinc) {
      iy = iy_min - (int) (r * (vidx - 0));
      iz = iy;
      g.drawLine(ix, iy, iw, iz);
      g.drawString(Double.toString(vidx), ix - 25, iy);
    }

    g.setColor(Color.black);

    for (iHL = 0; iHL < this.boatCalc.hull.hLines.length; iHL++) {
      x_min = this.boatCalc.hull.hLines[iHL].min("X");
      x_max = this.boatCalc.hull.hLines[iHL].max("X");

      x = x_min;
      y = this.boatCalc.hull.hLines[iHL].hXY.interp4P(x);
      ix = ix_min + (int) (r * (x - this.boatCalc.hull.gx_min));
      iy = iy_min - (int) (r * (y - 0));
      for (double pct = 0.05; pct < 1.005; pct += 0.05) {
        x = x_min + (pct * (x_max - x_min));
        y = this.boatCalc.hull.hLines[iHL].hXY.interp4P(x);
        iw = ix_min + (int) (r * (x - this.boatCalc.hull.gx_min));
        iz = iy_min - (int) (r * (y - 0));
        g.drawLine(ix, iy, iw, iz);
        ix = iw;
        iy = iz;
      }
    }

    // draw stems
    g.setColor(Color.lightGray);
    for (int iSL = 0; iSL <= 1; iSL++) {
      if (this.boatCalc.hull.bStems[iSL] && this.boatCalc.hull.sLines[iSL].valid) {
        x = this.boatCalc.hull.sLines[iSL].hPoints[0].getX();
        y = this.boatCalc.hull.sLines[iSL].hPoints[0].getY();
        ix = ix_min + (int) (r * (x - this.boatCalc.hull.gx_min));
        iy = iy_min - (int) (r * (y - 0));
        for (int j = 1; j < this.boatCalc.hull.sLines[iSL].hPoints.length; j++) {
          x = this.boatCalc.hull.sLines[iSL].hPoints[j].getX();
          y = this.boatCalc.hull.sLines[iSL].hPoints[j].getY();
          iw = ix_min + (int) (r * (x - this.boatCalc.hull.gx_min));
          iz = iy_min - (int) (r * (y - 0));
          g.drawLine(ix, iy, iw, iz);
          ix = iw;
          iy = iz;
        }
      }
    }
    g.setColor(Color.black);

    // draw profile view

    px = 0;
    py = ((int) my / 2) + 4;
    pw = (int) mx;
    ph = ((int) my / 2) - 4;

    ix_min = px + 100;
    ix_max = (px + pw) - 25;
    iy_min = (py + ph) - 15;
    iy_max = py + 5;

    // x-axis
    ix = ix_min + (int) (r * (stn[0] - this.boatCalc.hull.gx_min));
    iy = iy_min - (int) (r * (0 - this.boatCalc.hull.gz_min));
    iw = ix_min + (int) (r * (stn[n1] - this.boatCalc.hull.gx_min));
    iz = iy;

    g.setColor(Color.blue);
    g.drawLine(ix, iy, iw, iz);
    for (int ic = 0; ic < stn.length; ic++) {
      ix = ix_min + (int) (r * (stn[ic] - this.boatCalc.hull.gx_min));
      g.drawLine(ix, iy - 5, ix, iy + 5);
    }

    // y-axis
    vinc = 12.0;
    if (this.boatCalc.hull.units.UNITS == 1) {
      vinc = 1.0;
    } else if (this.boatCalc.hull.units.UNITS == 2) {
      vinc = 10.0;
    } else if (this.boatCalc.hull.units.UNITS == 3) {
      vinc = 0.1;
    }
    vmax = vinc * Math.ceil(this.boatCalc.hull.gz_max / vinc);
    vmin = vinc * Math.floor(this.boatCalc.hull.gz_min / vinc);
    while (((vmax - vmin) / vinc) > 4.0) {
      vinc = 2.0 * vinc;
    }
    ix = ix_min + (int) (r * (stn[0] - this.boatCalc.hull.gx_min));
    // iy = iy_min - (int) (r * (vmin - hull.gz_min)) ;
    iy = iy_min;
    iw = ix;
    iz = iy_min - (int) (r * (vmax - this.boatCalc.hull.gz_min));
    g.drawLine(ix, iy, iw, iz);
    ix = ix - 5;
    for (double vidx = vmin; vidx <= vmax; vidx += vinc) {
      iy = iy_min - (int) (r * (vidx - this.boatCalc.hull.gz_min));
      iz = iy;
      if (iy <= iy_min) {
        g.drawLine(ix, iy, iw, iz);
        g.drawString(Double.toString(vidx), ix - 25, iy);
      }
    }

    g.setColor(Color.black);

    for (iHL = 0; iHL < this.boatCalc.hull.hLines.length; iHL++) {
      x_min = this.boatCalc.hull.hLines[iHL].min("X");
      x_max = this.boatCalc.hull.hLines[iHL].max("X");
      x = x_min;
      y = this.boatCalc.hull.hLines[iHL].hXZ.interp4P(x);
      ix = ix_min + (int) (r * (x - this.boatCalc.hull.gx_min));
      iy = iy_min - (int) (r * (y - this.boatCalc.hull.gz_min));
      for (double pct = 0.05; pct < 1.005; pct += 0.05) {
        x = x_min + (pct * (x_max - x_min));
        y = this.boatCalc.hull.hLines[iHL].hXZ.interp4P(x);
        iw = ix_min + (int) (r * (x - this.boatCalc.hull.gx_min));
        iz = iy_min - (int) (r * (y - this.boatCalc.hull.gz_min));
        g.drawLine(ix, iy, iw, iz);
        ix = iw;
        iy = iz;
      }
    }
    // draw stems
    g.setColor(Color.lightGray);
    for (int iSL = 0; iSL <= 1; iSL++) {
      if (this.boatCalc.hull.bStems[iSL] && this.boatCalc.hull.sLines[iSL].valid) {
        x = this.boatCalc.hull.sLines[iSL].hPoints[0].getX();
        y = this.boatCalc.hull.sLines[iSL].hPoints[0].getZ();
        ix = ix_min + (int) (r * (x - this.boatCalc.hull.gx_min));
        iy = iy_min - (int) (r * (y - this.boatCalc.hull.gz_min));
        for (int j = 1; j < this.boatCalc.hull.sLines[iSL].hPoints.length; j++) {
          x = this.boatCalc.hull.sLines[iSL].hPoints[j].getX();
          y = this.boatCalc.hull.sLines[iSL].hPoints[j].getZ();
          iw = ix_min + (int) (r * (x - this.boatCalc.hull.gx_min));
          iz = iy_min - (int) (r * (y - this.boatCalc.hull.gz_min));
          g.drawLine(ix, iy, iw, iz);
          ix = iw;
          iy = iz;
        }
      }
    }
    g.setColor(Color.black);

  }// end paint
}// end planPanel
