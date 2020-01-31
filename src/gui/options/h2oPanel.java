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
package gui.options;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import gui.boatCalc;

// TODO: Auto-generated Javadoc
/**
 * The Class h2oPanel.
 */
public class h2oPanel extends JPanel implements ActionListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The btn fresh. */
  public JRadioButton btnFresh;

  /** The btn salt. */
  public JRadioButton btnSalt;

  /** The boat calc. */
  private final boatCalc boatCalc;

  /** The b grp. */
  ButtonGroup bGrp;

  /** The d. */
  Dimension d;

  /** The lbl H 2 O. */
  JLabel lblH2O;

  /**
   * Instantiates a new h 2 o panel.
   *
   * @param boatCalc the boat calc
   * @param x the x
   * @param y the y
   */
  public h2oPanel(final boatCalc boatCalc, final int x, final int y) {
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

  /**
   * Action performed.
   *
   * @param e the e
   */
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

  /**
   * Gets the preferred size.
   *
   * @return the preferred size
   */
  @Override
  public Dimension getPreferredSize() {
    return this.d;
  }
}// end h2oPanel
