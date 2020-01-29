import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class bcUnits {
  String[] lMom = {" in.-lbs.", "ft.-lbs.", "cc-Kg", "m-Kg"};
  String[] lVol = {" cu. in.", " cu. ft.", " cc", " cu m"};
  String[] lWgt = {" lbs.", " lbs.", " Kg", " Kg"};
  int UNITS = 0;
  double[][] v2w;
  int WATER = 0;

  public bcUnits() {

    this.v2w = new double[2][4];
    this.v2w[0][0] = 64.0 / Math.pow(12.0, 3.0);
    this.v2w[0][1] = 64.0;
    this.v2w[0][2] = 0.0010256;
    this.v2w[0][3] = 1025.6;
    this.v2w[1][0] = 62.4 / Math.pow(12.0, 3.0);
    this.v2w[1][1] = 62.4;
    this.v2w[1][2] = 0.001;
    this.v2w[1][3] = 1000.0;

  }

  public double coefArea() {
    double r;
    if (this.UNITS == 0) {
      r = 1.0 / 144.0; // sq in to sq ft
    } else if (this.UNITS == 1) {
      r = 1.0;
    } else if (this.UNITS == 2) {
      r = 0.0001; // sq cm to sq m
    } else {
      r = 1.0;
    }
    return r;
  }// end coefArea

  public double coefPPI() {
    double r;
    if (this.UNITS == 0) {
      r = 1;
    } else if (this.UNITS == 1) {
      r = 0.083333; // =1/12
    } else if (this.UNITS == 2) {
      r = 1;
    } else {
      r = 0.01; // 1 cm
    }
    return r;
  }// end coefPPI

  public String lblArea() {
    String r;
    if (this.UNITS <= 1) {
      r = " sq. ft.";
    } else {
      r = " sq. m.";
    }
    return r;
  }// end lblArea

  public String lblMom() {
    return this.lMom[this.UNITS];
  }

  public String lblPPI() {
    String r;
    if (this.UNITS <= 1) {
      r = " ppi";
    } else {
      r = " Kg/cm";
    }
    return r;
  }// end lblPPI

  public String lblVol() {
    return this.lVol[this.UNITS];
  }

  public String lblWgt() {
    return this.lWgt[this.UNITS];
  }

  public double Vol2Ton() {
    double r;
    if (this.UNITS <= 1) {
      r = (this.v2w[this.WATER][this.UNITS] / 2240.0);
    } else {
      r = (this.v2w[this.WATER][this.UNITS] / 1000.0);
    }
    return r;
  }

  public double Vol2Wgt() {
    return this.v2w[this.WATER][this.UNITS];
  }



}
// end bcUnits


class Centerboard implements Cloneable {
  private double angle;
  public rscFoil board;
  private boolean changed;
  private double hx_min, hx_max;
  private double[] minHull;
  private final Point pivot;

  private final double[][] rCB;
  boolean valid = false;

  public Centerboard() {
    this.board = new rscFoil();
    this.board.setCB(true);
    this.pivot = new Point(0, 0, 0);
    this.rCB = new double[2][4];
    this.angle = 0;
    this.changed = true;
    this.valid = true;
  }

  @Override
  public Object clone() {

    try {
      final Centerboard r = (Centerboard) super.clone();
      r.board = (rscFoil) this.board.clone();
      return r;
    } catch (final CloneNotSupportedException e) {
      throw new InternalError(e.toString());
    }

  } // end clone

  public double getArea() {
    double a = 0;
    if (this.board.use) {
      a = a + this.board.getArea();
    }
    return a;
  }

  public double getAreaX() {
    double x = 0;
    if (this.board.use) {
      x = x + (this.board.getArea() * this.board.getAreaX());
    }
    final double a = this.getArea();
    if (a > 0) {
      x = x / a;
    }
    return x;
  }

  public double getAreaY() {
    double x = 0;
    if (this.board.use) {
      x = x + (this.board.getArea() * this.board.getAreaY());
    }
    final double a = this.getArea();
    if (a > 0) {
      x = x / a;
    }
    return x;
  }

  public double getMaxX() {
    double x = 0;
    if (this.board.use) {
      x = Math.max(x, this.board.getMaxX());
    }
    return x;
  }

  public double getMaxY() {
    double x = 0;
    if (this.board.use) {
      x = Math.max(x, this.board.getMaxY());
    }
    return x;
  }

  public double getMinX() {
    double x = 1000000;
    if (this.board.use) {
      x = Math.min(x, this.board.getMinX());
    }
    return x;
  }

  public double getMinY() {
    double x = 1000000;
    if (this.board.use) {
      x = Math.min(x, this.board.getMinY());
    }
    return x;
  }

  public double getPivotAngle() {
    return this.angle;
  }

  public double getPivotX() {
    return this.pivot.x;
  }

  public double getPivotZ() {
    return this.pivot.z;
  }

  public double getRX(final int i) {
    if (this.changed) {
      this.setCB();
    }
    return this.rCB[0][i];
  }

  public double getRZ(final int i) {
    if (this.changed) {
      this.setCB();
    }
    return this.rCB[1][i];
  }

  public void setBase(final double b) {
    this.board.setBase(b);
    this.changed = true;
  }

  private void setCB() {
    final double sinang = Math.sin(Math.toRadians(this.angle));
    final double cosang = Math.cos(Math.toRadians(this.angle));
    final double xp = this.pivot.x;
    final double yp = this.pivot.z + this.board.getBase();
    for (int i = 0; i < 4; i++) {
      final double x = this.board.getParamX(i);
      final double y = this.board.getParamY(i) + this.board.getBase();
      this.rCB[0][i] = (xp + (cosang * (x - xp))) - (sinang * (y - yp));
      this.rCB[1][i] = yp + (sinang * (x - xp)) + (cosang * (y - yp));
    }

    // computed wetted points
    // step one; circulate perimeter including mid-points
    // save points below keel or at keel
    // note max and min of points at keel

    final ArrayList wp = new ArrayList();
    // set up "prior" point
    final double ox = this.rCB[0][3];
    double tt = (100.0 * (ox - this.hx_min)) / (this.hx_max - this.hx_min);
    Math.min(Math.max((int) tt, 0), 99);

    for (int i = 0; i < 4; i++) {
      int k = i + 1;
      if (k == 1) {
        k = 0;
      }
      for (int j = 0; j <= 1; j++) {
        // calc location
        final double tx = this.rCB[0][i] + (j * 0.5 * (this.rCB[0][j] - this.rCB[0][i]));
        final double ty = this.rCB[1][i] + (j * 0.5 * (this.rCB[1][j] - this.rCB[1][i]));
        // find hull depth
        tt = (100.0 * (tx - this.hx_min)) / (this.hx_max - this.hx_min);
        final int ti = Math.min(Math.max((int) tt, 0), 99);
        if (tx < this.minHull[ti]) {
          wp.add(new Point(tx, 0, ty));
        }
      }
    }

    // skip adding additional points for now

    double tx = 0;
    double tz = 0;
    for (int i = 0; i < wp.size(); i++) {
      final Point p = (Point) wp.get(i);
      tx = tx + p.x;
      tz = tz + p.z;
    }
    tx = tx / Math.max(wp.size(), 1);
    tz = tz / Math.max(wp.size(), 1);

    final XZCompare xzComp = new XZCompare();
    xzComp.setAdj(tx, tz);
    this.board.setWetPts(xzComp);
    // board.sp = new TreeSet(xzComp);
    for (int i = 0; i < wp.size(); i++) {
      this.board.addWetPt(wp.get(i));
    }

    this.changed = false;
  }

  public void setMinHull(final double[] mH, final double xmin, final double xmax, final int incs) {
    this.minHull = mH;
    this.hx_min = xmin;
    this.hx_max = xmax;
    this.changed = true;
  }

  public void setPivotAngle(final double x) {
    this.angle = x;
    this.changed = true;
  }

  public void setPivotX(final double x) {
    this.pivot.x = x;
    this.changed = true;
  }

  public void setPivotZ(final double x) {
    this.pivot.z = x;
    this.changed = true;
  }

  public void setRX(final int i, final double x) {
    this.rCB[0][i] = x;
    this.changed = true;
  }

  public void setRZ(final int i, final double x) {
    this.rCB[1][i] = x;
    this.changed = true;
  }

} // end Centerboard


class hLine {
  Point[] hPoints;
  Interp hXY;
  Interp hXZ;
  boolean valid;

  // constructor
  public hLine(final Point[] p, final double b) {
    int i, j = -1;
    int n;
    final Point basePoint = new Point(0, 0, b);
    this.valid = false;
    // count valid points
    n = 0;
    for (i = 0; i < p.length; i++) {
      if (p[i].valid) {
        n++;
      }
    }

    this.hPoints = new Point[n];
    this.hXY = new Interp(n);
    this.hXZ = new Interp(n);

    for (i = 0; i < p.length; i++) {
      if (p[i].valid) {
        j++;
        this.hPoints[j] = p[i].plus(basePoint);
        this.hXY.setXY(j, p[i].x, this.hPoints[j].y);
        this.hXZ.setXY(j, p[i].x, this.hPoints[j].z);
        this.valid = true;
      }
    }

  } // end constructor

  public double max(final String D) {
    int i;
    double t_max = -1000000.0;
    for (i = 0; i < this.hPoints.length; i++) {
      if (this.hPoints[i].valid) {
        if (0 == D.compareTo("X")) {
          t_max = Math.max(t_max, this.hPoints[i].x);
        }
        if (0 == D.compareTo("Y")) {
          t_max = Math.max(t_max, this.hPoints[i].y);
        }
        if (0 == D.compareTo("Z")) {
          t_max = Math.max(t_max, this.hPoints[i].z);
        }
      }
    }
    return t_max;
  } // end max

