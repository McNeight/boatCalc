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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import boat.Hull;
import geom.Line;
import geom.Point;
import geom.rawLine;

public class FileHulls {
  /** Define a Hull. */
  Hull h;

  /**
   * Gets the hulls.
   *
   * @param hullfile the hullfile
   * @return the hulls
   */
  // read "Hulls" data file
  public Hull openHull(final File hullfile) {
    this.h = new Hull();
    this.h.Offsets = new ArrayList<>();
    BufferedReader r;
    String l;
    // initialization
    this.h.Stations = new double[13];
    this.h.newWgts();
    this.h.bChanged = false;

    int n, m;
    int i, j;
    double x = 0, y, z;
    double z_min = 1000.0, z_max = -1000.0;

    try {

      // read first line - name and constants

      final String filename = hullfile.getName();
      n = filename.indexOf('.');
      if (n > 0) {
        this.h.boatname = filename.substring(0, n);
      } else {
        this.h.boatname = filename;
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
        this.h.Stations[i] = x;
      }

      for (j = 0; j < n; j++) {
        final rawLine rL = new rawLine();
        rL.ln = new Line(p[j]);
        rL.lnName = "Line " + Integer.toString(j + 1);
        this.h.Offsets.add(rL);
      }

      // read waste lines.
      try {
        m = 17;
        for (i = 1; i <= m; i++) {
          l = r.readLine();
        }
        l = r.readLine();
        this.h.designer = l;
        l = r.readLine();
        this.h.boatname = l;
        for (i = 1; i <= 3; i++) {
          l = r.readLine();
        }
        this.h.wgtWgt[1] = Double.parseDouble(r.readLine().trim());
        this.h.wgtY[1] = Double.parseDouble(r.readLine().trim());
        this.h.wgtZ[1] = Double.parseDouble(r.readLine().trim());

      } catch (final NoSuchElementException e) {
        this.h.base = 0.0;
        // boatname="unknown";
        System.out.println("nse");
        System.out.println(e);
      } catch (final NullPointerException npe) {
        this.h.base = 0.0;
        // boatname="unknown";
        System.out.println("npe");
        System.out.println(npe);
      }

      r.close();
      if (z_min >= 0.0) {
        this.h.base = -(z_min + (0.25 * (z_max - z_min)));
      } else {
        this.h.base = 0.0;
      }

      this.h.setLines();
      this.h.valid = true;
    }

    catch (final NumberFormatException e) {
      e.printStackTrace();
    } catch (final NoSuchElementException e) {
      e.printStackTrace();
    } catch (final FileNotFoundException e) {
      e.printStackTrace();
    } catch (final IOException ioe) {
      ioe.printStackTrace();
    } catch (final NullPointerException npe) {
      npe.printStackTrace();
    }

    return this.h;

  }// end get hulls

