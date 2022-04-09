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

import java.util.ArrayList;

public class ShortTermAdapter extends RecyclerView.Adapter<ShortTermAdapter.ShortTermViewHolder> {
    Context context;
    public ShortTermAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ShortTermAdapter.ShortTermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.todo_row_layout,parent,false);
        return new ShortTermViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortTermAdapter.ShortTermViewHolder holder, int position) {
        String task = ApplicationClass.applicationShortTerm.get(position).getTaskTitle();
        if(task.length()<15) {
            holder.etTaskTitle.setText(task);
        }
        else {
            holder.etTaskTitle.setText(task.substring(0,14)+"...");
        }
        if(ApplicationClass.applicationShortTerm.get(position).isAccomplsished())
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
                            ApplicationClass.applicationShortTerm.get(position).setAccomplsished(false);
                            TodoInfo temp = ApplicationClass.applicationShortTerm.get(position);
                            ApplicationClass.applicationShortTerm.remove(position);
                            ApplicationClass.applicationShortTerm.add(0,temp);
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
                        ApplicationClass.applicationShortTerm.get(position).setAccomplsished(true);
                        TodoInfo temp = ApplicationClass.applicationShortTerm.get(position);
                        ApplicationClass.applicationShortTerm.remove(position);
                        ApplicationClass.applicationShortTerm.add(temp);
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
                        if (ApplicationClass.applicationShortTerm.size()==1)
                        {
                            ApplicationClass.applicationShortTerm.remove(position);
                            notifyDataSetChanged();
                            TextView tvShortEmpty = ((Activity)context).findViewById(R.id.tvShortEmpty);
                            tvShortEmpty.setVisibility(View.VISIBLE);
                        }
                        else {
                            ApplicationClass.applicationShortTerm.remove(position);
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
                etTask.setText(ApplicationClass.applicationShortTerm.get(position).getTaskTitle());
                etDesp.setText(ApplicationClass.applicationShortTerm.get(position).getTaskDesp());
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
                            ApplicationClass.applicationShortTerm.get(position).setTaskTitle(etTask.getText().toString().trim());
                            ApplicationClass.applicationShortTerm.get(position).setTaskDesp(etDesp.getText().toString().trim());
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
                builder.setTitle(ApplicationClass.applicationShortTerm.get(position).getTaskTitle());
                builder.setMessage(ApplicationClass.applicationShortTerm.get(position).getTaskDesp());
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
        return ApplicationClass.applicationShortTerm.size();
    }
    class ShortTermViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        ImageView ivChecked,ivEdit,ivDelete;
        TextView etTaskTitle;
        RelativeLayout rlRow;

        public ShortTermViewHolder(@NonNull View itemView) {
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
