import javax.swing.* ;
import javax.swing.event.* ;
import java.awt.* ;
import java.awt.event.* ;
import java.util.* ;
import java.io.* ;
import java.lang.Math;
import java.text.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
class Hull{
   
   // basic data
   boolean valid;
   boolean bChanged;

   // Constants
   int SAREA = 0;
   int CX = 1;
   int CY = 2;
   int CZ = 3;
   int DISP = 4;
   int WL_LEFT = 5;
   int WL_RIGHT = 6;
   int WETTED = 7;
   int NAREA = 8;

   java.util.List Offsets;
   double[] Stations;
   hLine[] hLines;
   hLine[] sLines;
   boolean bStems[];
   
   String wgtLbl[];
   double wgtWgt[];
   double wgtX[];
   double wgtY[];
   double wgtZ[];
   
   String boatname;
   String designer;
   double base;
   double angHeel = 0.0;
   bcUnits units;
   bcFormat bcf;

   Rig rig;
   Rudder rudder;
   Centerboard board;
   

   // derived data
   double gx_min, gx_max ;
   double gy_min, gy_max ;
   double gz_min, gz_max ;

   // displacement curve data
   int NDIV = 100;
   double dx;
   double lwlLeft, lwlRight;
   double[][] vDisp;
   double[][] vWL;
   double[] vWet;
   double[] hVals;
   Set DispTri;
   
   
   // minimal constructor
   public Hull() {
      bcf = new bcFormat();

      wgtLbl = new String[10];
      wgtWgt = new double[10];
      wgtX = new double[10];
      wgtY = new double[10];
      wgtZ = new double[10];
      units = new bcUnits();
      bStems = new boolean[2];
      bStems[0] = true; 
      bStems[1] = true;
      rig = new Rig();
      rudder = new Rudder();
      board = new Centerboard();
      valid = false;
   } //end constructor
   
   // read native (xml) data file
   public void getData(File hullfile){
   //declarations
   DefaultHandler h = new bcHandler();
   SAXParserFactory f = SAXParserFactory.newInstance();
   try{SAXParser saxParser = f.newSAXParser();
   saxParser.parse(hullfile,h);}
   catch(Throwable t){t.printStackTrace();}
   setLines();
   valid = true;
   } // end getData 

   class bcHandler extends DefaultHandler{
   int iStn;
   int iLine;
   int iWgt;
   
   Point[] p;
   Line l;
   double x, y, z;
   boolean v=true;
   StringBuffer sbWater, sbUnits;
   StringBuffer sbLStem, sbRStem;
   StringBuffer sbX, sbY, sbZ, sbV;
   StringBuffer sbStnX;
   StringBuffer sbBase, sbNA, sbDName, sbLName;
   StringBuffer sbWL, sbWW, sbWX, sbWY, sbWZ;
   StringBuffer sbRigX, sbRigY;
   StringBuffer sbFoilX, sbFoilY;
   String linename;
   int linecount = 0;
   int iSail;
   int iFoil;
   
   Sail curSail;
   rscFoil curFoil;
   
   String[] tags;
   int itag;
   
   
   public void startDocument()
   throws SAXException{

      base = 0.0;
      boatname = "boatCalc";
      designer = "boatCalc";

      tags = new String[10];
      itag = -1;
      
      Offsets = new ArrayList() ;
      bChanged = false;

   }
   public void startElement(String ns, String n, String qn, Attributes a)
      throws SAXException{
      itag++;
      tags[itag] = qn;

      if (qn.compareTo("hull") == 0){
         int m=Integer.parseInt(a.getValue("stations"));
         Stations = new double[m];
         iStn = -1;
         newWgts();
         iWgt = -1;}

      else if (qn.compareTo("rig") == 0){
         rig.dir=Integer.parseInt(a.getValue("dir"));
         rig.valid = Boolean.valueOf(a.getValue("valid")).booleanValue();
      }
      else if (qn.compareTo("sail") == 0){
         String t = a.getValue("type");
         if (t.compareTo("main") == 0) curSail = rig.main;
         else if (t.compareTo("jib") == 0) curSail = rig.jib;
         else curSail = rig.mizzen;
         curSail.setDir(rig.dir);
         curSail.use = Boolean.valueOf(a.getValue("use")).booleanValue();
         curSail.useGaff = Boolean.valueOf(a.getValue("head")).booleanValue();
         curSail.useRoach = Boolean.valueOf(a.getValue("roach")).booleanValue();
         iSail = -1;
      }
      else if (qn.compareTo("rigX") == 0){sbRigX = new StringBuffer();}
      else if (qn.compareTo("rigZ") == 0){sbRigY = new StringBuffer();}

      else if (qn.compareTo("rudder") == 0){
         rudder.valid = Boolean.valueOf(a.getValue("valid")).booleanValue();
      }
      else if (qn.compareTo("foil") == 0){
         String t = a.getValue("type");
         if (t.compareTo("rudder") == 0) curFoil = rudder.rudder;
         else curFoil = rudder.skeg;
         curFoil.use = Boolean.valueOf(a.getValue("use")).booleanValue();
         iFoil = -1;
      }
      else if (qn.compareTo("foilX") == 0){sbFoilX = new StringBuffer();}
      else if (qn.compareTo("foilZ") == 0){sbFoilY = new StringBuffer();}
         
      else if (qn.compareTo("wgtItem") == 0){iWgt++;}
      else if (qn.compareTo("stnX") == 0){iStn++;sbStnX = new StringBuffer();}
      else if (qn.compareTo("water") == 0){sbWater = new StringBuffer();}
      else if (qn.compareTo("lstem") == 0){sbLStem = new StringBuffer();}
      else if (qn.compareTo("rstem") == 0){sbRStem = new StringBuffer();}
      else if (qn.compareTo("units") == 0){sbUnits = new StringBuffer();}
      else if (qn.compareTo("baseline") == 0){sbBase = new StringBuffer();}
      else if (qn.compareTo("designer") == 0){sbNA = new StringBuffer();}
      else if (qn.compareTo("designname") == 0){sbDName = new StringBuffer();}
      else if (qn.compareTo("line") == 0){
         p = new Point[Stations.length];
         for (iLine=0;iLine<p.length;iLine++)p[iLine]=new Point();
         iLine=-1;
         linecount++;
         linename = "Line"+Integer.toString(linecount);}
      else if (qn.compareTo("linename") == 0){sbLName = new StringBuffer();}
      else if (qn.compareTo("point") == 0){iLine++;}
      else if (qn.compareTo("valX") == 0){sbX = new StringBuffer();}
      else if (qn.compareTo("valY") == 0){sbY = new StringBuffer();}
      else if (qn.compareTo("valZ") == 0){sbZ = new StringBuffer();}
      else if (qn.compareTo("ptValid") == 0){sbV = new StringBuffer(); v = true;}

      else if (qn.compareTo("wgtLbl") == 0){sbWL = new StringBuffer();}
      else if (qn.compareTo("wgtWgt") == 0){sbWW = new StringBuffer();}
      else if (qn.compareTo("wgtX") == 0){sbWX = new StringBuffer();}
      else if (qn.compareTo("wgtY") == 0){sbWY = new StringBuffer();}
      else if (qn.compareTo("wgtZ") == 0){sbWZ = new StringBuffer();}
      
  }      
   
   public void characters(char buf[], int offset, int len){
   String s = new String(buf,offset,len);
      if (tags[itag].compareTo("stnX") == 0){sbStnX.append(s);}
      else if (tags[itag].compareTo("baseline") == 0){sbBase.append(s);}
      else if (tags[itag].compareTo("water") == 0){sbWater.append(s);}
      else if (tags[itag].compareTo("lstem") == 0){sbLStem.append(s);}
      else if (tags[itag].compareTo("rstem") == 0){sbRStem.append(s);}
      else if (tags[itag].compareTo("units") == 0){sbUnits.append(s);}
      else if (tags[itag].compareTo("designer") == 0){sbNA.append(s);}
      else if (tags[itag].compareTo("designname") == 0){sbDName.append(s);}
      else if (tags[itag].compareTo("linename") == 0){sbLName.append(s);}

      else if (tags[itag].compareTo("valX") == 0){sbX.append(s);}
      else if (tags[itag].compareTo("valY") == 0){sbY.append(s);}
      else if (tags[itag].compareTo("valZ") == 0){sbZ.append(s);}
      else if (tags[itag].compareTo("ptValid") == 0){sbV.append(s);}
         

      else if (tags[itag].compareTo("wgtLbl") == 0){sbWL.append(s.trim());}
      else if (tags[itag].compareTo("wgtWgt") == 0){sbWW.append(s.trim());}
      else if (tags[itag].compareTo("wgtX") == 0){sbWX.append(s.trim());}
      else if (tags[itag].compareTo("wgtY") == 0){sbWY.append(s.trim());}
      else if (tags[itag].compareTo("wgtZ") == 0){sbWZ.append(s.trim());}

      else if (tags[itag].compareTo("rigX") == 0){sbRigX.append(s);}
      else if (tags[itag].compareTo("rigZ") == 0){sbRigY.append(s);}
      else if (tags[itag].compareTo("foilX") == 0){sbFoilX.append(s);}
      else if (tags[itag].compareTo("foilZ") == 0){sbFoilY.append(s);}

   }
   
