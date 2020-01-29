package geom;


public class Line extends Object {
  public int length;
  public Point[] points;

  public Line(final int n) {
    this.points = new Point[n];
    this.length = this.points.length;
  }

  public Line(final Point[] p) {
    this.points = p;
    this.length = this.points.length;
  }

  public int countValid() {
    int i, n;
    n = 0;
    for (i = 0; i < this.points.length; i++) {
      if (this.points[i].valid) {
        n++;
      }
    }
    return n;
  }// end countValid

  public Point getPoint(final int i) {
    return this.points[i];
  }

  public void setPoint(final Point p, final int i) {
    this.points[i] = p;
  }

  public boolean valid(final int i) {
    return this.points[i].valid;
  }

  public double valX(final int i) {
    return this.points[i].x;
  }

  public double valY(final int i) {
    return this.points[i].y;
  }

  public double valZ(final int i) {
    return this.points[i].z;
  }
} // end class Line
