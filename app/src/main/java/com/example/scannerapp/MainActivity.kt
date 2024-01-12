package com.example.scannerapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.scannerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var PERMISSION_REQUEST_CODE = 1

    private lateinit var binding: ActivityMainBinding
    private var selectedScanningSDK = ScannerSDK.MLKIT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =DataBindingUtil.setContentView(this,R.layout.activity_main)

        StartApp()
    }

    private fun StartApp() {
        binding.cardMlKit.setOnClickListener {
            selectedScanningSDK = ScannerSDK.MLKIT
            startScanning()
        }
        binding.cardZxing.setOnClickListener {
            selectedScanningSDK = ScannerSDK.ZXING
            startScanning()
        }


    }

    private fun startScanning() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCameraWithScanner()
        } else { ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISSION_REQUEST_CODE){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCameraWithScanner()
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraWithScanner()
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivityForResult(intent, PERMISSION_REQUEST_CODE)
            }
        }
    }

    private fun openCameraWithScanner() {
        val intent = Intent(this, ScannerActivity::class.java)
            intent.putExtra("scanning_SDK", selectedScanningSDK)
            startActivity(intent)


    }

    enum class ScannerSDK {
        MLKIT,
        ZXING
    }
}


