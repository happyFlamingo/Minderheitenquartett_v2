package com.example.minderheitenquartett;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;



public class BluetoothConnectionService {

    String TAG = "BluetoothConnectionService" ;
    private static final String appName = "Security Minderheiten Quartett";
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;
    private AcceptThread mInsecureAcceptThreat;
    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;
    private ConnectedThread mConnectedThread;

    public BluetoothConnectionService(Context context){
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // ruft die erste Methode aus dem Konstruktor aus auf
        start();
    }

    // innere Klasse: wartet auf mögliche Connections
    // verhält sich wie ein server-side client
    // wird ausgeführt, bis eine Verbindung akzeptiert oder abgebrochen wurde
    private class AcceptThread extends Thread {
        // local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            Log.d(TAG, "Interne Klasse 'AcceptThread' wurde aufgerufen");
            BluetoothServerSocket tmp = null;

            // neuer listening server socket
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(appName, MY_UUID_INSECURE);
                Log.d(TAG, "AcceptThread Klasse: Server wird aufgesetzt, mit UUID: " + MY_UUID_INSECURE);
            } catch (IOException e) {
                Log.e(TAG, "Exception in der AcceptThread Klasse: " + e.getMessage());
            }
            mmServerSocket = tmp;
        }

        public void run(){
            Log.d(TAG, "AcceptTread running...");

            // nicht das gleiche wie Server Socket
            BluetoothSocket socket = null;

            try {
                Log.d(TAG, "Run Methode: Server Socket startet...");
                socket = mmServerSocket.accept();
                Log.d(TAG, "Run Methode: Server Socket hat Verbindung akzeptiert");
            }
            catch (IOException e) {
                Log.e(TAG, "Exception in run() Methode, Klasse AcceptThread: " + e.getMessage());
            }

            // ausführlich in Video 3/4 Sending/Receiving Data
            if(socket != null){
                // Aufruf der connected Methode
                connected(socket, mmDevice);
            }
            Log.i(TAG, "Ende der AcceptTread run()");
        }

        // cancel Methode für die interne Klasse AcceptThread
        public void cancel() {
            Log.d(TAG, "cancel() Methode in AcceptThread: Thread wird beendet");

            try{
                mmServerSocket.close();
            }
            catch (IOException e) {
                Log.e(TAG, "Beenden des AcceptThread ServerSocket fehlgeschlagen: " + e.getMessage());
            }
        }

    }

    // interne Klasse: Thread läuft, solange eine Verbindung nach außen hergestellt wird
    // Die Verbindung kann erfolgreich sein oder abbrechen
    private class ConnectThread extends Thread {
      private BluetoothSocket mmSocket;

       public ConnectThread(BluetoothDevice device, UUID uuid){
           Log.d(TAG, "Interne Klasse 'ConnectThread' wurde aufgerufen");
           mmDevice = device;
           deviceUUID = uuid;
       }

       public void run(){
          BluetoothSocket tmp = null;
          Log.i(TAG, "mmConnected Thread wird ausgeführt");

          // Bluetooth Socket für die Verbindung mit dem gegebenen Bluetooth Device
           try{
               Log.d(TAG, "ConnectThread Klasse - erstelle InsecureRcommSocket mit UUID: " + MY_UUID_INSECURE);
               tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
           }
           catch (IOException e) {
               Log.e(TAG, "ConnectThread Klasse - InsecureRcommSocket konnte nicht erstelt werden: " + e.getMessage());
           }
           mmSocket = tmp;

           // sonst wird die Verbindung stark verlangsamt
           mBluetoothAdapter.cancelDiscovery();

           // der Call wird nur erfolgreich zurückgegeben, wenn erfolgreich eine Verbindung hergestellt wurde
           try {
               mmSocket.connect();
               Log.d(TAG, "ConnectThread erfolgreich");

           } catch (IOException e) {
               // zum Schließen des Sockets

               try {
                   mmSocket.close();
                   Log.d(TAG, "Socket wurde in interner Klasse 'ConnectThread' geschlossen");
               }
               catch (IOException e2) {
                   Log.e(TAG, "Schließen des Sockets in 'ConnectThread' nicht möglich " + e2.getMessage());
               }

               Log.d(TAG, "Konnte sich nicht mit UUID verbinden: " + MY_UUID_INSECURE);
           }

           // Aufruf der connected Methode
            connected(mmSocket, mmDevice);
       }

        // cancel Methode für die interne Klasse ConnectThread
        public void cancel() {

            try{
                Log.d(TAG, "cancel() Methode in ConnectThread: Client Socket wird beendet");
                mmSocket.close();
            }
            catch (IOException e) {
                Log.e(TAG, "Beenden des mmSocket in ConnectThread fehlgeschlagen: " + e.getMessage());
            }
        }
    }

    // hier wird der Bluetooth Service gestartet
    public synchronized void start() {
        Log.d(TAG, "Synchronized void start() wird aufgerufen");

        if(mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if(mInsecureAcceptThreat == null){
            mInsecureAcceptThreat = new AcceptThread();
            mInsecureAcceptThreat.start();
        }
    }

    public void startClient(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "Methode startClient() aufgerufen");

        // setzt Dialog auf
        mProgressDialog = ProgressDialog.show(mContext, "Bluetooth Verbindung wird hergestellt ", "Bitte warten...", true);

        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    // Input und Output Stream:
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket){
            Log.d(TAG, "interne Klasse ConnectedThread wurde aufgerufen");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // dismiss, wenn Verbindung hergestellt ist
            try {
                mProgressDialog.dismiss();
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }

            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024]; // buffer store für den Stream
            int bytes; // für die bytes, die read() zurückgibt

            while(true){
                try{
                    bytes = mmInStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "Input Stream: " + incomingMessage);


                    // Sending Data from a Thread to an Activity
                    Intent incomingMessageIntent = new Intent("incomingMessage");
                    incomingMessageIntent.putExtra("theMessage", incomingMessage);
                    // LocalBroadcastManager.getInstance(mContext).sendBroadcast(incomingMessageIntent);

                    // EventBus bus = EventBus.getDefault();



                }
                catch (IOException e){
                    Log.e(TAG, "Fehler beim lesen des Input Streams");
                    break;
                }
            }
        }

        // sendet Daten zum externen Device -> muss von der MainActivity aus (oder von wo anders) aufgerufen werden
        public void write(byte[] bytes){
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "sendet Daten zum externen Gerät: " + text);

            try {
                mmOutStream.write(bytes);
            }
            catch (IOException e){
                Log.e(TAG, "Fehler beim senden der Daten an externes Gerät: " + e.getMessage());
            }
        }

        // schließt die Verbindung -> muss von der MainActivity aus (oder von wo anders) aufgerufen werden
        public void cancel(){
            try {
                mmSocket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice){
        Log.d(TAG, "Aufruf der connected() Methode in der Hauptklasse BluetoothConnectionService");

        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    // weil die andere write() Methode nicht von anderen Klasse aus verfügbar ist
    public void write(byte[] out){
        // temporäres Objekt
        ConnectedThread r;

        Log.d(TAG, "Aufruf der write() Methode in der Hauptklasse BluetoothConnectionService");
        mConnectedThread.write(out);
    }


}
