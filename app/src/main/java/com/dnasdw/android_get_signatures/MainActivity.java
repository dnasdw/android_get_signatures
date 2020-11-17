package com.dnasdw.android_get_signatures;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "pkg_info";
    public static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/pkg_info/packageSignatures.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Button btnExport = (Button) findViewById(R.id.btn_export);
            if (btnExport != null) {
                btnExport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Context context = getApplicationContext();
                            PackageManager packageManager = context.getPackageManager();
                            List<PackageInfo> installedPackages = packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES);
                            JSONArray packageNameSignatures = new JSONArray();
                            for (PackageInfo packageInfo : installedPackages) {
                                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0
                                        && (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0
                                        && packageInfo.signatures.length != 0) {
                                    JSONObject packageNameSignature = new JSONObject();
                                    packageNameSignature.put("packageName", packageInfo.packageName);
                                    packageNameSignature.put("signature", packageInfo.signatures[0].toCharsString());
                                    packageNameSignatures.put(packageNameSignature);
                                }
                            }
                            JSONObject packages = new JSONObject();
                            packages.put("packages", packageNameSignatures);
                            String json = packages.toString(4);
                            Log.d(TAG, json);
                            File file = new File(PATH);
                            file.getParentFile().mkdirs();
                            file.createNewFile();
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            fileOutputStream.write(json.getBytes());
                            fileOutputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}