package com.example.aayushijain.lingua;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class Menuasl extends AppCompatActivity {

    byte[] data;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView mVoiceInputTv;
    private TextView buffer;
    private ImageButton mSpeakBtn;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket btSocket = null;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mDevice = null;
    private static final String TAG = "bluetooth1";
    OutputStream outStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView Gi=(TextView)findViewById(R.id.gi);
        Gi.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent numberIntent = new Intent(Menuasl.this, Menuisl.class);
                startActivity(numberIntent);
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        });
                super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuasl);

        mVoiceInputTv = (TextView) findViewById(R.id.voiceInputasl);
        mSpeakBtn = (ImageButton) findViewById(R.id.btnSpeak);
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Device does not support bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });

    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {}
        }
        if(device!=null)
            return  device.createRfcommSocketToServiceRecord(MY_UUID);
        else
            return null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mVoiceInputTv.setText(result.get(0));
                    byte[] data2 = mVoiceInputTv.getText().toString().getBytes();

                    if(outStream==null)
                    {
                        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                        if (pairedDevices.size() > 0) {
                            for (BluetoothDevice device : pairedDevices) {
                                if (device.getName().trim().equals("HC-05")) {
                                    mDevice = device;
                                    Log.v("Message :", "Device connected");
                                    break;
                                }
                            }
                        }

                        try {
                            if(mDevice!=null)
                                btSocket = createBluetoothSocket(mDevice);

                            // Discovery is resource intensive.  Make sure it isn't going on
                            // when you attempt to connect and pass your message.
                            mBluetoothAdapter.cancelDiscovery();

                            // Establish the connection.  This will block until it connects.
                            Log.d(TAG, "...Connecting...");
                            try {
                                if(btSocket!=null)
                                    btSocket.connect();
                                Log.d(TAG, "...Connection ok...");
                            } catch (IOException e) {
                                try {
                                    btSocket.close();
                                } catch (IOException e2) {
                                }
                            }

                            Log.d(TAG, "...Create Socket...");
                        } catch (IOException e) {
                        }
                        try {if(btSocket!=null)
                            outStream = btSocket.getOutputStream();
                        } catch (IOException e) {}
                    }
                    try
                    {
                        outStream.write(data2);
                        Log.d(TAG, "...Data send...");
                    } catch (IOException e) {}
                }
                break;
            }

        }
    }
}
