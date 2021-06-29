package PO;



import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class Astar implements Serializable  {

    HashSet<Node> nodes=new HashSet<Node>();

    PriorityQueue<Node> to_check = new PriorityQueue<Node>();

    HashSet<Node> checked = new HashSet<Node>();

    Node current=null;

    Node first;
    Node target;

    Boolean two_lanes;
    Boolean started=false;
    Boolean ended=false;


    public void save(String filename){
        try{
            ObjectOutputStream wy = new ObjectOutputStream(new FileOutputStream(filename));

            wy.writeObject(first);
            wy.writeObject(target);

            Iterator<Node> it = nodes.iterator();

            Node current;
            while (it.hasNext()){
                current=it.next();
                wy.writeObject(current);
            }



            // wy.writeObject(samoloty);
            wy.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void saveXML(String filename){
        Iterator<Node> it = nodes.iterator();
        try {
            FileWriter f = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(f);
            XStream mapping = new XStream(new DomDriver());

            String xml=mapping.toXML(nodes);

            out.write(xml);
            out.close();


        }
        catch (IOException e){

        }
    }

    public static String Node2Xml(Node n){
        XStream mapping = new XStream(new DomDriver());
        String xml = mapping.toXML(n);
        return xml;
    }

    public void saveResult(String filename){
        try {
            ObjectOutputStream wy = new ObjectOutputStream(new FileOutputStream(filename));
            Link l = target.getPrev();
            while (l != null) {
                wy.writeObject(l);
                l=l.from.getPrev();
            }
        }
        catch (IOException e){

        }

    }

    public void load(String filename){
        try{
            FileInputStream f =new FileInputStream(filename);
            ObjectInputStream we = new ObjectInputStream(new BufferedInputStream(f));

            nodes.clear();
            checked.clear();
            first=null;
            target=null;
            Node s=(Node) we.readObject();
            while (s!=null){
                s.initialise();
                if(first==null)
                    first=s;
                else if(target==null)
                    target=s;
                else {
                    nodes.add(s);
                }
                s=(Node) we.readObject();
            }

        }
        catch (EOFException e){

        }
        catch (Exception e){
            e.printStackTrace();
        }
        ended=false;
        started=false;
    }

    public void loadXML(String filename){
        String xml = "";
        String strLine = "";

        if (filename != null) {
            try {
                FileReader f = new FileReader(filename);
                BufferedReader r = new BufferedReader(f);
                XStream mapping = new XStream(new DomDriver());
                //lub
                /*FileInputStream f = new FileInputStream(filename);
                DataInputStream in = new DataInputStream(f);
                BufferedReader r = new BufferedReader(new InputStreamReader(in));*/
                while ((strLine = r.readLine()) != null)
                    xml += strLine;

                nodes = (HashSet<Node>) mapping.fromXML(xml);

                r.close();




            } catch (Exception e) {
                System.out.println(e);
                System.exit(0);
            }
        }
    }

    public void draw(Graphics2D g2d){

            g2d.setColor(new Color(0x47463F));
            g2d.fillRect(0,0,5000,5000);
            g2d.setColor(new Color(0xFFFFFF));


        Color c;
        for (Node n: nodes) {
            if(first!=null&&first.compareTo(n)==0)
                c=new Color(0x33FF00);
            else if(target!=null&&target.compareTo(n)==0)
                c=new Color(0xFF0000);
            else
                c=new Color(0xFFFFFF);

            n.draw(g2d, c);

        }
//        if(first!=null)
//        first.draw(g2d,new Color(0x33FF00));
//        if(target!=null)
//        target.draw(g2d,new Color(0xFF0000));

        Node t=target;

        while (t!=null){
           t = t.drawSolution(g2d);

        }



    }



    public void dodaj(int x, int y, int treshold) {

        Node t = new Node(x,y,treshold);
        if(nodes.contains(t)){

        }
        else {
            if(first==null)
                first=t;
            else if(target==null)
                target=t;

            nodes.add(t);
            System.out.println(nodes.size());
        }



    }


    public Node pobierz(int x, int y) throws Exception {
        Iterator<Node> it = nodes.iterator();

        Node tmp = new Node(x,y);


        Node current;
        while(it.hasNext() ) {
            current = it.next();

            Boolean tmps= current.compareLoc;

            current.compareLoc=true;
            if(current.equals(tmp)) {
                current.compareLoc=tmps;
                return current;
            }

        }

        return null;
    }

    public void usun(Node t) {

        if(first!=null && t.compareTo(first)==0)
            first=null;
        if(target!=null && t.compareTo(target)==0)
            target=null;




        Iterator<Node> it = nodes.iterator();
        Node current;
        while(it.hasNext() ) {
            current = it.next();

            Iterator<Link> it2 = current.links.iterator();
            ArrayList<Link> to_remove= new ArrayList<Link>();

            Link cur;
            while (it2.hasNext()){
                cur = it2.next();

                if(cur.to.compareTo(t)==0)
                    to_remove.add(cur);
            }

            Iterator<Link> it3 = to_remove.iterator();
            while (it3.hasNext()) {
                cur=it3.next();
                current.links.remove(cur);
            }
            to_remove.clear();

        }

        nodes.remove(t);

        if(first==null&&nodes.size()>0) {
            Optional<Node> tmp = nodes.stream().findFirst();
            if(!tmp.get().equals(target))
            first = tmp.get();
        }
       else if(target==null &&nodes.size()>1) {
            Optional<Node> tmp = nodes.stream().findFirst();
            if(!tmp.get().equals(first))
            target = tmp.get();
        }

    }

    public void usunLink(Link l){

        Iterator<Node> it = nodes.iterator();
        Node current;
        while (it.hasNext()) {
            current = it.next();
                if(current.links.contains(l)){
                 current.links.remove(l);
                return;
                }
        }
    }

    public Link pobierzLink(int mouseX, int mouseY) {




        double val=Double.MAX_VALUE;
        AbstractMap.SimpleEntry<Double, Link> res=null;

        Link tmp=null;

        if(target!=null)
            tmp=target.getPrev();

        while(tmp!=null){

            double tmps = obliczLink(tmp,mouseX,mouseY,val);
            if(tmps<val) {
                res=new AbstractMap.SimpleEntry<>(tmps,tmp);
                val=tmps;

            }
            tmp=tmp.from.getPrev();
        }

        Iterator<Node> it = nodes.iterator();
        AbstractMap.SimpleEntry<Double, Link> t;

        t=przeszukajNLink(it,mouseX,mouseY,val);
        if(t!=null){
            res = t;
            val = res.getKey();
            t = null;
        }






//        if(res!=null) {
//
//            val = res.getKey();
//           // System.out.println(res.getKey());
//        }


            Iterator<Node> it3 = checked.iterator();


            t = przeszukajNLink(it3, mouseX, mouseY, val);
            if(t!=null) {
                res = t;
                val = res.getKey();
                t = null;
            }


        Iterator<Node> it4 = to_check.iterator();

        t=przeszukajNLink(it4,mouseX,mouseY,val);
        if(t!=null) {
            res = t;
            val = res.getKey();
        }


        if(res==null)
            return null;

        return res.getValue();
    }

    public AbstractMap.SimpleEntry<Double, Link> przeszukajNLink(Iterator<Node> it,int mouseX, int mouseY, double val){

        double val2=val;

        AbstractMap.SimpleEntry<Double,Link> tmps=null;
        while (it.hasNext()){

            AbstractMap.SimpleEntry<Double,Link> tmpss=null;

            Node tmp=it.next();
            Iterator<Link> it2=tmp.links.iterator();

             tmpss =przeszukajLink(it2,mouseX,mouseY,val2);
             if(tmpss!=null) {
                 tmps = tmpss;
                 val2=  tmps.getKey();
             }

            Iterator<Link> it3=tmp.getChecked();
            tmpss =przeszukajLink(it3,mouseX,mouseY,val2);
            if(tmpss!=null) {
                tmps = tmpss;
                val2=  tmps.getKey();
            }

            Iterator<Link> it4=tmp.getSkipped();
            tmpss =przeszukajLink(it4,mouseX,mouseY,val2);
            if(tmpss!=null) {
                tmps = tmpss;
                val2=  tmps.getKey();
            }

        }
        return tmps;
    }

    public AbstractMap.SimpleEntry<Double, Link> przeszukajLink(Iterator<Link> it, int mouseX, int mouseY, double val){

        Link best=null;
        double val2=val;

        Link t;

        while (it.hasNext()){
            t=it.next();

            double tmp=obliczLink(t,mouseX,mouseY,val2);

            if(tmp<val2){
                best=t;
                val2=tmp;
            }
        }

        if(best==null)
            return null;

        AbstractMap.SimpleEntry<Double, Link> entry
                = new AbstractMap.SimpleEntry<>(val2, best);


        return entry ;
    }

    public double obliczLink(Link cur,int mouseX, int mouseY, double val){
        int x1=cur.from.posX;
        int y1=cur.from.posY;
        int x2=cur.to.posX;
        int y2=cur.to.posY;

        double dist;
        double tx;
        double ty;

        double a1;
        double a2;

        if(x1==x2){
            dist=Math.sqrt((y2-y1)*(y2-y1));
            ty=mouseY;
            tx=x1;

        }
        else if(y1==y2){
            dist=Math.sqrt((x2-x1)*(x2-x1));
            tx=mouseX;
            ty=y1;
        }
        else {

            double a=((double) y2-y1)/(x2-x1);

            double b= y1-a*x1;

            tx=(mouseX+a*(mouseY-b))/(a*a+1);

            ty=a*tx+b;

            dist = Math.sqrt((mouseX-tx)*(mouseX-tx)+(mouseY-ty)*(mouseY-ty));

        }

        a1=(tx-x1)/(x2-x1);
        a2=(ty-y1)/(y2-y1);

        if(dist<val&&dist<25 && a1 <=0.95 && a1>=0.05 && a2 <= 0.95 && a2 >= 0.05 ) {
            return  dist;

        }
        else
            return Double.MAX_VALUE;
    }

    public void start()throws Exception{
        started=true;
        if(first==null||target==null) {
            started=false;
            throw new MyException("Brak startu lub końca");
        }
       // first.local_value=0;
        first.check(null,0,target);

        to_check.add(first);
    }

    public Boolean oneStep(Rules rule) throws Exception{
        if(current==null){
            Node t =to_check.peek();
            if(t==null&&!started) {
                start();
                current=to_check.peek();
                current.checking(true);
            }
            else if(t==null){
                System.out.println("Koniec");
                ended=true;
                if(target.getPrev()==null)
                    throw new MyException("Nie znaleziono trasy");
                else {


                    JButton boxes = new JButton("Zapisz wynik do pliku ") ;

                    boxes.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JFileChooser chooser = new JFileChooser();

                            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                    ".dat files", "dat");

                            // chooser.setFileFilter(filter);
                            chooser.addChoosableFileFilter(filter);

                            FileNameExtensionFilter filter2 = new FileNameExtensionFilter(
                                    ".xml files", "xml");

                            chooser.addChoosableFileFilter(filter2);
                            int returnVal = chooser.showSaveDialog(null);
                            if (returnVal == JFileChooser.APPROVE_OPTION) {
                                File fileToSave = chooser.getSelectedFile();

                                if (!fileToSave.getAbsolutePath().endsWith(".dat"))
                                    saveResult(fileToSave.getAbsolutePath() + ".dat");
                                else if(fileToSave.getAbsolutePath().endsWith(".xml")){
                                    //wyszukiwanie.star.save();
                                }
                                else
                                    saveResult(fileToSave.getAbsolutePath() );
                            }

                        }
                    });


                    int option = JOptionPane.showConfirmDialog(null, boxes, "Test", JOptionPane.OK_CANCEL_OPTION);

                    throw new MyException("Algorytm znalazł najlepszą trasę");
                }

             //   return true;
            }
            else {
                current = to_check.peek();
                current.checking(true);
            }
        }


        Node nextNode= current.oneStep(target,rule);
        if(nextNode!=null){
            if(nextNode!=target) {
                to_check.add(nextNode);
                System.out.println("Dodano węzeł " + nextNode.posX + " " + nextNode.posY);
            }
            return false;
        }
        else {
            current.checking(false);
            checked.add(current);
            to_check.remove(current);
            //checked.add(current);
            current=null;

            current = to_check.peek();
            if(current!=null)
                current.checking(true);

            return true;
        }
    }


    public void resetuj() {

        nodes.addAll(to_check);
        nodes.addAll(checked);

        to_check.clear();
        checked.clear();

        ended=false;
        started=false;
        current=null;

        Iterator<Node> it = nodes.iterator();

        Node t;
        while (it.hasNext()){
            t=it.next();

            t.initialise();
        }

    }

    public void infoDraw(Graphics2D g2d) {

        if(started&&target.getPrev()==null){
            g2d.drawString("Szukanie ścieżki", 40,100);
        }
        else if(started){
            g2d.drawString("Znaleziona droga o długości: " + target.getLength(), 40,100);
        }
    }
}
