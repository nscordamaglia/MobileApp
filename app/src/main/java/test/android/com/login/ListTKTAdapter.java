package test.android.com.login;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Clase adapter que genera la UI para mostrar las listas de tickets en pantalla
 */

public class ListTKTAdapter extends BaseAdapter{
    ArrayList<TKTobj> result;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;

    public ListTKTAdapter(View v, ArrayList<TKTobj> arrayList) {
        // TODO Auto-generated constructor stub
        result=arrayList;
        context=v.getContext();

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
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
        TextView id,FA,grupo,tipificacion;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_row, null);
        holder.id=(TextView) rowView.findViewById(R.id.idtkt);
        holder.img=(ImageView) rowView.findViewById(R.id.list_image);
        holder.FA=(TextView) rowView.findViewById(R.id.opendate);
        holder.grupo = (TextView) rowView.findViewById(R.id.grupo);
        holder.tipificacion = (TextView) rowView.findViewById(R.id.tipif);
        holder.id.setText(result.get(position).getId());
        holder.FA.setText(result.get(position).getFA());
        holder.grupo.setText(result.get(position).getEquiposname());
        holder.tipificacion.setText(result.get(position).getTipif());
        return rowView;
    }

}


