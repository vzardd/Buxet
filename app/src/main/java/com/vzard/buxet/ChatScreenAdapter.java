package com.vzard.buxet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatScreenAdapter extends RecyclerView.Adapter<ChatScreenAdapter.ChatScreenViewHolder> {
    Context context;
    int pos;
    ArrayList<ChatNoteInfo> chat;
    public ChatScreenAdapter(Context context,int pos,ArrayList<ChatNoteInfo> chat)
    {
        this.context=context;
        this.pos=pos;
        this.chat=chat;
    }
    @NonNull
    @Override
    public ChatScreenAdapter.ChatScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.message_row_layout,null);
        return new ChatScreenViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatScreenAdapter.ChatScreenViewHolder holder, int position) {
        chat=ApplicationClass.applicationNotes;
        holder.msg.setText(chat.get(pos).getMessages().get(position));
        holder.msg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clip.setPrimaryClip(ClipData.newPlainText("msg",holder.msg.getText()));
                Toast.makeText(context,"Note Copied!",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        holder.msgDate.setText(chat.get(pos).getMsgTimes().get(position));
        if(position>0)
        {
            String prevDate = chat.get(pos).getMsgTimes().get(position-1);
            String curDate = chat.get(pos).getMsgTimes().get(position);
            if(prevDate.equals(curDate))
            {
                holder.llDateBox.setVisibility(View.GONE);
            }
        }
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this message?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(ApplicationClass.applicationNotes.get(pos).getMsgTimes().size()==1)// look here
                        {
                            ApplicationClass.applicationNotes.remove(pos);
                            ((Activity)context).finish();
                        }
                        else if(position==ApplicationClass.applicationNotes.get(pos).getMessages().size()-1)
                        {
                            ApplicationClass.applicationNotes.get(pos).setLastMsg(ApplicationClass.applicationNotes.get(pos).getMessages().get(position-1));
                            ApplicationClass.applicationNotes.get(pos).setLastMsgTime(ApplicationClass.applicationNotes.get(pos).getMsgTimes().get(position-1));
                            ApplicationClass.applicationNotes.get(pos).getMessages().remove(position);
                            ApplicationClass.applicationNotes.get(pos).getMsgTimes().remove(position);
                            chat=ApplicationClass.applicationNotes;
                            notifyDataSetChanged();
                        }
                        else{
                            ApplicationClass.applicationNotes.get(pos).getMessages().remove(position);
                            ApplicationClass.applicationNotes.get(pos).getMsgTimes().remove(position);
                            chat=ApplicationClass.applicationNotes;
                            notifyDataSetChanged();
                        }
                        Toast.makeText(context,"Message deleted successfully!",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View v2 = LayoutInflater.from(context).inflate(R.layout.chat_info_dialog,null);
                builder.setView(v2);
                AlertDialog dialog = builder.create();
                Button btnDone, btnCancel;
                EditText etTitle,etDesp,etMsg;
                btnDone = v2.findViewById(R.id.btnDone);
                btnCancel = v2.findViewById(R.id.btnCancel);
                etTitle=v2.findViewById(R.id.etChatTitle);
                etDesp=v2.findViewById(R.id.etDesp);
                etMsg=v2.findViewById(R.id.etMessage);
                etTitle.setVisibility(View.GONE);
                etDesp.setVisibility(View.GONE);
                etMsg.setText(ApplicationClass.applicationNotes.get(pos).getMessages().get(position));
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etMsg.getText().toString().trim().isEmpty())
                        {
                            Toast.makeText(context,"Message cannot be empty!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            holder.msg.setText(etMsg.getText().toString().trim());
                            ApplicationClass.applicationNotes.get(pos).getMessages().set(position,etMsg.getText().toString().trim());
                            if(position==ApplicationClass.applicationNotes.get(pos).getMessages().size()-1){
                                ApplicationClass.applicationNotes.get(pos).setLastMsg(etMsg.getText().toString().trim());
                            }
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
                //Toast.makeText(context,"Edited",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chat.get(pos).getMessages().size();
    }
    public class ChatScreenViewHolder extends RecyclerView.ViewHolder{
        TextView msgDate,msg;
        LinearLayout llDateBox;
        ImageView btnDel,btnEdit;

        public ChatScreenViewHolder(@NonNull View itemView) {
            super(itemView);
            msgDate=itemView.findViewById(R.id.tvMessageDate);
            msg=itemView.findViewById(R.id.tvChatScreenMessage);
            btnDel=itemView.findViewById(R.id.ivMessageDelete);
            btnEdit=itemView.findViewById(R.id.ivMessageEdit);
            llDateBox=itemView.findViewById(R.id.llMessageDate);
        }
    }

}
