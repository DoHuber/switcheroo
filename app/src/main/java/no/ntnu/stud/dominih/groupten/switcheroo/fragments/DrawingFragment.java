package no.ntnu.stud.dominih.groupten.switcheroo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import no.ntnu.stud.dominih.groupten.switcheroo.DrawView;
import no.ntnu.stud.dominih.groupten.switcheroo.GameActivity;
import no.ntnu.stud.dominih.groupten.switcheroo.R;


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
        drawView = view.findViewById(R.id.drawing_draw_view);

        textToDraw = view.findViewById(R.id.drawing_text_hint);
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
        Button sendButton = view.findViewById(R.id.drawing_send_button);
        sendButton.setOnClickListener(this);
        Button clearButton = view.findViewById(R.id.drawing_clear_button);
        clearButton.setOnClickListener(this);
        Button blackButton = view.findViewById(R.id.blackButton);
        blackButton.setOnClickListener(this);
        Button redButton = view.findViewById(R.id.redButton);
        redButton.setOnClickListener(this);
        Button yellowButton = view.findViewById(R.id.yellowButton);
        yellowButton.setOnClickListener(this);
        Button greenButton = view.findViewById(R.id.greenButton);
        greenButton.setOnClickListener(this);
        Button blueButton = view.findViewById(R.id.blueButton);
        blueButton.setOnClickListener(this);
        Button undoButon = view.findViewById(R.id.drawing_undo_button);
        undoButon.setOnClickListener(this);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.drawing_clear_button:
                drawView.clearCanvas();
                break;

            case R.id.drawing_send_button:

                String imgString = Base64.encodeToString(drawView.getBitmapBytes(), Base64.DEFAULT);
                parent.finishedDrawing(imgString);

                break;

            case R.id.blackButton:
                drawView.setColor(DrawView.BLACK);
                break;
            case R.id.redButton:
                drawView.setColor(DrawView.RED);
                break;

            case R.id.yellowButton:
                drawView.setColor(DrawView.YELLOW);
                break;
            case R.id.greenButton:
                drawView.setColor(DrawView.GREEN);
                break;

            case R.id.blueButton:
                drawView.setColor(DrawView.BLUE);
                break;
            case R.id.drawing_undo_button:
                drawView.onClickUndo();
                break;
        }


    }

}
