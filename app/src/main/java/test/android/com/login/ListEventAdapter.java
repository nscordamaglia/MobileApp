package test.android.com.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Normalizer;
import java.util.ArrayList;

/**
 * Clase adapter para armar el listview de eventos dentro de un webApp para mostrar en la UI
 */

public class ListEventAdapter extends BaseAdapter {
    private  RQcomplete<String> r;
    private  TKTobj tkt;
    private  String tktid;
    ArrayList<EventObj> eventlist;
    ArrayList<ActObj> actionlist;
    ArrayList<ElementObj> elementlist;
    Context context;
    Button before,button;
    int [] imageId;
    int pos;
    private static LayoutInflater inflater=null;
    private ProgressBar wait;
    private View view;

    public ListEventAdapter(View v, ArrayList<TKTobj> arrayList, int p,RQcomplete<String> rq) {
        // TODO Auto-generated constructor stub
        this.pos = p;
        this.tkt = arrayList.get(pos);
        this.tktid = arrayList.get(pos).getId(); Log.d("tktid","id: " + tktid);
        this.eventlist=arrayList.get(pos).getArrayevent();

        try{if(eventlist.get(eventlist.size()-1).getArrayactions().size()== 0){
            eventlist.remove(eventlist.size()-1);
        }}catch (ArrayIndexOutOfBoundsException e){}
        this.r = rq;
        this.context=v.getContext();
        this.view = v;
        this.inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.wait = (ProgressBar) v.findViewById(R.id.waiting);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub


        return eventlist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView evento,date,value,attach;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub


        Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.card_event, null);

        //seteo parametros de UI
        LinearLayout parentLayout = (LinearLayout) rowView.findViewById(R.id.cardlayout);
        parentLayout.setBottom(30);

        holder.evento=(TextView) rowView.findViewById(R.id.evento);
        holder.evento.setText(eventlist.get(position).getAlias());
        holder.date=(TextView) rowView.findViewById(R.id.FA);
        holder.date.setText(eventlist.get(position).getDate());
        holder.value=(TextView) rowView.findViewById(R.id.value);
        holder.value.setText(eventlist.get(position).getValue());
        try {


                for(int i=0; i<eventlist.get(position).getvAttach().size(); i++){

                    String filename = eventlist.get(position).getvAttach().get(i);
                    //creo textview dinamico
                    holder.attach = new TextView(context);
                    SpannableString filetxt = new SpannableString(filename);
                    filetxt.setSpan(new StyleSpan(Typeface.BOLD), 0, filetxt.length(), 0);
                    holder.attach.setText(filetxt);
                    holder.attach.setTextColor(Color.parseColor("blue"));
                    //creo imagen dinamica
                    holder.img = new ImageView(context);
                    holder.img.setImageDrawable(view.getResources().getDrawable(android.R.drawable.ic_menu_gallery));
                    holder.img.setColorFilter(Color.parseColor("blue"));
                    holder.img.setId(i);
                    holder.img.setPadding(0,0,350,0);
                    parentLayout.addView(holder.attach);
                    parentLayout.addView(holder.img);


                    holder.img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            FormObj.getInstance().setFile(eventlist.get(position).getvAttach().get(v.getId()));
                            FormObj.getInstance().setTypedown("adjunto");
                            Request rq = new Request(r);
                            webApp webApp = new webApp(context,rq,null);
                            GetDownload down = new GetDownload(webApp,null);
                            down.Ejecute();

                        }
                    });
                }



        }catch(IndexOutOfBoundsException e){}

      
        elementlist = eventlist.get(position).getArrayelements();

        for (int i =0; i<elementlist.size(); i++) {

            //creo textview dinamico
            TextView label = new TextView(context);
            SpannableString labeltxt = new SpannableString(elementlist.get(i).getLabel());
            labeltxt.setSpan(new StyleSpan(Typeface.BOLD), 0, labeltxt.length(), 0);
            label.setText(labeltxt);
            TextView value = new TextView(context);
            value.setText(elementlist.get(i).getValue());



            parentLayout.addView(label);
            parentLayout.addView(value);
        }

        actionlist = eventlist.get(position).getArrayactions();
        if (actionlist.size()>0 ){

            Log.d("getaction","acciones: " + String.valueOf(actionlist.size()));
            for (int i =0; i<actionlist.size(); i++) {



                button = new Button(context);
                SpannableString spanString = new SpannableString(actionlist.get(i).getAlias());
                spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
                button.setText(spanString);
                button.setTag(actionlist.get(i).getNombre());
                button.setHint(actionlist.get(i).getFormulario());
                button.setId(i);


                //seteo parametros de UI
                LinearLayout parentLayout1 = (LinearLayout) rowView.findViewById(R.id.actionlayout);
                parentLayout1.setBottom(5);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                button.setBackgroundResource(R.drawable.button);
                button.setTextColor(Color.parseColor("#ddd7d7"));


                //seteo los parametros y agrego el textview
                button.setCompoundDrawablePadding(20);
                params.setMargins(5,5,5,5);
                button.setLayoutParams(params);

                //before = button;
                parentLayout1.addView(button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button b = (Button)rowView.findViewById(v.getId());
                        String text = b.getText().toString();
                        String formulario;

                        if (b.getHint().toString().equalsIgnoreCase("1")){

                            formulario = " necesita formulario.";
                            Request rq = new Request(r);
                            webApp webApp = new webApp(context, rq, null);

                            /* Form */
                            GetForm form = new GetForm(webApp,tkt); Log.d("tktid","envio id: " + tkt.getId().toString());
                            String act = b.getTag().toString();
                            FormObj.getInstance().setAction(act);
                            FormObj.getInstance().setRq(r);
                            form.getDatanodes().setEditNodesData( form.getDatanodes().getEditNodesData()+ act);
                            Log.d("tktid",form.getDatanodes().getEditNodesData());
                            form.Ejecute();
                            wait.setVisibility(View.VISIBLE);

                        }else{

                            formulario = " no necesita formulario.";
                            Request rq = new Request(r);
                            webApp webApp = new webApp(context, rq, null);

                            /* Action */
                            GetAction action = new GetAction(webApp,tkt);
                            String act = b.getTag().toString();
                            String form = "[]";
                            action.getDatanodes().setEditNodesData(action.getDatanodes().getEditNodesData()+ act + ";" + form);
                            action.Ejecute();
                            wait.setVisibility(View.VISIBLE);
                        }


                        Log.d("clickb","accion: " + text + " en tkt: " + tktid + formulario + " nombre del tag: " + b.getTag().toString());

                    }
                });

            }

        }



        return rowView;
    }



}


