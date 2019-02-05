package edmt.dev.androidgridlayout;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    GridLayout mainGrid;
    private ZXingScannerView mScannerView;
    private static int SPLASH_TIME_OUT = 10000;
    public boolean camera_screen ;
    public boolean qr_code_moved ;
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    SessionManagement manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
//                == PackageManager.PERMISSION_DENIED)
//            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 0);
//

        manager = new SessionManagement(getApplicationContext());
        qr_code_moved = false;
        camera_screen = false;

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);


        //Set Event
        //setSingleEvent(mainGrid);
        //setToggleEvent(mainGrid);
    }
    /*@Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);


            }

        }
    }*/

    public void Click2(View view){
        Intent i = new Intent(getApplicationContext(),AutoActivity.class);
        startActivity(i);
    }

    public void Click(View view){
        //qr_code_moved = false;
        //camera_screen = false;
        final CardView cardView = (CardView)findViewById(R.id.barcode);
        cardView.setCardBackgroundColor(Color.parseColor("#FF6F00"));
        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));

        Intent i = new Intent(getApplicationContext(),Capture_Activity.class);
        //i.putExtra("info" , "" + rawResult.getText());
        startActivity(i);
        setContentView(R.layout.activity_main);

        /*
        QrScanner();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                // close this activity
                if(!qr_code_moved)
                Return();

                //setContentView(R.layout.activity_main);

            }
        }, SPLASH_TIME_OUT);
        */
    }

    public void Click_zerowaste(View view){
        Intent i = new Intent(getApplicationContext(),ZeroWasteActivity.class);
        startActivity(i);
    }

    public void hcf(View view){
        Intent i = new Intent(getApplicationContext(),HcfActivity.class);
        startActivity(i);
    }

    public void Routes(View view){
        Intent i = new Intent(getApplicationContext(),RoutesActivity.class);
        startActivity(i);
    }

    public void About(View view){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Are you sure you want to quit ?");
        builder.setMessage("Are you sure you want to Sign-out ?")
                .setCancelable(true)
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Intent i = new Intent(getApplicationContext(), ManualActivity.class);
                        //startActivity(i);
                        dialog.cancel();
                        //setContentView(R.layout.activity_main);
                        //dialog.cancel();
                    }
                })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        manager.logoutUser();
                        finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();



       /* Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);*/
    }

    public void QrScanner(){


        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view<br />
        setContentView(mScannerView);
                mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.<br />
        mScannerView.startCamera();         // Start camera<br />
        camera_screen = true;
        /*Intent intent_local = new Intent(getApplicationContext(),Main2Activity.class);
        startActivity(intent_local);*/


    }

    public void Return(){
        if(!qr_code_moved) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No barcode found");
            builder.setMessage("Do you want to try again?")
                    .setCancelable(false)
                    .setPositiveButton("Enter Manually", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            camera_screen = false;
                            Intent i = new Intent(getApplicationContext(), ManualActivity.class);
                            startActivity(i);
                            setContentView(R.layout.activity_main);
                            //dialog.cancel();
                        }
                    })
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            QrScanner();
                            new Handler().postDelayed(new Runnable() {
                                /*
                                 * Showing splash screen with a timer. This will be useful when you
                                 * want to show case your app logo / company
                                 */
                                @Override
                                public void run() {
                                    // This method will be executed once the timer is over
                                    // Start your app main activity
                                    // close this activity
                                    if (!qr_code_moved)
                                        Return();

                                    //setContentView(R.layout.activity_main);

                                }
                            }, SPLASH_TIME_OUT);


                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }


    public void Manual(View view){
        //Toast.makeText(getApplicationContext(),"Access Denied" , Toast.LENGTH_SHORT).show();

        Intent manual = new Intent(getApplicationContext(),ManualActivity.class);
        startActivity(manual);
    }

    private void setToggleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FF6F00"));
                        Toast.makeText(MainActivity.this, "State : True", Toast.LENGTH_SHORT).show();

                    } else {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        Toast.makeText(MainActivity.this, "State : False", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*if(finalI == 0)
                    {*/
                        //Intent intent_local = new Intent(getApplicationContext(),Main2Activity.class);
                        //startActivity(intent_local);
                        Log.i("mohit","qr");

                        QrScanner();



                        new Handler().postDelayed(new Runnable() {



                            /*
                             * Showing splash screen with a timer. This will be useful when you
                             * want to show case your app logo / company
                             */

                            @Override
                            public void run() {
                                // This method will be executed once the timer is over
                                // Start your app main activity
                                // close this activity
                                Return();

                                //setContentView(R.layout.activity_main);

                            }
                        }, SPLASH_TIME_OUT);



                    //}
                    /*else if(finalI == 1)
                    {
                        Intent intent_local = new Intent(getApplicationContext(),ManualActivity.class);
                        startActivity(intent_local);

                    }
                    else {
                        Intent intent = new Intent(MainActivity.this, ActivityOne.class);
                        intent.putExtra("info", "This is activity from card item index  " + finalI);
                        startActivity(intent);
                    }*/
                }
            });
        }
    }


    @Override
    public void handleResult(Result rawResult) {

        qr_code_moved = true;
        // Do something with the result here</p>
       Log.e("handler", rawResult.getText()); // Prints scan results<br />
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)</p>
        // show the scanner result into dialog box.<br />
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Scan Result");
                builder.setMessage(rawResult.getText());
                AlertDialog alert1 = builder.create();
                //alert1.show();
       // If you would like to resume scanning, call this method below:<br />
         //mScannerView.resumeCameraPreview(this);
        //setContentView(R.layout.activity_main);
        Intent i = new Intent(getApplicationContext(),Capture_Activity.class);
        i.putExtra("info" , "" + rawResult.getText());
        startActivity(i);
        setContentView(R.layout.activity_main);

    }
    @Override
    public void onBackPressed() {
        //moveTaskToBack(true);
        qr_code_moved = true;
        if(camera_screen){
            camera_screen = false;
            //qr_code_moved = false;
            setContentView(R.layout.activity_main);
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setTitle("Are you sure you want to quit ?");
            builder.setMessage("Are you sure you want to quit ?")
                    .setCancelable(false)
                    .setPositiveButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Intent i = new Intent(getApplicationContext(), ManualActivity.class);
                            //startActivity(i);

                            setContentView(R.layout.activity_main);
                            //dialog.cancel();
                        }
                    })
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            moveTaskToBack(true);
                            finishAffinity();
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                            //moveTaskToBack(true);

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

}
