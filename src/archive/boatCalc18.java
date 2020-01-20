/*
 * boatCalc.java
 */

/*
 * @author  Peter H. Vanderwaart
 * Copyright 2004
 */

import javax.swing.* ;
import javax.swing.event.* ;
import javax.swing.border.*;
import java.awt.* ;
import java.awt.event.* ;
import java.awt.print.* ;
import java.util.* ;
import java.io.* ;
import java.lang.Math;
import java.text.*;

public class boatCalc extends javax.swing.JFrame {
    JMenuBar mb ;
    JMenu menu ;
    JMenuItem m_new ;
    JMenuItem m_open ;
    JMenuItem m_save ;
    JMenuItem m_print ;
    JMenuItem m_exit ;
    JMenuItem m_disp ;
    JMenuItem m_edit ;
    JMenuItem m_instn ;
    JMenuItem m_destn ;
    JMenuItem m_about ;
    JMenuItem m_sailplan ;
    JMenuItem m_rudder ;
    JMenuItem m_board ;
    
    JPanel w ;
    planPanel plan;
    bodyPanel body;
    ctrlPanel ctrl;

    JFrame f_analysis;
    JPanel w_analysis;
    hdCtrl dispCtrl;
    hdBody dispFore;
    hdBody dispAft;
    JTabbedPane dispPane;
    hdPanel disp;
    wgtPanel dispWgt;
    wlPanel dispWL;  
    wetPanel dispWet;
    stnPanel dispStn;
   
    JFrame f_edit;
    JFrame f_sailplan;
    JFrame f_rudder;
    JFrame f_board;
    JFrame f_wgts;
   
    Hull hull;
    JFileChooser fc;
    //bcFileFilter ff;
    
    bcUnits units;
    boolean bOpen = true;    
    
    bcFormat bcf;
	
