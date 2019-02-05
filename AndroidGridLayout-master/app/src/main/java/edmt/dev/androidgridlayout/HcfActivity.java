package edmt.dev.androidgridlayout;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HcfActivity extends ListActivity {

    private ListView mListView;

    //Elements that will be displayed in android ListView
    String[] Countries = new String[]{"India", "Australia", "Newzealand",
            "Indonesia", "China", "Russia", "Japan", "South Korea"};

    String[] itemname ={
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War","India", "Australia", "Newzealand",
            "Indonesia", "China", "Russia", "Japan", "South Korea"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hcf);

        //ListView lv = (ListView)findViewById(R.id.list_view);
        this.setListAdapter(new ArrayAdapter<String>(
                this, R.layout.mylist,
                R.id.Itemname,SplashActivity.spinnerArray));



    }
}
