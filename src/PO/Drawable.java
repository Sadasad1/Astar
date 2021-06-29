package PO;

import java.awt.*;

public interface Drawable {

         void draw(Graphics2D g2d);
         void draw(Graphics2D g2d, Color c);


        default void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2,int treshold, int radius, Color c){


            g2d.setColor(c);
            int deltaX =x2 - x1;
            int deltaY = y2 - y1;
            double a;
            if(deltaX!=0)
                a =((double) deltaY)/deltaX;
            else
                a =0.00001;

            double angle = Math.atan2(deltaY,deltaX);

            double dist=Math.sqrt(deltaX*deltaX+deltaY*deltaY);

            int deltX=(int) (Math.cos(angle)*(2.0*treshold/3.0));
            int deltY=(int) (Math.sin(angle)*(2.0*treshold/3.0));

            x2=x1+(int) (Math.cos(angle)*(dist-2.0*treshold/3.0-radius/2.0));
            y2=y1+(int) (Math.sin(angle)*(dist-2.0*treshold/3.0-radius/2.0));

            g2d.drawLine(x1+deltX, y1+deltY, x2,y2);

            x2=x1+(int) (Math.cos(angle)*(dist-2.0*treshold/3.0));
            y2=y1+(int) (Math.sin(angle)*(dist-2.0*treshold/3.0));

            angle += (5.0/12.0) * 2*Math.PI;



            double x = radius * Math.cos(angle) + x2;
            double y = radius * Math.sin(angle) + y2;


            //  g2d.drawLine(targX,targY,(int)x,(int)y);

            angle +=(2.0/12.0) * 2*Math.PI;

            double x3 = radius * Math.cos(angle) + x2;
            double y3 = radius * Math.sin(angle) + y2;


            //  g2d.drawLine(targX,targY,(int)x2,(int)y2);

            g2d.fillPolygon(new int[]{x2,(int)x,(int)x3},new int[]{y2,(int)y,(int)y3},3);
        }

}
