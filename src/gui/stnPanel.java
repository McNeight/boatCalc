/**
 *
 */
package gui;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class stnPanel extends JPanel implements ChangeListener {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  /**
   *
   */
  private final boatCalc boatCalc;
  Dimension d;
  int iPct = 50;
  JPanel pnlSlct;
  JSlider xSlct;

  public stnPanel(boatCalc boatCalc, final int x, final int y) {
    this.boatCalc = boatCalc;
    this.d = new Dimension(x, y);
    final stnBody body = new stnBody(this.boatCalc, 400, 300);
    this.add(body);

    this.xSlct = new JSlider();
    this.xSlct.setPreferredSize(new Dimension(250, 42));
    this.xSlct.setMajorTickSpacing(10);
    this.xSlct.setMinorTickSpacing(5);
    this.xSlct.setPaintTicks(true);
    this.xSlct.setPaintLabels(true);

    this.xSlct.addChangeListener(this);

    this.pnlSlct = new JPanel();
    final Border bcBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    this.pnlSlct.setBorder(BorderFactory.createTitledBorder(bcBorder, "Station (%LWL)"));
    this.pnlSlct.add(this.xSlct);
    this.add(this.pnlSlct);

  }// end constructor

  @Override
  public Dimension getPreferredSize() {
    return this.d;
  }

  @Override
  public void stateChanged(final ChangeEvent e) {
    this.iPct = this.xSlct.getValue();
    this.boatCalc.dispStn.repaint();
  }


}// end stnPanel
