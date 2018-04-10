package com.example.wuke.flashnote;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by kumbaya on 2018/4/9.
 */


public class Locallogin {
    public static void save(String a, String p){
        try {
            File account = Environment.getDataDirectory();
            File newFile = new File(account, "/data/com.example.wuke.flashnote/account.txt");
            FileWriter fw = new FileWriter(newFile, true);
            /*if(check() == true){
                String[] x = new String[2];
                FileReader fr = new FileReader(newFile);
                BufferedReader br = new BufferedReader(fr);
                x[0] = br.readLine();
                if(x[0]!=null){
                    x[1] = br.readLine();
                    if(x[0] == a && x[1] == p){
                        fw.close();
                    }
                    else {
                        newFile.delete();
                        fw.write(a + "\n");
                        fw.write(p + "\n");
                        fw.close();
                    }
                }
            }
            else{
                fw.write(a + "\n");
                fw.write(p + "\n");

                fw.close();
            }*/
            fw.write(a + "\n");
            fw.write(p + "\n");
            //Log.d("a", "yes");
            fw.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String[] getaccount(){
        String[] x = new String[2];
        File account = Environment.getDataDirectory();
        File newFile = new File(account, "/data/com.example.wuke.flashnote/account.txt");
        try {
            FileReader fr = new FileReader(newFile);
            BufferedReader br = new BufferedReader(fr);
            x[0] = br.readLine();
            x[1] = br.readLine();
            return x;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public static boolean check(){
        File account = Environment.getDataDirectory();
        File newFile = new File(account, "/data/com.example.wuke.flashnote/account.txt");
        if(newFile.exists()){
            return true;
        }else {
            return false;
        }
    }

    public static void delete(){
        File account = Environment.getDataDirectory();
        File newFile = new File(account, "/data/com.example.wuke.flashnote/account.txt");
        if(check()==true){
            newFile.delete();
        }
        else {
            return;
        }
    }
}
