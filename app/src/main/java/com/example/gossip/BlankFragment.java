package com.example.gossip;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.gossip.messages.MessagesAdapter;
import com.example.gossip.messages.MessagesList;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gossip.chat.Chat;
import com.example.gossip.messages.MessagesAdapter;
import com.example.gossip.messages.MessagesList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
    public com.google.android.material.bottomappbar.BottomAppBar bottomBar;
    private ImageView imageView;
    private ImageView imageView2;
    private RecyclerView messagesRecyclerView ;
    private RelativeLayout relativeLayout;
    public static String mobile;
    private String name;
    private String lastMessage="";
    private int unseenMessages=0;
    com.example.gossip.EarthView global;
    private String chatKey="";
    private boolean dataSet=false;
    private MessagesAdapter messagesAdapter;
    private ImageView add;
    private int i=1;
    public static String newUser_mobile;
//    private TreeSet<String> set;
    private final List<MessagesList> messagesLists=new ArrayList<>();
    private DatabaseReference databaseReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public int bottomNavigationViewWidth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_blank, container, false);
        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");
        mobile= MemoryData.getData(getContext());
//        Log.i("my mobile no",mobile);
        imageView=view.findViewById(R.id.left_bottom_nav);
        relativeLayout=view.findViewById(R.id.relativeLayout);
        imageView2=view.findViewById(R.id.right_bottom_nav);
        name=MemoryData.getName(getContext());
        messagesRecyclerView=view.findViewById(R.id.messagesRecyclerView);
        add=view.findViewById(R.id.add);
        messagesRecyclerView.setHasFixedSize(true);
        com.google.android.material.bottomappbar.BottomAppBar bottomBar = ((BottomNavigationPage) getActivity()).bottomBar;
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messagesAdapter=new MessagesAdapter(messagesLists,getContext());
        messagesRecyclerView.setAdapter(messagesAdapter);

        Resources resources = getResources();
        bottomNavigationViewWidth=resources.getDisplayMetrics().widthPixels;
        int halfWidth = bottomNavigationViewWidth/2 ;
        ViewGroup.MarginLayoutParams initialLayoutParams = (ViewGroup.MarginLayoutParams) bottomBar.getLayoutParams();
        int w=initialLayoutParams.leftMargin+8;
        int h=initialLayoutParams.rightMargin+8;
//// create a new ValueAnimator to animate the width when scrolling down
        ValueAnimator animatorDown = ValueAnimator.ofInt(bottomNavigationViewWidth, halfWidth);

// set the duration and interpolator for the animator
        animatorDown.setDuration(200);
        animatorDown.setInterpolator(new AccelerateDecelerateInterpolator());

// add an update listener to the animator to adjust the width of the BottomNavigationView
        animatorDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) bottomBar.getLayoutParams();
                params.leftMargin = (bottomNavigationViewWidth - value) / 2;
                params.rightMargin = params.leftMargin;
                bottomBar.setLayoutParams(params);
                int alphaValue = (int) ((value - halfWidth) / (float) (bottomNavigationViewWidth - halfWidth) * 255);
                Drawable background = bottomBar.getBackground();
                if (background != null) {
                    background.setAlpha(alphaValue);
                }
            }
        });

// create a new ValueAnimator to animate the width when scrolling up
        ValueAnimator animatorUp = ValueAnimator.ofInt(halfWidth, bottomNavigationViewWidth);

// set the duration and interpolator for the animator
        animatorUp.setDuration(200);
        animatorUp.setInterpolator(new AccelerateDecelerateInterpolator());

// add an update listener to the animator to adjust the width of the BottomNavigationView
        animatorUp.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) bottomBar.getLayoutParams();
                params.leftMargin = (bottomNavigationViewWidth - value) / 2;
                params.rightMargin = params.leftMargin;
                bottomBar.setLayoutParams(params);
                int alphaValue = (int) ((value - halfWidth) / (float) (bottomNavigationViewWidth - halfWidth) * 255);
                Drawable background = bottomBar.getBackground();
//                Toast.makeText(getContext(),String.valueOf(alphaValue)+"   "+String.valueOf(halfWidth),Toast.LENGTH_SHORT).show();
                if (background != null) {
                    background.setAlpha(alphaValue);
                }
            }
        });

// add a scroll listener to the RecyclerView to start the animators when scrolling
        messagesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
