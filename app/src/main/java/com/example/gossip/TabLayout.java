package com.example.gossip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabLayout#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabLayout extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewPager viewPager;

    public TabLayout() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabLayout.
     */
    // TODO: Rename and change types and number of parameters
    public static TabLayout newInstance(String param1, String param2) {
        TabLayout fragment = new TabLayout();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
//    private void setupViewPager() {
//        CustomPagerAdapter adapter = new CustomPagerAdapter();
//        adapter.addFragment(new Fragment1(), "Tab 1");
//        adapter.addFragment(new Fragment2(), "Tab 2");
//        adapter.addFragment(new Fragment3(), "Tab 3");
//
//        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);
//    }

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
        View rootView= inflater.inflate(R.layout.fragment_tab_layout, container, false);
        com.google.android.material.tabs.TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);
        viewPager = rootView.findViewById(R.id.view_pager);

        // Create an instance of your FragmentPagerAdapter or FragmentStatePagerAdapter
        CustomPagerAdapter pagerAdapter = new CustomPagerAdapter(getChildFragmentManager());


        pagerAdapter.addFragment(new BlankFragment(),"tab 1");
        pagerAdapter.addFragment(new RandomChat(),"tab 2");
        // Set the adapter to the ViewPager
        viewPager.setAdapter(pagerAdapter);

        // Connect the TabLayout with the ViewPager
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }
}