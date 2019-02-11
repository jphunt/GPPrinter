package com.example.gpprinter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Button buttonPrint;
    private Button mButtonScan;
    private BluetoothAdapter mBluetoothAdapter;

    private BroadcastReceiver mFindBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n"
                            + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d("finish", "onReceive: finish discovery" + mNewDevicesArrayAdapter.size());
            }
        }
    };
    private ArrayList<String> mNewDevicesArrayAdapter = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mFindBluetoothReceiver, intentFilter);

        intentFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mFindBluetoothReceiver, intentFilter);

        initBluetoothAdapter();
        initTestButton();
        initButtonScan();
    }

    private void initBluetoothAdapter() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Log.d("bluetooth error", "initBluetoothAdapter: not open blue tooth");
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Log.d("bt is not enabled", "initBluetoothAdapter: blue tooth error");
            } else {
                getDeviceList();
            }
        }
    }

    private void getDeviceList() {
        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
        Log.d("bondedDevices", "getDeviceList: "+bondedDevices);

    }

    private void initButtonScan() {


        mButtonScan = (Button) findViewById(R.id.button_scan);
        mButtonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothAdapter.startDiscovery();

            }
        });
    }

    private void initTestButton() {
        buttonPrint = (Button) findViewById(R.id.button_test);

        buttonPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("app", "onClick: OK");
            }
        });
    }
}
