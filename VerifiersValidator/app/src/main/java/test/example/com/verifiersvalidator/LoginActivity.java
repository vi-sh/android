package test.example.com.verifiersvalidator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class LoginActivity extends Activity
{
    public static final String tag = "LoginActivity Verifier Validator";
    private EditText tbusername;
    private EditText tbpassword;
    public Button submitbtn;
    public static String username;
    public static String password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);

        // Set up the login form.
        tbusername = (EditText) findViewById(R.id.usernametxtbox);
        tbpassword = (EditText) findViewById(R.id.passwordtxtbox);

        //typeface setting for LOGIN LABEL
        TitanicTextView labellogin = (TitanicTextView)findViewById(R.id.loginlbl);
        Typeface font1 = Typeface.createFromAsset(getAssets(),  "fonts/DroidSerif-Regular.ttf");
        labellogin.setTypeface(font1);

        //typeface for VISH label
        TitanicTextView labelname  = (TitanicTextView)findViewById(R.id.namelbl);
        Typeface font2 = Typeface.createFromAsset(getAssets(),  "fonts/ufonts.com_lucia-bt.ttf");
        labelname.setTypeface(font2);


        //Animating LOGIN and VISH
        Titanic titanic=new Titanic();
        titanic.start(labellogin);
        titanic.start(labelname);



        submitbtn = (Button) findViewById(R.id.btnsubmit);
        submitbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                username = tbusername.getText().toString();
                password = tbpassword.getText().toString();

                if (checkNetworkConnection())
                {
                    if(isvalidinfo())
                    {

                        Intent i = new Intent(LoginActivity.this,ValidatingActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);

                    }
                    else
                    {
                        final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.WARNING_TYPE);
                        pDialog.setTitleText("Error");
                        pDialog.setContentText("Invalid Credentials");
                        pDialog.setConfirmText("Try again");
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                        {
                            @Override
                            public void onClick(SweetAlertDialog sDialog)
                            {
                                sDialog.dismissWithAnimation();
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        })
                                .show();
                    }
                }
                else
                {
                    final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.WARNING_TYPE);
                    pDialog.setTitleText("Error");
                    pDialog.setContentText("No Internet Access");
                    pDialog.setConfirmText("Try again");
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                    {
                        @Override
                        public void onClick(SweetAlertDialog sDialog)
                        {
                            sDialog.dismissWithAnimation();
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    })
                            .show();
                }
            }


            private boolean checkNetworkConnection()
            {
                Log.d(tag, "in checkNetworkConnection()-->");
                NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected())
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

            private boolean isvalidinfo()
            {
                Log.d(tag, "in valid info()-->");

                if (username.equalsIgnoreCase("verifier") && password.equalsIgnoreCase("admin123"))
                {
                    Log.d(tag, "in (if) isvalidinfo()-->");
                    return true;
                }
                else
                {
                    Log.d(tag, "in (else) isvalidinfo()-->");
                    return false;
                }
            }
        });

    }
}
