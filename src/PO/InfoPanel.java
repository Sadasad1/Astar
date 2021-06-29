package PO;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {

    private Wyszukiwanie wyszukiwanie;

    public boolean draw = true;

    private static final int DEFAULT_WIDTH = 8;
    private static final int DEFAULT_HEIGHT =12 ;

    InfoPanel(Wyszukiwanie wysz){
        wyszukiwanie=wysz;
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
    }

    public void change(){
        draw=!draw;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        wyszukiwanie.infoDraw(g2d);
    }
}
