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
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class pnlAbout.
 */
public class pnlAbout extends JPanel {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new pnl about.
   */
  public pnlAbout() {
    JLabel lbl;
    final Font wpFont = new Font("Serif", Font.BOLD, 14);
    this.setBorder(BorderFactory.createEtchedBorder());
    this.setLayout(new GridLayout(0, 1));
    lbl = new JLabel("boatCalc");
    lbl.setFont(wpFont);
    lbl.setHorizontalAlignment(SwingConstants.CENTER);
    this.add(lbl);
    lbl = new JLabel(
        "<html>Copyright © 2004 by Peter H. Vanderwaart<br>Copyright © 2020 by Neil McNeight</html>");
    lbl.setFont(wpFont);
    lbl.setHorizontalAlignment(SwingConstants.CENTER);
    this.add(lbl);
    lbl = new JLabel("Version 0.5 - 29 Jan 2020");
    lbl.setFont(wpFont);
    lbl.setHorizontalAlignment(SwingConstants.CENTER);
    this.add(lbl);
  }
}// end pnlAbout