  /**
   * Save hulls.
   *
   * @param hullFile the hull file
   */
  public void saveHull(File hullFile, final Hull h) {

    final Emitter er = new Emitter();
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
      final int n = h.Offsets.size();
      // number of chines
      er.emitln(w, Integer.toString(n));
      // left end
      double xmin = -1000000.0;
      double xmax = +1000000.0;

      // beging first block
      for (i = 0; i < n; i++) {
        x = h.hLines[i].min("X") - h.gx_min;
        y = h.hLines[i].hXY.interp4P(x) - h.gy_min;
        z = h.hLines[i].hXZ.interp4P(x) - h.gz_min;
        er.emitln(w, h.bcf.DF4d.format(y));
        er.emitln(w, h.bcf.DF4d.format(z));
        er.emitln(w, h.bcf.DF4d.format(x));

        // find max, min for spacing of other stations
        xmin = Math.max(x, xmin);
        x = h.hLines[i].max("X");
        xmax = Math.min(x, xmax);
      }


      for (int j = 1; j <= 3; j++) {

        x = xmin + ((j) * 0.25 * (xmax - xmin));

        for (i = 0; i < n; i++) {
          y = h.hLines[i].hXY.interp4P(x) - h.gy_min;
          z = h.hLines[i].hXZ.interp4P(x) - h.gz_min;
          er.emitln(w, h.bcf.DF4d.format(y));
          er.emitln(w, h.bcf.DF4d.format(z));
          er.emitln(w, h.bcf.DF4d.format(x - h.gx_min));
        }
      }

      // right end
      for (i = 0; i < n; i++) {
        x = h.hLines[i].max("X");
        y = h.hLines[i].hXY.interp4P(x) - h.gy_min;
        z = h.hLines[i].hXZ.interp4P(x) - h.gz_min;
        er.emitln(w, h.bcf.DF4d.format(y));
        er.emitln(w, h.bcf.DF4d.format(z));
        er.emitln(w, h.bcf.DF4d.format(x - h.gx_min));
      }
      er.emitln(w, Integer.toString(-1));

      // begin second block
      for (i = 0; i < n; i++) {
        x = h.hLines[i].min("X");
        y = h.hLines[i].hXY.interp4P(x) - h.gy_min;
        z = h.hLines[i].hXZ.interp4P(x) - h.gz_min;
        er.emitln(w, h.bcf.DF4d.format(y));
        er.emitln(w, h.bcf.DF4d.format(z));
        er.emitln(w, h.bcf.DF4d.format(x - h.gx_min));
      }

      for (int j = 1; j <= 11; j++) {
        x = xmin + ((j) * 0.0833333 * (xmax - xmin));
        // emitln(w,"line "+j+" "+x);

        for (i = 0; i < n; i++) {
          y = h.hLines[i].hXY.interp4P(x) - h.gy_min;
          z = h.hLines[i].hXZ.interp4P(x) - h.gz_min;
          er.emitln(w, h.bcf.DF4d.format(y));
          er.emitln(w, h.bcf.DF4d.format(z));
          er.emitln(w, h.bcf.DF4d.format(x - h.gx_min));
        }
      }

      // right end
      for (i = 0; i < n; i++) {
        x = h.hLines[i].max("X");
        y = h.hLines[i].hXY.interp4P(x) - h.gy_min;
        z = h.hLines[i].hXZ.interp4P(x) - h.gz_min;
        er.emitln(w, h.bcf.DF4d.format(y));
        er.emitln(w, h.bcf.DF4d.format(z));
        er.emitln(w, h.bcf.DF4d.format(x - h.gx_min));
      }

      // frames
      for (int j = 0; j < 8; j++) {
        x = xmin + ((j) * 0.1429 * (xmax - xmin));
        er.emitln(w, h.bcf.DF4d.format(x - h.gx_min));
      }

      er.emitln(w, Integer.toString(0)); // mast length
      er.emitln(w, Integer.toString(0)); // boom from deck
      er.emitln(w, Integer.toString(0)); // mast dist aft
      er.emitln(w, Integer.toString(0)); // luff length
      er.emitln(w, Integer.toString(0)); // head length
      er.emitln(w, Integer.toString(0)); // head angle
      er.emitln(w, Integer.toString(0)); // foot length
      er.emitln(w, Integer.toString(0)); // deck-stepped
      er.emitln(w, Integer.toString(-1)); // keel stepped
      er.emitln(w, h.designer); // designer name
      er.emitln(w, h.boatname); // hull info
      er.emitln(w, "here@there"); // url
      er.emitln(w, "01-01-2004"); // date
      er.emitln(w, " "); // unused
      er.emitln(w, Double.toString(h.wgtWgt[1])); // ballast weight
      er.emitln(w, Double.toString(h.wgtZ[1])); // ballast x
      er.emitln(w, Double.toString(h.wgtY[1])); // ballast y
      er.emitln(w, Integer.toString(0)); // auto smoothing

      w.flush();
      w.close();
      h.bChanged = false;

    } catch (final IOException e) {
      System.out.println(e);
    }


  }// end saveHulls

}