    /** Creates a new instance of boatCalc */
public boatCalc() {
   super ("boatCalc") ;
   
	try {SaveOutput.start("boatCalc.log");}
	catch (Exception e) { e.printStackTrace();}
	
   bcf = new bcFormat();
   
	addWindowListener(new WindowAdapter()
      {public void windowClosing(WindowEvent e){
          if (hull.bChanged)
          {int n = JOptionPane.showConfirmDialog(f_edit,"Save Changes?","Data has been changed.",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){
            
        	   try{
              //bcFileFilter ff = new bcFileFilter(new String[] {"xml", "hul"}, "boatCalc and Hulls files");
              //fc.setFileFilter(ff);
              int returnVal = fc.showSaveDialog(null);
              if (returnVal == JFileChooser.APPROVE_OPTION){
               String fn = (fc.getSelectedFile().getName()).toLowerCase();
              if (fn.indexOf(".hul") > 0 ) hull.saveHulls(fc.getSelectedFile());
              else hull.saveData(fc.getSelectedFile());
              }
            } catch (NullPointerException npe){System.out.println(npe);return;}

            }
            else if (n == JOptionPane.CANCEL_OPTION){return;}
          } 
            
	    SaveOutput.stop();
	    System.exit(0);}}); 
            
      mb = new JMenuBar() ;
      menu = new JMenu("File") ;


      m_new = new JMenuItem("New") ;
      m_new.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          if (hull.bChanged){
           int n = JOptionPane.showConfirmDialog(f_edit,"Save Changes?","Data has been changed.",JOptionPane.YES_NO_OPTION);
           if (n == JOptionPane.YES_OPTION){
            
        	   try{
              //bcFileFilter ff = new bcFileFilter(new String[] {"xml", "hul"}, "boatCalc and Hulls files");
              //fc.setFileFilter(ff);
              int returnVal = fc.showSaveDialog(null);
              if (returnVal == JFileChooser.APPROVE_OPTION){
               String fn = (fc.getSelectedFile().getName()).toLowerCase();
              if (fn.indexOf(".hul") > 0 ) hull.saveHulls(fc.getSelectedFile());
              else hull.saveData(fc.getSelectedFile());
              }
            } catch (NullPointerException npe){System.out.println(npe);return;}

            }
            else if (n == JOptionPane.CANCEL_OPTION){return;}
          } 
          String s = JOptionPane.showInputDialog(f_edit,"Number of Stations:"); 
          int n_stn = 11;;
          if ((s != null) && (s.length() > 0)) {
          
          try{n_stn = Integer.parseInt(s);} 
	       catch (NumberFormatException nfe){
           JOptionPane.showMessageDialog(f_edit,"Bad number format..","Warning!", JOptionPane.ERROR_MESSAGE);
          return ;}}
          hull = new Hull();
          hull.Stations = new double[n_stn];
          hull.Offsets = new ArrayList();
          hull.valid = false;
          hull.designer = "NA";
          hull.boatname = "new boat";
          for (int j=0;j<n_stn;j++) hull.Stations[j] = j;
          hull.newWgts();
          hull.setLines();
          f_edit = new JFrame("Data Entry/Edit");
          f_edit.setSize(770,550);
          pnlDataEntry w_edit = new pnlDataEntry();
          f_edit.getContentPane().add(w_edit) ;
          f_edit.setVisible(true);
      }});
      menu.add(m_new) ;    
      
      m_open = new JMenuItem("Open") ;
      m_open.addActionListener(new ActionListener()
            { public void actionPerformed(ActionEvent e)
                {
                 getHull();
                 if (hull.valid){hull.calcDisp();
                                 dispWgt.setWeights();}
		           repaint();
                }
            });
      menu.add(m_open) ;    

      m_save = new JMenuItem("Save") ;
      m_save.addActionListener(new ActionListener()
            { public void actionPerformed(ActionEvent e)
                {
                 saveHull();
		           repaint();
                }
            });
      menu.add(m_save) ;    
      
      m_print = new JMenuItem("Print") ;
      m_print.addActionListener(new ActionListener()
            { public void actionPerformed(ActionEvent e)
                {PrinterJob pJ = PrinterJob.getPrinterJob();
                 //pJ.setPrintable(body);
                 //if(pJ.printDialog()){
                 //   try{pJ.print();}
                 //   catch(Exception ex){System.out.println(ex);}
                 //}
                }
            });
      menu.add(m_print) ;    

      
      m_exit = new JMenuItem("Exit") ;
      m_exit.addActionListener(new ActionListener()
      { public void actionPerformed(ActionEvent e){
          if (hull.bChanged)
          {int n = JOptionPane.showConfirmDialog(f_edit,"Save Changes?","Data has been changed.",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){
            
        	   try{
              //bcFileFilter ff = new bcFileFilter(new String[] {"xml", "hul"}, "boatCalc and Hulls files");
              //fc.setFileFilter(ff);
              int returnVal = fc.showSaveDialog(null);
              if (returnVal == JFileChooser.APPROVE_OPTION){
               String fn = (fc.getSelectedFile().getName()).toLowerCase();
              if (fn.indexOf(".hul") > 0 ) hull.saveHulls(fc.getSelectedFile());
              else hull.saveData(fc.getSelectedFile());
              }
            } catch (NullPointerException npe){System.out.println(npe);return;}

            }
            else if (n == JOptionPane.CANCEL_OPTION){return;}
          } 
           SaveOutput.stop();
		     System.exit(0);
      }});
      menu.add(m_exit) ;    
      mb.add(menu) ;

      menu = new JMenu("Edit");
      m_instn = new JMenuItem("Insert station");
      m_instn.addActionListener(new ActionListener()
            { public void actionPerformed(ActionEvent e)
                {insertStation();}});
      m_destn = new JMenuItem("Delete station");
      m_destn.addActionListener(new ActionListener()
            { public void actionPerformed(ActionEvent e)
                {deleteStation();}});
      m_edit = new JMenuItem("Edit data");
      m_edit.addActionListener(new ActionListener()
            { public void actionPerformed(ActionEvent e)
                {f_edit = new JFrame("Data Entry/Edit");
                 f_edit.setSize(770,550);
	              pnlDataEntry w_edit = new pnlDataEntry();
                 f_edit.getContentPane().add(w_edit) ;
                 f_edit.setVisible(true);
                }
            });
      menu.add(m_instn) ;
	   menu.add(m_destn) ;
	   menu.add(m_edit) ;
	   mb.add(menu);

      menu = new JMenu("Design");
	   m_sailplan = new JMenuItem("Sailplan") ;
      m_sailplan.addActionListener(new ActionListener()
            { public void actionPerformed(ActionEvent e)
                {f_sailplan = new JFrame("Sailplan");
                 f_sailplan.setSize(770,550);
	              pnlSailplan w_sailplan = new pnlSailplan();
                 f_sailplan.getContentPane().add(w_sailplan) ;
                 f_sailplan.setVisible(true);
                }
            });
      menu.add(m_sailplan);
      
	   m_rudder = new JMenuItem("Rudder") ;
      m_rudder.addActionListener(new ActionListener()
            { public void actionPerformed(ActionEvent e)
                {f_rudder = new JFrame("Rudder");
                 f_rudder.setSize(770,550);
	              pnlRudder w_rudder = new pnlRudder();
                 f_rudder.getContentPane().add(w_rudder) ;
                 f_rudder.setVisible(true);
                }
            });
      menu.add(m_rudder);

 	   m_board = new JMenuItem("Centerboard") ;
      m_board.addActionListener(new ActionListener()
            { public void actionPerformed(ActionEvent e)
                {f_board = new JFrame("Centerboard");
                 f_board.setSize(770,550);
	              pnlCenterboard w_board = new pnlCenterboard();
                 f_board.getContentPane().add(w_board) ;
                 f_board.setVisible(true);
                }
            });
      menu.add(m_board);
      
      mb.add(menu) ;
      
      menu = new JMenu("Analysis") ;
	   m_disp = new JMenuItem("Displacement") ;
      m_disp.addActionListener(new ActionListener()
            { public void actionPerformed(ActionEvent e)
                {f_analysis.setVisible(true);
                   if (hull.valid){ hull.calcDisp();  
                   dispWgt.setWeights();}
                 repaint(); }
            });
      menu.add(m_disp) ;
	   mb.add(menu);

      menu = new JMenu("About");
      m_about = new JMenuItem("About");
      m_about.addActionListener(new ActionListener()
         { public void actionPerformed(ActionEvent e)
           {JOptionPane.showMessageDialog(null,new pnlAbout(),"About", JOptionPane.INFORMATION_MESSAGE);
      }});
      menu.add(m_about);
      mb.add(menu);
      
      setJMenuBar(mb);

      fc = new JFileChooser(".");
      bcFileFilter ff = new bcFileFilter(new String[] {"xml", "hul"}, "boatCalc and Hulls files");
      fc.setFileFilter(ff);
      
      //getHull();
      hull = new Hull();
        
      ctrl = new ctrlPanel(400,200);
      body = new bodyPanel(300,200);
      plan = new planPanel(705,300);
      w = new JPanel() ;
      w.setLayout(new FlowLayout()) ;
      w.setBorder(BorderFactory.createEtchedBorder()) ;
      w.add(ctrl);
      w.add(body);
      w.add(plan);
      getContentPane().add(w) ;
	
	   f_analysis = new JFrame("Displacement Analysis");
      f_analysis.setSize(770,550);
	   disp = new hdPanel(710,300);
      w_analysis = new JPanel();
	   w_analysis.setLayout(new FlowLayout());
      w_analysis.setBorder(BorderFactory.createEtchedBorder()) ;

      dispWgt = new wgtPanel(710,300);
      dispWL = new wlPanel(710,300);     
      dispWet = new wetPanel(710,300);     
      dispStn = new stnPanel(710,300);     
      
      dispCtrl = new hdCtrl(300,200);
      dispFore = new hdBody(200,200);
      dispFore.setTitle("Left");
      dispFore.setType(true);
      dispAft = new hdBody(200,200);
      dispAft.setTitle("Right");
      dispAft.setType(false);
      w_analysis.add(dispFore);
      w_analysis.add(dispAft);
      w_analysis.add(dispCtrl);
      dispPane = new JTabbedPane();
      dispPane.setTabPlacement(JTabbedPane.TOP);
      dispPane.setPreferredSize(new Dimension(710,300));
      dispPane.add("Displacement",disp);
      dispPane.add("Waterplane",dispWL);
      dispPane.add("Wetted Surface",dispWet);
      dispPane.add("Weights",dispWgt);
      dispPane.add("Station Area",dispStn);
      w_analysis.add(dispPane);
      
      f_analysis.getContentPane().add(w_analysis) ;
      setCtrls();
      bOpen = false;
      
	   repaint();
    }
    
    public static void main(String[] args){
       
    try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
    catch (Exception e) { }

     boatCalc c = new boatCalc() ;
     c.setSize(770,570) ;
     c.setBackground(Color.white) ;
     c.setVisible(true) ;
    } // end main
   
   public class wlPanel extends JPanel{
      Dimension d ;
      int[][] iComp = new int [2][hull.NDIV+1];
      int[][] iCur = new int [2][hull.NDIV+1];

   public wlPanel(int x, int y){
      d = new Dimension(x,y) ;
   
   
   }// end constructor
   public Dimension getPreferredSize(){return d ; }
   protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    double sinone = Math.sin(Math.toRadians(1.0));


     double mx = getWidth() ;
     double my = getHeight() ;
     int ix = (int) mx;
     int iy = (int) my;
     int xb = (int) 100;
     int yb = (int) my/2;

     g.clearRect(0,0,ix,iy) ;
     
     double rx = (mx - 200.0)/(hull.gx_max - hull.gx_min);
     double ry = (0.8 * my)/(hull.gy_max - hull.gy_min);
     double r = Math.min(rx,ry);
     int iu, iv, iw, iz; 
     iu = xb + (int) (r * (hull.gx_min - hull.gx_min));
     iv = yb + (int) (r * 0);
     iw = xb + (int) (r * (hull.gx_max - hull.gx_min));
     iz = yb + (int) (r * 0);
     
     // draw axis
     g.setColor(Color.red);
     g.drawLine(iu,iv,iw,iz);
     g.setColor(Color.black);
     
     if (disp.bComp){
        g.setColor(Color.blue);
        iu = xb + (int) (r * (hull.vDisp[hull.CX][0] - hull.gx_min));
        for (int i=1; i<= hull.NDIV; i++){
         iv = xb + (int) (r * (hull.vDisp[hull.CX][i] - hull.gx_min));
         g.drawLine(iu,iComp[0][i-1],iv,iComp[0][i]);
         iu = iv;
        }
        
        iu = xb + (int) (r * (hull.vDisp[hull.CX][0] - hull.gx_min));
        for (int i=1; i<= hull.NDIV; i++){
         iv = xb + (int) (r * (hull.vDisp[hull.CX][i] - hull.gx_min));
         g.drawLine(iu,iComp[1][i-1],iv,iComp[1][i]);
         iu = iv;
        }
        g.setColor(Color.black);
     }
     
     double x = hull.vDisp[hull.CX][0];
     double y = hull.vWL[0][0];
     iu = xb + (int) (r * (x - hull.gx_min));
     iv = yb - (int) (r *  y);

     iCur[0][0] = iv;
     for (int i=1; i<=hull.NDIV; i++){
        x = hull.vDisp[hull.CX][i];
        y = hull.vWL[0][i];
        iw = xb + (int) (r * (x - hull.gx_min));
        iz = yb - (int) (r *  y);
        g.drawLine(iu,iv,iw,iz);
        iu = iw;
        iv = iz;
        iCur[0][i] = iv;
     }
     
     x = hull.vDisp[hull.CX][0];
     y = hull.vWL[1][0];
     iu = xb + (int) (r * (x - hull.gx_min));
     iv = yb - (int) (r *  y);

     iCur[1][0] = iv;
     for (int i=1; i<=hull.NDIV; i++){
        x = hull.vDisp[hull.CX][i];
        y = hull.vWL[1][i];
        iw = xb + (int) (r * (x - hull.gx_min));
        iz = yb - (int) (r *  y);
        g.drawLine(iu,iv,iw,iz);
        iu = iw;
        iv = iz;
        iCur[1][i] = iv;
     }
     
     
     // compute 
     
     double wlCur = hull.vWL[1][0]-hull.vWL[0][0];
     double wlX = wlCur * hull.vDisp[hull.CX][0];
     double wlY = wlCur * 0.5 * (hull.vWL[1][0]+hull.vWL[0][0]);
     double wlSum = wlCur;
     double wlArea = 0;
     double wlLast = wlCur;
     double wlMax = wlCur;
     double wlXMax = hull.vDisp[hull.CX][0];
     
     for (int i=1; i<=hull.NDIV; i++){
        wlCur = hull.vWL[1][i]-hull.vWL[0][i];
        if (wlCur > wlMax) {wlMax = wlCur;
                            wlXMax = hull.vDisp[hull.CX][i];}
        wlX = wlX + wlCur * hull.vDisp[hull.CX][i];
        wlY = wlY + wlCur * 0.5 * (hull.vWL[1][i]+hull.vWL[0][i]);
        wlSum = wlSum + wlCur;
        wlArea = wlArea + (hull.vDisp[hull.CX][i] - hull.vDisp[hull.CX][i-1])* 0.5*(wlCur + wlLast);
        wlLast = wlCur;
     }     
     
     int il = 25;
     g.drawString("Waterplane Area",10,il);
     g.drawString(bcf.DF2d.format(hull.units.coefArea()*wlArea)+hull.units.lblArea(),125,il);
     il+=20;
     g.drawString("Max WL Beam:",10,il);
     g.drawString(bcf.DF1d.format(wlMax),125,il);
     il+=15;
     g.drawString("   @Station:",10,il);
     g.drawString(bcf.DF1d.format(wlXMax),125,il);
     il+=20;
     
     if (wlSum > 0){
        wlX = wlX / wlSum;
        wlY = wlY / wlSum;
        g.drawString("CoA @ Station:",10,il);
        g.drawString(bcf.DF1d.format(wlX),125,il);
        il+=15;
        g.drawString("   Breadth:",10,il);
        g.drawString(bcf.DF1d.format(wlY),125,il);
        il+=20;
        iw = xb + (int) (r * (wlX - hull.gx_min));
        iz = yb - (int) (r *  wlY);
        g.setColor(Color.red);
        g.drawArc(iw-5,iz-5,10,10,0,360);
        g.setColor(Color.black);
        
        
     wlLast = hull.vWL[1][0]-hull.vWL[0][0];
     double d, w, h, xm;
     double mp = 0;
     for (int i=1; i<=hull.NDIV; i++){
        d = hull.vDisp[hull.CX][i] - hull.vDisp[hull.CX][i-1] ; // delta x
        wlCur = hull.vWL[1][i]-hull.vWL[0][i];
        w = 0.5 * (wlCur + wlLast); // average width
        xm = 0.5 * (hull.vDisp[hull.CX][i] + hull.vDisp[hull.CX][i-1]); // average x
        h = sinone * (xm-wlX); // height
        mp = mp + hull.units.Vol2Wgt() * d * w * h * (xm-wlX);
        wlLast = wlCur;
     }        
        il+=75;
        
        double ppi = wlArea * hull.units.coefPPI() * hull.units.Vol2Wgt();
        g.drawString("Imersion:",10,il);
        g.drawString(bcf.DF1d.format(ppi)+hull.units.lblPPI(),125,il);
        il+=20;
        
        g.drawString("Moment to ",10,il);
        il+=15;
        g.drawString("pitch 1 deg:",10,il);
        g.drawString(bcf.DF1d.format(mp)+" "+hull.units.lblMom(),125,il);
        il+=20;
        il = 25;

        int ic = ix - 200;
        il=25;
        g.drawString("Waterplane Coef.:",ic,il);
        g.drawString(bcf.DF2d.format(wlArea / (wlMax * (hull.lwlRight - hull.lwlLeft))),ic+150,il);
        
        // half-angle computation
        int i1, i2;
        double a1, a2;
        il+=20;
        g.drawString("Half Angles (avg):",ic,il);

        // left
        if (hull.NDIV > 10) {i1 = 1; i2 = 5;}
        else {i1=1;i2=2;}
        a1 = Math.atan2(hull.vWL[1][i2]-hull.vWL[1][i1],(hull.vDisp[hull.CX][i2]-hull.vDisp[hull.CX][i1]) );
        a2 = Math.atan2(hull.vWL[0][i2]-hull.vWL[0][i1],(hull.vDisp[hull.CX][i2]-hull.vDisp[hull.CX][i1]) );
        il+=15;
        g.drawString("Left -",ic+50,il);
        g.drawString(bcf.DF1d.format(Math.toDegrees(a1-a2)),ic+150,il);

        // right
        if (hull.NDIV > 10) {i1 = hull.NDIV-1; i2 = hull.NDIV-4;}
        else {i1=hull.NDIV-1;i2=hull.NDIV-2;}
        a1 = Math.atan2(hull.vWL[1][i2]-hull.vWL[1][i1],(hull.vDisp[hull.CX][i1]-hull.vDisp[hull.CX][i2]) );
        a2 = Math.atan2(hull.vWL[0][i2]-hull.vWL[0][i1],(hull.vDisp[hull.CX][i1]-hull.vDisp[hull.CX][i2]) );
        il+=15;
        g.drawString("Right -",ic+50,il);
        g.drawString(bcf.DF1d.format(Math.toDegrees(a1-a2)),ic+150,il);
        il+=15;
        
     }
     
   } // end paintComponent 
   }; // end wlPanel
   

   public class stnPanel extends JPanel implements ChangeListener{
   Dimension d ;
   int iPct=50;
   JPanel pnlSlct;
   JSlider xSlct;
   
   public stnPanel(int x, int y){
      d = new Dimension(x,y) ;
      stnBody body = new stnBody(400,300);
      add(body);
      
      xSlct = new JSlider();
      xSlct.setPreferredSize(new Dimension(250,42));
      xSlct.setMajorTickSpacing(10);
      xSlct.setMinorTickSpacing(5);
      xSlct.setPaintTicks(true);
      xSlct.setPaintLabels(true);
       
      xSlct.addChangeListener(this);

      pnlSlct = new JPanel();
      Border bcBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
      pnlSlct.setBorder(BorderFactory.createTitledBorder(bcBorder,"Station (%LWL)"));
      pnlSlct.add(xSlct);
      add(pnlSlct);
      
   }// end constructor

   public Dimension getPreferredSize(){return d ; }
   
   public void stateChanged(ChangeEvent e)
                {  iPct = xSlct.getValue();
                   dispStn.repaint();  
                }
   
   
   }//end stnPanel
   
    public class stnBody extends JPanel{
    Dimension d ;
    String title = "Station Area";
    
    public stnBody(int x, int y) {
         d = new Dimension(x,y) ;
         setBackground(Color.white) ;
    }
        
    public Dimension getPreferredSize(){return d ; }
    public void setTitle(String s){title = s;}
    
    protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     double sinang = Math.sin(Math.toRadians(hull.angHeel));
     double cosang = Math.cos(Math.toRadians(hull.angHeel));

     double mx = getWidth() ;
     double my = getHeight() ;
     int ix = (int) mx;
     int iy = (int) my;
     int xb = ix/2;
     int yb = iy/2;
     Point p1, p2;
     int u,v,w,z;
     double r = 0.60*xb/hull.gy_max;
     g.clearRect(0,0,ix,iy) ;
     int il = 15;
     g.drawString(title,il,10);
     // draw axes
     g.setColor(Color.red);
     g.drawLine(xb,5,xb,iy-5);
     g.drawLine(5,yb,ix-5,yb);
     g.setColor(Color.black);

     double x = hull.lwlLeft + (0.01 * (double) dispStn.iPct) * (hull.lwlRight - hull.lwlLeft);
     double hsArea = 0;
     double ty = 0, tz = 0;

        // draw station
        Iterator si = hull.getStation(x,hull.angHeel);
        p1 = (Point) si.next();
        Point p0 = new Point(p1);
        while(si.hasNext()){
           p2 = (Point) si.next();
           u = xb + (int) (r * p1.y);
           v = yb - (int) (r * p1.z);
           w = xb + (int) (r * p2.y);
           z = yb - (int) (r * p2.z);
           g.drawLine(u,v,w,z);
           
           g.setColor(Color.red);
           hsArea = hsArea + hull.TriArea (ty,tz,p1.y,p1.z,p2.y,p2.z);
           u = xb + (int) (r * ty);
           v = yb - (int) (r * tz);
           w = xb + (int) (r * p1.y);
           z = yb - (int) (r * p1.z);
           g.drawLine(u,v,w,z);
           u = xb + (int) (r * p2.y);
           v = yb - (int) (r * p2.z);
           g.drawLine(u,v,w,z);
           w = xb + (int) (r * ty);
           z = yb - (int) (r * tz);
           g.drawLine(u,v,w,z);
           g.setColor(Color.black);
           
           p1 = p2;
        }
           p2 = p0;
           hsArea = hsArea + hull.TriArea (ty,tz,p1.y,p1.z,p2.y,p2.z);
           g.setColor(Color.red);
           u = xb + (int) (r * ty);
           v = yb - (int) (r * tz);
           w = xb + (int) (r * p1.y);
           z = yb - (int) (r * p1.z);
           g.drawLine(u,v,w,z);
           u = xb + (int) (r * p2.y);
           v = yb - (int) (r * p2.z);
           g.drawLine(u,v,w,z);
           w = xb + (int) (r * ty);
           z = yb - (int) (r * tz);
           g.drawLine(u,v,w,z);
           g.setColor(Color.black);
        
        
        
    double [] rVals = hull.getArea(x,hull.angHeel,true);
    
    il += 25;
    g.drawString("Station (%): "+bcf.DF0d.format(dispStn.iPct), 10,il);
    il += 20;
    g.drawString("          @: "+bcf.DF0d.format(x), 10,il);
    il += 20;
    g.drawString("Displaced Area: "+bcf.DF1d.format(hull.units.coefArea()*rVals[hull.SAREA])+hull.units.lblArea(), 10,il);
    il += 20;
    g.drawString("    Total Area: "+bcf.DF1d.format(hull.units.coefArea()*hsArea)+hull.units.lblArea(), 10,il);

    g.setColor(Color.blue);
    Iterator di = hull.DispTri.iterator(); 
    while (di.hasNext()){
    double[] tri = (double[]) di.next();
           u = xb + (int) (r * tri[0]);
           v = yb - (int) (r * tri[1]);
           w = xb + (int) (r * tri[2]);
           z = yb - (int) (r * tri[3]);
           g.drawLine(u,v,w,z);
           u = xb + (int) (r * tri[4]);
           v = yb - (int) (r * tri[5]);
           g.drawLine(u,v,w,z);
           w = xb + (int) (r * tri[0]);
           z = yb - (int) (r * tri[1]);
           g.drawLine(u,v,w,z);
    }
     
    }// end paint
    }// end hdBody

   
   public class wetPanel extends JPanel{
   Dimension d ;
   int[] iComp = new int[hull.NDIV+1];
   int[] iCur = new int[hull.NDIV+1];

   public wetPanel(int x, int y){
      d = new Dimension(x,y) ;
   
   
   }// end constructor
   public Dimension getPreferredSize(){return d ; }
   protected void paintComponent(Graphics g) {
    super.paintComponent(g);

     double mx = getWidth() ;
     double my = getHeight() ;
     int ix = (int) mx;
     int iy = (int) my;
     int xb = (int) 100;
     int yb = (int) my-50;

     g.clearRect(0,0,ix,iy) ;
     
     double rx = (mx - 200.0)/(hull.gx_max - hull.gx_min);
     double ry = my/(hull.gy_max - hull.gy_min);
     double r = Math.min(rx,ry);
     int iu, iv, iw, iz; 
     iu = xb + (int) (r * (hull.gx_min - hull.gx_min));
     iv = yb + (int) (r * 0);
     iw = xb + (int) (r * (hull.gx_max - hull.gx_min));
     iz = yb + (int) (r * 0);
     
     // draw axis
     g.setColor(Color.red);
     g.drawLine(iu,iv,iw,iz);
     g.setColor(Color.black);
     
     if (disp.bComp){
        g.setColor(Color.blue);
        iu = xb + (int) (r * (hull.vDisp[hull.CX][0] - hull.gx_min));
        for (int i=1; i<= hull.NDIV; i++){
         iv = xb + (int) (r * (hull.vDisp[hull.CX][i] - hull.gx_min));
         g.drawLine(iu,iComp[i-1],iv,iComp[i]);
         iu = iv;
        }
        g.setColor(Color.black);
     }
     
     double x = hull.vDisp[hull.CX][0];
     double y = hull.vWet[0];
     iu = xb + (int) (r * (x - hull.gx_min));
     iv = yb - (int) (r *  y);

     iCur[0] = iv;
     for (int i=1; i<=hull.NDIV; i++){
        x = hull.vDisp[hull.CX][i];
        y = hull.vWet[i];
        iw = xb + (int) (r * (x - hull.gx_min));
        iz = yb - (int) (r *  y);
        g.drawLine(iu,iv,iw,iz);
        iu = iw;
        iv = iz;
        iCur[i] = iv;
     }
     
     // compute 
     
     double wetX = hull.vWet[0] * hull.vDisp[hull.CX][0];
     double wetSum = hull.vWet[0];
     double wetArea = 0;
     double wlCur = hull.vWL[1][0]-hull.vWL[0][0];

     for (int i=1; i<=hull.NDIV; i++){
        wetX = wetX + hull.vWet[i] * hull.vDisp[hull.CX][i];
        wetSum = wetSum + hull.vWet[i] ;
        wetArea = wetArea + (hull.vDisp[hull.CX][i]-hull.vDisp[hull.CX][i-1]) * 0.5 * (hull.vWet[i] + hull.vWet[i-1]);        
        wlCur = hull.vWL[1][i]-hull.vWL[0][i];
     }     
     
     
     int il = 25;
     g.drawString("Wetted Surface",10,il);
     g.drawString(bcf.DF1d.format(hull.units.coefArea()*wetArea)+hull.units.lblArea(),125,il);
     il+=20;
     if (wetSum > 0) {
        wetX = wetX / wetSum;
        g.drawString("Ctr of Area @ Stn: ",10,il);
        g.drawString(bcf.DF1d.format(wetX),125,il);
     }
     
     
   } // end paintComponent 
   }; // end wetPanel

   
   
    public class wgtPanel extends JPanel{
    Dimension d ;
    JLabel[][] lblWgtDisp;
    JLabel[] lblMoments;
    bcLabel lblHeel;
    
    public wgtPanel(int x, int y) {
         d = new Dimension(x,y) ;
         lblHeel = new bcLabel("Heel:",JLabel.CENTER);
         lblWgtDisp = new JLabel[4][3];
         lblMoments = new JLabel[2];
         //setBackground(Color.lightGray) ;
         setBorder(BorderFactory.createEtchedBorder()) ;
         setLayout(new GridLayout(0,8));

         add(lblHeel);
         add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
         add(new bcLabel("CoB",JLabel.CENTER));
         add(new bcLabel("CoG",JLabel.CENTER));
         add(new bcLabel("Difference",JLabel.CENTER));
         add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
         add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
         add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));

         add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
         add(new bcLabel("Weight",JLabel.RIGHT));
         add(lblWgtDisp[0][0] = new JLabel("disp_x",JLabel.RIGHT));
         add(lblWgtDisp[0][1] = new JLabel("wgt_x",JLabel.RIGHT));
         add(lblWgtDisp[0][2] = new JLabel("diff_x",JLabel.RIGHT));
         add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
         add(new bcLabel("  Moments  ",JLabel.CENTER));
         add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
         
         add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
         add(new bcLabel("Station",JLabel.RIGHT));
         add(lblWgtDisp[1][0] = new JLabel("disp_x",JLabel.RIGHT));
         add(lblWgtDisp[1][1] = new JLabel("wgt_x",JLabel.RIGHT));
         add(lblWgtDisp[1][2] = new JLabel("diff_x",JLabel.RIGHT));
         add(new bcLabel("Pitch",JLabel.RIGHT));
         add(lblMoments[0] = new JLabel("moment_p",JLabel.RIGHT));
         add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
         
         add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
         add(new bcLabel("Breadth",JLabel.RIGHT));
         add(lblWgtDisp[2][0] = new JLabel("disp_y",JLabel.RIGHT));
         add(lblWgtDisp[2][1] = new JLabel("wgt_y",JLabel.RIGHT));
         add(lblWgtDisp[2][2] = new JLabel("diff_y",JLabel.RIGHT));
         add(new bcLabel("Heel",JLabel.RIGHT));
         add(lblMoments[1] = new JLabel("moment_h",JLabel.RIGHT));
         add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
         
         add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
         add(new bcLabel("Height",JLabel.RIGHT));
         add(lblWgtDisp[3][0] = new JLabel("disp_z",JLabel.RIGHT));
         add(lblWgtDisp[3][1] = new JLabel("wgt_z",JLabel.RIGHT));
         add(lblWgtDisp[3][2] = new JLabel("diff_z",JLabel.RIGHT));
         add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
         add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
         add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));

    }
        
    public Dimension getPreferredSize(){return d ; }
    
    
    public void setWeights(){
        double sinang = Math.sin(Math.toRadians(hull.angHeel));
        double cosang = Math.cos(Math.toRadians(hull.angHeel));

        double dWgt = hull.units.Vol2Wgt()*hull.hVals[hull.DISP]; 
        
        lblHeel.setText("Heel: "+bcf.DF0d.format(hull.angHeel));
        lblWgtDisp[0][0].setText(bcf.DF1d.format(dWgt));     
        lblWgtDisp[1][0].setText(bcf.DF1d.format(hull.hVals[hull.CX]));     
        lblWgtDisp[2][0].setText(bcf.DF1d.format(hull.hVals[hull.CY]));     
        lblWgtDisp[3][0].setText(bcf.DF1d.format(hull.hVals[hull.CZ]));  
        
        double tw = 0;
        double tx = 0;
        double ty = 0;
        double tz = 0;   
         for (int i=0;i<10;i++){
         tw += hull.wgtWgt[i]; 
         tx += hull.wgtWgt[i] * hull.wgtX[i]; 
         ty += hull.wgtWgt[i] * hull.wgtY[i]; 
         tz += hull.wgtWgt[i] * hull.wgtZ[i]; 
        }
      
      if(tw>0){
      tx = tx/tw;
      ty = ty/tw;
      tz = tz/tw + hull.base ;
      }        
      double rty = cosang * ty - sinang * tz;
      double rtz = sinang * ty + cosang * tz;

      lblWgtDisp[0][1].setText(bcf.DF1d.format(tw));     
      lblWgtDisp[1][1].setText(bcf.DF1d.format(tx));     
      lblWgtDisp[2][1].setText(bcf.DF1d.format(rty));     
      lblWgtDisp[3][1].setText(bcf.DF1d.format(rtz));  
        
      lblWgtDisp[0][2].setText(bcf.DF1d.format(dWgt-tw));     
      lblWgtDisp[1][2].setText(bcf.DF1d.format(hull.hVals[hull.CX]-tx));     
      lblWgtDisp[2][2].setText(bcf.DF1d.format(hull.hVals[hull.CY]-rty));     
      lblWgtDisp[3][2].setText(bcf.DF1d.format(hull.hVals[hull.CZ]-rtz));  
        
      lblMoments[0].setText(bcf.DF1d.format(dWgt * (tx-hull.hVals[hull.CX])));  
      lblMoments[1].setText(bcf.DF1d.format(dWgt * (rty-hull.hVals[hull.CY])));  
    }
    
   //protected void paintComponent(Graphics g) {setWeights();}
    } // ends wgtPanel   
   
   
   
   
    public class hdPanel extends JPanel{
    Dimension d ;
    
    boolean bComp = false;
    int[][] iComp = new int [2][hull.NDIV+1];
    int[][] iCur = new int [2][hull.NDIV+1];
    double xComp = 0;
    double yComp = 0;
    double xCur = 0;
    double yCur = 0;
    
    public hdPanel(int x, int y) {
         d = new Dimension(x,y) ;
         //setBackground(Color.lightGray) ;
    }
        
    public Dimension getPreferredSize(){return d ; }
    
    protected void paintComponent(Graphics g) {
    super.paintComponent(g);

     double mx = getWidth() ;
     double my = getHeight() ;
     int ix = (int) mx;
     int iy = (int) my;
     int xb = (int) 100;
     int yb = (int) my-50;

     g.clearRect(0,0,ix,iy) ;
     
     double rx = (mx - 200.0)/(hull.gx_max - hull.gx_min);
     double ry = (0.8 * my)/(hull.gy_max* hull.gy_max);
     int iu, iv, iw, iz; 
     iu = xb + (int) (rx * (hull.gx_min - hull.gx_min));
     iv = yb + (int) (ry * 0);
     iw = xb + (int) (rx * (hull.gx_max - hull.gx_min));
     iz = yb + (int) (ry * 0);
     
     // draw horicontal axis
     g.setColor(Color.red);
     g.drawLine(iu,iv,iw,iz);
     g.setColor(Color.black);
     
     // draw basic area curve
     
     double lastArea = 0.0;
     double maxArea = 0;
     double maxStn = 0;
     double leftLWL = hull.lwlLeft;
     double rightLWL = hull.lwlRight;
     
     double dx  = hull.dx;
     double x = hull.vDisp[hull.CX][0];
     double y  = hull.vDisp[hull.SAREA][0];

     iu = xb + (int) (rx * (x - hull.gx_min));
     iv = yb - (int) (ry *  y);

     int imax, jmax ;
     
     if (bComp){
        g.setColor(Color.blue);
        imax = 0;
        jmax = iComp[1][0];
        for (int i=1; i<= hull.NDIV; i++){
         g.drawLine(iComp[0][i-1],iComp[1][i-1],iComp[0][i],iComp[1][i]);
        }
        g.setColor(Color.cyan);
        iu = xb + (int) (rx * (xComp - hull.gx_min));
        iv = yb - (int) (ry *  0);
        iw = xb + (int) (rx * (xComp - hull.gx_min));
        iz = yb - (int) (ry *  yComp);
        g.drawLine(iu,iv,iw,iz);        
        g.setColor(Color.black);
     }

     x = hull.vDisp[hull.CX][0];
     y = hull.vDisp[hull.SAREA][0];
     iu = xb + (int) (rx * (x - hull.gx_min));
     iv = yb - (int) (ry *  y);

     iCur[0][0] = iu;
     iCur[1][0] = iv;
     for (int i=1; i<=hull.NDIV; i++){
        x = hull.vDisp[hull.CX][i];
        y = hull.vDisp[hull.SAREA][i];
        iw = xb + (int) (rx * (x - hull.gx_min));
        iz = yb - (int) (ry *  y);
        g.drawLine(iu,iv,iw,iz);
        iu = iw;
        iv = iz;
        iCur[0][i] = iu;
        iCur[1][i] = iv;
        
        if (hull.vDisp[hull.SAREA][i] > maxArea){
           maxArea = hull.vDisp[hull.SAREA][i];
           maxStn = x;}
        lastArea = hull.vDisp[hull.SAREA][i];
     }
     xCur = maxStn;
     yCur = maxArea;
     
     g.setColor(Color.red);
     iu = xb + (int) (rx * (maxStn - hull.gx_min));
     iv = yb - (int) (ry *  0);
     iw = xb + (int) (rx * (maxStn - hull.gx_min));
     iz = yb - (int) (ry *  maxArea);
     g.drawLine(iu,iv,iw,iz);        

     double Cp = hull.hVals[hull.DISP] / ((rightLWL-leftLWL) * maxArea);

     g.setColor(Color.black);
     
     int il = 25;
     g.drawString("Baseline Offset",10,il);
     g.drawString(bcf.DF2d.format(hull.base),125,il);
     il+=15;     
     g.drawString("Angle of Heel",10,il);
     g.drawString(bcf.DF0d.format(hull.angHeel),125,il);     
     il+=20;

     g.drawString("LWL - minimum",10,il);
     g.drawString(bcf.DF1d.format(leftLWL),125,il);     
     il+=15;
     g.drawString("    - maximum",10,il);
     g.drawString(bcf.DF1d.format(rightLWL),125,il);     
     il+=15;
     g.drawString("    - length",10,il);
     g.drawString(bcf.DF1d.format(rightLWL-leftLWL),125,il);     
     il+=20;
     g.drawString("Max Section - Area",10,il);
     g.drawString(bcf.DF2d.format(hull.units.coefArea()*maxArea)+hull.units.lblArea(),125,il);     
     il+=15;
     g.drawString("            @ Station",10,il);
     g.drawString(bcf.DF1d.format(maxStn),125,il);     
     
     il = 25;
     int ic = ix - 200;
     g.drawString("Displacement",ic,il);
     g.drawString(bcf.DF1d.format(hull.units.Vol2Wgt()*hull.hVals[hull.DISP])+hull.units.lblWgt(),ic+115,il);     
     il+=15;
     g.drawString("CoB - Station",ic,il);
     g.drawString(bcf.DF1d.format(hull.hVals[hull.CX]),ic+115,il);     
     il+=15;
     g.drawString("    - Lateral",ic,il);
     g.drawString(bcf.DF1d.format(hull.hVals[hull.CY]),ic+115,il);     
     il+=15;
     g.drawString("    - Height",ic,il);
     g.drawString(bcf.DF1d.format(hull.hVals[hull.CZ]),ic+115,il);     

     il+=20;
     g.drawString("Prismatic Coeff",ic,il);
     g.drawString(bcf.DF3d.format(Cp),ic+115,il);     
     
     double num, denom;
     String dlVal,dlLbl;
     
     if (hull.units.UNITS == 0) {
        num = hull.units.Vol2Ton()*hull.hVals[hull.DISP];    
        denom = Math.pow(0.01 * (rightLWL-leftLWL)/12.0,3.0);
        dlVal = bcf.DF0d.format(num / denom);
        dlLbl = "Disp/Length Ratio:";
      }
     else if (hull.units.UNITS == 1) {
        num = hull.units.Vol2Ton()*hull.hVals[hull.DISP];    
        denom = Math.pow(0.01 * (rightLWL-leftLWL),3.0);
        dlVal = bcf.DF0d.format(num / denom);
        dlLbl = "Disp/Length Ratio:";
      }
     else {
        num = rightLWL-leftLWL;
        denom = Math.pow(hull.hVals[hull.DISP],0.33333);    
        dlVal = bcf.DF2d.format(num / denom);
        dlLbl = "Length/Disp ratio:";
      }
     il+=20;
     g.drawString(dlLbl,ic,il);
     g.drawString(dlVal,ic+115,il);     

     
     
    }// end paint
    
    }// end hdPanel
   

   
    public class hdCtrl extends JPanel{
    Dimension d ;
    
    JPanel  pnlBase;
    JSlider slBase;

    JPanel  pnlHeel;
    JSlider slHeel;

    JPanel  pnlButton;
    JToggleButton btnComp;
    JButton btnWgt;
    
    public hdCtrl(int x, int y) {
       d = new Dimension(x,y) ;
       setLayout(new FlowLayout());
       setBorder(BorderFactory.createEtchedBorder()) ;
       
       Border bcBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

       double sl_ctr = -(0.5 * ( hull.gz_max + hull.gz_min) - hull.base);
       double sl_min = sl_ctr - 0.5 * ( hull.gz_max - hull.gz_min);
       double sl_max = sl_ctr + 0.5 * ( hull.gz_max - hull.gz_min);
       
       slBase = new JSlider();
       slBase.setPreferredSize(new Dimension(250,42));

       slBase.addChangeListener(new ChangeListener()
            { public void stateChanged(ChangeEvent e)
                {double r = 4;
                 if (hull.units.UNITS == 1) r = 48;
                 else if (hull.units.UNITS == 2) r = 1;
                 else if (hull.units.UNITS == 3) r = 100;
                 hull.base = ((double) slBase.getValue()) / r;
                 hull.setLines(); 
                 hull.bChanged = true;
                 hull.calcDisp();
                 dispWgt.setWeights();
                 dispFore.repaint();  
                 dispAft.repaint();  
                 dispWL.repaint();  
                 dispWet.repaint();  
                 dispStn.repaint();  
                 disp.repaint();  
                 body.repaint();  
                 plan.repaint();  
                }
            });

       
       pnlBase = new JPanel();
       pnlBase.setBorder(BorderFactory.createTitledBorder(bcBorder,"Baseline Offset"));
       pnlBase.setLayout(new BorderLayout());
       pnlBase.add(slBase,BorderLayout.CENTER);
       add(pnlBase);

       slHeel = new JSlider(0,90,(int)hull.angHeel);
       slHeel.setPreferredSize(new Dimension(250,42));
       slHeel.setMajorTickSpacing(15);
       slHeel.setMinorTickSpacing(5);
       slHeel.setPaintTicks(true);
       slHeel.setPaintLabels(true);
       
       slHeel.addChangeListener(new ChangeListener()
            { public void stateChanged(ChangeEvent e)
                {hull.angHeel = (double) slHeel.getValue();
                 hull.calcDisp();
                 dispWgt.setWeights();
                 dispFore.repaint();  
                 dispAft.repaint();
                 dispWL.repaint();  
                 dispWet.repaint(); 
                 dispStn.repaint();  
                 disp.repaint();  
                }
            });
       
       
       pnlHeel = new JPanel();
       pnlHeel.setBorder(BorderFactory.createTitledBorder(bcBorder,"Angle of Heel"));
       pnlHeel.setLayout(new BorderLayout());
       //pnlHeel.setBackground(Color.lightGray);
       pnlHeel.add(slHeel,BorderLayout.CENTER);
       add(pnlHeel);
       
       pnlButton = new JPanel();
       pnlButton.setLayout(new FlowLayout());
       btnComp = new JToggleButton("Set Compare");
       btnComp.addActionListener(new ActionListener()
            { public void actionPerformed(ActionEvent e)
               {if (!btnComp.isSelected()) {
                     disp.bComp = false;
                     disp.repaint();
                     dispWL.repaint();
                     dispWet.repaint();
                     dispStn.repaint();  
              }
                 else{
                    for (int j=0;j<=hull.NDIV;j++){
                       disp.iComp[0][j] = disp.iCur[0][j];
                       disp.iComp[1][j] = disp.iCur[1][j];
                       dispWL.iComp[0][j] = dispWL.iCur[0][j];
                       dispWL.iComp[1][j] = dispWL.iCur[1][j];
                       dispWet.iComp[j] = dispWet.iCur[j];
                    }
                    disp.xComp = disp.xCur;
                    disp.yComp = disp.yCur;
                    disp.bComp = true;
                    disp.repaint();
                    dispWL.repaint();
                    dispWet.repaint();
                    dispStn.repaint();  
            }}
            });
       
       btnWgt = new JButton("Set Weights");
       btnWgt.addActionListener(new ActionListener()
            { public void actionPerformed(ActionEvent e)
               {wgtEdit();}
            });
       
       pnlButton.add(btnComp);
       pnlButton.add(btnWgt);
       add(pnlButton);
       
    }
        
    public Dimension getPreferredSize(){return d ; }
    

    }// end hdCtrl

    public class hdBody extends JPanel{
    Dimension d ;
    String title;
    boolean type;
    
    public hdBody(int x, int y) {
         d = new Dimension(x,y) ;
         setBackground(Color.white) ;
    }
        
    public Dimension getPreferredSize(){return d ; }
    public void setTitle(String s){title = s;}
    public void setType(boolean b){type = b;}
    
    protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     double sinang = Math.sin(Math.toRadians(hull.angHeel));
     double cosang = Math.cos(Math.toRadians(hull.angHeel));

     double mx = getWidth() ;
     double my = getHeight() ;
     int ix = (int) mx;
     int iy = (int) my;
     int xb = ix/2;
     int yb = iy/2;
     Point p1, p2;
     int u,v,w,z;
     double r = 0.85*xb/hull.gy_max;
     g.clearRect(0,0,ix,iy) ;
     g.drawString(title,10,10);

     // draw axes
     g.setColor(Color.red);
     g.drawLine(xb,5,xb,iy-5);
     g.drawLine(5,yb,ix-5,yb);
     g.setColor(Color.black);
     
     int jLow, jHigh;
     if (type){ jLow = 0; jHigh = hull.Stations.length /2;}
     else{ jLow = hull.Stations.length /2 ; jHigh = hull.Stations.length - 1;}
     for (int j=jLow;j<=jHigh;j++){
        // draw station
        Iterator si = hull.getStation(j,hull.angHeel);
        p1 = (Point) si.next();
        while(si.hasNext()){
           p2 = (Point) si.next();
           u = xb + (int) (r * p1.y);
           v = yb - (int) (r * p1.z);
           w = xb + (int) (r * p2.y);
           z = yb - (int) (r * p2.z);
           g.drawLine(u,v,w,z);
           p1 = p2;
        }
     }
     
     int jm = hull.Stations.length /2;
     boolean bDraw = (type && hull.CX <= hull.Stations[jm]) || (!type && hull.CX >= hull.Stations[jm]); 
     // put circle on computed CoG
     g.setColor(Color.red);
     u = xb + (int) (r * hull.hVals[hull.CY]);
     v = yb - (int) (r * hull.hVals[hull.CZ]);
     if (bDraw) g.drawArc(u-5,v-5,10,10,0,360);

     g.setColor(Color.blue);
     double rtw = 0;
     double rty = 0;
     double rtz = 0;
     for (int j=0; j<10;j++){
        if (hull.wgtWgt[j] >0){
           double ry = cosang * hull.wgtY[j] - sinang * (hull.wgtZ[j]+hull.base);
           double rz = sinang * hull.wgtY[j] + cosang * (hull.wgtZ[j]+hull.base);
           if ((type && hull.wgtX[j]<=hull.Stations[jm]) || (!type && hull.wgtX[j]>= hull.Stations[jm])){ 
              w = xb + (int) (r * ry);
              z = yb - (int) (r * rz);
              g.fillRect(w-3,z-3,6,6);
           }
           rtw = rtw + hull.wgtWgt[j];
           rty = rty + hull.wgtWgt[j]*ry;
           rtz = rtz + hull.wgtWgt[j]*rz;
        }}
        if (bDraw && (rtw > 0)){
           rty = rty / rtw;
           rtz = rtz / rtw;
           w = xb + (int) (r * rty);
           z = yb - (int) (r * rtz);
           g.drawArc(w-5,z-5,10,10,0,360);
           
           g.drawLine(w,z,w,v);
           g.setColor(Color.red);
           g.drawLine(u,v,w,v);
        }
     
    }// end paint
    }// end hdBody
   
    
    public class bodyPanel extends JPanel{
    Dimension d ;
    
    public bodyPanel(int x, int y) {
         d = new Dimension(x,y) ;
         setBackground(Color.white) ;
    }
        
    public Dimension getPreferredSize(){return d ; }
    
    protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Font bigFont = new Font("Serif",Font.PLAIN,12);
    Font lilFont = new Font("SansSerif",Font.PLAIN,10);
    g.setFont(bigFont);

     double my = getWidth() ;
     double mz = getHeight() ;
     int ix = (int) my ;
     int iy = (int) mz ;
     g.clearRect(0,0,ix,iy) ;
     g.drawString("Body",10,10);
     if (!hull.valid) return;
     
     Set s;     
     Iterator si;
     Line ln;
     Point p1, p2;
     YZCompare yzComp;
     double[] stn = hull.Stations;
     
     int iw, iz;
      
     int py = 2 ;
     int pz = 2 ;
     int pw = (int) my ;
     int ph = (int) mz - 4;

     int iy_min = py+5;
     int iy_max = py+pw-25 ;
     int iz_min = pz+ph-5 ;
     int iz_max = pz+5 ;
     
     g.clearRect(py, pz, pw, ph) ;
     g.drawString("Body",10,12);
     
     // note: what would usually be gy_min is replaced by -gy_max
     
     double ry, rz, r ;
     ry = (iy_max - iy_min) / (2 * hull.gy_max) ;
     rz = (iz_max - iz_min) / (hull.gz_max-hull.gz_min) ;
     r = Math.min(Math.abs(ry),Math.abs(rz)) ;
     
     iy_min = (int)(((double) (my)) / 2.0 - r * hull.gy_max);
     
     g.setColor(Color.red);
     ix = iy_min + (int) (r * (0 + hull.gy_max)) ;
     iy = iz_min - (int) (r * (hull.gz_max - hull.gz_min));
     iw = iy_min + (int) (r * (0 + hull.gy_max)) ;
     iz = iz_min - (int) (r * (hull.gz_min - hull.gz_min));
     g.drawLine(ix,iy, iw,iz);

     g.setColor(Color.blue);
     ix = iy_min + (int) (r * (2*hull.gy_max)) ;
     iy = iz_min - (int) (r * (0 - hull.gz_min));
     iw = iy_min + (int) (r * (hull.gy_min - hull.gy_min)) ;
     iz = iz_min - (int) (r * (0 - hull.gz_min));
     g.drawLine(ix,iy, iw,iz);
     g.setColor(Color.black);
     
     int j;
     int jm = stn.length /2;
     for (j=0;j < stn.length; j++){
     
     s = new HashSet();
     
     double zmin = +1000000.0;
     double zmax = -1000000.0;
     
     for (int iHL=0; iHL < hull.hLines.length;iHL++){
        double tx = stn[j];
        double x_min = hull.hLines[iHL].min("X");
        double x_max = hull.hLines[iHL].max("X");
        if (x_min <= tx && tx <= x_max) {
        double ty = hull.hLines[iHL].hXY.interp4P(tx);
        double tz = hull.hLines[iHL].hXZ.interp4P(tx);
        if (j<=jm) s.add(new Point(tx,ty,tz));
        if (j>=jm) s.add(new Point(tx,-ty,tz));
        zmin = Math.min(zmin,tz);
        zmax = Math.max(zmax,tz);
     }}
     
     
     yzComp = new YZCompare();
     yzComp.setAdj(0,zmax-0.3 * (zmax - zmin));
     SortedSet ts = new TreeSet(yzComp);
     si = s.iterator();
     while (si.hasNext()) ts.add(si.next());
     si = ts.iterator();
     p1 = (Point) si.next();
 
     if( p1.z <= 0){
     ix = iy_min + (int) (r * (   0 + hull.gy_max)) ;
     iy = iz_min - (int) (r * (p1.z - hull.gz_min));
     iw = iy_min + (int) (r * (p1.y + hull.gy_max)) ;
     iz = iz_min - (int) (r * (p1.z - hull.gz_min));
     g.drawLine(ix,iy,iw,iz);
     }
          
     while (si.hasNext()){
     p2 = (Point) si.next();
     ix = iy_min + (int) (r * (p1.y + hull.gy_max)) ;
     iy = iz_min - (int) (r * (p1.z - hull.gz_min));
     iw = iy_min + (int) (r * (p2.y + hull.gy_max)) ;
     iz = iz_min - (int) (r * (p2.z - hull.gz_min));
     g.drawLine(ix,iy,iw,iz);
     p1 = p2;
     }
     if( p1.z <= 0){
     ix = iy_min + (int) (r * (p1.y + hull.gy_max)) ;
     iy = iz_min - (int) (r * (p1.z - hull.gz_min));
     iw = iy_min + (int) (r * (   0 + hull.gy_max)) ;
     iz = iz_min - (int) (r * (p1.z - hull.gz_min));
     g.drawLine(ix,iy,iw,iz);
     }
     }
   
   

    }// end paint
    }// end bodyPanel

    public class planPanel extends JPanel{
    Dimension d ;
    
    public planPanel(int x, int y) {
         d = new Dimension(x,y) ;
         setBackground(Color.white) ;
    }
        
    public Dimension getPreferredSize(){return d ; }
    
    protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Font bigFont = new Font("Serif",Font.PLAIN,12);
    Font lilFont = new Font("SansSerif",Font.PLAIN,10);
    g.setFont(bigFont);
    
     double mx = getWidth() ;
     double my = getHeight() ;
     int px = 0 ;
     int py = 2 ;
     int pw = (int) mx ;
     int ph = (int) my/2 - 4;
     
     g.clearRect(px,py, pw, ph) ;
     g.drawString("Plan",px+10,py+15);

     g.drawString("Length: "+bcf.DF1d.format(hull.gx_max-hull.gx_min),pw-100,py+15);
     g.drawString("Beam: "+bcf.DF1d.format(2.0*hull.gy_max),pw-100,py+30);
     g.drawString("Depth: "+bcf.DF1d.format(hull.gz_max-hull.gz_min),pw-100,py+45);
     
     g.clearRect(0,(int)my/2 + 4, (int) mx, (int) my/2 - 4) ;
     g.drawString("Profile",10,(int) my/2 + 15);

     if (!hull.valid) return;
     
     int i, ix, iy, iw, iz;
     int iHL;
     double x, y, x_min, x_max;
     //Line ch;
     //Iterator listLine;
     
     double[] stn = hull.Stations;
    
    g.setFont(lilFont);
    
     // draw plan view
     int ix_min = px+100;
     int ix_max = px+pw-25 ;
     int iy_min = py+ph-15 ;
     int iy_max = py+5 ;
     int n = stn.length ;
     int n1 = n - 1;
     
     double rx, ry, r ;
     rx = (ix_max - ix_min) / (hull.gx_max - hull.gx_min) ;
     ry = Math.abs(iy_max - iy_min) / Math.max((hull.gy_max - hull.gy_min),(hull.gz_max-hull.gz_min)) ;
     r = Math.min(Math.abs(rx),Math.abs(ry)) ;
     
     ix = ix_min + (int) (r * (stn[0] - hull.gx_min)) ;
     iy = iy_min ;
     iw = ix_min + (int) (r * (stn[n1] - hull.gx_min)) ;
     iz = iy_min ;
     g.setColor(Color.blue);
     g.drawLine(ix,iy, iw,iz);
     g.drawString("0.0",ix-25,iy);
     for (int ic=0;ic<stn.length;ic++){
     ix = ix_min + (int) (r * (stn[ic] - hull.gx_min)) ;
     g.drawLine(ix,iy-5,ix,iy+5);
     g.drawString(bcf.DF2d.format(stn[ic]),ix+1,iy+12);
     }
    
     //draw vertical axis
     double vmin, vmax;
     double vinc = 12.0;
     if (hull.units.UNITS == 1) vinc = 1.0;
     else if (hull.units.UNITS == 2) vinc = 10.0;
     else if (hull.units.UNITS == 3) vinc = 0.1;
     vmax =   vinc * Math.ceil(hull.gy_max/vinc);
     while (vmax / vinc > 4.0) vinc = 2.0 * vinc;
     ix = ix_min + (int) (r * (stn[0] - hull.gx_min)) ;
     iy = iy_min - (int) (r * (0)) ;
     iw = ix;
     iz = iy_min - (int) (r * (vmax - 0)) ;
     g.drawLine(ix,iy,iw,iz);
     ix = ix - 5;     
     for (double vidx = vinc; vidx <= vmax; vidx+=vinc){
     iy = iy_min - (int) (r * (vidx - 0)) ;
     iz = iy;
     g.drawLine(ix,iy,iw,iz);
     g.drawString(Double.toString(vidx),ix-25,iy);
    }
     
     g.setColor(Color.black);

     for (iHL=0;iHL<hull.hLines.length;iHL++){
        x_min = hull.hLines[iHL].min("X");
        x_max = hull.hLines[iHL].max("X");

        x = x_min; 
        y = hull.hLines[iHL].hXY.interp4P(x);
        ix = ix_min + (int) (r * (x - hull.gx_min)) ;
        iy = iy_min - (int) (r * (y - 0)) ;
        for (double pct=0.05; pct < 1.005; pct+=0.05){
          x = x_min + pct * (x_max - x_min);
          y = hull.hLines[iHL].hXY.interp4P(x);
          iw = ix_min + (int) (r * (x - hull.gx_min)) ;
          iz = iy_min - (int) (r * (y - 0)) ;
          g.drawLine(ix,iy, iw,iz);
          ix = iw;
          iy = iz;
        }        
     }
     
     // draw stems
     g.setColor(Color.lightGray);
     for (int iSL=0;iSL<=1;iSL++){
        if (hull.bStems[iSL] && hull.sLines[iSL].valid){
        x = hull.sLines[iSL].hPoints[0].getX(); 
        y = hull.sLines[iSL].hPoints[0].getY(); 
        ix = ix_min + (int) (r * (x - hull.gx_min)) ;
        iy = iy_min - (int) (r * (y - 0)) ;
        for (int j=1; j<hull.sLines[iSL].hPoints.length;j++){
          x = hull.sLines[iSL].hPoints[j].getX(); 
          y = hull.sLines[iSL].hPoints[j].getY(); 
          iw = ix_min + (int) (r * (x - hull.gx_min)) ;
          iz = iy_min - (int) (r * (y - 0)) ;
          g.drawLine(ix,iy, iw,iz);
          ix = iw;
          iy = iz;
        }        
     }}   
     g.setColor(Color.black);
     
     // draw profile view 
     
     px = 0 ;
     py = (int) my/2 + 4 ;
     pw = (int) mx ;
     ph = (int) my/2 - 4 ;
     
     ix_min = px+100;
     ix_max = px+pw-25 ;
     iy_min = py+ph-15 ;
     iy_max = py+5 ;
     
     // x-axis
     ix = ix_min + (int) (r * (stn[0] - hull.gx_min)) ;
     iy = iy_min - (int) (r * (0  - hull.gz_min));
     iw = ix_min + (int) (r * (stn[n1] - hull.gx_min)) ;
     iz = iy ;
     
     g.setColor(Color.blue);
     g.drawLine(ix,iy, iw,iz);
     for (int ic=0;ic<stn.length;ic++){
     ix = ix_min + (int) (r * (stn[ic] - hull.gx_min)) ;
     g.drawLine(ix,iy-5,ix,iy+5);}
     
     // y-axis
     vinc = 12.0;
     if (hull.units.UNITS == 1) vinc = 1.0;
     else if (hull.units.UNITS == 2) vinc = 10.0;
     else if (hull.units.UNITS == 3) vinc = 0.1;
     vmax =   vinc * Math.ceil(hull.gz_max/vinc);
     vmin =   vinc * Math.floor(hull.gz_min/vinc);
     while ((vmax - vmin) / vinc > 4.0) vinc = 2.0 * vinc;
     ix = ix_min + (int) (r * (stn[0] - hull.gx_min)) ;
     //iy = iy_min - (int) (r * (vmin - hull.gz_min)) ;
     iy = iy_min ;
     iw = ix;
     iz = iy_min - (int) (r * (vmax - hull.gz_min)) ;
     g.drawLine(ix,iy,iw,iz);
     ix = ix - 5;     
     for (double vidx = vmin; vidx <= vmax; vidx+=vinc){
     iy = iy_min - (int) (r * (vidx - hull.gz_min)) ;
     iz = iy;
     if (iy <= iy_min) {
     g.drawLine(ix,iy,iw,iz);
     g.drawString(Double.toString(vidx),ix-25,iy);}
    }
     
     g.setColor(Color.black);

     for (iHL=0;iHL<hull.hLines.length;iHL++){
        x_min = hull.hLines[iHL].min("X");
        x_max = hull.hLines[iHL].max("X");
        x = x_min; 
        y = hull.hLines[iHL].hXZ.interp4P(x);
        ix = ix_min + (int) (r * (x - hull.gx_min)) ;
        iy = iy_min - (int) (r * (y - hull.gz_min)) ;
        for (double pct=0.05; pct < 1.005; pct+=0.05){
          x = x_min + pct * (x_max - x_min);
          y = hull.hLines[iHL].hXZ.interp4P(x);
          iw = ix_min + (int) (r * (x - hull.gx_min)) ;
          iz = iy_min - (int) (r * (y - hull.gz_min)) ;
          g.drawLine(ix,iy, iw,iz);
          ix = iw;
          iy = iz;
        }        
     }   
     // draw stems
     g.setColor(Color.lightGray);
     for (int iSL=0;iSL<=1;iSL++){
        if (hull.bStems[iSL] && hull.sLines[iSL].valid){
        x = hull.sLines[iSL].hPoints[0].getX(); 
        y = hull.sLines[iSL].hPoints[0].getZ(); 
        ix = ix_min + (int) (r * (x - hull.gx_min)) ;
        iy = iy_min - (int) (r * (y - hull.gz_min)) ;
        for (int j=1; j<hull.sLines[iSL].hPoints.length;j++){
          x = hull.sLines[iSL].hPoints[j].getX(); 
          y = hull.sLines[iSL].hPoints[j].getZ(); 
          iw = ix_min + (int) (r * (x - hull.gx_min)) ;
          iz = iy_min - (int) (r * (y - hull.gz_min)) ;
          g.drawLine(ix,iy, iw,iz);
          ix = iw;
          iy = iz;
        }        
     }}   
     g.setColor(Color.black);
     
    }//end paint
    }//end planPanel

    public class ctrlPanel extends JPanel{
    Dimension d ;
    JLabel lblName;
    JLabel lblNA;
    h2oPanel hP;
    unitPanel uP;
    stmPanel sP;
    
    public ctrlPanel(int x, int y) {
         Font cpFont = new Font("Serif", Font.BOLD, 14);
         d = new Dimension(x,y) ;
         setBorder(BorderFactory.createEtchedBorder()) ;
         setLayout(new FlowLayout());
         lblName = new JLabel("name here");
         lblName.setFont(cpFont);
         lblName.setHorizontalAlignment(SwingConstants.LEFT);

         lblNA = new JLabel("n/a");
         lblNA.setFont(cpFont);
         lblNA.setHorizontalAlignment(SwingConstants.RIGHT);
         
         hP = new h2oPanel(170,35);
         sP = new stmPanel(210,35);
         uP = new unitPanel(380,35);
         add(lblName);
         add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));

         add(lblNA);
         add(hP);
         add(sP);
         add(uP);
    }

    public Dimension getPreferredSize(){return d ; }
    
    }// end ctrlPanel


    public class unitPanel extends JPanel implements ActionListener{
    Dimension d ;
    JLabel lblUnit;
    JRadioButton btnInLbs;
    JRadioButton btnFtLbs;
    JRadioButton btnCmKg;
    JRadioButton btnMKg;
    ButtonGroup bGrp;
    
    public unitPanel(int x, int y) {
       d = new Dimension(x,y) ;
       lblUnit = new JLabel("Units:");
       btnInLbs = new JRadioButton("in,lbs");
       btnInLbs.setSelected(true);
       btnFtLbs = new JRadioButton("ft,lbs");
       btnCmKg = new JRadioButton("cm,Kg");
       btnMKg = new JRadioButton("m,Kg");
       
       btnInLbs.addActionListener(this);
       btnFtLbs.addActionListener(this);
       btnCmKg.addActionListener(this);
       btnMKg.addActionListener(this);
       
       bGrp = new ButtonGroup();
       bGrp.add(btnInLbs);
       bGrp.add(btnFtLbs);
       bGrp.add(btnCmKg);
       bGrp.add(btnMKg);

       setBorder(BorderFactory.createEtchedBorder()) ;
       setLayout(new FlowLayout());
       add(lblUnit);
       add(btnInLbs);
       add(btnFtLbs);
       add(btnCmKg);
       add(btnMKg);
    }

    public Dimension getPreferredSize(){return d ; }

    public void actionPerformed(ActionEvent e){
       if (btnInLbs.isSelected()) hull.units.UNITS = 0;    
       if (btnFtLbs.isSelected()) hull.units.UNITS = 1;    
       if (btnCmKg.isSelected()) hull.units.UNITS = 2 ;    
       if (btnMKg.isSelected()) hull.units.UNITS = 3;
       setCtrls();
    }
    }// end unitPanel

    
    public class stmPanel extends JPanel implements ActionListener{
    Dimension d;
    JLabel lblStem;
    JRadioButton btnLeft;
    JRadioButton btnRight;

    public stmPanel(int x, int y) {
       d = new Dimension(x,y) ;
       lblStem = new JLabel("Auto Stem:");
       btnLeft = new JRadioButton("Left");
       btnLeft.setSelected(true);
       btnRight = new JRadioButton("Right");
       btnRight.setSelected(true);
       
       btnLeft.addActionListener(this);
       btnRight.addActionListener(this);

       setBorder(BorderFactory.createEtchedBorder()) ;
       setLayout(new FlowLayout());
       add(lblStem);
       add(btnLeft);
       add(btnRight);
    }

    public Dimension getPreferredSize(){return d ; }

    public void actionPerformed(ActionEvent e){
       if (btnLeft.isSelected()) hull.bStems[0] = true;
       else hull.bStems[0] = false;
       if (btnRight.isSelected()) hull.bStems[1] = true;
       else hull.bStems[1] = false;
       hull.bChanged = true;
       body.repaint();  
       plan.repaint();  
    }
    
    }// end stmPanel
    
    public class h2oPanel extends JPanel implements ActionListener{
    Dimension d ;
    JLabel lblH2O;
    JRadioButton btnSalt;
    JRadioButton btnFresh;
    ButtonGroup bGrp;
    
    public h2oPanel(int x, int y) {
       d = new Dimension(x,y) ;
       lblH2O = new JLabel("Water:");
       btnSalt = new JRadioButton("salt");
       btnSalt.setSelected(true);
       btnFresh = new JRadioButton("fresh");
       
       btnSalt.addActionListener(this);
       btnFresh.addActionListener(this);
       
       bGrp = new ButtonGroup();
       bGrp.add(btnSalt);
       bGrp.add(btnFresh);

       setBorder(BorderFactory.createEtchedBorder()) ;
       setLayout(new FlowLayout());
       add(lblH2O);
       add(btnSalt);
       add(btnFresh);
    }

    public Dimension getPreferredSize(){return d ; }

    public void actionPerformed(ActionEvent e){
       if (btnSalt.isSelected()) hull.units.WATER = 0;    
       if (btnFresh.isSelected()) hull.units.WATER = 1;
       hull.bChanged = true;
       body.repaint();  
       plan.repaint();  
    } 
    }// end h2oPanel


   public void getHull(){
      String fn;
      int returnVal;
      
  	   try{
        hull = new Hull();
        
        //ff = new bcFileFilter(new String[] {"xml", "hul"}, "boatCalc and Hulls files");
        //fc.setFileFilter(ff);
        returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION){
           fn = (fc.getSelectedFile().getName()).toLowerCase();
           if (fn.indexOf(".hul") > 0 ) hull.getHulls(fc.getSelectedFile());
           else hull.getData(fc.getSelectedFile());
           if (!bOpen) setCtrls();
        }
        }
      catch (NullPointerException npe){System.out.println(npe);}
   }// end getHull
   
   public void saveHull(){
      String fn;
      int returnVal;
      
  	   try{
        //ff = new bcFileFilter(new String[] {"xml", "hul"}, "boatCalc and Hulls files");
        //fc.setFileFilter(ff);
        returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION){
           fn = (fc.getSelectedFile().getName()).toLowerCase();
           if (fn.indexOf(".hul") > 0 ) hull.saveHulls(fc.getSelectedFile());
           else hull.saveData(fc.getSelectedFile());
        }
        }
      catch (NullPointerException npe){System.out.println(npe);}
   
   }
   
   public void setCtrls(){
        double sl_ctr = -(0.5 * ( hull.gz_max + hull.gz_min) - hull.base);
        double sl_min = sl_ctr - 0.5 * ( hull.gz_max - hull.gz_min);
        double sl_max = sl_ctr + 0.5 * ( hull.gz_max - hull.gz_min);
        int itic, dinc;
        
        if (hull.base < sl_min || hull.base > sl_max){
          hull.base = sl_ctr;
          JOptionPane.showMessageDialog(null,"Setting baseline offset.","Warning!", JOptionPane.ERROR_MESSAGE);
        }
        
        if (hull.units.UNITS == 0){ 
           sl_min = 12.0 * Math.floor(sl_min/12.0);
           sl_max = 12.0 * Math.ceil(sl_max/12.0);

           itic = 4;
           dinc = 48;
           if ((sl_max - sl_min) > 54.0) {
              itic = 8;
              dinc = 96;}
           dispCtrl.slBase.setModel(
           new DefaultBoundedRangeModel((int)(4.0*hull.base),0,4*(int)sl_min,4*(int)sl_max));
           dispCtrl.slBase.setMajorTickSpacing(48);
           dispCtrl.slBase.setMinorTickSpacing(itic);
        
           //Create the label table
           Hashtable labelTable = new Hashtable();
           for (double d = 4*sl_min; d<=4*sl_max+0.5 ; d+=dinc){
              labelTable.put( new Integer ((int) d), new JLabel(Double.toString(d / 4)) );}   
              dispCtrl.slBase.setLabelTable( labelTable );
        }
        
        else if (hull.units.UNITS == 1){
           sl_min = Math.floor(sl_min);
           sl_max = Math.max(Math.ceil(sl_max),sl_min+1);
           
           itic = 4;
           dinc = 48;
           if ((sl_max - sl_min) > 72) {itic = 128; dinc = 1536;}
           else if ((sl_max - sl_min) > 36) {itic = 64; dinc = 768;}
           else if ((sl_max - sl_min) > 18) {itic = 32; dinc = 384;}
           else if ((sl_max - sl_min) > 9.0) {itic = 16; dinc = 192;}
           else if ((sl_max - sl_min) > 4.5) {itic = 8; dinc = 96;}
           dispCtrl.slBase.setModel(
           new DefaultBoundedRangeModel((int)(48.0*hull.base),0,48*(int)sl_min,48*(int)sl_max));
           dispCtrl.slBase.setMajorTickSpacing(12 * itic);
           dispCtrl.slBase.setMinorTickSpacing(itic);
        
           //Create the label table
           Hashtable labelTable = new Hashtable();
           for (double d = 48*sl_min; d<=48*sl_max+0.5 ; d+=dinc){
              labelTable.put( new Integer ((int) d), new JLabel(Double.toString(d / 48)) );}   
              dispCtrl.slBase.setLabelTable( labelTable );

           }
           
        else if (hull.units.UNITS == 2){ 
           sl_min = 10.0 * Math.floor(sl_min/10.0);
           sl_max = 10.0 * Math.ceil(sl_max/10.0);
           
           itic = 1;
           dinc = 50;
           
           dispCtrl.slBase.setModel(
           new DefaultBoundedRangeModel((int)hull.base,0,(int)sl_min,(int)sl_max));
           dispCtrl.slBase.setMajorTickSpacing(10);
           dispCtrl.slBase.setMinorTickSpacing(itic);
        
           //Create the label table
           Hashtable labelTable = new Hashtable();
           for (double d = sl_min; d<=sl_max+0.5 ; d+=dinc){
              labelTable.put( new Integer ((int) d), new JLabel(Double.toString(d)) );}   
              dispCtrl.slBase.setLabelTable( labelTable );

           }
        
        else if (hull.units.UNITS == 3){
           sl_min = 100 * Math.floor(sl_min);
           sl_max = Math.max(100 * Math.ceil(sl_max),sl_min+1.0);
        
           boolean OK = true;
           
           while (OK && (sl_min < sl_max)){
           try{          
               itic = 10;
               dinc = 50;
               if ((sl_max - sl_min) > 12.0) {itic = 200;dinc = 400;}
               else if ((sl_max - sl_min) > 6.0) {itic = 100;dinc = 200;}
               else if ((sl_max - sl_min) > 3.0) {itic = 50; dinc = 100;}
              
           dispCtrl.slBase.setModel(
           new DefaultBoundedRangeModel((int)(100.0*hull.base),0,(int)sl_min,(int)sl_max));
           dispCtrl.slBase.setMajorTickSpacing(10 * itic);
           dispCtrl.slBase.setMinorTickSpacing(itic);
        
           //Create the label table
           Hashtable labelTable = new Hashtable();
           for (double d = sl_min; d<=sl_max+0.5 ; d+=dinc){
              labelTable.put( new Integer ((int) d), new JLabel(Double.toString(d/100.0)) );}   
              dispCtrl.slBase.setLabelTable( labelTable );
           OK = false;   
              
           } 
          catch (IllegalArgumentException iae){
            sl_min = sl_min + 100;
            sl_max = sl_max - 100;
            System.out.println("slider limits: "+sl_min+" "+sl_max);}
           }// end while

        }
           
        dispCtrl.slBase.setPaintTicks(true);
        dispCtrl.slBase.setPaintLabels(true);
        dispCtrl.slBase.revalidate();
        dispCtrl.slBase.repaint();
        
        ctrl.lblName.setText("Design: "+hull.boatname);
        ctrl.lblNA.setText("Designer: "+hull.designer);
        
        if (hull.units.WATER == 0) ctrl.hP.btnSalt.setSelected(true); 
        if (hull.units.WATER == 1) ctrl.hP.btnFresh.setSelected(true); 

        if (hull.units.UNITS == 0) ctrl.uP.btnInLbs.setSelected(true); 
        if (hull.units.UNITS == 1) ctrl.uP.btnFtLbs.setSelected(true); 
        if (hull.units.UNITS == 2) ctrl.uP.btnCmKg.setSelected(true); 
        if (hull.units.UNITS == 3) ctrl.uP.btnMKg.setSelected(true); 

        ctrl.sP.btnLeft.setSelected(hull.bStems[0]); 
        ctrl.sP.btnRight.setSelected(hull.bStems[1]);
        
   }// end setCtrls

   
   
   
   
   
   class pnlCenterboard extends JPanel{
      JTabbedPane pDisp;
      cbDraw pDraw;
      cbSpec pSpec;
      cbData pData;
      cbArea pRpt;
      Centerboard pCb;
      Border bCb;
      cbTabOrder to;
      boolean cbChange;
      
   public pnlCenterboard(){

      if (hull.board.valid) pCb = (Centerboard) hull.board.clone();
      else pCb = new Centerboard();
      pCb.setBase(hull.base);
      cbChange = false;
      
      bCb = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
      setLayout(new BorderLayout());
      
      pDisp = new JTabbedPane();
      
      pDraw = new cbDraw(750,325);
      pDraw.setBackground(Color.white) ;
      pDraw.setBorder(bCb);
      pDisp.add(pDraw,"Drawing");
      
      pSpec = new cbSpec(750,325);
      pSpec.setBackground(Color.white) ;
      pSpec.setBorder(bCb);
      pDisp.add(pSpec,"Dimensions");
      
      add(pDisp,BorderLayout.CENTER);

      pRpt = new cbArea(180,200);
      add(pRpt,BorderLayout.LINE_END);


      pData = new cbData(750,185);
      pData.setBorder(bCb);
      add(pData,BorderLayout.PAGE_END);

      //to = new cbTabOrder(this);
      //f_board.setFocusTraversalPolicy(to);

      f_board.addWindowListener(new WindowAdapter()
      {public void windowClosing(WindowEvent e){
            if(cbChange){  
            int n = JOptionPane.showConfirmDialog(f_sailplan,"Data has changed. Do you wish to apply changes?","Sailplan Design",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){saveCenterboard();}}
            f_board.setVisible(false);}}); 

      pDraw.repaint();
      pSpec.repaint();
      pRpt.setTable();
   
   }// end constructor

   public void saveCenterboard(){
     hull.board = (Centerboard) pCb.clone();
     hull.bChanged = true;
   }
   
   public class cbTabOrder extends FocusTraversalPolicy{
   cbData r;
   public cbTabOrder(pnlCenterboard p){
      pnlCenterboard psp = p;
      r = p.pData;
   }
   public Component getComponentAfter(Container focusCycleRoot, Component aComponent){
   return aComponent;
   }
   public Component getComponentBefore(Container focusCycleRoot, Component aComponent){
   return aComponent;
   }
   public Component getLastComponent(Container focusCycleRoot){
   return r;   }

   public Component getFirstComponent(Container focusCycleRoot){
   return r;   }

   public Component getDefaultComponent(Container focusCycleRoot){
   return r;}
   }// end cbTabOrder

   class cbSpec extends JPanel{
   Dimension d;
   public cbSpec(int x, int y){
      d = new Dimension(x,y);
   }
   public Dimension getPreferredSize(){return d ; }
   
   protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int iL = 20;
    int iC1 = 20;
    int iC2 = 80;
    int iC3 = 250;
    int iC4 = 325;
    int kL = 0;
    if (pCb.board.use){
       int jL = 0;
       g.drawString("Centerboard",iC1,iL);
       jL = 20;
       g.drawString("Area: ",iC1,iL+jL);
       g.drawString(bcf.DF1d.format(pCb.board.getArea()),iC2,iL+jL);
       jL = jL + 20;
       g.drawString("CoA: ",iC1,iL+jL);
       g.drawString(bcf.DF2d.format(pCb.board.getAreaX())+", "+bcf.DF2d.format(pCb.board.getAreaY()),iC2,iL+jL);
       jL = jL + 20;
       g.drawString("Points: ",iC1,iL+jL);
       for (int i=0;i<4;i++){
          g.drawString(bcf.DF2d.format(pCb.board.getParamX(i))+", "+bcf.DF2d.format(pCb.board.getParamY(i)),iC2,iL+jL);
          jL = jL + 20;
       }
       g.drawString("ref: baseline",iC2+90,iL+jL-20);
       kL = jL;
       jL = 0;
       g.drawString("Immersed Portion",iC3,iL);
       jL = 20;
       g.drawString("Area: ",iC3,iL+jL);
       g.drawString(bcf.DF1d.format(pCb.board.getWetArea()),iC4,iL+jL);
       jL = jL + 20;
       g.drawString("CoA: ",iC3,iL+jL);
       g.drawString(bcf.DF2d.format(pCb.board.getWetX())+", "+bcf.DF2d.format(pCb.board.getWetY()),iC4,iL+jL);
       jL = jL + 20;
       g.drawString("Points: ",iC3,iL+jL);
    
       SortedSet wp = pCb.board.getWetPts();
       Iterator pi = wp.iterator();
       while (pi.hasNext()){
          Point p = (Point) pi.next();
          g.drawString(bcf.DF2d.format(p.x)+", "+bcf.DF2d.format(p.z),iC4,iL+jL);
          jL = jL + 20;
         }
         g.drawString("ref: lwl",iC4+90,iL+jL-20);
         kL = Math.max(kL,jL);
    }
    iL = iL+kL;

    
   }// end paintComponent    
   }// ends cbSpec
   
   class cbDraw extends JPanel{
   Dimension d;
   public cbDraw(int x, int y){
      d = new Dimension(x,y);
   }
   public Dimension getPreferredSize(){return d ; }
   
   protected void paintComponent(Graphics g) {
    super.paintComponent(g);
     
     double mx = getWidth() ;
     double my = getHeight() ;
     int ix = (int) mx;
     int iy = (int) my;
     int xb = (int) 50;
     int yb = (int) my / 2;
     int iu, iv, iw, iz;
     
     g.clearRect(0,0,ix,iy) ;
     g.drawString("Centerboard",10,20);
     g.setColor(Color.red);  g.drawLine(100,12,125,12);
     g.setColor(Color.blue); g.drawLine(100,17,125,17);
     g.setColor(Color.black);
     g.setColor(Color.black);
     
     if (!hull.valid) return;
     
     double rx = (mx - 100.0)/(hull.gx_max - hull.gx_min);
     double ry = (my - 75.0)/(hull.gy_max - hull.gy_min);
     double r = Math.min(rx,ry);
     
          // draw waterline
          iu = xb + (int) (r * (hull.gx_min - hull.gx_min));
          iv = yb - (int) (r * (0.0 - hull.gz_min));
          iw = xb + (int) (r * (hull.gx_max - hull.gx_min));
          iz = yb - (int) (r * (0.0 - hull.gz_min));
          g.setColor(Color.blue);
          g.drawLine(iu,iv,iw,iz);
     
     // draw hull profile
     g.setColor(Color.black);
     double z1Lo = hull.gz_max; 
     double z1Hi = hull.gz_min; 
     double x1 = 0;
     
     double[] minHull = new double[101];
     int idx = -1;
     
     for (double pct=0.0; pct<=1.0025; pct=pct+0.01){
        double x = hull.gx_min + pct * (hull.gx_max - hull.gx_min);
        SortedSet ss = hull.getStnSet(x,0.0);
        Iterator si = ss.iterator();
        double zLo = hull.gz_max;
        double zHi = hull.gz_min;
        boolean bOk = false;
        while(si.hasNext()){
           Point p = (Point) si.next();
           zLo = Math.min(zLo, p.z);
           zHi = Math.max(zHi, p.z);
           bOk = true;
           }
       if (bOk && pct > 0.0){
          iu = xb + (int) (r * (x1 - hull.gx_min));
          iv = yb - (int) (r * (z1Lo - hull.gz_min));
          iw = xb + (int) (r * (x - hull.gx_min));
          iz = yb - (int) (r * (zLo - hull.gz_min));
          g.drawLine(iu,iv,iw,iz);
          iv = yb - (int) (r * (z1Hi - hull.gz_min));
          iz = yb - (int) (r * (zHi - hull.gz_min));
          g.drawLine(iu,iv,iw,iz);
          }
          
       x1 = x;    
       z1Lo = zLo;
       z1Hi = zHi;
       
       idx++;
       minHull[idx] = Math.min(zLo,0.0) - hull.base;
     
     }
     pCb.setMinHull(minHull,hull.gx_min,hull.gx_max,100);
     
     // draw stems
     g.setColor(Color.lightGray);
     for (int iSL=0;iSL<=1;iSL++){
        if (hull.bStems[iSL] && hull.sLines[iSL].valid){
        double x = hull.sLines[iSL].hPoints[0].getX(); 
        double y = hull.sLines[iSL].hPoints[0].getZ(); 
        iu = xb + (int) (r * (x - hull.gx_min)) ;
        iv = yb - (int) (r * (y - hull.gz_min)) ;
        for (int j=1; j<hull.sLines[iSL].hPoints.length;j++){
          x = hull.sLines[iSL].hPoints[j].getX(); 
          y = hull.sLines[iSL].hPoints[j].getZ(); 
          iw = xb + (int) (r * (x - hull.gx_min)) ;
          iz = yb - (int) (r * (y - hull.gz_min)) ;
          g.drawLine(iu,iv,iw,iz);
          iu = iw;
          iv = iz;
        }        
     }}
    
    // draw board
    if (pCb.board.use){
       
      // draw pivot point   
     
     g.setColor(Color.red);
     double xp = pCb.getPivotX();
     double yp = pCb.getPivotZ()+hull.base;
     iu = xb + (int) (r * (xp - hull.gx_min)) ;
     iv = yb - (int) (r * (yp - hull.gz_min)) ;
     g.drawLine(iu+6,iv,iu-6,iv);
     g.drawLine(iu,iv+6,iu,iv-6);
       
    g.setColor(Color.lightGray);
    iu = xb + (int) (r * (pCb.getRX(0) - hull.gx_min)) ;
    iv = yb - (int) (r * (pCb.getRZ(0) - hull.gz_min)) ;
    iw = xb + (int) (r * (pCb.getRX(1) - hull.gx_min)) ;
    iz = yb - (int) (r * (pCb.getRZ(1) - hull.gz_min)) ;
    g.drawLine(iu,iv,iw,iz);
    
    iu = xb + (int) (r * (pCb.getRX(2) - hull.gx_min)) ;
    iv = yb - (int) (r * (pCb.getRZ(2) - hull.gz_min)) ;
    g.drawLine(iw,iz,iu,iv);
    
    iw = xb + (int) (r * (pCb.getRX(3) - hull.gx_min)) ;
    iz = yb - (int) (r * (pCb.getRZ(3) - hull.gz_min)) ;
    g.drawLine(iu,iv,iw,iz);
    
    iu = xb + (int) (r * (pCb.getRX(0) - hull.gx_min)) ;
    iv = yb - (int) (r * (pCb.getRZ(0) - hull.gz_min)) ;
    g.drawLine(iw,iz,iu,iv);
    
    // draw wet board
    
     //double testx = 7.11;
     //double testpct = 100.0*(testx-hull.gx_min)/(hull.gx_max-hull.gx_min);
     //int testint =     Math.min(Math.max((int) testpct,0),99);
     //System.out.println(testx+" "+testpct+" "+testint+" "+minHull[testint]);

    //System.out.println("before");
    if (pCb.board.getWetArea() > 0) {
   //System.out.println("in");
        
    g.setColor(Color.red);
    SortedSet wp = pCb.board.getWetPts();
    Iterator pi = wp.iterator();
    if (pi.hasNext()){
    Point p0 = (Point) pi.next();
    Point p1 = new Point(p0);
    while (pi.hasNext()){
	    //System.out.println("Point");
    
      Point p2 = (Point)pi.next();
      iu = xb + (int) (r * (p1.x - hull.gx_min)) ;
      iv = yb - (int) (r * (p1.z - hull.gz_min)) ;
      iw = xb + (int) (r * (p2.x - hull.gx_min)) ;
      iz = yb - (int) (r * (p2.z - hull.gz_min)) ;
      g.drawLine(iu,iv,iw,iz);
      p1 = p2;
    }
      iu = xb + (int) (r * (p1.x - hull.gx_min)) ;
      iv = yb - (int) (r * (p1.z - hull.gz_min)) ;
      iw = xb + (int) (r * (p0.x - hull.gx_min)) ;
      iz = yb - (int) (r * (p0.z - hull.gz_min)) ;
      //System.out.println("line");
    
      g.drawLine(iu,iv,iw,iz);
    }
    int cx = xb + (int) (r * (pCb.board.getWetX() - hull.gx_min)) ;
    int cy = yb - (int) (r * (pCb.board.getWetY() - hull.gz_min)) ;
    g.drawArc(cx-5,cy-5,10,10,0,360);
    g.drawLine(cx+5,cy,cx-5,cy);
    g.drawLine(cx,cy+5,cx,cy-5);
    }
    }// end if use board
    
    /*
    g.setColor(Color.black);

    double tA = 0;
    double tX = 0;
    double tY = 0;
    if (pCb.board.use){tA = tA + pCb.board.getWetArea();
                         tX = tX + pCb.board.getWetArea() * pCb.board.getWetX();
                         tY = tY + pCb.board.getWetArea() * pCb.board.getWetY();}

    if (tA > 0){
        tX = tX / tA;
        tY = tY / tA;
        int cx = xb + (int) (r * (tX - hull.gx_min));
        int cy = yb - (int) (r * (tY - hull.gz_min));
        g.drawLine(cx+4,cy+4,cx-4,cy+4);
        g.drawLine(cx+4,cy-4,cx-4,cy-4);
        g.drawLine(cx-4,cy-4,cx-4,cy+4);
        g.drawLine(cx+4,cy-4,cx+4,cy+4);
        g.drawLine(cx+6,cy,cx-6,cy);
      
      g.drawLine(cx,cy+6,cx,cy-6);
     }
     */

    
   } // end paintComponent  
   
   }// end cbDraw

   class cbArea extends JPanel{
   Dimension d;
   JLabel jlRArea, jlRCoA;   
   JLabel jlSArea, jlSCoA; 
   JLabel jlTArea, jlTCoA; 
   JButton jbApply, jbClose;

      public cbArea(int x, int y){
      d = new Dimension(x,y);
      setLayout(new GridLayout(0,2));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new bcLabel("Immersed Areas",JLabel.LEFT));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));

      add(new bcLabel("Centerboard - Area: ",JLabel.RIGHT));
      add(jlRArea = new JLabel("0.0",JLabel.LEFT));
      
      add(new bcLabel("CoA: ",JLabel.RIGHT));
      add(jlRCoA = new JLabel("0.0,0.0",JLabel.LEFT));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new bcLabel("Skeg - Area: ",JLabel.RIGHT));
      add(jlSArea = new JLabel("0.0",JLabel.LEFT));
      add(new bcLabel("CoA: ",JLabel.RIGHT));
      add(jlSCoA = new JLabel("0.0,0.0",JLabel.LEFT));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new bcLabel("Total - Area: ",JLabel.RIGHT));
      add(jlTArea = new JLabel("0.0",JLabel.LEFT));
      add(new bcLabel("CoA: ",JLabel.RIGHT));
      add(jlTCoA = new JLabel("0.0,0.0",JLabel.LEFT));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));

      jbApply = new JButton("Apply");
      jbClose = new JButton("Close");
      
      jbApply.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e)
           { saveCenterboard();cbChange=false;}
      });
            
      jbClose.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e)
           {if(cbChange){  
            int n = JOptionPane.showConfirmDialog(f_edit,"Data has changed. Do you wish to apply changes?","Data Edit",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){saveCenterboard();cbChange=false;}}
            f_board.setVisible(false);}
            });
      
      add(jbApply);
      add(jbClose);

      }
      
   public Dimension getPreferredSize(){return d ; }

   protected void setTable() {
    String s;
    double wa = 0;
    double wx = 0;
    double wy = 0;
      if(pCb.board.use){
         jlRArea.setText(bcf.DF1d.format(hull.units.coefArea()*pCb.board.getWetArea())+hull.units.lblArea());
         s = bcf.DF1d.format(pCb.board.getWetX())+", "+bcf.DF1d.format(pCb.board.getWetY());
         jlRCoA.setText(s);
         wa = wa + pCb.board.getWetArea();
         wx = wx + pCb.board.getWetArea() * pCb.board.getWetX();
         wy = wy + pCb.board.getWetArea() * pCb.board.getWetY();
      }
      else{
         jlRArea.setText("0.00");
         jlRCoA.setText("-.--, -.--");
      }


      if(wa > 0){
         jlTArea.setText(bcf.DF1d.format(wa));
         jlTArea.setText(bcf.DF1d.format(hull.units.coefArea()*wa)+hull.units.lblArea());
         s = bcf.DF1d.format(wx / wa)+", "+bcf.DF1d.format(wy / wa);
         jlTCoA.setText(s);
      }
      else{
         jlTArea.setText("0.00");
         jlTCoA.setText("-.--, -.--");
      }
   }
   
   }// end cbArea
   
   class cbData extends JPanel implements ActionListener{
   Dimension d;
   editFoil pCenterboard;
   editPivot pPivot;
   JButton btnInc,btnDec;
   JRadioButton rbTLX, rbTLZ, rbTRX, rbTRZ;
   JRadioButton rbBLX, rbBLZ, rbBRX, rbBRZ;
   JRadioButton rbMoveX, rbMoveZ;
   JRadioButton rbPivotX, rbPivotZ;
   JRadioButton rbScale;
   JComboBox cbxInc;
   
   public cbData(int x, int y) {
      
      JPanel pCB;
      
      d = new Dimension(x,y);
      setLayout(new BorderLayout());
      pCenterboard = new editFoil(pCb.board);
      pCenterboard.setBorder(BorderFactory.createEtchedBorder()) ;

      setLayout(new BorderLayout());
      add(pCenterboard,BorderLayout.CENTER);

      pPivot = new editPivot();
      add(pPivot,BorderLayout.LINE_END);
      
      JPanel pInc = new JPanel();
      pInc.setPreferredSize(new Dimension(x-5,(3*y)/10));
      pInc.setLayout(new GridLayout(0,5));
      ButtonGroup bgInc = new ButtonGroup();
      
      btnInc = new JButton("Increase");
      btnInc.addActionListener(this);
      pInc.add(btnInc);

      pCB = new JPanel();
      pCB.add(new JLabel("Top/Left ",JLabel.RIGHT));
      rbTLX = new JRadioButton("X");
      bgInc.add(rbTLX);
      pCB.add(rbTLX);
      rbTLZ = new JRadioButton("Z");
      bgInc.add(rbTLZ);
      pCB.add(rbTLZ);
      pInc.add(pCB);
      
      pCB = new JPanel();
      pCB.add(new JLabel("Top/Right ",JLabel.RIGHT));
      rbTRX = new JRadioButton("X");
      bgInc.add(rbTRX);
      pCB.add(rbTRX);
      rbTRZ = new JRadioButton("Z");
      bgInc.add(rbTRZ);
      pCB.add(rbTRZ);
      pInc.add(pCB);

      pCB = new JPanel();
      pCB.add(new JLabel("Pivot ",JLabel.RIGHT));
      rbPivotX = new JRadioButton("X");
      bgInc.add(rbPivotX);
      pCB.add(rbPivotX);
      rbPivotZ = new JRadioButton("Z");
      bgInc.add(rbPivotZ);
      pCB.add(rbPivotZ);
      pInc.add(pCB);

      pCB = new JPanel();
      pCB.add(new JLabel("Scale ",JLabel.RIGHT));
      rbScale = new JRadioButton("%");
      bgInc.add(rbScale);
      pCB.add(rbScale);
      //pCB.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      pInc.add(pCB);
      

      btnDec = new JButton("Decrease");
      btnDec.addActionListener(this);
      pInc.add(btnDec);
      
      
      pCB = new JPanel();
      pCB.add(new JLabel("Bot/Left ",JLabel.RIGHT));
      rbBLX = new JRadioButton("X");
      bgInc.add(rbBLX);
      pCB.add(rbBLX);
      rbBLZ = new JRadioButton("Z");
      bgInc.add(rbBLZ);
      pCB.add(rbBLZ);
      pInc.add(pCB);
      
      pCB = new JPanel();
      pCB.add(new JLabel("Bot/Right ",JLabel.RIGHT));
      rbBRX = new JRadioButton("X");
      bgInc.add(rbBRX);
      pCB.add(rbBRX);
      rbBRZ = new JRadioButton("Z");
      bgInc.add(rbBRZ);
      pCB.add(rbBRZ);
      pInc.add(pCB);

      pCB = new JPanel();
      pCB.add(new JLabel("Move ",JLabel.RIGHT));
      rbMoveX = new JRadioButton("X");
      bgInc.add(rbMoveX);
      pCB.add(rbMoveX);
      rbMoveZ = new JRadioButton("Z");
      bgInc.add(rbMoveZ);
      pCB.add(rbMoveZ);
      pInc.add(pCB);
      
      pCB = new JPanel();
      pCB.setLayout(new GridLayout(0,2));
      pCB.add(new JLabel("Step: ",JLabel.RIGHT));
      
      String[] incs = {"0.01","0.02","0.05","0.1","0.2","0.5","1","2","5","10","20","50","100"};
      cbxInc = new JComboBox(incs);
      cbxInc.setEditable(true);
      cbxInc.setSelectedIndex(6);
      pCB.add(cbxInc);
      
      //tfInc = new JTextField("1.0",8);
      //pCB.add(tfInc);
      pInc.add(pCB);
      
      add(pInc,BorderLayout.PAGE_END);
      
   } // end constructor
   public Dimension getPreferredSize(){return d ; }

   public void actionPerformed(ActionEvent e){
    if (e.getSource()==btnInc||e.getSource()==btnDec){
    
    rscFoil f = pCb.board;
    editFoil eF = pCenterboard;
    editPivot eP = pPivot;
    double d=0;
    double v;
    double sgn;
    
    try{d = Double.parseDouble((String)cbxInc.getSelectedItem());
    } catch (NumberFormatException nfe){
     JOptionPane.showMessageDialog(null,"Unable to interperet step as number.","Warning!",JOptionPane.ERROR_MESSAGE);
     return;
    }
    
    if (e.getSource()==btnInc) sgn = 1.0;
    else sgn = -1.0;
    
      if(rbTLX.isSelected() || rbMoveX.isSelected()){
      try{v = Double.parseDouble(eF.ff[0][f.TL].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eF.ff[0][f.TL].setText(bcf.DF2d.format(v));
      f.setParamX(f.TL,v);
      }

      if(rbTLZ.isSelected() || rbMoveZ.isSelected()){
      try{v = Double.parseDouble(eF.ff[1][f.TL].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eF.ff[1][f.TL].setText(bcf.DF2d.format(v));
      f.setParamY(f.TL,v);
      }


      if(rbTRX.isSelected() || rbMoveX.isSelected()){
      try{v = Double.parseDouble(eF.ff[0][f.TR].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eF.ff[0][f.TR].setText(bcf.DF2d.format(v));
      f.setParamX(f.TR,v);
      }

      if(rbTRZ.isSelected() || rbMoveZ.isSelected()){
      try{v = Double.parseDouble(eF.ff[1][f.TR].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eF.ff[1][f.TR].setText(bcf.DF2d.format(v));
      f.setParamY(f.TR,v);
      }


      if(rbBRX.isSelected() || rbMoveX.isSelected()){
      try{v = Double.parseDouble(eF.ff[0][f.BR].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eF.ff[0][f.BR].setText(bcf.DF2d.format(v));
      f.setParamX(f.BR,v);
      }

      if(rbBRZ.isSelected() || rbMoveZ.isSelected()){
      try{v = Double.parseDouble(eF.ff[1][f.BR].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eF.ff[1][f.BR].setText(bcf.DF2d.format(v));
      f.setParamY(f.BR,v);
      }

      if(rbBLX.isSelected() || rbMoveX.isSelected()){
      try{v = Double.parseDouble(eF.ff[0][f.BL].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eF.ff[0][f.BL].setText(bcf.DF2d.format(v));
      f.setParamX(f.BL,v);
      }

      if(rbBLZ.isSelected() || rbMoveZ.isSelected()){
      try{v = Double.parseDouble(eF.ff[1][f.BL].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eF.ff[1][f.BL].setText(bcf.DF2d.format(v));
      f.setParamY(f.BL,v);
      }

      if(rbPivotX.isSelected() || rbMoveX.isSelected()){
      try{v = Double.parseDouble(eP.px.getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eP.px.setText(bcf.DF2d.format(v));
      pCb.setPivotX(v);
      }
      
      if(rbPivotZ.isSelected() || rbMoveZ.isSelected()){
      try{v = Double.parseDouble(eP.py.getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eP.py.setText(bcf.DF2d.format(v));
      pCb.setPivotZ(v);
      }
      
      if(rbScale.isSelected()){
         double mx = pCb.getPivotX();
         double my = pCb.getPivotZ();
         for (int i=0;i<4;i++){   
            v = mx + 0.01 * (100+sgn*d)*(f.getParamX(i)-mx);
            eF.ff[0][i].setText(bcf.DF2d.format(v));
            f.setParamX(i,v);
            v = my + 0.01 * (100+sgn*d)*(f.getParamY(i)-my);
            eF.ff[1][i].setText(bcf.DF2d.format(v));
            f.setParamY(i,v);
         }
      }
    
    }
    pDraw.repaint();
    pSpec.repaint();
    pRpt.setTable();
   }
   }// end cbData
  
  class editPivot extends JPanel implements DocumentListener, ItemListener, FocusListener, ActionListener {

   JTextField px;
   JTextField py;
   JSlider sa;
   boolean bChanged;
  
  public editPivot(){
   super (new GridLayout(0,1));
   setBorder(BorderFactory.createEtchedBorder()) ;

   Font efFont = new Font("Serif", Font.BOLD, 14);
   JLabel lbl;
   JPanel pRow;
   JPanel pCol;
   px = new JTextField(Double.toString(0.0),6);
   px.getDocument().addDocumentListener(this);
   px.addFocusListener(this);
   py = new JTextField(Double.toString(0.0),6);
   py.getDocument().addDocumentListener(this);
   py.addFocusListener(this);
   
   lbl = new JLabel("Pivot",JLabel.CENTER);
   lbl.setFont(efFont);
   add(lbl);
   
   pRow = new JPanel();
   pCol = new JPanel();
   pCol.add(new JLabel("X:",JLabel.RIGHT));
   pCol.add(px);
   pRow.add(pCol);
   pCol = new JPanel();
   pCol.add(new JLabel("Y:",JLabel.RIGHT));
   pCol.add(py);
   pRow.add(pCol);
   add(pRow);
   
   sa = new JSlider(-90,90,0);
   
   //sa.setModel(new DefaultBoundedRangeModel(-90,0,0,90));
   sa.setMajorTickSpacing(30);
   sa.setMinorTickSpacing(5);
   sa.setPaintLabels(true);
   sa.setPaintTicks(true);
   
   
   sa.addChangeListener(new ChangeListener()
            { public void stateChanged(ChangeEvent e)
                {double a = (double) sa.getValue();
                 pCb.setPivotAngle(a);
                 bChanged = true;
                 pDraw.repaint();
                 pSpec.repaint();
                 pRpt.setTable();
                }
            });
   add(sa);
  }  
  
   public void changedUpdate(DocumentEvent e) {cbChange = true; bChanged=true;}
   public void insertUpdate(DocumentEvent e) {cbChange = true; bChanged=true;}
   public void removeUpdate(DocumentEvent e) {cbChange = true; bChanged=true;}
   public void itemStateChanged(ItemEvent e) {cbChange = true; bChanged=true;}
   public void focusGained(FocusEvent e) {
    JTextField t = (JTextField) e.getComponent();
    t.select(0,100);}
   public void focusLost(FocusEvent e) {
      double v,w,a ;
      try{
      for (int i=0; i<4;i++){
         v = Double.parseDouble(px.getText());
         w = Double.parseDouble(py.getText());
         pCb.setPivotX(v);
         pCb.setPivotZ(w);
         a = (double) sa.getValue();
         pCb.setPivotAngle(a);
      }
      }
      catch (NumberFormatException nfe){
      JOptionPane.showMessageDialog(f_edit,"Bad number format in data entry.","Warning!", JOptionPane.ERROR_MESSAGE);
      return;}
      
      cbChange = true;
      bChanged = true;
      pDraw.repaint();
      pSpec.repaint();
      pRpt.setTable();

   }

   public void actionPerformed(ActionEvent e){
     //fo.use = cbFoil.isSelected();
     cbChange = true;
     bChanged = true;
     pDraw.repaint();
     pSpec.repaint();
     pRpt.setTable();
   }

  } // end editPivot
  
  
   class editFoil extends JPanel  implements DocumentListener, ItemListener, FocusListener, ActionListener {
   Dimension d;
   boolean bChanged;
   JCheckBox cbFoil;
   rscFoil fo;
   JTextField[][] ff;


   public editFoil(rscFoil f){
   super (new GridLayout(0,5));
   d = new Dimension(600,150);
   Font efFont = new Font("Serif", Font.BOLD, 14);
   fo = f;
   
   ff = new JTextField[2][4];
   for (int i=0; i<4;i++){
      ff[0][i] = new JTextField(Double.toString(f.getParamX(i)),6);
      ff[0][i].getDocument().addDocumentListener(this);
      ff[0][i].addFocusListener(this);
      ff[1][i] = new JTextField(Double.toString(f.getParamY(i)),6);
      ff[1][i].getDocument().addDocumentListener(this);
      ff[1][i].addFocusListener(this);
   }
   
   
   JLabel lbl;
   JPanel pC;
   
   // row 1, labels
   cbFoil = new JCheckBox("Use");
   cbFoil.setHorizontalAlignment(SwingConstants.LEFT);
   cbFoil.setSelected(fo.use);
   cbFoil.addActionListener(this);
   add(cbFoil);
   lbl = new JLabel("Left",JLabel.RIGHT);
   lbl.setFont(efFont);
   add(lbl);
   add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
   lbl = new JLabel("Right",JLabel.RIGHT);
   lbl.setFont(efFont);
   add(lbl);
   add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));

   // row 2, top
   lbl = new JLabel("Top:",JLabel.CENTER);
   lbl.setFont(efFont);
   add(lbl);
   
   pC = new JPanel();
   pC.add(new JLabel("X:",JLabel.RIGHT));
   pC.add(ff[0][fo.TL]);
   add(pC);
   
   pC = new JPanel();
   pC.add(new JLabel("Z:",JLabel.RIGHT));
   pC.add(ff[1][fo.TL]);
   add(pC);
   
   pC = new JPanel();
   pC.add(new JLabel("X:",JLabel.RIGHT));
   pC.add(ff[0][fo.TR]);
   add(pC);
   
   pC = new JPanel();
   pC.add(new JLabel("Z:",JLabel.RIGHT));
   pC.add(ff[1][fo.TR]);
   add(pC);

   // row 3, bottom
   lbl = new JLabel("Bottom:",JLabel.CENTER);
   lbl.setFont(efFont);
   add(lbl);
   pC = new JPanel();
   pC.add(new JLabel("X:",JLabel.RIGHT));
   pC.add(ff[0][fo.BL]);
   add(pC);
   
   pC = new JPanel();
   pC.add(new JLabel("Z:",JLabel.RIGHT));
   pC.add(ff[1][fo.BL]);
   add(pC);
   pC = new JPanel();
   pC.add(new JLabel("X:",JLabel.RIGHT));
   pC.add(ff[0][fo.BR]);
   add(pC);
   
   pC = new JPanel();
   pC.add(new JLabel("Z:",JLabel.RIGHT));
   pC.add(ff[1][fo.BR]);
   add(pC);
   
   }
   public Dimension getPreferredSize(){return d ; }
   public void changedUpdate(DocumentEvent e) {cbChange = true; bChanged=true;}
   public void insertUpdate(DocumentEvent e) {cbChange = true; bChanged=true;}
   public void removeUpdate(DocumentEvent e) {cbChange = true; bChanged=true;}
   public void itemStateChanged(ItemEvent e) {cbChange = true; bChanged=true;}
   public void focusGained(FocusEvent e) {
    JTextField t = (JTextField) e.getComponent();
    t.select(0,100);}
   public void focusLost(FocusEvent e) {
      double v,w ;
      try{
      for (int i=0; i<4;i++){
         v = Double.parseDouble(ff[0][i].getText());
         w = Double.parseDouble(ff[1][i].getText());
         fo.setParamXY(i,v,w);
      }
      }
      catch (NumberFormatException nfe){
      JOptionPane.showMessageDialog(f_edit,"Bad number format in data entry.","Warning!", JOptionPane.ERROR_MESSAGE);
      return;}
      
      cbChange = true;
      bChanged = true;
      pDraw.repaint();
      pSpec.repaint();
      pRpt.setTable();

   }
   public void actionPerformed(ActionEvent e){
     fo.use = cbFoil.isSelected();
     cbChange = true;
     bChanged = true;
     pDraw.repaint();
     pSpec.repaint();
     pRpt.setTable();
   }
   
   }//end editFoil

   }// end pnlCenterboard
   
   
   class pnlRudder extends JPanel{
      JTabbedPane pDisp;
      rdrDraw pDraw;
      rdrSpec pSpec;
      rdrData pData;
      rdrArea pRpt;
      Rudder pRdr;
      Border bRdr;
      rdrTabOrder to;
      boolean rdrChange;
      
   public pnlRudder(){

      if (hull.rudder.valid) pRdr = (Rudder) hull.rudder.clone();
      else pRdr = new Rudder();
      pRdr.setBase(hull.base);
      rdrChange = false;
      
      bRdr = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
      setLayout(new BorderLayout());
      
      pDisp = new JTabbedPane();
      
      pDraw = new rdrDraw(750,325);
      pDraw.setBackground(Color.white) ;
      pDraw.setBorder(bRdr);
      pDisp.add(pDraw,"Drawing");
      
      pSpec = new rdrSpec(750,325);
      pSpec.setBackground(Color.white) ;
      pSpec.setBorder(bRdr);
      pDisp.add(pSpec,"Dimensions");
      
      add(pDisp,BorderLayout.CENTER);

      pRpt = new rdrArea(180,200);
      add(pRpt,BorderLayout.LINE_END);


      pData = new rdrData(750,185);
      pData.setBorder(bRdr);
      add(pData,BorderLayout.PAGE_END);

      //to = new rdrTabOrder(this);
      //f_rudder.setFocusTraversalPolicy(to);

      f_rudder.addWindowListener(new WindowAdapter()
      {public void windowClosing(WindowEvent e){
            if(rdrChange){  
            int n = JOptionPane.showConfirmDialog(f_rudder,"Data has changed. Do you wish to apply changes?","Sailplan Design",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){saveRudder();}}
            f_rudder.setVisible(false);}}); 

      pDraw.repaint();
      pSpec.repaint();
      pRpt.setTable();
   
   }// end constructor

   public void saveRudder(){
     hull.rudder = (Rudder) pRdr.clone();
     hull.bChanged = true;
   }
   
   public class rdrTabOrder extends FocusTraversalPolicy{
   rdrData r;
   public rdrTabOrder(pnlRudder p){
      pnlRudder psp = p;
      r = p.pData;
   }
   public Component getComponentAfter(Container focusCycleRoot, Component aComponent){
   return aComponent;
   }
   public Component getComponentBefore(Container focusCycleRoot, Component aComponent){
   return aComponent;
   }
   public Component getLastComponent(Container focusCycleRoot){
   return r;   }

   public Component getFirstComponent(Container focusCycleRoot){
   return r;   }

   public Component getDefaultComponent(Container focusCycleRoot){
   return r;}
   }// end rdrTabOrder

   class rdrSpec extends JPanel{
   Dimension d;
   public rdrSpec(int x, int y){
      d = new Dimension(x,y);
   }
   public Dimension getPreferredSize(){return d ; }
   
   protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int iL = 20;
    int iC1 = 20;
    int iC2 = 80;
    int iC3 = 250;
    int iC4 = 325;
    int kL = 0;
    if (pRdr.rudder.use){
       int jL = 0;
       g.drawString("Rudder",iC1,iL);
       jL = 20;
       g.drawString("Area: ",iC1,iL+jL);
       g.drawString(bcf.DF1d.format(pRdr.rudder.getArea()),iC2,iL+jL);
       jL = jL + 20;
       g.drawString("CoA: ",iC1,iL+jL);
       g.drawString(bcf.DF2d.format(pRdr.rudder.getAreaX())+", "+bcf.DF2d.format(pRdr.rudder.getAreaY()),iC2,iL+jL);
       jL = jL + 20;
       g.drawString("Points: ",iC1,iL+jL);
       for (int i=0;i<4;i++){
          g.drawString(bcf.DF2d.format(pRdr.rudder.getParamX(i))+", "+bcf.DF2d.format(pRdr.rudder.getParamY(i)),iC2,iL+jL);
          jL = jL + 20;
       }
       g.drawString("ref: baseline",iC2+90,iL+jL-20);
       kL = jL;
       jL = 0;
       g.drawString("Immersed Portion",iC3,iL);
       jL = 20;
       g.drawString("Area: ",iC3,iL+jL);
       g.drawString(bcf.DF1d.format(pRdr.rudder.getWetArea()),iC4,iL+jL);
       jL = jL + 20;
       g.drawString("CoA: ",iC3,iL+jL);
       g.drawString(bcf.DF2d.format(pRdr.rudder.getWetX())+", "+bcf.DF2d.format(pRdr.rudder.getWetY()),iC4,iL+jL);
       jL = jL + 20;
       g.drawString("Points: ",iC3,iL+jL);
    
       SortedSet wp = pRdr.rudder.getWetPts();
       Iterator pi = wp.iterator();
       while (pi.hasNext()){
          Point p = (Point) pi.next();
          g.drawString(bcf.DF2d.format(p.x)+", "+bcf.DF2d.format(p.z),iC4,iL+jL);
          jL = jL + 20;
         }
         g.drawString("ref: lwl",iC4+90,iL+jL-20);
         kL = Math.max(kL,jL);
    }
    iL = iL+kL;
    if (pRdr.skeg.use){
       int jL = 0;
       g.drawString("Skeg",iC1,iL);
       jL = 20;
       g.drawString("Area: ",iC1,iL+jL);
       g.drawString(bcf.DF1d.format(pRdr.skeg.getArea()),iC2,iL+jL);
       jL = jL + 20;
       g.drawString("CoA: ",iC1,iL+jL);
       g.drawString(bcf.DF2d.format(pRdr.skeg.getAreaX())+", "+bcf.DF2d.format(pRdr.skeg.getAreaY()),iC2,iL+jL);
       jL = jL + 20;
       g.drawString("Points: ",iC1,iL+jL);
       for (int i=0;i<4;i++){
          g.drawString(bcf.DF2d.format(pRdr.skeg.getParamX(i))+", "+bcf.DF2d.format(pRdr.skeg.getParamY(i)),iC2,iL+jL);
          jL = jL + 20;
       }
       g.drawString("ref: baseline",iC2+90,iL+jL-20);
       kL = jL;
       jL = 0;
       g.drawString("Immersed Portion",iC3,iL);
       jL = 20;
       g.drawString("Area: ",iC3,iL+jL);
       g.drawString(bcf.DF1d.format(pRdr.skeg.getWetArea()),iC4,iL+jL);
       jL = jL + 20;
       g.drawString("CoA: ",iC3,iL+jL);
       g.drawString(bcf.DF2d.format(pRdr.skeg.getWetX())+", "+bcf.DF2d.format(pRdr.skeg.getWetY()),iC4,iL+jL);
       jL = jL + 20;
       g.drawString("Points: ",iC3,iL+jL);
    
       SortedSet wp = pRdr.skeg.getWetPts();
       Iterator pi = wp.iterator();
       while (pi.hasNext()){
          Point p = (Point) pi.next();
          g.drawString(bcf.DF2d.format(p.x)+", "+bcf.DF2d.format(p.z),iC4,iL+jL);
          jL = jL + 20;
         }
         g.drawString("ref: lwl",iC4+90,iL+jL-20);
         //kL = Math.max(kL,jL);
    }
    
   }// end paintComponent    
   }// ends rdrSpec
   
   class rdrDraw extends JPanel{
   Dimension d;
   public rdrDraw(int x, int y){
      d = new Dimension(x,y);
   }
   public Dimension getPreferredSize(){return d ; }
   
   protected void paintComponent(Graphics g) {
    super.paintComponent(g);
     
     double mx = getWidth() ;
     double my = getHeight() ;
     int ix = (int) mx;
     int iy = (int) my;
     int xb = (int) 50;
     int yb = (int) my / 2;
     int iu, iv, iw, iz;
     
     g.clearRect(0,0,ix,iy) ;
     g.drawString("Rudder",10,20);
     g.setColor(Color.red);  g.drawLine(60,12,85,12);
     g.setColor(Color.blue); g.drawLine(60,17,85,17);
     g.setColor(Color.black);
     g.drawString("Skeg",10,40);
     g.setColor(Color.orange); g.drawLine(60,32,85,32);
     g.setColor(Color.green);  g.drawLine(60,37,85,37);
     g.setColor(Color.black);
     
     if (!hull.valid) return;
     
     double rx = (mx - 100.0)/(hull.gx_max - hull.gx_min);
     double ry = (my - 75.0)/(hull.gy_max - hull.gy_min);
     double r = Math.min(rx,ry);
     
          // draw waterline
          iu = xb + (int) (r * (hull.gx_min - hull.gx_min));
          iv = yb - (int) (r * (0.0 - hull.gz_min));
          iw = xb + (int) (r * (hull.gx_max - hull.gx_min));
          iz = yb - (int) (r * (0.0 - hull.gz_min));
          g.setColor(Color.blue);
          g.drawLine(iu,iv,iw,iz);
     
     // draw hull profile
     g.setColor(Color.black);
     double z1Lo = hull.gz_max; 
     double z1Hi = hull.gz_min; 
     double x1 = 0;
     for (double pct=0.0; pct<=1.0025; pct=pct+0.01){
        double x = hull.gx_min + pct * (hull.gx_max - hull.gx_min);
        SortedSet ss = hull.getStnSet(x,0.0);
        Iterator si = ss.iterator();
        double zLo = hull.gz_max;
        double zHi = hull.gz_min;
        boolean bOk = false;
        while(si.hasNext()){
           Point p = (Point) si.next();
           zLo = Math.min(zLo, p.z);
           zHi = Math.max(zHi, p.z);
           bOk = true;
           }
       if (bOk && pct > 0.0){
          iu = xb + (int) (r * (x1 - hull.gx_min));
          iv = yb - (int) (r * (z1Lo - hull.gz_min));
          iw = xb + (int) (r * (x - hull.gx_min));
          iz = yb - (int) (r * (zLo - hull.gz_min));
          g.drawLine(iu,iv,iw,iz);
          iv = yb - (int) (r * (z1Hi - hull.gz_min));
          iz = yb - (int) (r * (zHi - hull.gz_min));
          g.drawLine(iu,iv,iw,iz);
          }
          
       x1 = x;    
       z1Lo = zLo;
       z1Hi = zHi;
     
     }
     
     // draw stems
     g.setColor(Color.lightGray);
     for (int iSL=0;iSL<=1;iSL++){
        if (hull.bStems[iSL] && hull.sLines[iSL].valid){
        double x = hull.sLines[iSL].hPoints[0].getX(); 
        double y = hull.sLines[iSL].hPoints[0].getZ(); 
        iu = xb + (int) (r * (x - hull.gx_min)) ;
        iv = yb - (int) (r * (y - hull.gz_min)) ;
        for (int j=1; j<hull.sLines[iSL].hPoints.length;j++){
          x = hull.sLines[iSL].hPoints[j].getX(); 
          y = hull.sLines[iSL].hPoints[j].getZ(); 
          iw = xb + (int) (r * (x - hull.gx_min)) ;
          iz = yb - (int) (r * (y - hull.gz_min)) ;
          g.drawLine(iu,iv,iw,iz);
          iu = iw;
          iv = iz;
        }        
     }}
    
    // draw rudder
    if (pRdr.rudder.use){
     g.setColor(Color.lightGray);
    double x,y;
    x = pRdr.rudder.getParamX(pRdr.rudder.TL);
    y = pRdr.rudder.getParamY(pRdr.rudder.TL)+hull.base;
    iu = xb + (int) (r * (x - hull.gx_min)) ;
    iv = yb - (int) (r * (y - hull.gz_min)) ;
    x = pRdr.rudder.getParamX(pRdr.rudder.TR);
    y = pRdr.rudder.getParamY(pRdr.rudder.TR)+hull.base;
    iw = xb + (int) (r * (x - hull.gx_min)) ;
    iz = yb - (int) (r * (y - hull.gz_min)) ;
    g.drawLine(iu,iv,iw,iz);
    
    x = pRdr.rudder.getParamX(pRdr.rudder.BR);
    y = pRdr.rudder.getParamY(pRdr.rudder.BR)+hull.base;
    iu = xb + (int) (r * (x - hull.gx_min)) ;
    iv = yb - (int) (r * (y - hull.gz_min)) ;
    g.drawLine(iw,iz,iu,iv);
    
    x = pRdr.rudder.getParamX(pRdr.rudder.BL);
    y = pRdr.rudder.getParamY(pRdr.rudder.BL)+hull.base;
    iw = xb + (int) (r * (x - hull.gx_min)) ;
    iz = yb - (int) (r * (y - hull.gz_min)) ;
    g.drawLine(iu,iv,iw,iz);
    
    x = pRdr.rudder.getParamX(pRdr.rudder.TL);
    y = pRdr.rudder.getParamY(pRdr.rudder.TL)+hull.base;
    iu = xb + (int) (r * (x - hull.gx_min)) ;
    iv = yb - (int) (r * (y - hull.gz_min)) ;
    g.drawLine(iw,iz,iu,iv);
    
    // draw wet rudder
    if (pRdr.rudder.getWetArea() > 0) {
    g.setColor(Color.blue);
    SortedSet wp = pRdr.rudder.getWetPts();
    Iterator pi = wp.iterator();
    if (pi.hasNext()){
    Point p0 = (Point) pi.next();
    Point p1 = new Point(p0);
    while (pi.hasNext()){
      Point p2 = (Point)pi.next();
      iu = xb + (int) (r * (p1.x - hull.gx_min)) ;
      iv = yb - (int) (r * (p1.z - hull.gz_min)) ;
      iw = xb + (int) (r * (p2.x - hull.gx_min)) ;
      iz = yb - (int) (r * (p2.z - hull.gz_min)) ;
      g.drawLine(iu,iv,iw,iz);
      p1 = p2;
    }
      iu = xb + (int) (r * (p1.x - hull.gx_min)) ;
      iv = yb - (int) (r * (p1.z - hull.gz_min)) ;
      iw = xb + (int) (r * (p0.x - hull.gx_min)) ;
      iz = yb - (int) (r * (p0.z - hull.gz_min)) ;
      g.drawLine(iu,iv,iw,iz);
    }
    int cx = xb + (int) (r * (pRdr.rudder.getWetX() - hull.gx_min)) ;
    int cy = yb - (int) (r * (pRdr.rudder.getWetY() - hull.gz_min)) ;
    g.drawArc(cx-5,cy-5,10,10,0,360);
    g.drawLine(cx+5,cy,cx-5,cy);
    g.drawLine(cx,cy+5,cx,cy-5);
    }
    }// end if use rudder
    
    // draw skeg
    if (pRdr.skeg.use){
    g.setColor(Color.orange);
    double x,y;
    x = pRdr.skeg.getParamX(pRdr.skeg.TL);
    y = pRdr.skeg.getParamY(pRdr.skeg.TL)+hull.base;
    iu = xb + (int) (r * (x - hull.gx_min)) ;
    iv = yb - (int) (r * (y - hull.gz_min)) ;
    x = pRdr.skeg.getParamX(pRdr.skeg.TR);
    y = pRdr.skeg.getParamY(pRdr.skeg.TR)+hull.base;
    iw = xb + (int) (r * (x - hull.gx_min)) ;
    iz = yb - (int) (r * (y - hull.gz_min)) ;
    g.drawLine(iu,iv,iw,iz);
    
    x = pRdr.skeg.getParamX(pRdr.skeg.BR);
    y = pRdr.skeg.getParamY(pRdr.skeg.BR)+hull.base;
    iu = xb + (int) (r * (x - hull.gx_min)) ;
    iv = yb - (int) (r * (y - hull.gz_min)) ;
    g.drawLine(iw,iz,iu,iv);
    
    x = pRdr.skeg.getParamX(pRdr.skeg.BL);
    y = pRdr.skeg.getParamY(pRdr.skeg.BL)+hull.base;
    iw = xb + (int) (r * (x - hull.gx_min)) ;
    iz = yb - (int) (r * (y - hull.gz_min)) ;
    g.drawLine(iu,iv,iw,iz);
    
    x = pRdr.skeg.getParamX(pRdr.skeg.TL);
    y = pRdr.skeg.getParamY(pRdr.skeg.TL)+hull.base;
    iu = xb + (int) (r * (x - hull.gx_min)) ;
    iv = yb - (int) (r * (y - hull.gz_min)) ;
    g.drawLine(iw,iz,iu,iv);
    
    // draw wet skeg
    if (pRdr.skeg.getWetArea() > 0) {
    g.setColor(Color.green);
    SortedSet wp = pRdr.skeg.getWetPts();
    Iterator pi = wp.iterator();
    if (pi.hasNext()){
    Point p0 = (Point) pi.next();
    Point p1 = new Point(p0);
    while (pi.hasNext()){
      Point p2 = (Point)pi.next();
      iu = xb + (int) (r * (p1.x - hull.gx_min)) ;
      iv = yb - (int) (r * (p1.z - hull.gz_min)) ;
      iw = xb + (int) (r * (p2.x - hull.gx_min)) ;
      iz = yb - (int) (r * (p2.z - hull.gz_min)) ;
      g.drawLine(iu,iv,iw,iz);
      p1 = p2;
    }
      iu = xb + (int) (r * (p1.x - hull.gx_min)) ;
      iv = yb - (int) (r * (p1.z - hull.gz_min)) ;
      iw = xb + (int) (r * (p0.x - hull.gx_min)) ;
      iz = yb - (int) (r * (p0.z - hull.gz_min)) ;
      g.drawLine(iu,iv,iw,iz);
    }
    int cx = xb + (int) (r * (pRdr.skeg.getWetX() - hull.gx_min)) ;
    int cy = yb - (int) (r * (pRdr.skeg.getWetY() - hull.gz_min)) ;
    g.drawArc(cx-5,cy-5,10,10,0,360);
    g.drawLine(cx+5,cy,cx-5,cy);
    g.drawLine(cx,cy+5,cx,cy-5);
    }
    }// end use skeg

    g.setColor(Color.black);

    double tA = 0;
    double tX = 0;
    double tY = 0;
    if (pRdr.rudder.use){tA = tA + pRdr.rudder.getWetArea();
                         tX = tX + pRdr.rudder.getWetArea() * pRdr.rudder.getWetX();
                         tY = tY + pRdr.rudder.getWetArea() * pRdr.rudder.getWetY();}
    if (pRdr.skeg.use){tA = tA + pRdr.skeg.getWetArea();
                         tX = tX + pRdr.skeg.getWetArea() * pRdr.skeg.getWetX();
                         tY = tY + pRdr.skeg.getWetArea() * pRdr.skeg.getWetY();}
    if (tA > 0){
        tX = tX / tA;
        tY = tY / tA;
        int cx = xb + (int) (r * (tX - hull.gx_min));
        int cy = yb - (int) (r * (tY - hull.gz_min));
        g.drawLine(cx+4,cy+4,cx-4,cy+4);
        g.drawLine(cx+4,cy-4,cx-4,cy-4);
        g.drawLine(cx-4,cy-4,cx-4,cy+4);
        g.drawLine(cx+4,cy-4,cx+4,cy+4);
        g.drawLine(cx+6,cy,cx-6,cy);
        g.drawLine(cx,cy+6,cx,cy-6);
     }

    
   } // end paintComponent  
   
   }// end rdrDraw

   class rdrArea extends JPanel{
   Dimension d;
   JLabel jlRArea, jlRCoA;   
   JLabel jlSArea, jlSCoA; 
   JLabel jlTArea, jlTCoA; 
   JButton jbApply, jbClose;

      public rdrArea(int x, int y){
      d = new Dimension(x,y);
      setLayout(new GridLayout(0,2));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new bcLabel("Immersed Areas",JLabel.LEFT));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));

      add(new bcLabel("Rudder - Area: ",JLabel.RIGHT));
      add(jlRArea = new JLabel("0.0",JLabel.LEFT));
      
      add(new bcLabel("CoA: ",JLabel.RIGHT));
      add(jlRCoA = new JLabel("0.0,0.0",JLabel.LEFT));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new bcLabel("Skeg - Area: ",JLabel.RIGHT));
      add(jlSArea = new JLabel("0.0",JLabel.LEFT));
      add(new bcLabel("CoA: ",JLabel.RIGHT));
      add(jlSCoA = new JLabel("0.0,0.0",JLabel.LEFT));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new bcLabel("Total - Area: ",JLabel.RIGHT));
      add(jlTArea = new JLabel("0.0",JLabel.LEFT));
      add(new bcLabel("CoA: ",JLabel.RIGHT));
      add(jlTCoA = new JLabel("0.0,0.0",JLabel.LEFT));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));

      jbApply = new JButton("Apply");
      jbClose = new JButton("Close");
      
      jbApply.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e)
           { saveRudder();rdrChange=false;}
      });
            
      jbClose.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e)
           {if(rdrChange){  
            int n = JOptionPane.showConfirmDialog(f_edit,"Data has changed. Do you wish to apply changes?","Data Edit",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){saveRudder();rdrChange=false;}}
            f_rudder.setVisible(false);}
            });
      
      add(jbApply);
      add(jbClose);

      }
      
   public Dimension getPreferredSize(){return d ; }

   protected void setTable() {
    String s;
    double wa = 0;
    double wx = 0;
    double wy = 0;
      if(pRdr.rudder.use){
         jlRArea.setText(bcf.DF1d.format(hull.units.coefArea()*pRdr.rudder.getWetArea())+hull.units.lblArea());
         s = bcf.DF1d.format(pRdr.rudder.getWetX())+", "+bcf.DF1d.format(pRdr.rudder.getWetY());
         jlRCoA.setText(s);
         wa = wa + pRdr.rudder.getWetArea();
         wx = wx + pRdr.rudder.getWetArea() * pRdr.rudder.getWetX();
         wy = wy + pRdr.rudder.getWetArea() * pRdr.rudder.getWetY();
      }
      else{
         jlRArea.setText("0.00");
         jlRCoA.setText("-.--, -.--");
      }

      if(pRdr.skeg.use){
         jlSArea.setText(bcf.DF1d.format(hull.units.coefArea()*pRdr.skeg.getWetArea())+hull.units.lblArea());
         s = bcf.DF1d.format(pRdr.skeg.getWetX())+", "+bcf.DF1d.format(pRdr.skeg.getWetY());
         jlSCoA.setText(s);
         wa = wa + pRdr.skeg.getWetArea();
         wx = wx + pRdr.skeg.getWetArea() * pRdr.skeg.getWetX();
         wy = wy + pRdr.skeg.getWetArea() * pRdr.skeg.getWetY();
      }
      else{
         jlSArea.setText("0.00");
         jlSCoA.setText("-.--, -.--");
      }

      if(wa > 0){
         jlTArea.setText(bcf.DF1d.format(wa));
         jlTArea.setText(bcf.DF1d.format(hull.units.coefArea()*wa)+hull.units.lblArea());
         s = bcf.DF1d.format(wx / wa)+", "+bcf.DF1d.format(wy / wa);
         jlTCoA.setText(s);
      }
      else{
         jlTArea.setText("0.00");
         jlTCoA.setText("-.--, -.--");
      }
   }
   
   }// end rdrArea
   
   class rdrData extends JPanel implements ActionListener{
   Dimension d;
   JTabbedPane tp = new JTabbedPane();
   editFoil pRudder;
   editFoil pSkeg;
   JButton btnInc,btnDec;
   JRadioButton rbTLX, rbTLZ, rbTRX, rbTRZ;
   JRadioButton rbBLX, rbBLZ, rbBRX, rbBRZ;
   JRadioButton rbMoveX, rbMoveZ;
   JRadioButton rbScale;
   JComboBox cbxInc;
   
   public rdrData(int x, int y) {
      
      JPanel pCB;
      
      d = new Dimension(x,y);
      setLayout(new BorderLayout());
      pRudder = new editFoil(pRdr.rudder);
      tp.addTab("Rudder",pRudder);
      pSkeg = new editFoil(pRdr.skeg);
      tp.addTab("Skeg",pSkeg);
      add(tp,BorderLayout.CENTER);
      
      JPanel pInc = new JPanel();
      pInc.setPreferredSize(new Dimension(x-5,(3*y)/10));
      pInc.setLayout(new GridLayout(0,8));
      ButtonGroup bgInc = new ButtonGroup();
      
      btnInc = new JButton("Increase");
      btnInc.addActionListener(this);
      pInc.add(btnInc);

      pInc.add(new JLabel("Top/Left ",JLabel.RIGHT));
      pCB = new JPanel();
      rbTLX = new JRadioButton("X");
      bgInc.add(rbTLX);
      pCB.add(rbTLX);
      rbTLZ = new JRadioButton("Z");
      bgInc.add(rbTLZ);
      pCB.add(rbTLZ);
      pInc.add(pCB);
      
      pInc.add(new JLabel("Top/Right ",JLabel.RIGHT));
      pCB = new JPanel();
      rbTRX = new JRadioButton("X");
      bgInc.add(rbTRX);
      pCB.add(rbTRX);
      rbTRZ = new JRadioButton("Z");
      bgInc.add(rbTRZ);
      pCB.add(rbTRZ);
      pInc.add(pCB);

      pInc.add(new JLabel("Move ",JLabel.RIGHT));
      pCB = new JPanel();
      rbMoveX = new JRadioButton("X");
      bgInc.add(rbMoveX);
      pCB.add(rbMoveX);
      rbMoveZ = new JRadioButton("Z");
      bgInc.add(rbMoveZ);
      pCB.add(rbMoveZ);
      pInc.add(pCB);

      pInc.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      

      btnDec = new JButton("Decrease");
      btnDec.addActionListener(this);
      pInc.add(btnDec);
      
      pInc.add(new JLabel("Bottom/Left ",JLabel.RIGHT));
      
      pCB = new JPanel();
      rbBLX = new JRadioButton("X");
      bgInc.add(rbBLX);
      pCB.add(rbBLX);
      rbBLZ = new JRadioButton("Z");
      bgInc.add(rbBLZ);
      pCB.add(rbBLZ);
      pInc.add(pCB);
      
      pInc.add(new JLabel("Bottom/Right ",JLabel.RIGHT));
      pCB = new JPanel();
      rbBRX = new JRadioButton("X");
      bgInc.add(rbBRX);
      pCB.add(rbBRX);
      rbBRZ = new JRadioButton("Z");
      bgInc.add(rbBRZ);
      pCB.add(rbBRZ);
      pInc.add(pCB);

      pInc.add(new JLabel("Scale ",JLabel.RIGHT));
      pCB = new JPanel();
      rbScale = new JRadioButton("%");
      bgInc.add(rbScale);
      pCB.add(rbScale);
      pCB.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      pInc.add(pCB);
      
      pCB = new JPanel();
      pCB.setLayout(new GridLayout(0,2));
      pCB.add(new JLabel("Step: ",JLabel.RIGHT));
      
      
      String[] incs = {"0.01","0.02","0.05","0.1","0.2","0.5","1","2","5","10","20","50","100"};
      cbxInc = new JComboBox(incs);
      cbxInc.setEditable(true);
      cbxInc.setSelectedIndex(6);
      pCB.add(cbxInc);
      
      //tfInc = new JTextField("1.0",8);
      //pCB.add(tfInc);
      pInc.add(pCB);
      
      add(pInc,BorderLayout.PAGE_END);
      
   } // end constructor
   public Dimension getPreferredSize(){return d ; }

   public void actionPerformed(ActionEvent e){
    if (e.getSource()==btnInc||e.getSource()==btnDec){
    
    rscFoil f = pRdr.rudder;
    editFoil eF = pRudder;
    if (tp.getSelectedIndex() == 1){
       f = pRdr.skeg;
       eF = pSkeg;
    }
    double d=0;
    double v;
    double sgn;
    
    try{d = Double.parseDouble((String)cbxInc.getSelectedItem());
    } catch (NumberFormatException nfe){
     JOptionPane.showMessageDialog(null,"Unable to interperet step as number.","Warning!",JOptionPane.ERROR_MESSAGE);
     return;
    }
    
    if (e.getSource()==btnInc) sgn = 1.0;
    else sgn = -1.0;
    
      if(rbTLX.isSelected() || rbMoveX.isSelected()){
      try{v = Double.parseDouble(eF.ff[0][f.TL].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eF.ff[0][f.TL].setText(bcf.DF2d.format(v));
      f.setParamX(f.TL,v);
      }

      if(rbTLZ.isSelected() || rbMoveZ.isSelected()){
      try{v = Double.parseDouble(eF.ff[1][f.TL].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eF.ff[1][f.TL].setText(bcf.DF2d.format(v));
      f.setParamY(f.TL,v);
      }


      if(rbTRX.isSelected() || rbMoveX.isSelected()){
      try{v = Double.parseDouble(eF.ff[0][f.TR].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eF.ff[0][f.TR].setText(bcf.DF2d.format(v));
      f.setParamX(f.TR,v);
      }

      if(rbTRZ.isSelected() || rbMoveZ.isSelected()){
      try{v = Double.parseDouble(eF.ff[1][f.TR].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eF.ff[1][f.TR].setText(bcf.DF2d.format(v));
      f.setParamY(f.TR,v);
      }


      if(rbBRX.isSelected() || rbMoveX.isSelected()){
      try{v = Double.parseDouble(eF.ff[0][f.BR].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eF.ff[0][f.BR].setText(bcf.DF2d.format(v));
      f.setParamX(f.BR,v);
      }

      if(rbBRZ.isSelected() || rbMoveZ.isSelected()){
      try{v = Double.parseDouble(eF.ff[1][f.BR].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eF.ff[1][f.BR].setText(bcf.DF2d.format(v));
      f.setParamY(f.BR,v);
      }

      if(rbBLX.isSelected() || rbMoveX.isSelected()){
      try{v = Double.parseDouble(eF.ff[0][f.BL].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eF.ff[0][f.BL].setText(bcf.DF2d.format(v));
      f.setParamX(f.BL,v);
      }

      if(rbBLZ.isSelected() || rbMoveZ.isSelected()){
      try{v = Double.parseDouble(eF.ff[1][f.BL].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eF.ff[1][f.BL].setText(bcf.DF2d.format(v));
      f.setParamY(f.BL,v);
      }

      if(rbScale.isSelected()){
         double mx = 0.25*(f.getParamX(f.TL)+f.getParamX(f.TR)+f.getParamX(f.BR)+f.getParamX(f.BL));
         double my = 0.25*(f.getParamY(f.TL)+f.getParamY(f.TR)+f.getParamY(f.BR)+f.getParamY(f.BL));
         for (int i=0;i<4;i++){   
            v = mx + 0.01 * (100+sgn*d)*(f.getParamX(i)-mx);
            eF.ff[0][i].setText(bcf.DF2d.format(v));
            f.setParamX(i,v);
            v = my + 0.01 * (100+sgn*d)*(f.getParamY(i)-my);
            eF.ff[1][i].setText(bcf.DF2d.format(v));
            f.setParamY(i,v);
         }
      }
    
    }
    pDraw.repaint();
    pSpec.repaint();
    pRpt.setTable();
   }
   }// end rdrData
  
   class editFoil extends JPanel  implements DocumentListener, ItemListener, FocusListener, ActionListener {
   Dimension d;
   boolean bChanged;
   JCheckBox cbFoil;
   rscFoil fo;
   JTextField[][] ff;
   
   public editFoil(rscFoil f){
   super (new GridLayout(0,8));
   d = new Dimension(600,150);
   Font efFont = new Font("Serif", Font.BOLD, 14);
   fo = f;
   
   ff = new JTextField[2][4];
   for (int i=0; i<4;i++){
      ff[0][i] = new JTextField(Double.toString(f.getParamX(i)),6);
      ff[0][i].getDocument().addDocumentListener(this);
      ff[0][i].addFocusListener(this);
      ff[1][i] = new JTextField(Double.toString(f.getParamY(i)),6);
      ff[1][i].getDocument().addDocumentListener(this);
      ff[1][i].addFocusListener(this);
   }
   
   
   JLabel lbl;
   JPanel pC;
   
   // row 1, labels
   cbFoil = new JCheckBox("Use");
   cbFoil.setHorizontalAlignment(SwingConstants.LEFT);
   cbFoil.setSelected(fo.use);
   cbFoil.addActionListener(this);
   add(cbFoil);
   add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
   lbl = new JLabel("Left",JLabel.RIGHT);
   lbl.setFont(efFont);
   add(lbl);
   add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
   add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
   lbl = new JLabel("Right",JLabel.RIGHT);
   lbl.setFont(efFont);
   add(lbl);
   add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
   add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));

   // row 2, top
   add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
   lbl = new JLabel("Top:",JLabel.CENTER);
   lbl.setFont(efFont);
   add(lbl);
   
   pC = new JPanel();
   pC.add(new JLabel("X:",JLabel.RIGHT));
   pC.add(ff[0][fo.TL]);
   add(pC);
   
   pC = new JPanel();
   pC.add(new JLabel("Z:",JLabel.RIGHT));
   pC.add(ff[1][fo.TL]);
   add(pC);
   
   add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
   pC = new JPanel();
   pC.add(new JLabel("X:",JLabel.RIGHT));
   pC.add(ff[0][fo.TR]);
   add(pC);
   
   pC = new JPanel();
   pC.add(new JLabel("Z:",JLabel.RIGHT));
   pC.add(ff[1][fo.TR]);
   add(pC);
   add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));

   // row 3, bottom
   add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
   lbl = new JLabel("Bottom:",JLabel.CENTER);
   lbl.setFont(efFont);
   add(lbl);
   pC = new JPanel();
   pC.add(new JLabel("X:",JLabel.RIGHT));
   pC.add(ff[0][fo.BL]);
   add(pC);
   
   pC = new JPanel();
   pC.add(new JLabel("Z:",JLabel.RIGHT));
   pC.add(ff[1][fo.BL]);
   add(pC);
   add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
   pC = new JPanel();
   pC.add(new JLabel("X:",JLabel.RIGHT));
   pC.add(ff[0][fo.BR]);
   add(pC);
   
   pC = new JPanel();
   pC.add(new JLabel("Z:",JLabel.RIGHT));
   pC.add(ff[1][fo.BR]);
   add(pC);
   add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
   
   }
   public Dimension getPreferredSize(){return d ; }
   public void changedUpdate(DocumentEvent e) {rdrChange = true; bChanged=true;}
   public void insertUpdate(DocumentEvent e) {rdrChange = true; bChanged=true;}
   public void removeUpdate(DocumentEvent e) {rdrChange = true; bChanged=true;}
   public void itemStateChanged(ItemEvent e) {rdrChange = true; bChanged=true;}
   public void focusGained(FocusEvent e) {
    JTextField t = (JTextField) e.getComponent();
    t.select(0,100);}
   public void focusLost(FocusEvent e) {
      double v,w ;
      try{
      for (int i=0; i<4;i++){
         v = Double.parseDouble(ff[0][i].getText());
         w = Double.parseDouble(ff[1][i].getText());
         fo.setParamXY(i,v,w);
      }
      }
      catch (NumberFormatException nfe){
      JOptionPane.showMessageDialog(f_edit,"Bad number format in data entry.","Warning!", JOptionPane.ERROR_MESSAGE);
      return;}
      
      rdrChange = true;
      bChanged = true;
      pDraw.repaint();
      pSpec.repaint();
      pRpt.setTable();

   }
   public void actionPerformed(ActionEvent e){
     fo.use = cbFoil.isSelected();
     rdrChange = true;
     bChanged = true;
     pDraw.repaint();
     pSpec.repaint();
     pRpt.setTable();
   }
   
   }//end editFoil

   }// end pnlRudder

   
   class pnlSailplan extends JPanel{
      JTabbedPane pDisp;
      sailDraw pDraw;
      sailSpec pSpec;
      sailData pData;
      sailArea pRpt;
      Rig pRig;
      Border bSail;
      pspTabOrder to;
      boolean rigChange;
      
   public pnlSailplan(){
      
      if (hull.rig.valid) pRig = (Rig) hull.rig.clone();
      else pRig = new Rig();
      rigChange = false;
      
      bSail = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
      setLayout(new BorderLayout());
      
      pDisp = new JTabbedPane();
      
      pDraw = new sailDraw(750,325);
      pDraw.setBackground(Color.white) ;
      pDraw.setBorder(bSail);
      pDisp.add(pDraw,"Drawing");
      
      pSpec = new sailSpec(750,325);
      pSpec.setBackground(Color.white) ;
      pSpec.setBorder(bSail);
      pDisp.add(pSpec,"Dimensions");
      
      add(pDisp,BorderLayout.CENTER);

      pRpt = new sailArea(180,200);
      add(pRpt,BorderLayout.LINE_END);


      pData = new sailData(750,185);
      //pData.setBackground(Color.white) ;
      pData.setBorder(bSail);
      add(pData,BorderLayout.PAGE_END);

      to = new pspTabOrder(this);
      f_sailplan.setFocusTraversalPolicy(to);

      f_sailplan.addWindowListener(new WindowAdapter()
      {public void windowClosing(WindowEvent e){
            if(rigChange){  
            int n = JOptionPane.showConfirmDialog(f_sailplan,"Data has changed. Do you wish to apply changes?","Sailplan Design",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){saveRig();}}
            f_sailplan.setVisible(false);}}); 

      pDraw.repaint();
      pSpec.repaint();
      pRpt.setTable();

   
   }// end constructor

   public void saveRig(){
     hull.rig = (Rig) pRig.clone();
     hull.bChanged = true;
   }
   
   public class pspTabOrder extends FocusTraversalPolicy{
   sailData p;
   public pspTabOrder(pnlSailplan s){
      pnlSailplan psp = s;
      p = s.pData;
   }
   public Component getComponentAfter(Container focusCycleRoot, Component aComponent){
      editSail s;
      if (aComponent.equals(p.rbL)) return p.rbR;
      else if (aComponent.equals(p.btnInc)) return p.btnDec;
      else if (aComponent.equals(p.btnDec)) return p.rbTackX;
      else if (aComponent.equals(p.rbTackX)) return p.rbTackZ;
      else if (aComponent.equals(p.rbTackZ)) return p.rbLuffLen;
      else if (aComponent.equals(p.rbLuffLen)) return p.rbLuffAng;
      else if (aComponent.equals(p.rbLuffAng)) return p.rbBoomLen;
      else if (aComponent.equals(p.rbBoomLen)) return p.rbBoomAng;
      else if (aComponent.equals(p.rbBoomAng)) return p.rbGaffLen;
      else if (aComponent.equals(p.rbGaffLen)) return p.rbGaffAng;
      else if (aComponent.equals(p.rbGaffAng)) return p.rbRoachMax;
      else if (aComponent.equals(p.rbRoachMax)) return p.rbRoachPct;
      else if (aComponent.equals(p.rbRoachPct)) return p.rbAll;
      else if (aComponent.equals(p.rbAll)) return p.cbxInc;
      else if (aComponent.getParent().equals(p.cbxInc)) return p.rbL;
      else if (p.tp.getSelectedIndex() == 0) s = p.pMain;
      else if (p.tp.getSelectedIndex() == 1) s = p.pJib;
      else if (p.tp.getSelectedIndex() == 2) s = p.pMiz;
      else{return p.rbL;}
      
      if (aComponent.equals(p.rbR)) {return s.cbSail;}
      else if (aComponent.equals(s.cbSail)) {return s.cbGaff;}
      else if (aComponent.equals(s.cbGaff)) {return s.cbRoach;}
      else if (aComponent.equals(s.cbRoach)) {return s.sf[0][0];}
      
      for (int i=0; i<5 ; i++){
         if(aComponent.equals(s.sf[0][i])){return s.sf[1][i];}
         if(aComponent.equals(s.sf[1][i])){
            int j = i + 1;
            if (j > 4) return p.btnInc ; 
            else return s.sf[0][j];}
      }      
   return p.rbL;
   }
   public Component getComponentBefore(Container focusCycleRoot, Component aComponent){
      editSail s;
      if (aComponent.equals(p.rbR)) {return p.rbL;}
      else if (aComponent.equals(p.rbL)) return p.cbxInc;
      else if (aComponent.getParent().equals(p.cbxInc)) return p.rbAll;
      else if (aComponent.equals(p.rbAll)) return p.rbRoachPct;
      else if (aComponent.equals(p.rbRoachPct)) return p.rbRoachMax;
      else if (aComponent.equals(p.rbRoachMax)) return p.rbGaffAng;
      else if (aComponent.equals(p.rbGaffAng)) return p.rbGaffLen;
      else if (aComponent.equals(p.rbGaffLen)) return p.rbBoomAng;
      else if (aComponent.equals(p.rbBoomAng)) return p.rbBoomLen;
      else if (aComponent.equals(p.rbBoomLen)) return p.rbLuffAng;
      else if (aComponent.equals(p.rbLuffAng)) return p.rbLuffLen;
      else if (aComponent.equals(p.rbLuffLen)) return p.rbTackZ;
      else if (aComponent.equals(p.rbTackZ)) return p.rbTackX;
      else if (aComponent.equals(p.rbTackX)) return p.btnDec;
      else if (aComponent.equals(p.btnDec)) return p.btnInc;
      else if (p.tp.getSelectedIndex() == 0) s = p.pMain;
      else if (p.tp.getSelectedIndex() == 1) s = p.pJib;
      else if (p.tp.getSelectedIndex() == 2) s = p.pMiz;
      else{return p.rbL;}

      if (aComponent.equals(s.cbSail)) {return p.rbR;}
      else if (aComponent.equals(s.cbGaff)) {return s.cbSail;}
      else if (aComponent.equals(s.cbRoach)) {return s.cbGaff;}
      else if (aComponent.equals(s.sf[0][0])) {return s.cbRoach;}
      else if (aComponent.equals(p.btnInc)) {return s.sf[1][4];}
      
      for (int i=0; i<5 ; i++){
         if(aComponent.equals(s.sf[1][4-i])){return s.sf[0][4-i];}
         if(aComponent.equals(s.sf[0][4-i])){
            int j = i + 1;
            if (j > 4) return s.cbRoach ; 
            else return s.sf[1][4-j];}
      }      
      
   return p.rbL;
   }
   public Component getLastComponent(Container focusCycleRoot){
      if (p.tp.getSelectedIndex() == 0) return p.pMain.sf[1][4];
      else if (p.tp.getSelectedIndex() == 1) return p.pJib.sf[1][4];
      else if (p.tp.getSelectedIndex() == 2) return p.pMiz.sf[1][4];
   return p.rbL;   }

   public Component getFirstComponent(Container focusCycleRoot){
      if (p.tp.getSelectedIndex() == 0) return p.pMain.sf[0][0];
      else if (p.tp.getSelectedIndex() == 1) return p.pJib.sf[0][0];
      else if (p.tp.getSelectedIndex() == 2) return p.pMiz.sf[0][0];
   return p.rbL;   }

   public Component getDefaultComponent(Container focusCycleRoot){
      if (p.tp.getSelectedIndex() == 0) return p.pMain.sf[0][0];
      else if (p.tp.getSelectedIndex() == 1) return p.pJib.sf[0][0];
      else if (p.tp.getSelectedIndex() == 2) return p.pMiz.sf[0][0];
   return p.rbL;}
   }// end pspTabOrder

   class sailSpec extends JPanel{
   Dimension d;
   public sailSpec(int x, int y){
      d = new Dimension(x,y);
   }
   public Dimension getPreferredSize(){return d ; }
   
   protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    
     Sail ts;
     int il = 25;
     int isp = 20;
     String s;
     double d;
     
     int ic = 160;
     int icw = 60;
     g.drawString("Co-ord",ic,il);
     ic = ic + 2 * icw;
     g.drawString("to: Clew",ic-15,il);
     ic = ic + icw;
     g.drawString("Roach",ic,il);
     ic = ic + icw;
     g.drawString("Peak",ic,il);
     ic = ic + icw;
     g.drawString("Head/Throat",ic-15,il);

     il = il + isp + 5;
     
     for (int is=0; is < 3; is++){
        
        ic = 50;
        icw = 60;
        
        if (is == 0){ 
           ts = pRig.main;
           s = "Main";}
        else if (is == 1) {
           ts = pRig.jib;
           s = "Jib";}
        else {ts = pRig.mizzen;     
           s = "Mizzen";}
           
           if (ts.use){

           g.drawString(s,ic,il);
           ic = 100;
           g.drawString("Tack:",ic,il);
           ic = ic + icw;
           s = bcf.DF2d.format(ts.getX(ts.TACK))+", "+bcf.DF2d.format(ts.getY(ts.TACK));  
           g.drawString(s,ic,il);

           ic = ic + 2 * icw;
           d = length(ts.getX(ts.TACK),ts.getY(ts.TACK),ts.getX(ts.CLEW),ts.getY(ts.CLEW));
           g.drawString(bcf.DF2d.format(d),ic,il);
           ic = ic + icw;
           d = length(ts.getX(ts.TACK),ts.getY(ts.TACK),ts.getX(ts.ROACH),ts.getY(ts.ROACH));
           if (ts.useRoach) g.drawString(bcf.DF2d.format(d),ic,il);
           ic = ic + icw;
           d = length(ts.getX(ts.TACK),ts.getY(ts.TACK),ts.getX(ts.PEAK),ts.getY(ts.PEAK));
           if (ts.useGaff) g.drawString(bcf.DF2d.format(d),ic,il);
           ic = ic + icw;
           d = length(ts.getX(ts.TACK),ts.getY(ts.TACK),ts.getX(ts.THROAT),ts.getY(ts.THROAT));
           g.drawString(bcf.DF2d.format(d),ic,il);
           
           il = il + isp;
           ic = 100;
           if (ts.useGaff) g.drawString("Throat:",ic,il);
           else g.drawString("Head:",ic,il);
           ic = ic + icw;
           s = bcf.DF2d.format(ts.getX(ts.THROAT))+", "+bcf.DF2d.format(ts.getY(ts.THROAT));  
           g.drawString(s,ic,il);

           ic = ic + 2 * icw;
           d = length(ts.getX(ts.THROAT),ts.getY(ts.THROAT),ts.getX(ts.CLEW),ts.getY(ts.CLEW));
           g.drawString(bcf.DF2d.format(d),ic,il);
           ic = ic + icw;
           d = length(ts.getX(ts.THROAT),ts.getY(ts.THROAT),ts.getX(ts.ROACH),ts.getY(ts.ROACH));
           if (ts.useRoach) g.drawString(bcf.DF2d.format(d),ic,il);
           ic = ic + icw;
           d = length(ts.getX(ts.THROAT),ts.getY(ts.THROAT),ts.getX(ts.PEAK),ts.getY(ts.PEAK));
           if (ts.useGaff) g.drawString(bcf.DF2d.format(d),ic,il);
           
           if(ts.useGaff){
           il = il + isp;
           ic = 100;
           g.drawString("Peak:",ic,il);
           ic = ic + icw;
           s = bcf.DF2d.format(ts.getX(ts.PEAK))+", "+bcf.DF2d.format(ts.getY(ts.PEAK));  
           g.drawString(s,ic,il);
                      
           ic = ic + 2 * icw;
           d = length(ts.getX(ts.PEAK),ts.getY(ts.PEAK),ts.getX(ts.CLEW),ts.getY(ts.CLEW));
           if (ts.useGaff) g.drawString(bcf.DF2d.format(d),ic,il);
           ic = ic + icw;
           d = length(ts.getX(ts.PEAK),ts.getY(ts.PEAK),ts.getX(ts.ROACH),ts.getY(ts.ROACH));
           if (ts.useRoach) g.drawString(bcf.DF2d.format(d),ic,il);
           }
           
           if (ts.useRoach){
           il = il + isp;
           ic = 100;
           g.drawString("Roach:",ic,il);
           ic = ic + icw;
           s = bcf.DF2d.format(ts.getX(ts.ROACH))+", "+bcf.DF2d.format(ts.getY(ts.ROACH));  
           g.drawString(s,ic,il);

           ic = ic + 2 * icw;
           d = length(ts.getX(ts.ROACH),ts.getY(ts.ROACH),ts.getX(ts.CLEW),ts.getY(ts.CLEW));
           g.drawString(bcf.DF2d.format(d),ic,il);
           }
           
           il = il + isp;
           ic = 100;
           g.drawString("Clew:",ic,il);
           ic = ic + icw;
           s = bcf.DF2d.format(ts.getX(ts.CLEW))+", "+bcf.DF2d.format(ts.getY(ts.CLEW));  
           g.drawString(s,ic,il);
            }// end use        
           il = il + 2 * isp;
           
     }
    
   }
   
   double length(double x1, double y1, double x2, double y2){
   return Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2)) ;
   }
   
   }// ends sailSpec
   
   class sailDraw extends JPanel{
   Dimension d;
   public sailDraw(int x, int y){
      d = new Dimension(x,y);
   }
   public Dimension getPreferredSize(){return d ; }
   
   protected void paintComponent(Graphics g) {
    super.paintComponent(g);

     double mx = getWidth() ;
     double my = getHeight() ;
     int ix = (int) mx;
     int iy = (int) my;
     int xb = (int) 100;
     int yb = (int) my-10;

     g.clearRect(0,0,ix,iy) ;
     
     if (!hull.valid){ 
        g.drawString("Hull not defined.",50,50);
        return;
     }
     
     if (pRig.getMaxX() == pRig.getMinX() || pRig.getMaxY() == pRig.getMinY()){
        g.drawString("Data incomplete.",50,50);
        return;
     }

     double max_x = Math.max(pRig.getMaxX(),hull.gx_max);
     double min_x = Math.min(pRig.getMinX(),hull.gx_min);
     double max_y = Math.max(pRig.getMaxY(),hull.gz_max);
     double min_z = Math.min(pRig.getMinY(),hull.gz_min);
     
     double rx = (mx - 200.0)/(max_x - min_x);
     double ry = (my - 25.0)/(max_y - min_z);
     double r = Math.min(rx,ry);
    
     int iu, iv, iw, iz; 
     int hx, hy, cx, cy;
    
    
    
     // draw waterline
     iu = xb + (int) (r * (hull.gx_min - min_x));
     iv = yb - (int) (r * (0.0 - min_z));
     iw = xb + (int) (r * (hull.gx_max - min_x));
     iz = yb - (int) (r * (0.0 - min_z));
     g.setColor(Color.blue);
     g.drawLine(iu,iv,iw,iz);
     
     // draw hull profile
     g.setColor(Color.black);
     double z1Lo = hull.gz_max; 
     double z1Hi = hull.gz_min; 
     double x1 = 0;
     for (double pct=0.0; pct<=1.0025; pct=pct+0.01){
        double x = hull.gx_min + pct * (hull.gx_max - hull.gx_min);
        SortedSet ss = hull.getStnSet(x,0.0);
        Iterator si = ss.iterator();
        double zLo = hull.gz_max;
        double zHi = hull.gz_min;
        boolean bOk = false;
        while(si.hasNext()){
           Point p = (Point) si.next();
           zLo = Math.min(zLo, p.z);
           zHi = Math.max(zHi, p.z);
           bOk = true;
           }
       if (bOk && pct > 0.0){
          iu = xb + (int) (r * (x1 - min_x));
          iv = yb - (int) (r * (z1Lo - min_z));
          iw = xb + (int) (r * (x - min_x));
          iz = yb - (int) (r * (zLo - min_z));
          g.drawLine(iu,iv,iw,iz);
          iv = yb - (int) (r * (z1Hi - min_z));
          iz = yb - (int) (r * (zHi - min_z));
          g.drawLine(iu,iv,iw,iz);
          }
          
       x1 = x;    
       z1Lo = zLo;
       z1Hi = zHi;
     
     }
     
     // draw stems
     g.setColor(Color.lightGray);
     for (int iSL=0;iSL<=1;iSL++){
        if (hull.bStems[iSL] && hull.sLines[iSL].valid){
        double x = hull.sLines[iSL].hPoints[0].getX(); 
        double y = hull.sLines[iSL].hPoints[0].getZ(); 
        iu = xb + (int) (r * (x - min_x)) ;
        iv = yb - (int) (r * (y - min_z)) ;
        for (int j=1; j<hull.sLines[iSL].hPoints.length;j++){
          x = hull.sLines[iSL].hPoints[j].getX(); 
          y = hull.sLines[iSL].hPoints[j].getZ(); 
          iw = xb + (int) (r * (x - min_x)) ;
          iz = yb - (int) (r * (y - min_z)) ;
          g.drawLine(iu,iv,iw,iz);
          iu = iw;
          iv = iz;
        }        
     }}

     // draw sail 
     Sail ts;
     g.setColor(Color.black);
     double min_z_base = min_z-hull.base;

     for (int is=0; is < 3; is++){
     if (is == 0) ts = pRig.main;
     else if (is == 1) ts = pRig.jib;
     else ts = pRig.mizzen;     
     
     
     if (ts.use){
        iu = xb + (int) (r * (ts.getX(Sail.TACK) - min_x));
        iv = yb - (int) (r * (ts.getY(Sail.TACK) - min_z_base));
        iw = xb + (int) (r * (ts.getX(Sail.THROAT) - min_x));
        iz = yb - (int) (r * (ts.getY(Sail.THROAT) - min_z_base));   
        g.drawLine(iu,iv,iw,iz);
    
        if (ts.useGaff){
           iu = xb + (int) (r * (ts.getX(Sail.PEAK) - min_x));
           iv = yb - (int) (r * (ts.getY(Sail.PEAK) - min_z_base));
           g.drawLine(iw,iz,iu,iv);
           hx = iu;
           hy = iv;}
        else{   
           hx = iw;
           hy = iz;
        }

        if (ts.useRoach){
           iu = xb + (int) (r * (ts.getX(Sail.LEECH) - min_x));
           iv = yb - (int) (r * (ts.getY(Sail.LEECH) - min_z_base));
           g.drawLine(hx,hy,iu,iv);
           cx = xb + (int) (r * (ts.getX(Sail.CLEW) - min_x));
           cy = yb - (int) (r * (ts.getY(Sail.CLEW) - min_z_base));
           g.drawLine(iu,iv,cx,cy);
           g.setColor(Color.gray);
           g.drawLine(hx,hy,cx,cy);
           g.setColor(Color.black);}
        else{
           cx = xb + (int) (r * (ts.getX(Sail.CLEW) - min_x));
           cy = yb - (int) (r * (ts.getY(Sail.CLEW) - min_z_base));
           g.drawLine(hx,hy,cx,cy);
         }   
        
        iw = xb + (int) (r * (ts.getX(Sail.TACK) - min_x));
        iz = yb - (int) (r * (ts.getY(Sail.TACK) - min_z_base));
        g.drawLine(cx,cy,iw,iz);

        cx = xb + (int) (r * (ts.getAreaX() - min_x));
        cy = yb - (int) (r * (ts.getAreaY() - min_z_base));
        g.drawArc(cx-5,cy-5,10,10,0,360);
        g.drawLine(cx+5,cy,cx-5,cy);
        g.drawLine(cx,cy+5,cx,cy-5);
  
     } 
     } 
      
     double a = pRig.getArea();
     if (a > 0){
        cx = xb + (int) (r * (pRig.getAreaX() - min_x));
        cy = yb - (int) (r * (pRig.getAreaY() - min_z_base));
     
        g.drawLine(cx+4,cy+4,cx-4,cy+4);
        g.drawLine(cx+4,cy-4,cx-4,cy-4);
        g.drawLine(cx-4,cy-4,cx-4,cy+4);
        g.drawLine(cx+4,cy-4,cx+4,cy+4);
        g.drawLine(cx+6,cy,cx-6,cy);
        g.drawLine(cx,cy+6,cx,cy-6);
     }
      
   } // end paintComponent  
   
   }// end sailDraw

   class sailArea extends JPanel{
   Dimension d;
   //bcLabel bclMArea, bclJArea, bclZArea, bclTArea;
   //bcLabel bclMCoA, bclJCoA, bclZCoA, bclTCoA;
   JLabel jlMArea, jlJArea, jlZArea, jlTArea;
   JLabel jlMCoA, jlJCoA, jlZCoA, jlTCoA;
   JButton jbApply, jbClose;
   
      public sailArea(int x, int y){
      d = new Dimension(x,y);
      setLayout(new GridLayout(0,2));
      add(new bcLabel("Sail Area",JLabel.LEFT));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));

      add(new bcLabel("Main - Area: ",JLabel.RIGHT));
      add(jlMArea = new JLabel("0.0",JLabel.LEFT));
      
      add(new bcLabel("CoA: ",JLabel.RIGHT));
      add(jlMCoA = new JLabel("0.0,0.0",JLabel.LEFT));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      
      add(new bcLabel("Jib - Area: ",JLabel.RIGHT));
      add(jlJArea = new JLabel("0.0",JLabel.LEFT));
      
      add(new bcLabel("CoA: ",JLabel.RIGHT));
      add(jlJCoA = new JLabel("0.0,0.0",JLabel.LEFT));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      
      add(new bcLabel("Mizzen - Area: ",JLabel.RIGHT));
      add(jlZArea = new JLabel("0.0",JLabel.LEFT));
      
      add(new bcLabel("CoA: ",JLabel.RIGHT));
      add(jlZCoA = new JLabel("0.0,0.0",JLabel.LEFT));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      
      add(new bcLabel("Total - Area: ",JLabel.RIGHT));
      add(jlTArea = new JLabel("0.0",JLabel.LEFT));
      
      add(new bcLabel("CoA: ",JLabel.RIGHT));
      add(jlTCoA = new JLabel("0.0,0.0",JLabel.LEFT));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));

      jbApply = new JButton("Apply");
      jbClose = new JButton("Close");
      
      jbApply.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e)
           { saveRig();rigChange=false;}
      });
            
      jbClose.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e)
           {if(rigChange){  
            int n = JOptionPane.showConfirmDialog(f_edit,"Data has changed. Do you wish to apply changes?","Data Edit",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){saveRig();rigChange=false;}}
            f_sailplan.setVisible(false);}
            });
      
      add(jbApply);
      add(jbClose);
      
      }
   public Dimension getPreferredSize(){return d ; }

   protected void setTable() {
    String s;
    double wa = 0;
    double wx = 0;
    double wy = 0;
    
      if(pRig.main.use){
         jlMArea.setText(bcf.DF1d.format(hull.units.coefArea()*pRig.main.getArea())+hull.units.lblArea());
         s = bcf.DF1d.format(pRig.main.getAreaX())+", "+bcf.DF1d.format(pRig.main.getAreaY());
         jlMCoA.setText(s);
         wa = wa + pRig.main.getArea();
         wx = wx + pRig.main.getArea() * pRig.main.getAreaX();
         wy = wy + pRig.main.getArea() * pRig.main.getAreaY();
      }
      else{
         jlMArea.setText("0.00");
         jlMCoA.setText("-.--, -.--");
      }
      if(pRig.jib.use){
         jlJArea.setText(bcf.DF1d.format(hull.units.coefArea()*pRig.jib.getArea())+hull.units.lblArea());
         s = bcf.DF1d.format(pRig.jib.getAreaX())+", "+bcf.DF1d.format(pRig.jib.getAreaY());
         jlJCoA.setText(s);
         wa = wa + pRig.jib.getArea();
         wx = wx + pRig.jib.getArea() * pRig.jib.getAreaX();
         wy = wy + pRig.jib.getArea() * pRig.jib.getAreaY();
      }
      else{
         jlJArea.setText("0.00");
         jlJCoA.setText("-.--, -.--");
      }
      if(pRig.mizzen.use){
         jlZArea.setText(bcf.DF1d.format(hull.units.coefArea()*pRig.mizzen.getArea())+hull.units.lblArea());
         s = bcf.DF1d.format(pRig.mizzen.getAreaX())+", "+bcf.DF1d.format(pRig.mizzen.getAreaY());
         jlZCoA.setText(s);
         wa = wa + pRig.mizzen.getArea();
         wx = wx + pRig.mizzen.getArea() * pRig.mizzen.getAreaX();
         wy = wy + pRig.mizzen.getArea() * pRig.mizzen.getAreaY();
      }
      else{
         jlZArea.setText("0.00");
         jlZCoA.setText("-.--, -.--");
      }
   
      if(wa > 0){
         jlTArea.setText(bcf.DF1d.format(wa));
         jlTArea.setText(bcf.DF1d.format(hull.units.coefArea()*wa)+hull.units.lblArea());
         s = bcf.DF1d.format(wx / wa)+", "+bcf.DF1d.format(wy / wa);
         jlTCoA.setText(s);
      }
      else{
         jlTArea.setText("0.00");
         jlTCoA.setText("-.--, -.--");
      }
   }
   
   }// end sailArea
   
   class sailData extends JPanel implements ActionListener{
   Dimension d;
   JTabbedPane tp = new JTabbedPane();
   JRadioButton rbL, rbR;
   JRadioButton rbTackX, rbTackZ;
   JRadioButton rbLuffLen, rbLuffAng;
   JRadioButton rbBoomLen, rbBoomAng;
   JRadioButton rbGaffLen, rbGaffAng;
   JRadioButton rbRoachMax, rbRoachPct;
   JRadioButton rbAll;
   JComboBox cbxInc;
   editSail pMain;
   editSail pJib;
   editSail pMiz;
   JButton btnInc, btnDec;
   
   public sailData(int x, int y) {
      JPanel pRb;
      JLabel lRb;
      
      d = new Dimension(x,y);
      setLayout(new BorderLayout());
      JPanel pDir = new JPanel();
      pDir.setLayout(new GridLayout(0,1));
      rbL = new JRadioButton("Left");
      rbL.addActionListener(this);
      rbL.setVerticalAlignment(SwingConstants.BOTTOM);
      rbR = new JRadioButton("Right");
      rbR.addActionListener(this);
      rbL.setVerticalAlignment(SwingConstants.TOP);
      ButtonGroup bGrp = new ButtonGroup();
      bGrp.add(rbL);
      bGrp.add(rbR);
      pDir.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      pDir.add(rbL);
      pDir.add(rbR);
      pDir.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
   
      if (pRig.dir < 0) rbL.setSelected(true);
      if (pRig.dir > 0) rbR.setSelected(true);

      add(pDir,BorderLayout.LINE_START);
      
      pMain = new editSail(pRig.main);
      tp.addTab("Main",pMain);
      pJib = new editSail(pRig.jib);
      pJib.cbGaff.setSelected(false);
      pJib.cbRoach.setSelected(false);
      tp.addTab("Jib",pJib);
      pMiz = new editSail(pRig.mizzen);
      tp.addTab("Mizzen",pMiz);
      add(tp,BorderLayout.CENTER);

      JPanel pInc = new JPanel();
      pInc.setPreferredSize(new Dimension(x-5,(3*y)/10));
      pInc.setLayout(new GridLayout(0,8));
      
      btnInc = new JButton("Increase");
      btnInc.addActionListener(this);
      pInc.add(btnInc);
      
      lRb = new JLabel("Tack:");
      lRb.setHorizontalAlignment(SwingConstants.RIGHT);
      pInc.add(lRb);
      pRb = new JPanel();      
      rbTackX = new JRadioButton("X");
      rbTackZ = new JRadioButton("Z");
      pRb.add(rbTackX);
      pRb.add(rbTackZ);
      pInc.add(pRb);
      
      lRb = new JLabel("Luff:");
      lRb.setHorizontalAlignment(SwingConstants.RIGHT);
      pInc.add(lRb);
      pRb = new JPanel();      
      rbLuffLen = new JRadioButton("L");
      rbLuffAng = new JRadioButton("A");
      pRb.add(rbLuffLen);
      pRb.add(rbLuffAng);
      pInc.add(pRb);
      
      lRb = new JLabel("Foot:");
      lRb.setHorizontalAlignment(SwingConstants.RIGHT);
      pInc.add(lRb);
      pRb = new JPanel();      
      rbBoomLen = new JRadioButton("L");
      rbBoomAng = new JRadioButton("A");
      pRb.add(rbBoomLen);
      pRb.add(rbBoomAng);
      pInc.add(pRb);
      pInc.add(new JLabel("Step",SwingConstants.CENTER));
      
      btnDec = new JButton("Decrease");
      btnDec.addActionListener(this);
      pInc.add(btnDec);
      
      lRb = new JLabel("Gaff:");
      lRb.setHorizontalAlignment(SwingConstants.RIGHT);
      pInc.add(lRb);
      pRb = new JPanel();      
      rbGaffLen = new JRadioButton("L");
      rbGaffAng = new JRadioButton("A");
      pRb.add(rbGaffLen);
      pRb.add(rbGaffAng);
      pInc.add(pRb);

      lRb = new JLabel("Roach:");
      lRb.setHorizontalAlignment(SwingConstants.RIGHT);
      pInc.add(lRb);
      pRb = new JPanel();      
      rbRoachMax = new JRadioButton("M");
      rbRoachPct = new JRadioButton("%");
      pRb.add(rbRoachMax);
      pRb.add(rbRoachPct);
      pInc.add(pRb);
      
      lRb = new JLabel("Scale:");
      lRb.setHorizontalAlignment(SwingConstants.RIGHT);
      pInc.add(lRb);
      pRb = new JPanel(); 
      rbAll = new JRadioButton("%");
      pRb.add(rbAll);
      pRb.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      pInc.add(pRb);
      
      ButtonGroup bgInc = new ButtonGroup();
      bgInc.add(rbTackX);
      bgInc.add(rbTackZ);
      bgInc.add(rbLuffLen);
      bgInc.add(rbLuffAng);
      bgInc.add(rbBoomLen);
      bgInc.add(rbBoomAng);
      bgInc.add(rbGaffLen);
      bgInc.add(rbGaffAng);
      bgInc.add(rbRoachMax);
      bgInc.add(rbRoachPct);
      bgInc.add(rbAll);

      String[] incs = {"0.01","0.02","0.05","0.1","0.2","0.5","1","2","5","10","20","50","100"};
      cbxInc = new JComboBox(incs);
      cbxInc.setEditable(true);
      cbxInc.setSelectedIndex(6);
      pInc.add(cbxInc);
      
      add(pInc,BorderLayout.PAGE_END);
      
   } // end constructor
   public Dimension getPreferredSize(){return d ; }

   public void actionPerformed(ActionEvent e){

      if (e.getSource() == rbL || e.getSource() == rbR){
      if(rbL.isSelected()) {
         pRig.dir = -1;
         pRig.main.setDir(-1);
         pRig.jib.setDir(-1);
         pRig.mizzen.setDir(-1);
      }
      if(rbR.isSelected()) {
         pRig.dir = +1;
         pRig.main.setDir(1);
         pRig.jib.setDir(1);
         pRig.mizzen.setDir(1);
      }
      }
      
      if (e.getSource() == btnInc || e.getSource() == btnDec){
      double sgn, v, d;
      Sail s = pRig.main;
      editSail eS = pMain;
      if (tp.getSelectedIndex() == 1) {s = pRig.jib; eS = pJib;}
      else if (tp.getSelectedIndex() == 2) {s = pRig.mizzen; eS = pMiz;}
      if (e.getSource() == btnInc) sgn = 1.0;
      else sgn = -1.0;

      try{d = Double.parseDouble((String)cbxInc.getSelectedItem());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret step as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}

      if(rbTackX.isSelected()){
      try{v = Double.parseDouble(eS.sf[0][0].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = Math.max(v + sgn * d,0.0);
      eS.sf[0][0].setText(bcf.DF2d.format(v));
      s.setX(v);
      }

      if(rbTackZ.isSelected()){
      try{v = Double.parseDouble(eS.sf[1][0].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = Math.max(v + sgn * d,0.0);
      eS.sf[1][0].setText(bcf.DF2d.format(v));
      s.setY(v);
      }

      if(rbLuffLen.isSelected()){
      try{v = Double.parseDouble(eS.sf[0][1].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = Math.max(v + sgn * d,0.0);
      eS.sf[0][1].setText(bcf.DF2d.format(v));
      s.setLuffLen(v);
      }

      if(rbLuffAng.isSelected()){
      try{v = Double.parseDouble(eS.sf[1][1].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eS.sf[1][1].setText(bcf.DF2d.format(v));
      s.setLuffAng(v);
      }
      
      if(rbBoomLen.isSelected()){
      try{v = Double.parseDouble(eS.sf[0][2].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = Math.max(v + sgn * d,0.0);
      eS.sf[0][2].setText(bcf.DF2d.format(v));
      s.setBoomLen(v);
      }

      if(rbBoomAng.isSelected()){
      try{v = Double.parseDouble(eS.sf[1][2].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eS.sf[1][2].setText(bcf.DF2d.format(v));
      s.setBoomAng(v);
      }
      
      if(rbGaffLen.isSelected()){
      try{v = Double.parseDouble(eS.sf[0][3].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = Math.max(v + sgn * d,0.0);
      eS.sf[0][3].setText(bcf.DF2d.format(v));
      s.setGaffLen(v);
      }

      if(rbGaffAng.isSelected()){
      try{v = Double.parseDouble(eS.sf[1][3].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = v + sgn * d;
      eS.sf[1][3].setText(bcf.DF2d.format(v));
      s.setGaffAng(v);
      }

      if(rbRoachMax.isSelected()){
      try{v = Double.parseDouble(eS.sf[0][4].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = Math.max(v + sgn * d,0.0);
      eS.sf[0][4].setText(bcf.DF2d.format(v));
      s.setRoachMax(v);
      }

      if(rbRoachPct.isSelected()){
      try{v = Double.parseDouble(eS.sf[1][4].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = Math.max(v + sgn * d,0.0);
      eS.sf[1][4].setText(bcf.DF2d.format(v));
      s.setRoachPct(v);
      }

      if(rbAll.isSelected()){
      try{v = Double.parseDouble(eS.sf[0][1].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = Math.max(v + 0.01 * sgn * d * v,0.0);
      eS.sf[0][1].setText(bcf.DF2d.format(v));
      s.setLuffLen(v);         
      try{v = Double.parseDouble(eS.sf[0][2].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = Math.max(v + 0.01 * sgn * d * v,0.0);
      eS.sf[0][2].setText(bcf.DF2d.format(v));
      s.setBoomLen(v);
            try{v = Double.parseDouble(eS.sf[0][3].getText());
       } catch (NumberFormatException nfe){
         JOptionPane.showMessageDialog(null,"Unable to interpret value as number.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;}
      v = Math.max(v + 0.01 * sgn * d * v,0.0);
      eS.sf[0][3].setText(bcf.DF2d.format(v));
      s.setGaffLen(v);   
         
      }  
      } // end btn proc
      rigChange = true; 
      pDraw.repaint();
      pSpec.repaint();
      pRpt.setTable();
   }
   }// end sailData
  
   class editSail extends JPanel  implements DocumentListener, ItemListener, FocusListener, ActionListener {
   JTextField[][] sf;
   JCheckBox cbSail, cbGaff, cbRoach;
   Dimension d;
   Sail so;
   boolean bChanged;
   
   public editSail(Sail s){
   super (new GridLayout(0,11));
   d = new Dimension(600,200);
   Font spFont = new Font("Serif", Font.BOLD, 14);
   so = s;

   sf = new JTextField[2][5];
   for (int i=0; i<5;i++){
      sf[0][i] = new JTextField(Double.toString(s.getParamX(i)));
      sf[0][i].getDocument().addDocumentListener(this);
      sf[0][i].addFocusListener(this);
      sf[1][i] = new JTextField(Double.toString(s.getParamY(i)));
      sf[1][i].getDocument().addDocumentListener(this);
      sf[1][i].addFocusListener(this);
   }
   JLabel lbl;
   // row 1, labels
   cbSail = new JCheckBox("Use");
   cbSail.setHorizontalAlignment(SwingConstants.LEFT);
   cbSail.setSelected(so.use);
   cbSail.addActionListener(this);
   add(cbSail);
   lbl = new JLabel("Tack",JLabel.RIGHT);
   lbl.setFont(spFont);
   add(lbl);
   add(new Box.Filler(new Dimension(2,2),new Dimension(2,2),new Dimension(2,2)));
   lbl = new JLabel("Luff",JLabel.RIGHT);
   lbl.setFont(spFont);
   add(lbl);
   add(new Box.Filler(new Dimension(2,2),new Dimension(2,2),new Dimension(2,2)));
   lbl = new JLabel("Foot",JLabel.RIGHT);
   lbl.setFont(spFont);
   add(lbl);
   add(new Box.Filler(new Dimension(2,2),new Dimension(2,2),new Dimension(2,2)));
   add(new Box.Filler(new Dimension(2,2),new Dimension(2,2),new Dimension(2,2)));
   cbGaff = new JCheckBox("Gaff");
   cbGaff.setHorizontalAlignment(SwingConstants.CENTER);
   cbGaff.setSelected(so.useGaff);
   cbGaff.addActionListener(this);
   add(cbGaff);
   add(new Box.Filler(new Dimension(2,2),new Dimension(2,2),new Dimension(2,2)));
   cbRoach = new JCheckBox("Roach");
   cbRoach.setHorizontalAlignment(SwingConstants.CENTER);
   cbRoach.setSelected(so.useRoach);
   cbRoach.addActionListener(this);
   add(cbRoach);
//   add(new Box.Filler(new Dimension(2,2),new Dimension(2,2),new Dimension(2,2)));

   // row 2, data
   add(new Box.Filler(new Dimension(2,2),new Dimension(2,2),new Dimension(2,2)));
   lbl = new JLabel("X: ",JLabel.RIGHT);
   lbl.setFont(spFont);
   add(lbl);
   add(sf[0][0]);
   lbl = new JLabel("Len: ",JLabel.RIGHT);
   lbl.setFont(spFont);
   add(lbl);
   add(sf[0][1]);
   lbl = new JLabel("Len: ",JLabel.RIGHT);
   lbl.setFont(spFont);
   add(lbl);
   add(sf[0][2]);
   lbl = new JLabel("Len: ",JLabel.RIGHT);
   lbl.setFont(spFont);
   add(lbl);
   add(sf[0][3]);
   lbl = new JLabel("Max %: ",JLabel.RIGHT);
   lbl.setFont(spFont);
   add(lbl);
   add(sf[0][4]);
//   add(new Box.Filler(new Dimension(2,2),new Dimension(2,2),new Dimension(2,2)));

   // row 3, data
   add(new Box.Filler(new Dimension(2,2),new Dimension(2,2),new Dimension(2,2)));
   lbl = new JLabel("Z: ",JLabel.RIGHT);
   lbl.setFont(spFont);
   add(lbl);
   add(sf[1][0]);
   lbl = new JLabel("Rake: ",JLabel.RIGHT);
   lbl.setFont(spFont);
   add(lbl);
   add(sf[1][1]);
   lbl = new JLabel("Ang: ",JLabel.RIGHT);
   lbl.setFont(spFont);
   add(lbl);
   add(sf[1][2]);
   lbl = new JLabel("Ang: ",JLabel.RIGHT);
   lbl.setFont(spFont);
   add(lbl);
   add(sf[1][3]);
   lbl = new JLabel("Hgt %: ",JLabel.RIGHT);
   lbl.setFont(spFont);
   add(lbl);
   add(sf[1][4]);
   /*
   //row 4, spaces
   add(new Box.Filler(new Dimension(2,2),new Dimension(2,2),new Dimension(2,2)));
   //  add(new Box.Filler(new Dimension(2,2),new Dimension(2,2),new Dimension(2,2)));
   */
   }
   public Dimension getPreferredSize(){return d ; }
   public void changedUpdate(DocumentEvent e) {rigChange = true; bChanged=true;}
   public void insertUpdate(DocumentEvent e) {rigChange = true; bChanged=true;}
   public void removeUpdate(DocumentEvent e) {rigChange = true; bChanged=true;}
   public void itemStateChanged(ItemEvent e) {rigChange = true; bChanged=true;}
   public void focusGained(FocusEvent e) {
    JTextField t = (JTextField) e.getComponent();
    t.select(0,100);}
   public void focusLost(FocusEvent e) {
      double v,w ;
      try{
      for (int i=0; i<5;i++){
         v = Double.parseDouble(sf[0][i].getText());
         w = Double.parseDouble(sf[1][i].getText());
         so.setParam(i,v,w);
      }
      }
      catch (NumberFormatException nfe){
      JOptionPane.showMessageDialog(f_edit,"Bad number format in data entry.","Warning!", JOptionPane.ERROR_MESSAGE);
      return;}
      pDraw.repaint();
      pSpec.repaint();
      pRpt.setTable();
   }
   public void actionPerformed(ActionEvent e){
     so.setUse(cbSail.isSelected()) ;
     so.setUseGaff(cbGaff.isSelected()) ;
     so.setUseRoach(cbRoach.isSelected()) ;
     rigChange = true;
     pDraw.repaint();
     pSpec.repaint();
     pRpt.setTable();
   }
   
   }//end editSail

   }// end pnlSailplan

   
   class pnlDataEntry extends JPanel implements DocumentListener, ItemListener, FocusListener {
      JPanel de = new JPanel();
      JTabbedPane tp = new JTabbedPane();
      
      boolean bChanged = false;
      String DName;
      String NA;
      JLabel lblDName;
      JLabel lblNA;
      JTextField[] stn = new JTextField[hull.Stations.length];
      JButton btnHName, btnNA, btnName, btnInsert, btnAdd, btnDele, btnSave, btnClose;
      
   public pnlDataEntry() {
      super (new GridLayout(1,1));
      JLabel lbl;
      Font deFont = new Font("Serif", Font.BOLD, 14);
      de.setLayout(new BorderLayout());

      DName = hull.boatname;
      NA = hull.designer;
      lblDName = new JLabel(DName);
      lblDName.setFont(deFont);
      lblNA = new JLabel(NA);
      lblNA.setFont(deFont);

      JPanel ttl = new JPanel();
      ttl.setLayout(new GridLayout(0,3));
      ttl.add(lblDName);
      ttl.add(lblNA);
      ttl.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      de.add(ttl,BorderLayout.PAGE_START);
      
      JPanel pO = new JPanel();
      pO.setLayout(new GridLayout(0,6));
  
      pO.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));

      lbl = new JLabel("  Station  ",JLabel.CENTER);
      lbl.setFont(deFont);
      pO.add(lbl);
      lbl = new JLabel("  Value  ",JLabel.LEFT);
      lbl.setFont(deFont);
      pO.add(lbl);
      pO.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      pO.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      pO.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      
      
      for (int i=0;i<hull.Stations.length;i++){
         
        pO.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));

        lbl = new JLabel(Integer.toString(i),JLabel.CENTER);
        lbl.setFont(deFont);
        stn[i] = new JTextField(Double.toString(hull.Stations[i]));
        stn[i].getDocument().addDocumentListener(this);
        stn[i].addFocusListener(this);
        
        lbl.setLabelFor(stn[i]);
        pO.add(lbl);
        pO.add(stn[i]);
        pO.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
        pO.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
        pO.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      
      }
      tp.addTab("Stations",pO);

      edLinePanel pL = new edLinePanel(stn.length);
      int iL = 0;
      ListIterator l = hull.Offsets.listIterator() ;
      while (l.hasNext()){rawLine rL = (rawLine) l.next();
         Line ln = (Line) rL.ln;
       
         pL = new edLinePanel(stn.length);
      
         for (int i=0;i<hull.Stations.length;i++){
           
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
         tp.addTab(rL.lnName,pL);
         iL++;
      } 

      Border bcBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
      de.setBorder(BorderFactory.createTitledBorder(bcBorder));

      de.add(tp,BorderLayout.CENTER);
      
      JPanel bp = new JPanel();
      bp.setLayout(new FlowLayout());
      
      btnHName = new JButton("Design Name");
      btnHName.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e)
         { String s = JOptionPane.showInputDialog("Design Name:",DName); 
             if ((s != null) && (s.length() > 0)) {DName=s;  lblDName.setText("Design: "+DName); bChanged=true;}    
            }});
      
      btnNA = new JButton("Designer");
      btnNA.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e)
         { String s = JOptionPane.showInputDialog("Designer:",NA); 
             if ((s != null) && (s.length() > 0)) {NA=s;  lblNA.setText("Designer: "+NA); bChanged=true;}    
            }});
      
      btnName = new JButton("Line Name");
      btnName.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e)
           {  int i = tp.getSelectedIndex();
              if (i == 0){
                JOptionPane.showMessageDialog(f_edit,"Name 'Stations' cannot be changed.","Warning!", JOptionPane.ERROR_MESSAGE);
                return;}
            
             String s = JOptionPane.showInputDialog("Name:"); 
             if ((s != null) && (s.length() > 0)) {tp.setTitleAt(i,s); bChanged=true;}    
            }});

      btnInsert = new JButton("Insert Line");
      btnInsert.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e)
           {int n = JOptionPane.showConfirmDialog(f_edit,"Insert additional Line?","Data Edit",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){
                 String s = JOptionPane.showInputDialog(f_edit,"Name:");
                 if ((s != null) && (s.length() > 0)) {addLine(1,s);}
            }}});
            
      btnAdd = new JButton("Add Line");
      btnAdd.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e)
           {int n = JOptionPane.showConfirmDialog(f_edit,"Add additional Line?","Data Edit",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){
                 String s = JOptionPane.showInputDialog(f_edit,"Name:"); 
                 if ((s != null) && (s.length() > 0)) {addLine(0,s);}
            }}});
            
      btnDele = new JButton("Delete Line");
      btnDele.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e)
           {  int i = tp.getSelectedIndex();
              if (i == 0){
                JOptionPane.showMessageDialog(f_edit,"Stations pannel cannot be deleted.","Warning!", JOptionPane.ERROR_MESSAGE);
                return;}
            
            int n = JOptionPane.showConfirmDialog(f_edit,"Delete Selected line?","Data Edit",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){tp.remove(i);bChanged=true;}
             
            }});

      
      btnSave = new JButton("Apply");
      btnSave.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e)
           { saveEdit();}
      });

            
      btnClose = new JButton("Close");
      btnClose.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e)
           {if(bChanged){  
            int n = JOptionPane.showConfirmDialog(f_edit,"Data has changed. Do you wish to apply changes?","Data Edit",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){saveEdit();}}
            f_edit.setVisible(false);}
            });

      bp.add(btnHName);
      bp.add(btnNA);
      bp.add(btnName);
      bp.add(btnInsert);
      bp.add(btnAdd);
      bp.add(btnDele);
      bp.add(btnSave);
      bp.add(btnClose);
      
      //de.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)),BorderLayout.PAGE_START);

      de.add(bp,BorderLayout.PAGE_END);
	   f_edit.addWindowListener(new WindowAdapter()
      {public void windowClosing(WindowEvent e){
            if(bChanged){  
            int n = JOptionPane.showConfirmDialog(f_edit,"Data has changed. Do you wish to apply changes?","Data Edit",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){saveEdit();}}
            f_edit.setVisible(false);}}); 
      
      add(de);
      
      f_edit.setFocusTraversalPolicy(new pdeTabOrder(this));
      
   }//end constructor PnlDataEntry
   

   public void addLine(int it, String name){
   
                 // add new line

                 edLinePanel pL = new edLinePanel(stn.length);
                 
                 for (int i=0;i<hull.Stations.length;i++){
                    
                    pL.x[i].setText("00");
                    pL.x[i].setText(stn[i].getText());
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
                if (it < 1) tp.addTab(name,pL);
                else{ it = Math.max(1,tp.getSelectedIndex());
                      tp.insertTab(name,null,pL,"new tab",it); 
                }
                tp.setSelectedComponent(pL);
                bChanged=true;}  // end addLine  
   
   class edLinePanel extends JPanel implements ActionListener{
   public JTextField[] x ;
   public JTextField[] y ;
   public JTextField[] z ;
   public JCheckBox[] v;
   public int nf;
   public JButton btnWtr, btnButt;
      
      public edLinePanel(int n){
         
      Font lpFont = new Font("Serif", Font.BOLD, 14);
      JLabel lbl; 
      JCheckBox cb;
      nf = n;
      setLayout(new GridLayout(0,6));
  
      lbl = new JLabel("   #  ",JLabel.CENTER);
      lbl.setFont(lpFont);
      add(lbl);
      
      lbl = new JLabel("  Station  ",JLabel.CENTER);
      lbl.setFont(lpFont);
      add(lbl);

      lbl = new JLabel("  Breadth  ",JLabel.CENTER);
      lbl.setFont(lpFont);
      add(lbl);

      lbl = new JLabel("  Height  ",JLabel.CENTER);
      lbl.setFont(lpFont);
      add(lbl);
      
      lbl = new JLabel("  In Use  ",JLabel.LEFT);
      lbl.setFont(lpFont);
      add(lbl);

      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
         
      x = new JTextField[n];   
      y = new JTextField[n];   
      z = new JTextField[n];   
      v = new JCheckBox[n];   
      
      for (int i=0;i<n;i++){
         
           lbl = new JLabel(Integer.toString(i),JLabel.CENTER);
           lbl.setFont(lpFont);
           add(lbl);
           
           x[i] = new JTextField();
           add(x[i]);

           y[i] = new JTextField();
           add(y[i]);

           z[i] = new JTextField();
           add(z[i]);
           
           v[i] = new JCheckBox();
           add(v[i]);
           if (i == 1){
                 btnWtr = new JButton("Waterline");
                 btnWtr.addActionListener(this);
                 add(btnWtr);      
           }
           else if(i == 3){
                 btnButt = new JButton("Buttock");
                 btnButt.addActionListener(this);
                 add(btnButt);      
           }
           else {
           add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
           }
         }
      
      }// end constructor   
      public void actionPerformed(ActionEvent e){
      JButton btn = (JButton) e.getSource();
      boolean bOpt;
      String prompt;
      double val;
      if (0 == btn.getText().compareTo("Waterline")){ 
         bOpt = true ;
         prompt = "Height from baseline: ";}
      else{ 
         bOpt = false ;
         prompt = "Breadth from centerline: ";}

      String s = JOptionPane.showInputDialog(f_edit,prompt);
      if (s == null) {return;}

      try{
      val = Double.parseDouble(s);
	   } catch (NumberFormatException nfe){
      JOptionPane.showMessageDialog(f_edit,"Bad number format.","Warning!", JOptionPane.ERROR_MESSAGE);
      return ;}

      for (int i=0;i<nf;i++){
      if (bOpt) z[i].setText(s);
      else y[i].setText(s);
      }
      }
   }// end edLinePanel
   
   //public Dimension getPreferredSize(){return new Dimension(700,200) ; }
   public void changedUpdate(DocumentEvent e) {bChanged=true;}
   public void insertUpdate(DocumentEvent e) {bChanged=true;}
   public void removeUpdate(DocumentEvent e) {bChanged=true;}
   public void itemStateChanged(ItemEvent e) {bChanged=true;}
   public void focusGained(FocusEvent e) {
    JTextField t = (JTextField) e.getComponent();
    t.select(0,100);}
   public void focusLost(FocusEvent e) {}
    
   public void saveEdit(){
   double[] sta = new double[hull.Stations.length];
   ArrayList o = new ArrayList() ;
   
   try{
   for (int i=0; i<sta.length; i++){
      sta[i] = Double.parseDouble(stn[i].getText());
   } 
	} catch (NumberFormatException nfe){
   JOptionPane.showMessageDialog(f_edit,"Bad number format in Stations entries.","Warning!", JOptionPane.ERROR_MESSAGE);
   return ;}
   
   Point[] p;
   int iLine;
   double x, y, z;
   
   for (int j=1;j < tp.getTabCount();j++){

      edLinePanel pL = (edLinePanel) tp.getComponentAt(j);            

      try{
         
         p = new Point[stn.length];
         for (iLine=0;iLine<p.length;iLine++)p[iLine]=new Point();
         
         for (int i = 0;i<stn.length;i++){
         
         x = Double.parseDouble(pL.x[i].getText());
         y = Double.parseDouble(pL.y[i].getText());
         z = Double.parseDouble(pL.z[i].getText());
         p[i] = new Point(x,y,z);
         p[i].valid = pL.v[i].isSelected();
         }
      rawLine rL = new rawLine();
      rL.ln = new Line(p);
      rL.lnName = tp.getTitleAt(j);
      o.add(rL);}
      catch (NumberFormatException nfe){
      JOptionPane.showMessageDialog(f_edit,"Bad number format in Line entries"+j+".","Warning!", JOptionPane.ERROR_MESSAGE);
      return;}
         
      }// end j
      
      hull.Offsets = o;
      hull.Stations = sta;
      hull.setLines();
      hull.bChanged = true;
      hull.valid = (o.size() > 0);
      hull.designer = NA;
      hull.boatname = DName;
      bChanged=false;
      setCtrls();
   return;
   
   }// end saveEdit
   
   
   public class pdeTabOrder extends FocusTraversalPolicy{
   pnlDataEntry pde;
   
   public pdeTabOrder(pnlDataEntry de){
      pde = de;
   }
   
   public Component getComponentAfter(Container focusCycleRoot, Component aComponent){

      if(aComponent.equals(pde.btnHName)){return pde.btnNA;}
      if(aComponent.equals(pde.btnNA)){return pde.btnName;}
      if(aComponent.equals(pde.btnName)){return pde.btnInsert;}
      if(aComponent.equals(pde.btnInsert)){return pde.btnAdd;}
      if(aComponent.equals(pde.btnAdd)){return pde.btnDele;}
      if(aComponent.equals(pde.btnDele)){return pde.btnSave;}
      if(aComponent.equals(pde.btnSave)){return pde.btnClose;}
      
      if (pde.tp.getSelectedIndex() == 0){
         if(aComponent.equals(pde.btnClose)){return pde.stn[0];}
         for (int i=1; i<pde.stn.length; i++){
            if(aComponent.equals(pde.stn[i-1])){return pde.stn[i];}
        }
        return pde.btnHName;
      }
      else{
         edLinePanel pL = (edLinePanel) pde.tp.getSelectedComponent();

         if(aComponent.equals(pde.btnClose)){return pL.x[0];}
         if(aComponent.equals(pL.btnWtr)){return pL.btnButt;}
         if(aComponent.equals(pL.btnButt)){return pde.btnHName;}
         
         for (int i=0; i<pL.x.length; i++){
            if(aComponent.equals(pL.x[i])){return pL.y[i];}
            if(aComponent.equals(pL.y[i])){return pL.z[i];}
            if(aComponent.equals(pL.z[i])){return pL.v[i];}
            if(aComponent.equals(pL.v[i])){
              int j = i+1;
              if (j > pL.x.length-1) return pL.btnWtr;
              return pL.x[j];}
        }
      }
   return aComponent;}
   
   public Component getComponentBefore(Container focusCycleRoot, Component aComponent){

      if(aComponent.equals(pde.btnNA)){return pde.btnHName;}
      if(aComponent.equals(pde.btnName)){return pde.btnNA;}
      if(aComponent.equals(pde.btnInsert)){return pde.btnName;}
      if(aComponent.equals(pde.btnAdd)){return pde.btnInsert;}
      if(aComponent.equals(pde.btnDele)){return pde.btnAdd;}
      if(aComponent.equals(pde.btnSave)){return pde.btnDele;}
      if(aComponent.equals(pde.btnClose)){return pde.btnSave;}

      if (pde.tp.getSelectedIndex() == 0){
         if(aComponent.equals(pde.btnHName)){return pde.stn[pde.stn.length-1];}
         for (int i=0; i<pde.stn.length-1; i++){
            if(aComponent.equals(pde.stn[i+1])){return pde.stn[i];}
        }
        return pde.btnClose;
      }
      else{
         edLinePanel pL = (edLinePanel) pde.tp.getSelectedComponent();
         if(aComponent.equals(pde.btnHName)){return pL.btnButt;}
         if(aComponent.equals(pL.btnButt)){return pL.btnWtr;}
         if(aComponent.equals(pL.btnWtr)){return pL.v[pL.v.length-1];}
         
         for (int i=0; i<pL.x.length; i++){
            if(aComponent.equals(pL.v[i])){return pL.z[i];}
            if(aComponent.equals(pL.z[i])){return pL.y[i];}
            if(aComponent.equals(pL.y[i])){return pL.x[i];}
            if(aComponent.equals(pL.x[i])){
              int j = i-1;
              if (j < 0) return pde.btnClose;
              return pL.v[j];}
        }
      }
    return aComponent;
   }

   public Component getLastComponent(Container focusCycleRoot){
   return pde.btnClose;   }

   public Component getFirstComponent(Container focusCycleRoot){
   return pde.btnClose;   }

   public Component getDefaultComponent(Container focusCycleRoot){
   return pde.btnClose;}
   }// end pdeTabOrder

   }//end pnlDataEntry
   
   
   public void wgtEdit(){
     f_wgts = new JFrame("Weight Entry/Edit");
     f_wgts.setSize(600,400);
     pnlWgtEntry w_wgts = new pnlWgtEntry();
     f_wgts.getContentPane().add(w_wgts) ;
     w_wgts.getWgts();
     f_wgts.setVisible(true);
   }
   
   class pnlWgtEntry extends JPanel{
   edWgtPanel wp;
   public pnlWgtEntry(){
   JPanel p = new JPanel();
   p.setBorder(BorderFactory.createEtchedBorder()) ;
   p.setLayout(new BorderLayout());
   p.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)),BorderLayout.PAGE_START);
   
   wp = new edWgtPanel();
   p.add(wp,BorderLayout.CENTER);

      JButton btnSave = new JButton("Apply Changes");
      btnSave.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e)
           {applyWgts();dispWgt.setWeights();}
            });
            
      JButton btnClose = new JButton("Close");
      btnClose.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e)
           {if(wp.bChanged){  
            int n = JOptionPane.showConfirmDialog(f_wgts,"Data has changed. Do you wish to apply changes?","Weight Edit",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){applyWgts();dispWgt.setWeights();}}
            f_wgts.setVisible(false);}
            });
   JPanel bp = new JPanel();
   bp.add(btnSave);
   bp.add(btnClose);
   p.add(bp,BorderLayout.PAGE_END);
   add(p);

     f_wgts.addWindowListener(new WindowAdapter()
      {public void windowClosing(WindowEvent e){
            if(wp.bChanged){  
            int n = JOptionPane.showConfirmDialog(f_edit,"Data has changed. Do you wish to apply changes?","Data Edit",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){applyWgts();dispWgt.setWeights();}}
            f_wgts.setVisible(false);
      }}); 
   }

   public void applyWgts(){
      String[] tl = new String[10];
      double[] tw = new double[10];
      double[] tx = new double[10];
      double[] ty = new double[10];
      double[] tz = new double[10];
      try{
   for (int i = 0; i< 10;i++){
      tl[i] = wp.l[i].getText();
      tw[i] = Double.parseDouble(wp.w[i].getText());
      tx[i] = Double.parseDouble(wp.x[i].getText());
      ty[i] = Double.parseDouble(wp.y[i].getText());
      tz[i] = Double.parseDouble(wp.z[i].getText());
      hull.wgtLbl = tl;
      hull.wgtWgt = tw;
      hull.wgtX = tx;
      hull.wgtY = ty;
      hull.wgtZ = tz;
      }}catch (NumberFormatException e){
      JOptionPane.showMessageDialog(f_wgts,"Bad number format!","Warning!", JOptionPane.ERROR_MESSAGE);
      }
      wp.bChanged = false;
   }// end applyWgts
   
   public void getWgts(){
   for (int i = 0; i< 10;i++){
      wp.l[i].setText(hull.wgtLbl[i]);
      wp.w[i].setText(Double.toString(hull.wgtWgt[i]));
      wp.x[i].setText(Double.toString(hull.wgtX[i]));
      wp.y[i].setText(Double.toString(hull.wgtY[i]));
      wp.z[i].setText(Double.toString(hull.wgtZ[i]));
      wp.bChanged=false;
   }
   }// end getWgts
   
   
   }// end pnlWgtEntry
   
   class edWgtPanel extends JPanel implements DocumentListener, FocusListener {
   public JTextField[] l ;
   public JTextField[] w ;
   public JTextField[] x ;
   public JTextField[] y ;
   public JTextField[] z ;
   boolean bChanged;
   JLabel lblTot = new JLabel("CoG:");
   JLabel lblWgt = new JLabel("n/a");
   JLabel lblX = new JLabel("n/a");
   JLabel lblY = new JLabel("n/a");
   JLabel lblZ = new JLabel("n/a");
      
   public edWgtPanel(){
         
      JLabel lbl; 
      JCheckBox cb;

      Font wpFont = new Font("Serif", Font.BOLD, 14);
      setBorder(BorderFactory.createEtchedBorder()) ;
      setLayout(new GridLayout(0,7));
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
  
      lbl = new JLabel(" Type ",JLabel.CENTER);
      lbl.setFont(wpFont);
      add(lbl);
      
      lbl = new JLabel("  Weight  ",JLabel.CENTER);
      lbl.setFont(wpFont);
      add(lbl);

      lbl = new JLabel("  Station  ",JLabel.CENTER);
      lbl.setFont(wpFont);
      add(lbl);

      lbl = new JLabel("  Breadth  ",JLabel.CENTER);
      lbl.setFont(wpFont);
      add(lbl);

      lbl = new JLabel("  Height  ",JLabel.CENTER);
      lbl.setFont(wpFont);
      add(lbl);
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      

         
      l = new JTextField[10];   
      w = new JTextField[10];   
      x = new JTextField[10];   
      y = new JTextField[10];   
      z = new JTextField[10];   
      
      for (int i=0;i<10;i++){
         
           lbl = new JLabel(Integer.toString(i+1),JLabel.CENTER);
           lbl.setFont(wpFont);
           add(lbl);
           
           l[i] = new JTextField();
           l[i].getDocument().addDocumentListener(this);
           add(l[i]);

           w[i] = new JTextField();
           w[i].getDocument().addDocumentListener(this);
           w[i].addFocusListener(this);
           add(w[i]);

           x[i] = new JTextField();
           x[i].getDocument().addDocumentListener(this);
           x[i].addFocusListener(this);
           add(x[i]);

           y[i] = new JTextField();
           y[i].getDocument().addDocumentListener(this);
           y[i].addFocusListener(this);
           add(y[i]);

           z[i] = new JTextField();
           z[i].getDocument().addDocumentListener(this);
           z[i].addFocusListener(this);
           add(z[i]);
           
           add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
           
         }
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      lblTot.setFont(wpFont);
      lblWgt.setFont(wpFont);
      lblX.setFont(wpFont);
      lblY.setFont(wpFont);
      lblZ.setFont(wpFont);
      add(lblTot); 
      add(lblWgt); 
      add(lblX); 
      add(lblY); 
      add(lblZ); 
      add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
      
      }// end constructor 
      
   public void addWgts(){
   double tw = 0;
   double tx = 0;
   double ty = 0;
   double tz = 0;   
   try{
      for (int i=0;i<10;i++){
       tw += Double.parseDouble(w[i].getText()); 
       tx += Double.parseDouble(w[i].getText()) * Double.parseDouble(x[i].getText()); 
       ty += Double.parseDouble(w[i].getText()) * Double.parseDouble(y[i].getText()); 
       tz += Double.parseDouble(w[i].getText()) * Double.parseDouble(z[i].getText()); 
      }
      
      lblWgt.setText(bcf.DF1d.format(tw));
      if(tw>0){
      lblX.setText(bcf.DF1d.format(tx/tw));
      lblY.setText(bcf.DF1d.format(ty/tw));
      lblZ.setText(bcf.DF1d.format(tz/tw));
      }
      else{
      lblX.setText("n/a");
      lblY.setText("n/a");
      lblZ.setText("n/a");
      }
      
   }catch (NumberFormatException e){
      lblWgt.setText("n/a");
      lblX.setText("n/a");
      lblY.setText("n/a");
      lblZ.setText("n/a");
   }
      
   }// end addWgts   
   public void changedUpdate(DocumentEvent e) {addWgts();  bChanged = true;}
   public void insertUpdate(DocumentEvent e) {addWgts(); bChanged = true;}
   public void removeUpdate(DocumentEvent e) {addWgts(); bChanged = true;}
   public void focusGained(FocusEvent e) {
    JTextField t = (JTextField) e.getComponent();
    t.select(0,100);}
   public void focusLost(FocusEvent e) {}      
   }// end edWgtPanel

   public void insertStation(){
   
   try{
    if(f_edit.isVisible()){
      JOptionPane.showMessageDialog(null,"Data entry windows should be closed before changing the number of stations.","Warning!", JOptionPane.ERROR_MESSAGE);
      return;}
   } catch(Exception e){System.out.println(e);}
   double newStn;
   try{
   String s = JOptionPane.showInputDialog("New station at:");
   if (s == null) return;
   newStn = Double.parseDouble(s);
   } catch (NumberFormatException nfe){
     JOptionPane.showMessageDialog(null,"Unable to interpret number.","Warning!", JOptionPane.ERROR_MESSAGE);
     return;}
      
   boolean bDone = false;

   double[] sta = new double[hull.Stations.length+1];
   ArrayList o  = new ArrayList();

   int i, j;

   j = 0;
   for (i=0;i < hull.Stations.length;i++){
      if (newStn == hull.Stations[i]){
       JOptionPane.showMessageDialog(null,"New station must not equal old.","Warning!", JOptionPane.ERROR_MESSAGE);
       return;
      }
      
      if (newStn < hull.Stations[i] && !bDone){
         sta[j] = newStn;
         bDone = true; j++;}
      sta[j] = hull.Stations[i];
      j++;
   }   
   
   if (!bDone) sta[j] = newStn;

   
   Point[] p;
   Point q;
   int iLine;
   
   
    ListIterator l;
    l = hull.Offsets.listIterator() ;
    while (l.hasNext()){
       rawLine rL = (rawLine) l.next();
       Line ln = rL.ln;
       p = new Point[sta.length];
       
       bDone = false;
       j = 0;
       for (i=0;i < hull.Stations.length;i++){
          q = ln.getPoint(i);
          
          if (newStn < q.x && !bDone){
            p[j] = new Point(newStn,0.0,0.0);
            bDone = true; j++;}
          p[j] = q;
          j++;
       }
       if (!bDone) p[j] = new Point(newStn,0.0,0.0); 
       rawLine rLnew = new rawLine();
       rLnew.ln = new Line(p);
       rLnew.lnName = rL.lnName;
       o.add(rLnew);
   
   }
   hull.Offsets = o;
   hull.Stations = sta;
   hull.setLines();
   hull.bChanged = true;
   body.repaint();  
   plan.repaint();  
   setCtrls();
   
   
   } // end insertStation

   public void deleteStation(){
   
      try{
    if(f_edit.isVisible()){
      JOptionPane.showMessageDialog(null,"Data entry windows should be closed before changing the number of stations.","Warning!", JOptionPane.ERROR_MESSAGE);
      return;}
   } catch(Exception e){System.out.println(e);}
   int dStn;
   try{
   String s = JOptionPane.showInputDialog("Delete Station #:");
   if (s == null) return;
   dStn = Integer.parseInt(s);
   if (dStn >= hull.Stations.length || dStn < 0){
     JOptionPane.showMessageDialog(null,"Station # out of range.","Warning!", JOptionPane.ERROR_MESSAGE);
     return;}
   
   } catch (NumberFormatException nfe){
     JOptionPane.showMessageDialog(null,"Unable to interpret number.","Warning!", JOptionPane.ERROR_MESSAGE);
     return;}
      
   double[] sta = new double[hull.Stations.length-1];
   ArrayList o  = new ArrayList();

   int i;

   for (i=0;i < hull.Stations.length;i++){
      
      if (i < dStn) sta[i] = hull.Stations[i];
      if (i > dStn) sta[i-1] = hull.Stations[i];
   }   
   
   Point[] p;
   Point q;
   int iLine;
   
   
    ListIterator l;
    l = hull.Offsets.listIterator() ;
    while (l.hasNext()){
       rawLine rL = (rawLine) l.next();
       Line ln = rL.ln;
       p = new Point[sta.length];
       
       for (i=0;i < hull.Stations.length;i++){
          q = ln.getPoint(i);
          if (i < dStn) p[i] = q;
          if (i > dStn) p[i-1] = q;
          }
       
       rawLine rLnew = new rawLine();
       rLnew.ln = new Line(p);
       rLnew.lnName = rL.lnName;
       o.add(rLnew);
   
   }
   hull.Offsets = o;
   hull.Stations = sta;
   hull.setLines();
   hull.bChanged = true;
   body.repaint();  
   plan.repaint();  

   setCtrls();

   
   
   } // end deleteStation
   
   class pnlAbout extends JPanel{
   public pnlAbout(){
      JLabel lbl; 
      Font wpFont = new Font("Serif", Font.BOLD, 14);
      setBorder(BorderFactory.createEtchedBorder()) ;
      setLayout(new GridLayout(0,1));
      lbl = new JLabel ("boatCalc");
      lbl.setFont(wpFont);
      lbl.setHorizontalAlignment(SwingConstants.CENTER);
      add(lbl);
      lbl = new JLabel ("Copyright 2004 by Peter H. Vanderwaart");
      lbl.setFont(wpFont);
      lbl.setHorizontalAlignment(SwingConstants.CENTER);
      add(lbl);
      lbl = new JLabel ("Version 0.2e - 03/04/2004");
      lbl.setFont(wpFont);
      lbl.setHorizontalAlignment(SwingConstants.CENTER);
      add(lbl);
   }
   }// end pnlAbout
   
} // end CLASS boatCalc

