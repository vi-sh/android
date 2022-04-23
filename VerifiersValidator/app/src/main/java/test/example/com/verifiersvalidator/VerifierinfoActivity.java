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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

public class VerifierinfoActivity extends Activity
{
    private static final String tag = "VerifierinfoActivity";
    String verifierdatajson;
    String id,rev,name,password,phno,place,status;
    TextView idtv,revtv,nametv,passwordtv,phnotv,placetv;
    Button updatebtn;
    EditText statuset;
    String selectedid = ValidatingActivity.verifierid;
    String selectidurl = "https://verifier.cloudant.com/verifierinfo/" + selectedid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verifierinfoactivity);

        idtv=(TextView)findViewById(R.id.viidtext);
        nametv=(TextView)findViewById(R.id.vinametxt);
        phnotv=(TextView)findViewById(R.id.viphnotxt);
        placetv=(TextView)findViewById(R.id.viplacetxt);
        statuset=(EditText)findViewById(R.id.vistatustxt);
        updatebtn=(Button)findViewById(R.id.btnupdate);



        if (isinternetavailable())
        {
            Log.d(tag, "Selected id from the list and its url is--->" + selectidurl);
            new dipsplayselectedidinfo().execute(selectidurl);
        }
        else
        {
            final SweetAlertDialog pDialog = new SweetAlertDialog(VerifierinfoActivity.this, SweetAlertDialog.WARNING_TYPE);
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

        updatebtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(status.equalsIgnoreCase("valid") || status.equalsIgnoreCase("invalid"))
                {
                    new updatewithnewinfo().execute(selectidurl);
                }
                else
                {
                    final SweetAlertDialog pDialog = new SweetAlertDialog(VerifierinfoActivity.this, SweetAlertDialog.WARNING_TYPE);
                    pDialog.setTitleText("Error");
                    pDialog.setContentText("Invalid value for Status");
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


    private boolean isinternetavailable()
    {
        Log.d(tag, "in checkNetworkConnection()-->");
        NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Log.d(tag, "in checkNetworkConnection()-->");
            return false;
        }
        else
        {
            Log.d(tag, "in (else) checkNetworkConnection()-->");
            return true;
        }
    }

    public class dipsplayselectedidinfo extends AsyncTask<String, String, String>
    {
        AlertDialog pd1 = new SpotsDialog(VerifierinfoActivity.this, R.style.Custom);

        //PostJsonTask PreExecute
        protected void onPreExecute()
        {
            Log.d(tag, "In dipsplayselectedidinfo onPreExecute ->- PostJsonTask -->");
            super.onPreExecute();
            pd1.setCancelable(false);
            pd1.show();
            Log.d(tag, "Out from dipsplayselectedidinfo --<-- onPreExecute -<- PostJsonTask <--");
        }

        @Override
        protected String doInBackground(String... params)
        {
            Log.d(tag, "dipsplayselectedidinfo in doInBackground()-->");
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try
            {
                Log.d(tag, " dipsplayselectedidinfo in (try) doInBackground()-->");
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null)
                {
                    buffer.append(line).append("\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
                }
                Log.d(tag, "dipsplayselectedidinfo out (try) doInBackground() <--");
                return buffer.toString();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            finally
            {
                if (connection != null)
                {
                    connection.disconnect();
                }
                try
                {
                    if (reader != null)
                    {
                        reader.close();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            Log.d(tag, "dipsplayselectedidinfo out doInBackground() <--");
            return null;
        }

        protected void onPostExecute(String result)
        {
            Log.d(tag,"dipsplayselectedidinfo-------> Result before postexe is ---> "+result);
            Log.d(tag, "dipsplayselectedidinfo in onPostExecute()-->");
            super.onPostExecute(result);
            if (pd1.isShowing())
            {
                pd1.dismiss();
            }
            verifierdatajson=result;

            try
            {
                JSONObject object = new JSONObject(verifierdatajson);
                id = object.getString("_id");
                rev = object.getString("_rev");
                name = object.getString("name");
                password=object.getString("password");
                phno=object.getString("phno");
                place = object.getString("place");
                status = object.getString("status");

                Log.d(tag, "dipsplayselectedidinfo---->>> get string of _id is---->" +id);
                Log.d(tag, "dipsplayselectedidinfo---->>> get string of _rev is---->" + rev);
                Log.d(tag, "dipsplayselectedidinfo---->>> get string of name is---->" +name);
                Log.d(tag, "dipsplayselectedidinfo---->>> get string of phno is---->" + phno);
                Log.d(tag, "dipsplayselectedidinfo---->>> get string of place is---->" +place);
                Log.d(tag, "dipsplayselectedidinfo---->>> get string of status is---->" + status);

               //displaying all these below info in the verifierinfolayout
                idtv.setText(id);
                nametv.setText(name);
                phnotv.setText(phno);
                placetv.setText(place);
                statuset.setText(status);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public class updatewithnewinfo extends AsyncTask<String, String, String>
    {

        AlertDialog pd1 = new SpotsDialog(VerifierinfoActivity.this, R.style.Custom);

        //PostJsonTask PreExecute
        protected void onPreExecute()
        {
            Log.d(tag,"In update with new info task ");
            Log.d(tag, "updatewithnewinfo In onPreExecute ->- PostJsonTask -->");
            super.onPreExecute();
            pd1.setCancelable(false);
            pd1.show();
            Log.d(tag, "updatewithnewinfo Out from onPreExecute -<- PostJsonTask <--");
        }

        @Override
        protected String doInBackground(String... params)
        {
            String url="https://verifier.cloudant.com/verifierinfo/"+selectedid;
            String result1;
            Log.d(tag, "updatewithnewinfo In doInBackground ->- PostJsonTask -->");
            verifierdata verifierinfo = new verifierdata();

            //Setting all the info with the previous values along with the new Updated Status value
            verifierinfo.setId(id);
            verifierinfo.setRev(rev);
            verifierinfo.setName(name);
            verifierinfo.setPassword(password);
            verifierinfo.setPhno(phno);
            verifierinfo.setPlace(place);
            verifierinfo.setStatus(statuset.getText().toString());
            Log.d(tag, "updatewithnewinfo Out from doInBackground -<- PostJsonTask <--");
            Log.d(tag,"status edit text while updating is"+statuset.getText().toString());
            result1=PUT(params[0],verifierinfo);
            Log.d(tag,"updatewithnewinfo Result 1 is----)"+result1);
            return result1;

        }
        protected void onPostExecute(String result)
        {
            Log.d(tag,"updatewithnewinfo Result before postexe is ---> "+result);
            Log.d(tag, "in onPostExecute()-->");
            super.onPostExecute(result);
            if (pd1.isShowing())
            {
                pd1.dismiss();
            }
            final SweetAlertDialog pDialog = new SweetAlertDialog(VerifierinfoActivity.this, SweetAlertDialog.WARNING_TYPE);
            pDialog.setTitleText("Success");
            pDialog.setContentText(name+" is now a VeriFier\n Now you can Log in");
            pDialog.setConfirmText("Okay");
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
            new dipsplayselectedidinfo().execute(selectidurl);
            /*Log.d(tag, "---->>> setting updated string of _id is---->" +id);
            Log.d(tag, "---->>> setting updated string of _rev is---->" + rev);
            Log.d(tag, "---->>> setting updated string of name is---->" +name);
            Log.d(tag, "---->>> setting updated string of phno is---->" + phno);
            Log.d(tag, "---->>> setting updated string of place is---->" +place);
            Log.d(tag, "---->>> setting updated string of status is---->" + status);

            idtv.setText(id);
            nametv.setText(name);
            phnotv.setText(phno);
            placetv.setText(place);
            statuset.setText(status);*/
        }


    }
    public String PUT(String puturl,verifierdata verifierinfo)
    {
        String result=null;
        InputStream inputStream = null;
        try
        {
            URL url = new URL("https://verifier.cloudant.com/verifierinfo/"+selectedid);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("_id", verifierinfo.getId());
            jsonObject.accumulate("_rev",verifierinfo.getRev());
            jsonObject.accumulate("name", verifierinfo.getName());
            jsonObject.accumulate("password", verifierinfo.getPassword());
            jsonObject.accumulate("phno",verifierinfo.getPhno());
            jsonObject.accumulate("place", verifierinfo.getPlace());
            jsonObject.accumulate("status", verifierinfo.getStatus());
            json = jsonObject.toString();
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write(json);
            out.close();

            Log.d(json, "--------> is the DATA to be PUT and updated");
            String response=httpCon.getResponseMessage();
            Log.d(tag,"response after updating is "+response);
            // receive response as inputStream
            inputStream = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));

            // convert inputstream to string
            if (inputStream != null)
            {
                Log.d(tag, "control is in if input stream is not null");
                result = convertInputStreamToString(inputStream);
                Log.d(tag, "---->result contains" + result);
            } else {
                result = "Did not work!";

            }
            Log.d(tag, "----->while posting the data RESPONSE is---->" + result);

        }
        catch(Exception e)
        {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        Log.d(tag, "--->Out from POST method -<- doInBackground -<- PostJsonTask <---");
        return result;
    }


    private  String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        Log.d(tag,"--->In convertInputStreamToString ->- PostJsonTask --->");
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        Log.d(tag,"--->Out from convertInputStreamToString -<- PostJsonTask <---");
        return result;
    }

}