//                    Toast.makeText(getContext(),String.valueOf(bottomNavigationViewWidth),Toast.LENGTH_SHORT).show();
                    // scrolling down, animate the view to half size
                    ValueAnimator anim = ValueAnimator.ofFloat(BottomNavigationPage.navigationView.getTranslationY(), 200);
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            float value = (float) valueAnimator.getAnimatedValue();
                            BottomNavigationPage.navigationView.setTranslationY(value);
                        BottomNavigationPage.bottomBar.setTranslationY(value);
                        }
                    });
                    anim.setDuration(200);
                    anim.start();
                    animatorDown.start();

                } else if (dy < 0) {
                    // scrolling up, animate the view back to full size
                    ValueAnimator anim = ValueAnimator.ofFloat(BottomNavigationPage.navigationView.getTranslationY(), 0);
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            float value = (float) valueAnimator.getAnimatedValue();
                            BottomNavigationPage.navigationView.setTranslationY(value);
                        BottomNavigationPage.bottomBar.setTranslationY(value);
                        }
                    });
                    anim.setDuration(200);
                    anim.start();
                    animatorUp.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                        }
                        @Override
                        public void onAnimationEnd(Animator animator) {
//                            Toast.makeText(getContext(),"aaaaa",Toast.LENGTH_SHORT).show();
                            initialLayoutParams.leftMargin=w;
                            initialLayoutParams.rightMargin=h;
                            bottomBar.setLayoutParams(initialLayoutParams);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                            // Do nothing
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                            // Do nothing
                        }
                    });
                    animatorUp.start();
                }
            }
        });
        final int[] prevScrollY = {0};

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                dataSet=true;
//                    if(!getmobile.equals(mobile) ){
                Log.i("mno","2");
                databaseReference.child(mobile).child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int getChatCounts= (int) snapshot.getChildrenCount();
//                                Toast.makeText(getApplicationContext(),String.valueOf(getChatCounts),Toast.LENGTH_SHORT).show();
                        if(getChatCounts>0){
                            messagesLists.clear();
//                            set.clear();
                            unseenMessages=0;
                            lastMessage="";
                            chatKey="";
                            Log.i("mno","4");
//                                    messagesLists.clear();

                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
//                                        Log.i("mno","5");
                                final String getKey=dataSnapshot1.getKey();
                                chatKey=getKey;
                                final String getmobile=snapshot.child(chatKey).child("user_2").getValue(String.class);

//                                        assert getmobile != null;
                                String getName=snapshot1.child(mobile).child("contacts").child(getmobile).child("Name").getValue(String.class);
//                                        Toast.makeText(getApplicationContext(),getName+"   2",Toast.LENGTH_SHORT).show();
                                if(dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")){
                                    final String getUserOne=dataSnapshot1.child("user_1").getValue(String.class);
                                    final String getUserTwo=dataSnapshot1.child("user_2").getValue(String.class);
                                    if(getUserOne.equals(mobile) && getUserTwo.equals(getmobile) || getUserOne.equals(getmobile) && getUserTwo.equals(mobile)){
                                        if(dataSnapshot1.child("messages").getChildrenCount()==0){
//                                                    Toast.makeText(getApplicationContext(),String.valueOf(messagesLists.size())+"   "+chatKey,Toast.LENGTH_SHORT).show();
//                                                    if(!set.contains(chatKey)){
                                            MessagesList messagesList = new MessagesList(getName, getmobile, "", 0, chatKey);
                                            messagesLists.add(messagesList);
//                                                        set.add(chatKey);
                                            Log.i("if0","aaaaaa");
//                                                        break;
//                                                    }
                                        }else{



                                            for(DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()){
                                                final Long getMessageKey= Long.valueOf(chatDataSnapshot.getKey());
                                                final Long getLastSeenMessage= Long.valueOf(MemoryData.getLastMsgTs(getContext(),getKey));
                                                Log.i("myTest",getLastSeenMessage.toString()+"    "+getMessageKey.toString()+"     "+chatKey.toString());

                                                lastMessage=chatDataSnapshot.child("msg").getValue(String.class);
                                                Log.i("XYZ2",lastMessage+"   "+String.valueOf(getMessageKey));
                                                if(getMessageKey>getLastSeenMessage){
                                                    unseenMessages++;
                                                }else unseenMessages=0;
                                            }

                                            Log.i("chatKey",String.valueOf(chatKey)+"    "+String.valueOf(unseenMessages));
                                            MessagesList messagesList = new MessagesList(getName, getmobile, lastMessage, unseenMessages, chatKey);
                                            messagesLists.add(messagesList);

                                        }

//                                                    set.add(chatKey);
//                                                }


                                    }
                                }


                            }


                        }
                        if(messagesLists!=null) messagesAdapter.updateData(messagesLists);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                    }

//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                messagesLists.clear();
            }
        });
        return view;
    }
}