  public double min(final String D) {
    int i;
    double t_min = +1000000.0;
    for (i = 0; i < this.hPoints.length; i++) {
      if (this.hPoints[i].valid) {
        if (0 == D.compareTo("X")) {
          t_min = Math.min(t_min, this.hPoints[i].x);
        }
        if (0 == D.compareTo("Y")) {
          t_min = Math.min(t_min, this.hPoints[i].y);
        }
        if (0 == D.compareTo("Z")) {
          t_min = Math.min(t_min, this.hPoints[i].z);
        }
      }
    }
    return t_min;
  } // end min


}


class Hull {

  class bcHandler extends DefaultHandler {
    rscFoil curFoil;
    Sail curSail;
    int iFoil;

    int iLine;
    int iSail;
    int iStn;
    int itag;
    int iWgt;
    Line l;
    int linecount = 0;
    String linename;
    Point[] p;
    StringBuffer sbBase, sbNA, sbDName, sbLName;
    StringBuffer sbFoilX, sbFoilY;
    StringBuffer sbLStem, sbRStem;
    StringBuffer sbRigX, sbRigY;
    StringBuffer sbStnX;
    StringBuffer sbWater, sbUnits;
    StringBuffer sbWL, sbWW, sbWX, sbWY, sbWZ;

    StringBuffer sbX, sbY, sbZ, sbV;
    String[] tags;

    boolean v = true;
    double x, y, z;


    @Override
    public void characters(final char buf[], final int offset, final int len) {
      final String s = new String(buf, offset, len);
      if (this.tags[this.itag].compareTo("stnX") == 0) {
        this.sbStnX.append(s);
      } else if (this.tags[this.itag].compareTo("baseline") == 0) {
        this.sbBase.append(s);
      } else if (this.tags[this.itag].compareTo("water") == 0) {
        this.sbWater.append(s);
      } else if (this.tags[this.itag].compareTo("lstem") == 0) {
        this.sbLStem.append(s);
      } else if (this.tags[this.itag].compareTo("rstem") == 0) {
        this.sbRStem.append(s);
      } else if (this.tags[this.itag].compareTo("units") == 0) {
        this.sbUnits.append(s);
      } else if (this.tags[this.itag].compareTo("designer") == 0) {
        this.sbNA.append(s);
      } else if (this.tags[this.itag].compareTo("designname") == 0) {
        this.sbDName.append(s);
      } else if (this.tags[this.itag].compareTo("linename") == 0) {
        this.sbLName.append(s);
      }

      else if (this.tags[this.itag].compareTo("valX") == 0) {
        this.sbX.append(s);
      } else if (this.tags[this.itag].compareTo("valY") == 0) {
        this.sbY.append(s);
      } else if (this.tags[this.itag].compareTo("valZ") == 0) {
        this.sbZ.append(s);
      } else if (this.tags[this.itag].compareTo("ptValid") == 0) {
        this.sbV.append(s);
      }


      else if (this.tags[this.itag].compareTo("wgtLbl") == 0) {
        this.sbWL.append(s.trim());
      } else if (this.tags[this.itag].compareTo("wgtWgt") == 0) {
        this.sbWW.append(s.trim());
      } else if (this.tags[this.itag].compareTo("wgtX") == 0) {
        this.sbWX.append(s.trim());
      } else if (this.tags[this.itag].compareTo("wgtY") == 0) {
        this.sbWY.append(s.trim());
      } else if (this.tags[this.itag].compareTo("wgtZ") == 0) {
        this.sbWZ.append(s.trim());
      }

      else if (this.tags[this.itag].compareTo("rigX") == 0) {
        this.sbRigX.append(s);
      } else if (this.tags[this.itag].compareTo("rigZ") == 0) {
        this.sbRigY.append(s);
      } else if (this.tags[this.itag].compareTo("foilX") == 0) {
        this.sbFoilX.append(s);
      } else if (this.tags[this.itag].compareTo("foilZ") == 0) {
        this.sbFoilY.append(s);
      }

    }

    @Override
    public void endDocument() throws SAXException {}

    @Override
    public void endElement(final String ns, final String n, final String qn) throws SAXException {
      String ts;
      this.itag--;
      if (qn.compareTo("valX") == 0) {
        this.x = Double.parseDouble(this.sbX.toString());
      } else if (qn.compareTo("valY") == 0) {
        this.y = Double.parseDouble(this.sbY.toString());
      } else if (qn.compareTo("valZ") == 0) {
        this.z = Double.parseDouble(this.sbZ.toString());
      } else if (qn.compareTo("ptValid") == 0) {
        ts = this.sbV.toString();
        if (ts.compareTo("true") == 0) {
          this.v = true;
        } else {
          this.v = false;
        }
      }

      else if (qn.compareTo("point") == 0) {
        this.p[this.iLine] = new Point(this.x, this.y, this.z, this.v);
      } else if (qn.compareTo("line") == 0) {
        final rawLine rL = new rawLine();
        rL.ln = new Line(this.p);
        rL.lnName = this.linename;
        Hull.this.Offsets.add(rL);
      } else if (qn.compareTo("stnX") == 0) {
        Hull.this.Stations[this.iStn] = Double.parseDouble(this.sbStnX.toString());
      } else if (qn.compareTo("baseline") == 0) {
        Hull.this.base = Double.parseDouble(this.sbBase.toString());
      } else if (qn.compareTo("water") == 0) {
        Hull.this.units.WATER = Integer.parseInt(this.sbWater.toString());
      }

      else if (qn.compareTo("lstem") == 0) {
        ts = this.sbLStem.toString();
        if (ts.compareTo("true") == 0) {
          Hull.this.bStems[0] = true;
        } else {
          Hull.this.bStems[0] = false;
        }
      } else if (qn.compareTo("rstem") == 0) {
        ts = this.sbRStem.toString();
        if (ts.compareTo("true") == 0) {
          Hull.this.bStems[1] = true;
        } else {
          Hull.this.bStems[1] = false;
        }
      }

      else if (qn.compareTo("units") == 0) {
        Hull.this.units.UNITS = Integer.parseInt(this.sbUnits.toString());
      } else if (qn.compareTo("designer") == 0) {
        Hull.this.designer = this.sbNA.toString();
      } else if (qn.compareTo("designname") == 0) {
        Hull.this.boatname = this.sbDName.toString();
      } else if (qn.compareTo("linename") == 0) {
        this.linename = this.sbLName.toString();
      }

      else if (qn.compareTo("wgtLbl") == 0) {
        Hull.this.wgtLbl[this.iWgt] = this.sbWL.toString();
      } else if (qn.compareTo("wgtWgt") == 0) {
        Hull.this.wgtWgt[this.iWgt] = Double.parseDouble(this.sbWW.toString());
      } else if (qn.compareTo("wgtX") == 0) {
        Hull.this.wgtX[this.iWgt] = Double.parseDouble(this.sbWX.toString());
      } else if (qn.compareTo("wgtY") == 0) {
        Hull.this.wgtY[this.iWgt] = Double.parseDouble(this.sbWY.toString());
      } else if (qn.compareTo("wgtZ") == 0) {
        Hull.this.wgtZ[this.iWgt] = Double.parseDouble(this.sbWZ.toString());
      }

      else if (qn.compareTo("rigX") == 0) {
        this.iSail++;
        this.curSail.setParamX(this.iSail, Double.parseDouble(this.sbRigX.toString()));
      } else if (qn.compareTo("rigZ") == 0) {
        this.curSail.setParamY(this.iSail, Double.parseDouble(this.sbRigY.toString()));
      }

      else if (qn.compareTo("foilX") == 0) {
        this.iFoil++;
        this.curFoil.setParamX(this.iFoil, Double.parseDouble(this.sbFoilX.toString()));
      } else if (qn.compareTo("foilZ") == 0) {
        this.curFoil.setParamY(this.iFoil, Double.parseDouble(this.sbFoilY.toString()));
      }

    }

    @Override
    public void startDocument() throws SAXException {

      Hull.this.base = 0.0;
      Hull.this.boatname = "boatCalc";
      Hull.this.designer = "boatCalc";

      this.tags = new String[10];
      this.itag = -1;

      Hull.this.Offsets = new ArrayList();
      Hull.this.bChanged = false;

    }

