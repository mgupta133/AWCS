package edmt.dev.androidgridlayout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class TempScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{


    private ZXingScannerView mScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_scanner);
        QrScanner();
    }

    public void QrScanner(){


        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view<br />
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.<br />
        mScannerView.startCamera();         // Start camera<br />



    }

    @Override
    public void handleResult(Result result) {
        mScannerView.stopCamera();
        Log.i("mohit", "" + result);
        /*if(result.toString().length()!=30){
            Toast.makeText(getApplicationContext(),"Wrong QR code",Toast.LENGTH_SHORT).show();
            finish();
        }*/
        String temp = "" + result;
        Log.i("mohit" , "yhi hai jo hai" + temp.charAt(15));
        String substr = result.toString().substring(result.toString().length() - 5);
        final String hname = result.toString().substring(0,4);
        Log.i("mohit" , "" + substr);
        //substr = "00001";
        //final int hcf_id = Integer.parseInt(substr);
        final String hcf_id = substr;
        Log.i("hcf_id : " , "" + hcf_id);
        int id = Integer.parseInt("" + hcf_id);
        Log.i("hcf ka naam","" + SplashActivity.map.get("" + id));
        Capture_Activity.HCF_ka_name = (SplashActivity.map.get("" + id));
        Capture_Activity.HCF_ka_id = id;
        Capture_Activity.HCF_ka_validpana = true;
        Capture_Activity.bag_ki_id= result.toString().substring(5,15);
        int color_id = Integer.parseInt(String.valueOf(temp.charAt(15)));
        if(color_id==0){
            color_id = -1;
        }
        Capture_Activity.Color_ki_id = color_id;
        String tm = SplashActivity.color_map.get("" + color_id);
        Capture_Activity.Color_ka_name = SplashActivity.color_map.get("" + color_id);
        Log.i("mohittttttt" ,""+ Capture_Activity.Color_ka_name);
        finish();
    }
}
