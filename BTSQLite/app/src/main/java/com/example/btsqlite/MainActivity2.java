package com.example.btsqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    EditText edtmalop, edttenlop, edtsiso;
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
        setContentView(R.layout.activity_main_2);
        edtmalop = findViewById(R.id.editTextMaLop);
        edttenlop = findViewById(R.id.editTextTenLop);
        edtsiso = findViewById(R.id.editTextSiSo);
        btninsert = findViewById(R.id.themlop);
        btndelete = findViewById(R.id.xoalop);
        btnupdate = findViewById(R.id.sualop);
        btnquery = findViewById(R.id.querylop);
        // Tạo ListView
        lv = findViewById(R.id.listViewlop);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,mylist);

        lv.setAdapter(myadapter);
        mydatabase = openOrCreateDatabase("qlsinhvien.db",MODE_PRIVATE,null);
        // Tạo Table để chứa dữ liệu
        try {
            String sql1 = "CREATE TABLE tbllop(malop TEXT primary key,tenlop TEXT, siso INTEGER)";
            mydatabase.execSQL(sql1);
        } catch (Exception e)     {
            Log.e("Error","Table lớp đã tồn tại");
        }

        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String malop = edtmalop.getText().toString();
                String tenlop = edttenlop.getText().toString();
                int siso = Integer.parseInt(edtsiso.getText().toString());
                ContentValues myvalue = new ContentValues();
                myvalue.put("malop",malop);
                myvalue.put("tenlop",tenlop);
                myvalue.put("siso",siso);
                String msg = "";
                if (mydatabase.insert("tbllop",null,myvalue) == -1) {
                    msg = "Fail to Insert Record!";
                } else {
                    msg ="Insert record Sucessfully";
                }
                Toast.makeText(MainActivity2.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String malop = edtmalop.getText().toString();
                int n = mydatabase.delete("tbllop","malop = ?",new String[]{malop});
                String msg ="";
                if (n == 0)
                {
                    msg = "No record to Delete";
                } else {
                    msg = n +" record is deleted";
                }
                Toast.makeText(MainActivity2.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                int siso = Integer.parseInt(edtsiso.getText().toString());
                String malop = edtmalop.getText().toString();
                ContentValues myvalue = new ContentValues();
                myvalue.put("siso",siso);
                int n = mydatabase.update("tbllop",myvalue,"malop = ?",new String[]{malop});
                String msg = "";
                if (n == 0)
                {
                    msg = "No record to Update";
                } else {
                    msg = n+ " record is updated";
                }
                Toast.makeText(MainActivity2.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        btnquery.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                mylist.clear();
                Cursor c = mydatabase.query("tbllop",null,null,null,null,null,null);
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

        Button gotoQlsv = (Button) findViewById(R.id.gotoqlsv);
        gotoQlsv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent  intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }

        });

    }
}