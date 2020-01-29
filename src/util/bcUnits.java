package util;

public class bcUnits {
  String[] lMom = {" in.-lbs.", "ft.-lbs.", "cc-Kg", "m-Kg"};
  String[] lVol = {" cu. in.", " cu. ft.", " cc", " cu m"};
  String[] lWgt = {" lbs.", " lbs.", " Kg", " Kg"};
  public int UNITS = 0;
  double[][] v2w;
  public int WATER = 0;

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
