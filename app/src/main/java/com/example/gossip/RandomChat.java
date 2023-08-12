package com.example.gossip;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gossip.chat.Chat;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.google.android.flexbox.FlexboxLayout.LayoutParams;
import com.nitish.typewriterview.TypeWriterView;
import com.siddydevelops.customlottiedialogbox.CustomLottieDialog;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RandomChat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RandomChat extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static Dialog dialog;
    private EditText enter_tags;
    private ImageView add;
    public FlexboxLayout flexboxLayout;
    public RelativeLayout PopularHashTags;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;

    private TextView hint;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textView7;
    private TextView textView8;
    private TextView textView9;
    private  EditText UserName;
    private List<String> myTags;
    private TextView MyHashTags;
    private TypeWriterView typeWriterView;
    private CircleImageView profilePic;
    private View view1;
    private TextView search;
    public int condition;
    private TextView text;
    CustomLottieDialog customLottieDialog;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");
    ;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context context;
    private String myUserName;

    // TODO: Rename and change types of parameters
    private String mParam1;

    private List<View> dynamicViews = new ArrayList<>();
    private String mParam2;

    public RandomChat() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RandomChat.
     */
    // TODO: Rename and change types and number of parameters
    public static RandomChat newInstance(String param1, String param2) {
        RandomChat fragment = new RandomChat();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void addTextToFlowLayout(String text, FlexboxLayout flexboxLayout) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setBackgroundResource(R.drawable.hashtag_bg);
        textView.setTextSize(15);
        textView.setTextColor(Color.WHITE);
        textView.setPadding(40, 30, 40, 30);
        view1.setVisibility(View.VISIBLE);
        MyHashTags.setVisibility(View.VISIBLE);
        hint.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(15, 10, 15, 20);

        dynamicViews.add(textView);
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                flexboxLayout.removeView(textView);
                return true;
            }
        });
        flexboxLayout.addView(textView, layoutParams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_random_chat, container, false);
//        add=view.findViewById(R.id.add);
        enter_tags = view.findViewById(R.id.type_tags);
        ImageView my_dp = view.findViewById(R.id.profile_image);
//        BottomNavigationPage.customLottieDialog.dismiss();
        flexboxLayout = view.findViewById(R.id.flexboxLayout);
        PopularHashTags = view.findViewById(R.id.PopularHashTags);
        textView1 = view.findViewById(R.id.textview1);
        textView2 = view.findViewById(R.id.textview2);
        textView3 = view.findViewById(R.id.textview3);
        textView4 = view.findViewById(R.id.textview4);
        textView5 = view.findViewById(R.id.textview5);
        textView6 = view.findViewById(R.id.textview6);
        textView7 = view.findViewById(R.id.textview7);
        search = view.findViewById(R.id.Search);
        UserName=view.findViewById(R.id.user_name);
        hint=view.findViewById(R.id.hint);
        String[] arr=new String[3];
        typeWriterView = view.findViewById(R.id.typeWriterView);
        arr[0]="Connect with people from around the world based on common interests.";
        arr[1]="Enter the hashtags to chat on a common topic.";
        arr[2]="Chat with people from around the world without revealing you identity.";
        Handler handler = new Handler();
        final int[] currentTextIndex = {0};
        Runnable textRunnable = new Runnable() {
            @Override
            public void run() {
                typeWriterView.animateText(arr[currentTextIndex[0]]);
                currentTextIndex[0]++;

                if (currentTextIndex[0] < arr.length) {
                    // Schedule the next text change after a delay (e.g., 2 seconds)
                    handler.postDelayed(this, 4000);
                }
            }
        };

        handler.postDelayed(textRunnable, 10);
//        for(int i=0;i<3;i++){
//
//            typeWriterView.animateText(arr[i]);
//
//        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAuthenticationId",MODE_PRIVATE);



//        UserName.setText(sharedPreferences.getString("name","User@1a8e"));
        view1=view.findViewById(R.id.view1);
        MyHashTags=view.findViewById(R.id.MyHashTags);
        view1.setVisibility(View.GONE);
        MyHashTags.setVisibility(View.GONE);
        TextView showLess = view.findViewById(R.id.showLess);
        TextView showMore = view.findViewById(R.id.showMore);
        showMore.setVisibility(View.INVISIBLE);
        showLess.setVisibility(View.VISIBLE);
        myTags = new ArrayList<>();
        myUserName = UserName.getText().toString();
//        UserName.setClickable(false);
        UserName.setFocusable(false);
        hint.setVisibility(View.GONE);
        profilePic=view.findViewById(R.id.profile_image);


        UserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        databaseReference.child("RandomChat").child("HashTags").setValue("");