   public void endElement(String ns, String n, String qn)
      throws SAXException{
      String ts;
      itag--;
      if (qn.compareTo("valX") == 0){x = Double.parseDouble(sbX.toString());}
      else if (qn.compareTo("valY") == 0){y = Double.parseDouble(sbY.toString());}
      else if (qn.compareTo("valZ") == 0){z = Double.parseDouble(sbZ.toString());}
      else if (qn.compareTo("ptValid") == 0){ts = sbV.toString();
                                             if (ts.compareTo("true") == 0) v = true;
                                             else v = false;}

      else if (qn.compareTo("point") == 0){p[iLine]= new Point(x,y,z,v);}
      else if (qn.compareTo("line") == 0){rawLine rL = new rawLine();
                                          rL.ln = new Line(p);
                                          rL.lnName = linename;
                                          Offsets.add(rL);}
      else if (qn.compareTo("stnX") == 0){Stations[iStn] = Double.parseDouble(sbStnX.toString());}
      else if (qn.compareTo("baseline") == 0){base = Double.parseDouble(sbBase.toString());}
      else if (qn.compareTo("water") == 0){units.WATER = Integer.parseInt(sbWater.toString());}

      else if (qn.compareTo("lstem") == 0){ts = sbLStem.toString();
                                             if (ts.compareTo("true") == 0) bStems[0] = true;
                                             else bStems[0] = false;}
      else if (qn.compareTo("rstem") == 0){ts = sbRStem.toString();
                                             if (ts.compareTo("true") == 0) bStems[1] = true;
                                             else bStems[1] = false;}

      else if (qn.compareTo("units") == 0){units.UNITS = Integer.parseInt(sbUnits.toString());}
      else if (qn.compareTo("designer") == 0){designer = sbNA.toString();}
      else if (qn.compareTo("designname") == 0){boatname = sbDName.toString();}
      else if (qn.compareTo("linename") == 0){linename = sbLName.toString();}
      
      else if (qn.compareTo("wgtLbl") == 0){wgtLbl[iWgt] = sbWL.toString();}
      else if (qn.compareTo("wgtWgt") == 0){wgtWgt[iWgt] = Double.parseDouble(sbWW.toString());}
      else if (qn.compareTo("wgtX") == 0){wgtX[iWgt] = Double.parseDouble(sbWX.toString());}
      else if (qn.compareTo("wgtY") == 0){wgtY[iWgt] = Double.parseDouble(sbWY.toString());}
      else if (qn.compareTo("wgtZ") == 0){wgtZ[iWgt] = Double.parseDouble(sbWZ.toString());}

      else if (qn.compareTo("rigX") == 0){
         iSail++;
         curSail.setParamX(iSail, Double.parseDouble(sbRigX.toString()));}
      else if (qn.compareTo("rigZ") == 0){
         curSail.setParamY(iSail, Double.parseDouble(sbRigY.toString()));}

      else if (qn.compareTo("foilX") == 0){
         iFoil++;
         curFoil.setParamX(iFoil, Double.parseDouble(sbFoilX.toString()));}
      else if (qn.compareTo("foilZ") == 0){
         curFoil.setParamY(iFoil, Double.parseDouble(sbFoilY.toString()));}
      
   }
   public void endDocument()
   throws SAXException{
   }
   
   }// end bcHandler
   
   
   // read "Hulls" data file
   public void getHulls(File hullfile){
 	Offsets = new ArrayList() ;
   BufferedReader r;
   StringTokenizer st;
   String l;
   //initialization
	Stations = new double[13];
   newWgts();
   bChanged = false;

   int n, m;
   int i, j;
   String s;
   double x=0,y,z;
   double z_min = 1000.0, z_max = -1000.0;

   try{
   
   // read first line - name and constants

   String filename = hullfile.getName();
   n = filename.indexOf('.');
   if (n > 0 ) boatname = filename.substring(0,n);
   else boatname = filename;

   // open file for reading
	r = new BufferedReader(new FileReader(hullfile));
   
   // read number of chines
   n =  Integer.parseInt(r.readLine().trim());
   
   // read waste lines.
   m = 15 * n; 
   for (i=1; i <= m ; i++) {l = r.readLine();}
   l = r.readLine();   
   
   // read detailed data into lines.
   Point p[][] = new Point[n][13];
   
   for (i=0; i<13; i++){
      for (j=0; j<n;j++){
      
      y =  Double.parseDouble(r.readLine().trim());
      z =  Double.parseDouble(r.readLine().trim());
      x =  Double.parseDouble(r.readLine().trim());
      if (y < 0.1) y = 0.1;
      z_min = Math.min(z_min,z);
      z_max = Math.max(z_max,z);
      p[j][i] = new Point(x,y,z);
      
      }
      Stations[i] = x;
   }   
   
   for (j=0; j<n;j++){rawLine rL = new rawLine();
                      rL.ln = new Line(p[j]);
                      rL.lnName = "Line "+Integer.toString(j+1);
                      Offsets.add(rL);}
   
   // read waste lines.
   try{
   m = 17; 
   for (i=1; i <= m ; i++) {l = r.readLine();}
   l = r.readLine();   
   designer = l;
   l = r.readLine();   
   boatname = l;
   for (i=1; i <= 3 ; i++) {l = r.readLine();}
    wgtWgt[1] =  Double.parseDouble(r.readLine().trim());
    wgtY[1] =  Double.parseDouble(r.readLine().trim());
    wgtZ[1] =  Double.parseDouble(r.readLine().trim());
   
   }
   catch (NoSuchElementException e){
      base=0.0;
      //boatname="unknown";
      System.out.println("nse");
      System.out.println(e);}
   catch (NullPointerException npe){
      base=0.0;
      //boatname="unknown";
      System.out.println("npe");
      System.out.println(npe);}

   r.close();
   if (z_min>=0.0) base = -(z_min + 0.25 * (z_max - z_min));
   else base=0.0;

   setLines();
   valid = true;  
	}

   catch (NumberFormatException e){System.out.println(e);}
   catch (NoSuchElementException e){System.out.println(e);}
   catch (FileNotFoundException e){System.out.println(e);}
   catch (IOException ioe){System.out.println(ioe);}
   catch (NullPointerException npe){System.out.println(npe);}
   
   return;
   
   }// end get hulls
   
   
   public void saveData(File xmlFile){
   String fn = xmlFile.getName();
   BufferedWriter w;
   String s;
   DecimalFormat DF0d =  new DecimalFormat("#########");
   DecimalFormat DF4d =  new DecimalFormat("#########.0000");

   
   if (fn.indexOf(".xml") < 0 ) {
      fn = fn + ".xml";
      xmlFile = new File(xmlFile.getParent(),fn);
   }
   
   // open file for writing
   try{

   w = new BufferedWriter(new FileWriter(xmlFile));
   emitln(w,"<?xml version='1.0' encoding='UTF-8'?>");
   emitln(w,"<hull stations='"+Stations.length+"' lines='"+Offsets.size()+"'>");

   emitln(w,"   <water>"+bcf.DF0d.format(units.WATER)+"</water>");
   emitln(w,"   <units>"+bcf.DF0d.format(units.UNITS)+"</units>");

   emitln(w,"   <lstem>"+bStems[0]+"</lstem>");
   emitln(w,"   <rstem>"+bStems[1]+"</rstem>");

   emitln(w,"   <designer>"+designer+"</designer>");
   emitln(w,"   <designname>"+boatname+"</designname>");
   emitln(w,"   <baseline> "+bcf.DF4d.format(base)+" </baseline>");

   emitln(w,"   <weights>");
   for (int i=0; i<10;i++){
   emitln(w,"     <wgtItem>");
   emitln(w,"      <wgtLbl>"+wgtLbl[i]+"</wgtLbl>");
   emitln(w,"      <wgtWgt> "+bcf.DF4d.format(wgtWgt[i])+" </wgtWgt>");
   emitln(w,"      <wgtX> "+bcf.DF4d.format(wgtX[i])+" </wgtX>");
   emitln(w,"      <wgtY> "+bcf.DF4d.format(wgtY[i])+" </wgtY>");
   emitln(w,"      <wgtZ> "+bcf.DF4d.format(wgtZ[i])+" </wgtZ>");
   emitln(w,"     </wgtItem>");
   }
   emitln(w,"   </weights>");

   emitln(w,"   <stations>");
   for (int i=0; i<Stations.length;i++){
   emitln(w,"      <stnX> "+bcf.DF4d.format(Stations[i])+" </stnX>");
   }
   emitln(w,"   </stations>");
   
    ListIterator l;
    l = Offsets.listIterator() ;
    while (l.hasNext()){
       rawLine rL = (rawLine) l.next();
       Line ln = rL.ln;
       emitln(w,"   <line>");
       emitln(w,"      <linename>"+rL.lnName+"</linename>");
       for (int i=0; i<Stations.length;i++){
       emitln(w,"      <point>");
       emitln(w,"         <valX>"+bcf.DF4d.format(ln.valX(i))+"</valX>");
       emitln(w,"         <valY>"+bcf.DF4d.format(ln.valY(i))+"</valY>");
       emitln(w,"         <valZ>"+bcf.DF4d.format(ln.valZ(i))+"</valZ>");
       if (ln.valid(i))  emitln(w,"         <ptValid>true</ptValid>");
       else emitln(w,"         <ptValid>false</ptValid>");
       emitln(w,"      </point>");
       }
      emitln(w,"   </line>");
   }   

   if (rig.valid){
   emitln(w,"   <rig dir='"+rig.dir+"' valid='"+rig.valid+"' >");
   
   int is;
   Sail sl;
   for (is=0;is<3;is++){
   if (is == 0) {
      sl = rig.main;
      emitln(w,"   <sail type='main' use='"+sl.use+"' head='"+sl.useGaff+"' roach='"+sl.useRoach+"'>");}
   else if (is == 1) {
      sl = rig.jib;
      emitln(w,"   <sail type='jib' use='"+sl.use+"' head='"+sl.useGaff+"' roach='"+sl.useRoach+"'>");}
   else  {
      sl = rig.mizzen;
      emitln(w,"   <sail type='mizzen' use='"+sl.use+"' head='"+sl.useGaff+"' roach='"+sl.useRoach+"'>");}
      for (int js=0; js<5; js++){
       emitln(w,"         <rigX>"+bcf.DF4d.format(sl.getParamX(js))+"</rigX>");
       emitln(w,"         <rigZ>"+bcf.DF4d.format(sl.getParamY(js))+"</rigZ>");
      }  
   emitln(w,"   </sail>");
   }
   emitln(w,"   </rig>");
   }   
   
   if (rudder.valid){
   emitln(w,"   <rudder valid='"+rudder.valid+"' >");

   rscFoil f;
   for (int i=0;i<=1;i++){
   if (i == 0) {
      f = rudder.rudder;
      emitln(w,"   <foil type='rudder' use='"+f.use+"'>");}
   else {
      f = rudder.skeg;
      emitln(w,"   <foil type='skeg' use='"+f.use+"'>");}
  for (int jf=0; jf<4; jf++){
       emitln(w,"         <foilX>"+bcf.DF4d.format(f.getParamX(jf))+"</foilX>");
       emitln(w,"         <foilZ>"+bcf.DF4d.format(f.getParamY(jf))+"</foilZ>");
      }  
   emitln(w,"   </foil>");
   }
   emitln(w,"   </rudder>");
   }   
   
   
   
   
   
   
   
   
   emitln(w,"</hull>");
   
   w.flush();
   w.close();
   bChanged = false;
   } catch (IOException e){System.out.println(e);}
   
   }// end saveData
   
