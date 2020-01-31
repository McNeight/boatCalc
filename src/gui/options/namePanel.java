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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import gui.boatCalc;

// TODO: Auto-generated Javadoc
/**
 * The Class namePanel.
 */
public class namePanel extends JPanel implements ActionListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The lbl NA. */
  public JLabel lblNA;

  /** The lbl name. */
  public JLabel lblName;

  /** The boat calc. */
  private final boatCalc boatCalc;

  /** The d. */
  Dimension d;

  /**
   * Instantiates a new name panel.
   *
   * @param boatCalc the boat calc
   * @param x the x
   * @param y the y
   */
  public namePanel(final boatCalc boatCalc, final int x, final int y) {
    this.boatCalc = boatCalc;
    final Font cpFont = new Font("Serif", Font.BOLD, 14);
    this.d = new Dimension(x, y);
    this.setBorder(BorderFactory.createEtchedBorder());
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    this.lblName = new JLabel("Design: N/A");
    this.lblName.setFont(cpFont);
    this.lblName.setHorizontalAlignment(SwingConstants.LEFT);

    this.lblNA = new JLabel("Designer: N/A");
    this.lblNA.setFont(cpFont);
    this.lblNA.setHorizontalAlignment(SwingConstants.RIGHT);

    this.add(this.lblName);
    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
    this.add(this.lblNA);
  }

  /**
   * Action performed.
   *
   * @param e the e
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    // TODO Auto-generated method stub
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

}
