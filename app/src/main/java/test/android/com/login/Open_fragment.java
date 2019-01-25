package test.android.com.login;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;


import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;


/**
 * Clase que genera la UI de creación de un nuevo ticket
 */
public class Open_fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,RQcomplete<String>{


    private View view;
    private ListView lv1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Request rq;
    private webApp webApp;
    private ArrayList<OptionObj> list;
    private ListOptionAdapter adapter;
    private AppCompatActivity activity;
    private ActionBar actionBar;
    private String encodedImage;
    private CameraPhoto cameraPhoto;
    private final int CAMERA_REQUEST = 13323;
    private ProgressBar progressbar;


    @Override
    /**
     * Metodo que recibe la respuesta de procesos asincrónicos
     */
    public void onTaskComplete(String response, String method) {

        GetResponse parser = new GetResponse();
        try {
            if ("ejecute".equalsIgnoreCase(method)){
                Log.d("getaction","callback en lst_fragment");
            }
            parser.execute(getContext(),response,method);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if("error".equals(parser.getResponse().substring(0,5))) {

            String error = parser.getResponse().substring(6,parser.getResponse().length());
            switch (error){

                case "emptylist":
                    Snackbar snackbar1 = Snackbar
                            .make(view.findViewById(R.id.layout_open_fragment), parser.getResponse(), Snackbar.LENGTH_LONG);


                    snackbar1.show();
                    User.getInstance().setLoginStatus("OK");
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case "Parametros invalidos en la solicitud||Sesion invalida o vencida218":
                    webApp.setUser(User.getInstance().getUserName());
                    webApp.setPass(User.getInstance().getPass());


                    /** Login **/
                    Login login = new Login(webApp, null);
                    login.Ejecute();

                    break;
                case "firewall":

                    Snackbar snackbar3 = Snackbar
                            .make(view.findViewById(R.id.layout_tkt_fragment), "Fallo inesperado", Snackbar.LENGTH_LONG)
                            .setAction("REPORTAR", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    sendMail(view,User.getInstance().getReport());
                                }
                            });


                    snackbar3.show();
                    User.getInstance().setLoginStatus("firewall");
                    swipeRefreshLayout.setRefreshing(false);

                    break;
                default:
                    swipeRefreshLayout.setRefreshing(false);
                    Snackbar snackbar2 = Snackbar
                            .make(view.findViewById(R.id.layout_open_fragment), parser.getResponse(), Snackbar.LENGTH_LONG);


                    snackbar2.show();
                    break;

            }


        }else {

            if ("login".equalsIgnoreCase(method)) {

                Log.d("relog", "entro por afterlog");
                //tengo que reenviar la accion anterior al login
                afterLogin();
                //GetAction action = new GetAction(webApp);
                //action.Resend();


            } else if (!"downloadfile".equalsIgnoreCase(method)) {


                Log.d("tagback", "path: " + User.getInstance().getPath() + " back: " + User.getInstance().getBack());
                // Getting adapter by passing xml data ArrayList
                list = new ArrayList<>();
                list = ArrayOptions.getInstance().getArrayList();

                Log.d("webApp", "status: " + ArrayTKT.getInstance().getStatus());

                TextView optTitle = (TextView) view.findViewById(R.id.titleOpt);
                optTitle.setVisibility(View.VISIBLE);
                if (list.isEmpty()) {


                    //Muestro formulario de apertura

                    optTitle.setVisibility(View.INVISIBLE);
                    if (FormObj.getInstance().isSended() == false) {

                        FormObj.getInstance().setSending(true);
                        //Dibujo formulario de creacion de tkt
                        view.findViewById(R.id.openform_loyout).setVisibility(View.VISIBLE);
                        FormObj.getInstance().setRq(this);
                        FormObj.getInstance().WriteForm(view, this);
                        final Boolean[] collapse = {false};

                        if (FormObj.getInstance().getFabMenu() == true){

                            cameraPhoto = new CameraPhoto(view.getContext());
                            //muestro menu de adjntos
                            final FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.open_fab_attach_over);
                            floatingActionButton.setVisibility(View.VISIBLE);

                            final FloatingActionsMenu floatingActionsMenu = (FloatingActionsMenu) view.findViewById(R.id.open_fab_attach);
                            floatingActionsMenu.setVisibility(View.VISIBLE);
                            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6cc477")));
                            floatingActionsMenu.collapse();

                            final com.getbase.floatingactionbutton.FloatingActionButton delete = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.delete_attach);
                            com.getbase.floatingactionbutton.FloatingActionButton file = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.file_attach);
                            com.getbase.floatingactionbutton.FloatingActionButton camera = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.camera_attach);

                            //Abrir y cerrar menu
                            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(collapse[0] ==false){
                                        floatingActionsMenu.expand();
                                        collapse[0] = true;
                                    }else{

                                        floatingActionsMenu.collapse();
                                        collapse[0] = false;
                                    }

                                }
                            });
                            //Borrar adjunto
                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    User.getInstance().setAttach(false);
                                    floatingActionsMenu.collapse();
                                    delete.setVisibility(View.GONE);

                                    Toast.makeText(activity.getApplicationContext(), "Archivo adjunto eliminado", Toast.LENGTH_LONG).show();
                                }
                            });
                            //Adjuntar archivo
                            file.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent fileintent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                    fileintent.setType("*/*");
                                    fileintent.addCategory(Intent.CATEGORY_OPENABLE);



                                    try {
                                        startActivityForResult(fileintent,1212,null);
                                    } catch (ActivityNotFoundException e) {
                                        Log.e("tag", "No activity can handle picking a file. Showing alternatives.");
                                    }
                                }
                            });
                            //Capturar desde camara
                            camera.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    // Comprobar permiso
                                    int permissionStorage = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                    int permissionCamera = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CAMERA);

                                    if (permissionStorage == PackageManager.PERMISSION_GRANTED && permissionCamera == PackageManager.PERMISSION_GRANTED) {
                                        try {
                                            startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {


                                        // Solicitar el permiso
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 0);



                                    }
                                }
                            });
                        }
                        //fin muestro menu
                    } else {
                        FormObj.getInstance().setSended(false);
                        FormObj.getInstance().setSending(false);
                        User.getInstance().setBack("0");
                        User.getInstance().setPath("0");
                        Toast.makeText(activity.getApplicationContext(), "Se cargó correctamente el ticket " + parser.getResponse(), Toast.LENGTH_LONG).show();
                        activity.getSupportFragmentManager().popBackStackImmediate();

                    }

                }

                adapter = new ListOptionAdapter(view, list);
                lv1.setAdapter(adapter);
                lv1.setEnabled(true);

                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                if (getActivity() != null) {

                    getActivity().findViewById(R.id.transfer).setVisibility(View.GONE);
                    Log.d("imsettings", "oculta imagen");
                } else {

                    Log.d("imsettings", "getactivity null");

                }

            }else{
                Log.d("filelog", "creo el archivo, lenght: " + User.getInstance().getBase64().length());
                FileOutputStream fos = null;
                File filePath = null;
                try {
                    if (User.getInstance().getBase64() != null) {
                        //fos = activity.getApplicationContext().openFileOutput("adjunto.xlx", activity.getApplicationContext().MODE_PRIVATE);
                        filePath = new File("/mnt/sdcard/download//adjunto.xls");
                        fos = new FileOutputStream(filePath,true);
                        byte[] decodedString = android.util.Base64.decode(User.getInstance().getBase64(), android.util.Base64.DEFAULT);
                        fos.write(decodedString);
                        fos.flush();
                        fos.close();
                    }

                } catch (Exception e) {

                } finally {
                    if (fos != null) {
                        fos = null;
                    }
                }
                openXLS();
            }
        }

    }

    private void afterLogin() {

         /* Listing */
        GetNew options = new GetNew(webApp);
        options.Resend();
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        activity = (AppCompatActivity) getActivity();
        actionBar = activity.getSupportActionBar();
        // update the actionbar to show the up carat/affordance
        if(actionBar == null){Log.d("actionbar","actionbar null");}
        actionBar.setTitle("Nuevo webApp");
        User.getInstance().getToogle().setDrawerIndicatorEnabled(false);
        //User.getInstance().getToogle().setHomeAsUpIndicator(android.R.drawable.ic_delete);
        User.getInstance().getToogle().setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.getInstance().setPath("0");
                User.getInstance().setAttach(false);
                activity.onBackPressed();
            }
        });




        FloatingActionsMenu menu = (FloatingActionsMenu)activity.findViewById(R.id.fab);
        menu.setVisibility(View.INVISIBLE);

        int count = activity.getSupportFragmentManager().getBackStackEntryCount();
        Log.d("backbutton", "fragment: " + count);

        TextView text = (TextView)view.findViewById(R.id.htext);
        String htext = "";
        int lenght = FormObj.getInstance().getArrayHistoric().size();

        for (int i = 0; i<lenght; i++){

            htext += FormObj.getInstance().getArrayHistoric().get(i) + "->";
            Log.d("tagback","onresume");
        }

        text.setText(htext.substring(0,htext.length()-2));


    }

    public Open_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.open_fragment, container, false);
        if (getActivity().findViewById(R.id.transfer)!=null){getActivity().findViewById(R.id.transfer).setVisibility(View.VISIBLE); Log.d("imsettings","oncreate");}
        lv1 =(ListView)view.findViewById(R.id.listOptions);




        setHasOptionsMenu(true);

        // Set swipe
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        updateOptions();

                                    }
                                }
        );


        // item onclick event
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {



                swipeRefreshLayout.setRefreshing(true);
                lv1.setEnabled(false);
                View v = view;
                TextView t = (TextView) v.findViewById(R.id.opt);
                String path = t.getHint().toString();
                Log.d("option",path);
                User.getInstance().setPath(path);
                Log.d("tagback","path: " + User.getInstance().getPath() + " back: " + User.getInstance().getBack());
                GetNew options = new GetNew(webApp,null);
                options.setDatanodes(new DataNodes("NEW",null));
                options.Ejecute();
                String back = t.getText().toString();
                WriteHistoric(back);

            }
        });

        return view;
    }

    private View WriteHistoric(String back) {


        String htext = "";
        final TextView text = (TextView)view.findViewById(R.id.htext);
        text.setSelected(true);
        int lenght = FormObj.getInstance().getArrayHistoric().size();
        if (FormObj.getInstance().getArrayHistoric().get(0).equalsIgnoreCase("Ruta de creación")){

                htext = back;
                FormObj.getInstance().getArrayHistoric().clear();
                FormObj.getInstance().getArrayHistoric().add(htext);


        }else{


            FormObj.getInstance().getArrayHistoric().add(back);
            for (int i = 0; i<FormObj.getInstance().getArrayHistoric().size(); i++){

                htext += FormObj.getInstance().getArrayHistoric().get(i) + "->";

            }

            htext = htext.substring(0,htext.length()-2);

        }

        text.setText(htext);
        LinearLayout rel = (LinearLayout) view.findViewById(R.id.history);
        rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text.startAnimation(AnimationUtils.loadAnimation(view.getContext(),R.anim.move));
            }
        });

        return view;
    }

    private void updateOptions() {

        rq = new Request(this);
        webApp = new webApp(this.getContext(), rq, null);

        /* Listing */
        GetNew options = new GetNew(webApp,null);
        options.Ejecute();
    }

    @Override
    public void onRefresh() {

        updateOptions();
    }


    private void openXLS(){
        File xls = new File("/mnt/sdcard/download//adjunto.xls");
        Uri path = Uri.fromFile(xls);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(path, "application/vnd.ms-excel");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            activity.getApplicationContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity.getApplicationContext(), "No existe aplicación para ver XLS", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.d("filelog", "requestcode: " + requestCode + "/ data: " + data + "/ resultcode: " + resultCode);
        // TODO Fix no activity available


        switch (requestCode) {
            case 1212:
                if (resultCode == -1) {
                    Uri selectedImageURI = data.getData();
                    File file = new File(getRealPathFromURI(selectedImageURI));
                    Log.d("filelog", data.getData().toString());
                    try {
                        getByte(selectedImageURI);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }else{

                    if (data == null) {

                        FloatingActionsMenu floatingActionsMenu = (FloatingActionsMenu) view.findViewById(R.id.open_fab_attach);
                        floatingActionsMenu.collapse();
                        Toast.makeText(activity.getApplicationContext(), "No se ha adjuntado ningún archivo", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                break;
            case 13323:
                if (resultCode == activity.RESULT_OK){


                    String photopath = cameraPhoto.getPhotoPath();
                    Bitmap bitmap = null;

                    try {
                        bitmap = ImageLoader.init().from(photopath).requestSize(512,512).getBitmap();
                        encodedImage = ImageBase64.encode(bitmap);
                        User.getInstance().setAttach(true);
                        User.getInstance().setBase64(encodedImage);
                        Toast.makeText(activity.getApplicationContext(), "Archivo adjuntado correctamente", Toast.LENGTH_LONG).show();
                        com.getbase.floatingactionbutton.FloatingActionButton delete = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.delete_attach);
                        delete.setVisibility(View.VISIBLE);
                        FloatingActionsMenu floatingActionsMenu = (FloatingActionsMenu) view.findViewById(R.id.open_fab_attach);
                        floatingActionsMenu.collapse();


                        Log.d("uriimg","entra por try");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Log.d("uriimg","entra por catch");
                    }
                }
                break;
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void getByte(Uri uri) throws IOException{

        InputStream is = activity.getContentResolver().openInputStream(uri);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        byte bytes[] =  buffer.toByteArray();

        User.getInstance().setAttach(true);
        User.getInstance().setBase64(Base64.encodeToString(bytes,0));
        Log.d("filelog", User.getInstance().getBase64());
        Toast.makeText(activity.getApplicationContext(), "Archivo adjuntado correctamente", Toast.LENGTH_LONG).show();



        com.getbase.floatingactionbutton.FloatingActionButton delete = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.delete_attach);
        delete.setVisibility(View.VISIBLE);
        FloatingActionsMenu floatingActionsMenu = (FloatingActionsMenu) view.findViewById(R.id.open_fab_attach);
        floatingActionsMenu.collapse();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){

        if (requestCode == 0){

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){

                try {
                    startActivityForResult(FormObj.getInstance().getCameraPhoto().takePhotoIntent(), CAMERA_REQUEST);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    private void sendMail(View view, String report){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL,new String[] {"webAppmobile@domain.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Consulta");
        intent.putExtra(Intent.EXTRA_TEXT, "Consulta enviada por: " + User.getInstance().getUserName() + "\n"+  report);

        startActivity(Intent.createChooser(intent, "Envio de mail"));
        User.getInstance().setReport(null);

    }
}
