package es.iessaladillo.pedrojoya.pr02_greetimproved;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import es.iessaladillo.pedrojoya.pr02_greetimproved.databinding.MainActivityBinding;

import static es.iessaladillo.pedrojoya.pr02_greetimproved.databinding.MainActivityBinding.*;
import static es.iessaladillo.pedrojoya.pr02_greetimproved.utils.SoftInputUtils.*;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding b;
    private final int MAX_CHAR = 20;
    private int maxCharName = MAX_CHAR;
    private int maxCharSurname = MAX_CHAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setupViews();
    }

    private void setupViews() {
        showMaxCharName();
        showMaxCharSurname();
        showProgress();
        b.txtInputName.setOnFocusChangeListener((v, hasFocus) -> changeTextViewColorOnFocus(b.charName, hasFocus));
        b.txtInputSurname.setOnFocusChangeListener((v, hasFocus) -> changeTextViewColorOnFocus(b.charSurname, hasFocus));
        b.txtInputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateName();
                changeCountCharName();
            }
        });
        b.txtInputSurname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateSurname();
                changeCountCharSurname();
            }
        });
        setInitialState(b.charName);
        setInitialState(b.charSurname);
        b.btnGreet.setOnClickListener(v -> greet());
        b.rdgTreatment.setOnCheckedChangeListener((group, checkedId) -> b.txtIcon.setCompoundDrawablesWithIntrinsicBounds(changeIcon(checkedId), 0, 0, 0));
        b.swtPremium.setOnCheckedChangeListener((buttonView, isChecked) -> changeSwtPremium());
        b.txtInputSurname.setOnEditorActionListener((v, actionId, event) -> {
            greet();
            return false;
        });
    }

    private void validateSurname() {
        if(b.txtInputSurname.getText().toString().isEmpty()){
            b.txtInputSurname.setError(getString(R.string.required));
        } else {
            b.txtInputSurname.setError(null);
        }
    }

    private void validateName() {
        if(b.txtInputName.getText().toString().isEmpty()){
            b.txtInputName.setError(getString(R.string.required));
        } else {
            b.txtInputName.setError(null);
        }
    }

    private void changeTextViewColorOnFocus(TextView v, boolean hasFocus) {
        if(hasFocus) {
            v.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            setInitialState(v);
        }
    }

    private void setInitialState(TextView v) {
        v.setTextColor(getResources().getColor(R.color.textPrimary));
    }

    private void changeCountCharSurname() {
        maxCharSurname = MAX_CHAR - b.txtInputSurname.getText().toString().length();
        showMaxCharSurname();
    }

    private void changeCountCharName() {
        maxCharName = MAX_CHAR - b.txtInputName.getText().toString().length();
        showMaxCharName();
    }

    private void showMaxCharSurname() {
        b.charSurname.setText(getResources().getQuantityString(R.plurals.count_char_surname,
                maxCharSurname, maxCharSurname));
    }

    private void showMaxCharName() {
        b.charName.setText(getResources().getQuantityString(R.plurals.count_char_name, maxCharName,
                maxCharName));
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
        hideSoftKeyboard(b.txtInputName);
        if(b.txtInputName.getText().toString().isEmpty()){
            b.txtInputName.requestFocus();
            validateName();
        } else if(b.txtInputSurname.getText().toString().isEmpty()){
            b.txtInputSurname.requestFocus();
            validateSurname();
        } else {
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
        Toast.makeText(this, getString(greet, s), Toast.LENGTH_SHORT).show();
    }

    private void incrementProgress() {
        b.pgbCount.setProgress(b.pgbCount.getProgress() + 1);
        showProgress();
    }

    private void showProgress() {
        b.txtProgress.setText(getString(R.string.pgbCounting, b.pgbCount.getProgress()));
    }
}