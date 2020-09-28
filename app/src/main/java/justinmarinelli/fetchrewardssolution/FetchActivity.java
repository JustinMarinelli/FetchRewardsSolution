package justinmarinelli.fetchrewardssolution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FetchActivity extends AppCompatActivity {

    // A list containing a list of DataRows contains a datum's ID,
    // listId, and name
    private List<JSONObject> mData = new ArrayList<>();
    // The waiting bar that is displayed after a user attempts to fetch data
    private ProgressBar mWaitingBar;
    // The header TextView for the data
    private LinearLayout mDataHeader;
    // The LinearLayout that will contain each row of data
    private LinearLayout mDataContainer;
    // The FrameLayout that contains the center image
    private FrameLayout mCenterImageHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorNavigationBar));
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mWaitingBar = (ProgressBar)findViewById(R.id.waitingBar);
        mDataHeader = (LinearLayout)findViewById(R.id.dataHeader);
        mDataContainer = (LinearLayout)findViewById(R.id.dataContainer);
        mCenterImageHolder = (FrameLayout)findViewById(R.id.centerIDisplayHolder);

    }

    /**
     * When the fetch button is clicked, this function will fetch the results
     * from the intended URL.
     * @param view The view this function is associated with
     */
    public void fetchResults(View view) {
        mWaitingBar.setVisibility(View.VISIBLE);
        mDataContainer.setVisibility(View.INVISIBLE);
        mDataHeader.setVisibility(View.INVISIBLE);
        try {
            //JSONObject json = new JSONObject(readUrl());
            //String title = (String) json.get("title");
            new DataGetter().execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a configured TextView to place into the ScrollView for the data
     * as a column
     * @param content The text that will be displayed in the column
     * @return A properly configured TextView
     */
    public TextView createTextView(String content)
    {
        int rowHeight = (int) (60 * Resources.getSystem().getDisplayMetrics().density);
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, rowHeight, 1.0f));
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setText(content);
        textView.setBackgroundResource(R.drawable.bottom_data_border);
        textView.setTextColor(Color.parseColor("#300c39"));
        return textView;
    }

    /**
     * Display the data that was fetched from the URL
     */
    public void displayData() throws JSONException {
        mWaitingBar.setVisibility(View.INVISIBLE);
        mCenterImageHolder.setVisibility(View.INVISIBLE);
        mDataHeader.setVisibility(View.VISIBLE);
        mDataContainer.setVisibility(View.VISIBLE);

        for (JSONObject obj : mData)
        {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            TextView idView = createTextView(obj.get("id").toString());
            TextView listIdView = createTextView(obj.get("listId").toString());
            TextView nameView = createTextView(obj.get("name").toString());

            row.addView(idView);
            row.addView(listIdView);
            row.addView(nameView);

            mDataContainer.addView(row);


        }
    }

    /**
     * Sort data first by the listId, then by the name
     * @param data The data we're sorting
     */
    public void sortData(List<JSONObject> data)
    {
        Collections.sort(data, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                try {
                    String listId1 = (String)o1.get("listId").toString();
                    String listId2 = (String)o2.get("listId").toString();
                    int listIdComp = listId1.compareTo(listId2);

                    if (listIdComp != 0)
                    {
                        return listIdComp;
                    }

                    int name1 = Integer.parseInt(o1.get("id").toString());
                    int name2 = Integer.parseInt(o2.get("id").toString());
                    return Integer.compare(name1, name2);

                } catch (JSONException e) {
                    e.printStackTrace();
                    return -1;
                }
            }
        });
    }

    /**
     * Populate the mData member variable with the data that was fetched from
     * the source
     * @param data The data that was fetched
     */
    private void populateData(JSONArray data) throws JSONException {
        for (int i = 0; i < data.length(); i++) {
            JSONObject obj = data.getJSONObject(i);
            if (!obj.get("name").equals(null) && !TextUtils.isEmpty(obj.get("name").toString()) &&
                    obj.has("listId"))
            {
                mData.add(obj);
            }
        }
    }

    /**
     * Read the content from the URL our data is at
     * @return A string containing the content at the given URL
     */
    private static JSONArray readUrl() throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL("https://fetch-hiring.s3.amazonaws.com/hiring.json");
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            JSONArray arr = new JSONArray(buffer.toString());
            return arr;
        }
        catch (JSONException e) {
            Log.e("DataException", "Data could not be fetched", e);
            return null;
        }finally {
            if (reader != null)
                reader.close();
        }
    }

    private class DataGetter extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                JSONArray data = readUrl();
                assert data != null;
                populateData(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            sortData(mData);
            try {
                displayData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
