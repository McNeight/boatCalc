package geom;

class Dir extends Point {
  public Dir() {}

  public Dir(final double x, final double y, final double z) {
    this.setDir(x, y, z);
  }

  public Dir(final Point p) {
    this.setDir(p.x, p.y, p.z);
  }

  @Override
  public double getX() {
    return this.x;
  }

  @Override
  public double getY() {
    return this.y;
  }

  @Override
  public double getZ() {
    return this.z;
  }

  public void setDir(final double x, final double y, final double z) {
    final double l = Math.sqrt((x * x) + (y * y) + (z * z));
    if (l > 0) {
      this.setX(x / l);
      this.setY(y / l);
      this.setZ(z / l);
    } else {
      this.setX(0);
      this.setY(0);
      this.setZ(0);
    }
  }
} // end class Dir
