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



public class DrawingFragment extends Fragment implements View.OnClickListener {


    public static final String KEY_HAS_TEXT = "MDSFMSDFDS";
    public static final String KEY_TEXT_CAPTION = "MDFKSFMKSDFDSF";

    private DrawView drawView;
    private GameActivity parent;
    private TextView textToDraw;

    public DrawingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawing, container, false);
        parent = (GameActivity) getActivity();

        getViewReferences(view);
        loadText();
        setupButtons(view);

        return view;

    }

    private void getViewReferences(View view) {
        drawView = view.findViewById(R.id.drawing);

        textToDraw = view.findViewById(R.id.txt_to_draw);
    }

    private void loadText() {
        Bundle arguments = getArguments();
        String text = "You're first! Draw something inspiring.";
        if (arguments != null) {

            boolean hasText = arguments.getBoolean(KEY_HAS_TEXT);
            if (hasText) {

                text = arguments.getString(KEY_TEXT_CAPTION);

            }

        }
        textToDraw.setText(text);
    }

    private void setupButtons(View view) {
        Button sendButton = view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);
        Button clearButton = view.findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);
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

}
