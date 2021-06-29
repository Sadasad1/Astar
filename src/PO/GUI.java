package PO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class GUI extends JFrame {



    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;





    GUI(Wyszukiwanie wysz){

        //JFrame frame = new JFrame();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setSize(800,800);

        getContentPane().setSize(800,800);

        CalcGUI(getContentPane(),this,wysz);

        setResizable(false);
        setLocation(200,200);


        setMinimumSize(new Dimension(800,800));



        pack();

        setVisible(true);



    }

    public static void CalcGUI(Container panel, JFrame f,Wyszukiwanie wysz){

        if (RIGHT_TO_LEFT) {
            panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        panel.setBackground(new Color(0xA5A5B4));

        GridBagLayout grid = new GridBagLayout();
        grid.rowHeights= new int[]{25};
        grid.columnWidths= new int[]{25};

        panel.setLayout(grid);
        GridBagConstraints c = new GridBagConstraints(0,0,4,4,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(2,2,2,2),10,10);





      //  c.fill = GridBagConstraints.BOTH;

       // c.gridheight=4;
      //  c.gridwidth=4;


        c.gridy=0;
        c.gridx=24;
        c.gridwidth=8;
        c.gridheight=16;
        InfoPanel pan = new InfoPanel(wysz);
        panel.add(pan,c);

        Canvas can = new Canvas(wysz,pan);
        can.addMouseMotionListener(can);

        Timer refresh;

        refresh = new Timer();


        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                can.change();
                pan.change();
            }
        };



        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                can.change();
                pan.change();
            }
        };
        refresh.scheduleAtFixedRate(task,1000,50);

        c.gridy= 0;
        //c.weightx = 0.1;
        //c.weighty = 1;
       // c.gridwidth = 2;
     //   c.ipady=10;
     //   c.ipadx=5;
  //      c.gridwidth=4;
   //     c.gridheight=4;

        c.weighty=0.1;
        c.gridheight=2;
        c.weighty=c.gridheight/32;
        c.gridwidth=4;
        c.gridx=0;
        //c.anchor = GridBagConstraints.PAGE_START;
        makebutton("Wyjdź",grid,c,panel,wysz,al);
        c.gridx=4;
        makebutton("Wczytaj graf", grid, c, panel, wysz,al);
        c.gridx=8;
        makebutton("Zapisz graf", grid, c, panel, wysz,al);




        c.gridx=12;
        c.gridwidth=8;
        panel.add(Box.createRigidArea(new Dimension(300,50)), c);

        c.gridx=20;
        c.gridwidth=4;
        makebutton("Ustawienia \n algorytmu", grid, c, panel, wysz,al);

        c.gridx=0;
        c.gridy=2;
        c.gridwidth=8;
        panel.add(Box.createRigidArea(new Dimension(300,50)), c);

        c.gridx=8;
        c.gridwidth=4;
        makebutton("Zrób krok",grid,c,panel,wysz,al);
        c.gridx=12;
        makebutton("Przeszukaj \n punkt",grid,c,panel,wysz,al);
        c.gridx=16;
        makebutton("Wyszukaj",grid,c,panel,wysz,al);
        c.gridx=20;
        makebutton("Resetuj algorytm",grid,c,panel,wysz,al);

        c.weighty=1;

        c.gridx=0;
        c.gridy=4;
        c.gridwidth=24;
        c.gridheight=26;
        c.weighty=c.gridheight/32;
        // can = new Canvas(wysz);




        panel.add(can,c);




        ;

       // c.anchor = GridBagConstraints.LINE_END;
        c.gridx=28;
        c.gridwidth=4;

        c.gridheight=2;
        c.weighty=c.gridheight/32;
        c.weighty=0.001;
        c.ipadx=0;
        c.ipady=0;

        int base=16;
        c.gridy=base;
        makebutton("Dodaj wierzchołek", grid, c, panel, wysz,al);
        c.gridy=base+2;
        makebutton("Wybierz obiekt", grid, c, panel, wysz,al);
        c.gridy=base+4;
        makebutton("Usuń obiekt", grid, c, panel, wysz,al);
        c.gridy=base+6;
        makebutton("Połącz wierzchołki", grid, c, panel, wysz,al);
        c.gridy=base+8;
        makebutton("Dodaj wybrany \n jako start", grid, c, panel, wysz,al);
        c.gridy=base+10;
        makebutton("Dodaj wybrany \n jako cel", grid, c, panel, wysz,al);
        c.gridy=base+12;

        c.gridx=24;
        c.gridy=base;
        c.gridwidth=4;
        c.gridheight=18;
        c.weighty=c.gridheight/32;
        panel.add(Box.createRigidArea(new Dimension(100,450)),c);

        c.gridy=base+12;
        c.gridwidth=4;
        c.gridx=24;
        c.gridheight=18-base;
        c.weighty=c.gridheight/32;
        panel.add(Box.createRigidArea(new Dimension(100,150)),c);



        c.weighty=1/16;
        c.gridx=0;
        c.gridheight=2;
        c.gridy = 30;
        c.gridwidth = 32;
       // makebutton("das",grid,c,panel,wysz);
        panel.add(Box.createRigidArea(new Dimension(800,50)),c);

        c.ipadx=10;
        c.ipady=10;
        c.gridheight=4;
        c.gridwidth=4;







    }

    protected static void makebutton(String name, GridBagLayout gridbag, GridBagConstraints c, Container panel, Wyszukiwanie wyszuk,ActionListener al) {
        JButton button = new JButton("<html>" + name.replaceAll("\\n", "<br>") + "</html>");
        button.setActionCommand(name);
        button.addActionListener(new Zdarzenie(wyszuk));
        button.addActionListener(al);

        gridbag.setConstraints(button, c);

        panel.add(button);
    }

    protected static void makebutton(String name, GridBagLayout gridbag, GridBagConstraints c, Container panel, Boolean vis) {
        JButton button = new JButton("<html>" + name.replaceAll("\\n", "<br>") + "</html>");
        //button.setActionCommand(name);
       // button.addActionListener(new Zdarzenie(wyszuk));
        button.setVisible(vis);


        gridbag.setConstraints(button, c);
        panel.add(button);
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);

    }


}
