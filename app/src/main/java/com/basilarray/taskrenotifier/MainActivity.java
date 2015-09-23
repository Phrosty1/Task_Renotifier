package com.basilarray.taskrenotifier;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private Context mContext;
    public ScrollView svTasks;
    public ScrollView svTaskInstances;

    public class SortableTextView extends TextView {
        public ScrollView svParent;
        public Task task;

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            //if (event.getAction() == MotionEvent.ACTION_DOWN && event.getX() <= this.getHeight()) this.performLongClick();
            // LinearLayout ll = (LinearLayout) this.getParent();
            // ScrollView sv = (ScrollView) ll.getParent();
            // sv.smoothScrollBy(0, 500);
            return super.onTouchEvent(event);
        }

        public SortableTextView(Context context, Task inTask) {
            super(context);
            task = inTask;
            this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // SP for text, DIP for other
            this.setText(task.sTitle);
            //this.setCompoundDrawables(imgDragDrop, null, null, null);
        }
    }

    public class Task {
        public String sTitle, sDescription;
        public int freqType, freqNumber, freqUnits;
        public Long dtStart = new Long(0), dtLastSnoozed = new Long(0);
        public boolean bAllPushOut;
        public ArrayList<Long> tInstances = new ArrayList<Long>();

        public Task(String inImport) {
            String[] properties = Util.explode(inImport, cDivProperty);
            sTitle = properties[0];
            sDescription = properties[1];
            //Log.d("tmp","properties[2]:"+properties[2]);
            freqType = Integer.parseInt(properties[2]);
            freqNumber = Integer.parseInt(properties[3]);
            freqUnits = Integer.parseInt(properties[4]);
            //Log.d("tmp","properties[5]:"+properties[5]);
            dtStart = Long.parseLong(properties[5]);
            dtLastSnoozed = Long.parseLong(properties[6]);
            //Log.d("tmp","properties[7]:"+properties[7]);
            bAllPushOut = Boolean.parseBoolean(properties[7]);
            if (properties[8].compareTo("null") != 0) {
                String[] instances = Util.explode(properties[8], cDivInstance);
                for (String e : instances) tInstances.add(Long.parseLong(e));
            }
        }

        public Task(String inTitle, String inDescription) {
            sTitle = inTitle;
            sDescription = inDescription;
        }

        public String getExport() {
            String sInstances = Util.implode(tInstances.toArray(), cDivInstance);
            return Util.implode(new String[]{sTitle, sDescription, String.valueOf(freqType), String.valueOf(freqNumber), String.valueOf(freqUnits), String.valueOf(dtStart), String.valueOf(dtLastSnoozed), String.valueOf(bAllPushOut), sInstances}, cDivProperty);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Thread threadInit = new Thread(initializeScreen);
        threadInit.start();
    }

    //public static final long dfltLong = new Date().getTime();
    //public static final long dfltLong = 0;
    public static final String cDivTask = ",", cDivProperty = "|", cDivInstance = "`";
    public static final int meEVERY = 1, meEACH = 2;
    public static final int muDAYS = 1, muWEEKS = 2, muMONTHS = 3;
    public static final int muSUNDAY = Calendar.SUNDAY, muMONDAY = Calendar.MONDAY, muTUESDAY = Calendar.TUESDAY, muWEDNESDAY = Calendar.WEDNESDAY, muTHURSDAY = Calendar.THURSDAY, muFRIDAY = Calendar.FRIDAY, muSATURDAY = Calendar.SATURDAY, muDAY = 8, muWEEKDAY = 9, muWEEKENDDAY = 10;//calendar.get(Calendar.DAY_OF_WEEK)
    Runnable initializeScreen = new Runnable() {
        @Override
        public void run() {
            svTaskInstances = (ScrollView) findViewById(R.id.svTaskInstances);

            SharedPreferences lSettings = getPreferences(Activity.MODE_PRIVATE);

            Task dfltTask = new Task("Feed Kitty", "The kitty likes its food");
            String dfltExport = dfltTask.getExport();
            Log.d("tmp", "dfltExport:" + dfltExport);
            String sAllTasks = lSettings.getString("Tasks", dfltExport);
            //ListView mListView = (ListView) findViewById(R.id.sampleListView);
            //TextView mTextView = new TextView(mContext);
            //TextView mTextView = (TextView) findViewById(R.id.section_label);
            //mTextView.setText("Newly Added");
            //mListView.addView(mTextView);
            String[] tAllTasks = Util.explode(sAllTasks, cDivTask);
            for (String e : tAllTasks) {
                SortableTextView tmpTextView = new SortableTextView(mContext, new Task(e));
                ((LinearLayout) svTaskInstances.getChildAt(0)).addView(tmpTextView);
            }
        }
    };

    public void Remind(String title, String text) {
        Log.d("tmp", "Alarm Created Begin");
        //Toast.makeText(this, "Alarm Created Begin", Toast.LENGTH_LONG).show();
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra("title", title);
        alarmIntent.putExtra("text", text);

        //PendingIntent pendingIntent = PendingIntent.GetBroadcast(Forms.Context, 0, alarmIntent, PendingIntentFlags.UpdateCurrent);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //AlarmManager alarmManager = (AlarmManager) Forms.Context.GetSystemService(Context.AlarmService);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + (10 * 1000), pendingIntent); // 30 seconds
//        try {
//            Log.d("tmp", "BEFORE pendingIntent");
//            pendingIntent.send();
//            Log.d("tmp", "AFTER pendingIntent");
//        }catch (Exception e){
//            Log.d("tmp", "Alarm exception:"+e.getMessage());
//        }

        Log.d("tmp", "Alarm Created End");
        //Toast.makeText(this, "Alarm Created End", Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
