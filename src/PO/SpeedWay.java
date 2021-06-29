package PO;

import java.awt.*;

public class SpeedWay extends Link{

    SpeedWay(Node too, Node fromm, float weigh) {
        super(too, fromm, weigh);
    }

    @Override
    public void draw(Graphics2D g2d, Color c) {
        int deltaY =to.posY - from.posY;
        int deltaX = to.posX - from.posX;
        double a;
        if(deltaX!=0)
            a =((double) deltaY)/deltaX;
        else
            a =0.00001;
           // c=new Color(0x5619BA);

        int radius=15;

        if(hovered) {

            c=c.brighter();

            // g2d.setColor(new Color(0x0FD212));

            g2d.setStroke(new BasicStroke(6));
        radius=20;



        }
        else
            g2d.setStroke(new BasicStroke(4));

        g2d.setColor(c);

        drawArrow(g2d, from.posX,  from.posY ,to.posX,to.posY, from.treshold,radius, c);

        g2d.setColor(new Color(0xD0B414));

        if(a*a>1)
            g2d.drawString(String.valueOf(weight),(to.posX+ from.posX+10)/2,(to.posY+ from.posY+10)/2);
        else
            g2d.drawString(String.valueOf(weight),(to.posX+ from.posX-10)/2,(to.posY+ from.posY-10)/2);



        hovered=false;

    }
}
