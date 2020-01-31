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
 * The Class unitPanel.
 */
public class unitPanel extends JPanel implements ActionListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The btn cm kg. */
  public JRadioButton btnCmKg;

  /** The btn ft lbs. */
  public JRadioButton btnFtLbs;

  /** The btn in lbs. */
  public JRadioButton btnInLbs;

  /** The btn M kg. */
  public JRadioButton btnMKg;

  /** The boat calc. */
  private final boatCalc boatCalc;

  /** The b grp. */
  ButtonGroup bGrp;

  /** The d. */
  Dimension d;

  /** The lbl unit. */
  JLabel lblUnit;

  /**
   * Instantiates a new unit panel.
   *
   * @param boatCalc the boat calc
   * @param x the x
   * @param y the y
   */
  public unitPanel(final boatCalc boatCalc, final int x, final int y) {
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

  /**
   * Action performed.
   *
   * @param e the e
   */
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

  /**
   * Gets the preferred size.
   *
   * @return the preferred size
   */
  @Override
  public Dimension getPreferredSize() {
    return this.d;
  }
}// end unitPanel
