package edmt.dev.androidgridlayout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;

public class AutoActivity extends AppCompatActivity {
    ArrayList<String> local_spinnerArray, local_spinnerArray2;
    MaterialSpinner spinner_color;
    SearchableSpinner spinner_hcf;
    String COLOR;
    Boolean color_valid;
    Boolean hcf_adapted;
    Boolean hcf_valid;
    Button next;
    TextView hcf_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto);
        color_valid = true;
        hcf_adapted = false;
        hcf_valid = true;

        final String info = getIntent().getStringExtra("info");
        //tv.setText(info);
        Log.i("mohit", "" + info);
        String substr = info.substring(info.length() - 5);
        final String hname = info.substring(0,4);
        Log.i("mohit" , "" + substr);
        //substr = "00001";
        //final int hcf_id = Integer.parseInt(substr);
        final String hcf_id = substr;
        Log.i("hcf_id : " , "" + hcf_id);

        hcf_name = findViewById(R.id.hcf);
        //String temp = SplashActivity.hcf_response.get()


        hcf_name.setText("" + hname);

        local_spinnerArray = new ArrayList<String>(SplashActivity.spinnerArray);
        local_spinnerArray2 = new ArrayList<String>(SplashActivity.spinnerArray2);
        spinner_color = (MaterialSpinner) findViewById(R.id.spinner);
        //MaterialSpinner spinner_hcf = (MaterialSpinner)findViewById(R.id.hcf);
        //spinner_hcf = (SearchableSpinner) findViewById(R.id.hcf);



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


        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if (spinner_hcf.getSelectedItem() == null)
                    hcf_valid = false;
                else
                    hcf_valid = true;*/

                if(hcf_valid && color_valid) {
                    Intent i = new Intent(getApplicationContext(), Capture_Activity.class);
                    //int hcf_id = Integer.parseInt(SplashActivity.hcfIds.get(spinner_hcf.getSelectedItemPosition())) ;
                    int color_id = Integer.parseInt(SplashActivity.colorIDs.get(spinner_color.getSelectedIndex()) );
                    i.putExtra("hcf_name","" + hname);
                    i.putExtra("color_name" , "" + SplashActivity.spinnerArray2.get(spinner_color.getSelectedIndex()));
                    i.putExtra("hcf_id",""+hcf_id);
                    i.putExtra("color_id", "" + color_id);
                    Log.i("kamini","" + hcf_id + "  " + color_id + " "  + info+" " +
                            SplashActivity.spinnerArray2.get(spinner_color.getSelectedIndex()));
                    startActivity(i);
                    finish();
                }else{
                    if(!color_valid)
                        Toast.makeText(getApplicationContext(),"Please select Color",Toast.LENGTH_SHORT).show();
                    if(!hcf_valid)
                        Toast.makeText(getApplicationContext(),"Please select HCF",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
