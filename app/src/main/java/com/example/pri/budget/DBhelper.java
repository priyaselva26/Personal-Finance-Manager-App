package com.example.pri.budget;

/**
 * Created by pri on 2/1/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;


public class DBhelper extends SQLiteOpenHelper {

    static final String DATABASE = "financeManager.db";
    static final int VERSION = 9;
    static final String TABLE1 = "Exp_Category";
    static final String TABLE5 = "Inc_Category";
    static final String TABLE2 = "Budget";
    static final String TABLE3 = "Expenses";
    static final String TABLE4 = "Incomes";
    public final String TABLE6 = "month_budget_table";

    static final String C_ID = "_id";
    static final String Name = "name";
    static final String B_ID = "_id";
    static final String Description = "description";
    static final String Amount = "amount";

    public static final String ID1 = "_id";
    public static final String DATE_T1 = "date1";
    public static final String CATEGORY = "category";
    public static final String DETAIL = "detail";
    public static final String AMOUNT1 = "amount1";
    public static final String STATUS = "status";
    public static final String EX_YEAR = "exyear";
    public static final String EX_MONTH = "exmonth";


    public  static  final  String COL_1 = "YEAR";
    public  static  final  String COL_2 = "MONTH";
    public  static  final  String COL_3 = "AMOUNT";

    public DBhelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE1 + "(" + C_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + Name + " text unique not null)");

        db.execSQL("CREATE TABLE " + TABLE5 + "(" + C_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + Name + " text unique not null)");

        db.execSQL("CREATE TABLE " + TABLE2 + "(" + B_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + Description + " text,"
                + Amount + " text, FOREIGN KEY (" + Description + ") REFERENCES " + TABLE1 + "(" + Name + "));");

        db.execSQL("CREATE TABLE " + TABLE3 + " ( "
                + ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE_T1 + " text, "
                + CATEGORY + " text, "
                + DETAIL + " text, "
                + STATUS + " text, "
                + EX_YEAR + " text, "
                + EX_MONTH + " text, "
                + AMOUNT1 + " text, FOREIGN KEY (" + CATEGORY + ") REFERENCES " + TABLE1 + "(" + Name + "));");


        db.execSQL("CREATE TABLE " + TABLE4 + " ( "
                + ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE_T1 + " text, "
                + CATEGORY + " text, "
                + DETAIL + " text, "
                + STATUS + " text, "
                + EX_YEAR + " text, "
                + EX_MONTH + " text, "
                + AMOUNT1 + " text, FOREIGN KEY (" + CATEGORY + ") REFERENCES " + TABLE5 + "(" + Name + "));");


       /* db.execSQL("CREATE TABLE " + TABLE_NAME + " ( "
               // +COL_1 + " INTEGER, "
                +COL_2 + " INTEGER PRIMARY KEY, "
                +COL_3 + " DOUBLE)");

*/
        db.execSQL("CREATE TABLE " + TABLE6 + "(" + COL_2
                + " INTEGER PRIMARY KEY ," + COL_3 + " DOUBLE)");

       // db.execSQL("CREATE TABLE month_budget_table (Month INTEGER PRIMARY KEY, Amount DOUBLE)");

        db.execSQL("insert into   month_budget_table (MONTH, AMOUNT) values(3,25000)");

        db.execSQL("insert into " +  TABLE1 +  "(" + C_ID +"," + Name +") values(1,'Food')");
        db.execSQL("insert into " +  TABLE1 +  "(" + C_ID +"," + Name +") values(2,'Clothing')");
        db.execSQL("insert into " +  TABLE1 +  "(" + C_ID +"," + Name +") values(3,'Home')");

        db.execSQL("insert into " +  TABLE3 +  "(" + ID1 +"," + DATE_T1 +"," + CATEGORY +"," + DETAIL +"," + STATUS +"," + EX_YEAR +"," + EX_MONTH +"," + AMOUNT1 +") values(1,'4-1-2016','Food','lunch','paid','2017','1','500')");


        db.execSQL("insert into " +  TABLE5 +  "(" + C_ID +"," + Name +") values(1,'Salary')");
        db.execSQL("insert into " +  TABLE5 +  "(" + C_ID +"," + Name +") values(2,'Pocket Money')");
        db.execSQL("insert into " +  TABLE5 +  "(" + C_ID +"," + Name +") values(3,'Repayment')");

       // db.execSQL("insert into " +  TABLE4 +  "(" + ID1 +"," + DATE_T1 +"," + CATEGORY +"," + DETAIL +"," + STATUS +"," + EX_YEAR +"," + EX_MONTH +"," + AMOUNT1 +") values(1,'4-1-2016','Salary','md','paid','2017','1','5000')");



    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table " + TABLE1);
        onCreate(db);
    }

    //expense
    public ArrayList<category> getCategories() {
        ArrayList<category> arrayList = new ArrayList<category>(); //create a araaylist having objects in it
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(DBhelper.TABLE1, null, null, null, null, null, null); //cursor contain collection of data for particula query
        while (c.moveToNext()) {
            category cat = new category(c.getInt(0), c.getString(1)); //get the name and id
            arrayList.add(cat);//assign those name and id to the arraylist

        }

        return arrayList;
    }

    //income
    public ArrayList<category> getCategories1() {
        ArrayList<category> arrayList = new ArrayList<category>(); //create a araaylist having objects in it
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(DBhelper.TABLE5, null, null, null, null, null, null); //cursor contain collection of data for particula query
        while (c.moveToNext()) {
            category cat = new category(c.getInt(0), c.getString(1)); //get the name and id
            arrayList.add(cat);//assign those name and id to the arraylist

        }

        return arrayList;
    }





