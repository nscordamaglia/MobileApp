package test.android.com.login;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Clase que crea el array de eventos de un evento o formulario
 */
public class ArrayEvent {
    private static ArrayEvent ourInstance;
    private  String status;
    private  ArrayList<String> arrayEvent;
    private Date date;
    private String origin;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getArrayEvent() {
        return arrayEvent;
    }

    public void setArrayEvent(ArrayList<String> arrayEvent) {
        this.arrayEvent = arrayEvent;
    }

    public static ArrayEvent getInstance() {

        if (ourInstance == null){

            ourInstance = new ArrayEvent();

        }
        return ourInstance;
    }



    private ArrayEvent() {

        this.status = "empty";
        this.arrayEvent = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        this.date = c.getTime();




    }
}
