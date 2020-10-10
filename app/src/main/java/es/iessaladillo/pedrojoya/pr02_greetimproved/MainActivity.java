package es.iessaladillo.pedrojoya.pr02_greetimproved;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import es.iessaladillo.pedrojoya.pr02_greetimproved.databinding.MainActivityBinding;

import static es.iessaladillo.pedrojoya.pr02_greetimproved.databinding.MainActivityBinding.*;

public class MainActivity extends AppCompatActivity {
    private MainActivityBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = inflate(getLayoutInflater());
        setContentView(b.getRoot());
        showProgress();
        b.btnGreet.setOnClickListener(v -> greet());
        b.rdgTreatment.setOnCheckedChangeListener((group, checkedId) -> b.txtIcon.setCompoundDrawablesWithIntrinsicBounds(changeIcon(checkedId), 0, 0, 0));
        b.swtPremium.setOnCheckedChangeListener((buttonView, isChecked) -> changeSwtPremium());
    }

    private void changeSwtPremium() {
        if (b.swtPremium.isChecked()) {
            b.pgbCount.setVisibility(View.GONE);
            b.txtProgress.setVisibility(View.GONE);
        } else {
            b.pgbCount.setVisibility(View.VISIBLE);
            b.txtProgress.setVisibility(View.VISIBLE);
            showProgress();
        }

        b.pgbCount.setProgress(0);
    }

    private int changeIcon(int checkedId) {
        int icon = 0;

        if (checkedId == b.rbdMr.getId()) {
            icon = R.drawable.ic_mr;
        } else if (checkedId == b.rdbMrs.getId()) {
            icon = R.drawable.ic_mrs;
        } else if (checkedId == b.rdbMs.getId()) {
            icon = R.drawable.ic_ms;
        }

        return icon;
    }


    private void greet() {
        if (b.pgbCount.getProgress() < 10) {
            if (!b.txtInputSurname.getText().toString().equals("") && !b.txtInputName.getText().toString().equals("")) {
                showGreet();
                if (!b.swtPremium.isChecked()) {
                    incrementProgress();
                }
            }

        } else {
            b.txtGreet.setText(getString(R.string.noPremiun));
        }
    }

    private void showGreet() {
        int greet;
        String s = "", name =
                b.txtInputName.getText().toString() + " " + b.txtInputSurname.getText().toString();
        if (b.chkPolitely.isChecked()) {
            greet = R.string.txtPoliteGreet;
            //s = b.rdgTreatment.getChildAt(r).toString();
            if (b.rbdMr.isChecked()) {
                s = getString(R.string.rdbMr);
            } else if (b.rdbMs.isChecked()) {
                s = getString(R.string.rdbMs);
            } else if (b.rdbMrs.isChecked()) {
                s = getString(R.string.rdbMrs);
            }
        } else {
            greet = R.string.txtUnpoliteGreet;
        }
        s += name;
        b.txtGreet.setText(getString(greet, s));
    }

    private void incrementProgress() {
        b.pgbCount.setProgress(b.pgbCount.getProgress() + 1);
        showProgress();
    }

    private void showProgress() {
        b.txtProgress.setText(getString(R.string.pgbCounting, b.pgbCount.getProgress()));
    }
}