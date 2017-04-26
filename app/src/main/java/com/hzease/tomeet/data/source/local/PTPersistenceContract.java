package com.hzease.tomeet.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by Key on 2017/2/20 13:47
 * email: MrKey.K@gmail.com
 * description:
 */

public final class PTPersistenceContract {
    private PTPersistenceContract() {
    }

    public static abstract class PTEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_COMPLETED = "completed";
    }
}
