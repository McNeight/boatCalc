/**
 *
 */
package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ctrlPanel extends JPanel {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  /**
   *
   */
  private final boatCalc boatCalc;
  Dimension d;
  h2oPanel hP;
  JLabel lblNA;
  JLabel lblName;
  stmPanel sP;
  unitPanel uP;

  public ctrlPanel(boatCalc boatCalc, final int x, final int y) {
    this.boatCalc = boatCalc;
    final Font cpFont = new Font("Serif", Font.BOLD, 14);
    this.d = new Dimension(x, y);
    this.setBorder(BorderFactory.createEtchedBorder());
    this.setLayout(new FlowLayout());
    this.lblName = new JLabel("name here");
    this.lblName.setFont(cpFont);
    this.lblName.setHorizontalAlignment(SwingConstants.LEFT);

    this.lblNA = new JLabel("n/a");
    this.lblNA.setFont(cpFont);
    this.lblNA.setHorizontalAlignment(SwingConstants.RIGHT);

    this.hP = new h2oPanel(this.boatCalc, 170, 35);
    this.sP = new stmPanel(this.boatCalc, 210, 35);
    this.uP = new unitPanel(this.boatCalc, 380, 35);
    this.add(this.lblName);
    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

    this.add(this.lblNA);
    this.add(this.hP);
    this.add(this.sP);
    this.add(this.uP);
  }

  @Override
  public Dimension getPreferredSize() {
    return this.d;
  }

}// end ctrlPanel
