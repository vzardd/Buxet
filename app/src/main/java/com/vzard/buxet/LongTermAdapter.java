package com.vzard.buxet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LongTermAdapter extends RecyclerView.Adapter<LongTermAdapter.LongTermViewHolder> {
    Context context;
    public LongTermAdapter(Context context)
    {
        this.context=context;
    }
    @NonNull
    @Override
    public LongTermAdapter.LongTermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.todo_row_layout,parent,false);
        return new LongTermAdapter.LongTermViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LongTermAdapter.LongTermViewHolder holder, int position) {
        String task = ApplicationClass.applicationLongTerm.get(position).getTaskTitle();
        if(task.length()<15) {
            holder.etTaskTitle.setText(task);
        }
        else {
            holder.etTaskTitle.setText(task.substring(0,14)+"...");
        }
        if(ApplicationClass.applicationLongTerm.get(position).isAccomplsished())
        {
            holder.checkBox.setVisibility(View.GONE);
            holder.ivChecked.setVisibility(View.VISIBLE);
            holder.ivChecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builer = new AlertDialog.Builder(context);
                    builer.setMessage("Are you sure, you want to mark it as Incomplete?");
                    builer.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builer.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ApplicationClass.applicationLongTerm.get(position).setAccomplsished(false);
                            TodoInfo temp = ApplicationClass.applicationLongTerm.get(position);
                            ApplicationClass.applicationLongTerm.remove(position);
                            ApplicationClass.applicationLongTerm.add(0,temp);
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    builer.show();
                }
            });
        }
        else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.ivChecked.setVisibility(View.GONE);
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        ApplicationClass.applicationLongTerm.get(position).setAccomplsished(true);
                        TodoInfo temp = ApplicationClass.applicationLongTerm.get(position);
                        ApplicationClass.applicationLongTerm.remove(position);
                        ApplicationClass.applicationLongTerm.add(temp);
                        holder.checkBox.setChecked(false);
                        notifyDataSetChanged();
                    }
                }
            });
        }
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure, you want to delete it?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ApplicationClass.applicationLongTerm.size()==1)
                        {
                            ApplicationClass.applicationLongTerm.remove(position);
                            notifyDataSetChanged();
                            TextView tvLongEmpty = ((Activity)context).findViewById(R.id.tvLongEmpty);
                            tvLongEmpty.setVisibility(View.VISIBLE);
                        }
                        else {
                            ApplicationClass.applicationLongTerm.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                });
                builder.show();
            }
        });
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.todo_info_dialog,null);
                builder.setView(view);
                AlertDialog dialog = builder.create();
                EditText etTask = view.findViewById(R.id.etTask);
                EditText etDesp = view.findViewById(R.id.etGoalDesp);
                RadioGroup rg = view.findViewById(R.id.radioGroup);
                rg.setVisibility(View.GONE);
                etTask.setText(ApplicationClass.applicationLongTerm.get(position).getTaskTitle());
                etDesp.setText(ApplicationClass.applicationLongTerm.get(position).getTaskDesp());
                Button btnCancel = view.findViewById(R.id.btnGoalCancel);
                Button btnSet = view.findViewById(R.id.btnGoalSet);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                btnSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etTask.getText().toString().trim().isEmpty() || etDesp.getText().toString().trim().isEmpty())
                        {
                            Toast.makeText(context,"Please fill all the fields!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            ApplicationClass.applicationLongTerm.get(position).setTaskTitle(etTask.getText().toString().trim());
                            ApplicationClass.applicationLongTerm.get(position).setTaskDesp(etDesp.getText().toString().trim());
                            notifyDataSetChanged();
                        }
                        dialog.cancel();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            }
        });
        holder.rlRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(ApplicationClass.applicationLongTerm.get(position).getTaskTitle());
                builder.setMessage(ApplicationClass.applicationLongTerm.get(position).getTaskDesp());
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return ApplicationClass.applicationLongTerm.size();
    }
    class LongTermViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        ImageView ivChecked,ivEdit,ivDelete;
        TextView etTaskTitle;
        RelativeLayout rlRow;
        public LongTermViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            ivChecked =itemView.findViewById(R.id.ivChecked);
            ivDelete = itemView.findViewById(R.id.ivDelTodo);
            ivEdit = itemView.findViewById(R.id.ivEditTodo);
            etTaskTitle = itemView.findViewById(R.id.tvTodoTask);
            rlRow = itemView.findViewById(R.id.rlRow);
        }
    }
}
