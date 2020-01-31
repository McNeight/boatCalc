/* @formatter:off
 *
 * boatCalc
 * Copyright (C) 2004 Peter H. Vanderwaart
 * Copyright (C) 2020 Neil McNeight
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 *
 * @formatter:on
 */
package io;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PrintStream;

// TODO: Auto-generated Javadoc
/**
 * The Class SaveOutput.
 */
public class SaveOutput extends PrintStream {

  /** The logfile. */
  static OutputStream logfile;

  /** The old stderr. */
  static PrintStream oldStderr;

  /** The old stdout. */
  static PrintStream oldStdout;

  /**
   * Start copying stdout and stderr to the file f.
   *
   * @param f The filename.
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static void start(final String f) throws IOException {
    // Save old settings.
    SaveOutput.oldStdout = System.out;
    SaveOutput.oldStderr = System.err;
    // Create/Open logfile.
    SaveOutput.logfile = new PrintStream(new BufferedOutputStream(new FileOutputStream(f)));
    // Start redirecting the output.
    System.setOut(new SaveOutput(System.out));
    System.setErr(new SaveOutput(System.err));
  }

  /**
   * Stop copying stdout and stderr, and restore the original settings.
   */
  public static void stop() {
    System.setOut(SaveOutput.oldStdout);
    System.setErr(SaveOutput.oldStderr);
    try {
      SaveOutput.logfile.close();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Instantiates a new save output.
   *
   * @param ps the ps
   */
  SaveOutput(final PrintStream ps) {
    super(ps);
  }

  /**
   * PrintStream override. Writes <code>len</code> bytes from the specified byte array starting at
   * offset <code>off</code> to this stream. If automatic flushing is enabled then the
   * <code>flush</code> method will be invoked.
   *
   * <p>
   * Note that the bytes will be written as given; to write characters that will be translated
   * according to the platform's default character encoding, use the <code>print(char)</code> or
   * <code>println(char)</code> methods.
   *
   * @param buf A byte array
   * @param off Offset from which to start taking bytes
   * @param len Number of bytes to write
   */
  @Override
  public void write(final byte buf[], final int off, final int len) {
    try {
      SaveOutput.logfile.write(buf, off, len);
    } catch (final InterruptedIOException x) {
      Thread.currentThread().interrupt();
    } catch (final Exception e) {
      e.printStackTrace();
      this.setError();
    }
    super.write(buf, off, len);
  }

  /**
   * PrintStream override. Prints an integer. The string produced by <code>{@link
   * java.lang.String#valueOf(int)}</code> is translated into bytes according to the platform's
   * default character encoding, and these bytes are written in exactly the manner of the
   * <code>{@link #write(int)}</code> method.
   *
   * @param i The <code>int</code> to be printed
   * @see java.lang.Integer#toString(int)
   */
  @Override
  public void write(final int i) {
    try {
      SaveOutput.logfile.write(i);
    } catch (final Exception e) {
      e.printStackTrace();
      this.setError();
    }
    super.write(i);
  }
}
