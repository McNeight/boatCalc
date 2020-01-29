package geom;

public class Point extends Object {
  public boolean valid;
  public double x, y, z;

  public Point() {
    this.valid = true;
  }

  public Point(final boolean b) {
    this.setX(0);
    this.setY(0);
    this.setZ(0);
    this.valid = b;
  }

  public Point(final double x, final double y, final double z) {
    this.setX(x);
    this.setY(y);
    this.setZ(z);
    this.valid = true;
  }

  public Point(final double x, final double y, final double z, final boolean v) {
    this.setX(x);
    this.setY(y);
    this.setZ(z);
    this.valid = v;
  }

  public Point(final Point p) {
    this.setX(p.x);
    this.setY(p.y);
    this.setZ(p.z);
    this.valid = true;
  }

  public double angleXZ() {
    return Math.atan2(this.x, -this.z);
  }

  public double angleXZ(final double xAdj, final double zAdj) {
    return Math.atan2(this.x - xAdj, zAdj - this.z);
  }

  public double angleYZ() {
    return Math.atan2(this.y, -this.z);
  }

  public double angleYZ(final double yAdj, final double zAdj) {
    return Math.atan2(this.y - yAdj, zAdj - this.z);
  }

  public boolean equals(final Point p) {
    return ((p.x == this.x) && (p.y == this.y) && (p.z == this.z) && (p.valid == this.valid));
  }

  public double getX() {
    return this.x;
  }

  public double getY() {
    return this.y;
  }

  public double getZ() {
    return this.z;
  }

  public Point minus(final Point p) {
    return this.plus(-1, p);
  }

  public Point plus(final double a, final Point p) {
    final Point r = new Point();
    r.x = this.x + (a * p.x);
    r.y = this.y + (a * p.y);
    r.z = this.z + (a * p.z);
    return r;
  }

  public Point plus(final Point p) {
    final Point r = new Point();
    r.x = this.x + p.x;
    r.y = this.y + p.y;
    r.z = this.z + p.z;
    return r;
  }

  public void setX(final double x) {
    this.x = x;
  }

  public void setY(final double y) {
    this.y = y;
  }

  public void setZ(final double z) {
    this.z = z;
  }
} // end class Point
