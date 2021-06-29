package PO;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.spec.ECField;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

public class Wyszukiwanie implements Drawable {

    Astar star = new Astar();

        TimerTask szukanie= null;

  //  TreeSet<Node> nodes = new TreeSet<Node>();

    Node wybrany=null;
    Node doLaczenia=null;

    int mouseX=0;
    int mouseY=0;

    int treshold=8;

    MouseMode mode = MouseMode.PickNode;

    Rules rule=new Rules();

    Boolean calculation=false;


    Wyszukiwanie(){

    }

    void dodaj(){
        if(mode==MouseMode.WaitMode)
            return;

        Node t= new Node(mouseX,mouseY,treshold);
        if(wybrany==null)
            wybrany = t;

        star.nodes.add(t);


    }

    Node pobierz() throws Exception {
        Node t=star.pobierz(mouseX,mouseY);
        //if(t==null)
            //throw new MyException("Brak obiektu");

       return t;
    }

    Link pobierzLink(){return  star.pobierzLink(mouseX,mouseY);}

    void wybierz() throws Exception {
        Node tmp = pobierz();
        if(tmp!=null)
        wybrany = tmp;
        else
            throw new MyException("Brak obiektu");
    }

    void usun() throws Exception {



      //  star.usun(mouseX,mouseY);

        Node t = star.pobierz(mouseX,mouseY);

       // System.out.println(t.posX + " " + t.posY);

        if(t!=null&&wybrany!=null&&t.compareTo(wybrany)==0)
            wybrany = null;

        if(t!=null&&doLaczenia!=null &&t.compareTo(doLaczenia)==0)
            doLaczenia = null;


        if(t!=null)
        {
            star.usun(t);
        }
        else {
            Link l=star.pobierzLink(mouseX,mouseY);
            if(l!=null)
            star.usunLink(l);

        }








    }

    void toStart()throws Exception{
        if(mode==MouseMode.WaitMode)
            return;

        try {
            if (!wybrany.equals(star.target))
                star.first = wybrany;
        }
        catch (Exception e){
            throw new MyException("Brak wymaganych punktów");
        }

    }

    void  toKoniec()throws Exception {
        if(mode==MouseMode.WaitMode)
            return;

        try {
            if (!wybrany.equals(star.first))
                star.target = wybrany;
        }
        catch (Exception e){
            throw new MyException("Brak wymaganych punktów");
        }
    }

    void wcisciecie(){

        try {

            switch (mode) {

                case AddNode:
                    System.out.println("Wcisniecie, dodawanie");
                    star.dodaj(mouseX, mouseY, treshold);
                    wybierz();
                    break;

                case PickNode:

                    wybierz();
                    break;

                case DeleteNode:
                    usun();
                    break;

                case JoinPickNode:
                    wybierz();
                    mode = MouseMode.JoinNode;
                    break;
                case JoinNode:
                    doLaczenia = pobierz();
                    //połącz tutaj
                    if (doLaczenia != null) {

                        String[] choices = {"Zwykła", "Autostrada"};

                        Object[] boxes = new JComponent[choices.length + 2];
                        //Object[] boxes = new JCheckBox[choices.length+1];
                        ButtonGroup group = new ButtonGroup();

                        for (int i = 0; i < boxes.length - 2; i++) {
                            boxes[i] = new JCheckBox(choices[i]);
                            if (i == 0)
                                ((JCheckBox) boxes[i]).setSelected(true);

                            group.add((JCheckBox) boxes[i]);
                        }

                        JLabel label = new JLabel("Podaj wagę");
                        boxes[boxes.length - 2] = label;

                        JTextField field = new JTextField();
                        boxes[boxes.length - 1] = field;

                        // DialogInputs dialog = new DialogInputs();

                        // JOptionPane pan= new JOptionPane();

                        // pan.setOptions(boxes);


                        int option = JOptionPane.showConfirmDialog(null, boxes, "Test", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            try {

                                String res = ((JTextField) boxes[boxes.length - 1]).getText();
                                res = res.replaceAll(",", ".");

                                float var = Float.parseFloat(res);

                                for (int i = 0; i < choices.length; i++) {
                                    if (((JCheckBox) boxes[i]).isSelected()) {
                                        wybrany.addLink(doLaczenia, var, choices[i]);
                                        System.out.println(choices[i]);
                                    }
                                }
                            }
                            catch (Exception e){
                                throw new MyException("Błędne parametry");
                            }

                        } else {
                            //throw new MyException("Błędne parametry");
                           // System.out.println("Błąd");
                        }
                        //String res=res=(String)JOptionPane.showInputDialog(new JFrame(),"Podaj wagę połączenia","Połącz",JOptionPane.PLAIN_MESSAGE);

                        //res=res.replaceAll(",",".");

                        // float var = Float.parseFloat(res);

                        // wybrany.addLink(doLaczenia, var);


                    }

                    mode = MouseMode.JoinPickNode;
                    break;


            }

        }
        catch (Exception e){
            JOptionPane.showConfirmDialog(null,e.getMessage(),"Błąd",JOptionPane.OK_OPTION);
           // JOptionPane.showInputDialog(new JFrame(),"Podaj wagę połączenia","Połącz",JOptionPane.PLAIN_MESSAGE);
        }

    }

