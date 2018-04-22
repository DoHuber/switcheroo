package no.ntnu.stud.dominih.groupten.switcheroo.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import no.ntnu.stud.dominih.groupten.switcheroo.GameActivity;
import no.ntnu.stud.dominih.groupten.switcheroo.R;


public class WritingFragment extends Fragment implements View.OnClickListener {

    public static final String KEY_ENCODED_IMAGE = "Armed with iron, like a wall";

    private EditText answerText;
    private ImageView answerImage;
    private GameActivity parent;

    public WritingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View textFragmentView = inflater.inflate(R.layout.fragment_text, container, false);
        parent = (GameActivity) getActivity(); 

        getViewRefs(textFragmentView);
        setupSendButton(textFragmentView);
        loadImage();
        
        return textFragmentView;
    }

    private void getViewRefs(View textFragmentView) {
        answerImage = textFragmentView.findViewById(R.id.image_view);
        answerText = textFragmentView.findViewById(R.id.answer_text);
    }

    private void setupSendButton(View textFragmentView) {
        Button sendButton = textFragmentView.findViewById(R.id.send_button_text);
        sendButton.setOnClickListener(this);
    }

    private void loadImage() {
        Bundle arguments = getArguments();
        if (arguments != null) {

            String encodedImg = arguments.getString(KEY_ENCODED_IMAGE);
            byte [] imageBytes = Base64.decode(encodedImg, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            answerImage.setImageBitmap(decodedImage);

        }
    }

    @Override
    public void onClick(View view) {

        parent.finishedWriting(answerText.getText().toString());

    }

}
