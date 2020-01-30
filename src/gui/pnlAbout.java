package gui;

import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class pnlAbout extends JPanel {
  /**
   *
   */
  private static final long serialVersionUID = 1L;

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
