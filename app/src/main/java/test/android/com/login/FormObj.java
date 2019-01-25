package test.android.com.login;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kosalgeek.android.photoutil.CameraPhoto;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;

import static android.support.v4.app.ActivityCompat.startActivity;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Clase que se encarga de armar formularios dinamicamente
 */
public class FormObj {
    private static FormObj ourInstance = new FormObj();
    private ArrayList<ArrayList<String>> idselitem = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> nameselitem = new ArrayList<ArrayList<String>>();
    private String action;
    private ArrayList<ElementObj> arrayElemnt = new ArrayList<ElementObj>();
    private RQcomplete<String> rq;
    private boolean sended;
    private boolean sending;
    private String file;
    private String typedown;
    private ArrayList<String> arrayHistoric = new ArrayList<String>();
    private boolean envio;
    private Boolean fabMenu;
    private final int CAMERA_REQUEST = 13323;
    private CameraPhoto cameraPhoto;
    private String picker;
    private Spinner select;
    private int namesel;
    private int idsel;
    private EditText textForPicker;
    private char typeOfPicker;

    public EditText getTextForPicker() {
        return textForPicker;
    }

    public void setTextForPicker(EditText textForPicker) {
        this.textForPicker = textForPicker;
    }

    public char getTypeOfPicker() {
        return typeOfPicker;
    }

    public void setTypeOfPicker(char typeOfPicker) {
        this.typeOfPicker = typeOfPicker;
    }

    public String getPicker() {
        return picker;
    }

    public void setPicker(String picker) {
        this.picker = picker;
    }

    public Boolean getFabMenu() {
        return fabMenu;
    }

    public void setFabMenu(Boolean fabMenu) {
        this.fabMenu = fabMenu;
    }

    public CameraPhoto getCameraPhoto() {
        return cameraPhoto;
    }

    public void setCameraPhoto(CameraPhoto cameraPhoto) {
        this.cameraPhoto = cameraPhoto;
    }

    public ArrayList<String> getArrayHistoric() {
        return arrayHistoric;
    }

    public void setArrayHistoric(ArrayList<String> arrayHistoric) {
        this.arrayHistoric = arrayHistoric;
    }

    public String getTypedown() {
        return typedown;
    }

