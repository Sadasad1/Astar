package PO;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.*;

public class Node implements Serializable, Comparable<Node> {

    transient boolean hovered=false;
    transient  boolean compareLoc=true;

    int posX;
    int posY;
    int treshold;



    //Node foundPrev=null;


    HashSet<Link> links = new HashSet<>();



     int number;




    private class AlgInf implements  Comparable<AlgInf>{
        transient boolean visited=false;



        transient boolean checking=false;

        transient  double local_value=Double.MAX_VALUE;
        transient double global_value=Double.MAX_VALUE;

        transient Link foundPrev=null;

        transient ArrayList<Link> checked = new ArrayList<Link>();
        transient ArrayList<Link> skipped = new ArrayList<Link>();

        AlgInf(){
            compareLoc=true;
            foundPrev=null;
            checking=false;
            checked=new ArrayList<Link>();
            skipped=new ArrayList<Link>();
            visited=false;
            local_value=Double.MAX_VALUE;
            global_value=Double.MAX_VALUE;
        }

        public void initialise(){
           // compareLoc=false;
            foundPrev=null;
            checking=false;
            checked=new ArrayList<Link>();
            skipped=new ArrayList<Link>();
            visited=false;
            local_value=Double.MAX_VALUE;
            global_value=Double.MAX_VALUE;
        }

        public void checking(Boolean b){
            checking=b;
        }

        Iterator<Link> getChecked(){
            return checked.iterator();
        }

        public void check(Link first,double prev,Node target){
            if(first==null) {
                local_value=0;
                return;
            }

                if(local_value>prev) {

                   // Link

                    if(first instanceof SpeedWay)
                    foundPrev= new SpeedWay(first.to, first.from,first.weight);
                    else
                        foundPrev= new StandardWay(first.to, first.from,first.weight);


                    algorithm.local_value = prev;
                    global_value = local_value + Node.getDistance(first.to, target)/20;
                    compareLoc = false;
                    visited=true;
                }
        }

        public Node drawSolution(Graphics2D g2d){
            if(foundPrev!=null){
                foundPrev.draw(g2d,new Color(0xD20F0F));
                return foundPrev.from;
            }
            else
                return null;
        }

        public void draw(Graphics2D g2d){

            for (Link l: skipped) {
                l.draw(g2d,new Color(0x1AFF00));
            }

            for (Link l: checked) {
                l.draw(g2d,new Color(0x002AFF));
            }

            if(checking){

                g2d.setColor(new Color(0xFF25CD));
                g2d.drawRect(posX-treshold,posY-treshold,2*treshold,2*treshold);
            }

            if(visited){
                g2d.setColor(new Color(0xFFFFFF));
                g2d.drawString("(" + (int)global_value + ";" + (int)local_value + ")",posX-10,posY-5);
            }
        }


        @Override
        public int compareTo(AlgInf o) {

                return (int)( global_value-o.global_value);

        }

        public Link getPrev() {
        return foundPrev;
        }

        public Iterator<Link> getSkipped() {
            return skipped.iterator();
        }
    }

   transient private AlgInf algorithm;

    Node(){

    }

    Node(int posx,int posy){
        posX=posx;
        posY=posy;
        treshold=0;
        initialise();

    }

    Node(int posx,int posy, int tresho){

        posX=posx;
        posY=posy;
        treshold=tresho;
        initialise();

    }

    public void initialise(){

        hovered=false;

        if(algorithm!=null&&algorithm.checked!=null) {
            links.addAll(algorithm.checked);
            links.addAll(algorithm.skipped);
        }

        algorithm=new AlgInf();

        Iterator<Link> it = links.iterator();
        Link l;
        while (it.hasNext()){
            l=it.next();
            l.initialise();
        }

    }

    public void addLink(Node too, float weight, String type){
        Link l=null;
        switch (type){
            case "Autostrada":
                l=new SpeedWay(too,this , weight);
                break;
            case "Zwykła":
                l=new StandardWay(too,this , weight);
                break;
        }

        links.add(l);
    }

    public Node getThis(){
        return this;
    }

    public void setPos(int posx,int posy){
        posX=posx;
        posY=posy;
    }


    @Override
    public int compareTo(Node o) {

        //if()
        int dx=o.posX-posX;
        int dy = o.posY-posY;


        if(compareLoc) {
            if (dx * dx <= treshold * treshold && dy * dy <= treshold * treshold) return 0;
            else if (dx > 0) return 1;
            else return -1;
        }
        else
        {
            return algorithm.compareTo(o.algorithm);
        }

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return compareTo(node)==0? true:false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(posX, posY);
    }

    void draw(Graphics2D g2d, Color c){

        if(hovered){
            g2d.setColor(new Color(0x2AA5A5));
            g2d.drawRect(posX-treshold,posY-treshold,2*treshold,2*treshold);
        }



         g2d.setColor(c);
        g2d.fillRect(posX-treshold/2,posY-treshold/2,treshold,treshold);

         for (Link l: links) {
             if(l instanceof StandardWay)
             l.draw(g2d,new Color(0x1AFF00));
             else
                 l.draw(g2d,new Color(0x5619BA));
         }

         algorithm.draw(g2d);





        hovered=false;
    }

    Node drawSolution(Graphics2D g2d){
        //g2d.setColor(new Color(0xD20F0F));
        return algorithm.drawSolution(g2d);

    }

    public void check(Link first,double prev,Node target){
        algorithm.check( first, prev, target);

    }

    public static double getDistance(Node a, Node b){
        return Math.sqrt((a.posX-b.posX)*(a.posX-b.posX)+(a.posY-b.posY)*(a.posY-b.posY));
    }

    public Node oneStep(Node target, Rules rule){
        if(algorithm==null)
            algorithm= new AlgInf();

        Optional<Link> ll =links.stream().findFirst();

        Link l =null;

        Boolean found=false;
        while(!ll.isEmpty()&&!found) {

            Object[] boxes = rule.getBoxes();
            l= ll.get();
           if( l instanceof StandardWay &&((JCheckBox)boxes[1]).isSelected()){

               links.remove(l);
               found=true;
           }
           else if(l instanceof SpeedWay &&((JCheckBox)boxes[2]).isSelected()){

               links.remove(l);
               found=true;

           }
            else {
                algorithm.skipped.add(l);
                links.remove(l);
                l=null;
                ll=links.stream().findFirst();
           }

          // l= ll.get();
            //links.remove(l);

        }

        if(l!=null) {
            l.to.check(l,algorithm.local_value+l.weight, target);

            algorithm.checked.add(l);
            return l.to;
        }
        return null;
    }

    public void checking(Boolean b){
        algorithm.checking(b);
    }

    public Link getPrev(){
        if(algorithm==null)
            return null;
        else
            return algorithm.getPrev();

    }

    public Iterator<Link> getChecked() {
        if(algorithm==null)
            return null;
        else
            return algorithm.getChecked();
    }

    public Iterator<Link> getSkipped() {
        if(algorithm==null)
            return null;
        else
            return algorithm.getSkipped();
    }

    public double getLength(){
        return algorithm.local_value;
    }

}
