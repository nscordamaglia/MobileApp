package test.android.com.login;

import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;

import java.util.ArrayList;

/**
 * Clase que crea el perfil de usuario y completa otros atributos importantes
 * para ser utilizados a lo largo del ciclo de vida de la app
 */
public class User {
    private static User ourInstance = new User();
    private String UserName;
    private String pass;
    private ArrayList<Integer> ListTeam;
    private ArrayList<Integer> ListTeamFilter;
    private ArrayList<String> ListTeamName;
    private ArrayList<Integer> ListTeamGen;
    private ArrayList<Integer> ListTeamGenFilter;
    private ArrayList<String> ListTeamGenName;
    private String hash;
    private String loginStatus = "nologin";
    private ActionBarDrawerToggle toogle;
    private ArrayList<String> access;
    private Boolean attach = false;
    private String base64;
    private String report;
    private String path;
    private String back;

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public Boolean getAttach() {
        return attach;
    }

    public void setAttach(Boolean attach) {
        this.attach = attach;
    }

    public ArrayList<String> getAccess() {
        return access;
    }

    public void setAccess(ArrayList<String> access) {
        this.access = access;
    }

    public ArrayList<Integer> getListTeamGen() {
        return ListTeamGen;
    }

    public void setListTeamGen(ArrayList<Integer> listTeamGen) {
        ListTeamGen = listTeamGen;
    }

    public ArrayList<Integer> getListTeamGenFilter() {
        return ListTeamGenFilter;
    }

    public void setListTeamGenFilter(ArrayList<Integer> listTeamGenFilter) {
        ListTeamGenFilter = listTeamGenFilter;
    }

    public ArrayList<String> getListTeamGenName() {
        return ListTeamGenName;
    }

    public void setListTeamGenName(ArrayList<String> listTeamGenName) {
        ListTeamGenName = listTeamGenName;
    }

    public ActionBarDrawerToggle getToogle() {
        return toogle;
    }

    public void setToogle(ActionBarDrawerToggle toogle) {
        this.toogle = toogle;
    }

    public ArrayList<Integer> getListTeamFilter() {
        return ListTeamFilter;
    }

    public void setListTeamFilter(ArrayList<Integer> listTeamFilter) {
        ListTeamFilter = listTeamFilter;
    }

    public ArrayList<String> getListTeamName() {
        return ListTeamName;
    }

    public void setListTeamName(ArrayList<String> listTeamName) {
        ListTeamName = listTeamName;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }



    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public ArrayList<Integer> getListTeam() {
        return ListTeam;
    }

    public void setListTeam(ArrayList<Integer> listTeam) {
        ListTeam = listTeam;
    }

    public static User getInstance() {

        if (ourInstance == null){

            ourInstance = new User();

        }
        return ourInstance;

    }

    private User() {

        this.hash = null;
        this.ListTeam = new ArrayList<Integer>();
        this.ListTeamFilter = new ArrayList<Integer>();
        this.ListTeamName = new ArrayList<String>();
        this.ListTeamGen = new ArrayList<Integer>();
        this.ListTeamGenFilter = new ArrayList<Integer>();
        this.ListTeamGenName = new ArrayList<String>();
        this.path = "0";
        this.back = "0";


    }

    public String getListTeamToString() {

        String result = "0";
        if  (ListTeamFilter.size()==0 || ListTeamFilter.isEmpty()){

            return result;

        }else {


            for (int i = 0; i < ListTeamFilter.size(); i++) {

                result += ListTeamFilter.get(i) + ",";

            }
            Log.d("lenght listeam",result +"/"+result.substring(0,result.length()-1));

            return result.substring(0,result.length()-1);
        }
    }
    public String getListTeamGenToString() {


        String result = "0";
        if  (ListTeamGenFilter.size()==0 || ListTeamGenFilter.isEmpty()){

            Log.d("idteamlog", "entra por 0 " + ListTeamGenFilter.size());
            return result;

        }else {
            Log.d("idteamlog", "lenght: " + ListTeamGenFilter.size());

            for (int i = 0; i < ListTeamGenFilter.size(); i++) {

                result += ListTeamGenFilter.get(i) + ",";

            }

            Log.d("lenght idteam",result +"/"+result.substring(0,result.length()-1));
            return result.substring(0, result.length() - 1);
        }
    }

    public ArrayList<String> AccessToList(String a) {

        String[] access = a.split(",");
        ArrayList<String> arrayAccess = new ArrayList<String>();

        for (int i = 0;i<access.length; i++){

            arrayAccess.add(access[i]);
            Log.d("access",arrayAccess.get(i));

        }

        return arrayAccess;
    }
}
