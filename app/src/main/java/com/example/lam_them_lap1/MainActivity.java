package com.example.lam_them_lap1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lam_them_lap1.adapter.congviecadapter;
import com.example.lam_them_lap1.dao.congviecDAO;
import com.example.lam_them_lap1.model.congviec;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edttile, edtcontent, edtdate, edttype;
    Button btnadd, btnCancel, btnSave;
    RecyclerView recyclerView;
    private ArrayList<congviec> list = new ArrayList<congviec>();
    congviecDAO cvDAO;
    congviec cv;
    congviecadapter adapter;
    Dialog dialog;

    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnadd = findViewById(R.id.btnadd);
        //
        recyclerView = findViewById(R.id.review);
        //
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        cvDAO = new congviecDAO(this);
        list = cvDAO.selectAll();
        adapter = new congviecadapter(this, list, cvDAO);
        recyclerView.setAdapter(adapter);
        //

        //xử lí khi chọn nút add
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogThem();
            }
        });

    }


    public void dialogThem() {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = ((Activity) MainActivity.this).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_them, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        //
        edttile = view.findViewById(R.id.edttile);
        edtcontent = view.findViewById(R.id.edtcontent);
        edtdate = view.findViewById(R.id.edtdate);
        edttype = view.findViewById(R.id.edttype);
        //
        btnCancel = view.findViewById(R.id.btnCancel);
        btnSave = view.findViewById(R.id.btnSave);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void save() {
        String title = edttile.getText().toString();
        String content = edtcontent.getText().toString();
        String day = edtdate.getText().toString();
        String type = edttype.getText().toString();
        congviec cv = new congviec(title, content, day, type);//tạo đối tượng
        if (cvDAO.add(cv)) {
            list.clear();//xóa toàn bộ dữ liệu trong list
            list.addAll(cvDAO.selectAll());//add lại toàn bộ dữ liệu từ bảng công việc và add vàolisst
            adapter.notifyDataSetChanged();//thay đổi dữ liệu trong adapter
            Toast.makeText(MainActivity.this, "Add thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Add thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