   private void emit(BufferedWriter w, String s){
   try{w.write(s,0,s.length());} catch (IOException e){System.out.println(e);}}
   
   private void emitln(BufferedWriter w, String s){
   emit(w,s);
   emit(w,System.getProperty("line.separator"));
   }


   public void saveHulls(File hullFile){
   
   String fn = hullFile.getName();
   BufferedWriter w;
   String s;
   DecimalFormat DF4d =  new DecimalFormat("#########.0000");
   double x, y, z;
   int i;
   
   if (fn.indexOf(".hul") < 0 ) {
      fn = fn + ".hul";
      hullFile = new File(hullFile.getParent(),fn);
   }
   
   // open file for writing
   try{

   w = new BufferedWriter(new FileWriter(hullFile));
   int n = Offsets.size();
   // number of chines
   emitln(w,Integer.toString(n));
   // left end
   double xmin = -1000000.0;
   double xmax = +1000000.0;

   // beging first block
   for ( i = 0; i<n;i++){
   x = hLines[i].min("X")-gx_min;
   y = hLines[i].hXY.interp4P(x)-gy_min;
   z = hLines[i].hXZ.interp4P(x)-gz_min;
   emitln(w,bcf.DF4d.format(y));
   emitln(w,bcf.DF4d.format(z));
   emitln(w,bcf.DF4d.format(x));

   // find max, min for spacing of other stations 
   xmin = Math.max(x,xmin);
   x = hLines[i].max("X");
   xmax = Math.min(x,xmax);
   }
   

   for (int j=1;j<=3;j++){
   
   x = xmin + ((double)j) * 0.25 * (xmax - xmin); 

   for ( i = 0; i<n;i++){
   y = hLines[i].hXY.interp4P(x)-gy_min;
   z = hLines[i].hXZ.interp4P(x)-gz_min;
   emitln(w,bcf.DF4d.format(y));
   emitln(w,bcf.DF4d.format(z));
   emitln(w,bcf.DF4d.format(x-gx_min));
   }}

   // right end
   for ( i = 0; i<n;i++){
   x = hLines[i].max("X");
   y = hLines[i].hXY.interp4P(x)-gy_min;
   z = hLines[i].hXZ.interp4P(x)-gz_min;
   emitln(w,bcf.DF4d.format(y));
   emitln(w,bcf.DF4d.format(z));
   emitln(w,bcf.DF4d.format(x-gx_min));
   }
   emitln(w,Integer.toString(-1));

   // begin second block
   for ( i = 0; i<n;i++){
   x = hLines[i].min("X");
   y = hLines[i].hXY.interp4P(x)-gy_min;
   z = hLines[i].hXZ.interp4P(x)-gz_min;
   emitln(w,bcf.DF4d.format(y));
   emitln(w,bcf.DF4d.format(z));
   emitln(w,bcf.DF4d.format(x-gx_min));
   }

   for (int j=1;j<=11;j++){
   x = xmin + ((double)j) * 0.0833333 * (xmax - xmin); 
   //emitln(w,"line "+j+" "+x);

   for ( i = 0; i<n;i++){
   y = hLines[i].hXY.interp4P(x)-gy_min;
   z = hLines[i].hXZ.interp4P(x)-gz_min;
   emitln(w,bcf.DF4d.format(y));
   emitln(w,bcf.DF4d.format(z));
   emitln(w,bcf.DF4d.format(x-gx_min));
   }}

   // right end
   for ( i = 0; i<n;i++){
   x = hLines[i].max("X");
   y = hLines[i].hXY.interp4P(x)-gy_min;
   z = hLines[i].hXZ.interp4P(x)-gz_min;
   emitln(w,bcf.DF4d.format(y));
   emitln(w,bcf.DF4d.format(z));
   emitln(w,bcf.DF4d.format(x-gx_min));
   }
   
   // frames
   for (int j=0;j<8;j++){
   x = xmin + ((double)j) * 0.1429 * (xmax - xmin); 
   emitln(w,bcf.DF4d.format(x-gx_min));}
   
   emitln(w,Integer.toString(0)); //mast length
   emitln(w,Integer.toString(0)); //boom from deck
   emitln(w,Integer.toString(0)); //mast dist aft
   emitln(w,Integer.toString(0)); //luff length
   emitln(w,Integer.toString(0)); //head length
   emitln(w,Integer.toString(0)); //head angle
   emitln(w,Integer.toString(0)); //foot length
   emitln(w,Integer.toString(0)); //deck-stepped
   emitln(w,Integer.toString(-1)); //keel stepped
   emitln(w,designer);             //designer name
   emitln(w,boatname);             //hull info
   emitln(w,"here@there");         //url
   emitln(w,"01-01-2004");         //date
   emitln(w," ");             //unused
   emitln(w,Double.toString(wgtWgt[1])); //ballast weight
   emitln(w,Double.toString(wgtZ[1])); //ballast x
   emitln(w,Double.toString(wgtY[1])); //ballast y
   emitln(w,Integer.toString(0)); //auto smoothing
   
   w.flush();
   w.close();
   bChanged = false;

   } catch (IOException e){System.out.println(e);}

   
   }// end saveHulls
   
