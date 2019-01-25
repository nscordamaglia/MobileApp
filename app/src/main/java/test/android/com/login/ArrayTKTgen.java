package test.android.com.login;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Clase que crea la lista de tickets en el inbox de generados
 */
public class ArrayTKTgen {
    private static ArrayTKTgen ourInstance;
    private ArrayList<TKTobj> arrayList ;
    private ArrayList<TKTobj> arrayListInv ;
    private String status;
    private String tktOutside;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTktOutside() {
        return tktOutside;
    }

    public void setTktOutside(String tktOutside) {
        this.tktOutside = tktOutside;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<TKTobj> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<TKTobj> arrayList) {
        this.arrayList = arrayList;
    }

    public static ArrayTKTgen getInstance() {

        if (ourInstance == null){

            ourInstance = new ArrayTKTgen();

        }
        return ourInstance;
    }

    private ArrayTKTgen() {

        this.status = "empty";
        this.arrayList = new ArrayList<TKTobj>();
        this.arrayListInv = new ArrayList<TKTobj>();
        Calendar c = Calendar.getInstance();
        this.date = c.getTime();



    }

    public ArrayList<TKTobj> getArrayListInv() {

        for (int i = arrayList.size()-1; i >=0; i--){

            Log.d("itracker",arrayList.get(i).getId());
            arrayListInv.add(arrayList.get(i));

        }

        arrayList = arrayListInv;
        return arrayList;
    }
}
