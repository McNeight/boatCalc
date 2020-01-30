package geom;

import java.util.Comparator;

class XCompare implements Comparator<Point> {
  @Override
  public int compare(final Point p1, final Point p2) {
    return this.sgn(p1.getX() - p2.getX());
  }

  @Override
  public boolean equals(final Object o) {
    return o.equals(this);
  }

  private int sgn(final double a) {
    if (a > 0) {
      return 1;
    } else if (a < 0) {
      return -1;
    } else {
      return 0;
    }
  }
} // end class XCompare
