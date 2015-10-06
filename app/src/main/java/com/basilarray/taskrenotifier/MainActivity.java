package com.basilarray.taskrenotifier;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.basilarray.taskrenotifier.db.TaskContract;
import com.basilarray.taskrenotifier.db.TaskDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private Context mContext;
    public ListView svTasks;
    public ListView svTaskInstances;
    //public Drawable imgDragDrop;
    public ViewFlipper vfMain;


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
            //this.setCompoundDrawables(imgDragDrop, null, imgDragDrop, null);
        }
    }

    public class Task {
        public String sTitle, sDescription;
        public int freqType, freqNumber, freqUnits;
        public Long dtStart = new Date().getTime(), dtLastSnoozed = new Date().getTime();
        public boolean bAllPushOut = true, bEnabled = true;
        public ArrayList<Long> tInstances = new ArrayList<Long>();

        public Task(String inImport) {
            String[] properties = Util.explode(inImport, cDivProperty);
            sTitle = properties[0];
            sDescription = properties[1];
            freqType = Integer.parseInt(properties[2]);
            freqNumber = Integer.parseInt(properties[3]);
            freqUnits = Integer.parseInt(properties[4]);
            dtStart = Long.parseLong(properties[5]);
            dtLastSnoozed = Long.parseLong(properties[6]);
            bAllPushOut = Boolean.parseBoolean(properties[7]);
            bEnabled = Boolean.parseBoolean(properties[8]);
            if (properties[9].compareTo("null") != 0) {
                String[] instances = Util.explode(properties[9], cDivInstance);
                for (String e : instances) tInstances.add(Long.parseLong(e));
            }
        }

        public Task(String inTitle, String inDescription) {
            sTitle = inTitle;
            sDescription = inDescription;
        }

        public String getExport() {
            String sInstances = Util.implode(tInstances.toArray(), cDivInstance);
            return Util.implode(new String[]{sTitle, sDescription, String.valueOf(freqType), String.valueOf(freqNumber), String.valueOf(freqUnits), String.valueOf(dtStart), String.valueOf(dtLastSnoozed), String.valueOf(bAllPushOut), String.valueOf(bEnabled), sInstances}, cDivProperty);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Thread threadInit = new Thread(initializeScreen);
        threadInit.start();
        //initializeScreen.run();
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
            Log.d("tmp", "initializeScreen.run");
            vfMain = (ViewFlipper) findViewById(R.id.vfMain);
            svTaskInstances = (ListView) findViewById(R.id.svTaskInstances);
            svTasks = (ListView) findViewById(R.id.svTasks);

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

            //double pixelDensity = getApplicationContext().getResources().getDisplayMetrics().density;
            //imgDragDrop = getResources().getDrawable(R.drawable.ic_stat_name);
            //imgDragDrop.setBounds(0, 0, (int) (pixelDensity * 20), (int) (pixelDensity * 20));

            //((LinearLayout) svTaskInstances.getChildAt(0)).
            String[] tAllTasks = Util.explode(sAllTasks, cDivTask);
            ListView svTestTaskInstances = (ListView) findViewById(R.id.svTaskInstances);
            for (String e : tAllTasks) {
                SortableTextView tmpTextView = new SortableTextView(mContext, new Task(e));
                //SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(mContext, R.layout.task_view, )
                //ListAdapter listAdapter = new Adapter
                //svTestTaskInstances.setAdapter();
                //((LinearLayout) svTaskInstances.getChildAt(0)).addView(tmpTextView);
            }

            if (true) {
                TaskDBHelper helper = new TaskDBHelper(MainActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                //db.execSQL("DROP TABLE IF EXISTS tasks");
                helper.onUpgrade(db, 0, 1);
                //helper.onCreate(db);

                ContentValues values = new ContentValues();
                values.clear();
                values.put(TaskContract.Tasks.TITLE, "Feed Kitty");
                values.put(TaskContract.Tasks.DESCRIPTION, "The kitty likes its food");
                long nTaskID = db.insertWithOnConflict(TaskContract.Tasks.TABLENAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

                values.clear();
                values.put(TaskContract.Instances.PARENT_ID, nTaskID);
                values.put(TaskContract.Instances.DT_DUE, new Date().getTime());
                db.insertWithOnConflict(TaskContract.Instances.TABLENAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            }

            refreshTasks();
            refreshInstances();
        }
    };

    private void refreshTasks() {
        TaskDBHelper helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT " + TaskContract.Tasks._ID + "," + TaskContract.Tasks.TITLE +
                ", (SELECT MAX (dt_due) FROM instances i WHERE i.parent_id = t._id) " + TaskContract.Instances.DT_DUE +
                ", (SELECT datetime (MAX (dt_due), 'unixepoch') FROM instances i WHERE i.parent_id = t._id) txt_dt_due" +
                " FROM tasks t", null);

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.task_view,
                cursor,
                new String[]{TaskContract.Tasks.TITLE, TaskContract.Instances.DT_DUE, "txt_dt_due"},
                new int[]{R.id.txtTitle, R.id.dtDueDone, R.id.txtDueDone},
                0
        );

        svTasks.setAdapter(listAdapter);
        Object o = svTasks.getItemAtPosition(0);
        Log.d("tmp", "svTasks.getChildCount():"+svTasks.getChildCount());
        Log.d("tmp", o.toString());
    }

    private void refreshInstances() {
        TaskDBHelper helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT " + TaskContract.Tasks._ID + ", " + TaskContract.Tasks.TITLE + " FROM " + TaskContract.Tasks.TABLENAME, null);

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.instance_view,
                cursor,
                new String[]{TaskContract.Tasks.TITLE},
                new int[]{R.id.txtTitle},
                0
        );

        svTaskInstances.setAdapter(listAdapter);
    }

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
            vfMain.setDisplayedChild(0);
            return true;
        } else if (id == R.id.action_newtask) {
            vfMain.setDisplayedChild(0);
            return true;
        } else if (id == R.id.action_showTasks) {
            vfMain.setDisplayedChild(1);
            return true;
        } else if (id == R.id.action_showInstances) {
            vfMain.setDisplayedChild(2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
