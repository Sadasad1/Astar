package PO;

import java.util.ArrayList;
import java.util.Scanner;

public class Menus {

    ArrayList<Inter> choices = new ArrayList<Inter>();

    String name;

    Object input;

    Menus(String t){
        name=t;
    }

    public void addChoice(Inter tmp){
        choices.add(tmp);
    }


    public String wyswietl(){
        String tmp="";
        for(int i=1;i<=choices.size();i++){
            tmp+=i+". "+choices.get(i-1).text+"\n";
        }
        return tmp;
    }

    public int act(Scanner sc){
        return sc.nextInt();
    }

}
