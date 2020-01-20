<body style="color: rgb(0,0,0);" link="#0000ee" alink="#0000ee"
 vlink="#551a8b">
  <br>
   
<div style="text-align: center;"><big>boatCalc - Boat Design Calculation Program, Ver 0.2b<br>
  </big>  
<div style="text-align: center;"><big>Instructions/Help/Documentation<br>
  Peter H. Vanderwaart<br>
  </big>  
<div style="text-align: left;"><br>
  <br>
  <big><span style="color: rgb(255,102,102);"><span
 style="font-weight: bold;"><span style="text-decoration: underline;">Cautions</span><br>
  <br>
  1. boatCalc is a program. It can read and write files on your computer's 
disk drives. Running any program that you download from the Internet is an 
act of trust. I can depend on my own efforts, but I cannot keep someone else 
from creating a malicious program with the same name. If you download the 
program from the Yahoo group "boatdesign", be sure that I am listed as the 
person who posted it there. If you wish additional assurance that you have 
the correct program, I will send you, by e-mail, additional information that 
can be used to verify the file. Or, if you wish, I will send you a copy of 
the latest version of the program by e-mail.<br>
  <br>
  2. boatCalc is still under development. The calculations have not been
independently verified, nor have they stood the test of time.&nbsp; In fact,
significant errors are still being discovered and corrected. Do not use the
calculations from this program for anything important without independent
corroboration. In the same spirit, I solicit both examples of errors (all
kinds) and of examples that confirm calculations.<br>
  <br>
  Generally speaking, boatCalc is well-behaved with respect to trapping errors 
without crashing, prompting users about saving changes to data, etc. However, 
some data conditions cause it to crash, and some other niceties may be missing.<br>
  <br>
  3. boatCalc is being developed to be what I want it to be, which may not 
be what you expect it to be. And, it is incomplete.<br>
  <br>
  </span></span><span style="color: rgb(255,102,102);"><span
 style="font-weight: bold;">4. As suggested by the title, this is not a well-crafted 
document. It would be accurate to call it an all-purpose information dump. 
Cautions are in red. <span style="color: rgb(51,102,255);">Areas of future 
development are in blue</span>.</span></span><br>
  </big><span style="color: rgb(255,102,102);"><span
 style="font-weight: bold;"><br>
  <span style="color: rgb(0,0,0);"><big>1. Purpose</big><br>
  <br>
  </span></span><big><span style="color: rgb(0,0,0);">The purpose of boatCalc 
is make yacht design calculations based on the linesplan of a boat hull. It
is programmed to accept hull lines data in the form usually employed by yacht
designers: waterlines, buttocks, and diagonals.&nbsp; It is intended for
use by amateur designers and builders.<br>
  <br>
  This program is not a tool for creating a linesplan.&nbsp; It assumes the 
data entered represent a complete set of faired lines.<br>
  <br>
  Note: It is assumed in this document that the reader is familar with yacht 
design terminology. See References for more information.<br>
  </span></big><span style="font-weight: bold;"><span
 style="color: rgb(0,0,0);"><br>
  <big>2. Permission (license)<br>
  </big><br>
  </span></span><span style="color: rgb(0,0,0);"><big>This program is released 
for public use with the following restrictions:</big><br>
  </span></span>  
<ul>
    <li><big>Copyright is retained by the author.</big><br>
    </li>
    <li><big>The programmer(s) and distributor(s) of the program are released 
from all liability for any damage consequent to the use of boatCalc. This 
includes, but is not limited to, damage done to computers or computer files 
by the program, and damage or harm resulting from the building or use of any
boat for which calculations were done in this program.</big></li>
    <li><big>The user may distribute the program to others, but may not change 
it, nor charge money (or other compensation) for it. Nor may he take credit 
for it or do anything else that infringes the copyright.<br>
      </big></li>
   
</ul>
  <span style="font-weight: bold;"><big>3. System Requirements</big><br>
  <br>
  </span><big>boatCalc is programmed in Java. As a practical matter, it requires 
