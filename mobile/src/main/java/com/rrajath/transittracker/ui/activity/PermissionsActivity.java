package com.rrajath.transittracker.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.rrajath.transittracker.TransitTrackerApplication;
import com.rrajath.transittracker.service.TransitTrackerService;

public class PermissionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            showMessageOKCancel("You need to allow permissions to access location",
                    (dialogInterface, i) -> ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, TransitTrackerApplication.USER_PERMISSION_LOCATION));
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, TransitTrackerApplication.USER_PERMISSION_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case TransitTrackerApplication.USER_PERMISSION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startService(new Intent(this, TransitTrackerService.class));
                } else {
                    Toast.makeText(PermissionsActivity.this, "Please accept permissions", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }
}
