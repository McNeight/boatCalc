package boat;


public class Rig implements Cloneable {
  public int dir;
  public Sail jib;
  public Sail main;
  public Sail mizzen;
  public boolean valid;

  public Rig() {
    this.main = new Sail();
    this.jib = new Sail();
    this.mizzen = new Sail();
    this.dir = 1;
    this.valid = true;
  }

  @Override
  public Object clone() {

    try {
      final Rig r = (Rig) super.clone();
      // r.main = new Sail();
      // r.jib = new Sail();
      // r.mizzen = new Sail();

      r.main = (Sail) this.main.clone();
      r.jib = (Sail) this.jib.clone();
      r.mizzen = (Sail) this.mizzen.clone();
      return r;
    } catch (final CloneNotSupportedException e) {
      throw new InternalError(e.toString());
    }

  } // end clone

  public double getArea() {
    double a = 0;
    if (this.main.use) {
      a = a + this.main.getArea();
    }
    if (this.jib.use) {
      a = a + this.jib.getArea();
    }
    if (this.mizzen.use) {
      a = a + this.mizzen.getArea();
    }
    return a;
  }

  public double getAreaX() {
    double x = 0;
    if (this.main.use) {
      x = x + (this.main.getArea() * this.main.getAreaX());
    }
    if (this.jib.use) {
      x = x + (this.jib.getArea() * this.jib.getAreaX());
    }
    if (this.mizzen.use) {
      x = x + (this.mizzen.getArea() * this.mizzen.getAreaX());
    }
    final double a = this.getArea();
    if (a > 0) {
      x = x / a;
    }
    return x;
  }

  public double getAreaY() {
    double x = 0;
    if (this.main.use) {
      x = x + (this.main.getArea() * this.main.getAreaY());
    }
    if (this.jib.use) {
      x = x + (this.jib.getArea() * this.jib.getAreaY());
    }
    if (this.mizzen.use) {
      x = x + (this.mizzen.getArea() * this.mizzen.getAreaY());
    }
    final double a = this.getArea();
    if (a > 0) {
      x = x / a;
    }
    return x;
  }

  public double getMaxX() {
    double x = 0;
    if (this.main.use) {
      x = Math.max(x, this.main.getMaxX());
    }
    if (this.jib.use) {
      x = Math.max(x, this.jib.getMaxX());
    }
    if (this.mizzen.use) {
      x = Math.max(x, this.mizzen.getMaxX());
    }
    return x;
  }

  public double getMaxY() {
    double x = 0;
    if (this.main.use) {
      x = Math.max(x, this.main.getMaxY());
    }
    if (this.jib.use) {
      x = Math.max(x, this.jib.getMaxY());
    }
    if (this.mizzen.use) {
      x = Math.max(x, this.mizzen.getMaxY());
    }
    return x;
  }

  public double getMinX() {
    double x = 1000000;
    if (this.main.use) {
      x = Math.min(x, this.main.getMinX());
    }
    if (this.jib.use) {
      x = Math.min(x, this.jib.getMinX());
    }
    if (this.mizzen.use) {
      x = Math.min(x, this.mizzen.getMinX());
    }
    return x;
  }

  public double getMinY() {
    double x = 1000000;
    if (this.main.use) {
      x = Math.min(x, this.main.getMinY());
    }
    if (this.jib.use) {
      x = Math.min(x, this.jib.getMinY());
    }
    if (this.mizzen.use) {
      x = Math.min(x, this.mizzen.getMinY());
    }
    return x;
  }

} // end class Rig
