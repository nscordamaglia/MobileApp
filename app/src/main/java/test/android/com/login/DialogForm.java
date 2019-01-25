package test.android.com.login;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Clase que muestra en pantalla el formulario de acción dentro del ticket
 */
public class DialogForm extends DialogFragment implements RQcomplete<String>{

    private TKTobj tkt;
    private EditText input;
    private Spinner spinner,select;
    private Button send;
    private TextView label;
    private EditText inputlong;
    private ArrayList<String> arrayForm = new ArrayList<String>();
    private Dialog dialog;
    private Context context;
    private AppCompatActivity activity;
    private String encodedImage;
    private CameraPhoto cameraPhoto;
    private final int CAMERA_REQUEST = 13323;
    private LinearLayout attachLayout;
    private View v;
    private Boolean attachReady = false;
    private Button buttonAdj,buttonCap;
    private EditText date;
    private RQcomplete<String> cb;
    //variable de control de desplegables
    int namesel = 0;
    int idsel = 0;

    @Override
    public void onTaskComplete(String response, String m) {

        try {

            date.setText(response);
            EditText next = (EditText) date.focusSearch(View.FOCUS_DOWN);
            next.requestFocus();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop(){

        super.onStop();
        Log.d("itracker","saliendo...");
        User.getInstance().setAttach(false);
    }
    @Override
    public void onResume() {
        super.onResume();


        final FloatingActionButton floatingActionButton = (FloatingActionButton) v.findViewById(R.id.attach_fab);

        if (User.getInstance().getAttach()== true && attachReady == false){

            attachReady = true;

            final Drawable icon = getResources().getDrawable(R.drawable.ic_delete);
            floatingActionButton.setImageDrawable(icon);
            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(Color
                    .parseColor("#6cc477")));


            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    attachReady = false;
                    User.getInstance().setAttach(false);

                    Drawable icon = getResources().getDrawable(R.drawable.ic_attach_file_white_24dp);
                    floatingActionButton.setImageDrawable(icon);
                    floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#325ed6")));
                    Toast.makeText(context, "Archivo adjunto eliminado", Toast.LENGTH_LONG).show();
                }
            });

        }else{

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, "Puede adjuntar un archivo desde la botonera", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {

         attachReady = false;
         User.getInstance().setAttach(false);
         dialog = super.onCreateDialog(savedInstanceState);

         dialog.setOnCancelListener(
                new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //When you touch outside of dialog bounds,
                        //the dialog gets canceled and this method executes.



                    }
                }
        );

        String origin = getArguments().getString("origin");
        Log.d("tktid","recibo origin: " + origin);

        if(origin.equalsIgnoreCase("List_fragment")){

            this.tkt = ArrayTKT.getInstance().getArrayList().get(getArguments().getInt("position"));

        }else if(origin.equalsIgnoreCase("Generated_fragment")){

            this.tkt = ArrayTKTgen.getInstance().getArrayList().get(getArguments().getInt("position"));

        }else if(origin.equalsIgnoreCase("search")){

            this.tkt = ArrayTKTsearch.getInstance().getArrayList().get(getArguments().getInt("position"));

        }



        return createSimpleDialog();
    }

    /**
     * Crea el dialogo en pantalla
     * @return
     */
    public AlertDialog createSimpleDialog() {

        //TextInputLayout textInputLayout = null;
        //LinearLayout.LayoutParams textInputLayoutParams = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater  inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.dialog_form, null);

        builder.setView(v);

        activity = (AppCompatActivity) getActivity();
        context = activity.getApplicationContext();


        //seteo parametros de UI
        /*textInputLayout = new TextInputLayout(context);
        textInputLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textInputLayout.setLayoutParams(textInputLayoutParams);*/
        LinearLayout parentLayout = (LinearLayout) v.findViewById(R.id.formlayout);
        attachLayout = (LinearLayout) v.findViewById(R.id.attach);
        LinearLayout buttonLayout = (LinearLayout) v.findViewById(R.id.buttonLayout);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(45,45,45,15);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        parentLayout.setLayoutParams(params);


        LinearLayout.LayoutParams params_fields = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params_fields.setMargins(45,0,45,0);

        //Se crearan los campos dinamicamente segun los elementos de FormObj
        int secciones = FormObj.getInstance().getArrayElemnt().size();
        for (int i=0;i<secciones;i++) {

            label = new TextView(context);
            String label_txt = FormObj.getInstance().getArrayElemnt().get(i).getLabel();

            if (!label_txt.equalsIgnoreCase("Adjunto")){

                label.setText(label_txt);
                label.setTextColor(getResources().getColor(R.color.gris_form));
                label.setPadding(5, 0, 0, 0);
                label.setTextSize(13);
                label.setLayoutParams(params);
                //textInputLayout.addView(label);
                parentLayout.addView(label);

            }



            String type = FormObj.getInstance().getArrayElemnt().get(i).getType();
            Boolean numeric = FormObj.getInstance().getArrayElemnt().get(i).isNumeric();
            Log.d("getselection","type: " + type);
            switch (type){

                case "inputlong":
                    Log.d("getselection","creo inputlong");
                    inputlong = new EditText(context);
                    inputlong.setTextColor(getResources().getColor(R.color.gris_form));
                    inputlong.setHint(label_txt);
                    inputlong.setLayoutParams(params_fields);
                    //textInputLayout.addView(inputlong);
                    parentLayout.addView(inputlong);
                    break;

                case "month":
                    Log.d("getselection","creo month");
                    cb = this;
                    date = new EditText(context);
                    //date.setInputType(InputType.TYPE_CLASS_DATETIME);
                    date.setTextColor(getResources().getColor(R.color.gris_form));
                    /*date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                       @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(hasFocus){

                                FormObj.getInstance().setRq(cb);
                                PickerDialog picker = new PickerDialog();
                                picker.show(getActivity().getFragmentManager(),"date_picker");
                                Log.d("focus","onfocus");

                            }else{
                                Log.d("focus","lossfocus");
                                try {


                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });*/
                    date.setLayoutParams(params_fields);
                    label.setText(label.getText()+" - MM-AAAA");
                    parentLayout.addView(date);
                    break;
                case "input":
                    Log.d("getselection","creo input");
                    input = new EditText(context);
                    input.setTextColor(getResources().getColor(R.color.gris_form));
                    if (numeric == true){
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                    input.setLayoutParams(params_fields);
                    parentLayout.addView(input);
                    Log.d("getselection","fin creo input");
                    break;
                case "idsel":


                    Log.d("getselection","creo spinner");
                    spinner = new Spinner(context);
                    spinner.setBackgroundColor(getResources().getColor(R.color.spinner));

                    LinearLayout.LayoutParams idselParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,55);
                    idselParams.setMargins(45,5,45,5);
                    spinner.setLayoutParams(idselParams);


                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, FormObj.getInstance().getNameselitem().get(namesel));
                    spinner.setAdapter(spinnerArrayAdapter);
                    namesel++;
                    parentLayout.addView(spinner);
                    break;

                case "select":
                    Log.d("getselection","creo spinner");
                    select = new Spinner(context);
                    select.setBackgroundColor(getResources().getColor(R.color.spinner));

                    LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,55);
                    spinnerParams.setMargins(45,5,45,5);
                    select.setLayoutParams(spinnerParams);

                    ArrayAdapter<String> selectArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, FormObj.getInstance().getNameselitem().get(namesel));
                    select.setAdapter(selectArrayAdapter);
                    namesel++;
                    parentLayout.addView(select);
                    break;


                case "fileupl":
                    Log.d("getselection","creo img");

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(45,0,45,5);
                    attachLayout.setLayoutParams(layoutParams);
                    LinearLayout.LayoutParams button_right = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
                    LinearLayout.LayoutParams button_left = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);

                    //aduntar archivo

                    buttonAdj = new Button(context);
                    SpannableString spanString = new SpannableString("Adjuntar");
                    spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
                    buttonAdj.setText(spanString);
                    buttonAdj.setBackgroundResource(R.drawable.button2);
                    button_left.setMargins(0,0,5,0);
                    buttonAdj.setLayoutParams(button_left);

                    //capturar de camara

                    buttonCap = new Button(context);
                    SpannableString spanString11 = new SpannableString("Capturar");
                    spanString11.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString11.length(), 0);
                    buttonCap.setText(spanString11);
                    buttonCap.setBackgroundResource(R.drawable.button2);
                    buttonCap.setLayoutParams(button_right);

                    attachLayout.addView(buttonAdj);
                    attachLayout.addView(buttonCap);
                    break;


            }



        }

        cameraPhoto = new CameraPhoto(activity.getApplicationContext());


        try {

            //captura de camara
            buttonCap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("camera", "permission: " + ActivityCompat.checkSelfPermission(activity,
                            android.Manifest.permission.CAMERA));

                    // Comprobar permiso
                    int permissionStorage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    int permissionCamera = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);

                    if (permissionStorage == PackageManager.PERMISSION_GRANTED && permissionCamera == PackageManager.PERMISSION_GRANTED) {
                        try {
                            startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {

                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 0);



                    }
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {

            //adjuntar archivo desde explorador de archivos
            buttonAdj.setOnClickListener(new View.OnClickListener() {
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
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        send = new Button(context);
        send.setText("Enviar");
        send.setBackgroundResource(R.drawable.button);
        send.setLayoutParams(params);
        buttonLayout.addView(send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                * ejecuta la accion correspondiente
                */
                Request rq = new Request(FormObj.getInstance().getRq());
                Itracker itracker = new Itracker(getActivity().getApplicationContext(), rq, null);

                            /* Action */
                GetAction action = new GetAction(itracker,tkt);
                String act = FormObj.getInstance().getAction();

                int secciones = FormObj.getInstance().getArrayElemnt().size(); Log.d("secciones", String.valueOf(secciones));
                arrayForm.add("[");
                for (int x=0;x<secciones;x++) {


                    String type = FormObj.getInstance().getArrayElemnt().get(x).getType();
                    String valueForm = null;
                    switch (type){

                        case "idsel":
                            String value1 = getIdSpinner();
                            valueForm = value1;
                            break;
                        case "inputlong":
                            String value2 = inputlong.getText().toString();
                            valueForm = value2;
                            break;
                        case "input":
                            String value3 = input.getText().toString();
                            valueForm = value3;
                            break;
                        case "select":
                            String value4 = getIdSelect();
                            valueForm = value4;
                            break;
                        case "fileupl":
                            String fileTag=encodedImage;
                            break;
                        case "month":
                            String value5 = date.getText().toString();
                            valueForm = value5;


                    }

                    String form = "{\"id\":\"actionform_"+ FormObj.getInstance().getArrayElemnt().get(x).getId() + "\",\"value\":\""+ valueForm +"\"}";

                    arrayForm.add(form);
                    arrayForm.add(",");


                }
                arrayForm.remove(arrayForm.size()-1);
                arrayForm.add("]");
                String form ="";
                for (int i = 0;i<arrayForm.size();i++){

                    form += arrayForm.get(i);

                }

                Log.d("secciones",form);
                action.getDatanodes().setEditNodesData( action.getDatanodes().getEditNodesData()+ act + ";" + form);
                action.Ejecute();




            }

        });

        return builder.create();
    }




    private String getIdSelect() {

        String selected = select.getSelectedItem().toString();
        String item = null;

        int i = 0;
        while (FormObj.getInstance().getNameselitem().get(idsel).get(i) != selected){

            i++;
            item = FormObj.getInstance().getNameselitem().get(idsel).get(i);

        }
        int index = idsel;//guardo la posicion donde tengo que extraer el id del item seleccionado
        idsel++;
        return FormObj.getInstance().getIdselitem().get(index).get(i);
    }

    private String getIdSpinner() {

        String selected = spinner.getSelectedItem().toString();
        String item = null;

        int i = 0;
        while (FormObj.getInstance().getNameselitem().get(idsel).get(i) != selected){

            i++;
            item = FormObj.getInstance().getNameselitem().get(idsel).get(i);

        }

        int index = idsel;//guardo la posicion donde tengo que extraer el id del item seleccionado
        idsel++;
        return FormObj.getInstance().getIdselitem().get(index).get(i);

    }


    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == activity.RESULT_OK){
            if (requestCode == CAMERA_REQUEST){

                String photopath = cameraPhoto.getPhotoPath();
                Bitmap bitmap = null;

                try {
                    bitmap = ImageLoader.init().from(photopath).requestSize(512,512).getBitmap();
                    encodedImage = ImageBase64.encode(bitmap);
                    User.getInstance().setAttach(true);
                    User.getInstance().setBase64(encodedImage);
                    Log.d("uriimg","entra por try");
                    Toast.makeText(context, "Archivo adjuntado correctamente", Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.d("uriimg","entra por catch");
                }

            }else if (requestCode == 1212){

                Uri selectedImageURI = data.getData();
                File file = new File(getRealPathFromURI(selectedImageURI));

                Log.d("filelog", data.getData().toString());
                try {
                    getByte(selectedImageURI);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            result = cursor.getString(idx);
            Log.d("filelog",result);
            cursor.close();
        }
        return result;
    }

    /**
     * Metodo que convierte el archivo desde un uri a Base64 para ser adjunto en un ticket en el xml
     * @param uri
     * @throws IOException
     */
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
        Toast.makeText(context, "Archivo adjuntado correctamente", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){

        if (requestCode == 0){

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){

                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