    public void setTypedown(String typedown) {
        this.typedown = typedown;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public boolean isSended() {
        return sended;
    }

    public void setSended(boolean sended) {
        this.sended = sended;
    }

    public boolean isSending() {
        return sending;
    }

    public void setSending(boolean sending) {
        this.sending = sending;
    }

    public RQcomplete<String> getRq() {
        return rq;
    }

    public void setRq(RQcomplete<String> rq) {
        this.rq = rq;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ArrayList<ArrayList<String>> getNameselitem() {
        return nameselitem;
    }

    public void setNameselitem(ArrayList<ArrayList<String>> nameselitem) {
        this.nameselitem = nameselitem;
    }

    public ArrayList<ArrayList<String>> getIdselitem() {
        return idselitem;
    }

    public void setIdselitem(ArrayList<ArrayList<String>> idselitem) {
        this.idselitem = idselitem;
    }

    public ArrayList<ElementObj> getArrayElemnt() {
        return arrayElemnt;
    }

    public void setArrayElemnt(ArrayList<ElementObj> arrayElemnt) {
        this.arrayElemnt = arrayElemnt;
    }

    public static FormObj getInstance() {

        if (ourInstance == null){

            ourInstance = new FormObj();

        }
        return ourInstance;
    }

    private FormObj() {
    }

    /**
     * Metodo que devuelve en pantalla el formulario de creación
     * @param view
     * @param r
     * @return
     */
    public View WriteForm(final View view, RQcomplete r) {

        namesel = 0; //inicializo variable nombre select
        idsel = 0; //inicializo variable id select

        fabMenu = false;
        final ArrayList<Integer> arrayIds = new ArrayList<>();

        final RQcomplete activity = r;

        TextInputLayout textInputLayout = null;
        LinearLayout.LayoutParams textInputLayoutParams = null;
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.openform);

        Log.d("newlog", "elementos de formulario: " +  FormObj.getInstance().getArrayElemnt().size() );
        for(int i=0;i< FormObj.getInstance().getArrayElemnt().size();i++){

            int id = View.generateViewId();
            // TextInputLayout
            textInputLayout = new TextInputLayout(view.getContext());
            textInputLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textInputLayout.setLayoutParams(textInputLayoutParams);
            // edit text
            Log.d("option",FormObj.getInstance().getArrayElemnt().get(i).getLabel().toString());
            String type = FormObj.getInstance().getArrayElemnt().get(i).getType().toString();
            boolean numeric = FormObj.getInstance().getArrayElemnt().get(i).isNumeric();

            if(numeric == true){

                Log.d("numericlog","numeric true");
            }else{

                Log.d("numericlog","numeric false");
            }
            switch (type){

                case "input":
                    EditText text = new EditText(view.getContext());
                    if(numeric == true){

                        text.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }else{

                        text.setInputType(InputType.TYPE_CLASS_TEXT);
                    }
                    text.setHint(FormObj.getInstance().getArrayElemnt().get(i).getLabel().toString());
                    String comment = FormObj.getInstance().getArrayElemnt().get(i).getComment();
                    if(!("empty".equalsIgnoreCase(comment) || "".equalsIgnoreCase(comment))){

                        text.setHint(text.getHint() + " - " + comment);

                    }
                    text.setTextSize(15);
                    text.setBottom(30);
                    text.setId(id);
                    text.setTag("input");
                    arrayIds.add(text.getId());
                    textInputLayout.addView(text);
                    layout.addView(textInputLayout,textInputLayoutParams);
                break;
                case "inputlong":
                    EditText textlong = new EditText(view.getContext());
                    textlong.setHint(FormObj.getInstance().getArrayElemnt().get(i).getLabel().toString());
                    String comment1 = FormObj.getInstance().getArrayElemnt().get(i).getComment();
                    if(!("empty".equalsIgnoreCase(comment1) || "".equalsIgnoreCase(comment1))){

                        textlong.setHint(textlong.getHint() + " - " + comment1);

                    }
                    textlong.setTextSize(15);
                    textlong.setMaxLines(3);
                    textlong.setBottom(30);
                    textlong.setTag("inputlong");
                    textlong.setId(id);
                    arrayIds.add(textlong.getId());
                    textInputLayout.addView(textlong);
                    layout.addView(textInputLayout,textInputLayoutParams);
                    break;
                case "fileupl":
                    fabMenu = true;
                    LinearLayout layout_attach = (LinearLayout) view.findViewById(R.id.attach_open);
                    LinearLayout.LayoutParams button_param = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
                    cameraPhoto = new CameraPhoto(view.getContext());
                    Button buttonAdj;
                    buttonAdj = new Button(view.getContext());
                    SpannableString spanString = new SpannableString("Adjuntar");
                    spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
                    buttonAdj.setText(spanString);
                    buttonAdj.setId(id);
                    buttonAdj.setTag("button");
                    arrayIds.add(buttonAdj.getId());
                    buttonAdj.setLayoutParams(button_param);
                    //textInputLayout.addView(buttonAdj);
                    //layout_attach.addView(buttonAdj);
                    buttonAdj.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button b = (Button) v;
                            String text = ((Button) v).getText().toString();
                            Log.d("option",text);
                            Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                            fileintent.setType("gagt/sdf");
                            Fragment fr = (Fragment) activity;


                            try {
                                fr.startActivityForResult(fileintent,1212,null);
                            } catch (ActivityNotFoundException e) {
                                Log.e("tag", "No activity can handle picking a file. Showing alternatives.");
                            }
                        }
                    });
                    Button buttonCap;
                    buttonCap = new Button(view.getContext());
                    SpannableString spanString11 = new SpannableString("Capturar");
                    spanString11.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString11.length(), 0);
                    buttonCap.setText(spanString11);
                    buttonCap.setLayoutParams(button_param);
                    //textInputLayout.addView(buttonCap);
                    //layout_attach.addView(buttonCap);
                    buttonCap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button b = (Button) v;
                            String text = ((Button) v).getText().toString();
                            Log.d("option",text);
                            
                            Fragment fr = (Fragment) activity;


                            Log.d("camera", "permission: " + ActivityCompat.checkSelfPermission(((Fragment) activity).getContext(),
                                    android.Manifest.permission.CAMERA));

