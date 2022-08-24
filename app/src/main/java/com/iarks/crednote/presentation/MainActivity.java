package com.iarks.crednote.presentation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;
import com.iarks.crednote.R;
import com.iarks.crednote.databinding.ActivityMainBinding;
import com.iarks.crednote.models.CredNote;
import com.iarks.crednote.models.Good;
import com.iarks.crednote.models.InvoiceDetail;
import com.iarks.crednote.models.Organisation;
import com.iarks.crednote.service.SimpleCredNoteRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            verifyStoragePermissions();
        }

        try
        {
            CredNote credNote = new CredNote(new Organisation("B.K.Traders",
                    new String[]{"13", "Pageya Patti Street", "Kolkata - 700045"}, "18AABCU9603R1ZM",
                    "Madhya Pradesh", 999999999),
                    new Organisation("B.K.Traders",
                            new String[]{"13", "Pageya Patti Street", "Kolkata - 700045"}, "18AABCU9603R1ZM",
                            "Madhya Pradesh", 999999999), new InvoiceDetail());
            credNote.addGoods(new Good(1, "String description", 23, new BigDecimal("234"), "unit", BigDecimal.valueOf(23), BigDecimal.valueOf(2)));
            credNote.addGoods(new Good(1, "String description", 23, new BigDecimal("234"), "unit", BigDecimal.valueOf(23), BigDecimal.valueOf(2)));
            credNote.addGoods(new Good(1, "String description", 23, new BigDecimal("234"), "unit", BigDecimal.valueOf(23), BigDecimal.valueOf(2)));
            credNote.addGoods(new Good(1, "String description", 23, new BigDecimal("234"), "unit", BigDecimal.valueOf(23), BigDecimal.valueOf(2)));
            credNote.addGoods(new Good(1, "String description", 63, new BigDecimal("234"), "unit", BigDecimal.valueOf(23), BigDecimal.valueOf(2)));
            credNote.setSgst(BigDecimal.valueOf(23).setScale(2));
            credNote.setCgst(BigDecimal.valueOf(25).setScale(2));
            PdfDocument document = new SimpleCredNoteRenderer().generateCredNote(credNote);

            File file = new File(Environment.getExternalStorageDirectory(), "GFG.pdf");

            try {
                // after creating a file name we will
                // write our PDF file to that location.
                document.writeTo(new FileOutputStream(file));

                // below line is to print toast message
                // on completion of PDF generation.
                Toast.makeText(MainActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                // below line is used
                // to handle error
                e.printStackTrace();
            }
            // after storing our pdf to that
            // location we are closing our PDF file.
            document.close();


        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.MANAGE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}