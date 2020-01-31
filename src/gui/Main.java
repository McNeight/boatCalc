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

import javax.swing.WindowConstants;

public class Main {
  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(final String[] args) {
    try {
      javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
    } catch (final Exception e) {
      // Silently ignore -- there's nothing to be done.
    }

    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        final boatCalc c = new boatCalc();
        c.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        c.setSize(770, 570);
        c.setBackground(java.awt.Color.white);
        c.setVisible(true);
      }
    });
  } // end main

}

