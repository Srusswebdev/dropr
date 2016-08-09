package com.example.android.dropr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SimpleScannerActivity extends MainActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);  // Programmatically initialize the scanner view
        setContentView(mScannerView);               // Set the scanner view as the content view
    }

    @Override
    public  void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);    // Register ourselves as a handler for scan results.
        mScannerView.startCamera();             // Start camera on resume
    }

    @Override
    public void onPause () {
        super.onPause();
        mScannerView.stopCamera();              // Stop the camera on pause
    }


    @Override
    public void handleResult(Result rawResult) {
        // setting a test string variable for the TAG to see what it does.
        String TAG = "Dropr";

        //Toast.makeText(this, "Content - " + rawResult.getText() +
        //        ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();



        /**
         * Create Alert Dialog, so that user has time to read the information within.
         */

        AlertDialog.Builder scanInfo = new AlertDialog.Builder(this);


        String messageContent = "Content - " + rawResult.getText();
        String messageFormat = "Format - " + rawResult.getBarcodeFormat().toString() + ".";
        scanInfo.setTitle("Scan Information:");
        scanInfo.setMessage(messageContent + "\n" + messageFormat);
        scanInfo.setCancelable(true);

        scanInfo.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        // IF you would like to resume scanning, call this method below:
                        /**
                         * Note:
                         * Wait 2 seconds to resume the preview.
                         * On older devices continuously stopping and resuming camera preview can result in freezing the app
                         * I don't know why this is the case but until a solution can be found this slight delay
                         * provides and excellent work-around
                         */
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mScannerView.resumeCameraPreview(SimpleScannerActivity.this);
                            }
                        }, 1000);
                    }
                });

        AlertDialog showInfo = scanInfo.create();
        showInfo.show();

        // Do something with the result here
        Log.v(TAG, rawResult.getText());
        Log.v(TAG, rawResult.getBarcodeFormat().toString());
    }
}
