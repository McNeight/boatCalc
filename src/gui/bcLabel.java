package gui;

import java.awt.Font;
import javax.swing.JLabel;

public class bcLabel extends JLabel {
  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public bcLabel(final String s, final int l) {
    this.setText(s);
    this.setHorizontalAlignment(l);
    this.setFont(new Font("Serif", Font.BOLD, 12));
  }
}
