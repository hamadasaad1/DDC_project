package com.ibnsaad.ddc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bt_training)
    Button mTraining;
    @BindView(R.id.bt_jobs)
    Button mJobs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startActivity(new Intent(getApplicationContext(),LoginActivity.class));

        ButterKnife.bind(this);

        mTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,TrainingActivity.class));
            }
        });

        mJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,JobsActivity.class));

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.homes:
                //
                return true;
            case R.id.about_us:
                //
                return true;

            case R.id.contact:
                //
                return true;

            case R.id.logout:
                //
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }
}
