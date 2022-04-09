package com.vzard.buxet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    Context context;
    ArrayList<ChatNoteInfo> chat;
    public NotesAdapter(Context context){
        this.context=context;
        this.chat=ApplicationClass.applicationNotes;
    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.chat_row_layout,parent,false);
        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        chat=ApplicationClass.applicationNotes;
        if (position>=ApplicationClass.applicationNotes.size())
            return;
        holder.title.setText(chat.get(position).getTitle());
        holder.desp.setText(chat.get(position).getDesp());
        holder.lastmsgtime.setText(chat.get(position).getLastMsgTime());
        String s=chat.get(position).getLastMsg();
        if(s.length()>35)
        {
            holder.lastmsg.setText(s.substring(0,35)+"...");
        }
        else
        {
            holder.lastmsg.setText(s);
        }
        holder.despvisiblity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chat.get(position).isDespVisibility())
                {
                    holder.desp.setVisibility(View.GONE);
                    holder.tvbar.setVisibility(View.VISIBLE);
                    chat.get(position).setDespVisibility(false);
                    holder.despvisiblity.setImageResource(R.drawable.closed_arrow);
                }
                else
                {
                    holder.desp.setVisibility(View.VISIBLE);
                    chat.get(position).setDespVisibility(true);
                    holder.despvisiblity.setImageResource(R.drawable.open_arrow);
                    holder.tvbar.setVisibility(View.GONE);
                }
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"Delete clicked",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder =new AlertDialog.Builder(context);
                builder.setTitle("Are you sure, you want to delete it?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chat.remove(holder.getAdapterPosition());
                        ApplicationClass.applicationNotes=chat;
                        notifyDataSetChanged();
                        if(ApplicationClass.applicationNotes.size()==0)
                        {
                            Notes.tvEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        });
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"Edit clicked",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.chat_info_dialog,null);
                EditText etTitle,etDesp,etMessage;
                Button btnCancel,btnDone;
                etTitle=view.findViewById(R.id.etChatTitle);
                etDesp = view.findViewById(R.id.etDesp);
                etMessage = view.findViewById(R.id.etMessage);
                etMessage.setVisibility(View.GONE);
                btnDone=view.findViewById(R.id.btnDone);
                btnCancel=view.findViewById(R.id.btnCancel);
                etTitle.setText(chat.get(holder.getAdapterPosition()).getTitle());
                etDesp.setText(chat.get(holder.getAdapterPosition()).getDesp());
                builder.setView(view);
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etTitle.getText().toString().trim().length()>0)
                        {
                            String title = etTitle.getText().toString().trim();
                            String desp = etDesp.getText().toString().trim();
                            if(etDesp.getText().toString().trim().length()>0)
                            {
                                desp = etDesp.getText().toString();
                            }
                            else
                            {
                                desp="";
                            }
                            ChatNoteInfo dum = new ChatNoteInfo(title,desp,chat.get(holder.getAdapterPosition()).getLastMsg(),chat.get(holder.getAdapterPosition()).getMessages(),
                                    chat.get(holder.getAdapterPosition()).getLastMsgTime(),chat.get(holder.getAdapterPosition()).getMsgTimes());
                            chat.set(holder.getAdapterPosition(),dum);
                            ApplicationClass.applicationNotes=chat;
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(context,"Title and Message cannot be empty",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        holder.chat_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatScreen.class);
                intent.putExtra("chat_position",holder.getAdapterPosition());
                intent.putExtra("chat",chat);
               //((Activity) context).startActivityForResult(intent,1);
                // override onActivityResult in mainActivity
                context.startActivity(intent);
                //Toast.makeText(context,"Item clicked from holder",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }


    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView title,desp,lastmsg,lastmsgtime,tvbar;
        ImageView despvisiblity,ivDelete,ivEdit;
        ConstraintLayout chat_item;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.tvChatTitle);
            desp=itemView.findViewById(R.id.tvChatDescription);
            lastmsg=itemView.findViewById(R.id.tvLastMsg);
            lastmsgtime=itemView.findViewById(R.id.tvLastMsgTime);
            despvisiblity=itemView.findViewById(R.id.ivShowChatDesp);
            ivDelete=itemView.findViewById(R.id.ivChatDelete);
            ivEdit=itemView.findViewById(R.id.ivChatEdit);
            chat_item = itemView.findViewById(R.id.chat_item_layout);
            tvbar=itemView.findViewById(R.id.tvBar);
        }
    }
}
