package util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class bcFormat {
  public DecimalFormat DF0d;
  public DecimalFormat DF1d;
  public DecimalFormat DF2d;
  public DecimalFormat DF3d;
  public DecimalFormat DF4d;

  public bcFormat() {
    final Locale l = new Locale("en", "US");
    // Locale l = new Locale("fr","FR");
    final DecimalFormatSymbols dfs = new DecimalFormatSymbols(l);
    this.DF0d = new DecimalFormat("###,###,###", dfs);
    this.DF1d = new DecimalFormat("###,###,###.0", dfs);
    this.DF2d = new DecimalFormat("###,###,###.00", dfs);
    this.DF3d = new DecimalFormat("###,###,###.000", dfs);
    this.DF4d = new DecimalFormat("###,###,###.0000", dfs);
  }// end constructor
}// enc bcFormat
