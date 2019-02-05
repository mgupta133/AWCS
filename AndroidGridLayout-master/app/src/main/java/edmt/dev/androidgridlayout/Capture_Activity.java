package edmt.dev.androidgridlayout;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import android.graphics.Color;

public class Capture_Activity extends AppCompatActivity {

    String hcf_id;
    String color_id;
    String hcf_name;
    String color_name;
    Boolean qr_code_moved;
    private ZXingScannerView mScannerView;
    TextView hcf;
    TextView color;
    public static int count = 1;

    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private BluetoothGatt mGatt;
    ArrayList<String> local_spinnerArray2;
    MaterialSpinner spinner_color;
    String temp;
    Boolean found = false;
    String COLOR,WEIGHT,HCF;
    Boolean  color_valid,hcf_valid,weight_valid;
    Boolean dataSet;
    SessionManagement manager;
    String imei;
    TextView weight,weight_final;
    Button add , scan;
    TextView clear;
    CircularProgressButton circularProgressButton;
    Boolean connected = false;

    UUID uuid = UUID.fromString("0000FFF0-0000-1000-8000-00805F9B34FB");
    UUID uuid2 = UUID.fromString("0000FFF4-0000-1000-8000-00805F9B34FB");

    UUID uuid3 = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB");
    UUID uuid4 = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB");

    Double d_d= 0.0;

    public static String HCF_ka_name;
    public static Integer HCF_ka_id;
    public static Boolean HCF_ka_validpana;
    public static String bag_ki_id ;
    public static Integer Color_ki_id;
    public static String Color_ka_name;

    JSONArray jsonArray ;
    Integer jsonCount;

    RequestParams params ;


