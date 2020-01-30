/**
 *
 */
package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class wgtPanel extends JPanel {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  /**
   *
   */
  private final boatCalc boatCalc;
  Dimension d;
  bcLabel lblHeel;
  JLabel[] lblMoments;
  JLabel[][] lblWgtDisp;

  public wgtPanel(boatCalc boatCalc, final int x, final int y) {
    this.boatCalc = boatCalc;
    this.d = new Dimension(x, y);
    this.lblHeel = new bcLabel("Heel:", SwingConstants.CENTER);
    this.lblWgtDisp = new JLabel[4][3];
    this.lblMoments = new JLabel[2];
    // setBackground(Color.lightGray) ;
    this.setBorder(BorderFactory.createEtchedBorder());
    this.setLayout(new GridLayout(0, 8));

    this.add(this.lblHeel);
    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
    this.add(new bcLabel("CoB", SwingConstants.CENTER));
    this.add(new bcLabel("CoG", SwingConstants.CENTER));
    this.add(new bcLabel("Difference", SwingConstants.CENTER));
    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
    this.add(new bcLabel("Weight", SwingConstants.RIGHT));
    this.add(this.lblWgtDisp[0][0] = new JLabel("disp_x", SwingConstants.RIGHT));
    this.add(this.lblWgtDisp[0][1] = new JLabel("wgt_x", SwingConstants.RIGHT));
    this.add(this.lblWgtDisp[0][2] = new JLabel("diff_x", SwingConstants.RIGHT));
    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
    this.add(new bcLabel("  Moments  ", SwingConstants.CENTER));
    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
    this.add(new bcLabel("Station", SwingConstants.RIGHT));
    this.add(this.lblWgtDisp[1][0] = new JLabel("disp_x", SwingConstants.RIGHT));
    this.add(this.lblWgtDisp[1][1] = new JLabel("wgt_x", SwingConstants.RIGHT));
    this.add(this.lblWgtDisp[1][2] = new JLabel("diff_x", SwingConstants.RIGHT));
    this.add(new bcLabel("Pitch", SwingConstants.RIGHT));
    this.add(this.lblMoments[0] = new JLabel("moment_p", SwingConstants.RIGHT));
    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
    this.add(new bcLabel("Breadth", SwingConstants.RIGHT));
    this.add(this.lblWgtDisp[2][0] = new JLabel("disp_y", SwingConstants.RIGHT));
    this.add(this.lblWgtDisp[2][1] = new JLabel("wgt_y", SwingConstants.RIGHT));
    this.add(this.lblWgtDisp[2][2] = new JLabel("diff_y", SwingConstants.RIGHT));
    this.add(new bcLabel("Heel", SwingConstants.RIGHT));
    this.add(this.lblMoments[1] = new JLabel("moment_h", SwingConstants.RIGHT));
    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
    this.add(new bcLabel("Height", SwingConstants.RIGHT));
    this.add(this.lblWgtDisp[3][0] = new JLabel("disp_z", SwingConstants.RIGHT));
    this.add(this.lblWgtDisp[3][1] = new JLabel("wgt_z", SwingConstants.RIGHT));
    this.add(this.lblWgtDisp[3][2] = new JLabel("diff_z", SwingConstants.RIGHT));
    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

  }

  @Override
  public Dimension getPreferredSize() {
    return this.d;
  }


  public void setWeights() {
    final double sinang = Math.sin(Math.toRadians(this.boatCalc.hull.angHeel));
    final double cosang = Math.cos(Math.toRadians(this.boatCalc.hull.angHeel));

    final double dWgt =
        this.boatCalc.hull.units.Vol2Wgt() * this.boatCalc.hull.hVals[this.boatCalc.hull.DISP];

    this.lblHeel.setText("Heel: " + this.boatCalc.bcf.DF0d.format(this.boatCalc.hull.angHeel));
    this.lblWgtDisp[0][0].setText(this.boatCalc.bcf.DF1d.format(dWgt));
    this.lblWgtDisp[1][0]
        .setText(this.boatCalc.bcf.DF1d.format(this.boatCalc.hull.hVals[this.boatCalc.hull.CX]));
    this.lblWgtDisp[2][0]
        .setText(this.boatCalc.bcf.DF1d.format(this.boatCalc.hull.hVals[this.boatCalc.hull.CY]));
    this.lblWgtDisp[3][0]
        .setText(this.boatCalc.bcf.DF1d.format(this.boatCalc.hull.hVals[this.boatCalc.hull.CZ]));

    double tw = 0;
    double tx = 0;
    double ty = 0;
    double tz = 0;
    for (int i = 0; i < 10; i++) {
      tw += this.boatCalc.hull.wgtWgt[i];
      tx += this.boatCalc.hull.wgtWgt[i] * this.boatCalc.hull.wgtX[i];
      ty += this.boatCalc.hull.wgtWgt[i] * this.boatCalc.hull.wgtY[i];
      tz += this.boatCalc.hull.wgtWgt[i] * this.boatCalc.hull.wgtZ[i];
    }

    if (tw > 0) {
      tx = tx / tw;
      ty = ty / tw;
      tz = (tz / tw) + this.boatCalc.hull.base;
    }
    final double rty = (cosang * ty) - (sinang * tz);
    final double rtz = (sinang * ty) + (cosang * tz);

    this.lblWgtDisp[0][1].setText(this.boatCalc.bcf.DF1d.format(tw));
    this.lblWgtDisp[1][1].setText(this.boatCalc.bcf.DF1d.format(tx));
    this.lblWgtDisp[2][1].setText(this.boatCalc.bcf.DF1d.format(rty));
    this.lblWgtDisp[3][1].setText(this.boatCalc.bcf.DF1d.format(rtz));

    this.lblWgtDisp[0][2].setText(this.boatCalc.bcf.DF1d.format(dWgt - tw));
    this.lblWgtDisp[1][2].setText(
        this.boatCalc.bcf.DF1d.format(this.boatCalc.hull.hVals[this.boatCalc.hull.CX] - tx));
    this.lblWgtDisp[2][2].setText(
        this.boatCalc.bcf.DF1d.format(this.boatCalc.hull.hVals[this.boatCalc.hull.CY] - rty));
    this.lblWgtDisp[3][2].setText(
        this.boatCalc.bcf.DF1d.format(this.boatCalc.hull.hVals[this.boatCalc.hull.CZ] - rtz));

    this.lblMoments[0].setText(this.boatCalc.bcf.DF1d
        .format(dWgt * (tx - this.boatCalc.hull.hVals[this.boatCalc.hull.CX])));
    this.lblMoments[1].setText(this.boatCalc.bcf.DF1d
        .format(dWgt * (rty - this.boatCalc.hull.hVals[this.boatCalc.hull.CY])));
  }

  // protected void paintComponent(Graphics g) {setWeights();}
} // ends wgtPanel