    @Override
    public void startElement(final String ns, final String n, final String qn, final Attributes a)
        throws SAXException {
      this.itag++;
      this.tags[this.itag] = qn;

      if (qn.compareTo("hull") == 0) {
        final int m = Integer.parseInt(a.getValue("stations"));
        Hull.this.Stations = new double[m];
        this.iStn = -1;
        Hull.this.newWgts();
        this.iWgt = -1;
      }

      else if (qn.compareTo("rig") == 0) {
        Hull.this.rig.dir = Integer.parseInt(a.getValue("dir"));
        Hull.this.rig.valid = Boolean.valueOf(a.getValue("valid"));
      } else if (qn.compareTo("sail") == 0) {
        final String t = a.getValue("type");
        if (t.compareTo("main") == 0) {
          this.curSail = Hull.this.rig.main;
        } else if (t.compareTo("jib") == 0) {
          this.curSail = Hull.this.rig.jib;
        } else {
          this.curSail = Hull.this.rig.mizzen;
        }
        this.curSail.setDir(Hull.this.rig.dir);
        this.curSail.use = Boolean.valueOf(a.getValue("use"));
        this.curSail.useGaff = Boolean.valueOf(a.getValue("head"));
        this.curSail.useRoach = Boolean.valueOf(a.getValue("roach"));
        this.iSail = -1;
      } else if (qn.compareTo("rigX") == 0) {
        this.sbRigX = new StringBuffer();
      } else if (qn.compareTo("rigZ") == 0) {
        this.sbRigY = new StringBuffer();
      }

      else if (qn.compareTo("rudder") == 0) {
        Hull.this.rudder.valid = Boolean.valueOf(a.getValue("valid"));
      } else if (qn.compareTo("foil") == 0) {
        final String t = a.getValue("type");
        if (t.compareTo("rudder") == 0) {
          this.curFoil = Hull.this.rudder.rudder;
        } else {
          this.curFoil = Hull.this.rudder.skeg;
        }
        this.curFoil.use = Boolean.valueOf(a.getValue("use"));
        this.iFoil = -1;
      } else if (qn.compareTo("foilX") == 0) {
        this.sbFoilX = new StringBuffer();
      } else if (qn.compareTo("foilZ") == 0) {
        this.sbFoilY = new StringBuffer();
      }

      else if (qn.compareTo("wgtItem") == 0) {
        this.iWgt++;
      } else if (qn.compareTo("stnX") == 0) {
        this.iStn++;
        this.sbStnX = new StringBuffer();
      } else if (qn.compareTo("water") == 0) {
        this.sbWater = new StringBuffer();
      } else if (qn.compareTo("lstem") == 0) {
        this.sbLStem = new StringBuffer();
      } else if (qn.compareTo("rstem") == 0) {
        this.sbRStem = new StringBuffer();
      } else if (qn.compareTo("units") == 0) {
        this.sbUnits = new StringBuffer();
      } else if (qn.compareTo("baseline") == 0) {
        this.sbBase = new StringBuffer();
      } else if (qn.compareTo("designer") == 0) {
        this.sbNA = new StringBuffer();
      } else if (qn.compareTo("designname") == 0) {
        this.sbDName = new StringBuffer();
      } else if (qn.compareTo("line") == 0) {
        this.p = new Point[Hull.this.Stations.length];
        for (this.iLine = 0; this.iLine < this.p.length; this.iLine++) {
          this.p[this.iLine] = new Point();
        }
        this.iLine = -1;
        this.linecount++;
        this.linename = "Line" + Integer.toString(this.linecount);
      } else if (qn.compareTo("linename") == 0) {
        this.sbLName = new StringBuffer();
      } else if (qn.compareTo("point") == 0) {
        this.iLine++;
      } else if (qn.compareTo("valX") == 0) {
        this.sbX = new StringBuffer();
      } else if (qn.compareTo("valY") == 0) {
        this.sbY = new StringBuffer();
      } else if (qn.compareTo("valZ") == 0) {
        this.sbZ = new StringBuffer();
      } else if (qn.compareTo("ptValid") == 0) {
        this.sbV = new StringBuffer();
        this.v = true;
      }

      else if (qn.compareTo("wgtLbl") == 0) {
        this.sbWL = new StringBuffer();
      } else if (qn.compareTo("wgtWgt") == 0) {
        this.sbWW = new StringBuffer();
      } else if (qn.compareTo("wgtX") == 0) {
        this.sbWX = new StringBuffer();
      } else if (qn.compareTo("wgtY") == 0) {
        this.sbWY = new StringBuffer();
      } else if (qn.compareTo("wgtZ") == 0) {
        this.sbWZ = new StringBuffer();
      }

    }

  }// end bcHandler

  double angHeel = 0.0;

  double base;
  bcFormat bcf;
  boolean bChanged;
  Centerboard board;
  String boatname;
  boolean bStems[];
  int CX = 1;
  int CY = 2;
  int CZ = 3;

  String designer;
  int DISP = 4;
  Set DispTri;
  double dx;
  // derived data
  double gx_min, gx_max;

  double gy_min, gy_max;
  double gz_min, gz_max;
  hLine[] hLines;
  double[] hVals;
  double lwlLeft, lwlRight;

  int NAREA = 8;
  // displacement curve data
  int NDIV = 100;
  java.util.List Offsets;
  Rig rig;
  Rudder rudder;
  // Constants
  int SAREA = 0;

  hLine[] sLines;
  double[] Stations;
  bcUnits units;


  // basic data
  boolean valid;
  double[][] vDisp;
  double[] vWet;

  double[][] vWL;
  int WETTED = 7;
  String wgtLbl[];
  double wgtWgt[];
  double wgtX[];
  double wgtY[];
  double wgtZ[];
  int WL_LEFT = 5;


  int WL_RIGHT = 6;

  // minimal constructor
  public Hull() {
    this.bcf = new bcFormat();

    this.wgtLbl = new String[10];
    this.wgtWgt = new double[10];
    this.wgtX = new double[10];
    this.wgtY = new double[10];
    this.wgtZ = new double[10];
    this.units = new bcUnits();
    this.bStems = new boolean[2];
    this.bStems[0] = true;
    this.bStems[1] = true;
    this.rig = new Rig();
    this.rudder = new Rudder();
    this.board = new Centerboard();
    this.valid = false;
  } // end constructor

  public void calcDisp() {
    this.setLWL();
    this.dx = (this.lwlRight - this.lwlLeft) / (this.NDIV);

    this.vDisp = new double[4][this.NDIV + 1];
    this.vWL = new double[2][this.NDIV + 1];
    this.vWet = new double[this.NDIV + 1];

    this.hVals = new double[this.NAREA];
    for (int i = 0; i < this.NAREA; i++) {
      this.hVals[i] = 0;
    }

    double[] rVals = new double[4];
    for (int i = 0; i <= this.NDIV; i++) {
      final double x = this.lwlLeft + (this.dx * i);
      rVals = this.getArea(x, this.angHeel, false);
      this.vDisp[this.CX][i] = x;
      this.vDisp[this.CY][i] = rVals[this.CY];
      this.vDisp[this.CZ][i] = rVals[this.CZ];
      this.vDisp[this.SAREA][i] = rVals[this.SAREA];
      this.vWL[0][i] = rVals[this.WL_LEFT];
      this.vWL[1][i] = rVals[this.WL_RIGHT];
      this.vWet[i] = rVals[this.WETTED];
    }

    // accumulate data
    for (int i = 0; i <= this.NDIV; i++) {
      this.hVals[this.SAREA] = this.hVals[this.SAREA] + this.vDisp[this.SAREA][i];
      this.hVals[this.CX] =
          this.hVals[this.CX] + (this.vDisp[this.SAREA][i] * this.vDisp[this.CX][i]);
      this.hVals[this.CY] =
          this.hVals[this.CY] + (this.vDisp[this.SAREA][i] * this.vDisp[this.CY][i]);
      this.hVals[this.CZ] =
          this.hVals[this.CZ] + (this.vDisp[this.SAREA][i] * this.vDisp[this.CZ][i]);
      if (i > 0) {
        this.hVals[this.DISP] = this.hVals[this.DISP]
            + (this.dx * 0.5 * (this.vDisp[this.SAREA][i] + this.vDisp[this.SAREA][i - 1]));
      }
    }
    if (this.hVals[this.SAREA] > 0) {
      this.hVals[this.CX] = this.hVals[this.CX] / this.hVals[this.SAREA];
      this.hVals[this.CY] = this.hVals[this.CY] / this.hVals[this.SAREA];
      this.hVals[this.CZ] = this.hVals[this.CZ] / this.hVals[this.SAREA];
    }
  }// end calc Disp


  private void emit(final BufferedWriter w, final String s) {
    try {
      w.write(s, 0, s.length());
    } catch (final IOException e) {
      System.out.println(e);
    }
  }


  private void emitln(final BufferedWriter w, final String s) {
    this.emit(w, s);
    this.emit(w, System.getProperty("line.separator"));
  }

  public double[] getArea(final double x, final double ang, final boolean trisave) {
    final SortedSet ss = this.getStnSet(x, ang);
    final Iterator si = ss.iterator();
    Point p1, p2;
    double t1y, t1z, t2y, t2z, t3y, t3z;
    double area, cy, cz;
    double wlRight = 0;
    double wlLeft = 0;
    double wetted = 0;
    t3y = 0;
    t3z = 0;

    if (trisave) {
      this.DispTri = new HashSet();
    }

    final double[] rArea = new double[this.NAREA];
    for (int i = 0; i < this.NAREA; i++) {
      rArea[i] = 0;
    }

    if (ss.size() < 2) {
      return rArea;
    }
    if (!si.hasNext()) {
      return rArea;
    }

    p1 = (Point) ss.last();

    while (si.hasNext()) {

      p2 = (Point) si.next();

      t1y = p1.y;
      t1z = p1.z;
      t2y = p2.y;
      t2z = p2.z;
      if ((t1z > 0) && (t2z > 0)) {
        t1y = 0;
        t1z = 0;
        t2y = 0;
        t2z = 0;
      } else if ((t1z > 0) && (t2z <= 0)) {
        t1y = t2y + (((t1y - t2y) * (0 - t2z)) / (t1z - t2z));
        t1z = 0;
      } else if ((t1z <= 0) && (t2z > 0)) {
        t2y = t1y + (((t2y - t1y) * (0 - t1z)) / (t2z - t1z));
        t2z = 0;
      }

      cy = (t1y + t2y + t3y) / 3;
      cz = (t1z + t2z + t3z) / 3;
      area = this.TriArea(t1y, t1z, t2y, t2z, t3y, t3z);

      if (trisave) {
        final double[] tri = {t1y, t1z, t2y, t2z, t3y, t3z};
        this.DispTri.add(tri);
      }

      rArea[this.CY] = rArea[this.CY] + (area * cy);
      rArea[this.CZ] = rArea[this.CZ] + (area * cz);
      rArea[this.SAREA] = rArea[this.SAREA] + area;


      if ((t1z == 0) && (t1y < 0)) {
        wlLeft = t1y;
      } else if ((t1z == 0) && (t1y > 0)) {
        wlRight = t1y;
      } else if ((t2z == 0) && (t2y < 0)) {
        wlLeft = t2y;
      } else if ((t2z == 0) && (t2y > 0)) {
        wlRight = t2y;
      }

      wetted = wetted + Math.pow(Math.pow(t2y - t1y, 2) + Math.pow(t2z - t1z, 2), 0.5);

      p1 = p2;
    }
    if (rArea[this.SAREA] > 0) {
      rArea[this.CY] = rArea[this.CY] / rArea[this.SAREA];
      rArea[this.CZ] = rArea[this.CZ] / rArea[this.SAREA];
    }

    rArea[this.WL_LEFT] = wlLeft;
    rArea[this.WL_RIGHT] = wlRight;
    rArea[this.WETTED] = wetted;

    return rArea;
  } // end getArea (station=x)