    void anulujWcisniecie(){
        if(mode==MouseMode.JoinNode)
        mode=MouseMode.JoinPickNode;
        else if(mode==MouseMode.WaitMode) {
            szukanie.cancel();
            mode=MouseMode.PickNode;
        }

    }

    public void Dodawanie(){
        if(mode!=MouseMode.WaitMode)
        mode= MouseMode.AddNode;
    }

    public void Wybieranie(){
        if(mode!=MouseMode.WaitMode)
        mode= MouseMode.PickNode;
    }
    public void Usuwanie(){
        if(mode!=MouseMode.WaitMode)
        mode= MouseMode.DeleteNode;
    }
    public void Laczenie(){
        if(mode!=MouseMode.WaitMode)
        mode= MouseMode.JoinPickNode;
    }

    public void draw(Graphics2D g2d){

        star.draw(g2d);

        if(mode==MouseMode.JoinNode)
            drawLaczenie(g2d);


    }

    @Override
    public void draw(Graphics2D g2d, Color c) {
        star.draw(g2d);

        if(mode==MouseMode.JoinNode)
            drawLaczenie(g2d);
    }

    private void drawLaczenie(Graphics2D g2d) {

        //g2d.setColor(Color.ORANGE);

        drawArrow(g2d, wybrany.posX,  wybrany.posY,mouseX,mouseY, wybrany.treshold/3,10,Color.ORANGE );




    }


    public void infoDraw(Graphics2D g2d) {

        if(wybrany!=null){
            //g2d.drawString("Wybrany węzeł: ",50,60);
            g2d.drawString("Wybrany węzeł: ("+wybrany.posX + "," + wybrany.posY + ")",40,80);
        }

        g2d.drawString(mode.label, 40,40);

        star.infoDraw( g2d);

       // if(mode==MouseMode.WaitMode)

    }

    public void najechanie() throws Exception {

        if(mode==MouseMode.WaitMode )
            return;

        Node t=null ;
                t=pobierz();

        if(t!=null) {
            t.hovered = true;

        }
        else
        {
            Link l = pobierzLink();

            if(l!=null) {
                l.hovered = true;

            }
        }

        if(t==null)
            throw new MyException("Nie znaleziono obiektu");
    }

    public void przeciagnij(int x, int y) {

        if(mode!=MouseMode.PickNode&&mode!=MouseMode.DragMode)
            return;

            if(mode==MouseMode.DragMode) {
                wybrany.posX = x;
                wybrany.posY = y;
            }
            else if(wybrany.compareTo(new Node(x,y))==0) {
                wybrany.posX = x;
                wybrany.posY = y;
                mode=MouseMode.DragMode;
            }


    }

    public void pusc() {
        if(mode==MouseMode.DragMode)
            mode=MouseMode.PickNode;
    }

    public Boolean oneStep() throws Exception {
        return star.oneStep(rule);
    }

    public Boolean przeszukajJeden() throws Exception {

            Boolean all = false;
            while (!all) {
                all = oneStep();
            }

        return star.ended;
    }

    public void przeszukaj()throws Exception{

        mode=MouseMode.WaitMode;

        szukanie = new TimerTask() {
            @Override
            public void run() {
                try {
                    oneStep();
                } catch (Exception e) {
                    mode=MouseMode.PickNode;

                    JOptionPane.showConfirmDialog(null,e.getMessage(),"Błąd",JOptionPane.OK_OPTION);

                    this.cancel();

                }

                if(star.ended){
                    mode=MouseMode.PickNode;



                    this.cancel();
                }
            }
        };

        Timer czas = new Timer();

        czas.scheduleAtFixedRate(szukanie,0,400);
     //   czas.schedule(szukanie,0,200);



   //     Boolean all2=false;
    //    while(!all2){
    //       all2= przeszukajJeden();
     //   }

    }

    public void resetuj() {

        if(szukanie!=null) {
            szukanie.cancel();
        }
        mode = MouseMode.PickNode;

        star.resetuj();
    }

    public void ustawienia() throws MyException {



                Object [] boxes = rule.getBoxes();

        int option = JOptionPane.showConfirmDialog(null, boxes, "Test", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {



                for (int i = 1; i < boxes.length; i++) {
                    if (((JCheckBox) boxes[i]).isSelected()) {

                    }
                }

            }
            catch (Exception e){
                throw new MyException("Błędne parametry");
            }

        }

    }
}
