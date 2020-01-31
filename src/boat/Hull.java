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
package boat;

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
import geom.Line;
import geom.Point;
import geom.YZCompare;
import geom.ZCompare;
import geom.hLine;
import geom.rawLine;
import util.bcFormat;
import util.bcUnits;

// TODO: Auto-generated Javadoc
/**
 * The Class Hull.
 */
public class Hull {

  /**
   * The Class bcHandler.
   */
  class bcHandler extends DefaultHandler {

    /** The cur foil. */
    rscFoil curFoil;

    /** The cur sail. */
    Sail curSail;

    /** The i foil. */
    int iFoil;

    /** The i line. */
    int iLine;

    /** The i sail. */
    int iSail;

    /** The i stn. */
    int iStn;

    /** The itag. */
    int itag;

    /** The i wgt. */
    int iWgt;

    /** The l. */
    Line l;

    /** The linecount. */
    int linecount = 0;

    /** The linename. */
    String linename;

    /** The p. */
    Point[] p;

    /** The sb L name. */
    StringBuffer sbBase, sbNA, sbDName, sbLName;

    /** The sb foil Y. */
    StringBuffer sbFoilX, sbFoilY;

    /** The sb R stem. */
    StringBuffer sbLStem, sbRStem;

    /** The sb rig Y. */
    StringBuffer sbRigX, sbRigY;

    /** The sb stn X. */
    StringBuffer sbStnX;

    /** The sb units. */
    StringBuffer sbWater, sbUnits;

    /** The sb WZ. */
    StringBuffer sbWL, sbWW, sbWX, sbWY, sbWZ;

    /** The sb V. */
    StringBuffer sbX, sbY, sbZ, sbV;

    /** The tags. */
    String[] tags;

    /** The v. */
    boolean v = true;

    /** The z. */
    double x, y, z;


    /**
     * Characters.
     *
     * @param buf the buf
     * @param offset the offset
     * @param len the len
     */
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

    /**
     * End document.
     *
     * @throws SAXException the SAX exception
     */
    @Override
    public void endDocument() throws SAXException {}

    /**
     * End element.
     *
     * @param ns the ns
     * @param n the n
     * @param qn the qn
     * @throws SAXException the SAX exception
     */
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

    /**
     * Start document.
     *
     * @throws SAXException the SAX exception
     */
    @Override
    public void startDocument() throws SAXException {

      Hull.this.base = 0.0;
      Hull.this.boatname = "boatCalc";
      Hull.this.designer = "boatCalc";

      this.tags = new String[10];
      this.itag = -1;

      Hull.this.Offsets = new ArrayList<>();
      Hull.this.bChanged = false;

    }

    /**
     * Start element.
     *
     * @param ns the ns
     * @param n the n
     * @param qn the qn
     * @param a the a
     * @throws SAXException the SAX exception
     */
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

  /** The ang heel. */
  public double angHeel = 0.0;

  /** The base. */
  public double base;

  /** The b changed. */
  public boolean bChanged;

  /** The board. */
  public Centerboard board;

  /** The boatname. */
  public String boatname;

  /** The b stems. */
  public boolean bStems[];

  /** The cx. */
  public int CX = 1;

  /** The cy. */
  public int CY = 2;

  /** The cz. */
  public int CZ = 3;

  /** The designer. */
  public String designer;

  /** The disp. */
  public int DISP = 4;

  /** The Disp tri. */
  public Set<double[]> DispTri;

  /** The gx max. */
  public double gx_max;

  /** The gx min. */
  // derived data
  public double gx_min;

  /** The gy max. */
  public double gy_max;

  /** The gy min. */
  public double gy_min;

  /** The gz max. */
  public double gz_max;

  /** The gz min. */
  public double gz_min;

  /** The h lines. */
  public hLine[] hLines;

  /** The h vals. */
  public double[] hVals;

  /** The lwl left. */
  public double lwlLeft;

  /** The lwl right. */
  public double lwlRight;

  /** The ndiv. */
  // displacement curve data
  public int NDIV = 100;

  /** The Offsets. */
  public java.util.List<rawLine> Offsets;

  /** The rig. */
  public Rig rig;

  /** The rudder. */
  public Rudder rudder;

  /** The sarea. */
  // Constants
  public int SAREA = 0;

  /** The s lines. */
  public hLine[] sLines;

  /** The Stations. */
  public double[] Stations;

  /** The units. */
  public bcUnits units;

  /** The valid. */
  // basic data
  public boolean valid;

  /** The v disp. */
  public double[][] vDisp;

  /** The v wet. */
  public double[] vWet;


  /** The v WL. */
  public double[][] vWL;

  /** The wgt lbl. */
  public String wgtLbl[];

  /** The wgt wgt. */
  public double wgtWgt[];

  /** The wgt X. */
  public double wgtX[];

  /** The wgt Y. */
  public double wgtY[];

  /** The wgt Z. */
  public double wgtZ[];