/* Programming Utilities */


class bcLabel extends JLabel{
   public bcLabel(String s, int l){
   setText(s);
   setHorizontalAlignment(l);
   setFont(new Font("Serif", Font.BOLD, 12));}
}   

class SaveOutput extends PrintStream {
    static OutputStream logfile;
    static PrintStream oldStdout;
    static PrintStream oldStderr;

    SaveOutput(PrintStream ps) {
	super(ps);
    }

    // Starts copying stdout and 
    //stderr to the file f.
    public static void start(
     String f) throws IOException {
	// Save old settings.
	oldStdout = System.out;
	oldStderr = System.err;

	// Create/Open logfile.
	logfile = new PrintStream(
	    new BufferedOutputStream(
	    new FileOutputStream(f)));

	// Start redirecting the output.
	System.setOut(
	 new SaveOutput(System.out));
	System.setErr(
	 new SaveOutput(System.err));
    }

    // Restores the original settings.
    public static void stop() {
	System.setOut(oldStdout);
	System.setErr(oldStderr);
        try {
	    logfile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // PrintStream override.
    public void write(int b) {
        try {
	    logfile.write(b);
        } catch (Exception e) {
            e.printStackTrace();
            setError();
        }
	super.write(b);
    }
    // PrintStream override.
    public void write(
     byte buf[], int off, int len) {
        try {
	    logfile.write(buf, off, len);
        } catch (Exception e) {
            e.printStackTrace();
            setError();
        }
	super.write(buf, off, len);
    }
} // end CLASS SaveOutput

class bcFormat{
DecimalFormat DF0d;
DecimalFormat DF1d;
DecimalFormat DF2d;
DecimalFormat DF3d;
DecimalFormat DF4d;

public bcFormat(){
Locale l = new Locale("en","US");   
//Locale l = new Locale("fr","FR");   
DecimalFormatSymbols dfs = new DecimalFormatSymbols(l);
DF0d =  new DecimalFormat("###,###,###",dfs);
DF1d =  new DecimalFormat("###,###,###.0",dfs);
DF2d =  new DecimalFormat("###,###,###.00",dfs);
DF3d =  new DecimalFormat("###,###,###.000",dfs);
DF4d =  new DecimalFormat("###,###,###.0000",dfs);
}// end constructor
}// enc bcFormat