  // read native (xml) data file
  public void getData(final File hullfile) {
    // declarations
    final DefaultHandler h = new bcHandler();
    final SAXParserFactory f = SAXParserFactory.newInstance();
    try {
      final SAXParser saxParser = f.newSAXParser();
      saxParser.parse(hullfile, h);
    } catch (final Throwable t) {
      t.printStackTrace();
    }
    this.setLines();
    this.valid = true;
  } // end getData


  // read "Hulls" data file
  public void getHulls(final File hullfile) {
    this.Offsets = new ArrayList();
    BufferedReader r;
    String l;
    // initialization
    this.Stations = new double[13];
    this.newWgts();
    this.bChanged = false;

    int n, m;
    int i, j;
    double x = 0, y, z;
    double z_min = 1000.0, z_max = -1000.0;

    try {

      // read first line - name and constants

      final String filename = hullfile.getName();
      n = filename.indexOf('.');
      if (n > 0) {
        this.boatname = filename.substring(0, n);
      } else {
        this.boatname = filename;
      }

      // open file for reading
      r = new BufferedReader(new FileReader(hullfile));

      // read number of chines
      n = Integer.parseInt(r.readLine().trim());

      // read waste lines.
      m = 15 * n;
      for (i = 1; i <= m; i++) {
        l = r.readLine();
      }
      l = r.readLine();

      // read detailed data into lines.
      final Point p[][] = new Point[n][13];

      for (i = 0; i < 13; i++) {
        for (j = 0; j < n; j++) {

          y = Double.parseDouble(r.readLine().trim());
          z = Double.parseDouble(r.readLine().trim());
          x = Double.parseDouble(r.readLine().trim());
          if (y < 0.1) {
            y = 0.1;
          }
          z_min = Math.min(z_min, z);
          z_max = Math.max(z_max, z);
          p[j][i] = new Point(x, y, z);

        }
        this.Stations[i] = x;
      }

      for (j = 0; j < n; j++) {
        final rawLine rL = new rawLine();
        rL.ln = new Line(p[j]);
        rL.lnName = "Line " + Integer.toString(j + 1);
        this.Offsets.add(rL);
      }

      // read waste lines.
      try {
        m = 17;
        for (i = 1; i <= m; i++) {
          l = r.readLine();
        }
        l = r.readLine();
        this.designer = l;
        l = r.readLine();
        this.boatname = l;
        for (i = 1; i <= 3; i++) {
          l = r.readLine();
        }
        this.wgtWgt[1] = Double.parseDouble(r.readLine().trim());
        this.wgtY[1] = Double.parseDouble(r.readLine().trim());
        this.wgtZ[1] = Double.parseDouble(r.readLine().trim());

      } catch (final NoSuchElementException e) {
        this.base = 0.0;
        // boatname="unknown";
        System.out.println("nse");
        System.out.println(e);
      } catch (final NullPointerException npe) {
        this.base = 0.0;
        // boatname="unknown";
        System.out.println("npe");
        System.out.println(npe);
      }

      r.close();
      if (z_min >= 0.0) {
        this.base = -(z_min + (0.25 * (z_max - z_min)));
      } else {
        this.base = 0.0;
      }

      this.setLines();
      this.valid = true;
    }

    catch (final NumberFormatException e) {
      System.out.println(e);
    } catch (final NoSuchElementException e) {
      System.out.println(e);
    } catch (final FileNotFoundException e) {
      System.out.println(e);
    } catch (final IOException ioe) {
      System.out.println(ioe);
    } catch (final NullPointerException npe) {
      System.out.println(npe);
    }

    return;

  }// end get hulls

  public Iterator getStation(final double tx, final double ang) {
    final SortedSet ts = this.getStnSet(tx, ang);
    return ts.iterator();
  }

  public Iterator getStation(final int i, final double ang) {
    final double tx = this.Stations[i];
    return this.getStation(tx, ang);
  }


  public SortedSet getStnSet(final double tx, final double ang) {
    final double sinang = Math.sin(Math.toRadians(ang));
    final double cosang = Math.cos(Math.toRadians(ang));
    double ty, tz;

    final Set s = new HashSet();

    double z_min = +1000000.0;
    double z_max = -1000000.0;

    for (int iHL = 0; iHL < this.hLines.length; iHL++) {
      final double x_min = this.hLines[iHL].min("X");
      final double x_max = this.hLines[iHL].max("X");
      if ((x_min <= tx) && (tx <= x_max)) {
        final double y = this.hLines[iHL].hXY.interp4P(tx);
        final double z = this.hLines[iHL].hXZ.interp4P(tx);
        z_min = Math.min(z_min, z);
        z_max = Math.max(z_max, z);

        // point on first side
        ty = (cosang * y) - (sinang * z);
        tz = (sinang * y) + (cosang * z);
        s.add(new Point(tx, ty, tz));
        // point for opposite side of boat
        ty = (cosang * (-y)) - (sinang * z);
        tz = (sinang * (-y)) + (cosang * z);
        s.add(new Point(tx, ty, tz));
      }
    }

    // left stem
    if (this.bStems[0]) {
      final double x_min = this.sLines[0].min("X");
      final double x_max = this.sLines[0].max("X");

      if ((x_min <= tx) && (tx <= x_max)) {
        final double y = this.sLines[0].hXY.leftLinear(tx);
        final double z = this.sLines[0].hXZ.leftLinear(tx);
        // point on first side
        ty = (cosang * y) - (sinang * z);
        tz = (sinang * y) + (cosang * z);
        s.add(new Point(tx, ty, tz));
        // point for opposite side of boat
        ty = (cosang * (-y)) - (sinang * z);
        tz = (sinang * (-y)) + (cosang * z);
        s.add(new Point(tx, ty, tz));
      }
    }

    // right stem
    if (this.bStems[1]) {
      final double x_min = this.sLines[1].min("X");
      final double x_max = this.sLines[1].max("X");

      if ((x_min <= tx) && (tx <= x_max)) {
        final double y = this.sLines[1].hXY.rightLinear(tx);
        final double z = this.sLines[1].hXZ.rightLinear(tx);
        // point on first side
        ty = (cosang * y) - (sinang * z);
        tz = (sinang * y) + (cosang * z);
        s.add(new Point(tx, ty, tz));
        // point for opposite side of boat
        ty = (cosang * (-y)) - (sinang * z);
        tz = (sinang * (-y)) + (cosang * z);
        s.add(new Point(tx, ty, tz));
      }
    }

    final YZCompare yzComp2 = new YZCompare();
    ty = -sinang * (z_min + (0.8 * (z_max - z_min)));
    tz = cosang * (z_min + (0.8 * (z_max - z_min)));
    yzComp2.setAdj(ty, tz);

    final Iterator si = s.iterator();
    final SortedSet ts = new TreeSet(yzComp2);
    while (si.hasNext()) {
      ts.add(si.next());
    }
    return ts;

  }// end getStation

  public void newWgts() {
    this.wgtLbl = new String[10];
    this.wgtWgt = new double[10];
    this.wgtX = new double[10];
    this.wgtY = new double[10];
    this.wgtZ = new double[10];
    this.wgtLbl[0] = "Hull";
    this.wgtLbl[1] = "Ballast";
    this.wgtLbl[2] = "Engine";
    this.wgtLbl[3] = "Accomodation";
    this.wgtLbl[4] = "Crew";
    this.wgtLbl[5] = "Stores/Gear";
    this.wgtLbl[6] = "";
    this.wgtLbl[7] = "";
    this.wgtLbl[8] = "";
    this.wgtLbl[9] = "";

  }


