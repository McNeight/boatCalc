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
package io;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import boat.Hull;
import boat.Sail;
import boat.rscFoil;
import geom.Line;
import geom.Point;
import geom.rawLine;

/**
 * The Class XmlHullReader.
 */
class XmlHullReader extends DefaultHandler {
  /** The cur foil. */
  rscFoil curFoil;
  /** The cur sail. */
  Sail curSail;
  /** The current hull. */
  Hull h;
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
    } else if (this.tags[this.itag].compareTo("valX") == 0) {
      this.sbX.append(s);
    } else if (this.tags[this.itag].compareTo("valY") == 0) {
      this.sbY.append(s);
    } else if (this.tags[this.itag].compareTo("valZ") == 0) {
      this.sbZ.append(s);
    } else if (this.tags[this.itag].compareTo("ptValid") == 0) {
      this.sbV.append(s);
    } else if (this.tags[this.itag].compareTo("wgtLbl") == 0) {
      this.sbWL.append(s.trim());
    } else if (this.tags[this.itag].compareTo("wgtWgt") == 0) {
      this.sbWW.append(s.trim());
    } else if (this.tags[this.itag].compareTo("wgtX") == 0) {
      this.sbWX.append(s.trim());
    } else if (this.tags[this.itag].compareTo("wgtY") == 0) {
      this.sbWY.append(s.trim());
    } else if (this.tags[this.itag].compareTo("wgtZ") == 0) {
      this.sbWZ.append(s.trim());
    } else if (this.tags[this.itag].compareTo("rigX") == 0) {
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
    } else if (qn.compareTo("point") == 0) {
      this.p[this.iLine] = new Point(this.x, this.y, this.z, this.v);
    } else if (qn.compareTo("line") == 0) {
      final rawLine rL = new rawLine();
      rL.ln = new Line(this.p);
      rL.lnName = this.linename;
      this.h.Offsets.add(rL);
    } else if (qn.compareTo("stnX") == 0) {
      this.h.Stations[this.iStn] = Double.parseDouble(this.sbStnX.toString());
    } else if (qn.compareTo("baseline") == 0) {
      this.h.base = Double.parseDouble(this.sbBase.toString());
    } else if (qn.compareTo("water") == 0) {
      this.h.units.WATER = Integer.parseInt(this.sbWater.toString());
    } else if (qn.compareTo("lstem") == 0) {
      ts = this.sbLStem.toString();
      if (ts.compareTo("true") == 0) {
        this.h.bStems[0] = true;
      } else {
        this.h.bStems[0] = false;
      }
    } else if (qn.compareTo("rstem") == 0) {
      ts = this.sbRStem.toString();
      if (ts.compareTo("true") == 0) {
        this.h.bStems[1] = true;
      } else {
        this.h.bStems[1] = false;
      }
    } else if (qn.compareTo("units") == 0) {
      this.h.units.UNITS = Integer.parseInt(this.sbUnits.toString());
    } else if (qn.compareTo("designer") == 0) {
      this.h.designer = this.sbNA.toString();
    } else if (qn.compareTo("designname") == 0) {
      this.h.boatname = this.sbDName.toString();
    } else if (qn.compareTo("linename") == 0) {
      this.linename = this.sbLName.toString();
    } else if (qn.compareTo("wgtLbl") == 0) {
      this.h.wgtLbl[this.iWgt] = this.sbWL.toString();
    } else if (qn.compareTo("wgtWgt") == 0) {
      this.h.wgtWgt[this.iWgt] = Double.parseDouble(this.sbWW.toString());
    } else if (qn.compareTo("wgtX") == 0) {
      this.h.wgtX[this.iWgt] = Double.parseDouble(this.sbWX.toString());
    } else if (qn.compareTo("wgtY") == 0) {
      this.h.wgtY[this.iWgt] = Double.parseDouble(this.sbWY.toString());
    } else if (qn.compareTo("wgtZ") == 0) {
      this.h.wgtZ[this.iWgt] = Double.parseDouble(this.sbWZ.toString());
    } else if (qn.compareTo("rigX") == 0) {
      this.iSail++;
      this.curSail.setParamX(this.iSail, Double.parseDouble(this.sbRigX.toString()));
    } else if (qn.compareTo("rigZ") == 0) {
      this.curSail.setParamY(this.iSail, Double.parseDouble(this.sbRigY.toString()));
    } else if (qn.compareTo("foilX") == 0) {
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
    this.h.base = 0.0;
    this.h.boatname = "boatCalc";
    this.h.designer = "boatCalc";
    this.tags = new String[10];
    this.itag = -1;
    this.h.Offsets = new ArrayList<>();
    this.h.bChanged = false;
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
      this.h.Stations = new double[m];
      this.iStn = -1;
      this.h.newWgts();
      this.iWgt = -1;
    } else if (qn.compareTo("rig") == 0) {
      this.h.rig.dir = Integer.parseInt(a.getValue("dir"));
      this.h.rig.valid = Boolean.valueOf(a.getValue("valid"));
    } else if (qn.compareTo("sail") == 0) {
      final String t = a.getValue("type");
      if (t.compareTo("main") == 0) {
        this.curSail = this.h.rig.main;
      } else if (t.compareTo("jib") == 0) {
        this.curSail = this.h.rig.jib;
      } else {
        this.curSail = this.h.rig.mizzen;
      }
      this.curSail.setDir(this.h.rig.dir);
      this.curSail.use = Boolean.valueOf(a.getValue("use"));
      this.curSail.useGaff = Boolean.valueOf(a.getValue("head"));
      this.curSail.useRoach = Boolean.valueOf(a.getValue("roach"));
      this.iSail = -1;
    } else if (qn.compareTo("rigX") == 0) {
      this.sbRigX = new StringBuffer();
    } else if (qn.compareTo("rigZ") == 0) {
      this.sbRigY = new StringBuffer();
    } else if (qn.compareTo("rudder") == 0) {
      this.h.rudder.valid = Boolean.valueOf(a.getValue("valid"));
    } else if (qn.compareTo("foil") == 0) {
      final String t = a.getValue("type");
      if (t.compareTo("rudder") == 0) {
        this.curFoil = this.h.rudder.rudder;
      } else {
        this.curFoil = this.h.rudder.skeg;
      }
      this.curFoil.use = Boolean.valueOf(a.getValue("use"));
      this.iFoil = -1;
    } else if (qn.compareTo("foilX") == 0) {
      this.sbFoilX = new StringBuffer();
    } else if (qn.compareTo("foilZ") == 0) {
      this.sbFoilY = new StringBuffer();
    } else if (qn.compareTo("wgtItem") == 0) {
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
      this.p = new Point[this.h.Stations.length];
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
    } else if (qn.compareTo("wgtLbl") == 0) {
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
}// end XmlHullReader
