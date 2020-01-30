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

public class h2oPanel extends JPanel implements ActionListener {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  ButtonGroup bGrp;
  /**
   *
   */
  private final boatCalc boatCalc;
  JRadioButton btnFresh;
  JRadioButton btnSalt;
  Dimension d;
  JLabel lblH2O;

  public h2oPanel(boatCalc boatCalc, final int x, final int y) {
    this.boatCalc = boatCalc;
    this.d = new Dimension(x, y);
    this.lblH2O = new JLabel("Water:");
    this.btnSalt = new JRadioButton("salt");
    this.btnSalt.setSelected(true);
    this.btnFresh = new JRadioButton("fresh");

    this.btnSalt.addActionListener(this);
    this.btnFresh.addActionListener(this);

    this.bGrp = new ButtonGroup();
    this.bGrp.add(this.btnSalt);
    this.bGrp.add(this.btnFresh);

    this.setBorder(BorderFactory.createEtchedBorder());
    this.setLayout(new FlowLayout());
    this.add(this.lblH2O);
    this.add(this.btnSalt);
    this.add(this.btnFresh);
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    if (this.btnSalt.isSelected()) {
      this.boatCalc.hull.units.WATER = 0;
    }
    if (this.btnFresh.isSelected()) {
      this.boatCalc.hull.units.WATER = 1;
    }
    this.boatCalc.hull.bChanged = true;
    this.boatCalc.body.repaint();
    this.boatCalc.plan.repaint();
  }

  @Override
  public Dimension getPreferredSize() {
    return this.d;
  }
}// end h2oPanel