  public void saveData(File xmlFile) {
    String fn = xmlFile.getName();
    BufferedWriter w;
    new DecimalFormat("#########");
    new DecimalFormat("#########.0000");


    if (fn.indexOf(".xml") < 0) {
      fn = fn + ".xml";
      xmlFile = new File(xmlFile.getParent(), fn);
    }

    // open file for writing
    try {

      w = new BufferedWriter(new FileWriter(xmlFile));
      this.emitln(w, "<?xml version='1.0' encoding='UTF-8'?>");
      this.emitln(w,
          "<hull stations='" + this.Stations.length + "' lines='" + this.Offsets.size() + "'>");

      this.emitln(w, "   <water>" + this.bcf.DF0d.format(this.units.WATER) + "</water>");
      this.emitln(w, "   <units>" + this.bcf.DF0d.format(this.units.UNITS) + "</units>");

      this.emitln(w, "   <lstem>" + this.bStems[0] + "</lstem>");
      this.emitln(w, "   <rstem>" + this.bStems[1] + "</rstem>");

      this.emitln(w, "   <designer>" + this.designer + "</designer>");
      this.emitln(w, "   <designname>" + this.boatname + "</designname>");
      this.emitln(w, "   <baseline> " + this.bcf.DF4d.format(this.base) + " </baseline>");

      this.emitln(w, "   <weights>");
      for (int i = 0; i < 10; i++) {
        this.emitln(w, "     <wgtItem>");
        this.emitln(w, "      <wgtLbl>" + this.wgtLbl[i] + "</wgtLbl>");
        this.emitln(w, "      <wgtWgt> " + this.bcf.DF4d.format(this.wgtWgt[i]) + " </wgtWgt>");
        this.emitln(w, "      <wgtX> " + this.bcf.DF4d.format(this.wgtX[i]) + " </wgtX>");
        this.emitln(w, "      <wgtY> " + this.bcf.DF4d.format(this.wgtY[i]) + " </wgtY>");
        this.emitln(w, "      <wgtZ> " + this.bcf.DF4d.format(this.wgtZ[i]) + " </wgtZ>");
        this.emitln(w, "     </wgtItem>");
      }
      this.emitln(w, "   </weights>");

      this.emitln(w, "   <stations>");
      for (int i = 0; i < this.Stations.length; i++) {
        this.emitln(w, "      <stnX> " + this.bcf.DF4d.format(this.Stations[i]) + " </stnX>");
      }
      this.emitln(w, "   </stations>");

      ListIterator l;
      l = this.Offsets.listIterator();
      while (l.hasNext()) {
        final rawLine rL = (rawLine) l.next();
        final Line ln = rL.ln;
        this.emitln(w, "   <line>");
        this.emitln(w, "      <linename>" + rL.lnName + "</linename>");
        for (int i = 0; i < this.Stations.length; i++) {
          this.emitln(w, "      <point>");
          this.emitln(w, "         <valX>" + this.bcf.DF4d.format(ln.valX(i)) + "</valX>");
          this.emitln(w, "         <valY>" + this.bcf.DF4d.format(ln.valY(i)) + "</valY>");
          this.emitln(w, "         <valZ>" + this.bcf.DF4d.format(ln.valZ(i)) + "</valZ>");
          if (ln.valid(i)) {
            this.emitln(w, "         <ptValid>true</ptValid>");
          } else {
            this.emitln(w, "         <ptValid>false</ptValid>");
          }
          this.emitln(w, "      </point>");
        }
        this.emitln(w, "   </line>");
      }

      if (this.rig.valid) {
        this.emitln(w, "   <rig dir='" + this.rig.dir + "' valid='" + this.rig.valid + "' >");

        int is;
        Sail sl;
        for (is = 0; is < 3; is++) {
          if (is == 0) {
            sl = this.rig.main;
            this.emitln(w, "   <sail type='main' use='" + sl.use + "' head='" + sl.useGaff
                + "' roach='" + sl.useRoach + "'>");
          } else if (is == 1) {
            sl = this.rig.jib;
            this.emitln(w, "   <sail type='jib' use='" + sl.use + "' head='" + sl.useGaff
                + "' roach='" + sl.useRoach + "'>");
          } else {
            sl = this.rig.mizzen;
            this.emitln(w, "   <sail type='mizzen' use='" + sl.use + "' head='" + sl.useGaff
                + "' roach='" + sl.useRoach + "'>");
          }
          for (int js = 0; js < 5; js++) {
            this.emitln(w, "         <rigX>" + this.bcf.DF4d.format(sl.getParamX(js)) + "</rigX>");
            this.emitln(w, "         <rigZ>" + this.bcf.DF4d.format(sl.getParamY(js)) + "</rigZ>");
          }
          this.emitln(w, "   </sail>");
        }
        this.emitln(w, "   </rig>");
      }

      if (this.rudder.valid) {
        this.emitln(w, "   <rudder valid='" + this.rudder.valid + "' >");

        rscFoil f;
        for (int i = 0; i <= 1; i++) {
          if (i == 0) {
            f = this.rudder.rudder;
            this.emitln(w, "   <foil type='rudder' use='" + f.use + "'>");
          } else {
            f = this.rudder.skeg;
            this.emitln(w, "   <foil type='skeg' use='" + f.use + "'>");
          }
          for (int jf = 0; jf < 4; jf++) {
            this.emitln(w, "         <foilX>" + this.bcf.DF4d.format(f.getParamX(jf)) + "</foilX>");
            this.emitln(w, "         <foilZ>" + this.bcf.DF4d.format(f.getParamY(jf)) + "</foilZ>");
          }
          this.emitln(w, "   </foil>");
        }
        this.emitln(w, "   </rudder>");
      }



      this.emitln(w, "</hull>");

      w.flush();
      w.close();
      this.bChanged = false;
    } catch (final IOException e) {
      System.out.println(e);
    }

  }// end saveData

  public void saveHulls(File hullFile) {

    String fn = hullFile.getName();
    BufferedWriter w;
    new DecimalFormat("#########.0000");
    double x, y, z;
    int i;

    if (fn.indexOf(".hul") < 0) {
      fn = fn + ".hul";
      hullFile = new File(hullFile.getParent(), fn);
    }

    // open file for writing
    try {

      w = new BufferedWriter(new FileWriter(hullFile));
      final int n = this.Offsets.size();
      // number of chines
      this.emitln(w, Integer.toString(n));
      // left end
      double xmin = -1000000.0;
      double xmax = +1000000.0;

      // beging first block
      for (i = 0; i < n; i++) {
        x = this.hLines[i].min("X") - this.gx_min;
        y = this.hLines[i].hXY.interp4P(x) - this.gy_min;
        z = this.hLines[i].hXZ.interp4P(x) - this.gz_min;
        this.emitln(w, this.bcf.DF4d.format(y));
        this.emitln(w, this.bcf.DF4d.format(z));
        this.emitln(w, this.bcf.DF4d.format(x));

        // find max, min for spacing of other stations
        xmin = Math.max(x, xmin);
        x = this.hLines[i].max("X");
        xmax = Math.min(x, xmax);
      }


      for (int j = 1; j <= 3; j++) {

        x = xmin + ((j) * 0.25 * (xmax - xmin));

        for (i = 0; i < n; i++) {
          y = this.hLines[i].hXY.interp4P(x) - this.gy_min;
          z = this.hLines[i].hXZ.interp4P(x) - this.gz_min;
          this.emitln(w, this.bcf.DF4d.format(y));
          this.emitln(w, this.bcf.DF4d.format(z));
          this.emitln(w, this.bcf.DF4d.format(x - this.gx_min));
        }
      }

      // right end
      for (i = 0; i < n; i++) {
        x = this.hLines[i].max("X");
        y = this.hLines[i].hXY.interp4P(x) - this.gy_min;
        z = this.hLines[i].hXZ.interp4P(x) - this.gz_min;
        this.emitln(w, this.bcf.DF4d.format(y));
        this.emitln(w, this.bcf.DF4d.format(z));
        this.emitln(w, this.bcf.DF4d.format(x - this.gx_min));
      }
      this.emitln(w, Integer.toString(-1));

      // begin second block
      for (i = 0; i < n; i++) {
        x = this.hLines[i].min("X");
        y = this.hLines[i].hXY.interp4P(x) - this.gy_min;
        z = this.hLines[i].hXZ.interp4P(x) - this.gz_min;
        this.emitln(w, this.bcf.DF4d.format(y));
        this.emitln(w, this.bcf.DF4d.format(z));
        this.emitln(w, this.bcf.DF4d.format(x - this.gx_min));
      }

      for (int j = 1; j <= 11; j++) {
        x = xmin + ((j) * 0.0833333 * (xmax - xmin));
        // emitln(w,"line "+j+" "+x);

        for (i = 0; i < n; i++) {
          y = this.hLines[i].hXY.interp4P(x) - this.gy_min;
          z = this.hLines[i].hXZ.interp4P(x) - this.gz_min;
          this.emitln(w, this.bcf.DF4d.format(y));
          this.emitln(w, this.bcf.DF4d.format(z));
          this.emitln(w, this.bcf.DF4d.format(x - this.gx_min));
        }
      }

      // right end
      for (i = 0; i < n; i++) {
        x = this.hLines[i].max("X");
        y = this.hLines[i].hXY.interp4P(x) - this.gy_min;
        z = this.hLines[i].hXZ.interp4P(x) - this.gz_min;
        this.emitln(w, this.bcf.DF4d.format(y));
        this.emitln(w, this.bcf.DF4d.format(z));
        this.emitln(w, this.bcf.DF4d.format(x - this.gx_min));
      }

      // frames
      for (int j = 0; j < 8; j++) {
        x = xmin + ((j) * 0.1429 * (xmax - xmin));
        this.emitln(w, this.bcf.DF4d.format(x - this.gx_min));
      }

      this.emitln(w, Integer.toString(0)); // mast length
      this.emitln(w, Integer.toString(0)); // boom from deck
      this.emitln(w, Integer.toString(0)); // mast dist aft
      this.emitln(w, Integer.toString(0)); // luff length
      this.emitln(w, Integer.toString(0)); // head length
      this.emitln(w, Integer.toString(0)); // head angle
      this.emitln(w, Integer.toString(0)); // foot length
      this.emitln(w, Integer.toString(0)); // deck-stepped
      this.emitln(w, Integer.toString(-1)); // keel stepped
      this.emitln(w, this.designer); // designer name
      this.emitln(w, this.boatname); // hull info
      this.emitln(w, "here@there"); // url
      this.emitln(w, "01-01-2004"); // date
      this.emitln(w, " "); // unused
      this.emitln(w, Double.toString(this.wgtWgt[1])); // ballast weight
      this.emitln(w, Double.toString(this.wgtZ[1])); // ballast x
      this.emitln(w, Double.toString(this.wgtY[1])); // ballast y
      this.emitln(w, Integer.toString(0)); // auto smoothing

      w.flush();
      w.close();
      this.bChanged = false;

    } catch (final IOException e) {
      System.out.println(e);
    }


  }// end saveHulls

  public void setLines() {
    ListIterator l;
    final int n = this.Offsets.size();
    int i = 0;

    this.hLines = new hLine[n];

    l = this.Offsets.listIterator();
    while (l.hasNext()) {
      final rawLine rL = (rawLine) l.next();
      final Line ln = rL.ln;
      this.hLines[i] = new hLine(ln.points, this.base);
      if (i == 0) {
        this.gx_min = this.hLines[i].min("X");
        this.gx_max = this.hLines[i].max("X");
        this.gy_min = this.hLines[i].min("Y");
        this.gy_max = this.hLines[i].max("Y");
        this.gz_min = this.hLines[i].min("Z");
        this.gz_max = this.hLines[i].max("Z");
      } else {
        this.gx_min = Math.min(this.gx_min, this.hLines[i].min("X"));
        this.gx_max = Math.max(this.gx_max, this.hLines[i].max("X"));
        this.gy_min = Math.min(this.gy_min, this.hLines[i].min("Y"));
        this.gy_max = Math.max(this.gy_max, this.hLines[i].max("Y"));
        this.gz_min = Math.min(this.gz_min, this.hLines[i].min("Z"));
        this.gz_max = Math.max(this.gz_max, this.hLines[i].max("Z"));
      }
      i++;
    }

    // build stem/stern lines
    final ZCompare zComp = new ZCompare();
    final SortedSet sLow = new TreeSet(zComp);
    final SortedSet sHigh = new TreeSet(zComp);
    Iterator si;
    Line ln;
    Point p;
    final int m = this.Stations.length - 1;
    l = this.Offsets.listIterator();
    while (l.hasNext()) {
      final rawLine rL = (rawLine) l.next();
      ln = rL.ln;
      p = ln.getPoint(0);
      if (p.valid) {
        sLow.add(p);
      }
      p = ln.getPoint(m);
      if (p.valid) {
        sHigh.add(p);
      }
    }
    this.sLines = new hLine[2];

    ln = new Line(sLow.size());
    i = 0;
    si = sLow.iterator();
    while (si.hasNext()) {
      ln.setPoint((Point) si.next(), i);
      i++;
    }
    this.sLines[0] = new hLine(ln.points, this.base);

    ln = new Line(sHigh.size());
    i = 0;
    si = sHigh.iterator();
    while (si.hasNext()) {
      ln.setPoint((Point) si.next(), i);
      i++;
    }
    this.sLines[1] = new hLine(ln.points, this.base);

  } // end setLines