an up-to-date installation of Sun Java 2 1.4 which is distributed by Sun
Microsystems at no charge. For Windows and Linux operating systems, it should
be obtained from Sun(<a href="http://www.java.com/en/index.jsp">http://www.java.com/en/index.jsp</a>).
 For Apple computers, it should be obtained from Apple(<a
 href="http://www.apple.com/java/">http://www.apple.com/java/</a>). At this 
writing, the program has been run on several flavors of Windows, and several of Linux. 
Partial results have been obtained for the Mac-OS.
<br>
  <br>
  Note: If you have a web browser, you probably already have some flavor
of Java installed. The Sun install of Java on Windows uses a directory with 
a name like "c:\j2sdk1.4.2_02". (The 'sdk' may be 'jre'.) The lead digits 
must be 1.4.&nbsp; If you have the 1.3 version, there is no general reason 
that I know of not to upgrade to the newer version, except that the routine 
install does not erase the old version. (One user reported trouble with a company 
application specific to ver. 1.3.) &nbsp; </big> <span
 style="color: rgb(255,102,102);"><big>However, if you have an installed
version of Java that is not from Sun (e.g. from Microsoft), it is possible
that the Sun installation could cause something that you use to become inoperable. 
You are pretty much on your own with problems of this sort; I really don't 
know much about different Java versions, or the effect of different Java plug-ins.</big><span
 style="color: rgb(0,0,0);"><big> Sun Java is as close to a universal 'gold
standard' as exists in the Java world. If you wish additional information
about determining if you already have Java installed, and what version, e-mail
me.<br>
  <br>
  There is a workaround if you have the 1.3 version of Sun Java. The missing 
link is the SAX parser which is available from the SAX Project web site (<a
 href="http://www.saxproject.org/">http://www.saxproject.org/</a>). You can 
download SAX separately, as described in the FAQ (<a
 href="http://www.saxproject.org/?selected=faq">http://www.saxproject.org/?selected=faq</a>).
 Look for information about the Endorsed Standards Override Mechanism.<br>
  <br>
  The program has run on Windows machines barely into the Pentium class,
but slowly. Much of the development was done on a several year-old, 300mHz PIII,
on which it runs smoothly.<br>
  </big> </span></span><span style="font-weight: bold;"><span
 style="color: rgb(255,102,102);"><span style="color: rgb(0,0,0);"><br>
  <big>4. Obtaining boatCalc<br>
  <br>
  </big></span></span></span><span style="color: rgb(255,102,102);"><span
 style="color: rgb(0,0,0);"><big>boatCalc can be downloaded from the Files 
section of the Yahoo group 'boatdesign', or obtained directly from the author. 
<br>
  <br>
  <span style="text-decoration: underline;">Versioning</span>. The current 
version is 0.2b. A new release with changes and corrections that does not 
increase the feature set will increment the letter: 0.2c, 0.2s, etc. A release 
with a significant new feature (e.g. new main menu option) will increment 
the number and reset the alphabet: 0.3a. When the feature set is complete 
and the program is judged to be stable, a version will be released (with fanfare)
as 1.0. Changes in this document are likely to be erratic.<br>
  </big></span></span><span style="font-weight: bold;"><span
 style="color: rgb(255,102,102);"><span style="color: rgb(0,0,0);"><big><br>
  5. Starting boatCalc<br>
  </big><br>
  </span></span></span><span style="color: rgb(255,102,102);"><span
 style="color: rgb(0,0,0);"><big>The program is obtained as a file named "boatCalc.jar",
which can be put in any convenient directory. The command for starting the
program is<br>
  <br>
  </big> </span></span>  
<div style="margin-left: 40px;"><big><span
 style="color: rgb(255,102,102);"><span style="color: rgb(0,0,0);">java -jar 
boatCalc.jar</span></span></big><br>
  </div>
  <span style="font-weight: bold;"><span
 style="color: rgb(255,102,102);"><span style="color: rgb(0,0,0);"><br>
   
<table border="3">
    <tbody>
      <tr>
        <td>Windows Users: The nominal way to start the program is to open 
a Command Window from the Start/Programs menu, navigate to the appropriate 
directory, and issue the command above. It may be necessary to set a PATH 
to a directory containing java.exe. There are several alternate methods that 
are more convenient for repeated use. Some users have reported that simply 
double-clicking on boatCalc.jar (e.g. in Windows Explorer) will cause the 
program to run because their systems have been configured to recognize the 
.jar extension. (This is similar to having MS Word start when a .doc file 
is double-clicked.) On my own system this did not work since the .jar extension 
has been re-directed to an archiving program.&nbsp; <br>
        <br>
  Another possibility is to create a shortcut that can be left on the desktop 
or put in a menu. Find a copy of java.exe. You may find copies in both a Windows
System directory (name depends on OS, e.g. C:\WINNT\System32) or in a directory
with a name something like C:\j2sdk1.4.2_02\bin. In Windows Explorer, right
click on it and choose 'Create ShortCut'. Find the new shortcut, right-click
on it and choose properties. In the 'target' box, add the balance of the
command after java.exe, e.g.<br>
        <br>
  &nbsp;&nbsp;&nbsp; C:\j2sdk1.4.2_02\bin\java.exe -jar boatCalc.jar<br>
        <br>
  and in the 'start in' box, put the directory where boatCalc.jar is located. 
The shortcut can be renamed, and dragged to the desktop.&nbsp; (These instructions 
may need some modification, depending on the flavor of Windows.)<br><br>

At least one user found that instead of one file with the extension .jar, he 
got many files with the extension .class. The .jar file had been unarchived by 
accident. The program can be run directly using these class files using the 
command(:<br>
        <br>
  &nbsp;&nbsp;&nbsp; java boatCalc<br>
        <br>
  The capitalization of 'boatCalc' must be as shown. Probably, you will need to
  provide path information as described above. If the 
  program does not run, try issuing a CLASSPATH statement as follows:<br>
        <br>
  &nbsp;&nbsp;&nbsp; SET CLASSPATH = .;<br>
        <br>
  

        </td>
      </tr>
      <tr>
        <td>Other Operating Systems: I don't have much knowledge
of how to run the program on these platforms. Once you get it to run, please 
send me a note on how you did it.<br><br>
I ran the program on a laptop using the Linux operating system in the following 
way. I downloaded the Java install from Sun using a computer with a broadband 
connection to the internet, and moved the the install file to the laptop by 
via CD-RW disk. On my first try, I put install file on the Linux desktop and ran
it from there. Unexpectedly (to me), Java was installed in a sub-directory of the 
Desktop named "j2re1.4.2_03". The Java files were installed into j2re1.4.2_03/bin, 
j2re1.4.2_03/lib, etc. I created j2re1.4.2_03/boatCalc as an applications directory,
and moved my program files there. 

To run the program, I opened a terminal program (xterm), changed to the application 
directory<br><br>&nbsp;&nbsp;&nbsp; cd j2re1.4.2_03/boatCalc<br><br>
and ran the program using indirection to java.exe: 
<br><br> 
&nbsp;&nbsp;&nbsp; ../bin/java boatCalc<br><br>or<br><br>&nbsp;&nbsp;&nbsp; ../bin/java -jar boatCalc.jar
<br><br> 
Subsequently, I deleted the Java install, and re-installed in a more sensible place. 
        </td>
      </tr>
       
  </tbody>  
</table>
  </span></span></span><span style="color: rgb(255,102,102);"><span
 style="font-weight: bold;"><br>
  </span></span></div>
  </div>
  </div>
  <big>As the program starts, it creates an 'empty' hull object. Data can be entered via 
  the edit menu, or read from a disk file using the Open option on the file menu. 
  Sample files are available from the same sources as the 
program. These have the extension ".xml". boatCalc will also read files in 
the format of Greg Carlson's Chine Hull Designer (hull.exe). See the section 
below on data files for more information.<br>
  <br>
  boatCalc creates a file named boatcalc.log. Any error messages sent to
the  screen are echoed to this log file. Examples of known error messages
are:<br>
  </big>  
<ul>
    <li><big>Format errors caused by the host computer not having the expected 
fonts.</big></li>
    <li><big>File format errors (probably "null pointer" exceptions) , especially 
with old .hul format files.</big></li>
    <li><big>Call stack dumps triggered by major execution errors, e.g. division 
by zero.</big></li>
   
</ul>
  <big>Log files are overwritten by when the program is run again. (If you get an error 
  indicating that the log file cannot be created, it may be that there is already a file named 
  boatcalc.log which is read-only. Erase it and try again.) If you 
  have an unresolved difficulty, I may ask that you send a copy of the log
file generated when you encounted an error. <span
 style="color: rgb(255,0,0);">If you have difficulty ending the program after 
a crash, try Ctrl-C.</span><br>
  <br>
  <span style="color: rgb(51,102,255);">boatCalc does not use a configuration 
file to save user preferences.</span><br>
  <br>
  6. Main Screen<br>
  <br>
  The main screen displays the lines of a boat or yacht. boatCalc is not
meant to be a primary design tool; it is meant to analyze lines developed
or obtained elsewhere. The purpose of the main screen is to display these
lines for the user to verify visually that they have been correctly entered
and read into the computer, and that they describe the desired shape. For
example, one file (.hul format) resulted in a display with a difficult to
understand line on the body plan. Looking at the data in the Edit Data function,&nbsp;
it was noted that the right end of the the sheer was at 240 inches, but that 
other lines ended at 239.9 inches. These lines were being excluded from the 
end section of the boat. Changing all the section values to 240 inches resulted 
in a clean drawing.<br>
  <br>
  The screen is divided into the following sections:<br>
  <br>
  Top left: a text information block with the name of the design and the
designer. There are radio buttons to select fresh or salt water, and to select
the desired system of units. <span style="color: rgb(255,102,102);"> Caution: 
boatCalc was created and most testing has been done using the units of inches 
and pounds. Calculations in other units have had little testing and are likely 
to have errors.</span> boatCalc does not convert units; the choices are available 
to make the program conform to the units in which the data was entered.<br>
  <br>
  Top right: The body plan is displayed with stations according to the master 
station locations. (see section on data model,)<br>
  <br>
  Center: Lines in plan view.<br>
  <br>
  Bottom: Lines in profile view.<br>
  <br>
  Note: The body plan is not in the same scale as the plan and profile ;
it is drawn as large as possible in the space available. For each drawing,
the horizontal and vertical scales are the same, i.e a square should appear
as a square and not a rectangle. However, monitors differ and the apparent
length/beam may differ from the true one. <span
 style="color: rgb(51,102,255);">There is no allowance or correction for
this type of error.</span><br>
  <big></big></big>  
<p><big><span style="color: rgb(255,0,0);">boatCalc draws all sections with 
straight lines, i.e. as chine hulls. Calculations are made on the same basis. 
For round bottom hulls, a complete set of lines (e.g. 4 waterlines, 4 buttocks, 
4 diagonals) is required to closely approximate curved sections. See examples.</span><br>
  </big></p>
   
<p><big>7. Data Model<br>
  </big></p>
   
<p><big>boatCalc is designed around a "hull object." This is a collection of information for a
 particular design. It may include any or all of the following parts: table of offsets, sailplan, 
 <span style="color: rgb(51,102,255);">rudder/skeg, centerboard, or keel</span>.<br>
  </big></p>
   
<p><big>The way that boatCalc treats the table of offsets is straightforward, and compatible with  
traditional yacht design practice. However, there are some subtleties that 
permit great flexibility. Please read this section carefully.<br>
  </big></p>
   
<p><big><span style="text-decoration: underline;">Stations:</span>&nbsp;
There is a master list of stations locations which are automatically numbered
by the program, beginning with 0 (zero).&nbsp; They must be in ascending
order, and must be distinct (not equal). These define the location of sections
on body plan drawings. The number of stations is a key variable; all hull
lines will be defined by a number of points equal (or, optionally, less than)
to the number of stations.<br>
 </big></p>
 
<p><big>Usual yacht design practice is to number stations with station 0 at
one end of the waterline. Stations on the LWL are always equally spaced. </big><big>Station
outside the LWL (i.e. the overhands) may have irregular spacings, and may
have negative labels, e.g. -1.</big><big>&nbsp;Designers may then refer to
intermediate stations which are not marked on the linesplan. For example,
the spot half way between station 4 and station 5 may be referred to as station
4.5. <br>
  <br>
  boatCalc works a little differently. The lowest numbered station is 0,
never  negative. The numbered stations do have not have to be equally spaced.
Intermediate locations are referred to in linear units (in, ft, m, cm) from
station 0. Thus station 127 may be 127 inches from station 0. In this text,
I refer to this as 'station value.'</big></p>
   
<p><big>Hulls may point in either direction, but station values must increase 
from left to right. Designers sometimes put the "0" station at the bow for 
a right-facing hull. These hulls will face left in boatCalc. The computations 
are unaffected.<br>
  </big></p>
   
<p><big><span style="text-decoration: underline;">Lines</span><span
 style="font-weight: bold;">:</span> Each line is defined by a number of points
equal to the number of stations. Each point is defined by three values: station(fore/aft),
breadth(offset from centerline), and height (vertical distance from baseline).&nbsp;
Optionally, points can be used or not; unused points are ignored. Since each
point has a station value, a point need not fall on a master station.
Lines are assumed to be fair. For station values  between the defining points,
breadth and height values are obtained by polynomial interpolation through
four points, 2 on either side of the desired station value. (For station
values near the end of the hull, at least three points are always used.)<br>
  </big></p>
   
<p><big>Each hull line also has a text name used for identification.<br>
  </big></p>
   
<p><big>In the program and this document, the coordinates are sometimes considered 
as <br>
  </big></p>
   
<ul>
    <li><big>station = x-axis = fore/aft</big></li>
    <li><big>breath = y-axis = left/right</big></li>
    <li><big>height = z-axis = up/down</big></li>
   
</ul>
 <br>
  <big>Therefore, the standard drawings use coordinate systems as follows:<br>
  </big>  
<ul>
    <li><big>plan - (x,y)</big></li>
    <li><big>profile - (x,z)</big></li>
    <li><big>body - (y,z)</big></li>
   
</ul>
  <big style="color: rgb(255,0,0);">The data model takes all lines in station 
value order, i.e. lines that recurve are not allowed. <br>
  </big><br>
  <big><span style="text-decoration: underline;">Auto-Stems:</span>
 Since hull lines do not necessarily define the bow profile and transom,
the program has a feature to create two additional lines automatically by taking
the left (right) endpoints of each hull line. If the end point
of a line is not in use, that line is not included. For this reason, lines
that do not extend to the end of the yacht, such as buttocks,
should have end points that are not in use.
<br><br>
Auto-Stems are optional and are 
selected using radio buttons on the main screen. They are computed by
linear interpolation between the points taken in order of height.  
</big><br><br>
  <big><span style="text-decoration: underline;">Method of Drawing a Section:</span> 
Since hull lines run fore and aft, the data does not define sections explicitly. 
The program constructs sections by examining each hull line. Lines that end 
before or begin after the section are ignored (i.e. lines are not extrapolated 
beyond their end points). For other lines, the interpolated (y,z) values are
computed. The program constructs the section by connecting them in counterclockwise 
order as observed from a point presumed to be in the middle of the hull, beginning
at 12 o'clock. This method works well for most monohulls from sheer to sheer.
<span style="color: rgb(255,0,0);">It will fail for catamarans (though it
will work for a single, symmetrical catamaran hull set on the centerline). 
It may also fail for some faired-in keels, and for complex deck and coaming 
arrangements. When the method fails, it is usually obvious from drawings.</span><br>
  <br>
  <span style="text-decoration: underline;">Keels:</span> Although some keels 
can be included using the standard hull lines, others will be very difficult. 
<span style="color: rgb(51,51,255);">A tool for design of fin keels is planned 
for the future.<br>
  <br>
  <big style="color: rgb(0,0,0);"><small>8. Data Files</small> <small><br>
  <br>
  boatCalc can save the information about a design in a disk file so that
it can be recalled later or moved to a different computer. The file format
is based on XML which is a popular scheme for exchanging data between computer 
programs. XML is a tag-based, markup language similar to HTML. Individual 
data items are defined by tags (text markers of the form "&lt;tag&gt;data&lt;/tag&gt;". 
Tags may be nested (</small></big></span></big><big><span
 style="color: rgb(51,51,255);"><big style="color: rgb(0,0,0);"><small>"&lt;tag1&gt;</small></big></span></big><big><span
 style="color: rgb(51,51,255);"><big style="color: rgb(0,0,0);"><small>&lt;tag2&gt;data&lt;/tag2&gt;</small></big></span></big><big><span
 style="color: rgb(51,51,255);"><big style="color: rgb(0,0,0);"><small>&lt;/tag1&gt;"</small></big></span></big><big><span
 style="color: rgb(51,51,255);"><big style="color: rgb(0,0,0);"><small>)
to any depth. Among the advantages of XML are that these files can be constructed 
and modified directly via a text editor (e.g. Notepad or Wordpad),&nbsp; and
the file format is ignored except for the tags. &nbsp; It seems unnecessary 
to completely define the format of the file here for several reasons. <br>
  </small></big></span></big>  
<ul>
    <li><big><span style="color: rgb(51,51,255);"><big
 style="color: rgb(0,0,0);"><small>The file format is obvious when directly
 examined.</small></big></span></big></li>
    <li><big><span style="color: rgb(51,51,255);"><big
 style="color: rgb(0,0,0);"><small>The continued development of the program 
will lead to the definition of additional tags and other features.</small></big></span></big></li>
    <li><big><span style="color: rgb(51,51,255);"><big
 style="color: rgb(0,0,0);"><small>There is little reason to compile data 
in XML files outside the program.</small></big></span></big></li>
   
</ul>
  <big><span style="color: rgb(51,51,255);"><big
 style="color: rgb(0,0,0);"><small>As currently written, 
boatCalc treats the tags as case-sensitive.<br>
  <br>
  boatCalc expects these files to have the extension ".xml".<br>
  <br>
  The program uses standard file dialog boxes to choose or assign file names 
when files are read or written. The process should be essentially the same 
as for any standard application. <span style="color: rgb(255,0,0);">However, 
this has not been tested extensively for non-Windows platforms. Please report 
any difficulties so that the program can be corrected.</span><br>
  <br>
  Note that numeric data is rounded to four decimal places on output. <span
 style="color: rgb(51,51,255);">(This might be made a user option.)</span>
 Hence data may be changed slightly from the way it was read from a file, 
or input.<br>
  <br>
  <span style="text-decoration: underline;">Hulls Files:</span> Due to the 
popularity of <a href="http://www.carlsondesign.com/#Fun_Shareware">Greg Carlson's
Chine Hull Designer</a> (Hulls or hull.exe), boatCalc has been programmed
to read and write files in the .hul format native to Hulls. Note the following
limitations:<br>
  <br>
  On input, boatCalc uses the 'smoothed', 13-station version of the lines
from the .hul file. boatCalc and Hulls are based on different concepts with
respect to coordinate systems and the Hulls file does not contain a value
that corresponds to the baseline offset in boatCalc. This value will have
to be set in boatCalc. <br>
  <br>
  boatCalc does write files in a format that is readable by Hulls, based
on station locations that are internally calculated and which bear no relation 
to the stations defined in boatCalc data, or to the values in a .hul input 
file. In addition, the vertical height is adjusted so that all values are 
non-negative. Hulls is not designed to handle lines that cross, so shapes 
defined in boatCalc by waterlines and buttocks may be garbled when read into 
Hulls.<br>
  <br>
  Note that, by design, the features of Hulls and boatCalc are complementary 
with minimal overlap. Hulls is optimized for the easy development of a set 
of lines, but is weak on calculation. boatCalc is not a design tool, and is
strong on calculation. There is every reason to move a set of lines from Hulls
to boatCalc, but less reason to go in the other direction. <span
 style="color: rgb(51,51,255);">Improvement in the reading of .hul files will
be done as soon as need is demonstrated and the required change is well-defined. 
Greater sophistication in the output of .hul files is a possibility for future
 development, but with low priority.<br>
 <br>
  </span></small></big></span></big> 
<table cellpadding="2" cellspacing="2" border="2" width="100%">
   <tbody>
     <tr>
       <td valign="top"><span style="color: rgb(51,51,255);"><big
 style="color: rgb(0,0,0);"><small><span style="color: rgb(51,51,255);"><span
 style="color: rgb(0,0,0);">Sample datafiles are made available
along with the program. Edmunds.xml is derived from data taken from an example in 
Designing Power
&amp; Sail by Arthur Edmunds. The fin keel is not modeled. Pipedream.xml is derived 
from data in Skene's Elements of Yacht Design by Francis Kinney. Curiously, the
data for the ends of these boats is not included in the respective tables of offsets; it
was crudely approximated from examination of the lines plan. 
</span></span></small></big></span><br>
       </td>
     </tr>
   
  </tbody> 
</table>
 <big><span style="color: rgb(51,51,255);"><big
 style="color: rgb(0,0,0);"><small><span style="color: rgb(51,51,255);"><br>
  <span style="color: rgb(0,0,0);"></span><br>
  <span style="color: rgb(0,0,0);">9. File Menu<br>
  <br>
  This section describes the options on the File Menu.<br>
  <br>
  <span style="text-decoration: underline;">New:</span> The initial step
in creating a new set of lines. It prompts the user for the number of stations, 
then goes to the Edit function described below. The stations are given arbitrary 
inital values that can be changed by the user. The station values should be
in ascending order, and must be distinct, but need not be equally spaced.<br>
  <br>
  <span style="text-decoration: underline;">Open:</span> Used to read a data 
file from the disk. The file dialog displays files with the extensions .xml 
(boatCalc files) and .hul (Hulls files). <br>
  <br>
  <span style="text-decoration: underline;">Save:</span> Used to save a data 
file to disk. If the selected file name has the extension .xml, the data will
be changed in boatCalc format. If the extension is .hul, it will be in Hulls
format. If neither extension is present, the program will append .xml to
the filename and save the data in boatCalc format.<br>
  <br>
  <span style="text-decoration: underline;">Print:</span> <span
 style="color: rgb(51,51,255);">Not yet implemented</span>. I am accepting 
ideas for the design of printed output.<br>
  <br>
  <span style="text-decoration: underline;">Exit:</span> Used to end program 
execution.<br>
  <br>
  10. Edit Menu<br>
  <br>
  This section describes the options on the Edit Menu.<br>
  <br>
  <span style="text-decoration: underline;">Insert Station &amp; Delete Station</span>: 
Because the number of stations is basic to the data model, the menu options 
to change the number of stations are separate from the standard data edit 
function. <br>
  <br>
  The Insert Station function prompts for input for the new station value.
 The new station will be entered in the list of stations at the appropriate 
point. New entries will be created at the appropriate intervals in the other 
hull lines, also in the appropriate interval, but the points will be invalid 
(i.e. not in use). <br>
  <br>
  The Delete Station function prompts the user for the number of the station 
to delete. This is the sequence number assigned by the program, not the station 
value. The point corresponding to the same station number will be deleted 
from each hull line.<br>
  <br>
  <span style="text-decoration: underline;">Edit Data:</span> Used to modify 
the data of the hull currently being displayed. The Edit Data function displays 
a tabbed window with separate panes for the stations and for each hull line. 
Data for each can be displayed by clicking on the desired tab, and data values 
can be changed directly. There are some controls to prevent bad values from 
being entered, but they are not exhaustive. <span
 style="color: rgb(255,0,0);">Duplicate station values may cause the program 
to crash!</span><br>
  <br>
  Along the bottom of the window are buttons for additional features. <span
 style="text-decoration: underline;">Design Name</span> and <span
 style="text-decoration: underline;">Designer</span> prompt the user for text
information used labels. <span style="text-decoration: underline;">Line Name</span>
permits changes to the name of the currently selected line.&nbsp; <span
 style="text-decoration: underline;">Insert Line</span> creates a new  line
displayed at the place in the sequence of the current line. (The sequence 
does not matter to the calculations.) <span
 style="text-decoration: underline;">Add Line</span> creates a new line at 
the end of the sequence.&nbsp; <span style="text-decoration: underline;">Delete 
Line</span> can be used to remove an unwanted line. <span
 style="text-decoration: underline;">Apply</span> transfers the current (i.e. 
changed) data to the main part of the program and cause it to be displayed 
on the other program windows. <br>
  </span><br>
  
  
  <span style="color: rgb(0,0,0);">11. Design Menu
  <br><br>
  <span style="text-decoration: underline;">Sailplan:</span> This tool is for the design of 
  sailboat rigs. Provision is made for three sails: main, jib, mizzen. Although the sails are 
  labeled for convenience, the processing is identical for each. A checkbox labeled 'use' 
  controls inclusion of each sail in the overall plan.  Calculated sail areas and 
  Center of Area appear in a table.<br><br>
  
  Basic triangular sails are 
  defined by the tack location, luff length, mast rake (from vertical), foot length, and foot angle. 
  The nominal (zero degree) angle for the foot is perpendicular to the mast. (For example, if the mast is raked at 
  3 degrees, and the foot angle is zero, the foot will be 3 degrees below horizontal.)
  A radio button controls whether the boom is to the right or left of the 
  mast. 
  <br><br>
  For a 4-sided sail, check the box marked 'head' and enter the head length and angle. Head angles 
  are defined in the same way as foot angles, i.e. in degress above perpendicular from the mast. These angles 
  are defined in this way so that changing the rake of the mast does not change the shape of the sail.
  <br><br>
  To include roach on the leech, check the box marked 'roach' and enter the amount of 
  roach (Max %) and the desired location along the leech. Note that these values are taken as percent 
  of the leech length. Typical values are 10 for Max%, and 65-75 for height.<br><br>
  
  Controls on the bottom of the page permit dimensions to be increased or decreased easily. 
  Use the appropriate radio button to determine the value to be changed. 

(Label key: X,Z = coordinates, L=length, A=Angle, M=mid-point)

The step size can be selected from a drop-down, or the the desired number may be keyed. Clicking the increment (decrement) button causes the appropriate value to 
  be increased (decreased) by the step amount. The 'scale' option is an exception. This causes the 
  luff, boom, and gaff dimensions to changed by the step interpreted as a percentage, thus 
  preserving sail shape.
  <br><br>
  Clicking the button marked 'Apply' will cause the design in the tool to be transferred to 
  the underlying hull. (It will not be saved to disk until the hull is saved.) Clicking 'Close' 
  will discard the changes and the hull will have the pre-existing sailplan data.
  <br><br>
  <span style="text-decoration: underline;">Rudder/Skeg:</span> The Rudder/Skeg tool is similar to 
the sailplan tool. It is used to define the shapes and areas of the rudder and/or skeg. The two foils 
are labeled for convenience, but the calculations are identical (in this tool).
<br><br>
Each foil is defined as a quadrialteral by four corner points labeled upper left, upper right, etc. 
The program does not enforce this orientation, nor rely on it for calculations. The user must be 
careful that the "permeter" does not assume an hourglass shape.
  <br><br>
  Controls on the bottom of the page permit the corner points to be moved easily. 
  Use the appropriate radio button to determine the value to be changed. 
  The step size can be selected from a drop-down, or the the desired number may be keyed. Clicking the increment (decrement) button causes the appropriate value to 
  be increased (decreased) by the step amount. The 'scale' option is an exception. This causes the 
foil sides to be changed by the appropriate perent, preserving foil shape, 
and keeping the center of the foil in the same place.
  <br><br>
  Clicking the button marked 'Apply' will cause the design in the tool to be transferred to 
  the underlying hull. (It will not be saved to disk until the hull is saved.) Clicking 'Close' 
  will discard the changes and the hull will have the pre-existing sailplan data.
  <br><br>
  </span>

  <span style="color: rgb(0,0,0);">12. Analysis Menu<br>
  <br>
  <span style="text-decoration: underline;">Displacement:</span> Calculations 
of volume, displacement, etc. <span style="color: rgb(255,0,0);">See the notes
on calculation below. Errors in this section are especially likely for choices
of units other than inches &amp; pounds.</span><br>
  <br>
  In the upper left are two panes showing the body plan for the left and
right ends of the hull. (They are not marked fore &amp; aft because the hull
lines could show the vessel going in either direction). <br>
  <br>
  The pane in the upper right has several controls, described below.<br>
  <br>
  At the bottom of the window is a tabbed pane display. These computation
panes display data as described below.<br>
  <br>
  The controls are as follows:<br>
  </span></span></small></big></span></big>  
<ul>
    <li><big><span style="color: rgb(51,51,255);"><big
 style="color: rgb(0,0,0);"><small><span style="color: rgb(51,51,255);"><span
 style="color: rgb(0,0,0);">Baseline Offset Slider - sets the vertical offset 
betweent the waterline and the "zero" point of reference for the hull lines. 
Moving the slider left and right effectively moves the hull up and down. The
slider can be moved by mouse. Once the slider has been selected by clicking, 
it can be moved using the left/right arrow keys. A single step is either
1/4" for English units or 1cm for metric units. It is easier to get fine
control with the arrow keys than with the mouse.</span></span></small></big></span></big></li>
    <li><big><span style="color: rgb(51,51,255);"><big
 style="color: rgb(0,0,0);"><small><span style="color: rgb(51,51,255);"><span
 style="color: rgb(0,0,0);">Angle of Heel Slider - set the angle of heel between
zero and ninety degrees. As programmed, calculations are only valid until
the sheer reaches the waterline. Computation with the sheer below the waterline
are unreliable. <font color="#3333ff">This may be improved in the future.</font>
The finest degree of control is 1 degree. <span
 style="color: rgb(204,0,0);">The displacement is not adjusted when the angle 
of heel is changed. A change in angle of heel will cause a change in displacement. 
To adjust for the desired displacement, use the Baseline Offset slider, noting
 displacement calculation on the displacment pane.</span><br>
      </span></span></small></big></span></big></li>
    <li><big><span style="color: rgb(51,51,255);"><big
 style="color: rgb(0,0,0);"><small><span style="color: rgb(51,51,255);"><span
 style="color: rgb(0,0,0);">"Set Compare" button - used to capture data for 
a current setting for comparison to alternates. The purpose is to see how 
the Curve of Areas (for example) changes with change in displacement or heel.<br>
      </span></span></small></big></span></big></li>
    <li><big><span style="color: rgb(51,51,255);"><big
 style="color: rgb(0,0,0);"><small><span style="color: rgb(51,51,255);"><span
 style="color: rgb(0,0,0);">"Set Weights" button - used to enter the weights 
and CoG's of the hull, ballast, rig, crew, etc for stability calculation. 
See the section on Weights Computation<br>
      </span></span></small></big></span></big></li>
   
</ul>
  <big><span style="color: rgb(51,51,255);"><big
 style="color: rgb(0,0,0);"><small><span style="color: rgb(51,51,255);"><span
 style="color: rgb(0,0,0);"><span style="color: rgb(51,102,255);">It is possible 
that future development will include a control to change pitch as well as 
roll (heel). Although the coordinate computations are easy enough, there are
sticky programming issues caused, among other things, by the change in  station
locations. However, it would be very nice to be able to rotate a lines plan
drawn square to the keel rather than square to the waterline.</span><br
 style="color: rgb(51,102,255);">
  <br>
  The computation panes are as follows:<br>
  </span></span></small></big></span></big>  
<ul>
    <li><big><span style="color: rgb(51,51,255);"><big
 style="color: rgb(0,0,0);"><small><span style="color: rgb(51,51,255);"><span
 style="color: rgb(0,0,0);">Displacement - shows the curve of areas and related 
computations such as displacement, location of the center of buoyancy, prismatic
 coeffecient. If "Set Compare" is on, the current line is captured and displayed 
for comparision to other baseline offset and heel settings.</span></span></small></big></span></big></li>
    <li><big><span style="color: rgb(51,51,255);"><big
 style="color: rgb(0,0,0);"><small><span style="color: rgb(51,51,255);"><span
 style="color: rgb(0,0,0);">Waterplane - shows the shape of the waterplane 
and related computations such as waterplane area, center of area, immersion, 
waterplane coeffectient, etc. Observes the "Set Capture" button.</span></span></small></big></span></big></li>
    <li><big><span style="color: rgb(51,51,255);"><big
 style="color: rgb(0,0,0);"><small><span style="color: rgb(51,51,255);"><span
 style="color: rgb(0,0,0);">Wetted Surface - shows the distribribution of 
wetted surface by station, and related values. Oberves "Set Compare" button.</span></span></small></big></span></big></li>
    <li><big><span style="color: rgb(51,51,255);"><big
 style="color: rgb(0,0,0);"><small><span style="color: rgb(51,51,255);"><span
 style="color: rgb(0,0,0);">Weights - displays a tabluar computation of the 
location of various weights and computed moments. See the section on Weights 
Computation.</span></span></small></big></span></big></li>
    <li><big><span style="color: rgb(51,51,255);"><big
 style="color: rgb(0,0,0);"><small><span style="color: rgb(51,51,255);"><span
 style="color: rgb(0,0,0);">Station Area - displays the section area calculation 
at any desired station graphically. The station can be selected using the 
slider which works in increments of 1% of the waterline (as computed for the
current baseline offset and angle of heel).&nbsp; The section area calculation
 is done using the triangles shown in blue and can be relied on to be the 
same as used for the displacement calculations. Thus, this pane is a visual 
check that the calculation is being done as intended, and that the lines correctly
represent the desired hull shape. <span style="color: rgb(255,0,0);">(They
also indicate the problems that arise when the angle of heel puts the sheer
under water.)</span></span></span></small></big></span></big></li>
   
</ul>
  <big><span style="color: rgb(51,51,255);"><big
 style="color: rgb(0,0,0);"><small><span style="color: rgb(51,51,255);"><span
 style="color: rgb(0,0,0);"><span style="text-decoration: underline;">Notes 
on Displacement Calculation.</span>&nbsp; The left and right ends of the waterline
are determined for the current baseline offset and angle of heel. The resulting
interval is divided into 100 sections. The volume of each section is computed
along with the sums and other statistics displayed. Calculation of section
area is done by dividing the section into triangles as illustrated on the
Section Area pane. The program treats all hulls as chine hulls. The  prismatic
coeffecient is computed using the largest section area. The center of buoyancy
is shown as a red circle on one of the body plan panels (depend on the station
value) in upper left of the window.<br>
  <br>
  <span style="text-decoration: underline;">Notes on Weight Calculation.</span>
 This is a comparison of two views of the weight and flotation of the hull. 
The first is the displacement of the hull and the associated center of buoyancy 
from the displacement calculation. The second computation yields the weight 
and center of gravity of the hull and 
equipment as computed <u>outside the program</u> based on the construction 
plan. The data entry screen is activated by the "Set Weights" button. Up to
ten items can be entered with weight and the location (in 3 dimensions) of
the center of gravity. Note the vertical dimension is on the same coordiante 
system as the hull lines (i.e. before addition of baseline offset). The first 
six items have been given suggested labels. These can be changed if desired. 
Each item entered will be represented by a blue square on one of the body 
plan panels (depending on station value)&nbsp; in the upper left of the window. 
The combined center of gravity will be shown as a blue circle. This will be
shown on the same body panel as the center of buoyancy even if the station 
value would put it on the other panel. The horizontal and vertical differences 
between the center of buoyancy and center of gravity are shown by straight 
lines. <br>
  <br>
  The same information appears in tabular format on the weights pane. Note 
that the heeling moment is the product of the weight determined by displacement 
(which may be different from the weight entered) and the horizontal difference 
(righting arm) in the locations of the centers.<br>
  <br>
  Various sorts of experimentation are possible. For example, to determine 
the amount of heel that would result from a crewperson sitting at the sheer, 
put in the crew weight at the desired height and offset from the centerline. 
The body plan is a useful guide. Then the sliders can be adjusted until the 
displacment is correct and the heeling moment is zero. <br>
  <br style="color: rgb(0,0,0);">
  </span><font color="#000000">13. References<br>
 <br>
 Links: <br>
 <br>
 <a href="http://www.tedbrewer.com/yachtdesign.html">Understanding Yacht
Design</a>, by <a href="http://www.tedbrewer.com/index.html">Ted Brewer</a>.<br>
 <br>
 Books:<br>
 <br>
Edmunds, Arthur, <u>Designing Power &amp; Sail</u>, Bristol Fashion Publications,
Enola, PA, 1998<br>
 Kinney, Francis S., <u>Skene's Elements of Yacht Design</u>, Dodd, Mead &amp; 
Company, New York, NY, 1981</font><br>
  <font color="#000000">Teale, John, <u>How To Design a Boat</u>, Sheridan 
House, Dobbs Ferry, NY, 1992</font><br>
  </span></small></big></span><br>
  </big> <br>
 <br>
</body>