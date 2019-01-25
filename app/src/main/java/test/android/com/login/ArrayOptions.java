package test.android.com.login;

import java.util.ArrayList;

/**
 * Clase encargada de generar la lista de opciones de tificación en la creación de un nuevo itracker
 */
public class ArrayOptions {

    private static ArrayOptions ourInstance;
    private String status;
    private ArrayList<OptionObj> arrayList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<OptionObj> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<OptionObj> arrayList) {
        this.arrayList = arrayList;
    }

    public static ArrayOptions getInstance() {

        if (ourInstance == null){

            ourInstance = new ArrayOptions();

        }
        return ourInstance;
    }

    private ArrayOptions() {

        this.status = "empty";
        this.arrayList = new ArrayList<OptionObj>();


    }
}
