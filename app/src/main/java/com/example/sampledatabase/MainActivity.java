package com.example.sampledatabase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    String tableName;
    String databaseName;
    TextView status;
    boolean databaseCreated = false;
    boolean tableCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText databaseNameInput = (EditText) findViewById(R.id.databaseNameInput);
        final EditText tableNameInput = (EditText) findViewById(R.id.tableNameInput);
        final EditText dataInput1 = (EditText) findViewById(R.id.dataInput1);
        final EditText dataInput2 = (EditText) findViewById(R.id.dataInput2);
        final EditText dataInput3 = (EditText) findViewById(R.id.dataInput3);
        status = (TextView) findViewById(R.id.status);

        Button createDatabaseBtn = (Button) findViewById(R.id.createDatabaseBtn);
        createDatabaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseName = databaseNameInput.getText().toString();
                createDatabase(databaseName);
            }
        });

        Button createTableBtn = (Button) findViewById(R.id.createTableBtn);
        createTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableName = tableNameInput.getText().toString();
                createTable(tableName);
            }
        });

        Button addDataBtn = (Button) findViewById(R.id.addDataBtn);
        addDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = dataInput1.getText().toString().trim();
                String ageStr = dataInput2.getText().toString().trim();
                String phoneStr = dataInput3.getText().toString().trim();

                int age = -1;
                try {
                    age = Integer.parseInt(ageStr);
                } catch (Exception e) {
                }

                insertRecord(name, age, phoneStr);
            }
        });

        Button dataCheckBtn = (Button) findViewById(R.id.dataCheckBtn);
        dataCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData(tableName);
            }
        });
    }

    private void createDatabase(String name) {
        println("creating database [" + name + "]");
        db = openOrCreateDatabase(name, MODE_PRIVATE, null);
        databaseCreated = true;
    }

    private void createTable(String name) {
        println("creating table [" + name + "]");

        if (db != null) {
            db.execSQL("create table " + name + "("
                    + "_id integer PRIMARY KEY autoincrement, "
                    + " name text, "
                    + " age integer, "
                    + "phone text);");

            tableCreated = true;
        } else {
            println("데이터베이스를 먼저 오픈하세요");
        }

    }

    private void insertRecord(String name, int age, String phone) {
        println("inserting records");

        if (db != null) {
            String sql = "insert into customer(name, age, phone) values (?, ?, ?);";
            Object[] params = {name, age, phone};
            db.execSQL(sql, params);
            println("Data 추가됌");
        } else {
            println("데이터베이스를 먼저 오픈하세요");
        }
    }

    private void checkData(String tableName) {
        println("checkData 호출");

        if (db != null){
            String sql = "select name, age, phone from " + tableName;
            Cursor cursor = db.rawQuery(sql, null);
            println("추가된 데이터 개수 : " + cursor.getCount());
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                String name = cursor.getString(0);
                int age = cursor.getInt(1);
                String phone = cursor.getString(2);

                println("#" + i + " -> " + name + ", " + age + ", " + phone);
            }
        }
    }

    public void println(String data) {
        status.append(data + "\n");
    }
}
