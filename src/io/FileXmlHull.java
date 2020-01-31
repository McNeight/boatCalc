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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ListIterator;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;
import boat.Hull;
import boat.Sail;
import boat.rscFoil;
import geom.Line;
import geom.rawLine;
import io.XmlHullReader;;

public class FileXmlHull {
  /** Define a Hull. */
  Hull h;

  /**
   * Gets the data.
   *
   * @param hullfile the hullfile
   * @return the data
   */
  // read native (xml) data file
  public Hull openHull(final File hullfile) {
    // declarations
    final DefaultHandler hd = new XmlHullReader();
    final SAXParserFactory f = SAXParserFactory.newInstance();
    try {
      final SAXParser saxParser = f.newSAXParser();
      saxParser.parse(hullfile, hd);
    } catch (final Throwable t) {
      t.printStackTrace();
    }
    this.h.setLines();
    this.h.valid = true;
    return h;
  } // end getData

  /**
   * Save data.
   *
   * @param xmlFile the xml file
   */
  public void saveHull(File xmlFile, Hull h) {
    Emitter er = new Emitter();
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
      er.emitln(w, "<?xml version='1.0' encoding='UTF-8'?>");
      er.emitln(w,
          "<hull stations='" + h.Stations.length + "' lines='" + h.Offsets.size() + "'>");

      er.emitln(w, "   <water>" + h.bcf.DF0d.format(h.units.WATER) + "</water>");
      er.emitln(w, "   <units>" + h.bcf.DF0d.format(h.units.UNITS) + "</units>");

      er.emitln(w, "   <lstem>" + h.bStems[0] + "</lstem>");
      er.emitln(w, "   <rstem>" + h.bStems[1] + "</rstem>");

      er.emitln(w, "   <designer>" + h.designer + "</designer>");
      er.emitln(w, "   <designname>" + h.boatname + "</designname>");
      er.emitln(w, "   <baseline> " + h.bcf.DF4d.format(h.base) + " </baseline>");

      er.emitln(w, "   <weights>");
      for (int i = 0; i < 10; i++) {
        er.emitln(w, "     <wgtItem>");
        er.emitln(w, "      <wgtLbl>" + h.wgtLbl[i] + "</wgtLbl>");
        er.emitln(w, "      <wgtWgt> " + h.bcf.DF4d.format(h.wgtWgt[i]) + " </wgtWgt>");
        er.emitln(w, "      <wgtX> " + h.bcf.DF4d.format(h.wgtX[i]) + " </wgtX>");
        er.emitln(w, "      <wgtY> " + h.bcf.DF4d.format(h.wgtY[i]) + " </wgtY>");
        er.emitln(w, "      <wgtZ> " + h.bcf.DF4d.format(h.wgtZ[i]) + " </wgtZ>");
        er.emitln(w, "     </wgtItem>");
      }
      er.emitln(w, "   </weights>");

      er.emitln(w, "   <stations>");
      for (int i = 0; i < h.Stations.length; i++) {
        er.emitln(w, "      <stnX> " + h.bcf.DF4d.format(h.Stations[i]) + " </stnX>");
      }
      er.emitln(w, "   </stations>");

      ListIterator<rawLine> l;
      l = h.Offsets.listIterator();
      while (l.hasNext()) {
        final rawLine rL = l.next();
        final Line ln = rL.ln;
        er.emitln(w, "   <line>");
        er.emitln(w, "      <linename>" + rL.lnName + "</linename>");
        for (int i = 0; i < h.Stations.length; i++) {
          er.emitln(w, "      <point>");
          er.emitln(w, "         <valX>" + h.bcf.DF4d.format(ln.valX(i)) + "</valX>");
          er.emitln(w, "         <valY>" + h.bcf.DF4d.format(ln.valY(i)) + "</valY>");
          er.emitln(w, "         <valZ>" + h.bcf.DF4d.format(ln.valZ(i)) + "</valZ>");
          if (ln.valid(i)) {
            er.emitln(w, "         <ptValid>true</ptValid>");
          } else {
            er.emitln(w, "         <ptValid>false</ptValid>");
          }
          er.emitln(w, "      </point>");
        }
        er.emitln(w, "   </line>");
      }

      if (h.rig.valid) {
        er.emitln(w, "   <rig dir='" + h.rig.dir + "' valid='" + h.rig.valid + "' >");

        int is;
        Sail sl;
        for (is = 0; is < 3; is++) {
          if (is == 0) {
            sl = h.rig.main;
            er.emitln(w, "   <sail type='main' use='" + sl.use + "' head='" + sl.useGaff
                + "' roach='" + sl.useRoach + "'>");
          } else if (is == 1) {
            sl = h.rig.jib;
            er.emitln(w, "   <sail type='jib' use='" + sl.use + "' head='" + sl.useGaff
                + "' roach='" + sl.useRoach + "'>");
          } else {
            sl = h.rig.mizzen;
            er.emitln(w, "   <sail type='mizzen' use='" + sl.use + "' head='" + sl.useGaff
                + "' roach='" + sl.useRoach + "'>");
          }
          for (int js = 0; js < 5; js++) {
            er.emitln(w, "         <rigX>" + h.bcf.DF4d.format(sl.getParamX(js)) + "</rigX>");
            er.emitln(w, "         <rigZ>" + h.bcf.DF4d.format(sl.getParamY(js)) + "</rigZ>");
          }
          er.emitln(w, "   </sail>");
        }
        er.emitln(w, "   </rig>");
      }

      if (h.rudder.valid) {
        er.emitln(w, "   <rudder valid='" + h.rudder.valid + "' >");

        rscFoil f;
        for (int i = 0; i <= 1; i++) {
          if (i == 0) {
            f = h.rudder.rudder;
            er.emitln(w, "   <foil type='rudder' use='" + f.use + "'>");
          } else {
            f = h.rudder.skeg;
            er.emitln(w, "   <foil type='skeg' use='" + f.use + "'>");
          }
          for (int jf = 0; jf < 4; jf++) {
            er.emitln(w, "         <foilX>" + h.bcf.DF4d.format(f.getParamX(jf)) + "</foilX>");
            er.emitln(w, "         <foilZ>" + h.bcf.DF4d.format(f.getParamY(jf)) + "</foilZ>");
          }
          er.emitln(w, "   </foil>");
        }
        er.emitln(w, "   </rudder>");
      }



      er.emitln(w, "</hull>");

      w.flush();
      w.close();
      h.bChanged = false;
    } catch (final IOException e) {
      System.out.println(e);
    }

  }// end saveData

}
