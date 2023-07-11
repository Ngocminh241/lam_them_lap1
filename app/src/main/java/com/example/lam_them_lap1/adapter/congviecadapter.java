package com.example.lam_them_lap1.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lam_them_lap1.MainActivity;
import com.example.lam_them_lap1.R;
import com.example.lam_them_lap1.dao.congviecDAO;
import com.example.lam_them_lap1.model.congviec;

import java.util.ArrayList;

public class congviecadapter extends RecyclerView.Adapter<congviecadapter.ViewHolder> {
    final private Context context;
    final private ArrayList<congviec> list;
    final private congviecDAO cvDAO;

    public congviecadapter(Context context, ArrayList<congviec> list, congviecDAO cvDAO) {
        this.context = context;
        this.list = list;
        this.cvDAO = cvDAO;
    }

    congviec cv;
    Dialog dialog;

    @NonNull
    @Override
    public congviecadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_item, parent, false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull congviecadapter.ViewHolder holder, int position) {
        holder.txttieude.setText(list.get(position).getTitle());
        holder.txtnoidung.setText(list.get(position).getContent());
        holder.txtngay.setText(String.valueOf(list.get(position).getDate()));
        holder.txttype.setText(String.valueOf(list.get(position).getType()));
        //
        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = list.get(holder.getAdapterPosition()).getId();
                boolean check = cvDAO.delete(id);
                if (check){
                    Toast.makeText(context, "Xoa thanh cong", Toast.LENGTH_SHORT).show();
                    //
                    list.clear();
                    list.addAll(cvDAO.selectAll());
                    notifyItemRemoved(holder.getAdapterPosition());
                }else {
                    Toast.makeText(context, "Xoa that bai", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //
        holder.btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.dialogSua();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txttieude, txtnoidung, txtngay, txttype;
        EditText edttile, edtcontent, edtdate, edttype;
        Button btnCancel, btnSave, btnupdate, btndelete;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
                txttieude = itemView.findViewById(R.id.txttieude);
                txtnoidung = itemView.findViewById(R.id.txtnoidung);
                txtngay = itemView.findViewById(R.id.txtngay);
                txttype = itemView.findViewById(R.id.txttype);
                btnupdate = itemView.findViewById(R.id.btnupdate);
                btndelete = itemView.findViewById(R.id.btndelete);
        }
        public void dialogSua() {
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
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
            //
            edttile.setText(list.get(getAdapterPosition()).getTitle());
            edtcontent.setText(list.get(getAdapterPosition()).getContent());
            edtdate.setText(list.get(getAdapterPosition()).getDate());
            edttype.setText(list.get(getAdapterPosition()).getType());

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
            congviec cv = list.get(getAdapterPosition());
            cv.setTitle(edttile.getText().toString());
            cv.setContent(edtcontent.getText().toString());
            cv.setDate(edtdate.getText().toString());
            cv.setType(edttype.getText().toString());
            //nếu cập nhật thành công trong bảng công việc thì sẽ load lại dữ liệu lên view
            if (cvDAO.update(cv)){
                list.clear();//xóa toàn bộ dữ liệu trong list
                list.addAll(cvDAO.selectAll());//add lại toàn bộ dữ liệu từ bảng công việc và add vàolisst
                notifyDataSetChanged();//thay đổi dữ liệu trong adapter
                Toast.makeText(context, "Update thành công", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "Update thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
