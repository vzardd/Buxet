package com.vzard.buxet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatScreen extends AppCompatActivity {
    int pos;
    ArrayList<ChatNoteInfo> chat;
    RecyclerView rvChatScreen;
    EditText etType;
    ImageView ivSend;
    boolean flag=false,done=true;
    ChatScreenAdapter adapter;

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.NOTES_FILE,MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(ApplicationClass.applicationNotes);
        editor.putString("notes",json);
        //Toast.makeText(ChatScreen.this,json,Toast.LENGTH_SHORT).show();
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(flag && done) {
            ChatNoteInfo dum = ApplicationClass.applicationNotes.get(pos);
            ApplicationClass.applicationNotes.remove(pos);
            ApplicationClass.applicationNotes.add(0, dum);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(flag) {
            ChatNoteInfo dum = ApplicationClass.applicationNotes.get(pos);
            ApplicationClass.applicationNotes.remove(pos);
            ApplicationClass.applicationNotes.add(0, dum);
            done = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        etType=findViewById(R.id.etInputMessage);
        ivSend=findViewById(R.id.btnSendMessage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pos=getIntent().getIntExtra("chat_position",0);
        chat=new ArrayList<ChatNoteInfo>();//here
        chat=getIntent().getParcelableArrayListExtra("chat");
        getSupportActionBar().setTitle(chat.get(pos).getTitle());
        rvChatScreen=findViewById(R.id.chat_screen_recyclerview);
        rvChatScreen.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        //llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        rvChatScreen.setLayoutManager(llm);
        adapter=new ChatScreenAdapter(this,pos,chat);
        rvChatScreen.setAdapter(adapter);
        ivSend.setOnClickListener(new View.OnClickListener() { //look here.... It's buggy!
            @Override
            public void onClick(View v) {
                if(etType.getText().toString().trim().length()>0)
                {
                    flag=true;
                    String mesg =etType.getText().toString().trim();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String date = sdf.format(new Date());
                    ApplicationClass.applicationNotes.get(pos).setLastMsg(mesg);
                    ApplicationClass.applicationNotes.get(pos).setLastMsgTime(date);
                    ApplicationClass.applicationNotes.get(pos).getMessages().add(mesg);
                    ApplicationClass.applicationNotes.get(pos).getMsgTimes().add(date);
                    etType.setText("");
                    adapter.notifyDataSetChanged();
                    rvChatScreen.scrollToPosition(ApplicationClass.applicationNotes.get(pos).getMessages().size()-1);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_screen_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        else if(item.getItemId() == R.id.menu_clear)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure?");
            builder.setMessage("All your notes will be lost!");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ApplicationClass.applicationNotes.remove(pos);
                    dialog.dismiss();
                    ChatScreen.this.finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

}