   public void setLines(){
    ListIterator l;
    int n = Offsets.size();
    int i = 0;
    
    hLines = new hLine[n];

    l = Offsets.listIterator() ;
    while (l.hasNext()){
       rawLine rL = (rawLine) l.next();
       Line ln = rL.ln;
       hLines[i] = new hLine(ln.points,base);
       if(i==0){
       gx_min = hLines[i].min("X");
       gx_max = hLines[i].max("X");
       gy_min = hLines[i].min("Y");
       gy_max = hLines[i].max("Y");
       gz_min = hLines[i].min("Z");
       gz_max = hLines[i].max("Z");}
       else{
       gx_min = Math.min(gx_min,hLines[i].min("X"));
       gx_max = Math.max(gx_max,hLines[i].max("X"));
       gy_min = Math.min(gy_min,hLines[i].min("Y"));
       gy_max = Math.max(gy_max,hLines[i].max("Y"));
       gz_min = Math.min(gz_min,hLines[i].min("Z"));
       gz_max = Math.max(gz_max,hLines[i].max("Z"));
       }
       i++;
       }
   
    // build stem/stern lines
    ZCompare zComp = new ZCompare();
    SortedSet sLow = new TreeSet(zComp);
    SortedSet sHigh = new TreeSet(zComp);
    Iterator si;
    Line ln;
    Point p;
    int m = Stations.length - 1 ;
    l = Offsets.listIterator() ;
    while (l.hasNext()){
       rawLine rL = (rawLine) l.next();
       ln = rL.ln;
       p = ln.getPoint(0);
       if (p.valid){sLow.add(p);}
       p = ln.getPoint(m);
       if (p.valid){sHigh.add(p);}
    }
    sLines = new hLine[2];

    ln = new Line(sLow.size());
    i = 0;
    si = sLow.iterator();
    while(si.hasNext()){ln.setPoint((Point) si.next(),i);i++;} 
    sLines[0] = new hLine(ln.points,base);

    ln = new Line(sHigh.size());
    i = 0;
    si = sHigh.iterator();
    while(si.hasNext()){ln.setPoint((Point) si.next(),i);i++;} 
    sLines[1] = new hLine(ln.points,base);
   
   } // end setLines   
   
   public Iterator getStation(int i, double ang){
      double tx = Stations[i];
      return getStation(tx,ang); 
   }
   
   
   public Iterator getStation(double tx, double ang){
   SortedSet ts = getStnSet(tx,ang);
   return ts.iterator();
   }
   
   public SortedSet getStnSet(double tx, double ang){
   double sinang = Math.sin(Math.toRadians(ang));
   double cosang = Math.cos(Math.toRadians(ang));
   double ty, tz;
   
   Set s = new HashSet();

   Line ln;
   Point p;
   
   double z_min = +1000000.0;
   double z_max = -1000000.0;
   
   for (int iHL = 0; iHL < hLines.length; iHL++){
        double x_min = hLines[iHL].min("X");
        double x_max = hLines[iHL].max("X");
	     if (x_min <= tx && tx <= x_max){
           double y = hLines[iHL].hXY.interp4P(tx);
           double z = hLines[iHL].hXZ.interp4P(tx);
           z_min = Math.min(z_min,z);
           z_max = Math.max(z_max,z);
           
           //point on first side
           ty = cosang * y - sinang * z;
           tz = sinang * y + cosang * z;
           s.add(new Point(tx,ty,tz));
           //point for opposite side of boat
           ty = cosang * (-y) - sinang * z;
           tz = sinang * (-y) + cosang * z;
           s.add(new Point(tx,ty,tz));
	     }
     }
     
        // left stem
        if (bStems[0]){
        double x_min = sLines[0].min("X");
        double x_max = sLines[0].max("X");

	     if (x_min <= tx && tx <= x_max){
           double y = sLines[0].hXY.leftLinear(tx);
           double z = sLines[0].hXZ.leftLinear(tx);
           //point on first side
           ty = cosang * y - sinang * z;
           tz = sinang * y + cosang * z;
           s.add(new Point(tx,ty,tz));
           //point for opposite side of boat
           ty = cosang * (-y) - sinang * z;
           tz = sinang * (-y) + cosang * z;
           s.add(new Point(tx,ty,tz));
        }}

        // right stem
        if (bStems[1]){
        double x_min = sLines[1].min("X");
        double x_max = sLines[1].max("X");

	     if (x_min <= tx && tx <= x_max){
           double y = sLines[1].hXY.rightLinear(tx);
           double z = sLines[1].hXZ.rightLinear(tx);
           //point on first side
           ty = cosang * y - sinang * z;
           tz = sinang * y + cosang * z;
           s.add(new Point(tx,ty,tz));
           //point for opposite side of boat
           ty = cosang * (-y) - sinang * z;
           tz = sinang * (-y) + cosang * z;
           s.add(new Point(tx,ty,tz));
        }}
   
      YZCompare yzComp2 = new YZCompare();
      ty = - sinang * (z_min + 0.8 *(z_max-z_min));
      tz =   cosang * (z_min + 0.8 *(z_max-z_min));
      yzComp2.setAdj(ty,tz);
     
    Iterator si = s.iterator(); 
    SortedSet ts = new TreeSet(yzComp2);
    while(si.hasNext()){ts.add(si.next());};
    return ts;

   }// end getStation

   
   public void setLWL(){
    ArrayList ax = new ArrayList();
    ArrayList ay = new ArrayList();
    double [] rVals ;
    double x, y;
    
    // find left end
    int i = 0;
    x = Stations[i];
    rVals = getArea(x,angHeel,false);
    y = rVals[SAREA];
    if (y>0){lwlLeft = x;}
    else{
       while (y==0 && i < Stations.length-1){
          i++;
          x = Stations[i];
          rVals = getArea(x,angHeel,false);
          y = rVals[SAREA];
       }
       if (i >= Stations.length-1) lwlLeft = Stations[0];
       else{
          double xl, xr;
          xl = Stations[i-1];
          xr = Stations[i];
          for (int j=0;j<=10;j++){
          x = 0.5 *(xl+xr);
          rVals = getArea(x,angHeel,false);
          y = rVals[SAREA];
          if (y>0)xr = x;
          else xl=x;
          }
        lwlLeft = x;  
       }
    }
    // find right end
    i = Stations.length - 1;
    x = Stations[i];
    rVals = getArea(x,angHeel,false);
    y = rVals[SAREA];
    if (y>0){lwlRight = x;}
    else{
       while (y==0 && i > 0){
          i--;
          x = Stations[i];
          rVals = getArea(x,angHeel,false);
          y = rVals[SAREA];
       }
       if (i <=0) lwlRight = Stations[Stations.length-1];
       else{
          double xl, xr;
          xl = Stations[i];
          xr = Stations[i+1];
          for (int j=0;j<=10;j++){
          x = 0.5 *(xl+xr);
          rVals = getArea(x,angHeel,false);
          y = rVals[SAREA];
          if (y>0)xl = x;
          else xr=x;
          }
        lwlRight = x;  
       }
    }
   }// end setLWL
   
   public void calcDisp(){
     setLWL(); 
     dx = (lwlRight - lwlLeft)/((double)NDIV);

     vDisp = new double[4][NDIV+1];
     vWL = new double[2][NDIV+1];
     vWet = new double[NDIV+1];
     
     hVals = new double[NAREA];
     for (int i=0;i<NAREA;i++){hVals[i]=0;}

     double [] rVals = new double[4];
     for (int i=0; i<=NDIV; i++){
        double x = lwlLeft + dx  * (double) i;
        rVals = getArea(x,angHeel,false);
        vDisp[CX][i] = x;
        vDisp[CY][i] = rVals[CY];
        vDisp[CZ][i] = rVals[CZ];
        vDisp[SAREA][i] = rVals[SAREA];
        vWL[0][i] = rVals[WL_LEFT];
        vWL[1][i] = rVals[WL_RIGHT];
        vWet[i] = rVals[WETTED];
     }

    // accumulate data
    for (int i=0; i<=NDIV; i++){
        hVals[SAREA] = hVals[SAREA]+vDisp[SAREA][i];
        hVals[CX] = hVals[CX]+vDisp[SAREA][i] * vDisp[CX][i];
        hVals[CY] = hVals[CY]+vDisp[SAREA][i] * vDisp[CY][i];
        hVals[CZ] = hVals[CZ]+vDisp[SAREA][i] * vDisp[CZ][i];
        if (i>0) hVals[DISP] = hVals[DISP]+dx * 0.5 * (vDisp[SAREA][i]+vDisp[SAREA][i-1]);
    }    
        if (hVals[SAREA] > 0){
        hVals[CX] = hVals[CX]/hVals[SAREA];
        hVals[CY] = hVals[CY]/hVals[SAREA];
        hVals[CZ] = hVals[CZ]/hVals[SAREA];
     }     
   }// end calc Disp
   
