package gui;
/*
 * boatCalc.java
 */

/*
 * @author Peter H. Vanderwaart Copyright 2004
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterJob;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.SortedSet;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import boat.Centerboard;
import boat.Hull;
import boat.Rig;
import boat.Rudder;
import boat.Sail;
import boat.rscFoil;
import geom.Line;
import geom.Point;
import geom.rawLine;
import util.bcFileFilter;
import util.bcFormat;
import util.bcUnits;



/* Programming Utilities */


public class boatCalc extends javax.swing.JFrame {
  public class hdCtrl extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    JToggleButton btnComp;

    JButton btnWgt;
    Dimension d;

    JPanel pnlBase;
    JPanel pnlButton;

    JPanel pnlHeel;
    JSlider slBase;
    JSlider slHeel;

    public hdCtrl(final int x, final int y) {
      this.d = new Dimension(x, y);
      this.setLayout(new FlowLayout());
      this.setBorder(BorderFactory.createEtchedBorder());

      final Border bcBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

      this.slBase = new JSlider();
      this.slBase.setPreferredSize(new Dimension(250, 42));

      this.slBase.addChangeListener(new ChangeListener() {
        @Override
        public void stateChanged(final ChangeEvent e) {
          double r = 4;
          if (boatCalc.this.hull.units.UNITS == 1) {
            r = 48;
          } else if (boatCalc.this.hull.units.UNITS == 2) {
            r = 1;
          } else if (boatCalc.this.hull.units.UNITS == 3) {
            r = 100;
          }
          boatCalc.this.hull.base = (hdCtrl.this.slBase.getValue()) / r;
          boatCalc.this.hull.setLines();
          boatCalc.this.hull.bChanged = true;
          boatCalc.this.hull.calcDisp();
          boatCalc.this.dispWgt.setWeights();
          boatCalc.this.dispFore.repaint();
          boatCalc.this.dispAft.repaint();
          boatCalc.this.dispWL.repaint();
          boatCalc.this.dispWet.repaint();
          boatCalc.this.dispStn.repaint();
          boatCalc.this.disp.repaint();
          boatCalc.this.body.repaint();
          boatCalc.this.plan.repaint();
        }
      });


      this.pnlBase = new JPanel();
      this.pnlBase.setBorder(BorderFactory.createTitledBorder(bcBorder, "Baseline Offset"));
      this.pnlBase.setLayout(new BorderLayout());
      this.pnlBase.add(this.slBase, BorderLayout.CENTER);
      this.add(this.pnlBase);

      this.slHeel = new JSlider(0, 90, (int) boatCalc.this.hull.angHeel);
      this.slHeel.setPreferredSize(new Dimension(250, 42));
      this.slHeel.setMajorTickSpacing(15);
      this.slHeel.setMinorTickSpacing(5);
      this.slHeel.setPaintTicks(true);
      this.slHeel.setPaintLabels(true);

      this.slHeel.addChangeListener(new ChangeListener() {
        @Override
        public void stateChanged(final ChangeEvent e) {
          boatCalc.this.hull.angHeel = hdCtrl.this.slHeel.getValue();
          boatCalc.this.hull.calcDisp();
          boatCalc.this.dispWgt.setWeights();
          boatCalc.this.dispFore.repaint();
          boatCalc.this.dispAft.repaint();
          boatCalc.this.dispWL.repaint();
          boatCalc.this.dispWet.repaint();
          boatCalc.this.dispStn.repaint();
          boatCalc.this.disp.repaint();
        }
      });


      this.pnlHeel = new JPanel();
      this.pnlHeel.setBorder(BorderFactory.createTitledBorder(bcBorder, "Angle of Heel"));
      this.pnlHeel.setLayout(new BorderLayout());
      // pnlHeel.setBackground(Color.lightGray);
      this.pnlHeel.add(this.slHeel, BorderLayout.CENTER);
      this.add(this.pnlHeel);

      this.pnlButton = new JPanel();
      this.pnlButton.setLayout(new FlowLayout());
      this.btnComp = new JToggleButton("Set Compare");
      this.btnComp.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          if (!hdCtrl.this.btnComp.isSelected()) {
            boatCalc.this.disp.bComp = false;
            boatCalc.this.disp.repaint();
            boatCalc.this.dispWL.repaint();
            boatCalc.this.dispWet.repaint();
            boatCalc.this.dispStn.repaint();
          } else {
            for (int j = 0; j <= boatCalc.this.hull.NDIV; j++) {
              boatCalc.this.disp.iComp[0][j] = boatCalc.this.disp.iCur[0][j];
              boatCalc.this.disp.iComp[1][j] = boatCalc.this.disp.iCur[1][j];
              boatCalc.this.dispWL.iComp[0][j] = boatCalc.this.dispWL.iCur[0][j];
              boatCalc.this.dispWL.iComp[1][j] = boatCalc.this.dispWL.iCur[1][j];
              boatCalc.this.dispWet.iComp[j] = boatCalc.this.dispWet.iCur[j];
            }
            boatCalc.this.disp.xComp = boatCalc.this.disp.xCur;
            boatCalc.this.disp.yComp = boatCalc.this.disp.yCur;
            boatCalc.this.disp.bComp = true;
            boatCalc.this.disp.repaint();
            boatCalc.this.dispWL.repaint();
            boatCalc.this.dispWet.repaint();
            boatCalc.this.dispStn.repaint();
          }
        }
      });

      this.btnWgt = new JButton("Set Weights");
      this.btnWgt.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          boatCalc.this.wgtEdit();
        }
      });

      this.pnlButton.add(this.btnComp);
      this.pnlButton.add(this.btnWgt);
      this.add(this.pnlButton);

    }

    @Override
    public Dimension getPreferredSize() {
      return this.d;
    }


  }// end hdCtrl
  class pnlCenterboard extends JPanel {
    class cbArea extends JPanel {
      /**
       *
       */
      private static final long serialVersionUID = 1L;
      Dimension d;
      JButton jbApply, jbClose;
      JLabel jlRArea, jlRCoA;
      JLabel jlSArea, jlSCoA;
      JLabel jlTArea, jlTCoA;

      public cbArea(final int x, final int y) {
        this.d = new Dimension(x, y);
        this.setLayout(new GridLayout(0, 2));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(new bcLabel("Immersed Areas", SwingConstants.LEFT));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

        this.add(new bcLabel("Centerboard - Area: ", SwingConstants.RIGHT));
        this.add(this.jlRArea = new JLabel("0.0", SwingConstants.LEFT));

        this.add(new bcLabel("CoA: ", SwingConstants.RIGHT));
        this.add(this.jlRCoA = new JLabel("0.0,0.0", SwingConstants.LEFT));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(new bcLabel("Skeg - Area: ", SwingConstants.RIGHT));
        this.add(this.jlSArea = new JLabel("0.0", SwingConstants.LEFT));
        this.add(new bcLabel("CoA: ", SwingConstants.RIGHT));
        this.add(this.jlSCoA = new JLabel("0.0,0.0", SwingConstants.LEFT));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(new bcLabel("Total - Area: ", SwingConstants.RIGHT));
        this.add(this.jlTArea = new JLabel("0.0", SwingConstants.LEFT));
        this.add(new bcLabel("CoA: ", SwingConstants.RIGHT));
        this.add(this.jlTCoA = new JLabel("0.0,0.0", SwingConstants.LEFT));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

        this.jbApply = new JButton("Apply");
        this.jbClose = new JButton("Close");

        this.jbApply.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(final ActionEvent e) {
            pnlCenterboard.this.saveCenterboard();
            pnlCenterboard.this.cbChange = false;
          }
        });

        this.jbClose.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(final ActionEvent e) {
            if (pnlCenterboard.this.cbChange) {
              final int n = JOptionPane.showConfirmDialog(boatCalc.this.f_edit,
                  "Data has changed. Do you wish to apply changes?", "Data Edit",
                  JOptionPane.YES_NO_OPTION);
              if (n == JOptionPane.YES_OPTION) {
                pnlCenterboard.this.saveCenterboard();
                pnlCenterboard.this.cbChange = false;
              }
            }
            boatCalc.this.f_board.setVisible(false);
          }
        });

        this.add(this.jbApply);
        this.add(this.jbClose);

      }

      @Override
      public Dimension getPreferredSize() {
        return this.d;
      }

      protected void setTable() {
        String s;
        double wa = 0;
        double wx = 0;
        double wy = 0;
        if (pnlCenterboard.this.pCb.board.use) {
          this.jlRArea.setText(boatCalc.this.bcf.DF1d.format(
              boatCalc.this.hull.units.coefArea() * pnlCenterboard.this.pCb.board.getWetArea())
              + boatCalc.this.hull.units.lblArea());
          s = boatCalc.this.bcf.DF1d.format(pnlCenterboard.this.pCb.board.getWetX()) + ", "
              + boatCalc.this.bcf.DF1d.format(pnlCenterboard.this.pCb.board.getWetY());
          this.jlRCoA.setText(s);
          wa = wa + pnlCenterboard.this.pCb.board.getWetArea();
          wx = wx + (pnlCenterboard.this.pCb.board.getWetArea()
              * pnlCenterboard.this.pCb.board.getWetX());
          wy = wy + (pnlCenterboard.this.pCb.board.getWetArea()
              * pnlCenterboard.this.pCb.board.getWetY());
        } else {
          this.jlRArea.setText("0.00");
          this.jlRCoA.setText("-.--, -.--");
        }


        if (wa > 0) {
          this.jlTArea.setText(boatCalc.this.bcf.DF1d.format(wa));
          this.jlTArea
              .setText(boatCalc.this.bcf.DF1d.format(boatCalc.this.hull.units.coefArea() * wa)
                  + boatCalc.this.hull.units.lblArea());
          s = boatCalc.this.bcf.DF1d.format(wx / wa) + ", "
              + boatCalc.this.bcf.DF1d.format(wy / wa);
          this.jlTCoA.setText(s);
        } else {
          this.jlTArea.setText("0.00");
          this.jlTCoA.setText("-.--, -.--");
        }
      }

    }// end cbArea
    class cbData extends JPanel implements ActionListener {
      /**
       *
       */
      private static final long serialVersionUID = 1L;
      JButton btnInc, btnDec;
      JComboBox<?> cbxInc;
      Dimension d;
      editFoil pCenterboard;
      editPivot pPivot;
      JRadioButton rbBLX, rbBLZ, rbBRX, rbBRZ;
      JRadioButton rbMoveX, rbMoveZ;
      JRadioButton rbPivotX, rbPivotZ;
      JRadioButton rbScale;
      JRadioButton rbTLX, rbTLZ, rbTRX, rbTRZ;

      public cbData(final int x, final int y) {

        JPanel pCB;

        this.d = new Dimension(x, y);
        this.setLayout(new BorderLayout());
        this.pCenterboard = new editFoil(pnlCenterboard.this.pCb.board);
        this.pCenterboard.setBorder(BorderFactory.createEtchedBorder());

        this.setLayout(new BorderLayout());
        this.add(this.pCenterboard, BorderLayout.CENTER);

        this.pPivot = new editPivot();
        this.add(this.pPivot, BorderLayout.LINE_END);

        final JPanel pInc = new JPanel();
        pInc.setPreferredSize(new Dimension(x - 5, (3 * y) / 10));
        pInc.setLayout(new GridLayout(0, 5));
        final ButtonGroup bgInc = new ButtonGroup();

        this.btnInc = new JButton("Increase");
        this.btnInc.addActionListener(this);
        pInc.add(this.btnInc);

        pCB = new JPanel();
        pCB.add(new JLabel("Top/Left ", SwingConstants.RIGHT));
        this.rbTLX = new JRadioButton("X");
        bgInc.add(this.rbTLX);
        pCB.add(this.rbTLX);
        this.rbTLZ = new JRadioButton("Z");
        bgInc.add(this.rbTLZ);
        pCB.add(this.rbTLZ);
        pInc.add(pCB);

        pCB = new JPanel();
        pCB.add(new JLabel("Top/Right ", SwingConstants.RIGHT));
        this.rbTRX = new JRadioButton("X");
        bgInc.add(this.rbTRX);
        pCB.add(this.rbTRX);
        this.rbTRZ = new JRadioButton("Z");
        bgInc.add(this.rbTRZ);
        pCB.add(this.rbTRZ);
        pInc.add(pCB);

        pCB = new JPanel();
        pCB.add(new JLabel("Pivot ", SwingConstants.RIGHT));
        this.rbPivotX = new JRadioButton("X");
        bgInc.add(this.rbPivotX);
        pCB.add(this.rbPivotX);
        this.rbPivotZ = new JRadioButton("Z");
        bgInc.add(this.rbPivotZ);
        pCB.add(this.rbPivotZ);
        pInc.add(pCB);

        pCB = new JPanel();
        pCB.add(new JLabel("Scale ", SwingConstants.RIGHT));
        this.rbScale = new JRadioButton("%");
        bgInc.add(this.rbScale);
        pCB.add(this.rbScale);
        // pCB.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
        pInc.add(pCB);


        this.btnDec = new JButton("Decrease");
        this.btnDec.addActionListener(this);
        pInc.add(this.btnDec);


        pCB = new JPanel();
        pCB.add(new JLabel("Bot/Left ", SwingConstants.RIGHT));
        this.rbBLX = new JRadioButton("X");
        bgInc.add(this.rbBLX);
        pCB.add(this.rbBLX);
        this.rbBLZ = new JRadioButton("Z");
        bgInc.add(this.rbBLZ);
        pCB.add(this.rbBLZ);
        pInc.add(pCB);

        pCB = new JPanel();
        pCB.add(new JLabel("Bot/Right ", SwingConstants.RIGHT));
        this.rbBRX = new JRadioButton("X");
        bgInc.add(this.rbBRX);
        pCB.add(this.rbBRX);
        this.rbBRZ = new JRadioButton("Z");
        bgInc.add(this.rbBRZ);
        pCB.add(this.rbBRZ);
        pInc.add(pCB);

        pCB = new JPanel();
        pCB.add(new JLabel("Move ", SwingConstants.RIGHT));
        this.rbMoveX = new JRadioButton("X");
        bgInc.add(this.rbMoveX);
        pCB.add(this.rbMoveX);
        this.rbMoveZ = new JRadioButton("Z");
        bgInc.add(this.rbMoveZ);
        pCB.add(this.rbMoveZ);
        pInc.add(pCB);

        pCB = new JPanel();
        pCB.setLayout(new GridLayout(0, 2));
        pCB.add(new JLabel("Step: ", SwingConstants.RIGHT));

        final String[] incs =
            {"0.01", "0.02", "0.05", "0.1", "0.2", "0.5", "1", "2", "5", "10", "20", "50", "100"};
        this.cbxInc = new JComboBox<Object>(incs);
        this.cbxInc.setEditable(true);
        this.cbxInc.setSelectedIndex(6);
        pCB.add(this.cbxInc);

        // tfInc = new JTextField("1.0",8);
        // pCB.add(tfInc);
        pInc.add(pCB);

        this.add(pInc, BorderLayout.PAGE_END);

      } // end constructor

      @Override
      public void actionPerformed(final ActionEvent e) {
        if ((e.getSource() == this.btnInc) || (e.getSource() == this.btnDec)) {

          final rscFoil f = pnlCenterboard.this.pCb.board;
          final editFoil eF = this.pCenterboard;
          final editPivot eP = this.pPivot;
          double d = 0;
          double v;
          double sgn;

          try {
            d = Double.parseDouble((String) this.cbxInc.getSelectedItem());
          } catch (final NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Unable to interperet step as number.", "Warning!",
                JOptionPane.ERROR_MESSAGE);
            return;
          }

          if (e.getSource() == this.btnInc) {
            sgn = 1.0;
          } else {
            sgn = -1.0;
          }

          if (this.rbTLX.isSelected() || this.rbMoveX.isSelected()) {
            try {
              v = Double.parseDouble(eF.ff[0][rscFoil.TL].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eF.ff[0][rscFoil.TL].setText(boatCalc.this.bcf.DF2d.format(v));
            f.setParamX(rscFoil.TL, v);
          }

          if (this.rbTLZ.isSelected() || this.rbMoveZ.isSelected()) {
            try {
              v = Double.parseDouble(eF.ff[1][rscFoil.TL].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eF.ff[1][rscFoil.TL].setText(boatCalc.this.bcf.DF2d.format(v));
            f.setParamY(rscFoil.TL, v);
          }


          if (this.rbTRX.isSelected() || this.rbMoveX.isSelected()) {
            try {
              v = Double.parseDouble(eF.ff[0][rscFoil.TR].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eF.ff[0][rscFoil.TR].setText(boatCalc.this.bcf.DF2d.format(v));
            f.setParamX(rscFoil.TR, v);
          }

          if (this.rbTRZ.isSelected() || this.rbMoveZ.isSelected()) {
            try {
              v = Double.parseDouble(eF.ff[1][rscFoil.TR].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eF.ff[1][rscFoil.TR].setText(boatCalc.this.bcf.DF2d.format(v));
            f.setParamY(rscFoil.TR, v);
          }


          if (this.rbBRX.isSelected() || this.rbMoveX.isSelected()) {
            try {
              v = Double.parseDouble(eF.ff[0][rscFoil.BR].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eF.ff[0][rscFoil.BR].setText(boatCalc.this.bcf.DF2d.format(v));
            f.setParamX(rscFoil.BR, v);
          }

          if (this.rbBRZ.isSelected() || this.rbMoveZ.isSelected()) {
            try {
              v = Double.parseDouble(eF.ff[1][rscFoil.BR].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eF.ff[1][rscFoil.BR].setText(boatCalc.this.bcf.DF2d.format(v));
            f.setParamY(rscFoil.BR, v);
          }

          if (this.rbBLX.isSelected() || this.rbMoveX.isSelected()) {
            try {
              v = Double.parseDouble(eF.ff[0][rscFoil.BL].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eF.ff[0][rscFoil.BL].setText(boatCalc.this.bcf.DF2d.format(v));
            f.setParamX(rscFoil.BL, v);
          }

          if (this.rbBLZ.isSelected() || this.rbMoveZ.isSelected()) {
            try {
              v = Double.parseDouble(eF.ff[1][rscFoil.BL].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eF.ff[1][rscFoil.BL].setText(boatCalc.this.bcf.DF2d.format(v));
            f.setParamY(rscFoil.BL, v);
          }

          if (this.rbPivotX.isSelected() || this.rbMoveX.isSelected()) {
            try {
              v = Double.parseDouble(eP.px.getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eP.px.setText(boatCalc.this.bcf.DF2d.format(v));
            pnlCenterboard.this.pCb.setPivotX(v);
          }

          if (this.rbPivotZ.isSelected() || this.rbMoveZ.isSelected()) {
            try {
              v = Double.parseDouble(eP.py.getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eP.py.setText(boatCalc.this.bcf.DF2d.format(v));
            pnlCenterboard.this.pCb.setPivotZ(v);
          }

          if (this.rbScale.isSelected()) {
            final double mx = pnlCenterboard.this.pCb.getPivotX();
            final double my = pnlCenterboard.this.pCb.getPivotZ();
            for (int i = 0; i < 4; i++) {
              v = mx + (0.01 * (100 + (sgn * d)) * (f.getParamX(i) - mx));
              eF.ff[0][i].setText(boatCalc.this.bcf.DF2d.format(v));
              f.setParamX(i, v);
              v = my + (0.01 * (100 + (sgn * d)) * (f.getParamY(i) - my));
              eF.ff[1][i].setText(boatCalc.this.bcf.DF2d.format(v));
              f.setParamY(i, v);
            }
          }

        }
        pnlCenterboard.this.pDraw.repaint();
        pnlCenterboard.this.pSpec.repaint();
        pnlCenterboard.this.pRpt.setTable();
      }

      @Override
      public Dimension getPreferredSize() {
        return this.d;
      }
    }// end cbData
    class cbDraw extends JPanel {
      /**
       *
       */
      private static final long serialVersionUID = 1L;
      Dimension d;

      public cbDraw(final int x, final int y) {
        this.d = new Dimension(x, y);
      }

      @Override
      public Dimension getPreferredSize() {
        return this.d;
      }

      @Override
      protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final double mx = this.getWidth();
        final double my = this.getHeight();
        final int ix = (int) mx;
        final int iy = (int) my;
        final int xb = 50;
        final int yb = (int) my / 2;
        int iu, iv, iw, iz;

        g.clearRect(0, 0, ix, iy);
        g.drawString("Centerboard", 10, 20);
        g.setColor(Color.red);
        g.drawLine(100, 12, 125, 12);
        g.setColor(Color.blue);
        g.drawLine(100, 17, 125, 17);
        g.setColor(Color.black);
        g.setColor(Color.black);

        if (!boatCalc.this.hull.valid) {
          return;
        }

        final double rx = (mx - 100.0) / (boatCalc.this.hull.gx_max - boatCalc.this.hull.gx_min);
        final double ry = (my - 75.0) / (boatCalc.this.hull.gy_max - boatCalc.this.hull.gy_min);
        final double r = Math.min(rx, ry);

        // draw waterline
        iu = xb + (int) (r * (boatCalc.this.hull.gx_min - boatCalc.this.hull.gx_min));
        iv = yb - (int) (r * (0.0 - boatCalc.this.hull.gz_min));
        iw = xb + (int) (r * (boatCalc.this.hull.gx_max - boatCalc.this.hull.gx_min));
        iz = yb - (int) (r * (0.0 - boatCalc.this.hull.gz_min));
        g.setColor(Color.blue);
        g.drawLine(iu, iv, iw, iz);

        // draw hull profile
        g.setColor(Color.black);
        double z1Lo = boatCalc.this.hull.gz_max;
        double z1Hi = boatCalc.this.hull.gz_min;
        double x1 = 0;

        final double[] minHull = new double[101];
        int idx = -1;

        for (double pct = 0.0; pct <= 1.0025; pct = pct + 0.01) {
          final double x = boatCalc.this.hull.gx_min
              + (pct * (boatCalc.this.hull.gx_max - boatCalc.this.hull.gx_min));
          final SortedSet<?> ss = boatCalc.this.hull.getStnSet(x, 0.0);
          final Iterator<?> si = ss.iterator();
          double zLo = boatCalc.this.hull.gz_max;
          double zHi = boatCalc.this.hull.gz_min;
          boolean bOk = false;
          while (si.hasNext()) {
            final Point p = (Point) si.next();
            zLo = Math.min(zLo, p.z);
            zHi = Math.max(zHi, p.z);
            bOk = true;
          }
          if (bOk && (pct > 0.0)) {
            iu = xb + (int) (r * (x1 - boatCalc.this.hull.gx_min));
            iv = yb - (int) (r * (z1Lo - boatCalc.this.hull.gz_min));
            iw = xb + (int) (r * (x - boatCalc.this.hull.gx_min));
            iz = yb - (int) (r * (zLo - boatCalc.this.hull.gz_min));
            g.drawLine(iu, iv, iw, iz);
            iv = yb - (int) (r * (z1Hi - boatCalc.this.hull.gz_min));
            iz = yb - (int) (r * (zHi - boatCalc.this.hull.gz_min));
            g.drawLine(iu, iv, iw, iz);
          }

          x1 = x;
          z1Lo = zLo;
          z1Hi = zHi;

          idx++;
          minHull[idx] = Math.min(zLo, 0.0) - boatCalc.this.hull.base;

        }
        pnlCenterboard.this.pCb.setMinHull(minHull, boatCalc.this.hull.gx_min,
            boatCalc.this.hull.gx_max, 100);

        // draw stems
        g.setColor(Color.lightGray);
        for (int iSL = 0; iSL <= 1; iSL++) {
          if (boatCalc.this.hull.bStems[iSL] && boatCalc.this.hull.sLines[iSL].valid) {
            double x = boatCalc.this.hull.sLines[iSL].hPoints[0].getX();
            double y = boatCalc.this.hull.sLines[iSL].hPoints[0].getZ();
            iu = xb + (int) (r * (x - boatCalc.this.hull.gx_min));
            iv = yb - (int) (r * (y - boatCalc.this.hull.gz_min));
            for (int j = 1; j < boatCalc.this.hull.sLines[iSL].hPoints.length; j++) {
              x = boatCalc.this.hull.sLines[iSL].hPoints[j].getX();
              y = boatCalc.this.hull.sLines[iSL].hPoints[j].getZ();
              iw = xb + (int) (r * (x - boatCalc.this.hull.gx_min));
              iz = yb - (int) (r * (y - boatCalc.this.hull.gz_min));
              g.drawLine(iu, iv, iw, iz);
              iu = iw;
              iv = iz;
            }
          }
        }

        // draw board
        if (pnlCenterboard.this.pCb.board.use) {

          // draw pivot point

          g.setColor(Color.red);
          final double xp = pnlCenterboard.this.pCb.getPivotX();
          final double yp = pnlCenterboard.this.pCb.getPivotZ() + boatCalc.this.hull.base;
          iu = xb + (int) (r * (xp - boatCalc.this.hull.gx_min));
          iv = yb - (int) (r * (yp - boatCalc.this.hull.gz_min));
          g.drawLine(iu + 6, iv, iu - 6, iv);
          g.drawLine(iu, iv + 6, iu, iv - 6);

          g.setColor(Color.lightGray);
          iu = xb + (int) (r * (pnlCenterboard.this.pCb.getRX(0) - boatCalc.this.hull.gx_min));
          iv = yb - (int) (r * (pnlCenterboard.this.pCb.getRZ(0) - boatCalc.this.hull.gz_min));
          iw = xb + (int) (r * (pnlCenterboard.this.pCb.getRX(1) - boatCalc.this.hull.gx_min));
          iz = yb - (int) (r * (pnlCenterboard.this.pCb.getRZ(1) - boatCalc.this.hull.gz_min));
          g.drawLine(iu, iv, iw, iz);

          iu = xb + (int) (r * (pnlCenterboard.this.pCb.getRX(2) - boatCalc.this.hull.gx_min));
          iv = yb - (int) (r * (pnlCenterboard.this.pCb.getRZ(2) - boatCalc.this.hull.gz_min));
          g.drawLine(iw, iz, iu, iv);

          iw = xb + (int) (r * (pnlCenterboard.this.pCb.getRX(3) - boatCalc.this.hull.gx_min));
          iz = yb - (int) (r * (pnlCenterboard.this.pCb.getRZ(3) - boatCalc.this.hull.gz_min));
          g.drawLine(iu, iv, iw, iz);

          iu = xb + (int) (r * (pnlCenterboard.this.pCb.getRX(0) - boatCalc.this.hull.gx_min));
          iv = yb - (int) (r * (pnlCenterboard.this.pCb.getRZ(0) - boatCalc.this.hull.gz_min));
          g.drawLine(iw, iz, iu, iv);

          // draw wet board

          // double testx = 7.11;
          // double testpct = 100.0*(testx-hull.gx_min)/(hull.gx_max-hull.gx_min);
          // int testint = Math.min(Math.max((int) testpct,0),99);
          // System.out.println(testx+" "+testpct+" "+testint+" "+minHull[testint]);

          // System.out.println("before");
          if (pnlCenterboard.this.pCb.board.getWetArea() > 0) {
            // System.out.println("in");

            g.setColor(Color.red);
            final SortedSet<?> wp = pnlCenterboard.this.pCb.board.getWetPts();
            final Iterator<?> pi = wp.iterator();
            if (pi.hasNext()) {
              final Point p0 = (Point) pi.next();
              Point p1 = new Point(p0);
              while (pi.hasNext()) {
                // System.out.println("Point");

                final Point p2 = (Point) pi.next();
                iu = xb + (int) (r * (p1.x - boatCalc.this.hull.gx_min));
                iv = yb - (int) (r * (p1.z - boatCalc.this.hull.gz_min));
                iw = xb + (int) (r * (p2.x - boatCalc.this.hull.gx_min));
                iz = yb - (int) (r * (p2.z - boatCalc.this.hull.gz_min));
                g.drawLine(iu, iv, iw, iz);
                p1 = p2;
              }
              iu = xb + (int) (r * (p1.x - boatCalc.this.hull.gx_min));
              iv = yb - (int) (r * (p1.z - boatCalc.this.hull.gz_min));
              iw = xb + (int) (r * (p0.x - boatCalc.this.hull.gx_min));
              iz = yb - (int) (r * (p0.z - boatCalc.this.hull.gz_min));
              // System.out.println("line");

              g.drawLine(iu, iv, iw, iz);
            }
            final int cx = xb
                + (int) (r * (pnlCenterboard.this.pCb.board.getWetX() - boatCalc.this.hull.gx_min));
            final int cy = yb
                - (int) (r * (pnlCenterboard.this.pCb.board.getWetY() - boatCalc.this.hull.gz_min));
            g.drawArc(cx - 5, cy - 5, 10, 10, 0, 360);
            g.drawLine(cx + 5, cy, cx - 5, cy);
            g.drawLine(cx, cy + 5, cx, cy - 5);
          }
        } // end if use board

        /*
         * g.setColor(Color.black);
         *
         * double tA = 0; double tX = 0; double tY = 0; if (pCb.board.use){tA = tA +
         * pCb.board.getWetArea(); tX = tX + pCb.board.getWetArea() * pCb.board.getWetX(); tY = tY +
         * pCb.board.getWetArea() * pCb.board.getWetY();}
         *
         * if (tA > 0){ tX = tX / tA; tY = tY / tA; int cx = xb + (int) (r * (tX - hull.gx_min));
         * int cy = yb - (int) (r * (tY - hull.gz_min)); g.drawLine(cx+4,cy+4,cx-4,cy+4);
         * g.drawLine(cx+4,cy-4,cx-4,cy-4); g.drawLine(cx-4,cy-4,cx-4,cy+4);
         * g.drawLine(cx+4,cy-4,cx+4,cy+4); g.drawLine(cx+6,cy,cx-6,cy);
         *
         * g.drawLine(cx,cy+6,cx,cy-6); }
         */


      } // end paintComponent

    }// end cbDraw
    class cbSpec extends JPanel {
      /**
       *
       */
      private static final long serialVersionUID = 1L;
      Dimension d;

      public cbSpec(final int x, final int y) {
        this.d = new Dimension(x, y);
      }

      @Override
      public Dimension getPreferredSize() {
        return this.d;
      }

      @Override
      protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        int iL = 20;
        final int iC1 = 20;
        final int iC2 = 80;
        final int iC3 = 250;
        final int iC4 = 325;
        int kL = 0;
        if (pnlCenterboard.this.pCb.board.use) {
          int jL = 0;
          g.drawString("Centerboard", iC1, iL);
          jL = 20;
          g.drawString("Area: ", iC1, iL + jL);
          g.drawString(boatCalc.this.bcf.DF1d.format(pnlCenterboard.this.pCb.board.getArea()), iC2,
              iL + jL);
          jL = jL + 20;
          g.drawString("CoA: ", iC1, iL + jL);
          g.drawString(
              boatCalc.this.bcf.DF2d.format(pnlCenterboard.this.pCb.board.getAreaX()) + ", "
                  + boatCalc.this.bcf.DF2d.format(pnlCenterboard.this.pCb.board.getAreaY()),
              iC2, iL + jL);
          jL = jL + 20;
          g.drawString("Points: ", iC1, iL + jL);
          for (int i = 0; i < 4; i++) {
            g.drawString(
                boatCalc.this.bcf.DF2d.format(pnlCenterboard.this.pCb.board.getParamX(i)) + ", "
                    + boatCalc.this.bcf.DF2d.format(pnlCenterboard.this.pCb.board.getParamY(i)),
                iC2, iL + jL);
            jL = jL + 20;
          }
          g.drawString("ref: baseline", iC2 + 90, (iL + jL) - 20);
          kL = jL;
          jL = 0;
          g.drawString("Immersed Portion", iC3, iL);
          jL = 20;
          g.drawString("Area: ", iC3, iL + jL);
          g.drawString(boatCalc.this.bcf.DF1d.format(pnlCenterboard.this.pCb.board.getWetArea()),
              iC4, iL + jL);
          jL = jL + 20;
          g.drawString("CoA: ", iC3, iL + jL);
          g.drawString(
              boatCalc.this.bcf.DF2d.format(pnlCenterboard.this.pCb.board.getWetX()) + ", "
                  + boatCalc.this.bcf.DF2d.format(pnlCenterboard.this.pCb.board.getWetY()),
              iC4, iL + jL);
          jL = jL + 20;
          g.drawString("Points: ", iC3, iL + jL);

          final SortedSet<?> wp = pnlCenterboard.this.pCb.board.getWetPts();
          final Iterator<?> pi = wp.iterator();
          while (pi.hasNext()) {
            final Point p = (Point) pi.next();
            g.drawString(
                boatCalc.this.bcf.DF2d.format(p.x) + ", " + boatCalc.this.bcf.DF2d.format(p.z), iC4,
                iL + jL);
            jL = jL + 20;
          }
          g.drawString("ref: lwl", iC4 + 90, (iL + jL) - 20);
          kL = Math.max(kL, jL);
        }
        iL = iL + kL;


      }// end paintComponent
    }// ends cbSpec
    public class cbTabOrder extends FocusTraversalPolicy {
      cbData r;

      public cbTabOrder(final pnlCenterboard p) {
        this.r = p.pData;
      }

      @Override
      public Component getComponentAfter(final Container focusCycleRoot,
          final Component aComponent) {
        return aComponent;
      }

      @Override
      public Component getComponentBefore(final Container focusCycleRoot,
          final Component aComponent) {
        return aComponent;
      }

      @Override
      public Component getDefaultComponent(final Container focusCycleRoot) {
        return this.r;
      }

      @Override
      public Component getFirstComponent(final Container focusCycleRoot) {
        return this.r;
      }

      @Override
      public Component getLastComponent(final Container focusCycleRoot) {
        return this.r;
      }
    }// end cbTabOrder
    class editFoil extends JPanel
        implements DocumentListener, ItemListener, FocusListener, ActionListener {
      /**
       *
       */
      private static final long serialVersionUID = 1L;
      boolean bChanged;
      JCheckBox cbFoil;
      Dimension d;
      JTextField[][] ff;
      rscFoil fo;


      public editFoil(final rscFoil f) {
        super(new GridLayout(0, 5));
        this.d = new Dimension(600, 150);
        final Font efFont = new Font("Serif", Font.BOLD, 14);
        this.fo = f;

        this.ff = new JTextField[2][4];
        for (int i = 0; i < 4; i++) {
          this.ff[0][i] = new JTextField(Double.toString(f.getParamX(i)), 6);
          this.ff[0][i].getDocument().addDocumentListener(this);
          this.ff[0][i].addFocusListener(this);
          this.ff[1][i] = new JTextField(Double.toString(f.getParamY(i)), 6);
          this.ff[1][i].getDocument().addDocumentListener(this);
          this.ff[1][i].addFocusListener(this);
        }


        JLabel lbl;
        JPanel pC;

        // row 1, labels
        this.cbFoil = new JCheckBox("Use");
        this.cbFoil.setHorizontalAlignment(SwingConstants.LEFT);
        this.cbFoil.setSelected(this.fo.use);
        this.cbFoil.addActionListener(this);
        this.add(this.cbFoil);
        lbl = new JLabel("Left", SwingConstants.RIGHT);
        lbl.setFont(efFont);
        this.add(lbl);
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        lbl = new JLabel("Right", SwingConstants.RIGHT);
        lbl.setFont(efFont);
        this.add(lbl);
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

        // row 2, top
        lbl = new JLabel("Top:", SwingConstants.CENTER);
        lbl.setFont(efFont);
        this.add(lbl);

        pC = new JPanel();
        pC.add(new JLabel("X:", SwingConstants.RIGHT));
        pC.add(this.ff[0][rscFoil.TL]);
        this.add(pC);

        pC = new JPanel();
        pC.add(new JLabel("Z:", SwingConstants.RIGHT));
        pC.add(this.ff[1][rscFoil.TL]);
        this.add(pC);

        pC = new JPanel();
        pC.add(new JLabel("X:", SwingConstants.RIGHT));
        pC.add(this.ff[0][rscFoil.TR]);
        this.add(pC);

        pC = new JPanel();
        pC.add(new JLabel("Z:", SwingConstants.RIGHT));
        pC.add(this.ff[1][rscFoil.TR]);
        this.add(pC);

        // row 3, bottom
        lbl = new JLabel("Bottom:", SwingConstants.CENTER);
        lbl.setFont(efFont);
        this.add(lbl);
        pC = new JPanel();
        pC.add(new JLabel("X:", SwingConstants.RIGHT));
        pC.add(this.ff[0][rscFoil.BL]);
        this.add(pC);

        pC = new JPanel();
        pC.add(new JLabel("Z:", SwingConstants.RIGHT));
        pC.add(this.ff[1][rscFoil.BL]);
        this.add(pC);
        pC = new JPanel();
        pC.add(new JLabel("X:", SwingConstants.RIGHT));
        pC.add(this.ff[0][rscFoil.BR]);
        this.add(pC);

        pC = new JPanel();
        pC.add(new JLabel("Z:", SwingConstants.RIGHT));
        pC.add(this.ff[1][rscFoil.BR]);
        this.add(pC);

      }

      @Override
      public void actionPerformed(final ActionEvent e) {
        this.fo.use = this.cbFoil.isSelected();
        pnlCenterboard.this.cbChange = true;
        this.bChanged = true;
        pnlCenterboard.this.pDraw.repaint();
        pnlCenterboard.this.pSpec.repaint();
        pnlCenterboard.this.pRpt.setTable();
      }

      @Override
      public void changedUpdate(final DocumentEvent e) {
        pnlCenterboard.this.cbChange = true;
        this.bChanged = true;
      }

      @Override
      public void focusGained(final FocusEvent e) {
        final JTextField t = (JTextField) e.getComponent();
        t.select(0, 100);
      }

      @Override
      public void focusLost(final FocusEvent e) {
        double v, w;
        try {
          for (int i = 0; i < 4; i++) {
            v = Double.parseDouble(this.ff[0][i].getText());
            w = Double.parseDouble(this.ff[1][i].getText());
            this.fo.setParamXY(i, v, w);
          }
        } catch (final NumberFormatException nfe) {
          JOptionPane.showMessageDialog(boatCalc.this.f_edit, "Bad number format in data entry.",
              "Warning!", JOptionPane.ERROR_MESSAGE);
          return;
        }

        pnlCenterboard.this.cbChange = true;
        this.bChanged = true;
        pnlCenterboard.this.pDraw.repaint();
        pnlCenterboard.this.pSpec.repaint();
        pnlCenterboard.this.pRpt.setTable();

      }

      @Override
      public Dimension getPreferredSize() {
        return this.d;
      }

      @Override
      public void insertUpdate(final DocumentEvent e) {
        pnlCenterboard.this.cbChange = true;
        this.bChanged = true;
      }

      @Override
      public void itemStateChanged(final ItemEvent e) {
        pnlCenterboard.this.cbChange = true;
        this.bChanged = true;
      }

      @Override
      public void removeUpdate(final DocumentEvent e) {
        pnlCenterboard.this.cbChange = true;
        this.bChanged = true;
      }

    }// end editFoil
    class editPivot extends JPanel
        implements DocumentListener, ItemListener, FocusListener, ActionListener {

      /**
       *
       */
      private static final long serialVersionUID = 1L;
      boolean bChanged;
      JTextField px;
      JTextField py;
      JSlider sa;

      public editPivot() {
        super(new GridLayout(0, 1));
        this.setBorder(BorderFactory.createEtchedBorder());

        final Font efFont = new Font("Serif", Font.BOLD, 14);
        JLabel lbl;
        JPanel pRow;
        JPanel pCol;
        this.px = new JTextField(Double.toString(0.0), 6);
        this.px.getDocument().addDocumentListener(this);
        this.px.addFocusListener(this);
        this.py = new JTextField(Double.toString(0.0), 6);
        this.py.getDocument().addDocumentListener(this);
        this.py.addFocusListener(this);

        lbl = new JLabel("Pivot", SwingConstants.CENTER);
        lbl.setFont(efFont);
        this.add(lbl);

        pRow = new JPanel();
        pCol = new JPanel();
        pCol.add(new JLabel("X:", SwingConstants.RIGHT));
        pCol.add(this.px);
        pRow.add(pCol);
        pCol = new JPanel();
        pCol.add(new JLabel("Y:", SwingConstants.RIGHT));
        pCol.add(this.py);
        pRow.add(pCol);
        this.add(pRow);

        this.sa = new JSlider(-90, 90, 0);

        // sa.setModel(new DefaultBoundedRangeModel(-90,0,0,90));
        this.sa.setMajorTickSpacing(30);
        this.sa.setMinorTickSpacing(5);
        this.sa.setPaintLabels(true);
        this.sa.setPaintTicks(true);


        this.sa.addChangeListener(new ChangeListener() {
          @Override
          public void stateChanged(final ChangeEvent e) {
            final double a = editPivot.this.sa.getValue();
            pnlCenterboard.this.pCb.setPivotAngle(a);
            editPivot.this.bChanged = true;
            pnlCenterboard.this.pDraw.repaint();
            pnlCenterboard.this.pSpec.repaint();
            pnlCenterboard.this.pRpt.setTable();
          }
        });
        this.add(this.sa);
      }

      @Override
      public void actionPerformed(final ActionEvent e) {
        // fo.use = cbFoil.isSelected();
        pnlCenterboard.this.cbChange = true;
        this.bChanged = true;
        pnlCenterboard.this.pDraw.repaint();
        pnlCenterboard.this.pSpec.repaint();
        pnlCenterboard.this.pRpt.setTable();
      }

      @Override
      public void changedUpdate(final DocumentEvent e) {
        pnlCenterboard.this.cbChange = true;
        this.bChanged = true;
      }

      @Override
      public void focusGained(final FocusEvent e) {
        final JTextField t = (JTextField) e.getComponent();
        t.select(0, 100);
      }

      @Override
      public void focusLost(final FocusEvent e) {
        double v, w, a;
        try {
          for (int i = 0; i < 4; i++) {
            v = Double.parseDouble(this.px.getText());
            w = Double.parseDouble(this.py.getText());
            pnlCenterboard.this.pCb.setPivotX(v);
            pnlCenterboard.this.pCb.setPivotZ(w);
            a = this.sa.getValue();
            pnlCenterboard.this.pCb.setPivotAngle(a);
          }
        } catch (final NumberFormatException nfe) {
          JOptionPane.showMessageDialog(boatCalc.this.f_edit, "Bad number format in data entry.",
              "Warning!", JOptionPane.ERROR_MESSAGE);
          return;
        }

        pnlCenterboard.this.cbChange = true;
        this.bChanged = true;
        pnlCenterboard.this.pDraw.repaint();
        pnlCenterboard.this.pSpec.repaint();
        pnlCenterboard.this.pRpt.setTable();

      }

      @Override
      public void insertUpdate(final DocumentEvent e) {
        pnlCenterboard.this.cbChange = true;
        this.bChanged = true;
      }

      @Override
      public void itemStateChanged(final ItemEvent e) {
        pnlCenterboard.this.cbChange = true;
        this.bChanged = true;
      }

      @Override
      public void removeUpdate(final DocumentEvent e) {
        pnlCenterboard.this.cbChange = true;
        this.bChanged = true;
      }

    } // end editPivot

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Border bCb;
    boolean cbChange;

    Centerboard pCb;

    cbData pData;

    JTabbedPane pDisp;

    cbDraw pDraw;

    cbArea pRpt;

    cbSpec pSpec;

    cbTabOrder to;

    public pnlCenterboard() {

      if (boatCalc.this.hull.board.valid) {
        this.pCb = (Centerboard) boatCalc.this.hull.board.clone();
      } else {
        this.pCb = new Centerboard();
      }
      this.pCb.setBase(boatCalc.this.hull.base);
      this.cbChange = false;

      this.bCb = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
      this.setLayout(new BorderLayout());

      this.pDisp = new JTabbedPane();

      this.pDraw = new cbDraw(750, 325);
      this.pDraw.setBackground(Color.white);
      this.pDraw.setBorder(this.bCb);
      this.pDisp.add(this.pDraw, "Drawing");

      this.pSpec = new cbSpec(750, 325);
      this.pSpec.setBackground(Color.white);
      this.pSpec.setBorder(this.bCb);
      this.pDisp.add(this.pSpec, "Dimensions");

      this.add(this.pDisp, BorderLayout.CENTER);

      this.pRpt = new cbArea(180, 200);
      this.add(this.pRpt, BorderLayout.LINE_END);


      this.pData = new cbData(750, 185);
      this.pData.setBorder(this.bCb);
      this.add(this.pData, BorderLayout.PAGE_END);

      // to = new cbTabOrder(this);
      // f_board.setFocusTraversalPolicy(to);

      boatCalc.this.f_board.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(final WindowEvent e) {
          if (pnlCenterboard.this.cbChange) {
            final int n = JOptionPane.showConfirmDialog(boatCalc.this.f_sailplan,
                "Data has changed. Do you wish to apply changes?", "Sailplan Design",
                JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
              pnlCenterboard.this.saveCenterboard();
            }
          }
          boatCalc.this.f_board.setVisible(false);
        }
      });

      this.pDraw.repaint();
      this.pSpec.repaint();
      this.pRpt.setTable();

    }// end constructor


    public void saveCenterboard() {
      boatCalc.this.hull.board = (Centerboard) this.pCb.clone();
      boatCalc.this.hull.bChanged = true;
    }

  }// end pnlCenterboard
  class pnlDataEntry extends JPanel implements DocumentListener, ItemListener, FocusListener {
    class edLinePanel extends JPanel implements ActionListener {
      /**
       *
       */
      private static final long serialVersionUID = 1L;
      public JButton btnWtr, btnButt;
      public int nf;
      public JCheckBox[] v;
      public JTextField[] x;
      public JTextField[] y;
      public JTextField[] z;

      public edLinePanel(final int n) {

        final Font lpFont = new Font("Serif", Font.BOLD, 14);
        JLabel lbl;
        this.nf = n;
        this.setLayout(new GridLayout(0, 6));

        lbl = new JLabel("   #  ", SwingConstants.CENTER);
        lbl.setFont(lpFont);
        this.add(lbl);

        lbl = new JLabel("  Station  ", SwingConstants.CENTER);
        lbl.setFont(lpFont);
        this.add(lbl);

        lbl = new JLabel("  Breadth  ", SwingConstants.CENTER);
        lbl.setFont(lpFont);
        this.add(lbl);

        lbl = new JLabel("  Height  ", SwingConstants.CENTER);
        lbl.setFont(lpFont);
        this.add(lbl);

        lbl = new JLabel("  In Use  ", SwingConstants.LEFT);
        lbl.setFont(lpFont);
        this.add(lbl);

        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

        this.x = new JTextField[n];
        this.y = new JTextField[n];
        this.z = new JTextField[n];
        this.v = new JCheckBox[n];

        for (int i = 0; i < n; i++) {

          lbl = new JLabel(Integer.toString(i), SwingConstants.CENTER);
          lbl.setFont(lpFont);
          this.add(lbl);

          this.x[i] = new JTextField();
          this.add(this.x[i]);

          this.y[i] = new JTextField();
          this.add(this.y[i]);

          this.z[i] = new JTextField();
          this.add(this.z[i]);

          this.v[i] = new JCheckBox();
          this.add(this.v[i]);
          if (i == 1) {
            this.btnWtr = new JButton("Waterline");
            this.btnWtr.addActionListener(this);
            this.add(this.btnWtr);
          } else if (i == 3) {
            this.btnButt = new JButton("Buttock");
            this.btnButt.addActionListener(this);
            this.add(this.btnButt);
          } else {
            this.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20),
                new Dimension(20, 20)));
          }
        }

      }// end constructor

      @Override
      public void actionPerformed(final ActionEvent e) {
        final JButton btn = (JButton) e.getSource();
        boolean bOpt;
        String prompt;
        if (0 == btn.getText().compareTo("Waterline")) {
          bOpt = true;
          prompt = "Height from baseline: ";
        } else {
          bOpt = false;
          prompt = "Breadth from centerline: ";
        }

        final String s = JOptionPane.showInputDialog(boatCalc.this.f_edit, prompt);
        if (s == null) {
          return;
        }

        try {
          Double.parseDouble(s);
        } catch (final NumberFormatException nfe) {
          JOptionPane.showMessageDialog(boatCalc.this.f_edit, "Bad number format.", "Warning!",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        for (int i = 0; i < this.nf; i++) {
          if (bOpt) {
            this.z[i].setText(s);
          } else {
            this.y[i].setText(s);
          }
        }
      }
    }// end edLinePanel
    public class pdeTabOrder extends FocusTraversalPolicy {
      pnlDataEntry pde;

      public pdeTabOrder(final pnlDataEntry de) {
        this.pde = de;
      }

      @Override
      public Component getComponentAfter(final Container focusCycleRoot,
          final Component aComponent) {

        if (aComponent.equals(this.pde.btnHName)) {
          return this.pde.btnNA;
        }
        if (aComponent.equals(this.pde.btnNA)) {
          return this.pde.btnName;
        }
        if (aComponent.equals(this.pde.btnName)) {
          return this.pde.btnInsert;
        }
        if (aComponent.equals(this.pde.btnInsert)) {
          return this.pde.btnAdd;
        }
        if (aComponent.equals(this.pde.btnAdd)) {
          return this.pde.btnDele;
        }
        if (aComponent.equals(this.pde.btnDele)) {
          return this.pde.btnSave;
        }
        if (aComponent.equals(this.pde.btnSave)) {
          return this.pde.btnClose;
        }

        if (this.pde.tp.getSelectedIndex() == 0) {
          if (aComponent.equals(this.pde.btnClose)) {
            return this.pde.stn[0];
          }
          for (int i = 1; i < this.pde.stn.length; i++) {
            if (aComponent.equals(this.pde.stn[i - 1])) {
              return this.pde.stn[i];
            }
          }
          return this.pde.btnHName;
        } else {
          final edLinePanel pL = (edLinePanel) this.pde.tp.getSelectedComponent();

          if (aComponent.equals(this.pde.btnClose)) {
            return pL.x[0];
          }
          if (aComponent.equals(pL.btnWtr)) {
            return pL.btnButt;
          }
          if (aComponent.equals(pL.btnButt)) {
            return this.pde.btnHName;
          }

          for (int i = 0; i < pL.x.length; i++) {
            if (aComponent.equals(pL.x[i])) {
              return pL.y[i];
            }
            if (aComponent.equals(pL.y[i])) {
              return pL.z[i];
            }
            if (aComponent.equals(pL.z[i])) {
              return pL.v[i];
            }
            if (aComponent.equals(pL.v[i])) {
              final int j = i + 1;
              if (j > (pL.x.length - 1)) {
                return pL.btnWtr;
              }
              return pL.x[j];
            }
          }
        }
        return aComponent;
      }

      @Override
      public Component getComponentBefore(final Container focusCycleRoot,
          final Component aComponent) {

        if (aComponent.equals(this.pde.btnNA)) {
          return this.pde.btnHName;
        }
        if (aComponent.equals(this.pde.btnName)) {
          return this.pde.btnNA;
        }
        if (aComponent.equals(this.pde.btnInsert)) {
          return this.pde.btnName;
        }
        if (aComponent.equals(this.pde.btnAdd)) {
          return this.pde.btnInsert;
        }
        if (aComponent.equals(this.pde.btnDele)) {
          return this.pde.btnAdd;
        }
        if (aComponent.equals(this.pde.btnSave)) {
          return this.pde.btnDele;
        }
        if (aComponent.equals(this.pde.btnClose)) {
          return this.pde.btnSave;
        }

        if (this.pde.tp.getSelectedIndex() == 0) {
          if (aComponent.equals(this.pde.btnHName)) {
            return this.pde.stn[this.pde.stn.length - 1];
          }
          for (int i = 0; i < (this.pde.stn.length - 1); i++) {
            if (aComponent.equals(this.pde.stn[i + 1])) {
              return this.pde.stn[i];
            }
          }
          return this.pde.btnClose;
        } else {
          final edLinePanel pL = (edLinePanel) this.pde.tp.getSelectedComponent();
          if (aComponent.equals(this.pde.btnHName)) {
            return pL.btnButt;
          }
          if (aComponent.equals(pL.btnButt)) {
            return pL.btnWtr;
          }
          if (aComponent.equals(pL.btnWtr)) {
            return pL.v[pL.v.length - 1];
          }

          for (int i = 0; i < pL.x.length; i++) {
            if (aComponent.equals(pL.v[i])) {
              return pL.z[i];
            }
            if (aComponent.equals(pL.z[i])) {
              return pL.y[i];
            }
            if (aComponent.equals(pL.y[i])) {
              return pL.x[i];
            }
            if (aComponent.equals(pL.x[i])) {
              final int j = i - 1;
              if (j < 0) {
                return this.pde.btnClose;
              }
              return pL.v[j];
            }
          }
        }
        return aComponent;
      }

      @Override
      public Component getDefaultComponent(final Container focusCycleRoot) {
        return this.pde.btnClose;
      }

      @Override
      public Component getFirstComponent(final Container focusCycleRoot) {
        return this.pde.btnClose;
      }

      @Override
      public Component getLastComponent(final Container focusCycleRoot) {
        return this.pde.btnClose;
      }
    }// end pdeTabOrder

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    boolean bChanged = false;
    JButton btnHName, btnNA, btnName, btnInsert, btnAdd, btnDele, btnSave, btnClose;
    JPanel de = new JPanel();
    String DName;
    JLabel lblDName;
    JLabel lblNA;
    String NA;

    JTextField[] stn = new JTextField[boatCalc.this.hull.Stations.length];


    JTabbedPane tp = new JTabbedPane();

    public pnlDataEntry() {
      super(new GridLayout(1, 1));
      JLabel lbl;
      final Font deFont = new Font("Serif", Font.BOLD, 14);
      this.de.setLayout(new BorderLayout());

      this.DName = boatCalc.this.hull.boatname;
      this.NA = boatCalc.this.hull.designer;
      this.lblDName = new JLabel(this.DName);
      this.lblDName.setFont(deFont);
      this.lblNA = new JLabel(this.NA);
      this.lblNA.setFont(deFont);

      final JPanel ttl = new JPanel();
      ttl.setLayout(new GridLayout(0, 3));
      ttl.add(this.lblDName);
      ttl.add(this.lblNA);
      ttl.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
      this.de.add(ttl, BorderLayout.PAGE_START);

      final JPanel pO = new JPanel();
      pO.setLayout(new GridLayout(0, 6));

      pO.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

      lbl = new JLabel("  Station  ", SwingConstants.CENTER);
      lbl.setFont(deFont);
      pO.add(lbl);
      lbl = new JLabel("  Value  ", SwingConstants.LEFT);
      lbl.setFont(deFont);
      pO.add(lbl);
      pO.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
      pO.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
      pO.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));


      for (int i = 0; i < boatCalc.this.hull.Stations.length; i++) {

        pO.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

        lbl = new JLabel(Integer.toString(i), SwingConstants.CENTER);
        lbl.setFont(deFont);
        this.stn[i] = new JTextField(Double.toString(boatCalc.this.hull.Stations[i]));
        this.stn[i].getDocument().addDocumentListener(this);
        this.stn[i].addFocusListener(this);

        lbl.setLabelFor(this.stn[i]);
        pO.add(lbl);
        pO.add(this.stn[i]);
        pO.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        pO.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        pO.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

      }
      this.tp.addTab("Stations", pO);

      edLinePanel pL = new edLinePanel(this.stn.length);
      final ListIterator<?> l = boatCalc.this.hull.Offsets.listIterator();
      while (l.hasNext()) {
        final rawLine rL = (rawLine) l.next();
        final Line ln = rL.ln;

        pL = new edLinePanel(this.stn.length);

        for (int i = 0; i < boatCalc.this.hull.Stations.length; i++) {

          pL.x[i].setText(Double.toString(ln.valX(i)));
          pL.x[i].getDocument().addDocumentListener(this);
          pL.x[i].addFocusListener(this);

          pL.y[i].setText(Double.toString(ln.valY(i)));
          pL.y[i].getDocument().addDocumentListener(this);
          pL.y[i].addFocusListener(this);

          pL.z[i].setText(Double.toString(ln.valZ(i)));
          pL.z[i].getDocument().addDocumentListener(this);
          pL.z[i].addFocusListener(this);

          pL.v[i].setSelected(ln.valid(i));
          pL.v[i].addItemListener(this);

        }
        this.tp.addTab(rL.lnName, pL);
      }

      final Border bcBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
      this.de.setBorder(BorderFactory.createTitledBorder(bcBorder));

      this.de.add(this.tp, BorderLayout.CENTER);

      final JPanel bp = new JPanel();
      bp.setLayout(new FlowLayout());

      this.btnHName = new JButton("Design Name");
      this.btnHName.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          final String s = JOptionPane.showInputDialog("Design Name:", pnlDataEntry.this.DName);
          if ((s != null) && (s.length() > 0)) {
            pnlDataEntry.this.DName = s;
            pnlDataEntry.this.lblDName.setText("Design: " + pnlDataEntry.this.DName);
            pnlDataEntry.this.bChanged = true;
          }
        }
      });

      this.btnNA = new JButton("Designer");
      this.btnNA.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          final String s = JOptionPane.showInputDialog("Designer:", pnlDataEntry.this.NA);
          if ((s != null) && (s.length() > 0)) {
            pnlDataEntry.this.NA = s;
            pnlDataEntry.this.lblNA.setText("Designer: " + pnlDataEntry.this.NA);
            pnlDataEntry.this.bChanged = true;
          }
        }
      });

      this.btnName = new JButton("Line Name");
      this.btnName.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          final int i = pnlDataEntry.this.tp.getSelectedIndex();
          if (i == 0) {
            JOptionPane.showMessageDialog(boatCalc.this.f_edit,
                "Name 'Stations' cannot be changed.", "Warning!", JOptionPane.ERROR_MESSAGE);
            return;
          }

          final String s = JOptionPane.showInputDialog("Name:");
          if ((s != null) && (s.length() > 0)) {
            pnlDataEntry.this.tp.setTitleAt(i, s);
            pnlDataEntry.this.bChanged = true;
          }
        }
      });

      this.btnInsert = new JButton("Insert Line");
      this.btnInsert.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          final int n = JOptionPane.showConfirmDialog(boatCalc.this.f_edit,
              "Insert additional Line?", "Data Edit", JOptionPane.YES_NO_OPTION);
          if (n == JOptionPane.YES_OPTION) {
            final String s = JOptionPane.showInputDialog(boatCalc.this.f_edit, "Name:");
            if ((s != null) && (s.length() > 0)) {
              pnlDataEntry.this.addLine(1, s);
            }
          }
        }
      });

      this.btnAdd = new JButton("Add Line");
      this.btnAdd.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          final int n = JOptionPane.showConfirmDialog(boatCalc.this.f_edit, "Add additional Line?",
              "Data Edit", JOptionPane.YES_NO_OPTION);
          if (n == JOptionPane.YES_OPTION) {
            final String s = JOptionPane.showInputDialog(boatCalc.this.f_edit, "Name:");
            if ((s != null) && (s.length() > 0)) {
              pnlDataEntry.this.addLine(0, s);
            }
          }
        }
      });

      this.btnDele = new JButton("Delete Line");
      this.btnDele.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          final int i = pnlDataEntry.this.tp.getSelectedIndex();
          if (i == 0) {
            JOptionPane.showMessageDialog(boatCalc.this.f_edit,
                "Stations pannel cannot be deleted.", "Warning!", JOptionPane.ERROR_MESSAGE);
            return;
          }

          final int n = JOptionPane.showConfirmDialog(boatCalc.this.f_edit, "Delete Selected line?",
              "Data Edit", JOptionPane.YES_NO_OPTION);
          if (n == JOptionPane.YES_OPTION) {
            pnlDataEntry.this.tp.remove(i);
            pnlDataEntry.this.bChanged = true;
          }

        }
      });


      this.btnSave = new JButton("Apply");
      this.btnSave.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          pnlDataEntry.this.saveEdit();
        }
      });


      this.btnClose = new JButton("Close");
      this.btnClose.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          if (pnlDataEntry.this.bChanged) {
            final int n = JOptionPane.showConfirmDialog(boatCalc.this.f_edit,
                "Data has changed. Do you wish to apply changes?", "Data Edit",
                JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
              pnlDataEntry.this.saveEdit();
            }
          }
          boatCalc.this.f_edit.setVisible(false);
        }
      });

      bp.add(this.btnHName);
      bp.add(this.btnNA);
      bp.add(this.btnName);
      bp.add(this.btnInsert);
      bp.add(this.btnAdd);
      bp.add(this.btnDele);
      bp.add(this.btnSave);
      bp.add(this.btnClose);

      // de.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new
      // Dimension(20,20)),BorderLayout.PAGE_START);

      this.de.add(bp, BorderLayout.PAGE_END);
      boatCalc.this.f_edit.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(final WindowEvent e) {
          if (pnlDataEntry.this.bChanged) {
            final int n = JOptionPane.showConfirmDialog(boatCalc.this.f_edit,
                "Data has changed. Do you wish to apply changes?", "Data Edit",
                JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
              pnlDataEntry.this.saveEdit();
            }
          }
          boatCalc.this.f_edit.setVisible(false);
        }
      });

      this.add(this.de);

      boatCalc.this.f_edit.setFocusTraversalPolicy(new pdeTabOrder(this));

    }// end constructor PnlDataEntry

    public void addLine(int it, final String name) {

      // add new line

      final edLinePanel pL = new edLinePanel(this.stn.length);

      for (int i = 0; i < boatCalc.this.hull.Stations.length; i++) {

        pL.x[i].setText("00");
        pL.x[i].setText(this.stn[i].getText());
        pL.x[i].getDocument().addDocumentListener(this);
        pL.x[i].addFocusListener(this);

        pL.y[i].setText("0.0");
        pL.y[i].getDocument().addDocumentListener(this);
        pL.y[i].addFocusListener(this);

        pL.z[i].setText("0.0");
        pL.z[i].getDocument().addDocumentListener(this);
        pL.z[i].addFocusListener(this);

        pL.v[i].setSelected(true);
        pL.v[i].addItemListener(this);

      }
      if (it < 1) {
        this.tp.addTab(name, pL);
      } else {
        it = Math.max(1, this.tp.getSelectedIndex());
        this.tp.insertTab(name, null, pL, "new tab", it);
      }
      this.tp.setSelectedComponent(pL);
      this.bChanged = true;
    } // end addLine

    // public Dimension getPreferredSize(){return new Dimension(700,200) ; }
    @Override
    public void changedUpdate(final DocumentEvent e) {
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
      this.bChanged = true;
    }

    @Override
    public void itemStateChanged(final ItemEvent e) {
      this.bChanged = true;
    }

    @Override
    public void removeUpdate(final DocumentEvent e) {
      this.bChanged = true;
    }


    public void saveEdit() {
      final double[] sta = new double[boatCalc.this.hull.Stations.length];
      final ArrayList<rawLine> o = new ArrayList<>();

      try {
        for (int i = 0; i < sta.length; i++) {
          sta[i] = Double.parseDouble(this.stn[i].getText());
        }
      } catch (final NumberFormatException nfe) {
        JOptionPane.showMessageDialog(boatCalc.this.f_edit,
            "Bad number format in Stations entries.", "Warning!", JOptionPane.ERROR_MESSAGE);
        return;
      }

      Point[] p;
      int iLine;
      double x, y, z;

      for (int j = 1; j < this.tp.getTabCount(); j++) {

        final edLinePanel pL = (edLinePanel) this.tp.getComponentAt(j);

        try {

          p = new Point[this.stn.length];
          for (iLine = 0; iLine < p.length; iLine++) {
            p[iLine] = new Point();
          }

          for (int i = 0; i < this.stn.length; i++) {

            x = Double.parseDouble(pL.x[i].getText());
            y = Double.parseDouble(pL.y[i].getText());
            z = Double.parseDouble(pL.z[i].getText());
            p[i] = new Point(x, y, z);
            p[i].valid = pL.v[i].isSelected();
          }
          final rawLine rL = new rawLine();
          rL.ln = new Line(p);
          rL.lnName = this.tp.getTitleAt(j);
          o.add(rL);
        } catch (final NumberFormatException nfe) {
          JOptionPane.showMessageDialog(boatCalc.this.f_edit,
              "Bad number format in Line entries" + j + ".", "Warning!", JOptionPane.ERROR_MESSAGE);
          return;
        }

      } // end j

      boatCalc.this.hull.Offsets = o;
      boatCalc.this.hull.Stations = sta;
      boatCalc.this.hull.setLines();
      boatCalc.this.hull.bChanged = true;
      boatCalc.this.hull.valid = (o.size() > 0);
      boatCalc.this.hull.designer = this.NA;
      boatCalc.this.hull.boatname = this.DName;
      this.bChanged = false;
      boatCalc.this.setCtrls();
      return;

    }// end saveEdit

  }// end pnlDataEntry
  class pnlRudder extends JPanel {
    class editFoil extends JPanel
        implements DocumentListener, ItemListener, FocusListener, ActionListener {
      /**
       *
       */
      private static final long serialVersionUID = 1L;
      boolean bChanged;
      JCheckBox cbFoil;
      Dimension d;
      JTextField[][] ff;
      rscFoil fo;

      public editFoil(final rscFoil f) {
        super(new GridLayout(0, 8));
        this.d = new Dimension(600, 150);
        final Font efFont = new Font("Serif", Font.BOLD, 14);
        this.fo = f;

        this.ff = new JTextField[2][4];
        for (int i = 0; i < 4; i++) {
          this.ff[0][i] = new JTextField(Double.toString(f.getParamX(i)), 6);
          this.ff[0][i].getDocument().addDocumentListener(this);
          this.ff[0][i].addFocusListener(this);
          this.ff[1][i] = new JTextField(Double.toString(f.getParamY(i)), 6);
          this.ff[1][i].getDocument().addDocumentListener(this);
          this.ff[1][i].addFocusListener(this);
        }


        JLabel lbl;
        JPanel pC;

        // row 1, labels
        this.cbFoil = new JCheckBox("Use");
        this.cbFoil.setHorizontalAlignment(SwingConstants.LEFT);
        this.cbFoil.setSelected(this.fo.use);
        this.cbFoil.addActionListener(this);
        this.add(this.cbFoil);
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        lbl = new JLabel("Left", SwingConstants.RIGHT);
        lbl.setFont(efFont);
        this.add(lbl);
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        lbl = new JLabel("Right", SwingConstants.RIGHT);
        lbl.setFont(efFont);
        this.add(lbl);
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

        // row 2, top
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        lbl = new JLabel("Top:", SwingConstants.CENTER);
        lbl.setFont(efFont);
        this.add(lbl);

        pC = new JPanel();
        pC.add(new JLabel("X:", SwingConstants.RIGHT));
        pC.add(this.ff[0][rscFoil.TL]);
        this.add(pC);

        pC = new JPanel();
        pC.add(new JLabel("Z:", SwingConstants.RIGHT));
        pC.add(this.ff[1][rscFoil.TL]);
        this.add(pC);

        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        pC = new JPanel();
        pC.add(new JLabel("X:", SwingConstants.RIGHT));
        pC.add(this.ff[0][rscFoil.TR]);
        this.add(pC);

        pC = new JPanel();
        pC.add(new JLabel("Z:", SwingConstants.RIGHT));
        pC.add(this.ff[1][rscFoil.TR]);
        this.add(pC);
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

        // row 3, bottom
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        lbl = new JLabel("Bottom:", SwingConstants.CENTER);
        lbl.setFont(efFont);
        this.add(lbl);
        pC = new JPanel();
        pC.add(new JLabel("X:", SwingConstants.RIGHT));
        pC.add(this.ff[0][rscFoil.BL]);
        this.add(pC);

        pC = new JPanel();
        pC.add(new JLabel("Z:", SwingConstants.RIGHT));
        pC.add(this.ff[1][rscFoil.BL]);
        this.add(pC);
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        pC = new JPanel();
        pC.add(new JLabel("X:", SwingConstants.RIGHT));
        pC.add(this.ff[0][rscFoil.BR]);
        this.add(pC);

        pC = new JPanel();
        pC.add(new JLabel("Z:", SwingConstants.RIGHT));
        pC.add(this.ff[1][rscFoil.BR]);
        this.add(pC);
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

      }

      @Override
      public void actionPerformed(final ActionEvent e) {
        this.fo.use = this.cbFoil.isSelected();
        pnlRudder.this.rdrChange = true;
        this.bChanged = true;
        pnlRudder.this.pDraw.repaint();
        pnlRudder.this.pSpec.repaint();
        pnlRudder.this.pRpt.setTable();
      }

      @Override
      public void changedUpdate(final DocumentEvent e) {
        pnlRudder.this.rdrChange = true;
        this.bChanged = true;
      }

      @Override
      public void focusGained(final FocusEvent e) {
        final JTextField t = (JTextField) e.getComponent();
        t.select(0, 100);
      }

      @Override
      public void focusLost(final FocusEvent e) {
        double v, w;
        try {
          for (int i = 0; i < 4; i++) {
            v = Double.parseDouble(this.ff[0][i].getText());
            w = Double.parseDouble(this.ff[1][i].getText());
            this.fo.setParamXY(i, v, w);
          }
        } catch (final NumberFormatException nfe) {
          JOptionPane.showMessageDialog(boatCalc.this.f_edit, "Bad number format in data entry.",
              "Warning!", JOptionPane.ERROR_MESSAGE);
          return;
        }

        pnlRudder.this.rdrChange = true;
        this.bChanged = true;
        pnlRudder.this.pDraw.repaint();
        pnlRudder.this.pSpec.repaint();
        pnlRudder.this.pRpt.setTable();

      }

      @Override
      public Dimension getPreferredSize() {
        return this.d;
      }

      @Override
      public void insertUpdate(final DocumentEvent e) {
        pnlRudder.this.rdrChange = true;
        this.bChanged = true;
      }

      @Override
      public void itemStateChanged(final ItemEvent e) {
        pnlRudder.this.rdrChange = true;
        this.bChanged = true;
      }

      @Override
      public void removeUpdate(final DocumentEvent e) {
        pnlRudder.this.rdrChange = true;
        this.bChanged = true;
      }

    }// end editFoil
    class rdrArea extends JPanel {
      /**
       *
       */
      private static final long serialVersionUID = 1L;
      Dimension d;
      JButton jbApply, jbClose;
      JLabel jlRArea, jlRCoA;
      JLabel jlSArea, jlSCoA;
      JLabel jlTArea, jlTCoA;

      public rdrArea(final int x, final int y) {
        this.d = new Dimension(x, y);
        this.setLayout(new GridLayout(0, 2));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(new bcLabel("Immersed Areas", SwingConstants.LEFT));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

        this.add(new bcLabel("Rudder - Area: ", SwingConstants.RIGHT));
        this.add(this.jlRArea = new JLabel("0.0", SwingConstants.LEFT));

        this.add(new bcLabel("CoA: ", SwingConstants.RIGHT));
        this.add(this.jlRCoA = new JLabel("0.0,0.0", SwingConstants.LEFT));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(new bcLabel("Skeg - Area: ", SwingConstants.RIGHT));
        this.add(this.jlSArea = new JLabel("0.0", SwingConstants.LEFT));
        this.add(new bcLabel("CoA: ", SwingConstants.RIGHT));
        this.add(this.jlSCoA = new JLabel("0.0,0.0", SwingConstants.LEFT));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(new bcLabel("Total - Area: ", SwingConstants.RIGHT));
        this.add(this.jlTArea = new JLabel("0.0", SwingConstants.LEFT));
        this.add(new bcLabel("CoA: ", SwingConstants.RIGHT));
        this.add(this.jlTCoA = new JLabel("0.0,0.0", SwingConstants.LEFT));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

        this.jbApply = new JButton("Apply");
        this.jbClose = new JButton("Close");

        this.jbApply.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(final ActionEvent e) {
            pnlRudder.this.saveRudder();
            pnlRudder.this.rdrChange = false;
          }
        });

        this.jbClose.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(final ActionEvent e) {
            if (pnlRudder.this.rdrChange) {
              final int n = JOptionPane.showConfirmDialog(boatCalc.this.f_edit,
                  "Data has changed. Do you wish to apply changes?", "Data Edit",
                  JOptionPane.YES_NO_OPTION);
              if (n == JOptionPane.YES_OPTION) {
                pnlRudder.this.saveRudder();
                pnlRudder.this.rdrChange = false;
              }
            }
            boatCalc.this.f_rudder.setVisible(false);
          }
        });

        this.add(this.jbApply);
        this.add(this.jbClose);

      }

      @Override
      public Dimension getPreferredSize() {
        return this.d;
      }

      protected void setTable() {
        String s;
        double wa = 0;
        double wx = 0;
        double wy = 0;
        if (pnlRudder.this.pRdr.rudder.use) {
          this.jlRArea.setText(boatCalc.this.bcf.DF1d
              .format(boatCalc.this.hull.units.coefArea() * pnlRudder.this.pRdr.rudder.getWetArea())
              + boatCalc.this.hull.units.lblArea());
          s = boatCalc.this.bcf.DF1d.format(pnlRudder.this.pRdr.rudder.getWetX()) + ", "
              + boatCalc.this.bcf.DF1d.format(pnlRudder.this.pRdr.rudder.getWetY());
          this.jlRCoA.setText(s);
          wa = wa + pnlRudder.this.pRdr.rudder.getWetArea();
          wx = wx
              + (pnlRudder.this.pRdr.rudder.getWetArea() * pnlRudder.this.pRdr.rudder.getWetX());
          wy = wy
              + (pnlRudder.this.pRdr.rudder.getWetArea() * pnlRudder.this.pRdr.rudder.getWetY());
        } else {
          this.jlRArea.setText("0.00");
          this.jlRCoA.setText("-.--, -.--");
        }

        if (pnlRudder.this.pRdr.skeg.use) {
          this.jlSArea.setText(boatCalc.this.bcf.DF1d
              .format(boatCalc.this.hull.units.coefArea() * pnlRudder.this.pRdr.skeg.getWetArea())
              + boatCalc.this.hull.units.lblArea());
          s = boatCalc.this.bcf.DF1d.format(pnlRudder.this.pRdr.skeg.getWetX()) + ", "
              + boatCalc.this.bcf.DF1d.format(pnlRudder.this.pRdr.skeg.getWetY());
          this.jlSCoA.setText(s);
          wa = wa + pnlRudder.this.pRdr.skeg.getWetArea();
          wx = wx + (pnlRudder.this.pRdr.skeg.getWetArea() * pnlRudder.this.pRdr.skeg.getWetX());
          wy = wy + (pnlRudder.this.pRdr.skeg.getWetArea() * pnlRudder.this.pRdr.skeg.getWetY());
        } else {
          this.jlSArea.setText("0.00");
          this.jlSCoA.setText("-.--, -.--");
        }

        if (wa > 0) {
          this.jlTArea.setText(boatCalc.this.bcf.DF1d.format(wa));
          this.jlTArea
              .setText(boatCalc.this.bcf.DF1d.format(boatCalc.this.hull.units.coefArea() * wa)
                  + boatCalc.this.hull.units.lblArea());
          s = boatCalc.this.bcf.DF1d.format(wx / wa) + ", "
              + boatCalc.this.bcf.DF1d.format(wy / wa);
          this.jlTCoA.setText(s);
        } else {
          this.jlTArea.setText("0.00");
          this.jlTCoA.setText("-.--, -.--");
        }
      }

    }// end rdrArea
    class rdrData extends JPanel implements ActionListener {
      /**
       *
       */
      private static final long serialVersionUID = 1L;
      JButton btnInc, btnDec;
      JComboBox<?> cbxInc;
      Dimension d;
      editFoil pRudder;
      editFoil pSkeg;
      JRadioButton rbBLX, rbBLZ, rbBRX, rbBRZ;
      JRadioButton rbMoveX, rbMoveZ;
      JRadioButton rbScale;
      JRadioButton rbTLX, rbTLZ, rbTRX, rbTRZ;
      JTabbedPane tp = new JTabbedPane();

      public rdrData(final int x, final int y) {

        JPanel pCB;

        this.d = new Dimension(x, y);
        this.setLayout(new BorderLayout());
        this.pRudder = new editFoil(pnlRudder.this.pRdr.rudder);
        this.tp.addTab("Rudder", this.pRudder);
        this.pSkeg = new editFoil(pnlRudder.this.pRdr.skeg);
        this.tp.addTab("Skeg", this.pSkeg);
        this.add(this.tp, BorderLayout.CENTER);

        final JPanel pInc = new JPanel();
        pInc.setPreferredSize(new Dimension(x - 5, (3 * y) / 10));
        pInc.setLayout(new GridLayout(0, 8));
        final ButtonGroup bgInc = new ButtonGroup();

        this.btnInc = new JButton("Increase");
        this.btnInc.addActionListener(this);
        pInc.add(this.btnInc);

        pInc.add(new JLabel("Top/Left ", SwingConstants.RIGHT));
        pCB = new JPanel();
        this.rbTLX = new JRadioButton("X");
        bgInc.add(this.rbTLX);
        pCB.add(this.rbTLX);
        this.rbTLZ = new JRadioButton("Z");
        bgInc.add(this.rbTLZ);
        pCB.add(this.rbTLZ);
        pInc.add(pCB);

        pInc.add(new JLabel("Top/Right ", SwingConstants.RIGHT));
        pCB = new JPanel();
        this.rbTRX = new JRadioButton("X");
        bgInc.add(this.rbTRX);
        pCB.add(this.rbTRX);
        this.rbTRZ = new JRadioButton("Z");
        bgInc.add(this.rbTRZ);
        pCB.add(this.rbTRZ);
        pInc.add(pCB);

        pInc.add(new JLabel("Move ", SwingConstants.RIGHT));
        pCB = new JPanel();
        this.rbMoveX = new JRadioButton("X");
        bgInc.add(this.rbMoveX);
        pCB.add(this.rbMoveX);
        this.rbMoveZ = new JRadioButton("Z");
        bgInc.add(this.rbMoveZ);
        pCB.add(this.rbMoveZ);
        pInc.add(pCB);

        pInc.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));


        this.btnDec = new JButton("Decrease");
        this.btnDec.addActionListener(this);
        pInc.add(this.btnDec);

        pInc.add(new JLabel("Bottom/Left ", SwingConstants.RIGHT));

        pCB = new JPanel();
        this.rbBLX = new JRadioButton("X");
        bgInc.add(this.rbBLX);
        pCB.add(this.rbBLX);
        this.rbBLZ = new JRadioButton("Z");
        bgInc.add(this.rbBLZ);
        pCB.add(this.rbBLZ);
        pInc.add(pCB);

        pInc.add(new JLabel("Bottom/Right ", SwingConstants.RIGHT));
        pCB = new JPanel();
        this.rbBRX = new JRadioButton("X");
        bgInc.add(this.rbBRX);
        pCB.add(this.rbBRX);
        this.rbBRZ = new JRadioButton("Z");
        bgInc.add(this.rbBRZ);
        pCB.add(this.rbBRZ);
        pInc.add(pCB);

        pInc.add(new JLabel("Scale ", SwingConstants.RIGHT));
        pCB = new JPanel();
        this.rbScale = new JRadioButton("%");
        bgInc.add(this.rbScale);
        pCB.add(this.rbScale);
        pCB.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        pInc.add(pCB);

        pCB = new JPanel();
        pCB.setLayout(new GridLayout(0, 2));
        pCB.add(new JLabel("Step: ", SwingConstants.RIGHT));


        final String[] incs =
            {"0.01", "0.02", "0.05", "0.1", "0.2", "0.5", "1", "2", "5", "10", "20", "50", "100"};
        this.cbxInc = new JComboBox<Object>(incs);
        this.cbxInc.setEditable(true);
        this.cbxInc.setSelectedIndex(6);
        pCB.add(this.cbxInc);

        // tfInc = new JTextField("1.0",8);
        // pCB.add(tfInc);
        pInc.add(pCB);

        this.add(pInc, BorderLayout.PAGE_END);

      } // end constructor

      @Override
      public void actionPerformed(final ActionEvent e) {
        if ((e.getSource() == this.btnInc) || (e.getSource() == this.btnDec)) {

          rscFoil f = pnlRudder.this.pRdr.rudder;
          editFoil eF = this.pRudder;
          if (this.tp.getSelectedIndex() == 1) {
            f = pnlRudder.this.pRdr.skeg;
            eF = this.pSkeg;
          }
          double d = 0;
          double v;
          double sgn;

          try {
            d = Double.parseDouble((String) this.cbxInc.getSelectedItem());
          } catch (final NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Unable to interperet step as number.", "Warning!",
                JOptionPane.ERROR_MESSAGE);
            return;
          }

          if (e.getSource() == this.btnInc) {
            sgn = 1.0;
          } else {
            sgn = -1.0;
          }

          if (this.rbTLX.isSelected() || this.rbMoveX.isSelected()) {
            try {
              v = Double.parseDouble(eF.ff[0][rscFoil.TL].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eF.ff[0][rscFoil.TL].setText(boatCalc.this.bcf.DF2d.format(v));
            f.setParamX(rscFoil.TL, v);
          }

          if (this.rbTLZ.isSelected() || this.rbMoveZ.isSelected()) {
            try {
              v = Double.parseDouble(eF.ff[1][rscFoil.TL].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eF.ff[1][rscFoil.TL].setText(boatCalc.this.bcf.DF2d.format(v));
            f.setParamY(rscFoil.TL, v);
          }


          if (this.rbTRX.isSelected() || this.rbMoveX.isSelected()) {
            try {
              v = Double.parseDouble(eF.ff[0][rscFoil.TR].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eF.ff[0][rscFoil.TR].setText(boatCalc.this.bcf.DF2d.format(v));
            f.setParamX(rscFoil.TR, v);
          }

          if (this.rbTRZ.isSelected() || this.rbMoveZ.isSelected()) {
            try {
              v = Double.parseDouble(eF.ff[1][rscFoil.TR].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eF.ff[1][rscFoil.TR].setText(boatCalc.this.bcf.DF2d.format(v));
            f.setParamY(rscFoil.TR, v);
          }


          if (this.rbBRX.isSelected() || this.rbMoveX.isSelected()) {
            try {
              v = Double.parseDouble(eF.ff[0][rscFoil.BR].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eF.ff[0][rscFoil.BR].setText(boatCalc.this.bcf.DF2d.format(v));
            f.setParamX(rscFoil.BR, v);
          }

          if (this.rbBRZ.isSelected() || this.rbMoveZ.isSelected()) {
            try {
              v = Double.parseDouble(eF.ff[1][rscFoil.BR].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eF.ff[1][rscFoil.BR].setText(boatCalc.this.bcf.DF2d.format(v));
            f.setParamY(rscFoil.BR, v);
          }

          if (this.rbBLX.isSelected() || this.rbMoveX.isSelected()) {
            try {
              v = Double.parseDouble(eF.ff[0][rscFoil.BL].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eF.ff[0][rscFoil.BL].setText(boatCalc.this.bcf.DF2d.format(v));
            f.setParamX(rscFoil.BL, v);
          }

          if (this.rbBLZ.isSelected() || this.rbMoveZ.isSelected()) {
            try {
              v = Double.parseDouble(eF.ff[1][rscFoil.BL].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eF.ff[1][rscFoil.BL].setText(boatCalc.this.bcf.DF2d.format(v));
            f.setParamY(rscFoil.BL, v);
          }

          if (this.rbScale.isSelected()) {
            final double mx = 0.25 * (f.getParamX(rscFoil.TL) + f.getParamX(rscFoil.TR)
                + f.getParamX(rscFoil.BR) + f.getParamX(rscFoil.BL));
            final double my = 0.25 * (f.getParamY(rscFoil.TL) + f.getParamY(rscFoil.TR)
                + f.getParamY(rscFoil.BR) + f.getParamY(rscFoil.BL));
            for (int i = 0; i < 4; i++) {
              v = mx + (0.01 * (100 + (sgn * d)) * (f.getParamX(i) - mx));
              eF.ff[0][i].setText(boatCalc.this.bcf.DF2d.format(v));
              f.setParamX(i, v);
              v = my + (0.01 * (100 + (sgn * d)) * (f.getParamY(i) - my));
              eF.ff[1][i].setText(boatCalc.this.bcf.DF2d.format(v));
              f.setParamY(i, v);
            }
          }

        }
        pnlRudder.this.pDraw.repaint();
        pnlRudder.this.pSpec.repaint();
        pnlRudder.this.pRpt.setTable();
      }

      @Override
      public Dimension getPreferredSize() {
        return this.d;
      }
    }// end rdrData
    class rdrDraw extends JPanel {
      /**
       *
       */
      private static final long serialVersionUID = 1L;
      Dimension d;

      public rdrDraw(final int x, final int y) {
        this.d = new Dimension(x, y);
      }

      @Override
      public Dimension getPreferredSize() {
        return this.d;
      }

      @Override
      protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final double mx = this.getWidth();
        final double my = this.getHeight();
        final int ix = (int) mx;
        final int iy = (int) my;
        final int xb = 50;
        final int yb = (int) my / 2;
        int iu, iv, iw, iz;

        g.clearRect(0, 0, ix, iy);
        g.drawString("Rudder", 10, 20);
        g.setColor(Color.red);
        g.drawLine(60, 12, 85, 12);
        g.setColor(Color.blue);
        g.drawLine(60, 17, 85, 17);
        g.setColor(Color.black);
        g.drawString("Skeg", 10, 40);
        g.setColor(Color.orange);
        g.drawLine(60, 32, 85, 32);
        g.setColor(Color.green);
        g.drawLine(60, 37, 85, 37);
        g.setColor(Color.black);

        if (!boatCalc.this.hull.valid) {
          return;
        }

        final double rx = (mx - 100.0) / (boatCalc.this.hull.gx_max - boatCalc.this.hull.gx_min);
        final double ry = (my - 75.0) / (boatCalc.this.hull.gy_max - boatCalc.this.hull.gy_min);
        final double r = Math.min(rx, ry);

        // draw waterline
        iu = xb + (int) (r * (boatCalc.this.hull.gx_min - boatCalc.this.hull.gx_min));
        iv = yb - (int) (r * (0.0 - boatCalc.this.hull.gz_min));
        iw = xb + (int) (r * (boatCalc.this.hull.gx_max - boatCalc.this.hull.gx_min));
        iz = yb - (int) (r * (0.0 - boatCalc.this.hull.gz_min));
        g.setColor(Color.blue);
        g.drawLine(iu, iv, iw, iz);

        // draw hull profile
        g.setColor(Color.black);
        double z1Lo = boatCalc.this.hull.gz_max;
        double z1Hi = boatCalc.this.hull.gz_min;
        double x1 = 0;
        for (double pct = 0.0; pct <= 1.0025; pct = pct + 0.01) {
          final double x = boatCalc.this.hull.gx_min
              + (pct * (boatCalc.this.hull.gx_max - boatCalc.this.hull.gx_min));
          final SortedSet<?> ss = boatCalc.this.hull.getStnSet(x, 0.0);
          final Iterator<?> si = ss.iterator();
          double zLo = boatCalc.this.hull.gz_max;
          double zHi = boatCalc.this.hull.gz_min;
          boolean bOk = false;
          while (si.hasNext()) {
            final Point p = (Point) si.next();
            zLo = Math.min(zLo, p.z);
            zHi = Math.max(zHi, p.z);
            bOk = true;
          }
          if (bOk && (pct > 0.0)) {
            iu = xb + (int) (r * (x1 - boatCalc.this.hull.gx_min));
            iv = yb - (int) (r * (z1Lo - boatCalc.this.hull.gz_min));
            iw = xb + (int) (r * (x - boatCalc.this.hull.gx_min));
            iz = yb - (int) (r * (zLo - boatCalc.this.hull.gz_min));
            g.drawLine(iu, iv, iw, iz);
            iv = yb - (int) (r * (z1Hi - boatCalc.this.hull.gz_min));
            iz = yb - (int) (r * (zHi - boatCalc.this.hull.gz_min));
            g.drawLine(iu, iv, iw, iz);
          }

          x1 = x;
          z1Lo = zLo;
          z1Hi = zHi;

        }

        // draw stems
        g.setColor(Color.lightGray);
        for (int iSL = 0; iSL <= 1; iSL++) {
          if (boatCalc.this.hull.bStems[iSL] && boatCalc.this.hull.sLines[iSL].valid) {
            double x = boatCalc.this.hull.sLines[iSL].hPoints[0].getX();
            double y = boatCalc.this.hull.sLines[iSL].hPoints[0].getZ();
            iu = xb + (int) (r * (x - boatCalc.this.hull.gx_min));
            iv = yb - (int) (r * (y - boatCalc.this.hull.gz_min));
            for (int j = 1; j < boatCalc.this.hull.sLines[iSL].hPoints.length; j++) {
              x = boatCalc.this.hull.sLines[iSL].hPoints[j].getX();
              y = boatCalc.this.hull.sLines[iSL].hPoints[j].getZ();
              iw = xb + (int) (r * (x - boatCalc.this.hull.gx_min));
              iz = yb - (int) (r * (y - boatCalc.this.hull.gz_min));
              g.drawLine(iu, iv, iw, iz);
              iu = iw;
              iv = iz;
            }
          }
        }

        // draw rudder
        if (pnlRudder.this.pRdr.rudder.use) {
          g.setColor(Color.lightGray);
          double x, y;
          x = pnlRudder.this.pRdr.rudder.getParamX(rscFoil.TL);
          y = pnlRudder.this.pRdr.rudder.getParamY(rscFoil.TL) + boatCalc.this.hull.base;
          iu = xb + (int) (r * (x - boatCalc.this.hull.gx_min));
          iv = yb - (int) (r * (y - boatCalc.this.hull.gz_min));
          x = pnlRudder.this.pRdr.rudder.getParamX(rscFoil.TR);
          y = pnlRudder.this.pRdr.rudder.getParamY(rscFoil.TR) + boatCalc.this.hull.base;
          iw = xb + (int) (r * (x - boatCalc.this.hull.gx_min));
          iz = yb - (int) (r * (y - boatCalc.this.hull.gz_min));
          g.drawLine(iu, iv, iw, iz);

          x = pnlRudder.this.pRdr.rudder.getParamX(rscFoil.BR);
          y = pnlRudder.this.pRdr.rudder.getParamY(rscFoil.BR) + boatCalc.this.hull.base;
          iu = xb + (int) (r * (x - boatCalc.this.hull.gx_min));
          iv = yb - (int) (r * (y - boatCalc.this.hull.gz_min));
          g.drawLine(iw, iz, iu, iv);

          x = pnlRudder.this.pRdr.rudder.getParamX(rscFoil.BL);
          y = pnlRudder.this.pRdr.rudder.getParamY(rscFoil.BL) + boatCalc.this.hull.base;
          iw = xb + (int) (r * (x - boatCalc.this.hull.gx_min));
          iz = yb - (int) (r * (y - boatCalc.this.hull.gz_min));
          g.drawLine(iu, iv, iw, iz);

          x = pnlRudder.this.pRdr.rudder.getParamX(rscFoil.TL);
          y = pnlRudder.this.pRdr.rudder.getParamY(rscFoil.TL) + boatCalc.this.hull.base;
          iu = xb + (int) (r * (x - boatCalc.this.hull.gx_min));
          iv = yb - (int) (r * (y - boatCalc.this.hull.gz_min));
          g.drawLine(iw, iz, iu, iv);

          // draw wet rudder
          if (pnlRudder.this.pRdr.rudder.getWetArea() > 0) {
            g.setColor(Color.blue);
            final SortedSet<?> wp = pnlRudder.this.pRdr.rudder.getWetPts();
            final Iterator<?> pi = wp.iterator();
            if (pi.hasNext()) {
              final Point p0 = (Point) pi.next();
              Point p1 = new Point(p0);
              while (pi.hasNext()) {
                final Point p2 = (Point) pi.next();
                iu = xb + (int) (r * (p1.x - boatCalc.this.hull.gx_min));
                iv = yb - (int) (r * (p1.z - boatCalc.this.hull.gz_min));
                iw = xb + (int) (r * (p2.x - boatCalc.this.hull.gx_min));
                iz = yb - (int) (r * (p2.z - boatCalc.this.hull.gz_min));
                g.drawLine(iu, iv, iw, iz);
                p1 = p2;
              }
              iu = xb + (int) (r * (p1.x - boatCalc.this.hull.gx_min));
              iv = yb - (int) (r * (p1.z - boatCalc.this.hull.gz_min));
              iw = xb + (int) (r * (p0.x - boatCalc.this.hull.gx_min));
              iz = yb - (int) (r * (p0.z - boatCalc.this.hull.gz_min));
              g.drawLine(iu, iv, iw, iz);
            }
            final int cx =
                xb + (int) (r * (pnlRudder.this.pRdr.rudder.getWetX() - boatCalc.this.hull.gx_min));
            final int cy =
                yb - (int) (r * (pnlRudder.this.pRdr.rudder.getWetY() - boatCalc.this.hull.gz_min));
            g.drawArc(cx - 5, cy - 5, 10, 10, 0, 360);
            g.drawLine(cx + 5, cy, cx - 5, cy);
            g.drawLine(cx, cy + 5, cx, cy - 5);
          }
        } // end if use rudder

        // draw skeg
        if (pnlRudder.this.pRdr.skeg.use) {
          g.setColor(Color.orange);
          double x, y;
          x = pnlRudder.this.pRdr.skeg.getParamX(rscFoil.TL);
          y = pnlRudder.this.pRdr.skeg.getParamY(rscFoil.TL) + boatCalc.this.hull.base;
          iu = xb + (int) (r * (x - boatCalc.this.hull.gx_min));
          iv = yb - (int) (r * (y - boatCalc.this.hull.gz_min));
          x = pnlRudder.this.pRdr.skeg.getParamX(rscFoil.TR);
          y = pnlRudder.this.pRdr.skeg.getParamY(rscFoil.TR) + boatCalc.this.hull.base;
          iw = xb + (int) (r * (x - boatCalc.this.hull.gx_min));
          iz = yb - (int) (r * (y - boatCalc.this.hull.gz_min));
          g.drawLine(iu, iv, iw, iz);

          x = pnlRudder.this.pRdr.skeg.getParamX(rscFoil.BR);
          y = pnlRudder.this.pRdr.skeg.getParamY(rscFoil.BR) + boatCalc.this.hull.base;
          iu = xb + (int) (r * (x - boatCalc.this.hull.gx_min));
          iv = yb - (int) (r * (y - boatCalc.this.hull.gz_min));
          g.drawLine(iw, iz, iu, iv);

          x = pnlRudder.this.pRdr.skeg.getParamX(rscFoil.BL);
          y = pnlRudder.this.pRdr.skeg.getParamY(rscFoil.BL) + boatCalc.this.hull.base;
          iw = xb + (int) (r * (x - boatCalc.this.hull.gx_min));
          iz = yb - (int) (r * (y - boatCalc.this.hull.gz_min));
          g.drawLine(iu, iv, iw, iz);

          x = pnlRudder.this.pRdr.skeg.getParamX(rscFoil.TL);
          y = pnlRudder.this.pRdr.skeg.getParamY(rscFoil.TL) + boatCalc.this.hull.base;
          iu = xb + (int) (r * (x - boatCalc.this.hull.gx_min));
          iv = yb - (int) (r * (y - boatCalc.this.hull.gz_min));
          g.drawLine(iw, iz, iu, iv);

          // draw wet skeg
          if (pnlRudder.this.pRdr.skeg.getWetArea() > 0) {
            g.setColor(Color.green);
            final SortedSet<?> wp = pnlRudder.this.pRdr.skeg.getWetPts();
            final Iterator<?> pi = wp.iterator();
            if (pi.hasNext()) {
              final Point p0 = (Point) pi.next();
              Point p1 = new Point(p0);
              while (pi.hasNext()) {
                final Point p2 = (Point) pi.next();
                iu = xb + (int) (r * (p1.x - boatCalc.this.hull.gx_min));
                iv = yb - (int) (r * (p1.z - boatCalc.this.hull.gz_min));
                iw = xb + (int) (r * (p2.x - boatCalc.this.hull.gx_min));
                iz = yb - (int) (r * (p2.z - boatCalc.this.hull.gz_min));
                g.drawLine(iu, iv, iw, iz);
                p1 = p2;
              }
              iu = xb + (int) (r * (p1.x - boatCalc.this.hull.gx_min));
              iv = yb - (int) (r * (p1.z - boatCalc.this.hull.gz_min));
              iw = xb + (int) (r * (p0.x - boatCalc.this.hull.gx_min));
              iz = yb - (int) (r * (p0.z - boatCalc.this.hull.gz_min));
              g.drawLine(iu, iv, iw, iz);
            }
            final int cx =
                xb + (int) (r * (pnlRudder.this.pRdr.skeg.getWetX() - boatCalc.this.hull.gx_min));
            final int cy =
                yb - (int) (r * (pnlRudder.this.pRdr.skeg.getWetY() - boatCalc.this.hull.gz_min));
            g.drawArc(cx - 5, cy - 5, 10, 10, 0, 360);
            g.drawLine(cx + 5, cy, cx - 5, cy);
            g.drawLine(cx, cy + 5, cx, cy - 5);
          }
        } // end use skeg

        g.setColor(Color.black);

        double tA = 0;
        double tX = 0;
        double tY = 0;
        if (pnlRudder.this.pRdr.rudder.use) {
          tA = tA + pnlRudder.this.pRdr.rudder.getWetArea();
          tX = tX
              + (pnlRudder.this.pRdr.rudder.getWetArea() * pnlRudder.this.pRdr.rudder.getWetX());
          tY = tY
              + (pnlRudder.this.pRdr.rudder.getWetArea() * pnlRudder.this.pRdr.rudder.getWetY());
        }
        if (pnlRudder.this.pRdr.skeg.use) {
          tA = tA + pnlRudder.this.pRdr.skeg.getWetArea();
          tX = tX + (pnlRudder.this.pRdr.skeg.getWetArea() * pnlRudder.this.pRdr.skeg.getWetX());
          tY = tY + (pnlRudder.this.pRdr.skeg.getWetArea() * pnlRudder.this.pRdr.skeg.getWetY());
        }
        if (tA > 0) {
          tX = tX / tA;
          tY = tY / tA;
          final int cx = xb + (int) (r * (tX - boatCalc.this.hull.gx_min));
          final int cy = yb - (int) (r * (tY - boatCalc.this.hull.gz_min));
          g.drawLine(cx + 4, cy + 4, cx - 4, cy + 4);
          g.drawLine(cx + 4, cy - 4, cx - 4, cy - 4);
          g.drawLine(cx - 4, cy - 4, cx - 4, cy + 4);
          g.drawLine(cx + 4, cy - 4, cx + 4, cy + 4);
          g.drawLine(cx + 6, cy, cx - 6, cy);
          g.drawLine(cx, cy + 6, cx, cy - 6);
        }


      } // end paintComponent

    }// end rdrDraw
    class rdrSpec extends JPanel {
      /**
       *
       */
      private static final long serialVersionUID = 1L;
      Dimension d;

      public rdrSpec(final int x, final int y) {
        this.d = new Dimension(x, y);
      }

      @Override
      public Dimension getPreferredSize() {
        return this.d;
      }

      @Override
      protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        int iL = 20;
        final int iC1 = 20;
        final int iC2 = 80;
        final int iC3 = 250;
        final int iC4 = 325;
        int kL = 0;
        if (pnlRudder.this.pRdr.rudder.use) {
          int jL = 0;
          g.drawString("Rudder", iC1, iL);
          jL = 20;
          g.drawString("Area: ", iC1, iL + jL);
          g.drawString(boatCalc.this.bcf.DF1d.format(pnlRudder.this.pRdr.rudder.getArea()), iC2,
              iL + jL);
          jL = jL + 20;
          g.drawString("CoA: ", iC1, iL + jL);
          g.drawString(
              boatCalc.this.bcf.DF2d.format(pnlRudder.this.pRdr.rudder.getAreaX()) + ", "
                  + boatCalc.this.bcf.DF2d.format(pnlRudder.this.pRdr.rudder.getAreaY()),
              iC2, iL + jL);
          jL = jL + 20;
          g.drawString("Points: ", iC1, iL + jL);
          for (int i = 0; i < 4; i++) {
            g.drawString(
                boatCalc.this.bcf.DF2d.format(pnlRudder.this.pRdr.rudder.getParamX(i)) + ", "
                    + boatCalc.this.bcf.DF2d.format(pnlRudder.this.pRdr.rudder.getParamY(i)),
                iC2, iL + jL);
            jL = jL + 20;
          }
          g.drawString("ref: baseline", iC2 + 90, (iL + jL) - 20);
          kL = jL;
          jL = 0;
          g.drawString("Immersed Portion", iC3, iL);
          jL = 20;
          g.drawString("Area: ", iC3, iL + jL);
          g.drawString(boatCalc.this.bcf.DF1d.format(pnlRudder.this.pRdr.rudder.getWetArea()), iC4,
              iL + jL);
          jL = jL + 20;
          g.drawString("CoA: ", iC3, iL + jL);
          g.drawString(
              boatCalc.this.bcf.DF2d.format(pnlRudder.this.pRdr.rudder.getWetX()) + ", "
                  + boatCalc.this.bcf.DF2d.format(pnlRudder.this.pRdr.rudder.getWetY()),
              iC4, iL + jL);
          jL = jL + 20;
          g.drawString("Points: ", iC3, iL + jL);

          final SortedSet<?> wp = pnlRudder.this.pRdr.rudder.getWetPts();
          final Iterator<?> pi = wp.iterator();
          while (pi.hasNext()) {
            final Point p = (Point) pi.next();
            g.drawString(
                boatCalc.this.bcf.DF2d.format(p.x) + ", " + boatCalc.this.bcf.DF2d.format(p.z), iC4,
                iL + jL);
            jL = jL + 20;
          }
          g.drawString("ref: lwl", iC4 + 90, (iL + jL) - 20);
          kL = Math.max(kL, jL);
        }
        iL = iL + kL;
        if (pnlRudder.this.pRdr.skeg.use) {
          int jL = 0;
          g.drawString("Skeg", iC1, iL);
          jL = 20;
          g.drawString("Area: ", iC1, iL + jL);
          g.drawString(boatCalc.this.bcf.DF1d.format(pnlRudder.this.pRdr.skeg.getArea()), iC2,
              iL + jL);
          jL = jL + 20;
          g.drawString("CoA: ", iC1, iL + jL);
          g.drawString(
              boatCalc.this.bcf.DF2d.format(pnlRudder.this.pRdr.skeg.getAreaX()) + ", "
                  + boatCalc.this.bcf.DF2d.format(pnlRudder.this.pRdr.skeg.getAreaY()),
              iC2, iL + jL);
          jL = jL + 20;
          g.drawString("Points: ", iC1, iL + jL);
          for (int i = 0; i < 4; i++) {
            g.drawString(
                boatCalc.this.bcf.DF2d.format(pnlRudder.this.pRdr.skeg.getParamX(i)) + ", "
                    + boatCalc.this.bcf.DF2d.format(pnlRudder.this.pRdr.skeg.getParamY(i)),
                iC2, iL + jL);
            jL = jL + 20;
          }
          g.drawString("ref: baseline", iC2 + 90, (iL + jL) - 20);
          kL = jL;
          jL = 0;
          g.drawString("Immersed Portion", iC3, iL);
          jL = 20;
          g.drawString("Area: ", iC3, iL + jL);
          g.drawString(boatCalc.this.bcf.DF1d.format(pnlRudder.this.pRdr.skeg.getWetArea()), iC4,
              iL + jL);
          jL = jL + 20;
          g.drawString("CoA: ", iC3, iL + jL);
          g.drawString(
              boatCalc.this.bcf.DF2d.format(pnlRudder.this.pRdr.skeg.getWetX()) + ", "
                  + boatCalc.this.bcf.DF2d.format(pnlRudder.this.pRdr.skeg.getWetY()),
              iC4, iL + jL);
          jL = jL + 20;
          g.drawString("Points: ", iC3, iL + jL);

          final SortedSet<?> wp = pnlRudder.this.pRdr.skeg.getWetPts();
          final Iterator<?> pi = wp.iterator();
          while (pi.hasNext()) {
            final Point p = (Point) pi.next();
            g.drawString(
                boatCalc.this.bcf.DF2d.format(p.x) + ", " + boatCalc.this.bcf.DF2d.format(p.z), iC4,
                iL + jL);
            jL = jL + 20;
          }
          g.drawString("ref: lwl", iC4 + 90, (iL + jL) - 20);
          // kL = Math.max(kL,jL);
        }

      }// end paintComponent
    }// ends rdrSpec
    public class rdrTabOrder extends FocusTraversalPolicy {
      rdrData r;

      public rdrTabOrder(final pnlRudder p) {
        this.r = p.pData;
      }

      @Override
      public Component getComponentAfter(final Container focusCycleRoot,
          final Component aComponent) {
        return aComponent;
      }

      @Override
      public Component getComponentBefore(final Container focusCycleRoot,
          final Component aComponent) {
        return aComponent;
      }

      @Override
      public Component getDefaultComponent(final Container focusCycleRoot) {
        return this.r;
      }

      @Override
      public Component getFirstComponent(final Container focusCycleRoot) {
        return this.r;
      }

      @Override
      public Component getLastComponent(final Container focusCycleRoot) {
        return this.r;
      }
    }// end rdrTabOrder

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Border bRdr;
    rdrData pData;
    JTabbedPane pDisp;

    rdrDraw pDraw;

    Rudder pRdr;

    rdrArea pRpt;

    rdrSpec pSpec;

    boolean rdrChange;

    rdrTabOrder to;

    public pnlRudder() {

      if (boatCalc.this.hull.rudder.valid) {
        this.pRdr = (Rudder) boatCalc.this.hull.rudder.clone();
      } else {
        this.pRdr = new Rudder();
      }
      this.pRdr.setBase(boatCalc.this.hull.base);
      this.rdrChange = false;

      this.bRdr = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
      this.setLayout(new BorderLayout());

      this.pDisp = new JTabbedPane();

      this.pDraw = new rdrDraw(750, 325);
      this.pDraw.setBackground(Color.white);
      this.pDraw.setBorder(this.bRdr);
      this.pDisp.add(this.pDraw, "Drawing");

      this.pSpec = new rdrSpec(750, 325);
      this.pSpec.setBackground(Color.white);
      this.pSpec.setBorder(this.bRdr);
      this.pDisp.add(this.pSpec, "Dimensions");

      this.add(this.pDisp, BorderLayout.CENTER);

      this.pRpt = new rdrArea(180, 200);
      this.add(this.pRpt, BorderLayout.LINE_END);


      this.pData = new rdrData(750, 185);
      this.pData.setBorder(this.bRdr);
      this.add(this.pData, BorderLayout.PAGE_END);

      // to = new rdrTabOrder(this);
      // f_rudder.setFocusTraversalPolicy(to);

      boatCalc.this.f_rudder.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(final WindowEvent e) {
          if (pnlRudder.this.rdrChange) {
            final int n = JOptionPane.showConfirmDialog(boatCalc.this.f_rudder,
                "Data has changed. Do you wish to apply changes?", "Sailplan Design",
                JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
              pnlRudder.this.saveRudder();
            }
          }
          boatCalc.this.f_rudder.setVisible(false);
        }
      });

      this.pDraw.repaint();
      this.pSpec.repaint();
      this.pRpt.setTable();

    }// end constructor

    public void saveRudder() {
      boatCalc.this.hull.rudder = (Rudder) this.pRdr.clone();
      boatCalc.this.hull.bChanged = true;
    }

  }// end pnlRudder
  class pnlSailplan extends JPanel {
    class editSail extends JPanel
        implements DocumentListener, ItemListener, FocusListener, ActionListener {
      /**
       *
       */
      private static final long serialVersionUID = 1L;
      boolean bChanged;
      JCheckBox cbSail, cbGaff, cbRoach;
      Dimension d;
      JTextField[][] sf;
      Sail so;

      public editSail(final Sail s) {
        super(new GridLayout(0, 11));
        this.d = new Dimension(600, 200);
        final Font spFont = new Font("Serif", Font.BOLD, 14);
        this.so = s;

        this.sf = new JTextField[2][5];
        for (int i = 0; i < 5; i++) {
          this.sf[0][i] = new JTextField(Double.toString(s.getParamX(i)));
          this.sf[0][i].getDocument().addDocumentListener(this);
          this.sf[0][i].addFocusListener(this);
          this.sf[1][i] = new JTextField(Double.toString(s.getParamY(i)));
          this.sf[1][i].getDocument().addDocumentListener(this);
          this.sf[1][i].addFocusListener(this);
        }
        JLabel lbl;
        // row 1, labels
        this.cbSail = new JCheckBox("Use");
        this.cbSail.setHorizontalAlignment(SwingConstants.LEFT);
        this.cbSail.setSelected(this.so.use);
        this.cbSail.addActionListener(this);
        this.add(this.cbSail);
        lbl = new JLabel("Tack", SwingConstants.RIGHT);
        lbl.setFont(spFont);
        this.add(lbl);
        this.add(new Box.Filler(new Dimension(2, 2), new Dimension(2, 2), new Dimension(2, 2)));
        lbl = new JLabel("Luff", SwingConstants.RIGHT);
        lbl.setFont(spFont);
        this.add(lbl);
        this.add(new Box.Filler(new Dimension(2, 2), new Dimension(2, 2), new Dimension(2, 2)));
        lbl = new JLabel("Foot", SwingConstants.RIGHT);
        lbl.setFont(spFont);
        this.add(lbl);
        this.add(new Box.Filler(new Dimension(2, 2), new Dimension(2, 2), new Dimension(2, 2)));
        this.add(new Box.Filler(new Dimension(2, 2), new Dimension(2, 2), new Dimension(2, 2)));
        this.cbGaff = new JCheckBox("Gaff");
        this.cbGaff.setHorizontalAlignment(SwingConstants.CENTER);
        this.cbGaff.setSelected(this.so.useGaff);
        this.cbGaff.addActionListener(this);
        this.add(this.cbGaff);
        this.add(new Box.Filler(new Dimension(2, 2), new Dimension(2, 2), new Dimension(2, 2)));
        this.cbRoach = new JCheckBox("Roach");
        this.cbRoach.setHorizontalAlignment(SwingConstants.CENTER);
        this.cbRoach.setSelected(this.so.useRoach);
        this.cbRoach.addActionListener(this);
        this.add(this.cbRoach);
        // add(new Box.Filler(new Dimension(2,2),new Dimension(2,2),new Dimension(2,2)));

        // row 2, data
        this.add(new Box.Filler(new Dimension(2, 2), new Dimension(2, 2), new Dimension(2, 2)));
        lbl = new JLabel("X: ", SwingConstants.RIGHT);
        lbl.setFont(spFont);
        this.add(lbl);
        this.add(this.sf[0][0]);
        lbl = new JLabel("Len: ", SwingConstants.RIGHT);
        lbl.setFont(spFont);
        this.add(lbl);
        this.add(this.sf[0][1]);
        lbl = new JLabel("Len: ", SwingConstants.RIGHT);
        lbl.setFont(spFont);
        this.add(lbl);
        this.add(this.sf[0][2]);
        lbl = new JLabel("Len: ", SwingConstants.RIGHT);
        lbl.setFont(spFont);
        this.add(lbl);
        this.add(this.sf[0][3]);
        lbl = new JLabel("Max %: ", SwingConstants.RIGHT);
        lbl.setFont(spFont);
        this.add(lbl);
        this.add(this.sf[0][4]);
        // add(new Box.Filler(new Dimension(2,2),new Dimension(2,2),new Dimension(2,2)));

        // row 3, data
        this.add(new Box.Filler(new Dimension(2, 2), new Dimension(2, 2), new Dimension(2, 2)));
        lbl = new JLabel("Z: ", SwingConstants.RIGHT);
        lbl.setFont(spFont);
        this.add(lbl);
        this.add(this.sf[1][0]);
        lbl = new JLabel("Rake: ", SwingConstants.RIGHT);
        lbl.setFont(spFont);
        this.add(lbl);
        this.add(this.sf[1][1]);
        lbl = new JLabel("Ang: ", SwingConstants.RIGHT);
        lbl.setFont(spFont);
        this.add(lbl);
        this.add(this.sf[1][2]);
        lbl = new JLabel("Ang: ", SwingConstants.RIGHT);
        lbl.setFont(spFont);
        this.add(lbl);
        this.add(this.sf[1][3]);
        lbl = new JLabel("Hgt %: ", SwingConstants.RIGHT);
        lbl.setFont(spFont);
        this.add(lbl);
        this.add(this.sf[1][4]);
        /*
         * //row 4, spaces add(new Box.Filler(new Dimension(2,2),new Dimension(2,2),new
         * Dimension(2,2))); // add(new Box.Filler(new Dimension(2,2),new Dimension(2,2),new
         * Dimension(2,2)));
         */
      }

      @Override
      public void actionPerformed(final ActionEvent e) {
        this.so.setUse(this.cbSail.isSelected());
        this.so.setUseGaff(this.cbGaff.isSelected());
        this.so.setUseRoach(this.cbRoach.isSelected());
        pnlSailplan.this.rigChange = true;
        pnlSailplan.this.pDraw.repaint();
        pnlSailplan.this.pSpec.repaint();
        pnlSailplan.this.pRpt.setTable();
      }

      @Override
      public void changedUpdate(final DocumentEvent e) {
        pnlSailplan.this.rigChange = true;
        this.bChanged = true;
      }

      @Override
      public void focusGained(final FocusEvent e) {
        final JTextField t = (JTextField) e.getComponent();
        t.select(0, 100);
      }

      @Override
      public void focusLost(final FocusEvent e) {
        double v, w;
        try {
          for (int i = 0; i < 5; i++) {
            v = Double.parseDouble(this.sf[0][i].getText());
            w = Double.parseDouble(this.sf[1][i].getText());
            this.so.setParam(i, v, w);
          }
        } catch (final NumberFormatException nfe) {
          JOptionPane.showMessageDialog(boatCalc.this.f_edit, "Bad number format in data entry.",
              "Warning!", JOptionPane.ERROR_MESSAGE);
          return;
        }
        pnlSailplan.this.pDraw.repaint();
        pnlSailplan.this.pSpec.repaint();
        pnlSailplan.this.pRpt.setTable();
      }

      @Override
      public Dimension getPreferredSize() {
        return this.d;
      }

      @Override
      public void insertUpdate(final DocumentEvent e) {
        pnlSailplan.this.rigChange = true;
        this.bChanged = true;
      }

      @Override
      public void itemStateChanged(final ItemEvent e) {
        pnlSailplan.this.rigChange = true;
        this.bChanged = true;
      }

      @Override
      public void removeUpdate(final DocumentEvent e) {
        pnlSailplan.this.rigChange = true;
        this.bChanged = true;
      }

    }// end editSail
    public class pspTabOrder extends FocusTraversalPolicy {
      sailData p;

      public pspTabOrder(final pnlSailplan s) {
        this.p = s.pData;
      }

      @Override
      public Component getComponentAfter(final Container focusCycleRoot,
          final Component aComponent) {
        editSail s;
        if (aComponent.equals(this.p.rbL)) {
          return this.p.rbR;
        } else if (aComponent.equals(this.p.btnInc)) {
          return this.p.btnDec;
        } else if (aComponent.equals(this.p.btnDec)) {
          return this.p.rbTackX;
        } else if (aComponent.equals(this.p.rbTackX)) {
          return this.p.rbTackZ;
        } else if (aComponent.equals(this.p.rbTackZ)) {
          return this.p.rbLuffLen;
        } else if (aComponent.equals(this.p.rbLuffLen)) {
          return this.p.rbLuffAng;
        } else if (aComponent.equals(this.p.rbLuffAng)) {
          return this.p.rbBoomLen;
        } else if (aComponent.equals(this.p.rbBoomLen)) {
          return this.p.rbBoomAng;
        } else if (aComponent.equals(this.p.rbBoomAng)) {
          return this.p.rbGaffLen;
        } else if (aComponent.equals(this.p.rbGaffLen)) {
          return this.p.rbGaffAng;
        } else if (aComponent.equals(this.p.rbGaffAng)) {
          return this.p.rbRoachMax;
        } else if (aComponent.equals(this.p.rbRoachMax)) {
          return this.p.rbRoachPct;
        } else if (aComponent.equals(this.p.rbRoachPct)) {
          return this.p.rbAll;
        } else if (aComponent.equals(this.p.rbAll)) {
          return this.p.cbxInc;
        } else if (aComponent.getParent().equals(this.p.cbxInc)) {
          return this.p.rbL;
        } else if (this.p.tp.getSelectedIndex() == 0) {
          s = this.p.pMain;
        } else if (this.p.tp.getSelectedIndex() == 1) {
          s = this.p.pJib;
        } else if (this.p.tp.getSelectedIndex() == 2) {
          s = this.p.pMiz;
        } else {
          return this.p.rbL;
        }

        if (aComponent.equals(this.p.rbR)) {
          return s.cbSail;
        } else if (aComponent.equals(s.cbSail)) {
          return s.cbGaff;
        } else if (aComponent.equals(s.cbGaff)) {
          return s.cbRoach;
        } else if (aComponent.equals(s.cbRoach)) {
          return s.sf[0][0];
        }

        for (int i = 0; i < 5; i++) {
          if (aComponent.equals(s.sf[0][i])) {
            return s.sf[1][i];
          }
          if (aComponent.equals(s.sf[1][i])) {
            final int j = i + 1;
            if (j > 4) {
              return this.p.btnInc;
            } else {
              return s.sf[0][j];
            }
          }
        }
        return this.p.rbL;
      }

      @Override
      public Component getComponentBefore(final Container focusCycleRoot,
          final Component aComponent) {
        editSail s;
        if (aComponent.equals(this.p.rbR)) {
          return this.p.rbL;
        } else if (aComponent.equals(this.p.rbL)) {
          return this.p.cbxInc;
        } else if (aComponent.getParent().equals(this.p.cbxInc)) {
          return this.p.rbAll;
        } else if (aComponent.equals(this.p.rbAll)) {
          return this.p.rbRoachPct;
        } else if (aComponent.equals(this.p.rbRoachPct)) {
          return this.p.rbRoachMax;
        } else if (aComponent.equals(this.p.rbRoachMax)) {
          return this.p.rbGaffAng;
        } else if (aComponent.equals(this.p.rbGaffAng)) {
          return this.p.rbGaffLen;
        } else if (aComponent.equals(this.p.rbGaffLen)) {
          return this.p.rbBoomAng;
        } else if (aComponent.equals(this.p.rbBoomAng)) {
          return this.p.rbBoomLen;
        } else if (aComponent.equals(this.p.rbBoomLen)) {
          return this.p.rbLuffAng;
        } else if (aComponent.equals(this.p.rbLuffAng)) {
          return this.p.rbLuffLen;
        } else if (aComponent.equals(this.p.rbLuffLen)) {
          return this.p.rbTackZ;
        } else if (aComponent.equals(this.p.rbTackZ)) {
          return this.p.rbTackX;
        } else if (aComponent.equals(this.p.rbTackX)) {
          return this.p.btnDec;
        } else if (aComponent.equals(this.p.btnDec)) {
          return this.p.btnInc;
        } else if (this.p.tp.getSelectedIndex() == 0) {
          s = this.p.pMain;
        } else if (this.p.tp.getSelectedIndex() == 1) {
          s = this.p.pJib;
        } else if (this.p.tp.getSelectedIndex() == 2) {
          s = this.p.pMiz;
        } else {
          return this.p.rbL;
        }

        if (aComponent.equals(s.cbSail)) {
          return this.p.rbR;
        } else if (aComponent.equals(s.cbGaff)) {
          return s.cbSail;
        } else if (aComponent.equals(s.cbRoach)) {
          return s.cbGaff;
        } else if (aComponent.equals(s.sf[0][0])) {
          return s.cbRoach;
        } else if (aComponent.equals(this.p.btnInc)) {
          return s.sf[1][4];
        }

        for (int i = 0; i < 5; i++) {
          if (aComponent.equals(s.sf[1][4 - i])) {
            return s.sf[0][4 - i];
          }
          if (aComponent.equals(s.sf[0][4 - i])) {
            final int j = i + 1;
            if (j > 4) {
              return s.cbRoach;
            } else {
              return s.sf[1][4 - j];
            }
          }
        }

        return this.p.rbL;
      }

      @Override
      public Component getDefaultComponent(final Container focusCycleRoot) {
        if (this.p.tp.getSelectedIndex() == 0) {
          return this.p.pMain.sf[0][0];
        } else if (this.p.tp.getSelectedIndex() == 1) {
          return this.p.pJib.sf[0][0];
        } else if (this.p.tp.getSelectedIndex() == 2) {
          return this.p.pMiz.sf[0][0];
        }
        return this.p.rbL;
      }

      @Override
      public Component getFirstComponent(final Container focusCycleRoot) {
        if (this.p.tp.getSelectedIndex() == 0) {
          return this.p.pMain.sf[0][0];
        } else if (this.p.tp.getSelectedIndex() == 1) {
          return this.p.pJib.sf[0][0];
        } else if (this.p.tp.getSelectedIndex() == 2) {
          return this.p.pMiz.sf[0][0];
        }
        return this.p.rbL;
      }

      @Override
      public Component getLastComponent(final Container focusCycleRoot) {
        if (this.p.tp.getSelectedIndex() == 0) {
          return this.p.pMain.sf[1][4];
        } else if (this.p.tp.getSelectedIndex() == 1) {
          return this.p.pJib.sf[1][4];
        } else if (this.p.tp.getSelectedIndex() == 2) {
          return this.p.pMiz.sf[1][4];
        }
        return this.p.rbL;
      }
    }// end pspTabOrder
    class sailArea extends JPanel {
      /**
       *
       */
      private static final long serialVersionUID = 1L;
      Dimension d;
      JButton jbApply, jbClose;
      // bcLabel bclMArea, bclJArea, bclZArea, bclTArea;
      // bcLabel bclMCoA, bclJCoA, bclZCoA, bclTCoA;
      JLabel jlMArea, jlJArea, jlZArea, jlTArea;
      JLabel jlMCoA, jlJCoA, jlZCoA, jlTCoA;

      public sailArea(final int x, final int y) {
        this.d = new Dimension(x, y);
        this.setLayout(new GridLayout(0, 2));
        this.add(new bcLabel("Sail Area", SwingConstants.LEFT));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

        this.add(new bcLabel("Main - Area: ", SwingConstants.RIGHT));
        this.add(this.jlMArea = new JLabel("0.0", SwingConstants.LEFT));

        this.add(new bcLabel("CoA: ", SwingConstants.RIGHT));
        this.add(this.jlMCoA = new JLabel("0.0,0.0", SwingConstants.LEFT));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

        this.add(new bcLabel("Jib - Area: ", SwingConstants.RIGHT));
        this.add(this.jlJArea = new JLabel("0.0", SwingConstants.LEFT));

        this.add(new bcLabel("CoA: ", SwingConstants.RIGHT));
        this.add(this.jlJCoA = new JLabel("0.0,0.0", SwingConstants.LEFT));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

        this.add(new bcLabel("Mizzen - Area: ", SwingConstants.RIGHT));
        this.add(this.jlZArea = new JLabel("0.0", SwingConstants.LEFT));

        this.add(new bcLabel("CoA: ", SwingConstants.RIGHT));
        this.add(this.jlZCoA = new JLabel("0.0,0.0", SwingConstants.LEFT));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

        this.add(new bcLabel("Total - Area: ", SwingConstants.RIGHT));
        this.add(this.jlTArea = new JLabel("0.0", SwingConstants.LEFT));

        this.add(new bcLabel("CoA: ", SwingConstants.RIGHT));
        this.add(this.jlTCoA = new JLabel("0.0,0.0", SwingConstants.LEFT));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        this.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

        this.jbApply = new JButton("Apply");
        this.jbClose = new JButton("Close");

        this.jbApply.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(final ActionEvent e) {
            pnlSailplan.this.saveRig();
            pnlSailplan.this.rigChange = false;
          }
        });

        this.jbClose.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(final ActionEvent e) {
            if (pnlSailplan.this.rigChange) {
              final int n = JOptionPane.showConfirmDialog(boatCalc.this.f_edit,
                  "Data has changed. Do you wish to apply changes?", "Data Edit",
                  JOptionPane.YES_NO_OPTION);
              if (n == JOptionPane.YES_OPTION) {
                pnlSailplan.this.saveRig();
                pnlSailplan.this.rigChange = false;
              }
            }
            boatCalc.this.f_sailplan.setVisible(false);
          }
        });

        this.add(this.jbApply);
        this.add(this.jbClose);

      }

      @Override
      public Dimension getPreferredSize() {
        return this.d;
      }

      protected void setTable() {
        String s;
        double wa = 0;
        double wx = 0;
        double wy = 0;

        if (pnlSailplan.this.pRig.main.use) {
          this.jlMArea.setText(boatCalc.this.bcf.DF1d
              .format(boatCalc.this.hull.units.coefArea() * pnlSailplan.this.pRig.main.getArea())
              + boatCalc.this.hull.units.lblArea());
          s = boatCalc.this.bcf.DF1d.format(pnlSailplan.this.pRig.main.getAreaX()) + ", "
              + boatCalc.this.bcf.DF1d.format(pnlSailplan.this.pRig.main.getAreaY());
          this.jlMCoA.setText(s);
          wa = wa + pnlSailplan.this.pRig.main.getArea();
          wx = wx + (pnlSailplan.this.pRig.main.getArea() * pnlSailplan.this.pRig.main.getAreaX());
          wy = wy + (pnlSailplan.this.pRig.main.getArea() * pnlSailplan.this.pRig.main.getAreaY());
        } else {
          this.jlMArea.setText("0.00");
          this.jlMCoA.setText("-.--, -.--");
        }
        if (pnlSailplan.this.pRig.jib.use) {
          this.jlJArea.setText(boatCalc.this.bcf.DF1d
              .format(boatCalc.this.hull.units.coefArea() * pnlSailplan.this.pRig.jib.getArea())
              + boatCalc.this.hull.units.lblArea());
          s = boatCalc.this.bcf.DF1d.format(pnlSailplan.this.pRig.jib.getAreaX()) + ", "
              + boatCalc.this.bcf.DF1d.format(pnlSailplan.this.pRig.jib.getAreaY());
          this.jlJCoA.setText(s);
          wa = wa + pnlSailplan.this.pRig.jib.getArea();
          wx = wx + (pnlSailplan.this.pRig.jib.getArea() * pnlSailplan.this.pRig.jib.getAreaX());
          wy = wy + (pnlSailplan.this.pRig.jib.getArea() * pnlSailplan.this.pRig.jib.getAreaY());
        } else {
          this.jlJArea.setText("0.00");
          this.jlJCoA.setText("-.--, -.--");
        }
        if (pnlSailplan.this.pRig.mizzen.use) {
          this.jlZArea.setText(boatCalc.this.bcf.DF1d
              .format(boatCalc.this.hull.units.coefArea() * pnlSailplan.this.pRig.mizzen.getArea())
              + boatCalc.this.hull.units.lblArea());
          s = boatCalc.this.bcf.DF1d.format(pnlSailplan.this.pRig.mizzen.getAreaX()) + ", "
              + boatCalc.this.bcf.DF1d.format(pnlSailplan.this.pRig.mizzen.getAreaY());
          this.jlZCoA.setText(s);
          wa = wa + pnlSailplan.this.pRig.mizzen.getArea();
          wx = wx
              + (pnlSailplan.this.pRig.mizzen.getArea() * pnlSailplan.this.pRig.mizzen.getAreaX());
          wy = wy
              + (pnlSailplan.this.pRig.mizzen.getArea() * pnlSailplan.this.pRig.mizzen.getAreaY());
        } else {
          this.jlZArea.setText("0.00");
          this.jlZCoA.setText("-.--, -.--");
        }

        if (wa > 0) {
          this.jlTArea.setText(boatCalc.this.bcf.DF1d.format(wa));
          this.jlTArea
              .setText(boatCalc.this.bcf.DF1d.format(boatCalc.this.hull.units.coefArea() * wa)
                  + boatCalc.this.hull.units.lblArea());
          s = boatCalc.this.bcf.DF1d.format(wx / wa) + ", "
              + boatCalc.this.bcf.DF1d.format(wy / wa);
          this.jlTCoA.setText(s);
        } else {
          this.jlTArea.setText("0.00");
          this.jlTCoA.setText("-.--, -.--");
        }
      }

    }// end sailArea
    class sailData extends JPanel implements ActionListener {
      /**
       *
       */
      private static final long serialVersionUID = 1L;
      JButton btnInc, btnDec;
      JComboBox<?> cbxInc;
      Dimension d;
      editSail pJib;
      editSail pMain;
      editSail pMiz;
      JRadioButton rbAll;
      JRadioButton rbBoomLen, rbBoomAng;
      JRadioButton rbGaffLen, rbGaffAng;
      JRadioButton rbL, rbR;
      JRadioButton rbLuffLen, rbLuffAng;
      JRadioButton rbRoachMax, rbRoachPct;
      JRadioButton rbTackX, rbTackZ;
      JTabbedPane tp = new JTabbedPane();

      public sailData(final int x, final int y) {
        JPanel pRb;
        JLabel lRb;

        this.d = new Dimension(x, y);
        this.setLayout(new BorderLayout());
        final JPanel pDir = new JPanel();
        pDir.setLayout(new GridLayout(0, 1));
        this.rbL = new JRadioButton("Left");
        this.rbL.addActionListener(this);
        this.rbL.setVerticalAlignment(SwingConstants.BOTTOM);
        this.rbR = new JRadioButton("Right");
        this.rbR.addActionListener(this);
        this.rbL.setVerticalAlignment(SwingConstants.TOP);
        final ButtonGroup bGrp = new ButtonGroup();
        bGrp.add(this.rbL);
        bGrp.add(this.rbR);
        pDir.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        pDir.add(this.rbL);
        pDir.add(this.rbR);
        pDir.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));

        if (pnlSailplan.this.pRig.dir < 0) {
          this.rbL.setSelected(true);
        }
        if (pnlSailplan.this.pRig.dir > 0) {
          this.rbR.setSelected(true);
        }

        this.add(pDir, BorderLayout.LINE_START);

        this.pMain = new editSail(pnlSailplan.this.pRig.main);
        this.tp.addTab("Main", this.pMain);
        this.pJib = new editSail(pnlSailplan.this.pRig.jib);
        this.pJib.cbGaff.setSelected(false);
        this.pJib.cbRoach.setSelected(false);
        this.tp.addTab("Jib", this.pJib);
        this.pMiz = new editSail(pnlSailplan.this.pRig.mizzen);
        this.tp.addTab("Mizzen", this.pMiz);
        this.add(this.tp, BorderLayout.CENTER);

        final JPanel pInc = new JPanel();
        pInc.setPreferredSize(new Dimension(x - 5, (3 * y) / 10));
        pInc.setLayout(new GridLayout(0, 8));

        this.btnInc = new JButton("Increase");
        this.btnInc.addActionListener(this);
        pInc.add(this.btnInc);

        lRb = new JLabel("Tack:");
        lRb.setHorizontalAlignment(SwingConstants.RIGHT);
        pInc.add(lRb);
        pRb = new JPanel();
        this.rbTackX = new JRadioButton("X");
        this.rbTackZ = new JRadioButton("Z");
        pRb.add(this.rbTackX);
        pRb.add(this.rbTackZ);
        pInc.add(pRb);

        lRb = new JLabel("Luff:");
        lRb.setHorizontalAlignment(SwingConstants.RIGHT);
        pInc.add(lRb);
        pRb = new JPanel();
        this.rbLuffLen = new JRadioButton("L");
        this.rbLuffAng = new JRadioButton("A");
        pRb.add(this.rbLuffLen);
        pRb.add(this.rbLuffAng);
        pInc.add(pRb);

        lRb = new JLabel("Foot:");
        lRb.setHorizontalAlignment(SwingConstants.RIGHT);
        pInc.add(lRb);
        pRb = new JPanel();
        this.rbBoomLen = new JRadioButton("L");
        this.rbBoomAng = new JRadioButton("A");
        pRb.add(this.rbBoomLen);
        pRb.add(this.rbBoomAng);
        pInc.add(pRb);
        pInc.add(new JLabel("Step", SwingConstants.CENTER));

        this.btnDec = new JButton("Decrease");
        this.btnDec.addActionListener(this);
        pInc.add(this.btnDec);

        lRb = new JLabel("Gaff:");
        lRb.setHorizontalAlignment(SwingConstants.RIGHT);
        pInc.add(lRb);
        pRb = new JPanel();
        this.rbGaffLen = new JRadioButton("L");
        this.rbGaffAng = new JRadioButton("A");
        pRb.add(this.rbGaffLen);
        pRb.add(this.rbGaffAng);
        pInc.add(pRb);

        lRb = new JLabel("Roach:");
        lRb.setHorizontalAlignment(SwingConstants.RIGHT);
        pInc.add(lRb);
        pRb = new JPanel();
        this.rbRoachMax = new JRadioButton("M");
        this.rbRoachPct = new JRadioButton("%");
        pRb.add(this.rbRoachMax);
        pRb.add(this.rbRoachPct);
        pInc.add(pRb);

        lRb = new JLabel("Scale:");
        lRb.setHorizontalAlignment(SwingConstants.RIGHT);
        pInc.add(lRb);
        pRb = new JPanel();
        this.rbAll = new JRadioButton("%");
        pRb.add(this.rbAll);
        pRb.add(
            new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)));
        pInc.add(pRb);

        final ButtonGroup bgInc = new ButtonGroup();
        bgInc.add(this.rbTackX);
        bgInc.add(this.rbTackZ);
        bgInc.add(this.rbLuffLen);
        bgInc.add(this.rbLuffAng);
        bgInc.add(this.rbBoomLen);
        bgInc.add(this.rbBoomAng);
        bgInc.add(this.rbGaffLen);
        bgInc.add(this.rbGaffAng);
        bgInc.add(this.rbRoachMax);
        bgInc.add(this.rbRoachPct);
        bgInc.add(this.rbAll);

        final String[] incs =
            {"0.01", "0.02", "0.05", "0.1", "0.2", "0.5", "1", "2", "5", "10", "20", "50", "100"};
        this.cbxInc = new JComboBox<Object>(incs);
        this.cbxInc.setEditable(true);
        this.cbxInc.setSelectedIndex(6);
        pInc.add(this.cbxInc);

        this.add(pInc, BorderLayout.PAGE_END);

      } // end constructor

      @Override
      public void actionPerformed(final ActionEvent e) {

        if ((e.getSource() == this.rbL) || (e.getSource() == this.rbR)) {
          if (this.rbL.isSelected()) {
            pnlSailplan.this.pRig.dir = -1;
            pnlSailplan.this.pRig.main.setDir(-1);
            pnlSailplan.this.pRig.jib.setDir(-1);
            pnlSailplan.this.pRig.mizzen.setDir(-1);
          }
          if (this.rbR.isSelected()) {
            pnlSailplan.this.pRig.dir = +1;
            pnlSailplan.this.pRig.main.setDir(1);
            pnlSailplan.this.pRig.jib.setDir(1);
            pnlSailplan.this.pRig.mizzen.setDir(1);
          }
        }

        if ((e.getSource() == this.btnInc) || (e.getSource() == this.btnDec)) {
          double sgn, v, d;
          Sail s = pnlSailplan.this.pRig.main;
          editSail eS = this.pMain;
          if (this.tp.getSelectedIndex() == 1) {
            s = pnlSailplan.this.pRig.jib;
            eS = this.pJib;
          } else if (this.tp.getSelectedIndex() == 2) {
            s = pnlSailplan.this.pRig.mizzen;
            eS = this.pMiz;
          }
          if (e.getSource() == this.btnInc) {
            sgn = 1.0;
          } else {
            sgn = -1.0;
          }

          try {
            d = Double.parseDouble((String) this.cbxInc.getSelectedItem());
          } catch (final NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Unable to interpret step as number.", "Warning!",
                JOptionPane.ERROR_MESSAGE);
            return;
          }

          if (this.rbTackX.isSelected()) {
            try {
              v = Double.parseDouble(eS.sf[0][0].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = Math.max(v + (sgn * d), 0.0);
            eS.sf[0][0].setText(boatCalc.this.bcf.DF2d.format(v));
            s.setX(v);
          }

          if (this.rbTackZ.isSelected()) {
            try {
              v = Double.parseDouble(eS.sf[1][0].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = Math.max(v + (sgn * d), 0.0);
            eS.sf[1][0].setText(boatCalc.this.bcf.DF2d.format(v));
            s.setY(v);
          }

          if (this.rbLuffLen.isSelected()) {
            try {
              v = Double.parseDouble(eS.sf[0][1].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = Math.max(v + (sgn * d), 0.0);
            eS.sf[0][1].setText(boatCalc.this.bcf.DF2d.format(v));
            s.setLuffLen(v);
          }

          if (this.rbLuffAng.isSelected()) {
            try {
              v = Double.parseDouble(eS.sf[1][1].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eS.sf[1][1].setText(boatCalc.this.bcf.DF2d.format(v));
            s.setLuffAng(v);
          }

          if (this.rbBoomLen.isSelected()) {
            try {
              v = Double.parseDouble(eS.sf[0][2].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = Math.max(v + (sgn * d), 0.0);
            eS.sf[0][2].setText(boatCalc.this.bcf.DF2d.format(v));
            s.setBoomLen(v);
          }

          if (this.rbBoomAng.isSelected()) {
            try {
              v = Double.parseDouble(eS.sf[1][2].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eS.sf[1][2].setText(boatCalc.this.bcf.DF2d.format(v));
            s.setBoomAng(v);
          }

          if (this.rbGaffLen.isSelected()) {
            try {
              v = Double.parseDouble(eS.sf[0][3].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = Math.max(v + (sgn * d), 0.0);
            eS.sf[0][3].setText(boatCalc.this.bcf.DF2d.format(v));
            s.setGaffLen(v);
          }

          if (this.rbGaffAng.isSelected()) {
            try {
              v = Double.parseDouble(eS.sf[1][3].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = v + (sgn * d);
            eS.sf[1][3].setText(boatCalc.this.bcf.DF2d.format(v));
            s.setGaffAng(v);
          }

          if (this.rbRoachMax.isSelected()) {
            try {
              v = Double.parseDouble(eS.sf[0][4].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = Math.max(v + (sgn * d), 0.0);
            eS.sf[0][4].setText(boatCalc.this.bcf.DF2d.format(v));
            s.setRoachMax(v);
          }

          if (this.rbRoachPct.isSelected()) {
            try {
              v = Double.parseDouble(eS.sf[1][4].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = Math.max(v + (sgn * d), 0.0);
            eS.sf[1][4].setText(boatCalc.this.bcf.DF2d.format(v));
            s.setRoachPct(v);
          }

          if (this.rbAll.isSelected()) {
            try {
              v = Double.parseDouble(eS.sf[0][1].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = Math.max(v + (0.01 * sgn * d * v), 0.0);
            eS.sf[0][1].setText(boatCalc.this.bcf.DF2d.format(v));
            s.setLuffLen(v);
            try {
              v = Double.parseDouble(eS.sf[0][2].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = Math.max(v + (0.01 * sgn * d * v), 0.0);
            eS.sf[0][2].setText(boatCalc.this.bcf.DF2d.format(v));
            s.setBoomLen(v);
            try {
              v = Double.parseDouble(eS.sf[0][3].getText());
            } catch (final NumberFormatException nfe) {
              JOptionPane.showMessageDialog(null, "Unable to interpret value as number.",
                  "Warning!", JOptionPane.ERROR_MESSAGE);
              return;
            }
            v = Math.max(v + (0.01 * sgn * d * v), 0.0);
            eS.sf[0][3].setText(boatCalc.this.bcf.DF2d.format(v));
            s.setGaffLen(v);

          }
        } // end btn proc
        pnlSailplan.this.rigChange = true;
        pnlSailplan.this.pDraw.repaint();
        pnlSailplan.this.pSpec.repaint();
        pnlSailplan.this.pRpt.setTable();
      }

      @Override
      public Dimension getPreferredSize() {
        return this.d;
      }
    }// end sailData
    class sailDraw extends JPanel {
      /**
       *
       */
      private static final long serialVersionUID = 1L;
      Dimension d;

      public sailDraw(final int x, final int y) {
        this.d = new Dimension(x, y);
      }

      @Override
      public Dimension getPreferredSize() {
        return this.d;
      }

      @Override
      protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final double mx = this.getWidth();
        final double my = this.getHeight();
        final int ix = (int) mx;
        final int iy = (int) my;
        final int xb = 100;
        final int yb = (int) my - 10;

        g.clearRect(0, 0, ix, iy);

        if (!boatCalc.this.hull.valid) {
          g.drawString("Hull not defined.", 50, 50);
          return;
        }

        if ((pnlSailplan.this.pRig.getMaxX() == pnlSailplan.this.pRig.getMinX())
            || (pnlSailplan.this.pRig.getMaxY() == pnlSailplan.this.pRig.getMinY())) {
          g.drawString("Data incomplete.", 50, 50);
          return;
        }

        final double max_x = Math.max(pnlSailplan.this.pRig.getMaxX(), boatCalc.this.hull.gx_max);
        final double min_x = Math.min(pnlSailplan.this.pRig.getMinX(), boatCalc.this.hull.gx_min);
        final double max_y = Math.max(pnlSailplan.this.pRig.getMaxY(), boatCalc.this.hull.gz_max);
        final double min_z = Math.min(pnlSailplan.this.pRig.getMinY(), boatCalc.this.hull.gz_min);

        final double rx = (mx - 200.0) / (max_x - min_x);
        final double ry = (my - 25.0) / (max_y - min_z);
        final double r = Math.min(rx, ry);

        int iu, iv, iw, iz;
        int hx, hy, cx, cy;



        // draw waterline
        iu = xb + (int) (r * (boatCalc.this.hull.gx_min - min_x));
        iv = yb - (int) (r * (0.0 - min_z));
        iw = xb + (int) (r * (boatCalc.this.hull.gx_max - min_x));
        iz = yb - (int) (r * (0.0 - min_z));
        g.setColor(Color.blue);
        g.drawLine(iu, iv, iw, iz);

        // draw hull profile
        g.setColor(Color.black);
        double z1Lo = boatCalc.this.hull.gz_max;
        double z1Hi = boatCalc.this.hull.gz_min;
        double x1 = 0;
        for (double pct = 0.0; pct <= 1.0025; pct = pct + 0.01) {
          final double x = boatCalc.this.hull.gx_min
              + (pct * (boatCalc.this.hull.gx_max - boatCalc.this.hull.gx_min));
          final SortedSet<?> ss = boatCalc.this.hull.getStnSet(x, 0.0);
          final Iterator<?> si = ss.iterator();
          double zLo = boatCalc.this.hull.gz_max;
          double zHi = boatCalc.this.hull.gz_min;
          boolean bOk = false;
          while (si.hasNext()) {
            final Point p = (Point) si.next();
            zLo = Math.min(zLo, p.z);
            zHi = Math.max(zHi, p.z);
            bOk = true;
          }
          if (bOk && (pct > 0.0)) {
            iu = xb + (int) (r * (x1 - min_x));
            iv = yb - (int) (r * (z1Lo - min_z));
            iw = xb + (int) (r * (x - min_x));
            iz = yb - (int) (r * (zLo - min_z));
            g.drawLine(iu, iv, iw, iz);
            iv = yb - (int) (r * (z1Hi - min_z));
            iz = yb - (int) (r * (zHi - min_z));
            g.drawLine(iu, iv, iw, iz);
          }

          x1 = x;
          z1Lo = zLo;
          z1Hi = zHi;

        }

        // draw stems
        g.setColor(Color.lightGray);
        for (int iSL = 0; iSL <= 1; iSL++) {
          if (boatCalc.this.hull.bStems[iSL] && boatCalc.this.hull.sLines[iSL].valid) {
            double x = boatCalc.this.hull.sLines[iSL].hPoints[0].getX();
            double y = boatCalc.this.hull.sLines[iSL].hPoints[0].getZ();
            iu = xb + (int) (r * (x - min_x));
            iv = yb - (int) (r * (y - min_z));
            for (int j = 1; j < boatCalc.this.hull.sLines[iSL].hPoints.length; j++) {
              x = boatCalc.this.hull.sLines[iSL].hPoints[j].getX();
              y = boatCalc.this.hull.sLines[iSL].hPoints[j].getZ();
              iw = xb + (int) (r * (x - min_x));
              iz = yb - (int) (r * (y - min_z));
              g.drawLine(iu, iv, iw, iz);
              iu = iw;
              iv = iz;
            }
          }
        }

        // draw sail
        Sail ts;
        g.setColor(Color.black);
        final double min_z_base = min_z - boatCalc.this.hull.base;

        for (int is = 0; is < 3; is++) {
          if (is == 0) {
            ts = pnlSailplan.this.pRig.main;
          } else if (is == 1) {
            ts = pnlSailplan.this.pRig.jib;
          } else {
            ts = pnlSailplan.this.pRig.mizzen;
          }


          if (ts.use) {
            iu = xb + (int) (r * (ts.getX(Sail.TACK) - min_x));
            iv = yb - (int) (r * (ts.getY(Sail.TACK) - min_z_base));
            iw = xb + (int) (r * (ts.getX(Sail.THROAT) - min_x));
            iz = yb - (int) (r * (ts.getY(Sail.THROAT) - min_z_base));
            g.drawLine(iu, iv, iw, iz);

            if (ts.useGaff) {
              iu = xb + (int) (r * (ts.getX(Sail.PEAK) - min_x));
              iv = yb - (int) (r * (ts.getY(Sail.PEAK) - min_z_base));
              g.drawLine(iw, iz, iu, iv);
              hx = iu;
              hy = iv;
            } else {
              hx = iw;
              hy = iz;
            }

            if (ts.useRoach) {
              iu = xb + (int) (r * (ts.getX(Sail.LEECH) - min_x));
              iv = yb - (int) (r * (ts.getY(Sail.LEECH) - min_z_base));
              g.drawLine(hx, hy, iu, iv);
              cx = xb + (int) (r * (ts.getX(Sail.CLEW) - min_x));
              cy = yb - (int) (r * (ts.getY(Sail.CLEW) - min_z_base));
              g.drawLine(iu, iv, cx, cy);
              g.setColor(Color.gray);
              g.drawLine(hx, hy, cx, cy);
              g.setColor(Color.black);
            } else {
              cx = xb + (int) (r * (ts.getX(Sail.CLEW) - min_x));
              cy = yb - (int) (r * (ts.getY(Sail.CLEW) - min_z_base));
              g.drawLine(hx, hy, cx, cy);
            }

            iw = xb + (int) (r * (ts.getX(Sail.TACK) - min_x));
            iz = yb - (int) (r * (ts.getY(Sail.TACK) - min_z_base));
            g.drawLine(cx, cy, iw, iz);

            cx = xb + (int) (r * (ts.getAreaX() - min_x));
            cy = yb - (int) (r * (ts.getAreaY() - min_z_base));
            g.drawArc(cx - 5, cy - 5, 10, 10, 0, 360);
            g.drawLine(cx + 5, cy, cx - 5, cy);
            g.drawLine(cx, cy + 5, cx, cy - 5);

          }
        }

        final double a = pnlSailplan.this.pRig.getArea();
        if (a > 0) {
          cx = xb + (int) (r * (pnlSailplan.this.pRig.getAreaX() - min_x));
          cy = yb - (int) (r * (pnlSailplan.this.pRig.getAreaY() - min_z_base));

          g.drawLine(cx + 4, cy + 4, cx - 4, cy + 4);
          g.drawLine(cx + 4, cy - 4, cx - 4, cy - 4);
          g.drawLine(cx - 4, cy - 4, cx - 4, cy + 4);
          g.drawLine(cx + 4, cy - 4, cx + 4, cy + 4);
          g.drawLine(cx + 6, cy, cx - 6, cy);
          g.drawLine(cx, cy + 6, cx, cy - 6);
        }

      } // end paintComponent

    }// end sailDraw
    class sailSpec extends JPanel {
      /**
       *
       */
      private static final long serialVersionUID = 1L;
      Dimension d;

      public sailSpec(final int x, final int y) {
        this.d = new Dimension(x, y);
      }

      @Override
      public Dimension getPreferredSize() {
        return this.d;
      }

      double length(final double x1, final double y1, final double x2, final double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
      }

      @Override
      protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        Sail ts;
        int il = 25;
        final int isp = 20;
        String s;
        double d;

        int ic = 160;
        int icw = 60;
        g.drawString("Co-ord", ic, il);
        ic = ic + (2 * icw);
        g.drawString("to: Clew", ic - 15, il);
        ic = ic + icw;
        g.drawString("Roach", ic, il);
        ic = ic + icw;
        g.drawString("Peak", ic, il);
        ic = ic + icw;
        g.drawString("Head/Throat", ic - 15, il);

        il = il + isp + 5;

        for (int is = 0; is < 3; is++) {

          ic = 50;
          icw = 60;

          if (is == 0) {
            ts = pnlSailplan.this.pRig.main;
            s = "Main";
          } else if (is == 1) {
            ts = pnlSailplan.this.pRig.jib;
            s = "Jib";
          } else {
            ts = pnlSailplan.this.pRig.mizzen;
            s = "Mizzen";
          }

          if (ts.use) {

            g.drawString(s, ic, il);
            ic = 100;
            g.drawString("Tack:", ic, il);
            ic = ic + icw;
            s = boatCalc.this.bcf.DF2d.format(ts.getX(Sail.TACK)) + ", "
                + boatCalc.this.bcf.DF2d.format(ts.getY(Sail.TACK));
            g.drawString(s, ic, il);

            ic = ic + (2 * icw);
            d = this.length(ts.getX(Sail.TACK), ts.getY(Sail.TACK), ts.getX(Sail.CLEW),
                ts.getY(Sail.CLEW));
            g.drawString(boatCalc.this.bcf.DF2d.format(d), ic, il);
            ic = ic + icw;
            d = this.length(ts.getX(Sail.TACK), ts.getY(Sail.TACK), ts.getX(Sail.ROACH),
                ts.getY(Sail.ROACH));
            if (ts.useRoach) {
              g.drawString(boatCalc.this.bcf.DF2d.format(d), ic, il);
            }
            ic = ic + icw;
            d = this.length(ts.getX(Sail.TACK), ts.getY(Sail.TACK), ts.getX(Sail.PEAK),
                ts.getY(Sail.PEAK));
            if (ts.useGaff) {
              g.drawString(boatCalc.this.bcf.DF2d.format(d), ic, il);
            }
            ic = ic + icw;
            d = this.length(ts.getX(Sail.TACK), ts.getY(Sail.TACK), ts.getX(Sail.THROAT),
                ts.getY(Sail.THROAT));
            g.drawString(boatCalc.this.bcf.DF2d.format(d), ic, il);

            il = il + isp;
            ic = 100;
            if (ts.useGaff) {
              g.drawString("Throat:", ic, il);
            } else {
              g.drawString("Head:", ic, il);
            }
            ic = ic + icw;
            s = boatCalc.this.bcf.DF2d.format(ts.getX(Sail.THROAT)) + ", "
                + boatCalc.this.bcf.DF2d.format(ts.getY(Sail.THROAT));
            g.drawString(s, ic, il);

            ic = ic + (2 * icw);
            d = this.length(ts.getX(Sail.THROAT), ts.getY(Sail.THROAT), ts.getX(Sail.CLEW),
                ts.getY(Sail.CLEW));
            g.drawString(boatCalc.this.bcf.DF2d.format(d), ic, il);
            ic = ic + icw;
            d = this.length(ts.getX(Sail.THROAT), ts.getY(Sail.THROAT), ts.getX(Sail.ROACH),
                ts.getY(Sail.ROACH));
            if (ts.useRoach) {
              g.drawString(boatCalc.this.bcf.DF2d.format(d), ic, il);
            }
            ic = ic + icw;
            d = this.length(ts.getX(Sail.THROAT), ts.getY(Sail.THROAT), ts.getX(Sail.PEAK),
                ts.getY(Sail.PEAK));
            if (ts.useGaff) {
              g.drawString(boatCalc.this.bcf.DF2d.format(d), ic, il);
            }

            if (ts.useGaff) {
              il = il + isp;
              ic = 100;
              g.drawString("Peak:", ic, il);
              ic = ic + icw;
              s = boatCalc.this.bcf.DF2d.format(ts.getX(Sail.PEAK)) + ", "
                  + boatCalc.this.bcf.DF2d.format(ts.getY(Sail.PEAK));
              g.drawString(s, ic, il);

              ic = ic + (2 * icw);
              d = this.length(ts.getX(Sail.PEAK), ts.getY(Sail.PEAK), ts.getX(Sail.CLEW),
                  ts.getY(Sail.CLEW));
              if (ts.useGaff) {
                g.drawString(boatCalc.this.bcf.DF2d.format(d), ic, il);
              }
              ic = ic + icw;
              d = this.length(ts.getX(Sail.PEAK), ts.getY(Sail.PEAK), ts.getX(Sail.ROACH),
                  ts.getY(Sail.ROACH));
              if (ts.useRoach) {
                g.drawString(boatCalc.this.bcf.DF2d.format(d), ic, il);
              }
            }

            if (ts.useRoach) {
              il = il + isp;
              ic = 100;
              g.drawString("Roach:", ic, il);
              ic = ic + icw;
              s = boatCalc.this.bcf.DF2d.format(ts.getX(Sail.ROACH)) + ", "
                  + boatCalc.this.bcf.DF2d.format(ts.getY(Sail.ROACH));
              g.drawString(s, ic, il);

              ic = ic + (2 * icw);
              d = this.length(ts.getX(Sail.ROACH), ts.getY(Sail.ROACH), ts.getX(Sail.CLEW),
                  ts.getY(Sail.CLEW));
              g.drawString(boatCalc.this.bcf.DF2d.format(d), ic, il);
            }

            il = il + isp;
            ic = 100;
            g.drawString("Clew:", ic, il);
            ic = ic + icw;
            s = boatCalc.this.bcf.DF2d.format(ts.getX(Sail.CLEW)) + ", "
                + boatCalc.this.bcf.DF2d.format(ts.getY(Sail.CLEW));
            g.drawString(s, ic, il);
          } // end use
          il = il + (2 * isp);

        }

      }

    }// ends sailSpec

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Border bSail;
    sailData pData;
    JTabbedPane pDisp;

    sailDraw pDraw;

    Rig pRig;

    sailArea pRpt;

    sailSpec pSpec;

    boolean rigChange;

    pspTabOrder to;

    public pnlSailplan() {

      if (boatCalc.this.hull.rig.valid) {
        this.pRig = (Rig) boatCalc.this.hull.rig.clone();
      } else {
        this.pRig = new Rig();
      }
      this.rigChange = false;

      this.bSail = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
      this.setLayout(new BorderLayout());

      this.pDisp = new JTabbedPane();

      this.pDraw = new sailDraw(750, 325);
      this.pDraw.setBackground(Color.white);
      this.pDraw.setBorder(this.bSail);
      this.pDisp.add(this.pDraw, "Drawing");

      this.pSpec = new sailSpec(750, 325);
      this.pSpec.setBackground(Color.white);
      this.pSpec.setBorder(this.bSail);
      this.pDisp.add(this.pSpec, "Dimensions");

      this.add(this.pDisp, BorderLayout.CENTER);

      this.pRpt = new sailArea(180, 200);
      this.add(this.pRpt, BorderLayout.LINE_END);


      this.pData = new sailData(750, 185);
      // pData.setBackground(Color.white) ;
      this.pData.setBorder(this.bSail);
      this.add(this.pData, BorderLayout.PAGE_END);

      this.to = new pspTabOrder(this);
      boatCalc.this.f_sailplan.setFocusTraversalPolicy(this.to);

      boatCalc.this.f_sailplan.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(final WindowEvent e) {
          if (pnlSailplan.this.rigChange) {
            final int n = JOptionPane.showConfirmDialog(boatCalc.this.f_sailplan,
                "Data has changed. Do you wish to apply changes?", "Sailplan Design",
                JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
              pnlSailplan.this.saveRig();
            }
          }
          boatCalc.this.f_sailplan.setVisible(false);
        }
      });

      this.pDraw.repaint();
      this.pSpec.repaint();
      this.pRpt.setTable();


    }// end constructor

    public void saveRig() {
      boatCalc.this.hull.rig = (Rig) this.pRig.clone();
      boatCalc.this.hull.bChanged = true;
    }

  }// end pnlSailplan
  class pnlWgtEntry extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    edWgtPanel wp;

    public pnlWgtEntry() {
      final JPanel p = new JPanel();
      p.setBorder(BorderFactory.createEtchedBorder());
      p.setLayout(new BorderLayout());
      p.add(new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20)),
          BorderLayout.PAGE_START);

      this.wp = new edWgtPanel(boatCalc.this);
      p.add(this.wp, BorderLayout.CENTER);

      final JButton btnSave = new JButton("Apply Changes");
      btnSave.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          pnlWgtEntry.this.applyWgts();
          boatCalc.this.dispWgt.setWeights();
        }
      });

      final JButton btnClose = new JButton("Close");
      btnClose.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          if (pnlWgtEntry.this.wp.bChanged) {
            final int n = JOptionPane.showConfirmDialog(boatCalc.this.f_wgts,
                "Data has changed. Do you wish to apply changes?", "Weight Edit",
                JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
              pnlWgtEntry.this.applyWgts();
              boatCalc.this.dispWgt.setWeights();
            }
          }
          boatCalc.this.f_wgts.setVisible(false);
        }
      });
      final JPanel bp = new JPanel();
      bp.add(btnSave);
      bp.add(btnClose);
      p.add(bp, BorderLayout.PAGE_END);
      this.add(p);

      boatCalc.this.f_wgts.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(final WindowEvent e) {
          if (pnlWgtEntry.this.wp.bChanged) {
            final int n = JOptionPane.showConfirmDialog(boatCalc.this.f_edit,
                "Data has changed. Do you wish to apply changes?", "Data Edit",
                JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
              pnlWgtEntry.this.applyWgts();
              boatCalc.this.dispWgt.setWeights();
            }
          }
          boatCalc.this.f_wgts.setVisible(false);
        }
      });
    }

    public void applyWgts() {
      final String[] tl = new String[10];
      final double[] tw = new double[10];
      final double[] tx = new double[10];
      final double[] ty = new double[10];
      final double[] tz = new double[10];
      try {
        for (int i = 0; i < 10; i++) {
          tl[i] = this.wp.l[i].getText();
          tw[i] = Double.parseDouble(this.wp.w[i].getText());
          tx[i] = Double.parseDouble(this.wp.x[i].getText());
          ty[i] = Double.parseDouble(this.wp.y[i].getText());
          tz[i] = Double.parseDouble(this.wp.z[i].getText());
          boatCalc.this.hull.wgtLbl = tl;
          boatCalc.this.hull.wgtWgt = tw;
          boatCalc.this.hull.wgtX = tx;
          boatCalc.this.hull.wgtY = ty;
          boatCalc.this.hull.wgtZ = tz;
        }
      } catch (final NumberFormatException e) {
        JOptionPane.showMessageDialog(boatCalc.this.f_wgts, "Bad number format!", "Warning!",
            JOptionPane.ERROR_MESSAGE);
      }
      this.wp.bChanged = false;
    }// end applyWgts

    public void getWgts() {
      for (int i = 0; i < 10; i++) {
        this.wp.l[i].setText(boatCalc.this.hull.wgtLbl[i]);
        this.wp.w[i].setText(Double.toString(boatCalc.this.hull.wgtWgt[i]));
        this.wp.x[i].setText(Double.toString(boatCalc.this.hull.wgtX[i]));
        this.wp.y[i].setText(Double.toString(boatCalc.this.hull.wgtY[i]));
        this.wp.z[i].setText(Double.toString(boatCalc.this.hull.wgtZ[i]));
        this.wp.bChanged = false;
      }
    }// end getWgts


  }// end pnlWgtEntry

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public static void main(final String[] args) {

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (final Exception e) {
    }

    final boatCalc c = new boatCalc();
    c.setSize(770, 570);
    c.setBackground(Color.white);
    c.setVisible(true);
  } // end main

  bcFormat bcf;
  bodyPanel body;
  boolean bOpen = true;
  ctrlPanel ctrl;
  hdPanel disp;
  hdBody dispAft;
  hdCtrl dispCtrl;
  hdBody dispFore;

  JTabbedPane dispPane;
  stnPanel dispStn;
  wetPanel dispWet;
  wgtPanel dispWgt;
  wlPanel dispWL;

  JFrame f_analysis;
  JFrame f_board;

  JFrame f_edit;
  JFrame f_rudder;

  JFrame f_sailplan;

  JFrame f_wgts;

  JFileChooser fc;
  // bcFileFilter ff;

  Hull hull;


  JMenuItem m_about;

  JMenuItem m_board;


  JMenuItem m_destn;



  JMenuItem m_disp;



  JMenuItem m_edit;



  JMenuItem m_exit;

  JMenuItem m_instn;


  JMenuItem m_new;

  JMenuItem m_open;

  JMenuItem m_print;


  JMenuItem m_rudder;


  JMenuItem m_sailplan;

  JMenuItem m_save;


  JMenuBar mb;

  JMenu menu;

  planPanel plan;



  bcUnits units;


  JPanel w;


  JPanel w_analysis;


  /** Creates a new instance of boatCalc */
  public boatCalc() {
    super("boatCalc");

    try {
      SaveOutput.start("boatCalc.log");
    } catch (final Exception e) {
      e.printStackTrace();
    }

    this.bcf = new bcFormat();

    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(final WindowEvent e) {
        if (boatCalc.this.hull.bChanged) {
          final int n = JOptionPane.showConfirmDialog(boatCalc.this.f_edit, "Save Changes?",
              "Data has been changed.", JOptionPane.YES_NO_OPTION);
          if (n == JOptionPane.YES_OPTION) {

            try {
              // bcFileFilter ff = new bcFileFilter(new String[] {"xml", "hul"}, "boatCalc and Hulls
              // files");
              // fc.setFileFilter(ff);
              final int returnVal = boatCalc.this.fc.showSaveDialog(null);
              if (returnVal == JFileChooser.APPROVE_OPTION) {
                final String fn = (boatCalc.this.fc.getSelectedFile().getName()).toLowerCase();
                if (fn.indexOf(".hul") > 0) {
                  boatCalc.this.hull.saveHulls(boatCalc.this.fc.getSelectedFile());
                } else {
                  boatCalc.this.hull.saveData(boatCalc.this.fc.getSelectedFile());
                }
              }
            } catch (final NullPointerException npe) {
              System.out.println(npe);
              return;
            }

          } else if (n == JOptionPane.CANCEL_OPTION) {
            return;
          }
        }

        SaveOutput.stop();
        System.exit(0);
      }
    });

    this.mb = new JMenuBar();
    this.menu = new JMenu("File");


    this.m_new = new JMenuItem("New");
    this.m_new.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        if (boatCalc.this.hull.bChanged) {
          final int n = JOptionPane.showConfirmDialog(boatCalc.this.f_edit, "Save Changes?",
              "Data has been changed.", JOptionPane.YES_NO_OPTION);
          if (n == JOptionPane.YES_OPTION) {

            try {
              // bcFileFilter ff = new bcFileFilter(new String[] {"xml", "hul"}, "boatCalc and Hulls
              // files");
              // fc.setFileFilter(ff);
              final int returnVal = boatCalc.this.fc.showSaveDialog(null);
              if (returnVal == JFileChooser.APPROVE_OPTION) {
                final String fn = (boatCalc.this.fc.getSelectedFile().getName()).toLowerCase();
                if (fn.indexOf(".hul") > 0) {
                  boatCalc.this.hull.saveHulls(boatCalc.this.fc.getSelectedFile());
                } else {
                  boatCalc.this.hull.saveData(boatCalc.this.fc.getSelectedFile());
                }
              }
            } catch (final NullPointerException npe) {
              System.out.println(npe);
              return;
            }

          } else if (n == JOptionPane.CANCEL_OPTION) {
            return;
          }
        }
        final String s = JOptionPane.showInputDialog(boatCalc.this.f_edit, "Number of Stations:");
        int n_stn = 11;
        if ((s != null) && (s.length() > 0)) {

          try {
            n_stn = Integer.parseInt(s);
          } catch (final NumberFormatException nfe) {
            JOptionPane.showMessageDialog(boatCalc.this.f_edit, "Bad number format..", "Warning!",
                JOptionPane.ERROR_MESSAGE);
            return;
          }
        }
        boatCalc.this.hull = new Hull();
        boatCalc.this.hull.Stations = new double[n_stn];
        boatCalc.this.hull.Offsets = new ArrayList<>();
        boatCalc.this.hull.valid = false;
        boatCalc.this.hull.designer = "NA";
        boatCalc.this.hull.boatname = "new boat";
        for (int j = 0; j < n_stn; j++) {
          boatCalc.this.hull.Stations[j] = j;
        }
        boatCalc.this.hull.newWgts();
        boatCalc.this.hull.setLines();
        boatCalc.this.f_edit = new JFrame("Data Entry/Edit");
        boatCalc.this.f_edit.setSize(770, 550);
        final pnlDataEntry w_edit = new pnlDataEntry();
        boatCalc.this.f_edit.getContentPane().add(w_edit);
        boatCalc.this.f_edit.setVisible(true);
      }
    });
    this.menu.add(this.m_new);

    this.m_open = new JMenuItem("Open");
    this.m_open.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        boatCalc.this.getHull();
        if (boatCalc.this.hull.valid) {
          boatCalc.this.hull.calcDisp();
          boatCalc.this.dispWgt.setWeights();
        }
        boatCalc.this.repaint();
      }
    });
    this.menu.add(this.m_open);

    this.m_save = new JMenuItem("Save");
    this.m_save.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        boatCalc.this.saveHull();
        boatCalc.this.repaint();
      }
    });
    this.menu.add(this.m_save);

    this.m_print = new JMenuItem("Print");
    this.m_print.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        PrinterJob.getPrinterJob();
      }
    });
    this.menu.add(this.m_print);


    this.m_exit = new JMenuItem("Exit");
    this.m_exit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        if (boatCalc.this.hull.bChanged) {
          final int n = JOptionPane.showConfirmDialog(boatCalc.this.f_edit, "Save Changes?",
              "Data has been changed.", JOptionPane.YES_NO_OPTION);
          if (n == JOptionPane.YES_OPTION) {

            try {
              // bcFileFilter ff = new bcFileFilter(new String[] {"xml", "hul"}, "boatCalc and Hulls
              // files");
              // fc.setFileFilter(ff);
              final int returnVal = boatCalc.this.fc.showSaveDialog(null);
              if (returnVal == JFileChooser.APPROVE_OPTION) {
                final String fn = (boatCalc.this.fc.getSelectedFile().getName()).toLowerCase();
                if (fn.indexOf(".hul") > 0) {
                  boatCalc.this.hull.saveHulls(boatCalc.this.fc.getSelectedFile());
                } else {
                  boatCalc.this.hull.saveData(boatCalc.this.fc.getSelectedFile());
                }
              }
            } catch (final NullPointerException npe) {
              System.out.println(npe);
              return;
            }

          } else if (n == JOptionPane.CANCEL_OPTION) {
            return;
          }
        }
        SaveOutput.stop();
        System.exit(0);
      }
    });
    this.menu.add(this.m_exit);
    this.mb.add(this.menu);

    this.menu = new JMenu("Edit");
    this.m_instn = new JMenuItem("Insert station");
    this.m_instn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        boatCalc.this.insertStation();
      }
    });
    this.m_destn = new JMenuItem("Delete station");
    this.m_destn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        boatCalc.this.deleteStation();
      }
    });
    this.m_edit = new JMenuItem("Edit data");
    this.m_edit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        boatCalc.this.f_edit = new JFrame("Data Entry/Edit");
        boatCalc.this.f_edit.setSize(770, 550);
        final pnlDataEntry w_edit = new pnlDataEntry();
        boatCalc.this.f_edit.getContentPane().add(w_edit);
        boatCalc.this.f_edit.setVisible(true);
      }
    });
    this.menu.add(this.m_instn);
    this.menu.add(this.m_destn);
    this.menu.add(this.m_edit);
    this.mb.add(this.menu);

    this.menu = new JMenu("Design");
    this.m_sailplan = new JMenuItem("Sailplan");
    this.m_sailplan.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        boatCalc.this.f_sailplan = new JFrame("Sailplan");
        boatCalc.this.f_sailplan.setSize(770, 550);
        final pnlSailplan w_sailplan = new pnlSailplan();
        boatCalc.this.f_sailplan.getContentPane().add(w_sailplan);
        boatCalc.this.f_sailplan.setVisible(true);
      }
    });
    this.menu.add(this.m_sailplan);

    this.m_rudder = new JMenuItem("Rudder");
    this.m_rudder.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        boatCalc.this.f_rudder = new JFrame("Rudder");
        boatCalc.this.f_rudder.setSize(770, 550);
        final pnlRudder w_rudder = new pnlRudder();
        boatCalc.this.f_rudder.getContentPane().add(w_rudder);
        boatCalc.this.f_rudder.setVisible(true);
      }
    });
    this.menu.add(this.m_rudder);

    this.m_board = new JMenuItem("Centerboard");
    this.m_board.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        boatCalc.this.f_board = new JFrame("Centerboard");
        boatCalc.this.f_board.setSize(770, 550);
        final pnlCenterboard w_board = new pnlCenterboard();
        boatCalc.this.f_board.getContentPane().add(w_board);
        boatCalc.this.f_board.setVisible(true);
      }
    });
    this.menu.add(this.m_board);

    this.mb.add(this.menu);

    this.menu = new JMenu("Analysis");
    this.m_disp = new JMenuItem("Displacement");
    this.m_disp.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        boatCalc.this.f_analysis.setVisible(true);
        if (boatCalc.this.hull.valid) {
          boatCalc.this.hull.calcDisp();
          boatCalc.this.dispWgt.setWeights();
        }
        boatCalc.this.repaint();
      }
    });
    this.menu.add(this.m_disp);
    this.mb.add(this.menu);

    this.menu = new JMenu("About");
    this.m_about = new JMenuItem("About");
    this.m_about.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        JOptionPane.showMessageDialog(null, new pnlAbout(), "About",
            JOptionPane.INFORMATION_MESSAGE);
      }
    });
    this.menu.add(this.m_about);
    this.mb.add(this.menu);

    this.setJMenuBar(this.mb);

    this.fc = new JFileChooser(".");
    final bcFileFilter ff =
        new bcFileFilter(new String[] {"xml", "hul"}, "boatCalc and Hulls files");
    this.fc.setFileFilter(ff);

    // getHull();
    this.hull = new Hull();

    this.ctrl = new ctrlPanel(this, 400, 200);
    this.body = new bodyPanel(this, 300, 200);
    this.plan = new planPanel(this, 705, 300);
    this.w = new JPanel();
    this.w.setLayout(new FlowLayout());
    this.w.setBorder(BorderFactory.createEtchedBorder());
    this.w.add(this.ctrl);
    this.w.add(this.body);
    this.w.add(this.plan);
    this.getContentPane().add(this.w);

    this.f_analysis = new JFrame("Displacement Analysis");
    this.f_analysis.setSize(770, 550);
    this.disp = new hdPanel(this, 710, 300);
    this.w_analysis = new JPanel();
    this.w_analysis.setLayout(new FlowLayout());
    this.w_analysis.setBorder(BorderFactory.createEtchedBorder());

    this.dispWgt = new wgtPanel(this, 710, 300);
    this.dispWL = new wlPanel(this, 710, 300);
    this.dispWet = new wetPanel(this, 710, 300);
    this.dispStn = new stnPanel(this, 710, 300);

    this.dispCtrl = new hdCtrl(300, 200);
    this.dispFore = new hdBody(this, 200, 200);
    this.dispFore.setTitle("Left");
    this.dispFore.setType(true);
    this.dispAft = new hdBody(this, 200, 200);
    this.dispAft.setTitle("Right");
    this.dispAft.setType(false);
    this.w_analysis.add(this.dispFore);
    this.w_analysis.add(this.dispAft);
    this.w_analysis.add(this.dispCtrl);
    this.dispPane = new JTabbedPane();
    this.dispPane.setTabPlacement(SwingConstants.TOP);
    this.dispPane.setPreferredSize(new Dimension(710, 300));
    this.dispPane.add("Displacement", this.disp);
    this.dispPane.add("Waterplane", this.dispWL);
    this.dispPane.add("Wetted Surface", this.dispWet);
    this.dispPane.add("Weights", this.dispWgt);
    this.dispPane.add("Station Area", this.dispStn);
    this.w_analysis.add(this.dispPane);

    this.f_analysis.getContentPane().add(this.w_analysis);
    this.setCtrls();
    this.bOpen = false;

    this.repaint();
  }


  public void deleteStation() {

    try {
      if (this.f_edit.isVisible()) {
        JOptionPane.showMessageDialog(null,
            "Data entry windows should be closed before changing the number of stations.",
            "Warning!", JOptionPane.ERROR_MESSAGE);
        return;
      }
    } catch (final Exception e) {
      System.out.println(e);
    }
    int dStn;
    try {
      final String s = JOptionPane.showInputDialog("Delete Station #:");
      if (s == null) {
        return;
      }
      dStn = Integer.parseInt(s);
      if ((dStn >= this.hull.Stations.length) || (dStn < 0)) {
        JOptionPane.showMessageDialog(null, "Station # out of range.", "Warning!",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

    } catch (final NumberFormatException nfe) {
      JOptionPane.showMessageDialog(null, "Unable to interpret number.", "Warning!",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    final double[] sta = new double[this.hull.Stations.length - 1];
    final ArrayList<rawLine> o = new ArrayList<>();

    int i;

    for (i = 0; i < this.hull.Stations.length; i++) {

      if (i < dStn) {
        sta[i] = this.hull.Stations[i];
      }
      if (i > dStn) {
        sta[i - 1] = this.hull.Stations[i];
      }
    }

    Point[] p;
    Point q;
    ListIterator<?> l;
    l = this.hull.Offsets.listIterator();
    while (l.hasNext()) {
      final rawLine rL = (rawLine) l.next();
      final Line ln = rL.ln;
      p = new Point[sta.length];

      for (i = 0; i < this.hull.Stations.length; i++) {
        q = ln.getPoint(i);
        if (i < dStn) {
          p[i] = q;
        }
        if (i > dStn) {
          p[i - 1] = q;
        }
      }

      final rawLine rLnew = new rawLine();
      rLnew.ln = new Line(p);
      rLnew.lnName = rL.lnName;
      o.add(rLnew);

    }
    this.hull.Offsets = o;
    this.hull.Stations = sta;
    this.hull.setLines();
    this.hull.bChanged = true;
    this.body.repaint();
    this.plan.repaint();

    this.setCtrls();



  } // end deleteStation

  public void getHull() {
    String fn;
    int returnVal;

    try {
      this.hull = new Hull();

      // ff = new bcFileFilter(new String[] {"xml", "hul"}, "boatCalc and Hulls files");
      // fc.setFileFilter(ff);
      returnVal = this.fc.showOpenDialog(null);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        fn = (this.fc.getSelectedFile().getName()).toLowerCase();
        if (fn.indexOf(".hul") > 0) {
          this.hull.getHulls(this.fc.getSelectedFile());
        } else {
          this.hull.getData(this.fc.getSelectedFile());
        }
        if (!this.bOpen) {
          this.setCtrls();
        }
      }
    } catch (final NullPointerException npe) {
      System.out.println(npe);
    }
  }// end getHull

  public void insertStation() {

    try {
      if (this.f_edit.isVisible()) {
        JOptionPane.showMessageDialog(null,
            "Data entry windows should be closed before changing the number of stations.",
            "Warning!", JOptionPane.ERROR_MESSAGE);
        return;
      }
    } catch (final Exception e) {
      System.out.println(e);
    }
    double newStn;
    try {
      final String s = JOptionPane.showInputDialog("New station at:");
      if (s == null) {
        return;
      }
      newStn = Double.parseDouble(s);
    } catch (final NumberFormatException nfe) {
      JOptionPane.showMessageDialog(null, "Unable to interpret number.", "Warning!",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    boolean bDone = false;

    final double[] sta = new double[this.hull.Stations.length + 1];
    final ArrayList<rawLine> o = new ArrayList<>();

    int i, j;

    j = 0;
    for (i = 0; i < this.hull.Stations.length; i++) {
      if (newStn == this.hull.Stations[i]) {
        JOptionPane.showMessageDialog(null, "New station must not equal old.", "Warning!",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      if ((newStn < this.hull.Stations[i]) && !bDone) {
        sta[j] = newStn;
        bDone = true;
        j++;
      }
      sta[j] = this.hull.Stations[i];
      j++;
    }

    if (!bDone) {
      sta[j] = newStn;
    }


    Point[] p;
    Point q;
    ListIterator<?> l;
    l = this.hull.Offsets.listIterator();
    while (l.hasNext()) {
      final rawLine rL = (rawLine) l.next();
      final Line ln = rL.ln;
      p = new Point[sta.length];

      bDone = false;
      j = 0;
      for (i = 0; i < this.hull.Stations.length; i++) {
        q = ln.getPoint(i);

        if ((newStn < q.x) && !bDone) {
          p[j] = new Point(newStn, 0.0, 0.0);
          bDone = true;
          j++;
        }
        p[j] = q;
        j++;
      }
      if (!bDone) {
        p[j] = new Point(newStn, 0.0, 0.0);
      }
      final rawLine rLnew = new rawLine();
      rLnew.ln = new Line(p);
      rLnew.lnName = rL.lnName;
      o.add(rLnew);

    }
    this.hull.Offsets = o;
    this.hull.Stations = sta;
    this.hull.setLines();
    this.hull.bChanged = true;
    this.body.repaint();
    this.plan.repaint();
    this.setCtrls();


  } // end insertStation

  public void saveHull() {
    String fn;
    int returnVal;

    try {
      // ff = new bcFileFilter(new String[] {"xml", "hul"}, "boatCalc and Hulls files");
      // fc.setFileFilter(ff);
      returnVal = this.fc.showSaveDialog(null);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        fn = (this.fc.getSelectedFile().getName()).toLowerCase();
        if (fn.indexOf(".hul") > 0) {
          this.hull.saveHulls(this.fc.getSelectedFile());
        } else {
          this.hull.saveData(this.fc.getSelectedFile());
        }
      }
    } catch (final NullPointerException npe) {
      System.out.println(npe);
    }

  }

  public void setCtrls() {
    final double sl_ctr = -((0.5 * (this.hull.gz_max + this.hull.gz_min)) - this.hull.base);
    double sl_min = sl_ctr - (0.5 * (this.hull.gz_max - this.hull.gz_min));
    double sl_max = sl_ctr + (0.5 * (this.hull.gz_max - this.hull.gz_min));
    int itic, dinc;

    if ((this.hull.base < sl_min) || (this.hull.base > sl_max)) {
      this.hull.base = sl_ctr;
      JOptionPane.showMessageDialog(null, "Setting baseline offset.", "Warning!",
          JOptionPane.ERROR_MESSAGE);
    }

    if (this.hull.units.UNITS == 0) {
      sl_min = 12.0 * Math.floor(sl_min / 12.0);
      sl_max = 12.0 * Math.ceil(sl_max / 12.0);

      itic = 4;
      dinc = 48;
      if ((sl_max - sl_min) > 54.0) {
        itic = 8;
        dinc = 96;
      }
      this.dispCtrl.slBase.setModel(new DefaultBoundedRangeModel((int) (4.0 * this.hull.base), 0,
          4 * (int) sl_min, 4 * (int) sl_max));
      this.dispCtrl.slBase.setMajorTickSpacing(48);
      this.dispCtrl.slBase.setMinorTickSpacing(itic);

      // Create the label table
      final Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
      for (double d = 4 * sl_min; d <= ((4 * sl_max) + 0.5); d += dinc) {
        labelTable.put(new Integer((int) d), new JLabel(Double.toString(d / 4)));
      }
      this.dispCtrl.slBase.setLabelTable(labelTable);
    }

    else if (this.hull.units.UNITS == 1) {
      sl_min = Math.floor(sl_min);
      sl_max = Math.max(Math.ceil(sl_max), sl_min + 1);

      itic = 4;
      dinc = 48;
      if ((sl_max - sl_min) > 72) {
        itic = 128;
        dinc = 1536;
      } else if ((sl_max - sl_min) > 36) {
        itic = 64;
        dinc = 768;
      } else if ((sl_max - sl_min) > 18) {
        itic = 32;
        dinc = 384;
      } else if ((sl_max - sl_min) > 9.0) {
        itic = 16;
        dinc = 192;
      } else if ((sl_max - sl_min) > 4.5) {
        itic = 8;
        dinc = 96;
      }
      this.dispCtrl.slBase.setModel(new DefaultBoundedRangeModel((int) (48.0 * this.hull.base), 0,
          48 * (int) sl_min, 48 * (int) sl_max));
      this.dispCtrl.slBase.setMajorTickSpacing(12 * itic);
      this.dispCtrl.slBase.setMinorTickSpacing(itic);

      // Create the label table
      final Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
      for (double d = 48 * sl_min; d <= ((48 * sl_max) + 0.5); d += dinc) {
        labelTable.put(new Integer((int) d), new JLabel(Double.toString(d / 48)));
      }
      this.dispCtrl.slBase.setLabelTable(labelTable);

    }

    else if (this.hull.units.UNITS == 2) {
      sl_min = 10.0 * Math.floor(sl_min / 10.0);
      sl_max = 10.0 * Math.ceil(sl_max / 10.0);

      itic = 1;
      dinc = 50;

      this.dispCtrl.slBase.setModel(
          new DefaultBoundedRangeModel((int) this.hull.base, 0, (int) sl_min, (int) sl_max));
      this.dispCtrl.slBase.setMajorTickSpacing(10);
      this.dispCtrl.slBase.setMinorTickSpacing(itic);

      // Create the label table
      final Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
      for (double d = sl_min; d <= (sl_max + 0.5); d += dinc) {
        labelTable.put(new Integer((int) d), new JLabel(Double.toString(d)));
      }
      this.dispCtrl.slBase.setLabelTable(labelTable);

    }

    else if (this.hull.units.UNITS == 3) {
      sl_min = 100 * Math.floor(sl_min);
      sl_max = Math.max(100 * Math.ceil(sl_max), sl_min + 1.0);

      boolean OK = true;

      while (OK && (sl_min < sl_max)) {
        try {
          itic = 10;
          dinc = 50;
          if ((sl_max - sl_min) > 12.0) {
            itic = 200;
            dinc = 400;
          } else if ((sl_max - sl_min) > 6.0) {
            itic = 100;
            dinc = 200;
          } else if ((sl_max - sl_min) > 3.0) {
            itic = 50;
            dinc = 100;
          }

          this.dispCtrl.slBase.setModel(new DefaultBoundedRangeModel((int) (100.0 * this.hull.base),
              0, (int) sl_min, (int) sl_max));
          this.dispCtrl.slBase.setMajorTickSpacing(10 * itic);
          this.dispCtrl.slBase.setMinorTickSpacing(itic);

          // Create the label table
          final Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
          for (double d = sl_min; d <= (sl_max + 0.5); d += dinc) {
            labelTable.put(new Integer((int) d), new JLabel(Double.toString(d / 100.0)));
          }
          this.dispCtrl.slBase.setLabelTable(labelTable);
          OK = false;

        } catch (final IllegalArgumentException iae) {
          sl_min = sl_min + 100;
          sl_max = sl_max - 100;
          System.out.println("slider limits: " + sl_min + " " + sl_max);
        }
      } // end while

    }

    this.dispCtrl.slBase.setPaintTicks(true);
    this.dispCtrl.slBase.setPaintLabels(true);
    this.dispCtrl.slBase.revalidate();
    this.dispCtrl.slBase.repaint();

    this.ctrl.lblName.setText("Design: " + this.hull.boatname);
    this.ctrl.lblNA.setText("Designer: " + this.hull.designer);

    if (this.hull.units.WATER == 0) {
      this.ctrl.hP.btnSalt.setSelected(true);
    }
    if (this.hull.units.WATER == 1) {
      this.ctrl.hP.btnFresh.setSelected(true);
    }

    if (this.hull.units.UNITS == 0) {
      this.ctrl.uP.btnInLbs.setSelected(true);
    }
    if (this.hull.units.UNITS == 1) {
      this.ctrl.uP.btnFtLbs.setSelected(true);
    }
    if (this.hull.units.UNITS == 2) {
      this.ctrl.uP.btnCmKg.setSelected(true);
    }
    if (this.hull.units.UNITS == 3) {
      this.ctrl.uP.btnMKg.setSelected(true);
    }

    this.ctrl.sP.btnLeft.setSelected(this.hull.bStems[0]);
    this.ctrl.sP.btnRight.setSelected(this.hull.bStems[1]);

  }// end setCtrls

  public void wgtEdit() {
    this.f_wgts = new JFrame("Weight Entry/Edit");
    this.f_wgts.setSize(600, 400);
    final pnlWgtEntry w_wgts = new pnlWgtEntry();
    this.f_wgts.getContentPane().add(w_wgts);
    w_wgts.getWgts();
    this.f_wgts.setVisible(true);
  }

} // end CLASS boatCalc


class SaveOutput extends PrintStream {
  static OutputStream logfile;
  static PrintStream oldStderr;
  static PrintStream oldStdout;

  // Starts copying stdout and
  // stderr to the file f.
  public static void start(final String f) throws IOException {
    // Save old settings.
    SaveOutput.oldStdout = System.out;
    SaveOutput.oldStderr = System.err;

    // Create/Open logfile.
    SaveOutput.logfile = new PrintStream(new BufferedOutputStream(new FileOutputStream(f)));

    // Start redirecting the output.
    System.setOut(new SaveOutput(System.out));
    System.setErr(new SaveOutput(System.err));
  }

  // Restores the original settings.
  public static void stop() {
    System.setOut(SaveOutput.oldStdout);
    System.setErr(SaveOutput.oldStderr);
    try {
      SaveOutput.logfile.close();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  SaveOutput(final PrintStream ps) {
    super(ps);
  }

  // PrintStream override.
  @Override
  public void write(final byte buf[], final int off, final int len) {
    try {
      SaveOutput.logfile.write(buf, off, len);
    } catch (final Exception e) {
      e.printStackTrace();
      this.setError();
    }
    super.write(buf, off, len);
  }

  // PrintStream override.
  @Override
  public void write(final int b) {
    try {
      SaveOutput.logfile.write(b);
    } catch (final Exception e) {
      e.printStackTrace();
      this.setError();
    }
    super.write(b);
  }
} // end CLASS SaveOutput


