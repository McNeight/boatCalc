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

import java.awt.Font;
import javax.swing.JLabel;

// TODO: Auto-generated Javadoc
/**
 * The Class bcLabel.
 */
public class bcLabel extends JLabel {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new bc label.
   *
   * @param s the s
   * @param l the l
   */
  public bcLabel(final String s, final int l) {
    this.setText(s);
    this.setHorizontalAlignment(l);
    this.setFont(new Font("Serif", Font.BOLD, 12));
  }
}
