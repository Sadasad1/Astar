package PO;

import java.awt.*;
import java.io.Serializable;

public abstract class Link implements Comparable<Link>, Serializable,Drawable {

    Node from;
    Node to;

    transient Boolean hovered;




    boolean two_lanes;

    float weight;

    Link(Node too,Node fromm, float weigh){
        this.to=too;
        this.from=fromm;
        this.weight=weigh;
        initialise();
    }

    public void initialise(){
        hovered=false;
    }


    @Override
    public int compareTo(Link o) {

        if(from.compareTo(o.from)==0 &&to.compareTo(o.to)==0)
            return 0;
        else if(from.compareTo(o.from)!=0)
            return from.compareTo(o.from);
        else
            return to.compareTo(o.to);

    }

    @Override
    public void draw(Graphics2D g2d) {
        //draw(g2d,new Color(100));
    }

    public void draw(Graphics2D g2d, Color c ) {


        int deltaY =to.posY - from.posY;
        int deltaX = to.posX - from.posX;
        double a;
        if(deltaX!=0)
         a =((double) deltaY)/deltaX;
        else
            a =0.00001;

        int radius=10;
        if(hovered) {

            c=c.brighter();

           // g2d.setColor(new Color(0x0FD212));

            g2d.setStroke(new BasicStroke(3));
            radius=15;


        }
        else
            g2d.setStroke(new BasicStroke(2));

        g2d.setColor(c);

        drawArrow(g2d, from.posX,  from.posY ,to.posX,to.posY, from.treshold,radius, c);


        if(hovered||!hovered){
            g2d.setColor(new Color(0xD0B414));

            if(a*a>1)
                g2d.drawString(String.valueOf(weight),(to.posX+ from.posX+10)/2,(to.posY+ from.posY+10)/2);
            else
                g2d.drawString(String.valueOf(weight),(to.posX+ from.posX-10)/2,(to.posY+ from.posY-10)/2);
        }


        hovered=false;
    }
}