//check the budget amount with expenses
    public boolean checkBudget(String cat) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT e.category " +
                "FROM Expenses e, Budget b " +
                "WHERE e.category=b.description and e.category='" + cat +
                "' GROUP BY e.category " +
                "HAVING sum(amount1)>b.amount";


        Cursor c = db.rawQuery(query, null);
        if (c.moveToNext()) {
            return true;
        }

        return false;
    }

    //get the category expenses
    public ArrayList<Expence> getCategoryExpences(String category) {
        ArrayList<Expence> expences = new ArrayList<Expence>();

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int tmonth = c.get(Calendar.MONTH);
        int month = tmonth + 1;

        String query = "SELECT * FROM " + TABLE3 + " WHERE " + EX_YEAR + "='" + year + "' and " + EX_MONTH + "='" + month + "' and " + CATEGORY + "='" + category + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Expence ex = new Expence();
                ex.setCategory(category);
                ex.setDate(cursor.getString(1));
                ex.setDescription(cursor.getString(3));
                ex.setStatus(cursor.getString(4));
                ex.setYear(cursor.getString(5));
                ex.setMonth(cursor.getString(6));
                ex.setAmount(cursor.getString(7));
                expences.add(ex);
            }
            while (cursor.moveToNext());
        } else {
            return null;
        }
        return expences;
    }

    public boolean insertData(int month, double amount){
        long result;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(COL_1, 2016);
        contentValues.put(COL_2,month);
        contentValues.put(COL_3,amount);

        Cursor res = db.rawQuery("select * from " + TABLE6 , null);

        if (res.getCount() == 0) {
            result = db.insert(TABLE6,null,contentValues);

        }
        else {
           // result = db.update(TABLE_NAME,contentValues, COL_2+"="+month + " AND " + COL_1 + "="+year, null);
            result = db.update(TABLE6,contentValues, COL_2+"="+month , null);
        }


        //long result = db.insert(TABLE_NAME,null,contentValues);

        if(result == -1)
            return  false;
        else
            return true;


    }

    public Cursor getMonthlyBudget(int month){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE6 + " where " + COL_2 + "=" + month, null);

        return  res;
    }

    public Cursor calBudget(int month){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE6 + " where "+COL_2+" = " + month, null);



        return  res;

    }




}
