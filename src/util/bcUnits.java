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
package util;

// TODO: Auto-generated Javadoc
/**
 * The Class bcUnits.
 */
public class bcUnits {

  /** The units. */
  public int UNITS = 0;

  /** The water. */
  public int WATER = 0;

  /** The l mom. */
  String[] lMom = {" in.-lbs.", "ft.-lbs.", "cc-Kg", "m-Kg"};

  /** The l vol. */
  String[] lVol = {" cu. in.", " cu. ft.", " cc", " cu m"};

  /** The l wgt. */
  String[] lWgt = {" lbs.", " lbs.", " Kg", " Kg"};

  /** The v 2 w. */
  double[][] v2w;

  /**
   * Instantiates a new bc units.
   */
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

  /**
   * Coef area.
   *
   * @return the double
   */
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

  /**
   * Coef PPI.
   *
   * @return the double
   */
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

  /**
   * Lbl area.
   *
   * @return the string
   */
  public String lblArea() {
    String r;
    if (this.UNITS <= 1) {
      r = " sq. ft.";
    } else {
      r = " sq. m.";
    }
    return r;
  }// end lblArea

  /**
   * Lbl mom.
   *
   * @return the string
   */
  public String lblMom() {
    return this.lMom[this.UNITS];
  }

  /**
   * Lbl PPI.
   *
   * @return the string
   */
  public String lblPPI() {
    String r;
    if (this.UNITS <= 1) {
      r = " ppi";
    } else {
      r = " Kg/cm";
    }
    return r;
  }// end lblPPI

  /**
   * Lbl vol.
   *
   * @return the string
   */
  public String lblVol() {
    return this.lVol[this.UNITS];
  }

  /**
   * Lbl wgt.
   *
   * @return the string
   */
  public String lblWgt() {
    return this.lWgt[this.UNITS];
  }

  /**
   * Vol 2 ton.
   *
   * @return the double
   */
  public double Vol2Ton() {
    double r;
    if (this.UNITS <= 1) {
      r = (this.v2w[this.WATER][this.UNITS] / 2240.0);
    } else {
      r = (this.v2w[this.WATER][this.UNITS] / 1000.0);
    }
    return r;
  }

  /**
   * Vol 2 wgt.
   *
   * @return the double
   */
  public double Vol2Wgt() {
    return this.v2w[this.WATER][this.UNITS];
  }



}
// end bcUnits
