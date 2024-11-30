package com.example.cbill;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText tvUnit;
    private SeekBar rbtSlider;
    private Button btnCalc, btnClear;
    private TextView rbtLabel, tvOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvUnit = findViewById(R.id.tvUnit);
        rbtSlider = findViewById(R.id.rbtSlider);
        rbtLabel = findViewById(R.id.rbtLabel);
        btnCalc = findViewById(R.id.btnCalc);
        btnClear = findViewById(R.id.btnClear);
        tvOutput = findViewById(R.id.tvOutput);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_cbill);

        rbtSlider.setMax(50);
        rbtSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double rebatePercentage = progress / 10.0;
                rbtLabel.setText(getString(R.string.rebate_label, rebatePercentage));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unitStr = tvUnit.getText().toString();

                try {
                    if (unitStr.isEmpty()) {
                        Toast.makeText(MainActivity.this, getString(R.string.please_enter_units), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    double units = Double.parseDouble(unitStr);
                    double rebate = rbtSlider.getProgress() / 10.0;

                    double totCost = calcBill(units);
                    double rebateAmt = totCost * (rebate / 100);
                    double finalCost = totCost - rebateAmt;

                    tvOutput.setText(String.format(Locale.getDefault(), "Final Cost: RM %.2f", finalCost));
                    tvOutput.setBackgroundResource(R.drawable.tvoutputbground);
                    tvOutput.setVisibility(View.VISIBLE);

                } catch (NumberFormatException nfe) {
                    Toast.makeText(MainActivity.this, getString(R.string.enter_valid_number), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvUnit.setText("");
                rbtSlider.setProgress(0);
                rbtLabel.setText(getString(R.string.rebate_label, 0.0));
                tvOutput.setText("");
                tvOutput.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        if (selected == R.id.menuAbt) {
            Toast.makeText(this, "About clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;

        } else if (selected == R.id.menuIns) {
            Toast.makeText(this, "Instructions clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, InstructionsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private double calcBill(double units) {
        double totCost = 0;

        if (units <= 200) {
            totCost += units * 0.218;
        } else if (units <= 300) {
            totCost += (200 * 0.218) + ((units - 200) * 0.334);
        } else if (units <= 600) {
            totCost += (200 * 0.218) + (100 * 0.334) + ((units - 300) * 0.516);
        } else {
            totCost += (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((units - 600) * 0.546);
        }
        return totCost;
    }
}