  /** The bcf. */
  bcFormat bcf;

  /** The dx. */
  double dx;

  /** The narea. */
  int NAREA = 8;

  /** The wetted. */
  int WETTED = 7;

  /** The wl left. */
  int WL_LEFT = 5;


  /** The wl right. */
  int WL_RIGHT = 6;

  /**
   * Instantiates a new hull.
   */
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

  /**
   * Calc disp.
   */
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


  /**
   * Gets the area.
   *
   * @param x the x
   * @param ang the ang
   * @param trisave the trisave
   * @return the area
   */
  public double[] getArea(final double x, final double ang, final boolean trisave) {
    final SortedSet<Point> ss = this.getStnSet(x, ang);
    final Iterator<Point> si = ss.iterator();
    Point p1, p2;
    double t1y, t1z, t2y, t2z, t3y, t3z;
    double area, cy, cz;
    double wlRight = 0;
    double wlLeft = 0;
    double wetted = 0;
    t3y = 0;
    t3z = 0;

    if (trisave) {
      this.DispTri = new HashSet<>();
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

    p1 = ss.last();

    while (si.hasNext()) {

      p2 = si.next();

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


  /**
   * Gets the data.
   *
   * @param hullfile the hullfile
   * @return the data
   */
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

  /**
   * Gets the hulls.
   *
   * @param hullfile the hullfile
   * @return the hulls
   */
  // read "Hulls" data file
  public void getHulls(final File hullfile) {
    this.Offsets = new ArrayList<>();
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

  /**
   * Gets the station.
   *
   * @param tx the tx
   * @param ang the ang
   * @return the station
   */
  public Iterator<Point> getStation(final double tx, final double ang) {
    final SortedSet<Point> ts = this.getStnSet(tx, ang);
    return ts.iterator();
  }


  /**
   * Gets the station.
   *
   * @param i the i
   * @param ang the ang
   * @return the station
   */
  public Iterator<Point> getStation(final int i, final double ang) {
    final double tx = this.Stations[i];
    return this.getStation(tx, ang);
  }

  /**
   * Gets the stn set.
   *
   * @param tx the tx
   * @param ang the ang
   * @return the stn set
   */
  public SortedSet<Point> getStnSet(final double tx, final double ang) {
    final double sinang = Math.sin(Math.toRadians(ang));
    final double cosang = Math.cos(Math.toRadians(ang));
    double ty, tz;

    final Set<Point> s = new HashSet<>();

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

    final Iterator<Point> si = s.iterator();
    final SortedSet<Point> ts = new TreeSet<>(yzComp2);
    while (si.hasNext()) {
      ts.add(si.next());
    }
    return ts;

  }// end getStation

  /**
   * New wgts.
   */
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


  /**
   * Save data.
   *
   * @param xmlFile the xml file
   */
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

      ListIterator<rawLine> l;
      l = this.Offsets.listIterator();
      while (l.hasNext()) {
        final rawLine rL = l.next();
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

  /**
   * Save hulls.
   *
   * @param hullFile the hull file
   */
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


  /**
   * Sets the lines.
   */
  public void setLines() {
    ListIterator<rawLine> l;
    final int n = this.Offsets.size();
    int i = 0;

    this.hLines = new hLine[n];

    l = this.Offsets.listIterator();
    while (l.hasNext()) {
      final rawLine rL = l.next();
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
    final SortedSet<Point> sLow = new TreeSet<>(zComp);
    final SortedSet<Point> sHigh = new TreeSet<>(zComp);
    Iterator<Point> si;
    Line ln;
    Point p;
    final int m = this.Stations.length - 1;
    l = this.Offsets.listIterator();
    while (l.hasNext()) {
      final rawLine rL = l.next();
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
      ln.setPoint(si.next(), i);
      i++;
    }
    this.sLines[0] = new hLine(ln.points, this.base);

    ln = new Line(sHigh.size());
    i = 0;
    si = sHigh.iterator();
    while (si.hasNext()) {
      ln.setPoint(si.next(), i);
      i++;
    }
    this.sLines[1] = new hLine(ln.points, this.base);

  } // end setLines

  /**
   * Sets the LWL.
   */
  public void setLWL() {
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

  /**
   * Tri area.
   *
   * @param x1 the x 1
   * @param y1 the y 1
   * @param x2 the x 2
   * @param y2 the y 2
   * @param x3 the x 3
   * @param y3 the y 3
   * @return the double
   */
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

  /**
   * Emit.
   *
   * @param w the w
   * @param s the s
   */
  private void emit(final BufferedWriter w, final String s) {
    try {
      w.write(s, 0, s.length());
    } catch (final IOException e) {
      System.out.println(e);
    }
  }

  /**
   * Emitln.
   *
   * @param w the w
   * @param s the s
   */
  private void emitln(final BufferedWriter w, final String s) {
    this.emit(w, s);
    this.emit(w, System.getProperty("line.separator"));
  }



} // end class Hull


