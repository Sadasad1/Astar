package PO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.TimerTask;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener {

    private int x, y;

    private InfoPanel panel;

    private Wyszukiwanie wyszukiwanie;

    public boolean draw = true;

    private static final int DEFAULT_WIDTH = 24;
    private static final int DEFAULT_HEIGHT = 26;



    public Canvas(Wyszukiwanie wysz, InfoPanel pan){
        addMouseListener(this);
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        wyszukiwanie=wysz;
        panel=pan;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        try {
            if (e.getButton() == MouseEvent.BUTTON1)
                wyszukiwanie.wcisciecie();
            else
                wyszukiwanie.anulujWcisniecie();

        }
        catch (Exception e1){
            JOptionPane.showConfirmDialog(null,e1.getMessage(),"Błąd",JOptionPane.OK_OPTION);
        }

       // wyszukiwanie.dodaj(e.getX(),e.getY());
     //   System.out.println(wyszukiwanie.star.nodes.size() + " " + e.getX() + " " + e.getY());
        change();
        panel.change();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
            wyszukiwanie.pusc();
            panel.change();
    }

    @Override
    public void mouseEntered(MouseEvent e) {



    }



    @Override
    public void mouseExited(MouseEvent e) {

    }



    public void change(){
        draw=!draw;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        try {
            wyszukiwanie.najechanie();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        wyszukiwanie.draw(g2d);



    }

    @Override
    public void mouseDragged(MouseEvent e) {

        int tx=e.getX();
        int ty=e.getY();


        if (tx>=this.getSize().width-1)
            tx=this.getSize().width-2;
        if(tx<=0)
            tx=1;
        if (ty>=this.getSize().height-1)
            ty=this.getSize().height-2;
        if(ty<=0)
            ty=1;

        wyszukiwanie.przeciagnij(tx,ty);
        wyszukiwanie.mouseX=e.getX();
        wyszukiwanie.mouseY=e.getY();
        change();
        panel.change();
    }

    @Override
    public void mouseMoved(MouseEvent e) {


        wyszukiwanie.mouseX=e.getX();
        wyszukiwanie.mouseY=e.getY();
        try {
            wyszukiwanie.najechanie();
        } catch (Exception exception) {
           // JOptionPane.showConfirmDialog(null,exception.getMessage(),"Błąd",JOptionPane.OK_OPTION);
        }
        change();

    }
}
