package edmt.dev.androidgridlayout;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class SplashActivity extends AppCompatActivity {

    public static ArrayList<String> spinnerArray ;
    public static ArrayList<String> spinnerArray2 ;
    public static ArrayList<String> colorIDs ;
    public static ArrayList<String> hcfIds ;
    public static JSONObject hcf_response ;
    public static HashMap<String,String> map;
    public static HashMap<String,String> color_map;
    private static int SPLASH_TIME_OUT = 2000;

    SessionManagement manager;

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        manager = new SessionManagement(getApplicationContext());
       // getWindow().setWindowAnimations();
        map = new HashMap<String, String>();
        color_map = new HashMap<String, String>();
        //http://cyphertextsolutions.com/awcs/dev/api/get_hcf_list.php
        //http://awcs.net.in/api/get_hcf_list.php
         AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://awcs.net.in/api/get_hcf_list.php"
                , new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //Toast.makeText(getApplicationContext(),"Getting HCF" , Toast.LENGTH_SHORT).show();
                        Log.i("mohit hcf response","" + response);
                        hcf_response = response;
                        Log.i("sizee","" + response.length());
                        String test = "";
                        String hcf_name = "";
                        String hcf_id="";
                        try {
                        /*json2 = response.getJSONObject("results");
                        String test = (String) json2.get("name");*/
                            spinnerArray = new ArrayList<String>();
                            hcfIds = new ArrayList<String>();
                            for(int i=1 ; i < 100; i++){

                                Log.i("mohit" , "i= " + i);
                                try {
                                    Log.i("mohit", "value at " + response.getString("" + i));
                                    test = response.getString("" + i);
                                }catch (Exception e){
                                    e.printStackTrace();
                                    Log.i("mohit" , "error " + e);
                                    break;
                                }
                                if(test == null)
                                {
                                    continue;
                                }else{

                                    JSONObject jsonobj = new JSONObject(test);
                                    hcf_name = jsonobj.getString("HCFName");
                                    hcf_id = jsonobj.getString("HCFID");
                                    Log.i("mohit", "hcf name " + hcf_name );
                                    spinnerArray.add(hcf_name);
                                    hcfIds.add(hcf_id);
                                    map.put(hcf_id,hcf_name);
                                }

                            }


                            /*ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (getApplicationContext(), android.R.layout.simple_spinner_item,
                                            spinnerArray); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_hcf.setAdapter(spinnerArrayAdapter);*/


                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("mohit","error aagya" + e);
                        }

                        Log.i("mohit" , "spinner array  " + spinnerArray);
                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        //verridePendingTransition(R.anim.gototop , R.anim.gototop);
                        startActivity(i);
                        finish();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(getApplicationContext(),"Connection Problem - Please Try Again" , Toast.LENGTH_LONG).show();

                        /*
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        //verridePendingTransition(R.anim.gototop , R.anim.gototop);
                        startActivity(i);
                        */
                        //Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        //startActivity(i);
                        finish();


                    }





                });



        AsyncHttpClient client2 = new AsyncHttpClient();
        client2.get("http://awcs.net.in/api/get_color_list.php"
                , new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //Toast.makeText(getApplicationContext(),"Getting COLOR" , Toast.LENGTH_SHORT).show();
                        Log.i("mohit color response","" + response);
                        String test = "";
                        String hcf_name = "";
                        String colorId = "";
                        try {
                        /*json2 = response.getJSONObject("results");
                        String test = (String) json2.get("name");*/
                            spinnerArray2 = new ArrayList<String>();
                            colorIDs = new ArrayList<String>();
                            for(int i=-1 ; ; i++){
                                if(i==0)
                                    continue;
                                Log.i("mohit" , "i= " + i);
                                Log.i("mohit","value at " + response.getString("" + i));
                                test = response.getString("" + i);
                                if(test == null)
                                {
                                    break;
                                }else{

                                    JSONObject jsonobj = new JSONObject(test);
                                    hcf_name = jsonobj.getString("ColorName");
                                    colorId = jsonobj.getString("ColorID");
                                    Log.i("mohit", "hcf name " + hcf_name );
                                    Log.i("mohit", "colorId " + colorId );
                                    spinnerArray2.add(hcf_name);
                                    colorIDs.add(colorId);
                                    color_map.put(colorId,hcf_name);
                                    Log.i("mohitt" , "" + color_map);
                                }

                            }


                            /*ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (getApplicationContext(), android.R.layout.simple_spinner_item,
                                            spinnerArray); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_hcf.setAdapter(spinnerArrayAdapter);*/


                        } catch (Exception e) {
                            e.printStackTrace();
                        }




                        Log.i("mohit" , "spinner array  " + spinnerArray);
                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                        //Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        Intent i2 = new Intent(getApplicationContext(),MainActivity.class);

                        if(manager.isLoggedIn()) {
                            //startActivity(i2);
                            //LoginActivity.email_id = manager.getUserDetails().get("email");
                            HashMap<String, String> user = manager.getUserDetails();
                            //LoginActivity.email_id = user.get(SessionManagement.KEY_EMAIL);
                            Log.i("mohit","email dekho  " + user.get(SessionManagement.KEY_EMAIL));
                            //Log.i("mohit", "email_id fetched fromsp" + LoginActivity.email_id);
                        }
                        //manager.checkLogin();
                       /* HashMap<String, String> user = manager.getUserDetails();
                        //LoginActivity.email_id = user.get(SessionManagement.KEY_EMAIL);
                        Log.i("mohit","email dekho" + LoginActivity.email_id);*/
                        //startActivity(i);
                        finish();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(getApplicationContext(),"Connection Problem - Please Try Again" , Toast.LENGTH_LONG).show();

                        /*
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        //verridePendingTransition(R.anim.gototop , R.anim.gototop);
                        startActivity(i);
                        */
                       // Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                       // startActivity(i);
                        finish();


                    }





                });

        /*new Handler().postDelayed(new Runnable() {

            *//*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             *//*

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                *//*Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                //verridePendingTransition(R.anim.gototop , R.anim.gototop);
                startActivity(i);*//*

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);*/

    }
}
