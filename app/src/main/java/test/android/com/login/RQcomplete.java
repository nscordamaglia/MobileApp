package test.android.com.login;

/**
 * Interface para el tratamiento de procesos asíncronos
 */
public interface RQcomplete<T> {

    public void onTaskComplete (String r, String m);
}
