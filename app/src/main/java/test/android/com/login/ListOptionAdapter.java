package test.android.com.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Clase adapter para listar en la UI las opciones de tipificación en la creación de un nuevo ticket
 */
public class ListOptionAdapter extends BaseAdapter {

    private final View v;
    private ArrayList<OptionObj> result;
    private static LayoutInflater inflater=null;
    private Context context;

    public ListOptionAdapter(View view, ArrayList<OptionObj> list) {

        result=list;
        context=view.getContext();
        v = view;

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
        TextView opt;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub


            Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.card_opt, null);
            holder.opt = (TextView) rowView.findViewById(R.id.opt);
            holder.opt.setText(result.get(position).getOption());
            holder.opt.setHint(result.get(position).getPath());


            return rowView;

    }
}
