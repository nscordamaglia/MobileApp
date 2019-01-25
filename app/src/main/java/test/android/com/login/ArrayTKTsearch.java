package test.android.com.login;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Clase que crea una lista auxiliar para la busqueda de tickets por número
 */
public class ArrayTKTsearch {
    private static ArrayTKTsearch ourInstance;
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

    public static ArrayTKTsearch getInstance() {

        if (ourInstance == null){

            ourInstance = new ArrayTKTsearch();

        }
        return ourInstance;
    }

    private ArrayTKTsearch() {

        this.status = "empty";
        this.arrayList = new ArrayList<TKTobj>();
        this.arrayListInv = new ArrayList<TKTobj>();
        Calendar c = Calendar.getInstance();
        this.date = c.getTime();



    }

    public ArrayList<TKTobj> getArrayListInv() {

        for (int i = arrayList.size()-1; i >=0; i--){

            Log.d("webApp",arrayList.get(i).getId());
            arrayListInv.add(arrayList.get(i));

        }

        arrayList = arrayListInv;
        return arrayList;
    }
}