   public double[] getArea(double x, double ang, boolean trisave){
   SortedSet ss = getStnSet(x,ang);
   Iterator si = ss.iterator();
   Point p1, p2;
   double t1y, t1z, t2y, t2z, t3y, t3z ;
   double area, cy, cz ;
   double wlRight = 0;
   double wlLeft = 0;
   double wetted = 0;
   t3y = 0;
   t3z = 0;

   if (trisave) DispTri = new HashSet();
   
   double[] rArea = new double[NAREA];
   for (int i=0;i<NAREA;i++){rArea[i]=0;}
   
   if (ss.size() < 2) return rArea;
   if (!si.hasNext()) return rArea;
   
   p1 = (Point) ss.last();
   
   while(si.hasNext()){
      
     p2 = (Point) si.next();
 
     t1y = p1.y;
     t1z = p1.z;
     t2y = p2.y;
     t2z = p2.z;
     if (t1z > 0 && t2z > 0) {
        t1y = 0; t1z = 0 ;
        t2y = 0; t2z = 0 ; }
     else if (t1z > 0 && t2z <= 0) {
	     t1y = t2y + (t1y - t2y) * (0 - t2z) / (t1z - t2z);
	     t1z = 0;}	
     else if (t1z <= 0 && t2z > 0) {
	     t2y = t1y + (t2y - t1y) * (0 - t1z) / (t2z - t1z);
	     t2z = 0;}	
          
     cy = (t1y + t2y + t3y) /3;
     cz = (t1z + t2z + t3z) /3;
     area = TriArea (t1y,t1z,t2y,t2z,t3y,t3z);

     if(trisave){ 
        double[] tri = {t1y,t1z,t2y,t2z,t3y,t3z};
        DispTri.add(tri);
     }
     
     rArea[CY] = rArea[CY] + area * cy;
     rArea[CZ] = rArea[CZ] + area * cz;
     rArea[SAREA] = rArea[SAREA] + area;
     
     
     if (t1z == 0 && t1y < 0) wlLeft = t1y;
     else if (t1z == 0 && t1y > 0) wlRight = t1y;
     else if (t2z == 0 && t2y < 0) wlLeft = t2y;
     else if (t2z == 0 && t2y > 0) wlRight = t2y;
     
     wetted = wetted + Math.pow(Math.pow(t2y-t1y,2) + Math.pow(t2z-t1z,2),0.5) ;
     
     p1 = p2;
   }
   if (rArea[SAREA] > 0){
      rArea[CY] = rArea[CY] / rArea[SAREA];
      rArea[CZ] = rArea[CZ] / rArea[SAREA];
   }
   
   rArea[WL_LEFT] = wlLeft;
   rArea[WL_RIGHT] = wlRight;
   rArea[WETTED] = wetted;
   
   return rArea;   
   } //end getArea (station=x)

    public final double TriArea(double x1, double y1, double x2, double y2, double x3, double y3){
    double a;
    if (x1 <= x2 && x1 <= x3 )      a = 0.5 * Math.abs((x2 - x1)*(y3 - y1) - (x3-x1) * (y2-y1)) ;  
    else if (x2 <= x1 && x2 <= x3 ) a = 0.5 * Math.abs((x3 - x2)*(y1 - y2) - (x1-x2) * (y3-y2)) ;  
    else                            a = 0.5 * Math.abs((x1 - x3)*(y2 - y3) - (x2-x3) * (y1-y3)) ;  
    return a; 
    }

    public void newWgts(){ 
      wgtLbl = new String[10];
      wgtWgt = new double[10];
      wgtX = new double[10];
      wgtY = new double[10];
      wgtZ = new double[10];
      wgtLbl[0] = "Hull";      
      wgtLbl[1] = "Ballast";      
      wgtLbl[2] = "Engine";      
      wgtLbl[3] = "Accomodation";      
      wgtLbl[4] = "Crew";      
      wgtLbl[5] = "Stores/Gear";      
      wgtLbl[6] = "";      
      wgtLbl[7] = "";      
      wgtLbl[8] = "";      
      wgtLbl[9] = "";      
      
    }

    
   
} // end class Hull  

class Rig implements Cloneable{
 public Sail main;
 public Sail jib;
 public Sail mizzen;
 int dir;
 boolean valid;
 
 public Rig(){
 main = new Sail();
 jib = new Sail();
 mizzen = new Sail();
 dir = 1;
 valid = true;
 }

 public double getMaxX(){
 double x = 0;
 if (main.use) x = Math.max(x,main.getMaxX());
 if (jib.use) x = Math.max(x,jib.getMaxX());
 if (mizzen.use) x = Math.max(x,mizzen.getMaxX());
 return x;} 
 public double getMaxY(){
 double x = 0;
 if (main.use) x = Math.max(x,main.getMaxY());
 if (jib.use) x = Math.max(x,jib.getMaxY());
 if (mizzen.use) x = Math.max(x,mizzen.getMaxY());
 return x;} 
 public double getMinX(){
 double x = 1000000;
 if (main.use) x = Math.min(x,main.getMinX());
 if (jib.use) x = Math.min(x,jib.getMinX());
 if (mizzen.use) x = Math.min(x,mizzen.getMinX());
 return x;} 
 public double getMinY(){
 double x = 1000000;
 if (main.use) x = Math.min(x,main.getMinY());
 if (jib.use) x = Math.min(x,jib.getMinY());
 if (mizzen.use) x = Math.min(x,mizzen.getMinY());
 return x;}

 public double getArea(){
 double a = 0;
 if (main.use) a = a + main.getArea();
 if (jib.use) a = a + jib.getArea();
 if (mizzen.use) a = a + mizzen.getArea();
 return a;} 

 public double getAreaX(){
 double x = 0;
 if (main.use)   x = x + main.getArea()*main.getAreaX();
 if (jib.use)    x = x + jib.getArea()*jib.getAreaX();
 if (mizzen.use) x = x + mizzen.getArea()*mizzen.getAreaX();
 double a = getArea();
 if (a > 0) x = x / a;
 return x;} 

 public double getAreaY(){
 double x = 0;
 if (main.use)   x = x + main.getArea()*main.getAreaY();
 if (jib.use)    x = x + jib.getArea()*jib.getAreaY();
 if (mizzen.use) x = x + mizzen.getArea()*mizzen.getAreaY();
 double a = getArea();
 if (a > 0) x = x / a;
 return x;} 
 
 public Object clone(){
 
 try{
  Rig r = (Rig) super.clone(); 
  //r.main = new Sail();
  //r.jib = new Sail();
  //r.mizzen = new Sail();
  
  r.main = (Sail) main.clone();
  r.jib = (Sail) jib.clone();
  r.mizzen = (Sail) mizzen.clone();
  return r;}
 catch(CloneNotSupportedException e){
 throw new InternalError(e.toString());}
 
 } // end clone
 
} // end class Rig

