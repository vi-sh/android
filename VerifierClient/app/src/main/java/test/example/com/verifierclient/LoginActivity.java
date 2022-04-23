package test.example.com.verifierclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;


public class LoginActivity extends Activity
{
    public static String jsondata;
    String userid;
    String password;
    EditText uidtb,passwordtb;
    Button sumbitbtnlg;
    TitanicTextView labelnewuser;
    String attr1,attr3;

    private static final String tag = "LoginActivity";




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);


        TitanicTextView labellogin = (TitanicTextView)findViewById(R.id.loginlbl);
        Typeface font1 = Typeface.createFromAsset(getAssets(),"fonts/DroidSerif-Regular.ttf");
        labellogin.setTypeface(font1);

        //typeface for VISH label
        TitanicTextView labelname  = (TitanicTextView)findViewById(R.id.namelblvishloginact);
        Typeface font2 = Typeface.createFromAsset(getAssets(),  "fonts/ufonts.com_lucia-bt.ttf");
        labelname.setTypeface(font2);


        //Animating LOGIN and VISH
        Titanic titanic=new Titanic();
        titanic.start(labellogin);
        titanic.start(labelname);


        uidtb = (EditText)findViewById(R.id.txtboxemail);
        passwordtb = (EditText)findViewById(R.id.txtboxpassword);

        labelnewuser=(TitanicTextView)findViewById(R.id.newuserlbl);

        sumbitbtnlg = (Button)findViewById(R.id.lgbtnsubmit);
        //on Clicking Sumbit button
        sumbitbtnlg.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                userid = uidtb.getText().toString();
                password = passwordtb.getText().toString();
                Log.d(tag, "userid is ---> " + userid);
                Log.d(tag, "password is ---> " + password);
                    if (checkNetworkConnection() && validinfo())
                    {
                        //final View focusView = null;
                        Log.d(tag, "-----user id is -->" + userid);
                        Log.d(tag, "---->password is --->" + password);
                        Log.d(tag, "in (if) onClick(showbtn)-->");
                        String site = ".cloudant.com";
                        String dbname = "/userinfo/";
                        String url = "https://verifier" + site + dbname + userid;
                        Log.d(tag, "is the url im trying to get data from\n" + url);
                        new JsonTask().execute(url);
                    }
                    else
                    {
                        final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.WARNING_TYPE);
                        pDialog.setTitleText("Error");
                        pDialog.setContentText("Invalid Credentials or No Internet Access");
                        pDialog.setConfirmText("Try again");
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                        {
                            @Override
                            public void onClick(SweetAlertDialog sDialog)
                            {
                                sDialog.dismissWithAnimation();
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                finish();
                            }
                        })
                                .show();
                    }

            }
            private boolean validinfo()
            {
                String a=uidtb.getText().toString();
                String b=passwordtb.getText().toString();
                Log.d(tag,"-------> a====>>"+a);
                Log.d(tag,"-------> b====>>"+b);

                if(a.equalsIgnoreCase("")||b.equalsIgnoreCase(""))
                {
                    return false;
                }
                else
                    return true;
            }

            private boolean checkNetworkConnection()
            {
                Log.d(tag, "in checkNetworkConnection()-->");
                NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected() || uidtb==null)
                {
                    Log.d(tag, "in (if) checkNetworkConnection()-->");
                    return false;
                }
                else
                {
                    Log.d(tag, "in (else) checkNetworkConnection()-->");
                    return true;
                }
            }
        });

        labelnewuser.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivityForResult(intent,1);
                overridePendingTransition(R.anim.righttoleft,R.anim.lefttoright);
            }
        });
    }



    private class JsonTask extends AsyncTask<String, String, String>
    {

        AlertDialog pd1 = new SpotsDialog(LoginActivity.this,R.style.Custom);
        protected void onPreExecute()
        {
            Log.d(tag, "in onPreExecute()-->");
            super.onPreExecute();
            pd1.setCancelable(false);
            pd1.show();
            Log.d(tag, "out onPreExecute() <--");
        }

        protected String doInBackground(String... params) throws NullPointerException {
            Log.d(tag, "in doInBackground()-->");
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try
            {
                Log.d(tag, "in (try) doInBackground()-->");
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
                Log.d(tag, "out (try) doInBackground() <--");
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
            Log.d(tag, "out doInBackground() <--");
            return null;
        }

        public boolean invalidjson()
        {
            Log.d(tag,"in checking valid json or not");
            if(jsondata==null)
            {
                Log.d(tag,"Since json data is null it will now return false");
                return true;
            }
                else
               return false;
        }
        protected void onPostExecute(String result)
        {

            Log.d(tag,"-------> Result before postexe is ---> "+result);
            Log.d(tag, "in onPostExecute()-->");
            super.onPostExecute(result);
            if (pd1.isShowing())
            {
                pd1.dismiss();
            }
            jsondata=result;
            Log.d(tag,"jsondata value is--->"+jsondata);
            if(invalidjson())
            {
                Log.d(tag,"in invalid json if");
                final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText("Error");
                pDialog.setContentText("Incorrect UserId and Password");
                pDialog.setConfirmText("Try again");
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                {
                    @Override
                    public void onClick(SweetAlertDialog sDialog)
                    {
                        sDialog.dismissWithAnimation();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    }
                })
                        .show();
            }
            else
            {
                try {
                    JSONObject object = new JSONObject(jsondata);
                    attr1 = object.getString("_id");
                    attr3 = object.getString("password");

                    Log.d(tag, "---->>> get string of _id is---->" + attr1);
                    Log.d(tag, "---->>> get string of password is---->" + attr3);


                    if (attr1.equalsIgnoreCase(userid) && attr3.equalsIgnoreCase(password) && attr1 != null) {
                        Log.d(tag, "------>inside of  if(attr1==userid && attr3==password)----->");
                        Intent i = new Intent(LoginActivity.this, UserinfoActivity.class);
                        startActivityForResult(i, 1);
                        overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);
                    }
                    else
                    {
                        Log.d(tag, "------>inside of  else(attr1==userid && attr3==password)----->");
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("Error");
                        builder.setMessage("Incorrect UserId and Password")
                                .setPositiveButton("Tryagain", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(i);
                                    }
                                })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.d(tag, "out onPostExecute() <--");
        }


    }


    //When back Button Pressed
    public void onBackPressed()
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        pDialog.setTitleText("Quit");
        pDialog.setContentText("You wanna get away from me :( ");
        pDialog.setConfirmText("Yes,I want to !");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog)
                    {
                        sDialog.dismissWithAnimation();
                        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                    }
                });
                 pDialog.setCancelText("No")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog)
                    {
                        pDialog.cancel();
                    }
                })
                .show();

    }
}
