package com.example.gossip;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gossip.Status.StatusGallery;

public class ReportDialog {
    public void openCenteredDialog(Context context,String text1,String text2,boolean FrndReq,String UserName) {
        // Create a Dialog instance
        Dialog dialog = new Dialog(context);

        // Set the layout for the Dialog
        dialog.setContentView(R.layout.report_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        // Optionally set other properties for the Dialog, e.g., size, title, etc.
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setTitle("Your Dialog Title");
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        TextView report=dialog.findViewById(R.id.report);
        TextView sendReq=dialog.findViewById(R.id.sendReq);
        if(FrndReq) {
            sendReq.setVisibility(View.VISIBLE);
            sendReq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.sendRequest(UserName,context,sendReq,dialog);
                }
            });
        }
        report.setText(text2);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,text1,Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        // Show the Dialog
        dialog.show();
    }
}
