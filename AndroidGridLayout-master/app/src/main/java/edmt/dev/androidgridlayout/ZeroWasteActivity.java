package edmt.dev.androidgridlayout;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ZeroWasteActivity extends AppCompatActivity {

    ArrayList<String> local_spinnerArray ;
    EditText weight;
    SearchableSpinner spinner_hcf;
    String HCF;
    String COLOR;
    String WEIGHT;
    Boolean hcf_valid, color_valid, weight_valid;
    String imei;
    SessionManagement manager;
    Boolean VALID ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zero_waste);
        manager = new SessionManagement(getApplicationContext());
        //MaterialSpinner spinner_color = (MaterialSpinner) findViewById(R.id.spinner);
        /*MaterialSpinner spinner_hcf = (MaterialSpinner)findViewById(R.id.hcf);


        spinner_hcf.setItems("RML", "AIIMS", "SGRH", "Leelavati");
        spinner_hcf.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });*/
        HCF ="";
        hcf_valid = true;
        weight_valid = true;
        color_valid = true;
        VALID = true;
        spinner_hcf = (SearchableSpinner) findViewById(R.id.hcf);
        local_spinnerArray = new ArrayList<String>(SplashActivity.spinnerArray);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getApplicationContext(), R.layout.selectbg,
                        local_spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(R.layout.selectdropdown);
        spinner_hcf.setAdapter(spinnerArrayAdapter);

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


        final SwipeButton sb = (SwipeButton)findViewById(R.id.sb);
        //sb.toggleState();
        sb.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                //Toast.makeText(getApplicationContext(),"Send",Toast.LENGTH_LONG).show();
                if(!VALID){
                    VALID = true;
                }else{

                WEIGHT = "0";
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
                    Toast.makeText(getApplicationContext(), "Send", Toast.LENGTH_LONG).show();
                    sb.setVisibility(View.GONE);
                    int hcf_id = Integer.parseInt(SplashActivity.hcfIds.get(spinner_hcf.getSelectedItemPosition())) ;
                    int color_id = -1;
                    Log.i("mohit", " hcfid:  color_id :  " + hcf_id + "" + color_id);
                    String temp_email = manager.getUserDetails().get(SessionManagement.KEY_EMAIL);
                    Log.i("mohit", "http://awcs.net.in/api/post_record.php?bag_id=" + color_id + "&weight="
                            + WEIGHT + "&IMEI=" + imei + "&email=" + temp_email + "&hcf_id=" + hcf_id);
                    //----------------------------------------------------
                    //"http://awcs.net.in/api/post_record.php?bag_id="+COLOR+"&weight="
                    //                                    +WEIGHT+"&IMEI=9213123123231&email=pgfiry%40gmail.com%hcf_id=1"+HCF
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get("http://awcs.net.in/api/post_record.php?bag_id=" + color_id + "&weight="
                                    + WEIGHT + "&IMEI=" + imei + "&email=" + temp_email + "&hcf_id=" + hcf_id
                            , new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);

                                    String test = "";

                                    try {
                        /*json2 = response.getJSONObject("results");
                        String test = (String) json2.get("name");*/
                                        Log.i("mohit", "" + response.getString("success"));
                                        test = response.getString("success");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    if (test.equals("true")) {
                                        Toast.makeText(getApplicationContext(), "Record Inserted", Toast.LENGTH_SHORT).show();
                                        finish();
                                        //btn.revertAnimation();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Invalid data", Toast.LENGTH_SHORT).show();

                                    }

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                    sb.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), "Connection Problem - Please Try Again", Toast.LENGTH_LONG).show();
                                    VALID = false;
                                }
                            });

                    //----------------------------------------------------

                } else {



                    if (!color_valid) {
                        VALID = false;
                        Toast.makeText(getApplicationContext(), "Invalid Color", Toast.LENGTH_SHORT).show();
                    }
                    if (!hcf_valid) {
                        VALID = false;
                        Toast.makeText(getApplicationContext(), "Invalid HCF", Toast.LENGTH_SHORT).show();
                    }
                    if (!weight_valid) {
                        VALID = false;
                        weight.setError("Invalid Weight");
                    }
                }

                // finish();
            }
        }
        });


    }
}
