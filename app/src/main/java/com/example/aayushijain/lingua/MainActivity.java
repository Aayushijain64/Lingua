

        package com.example.aayushijain.lingua;

        import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
        import android.bluetooth.BluetoothSocket;
        import android.content.Intent;
        import android.os.Build;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import java.io.IOException;
        import java.io.OutputStream;
        import java.lang.reflect.Method;
        import java.util.Set;
        import java.util.UUID;
        import android.content.ActivityNotFoundException;
        import android.speech.RecognizerIntent;
        import android.widget.ImageButton;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.Locale;

public class MainActivity extends AppCompatActivity {
        /* byte[] data;
         private static final int REQ_CODE_SPEECH_INPUT = 100;
         private TextView mVoiceInputTv;
         private ImageButton mSpeakBtn;
         private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
         private BluetoothSocket btSocket = null;
         BluetoothAdapter mBluetoothAdapter;
         BluetoothDevice mDevice = null;
         private static final String TAG = "bluetooth1";
         OutputStream outStream;

 */
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                TextView asl = (TextView) findViewById(R.id.asl);
                TextView isl = (TextView) findViewById(R.id.isl);

                asl.setOnClickListener(new View.OnClickListener() {
                        // The code in this method will be executed when the numbers View is clicked on.
                        @Override
                        public void onClick(View view) {
                                Intent numbersIntent = new Intent(MainActivity.this, Menuasl.class);
                                startActivity(numbersIntent);
                        }
                });

                isl.setOnClickListener(new View.OnClickListener() {
                        // The code in this method will be executed when the numbers View is clicked on.
                        @Override
                        public void onClick(View view) {
                                Intent familyIntent = new Intent(MainActivity.this, Menuisl.class);
                                startActivity(familyIntent);
                        }
                });

        }
}