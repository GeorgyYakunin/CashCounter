package com.Example.cashcounter.Class;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class Database extends SQLiteOpenHelper {
    public static final String ASC = " ASC";
    public static final int AVAILABLE = 1;
    public static final int CASH_IN = 1;
    public static final int CASH_OUT = 2;
    public static final String COL_CI_AMOUNT = "col_ci_amount";
    public static final String COL_CI_QTY = "col_ci_qty";
    public static final String COL_C_AMOUNT = "col_c_amount";
    public static final String COL_C_STATUS = "col_c_status";
    public static final String COL_E_AMOUNT = "col_e_amount";
    public static final String COL_E_TOTAL_IN_QTY = "col_e_total_in_qty";
    public static final String COL_E_TOTAL_OUT_QTY = "col_e_total_out_qty";
    public static final String COL_NOTE = "col_note";
    public static final String COL_PK_DURATION = "COL_P_DURATION";
    public static final String COL_PK_ID = "COL_P_ID";
    public static final String COL_PK_NAME = "COL_P_NAME";
    public static final String COL_PK_RATE = "COL_P_RATE";
    public static final String COL_PURPOSE = "col_purpose";
    public static final String COL_P_NAME = "col_p_name";
    public static final String COL_P_TRANSATION_TYPE = "col_p_transation_type";
    public static final String COL_P_TYPE = "col_p_type";
    public static final String COL_S_BILL_AMOUNT = "col_s_bill_amount";
    public static final String COL_S_BILL_NO = "col_s_bill_no";
    public static final String COL_S_PAID_AMOUNT = "col_s_paid_amount";
    public static final String COL_S_RETURN_AMOUNT = "col_s_return_amount";
    public static final String COL_S_TOTAL_CASHIN_QTY = "col_s_total_cashin_qty";
    public static final String COL_S_TOTAL_CASHOUT_QTY = "col_s_total_cashout_qty";
    public static final String COL_T_CURRENCY_AMOUNT = "col_t_currency_amount";
    public static final String COL_T_CURRENCY_ID = "col_t_currency_id";
    public static final String COL_T_CURRENCY_QTY = "col_t_currency_qty";
    public static final String COL_T_PARENT_ID = "col_t_parent_id";
    public static final String COL_T_PARENT_TYPE = "col_t_parent_type";
    public static final String CREATED_ON = "created_on";
    public static final String CUSTOMER = "customer";
    public static final String DATABASE_NAME = "CashCounter.db";
    public static final int DATABASE_VERSION = 2;
    public static final int DELETE = 2;
    public static final String DESC = " DESC";
    public static final String PURPOSE_CASHIN = "Cash Received";
    public static final String PURPOSE_CASHOUT = "Cash Paid";
    public static final String PURPOSE_EXCHANGE = "Cash Exchange";
    public static final String PURPOSE_SALES = "Sales";
    public static final String ROW_ID = "ROWID";
    public static final String SR_ID = "sr_id";
    public static final String SYSTEM = "system";
    public static final int SYSTEM_ROW = 1;
    public static final String TABLE_CASH_IN = "table_cash_in";
    public static final String TABLE_CASH_OUT = "table_cash_out";
    public static final String TABLE_CURRENCY = "table_currency";
    public static final String TABLE_EXCHANGE = "table_exchange";
    public static final String TABLE_PACKAGES = "TABLE_PACKAGES";
    public static final String TABLE_PURPOSE = "table_purpose";
    public static final String TABLE_SALES = "table_sales";
    public static final String TABLE_TRANSACTION = "table_transaction";
    public static final int TYPE_CASHIN = 1;
    public static final int TYPE_CASHOUT = 2;
    public static final int TYPE_EXCHANGE = 4;
    public static final int TYPE_SALES = 3;
    public static final int USER_ROW = 2;
    public static final String defaultDateFormate = "yyyy-MM-dd";
    public static final String defaultFormate = "yyyy-MM-dd HH:mm:ss";
    public static final String defaultTimeFormate = "HH:mm:ss";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE table_currency (sr_id INTEGER PRIMARY KEY AUTOINCREMENT,col_c_amount REAL,col_c_status INTEGER DEFAULT 1,created_on DATETIME)");
        sQLiteDatabase.execSQL("CREATE TABLE table_purpose (sr_id INTEGER PRIMARY KEY AUTOINCREMENT,col_p_name TEXT,col_p_transation_type INTEGER,col_p_type INTEGER,created_on DATETIME)");
        sQLiteDatabase.execSQL("CREATE TABLE table_cash_in (sr_id INTEGER PRIMARY KEY AUTOINCREMENT,col_ci_amount REAL,col_ci_qty INTEGER,col_purpose TEXT,col_note TEXT,created_on DATETIME)");
        sQLiteDatabase.execSQL("CREATE TABLE table_cash_out (sr_id INTEGER PRIMARY KEY AUTOINCREMENT,col_ci_amount REAL,col_ci_qty INTEGER,col_purpose TEXT,col_note TEXT,created_on DATETIME)");
        sQLiteDatabase.execSQL("CREATE TABLE table_sales (sr_id INTEGER PRIMARY KEY AUTOINCREMENT,col_s_bill_amount INTEGER,col_s_paid_amount INTEGER,col_s_bill_no TEXT,col_s_return_amount INTEGER,col_s_total_cashin_qty INTEGER,col_s_total_cashout_qty INTEGER,col_purpose TEXT,col_note TEXT,created_on DATETIME)");
        sQLiteDatabase.execSQL("CREATE TABLE table_exchange (sr_id INTEGER PRIMARY KEY AUTOINCREMENT,col_e_amount INTEGER,col_e_total_in_qty INTEGER,col_e_total_out_qty INTEGER,col_purpose TEXT,col_note TEXT,created_on DATETIME)");
        sQLiteDatabase.execSQL("CREATE TABLE table_transaction (sr_id INTEGER PRIMARY KEY AUTOINCREMENT,col_t_parent_id INTEGER,col_t_parent_type INTEGER,col_t_currency_id INTEGER,col_t_currency_amount REAL,col_t_currency_qty INTEGER,created_on DATETIME)");
        sQLiteDatabase.execSQL("CREATE TABLE TABLE_PACKAGES (sr_id INTEGER PRIMARY KEY AUTOINCREMENT,COL_P_ID TEXT,COL_P_NAME TEXT,COL_P_DURATION TEXT,COL_P_RATE TEXT,created_on DATETIME)");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        onCreate(sQLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        onUpgrade(sQLiteDatabase, i, i2);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.disableWriteAheadLogging();
        }
    }
}
