package io;

import java.io.BufferedWriter;
import java.io.IOException;

public class Emitter {
  /**
   * Emitter.
   *
   * @param w the w
   * @param s the s
   */
  public void emit(final BufferedWriter w, final String s) {
    try {
      w.write(s, 0, s.length());
    } catch (final IOException e) {
      System.out.println(e);
    }
  }

  /**
   * Emitln.
   *
   * @param w the w
   * @param s the s
   */
  public void emitln(final BufferedWriter w, final String s) {
    this.emit(w, s);
    this.emit(w, System.getProperty("line.separator"));
  }

}
