package br.ufpe.cin.if1001.rss;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;


public class ViewHolder implements View.OnClickListener {
    TextView data;
    TextView titulo;
    Activity activity;
    String url;

    public ViewHolder(View v, Activity activity){
        this.data = (TextView) v.findViewById(R.id.item_data);
        this.titulo = (TextView) v.findViewById(R.id.item_titulo);
        this.activity = activity;
        v.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(this.url));
        activity.startActivity(i);
    }
}
