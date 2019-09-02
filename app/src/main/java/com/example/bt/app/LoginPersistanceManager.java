package com.example.bt.app;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoginPersistanceManager {

    private static final String LOGINFILENAME = "g2gInfo.dat";

    public static void writeToFile(Context context, String data)
    {
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(LOGINFILENAME, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(Context context)
    {
        String accTok = "";

        try{
            FileInputStream fileInputStream = context.openFileInput(LOGINFILENAME);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            accTok = bufferedReader.readLine();
            fileInputStream.close();
            bufferedReader.close();
        }catch(FileNotFoundException e){

            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return accTok;
    }
}
