package com.pp.bot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Counter declaration
    private int count= 0;

    // On create function of activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button declaration
        Button generate_msg = findViewById(R.id.generate_message);
        Button stop_service = findViewById(R.id.stopService);

        // Button onclick listener methods
        generate_msg.setOnClickListener((View.OnClickListener) this);
        stop_service.setOnClickListener((View.OnClickListener) this);

        registerServiceStateChangeReceiver();
    }

    //Generate Message
    private void generateMsg(){
        Bundle data = new Bundle();
        data.putInt(ChatService.CMD, ChatService.CHAT_GENERATE_MSG);
        Intent intent = new Intent(this, ChatService.class);
        intent.putExtras(data);
        startService(intent);
    }

    //Stop Service
    private void stopService(){
        Bundle data = new Bundle();
        data.putInt(ChatService.CMD, ChatService.CHAT_STOP_SERVICE);
        Intent intent = new Intent(this, ChatService.class);
        intent.putExtras(data);
        startService(intent);
    }

    //Button onclick method
    @Override
    public void onClick(View view) {
        int button_id = view.getId();
        if (button_id == R.id.generate_message){
            generateMsg();
        }
        else{
            stopService();
        }

    }

    //Broadcast Receiver
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            TextView msg_output = (TextView) findViewById(R.id.msgtext);
            if (Constants.BROADCAST_GENERATE_MSG.equals(action)) {
                msg_output.findViewById(R.id.msgtext);
                // Updating counter
                count++;
                if(count == 1){
                    msg_output.setText("Hello Poojan \n");

                }else if(count == 2){
                    msg_output.append("How are you? \n");
                }
                else if(count == 3){
                    msg_output.append("Bye Poojan\n");
                }
                else{
                    msg_output.append("Sorry I am out of ideas\n");
                }

            } else if (Constants.BROADCAST_STOP_SERVICE.equals(action)) {
                //Code as last digits of student ID
                msg_output.setText("Chat Bot Stopped: 11");

                // Updating counter
                count = 0;
            }
        }
    };

    //Registering broadcast messages in service
    private void registerServiceStateChangeReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_GENERATE_MSG);
        intentFilter.addAction(Constants.BROADCAST_STOP_SERVICE);
        registerReceiver(broadcastReceiver, intentFilter);

    }
}