  public void setLWL() {
    new ArrayList();
    new ArrayList();
    double[] rVals;
    double x, y;

    // find left end
    int i = 0;
    x = this.Stations[i];
    rVals = this.getArea(x, this.angHeel, false);
    y = rVals[this.SAREA];
    if (y > 0) {
      this.lwlLeft = x;
    } else {
      while ((y == 0) && (i < (this.Stations.length - 1))) {
        i++;
        x = this.Stations[i];
        rVals = this.getArea(x, this.angHeel, false);
        y = rVals[this.SAREA];
      }
      if (i >= (this.Stations.length - 1)) {
        this.lwlLeft = this.Stations[0];
      } else {
        double xl, xr;
        xl = this.Stations[i - 1];
        xr = this.Stations[i];
        for (int j = 0; j <= 10; j++) {
          x = 0.5 * (xl + xr);
          rVals = this.getArea(x, this.angHeel, false);
          y = rVals[this.SAREA];
          if (y > 0) {
            xr = x;
          } else {
            xl = x;
          }
        }
        this.lwlLeft = x;
      }
    }
    // find right end
    i = this.Stations.length - 1;
    x = this.Stations[i];
    rVals = this.getArea(x, this.angHeel, false);
    y = rVals[this.SAREA];
    if (y > 0) {
      this.lwlRight = x;
    } else {
      while ((y == 0) && (i > 0)) {
        i--;
        x = this.Stations[i];
        rVals = this.getArea(x, this.angHeel, false);
        y = rVals[this.SAREA];
      }
      if (i <= 0) {
        this.lwlRight = this.Stations[this.Stations.length - 1];
      } else {
        double xl, xr;
        xl = this.Stations[i];
        xr = this.Stations[i + 1];
        for (int j = 0; j <= 10; j++) {
          x = 0.5 * (xl + xr);
          rVals = this.getArea(x, this.angHeel, false);
          y = rVals[this.SAREA];
          if (y > 0) {
            xl = x;
          } else {
            xr = x;
          }
        }
        this.lwlRight = x;
      }
    }
  }// end setLWL

  public final double TriArea(final double x1, final double y1, final double x2, final double y2,
      final double x3, final double y3) {
    double a;
    if ((x1 <= x2) && (x1 <= x3)) {
      a = 0.5 * Math.abs(((x2 - x1) * (y3 - y1)) - ((x3 - x1) * (y2 - y1)));
    } else if ((x2 <= x1) && (x2 <= x3)) {
      a = 0.5 * Math.abs(((x3 - x2) * (y1 - y2)) - ((x1 - x2) * (y3 - y2)));
    } else {
      a = 0.5 * Math.abs(((x1 - x3) * (y2 - y3)) - ((x2 - x3) * (y1 - y3)));
    }
    return a;
  }



} // end class Hull


class rawLine {
  Line ln;
  String lnName;
}


class Rig implements Cloneable {
  int dir;
  public Sail jib;
  public Sail main;
  public Sail mizzen;
  boolean valid;

  public Rig() {
    this.main = new Sail();
    this.jib = new Sail();
    this.mizzen = new Sail();
    this.dir = 1;
    this.valid = true;
  }

  @Override
  public Object clone() {

    try {
      final Rig r = (Rig) super.clone();
      // r.main = new Sail();
      // r.jib = new Sail();
      // r.mizzen = new Sail();

      r.main = (Sail) this.main.clone();
      r.jib = (Sail) this.jib.clone();
      r.mizzen = (Sail) this.mizzen.clone();
      return r;
    } catch (final CloneNotSupportedException e) {
      throw new InternalError(e.toString());
    }

  } // end clone

  public double getArea() {
    double a = 0;
    if (this.main.use) {
      a = a + this.main.getArea();
    }
    if (this.jib.use) {
      a = a + this.jib.getArea();
    }
    if (this.mizzen.use) {
      a = a + this.mizzen.getArea();
    }
    return a;
  }

  public double getAreaX() {
    double x = 0;
    if (this.main.use) {
      x = x + (this.main.getArea() * this.main.getAreaX());
    }
    if (this.jib.use) {
      x = x + (this.jib.getArea() * this.jib.getAreaX());
    }
    if (this.mizzen.use) {
      x = x + (this.mizzen.getArea() * this.mizzen.getAreaX());
    }
    final double a = this.getArea();
    if (a > 0) {
      x = x / a;
    }
    return x;
  }

  public double getAreaY() {
    double x = 0;
    if (this.main.use) {
      x = x + (this.main.getArea() * this.main.getAreaY());
    }
    if (this.jib.use) {
      x = x + (this.jib.getArea() * this.jib.getAreaY());
    }
    if (this.mizzen.use) {
      x = x + (this.mizzen.getArea() * this.mizzen.getAreaY());
    }
    final double a = this.getArea();
    if (a > 0) {
      x = x / a;
    }
    return x;
  }

  public double getMaxX() {
    double x = 0;
    if (this.main.use) {
      x = Math.max(x, this.main.getMaxX());
    }
    if (this.jib.use) {
      x = Math.max(x, this.jib.getMaxX());
    }
    if (this.mizzen.use) {
      x = Math.max(x, this.mizzen.getMaxX());
    }
    return x;
  }

  public double getMaxY() {
    double x = 0;
    if (this.main.use) {
      x = Math.max(x, this.main.getMaxY());
    }
    if (this.jib.use) {
      x = Math.max(x, this.jib.getMaxY());
    }
    if (this.mizzen.use) {
      x = Math.max(x, this.mizzen.getMaxY());
    }
    return x;
  }

  public double getMinX() {
    double x = 1000000;
    if (this.main.use) {
      x = Math.min(x, this.main.getMinX());
    }
    if (this.jib.use) {
      x = Math.min(x, this.jib.getMinX());
    }
    if (this.mizzen.use) {
      x = Math.min(x, this.mizzen.getMinX());
    }
    return x;
  }

  public double getMinY() {
    double x = 1000000;
    if (this.main.use) {
      x = Math.min(x, this.main.getMinY());
    }
    if (this.jib.use) {
      x = Math.min(x, this.jib.getMinY());
    }
    if (this.mizzen.use) {
      x = Math.min(x, this.mizzen.getMinY());
    }
    return x;
  }

} // end class Rig



class rscFoil implements Cloneable {

  public static int BL = 3;
  public static int BR = 2;
  public static int TL = 0;

  public static int TR = 1;
  private double ang;

  private double area;
  private double base;
  private boolean changed;

  private double cX;
  private double cY;
  private boolean isCB;
  private double maxX;

  private double maxY;
  private double minX;
  private double minY;

  private double[][] p;
  private SortedSet sp;

  public boolean use;
  private double wetArea;
  private double wetX;
  private double wetY;

  public rscFoil() {
    this.p = new double[2][4];
    this.use = false;
    this.changed = true;
  }

  public void addWetPt(final Object p) {
    this.sp.add(p);
  }

  @Override
  public Object clone() {

    try {
      final rscFoil f = (rscFoil) super.clone();
      f.p = new double[2][4];
      f.p[0] = this.p[0].clone();
      f.p[1] = this.p[1].clone();
      f.changed = true;
      return f;
    } catch (final CloneNotSupportedException e) {
      throw new InternalError(e.toString());
    }

  } // end clone

  public double getAngle() {
    return this.ang;
  }

  public double getArea() {
    if (this.changed) {
      this.setFoil();
    }
    return this.area;
  }

  public double getAreaX() {
    if (this.changed) {
      this.setFoil();
    }
    return this.cX;
  }

  public double getAreaY() {
    if (this.changed) {
      this.setFoil();
    }
    return this.cY;
  }

  public double getBase() {
    return this.base;
  }

  public double getMaxX() {
    if (this.changed) {
      this.setFoil();
    }
    return this.maxX;
  }

  public double getMaxY() {
    if (this.changed) {
      this.setFoil();
    }
    return this.maxY;
  }

  public double getMinX() {
    if (this.changed) {
      this.setFoil();
    }
    return this.minX;
  }

  public double getMinY() {
    if (this.changed) {
      this.setFoil();
    }
    return this.minY;
  }

  public double getParamX(final int i) {
    return this.p[0][i];
  }

  public double getParamY(final int i) {
    return this.p[1][i];
  }

  public double getWetArea() {
    if (this.changed) {
      this.setFoil();
    }
    return this.wetArea;
  }

  public SortedSet getWetPts() {
    if (this.changed) {
      this.setFoil();
    }
    return this.sp;
  }

  public double getWetX() {
    if (this.changed) {
      this.setFoil();
    }
    return this.wetX;
  }

  public double getWetY() {
    if (this.changed) {
      this.setFoil();
    }
    return this.wetY;
  }

  public boolean isCB() {
    return this.isCB;
  }

  public boolean isRS() {
    return !this.isCB;
  }

  public void setAngle(final double a) {
    this.ang = a;
  }

  public void setBase(final double b) {
    this.base = b;
  }

  public void setCB(final boolean b) {
    this.isCB = b;
  }

