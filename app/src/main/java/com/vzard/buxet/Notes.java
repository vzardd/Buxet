package com.vzard.buxet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Notes extends Fragment{
    AlertDialog.Builder myalert;
    NotesAdapter adapter;
    public static TextView tvEmpty;
    public Notes() {
    }
    FloatingActionButton create_chat;
    RecyclerView recyclerView;
    ArrayList<ChatNoteInfo> notes;
    EditText etTitle,etDesp,etMessage;
    Button btnCancel,btnDone;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_notes, container, false);
        create_chat= v.findViewById(R.id.createChat);
        setHasOptionsMenu(true);
        recyclerView = v.findViewById(R.id.chatRecyclerView);
        notes=new ArrayList<ChatNoteInfo>();
        notes=ApplicationClass.applicationNotes;
        tvEmpty=v.findViewById(R.id.tvEmpty);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NotesAdapter(getContext());
        recyclerView.setAdapter(adapter);
        create_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateChatDialog();
                adapter.notifyDataSetChanged();
            }
        });

        return v;
    }

    private void showCreateChatDialog() { //buggyyyy here.... Check here..
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.chat_info_dialog,null);
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    etTitle=v.findViewById(R.id.etChatTitle);
    etDesp = v.findViewById(R.id.etDesp);
    etMessage = v.findViewById(R.id.etMessage);
    btnDone=v.findViewById(R.id.btnDone);
    btnCancel=v.findViewById(R.id.btnCancel);
    builder.setView(v);
    AlertDialog alertDialog = builder.create();
    btnDone.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(etTitle.getText().toString().trim().length()>0 && etMessage.getText().toString().trim().length()>0)
            {
                String title = etTitle.getText().toString().trim();
                String message = etMessage.getText().toString().trim();
                String desp;
                if(etDesp.getText().toString().trim().length()>0)
                {
                    desp = etDesp.getText().toString();
                }
                else
                {
                    desp="";
                }
                ArrayList<String> msgs = new ArrayList<String>();
                ArrayList<String> msgdates = new ArrayList<String>();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String msgdate = sdf.format(new Date());
                msgdates.add(msgdate);
                msgs.add(message);
                notes.add(0,new ChatNoteInfo(title,desp,message,msgs,msgdate,msgdates));
                adapter.notifyDataSetChanged();
                ApplicationClass.applicationNotes=notes;
                tvEmpty.setVisibility(View.GONE);
                alertDialog.dismiss();
            }
            else
            {
                Toast.makeText(Notes.this.getActivity(),"Title and Message cannot be empty",Toast.LENGTH_SHORT).show();
            }
        }
    });
    btnCancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alertDialog.dismiss();
        }
    });
    alertDialog.setCancelable(false);
    alertDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        //notes=ApplicationClass.applicationNotes;
        if(ApplicationClass.applicationNotes.size()==0)
        {
            tvEmpty.setVisibility(View.VISIBLE);
        }
        else
        {
            tvEmpty.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.notes_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.mnShare:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"Check out this amazing app that helps you to take Notes, keep track of your Bucket List, and set Remainders." +
                        "\nDownload the app from here, https://play.google.com/store/apps/details?id=com.cs.buxet");
                startActivity(intent);
            break;
            case R.id.mnRateUs:
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.cs.buxet"));
                startActivity(intent1);
            break;
        }
        return true;
    }
}