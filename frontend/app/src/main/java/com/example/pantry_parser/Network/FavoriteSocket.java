package com.example.pantry_parser.Network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.pantry_parser.Pages.Recipe_Page;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class FavoriteSocket {

    public static WebSocketClient mWebSocketClient;
    public static Context mContext;
    public static void connectFavoriteSocket(URI serverUri) {
        mWebSocketClient = new WebSocketClient(serverUri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
            }

            @Override
            public void onMessage(String msg) {
                Log.i("Websocket", "Message Received");
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        Toast.makeText(FavoriteSocket.mContext, msg, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onClose(int errorCode, String reason, boolean remote) {
                Log.i("Websocket", "Closed " + reason);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

    public static void changeContext(Context context) {
        mContext = context;
    }


}
