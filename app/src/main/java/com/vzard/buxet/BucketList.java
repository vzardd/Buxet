package com.vzard.buxet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

public class BucketList extends Fragment {
    boolean shortTermVisible=true,longTermVisible=true;
    ImageView ivShortVisible,ivLongVisible;
    TextView tvShortEmpty,tvLongEmpty;
    RecyclerView rvShort,rvLong;
    FloatingActionButton fab_add_goal;
    ShortTermAdapter shortTermAdapter;
    LongTermAdapter longTermAdapter;


    public BucketList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Short Term Recyclerview over
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_bucket_list, container, false);
        ivShortVisible = v.findViewById(R.id.ivShortVisible);
        ivLongVisible = v.findViewById(R.id.ivLongVisible);
        tvShortEmpty=v.findViewById(R.id.tvShortEmpty);
        fab_add_goal=v.findViewById(R.id.btnAddGoal);
        tvLongEmpty=v.findViewById(R.id.tvLongEmpty);
        rvShort=v.findViewById(R.id.rvShort);
        rvLong = v.findViewById(R.id.rvLong);
        if(ApplicationClass.applicationShortTerm.size()==0)
        {
            tvShortEmpty.setVisibility(View.VISIBLE);
            rvShort.setVisibility(View.GONE);
        }
        else {
            tvShortEmpty.setVisibility(View.GONE);
            rvShort.setVisibility(View.VISIBLE);
        }
        if(ApplicationClass.applicationLongTerm.size()==0)
        {
            tvLongEmpty.setVisibility(View.VISIBLE);
            rvLong.setVisibility(View.GONE);
        }
        else {
            tvLongEmpty.setVisibility(View.GONE);
            rvLong.setVisibility(View.VISIBLE);
        }
        rvShort.setHasFixedSize(true);
        rvShort.setLayoutManager(new LinearLayoutManager(getActivity()));
        shortTermAdapter = new ShortTermAdapter(getActivity());
        rvShort.setAdapter(shortTermAdapter);
        rvLong.setHasFixedSize(true);
        rvLong.setLayoutManager(new LinearLayoutManager(getActivity()));
        longTermAdapter = new LongTermAdapter(getActivity());
        rvLong.setAdapter(longTermAdapter);
        ivShortVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shortTermVisible)
                {
                    shortTermVisible=false;
                    ivShortVisible.setImageResource(R.drawable.closed_arrow);
                    rvShort.setVisibility(View.GONE);
                    tvShortEmpty.setVisibility(View.GONE);
                }
                else {
                    if(ApplicationClass.applicationShortTerm.size()==0)
                    {
                        tvShortEmpty.setVisibility(View.VISIBLE);
                        rvShort.setVisibility(View.GONE);
                    }
                    else {
                        tvShortEmpty.setVisibility(View.GONE);
                        rvShort.setVisibility(View.VISIBLE);
                    }
                    shortTermVisible=true;
                    ivShortVisible.setImageResource(R.drawable.open_arrow);
                }
            }
        });
        ivLongVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (longTermVisible)
                {
                    longTermVisible=false;
                    ivLongVisible.setImageResource(R.drawable.closed_arrow);
                    rvLong.setVisibility(View.GONE);
                    tvLongEmpty.setVisibility(View.GONE);
                }
                else {
                    if(ApplicationClass.applicationLongTerm.size()==0)
                    {
                        tvLongEmpty.setVisibility(View.VISIBLE);
                        rvLong.setVisibility(View.GONE);
                    }
                    else {
                        tvLongEmpty.setVisibility(View.GONE);
                        rvLong.setVisibility(View.VISIBLE);
                    }
                    longTermVisible=true;
                    ivLongVisible.setImageResource(R.drawable.open_arrow);
                }
            }
        });
        fab_add_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.todo_info_dialog,null);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                EditText etGoal,etGoalDesp;
                RadioButton rbShortTerm,rbLongTerm;
                Button btnCancel,btnSet;
                etGoal=dialogView.findViewById(R.id.etTask);
                etGoalDesp =dialogView.findViewById(R.id.etGoalDesp);
                rbShortTerm = dialogView.findViewById(R.id.rbShortTerm);
                rbLongTerm = dialogView.findViewById(R.id.rbLongTerm);
                btnCancel = dialogView.findViewById(R.id.btnGoalCancel);
                btnSet = dialogView.findViewById(R.id.btnGoalSet);
                dialog.setCancelable(false);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etGoal.getText().toString().trim().isEmpty() || etGoalDesp.getText().toString().trim().isEmpty())
                        {
                            Toast.makeText(getActivity(),"Please fill all the fields!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if(rbShortTerm.isChecked())
                            {
                                //Toast.makeText(getActivity(),"Short Term",Toast.LENGTH_SHORT).show();
                                int pos;
                                boolean flag=false;
                                for(pos=0;pos<ApplicationClass.applicationShortTerm.size();++pos)
                                {
                                    if(ApplicationClass.applicationShortTerm.get(pos).isAccomplsished())
                                    {
                                        flag=true;
                                        ApplicationClass.applicationShortTerm.add(pos,new TodoInfo(etGoal.getText().toString().trim(),etGoalDesp.getText().toString().trim()));
                                        break;
                                    }
                                }
                                if(!flag)
                                {
                                    ApplicationClass.applicationShortTerm.add(new TodoInfo(etGoal.getText().toString().trim(),etGoalDesp.getText().toString().trim()));
                                }
                                shortTermAdapter.notifyDataSetChanged();
                                if(shortTermVisible)
                                {
                                    tvShortEmpty.setVisibility(View.GONE);
                                    rvShort.setVisibility(View.VISIBLE);
                                }
                                dialog.dismiss();
                            }
                            else if(rbLongTerm.isChecked())
                            {
                                //Toast.makeText(getActivity(),"Long Term",Toast.LENGTH_SHORT).show();
                                int pos;
                                boolean flag=false;
                                for(pos=0;pos<ApplicationClass.applicationLongTerm.size();++pos)
                                {
                                    if(ApplicationClass.applicationLongTerm.get(pos).isAccomplsished())
                                    {
                                        flag=true;
                                        ApplicationClass.applicationLongTerm.add(pos,new TodoInfo(etGoal.getText().toString().trim(),etGoalDesp.getText().toString().trim()));
                                        break;
                                    }
                                }
                                if(!flag)
                                {
                                    ApplicationClass.applicationLongTerm.add(new TodoInfo(etGoal.getText().toString().trim(),etGoalDesp.getText().toString().trim()));
                                }
                                longTermAdapter.notifyDataSetChanged();
                                if(longTermVisible)
                                {
                                    tvLongEmpty.setVisibility(View.GONE);
                                    rvLong.setVisibility(View.VISIBLE);
                                }
                                dialog.dismiss();
                            }
                            else {
                                Toast.makeText(getActivity(),"Please fill all the fields!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                dialog.show();
            }
        });
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        Gson gson = new Gson();
        String jsonShort = gson.toJson(ApplicationClass.applicationShortTerm);
        String jsonLong = gson.toJson(ApplicationClass.applicationLongTerm);
        SharedPreferences.Editor editor = getContext().getSharedPreferences(MainActivity.TO_DO_FILE, Context.MODE_PRIVATE).edit();
        editor.putString("shortTerm",jsonShort);
        editor.putString("longTerm",jsonLong);
        editor.apply();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.auto_delete,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.mnAutoDelete)
        {
            shortTermAdapter.notifyDataSetChanged();
            longTermAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(),"Refreshed",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}