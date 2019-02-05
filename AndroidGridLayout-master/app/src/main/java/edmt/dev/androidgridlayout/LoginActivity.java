package edmt.dev.androidgridlayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passEditText;
    CircularProgressButton circularProgressButton ;
    static public String email_id ;
    SessionManagement manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        manager = new SessionManagement(getApplicationContext());

        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_DENIED)||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH)
                        == PackageManager.PERMISSION_DENIED)||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_ADMIN)
                        == PackageManager.PERMISSION_DENIED)||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_DENIED)
                )
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 0);


        /*if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_PHONE_STATE}, 0);*/


        if(manager.isLoggedIn()){
         Intent i = new Intent(getApplicationContext(),MainActivity.class);
         startActivity(i);
         finish();
        }




        // Address the email and password field
        emailEditText = (EditText) findViewById(R.id.username);
        passEditText = (EditText) findViewById(R.id.password);
        circularProgressButton = (CircularProgressButton)findViewById(R.id.login);
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                circularProgressButton.startAnimation();
                //checkLogin();

                final String email = emailEditText.getText().toString();
                if (!isValidEmail(email)) {
                    //Set error message for email field
                    emailEditText.setError("Invalid Email");
                }

                final String pass = passEditText.getText().toString();
                if (!isValidPassword(pass)) {
                    //Set error message for password field
                    passEditText.setError("Password cannot be empty");
                }


                if(isValidEmail(email) && isValidPassword(pass))
                {
                    // Validation Completed
                    final Intent i = new Intent(getApplicationContext(),MainActivity.class);

                    //http://cyphertextsolutions.com/awcs/dev/api/login_authentication.php?email=adminawcs@gmail.com&password=admin@123

                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get("http://awcs.net.in/api/login_authentication.php?email="
                                    +emailEditText.getText()+"&password="+passEditText.getText()
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
                                        Log.i("test" , "" + test);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    if(test.equals("admin")|| test.equals("HCF")|| test.equals("superadmin")) {
                                        Toast.makeText(getApplicationContext(),"Welcome "+ emailEditText.getText()  , Toast.LENGTH_SHORT).show();
                                        circularProgressButton.doneLoadingAnimation(Color.parseColor("#333639"), BitmapFactory.decodeResource(getResources(),R.drawable.ic_done_white_48dp));
                                        //btn.revertAnimation();
                                        email_id = emailEditText.getText().toString();
                                        manager.createLoginSession(emailEditText.getText().toString());
                                        Log.i("mohit", "is logged in :" + manager.isLoggedIn());
                                        startActivity(i);
                                        finish();
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Invalid Username or Password"  , Toast.LENGTH_SHORT).show();
                                        circularProgressButton.revertAnimation();
                                        circularProgressButton.setBackground(getResources().getDrawable(R.drawable.shape_default));
                                    }

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                    Toast.makeText(getApplicationContext(),"Connection Problem - Please Try Again" , Toast.LENGTH_LONG).show();
                                    circularProgressButton.revertAnimation();
                                    circularProgressButton.setBackground(getResources().getDrawable(R.drawable.shape_default));
                                }
                            });

                    // btn.revertAnimation();




                }else{
                    circularProgressButton.revertAnimation();
                }























            }
        });

    }
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
                finishAffinity();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);


            }

        }
    }

    public void checkLogin() {   //View arg0




        final String email = emailEditText.getText().toString();
        if (!isValidEmail(email)) {
            //Set error message for email field
            emailEditText.setError("Invalid Email");
        }

        final String pass = passEditText.getText().toString();
        if (!isValidPassword(pass)) {
            //Set error message for password field
            passEditText.setError("Password cannot be empty");
        }


        if(isValidEmail(email) && isValidPassword(pass))
        {
            // Validation Completed
            final Intent i = new Intent(getApplicationContext(),MainActivity.class);



            AsyncHttpClient client = new AsyncHttpClient();
            client.get("http://awcs.net.in/api/login_authentication.php?email="
                    +emailEditText.getText()+"&password="+passEditText.getText()
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


                    if(test.equals("pass")) {
                        Toast.makeText(getApplicationContext(),"Welcome "+ emailEditText.getText()  , Toast.LENGTH_SHORT).show();
                        //btn.revertAnimation();
                        startActivity(i);
                    }else{
                        Toast.makeText(getApplicationContext(),"Invalid Username or Password"  , Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Toast.makeText(getApplicationContext(),"Connection Problem - Please Try Again" , Toast.LENGTH_LONG).show();
                }
            });

           // btn.revertAnimation();




        }

    }

    // validating email id
    private boolean isValidEmail(String email) {
        /*String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();*/

        if(email.equals("")){
            return false;
        }else{
            return true;
        }

    }

    // validating password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 1) {
            return true;
        }
        return false;
    }

    public void skip(View view){

        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        email_id = "test@awcs.com";
        startActivity(i);
    }

}
