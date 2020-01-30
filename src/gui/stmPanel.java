/**
 *
 */
package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class stmPanel extends JPanel implements ActionListener {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  /**
   *
   */
  private final boatCalc boatCalc;
  JRadioButton btnLeft;
  JRadioButton btnRight;
  Dimension d;
  JLabel lblStem;

  public stmPanel(boatCalc boatCalc, final int x, final int y) {
    this.boatCalc = boatCalc;
    this.d = new Dimension(x, y);
    this.lblStem = new JLabel("Auto Stem:");
    this.btnLeft = new JRadioButton("Left");
    this.btnLeft.setSelected(true);
    this.btnRight = new JRadioButton("Right");
    this.btnRight.setSelected(true);

    this.btnLeft.addActionListener(this);
    this.btnRight.addActionListener(this);

    this.setBorder(BorderFactory.createEtchedBorder());
    this.setLayout(new FlowLayout());
    this.add(this.lblStem);
    this.add(this.btnLeft);
    this.add(this.btnRight);
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    if (this.btnLeft.isSelected()) {
      this.boatCalc.hull.bStems[0] = true;
    } else {
      this.boatCalc.hull.bStems[0] = false;
    }
    if (this.btnRight.isSelected()) {
      this.boatCalc.hull.bStems[1] = true;
    } else {
      this.boatCalc.hull.bStems[1] = false;
    }
    this.boatCalc.hull.bChanged = true;
    this.boatCalc.body.repaint();
    this.boatCalc.plan.repaint();
  }

  @Override
  public Dimension getPreferredSize() {
    return this.d;
  }

}// end stmPanel
