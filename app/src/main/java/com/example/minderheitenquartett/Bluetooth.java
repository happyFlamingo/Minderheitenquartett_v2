package com.example.minderheitenquartett;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;


public class Bluetooth extends AppCompatActivity implements AdapterView.OnItemClickListener {

    // Deklarationsteil
    private static final String TAG = "BluetoothActivity" ;
    Button button;
    Button discoverable;
    Button btnFindUnpairedDevices;
    BluetoothAdapter bluetoothAdapter;
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter; // siehe Klasse "DeviceListAdapter"
    ListView lvNewDevices;
    EditText editText;
    Button btnSend;
    Button btnStartConnection;
    BluetoothConnectionService mBluetoothConnection;
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    BluetoothDevice mBTDevice;
    TextView incomingMessages;
    StringBuilder messages; // für die Nachrichten, die hin und her gesendet werden

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        // Verknüpfung von Design aus activity_bluetooth.xml & Deklarationsteil
        button = findViewById(R.id.button_bluetooth);
        discoverable = findViewById(R.id.button_discoverable);
        btnFindUnpairedDevices = findViewById(R.id.btnFindUnpairedDevices);
        lvNewDevices = findViewById(R.id.lvNewDevices);
        editText = findViewById(R.id.editText);
        btnSend = findViewById(R.id.btnSend);
        btnStartConnection = findViewById(R.id.btnStartConnection);
        incomingMessages = findViewById(R.id.incomingMessage);
        mBTDevices = new ArrayList<>();

        // dem deklarierten Adapter wird hier zunächst ein Default Wert zugewiesen
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Receiver fürs pairen
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(broadCastReceiver4, filter);

        lvNewDevices.setOnItemClickListener(Bluetooth.this);

        // muss noch ersetzt werden, da Klasse veraltet
         // LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("incomingMessage"));