class Sail implements Cloneable{

private double[][] p;
private double[][] c;
private double area;
private double cX;
private double cY;

private double maxX;
private double maxY;
private double minX;
private double minY;

public boolean use;
private boolean changed;
public boolean useGaff;
public boolean useRoach;

private int dir = 1;

public static int TACK = 0;
public static int LUFF = 1;
public static int BOOM = 2;
public static int GAFF = 3;
public static int ROACH = 4;

public static int THROAT  = 1;
public static int PEAK  = 2;
public static int LEECH = 3;
public static int CLEW  = 4;
 
public Sail(){
p = new double[2][5];
p[0][4] = 10.0;
p[1][4] = 70.0;
use = false;
changed = true;
useGaff = false;
useRoach = false;
}
 
public void setParam(int i, double x, double y){
p[0][i] = x;
p[1][i] = y;
changed = true;
}

public void setParamX(int i, double x){p[0][i] = x;}
public void setParamY(int i, double y){p[1][i] = y;}

public double getParamX(int i){return p[0][i];}
public double getParamY(int i){return p[1][i];}

public void setDir(int i){dir = i;changed=true;}
public void setUse(boolean b){use=b;changed=true;}
public void setUseGaff(boolean b){useGaff=b;changed=true;}
public void setUseRoach(boolean b){useRoach=b;changed=true;}

public void setX(double x){p[0][0] = x; changed=true;}
public void setY(double x){p[1][0] = x; changed=true;}
public void setLuffLen(double x){p[0][1] = x; changed=true;}
public void setLuffAng(double x){p[1][1] = x; changed=true;}
public void setBoomLen(double x){p[0][2] = x; changed=true;}
public void setBoomAng(double x){p[1][2] = x; changed=true;}
public void setGaffLen(double x){p[0][3] = x; changed=true;}
public void setGaffAng(double x){p[1][3] = x; changed=true;}
public void setRoachMax(double x){p[0][4] = x; changed=true;}
public void setRoachPct(double x){p[1][4] = x; changed=true;}

public double getX(int i){if (changed) setSail(); return c[0][i];}
public double getY(int i){if (changed) setSail(); return c[1][i];}
public double getVal(int j, int i){if (changed) setSail(); return c[j][i];}

public double getMaxX(){if (changed) setSail(); return maxX;}
public double getMinX(){if (changed) setSail(); return minX;}
public double getMaxY(){if (changed) setSail(); return maxY;}
public double getMinY(){if (changed) setSail(); return minY;}

public double getArea(){if (changed) setSail(); return area;}
public double getAreaX(){if (changed) setSail(); return cX;}
public double getAreaY(){if (changed) setSail(); return cY;}


public void setSail(){
   double tx, ty;
   double mx, my;
   c = new double[2][5];
   //find corners
   c[0][TACK] = p[0][TACK];
   c[1][TACK] = p[1][TACK];

   c[0][THROAT] = c[0][TACK] + dir * p[0][LUFF] *  Math.sin(Math.toRadians(p[1][LUFF]));
   c[1][THROAT] = c[1][TACK] + p[0][LUFF] *  Math.cos(Math.toRadians(p[1][LUFF]));
   
   c[0][CLEW] = c[0][TACK] + dir * p[0][BOOM] *  Math.cos(Math.toRadians(p[1][BOOM]-p[1][LUFF]));
   c[1][CLEW] = c[1][TACK] + p[0][BOOM] *  Math.sin(Math.toRadians(p[1][BOOM]-p[1][LUFF]));
   
   if (useGaff){
      c[0][PEAK] = c[0][THROAT] + dir * p[0][GAFF] *  Math.cos(Math.toRadians(p[1][GAFF]-p[1][LUFF]));
      c[1][PEAK] = c[1][THROAT] + p[0][GAFF] *  Math.sin(Math.toRadians(p[1][GAFF]-p[1][LUFF]));
      tx = c[0][PEAK];
      ty = c[1][PEAK];
   }
   else{
      tx = c[0][THROAT];
      ty = c[1][THROAT];
   }
   
   if(useRoach){
      double h,v,l, d;
      h = Math.abs(tx - c[0][CLEW]);
      v = Math.abs(ty - c[1][CLEW]);
      l = Math.sqrt(Math.pow(h,2) + Math.pow(v,2)) ;
      d = 0.01 * p[0][ROACH] * l;
      mx = c[0][CLEW] + 0.01 * p[1][ROACH] * (tx - c[0][CLEW]); 
      my = c[1][CLEW] + 0.01 * p[1][ROACH] * (ty - c[1][CLEW]); 
      c[0][LEECH] = mx + dir * d * v/l; 
      c[1][LEECH] = my + d * h/l;
   }
   //extremes
   maxX = Math.max(Math.max(c[0][TACK],c[0][THROAT]),c[0][CLEW]);
   maxY = Math.max(Math.max(c[1][TACK],c[1][THROAT]),c[1][CLEW]);
   minX = Math.min(Math.min(c[0][TACK],c[0][THROAT]),c[0][CLEW]);
   minY = Math.min(Math.min(c[1][TACK],c[1][THROAT]),c[1][CLEW]);
   if (useGaff){
      maxX = Math.max(maxX,c[0][PEAK]);
      maxY = Math.max(maxY,c[1][PEAK]);
      minX = Math.min(minX,c[0][PEAK]);
      minY = Math.min(minY,c[1][PEAK]);
   }
   if (useRoach){
      maxX = Math.max(maxX,c[0][LEECH]);
      maxY = Math.max(maxY,c[1][LEECH]);
      minX = Math.min(minX,c[0][LEECH]);
      minY = Math.min(minY,c[1][LEECH]);
   }
   //area, CoA

   int i = 3;
   mx = c[0][TACK] + c[0][THROAT] + c[0][CLEW] ;
   my = c[1][TACK] + c[1][THROAT] + c[1][CLEW] ;
   if (useGaff){
    mx = mx + c[0][PEAK];
    my = my + c[1][PEAK];
    i++;
   }
   if (useRoach){
    mx = mx + c[0][LEECH];
    my = my + c[1][LEECH];
    i++;
   }
   mx = mx / (double) i;
   my = my / (double) i;
   
   double ta, wx, wy, hx, hy;
   ta = TriArea(mx,my,c[0][CLEW],c[1][CLEW],c[0][TACK],c[1][TACK]);
   area = ta;
   wx = ta * (mx + c[0][CLEW] + c[0][TACK])/3.0;
   wy = ta * (my + c[1][CLEW] + c[1][TACK])/3.0;
   
   ta = TriArea(mx,my,c[0][TACK],c[1][TACK],c[0][THROAT],c[1][THROAT]);
   area = area + ta;
   wx = wx + ta * (mx + c[0][TACK] + c[0][THROAT])/3.0;
   wy = wy + ta * (my + c[1][TACK] + c[1][THROAT])/3.0;
   hx = c[0][THROAT] ;
   hy = c[1][THROAT] ;

   if (useGaff){
      ta = TriArea(mx,my,c[0][THROAT],c[1][THROAT],c[0][PEAK],c[1][PEAK]);
      area = area + ta;
      wx = wx + ta * (mx + c[0][THROAT] + c[0][PEAK])/3.0;
      wy = wy +ta * (my + c[1][THROAT] + c[1][PEAK])/3.0;
      hx = c[0][PEAK] ;
      hy = c[1][PEAK] ;
   }

   if (useRoach){
      ta = TriArea(mx,my,hx,hy,c[0][LEECH],c[1][LEECH]);
      area = area + ta;
      wx = wx + ta * (mx + hx + c[0][LEECH])/3.0;
      wy = wy + ta * (my + hy + c[1][LEECH])/3.0;
      hx = c[0][LEECH] ;
      hy = c[1][LEECH] ;
   }
   
      ta = TriArea(mx,my,hx,hy,c[0][CLEW],c[1][CLEW]);
      area = area + ta;
      wx = wx + ta * (mx + hx + c[0][CLEW])/3.0;
      wy = wy + ta * (my + hy + c[1][CLEW])/3.0;

      if (area > 0){
       cX = wx / area;
       cY = wy / area;}
       else{
       cX = 0.0;
       cY = 0.0; }
      
   changed = false;
}

    public final double TriArea(double x1, double y1, double x2, double y2, double x3, double y3){
    double a;
    if (x1 <= x2 && x1 <= x3 )      a = 0.5 * Math.abs((x2 - x1)*(y3 - y1) - (x3-x1) * (y2-y1)) ;  
    else if (x2 <= x1 && x2 <= x3 ) a = 0.5 * Math.abs((x3 - x2)*(y1 - y2) - (x1-x2) * (y3-y2)) ;  
    else                            a = 0.5 * Math.abs((x1 - x3)*(y2 - y3) - (x2-x3) * (y1-y3)) ;  
    return a; 
    }

 public Object clone(){
 
 try{ Sail s = (Sail) super.clone();
      s.p = new double[2][5];
      s.p[0] = (double[]) p[0].clone();
      s.p[1] = (double[]) p[1].clone();
      //s.p = (double[][]) p.clone(); // doesn't work
      s.changed = true;
      return s;}
 catch(CloneNotSupportedException e){
 throw new InternalError(e.toString());}
 
 } // end clone
    
}// end Sail


class Rudder implements Cloneable{
 public rscFoil rudder;
 public rscFoil skeg;
 int dir;
 boolean valid = false;
 
