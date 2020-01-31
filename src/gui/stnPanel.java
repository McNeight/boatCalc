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

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// TODO: Auto-generated Javadoc
/**
 * The Class stnPanel.
 */
public class stnPanel extends JPanel implements ChangeListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The boat calc. */
  private final boatCalc boatCalc;

  /** The d. */
  Dimension d;

  /** The i pct. */
  int iPct = 50;

  /** The pnl slct. */
  JPanel pnlSlct;

  /** The x slct. */
  JSlider xSlct;

  /**
   * Instantiates a new stn panel.
   *
   * @param boatCalc the boat calc
   * @param x the x
   * @param y the y
   */
  public stnPanel(final boatCalc boatCalc, final int x, final int y) {
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
   * State changed.
   *
   * @param e the e
   */
  @Override
  public void stateChanged(final ChangeEvent e) {
    this.iPct = this.xSlct.getValue();
    this.boatCalc.dispStn.repaint();
  }


}// end stnPanel