        // wird aufgerufen, wenn der Knopf "Bluetooth ein" gedrückt wird
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: enabling/disabling bluetooth.");
                bluetoothAn();
            }
        });

        btnStartConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: startConnection() wird aufgerufen.");
                startConnection();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Nachricht wird gesendet.");
                // hier müssen die bytes übergeben werden / holt sich hier den Inhalt aus dem EditText Feld und gibt sie an BluetoothConnectionService
                byte[] bytes = editText.getText().toString().getBytes(Charset.defaultCharset());
                mBluetoothConnection.write(bytes);

                editText.setText(" ");

            }
        });

    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra("theMessage");

            messages.append(text + "\n");
            incomingMessages.setText(messages);
        }
    };

    // Deklaration & Anweisungen zum broadCastReceiver (gehört zum Knopf "Bluetooth ein")
    private final BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(bluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, bluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "BroadcastReceiver: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "BroadcastReceiver: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "BroadcastReceiver: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    // Deklaration & Anweisungen zum broadCastReceiver2 (gehört zum Knopf "Auffindbarkeit ein")
    private final BroadcastReceiver broadCastReceiver2 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ( action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED) ) {
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "broadCastReceiver2: Auffindbarkeit ein");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG,"broadCastReceiver2: Verbindung kann stattfinden");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG,"broadCastReceiver2: Verbindung nicht möglich");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG,"broadCastReceiver2: Verbindet...");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG,"broadCastReceiver2: Verbunden");
                        break;
                }
            }
        }
    };

    // Deklaration & Anweisungen zum broadCastReceiver3 (gehört zum Knopf "andere Geräte finden")
    private BroadcastReceiver broadCastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "Aktion gefunden");

            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d(TAG, "onReceive Methode:" + device.getName() + ":" + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                lvNewDevices.setAdapter((ListAdapter) mDeviceListAdapter);
            }
        }
    };


    private final BroadcastReceiver broadCastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // wenn schon miteinander verbunden
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "onReceive Pairing: BOND_BONDED" );
                    // assigning the device
                    mBTDevice = mDevice;
                }
                // Verbindung herstellen
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDING){
                    Log.d(TAG, "onReceive Pairing: BOND_BONDING" );
                }
                // Verbindung trennen
                if(mDevice.getBondState() == BluetoothDevice.BOND_NONE){
                    Log.d(TAG, "onReceive Pairing: BOND_NONE" );
                }

            } else {
                Log.d(TAG, "onReceive Pairing: FEHLER" );
            }
        }
    };

    // wird in der button.onClick aufgerufen
    public void bluetoothAn() {
        // Android Gerät hat keine Bluetooth Kapazität
        if(bluetoothAdapter == null){
            Log.d(TAG, "kein Bluetooth möglich");
        } else {
            // Android hat Bluetooth Kapazität
            if (!bluetoothAdapter.isEnabled())
            // falls Bluetooth nicht bereits aktiviert ist
            {
                Log.d(TAG, "Bluetooth wird aktiviert");
                Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(enableBTIntent);

                IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(broadCastReceiver, BTIntent);
            }

            // Wenn der Bluetooth bereits an ist, wird er bei, Klick deaktiviert
            else {
                Log.d(TAG, "Bluetooth wird deaktiviert");
                bluetoothAdapter.disable();

                IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(broadCastReceiver, BTIntent);
            }
        }
    }

    // wird ausgelöst, wenn man auf den Button "Auffindbarkeit ein" klickt
    public void auffindbarkeit(View v) {
        // funktioniert, gibt in der Logcat die Message aus
        Log.d(TAG,"Auffindbarkeit ist an");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        // hier wird die obige Deklaration des "private final broadCastReceiver2" übergeben
        IntentFilter intentFilter = new IntentFilter(bluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(broadCastReceiver2, intentFilter);
    }


    // Startet die Suche nach anderen Geräten mit Bluetooth, wenn auf "andere Geräte finden" geklickt wird
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void btnDiscover(View v) {
        Log.d(TAG,"Suche nach anderen Geräten");

        // wenn das Gerät bereits sucht, wird die Suche neu gestartet
        if(bluetoothAdapter.isDiscovering()){

            bluetoothAdapter.cancelDiscovery();
            Log.d(TAG,"Suche gestoppt");

            // Aufruf der Methode "checkBTPermissions()" (prüft Bluetooth Erlaubnis anderer Geräte)
            checkBTPermissions();

            bluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(broadCastReceiver3, discoverDevicesIntent);
        }
        // sonst wird direkt nach anderen Geräten gesucht
        else {
            // Aufruf der Methode "checkBTPermissions()" (prüft Bluetooth Erlaubnis anderer Geräte)
            checkBTPermissions();

            bluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(broadCastReceiver3, discoverDevicesIntent);
        }

    }

    // Android muss für alle Devices mit API23+ bluetooth die Permissions checken
    // ...nur die <permissions/> allein im manifest sind nicht ausreichend
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }
        } else {
            Log.d(TAG, "Braucht keinen Permission Check: SDK version < Lollipop");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // cancelDiscovery wird zuerst durchgeführt, weil der Memory sonst schnell überlastet ist
        bluetoothAdapter.cancelDiscovery();

        Log.d(TAG, "onItemClick Methode: anderes Bluetooth Gerät gefunden und angeklickt");

        String deviceName = mBTDevices.get(i).getName();
        String deviceAdresse = mBTDevices.get(i).getAddress();

        Log.d(TAG, "onItemClick Methode:  Name des Device = " + deviceName);
        Log.d(TAG, "onItemClick Methode:  Adresse des Device = " + deviceAdresse);

        // jetzt wird die Verbindung zwischen den Geräten hergestellt
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.d(TAG, "Verbindung wird aufgebaut zu: " + deviceName);
            mBTDevices.get(i).createBond();
            mBTDevice = mBTDevices.get(i);
            mBluetoothConnection = new BluetoothConnectionService(Bluetooth.this);
            // wartet in diesem Zustand, bis der btnStartConnection Button gedrückt wird
        }

    }

    @Override
    protected void onDestroy(){
        Log.d(TAG, "onDestroy Methode");
        super.onDestroy();
        unregisterReceiver(broadCastReceiver);
        unregisterReceiver(broadCastReceiver2);
        unregisterReceiver(broadCastReceiver3);
        unregisterReceiver(broadCastReceiver4);
    }

    // zugehörige Methode zur startClient Methode in "BluetoothConnectionService"
    // startet den möglichen Chat Service
    public void startBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startBTConnection() Methode: RFCOM Bluetooth Connection wird hergestellt");
        mBluetoothConnection.startClient(device, uuid);
    }

    // ohne vorheriges pairen wird diese Methode einen Absturz der App erzeugen
    public void startConnection(){
        Log.d(TAG, "startConnection() Methode aufgerufen");
        startBTConnection(mBTDevice, MY_UUID_INSECURE);
        // nach diesem Teil kann der Datenaustausch beginnen
    }
}