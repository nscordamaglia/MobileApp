package test.android.com.login;

import java.util.ArrayList;

/**
 * Clase que genera el objeto tkt para ser completado con sus atributos e insertado a la lista que corresponda
 */
public class TKTobj {

    private String id;
    private String FA;
    private String master;

    private String OpenTime;
    private String ClosedTime;

    private String Description;

    private String Status;
    private String equiposname;
    private String tipif;
    private ArrayList<EventObj> arrayevent = new ArrayList<EventObj>();
    private ArrayList<ActionObj> arrayactions = new ArrayList<ActionObj>();

    public ArrayList<EventObj> getArrayevent() {
        return arrayevent;
    }

    public void setArrayevent(ArrayList<EventObj> arrayevent) {
        this.arrayevent = arrayevent;
    }

    public ArrayList<ActionObj> getArrayactions() {
        return arrayactions;
    }

    public void setArrayactions(ArrayList<ActionObj> arrayactions) {
        this.arrayactions = arrayactions;
    }

    public String getTipif() {
        return tipif;
    }

    public void setTipif(String tipif) {
        this.tipif = tipif;
    }

    public String getEquiposname() {
        return equiposname;
    }

    public void setEquiposname(String equiposname) {
        this.equiposname = equiposname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFA() {
        return FA;
    }

    public void setFA(String FA) {
        this.FA = FA;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getOpenTime() {
        return OpenTime;
    }

    public void setOpenTime(String openTime) {
        OpenTime = openTime;
    }

    public String getClosedTime() {
        return ClosedTime;
    }

    public void setClosedTime(String closedTime) {
        ClosedTime = closedTime;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public TKTobj() {



    }




    private String caracterControl(String data){

        String replace = null;
        String[] caracter = {";","/","\\\\/","'","&","<",">","\""};
        for (int i=0;i<8;i++){
            if (";".equals(caracter[i])){

                replace = ".";

            }
            else if ("/".equals(caracter[i])){

                replace = "-";


            }else{

                replace = " ";

            }
            data = data.replace(caracter[i], replace);
            //System.out.println(data);
        }

        data = data.replaceAll("\\t", " ");
        data = data.replaceAll("\\n", ". ");
        data = data.replaceAll("\\r", ". ");
        return data;
    }

    public void clearEvents() {

        setArrayevent(new ArrayList<EventObj>());
        setArrayactions(new ArrayList<ActionObj>());
    }
}
