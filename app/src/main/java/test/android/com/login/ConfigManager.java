package test.android.com.login;


import android.util.Log;

/**
 * Clase que contiene variables estáticas que utiliza recurrentemente la app y son parte de la estructura
 */
public class ConfigManager {


/** url API **/

    final static String urlwebApp="https://example.com/api";



    //****************LOGIN****************
    final static String editHeaderLOGIN=";user;login";
    final static String createNodesLOGIN="user;pass";
    //****************LOGOUT****************
    final static String editHeaderLOGOUT=";user;logout";
    //****************PROFILE*****************
    final static String editHeaderPROFILE="hash;user;idsel_listteams";
    final static String createNodesPROFILE="L;m;htmlid;multiple;sel_params";
    final static String editNodesDataPROFILE="mytkts;menu;txt_filtro_equipo;true;{\"filter\":\"staffhome_vista\"}";
    //*******************LISTING**********************
    final static String editHeaderLISTING="hash;tkt;listteam";
    final static String createNodesLISTING="L;m;team;filter";
    final static String editNodesDataLISTING="staffhome;menu;UserTeamResult;myNtom";
    //********************ACTION************************
    final static String editHeaderACTION="hash;action;ejecute";
    final static String createNodesACTION="L;sendfiles;idtkt;action;form";
    final static String editNodesDataACTION="staffhome;true;";
    //*******************EVENT********************************
    final static String editHeaderEVENT="hash;tkt;geth";
    final static String createNodesEVENT="L;id;";
    final static String editNodesDataEVENT="mytkts;idtkt";
    //********************FORM***********************************
    final static String editHeaderFORM="hash;action;getform";
    final static String createNodesFORM="L;m;idtkt;action";
    final static String editNodesDataFORM="staffhome;menu;";
    //*********************SELECT******************************
    final static String editHeaderSELECT="hash;tkt;idsel_teamderive";
    final static String createNodesSELECT="L;m;htmlid;multiple;sel_params";
    final static String editNodesDataSELECT="staffhome;menu;actionform_idequipo;false;";
    //*********************IDTEAMS******************************
    final static String editHeaderIDTEAMS="hash;user;idsel_listteams";
    final static String createNodesIDTEAMS="L;m;htmlid;multiple;sel_params";
    final static String editNodesDataIDTEAMS="mytkts;menu;txt_filtro_equipo;true;{\"filter\":\"mytkts_vista\"}";
    //*********************GENERATED******************************
    final static String editHeaderGENERATED="hash;tkt;listmyteams";
    final static String createNodesGENERATED="L;m;status;teams";
    final static String editNodesDataGENERATED="mytkts;menu;open;teams";
    //********************NEW******************************************
    final static String editHeaderNEW="hash;tkt;get_tree_options";
    final static String createNodesNEW="L;path";
    final static String editNodesDataNEW="newtkt;path";
    //*******************DOWNLOAD**********************************
    final static String editHeaderDOWN="hash;tkt;downloadfile";
    final static String createNodesDOWN="type;file";
    final static String editNodesDataDOWN="type;file";
    //********************CREATE******************************************
    final static String editHeaderCREATE="hash;action;ejecute";
    final static String createNodesCREATE="L;sendfiles;action;path;form";
    final static String editNodesDataCREATE="newtkt;true;abrir;path";



    final static String xml ="<?xml version=\"1.0\" encoding=\"iso-8859-15\"?>" +
            "<webApp>" +
            "  <header>" +
            "    <front>API_DMZ</front>" +
            "    <usr>{user}</usr>" +
            "    <instance>AGENTES</instance>" +
            "    <hash>{hash}</hash>" +
            "   <ip>::1</ip>" +
            "  </header>" +
            "  <request>" +
            "    <class>{class}</class>" +
            "    <method>{method}</method>" +
            "    <files></files>" +
            "    <params>" +
            "    </params>" +
            "  </request>" +
            "</webApp>";

    public static String getEditHeaderLOGOUT() {
        return editHeaderLOGOUT;
    }

    public static String getEditHeaderCREATE() {
        return editHeaderCREATE;
    }

    public static String getCreateNodesCREATE() {
        return createNodesCREATE;
    }

    public static String getEditNodesDataCREATE() {
        return editNodesDataCREATE;
    }

    public static String getEditHeaderDOWN() {
        return editHeaderDOWN;
    }

    public static String getCreateNodesDOWN() {
        return createNodesDOWN;
    }

    public static String getEditNodesDataDOWN() {
        return editNodesDataDOWN;
    }

    public static String getEditHeaderNEW() {
        return editHeaderNEW;
    }

    public static String getCreateNodesNEW() {
        return createNodesNEW;
    }

    public static String getEditNodesDataNEW() {
        return editNodesDataNEW;
    }

    public static String getEditHeaderIDTEAMS() {
        return editHeaderIDTEAMS;
    }

    public static String getCreateNodesIDTEAMS() {
        return createNodesIDTEAMS;
    }

    public static String getEditNodesDataIDTEAMS() {
        return editNodesDataIDTEAMS;
    }

    public static String getEditHeaderGENERATED() {
        return editHeaderGENERATED;
    }

    public static String getCreateNodesGENERATED() {
        return createNodesGENERATED;
    }

    public static String getEditNodesDataGENERATED() {
        return editNodesDataGENERATED;
    }

    public static String getEditHeaderSELECT() {
        return editHeaderSELECT;
    }

    public static String getCreateNodesSELECT() {
        return createNodesSELECT;
    }

    public static String getEditNodesDataSELECT() {
        return editNodesDataSELECT;
    }

    public static String getEditHeaderFORM() {
        return editHeaderFORM;
    }

    public static String getCreateNodesFORM() {
        return createNodesFORM;
    }

    public static String getEditNodesDataFORM() {
        return editNodesDataFORM;
    }

    public static String getUrlwebApp() {
        return urlwebApp;
    }



    public static String getEditHeaderLOGIN() {

        return editHeaderLOGIN;
    }

    public static String getCreateNodesLOGIN() {

        return createNodesLOGIN;
    }

    public static String getEditHeaderLISTING() {

        return editHeaderLISTING;
    }

    public static String getCreateNodesLISTING() {

        return createNodesLISTING;
    }

    public static String getEditNodesDataLISTING() {

        return editNodesDataLISTING;
    }

    public static String getEditHeaderACTION() {

        return editHeaderACTION;
    }

    public static String getCreateNodesACTION() {

        return createNodesACTION;
    }

    public static String getEditNodesDataACTION()
    {
        return editNodesDataACTION;
    }

    public static String getEditHeaderPROFILE() {
        return editHeaderPROFILE;
    }

    public static String getCreateNodesPROFILE() {
        return createNodesPROFILE;
    }

    public static String getEditNodesDataPROFILE() {
        return editNodesDataPROFILE;
    }

    public static String getXml() {

        return xml;
    }

    public static String getEditHeaderEVENT() {
        return editHeaderEVENT;
    }

    public static String getCreateNodesEVENT() {
        return createNodesEVENT;
    }

    public static String getEditNodesDataEVENT() {
        return editNodesDataEVENT;
    }


}


