package com.basilarray.taskrenotifier.db;

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.basilarray.taskrenotifier.db.tasks";
    public static final int DB_VERSION = 1;
    //public static final String TABLE = "tasks";

    public class Tasks {
        public static final String TABLENAME = "tasks";
        public static final String _ID = BaseColumns._ID;
        public static final String TITLE = "title"; // TEXT
        public static final String DESCRIPTION = "description"; // TEXT
        public static final String FREQTYPE = "freq_type"; // INTEGER
        public static final String FREQNUM = "freq_num"; // INTEGER
        public static final String FREQUNITS = "freq_units"; // INTEGER
        public static final String DT_START = "dt_start"; // INTEGER
        public static final String ENABLED = "enabled"; // INTEGER
        public static final String ALLPUSHOUT = "allpushout"; // INTEGER
    }

    public class Instances {
        public static final String TABLENAME = "instances";
        public static final String PARENT_ID = "parent_id";
        public static final String _ID = BaseColumns._ID;
        public static final String DT_DUE = "dt_due";
        public static final String DT_DONE = "dt_done";
    }
}
