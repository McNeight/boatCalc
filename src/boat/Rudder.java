package boat;


public class Rudder implements Cloneable {
  public int dir;
  public rscFoil rudder;
  public rscFoil skeg;
  public boolean valid = false;

  public Rudder() {
    this.rudder = new rscFoil();
    this.skeg = new rscFoil();
    this.dir = 1;
    this.valid = true;
  }

  @Override
  public Object clone() {

    try {
      final Rudder r = (Rudder) super.clone();

      r.rudder = (rscFoil) this.rudder.clone();
      r.skeg = (rscFoil) this.skeg.clone();
      return r;
    } catch (final CloneNotSupportedException e) {
      throw new InternalError(e.toString());
    }

  } // end clone

  public double getArea() {
    double a = 0;
    if (this.rudder.use) {
      a = a + this.rudder.getArea();
    }
    if (this.skeg.use) {
      a = a + this.skeg.getArea();
    }
    return a;
  }

  public double getAreaX() {
    double x = 0;
    if (this.rudder.use) {
      x = x + (this.rudder.getArea() * this.rudder.getAreaX());
    }
    if (this.skeg.use) {
      x = x + (this.skeg.getArea() * this.skeg.getAreaX());
    }
    final double a = this.getArea();
    if (a > 0) {
      x = x / a;
    }
    return x;
  }

  public double getAreaY() {
    double x = 0;
    if (this.rudder.use) {
      x = x + (this.rudder.getArea() * this.rudder.getAreaY());
    }
    if (this.skeg.use) {
      x = x + (this.skeg.getArea() * this.skeg.getAreaY());
    }
    final double a = this.getArea();
    if (a > 0) {
      x = x / a;
    }
    return x;
  }

  public double getMaxX() {
    double x = 0;
    if (this.rudder.use) {
      x = Math.max(x, this.rudder.getMaxX());
    }
    if (this.skeg.use) {
      x = Math.max(x, this.skeg.getMaxX());
    }
    return x;
  }

  public double getMaxY() {
    double x = 0;
    if (this.rudder.use) {
      x = Math.max(x, this.rudder.getMaxY());
    }
    if (this.skeg.use) {
      x = Math.max(x, this.skeg.getMaxY());
    }
    return x;
  }

  public double getMinX() {
    double x = 1000000;
    if (this.rudder.use) {
      x = Math.min(x, this.rudder.getMinX());
    }
    if (this.skeg.use) {
      x = Math.min(x, this.skeg.getMinX());
    }
    return x;
  }

  public double getMinY() {
    double x = 1000000;
    if (this.rudder.use) {
      x = Math.min(x, this.rudder.getMinY());
    }
    if (this.skeg.use) {
      x = Math.min(x, this.skeg.getMinY());
    }
    return x;
  }

  public void setBase(final double b) {
    this.rudder.setBase(b);
    this.skeg.setBase(b);
  }

} // end Rudder
