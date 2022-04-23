package test.example.com.verifierclient;

import android.app.Activity;
import android.app.AlertDialog;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;


//Start of Registration Activity
public class RegistrationActivity extends Activity
{
    private static final String tag = "RegistrationActivity client";
    public static String uid,name,phno,pwd,local,comp,uniqueid;
    EditText reguid,regname,regphno,regpwd,reregpwd,reglocal,regcomp;
    TitanicTextView labelmember;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrationlayout);
        Button registersubmitbtn;
        registersubmitbtn = (Button) findViewById(R.id.regbtnsubmit);
        reguid=(EditText)findViewById(R.id.reguidtxt);
        regname=(EditText)findViewById(R.id.regnametxt);
        regpwd=(EditText)findViewById(R.id.regpasswordtxt);
        reregpwd=(EditText)findViewById(R.id.reregpasswordtxt);
        regphno=(EditText)findViewById(R.id.regphnotxt);
        reglocal=(EditText)findViewById(R.id.reglocaltxt);
        regcomp=(EditText)findViewById(R.id.regcomptxt);
        labelmember=(TitanicTextView) findViewById(R.id.memberlbl);


        //setting Registration with diff font and animating
        TitanicTextView labelregister = (TitanicTextView)findViewById(R.id.reglbl);
        Typeface font1 = Typeface.createFromAsset(getAssets(),  "fonts/DroidSerif-Regular.ttf");
        labelregister.setTypeface(font1);

        //setting Vish with diff font and animating
        TitanicTextView labelname  = (TitanicTextView)findViewById(R.id.namelblvishregact);
        Typeface font2 = Typeface.createFromAsset(getAssets(),  "fonts/ufonts.com_lucia-bt.ttf");
        labelname.setTypeface(font2);

        //Animating LOGIN and VISH
        Titanic titanic=new Titanic();
        titanic.start(labelregister);
        titanic.start(labelname);

        //Start of <<Submit>> Button Click
        registersubmitbtn.setOnClickListener(new View.OnClickListener()
        {
            //checking whether any field are empty if yes displaying error message
            private boolean isallfieldsfilled()
            {
                boolean var;
                if(reguid.getText().toString().isEmpty())
                {
                    reguid.requestFocus();
                    reguid.setError("Username cannot be empty");
                    var=false;
                }
                else if(regname.getText().toString().isEmpty())
                {
                    regname.requestFocus();
                    regname.setError("Name cannot be empty");
                    var=false;
                }
                else if(regpwd.getText().toString().isEmpty())
                {
                    regpwd.requestFocus();
                    regpwd.setError("Password cannot be empty");
                    var=false;
                }
                else if(reregpwd.getText().toString().isEmpty())
                {
                    reregpwd.requestFocus();
                    reregpwd.setError("Password cannot be empty");
                    var=false;
                }
                else if(regphno.getText().toString().isEmpty())
                {
                    regphno.requestFocus();
                    regphno.setError("Phone number cannot be empty");
                    var=false;
                }
                else if(reglocal.getText().toString().isEmpty())
                {
                    reglocal.requestFocus();
                    reglocal.setError("Locality cannot be empty");
                    var=false;
                }
                else if(regcomp.getText().toString().isEmpty())
                {
                    regcomp.requestFocus();
                    regcomp.setError("Comapany cannot be empty");
                    var=false;
                }
                else
                {
                    var=true;
                }
                return var;
            }

            //checking password mismatching
            private boolean ispasswordmatching()
            {
                boolean var;
                if(regpwd.getText().toString().equalsIgnoreCase(reregpwd.getText().toString()))
                {
                    var=true;
                }
                else
                {
                    var=false;
                }
                return var;
            }

            //checking internet access
            private boolean isnetworkavailable()
            {
                boolean var=true;
                Log.d(tag, "In checkNetworkConnection()-->");
                NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected())
                {
                    Log.d(tag, "In (if) checkNetworkConnection()-->");
                    var=false;
                    return var;
                }
                else
                {
                    var=true;
                    return var;
                }
            }


            @Override
            public void onClick(View v)
            {
                if(isallfieldsfilled())
                {
                    if(ispasswordmatching())
                    {
                        if (isnetworkavailable())
                        {
                            uid = reguid.getText().toString();
                            name = regname.getText().toString();
                            pwd = regpwd.getText().toString();
                            phno = regphno.getText().toString();
                            local = reglocal.getText().toString();
                            comp = regcomp.getText().toString();
                            uniqueid = uid;
                            Log.d(uid, "uid is--------------");
                            Log.d(name, "name is--------------");
                            Log.d(pwd, "Password is--------------");
                            Log.d(phno, "phno is--------------");
                            Log.d(local, "locality is--------------");
                            Log.d(comp, "Company is--------------");
                            new PostJsonTask().execute("https://verifier.cloudant.com/userinfo");
                        }
                        else
                        {
                            final SweetAlertDialog pDialog = new SweetAlertDialog(RegistrationActivity.this, SweetAlertDialog.WARNING_TYPE);
                            pDialog.setTitleText("Error");
                            pDialog.setContentText("No Internet Access");
                            pDialog.setConfirmText("Try Again");
                            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                }
                            })
                                    .show();
                        }

                    }
                    else
                    {
                        reregpwd.requestFocus();
                        reregpwd.setError("password mismatch !");
                    }
                }
            }
        });
        //End of <<Submit>> Button Click


        //On Clicking the text of Aleardy a Member ?

        labelmember.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivityForResult(intent,1);
                overridePendingTransition(R.anim.lefttoright,R.anim.righttoleft);
            }
        });

        if (getIntent().getBooleanExtra("EXIT", false))
        {
            finish();
        }


    }


    //Seperate Thread to Post Json data to the Url inRegistration Activity
    public class PostJsonTask extends AsyncTask<String, Void, String>
    {
        AlertDialog pd1 = new SpotsDialog(RegistrationActivity.this,R.style.Custom);

        //PostJsonTask PreExecute
        protected void onPreExecute()
        {
            Log.d(tag, "In onPreExecute ->- PostJsonTask -->");
            super.onPreExecute();
            pd1.setCancelable(false);
            pd1.show();
            Log.d(tag, "Out from onPreExecute -<- PostJsonTask <--");
        }

        //PostJsonTask doInBackground
        @Override
        protected String doInBackground(String... params)
        {
            String result1;
            Log.d(tag, "In doInBackground ->- PostJsonTask -->");
            Empinfo empinfo = new Empinfo();
            empinfo.setId(uid);
            empinfo.setName(name);
            empinfo.setPwd(pwd);
            empinfo.setPhno(phno);
            empinfo.setLocality(local);
            empinfo.setCompany(comp);
            Log.d(tag, "Out from doInBackground -<- PostJsonTask <--");
            result1=POST(params[0],empinfo);
            Log.d(tag,"-------))))) Result 1 is----)"+result1);
            return result1;
        }

        //PostJsonTask onPostExecute
        @Override
        protected void onPostExecute(String params)
        {
            super.onPostExecute(params);
            if (pd1.isShowing())
            {
                pd1.dismiss();
            }
            String errorvariable="{\"error\":\"conflict\",\"reason\":\"Document update conflict.\"}";
            String nullvariable="{\"error\":\"illegal_docid\",\"reason\":\"Document id must not be empty\"}";
            Log.d(tag, "In onPostExecute ->- PostJsonTask -->");
            Log.d(tag,"----->parameter value in postexecute ->- PostJsonTask -->"+params);
            if(params.equalsIgnoreCase(nullvariable))
            {
                Log.d(tag,"------------------------->>>in if part of params==null");
                final SweetAlertDialog pDialog = new SweetAlertDialog(RegistrationActivity.this,SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText("Error");
                pDialog.setContentText("No Internet Access or Empty entries ");
                pDialog.setConfirmText("Try Again");
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                {
                    @Override
                    public void onClick(SweetAlertDialog sDialog)
                    {
                        sDialog.dismissWithAnimation();
                        Intent i=new Intent(getApplicationContext(),RegistrationActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                })
                .show();
            }
            else if(params.equalsIgnoreCase(errorvariable))
            {
                final SweetAlertDialog pDialog = new SweetAlertDialog(RegistrationActivity.this, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText("User already Exist");
                pDialog.setContentText("Continue Logging in ?");
                pDialog.setConfirmText("Yes");
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog)
                    {
                        sDialog.dismissWithAnimation();
                        Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivityForResult(i,1);
                        overridePendingTransition(R.anim.lefttoright,R.anim.righttoleft);
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
            else
            {
                final SweetAlertDialog pDialog = new SweetAlertDialog(RegistrationActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                pDialog.setTitleText("Success");
                pDialog.setContentText("User Registered, Download the QR code for further Authentication ");
                pDialog.setConfirmText("Okie :)");
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                {
                    @Override
                    public void onClick(SweetAlertDialog sDialog)
                    {
                        sDialog.dismissWithAnimation();
                        Intent i = new Intent(RegistrationActivity.this, QrcodedownloadActivity.class);
                        startActivityForResult(i,1);
                        overridePendingTransition(R.anim.lefttoright,R.anim.righttoleft);
                    }
                })
                        .show();
            }
            Log.d(tag, "Out from onPostExecute -<- PostJsonTask <--");
        }
    }


    public  String POST(String url, Empinfo empinfo)
    {
        Log.d(tag,"--->In POST method ->- doInBackground ->- PostJsonTask --->");
        String result=null;
        InputStream inputStream = null;
        try
        {
                HttpClient httpclient = new DefaultHttpClient();
                // make POST request to the given URL
                HttpPost httpPost = new HttpPost(url);
                String json = "";
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("_id", empinfo.getId());
                jsonObject.accumulate("name", empinfo.getName());
                jsonObject.accumulate("password", empinfo.getPwd());
                jsonObject.accumulate("phno", empinfo.getPhno());
                jsonObject.accumulate("locality", empinfo.getLocality());
                jsonObject.accumulate("Company", empinfo.getCompany());
                json = jsonObject.toString();
                Log.d(json, "--------> is the DATA to be POSTED");
                Log.d(url, "---------> is the URL for the DATA to be POSTED");

                //Implementations
                // set json to StringEntity

                StringEntity se = new StringEntity(json);
                // set httpPost Entity
                httpPost.setEntity(se);
                // Set some headers to inform server about the type of the content
                httpPost.setHeader("Accept", "*/*");
                httpPost.setHeader("Content-type", "application/json");
                // Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPost);
                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

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
                finish();
                System.exit(0);
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
    //End of Registration Activity
}

