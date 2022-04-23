package test.example.com.verifierclient;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;


//Start of QRCODE Download Activity
public class QrcodedownloadActivity extends RegistrationActivity
{
    public static final String tag = "QrcodedownloadActivity";
    ImageView iView;
    public static String qrurl="https://qrcode.loresoft.de/interface/qrcode?level=l&size=5&content=";
    public static Bitmap bitmap;
    public static String completeqrurl;
    String pathstored;
    Button btngetqr;
    public static  String qrcodetemp;
    String base64id;

    //Getting Uid and Name from Registration Activity
    public String name=RegistrationActivity.name;
    public String uid=RegistrationActivity.uid;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rcodedownloadactivity);
        iView = (ImageView) findViewById(R.id.qrcodeimg);
        btngetqr=(Button)findViewById(R.id.getqrbtn);
        Log.d("User id in QRcode download activity",   uid);

        //Setting Download label with diff font and animating
        TitanicTextView labeldownload = (TitanicTextView)findViewById(R.id.downloadlbl);
        Typeface font1 = Typeface.createFromAsset(getAssets(),  "fonts/DroidSerif-Regular.ttf");
        labeldownload.setTypeface(font1);

        TitanicTextView labelname  = (TitanicTextView)findViewById(R.id.namelblvishqrdownloadact);
        Typeface font2 = Typeface.createFromAsset(getAssets(),  "fonts/ufonts.com_lucia-bt.ttf");
        labelname.setTypeface(font2);

        Titanic titanic=new Titanic();
        titanic.start(labelname);
        titanic.start(labeldownload);

        //encoding uid in base64 and concatinating with the qrgeneratorurl
        byte[] data;
        String tempqrdata = uid;
        Log.d(tag,"Tempqrdata is uid now = -->"+tempqrdata);
        try
        {
            data = tempqrdata.getBytes("UTF-8");
            qrcodetemp = Base64.encodeToString(data, Base64.DEFAULT);
            //qrcode is base64 format of name and uid of the user
            Log.d(tag,"Base64 encoded string of Name & Uid is --> "+qrcodetemp);
            completeqrurl=qrurl+qrcodetemp;
            base64id=qrcodetemp;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Log.d(tag,"Final URL to download Image is --->"+completeqrurl);

        //Start of <<Get Qr>> Button Click
        btngetqr.setOnClickListener(new View.OnClickListener()
        {
                //checking internet access
                private boolean checkNetworkConnection()
                {
                    Log.d(tag, "In checkNetworkConnection()-->");
                    NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                    if (networkInfo == null || !networkInfo.isConnected())
                    {
                        final SweetAlertDialog pDialog = new SweetAlertDialog(QrcodedownloadActivity.this,SweetAlertDialog.WARNING_TYPE);
                        pDialog.setTitleText("No Internet Connection");
                        pDialog.setContentText("Please make sure you have working Internet connection");
                        pDialog.setConfirmText("Try again");
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                        {
                            @Override
                            public void onClick(SweetAlertDialog sDialog)
                            {
                                sDialog.dismissWithAnimation();
                            }
                        })
                                .show();
                        Log.d(tag, "In (if) checkNetworkConnection()-->");
                        return false;
                    }
                    else
                    {
                        Log.d(tag, "In (else) checkNetworkConnection()-->");
                        return true;
                    }
                }
                @Override
                public void onClick(View v)
                {
                    if(checkNetworkConnection())
                    {
                        new DownLoadImageTask(iView).execute(completeqrurl);
                        new saveImageToInternalStorageTask().execute(completeqrurl);
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Network is not Available", Toast.LENGTH_SHORT).show();
                    }
                }
        });
        //End of <<Get Qr>> Button Click
    }



    //Seperate Thread to set the Qr image in the Imageview of Qrdownloadactivity
    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap>
    {
        ImageView asyncimg;
        AlertDialog pd1 = new SpotsDialog(QrcodedownloadActivity.this,R.style.Custom);
        //DownloadImageTask PreExecute
        protected void onPreExecute()
        {
            Log.d(tag, "In onPreExecute ->- DownLoadImageTask -->");
            pd1.setCancelable(false);
            pd1.show();
            Log.d(tag, "Out from onPreExecute -<- DownLoadImageTasks <--");
        }

        private DownLoadImageTask(ImageView imageView)
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
    //End of DownloadImageTask



    //Seperate Thread to SaveImageToInternal/External Storage if present
    private class saveImageToInternalStorageTask extends AsyncTask<String,Void,Bitmap>
    {
        String urlOfImage = "https://qrcode.loresoft.de/interface/qrcode?level=l&size=5&content=" + base64id;
        AlertDialog pd1 = new SpotsDialog(QrcodedownloadActivity.this,R.style.Custom);
        protected void onPreExecute()
        {
            Log.d(tag, "In onPreExecute ->- saveImageToInternalStorageTask -->");
            pd1.setCancelable(false);
            pd1.show();
            Log.d(tag, "Out from onPreExecute -<- saveImageToInternalStorageTask <--");
        }


        protected Bitmap doInBackground(String...urls)
        {
            try
            {
                Log.d(tag, "In doInBackground ->- saveImageToInternalStorageTask -->");
                Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
                if (isSDPresent)
                {
                    Log.d(tag,"In (If) isSDPresent ->- doInBackground ->- saveImageToInternalStorageTask -->");
                    File direct = new File(Environment.getExternalStorageDirectory() + "/VerifierClient");
                    if (!direct.exists())
                    {
                        File wallpaperDirectory = new File("/sdcard/VerifierClient");
                        wallpaperDirectory.mkdirs();
                    }

                    //path of where the downloaded qr exist
                    pathstored = direct.toString();
                    //If an image with same name exists delete it
                    File file = new File(direct, name + ".jpg");
                    if (file.exists())
                    {
                        file.delete();
                    }
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
                            //Log.i("Progress:", "downloadedSize:" + downloadedSize + "totalSize:" + totalSize);
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

                    File file = new File(direct, name+ ".jpg");
                    if (file.exists()) {
                        file.delete();
                    }


                    Log.d(tag, "Image saved In Internal memory -->");
                    try {
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

        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        overridePendingTransition(R.anim.righttoleft,R.anim.lefttoright);

    }
   //End of QRCODE Download Activity
}
