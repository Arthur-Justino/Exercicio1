package br.ufpe.cin.if1001.rss;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import android.widget.BaseAdapter;

public class PersonalizedAdapter extends BaseAdapter {
    private Activity activity;
    private List<ItemRSS> items;

    public PersonalizedAdapter(Activity activity, List<ItemRSS> items){
        this.activity=activity;
        this.items=items;
    }
    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        ViewHolder holder;

        if (view == null) {
            v = activity.getLayoutInflater().inflate(R.layout.itemlista, viewGroup, false);
            holder = new ViewHolder(v, activity);
            v.setTag(holder);
        } else {
            v = view;
            holder = (ViewHolder) v.getTag();
        }
        holder.titulo.setText(items.get(i).getTitle());
        holder.data.setText(items.get(i).getPubDate());

        return v;
    }

}
