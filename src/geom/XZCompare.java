package geom;

import java.util.Comparator;

public class XZCompare implements Comparator {
  double xAdj = 0;
  double zAdj = 0;

  @Override
  public int compare(final Object o1, final Object o2) {
    final Point p1 = (Point) o1;
    final Point p2 = (Point) o2;
    return this.sgn(p1.angleXZ(this.xAdj, this.zAdj) - p2.angleXZ(this.xAdj, this.zAdj));
  }

  @Override
  public boolean equals(final Object o) {
    return o.equals(this);
  }

  public void setAdj(final double x, final double z) {
    this.xAdj = x;
    this.zAdj = z;
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
} // end class XZCompare
