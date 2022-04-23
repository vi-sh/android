package test.example.com.verifiersvalidator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ValidatingActivity extends Activity
{
    private static final String tag = "ValidatingActivity";
    InputStream inputStream = null;
    String result=null,responsestr;
    String findquery="{\n" +
            "  \"selector\":\n" +
            "  {\n" +
            "    \"_id\":\n" +
            "    {\n" +
            "      \"$gt\": 0\n" +
            "    }\n" +
            "  },\n" +
            "  \"fields\": [\n" +
            "    \"_id\",\n" +
            "    \"name\"\n" +
            "  ]\n" +
            "}";
    ListView lv;
    static String verifierid;
    ArrayList<HashMap<String, String>> verifierlist;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.validatingactivity);

        verifierlist = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        final Button refreshbtn=(Button)findViewById(R.id.btnrefresh);
        final String posturl="https://verifier.cloudant.com/verifierinfo/_find";


        refreshbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(checkNetworkConnection())
                {
                    Log.d(tag, "Refresh button clicked");
                    new postfindquery().execute(posturl);
                    refreshbtn.setVisibility(View.GONE);
                }
                else
                {
                    final SweetAlertDialog pDialog = new SweetAlertDialog(ValidatingActivity.this, SweetAlertDialog.WARNING_TYPE);
                    pDialog.setTitleText("Error");
                    pDialog.setContentText("No Internet Access");
                    pDialog.setConfirmText("Try again");
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                        }
                    })
                            .show();
                }
            }
        });

}

    private boolean checkNetworkConnection()
    {
        Log.d(tag, "in checkNetworkConnection()-->");
        NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Log.d(tag, "in (if) checkNetworkConnection()-->");
            return false;
        }
        else
        {
            Log.d(tag, "in (else) checkNetworkConnection()-->");
            return true;
        }
    }

public class postfindquery extends AsyncTask<String,Void ,String>
{
    AlertDialog pd1 = new SpotsDialog(ValidatingActivity.this,R.style.Custom);

    //PostJsonTask PreExecute
    protected void onPreExecute()
    {
        Log.d(tag, "In onPreExecute ->- PostJsonTask -->");
        super.onPreExecute();
        pd1.setCancelable(false);
        pd1.show();
        Log.d(tag, "Out from onPreExecute -<- PostJsonTask <--");
    }

    @Override
    protected String doInBackground(String... params)
    {
        Log.d(tag,"In do background thread");
        try {
            MediaType BODY_DATATYPE = MediaType.parse("application/json");
            OkHttpClient client = new OkHttpClient();
            client = new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(3, TimeUnit.MINUTES)
                    .build();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(BODY_DATATYPE, findquery);
            Request request = new Request.Builder()
                    .url("https://verifier.cloudant.com/verifierinfo/_find")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("accept","application/json")
                    .build();
            Response response = client.newCall(request).execute();
            responsestr=response.body().string();
            Log.d(tag,"response string is ---->"+responsestr);


            /////////////now we got the json data need to display them in listview

            if (responsestr != null)
            {
                try
                {
                    JSONObject jsonObj = new JSONObject(responsestr);
                    // Getting JSON Array node
                    JSONArray docs = jsonObj.getJSONArray("docs");
                    for (int i=0; i<docs.length(); i++)
                    {
                        JSONObject c = docs.getJSONObject(i);
                        String id = c.getString("_id");
                        String name = c.getString("name");

                        HashMap<String, String> verifierdoc = new HashMap<>();

                        verifierdoc.put("_id", id);
                        verifierdoc.put("name", name);


                        // adding contact to contact list
                        verifierlist.add(verifierdoc);

                    }



                }
                catch (final JSONException e)
                {
                    Log.e(tag, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }
            else
            {
                Log.e(tag, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Check InternetAccess and tryagain",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        }
        catch (ConnectTimeoutException e)
        {
            final SweetAlertDialog pDialog = new SweetAlertDialog(ValidatingActivity.this, SweetAlertDialog.WARNING_TYPE);
            pDialog.setTitleText("Error");
            pDialog.setContentText("Connection Timed out");
            pDialog.setConfirmText("Try again");
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismissWithAnimation();
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                }
            })
                    .show();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
       return result;
    }

    @Override
    protected void onPostExecute(String params)
    {
        super.onPostExecute(params);
        if (pd1.isShowing())
        {
            pd1.dismiss();
        }
        ListAdapter adapter = new SimpleAdapter(
                ValidatingActivity.this, verifierlist,
                R.layout.cell, new String[]{"_id", "name"}, new int[]{R.id.listitem_id,
                R.id.listitem_name});
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                verifierid = ((TextView) view.findViewById(R.id.listitem_id)).getText().toString();
                String selectedFromList =(lv.getItemAtPosition(position).toString());
                // When clicked, show a toast with the TextView text
                Log.d(tag,"selected item from the list is--->"+selectedFromList);
                Log.d(tag,"selected verifier id  from the list is--->"+verifierid);

                Intent i = new Intent(ValidatingActivity.this,VerifierinfoActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);
            }
        });
    }
}
}