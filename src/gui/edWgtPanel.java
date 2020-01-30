/**
 *
 */
package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

class edWgtPanel extends JPanel implements DocumentListener, FocusListener {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  boolean bChanged;
  /**
   *
   */
  private final boatCalc boatCalc;
  public JTextField[] l;
  JLabel lblTot = new JLabel("CoG:");
  JLabel lblWgt = new JLabel("n/a");
  JLabel lblX = new JLabel("n/a");
  JLabel lblY = new JLabel("n/a");
  JLabel lblZ = new JLabel("n/a");
  public JTextField[] w;
  public JTextField[] x;
  public JTextField[] y;
  public JTextField[] z;

  public edWgtPanel(boatCalc boatCalc) {

    this.boatCalc = boatCalc;
    JLabel lbl;
    final Font wpFont = new Font("Serif", Font.BOLD, 14);
    this.setBorder(BorderFactory.createEtchedBorder());
    this.setLayout(new GridLayout(0, 7));
    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

    lbl = new JLabel(" Type ", SwingConstants.CENTER);
    lbl.setFont(wpFont);
    this.add(lbl);

    lbl = new JLabel("  Weight  ", SwingConstants.CENTER);
    lbl.setFont(wpFont);
    this.add(lbl);

    lbl = new JLabel("  Station  ", SwingConstants.CENTER);
    lbl.setFont(wpFont);
    this.add(lbl);

    lbl = new JLabel("  Breadth  ", SwingConstants.CENTER);
    lbl.setFont(wpFont);
    this.add(lbl);

    lbl = new JLabel("  Height  ", SwingConstants.CENTER);
    lbl.setFont(wpFont);
    this.add(lbl);
    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));



    this.l = new JTextField[10];
    this.w = new JTextField[10];
    this.x = new JTextField[10];
    this.y = new JTextField[10];
    this.z = new JTextField[10];

    for (int i = 0; i < 10; i++) {

      lbl = new JLabel(Integer.toString(i + 1), SwingConstants.CENTER);
      lbl.setFont(wpFont);
      this.add(lbl);

      this.l[i] = new JTextField();
      this.l[i].getDocument().addDocumentListener(this);
      this.add(this.l[i]);

      this.w[i] = new JTextField();
      this.w[i].getDocument().addDocumentListener(this);
      this.w[i].addFocusListener(this);
      this.add(this.w[i]);

      this.x[i] = new JTextField();
      this.x[i].getDocument().addDocumentListener(this);
      this.x[i].addFocusListener(this);
      this.add(this.x[i]);

      this.y[i] = new JTextField();
      this.y[i].getDocument().addDocumentListener(this);
      this.y[i].addFocusListener(this);
      this.add(this.y[i]);

      this.z[i] = new JTextField();
      this.z[i].getDocument().addDocumentListener(this);
      this.z[i].addFocusListener(this);
      this.add(this.z[i]);

      this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

    }
    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
    this.lblTot.setFont(wpFont);
    this.lblWgt.setFont(wpFont);
    this.lblX.setFont(wpFont);
    this.lblY.setFont(wpFont);
    this.lblZ.setFont(wpFont);
    this.add(this.lblTot);
    this.add(this.lblWgt);
    this.add(this.lblX);
    this.add(this.lblY);
    this.add(this.lblZ);
    this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

  }// end constructor

  public void addWgts() {
    double tw = 0;
    double tx = 0;
    double ty = 0;
    double tz = 0;
    try {
      for (int i = 0; i < 10; i++) {
        tw += Double.parseDouble(this.w[i].getText());
        tx += Double.parseDouble(this.w[i].getText()) * Double.parseDouble(this.x[i].getText());
        ty += Double.parseDouble(this.w[i].getText()) * Double.parseDouble(this.y[i].getText());
        tz += Double.parseDouble(this.w[i].getText()) * Double.parseDouble(this.z[i].getText());
      }

      this.lblWgt.setText(this.boatCalc.bcf.DF1d.format(tw));
      if (tw > 0) {
        this.lblX.setText(this.boatCalc.bcf.DF1d.format(tx / tw));
        this.lblY.setText(this.boatCalc.bcf.DF1d.format(ty / tw));
        this.lblZ.setText(this.boatCalc.bcf.DF1d.format(tz / tw));
      } else {
        this.lblX.setText("n/a");
        this.lblY.setText("n/a");
        this.lblZ.setText("n/a");
      }

    } catch (final NumberFormatException e) {
      this.lblWgt.setText("n/a");
      this.lblX.setText("n/a");
      this.lblY.setText("n/a");
      this.lblZ.setText("n/a");
    }

  }// end addWgts

  @Override
  public void changedUpdate(final DocumentEvent e) {
    this.addWgts();
    this.bChanged = true;
  }

  @Override
  public void focusGained(final FocusEvent e) {
    final JTextField t = (JTextField) e.getComponent();
    t.select(0, 100);
  }

  @Override
  public void focusLost(final FocusEvent e) {}

  @Override
  public void insertUpdate(final DocumentEvent e) {
    this.addWgts();
    this.bChanged = true;
  }

  @Override
  public void removeUpdate(final DocumentEvent e) {
    this.addWgts();
    this.bChanged = true;
  }
}// end edWgtPanel