  public void setDir(final int i) {
    this.changed = true;
  }

  public void setFoil() {

    final ArrayList al = new ArrayList();
    for (int i = 0; i < 4; i++) {
      al.add(new Point(this.p[0][i], 0, this.p[1][i]));
    }
    final xzArea xzA = new xzArea(al);
    this.area = xzA.getArea();
    this.cX = xzA.getAreaX();
    this.cY = xzA.getAreaZ();

    this.maxX = xzA.getMidX();
    this.minX = xzA.getMidX();
    this.maxY = xzA.getMidZ();
    this.minY = xzA.getMidZ();

    final ArrayList wp = new ArrayList();

    for (int i = 0; i < 4; i++) {
      this.maxX = Math.max(this.maxX, this.p[0][i]);
      this.minX = Math.min(this.minX, this.p[0][i]);
      this.maxY = Math.max(this.maxY, this.p[1][i]);
      this.minY = Math.min(this.minY, this.p[1][i]);
      // find points below waterline
      if ((this.p[1][i] + this.base) < 0) {
        wp.add(new Point(this.p[0][i], 0, this.p[1][i] + this.base));
      }
    }

    // find line intersections with waterline
    double wx, wy;
    final double tb = -this.base;

    for (int i = 0; i < 4; i++) {
      int j = i + 1;
      if (j == 4) {
        j = 0;
      }

      if ((this.p[1][i] == tb) && (this.p[1][j] == tb)) {
        wp.add(new Point(this.p[0][i], 0, this.p[1][i] + this.base));
        wp.add(new Point(this.p[0][j], 0, this.p[1][j] + this.base));
      } else if ((this.p[1][i] <= tb) && (this.p[1][j] >= tb)) {
        final double f = (tb - this.p[1][i]) / (this.p[1][j] - this.p[1][i]);
        wx = this.p[0][i] + (f * (this.p[0][j] - this.p[0][i]));
        wy = this.p[1][i] + (f * (this.p[1][j] - this.p[1][i]));
        wp.add(new Point(wx, 0, wy + this.base));
      } else if ((this.p[1][j] <= tb) && (this.p[1][i] >= tb)) {
        final double f = (tb - this.p[1][i]) / (this.p[1][j] - this.p[1][i]);
        wx = this.p[0][i] + (f * (this.p[0][j] - this.p[0][i]));
        wy = this.p[1][i] + (f * (this.p[1][j] - this.p[1][i]));
        wp.add(new Point(wx, 0, wy + this.base));
      }
    }

    double tx = 0;
    double tz = 0;
    for (int i = 0; i < wp.size(); i++) {
      final Point p = (Point) wp.get(i);
      tx = tx + p.x;
      tz = tz + p.z;
    }
    tx = tx / Math.max(wp.size(), 1);
    tz = tz / Math.max(wp.size(), 1);

    final XZCompare xzComp = new XZCompare();
    xzComp.setAdj(tx, tz);
    this.sp = new TreeSet(xzComp);
    for (int i = 0; i < wp.size(); i++) {
      this.sp.add(wp.get(i));
    }

    final xzArea xzWA = new xzArea(wp);
    this.wetArea = xzWA.getArea();
    this.wetX = xzWA.getAreaX();
    this.wetY = xzWA.getAreaZ();


    this.changed = false;

  }// end setFoil

  public void setParamX(final int i, final double x) {
    this.p[0][i] = x;
    this.changed = true;
  }

  public void setParamXY(final int i, final double x, final double y) {
    this.p[0][i] = x;
    this.p[1][i] = y;
    this.changed = true;
  }

  public void setParamY(final int i, final double y) {
    this.p[1][i] = y;
    this.changed = true;
  }

  public void setUse(final boolean b) {
    this.use = b;
    this.changed = true;
  }

  public void setWetPts(final XZCompare xzComp) {
    this.sp = new TreeSet(xzComp);
  }

  public void setX(final double x) {
    this.p[0][0] = x;
    this.changed = true;
  }

  public void setY(final double x) {
    this.p[1][0] = x;
    this.changed = true;
  }


  public final double TriArea(final double x1, final double y1, final double x2, final double y2,
      final double x3, final double y3) {
    double a;
    if ((x1 <= x2) && (x1 <= x3)) {
      a = 0.5 * Math.abs(((x2 - x1) * (y3 - y1)) - ((x3 - x1) * (y2 - y1)));
    } else if ((x2 <= x1) && (x2 <= x3)) {
      a = 0.5 * Math.abs(((x3 - x2) * (y1 - y2)) - ((x1 - x2) * (y3 - y2)));
    } else {
      a = 0.5 * Math.abs(((x1 - x3) * (y2 - y3)) - ((x2 - x3) * (y1 - y3)));
    }
    return a;
  }

}// end rscFoil



class Rudder implements Cloneable {
  int dir;
  public rscFoil rudder;
  public rscFoil skeg;
  boolean valid = false;

  public Rudder() {
    this.rudder = new rscFoil();
    this.skeg = new rscFoil();
    this.dir = 1;
    this.valid = true;
  }

  @Override
  public Object clone() {

    try {
      final Rudder r = (Rudder) super.clone();

      r.rudder = (rscFoil) this.rudder.clone();
      r.skeg = (rscFoil) this.skeg.clone();
      return r;
    } catch (final CloneNotSupportedException e) {
      throw new InternalError(e.toString());
    }

  } // end clone

  public double getArea() {
    double a = 0;
    if (this.rudder.use) {
      a = a + this.rudder.getArea();
    }
    if (this.skeg.use) {
      a = a + this.skeg.getArea();
    }
    return a;
  }

  public double getAreaX() {
    double x = 0;
    if (this.rudder.use) {
      x = x + (this.rudder.getArea() * this.rudder.getAreaX());
    }
    if (this.skeg.use) {
      x = x + (this.skeg.getArea() * this.skeg.getAreaX());
    }
    final double a = this.getArea();
    if (a > 0) {
      x = x / a;
    }
    return x;
  }

  public double getAreaY() {
    double x = 0;
    if (this.rudder.use) {
      x = x + (this.rudder.getArea() * this.rudder.getAreaY());
    }
    if (this.skeg.use) {
      x = x + (this.skeg.getArea() * this.skeg.getAreaY());
    }
    final double a = this.getArea();
    if (a > 0) {
      x = x / a;
    }
    return x;
  }

  public double getMaxX() {
    double x = 0;
    if (this.rudder.use) {
      x = Math.max(x, this.rudder.getMaxX());
    }
    if (this.skeg.use) {
      x = Math.max(x, this.skeg.getMaxX());
    }
    return x;
  }

  public double getMaxY() {
    double x = 0;
    if (this.rudder.use) {
      x = Math.max(x, this.rudder.getMaxY());
    }
    if (this.skeg.use) {
      x = Math.max(x, this.skeg.getMaxY());
    }
    return x;
  }

  public double getMinX() {
    double x = 1000000;
    if (this.rudder.use) {
      x = Math.min(x, this.rudder.getMinX());
    }
    if (this.skeg.use) {
      x = Math.min(x, this.skeg.getMinX());
    }
    return x;
  }

  public double getMinY() {
    double x = 1000000;
    if (this.rudder.use) {
      x = Math.min(x, this.rudder.getMinY());
    }
    if (this.skeg.use) {
      x = Math.min(x, this.skeg.getMinY());
    }
    return x;
  }

  public void setBase(final double b) {
    this.rudder.setBase(b);
    this.skeg.setBase(b);
  }

} // end Rudder


class Sail implements Cloneable {

  public static int BOOM = 2;
  public static int CLEW = 4;
  public static int GAFF = 3;
  public static int LEECH = 3;
  public static int LUFF = 1;

  public static int PEAK = 2;
  public static int ROACH = 4;
  public static int TACK = 0;
  public static int THROAT = 1;

  private double area;
  private double[][] c;
  private boolean changed;
  private double cX;

  private double cY;

  private int dir = 1;
  private double maxX;
  private double maxY;
  private double minX;
  private double minY;

  private double[][] p;
  public boolean use;
  public boolean useGaff;
  public boolean useRoach;

  public Sail() {
    this.p = new double[2][5];
    this.p[0][4] = 10.0;
    this.p[1][4] = 70.0;
    this.use = false;
    this.changed = true;
    this.useGaff = false;
    this.useRoach = false;
  }

  @Override
  public Object clone() {

    try {
      final Sail s = (Sail) super.clone();
      s.p = new double[2][5];
      s.p[0] = this.p[0].clone();
      s.p[1] = this.p[1].clone();
      // s.p = (double[][]) p.clone(); // doesn't work
      s.changed = true;
      return s;
    } catch (final CloneNotSupportedException e) {
      throw new InternalError(e.toString());
    }

  } // end clone

  public double getArea() {
    if (this.changed) {
      this.setSail();
    }
    return this.area;
  }

  public double getAreaX() {
    if (this.changed) {
      this.setSail();
    }
    return this.cX;
  }

  public double getAreaY() {
    if (this.changed) {
      this.setSail();
    }
    return this.cY;
  }

  public double getMaxX() {
    if (this.changed) {
      this.setSail();
    }
    return this.maxX;
  }

  public double getMaxY() {
    if (this.changed) {
      this.setSail();
    }
    return this.maxY;
  }

  public double getMinX() {
    if (this.changed) {
      this.setSail();
    }
    return this.minX;
  }

  public double getMinY() {
    if (this.changed) {
      this.setSail();
    }
    return this.minY;
  }

  public double getParamX(final int i) {
    return this.p[0][i];
  }

  public double getParamY(final int i) {
    return this.p[1][i];
  }

  public double getVal(final int j, final int i) {
    if (this.changed) {
      this.setSail();
    }
    return this.c[j][i];
  }

  public double getX(final int i) {
    if (this.changed) {
      this.setSail();
    }
    return this.c[0][i];
  }

  public double getY(final int i) {
    if (this.changed) {
      this.setSail();
    }
    return this.c[1][i];
  }

