package tumbller.speed_control

import android.Manifest
import android.bluetooth.*
import android.bluetooth.le.*
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat

private const val DEVICE_NAME = "ELEGOO BT16"
private const val SCAN_PERIOD = 30000L
private const val SWITCH_TO_SPEED_CONTROL = '+'.code.toByte()
private const val EXIT_SPEED_CONTROL: Byte = -128
const val COMMAND_DOWN: Byte = -127
const val COMMAND_UP: Byte = -126
private const val TAG = "tumbller-speed-control"


object Bluetooth {
    private val scanner = BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner
    private var scanning = false
    private var gatt: BluetoothGatt? = null
    private var gattCharacteristic: BluetoothGattCharacteristic? = null
    private var valueToSend: ByteArray? = null
    private var valueIsToSend = false
    private var readyToSend = false


    @JvmStatic fun start() {
        if (!scanning) {
            Log.d(TAG, "Start Scanning")
            Handler().postDelayed({ stopScanning() }, SCAN_PERIOD)

            val scanFilters = listOf(ScanFilter.Builder().build())
            val scanSettings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build()
            scanning = true;

            scanner.startScan(scanFilters, scanSettings, DeviceScanCallback)
        } else {
            Log.d(TAG, "Already scanning")
        }
    }

    @JvmStatic fun stop() {
        sendCommand(EXIT_SPEED_CONTROL)
    }

    @JvmStatic fun sendCommand(command: Byte) {
        sendControlValue(command, 0)
    }

    @JvmStatic fun sendControlValue(first: Byte, second: Byte) {
        valueToSend = byteArrayOf(first, second)
        valueIsToSend = true
        if (readyToSend)
            sendValue()
    }

    private fun stopScanning() {
        Log.d(TAG, "Stopping Scanning")
        scanner.stopScan(DeviceScanCallback)
        scanning = false
    }

    private object DeviceScanCallback : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if (scanning) {
                super.onScanResult(callbackType, result)
                result.device?.let { device ->
                    if (result.scanRecord?.deviceName == DEVICE_NAME) {
                        Log.d(TAG, "onScanResult: found $DEVICE_NAME")
                        stopScanning()
                        device.connectGatt(null, false, GattClientCallback)
                    }
                }
            }
        }
    }

    private object GattClientCallback : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            val isSuccess = status == BluetoothGatt.GATT_SUCCESS
            val isConnected = newState == BluetoothProfile.STATE_CONNECTED
            Log.d(TAG, "onConnectionStateChange: Client $gatt  success: $isSuccess connected: $isConnected")
            if (isSuccess && isConnected) {
                gatt.discoverServices()
            }
        }

        override fun onServicesDiscovered(discoveredGatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(discoveredGatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onServicesDiscovered: Have gatt $discoveredGatt")
                gatt = discoveredGatt
                gattCharacteristic = discoveredGatt.services[0].characteristics[1]
                valueToSend = byteArrayOf(SWITCH_TO_SPEED_CONTROL)
                sendValue()
            }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            if (valueIsToSend)
                sendValue()
            else
                readyToSend = true
        }
    }

    private fun sendValue() {
        valueIsToSend = false
        readyToSend = false
        gattCharacteristic?.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
        gattCharacteristic?.value = valueToSend
        val success = gatt?.writeCharacteristic(gattCharacteristic)
        Log.d(TAG, "sendValue: $success, ${gattCharacteristic?.value.contentToString()}")
    }
}