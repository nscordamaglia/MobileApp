package test.android.com.login;

import java.util.ArrayList;

/**
 * Clase que da forma a la estructura de un evento en itracker
 */
public class EventObj {


    private String alias;
    private String date;
    private String value;
    private ArrayList<ElementObj> arrayelements = new ArrayList<ElementObj>();
    private ArrayList<ActObj> arrayactions = new ArrayList<ActObj>();
    private ArrayList<String> vAttach = new ArrayList<String>();


    public ArrayList<String> getvAttach() {
        return vAttach;
    }

    public void setvAttach(ArrayList<String> vAttach) {
        this.vAttach = vAttach;
    }

    public ArrayList<ActObj> getArrayactions() {
        return arrayactions;
    }

    public void setArrayactions(ArrayList<ActObj> arrayactions) {
        this.arrayactions = arrayactions;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ArrayList<ElementObj> getArrayelements() {
        return arrayelements;
    }

    public void setArrayelements(ArrayList<ElementObj> arrayelements) {
        this.arrayelements = arrayelements;
    }


}