  public void setBoomAng(final double x) {
    this.p[1][2] = x;
    this.changed = true;
  }

  public void setBoomLen(final double x) {
    this.p[0][2] = x;
    this.changed = true;
  }

  public void setDir(final int i) {
    this.dir = i;
    this.changed = true;
  }

  public void setGaffAng(final double x) {
    this.p[1][3] = x;
    this.changed = true;
  }

  public void setGaffLen(final double x) {
    this.p[0][3] = x;
    this.changed = true;
  }

  public void setLuffAng(final double x) {
    this.p[1][1] = x;
    this.changed = true;
  }

  public void setLuffLen(final double x) {
    this.p[0][1] = x;
    this.changed = true;
  }

  public void setParam(final int i, final double x, final double y) {
    this.p[0][i] = x;
    this.p[1][i] = y;
    this.changed = true;
  }

  public void setParamX(final int i, final double x) {
    this.p[0][i] = x;
  }

  public void setParamY(final int i, final double y) {
    this.p[1][i] = y;
  }

  public void setRoachMax(final double x) {
    this.p[0][4] = x;
    this.changed = true;
  }

  public void setRoachPct(final double x) {
    this.p[1][4] = x;
    this.changed = true;
  }

  public void setSail() {
    double tx, ty;
    double mx, my;
    this.c = new double[2][5];
    // find corners
    this.c[0][Sail.TACK] = this.p[0][Sail.TACK];
    this.c[1][Sail.TACK] = this.p[1][Sail.TACK];

    this.c[0][Sail.THROAT] = this.c[0][Sail.TACK]
        + (this.dir * this.p[0][Sail.LUFF] * Math.sin(Math.toRadians(this.p[1][Sail.LUFF])));
    this.c[1][Sail.THROAT] = this.c[1][Sail.TACK]
        + (this.p[0][Sail.LUFF] * Math.cos(Math.toRadians(this.p[1][Sail.LUFF])));

    this.c[0][Sail.CLEW] = this.c[0][Sail.TACK] + (this.dir * this.p[0][Sail.BOOM]
        * Math.cos(Math.toRadians(this.p[1][Sail.BOOM] - this.p[1][Sail.LUFF])));
    this.c[1][Sail.CLEW] = this.c[1][Sail.TACK] + (this.p[0][Sail.BOOM]
        * Math.sin(Math.toRadians(this.p[1][Sail.BOOM] - this.p[1][Sail.LUFF])));

    if (this.useGaff) {
      this.c[0][Sail.PEAK] = this.c[0][Sail.THROAT] + (this.dir * this.p[0][Sail.GAFF]
          * Math.cos(Math.toRadians(this.p[1][Sail.GAFF] - this.p[1][Sail.LUFF])));
      this.c[1][Sail.PEAK] = this.c[1][Sail.THROAT] + (this.p[0][Sail.GAFF]
          * Math.sin(Math.toRadians(this.p[1][Sail.GAFF] - this.p[1][Sail.LUFF])));
      tx = this.c[0][Sail.PEAK];
      ty = this.c[1][Sail.PEAK];
    } else {
      tx = this.c[0][Sail.THROAT];
      ty = this.c[1][Sail.THROAT];
    }

    if (this.useRoach) {
      double h, v, l, d;
      h = Math.abs(tx - this.c[0][Sail.CLEW]);
      v = Math.abs(ty - this.c[1][Sail.CLEW]);
      l = Math.sqrt(Math.pow(h, 2) + Math.pow(v, 2));
      d = 0.01 * this.p[0][Sail.ROACH] * l;
      mx = this.c[0][Sail.CLEW] + (0.01 * this.p[1][Sail.ROACH] * (tx - this.c[0][Sail.CLEW]));
      my = this.c[1][Sail.CLEW] + (0.01 * this.p[1][Sail.ROACH] * (ty - this.c[1][Sail.CLEW]));
      this.c[0][Sail.LEECH] = mx + ((this.dir * d * v) / l);
      this.c[1][Sail.LEECH] = my + ((d * h) / l);
    }
    // extremes
    this.maxX =
        Math.max(Math.max(this.c[0][Sail.TACK], this.c[0][Sail.THROAT]), this.c[0][Sail.CLEW]);
    this.maxY =
        Math.max(Math.max(this.c[1][Sail.TACK], this.c[1][Sail.THROAT]), this.c[1][Sail.CLEW]);
    this.minX =
        Math.min(Math.min(this.c[0][Sail.TACK], this.c[0][Sail.THROAT]), this.c[0][Sail.CLEW]);
    this.minY =
        Math.min(Math.min(this.c[1][Sail.TACK], this.c[1][Sail.THROAT]), this.c[1][Sail.CLEW]);
    if (this.useGaff) {
      this.maxX = Math.max(this.maxX, this.c[0][Sail.PEAK]);
      this.maxY = Math.max(this.maxY, this.c[1][Sail.PEAK]);
      this.minX = Math.min(this.minX, this.c[0][Sail.PEAK]);
      this.minY = Math.min(this.minY, this.c[1][Sail.PEAK]);
    }
    if (this.useRoach) {
      this.maxX = Math.max(this.maxX, this.c[0][Sail.LEECH]);
      this.maxY = Math.max(this.maxY, this.c[1][Sail.LEECH]);
      this.minX = Math.min(this.minX, this.c[0][Sail.LEECH]);
      this.minY = Math.min(this.minY, this.c[1][Sail.LEECH]);
    }
    // area, CoA

    int i = 3;
    mx = this.c[0][Sail.TACK] + this.c[0][Sail.THROAT] + this.c[0][Sail.CLEW];
    my = this.c[1][Sail.TACK] + this.c[1][Sail.THROAT] + this.c[1][Sail.CLEW];
    if (this.useGaff) {
      mx = mx + this.c[0][Sail.PEAK];
      my = my + this.c[1][Sail.PEAK];
      i++;
    }
    if (this.useRoach) {
      mx = mx + this.c[0][Sail.LEECH];
      my = my + this.c[1][Sail.LEECH];
      i++;
    }
    mx = mx / i;
    my = my / i;

    double ta, wx, wy, hx, hy;
    ta = this.TriArea(mx, my, this.c[0][Sail.CLEW], this.c[1][Sail.CLEW], this.c[0][Sail.TACK],
        this.c[1][Sail.TACK]);
    this.area = ta;
    wx = (ta * (mx + this.c[0][Sail.CLEW] + this.c[0][Sail.TACK])) / 3.0;
    wy = (ta * (my + this.c[1][Sail.CLEW] + this.c[1][Sail.TACK])) / 3.0;

    ta = this.TriArea(mx, my, this.c[0][Sail.TACK], this.c[1][Sail.TACK], this.c[0][Sail.THROAT],
        this.c[1][Sail.THROAT]);
    this.area = this.area + ta;
    wx = wx + ((ta * (mx + this.c[0][Sail.TACK] + this.c[0][Sail.THROAT])) / 3.0);
    wy = wy + ((ta * (my + this.c[1][Sail.TACK] + this.c[1][Sail.THROAT])) / 3.0);
    hx = this.c[0][Sail.THROAT];
    hy = this.c[1][Sail.THROAT];

    if (this.useGaff) {
      ta = this.TriArea(mx, my, this.c[0][Sail.THROAT], this.c[1][Sail.THROAT],
          this.c[0][Sail.PEAK], this.c[1][Sail.PEAK]);
      this.area = this.area + ta;
      wx = wx + ((ta * (mx + this.c[0][Sail.THROAT] + this.c[0][Sail.PEAK])) / 3.0);
      wy = wy + ((ta * (my + this.c[1][Sail.THROAT] + this.c[1][Sail.PEAK])) / 3.0);
      hx = this.c[0][Sail.PEAK];
      hy = this.c[1][Sail.PEAK];
    }

    if (this.useRoach) {
      ta = this.TriArea(mx, my, hx, hy, this.c[0][Sail.LEECH], this.c[1][Sail.LEECH]);
      this.area = this.area + ta;
      wx = wx + ((ta * (mx + hx + this.c[0][Sail.LEECH])) / 3.0);
      wy = wy + ((ta * (my + hy + this.c[1][Sail.LEECH])) / 3.0);
      hx = this.c[0][Sail.LEECH];
      hy = this.c[1][Sail.LEECH];
    }

    ta = this.TriArea(mx, my, hx, hy, this.c[0][Sail.CLEW], this.c[1][Sail.CLEW]);
    this.area = this.area + ta;
    wx = wx + ((ta * (mx + hx + this.c[0][Sail.CLEW])) / 3.0);
    wy = wy + ((ta * (my + hy + this.c[1][Sail.CLEW])) / 3.0);

    if (this.area > 0) {
      this.cX = wx / this.area;
      this.cY = wy / this.area;
    } else {
      this.cX = 0.0;
      this.cY = 0.0;
    }

    this.changed = false;
  }

  public void setUse(final boolean b) {
    this.use = b;
    this.changed = true;
  }

  public void setUseGaff(final boolean b) {
    this.useGaff = b;
    this.changed = true;
  }

  public void setUseRoach(final boolean b) {
    this.useRoach = b;
    this.changed = true;
  }


  public void setX(final double x) {
    this.p[0][0] = x;
    this.changed = true;
  }

  public void setY(final double x) {
    this.p[1][0] = x;
    this.changed = true;
  }

  public final double TriArea(final double x1, final double y1, final double x2, final double y2,
      final double x3, final double y3) {
    double a;
    if ((x1 <= x2) && (x1 <= x3)) {
      a = 0.5 * Math.abs(((x2 - x1) * (y3 - y1)) - ((x3 - x1) * (y2 - y1)));
    } else if ((x2 <= x1) && (x2 <= x3)) {
      a = 0.5 * Math.abs(((x3 - x2) * (y1 - y2)) - ((x1 - x2) * (y3 - y2)));
    } else {
      a = 0.5 * Math.abs(((x1 - x3) * (y2 - y3)) - ((x2 - x3) * (y1 - y3)));
    }
    return a;
  }

}// end Sail


