package boat;


public class Sail implements Cloneable {

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
