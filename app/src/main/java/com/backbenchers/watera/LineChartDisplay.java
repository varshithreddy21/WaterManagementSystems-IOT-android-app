package com.backbenchers.watera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

public class LineChartDisplay extends AppCompatActivity {
    LineChart lineChart;
    ArrayList<FlowObject> flowObjectArrayList;
    ArrayList<Entry> arrayList;
    ArrayList<String> times;
    String values[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart_display);
        final RequestQueue requestQueue;
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();
        lineChart=findViewById(R.id.linechart);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setNoDataText("");


        values = new String[20];// {"mon","tue","wed","thr","fri","sat","sun" };



        //  flowObjectArrayList=new ArrayList<FlowObject>();
        arrayList=new ArrayList<Entry>();
        times=new ArrayList<String>();
        // textView.setVisibility(View.GONE);
        String myUrl="https://api.thingspeak.com/channels/"+getIntent().getExtras().getInt("id")+"/fields/1.json?results";
        requestQueue.add(sendAPIReequest(myUrl));
        XAxis xAxis = lineChart.getXAxis();
       Object[] objects=times.toArray();
       int j=0;
       for (Object i:objects){

           values[j++]=i.toString();
           System.out.println(i.toString());
       }
        xAxis.setValueFormatter(new MyXAxisValueFormatter(values));



    }




    private JsonObjectRequest sendAPIReequest(String url) {
        // String url="https://api.myjson.com/bins/172jsb";



        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                     JSONArray myWeather = response.getJSONArray("feeds");
                    for (int i = 0; i < myWeather.length(); i++) {

                        JSONObject obj = myWeather.getJSONObject(i);
                        Gson gson = new Gson();
                        FlowObject flowObject = gson.fromJson(obj.toString(), FlowObject.class);
                        arrayList.add(new Entry(i, Float.parseFloat(obj.getString("field1"))));
                        values[i]=flowObject.getTime();

                    }




                    LineDataSet set=new LineDataSet(arrayList,"tap1");
                    set.setFillAlpha(110);
                    ArrayList<ILineDataSet> dataSets=new ArrayList<ILineDataSet>();
                    dataSets.add(set);
                    LineData lineData=new LineData(dataSets);
                    lineChart.setData(lineData);
                    lineChart.invalidate();

                   /* JSONObject obj=response.getJSONObject("main");
                    double fhit=obj.getDouble("temp");
                    temp.setText(String.format("%.2f",fhit-273.15)+(char) 0x00B0+"C");
                    temp.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                    */

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        return jsonObjectRequest;
    }
    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return mValues[(int) value];
        }

        /** this is only needed if numbers are returned, else return 0 */
       // @Override
       // public int getDecimalDigits() { return 0; }
    }
}
