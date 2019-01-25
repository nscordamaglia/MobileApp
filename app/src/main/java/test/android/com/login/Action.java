package test.android.com.login;

/**
 * Clase abstracta de la cual van a extender las acciones que se pueden ejecutar en los diferentes servicios
 * tales como login, listing , etc
 * @author Nicolas Scordamaglia
 */
abstract class Action {

    public Action(Itracker s,TKTobj t) {


    }

    public Action(Itracker s){


    }




    void Ejecute(){};

    void Resend(){};



}