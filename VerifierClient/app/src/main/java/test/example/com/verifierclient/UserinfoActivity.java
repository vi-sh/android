package test.example.com.verifierclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

public class UserinfoActivity extends Activity
{
    public String jsondata=LoginActivity.jsondata;
    private static final String tag = "UserInfoActivity";

    TitanicTextView labelnamevish,labelwelcomeuser;
    TextView idtxt, nametxt, phnotxt, localtxt, comptxt;
    String id;
    String name;
    String password;
    String phno;
    String locality;
    String company;
    Button btngetqr;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfoactivity);

        labelwelcomeuser = (TitanicTextView) findViewById(R.id.welcomeuserlbl);
        labelnamevish = (TitanicTextView) findViewById(R.id.namelblvishuserinfoact);

        btngetqr = (Button)findViewById(R.id.getqrbtnuserinfo);

        idtxt = (TextView) findViewById(R.id.idtext);
        nametxt = (TextView) findViewById(R.id.nametxt);
        phnotxt = (TextView) findViewById(R.id.phnotxt);
        localtxt = (TextView) findViewById(R.id.localtxt);
        comptxt = (TextView) findViewById(R.id.comptxt);


        Typeface font1 = Typeface.createFromAsset(getAssets(), "fonts/ufonts.com_lucia-bt.ttf");
        labelnamevish.setTypeface(font1);
        Typeface font3 = Typeface.createFromAsset(getAssets(), "fonts/DroidSerif-Regular.ttf");
        labelwelcomeuser.setTypeface(font3);

        //Animating LOGIN and VISH
       Titanic titanic = new Titanic();
        titanic.start(labelnamevish);
        titanic.start(labelwelcomeuser);


        //taking json data from the login activity which we get and is used to retrieve the value of (id name phno locality and company)
        // so that we can display them in the TextView
        new JsonTask().execute(jsondata);

        btngetqr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(UserinfoActivity.this,DisplayQrActivity.class);
                startActivityForResult(i,1);
                overridePendingTransition(R.anim.lefttoright,R.anim.righttoleft);
            }
        });
    }



    private class JsonTask extends AsyncTask<String, String, String>
    {
        AlertDialog pd1 = new SpotsDialog(UserinfoActivity.this,R.style.Custom);
        protected void onPreExecute()
        {
            Log.d(tag, "in onPreExecute()-->");
            super.onPreExecute();
            pd1.setCancelable(false);
            pd1.show();
            Log.d(tag, "out onPreExecute() <--");
        }

        protected String doInBackground(String... params)
        {
            try
            {
                Log.d(tag,"JsonDAta string is ---->"+jsondata);
                //here i omit attr3 which is password since i dont have to show password
                JSONObject object = new JSONObject(jsondata);
                String attr1 = object.getString("_id");
                String attr2 = object.getString("name");
                String attr4 = object.getString("phno");
                String attr5 = object.getString("locality");
                String attr6 = object.getString("Company");

                id=attr1;
                name=attr2;
                phno=attr4;
                locality=attr5;
                company=attr6;

                Log.d(tag,"id,name,phno,locality,company are ---->"+attr1+"-"+attr2+"-"+attr4+"-"+attr5+"-"+attr6);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result)
        {
            //setting all the id,name phno,locality and company string to respective TextViews
            idtxt.setText(id);
            nametxt.setText(name);
            phnotxt.setText(phno);
            localtxt.setText(locality);
            comptxt.setText(company);
            labelwelcomeuser.setText("Welcome\n"+name);
            if (pd1.isShowing())
            {
                pd1.dismiss();
            }
        }
    }

    //When back Button Pressed
    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        overridePendingTransition(R.anim.righttoleft,R.anim.lefttoright);
    }
}
