package geom;

public class hLine {
  public Point[] hPoints;
  public Interp hXY;
  public Interp hXZ;
  public boolean valid;

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