//        databaseReference.child("RandomChat").child("Status").child(myUserName).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                Toast.makeText(getContext(),"bhar",Toast.LENGTH_SHORT).show();
//                if (snapshot.child("State").getValue() == Long.valueOf(1)) {
////                    Toast.makeText(getContext(),"andar",Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getContext(), StartRandomChat.class);
//                    intent.putExtra("name", snapshot.child("OppoName").getValue(String.class));
//                    intent.putExtra("myUserName", myUserName);
//                    intent.putExtra("chatRoomName", snapshot.child("CRN").getValue(String.class));
//
//                    startMatching.dialog.dismiss();
////                    condition=1;
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startMatching=new StartMatching();
                Container.setMyTags(myTags);
                Log.i("Mytags",myTags.toString()+"   "+Container.getMyTags().toString());
//                startMatching.startMatching(getContext(),myUserName,Container.getMyTags());
                Intent i=new Intent(getContext(),MatchingAnimation.class);
                i.putExtra("name",Container.getMyName());
//                Container.setStop(false);
                startActivity(i);

            }
        });
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) PopularHashTags.getLayoutParams();
        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMore.setVisibility(View.INVISIBLE);
                showLess.setVisibility(View.VISIBLE);
                layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                PopularHashTags.setLayoutParams(layoutParams);
            }
        });

        showLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMore.setVisibility(View.VISIBLE);
                showLess.setVisibility(View.INVISIBLE);
                layoutParams.height = 180;
                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                PopularHashTags.setLayoutParams(layoutParams);
            }
        });
//        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        String filePath = prefs.getString("profile_picture_path", null);
//        if (filePath != null) {
//            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//            my_dp.setImageBitmap(bitmap);
//        }
//        List<String> PopularTags= Arrays.asList("ViratKolhi","PrimeMinisterOfIndia","BTS","TV","Roadies","SplitsVillaS2","BollywoodShows","BJP","InstagramReels","Kashmir","War","Cars","Science","InterstellarMovie");

//        for (String text : PopularTags) {
//            TextView textView = new TextView(getContext());
//            textView.setText(text);
//
//            // Set an OnClickListener for each TextView
//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // Add the clicked text to the selectedFlexboxLayout
//                    addTextToFlowLayout("#"+text,flexboxLayout);
//                }
//            });
//            addTextToFlowLayout("#"+text,PopularHashTags);
//            // Add the TextView to the original FlexboxLayout
////            originalFlexboxLayout.addView(textView);
//        }


//        List<TextItem> textItemList=new ArrayList<>();
//        TextAdapter adapter = new TextAdapter(textItemList);

        flexboxLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(dynamicViews.contains(v)){
                    flexboxLayout.removeView(v);
                    dynamicViews.remove(v);
                    return true;
                }
                return false;
            }
        });

        enter_tags.setInputType(InputType.TYPE_CLASS_TEXT);
        enter_tags.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    String text = enter_tags.getText().toString().trim();
                    if (!text.isEmpty()) {
                        addTextToFlowLayout("#" + text, flexboxLayout);

                        enter_tags.setText("");
                        myTags.add(text);
                    }
                    return true;
                }
                return false;
            }
        });


        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textView1.getText().toString();
                myTags.add(text.substring(1));
                addTextToFlowLayout(text, flexboxLayout);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textView2.getText().toString();
                myTags.add(text.substring(1));
                addTextToFlowLayout(text, flexboxLayout);
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textView3.getText().toString();
                myTags.add(text.substring(1));
                addTextToFlowLayout(text, flexboxLayout);
            }
        });
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textView4.getText().toString();
                myTags.add(text.substring(1));
                addTextToFlowLayout(text, flexboxLayout);
            }
        });
        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textView5.getText().toString();
                myTags.add(text.substring(1));
                addTextToFlowLayout(text, flexboxLayout);
            }
        });
        textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textView6.getText().toString();
                myTags.add(text.substring(1));
                addTextToFlowLayout(text, flexboxLayout);
            }
        });
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textView7.getText().toString();
                myTags.add(text.substring(1));
                addTextToFlowLayout(text, flexboxLayout);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Container.getMyImage()!=null){
            profilePic.setImageBitmap(Container.getMyImage());
        }else{
            Bitmap bitmap=MemoryData.getProfilePicture(MainActivity.mobile,getContext());
            if(bitmap!=null){
                profilePic.setImageBitmap(bitmap);
                Container.setMyImage(bitmap);
            }
        }
        if(Container.getMyName()!=null) UserName.setText(Container.getMyName());
        databaseReference.child("RandomChat").child("Status").child(myUserName).child("State").setValue(2);
        databaseReference.child("RandomChat").child("Status").child(myUserName).child("CRN").setValue("");
        if(Container.ChatRoomName!=null){
            databaseReference.child("RandomChat").child("ChatRoom").child(Container.ChatRoomName).removeValue();
            Container.ChatRoomName=null;
        }
    }
