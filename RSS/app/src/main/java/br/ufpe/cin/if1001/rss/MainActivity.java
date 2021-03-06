package br.ufpe.cin.if1001.rss;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    //ao fazer envio da resolucao, use este link no seu codigo!
    //private final String RSS_FEED = "http://leopoldomt.com/if1001/g1brasil.xml";
    private SharedPreferences sharedPref;
    //OUTROS LINKS PARA TESTAR...
    //http://rss.cnn.com/rss/edition.rss
    //http://pox.globo.com/rss/g1/brasil/
    //http://pox.globo.com/rss/g1/ciencia-e-saude/
    //http://pox.globo.com/rss/g1/tecnologia/

    //use ListView ao invés de TextView - deixe o atributo com o mesmo nome
    private ListView conteudoRSS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //use ListView ao invés de TextView - deixe o ID no layout XML com o mesmo nome conteudoRSS
        //isso vai exigir o processamento do XML baixado da internet usando o ParserRSS
        conteudoRSS = (ListView) findViewById(R.id.conteudoRSS);
        this.sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("rssFeed", getString(R.string.rss_feed_default));
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new CarregaRSStask().execute(this.sharedPref.getString("rssFeed", getString(R.string.rss_feed_default)));
    }

    private class CarregaRSStask extends AsyncTask<String, Void, List<ItemRSS>> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "iniciando...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<ItemRSS> doInBackground(String... params) {
            String conteudo = "provavelmente deu erro...";
            List<ItemRSS> titu = new ArrayList<ItemRSS>();

            try {
                conteudo = getRssFeed(params[0]);
                titu = ParserRSS.parse(conteudo);
            } catch (Exception e){
                e.printStackTrace();
            }
            return titu;
        }

        @Override
        protected void onPostExecute(List<ItemRSS> s) {
            Toast.makeText(getApplicationContext(), "terminando...", Toast.LENGTH_SHORT).show();

            //ajuste para usar uma ListView
            //o layout XML a ser utilizado esta em res/layout/itemlista.xml
            PersonalizedAdapter adapter = new PersonalizedAdapter(MainActivity.this, s);
            conteudoRSS.setAdapter(adapter);
        }
    }

    //Opcional - pesquise outros meios de obter arquivos da internet
    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed = "";
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }
}
