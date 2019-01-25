package test.android.com.login;

/**
 * Clase encargada de guardar la hash de conexión
 */
public class Hash {

    String value;



    public Hash(){

        this.value = null;

    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