                            // Comprobar permiso
                            int permissionStorage = ContextCompat.checkSelfPermission(((Fragment) activity).getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            int permissionCamera = ContextCompat.checkSelfPermission(((Fragment) activity).getContext(), Manifest.permission.CAMERA);

                            if (permissionStorage == PackageManager.PERMISSION_GRANTED && permissionCamera == PackageManager.PERMISSION_GRANTED) {
                                try {
                                    fr.startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {


                                // Solicitar el permiso
                                fr.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 0);



                            }
                        }
                    });
                    break;
                case "link":
                    Button buttonModel;
                    buttonModel = new Button(view.getContext());
                    SpannableString spanString1 = new SpannableString(FormObj.getInstance().getArrayElemnt().get(i).getLabel());
                    spanString1.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString1.length(), 0);
                    buttonModel.setText(spanString1);
                    buttonModel.setId(id);
                    buttonModel.setTag("button");
                    arrayIds.add(buttonModel.getId());
                    textInputLayout.addView(buttonModel);
                    layout.addView(textInputLayout,textInputLayoutParams);
                    String path = FormObj.getInstance().getArrayElemnt().get(i).getPath();
                    file = path;
                    typedown = "anexo";
                    buttonModel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button b = (Button) v;
                            String text = ((Button) v).getText().toString();
                            Log.d("option",text);

                            Request rq = new Request(activity);
                            webApp webApp = new webApp(view.getContext(),rq,null);
                            GetDownload down = new GetDownload(webApp,null);
                            down.Ejecute();
                        }
                    });
                    break;
                case "date":
                    EditText d = new EditText(view.getContext());
                    d.setInputType(InputType.TYPE_CLASS_DATETIME);
                    d.setHint(FormObj.getInstance().getArrayElemnt().get(i).getLabel().toString() + " - DD-MM-AAAA");
                    d.setTextSize(15);
                    d.setBottom(30);
                    d.setId(id);
                    d.setTag("date");
                    arrayIds.add(d.getId());

                    textInputLayout.addView(d);
                    layout.addView(textInputLayout,textInputLayoutParams);
                    d.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(hasFocus){

                                setTextForPicker((EditText)v);
                                setTypeOfPicker('d');
                                PickerDialog picker = new PickerDialog();
                                Fragment fr = (Fragment) activity;
                                FragmentTransaction ft = fr.getFragmentManager().beginTransaction();
                                picker.show(fr.getFragmentManager(),"picker");
                            }
                        }
                    });
                    break;
                case "month":
                    EditText date = new EditText(view.getContext());
                    date.setInputType(InputType.TYPE_CLASS_DATETIME);
                    date.setHint(FormObj.getInstance().getArrayElemnt().get(i).getLabel().toString() + " - MM-AAAA");
                    date.setTextSize(15);
                    date.setBottom(30);
                    date.setId(id);
                    date.setTag("month");
                    arrayIds.add(date.getId());

                    textInputLayout.addView(date);
                    layout.addView(textInputLayout,textInputLayoutParams);
                    date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(hasFocus){

                                setTextForPicker((EditText)v);
                                setTypeOfPicker('m');
                                PickerDialog picker = new PickerDialog();
                                Fragment fr = (Fragment) activity;
                                FragmentTransaction ft = fr.getFragmentManager().beginTransaction();
                                picker.show(fr.getFragmentManager(),"picker");
                            }
                        }
                    });
                    break;

                case "select":
                    Log.d("getselection","creo spinner");
                    select = new Spinner(view.getContext());
                    //select.setBackgroundColor(getResources().getColor(R.color.spinner));

                    ArrayAdapter<String> selectArrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, FormObj.getInstance().getNameselitem().get(namesel));
                    namesel++;
                    TextView textView = new TextView(view.getContext());
                    textView.setText(FormObj.getInstance().getArrayElemnt().get(i).getLabel().toString());
                    textView.setTypeface(Typeface.create("sans-serif-condensed", Typeface.BOLD));
                    select.setAdapter(selectArrayAdapter);
                    select.setId(id);
                    arrayIds.add(select.getId());
                    Log.d("newlog",type + " id " + id);
                    layout.addView(textView);
                    layout.addView(select);
                    break;

                default:
                    EditText text1 = new EditText(view.getContext());
                    text1.setId(id);
                    text1.setTag("text");
                    arrayIds.add(text1.getId());
                    break;

            }



        }


        Button buttonC;
        buttonC = new Button(view.getContext());
        SpannableString spanString = new SpannableString("Crear");
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        buttonC.setText(spanString);




        //seteo parametros de UI
        LinearLayout parentLayout = (LinearLayout) view.findViewById(R.id.openform_send);
        parentLayout.setBottom(5);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonC.setBackgroundResource(R.drawable.button);
        buttonC.setTextColor(Color.parseColor("#ddd7d7"));


        //seteo los parametros y agrego el textview
        buttonC.setCompoundDrawablePadding(20);
        params.setMargins(5,5,5,5);
        buttonC.setLayoutParams(params);

        parentLayout.addView(buttonC);
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idsel = 0;
                /*
                * ejecuta la accion correspondiente
                */
                Request rq = new Request(activity);
                webApp webApp = new webApp(view.getContext(), rq, null);

                            /* Action */
                GetAction action = new GetAction(webApp);
                action.setDatanodes(new DataNodes("CREATE",null));
                String act = FormObj.getInstance().getAction();

                int secciones = FormObj.getInstance().getArrayElemnt().size();
                ArrayList<String> arrayForm = new ArrayList<String>();
                arrayForm.add("[");

                for (int x=0;x<secciones;x++) {


                    String type = FormObj.getInstance().getArrayElemnt().get(x).getType();
                    String valueForm = null;
                    Log.d("newlog",type + " id " + arrayIds.get(x));
                    switch (type){


                        case "inputlong":

                            EditText textlong = (EditText) view.findViewById( arrayIds.get(x));
                            if(TextUtils.isEmpty(textlong.getText().toString())){

                                envio = false;
                                textlong.setError("Debe completar el campo");
                                return;

                            }else{

                                String value2 = textlong.getText().toString();
                                valueForm = value2;
                                envio = true;
                            }
                            break;
                        case "input":
                            EditText text = (EditText) view.findViewById( arrayIds.get(x));
                            if(TextUtils.isEmpty(text.getText().toString())){

                                envio = false;
                                text.setError("Debe completar el campo");
                                return;

                            }else {
                                String value3 = text.getText().toString();
                                valueForm = value3;
                                envio = true;
                            }
                            break;
                        case "date":
                            EditText d = (EditText) view.findViewById( arrayIds.get(x));
                            if(TextUtils.isEmpty(d.getText().toString())){

                                envio = false;
                                d.setError("Debe completar el campo");
                                return;

                            }else {
                                String value4 = d.getText().toString();
                                valueForm = value4;
                                envio = true;
                            }
                            break;
                        case "month":
                            EditText date = (EditText) view.findViewById( arrayIds.get(x));
                            if(TextUtils.isEmpty(date.getText().toString())){

                                envio = false;
                                date.setError("Debe completar el campo");
                                return;

                            }else {
                                String value4 = date.getText().toString();
                                valueForm = value4;
                                envio = true;
                            }
                            break;

                        case "select":
                            String value5 = IdSelected((Spinner) view.findViewById(arrayIds.get(x)));
                            valueForm = value5;
                            break;
                       default:
                           valueForm = "";

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

                if(envio == true){

                    action.getDatanodes().setEditNodesData(action.getDatanodes().getEditNodesData() + ";" + form);
                    action.Ejecute();

                                    }



            }

        });


        return view;
    }

    private String IdSelected(Spinner select) {

        String selected = select.getSelectedItem().toString();
        Log.d("newlog",selected);
        String item = null;

        int i = 0;
        while (FormObj.getInstance().getNameselitem().get(idsel).get(i) != selected){

            i++;
            item = FormObj.getInstance().getNameselitem().get(idsel).get(i);

        }
        int index = idsel;//guardo la posicion donde tengo que extraer el id del item seleccionado
        idsel++;
        Log.d("getselect",item + " : " + FormObj.getInstance().getIdselitem().get(index).get(i) );
        return FormObj.getInstance().getIdselitem().get(index).get(i);
    }


}
