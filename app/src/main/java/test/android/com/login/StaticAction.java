package test.android.com.login;

/**
 * Created by Nicolas Scordamaglia on 11/07/2016.
 */
 
 /** Clase que permite guardar la ultima accion antes de un relogin */
public class StaticAction {
    private static StaticAction ourInstance = new StaticAction();
    private String xml;
    private String method;

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public static StaticAction getInstance() {
        if (ourInstance == null){

            ourInstance = new StaticAction();

        }
        return ourInstance;
    }

    private StaticAction() {
    }
}
