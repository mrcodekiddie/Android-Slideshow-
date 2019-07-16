
package com.mks.androidsliderdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.yihsian.slider.library.SliderItemView;
import com.yihsian.slider.library.SliderLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<SliderItemView> slides=new ArrayList<>();
    SliderLayout sliderLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


         sliderLayout =  findViewById(R.id.sliderLayout);

        sliderLayout.setAutoCycle(false);


        if (!isFileReadPermissiongranted())
        {
            showPermissionRequestAlertmessage();

        } else
        {
            readFiles();
        }

        sliderLayout.setSliderInterface(new SliderLayout.SliderInterface() {

            @Override
            public void onSliderClicked(int idx) {
                Log.d("CLICKED", "*****Clicked " + idx + "********");
            }
        });





    }


    private void readFiles()
    {

        String path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();

        Log.d("Files", "Size: " + files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
            String filePath = files[i].getAbsolutePath();
            Log.d("Filesx", filePath);
            String fileExtension=filePath.substring(filePath.length() - 4);
            SliderItemView sliderView = new SliderItemView(this);
            sliderView.setScaleType(SliderItemView.ScaleType.CenterInside);
            if(fileExtension.equals(".mp4"))
            {
                sliderView.setItem(filePath , SliderItemView.ITEM_LOCAL_VIDEO);

            }
            if(fileExtension.equals(".jpg"))
            {
                sliderView.setItem(filePath , SliderItemView.ITEM_LOCAL_IMAGE);

            }

            slides.add(sliderView);

        }
        for(int i=0;i<slides.size();i++)
        {
            sliderLayout.addSlider(slides.get(i));
        }
    }

    private void showPermissionRequestAlertmessage()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this).setTitle("allow me please").setMessage("Allow me to read and write files").setPositiveButton("ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                showPermissionRequestDialog();
            }
        });
        alertDialog.show();
    }

    private boolean isFileReadPermissiongranted()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            return false;
        } else
        {
            return true;
        }
    }

    private void showPermissionRequestDialog()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 2:
                if ((grantResults.length > 0) && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Hurray, I got permission, Thanks", Toast.LENGTH_SHORT).show();
                    readFiles();

                } else
                {
                    Toast.makeText(this, "Common.., I won't eat you.. Just allow me to write something to your files", Toast.LENGTH_SHORT).show();
                    showPermissionRequestDialog();
                }
        }
    }

}
