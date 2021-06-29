package PO;

import java.util.ArrayList;

public class Inter {

    public String text;
    Menus next;
    ArrayList<Object> targs = new ArrayList<Object>();
    Object target;

    Inter(String txt,Menus nexts){
        this.text=txt;
        this.next=nexts;
    }

    public void setTarget(Object target){
        this.target=target;
    }

    public void addTarget(Object targ){
        targs.add(targ);

    }

    public void setTargs(Object targ, int n){
        targs.set(n,targ);

    }

   public void Action(){

   }


}
