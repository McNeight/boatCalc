/* Geometric Utilities */

import javax.swing.* ;
import javax.swing.event.* ;
import java.awt.* ;
import java.awt.event.* ;
import java.util.* ;
import java.io.* ;
import java.lang.Math;
import java.text.*;

    class Point extends Object {
    public double x, y, z ;
    public boolean valid;
    public Point (){valid=true;}
    public Point (double x, double y, double z){
    setX(x);
    setY(y);
    setZ(z);
    valid = true ;
    }
    public Point (double x, double y, double z, boolean v){
    setX(x);
    setY(y);
    setZ(z);
    valid = v ;
    }
    public Point (Point p){
    setX(p.x);
    setY(p.y);
    setZ(p.z);
    valid = true ;	    
    }
    public Point (boolean b){
    setX(0);
    setY(0);
    setZ(0);
    valid = b ;
    }
    public boolean equals(Point p) {return (p.x==this.x && p.y == this.y && p.z==this.z && p.valid == this.valid);}
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setZ(double z) { this.z = z; }
    public double getX() {return x;}
    public double getY() {return y;}
    public double getZ() {return z;}
    public Point plus(Point p) {
        Point r = new Point() ;
        r.x = x + p.x ;
        r.y = y + p.y ;
        r.z = z + p.z ;
        return r ;
    }
    public Point plus(double a, Point p) {
        Point r = new Point() ;
        r.x = x + a * p.x ;
        r.y = y + a * p.y ;
        r.z = z + a * p.z ;
        return r ;
    }
    public Point minus(Point p) { return plus(-1,p) ; }
    public double angleYZ() {return Math.atan2(y,-z);}
    public double angleYZ(double yAdj, double zAdj) {return Math.atan2(y-yAdj,zAdj-z);}
    public double angleXZ() {return Math.atan2(x,-z);}
    public double angleXZ(double xAdj, double zAdj) {return Math.atan2(x-xAdj,zAdj-z);}
    } //end class Point
    
    class Dir extends Point {
    public Dir(){} 
    public Dir (double x, double y, double z){setDir(x,y,z);}
    public Dir(Point p){setDir(p.x,p.y,p.z);}
    public void setDir(double x, double y, double z) {         
       double l = Math.sqrt(x*x + y*y + z*z) ;
       if (l > 0) {
        setX(x/l);
        setY(y/l);
        setZ(z/l); }
       else {
        setX(0);
        setY(0);
        setZ(0); }
        }
    public double getX() {return x;}
    public double getY() {return y;}
    public double getZ() {return z;}
    } // end class Dir

   class Line extends Object{
   public Point [] points ;
   public int length ;
   public Line ( Point[] p){
     points = p ;
     length = points.length ;
   }
   public Line(int n){
   points = new Point[n];
   length = points.length ;
   }
   
   public double valX(int i){return points[i].x; }
   public double valY(int i){return points[i].y; }
   public double valZ(int i){return points[i].z; }
   public Point getPoint(int i){return points[i]; }
   public void setPoint(Point p, int i){ points[i] = p; }
   public boolean valid(int i){return points[i].valid; }

   public int countValid(){
       int i, n;
       n = 0;
       for (i=0; i<points.length ; i++){ if(points[i].valid) n++;}
       return n;
   }// end countValid   
   } // end class Line
    
   class YZCompare implements Comparator{
	    double yAdj = 0;
	    double zAdj = 0;
	    public int compare (Object o1, Object o2){
	    Point p1 = (Point) o1;
	    Point p2 = (Point) o2;
	    return sgn( p1.angleYZ(yAdj,zAdj) - p2.angleYZ(yAdj,zAdj));
	    }
	    public boolean equals (Object o){
	    return o.equals(this);
            }
	    public void setAdj (double y, double z){
	     yAdj = y;
	     zAdj = z;
	    }
       private int sgn(double a){
        if(a > 0){return 1;}
	     else if(a < 0){return -1;}
	     else{return 0;}
       }
    } // end class YZCompare

   class XZCompare implements Comparator{
	    double xAdj = 0;
	    double zAdj = 0;
	    public int compare (Object o1, Object o2){
	    Point p1 = (Point) o1;
	    Point p2 = (Point) o2;
	    return sgn( p1.angleXZ(xAdj,zAdj) - p2.angleXZ(xAdj,zAdj));
	    }
	    public boolean equals (Object o){
	    return o.equals(this);
            }
	    public void setAdj (double x, double z){
	     xAdj = x;
	     zAdj = z;
	    }
       private int sgn(double a){
        if(a > 0){return 1;}
	     else if(a < 0){return -1;}
	     else{return 0;}
       }
    } // end class XZCompare
    
   class XCompare implements Comparator{
	    public int compare (Object o1, Object o2){
	    Point p1 = (Point) o1;
	    Point p2 = (Point) o2;
	    return sgn( p1.getX() - p2.getX());
	    }
	    public boolean equals (Object o){return o.equals(this);}
       private int sgn(double a){
        if(a > 0){return 1;}
	     else if(a < 0){return -1;}
	     else{return 0;}
       }
    } // end class XCompare
    
   class YCompare implements Comparator{
	    public int compare (Object o1, Object o2){
	    Point p1 = (Point) o1;
	    Point p2 = (Point) o2;
	    return sgn( p1.getY() - p2.getY());
	    }
	    public boolean equals (Object o){return o.equals(this);}
       private int sgn(double a){
        if(a > 0){return 1;}
	     else if(a < 0){return -1;}
	     else{return 0;}
       }
    } // end class YCompare

   class ZCompare implements Comparator{
	    public int compare (Object o1, Object o2){
	    Point p1 = (Point) o1;
	    Point p2 = (Point) o2;
	    return sgn( p1.getZ() - p2.getZ());
	    }
	    public boolean equals (Object o){return o.equals(this);}
       private int sgn(double a){
        if(a > 0){return 1;}
	     else if(a < 0){return -1;}
	     else{return 0;}
       }
    } // end class ZCompare
    
    
   class xzArea{
   private double area;
   private double cx, cz;
   private double tx, tz;
   public xzArea(ArrayList a){
   double ta;
   tx = 0;
   tz = 0;
   
   if (a.size() == 0){
      area = 0;
      cx = 0;
      cz = 0;
      return;
   }
   
   for (int i=0; i<a.size();i++){
      Point p = (Point)a.get(i);
      tx = tx + p.x;
      tz = tz + p.z;
   }   
   tx = tx / a.size();
   tz = tz / a.size();
   
   XZCompare xzComp = new XZCompare();
   xzComp.setAdj(tx,tz);
   SortedSet ss = new TreeSet(xzComp);   
   for (int i=0; i<a.size();i++){ss.add(a.get(i));}   
   Iterator si = ss.iterator();
   Point p0 = (Point) si.next();
   Point p1 = new Point(p0);
   while (si.hasNext()){
     Point p2 = (Point) si.next();
     ta = TriArea(p1.x,p1.z,p2.x,p2.z,tx,tz);
     area = area + ta;
     cx = cx + ta * (p1.x+p2.x+tx) /3.0 ;
     cz = cz + ta * (p1.z+p2.z+tz) /3.0 ;
     p1 = p2;
   }
   ta = TriArea(p1.x,p1.z,p0.x,p0.z,tx,tz);
   area = area + ta;
   cx = cx + ta * (p1.x+p0.x+tx) /3.0 ;
   cz = cz + ta * (p1.z+p0.z+tz) /3.0 ;
   
   cx = cx / area;
   cz = cz / area;
   
   }// end constructor
   
   public double getArea(){return area;}
   public double getAreaX() {return cx;}
   public double getAreaZ() {return cz;}
   public double getMidX() {return tx;}
   public double getMidZ() {return tz;}
   
    public double TriArea(double x1, double y1, double x2, double y2, double x3, double y3){
    double a;
    if (x1 <= x2 && x1 <= x3 )      a = 0.5 * Math.abs((x2 - x1)*(y3 - y1) - (x3-x1) * (y2-y1)) ;  
    else if (x2 <= x1 && x2 <= x3 ) a = 0.5 * Math.abs((x3 - x2)*(y1 - y2) - (x1-x2) * (y3-y2)) ;  
    else                            a = 0.5 * Math.abs((x1 - x3)*(y2 - y3) - (x2-x3) * (y1-y3)) ;  
    return a; 
    }
   
   }// end xzArea 
    
    
    
   class Interp{
   double [] x;
   double [] y;

   public Interp (int n){
   x = new double[n];
   y = new double[n];}

   public Interp(ArrayList ax, ArrayList ay){
      x = new double[ax.size()];
      y = new double[ay.size()];
      int i;
      for (i=0;i<ax.size();i++){
         x[i] = ((Double)ax.get(i)).doubleValue();
         y[i] = ((Double)ay.get(i)).doubleValue();
      }
   }
   
   public Interp (Line l, String X, String Y){
   int i,n;
   // count valid points
   n = 0;
   for (i=0; i< l.length ;i++){if (l.valid(i)) n++;}
   x = new double[n];
   y = new double[n];
   // move data 
   n = 0;
   for (i=0; i< l.length ;i++){
      if (l.valid(i)){
         if (0 == X.compareTo("X")) x[n] = l.valX(i);
         if (0 == X.compareTo("Y")) x[n] = l.valY(i);
         if (0 == X.compareTo("Z")) x[n] = l.valZ(i);
         if (0 == Y.compareTo("X")) y[n] = l.valX(i);
         if (0 == Y.compareTo("Y")) y[n] = l.valY(i);
         if (0 == Y.compareTo("Z")) y[n] = l.valZ(i);
         n++;
      }
     }
   }// end constuctor from Line
   
   public void setX(int i, double v){x[i] = v;}
   public void setY(int i, double v){y[i] = v;}
   public void setXY(int i, double v, double w){x[i] = v; y[i]=w;}

   public double getX(int i){ return x[i];}
   public double getY(int i){ return y[i];}
   public int size(){ return x.length;}

   
   public double interpLG(double v){
   double t;
   double p;
   int i, j;
   t = 0;
   for (i=0; i < x.length; i++){
      p = 1;
      for (j = 0; j < x.length; j++){
         if (i == j) p = p * y[i];
         else p = p * (v-x [j]) / (x[i]-x[j]);
      }
      t = t + p;
   }
   return t;
   }

   public double interp4P(double v){
   double t;
   double p;
   int i, j;
   int imin, imax;
   i=0;
   while ( x[i]< v && i < x.length-1){i++;}
   if (i <= 1) {imin = 0; imax=Math.min(2,x.length-1);}
   else if ( i > x.length - 2) {imin = Math.max(0,x.length-3); imax = x.length-1;}
   else {imin = Math.max(0,i - 2); imax = Math.min(imin + 3,x.length-1);}
   t = 0;
   for (i=imin; i <= imax; i++){
      p = 1;
      for (j = imin; j <= imax; j++){
         if (i == j) p = p * y[i];
         else p = p * (v-x [j]) / (x[i]-x[j]);
         }
      t = t + p;
      }
   return t;
   }
   
   public double leftLinear(double v){
   double t;
   int m = x.length-2;
   for (int i=0; i<=m; i++){ 
      if (x[i+1] == v && v == x[i]) return (y[i]); 
      if (x[i] <= v && v <= x[i+1]) return (y[i] + (y[i+1]- y[i])*(v-x[i])/(x[i+1]-x[i])); 
      if (x[i] >= v && v >= x[i+1]) return (y[i] + (y[i+1]- y[i])*(v-x[i])/(x[i+1]-x[i])); 
   }
   return y[x.length-1];
   }
   
   public double rightLinear(double v){
   for (int i=x.length-1; i>=1; i--){ 
      if (x[i-1] == v && v == x[i]) return (y[i]); 
      if (x[i-1] <= v && v <= x[i]) return (y[i-1] + (y[i]- y[i-1])*(v-x[i-1])/(x[i]-x[i-1])); 
      if (x[i-1] >= v && v >= x[i]) return (y[i-1] + (y[i]- y[i-1])*(v-x[i-1])/(x[i]-x[i-1])); 
   }
   return y[0];
   }
   
   public double leftZero(){
   if (y[0]>=y[1]) return x[0];
   double x1 = x[0];
   double y1 = y[0];
   double x2 = x[1];
   double y2 = y[1];
   double x0 = 0;
   double y0 = 1000000;
   int i = 0;
   while (Math.abs(y0) > 0.001 && i < 10){
     x0 = x2 - y2 * (x2-x1)/(y2-y1);
     y0 = interp4P(x0);
     x1 = x2;
     y1 = y2;
     x2 = x0;
     y2 = y0;
     i++;
   }
   return x0;   
   } // end leftZero

   public double rightZero(){
   int n = x.length;   
   if (y[n-2]<=y[n-1]) return x[n-1];
   double x1 = x[n-2];
   double y1 = y[n-2];
   double x2 = x[n-1];
   double y2 = y[n-1];
   double x0 = 0;
   double y0 = 1000000;
   int i = 0;
   while (Math.abs(y0) > 0.001 && i < 10){
     x0 = x2 - y2 * (x2-x1)/(y2-y1);
     y0 = interp4P(x0);
     x1 = x2;
     y1 = y2;
     x2 = x0;
     y2 = y0;
     i++;
   }
   return x0;   } // end rightZero
   
   
   } //end CLASS Interp
   

