package com.example.pantry_parser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDetails extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentDetailsListener listener;
    private TextView details;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FragmentDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment details.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDetails newInstance(String param1, String param2) {
        FragmentDetails fragment = new FragmentDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        details = getView().findViewById(R.id.recipeDetails);
        }



    public interface FragmentDetailsListener {
        void onInputDetailsSent(CharSequence input);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_details, container, false);
        details = v.findViewById(R.id.recipeDetails);

        return v;
    }

    public void updateText(Recipe recipe){
        details.setText(recipe.getSummary());

    }
}