package edmt.dev.androidgridlayout;

import android.app.Activity;
import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class RoutesActivity extends Activity {

    String[] itemname ={
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8","9", "10", "11",
            "12", "13", "14", "15", "16"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        /*this.setListAdapter(new ArrayAdapter<String>(
                this, R.layout.mylist2,
                R.id.Itemname,itemname));
    }*/
    }
}
