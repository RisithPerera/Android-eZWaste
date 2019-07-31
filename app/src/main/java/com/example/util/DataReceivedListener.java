package com.example.util;

import com.example.models.Dustbin;

import java.util.ArrayList;
import java.util.List;

public class DataReceivedListener {

    private ArrayList<Dustbin> dustBinList;
    private static DataReceivedListener dataReceivedListener;

    private DataReceivedListener(){
        dustBinList = new ArrayList<>();
    }

    public static DataReceivedListener getInstance(){
        if(dataReceivedListener==null){
            dataReceivedListener = new DataReceivedListener();
        }

        return dataReceivedListener;
    }
    public void onDataReceived(ArrayList<Dustbin> dustBinList){
        for(Dustbin dustbin: dustBinList){
            this.dustBinList.add(dustbin);
        }
    }

    public ArrayList<Dustbin> getDataReceived(){
        return dustBinList;
    }

}
