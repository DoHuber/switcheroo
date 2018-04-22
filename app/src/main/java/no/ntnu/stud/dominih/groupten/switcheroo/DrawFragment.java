package no.ntnu.stud.dominih.groupten.switcheroo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



public class DrawFragment extends Fragment implements View.OnClickListener {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DrawView drawView;
    private Button clearButton;
    private Button sendButton;
    private GameActivity parent;
    private TextView textToDraw;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public DrawFragment() {
        // Required empty public constructor
    }

    /*
    // TODO: Rename and change types and number of parameters
    public static DrawFragment newInstance(String param1, String param2) {
        DrawFragment fragment = new DrawFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parent = (GameActivity) getActivity();

        View _view = inflater.inflate(R.layout.fragment_drawing, container, false);
        drawView = _view.findViewById(R.id.drawing);
        clearButton = _view.findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);
        textToDraw = _view.findViewById(R.id.txt_to_draw);

        //TODO PASS TEXT TO textToDraw
        textToDraw.setText("PLACE HERE THE TEXT RECEIVED FROM THE SERVER");
        //
        sendButton = _view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);

        return _view;

    }


    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.clear_button:
                drawView.clearCanvas();
                break;

            case R.id.send_button:

                String imgString = Base64.encodeToString(drawView.toJPEG(), Base64.NO_WRAP);
                parent.finishedDrawing(imgString);

                break;

        }


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