//    public void openCenteredDialog() {
//        // Create a Dialog instance
//        dialog = new Dialog(getContext());
//
//        // Set the layout for the Dialog (your custom layout)
//        dialog.setContentView(R.layout.matching_anim_window);
//
//        // Set the corner radius for the Dialog
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
//
//        // Optionally set other properties for the Dialog, e.g., size, title, etc.
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        dialog.setTitle("Your Dialog Title");
//        databaseReference.child("RandomChat").child("Status").child(myUserName).child("State").setValue(0);
//        text=dialog.findViewById(R.id.textView);
//        TextView stop=dialog.findViewById(R.id.Stop);
//        stop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                databaseReference.child("RandomChat").child("Status").child(myUserName).child("State").setValue(2);
//                dialog.dismiss();
//            }
//        });
//        // Show the Dialog
//        dialog.show();
//    }
//    public void startMatching(){
//        openCenteredDialog();
////                Log.i("HashTagsList", String.valueOf(myTags));
//        if (!myTags.isEmpty()) {
//            databaseReference.child("RandomChat").child("HashTags").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                    int flag = 0;
//                    condition = 0;
//                    for (String s : myTags) {
////                                if(condition==0) {
//                        if (snapshot.hasChild(s) && snapshot.child(s).getChildrenCount() > 0) {
//                            flag = 1;
//                            int users_count = (int) snapshot.child(s).getChildrenCount();
////                                        Random random = new Random();
////                                        int randomUser = random.nextInt(users_count - 1 + 1);
////                                    Toast.makeText(getContext(),String.valueOf(randomUser),Toast.LENGTH_SHORT).show();
//                            for (DataSnapshot snapshot1 : snapshot.child(s).getChildren()) {
//                                String oppoUserName = snapshot1.getKey();
//                                if(!oppoUserName.equals(myUserName)) {
//                                    databaseReference.child("RandomChat").child("Status").child(oppoUserName).child("State").addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
//                                            if (snapshot2.getValue() == Long.valueOf(0)) {
//                                                databaseReference.child("RandomChat").child("Status").child(myUserName).child("CRN").setValue(myUserName + " " + oppoUserName);
//                                                databaseReference.child("RandomChat").child("Status").child(oppoUserName).child("CRN").setValue(myUserName + " " + oppoUserName);
//                                                databaseReference.child("RandomChat").child("Status").child(oppoUserName).child("OppoName").setValue(myUserName);
//                                                databaseReference.child("RandomChat").child("Status").child(myUserName).child("OppoName").setValue(oppoUserName);
//                                                databaseReference.child("RandomChat").child("Status").child(myUserName).child("State").setValue(1);
//                                                databaseReference.child("RandomChat").child("Status").child(oppoUserName).child("State").setValue(1);
//                                                condition = 1;
////                                                        Intent intent = new Intent(getContext(), StartRandomChat.class);
////                                                        intent.putExtra("name", oppoUserName);
////                                                        intent.putExtra("myUserName", myUserName);
////                                                        startActivity(intent);
//
//
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    });
//                                }
////                                            randomUser--;
//                            }
//
//
//                            // Now match with the Random User
//                        }
//                        databaseReference.child("RandomChat").child("HashTags").child(s).child(myUserName).setValue("");
////                                Toast.makeText(getContext(), "Working", Toast.LENGTH_SHORT).show();
////                                        databaseReference.child("RandomChat").child("Status").child(myUserName).child("State").setValue(0);
////                                        databaseReference.child("RandomChat").child("Status").child(myUserName).child("CRN").setValue("");
////                                    }
////                                }else break;
//
//                    }
//                    if (flag == 0)
//                        text.setText("No users Online with your hashtags");
////                                Toast.makeText(getContext(), "No users Online with your hashtags", Toast.LENGTH_SHORT).show();
//                    myTags.clear();
//                    flexboxLayout.removeAllViews();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
////                    for(String s:myTags){
////
////                    }
//        }
//    }


    @Override
    public void onPause() {
        super.onPause();
        typeWriterView.stopAnimation();
    }
}
