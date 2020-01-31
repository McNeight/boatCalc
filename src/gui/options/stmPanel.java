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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import gui.boatCalc;

// TODO: Auto-generated Javadoc
/**
 * The Class stmPanel.
 */
public class stmPanel extends JPanel implements ActionListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The box left. */
  public JCheckBox boxLeft;

  /** The box right. */
  public JCheckBox boxRight;

  /** The boat calc. */
  private final boatCalc boatCalc;

  /** The d. */
  Dimension d;

  /** The lbl stem. */
  JLabel lblStem;

  /**
   * Instantiates a new stm panel.
   *
   * @param boatCalc the boat calc
   * @param x the x
   * @param y the y
   */
  public stmPanel(final boatCalc boatCalc, final int x, final int y) {
    this.boatCalc = boatCalc;
    this.d = new Dimension(x, y);
    this.lblStem = new JLabel("Auto Stem:");
    this.boxLeft = new JCheckBox("Left");
    this.boxLeft.setSelected(true);
    this.boxRight = new JCheckBox("Right");
    this.boxRight.setSelected(true);

    this.boxLeft.addActionListener(this);
    this.boxRight.addActionListener(this);

    this.setBorder(BorderFactory.createEtchedBorder());
    this.setLayout(new FlowLayout());
    this.add(this.lblStem);
    this.add(this.boxLeft);
    this.add(this.boxRight);
  }

  /**
   * Action performed.
   *
   * @param e the e
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    if (this.boxLeft.isSelected()) {
      this.boatCalc.hull.bStems[0] = true;
    } else {
      this.boatCalc.hull.bStems[0] = false;
    }
    if (this.boxRight.isSelected()) {
      this.boatCalc.hull.bStems[1] = true;
    } else {
      this.boatCalc.hull.bStems[1] = false;
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

}// end stmPanel
