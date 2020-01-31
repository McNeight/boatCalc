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
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import gui.boatCalc;

// TODO: Auto-generated Javadoc
/**
 * The Class ctrlPanel.
 */
public class ctrlPanel extends JPanel {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The h P. */
  public h2oPanel hP;

  /** The n P. */
  public namePanel nP;

  /** The s P. */
  public stmPanel sP;

  /** The u P. */
  public unitPanel uP;

  /** The boat calc. */
  private final boatCalc boatCalc;

  /** The d. */
  Dimension d;

  /**
   * Instantiates a new ctrl panel.
   *
   * @param boatCalc the boat calc
   * @param x the x
   * @param y the y
   */
  public ctrlPanel(final boatCalc boatCalc, final int x, final int y) {
    this.boatCalc = boatCalc;
    this.d = new Dimension(x, y);
    this.setBorder(BorderFactory.createEtchedBorder());

    this.nP = new namePanel(this.boatCalc, 130, 35);
    this.hP = new h2oPanel(this.boatCalc, 170, 35);
    this.sP = new stmPanel(this.boatCalc, 210, 35);
    this.uP = new unitPanel(this.boatCalc, 380, 35);
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.add(this.nP);
    this.add(this.hP);
    this.add(this.sP);
    this.add(this.uP);
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

}// end ctrlPanel