 public Rudder(){
 rudder = new rscFoil();
 skeg = new rscFoil();
 dir = 1;
 valid = true;
 }
 public void setBase(double b){rudder.setBase(b);skeg.setBase(b);}
 public double getMaxX(){
 double x = 0;
 if (rudder.use) x = Math.max(x,rudder.getMaxX());
 if (skeg.use) x = Math.max(x,skeg.getMaxX());
 return x;} 
 public double getMaxY(){
 double x = 0;
 if (rudder.use) x = Math.max(x,rudder.getMaxY());
 if (skeg.use) x = Math.max(x,skeg.getMaxY());
 return x;} 
 public double getMinX(){
 double x = 1000000;
 if (rudder.use) x = Math.min(x,rudder.getMinX());
 if (skeg.use) x = Math.min(x,skeg.getMinX());
 return x;} 
 public double getMinY(){
 double x = 1000000;
 if (rudder.use) x = Math.min(x,rudder.getMinY());
 if (skeg.use) x = Math.min(x,skeg.getMinY());
 return x;}

 public double getArea(){
 double a = 0;
 if (rudder.use) a = a + rudder.getArea();
 if (skeg.use) a = a + skeg.getArea();
 return a;} 

 public double getAreaX(){
 double x = 0;
 if (rudder.use)   x = x + rudder.getArea()*rudder.getAreaX();
 if (skeg.use)    x = x + skeg.getArea()*skeg.getAreaX();
 double a = getArea();
 if (a > 0) x = x / a;
 return x;} 

 public double getAreaY(){
 double x = 0;
 if (rudder.use)   x = x + rudder.getArea()*rudder.getAreaY();
 if (skeg.use)    x = x + skeg.getArea()*skeg.getAreaY();
 double a = getArea();
 if (a > 0) x = x / a;
 return x;} 
 
 public Object clone(){
 
 try{
  Rudder r = (Rudder) super.clone(); 
  
  r.rudder = (rscFoil) rudder.clone();
  r.skeg = (rscFoil) skeg.clone();
  return r;}
 catch(CloneNotSupportedException e){
 throw new InternalError(e.toString());}
 
 } // end clone
 
} // end Rudder

class Centerboard implements Cloneable{
 public rscFoil board;
 boolean valid = false;
 private Point pivot;
 private double angle;
 private boolean changed;
 private double[][] rCB;
 
 private double[] minHull;
 private double hx_min, hx_max;
 private int maxInc;
 
 public Centerboard(){
 board = new rscFoil();
 board.setCB(true);
 pivot = new Point(0,0,0);
 rCB = new double[2][4];
 angle = 0;
 changed = true;
 valid = true;
 }

 public void setBase(double b){board.setBase(b); changed=true;}
 public double getMaxX(){
 double x = 0;
 if (board.use) x = Math.max(x,board.getMaxX());
 return x;} 
 public double getMaxY(){
 double x = 0;
 if (board.use) x = Math.max(x,board.getMaxY());
 return x;} 
 public double getMinX(){
 double x = 1000000;
 if (board.use) x = Math.min(x,board.getMinX());
 return x;} 
 public double getMinY(){
 double x = 1000000;
 if (board.use) x = Math.min(x,board.getMinY());
 return x;}

 public double getArea(){
 double a = 0;
 if (board.use) a = a + board.getArea();
 return a;} 

 public double getAreaX(){
 double x = 0;
 if (board.use)   x = x + board.getArea()*board.getAreaX();
 double a = getArea();
 if (a > 0) x = x / a;
 return x;} 

 public double getAreaY(){
 double x = 0;
 if (board.use)   x = x + board.getArea()*board.getAreaY();
 double a = getArea();
 if (a > 0) x = x / a;
 return x;} 
 
 public void setPivotX(double x) {pivot.x = x; changed=true;}
 public void setPivotZ(double x) {pivot.z = x; changed=true;}
 public void setPivotAngle(double x) {angle = x; changed=true;}
 public double getPivotX() {return pivot.x;}
 public double getPivotZ() {return pivot.z;}
 public double getPivotAngle() {return angle;}

 public void setRX(int i, double x) {rCB[0][i] = x; changed=true;}
 public void setRZ(int i, double x) {rCB[1][i] = x; changed=true;}
 public double getRX(int i) {if(changed) setCB(); return rCB[0][i];}
 public double getRZ(int i) {if(changed) setCB(); return rCB[1][i];}

 public void setMinHull(double[] mH, double xmin, double xmax, int incs) {
 minHull=mH;
 hx_min = xmin;
 hx_max = xmax;
 maxInc = incs; 
 changed=true;}
  
 public Object clone(){
 
 try{
  Centerboard r = (Centerboard) super.clone(); 
  r.board = (rscFoil) board.clone();
  return r;}
 catch(CloneNotSupportedException e){
 throw new InternalError(e.toString());}
 
 } // end clone
 
private void setCB(){ 
double sinang = Math.sin(Math.toRadians(angle));
double cosang = Math.cos(Math.toRadians(angle));
double xp = pivot.x;
double yp = pivot.z+board.getBase();
for (int i=0; i<4; i++){
    double x = board.getParamX(i);
    double y = board.getParamY(i)+board.getBase();
    rCB[0][i] = xp + cosang * (x-xp) - sinang * (y-yp);
    rCB[1][i] = yp + sinang * (x-xp) + cosang * (y-yp);
}

// computed wetted points
// step one; circulate perimeter including mid-points
// save points below keel or at keel
// note max and min of points at keel

ArrayList wp = new ArrayList();
// set up "prior" point
double ox = rCB[0][3];
double oy = rCB[1][3];
double tt = 100.0*(ox-hx_min)/(hx_max-hx_min);
int oi =  Math.min(Math.max((int) tt,0),99);

for (int i=0;i<4;i++){
   int k = i+1;
   if (k==1) k=0 ;
   for (int j=0;j<=1;j++){
   // calc location   
   double tx = rCB[0][i] + j * 0.5 * (rCB[0][j]-rCB[0][i]);
   double ty = rCB[1][i] + j * 0.5 * (rCB[1][j]-rCB[1][i]);
   // find hull depth
   tt = 100.0*(tx-hx_min)/(hx_max-hx_min);
   int ti = Math.min(Math.max((int) tt,0),99);
   if (tx < minHull[ti]) wp.add(new Point(tx,0,ty));
}
}

// skip adding additional points for now

 double tx = 0;
   double tz = 0;
   for (int i=0; i<wp.size();i++){
      Point p = (Point)wp.get(i);
      tx = tx + p.x;
      tz = tz + p.z;
   }   
   tx = tx / Math.max(wp.size(),1);
   tz = tz / Math.max(wp.size(),1);
   
   XZCompare xzComp = new XZCompare();
   xzComp.setAdj(tx,tz);
   board.setWetPts(xzComp);
   //board.sp = new TreeSet(xzComp);   
   for (int i=0; i<wp.size();i++){board.addWetPt(wp.get(i));} 

changed = false;
}

} // end Centerboard


