package com.example.gossip;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Start_screen_child1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Start_screen_child1 extends Fragment {
    androidx.cardview.widget.CardView cardView;
//    androidx.cardview.widget.CardView Next;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Start_screen_child1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Start_screen_child1.
     */
    // TODO: Rename and change types and number of parameters
    public static Start_screen_child1 newInstance(String param1, String param2) {
        Start_screen_child1 fragment = new Start_screen_child1();
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

        View view= inflater.inflate(R.layout.fragment_start_screen_child1, container, false);
        cardView=view.findViewById(R.id.getStarted);
//        Next=view.findViewById(R.id.Next);
//        Next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Start_Screen_1.viewPager = new viewPager(getActivity().getSupportFragmentManager(), 1);
//                Start_Screen_1.pager.setAdapter(Start_Screen_1.viewPager);
//
//            }
//        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),Login.class);
                startActivity(i);
                Start_Screen_1 activity= (Start_Screen_1) getActivity();
                activity.finish();
            }
        });
        return view;
    }
}