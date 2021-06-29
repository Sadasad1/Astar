package PO;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Zdarzenie implements ActionListener {

    Wyszukiwanie wyszukiwanie;

    public Zdarzenie(Wyszukiwanie wysz){
        wyszukiwanie=wysz;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();

        try {


            switch (command) {

                case "Wyjdź":
                    System.exit(0);
                    break;
                case "Usuń obiekt":
                    wyszukiwanie.Usuwanie();
                    break;
                case "Wybierz obiekt":
                    wyszukiwanie.Wybieranie();
                    break;
                case "Dodaj wierzchołek":
                    wyszukiwanie.Dodawanie();
                    break;
                case "Połącz wierzchołki":
                    wyszukiwanie.Laczenie();
                    break;
                case "Dodaj wybrany \n jako start":
                    wyszukiwanie.toStart();
                    break;
                case "Dodaj wybrany \n jako cel":
                    wyszukiwanie.toKoniec();
                    break;
                case "Zapisz graf":
                    wyszukiwanie.resetuj();
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


                         if(fileToSave.getAbsolutePath().endsWith(".xml")){
                            wyszukiwanie.star.saveXML(fileToSave.getAbsolutePath());
                        }
                        else if (!fileToSave.getAbsolutePath().endsWith(".dat"))
                            wyszukiwanie.star.save(fileToSave.getAbsolutePath() + ".dat");
                        else
                            wyszukiwanie.star.save(fileToSave.getAbsolutePath());
                    }
                    break;
                case "Wczytaj graf":
                    wyszukiwanie.resetuj();
                    chooser = new JFileChooser();
                    filter = new FileNameExtensionFilter(
                            ".dat files", "dat");

                    chooser.addChoosableFileFilter(filter);

                    FileNameExtensionFilter filter3 = new FileNameExtensionFilter(
                            ".xml files", "xml");

                    chooser.addChoosableFileFilter(filter3);

                    returnVal = chooser.showOpenDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File fileToLoad = chooser.getSelectedFile();

                        if(fileToLoad.getAbsolutePath().endsWith(".dat"))
                        wyszukiwanie.star.load(fileToLoad.getAbsolutePath());
                        else if(fileToLoad.getAbsolutePath().endsWith(".xml"))
                            wyszukiwanie.star.loadXML(fileToLoad.getAbsolutePath());


                        // wyszukiwanie.star.save(fileToSave.getAbsolutePath() + ".dat" );
                    }
                    break;
                case "Zrób krok":

                    wyszukiwanie.oneStep();

                    break;
                case "Przeszukaj \n punkt":
                    wyszukiwanie.przeszukajJeden();
                    break;
                case "Wyszukaj":
                    wyszukiwanie.przeszukaj();
                    break;
                case "Resetuj algorytm":
                    wyszukiwanie.resetuj();
                    break;
                case "Ustawienia \n algorytmu":

                    wyszukiwanie.ustawienia();

                    break;


            }
        }
        catch (Exception e1){
            JOptionPane.showConfirmDialog(null,e1.getMessage(),"Błąd",JOptionPane.OK_OPTION);
        }


    }
}
