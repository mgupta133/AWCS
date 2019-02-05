package edmt.dev.androidgridlayout;

import android.Manifest;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.annotation.TargetApi;
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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

@TargetApi(21)
public class BarcodeResultActivity extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private BluetoothGatt mGatt;
    ArrayList<String>  local_spinnerArray2;
    MaterialSpinner spinner_color;
    String temp;
    Boolean found = false;
    String COLOR,WEIGHT,HCF;
    Boolean  color_valid,hcf_valid,weight_valid;
    Boolean dataSet;
    SessionManagement manager;
    String imei;

    UUID uuid = UUID.fromString("0000FFF0-0000-1000-8000-00805F9B34FB");
    UUID uuid2 = UUID.fromString("0000FFF4-0000-1000-8000-00805F9B34FB");
    //UUID uuid2 = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    TextView tv;
    TextView weight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_result);
        manager = new SessionManagement(getApplicationContext());
        dataSet = true;
        HCF = "";
        COLOR = "";
        WEIGHT = "";
        hcf_valid = true;
        color_valid = true;
        weight_valid = false;
        imei = "";
        weight = findViewById(R.id.weightfinal);


        //tv = (TextView)findViewById(R.id.tvv);
        String info = getIntent().getStringExtra("info");
        //tv.setText(info);
        Log.i("mohit", "" + info);
        String substr = info.substring(info.length() - 5);
        Log.i("mohit" , "" + substr);
        //substr = "00001";
        //final int hcf_id = Integer.parseInt(substr);
        final String hcf_id = substr;
        Log.i("hcf_id : " , "" + hcf_id);

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

        final SwipeButton sb = (SwipeButton) findViewById(R.id.sb);
        try {
            sb.setOnStateChangeListener(new OnStateChangeListener() {
                @Override
                public void onStateChange(boolean active) {
                    //Toast.makeText(getApplicationContext(),"Send",Toast.LENGTH_LONG).show();


                    WEIGHT = weight.getText().toString();
                    Log.i("mohit", "weight = " + WEIGHT);
                    if (WEIGHT.equals("Weight")) {
                        weight_valid = false;
                        Log.i("mohit", "here");
                    } else {
                        weight_valid = true;
                    }

                    Log.i("mohit", "color hcf weight" + color_valid + hcf_valid + weight_valid);

                    if (color_valid && hcf_valid && weight_valid) {
                        Toast.makeText(getApplicationContext(), "Send", Toast.LENGTH_LONG).show();
                        sb.setVisibility(View.GONE);
                        //int hcf_id = spinner_hcf.getSelectedItemPosition() + 1;
                        int color_id = spinner_color.getSelectedIndex() + 1;
                        Log.i("mohit", " hcfid:  color_id :  " + HCF + "" + color_id);

                        //getting email from shared pref
                        //http://cyphertextsolutions.com/awcs/dev
                        String temp_email = manager.getUserDetails().get(SessionManagement.KEY_EMAIL);
                        Log.i("mohit", "gettting email " + temp_email);
                        Log.i("mohit", "http://cyphertextsolutions.com/awcs/dev/api/post_record.php?bag_id=" + color_id + "&weight="
                                + WEIGHT + "&IMEI=" + imei + "&email=" + temp_email + "&hcf_id=" + hcf_id);

                        //----------------------------------------------------
                        //"http://awcs.net.in/api/post_record.php?bag_id="+COLOR+"&weight="
                        //                                    +WEIGHT+"&IMEI=9213123123231&email=pgfiry%40gmail.com%hcf_id=1"+HCF
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.get("http://cyphertextsolutions.com/awcs/dev/api/post_record.php?bag_id=" + color_id + "&weight="
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

                                    }
                                });

                        //----------------------------------------------------

                    } else {


                        if (!color_valid) {
                            Toast.makeText(getApplicationContext(), "InValid Color", Toast.LENGTH_SHORT).show();
                        }
                        if (!hcf_valid) {
                            Toast.makeText(getApplicationContext(), "InValid HCF", Toast.LENGTH_SHORT).show();
                        }
                        if (!weight_valid) {
                            weight.setError("InValid Weight");
                        }
                    }

                    // finish();
                }
            });
        }catch (Exception e){
            Toast.makeText(getApplication(),"" + e,Toast.LENGTH_LONG).show();
            finish();
        }


        mHandler = new Handler();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported",Toast.LENGTH_SHORT).show();
            finish();
        }
        final BluetoothManager bluetoothManager =(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        local_spinnerArray2 = new ArrayList<String>(SplashActivity.spinnerArray2);
        spinner_color = (MaterialSpinner) findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_spinner_item,
                        local_spinnerArray2); //selected item will look like a spinner set from XML
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
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
            if(result.getDevice().toString().equals("D4:36:39:6D:85:7A")) {
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


            /*List<BluetoothGattService> services = gatt.getServices();
            Log.i("onServicesDiscovered", services.toString());
            gatt.readCharacteristic(services.get(1).getCharacteristics().get
                    (0));
            onCharacteristicRead(gatt,services.get(1).getCharacteristics().get(0),status);*/
            /*Log.i("mohit : ", "" + services.get(0).getCharacteristics().get(0));
            Log.i("mohit : ", "" + services.get(1).getCharacteristics().get(0));
            //Log.i("mohit : ", "" + services.get(1).getCharacteristics().get(1));
            Log.i("mohit : ", "" + services.get(2).getCharacteristics().get(0));
            Log.i("mohit : ", "" + services.get(3).getCharacteristics().get(0));
            Log.i("mohit pp: ", "" + services.get(1).getCharacteristic().getDescriptor(services.get(1).getCharacteristic().getUuid()).getCharacteristic().getStringValue(0));
            //Log.i("mohit : ", "" + services.get(4).getCharacteristics().get(0));
            //onCharacteristicRead(gatt,services.get(0).getCharacteristic());*/


            super.onServicesDiscovered(gatt, status);
            List<BluetoothGattService> services = gatt.getServices();
            Log.i("onServicesDiscovered", services.toString());
            services = mGatt.getServices();
            /*BluetoothGattCharacteristic characteristicData = services.get(1).getCharacteristics().get(0);//(uuid);
            gatt.readCharacteristic(services.get(1).getCharacteristics().get
                    (0));
            onCharacteristicRead(gatt,services.get(1).getCharacteristics().get(0),status);*/
            for(BluetoothGattService service : services){
                Log.i("iuuids" , "" + service.getUuid());
                if( service.getUuid().equals(uuid)) {
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

                    //onCharacteristicRead(gatt,characteristicData,status);
                    //onCharacteristicChanged(gatt,characteristicData);
                    //Log.i("char data final " , "" + characteristicData);
                    //Log.i("char data final " , "" + characteristicData.getValue());

                    //-------------------


                   /* int flag = characteristicData.getProperties();

                    int format = -1;

                    if ((flag & 0x01) != 0) {

                        format = BluetoothGattCharacteristic.FORMAT_UINT16;

                        Log.d("tototot", "Heart rate format UINT16.");

                    } else {

                        format = BluetoothGattCharacteristic.FORMAT_UINT8;

                        Log.d("tototo", "Heart rate format UINT8.");

                    }

                    final int heartRate = characteristicData.getIntValue(format, 1);

                    Log.d("tototo", String.format("Received heart rate: %d", heartRate));*/


                    //--------------------


                    //Log.i("char data 2 " , "" + characteristicData.getStringValue(0));
                }
            }
            /*if (dialog.isShowing()){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.hide();
                    }
                });
            }*/





        }
        @Override
        public synchronized void onCharacteristicChanged(BluetoothGatt gatt,
                                                         BluetoothGattCharacteristic characteristic) {
            weight = findViewById(R.id.weightfinal);
            gatt.setCharacteristicNotification(characteristic, false);
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
                weight.setText("" + d);
                weight_valid = true;
            }
           // weight.setText();
        //    gatt.setCharacteristicNotification(characteristic, false);
            /*Log.i("lenght " , "" + dataInput.length);
            Log.i("final data state chenged " , " "+ dataInput);
            Log.i("ramesh0" , "" + unsignedByteToInt(dataInput[0]));
            Log.i("ramesh1" , "" + unsignedByteToInt(dataInput[1]));
            Log.i("ramesh2" , "" + unsignedByteToInt(dataInput[2]));
            Log.i("ramesh3" , "" + unsignedByteToInt(dataInput[3]));
            Log.i("ramesh4" , "" + unsignedByteToInt(dataInput[4]));
            Log.i("ramesh5" , "" + unsignedByteToInt(dataInput[5]));
            Log.i("ramesh6" , "" + unsignedByteToInt(dataInput[6]));
*/          //Intent i = new Intent(getApplicationContext(),Main2Activity.class);
            //i.putExtra("pakad", "mohit : " + temp);
            //startActivity(i);

            //onDestroy();
            //finish();
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

    @Override
    public void onBackPressed() {
        //moveTaskToBack(true);
        /*qr_code_moved = true;
        if(camera_screen){
            camera_screen = false;
            //qr_code_moved = false;
            setContentView(R.layout.activity_main);
        }else {*/
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
                            /*moveTaskToBack(true);
                            finishAffinity();
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);*/
                            //moveTaskToBack(true);
                            //setContentView(R.layout.activity_main);
                            Intent m = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(m);
                            //onDestroy();

                            //finish();
                            /*Intent m = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(m);*/

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        //}
    }


}
