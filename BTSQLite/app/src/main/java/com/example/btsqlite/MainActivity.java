package com.example.btsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.*;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{
    EditText edtmasv, edttensv, edtmalopsv;
    Button btninsert, btndelete, btnupdate, btnquery;
    // khai báo ListView
    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    SQLiteDatabase mydatabase;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtmasv = findViewById(R.id.editTextMaSv);
        edttensv = findViewById(R.id.editTextTenSv);
        edtmalopsv = findViewById(R.id.editTextMaLopSv);

        btninsert = findViewById(R.id.themsv);
        btndelete = findViewById(R.id.xoasv);
        btnupdate = findViewById(R.id.suasv);
        btnquery = findViewById(R.id.querysv);
        // Tạo ListView
        lv = findViewById(R.id.listViewsv);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,mylist);

        lv.setAdapter(myadapter);
        mydatabase = openOrCreateDatabase("qlsinhvien.db",MODE_PRIVATE,null);
        // Tạo Table để chứa dữ liệu

        try {
            String sql2 = "CREATE TABLE tblsinhvien(masv TEXT primary key,tensv TEXT," +
                    " malop TEXT NOT NULL CONSTRAINT malop REFERENCES tbllop(malop) ON DELETE CASCADE)";
            mydatabase.execSQL(sql2);
        } catch (Exception e)     {
            Log.e("Error","Table Sinh viên đã tồn tại");
        }
        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String masv = edtmasv.getText().toString();
                String tensv = edttensv.getText().toString();
                String malopsv = edtmalopsv.getText().toString();
                ContentValues myvalue = new ContentValues();
                myvalue.put("masv",masv);
                myvalue.put("tensv",tensv);
                myvalue.put("malop",malopsv);
                String msg = "";
                if (mydatabase.insert("tblsinhvien",null,myvalue) == -1) {
                    msg = "Fail to Insert Record!";
                } else {
                    msg ="Insert record Sucessfully";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String masv = edtmasv.getText().toString();
                int n = mydatabase.delete("tblsinhvien","masv = ?",new String[]{masv});
                String msg ="";
                if (n == 0)
                {
                    msg = "No record to Delete";
                } else {
                    msg = n +" record is deleted";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {


                String masv = edtmasv.getText().toString();
                String tensv = edttensv.getText().toString();
                String malopsv = edtmalopsv.getText().toString();
                ContentValues myvalue = new ContentValues();

                myvalue.put("tensv",tensv);
                myvalue.put("malop",malopsv);
                int n = mydatabase.update("tblsinhvien",myvalue,"masv = ?",new String[]{masv});
                String msg = "";
                if (n == 0)
                {
                    msg = "No record to Update";
                } else {
                    msg = n+ " record is updated";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        btnquery.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                mylist.clear();
                Cursor c = mydatabase.query("tblsinhvien",null,null,null,null,null,null);
                c.moveToNext();
                String data = "";
                while (c.isAfterLast() == false)
                {
                    data = c.getString(0)+" - "+c.getString(1)+" - "+c.getString(2);
                    c.moveToNext();
                    mylist.add(data);
                }
                c.close();
                myadapter.notifyDataSetChanged();
            }
        });
        Button gotoQll = (Button) findViewById(R.id.gotoqll);
        gotoQll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent  intent = new Intent(getApplicationContext(),MainActivity2.class);
                startActivity(intent);
            }

        });

    }
}
