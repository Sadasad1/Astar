package PO;

import javax.swing.*;

public class Rules {


    String[] choices = {"Droga zwykła", "Autostrada"};

    Object[] boxes = new JComponent[choices.length + 1];

    Rules(){
        JLabel label = new JLabel("Uwzględniaj:");
        boxes[0] = label;

        for (int i = 1; i < boxes.length ; i++) {
            boxes[i] = new JCheckBox(choices[i-1]);

            ((JCheckBox) boxes[i]).setSelected(true);


        }
    }

    public Object[] getBoxes(){
        return boxes;
    }

}
