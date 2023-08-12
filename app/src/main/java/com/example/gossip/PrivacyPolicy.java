package com.example.gossip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PrivacyPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        ImageView back=findViewById(R.id.back);
        TextView privacyPolicyTextView = findViewById(R.id.privacy_policy_text);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // Set the formatted text using Html.fromHtml()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            privacyPolicyTextView.setText(Html.fromHtml(getString(R.string.privacy_policy_text), Html.FROM_HTML_MODE_COMPACT));
        }
    }
}