/**
 *
 */
package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class unitPanel extends JPanel implements ActionListener {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  ButtonGroup bGrp;
  /**
   *
   */
  private final boatCalc boatCalc;
  JRadioButton btnCmKg;
  JRadioButton btnFtLbs;
  JRadioButton btnInLbs;
  JRadioButton btnMKg;
  Dimension d;
  JLabel lblUnit;

  public unitPanel(boatCalc boatCalc, final int x, final int y) {
    this.boatCalc = boatCalc;
    this.d = new Dimension(x, y);
    this.lblUnit = new JLabel("Units:");
    this.btnInLbs = new JRadioButton("in,lbs");
    this.btnInLbs.setSelected(true);
    this.btnFtLbs = new JRadioButton("ft,lbs");
    this.btnCmKg = new JRadioButton("cm,Kg");
    this.btnMKg = new JRadioButton("m,Kg");

    this.btnInLbs.addActionListener(this);
    this.btnFtLbs.addActionListener(this);
    this.btnCmKg.addActionListener(this);
    this.btnMKg.addActionListener(this);

    this.bGrp = new ButtonGroup();
    this.bGrp.add(this.btnInLbs);
    this.bGrp.add(this.btnFtLbs);
    this.bGrp.add(this.btnCmKg);
    this.bGrp.add(this.btnMKg);

    this.setBorder(BorderFactory.createEtchedBorder());
    this.setLayout(new FlowLayout());
    this.add(this.lblUnit);
    this.add(this.btnInLbs);
    this.add(this.btnFtLbs);
    this.add(this.btnCmKg);
    this.add(this.btnMKg);
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    if (this.btnInLbs.isSelected()) {
      this.boatCalc.hull.units.UNITS = 0;
    }
    if (this.btnFtLbs.isSelected()) {
      this.boatCalc.hull.units.UNITS = 1;
    }
    if (this.btnCmKg.isSelected()) {
      this.boatCalc.hull.units.UNITS = 2;
    }
    if (this.btnMKg.isSelected()) {
      this.boatCalc.hull.units.UNITS = 3;
    }
    this.boatCalc.setCtrls();
  }

  @Override
  public Dimension getPreferredSize() {
    return this.d;
  }
}// end unitPanel
