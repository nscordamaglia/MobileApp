package test.android.com.login;

import android.util.Log;

/**
 * Clase que arma los nodos que van a ser utilizados en la estructura del XML para armar la consulta a la api
 */
public class DataNodes {

    String editHeader; // hash, class, method
    String createNodes; // lista de nodos segun sea la accion
    String editNodesData; // completar los nodos creados
    String method;
    String txtform;
    String action;
    String user;
    String pass;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEditHeader() {
        return editHeader;
    }

    public void setEditHeader(String editHeader) {
        this.editHeader = editHeader;
    }

    public String getCreateNodes() {
        return createNodes;
    }

    public void setCreateNodes(String createNodes) {
        this.createNodes = createNodes;
    }

    public String getEditNodesData() {
        return editNodesData;
    }

    public void setEditNodesData(String editNodesData) {
        this.editNodesData = editNodesData;
    }

    public String getTxtform() {
        return txtform;
    }

    public void setTxtform(String txtform) {
        this.txtform = txtform;
    }



    /**
     * Constructor basico
     * @param method
     */
    DataNodes(String method, TKTobj tkt) {

        this.method = method;
        if ("LOGIN".equalsIgnoreCase(method)) {


            this.editHeader = ConfigManager.getEditHeaderLOGIN();
            this.createNodes = ConfigManager.getCreateNodesLOGIN();
            this.editNodesData = user + ";" + pass;


        } else if ("PROFILE".equalsIgnoreCase(method)) {

            this.editHeader = ConfigManager.getEditHeaderPROFILE();
            this.createNodes = ConfigManager.getCreateNodesPROFILE();
            this.editNodesData = ConfigManager.getEditNodesDataPROFILE();

        } else if ("IDTEAMS".equalsIgnoreCase(method)) {

            this.editHeader = ConfigManager.getEditHeaderIDTEAMS();
            this.createNodes = ConfigManager.getCreateNodesIDTEAMS();
            this.editNodesData = ConfigManager.getEditNodesDataIDTEAMS();

        }else if ("LISTING".equalsIgnoreCase(method)) {

            this.editHeader = ConfigManager.getEditHeaderLISTING();
            this.createNodes = ConfigManager.getCreateNodesLISTING();
            this.editNodesData = ConfigManager.getEditNodesDataLISTING().replace("UserTeamResult", User.getInstance().getListTeamToString());


        } else if ("ACTION".equalsIgnoreCase(method)) {


            this.editHeader = ConfigManager.getEditHeaderACTION();
            this.createNodes = ConfigManager.getCreateNodesACTION();
            this.editNodesData = ConfigManager.getEditNodesDataACTION() + tkt.getId() + ";";


        } else if ("EVENT".equalsIgnoreCase(method)) {

            this.editHeader = ConfigManager.getEditHeaderEVENT();
            this.createNodes = ConfigManager.getCreateNodesEVENT();
            this.editNodesData = ConfigManager.getEditNodesDataEVENT().replace("idtkt", tkt.getId());


        } else if ("FORM".equalsIgnoreCase(method)) {

            this.editHeader = ConfigManager.getEditHeaderFORM();
            this.createNodes = ConfigManager.getCreateNodesFORM();
            this.editNodesData = ConfigManager.getEditNodesDataFORM() + tkt.getId() + ";";


        } else if ("SELECT".equalsIgnoreCase(method)) {

            this.editHeader = ConfigManager.getEditHeaderSELECT();
            this.createNodes = ConfigManager.getCreateNodesSELECT();
            this.editNodesData = ConfigManager.getEditNodesDataSELECT() + "{\"idtkt\":\"" + tkt.getId() + "\"}";
        } else if ("GENERATED".equalsIgnoreCase(method)){

            this.editHeader = ConfigManager.getEditHeaderGENERATED();
            this.createNodes = ConfigManager.getCreateNodesGENERATED();
            this.editNodesData = ConfigManager.getEditNodesDataGENERATED().replace("teams", User.getInstance().getListTeamGenToString());
            Log.d("idteam",editNodesData);

        }else if ("NEW".equalsIgnoreCase(method)){

            String path;
            this.editHeader = ConfigManager.getEditHeaderNEW();
            this.createNodes = ConfigManager.getCreateNodesNEW();
            if (User.getInstance().getPath().equalsIgnoreCase("0")){
                Log.d("logpath","es cero: " + User.getInstance().getPath());
                path = "";
            }else{

                Log.d("logpath","no es cero: " + User.getInstance().getPath());
                path = User.getInstance().getPath();
            }
            this.editNodesData = ConfigManager.getEditNodesDataNEW().replace("path",path);

        }else if("DOWNLOAD".equalsIgnoreCase(method)){

            this.editHeader = ConfigManager.getEditHeaderDOWN();
            this.createNodes = ConfigManager.getCreateNodesDOWN();
            this.editNodesData = ConfigManager.getEditNodesDataDOWN().replace("file",FormObj.getInstance().getFile());
            this.editNodesData = editNodesData.replace("type",FormObj.getInstance().getTypedown());

        }else if ("CREATE".equalsIgnoreCase(method)) {

            this.editHeader = ConfigManager.getEditHeaderCREATE();
            this.createNodes = ConfigManager.getCreateNodesCREATE();
            this.editNodesData = ConfigManager.getEditNodesDataCREATE().replace("path", User.getInstance().getPath());
        }
    }
}