class rscFoil implements Cloneable{

private double[][] p;
private SortedSet sp;
private double base;

private boolean isCB;
private double ang;

private double area;
private double cX;
private double cY;

private double maxX;
private double maxY;
private double minX;
private double minY;

private double wetArea;
private double wetX;
private double wetY;

public boolean use;
private boolean changed;

private int dir = 1;

public static int TL = 0;
public static int TR = 1;
public static int BR = 2;
public static int BL = 3;
 
public rscFoil(){
p = new double[2][4];
use = false;
changed = true;
}
 
public void setParamXY(int i, double x, double y){
p[0][i] = x;
p[1][i] = y;
changed = true;
}
public void setBase(double b) {base=b;}
public double getBase() {return base;}
public void setParamX(int i, double x){p[0][i] = x; changed=true;}
public void setParamY(int i, double y){p[1][i] = y; changed=true;}

public double getParamX(int i){return p[0][i];}
public double getParamY(int i){return p[1][i];}

public void setDir(int i){dir = i;changed=true;}
public void setUse(boolean b){use=b;changed=true;}

public void setX(double x){p[0][0] = x; changed=true;}
public void setY(double x){p[1][0] = x; changed=true;}

public double getMaxX(){if (changed) setFoil(); return maxX;}
public double getMinX(){if (changed) setFoil(); return minX;}
public double getMaxY(){if (changed) setFoil(); return maxY;}
public double getMinY(){if (changed) setFoil(); return minY;}

public double getArea(){if (changed) setFoil(); return area;}
public double getAreaX(){if (changed) setFoil(); return cX;}
public double getAreaY(){if (changed) setFoil(); return cY;}

public double getWetArea(){if (changed) setFoil(); return wetArea;}
public double getWetX(){if (changed) setFoil(); return wetX;}
public double getWetY(){if (changed) setFoil(); return wetY;}

public SortedSet getWetPts(){if (changed) setFoil(); return sp;}
public void setWetPts(XZCompare xzComp){sp = new TreeSet(xzComp);}
public void addWetPt(Object p) {sp.add(p);}
public boolean isCB(){return isCB;}
public boolean isRS(){return !isCB;}
public void setCB(boolean b){isCB = b;}
public void setAngle(double a) {ang = a;}
public double getAngle() {return ang;}

public void setFoil(){

ArrayList al = new ArrayList();
for (int i=0;i<4;i++)al.add(new Point(p[0][i],0,p[1][i]));
xzArea xzA = new xzArea(al);
area = xzA.getArea();
cX = xzA.getAreaX();
cY = xzA.getAreaZ();

maxX = xzA.getMidX();
minX = xzA.getMidX();
maxY = xzA.getMidZ();
minY = xzA.getMidZ();

ArrayList wp = new ArrayList();

for (int i = 0; i<4;i++){
   maxX = Math.max(maxX,p[0][i]);
   minX = Math.min(minX,p[0][i]);
   maxY = Math.max(maxY,p[1][i]);
   minY = Math.min(minY,p[1][i]);
   // find points below waterline
   if (p[1][i]+base < 0) wp.add(new Point(p[0][i],0,p[1][i]+base));
}

// find line intersections with waterline
double wx, wy;
double tb = -base;

for (int i=0;i<4;i++){
   int j = i + 1;
   if (j==4) j = 0;
   
   if (p[1][i]==tb && p[1][j]==tb){
      wp.add(new Point(p[0][i],0,p[1][i]+base));
      wp.add(new Point(p[0][j],0,p[1][j]+base));}
   else if (p[1][i]<=tb && p[1][j]>= tb){
      double f = (tb - p[1][i])/(p[1][j] - p[1][i]);
      wx = p[0][i] + f * (p[0][j]-p[0][i]);
      wy = p[1][i] + f * (p[1][j]-p[1][i]);
      wp.add(new Point(wx,0,wy+base));}
   else if (p[1][j]<=tb && p[1][i]>= tb){
      double f = (tb - p[1][i])/(p[1][j] - p[1][i]);
      wx = p[0][i] + f * (p[0][j]-p[0][i]);
      wy = p[1][i] + f * (p[1][j]-p[1][i]);
      wp.add(new Point(wx,0,wy+base));}
   }

   double tx = 0;
   double tz = 0;
   for (int i=0; i<wp.size();i++){
      Point p = (Point)wp.get(i);
      tx = tx + p.x;
      tz = tz + p.z;
   }   
   tx = tx / Math.max(wp.size(),1);
   tz = tz / Math.max(wp.size(),1);
   
   XZCompare xzComp = new XZCompare();
   xzComp.setAdj(tx,tz);
   sp = new TreeSet(xzComp);   
   for (int i=0; i<wp.size();i++){sp.add(wp.get(i));} 

   xzArea xzWA = new xzArea(wp);
   wetArea = xzWA.getArea();
   wetX = xzWA.getAreaX();
   wetY = xzWA.getAreaZ();

   
   changed = false;
   
}// end setFoil

    public final double TriArea(double x1, double y1, double x2, double y2, double x3, double y3){
    double a;
    if (x1 <= x2 && x1 <= x3 )      a = 0.5 * Math.abs((x2 - x1)*(y3 - y1) - (x3-x1) * (y2-y1)) ;  
    else if (x2 <= x1 && x2 <= x3 ) a = 0.5 * Math.abs((x3 - x2)*(y1 - y2) - (x1-x2) * (y3-y2)) ;  
    else                            a = 0.5 * Math.abs((x1 - x3)*(y2 - y3) - (x2-x3) * (y1-y3)) ;  
    return a; 
    }
    

 public Object clone(){
 
 try{ rscFoil f = (rscFoil) super.clone();
      f.p = new double[2][4];
      f.p[0] = (double[]) p[0].clone();
      f.p[1] = (double[]) p[1].clone();
      f.changed = true;
      return f;}
 catch(CloneNotSupportedException e){
 throw new InternalError(e.toString());}
 
 } // end clone
    
}// end rscFoil



   class bcUnits {
   double [][] v2w;
   String[] lVol = {" cu. in."," cu. ft."," cc", " cu m"};
   String[] lWgt = {" lbs.", " lbs."," Kg", " Kg"};
   String[] lMom = {" in.-lbs.", "ft.-lbs.","cc-Kg", "m-Kg"};
   int UNITS = 0;
   int WATER = 0;
   
   public bcUnits(){
   
   v2w = new double[2][4];
   v2w[0][0] = 64.0/Math.pow(12.0,3.0);;
   v2w[0][1] = 64.0;
   v2w[0][2] = 0.0010256;
   v2w[0][3] = 1025.6;
   v2w[1][0] = 62.4/Math.pow(12.0,3.0);;
   v2w[1][1] = 62.4;;
   v2w[1][2] = 0.001;
   v2w[1][3] = 1000.0;
   
   }

   public double Vol2Wgt(){return v2w[WATER][UNITS];}
   public double Vol2Ton(){
      double r;
      if (UNITS <= 1) r = (v2w[WATER][UNITS]/2240.0)  ;
      else r = (v2w[WATER][UNITS]/1000.0);
      return r;}
   
   public String lblVol(){return lVol[UNITS];}
   public String lblWgt(){return lWgt[UNITS];}
   public String lblMom(){return lMom[UNITS];}
   
   public double coefPPI() {
   double r;
   if (UNITS == 0)r = 1;
   else if (UNITS == 1) r = 0.083333 ; // =1/12
   else if (UNITS == 2) r = 1;
   else r = 0.01 ; // 1 cm
   return r;
   }// end coefPPI
   
   public String lblPPI(){
   String r;
   if (UNITS <= 1)r = " ppi";
   else r = " Kg/cm";
   return r;
   }// end lblPPI
   
   public double coefArea() {
   double r;
   if (UNITS == 0)r = 1.0 / 144.0; // sq in to sq ft
   else if (UNITS == 1) r = 1.0;
   else if (UNITS == 2) r = 0.0001; // sq cm to sq m
   else r = 1.0 ; 
   return r;
   }// end coefArea
   
   public String lblArea(){
   String r;
   if (UNITS <= 1)r = " sq. ft.";
   else r = " sq. m.";
   return r;
   }// end lblArea
   
   
   
   
   
   }
   // end bcUnits
   


class rawLine{
   Line ln;
   String lnName;}   

class hLine{
   Point[] hPoints;
   Interp hXY;
   Interp hXZ;
   boolean valid;

   //constructor
   public hLine(Point[] p, double b) {
   int i, j = -1;
   int n;
   Point basePoint = new Point(0,0,b);
   valid = false;
   // count valid points
   n = 0;
   for (i=0;i<p.length;i++) if (p[i].valid) n++;
      
   hPoints = new Point[n];
   hXY = new Interp(n);
   hXZ = new Interp(n);
   
   for (i=0;i<p.length;i++){
      if (p[i].valid){
      j++ ;  
      hPoints[j] = p[i].plus(basePoint);
      hXY.setXY(j,p[i].x,hPoints[j].y);
      hXZ.setXY(j,p[i].x,hPoints[j].z);
      valid = true;
   }}

   } //end constructor

   public double max(String D){
     int i;
     double t_max = -1000000.0 ;
     for (i=0;i<hPoints.length;i++){
       if (hPoints[i].valid) {
          if (0 == D.compareTo("X")) t_max = Math.max(t_max,hPoints[i].x);
          if (0 == D.compareTo("Y")) t_max = Math.max(t_max,hPoints[i].y);
          if (0 == D.compareTo("Z")) t_max = Math.max(t_max,hPoints[i].z);
        }
     }
   return t_max;
   } // end max
   
   public double min(String D){
     int i;
     double t_min = +1000000.0 ;
     for (i=0;i<hPoints.length;i++){
       if (hPoints[i].valid) {
          if (0 == D.compareTo("X")) t_min = Math.min(t_min,hPoints[i].x);
          if (0 == D.compareTo("Y")) t_min = Math.min(t_min,hPoints[i].y);
          if (0 == D.compareTo("Z")) t_min = Math.min(t_min,hPoints[i].z);
        }
     }
   return t_min;
   } // end min

   
}  






