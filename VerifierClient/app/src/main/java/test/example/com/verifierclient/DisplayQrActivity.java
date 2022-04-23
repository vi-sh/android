package test.example.com.verifierclient;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

public class DisplayQrActivity extends Activity
{

    private static final String tag = "DisplayQRActivity";
    public String jsondata = LoginActivity.jsondata;
    TitanicTextView labeluserqr,labelvishdisplayuserinfo;
    ImageView imgshowqr;
    String str1,id;
    String str2,name;
    String str3;
    String str4,phno;
    String str5,locality;
    String str6,company;
    String path;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String qrurl="https://qrcode.loresoft.de/interface/qrcode?level=l&size=5&content=";
    public static String completeqrurl;
    public static  String qrcodetemp;
    String base64id;
    String pathstored;
    TitanicTextView labelbacktoinfo;
    public static Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displayqractivity);

        //setting and animating User's QR label
        labeluserqr = (TitanicTextView) findViewById(R.id.userqrlbl);
        labelvishdisplayuserinfo=(TitanicTextView)findViewById(R.id.namelblvishdisplayuserinfoact);
        Typeface font1 = Typeface.createFromAsset(getAssets(), "fonts/DroidSerif-Regular.ttf");
        labeluserqr.setTypeface(font1);
        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/ufonts.com_lucia-bt.ttf");
        labelvishdisplayuserinfo.setTypeface(font2);


        Titanic titanic = new Titanic();
        titanic.start(labeluserqr);
        titanic.start(labelvishdisplayuserinfo);

        verifyStoragePermissions(DisplayQrActivity.this);
        imgshowqr = (ImageView) findViewById(R.id.showqrimg);
        labelbacktoinfo=(TitanicTextView)findViewById(R.id.backtoinfo);

        //Task 1
        new JsonTask().execute(jsondata);

        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (isSDPresent)
        {
            Log.d(tag,"in Sd present");
            path = Environment.getExternalStorageDirectory()+"/VerifierClient/"+name+".jpg";
            Log.d(tag,"----sd path is ---<"+path);
            File imgFile = new File(path);
            if (imgFile.exists())
            {
               // Log.d(tag,"Qr code exist in Sd card and the path is --->"+path);
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imgshowqr.setImageBitmap(myBitmap);
            }
            else
            {
                Log.d(tag,"Qr code doesnt exist !!! --->");
                final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText("Your QR-code is deleted !!");
                pDialog.setContentText("ReDownload QR code ? ");
                pDialog.setConfirmText("Yes,I want to ");
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                {
                    @Override
                    public void onClick(SweetAlertDialog sDialog)
                    {
                        sDialog.dismissWithAnimation();
                        new DownLoadQrSettoImageView(imgshowqr).execute(completeqrurl);
                        new saveImageToInternalStorageTask().execute(completeqrurl);
                    }
                });
                pDialog.setCancelText("No Sorry :)")
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
        else
        {
            Log.d(tag,"in Internal memory and its present");
            String path = Environment.getDataDirectory() + "/VerifierClient/"+str2+".jpg";
            Log.d(tag,"----Internal memory path is ---<"+path);
            File imgFile = new File(path);
            if (imgFile.exists())
            {
                //Log.d(tag,"Qr code exist in internal memory and the path is --->"+path);
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imgshowqr.setImageBitmap(myBitmap);
            }
            else
            {
                Log.d(tag,"Qr code exist doesnt exist !!! --->");
                final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText("Your QR-code is deleted !!");
                pDialog.setContentText("ReDownload QR code ? ");
                pDialog.setConfirmText("Yes,I want to ");
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                {
                    @Override
                    public void onClick(SweetAlertDialog sDialog)
                    {
                        sDialog.dismissWithAnimation();
                        new DownLoadQrSettoImageView(imgshowqr).execute(completeqrurl);
                        new saveImageToInternalStorageTask().execute(completeqrurl);
                    }
                });
                pDialog.setCancelText("No Sorry :)")
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


        labelbacktoinfo.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(DisplayQrActivity.this, UserinfoActivity.class);
                startActivityForResult(intent,200);
                overridePendingTransition(R.anim.righttoleft,R.anim.lefttoright);
            }
        });

    }



    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public static void verifyStoragePermissions(Activity activity)
    {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    //Thread 1: This thread get the Json data for particular Person who entered the data
    //i.e to get Id ,name,phno,locality and company info for further processing
    private class JsonTask extends AsyncTask<String, String, String>
    {
        AlertDialog pd1 = new SpotsDialog(DisplayQrActivity.this,R.style.Custom);

        protected void onPreExecute()
        {
            Log.d(tag, "in onPreExecute()-->");
            super.onPreExecute();
            pd1.setCancelable(false);
            pd1.show();
            Log.d(tag, "out onPreExecute() <--");
        }

        protected String doInBackground(String... params) {
            try {
                Log.d(tag, "JsonData string is ---->" + jsondata);
                //here i omit attr3 which is password since i dont have to show password
                JSONObject object = new JSONObject(jsondata);
                String attr1 = object.getString("_id");
                String attr2 = object.getString("name");
                String attr4 = object.getString("phno");
                String attr5 = object.getString("locality");
                String attr6 = object.getString("Company");

                str1 = attr1;
                str2 = attr2;
                str4 = attr4;
                str5 = attr5;
                str6 = attr6;
                //copying id name phno locality and company from str named variable to proper named variables i.e ID NAME etc
                id=str1;
                name=str2;
                phno=str4;
                locality=str5;
                company=str6;

                Log.d(tag, "id,name,phno,locality,company are ---->" + attr1 + "-" + attr2 + "-" + attr4 + "-" + attr5 + "-" + attr6);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result)
        {

            byte[] data;
            String tempqrdata = (id);
            Log.d(tag,"Name of the user to be base64 ed is-->"+tempqrdata);
            try
            {
                //here im checking whether the ID i got from the json is not null
                if(tempqrdata!=null)
                {
                    data = tempqrdata.getBytes("UTF-8");
                    qrcodetemp = Base64.encodeToString(data, Base64.DEFAULT);
                    //qrcode is base64 format of name and uid of the user
                    Log.d(tag, "Base64 encoded string of Uid is --> " + qrcodetemp);
                    completeqrurl = qrurl + qrcodetemp;
                    base64id = qrcodetemp;
                }
                else
                {
                    AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DisplayQrActivity.this);
                    builder.setTitle("Error Fetching data");
                    builder.setMessage("Please Try again")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);

                                }
                            });
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            Log.d(tag,"Final URL to download Image is --->"+completeqrurl);
            if (pd1.isShowing()) {
                pd1.dismiss();
            }
        }

    }

    //Thread 2: Downloading QR code if the existing QR code from sdcard or Internal memory from VerifierClient has been deleted
    //Seperate Thread to set the Qr image in the Imageview of Qrdownloadactivity
    private class DownLoadQrSettoImageView extends AsyncTask<String,Void,Bitmap>
    {
        ImageView asyncimg;
        AlertDialog pd1 = new SpotsDialog(DisplayQrActivity.this,R.style.Custom);
        //DownloadImageTask PreExecute
        protected void onPreExecute()
        {
            Log.d(tag, "In onPreExecute ->- DownLoadImageTask -->");
            pd1.setCancelable(false);
            pd1.show();
            Log.d(tag, "Out from onPreExecute -<- DownLoadImageTasks <--");
        }

        private DownLoadQrSettoImageView(ImageView imageView)
        {
            this.asyncimg = imageView;
        }

        //DownloadImageTask doInBackground
        protected Bitmap doInBackground(String...urls)
        {
            Log.d(tag, "In doInBackground ->- DownLoadImageTask -->");
            Log.d(tag,"base64id---"+base64id);
            String urlOfImage = "https://qrcode.loresoft.de/interface/qrcode?level=l&size=5&content="+base64id;
            Log.d(tag,"complete url with extention is---"+urlOfImage);
            Bitmap logo = null;
            try
            {
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
                is.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            Log.d(tag, "Out from doInBackground -<- DownLoadImageTasks <--");
            return logo;
        }

        //DownloadImageTask onPostExecute
        protected void onPostExecute(Bitmap result)
        {
            Log.d(tag, "In onPostExecute ->- DownLoadImageTask -->");
            asyncimg.setImageBitmap(result);
            if (pd1.isShowing())
            {
                pd1.dismiss();
            }
            Log.d(tag, "Out from onPostExecute -<- DownLoadImageTasks <--");
        }
    }




    //Thread 3: This thread is to save the QR code which is downloaded since it was deleted and asked for REdeownload
    private class saveImageToInternalStorageTask extends AsyncTask<String,Void,Bitmap>
    {
        String urlOfImage = "https://qrcode.loresoft.de/interface/qrcode?level=l&size=5&content=" + base64id;
        AlertDialog pd1 = new SpotsDialog(DisplayQrActivity.this,R.style.Custom);
        protected void onPreExecute()
        {
            Log.d(tag, "In onPreExecute ->- saveImageToInternalStorageTask -->");
            pd1.setCancelable(false);
            pd1.show();
            Log.d(tag, "Out from onPreExecute -<- saveImageToInternalStorageTask <--");
        }


        protected Bitmap doInBackground(String...urls)
        {
            try {
                Log.d(tag, "In doInBackground ->- saveImageToInternalStorageTask -->");

                Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
                if (isSDPresent)
                {
                    Log.d(tag,"In (If) isSDPresent ->- doInBackground ->- saveImageToInternalStorageTask -->");
                    File direct = new File("/sdcard/VerifierClient");
                    if (!direct.exists())
                    {
                        Log.d(tag,"In directory not exist--->");
                        File wallpaperDirectory = new File("/sdcard/VerifierClient");
                        wallpaperDirectory.mkdirs();
                    }

                    //path of where the downloaded qr exist
                    pathstored = direct.toString();

                    //If an image with same name exists delete it
                    File file = new File(direct, name+".jpg");

                    try
                    {
                        URL url = new URL(urlOfImage);
                        URLConnection ucon = url.openConnection();
                        InputStream inputStream = null;
                        HttpURLConnection httpConn = (HttpURLConnection) ucon;
                        httpConn.setRequestMethod("GET");
                        httpConn.connect();

                        if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK)
                        {
                            inputStream = httpConn.getInputStream();
                        }
                        FileOutputStream fos = new FileOutputStream(file);
                        int totalSize = httpConn.getContentLength();
                        int downloadedSize = 0;
                        byte[] buffer = new byte[1024];
                        int bufferLength = 0;
                        while ((bufferLength = inputStream.read(buffer)) > 0)
                        {
                            fos.write(buffer, 0, bufferLength);
                            downloadedSize += bufferLength;
                            Log.i("Progress:", "downloadedSize:" + downloadedSize + "totalSize:" + totalSize);
                        }
                        pathstored = direct.toString();
                        fos.close();
                        Log.d("test", "Image Saved in sdcard..at" + pathstored);

                    }
                    catch (IOException io)
                    {
                        io.printStackTrace();
                    }
                    Log.d(tag,"Out from (IF) isSDPresent -<- doInBackground <--");
                }


                else
                {
                    Log.d(tag,"In (ELSE) isSDPresent ->- doInBackground ->- saveImageToInternalStorageTask -->");
                    File direct = new File(Environment.getDataDirectory() + "/VerifierClient");
                    if (!direct.exists())
                    {
                        File wallpaperDirectory = new File("/VerifierClient");
                        wallpaperDirectory.mkdirs();
                    }
                    //path of where the downloaded qr exist
                    pathstored = direct.toString();

                    File file = new File(direct,name+".jpg");

                    Log.d(tag, "vish Im in Internal memory -->");
                    try {
                        URL url = new URL(urlOfImage);
                        URLConnection ucon = url.openConnection();
                        InputStream inputStream = null;
                        HttpURLConnection httpConn = (HttpURLConnection) ucon;
                        httpConn.setRequestMethod("GET");
                        httpConn.connect();

                        if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            inputStream = httpConn.getInputStream();
                        }

                        FileOutputStream fos = new FileOutputStream(file);
                        int totalSize = httpConn.getContentLength();
                        int downloadedSize = 0;
                        byte[] buffer = new byte[1024];
                        int bufferLength = 0;
                        while ((bufferLength = inputStream.read(buffer)) > 0)
                        {
                            fos.write(buffer, 0, bufferLength);
                            downloadedSize += bufferLength;
                            Log.i("Progress:", "downloadedSize:" + downloadedSize + "totalSize:" + totalSize);
                        }

                        pathstored = direct.toString();
                        fos.close();
                    }
                    catch (IOException io)
                    {
                        io.printStackTrace();
                    }
                    Log.d(tag,"Out from (ELSE) isSDPresent -<- doInBackground <--");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            Log.d(tag, "Out from doInBackground -<- saveImageToInternalStorageTask <--");
            return bitmap;

        }

        protected void onPostExecute(Bitmap result)
        {
            Log.d(tag, "In onPostExecute ->- saveImageToInternalStorageTask -->");
            if (pd1.isShowing())
            {
                pd1.dismiss();
            }
            Toast.makeText(getBaseContext(),"Qr downloaded", Toast.LENGTH_LONG).show();
            Log.d(tag, "Out from onPostExecute -<- saveImageToInternalStorageTask <--");
        }
    }
    //End of SaveImageToInternal


    //When back Button Pressed
    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(), UserinfoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        overridePendingTransition(R.anim.righttoleft,R.anim.lefttoright);

    }
}