    //public HashMap<Integer,Double> nestedMap;
    public HashMap<Integer,HashMap<Integer,Double>> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_);

        jsonCount = 0;
        jsonArray = new JSONArray();
        count = 1;
        HCF_ka_name = "";
        HCF_ka_id = -1;
        HCF_ka_validpana = false;
        bag_ki_id = "";
        Color_ki_id = -1;
        Color_ka_name = "";
        qr_code_moved = false;

        color_id = "-1" ;//+ bundle.getString("color_id");
        //hcf_name = "" + bundle.getString("hcf_name");
        ///////-----  hcf_name = "" + SplashActivity.map.get("" + Integer.parseInt(hcf_id));
        ///////------  color_name = "ZERO"; //+ bundle.getString("color_name");
        //Toast.makeText(getApplicationContext(),"HCF and color id  " + hcf_id +"   " + color_id + color_name + hcf_name,Toast.LENGTH_SHORT).show();


        //HCF_ka_id = Integer.parseInt(hcf_id);
        //HCF_ka_name = hcf_name;

        /*local_spinnerArray2 = new ArrayList<String>(SplashActivity.spinnerArray2);
        spinner_color = (MaterialSpinner) findViewById(R.id.spinner);*/
        //MaterialSpinner spinner_hcf = (MaterialSpinner)findViewById(R.id.hcf);
        //spinner_hcf = (SearchableSpinner) findViewById(R.id.hcf);


        /*ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>
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
                //if(item)
                //spinner_color.setTextColor(Color.RED);
            }
        });*/


        hcf = findViewById(R.id.hcf);
        color = findViewById(R.id.color);
        circularProgressButton = findViewById(R.id.login);
        //hcf.setText("" + HCF_ka_name);
        //color.setText("" + color_name);


        manager = new SessionManagement(getApplicationContext());
        dataSet = true;
        HCF = "";
        COLOR = "";
        WEIGHT = "";
        hcf_valid = true;
        color_valid = true;
        weight_valid = false;
        imei = "";
        weight = findViewById(R.id.weight);
        //weight_final = findViewById(R.id.WEIGHT);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }else {
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            Log.i("mohit","imei" + telephonyManager.getDeviceId());
            imei = telephonyManager.getDeviceId();

        }

        mHandler = new Handler();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported",Toast.LENGTH_SHORT).show();
            finish();
        }
        final BluetoothManager bluetoothManager =(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();


        add = findViewById(R.id.add);
        scan = findViewById(R.id.scan);
        clear = findViewById(R.id.clear);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!weight.getText().toString().equals("") && !color.getText().toString().equals("") && HCF_ka_validpana)
                {
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
                   JSONObject json = new JSONObject();

                    try {
                    json.put("color_id", Color_ki_id);
                    json.put("hcf_id",HCF_ka_id);
                    //json.put("nob",1);
                    json.put("weight",Double.parseDouble(weight.getText().toString()));
                    json.put("imei", imei);
                    String temp_email = manager.getUserDetails().get(SessionManagement.KEY_EMAIL);
                    json.put("email",temp_email);
                    json.put("u_num",bag_ki_id);
                    jsonArray.put(jsonCount,json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonCount++;
                    Log.d("kamini","" + jsonArray);


                }else{
                    Toast.makeText(getApplicationContext(), "Scan First..", Toast.LENGTH_SHORT).show();


                }



            }
        });

        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringEntity entity = null ;
                //Toast.makeText(getApplicationContext(),""+jsonArray,Toast.LENGTH_LONG).show();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("0",jsonArray);
                    Log.i("parameters", "" + jsonObject);
                 entity = new StringEntity(jsonObject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.i("parameters", ""  + entity);

                if (jsonCount>0) {

                    circularProgressButton.startAnimation();
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post(getApplicationContext(),"http://awcs.net.in/api/post_rescanned_data.php", entity,"application/json",
                             new JsonHttpResponseHandler() {
                                 @Override
                                 public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                     super.onSuccess(statusCode, headers, response);
                                     Log.i("response", "" + response);
                                     try {
                                         Toast.makeText(getApplicationContext(),"" + response.get("message"), Toast.LENGTH_SHORT).show();
                                     } catch (JSONException e) {
                                         e.printStackTrace();
                                     }
                                     circularProgressButton.doneLoadingAnimation(Color.parseColor("#333639"), BitmapFactory.decodeResource(getResources(),R.drawable.ic_done_white_48dp));
                                     circularProgressButton.revertAnimation();
                                     circularProgressButton.setBackground(getResources().getDrawable(R.drawable.shape_default));
                                     finish();
                                 }

                                 @Override
                                 public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                     super.onFailure(statusCode, headers, throwable, errorResponse);
                                     Toast.makeText(getApplicationContext(),"Connection Problem - Please Try Again" , Toast.LENGTH_LONG).show();
                                     circularProgressButton.revertAnimation();
                                     circularProgressButton.setBackground(getResources().getDrawable(R.drawable.shape_default));


                                 }
                                 @Override
                                 public void onFailure(int statusCode , Header[] headers ,String response, Throwable throwable){
                                     super.onFailure(statusCode,headers,response,throwable);
                                    // Toast.makeText(getApplicationContext(),"syapa",Toast.LENGTH_SHORT).show();
                                     Log.e("mohit",response);
                                     circularProgressButton.revertAnimation();
                                     circularProgressButton.setBackground(getResources().getDrawable(R.drawable.shape_default));
                                 }


                            });
                            }else{
                    Toast.makeText(getApplicationContext(),"Add first" , Toast.LENGTH_SHORT).show();
                }

                }



        });

        /*circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*StringEntity entity = null ;

                Toast.makeText(getApplicationContext(),""+jsonArray,Toast.LENGTH_LONG).show();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("0",jsonArray);
                    Log.i("parameters", "" + jsonObject);
                 entity = new StringEntity(jsonObject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.i("parameters", ""  + entity);*//*
                //WEIGHT = weight_final.getText().toString();
                    WEIGHT = weight.getText().toString();
                    Log.i("mohit", "weight = " + WEIGHT);
                    if (WEIGHT.equals("")) {
                        weight_valid = false;
                        Log.i("mohit", "here");
                    } else {
                        weight_valid = true;
                    }


                        //Log.i("mohit", "hcff   " + spinner_hcf.getSelectedItem().toString());
                        //HCF = hcf.getText().toString();
                        if(HCF_ka_validpana)
                        hcf_valid = true;
                        else
                        hcf_valid = false;


                    Log.i("mohit", "color hcf weight" + color_valid + hcf_valid + weight_valid);
                    if(COLOR.equals(""))
                    COLOR = "" + spinner_color.getText();
                    Log.i("coool","" + Integer.parseInt(SplashActivity.colorIDs.get(spinner_color.getSelectedIndex()) ));
                    if (color_valid && hcf_valid && weight_valid) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(Capture_Activity.this);
                        //builder.setTitle("Are you sure you want to quit ?");
                        builder.setMessage("Are you sure you want to send the following data? \n\nHCF :  " + hcf.getText().toString()
                                + "\nColor :  " + COLOR + "\nWeight :  "+WEIGHT)
                                .setCancelable(true)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Intent i = new Intent(getApplicationContext(), ManualActivity.class);
                                        //startActivity(i);


                                        Toast.makeText(getApplicationContext(), "Send", Toast.LENGTH_LONG).show();
                                        //sb.setVisibility(View.GONE);
                                        circularProgressButton.startAnimation();
                                        int hcf_idd = HCF_ka_id;
                                                //Integer.parseInt(SplashActivity.hcfIds.get(spinner_hcf.getSelectedItemPosition())) ;
                                        //int color_idd = Integer.parseInt(""+ color_id);
                                        int color_idd = Integer.parseInt(SplashActivity.colorIDs.get(spinner_color.getSelectedIndex()) );
                                                //Integer.parseInt(SplashActivity.colorIDs.get(spinner_color.getSelectedIndex()) );
                                        Log.i("mohit", " hcfid:  color_id :  " + hcf_idd + "" + color_idd);

                                        //getting email from shared pref
                                        //http://cyphertextsolutions.com/awcs/dev
                                        String temp_email = manager.getUserDetails().get(SessionManagement.KEY_EMAIL);
                                        Log.i("mohit", "gettting email " + temp_email);
                                        Log.i("mohit","http://cyphertextsolutions.com/awcs/dev/api/post_record.php?bag_id="+color_idd +"&weight="
                                                +WEIGHT+"&IMEI="+imei+"&email="+temp_email+"&hcf_id="+hcf_idd + "&NoOfBags=" + count );





                                        AsyncHttpClient client = new AsyncHttpClient();
                                        client.get("http://cyphertextsolutions.com/awcs/dev/api/post_record.php?bag_id="+color_idd +"&weight="
                                                        +WEIGHT+"&IMEI="+imei+"&email="+temp_email+"&hcf_id="+hcf_idd + "&NoOfBags=" + count
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
                                                            circularProgressButton.doneLoadingAnimation(Color.parseColor("#333639"), BitmapFactory.decodeResource(getResources(),R.drawable.ic_done_white_48dp));
                                                            circularProgressButton.revertAnimation();
                                                            circularProgressButton.setBackground(getResources().getDrawable(R.drawable.shape_default));
                                                            //finish();
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
                            //VALID = false;
                            Toast.makeText(getApplicationContext(),"Invalid Color",Toast.LENGTH_SHORT).show();
                        }
                        if(!hcf_valid){
                            //VALID = false;
                            Toast.makeText(getApplicationContext(),"Invalid HCF",Toast.LENGTH_SHORT).show();
                        }
                        if(!weight_valid){
                            //VALID = false;
                            //weight_tv.setError("InValid Weight");
                            Toast.makeText(getApplicationContext(),"Invalid Weight",Toast.LENGTH_SHORT).show();
                        }
                    }

                    // finish();
                }



        });*/




    }






    @Override
    protected void onResume() {
        super.onResume();
        hcf = findViewById(R.id.hcf);
        hcf.setText("" + HCF_ka_name);
        color = findViewById(R.id.color);
        color.setText("" + Color_ka_name);

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                filters = new ArrayList<ScanFilter>();
            }

            scanLeDevice(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            scanLeDevice(false);
        }
    }

    @Override
    protected void onDestroy() {
        if (mGatt == null) {
            super.onDestroy();
            return;
        }
        mGatt.close();
        mGatt = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                //Bluetooth not enabled.
                finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT < 21) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    } else {
                        mLEScanner.stopScan(mScanCallback);

                    }
                }
            }, SCAN_PERIOD);
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mLEScanner.startScan(filters, settings, mScanCallback);
            }
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else {
                mLEScanner.stopScan(mScanCallback);
            }
        }
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i("callbackType", String.valueOf(callbackType));
            Log.i("result", result.toString());
            Log.i("result device", "" + result.getDevice());
            Log.i("result device name","" +result.getScanRecord().getDeviceName());
            //if(result.getDevice().toString().equals("D4:36:39:6D:85:7A")) {
                //if(result.getDevice().toString().equals("98:5D:AD:01:05:A4")) {
            String deviceName = "" + result.getScanRecord().getDeviceName();
                    if(deviceName.contains("Phoenix Scale") || deviceName.contains("WSI") ) {

                found = true;
                BluetoothDevice btDevice = result.getDevice();
                connectToDevice(btDevice);
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            /*if(found==false){
                onResume();
            }*/
            for (ScanResult sr : results) {
                Log.i("ScanResult - Results", sr.toString());

            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("onLeScan", device.toString());
                            connectToDevice(device);
                        }
                    });
                }
            };

    public void connectToDevice(BluetoothDevice device) {
        if (mGatt == null) {
            temp = device.getAddress();
            mGatt = device.connectGatt(this, false, gattCallback);
            scanLeDevice(false);// will stop after first device detection
        }
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {



            super.onServicesDiscovered(gatt, status);
            List<BluetoothGattService> services = gatt.getServices();
            Log.i("onServicesDiscovered", services.toString());
            services = mGatt.getServices();
            connected = true;
            /*BluetoothGattCharacteristic characteristicData = services.get(1).getCharacteristics().get(0);//(uuid);
            gatt.readCharacteristic(services.get(1).getCharacteristics().get
                    (0));
            onCharacteristicRead(gatt,services.get(1).getCharacteristics().get(0),status);*/
            for(BluetoothGattService service : services){
                Log.i("raano" , "" + service.getUuid() );
                Log.i("raano" , "" + service.getUuid().toString().equalsIgnoreCase("" + uuid3));
                if( service.getUuid().toString().equalsIgnoreCase("" + uuid)) {
                    Log.i("in" , "innnn1");

                    BluetoothGattCharacteristic characteristicData = service.getCharacteristic(uuid2);
                    //gatt.setCharacteristicNotification(characteristicData, true);
                    Log.i("service type" , "" + service.getType());
                    Log.i("in" , "char data " + characteristicData.getUuid());
                    Log.i("in" , "char data " + characteristicData.getValue());
                    Log.i("in" , "char data imprtnat" + characteristicData);
                    for (BluetoothGattDescriptor descriptor : characteristicData.getDescriptors()) {
                        // Log.i("in" , "innnn2" + descriptor);
                        //Log.i("in" , "char data " + characteristicData.getUuid());
                        //descriptor.setValue( BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                        descriptor.setValue( BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        mGatt.writeDescriptor(descriptor);
                        Log.i("descriptor status", "" + descriptor.getValue());
                        Log.i("descriptor status", "" + descriptor.getCharacteristic());
                        //Log.i("descriptor atatus " ,"" + descriptor.describeContents() );
                        Log.i("deesc", " "+ descriptor.getPermissions());
                    }
                    gatt.setCharacteristicNotification(characteristicData, true);
                    gatt.readCharacteristic(characteristicData);


                }else if(service.getUuid().toString().equalsIgnoreCase("" + uuid3)){

                        BluetoothGattCharacteristic characteristicData = service.getCharacteristic(uuid4);
                        //gatt.setCharacteristicNotification(characteristicData, true);
                        Log.i("service type" , "" + service.getType());
                        Log.i("in" , "char data " + characteristicData.getUuid());
                        Log.i("in" , "char data " + characteristicData.getValue());
                        Log.i("in" , "char data imprtnat" + characteristicData);
                        for (BluetoothGattDescriptor descriptor : characteristicData.getDescriptors()) {
                            // Log.i("in" , "innnn2" + descriptor);
                            //Log.i("in" , "char data " + characteristicData.getUuid());
                            //descriptor.setValue( BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                            descriptor.setValue( BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            mGatt.writeDescriptor(descriptor);
                            Log.i("descriptor status", "" + descriptor.getValue());
                            Log.i("descriptor status", "" + descriptor.getCharacteristic());
                            //Log.i("descriptor atatus " ,"" + descriptor.describeContents() );
                            Log.i("deesc", " "+ descriptor.getPermissions());
                        }
                        gatt.setCharacteristicNotification(characteristicData, true);
                        gatt.readCharacteristic(characteristicData);

                }
            }


        }
        @Override
        public synchronized void onCharacteristicChanged(BluetoothGatt gatt,
                                                         BluetoothGattCharacteristic characteristic){
            weight = findViewById(R.id.weight);
            //Toast.makeText(getApplicationContext(),"here",Toast.LENGTH_SHORT).show();
            //gatt.setCharacteristicNotification(characteristic, false);
            Log.i("to check char" , " " + characteristic);
            //final byte[] dataInput = characteristic.getValue();
            final String temp = characteristic.getStringValue(0);
            String finalWeight = "" ;
            Log.i("String wali " , "" + temp);
            Pattern p = Pattern.compile("\\[.*?\\]");
            Matcher m = p.matcher(temp);
            if(m.find()) {
                //System.out.println(m.group().subSequence(1, m.group().length()-1));
                finalWeight = (String) m.group().subSequence(1, m.group().length() - 1);

                Double d = Double.parseDouble(finalWeight);
                d = d / 1000;
                try {
                    d_d = d;
                    //weight.setText("" + d);
                }catch (Exception e){
                    Log.e("Mohit","Error ocured" + e);
                }
                weight_valid = true;
            }else{
                String weight_wsi = temp.split("[\\+k]")[1];
                Double d = Double.parseDouble("" + weight_wsi);
                d_d = d;
                weight_valid = true;
            }

        }
        public int unsignedByteToInt(byte b) {
            return b & 0xFF;
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic
                                                 characteristic, int status) {
            // super.onCharacteristicRead(gatt,characteristic,status);
            Log.i("onCharacteristicRead", characteristic.toString());
            byte[] value=characteristic.getValue();
            Log.i("value" , "" + value);
            String v = new String(value);
            Log.i("onCharacteristicRead", "Value: " + v);



            Log.i("onCharacteristicRead", characteristic.toString());
            //.disconnect();
        }
    };

    /*public void QrScanner(){


        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view<br />
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.<br />
        mScannerView.startCamera();         // Start camera<br />
        //camera_screen = true;
        *//*Intent intent_local = new Intent(getApplicationContext(),Main2Activity.class);
        startActivity(intent_local);*//*


    }*/


    public void bscan(View view) {

        if(d_d !=0.0) {
            weight.setText("" + d_d);
            Intent i = new Intent(getApplicationContext(), TempScannerActivity.class);
            startActivity(i);
        }else{
            /*d_d = 0.25;
            weight_valid = true;
            weight.setText("" + d_d);
            Intent i = new Intent(getApplicationContext(), TempScannerActivity.class);
            startActivity(i);*/
            Toast.makeText(getApplicationContext(),"Connecting...." , Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed(){

        /*if (mGatt == null) {
            super.onDestroy();
            return;
        }*/
        if(mGatt!= null) {
            mGatt.close();
            mGatt = null;
            //super.onDestroy();
            //
        }
        finish();
    }


    /*@Override
    public void onBackPressed() {
        //moveTaskToBack(true);
        *//*qr_code_moved = true;
        if(camera_screen){
            camera_screen = false;
            //qr_code_moved = false;
            setContentView(R.layout.activity_main);
        }else {*//*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Are you sure you want to quit ?");
        builder.setMessage("Discard ?")
                .setCancelable(false)
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Intent i = new Intent(getApplicationContext(), ManualActivity.class);
                        //startActivity(i);

                        //setContentView(R.layout.activity_main);
                        dataSet = false;
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                            *//*moveTaskToBack(true);
                            finishAffinity();
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);*//*
                        //moveTaskToBack(true);
                        //setContentView(R.layout.activity_main);
                        Intent m = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(m);
                        //onDestroy();

                        //finish();
                            *//*Intent m = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(m);*//*

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        //}
    }*/

}

