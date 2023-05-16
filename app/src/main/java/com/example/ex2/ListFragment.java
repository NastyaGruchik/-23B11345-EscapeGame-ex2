package com.example.ex2;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ex2.Interfaces.CallBack_List;
import com.example.ex2.Models.GameDB;
import com.example.ex2.Utilities.MSP;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;


public class ListFragment extends Fragment {
    private GameDB gameDB;
    private CallBack_List callBackList;
    private MaterialTextView[] records = new MaterialTextView[10];
    public void setCallBackList(CallBack_List callBackList) {
        this.callBackList = callBackList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list,container,false);
        findViews(view);
        initViews();
        String fromJSON = MSP.getInstance(view.getContext().getApplicationContext()).getStringSP("GAME_DB", "");
        gameDB = new Gson().fromJson(fromJSON, GameDB.class);
        if (gameDB == null)
            gameDB = new GameDB();
        for (int i = 0; i < records.length; i++) {
            if (i < gameDB.getRecords().size())
                records[i].setText(gameDB.getRecords().get(i).toString());
        }

        return view;
    }

    private void initViews() {
        for (int i = 0; i < records.length; i++) {
            final int finI = i;
            records[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBackList.rowSelected(finI);
                }
            });
        }
    }



    private void findViews(View view) {
        records[0] = view.findViewById(R.id.list_record1);
        records[1] = view.findViewById(R.id.list_record2);
        records[2] = view.findViewById(R.id.list_record3);
        records[3] = view.findViewById(R.id.list_record4);
        records[4] = view.findViewById(R.id.list_record5);
        records[5] = view.findViewById(R.id.list_record6);
        records[6] = view.findViewById(R.id.list_record7);
        records[7] = view.findViewById(R.id.list_record8);
        records[8] = view.findViewById(R.id.list_record9);
        records[9] = view.findViewById(R.id.list_record10);
    }

}