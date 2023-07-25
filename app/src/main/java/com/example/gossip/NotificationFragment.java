package com.example.gossip;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gossip.Notifications.FriendRequestList;
import com.example.gossip.Notifications.NotificationAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private List<FriendRequestList> friendRequests;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private LinearLayout linearLayout;

    private LottieAnimationView lottieAnimationView;
    private TextView textView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        View v= inflater.inflate(R.layout.fragment_home, container, false);
//        LottieAnimationView animationView = v.findViewById(R.id.lottieAnimationView);
//        animationView.setAnimation("tick.json");
//        animationView.playAnimation();
        recyclerView = v.findViewById(R.id.recyclerView);
        friendRequests = new ArrayList<>();
        linearLayout=v.findViewById(R.id.linearLayout);
//        Toast.makeText(getContext(),MainActivity.mobile,Toast.LENGTH_SHORT).show();
        databaseReference.child(MainActivity.mobile).child("FrndReq").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendRequests.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    friendRequests.add(new FriendRequestList(snapshot1.child("Name").getValue(String.class),snapshot1.child("profilePic").getValue(String.class),snapshot1.getKey()));
                }
                adapter = new NotificationAdapter(friendRequests,getContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
                if(friendRequests.isEmpty()) linearLayout.setVisibility(View.VISIBLE);
                else linearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




//        friendRequests.add(new FriendRequestList(R.drawable.profile_picture_1));
//        friendRequests.add(new FriendRequestList(R.drawable.profile_picture_2));
        return v;
    }
}