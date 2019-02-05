package edmt.dev.androidgridlayout;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.toptoche.searchablespinnerlibrary.SearchableListDialog;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import cz.msebera.android.httpclient.Header;

public class ManualActivity extends AppCompatActivity {


    /*String[] spinnerArray ={
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8","9", "10", "11",
            "12", "13", "14", "15", "16"
    };*/
    Boolean hcf_adapted = false;
    ArrayList<String> local_spinnerArray, local_spinnerArray2;
    EditText weight;
    MaterialSpinner spinner_color;
    SearchableSpinner spinner_hcf;
    String HCF;
    String COLOR;
    String WEIGHT;
    Boolean hcf_valid, color_valid, weight_valid;
    String imei;
    SessionManagement manager;
    Boolean VALID ;
    TextView weight_tv;
    Button add;
    Integer nob;
    CircularProgressButton circularProgressButton;
    Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        manager = new SessionManagement(getApplicationContext());
        HCF = "";
        COLOR = "";
        WEIGHT = "";
        hcf_valid = true;
        color_valid = true;
        weight_valid = true;
        VALID = true;
        nob =0 ;
        local_spinnerArray = new ArrayList<String>(SplashActivity.spinnerArray);
        local_spinnerArray2 = new ArrayList<String>(SplashActivity.spinnerArray2);
        spinner_color = (MaterialSpinner) findViewById(R.id.spinner);
        //MaterialSpinner spinner_hcf = (MaterialSpinner)findViewById(R.id.hcf);
        spinner_hcf = (SearchableSpinner) findViewById(R.id.hcf);


        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>
                (getApplicationContext(), R.layout.selectbg,
                        local_spinnerArray2); //selected item will look like a spinner set from XML
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.selectdropdown);
        spinner_color.setAdapter(spinnerArrayAdapter2);

        //spinner_color.setItems("Red", "Black", "Blue", "Other");
        spinner_color.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                // Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                COLOR = item;
                Log.i("mohit", "" + item);
                color_valid = true;

            }
        });

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getApplicationContext(), R.layout.selectbg,
                        local_spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(R.layout.selectdropdown);
        spinner_hcf.setAdapter(spinnerArrayAdapter);
        hcf_adapted = true;

        add =findViewById(R.id.add);
        clear = findViewById(R.id.clear);
        weight_tv = findViewById(R.id.WEIGHT);
        weight = findViewById(R.id.weight);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(weight.getText().toString())){
                    //Toast.makeText(getApplicationContext(),"Empty weight",Toast.LENGTH_SHORT).show();
                }else {
                    double add_on = Double.parseDouble(weight.getText().toString());
                    Log.i("add on", "" + add_on);
                    double final_value = Double.parseDouble(weight_tv.getText().toString()) + add_on;
                    weight_tv.setText("" + final_value);
                    weight.setText("");
                    nob++;
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weight_tv.setText("0");
                nob = 0;
            }
        });


        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }else {
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            Log.i("mohit","imei" + telephonyManager.getDeviceId());
            imei = telephonyManager.getDeviceId();

        }

        circularProgressButton = (CircularProgressButton)findViewById(R.id.login);
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!VALID){
                    VALID = true;
                }else{

                    WEIGHT = weight_tv.getText().toString();
                    Log.i("mohit", "weight = " + WEIGHT);
                    if (WEIGHT.equals("")) {
                        weight_valid = false;
                        Log.i("mohit", "here");
                    } else {
                        weight_valid = true;
                    }
                /*Log.i("mohit" , "hcff   " + spinner_hcf.getSelectedItem().toString());
                HCF = spinner_hcf.getSelectedItem().toString() ;
                if(HCF.equals(""))
                    hcf_valid = false;*/
                    if (spinner_hcf.getSelectedItem() == null)
                        hcf_valid = false;
                    else {
                        Log.i("mohit", "hcff   " + spinner_hcf.getSelectedItem().toString());
                        HCF = spinner_hcf.getSelectedItem().toString();
                        hcf_valid = true;
                    }

                    Log.i("mohit", "color hcf weight" + color_valid + hcf_valid + weight_valid);

                    if (color_valid && hcf_valid && weight_valid) {






                        AlertDialog.Builder builder = new AlertDialog.Builder(ManualActivity.this);
                        //builder.setTitle("Are you sure you want to quit ?");
                        builder.setMessage("Are you sure you want to send the following data? \n\nHCF :" + spinner_hcf.getSelectedItem().toString()
                                + "\nColor :" + SplashActivity.spinnerArray2.get(spinner_color.getSelectedIndex()) + "\nWeight :"+WEIGHT+"\nNOB :" +
                                nob)
                                .setCancelable(true)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Intent i = new Intent(getApplicationContext(), ManualActivity.class);
                                        //startActivity(i);


                                        Toast.makeText(getApplicationContext(), "Send", Toast.LENGTH_LONG).show();
                                        //sb.setVisibility(View.GONE);
                                        circularProgressButton.startAnimation();
                                        int hcf_id = Integer.parseInt(SplashActivity.hcfIds.get(spinner_hcf.getSelectedItemPosition())) ;
                                        int color_id = Integer.parseInt(SplashActivity.colorIDs.get(spinner_color.getSelectedIndex()) );
                                        Log.i("mohit", " hcfid:  color_id :  " + hcf_id + "" + color_id);

                                        //getting email from shared pref
                                        //http://cyphertextsolutions.com/awcs/dev
                                        String temp_email = manager.getUserDetails().get(SessionManagement.KEY_EMAIL);
                                        Log.i("mohit", "gettting email " + temp_email);
                                        Log.i("mohit","http://awcs.net.in/api/post_record.php?bag_id="+color_id +"&weight="
                                                +WEIGHT+"&IMEI="+imei+"&email="+temp_email+"&hcf_id="+hcf_id + "&NoOfBags=" + nob );





                                        AsyncHttpClient client = new AsyncHttpClient();
                                        client.get("http://awcs.net.in/api/post_record.php?bag_id="+color_id +"&weight="
                                                        +WEIGHT+"&IMEI="+imei+"&email="+temp_email+"&hcf_id="+hcf_id + "&NoOfBags=" + nob
                                                , new JsonHttpResponseHandler() {
                                                    @Override
                                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                        super.onSuccess(statusCode, headers, response);

                                                        String test = "";

                                                        try {
                        /*json2 = response.getJSONObject("results");
                        String test = (String) json2.get("name");*/
                                                            Log.i("mohit","" + response.getString("success"));
                                                            test = response.getString("success");

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }


                                                        if(test.equals("true")) {
                                                            Toast.makeText(getApplicationContext(),"Record Inserted", Toast.LENGTH_SHORT).show();
                                                            circularProgressButton.doneLoadingAnimation(Color.parseColor("#333639"), BitmapFactory.decodeResource(getResources(),R.drawable.ic_done_white_48dp));
                                                            finish();
                                                            //btn.revertAnimation();
                                                        }else{
                                                            Toast.makeText(getApplicationContext(),"Invalid data"  , Toast.LENGTH_SHORT).show();
                                                            circularProgressButton.revertAnimation();
                                                            circularProgressButton.setBackground(getResources().getDrawable(R.drawable.shape_default));
                                                        }

                                                    }

                                                    @Override
                                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                                        super.onFailure(statusCode, headers, throwable, errorResponse);
                                                        //sb.setVisibility(View.VISIBLE);
                                                        Toast.makeText(getApplicationContext(),"Connection Problem - Please Try Again" , Toast.LENGTH_LONG).show();
                                                        circularProgressButton.revertAnimation();
                                                        circularProgressButton.setBackground(getResources().getDrawable(R.drawable.shape_default));

                                                    }
                                                });

                                        //----------------------------------------------------



                                        //setContentView(R.layout.activity_main);
                                        //dialog.cancel();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();










                    }else{


                        if(!color_valid){
                            VALID = false;
                            Toast.makeText(getApplicationContext(),"Invalid Color",Toast.LENGTH_SHORT).show();
                        }
                        if(!hcf_valid){
                            VALID = false;
                            Toast.makeText(getApplicationContext(),"Invalid HCF",Toast.LENGTH_SHORT).show();
                        }
                        if(!weight_valid){
                            VALID = false;
                            //weight_tv.setError("InValid Weight");
                            Toast.makeText(getApplicationContext(),"Invalid Weight",Toast.LENGTH_SHORT).show();
                        }
                    }

                    // finish();
                }


            }
        });



        /*final SwipeButton sb = (SwipeButton) findViewById(R.id.sb);
        //weight = (EditText) findViewById(R.id.weight);

        sb.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                //Toast.makeText(getApplicationContext(),"Send",Toast.LENGTH_LONG).show();
                if(!VALID){
                    VALID = true;
                }else{

                WEIGHT = weight_tv.getText().toString();
                Log.i("mohit", "weight = " + WEIGHT);
                if (WEIGHT.equals("")) {
                    weight_valid = false;
                    Log.i("mohit", "here");
                } else {
                    weight_valid = true;
                }
                *//*Log.i("mohit" , "hcff   " + spinner_hcf.getSelectedItem().toString());
                HCF = spinner_hcf.getSelectedItem().toString() ;
                if(HCF.equals(""))
                    hcf_valid = false;*//*
                if (spinner_hcf.getSelectedItem() == null)
                    hcf_valid = false;
                else {
                    Log.i("mohit", "hcff   " + spinner_hcf.getSelectedItem().toString());
                    HCF = spinner_hcf.getSelectedItem().toString();
                    hcf_valid = true;
                }

                Log.i("mohit", "color hcf weight" + color_valid + hcf_valid + weight_valid);

                if (color_valid && hcf_valid && weight_valid) {
                    Toast.makeText(getApplicationContext(), "Send", Toast.LENGTH_LONG).show();
                    sb.setVisibility(View.GONE);
                    int hcf_id = Integer.parseInt(SplashActivity.hcfIds.get(spinner_hcf.getSelectedItemPosition())) ;
                    int color_id = Integer.parseInt(SplashActivity.colorIDs.get(spinner_color.getSelectedIndex()) );
                    Log.i("mohit", " hcfid:  color_id :  " + hcf_id + "" + color_id);

                    //getting email from shared pref
                    //http://cyphertextsolutions.com/awcs/dev
                    String temp_email = manager.getUserDetails().get(SessionManagement.KEY_EMAIL);
                    Log.i("mohit", "gettting email " + temp_email);
                    Log.i("mohit","http://cyphertextsolutions.com/awcs/dev/api/post_record.php?bag_id="+color_id +"&weight="
                            +WEIGHT+"&IMEI="+imei+"&email="+temp_email+"&hcf_id="+hcf_id + "&noofbags=" + nob );

                    //----------------------------------------------------
                    //"http://awcs.net.in/api/post_record.php?bag_id="+COLOR+"&weight="
                    //                                    +WEIGHT+"&IMEI=9213123123231&email=pgfiry%40gmail.com%hcf_id=1"+HCF
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get("http://cyphertextsolutions.com/awcs/dev/api/post_record.php?bag_id="+color_id +"&weight="
                                    +WEIGHT+"&IMEI="+imei+"&email="+temp_email+"&hcf_id="+hcf_id + "&noofbags=" + nob
                            , new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);

                                    String test = "";

                                    try {
                        *//*json2 = response.getJSONObject("results");
                        String test = (String) json2.get("name");*//*
                                        Log.i("mohit","" + response.getString("success"));
                                        test = response.getString("success");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    if(test.equals("true")) {
                                        Toast.makeText(getApplicationContext(),"Record Inserted", Toast.LENGTH_SHORT).show();
                                        finish();
                                        //btn.revertAnimation();
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Invalid data"  , Toast.LENGTH_SHORT).show();

                                    }

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                    sb.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(),"Connection Problem - Please Try Again" , Toast.LENGTH_LONG).show();

                                }
                            });

                    //----------------------------------------------------

                }else{


                    if(!color_valid){
                        VALID = false;
                        Toast.makeText(getApplicationContext(),"Invalid Color",Toast.LENGTH_SHORT).show();
                    }
                    if(!hcf_valid){
                        VALID = false;
                        Toast.makeText(getApplicationContext(),"Invalid HCF",Toast.LENGTH_SHORT).show();
                    }
                    if(!weight_valid){
                        VALID = false;
                        //weight_tv.setError("InValid Weight");
                        Toast.makeText(getApplicationContext(),"Invalid Weight",Toast.LENGTH_SHORT).show();
                    }
                }

               // finish();
            }
            }
        });*/


    }


    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if(!hcf_adapted) {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                    (getApplicationContext(), android.R.layout.simple_spinner_item,
                            local_spinnerArray); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_hcf.setAdapter(spinnerArrayAdapter);
            hcf_adapted = true;
            //return super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }*/

}
