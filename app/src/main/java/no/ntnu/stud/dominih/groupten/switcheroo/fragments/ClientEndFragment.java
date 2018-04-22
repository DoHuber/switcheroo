package no.ntnu.stud.dominih.groupten.switcheroo.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import no.ntnu.stud.dominih.groupten.switcheroo.R;

public class ClientEndFragment extends Fragment {


    private Bitmap fullImage;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_clientend, container, false);
        context = getContext();

        Button mainMenu = v.findViewById(R.id.clientend_button_main_menu);
        Button exportButton = v.findViewById(R.id.clientend_button_export);

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity() != null) {

                    getActivity().finish();

                }

            }
        });

        ImageView fullImageView = v.findViewById(R.id.clientend_imageview);
        Bundle args = getArguments();

        if (args != null) {

            String base64 = args.getString("full-image");
            byte[] imageBytes = Base64.decode(base64, Base64.DEFAULT);
            fullImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            fullImageView.setImageBitmap(fullImage);

        }

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareTheImage();

            }
        });


        return v;

    }

    private void shareTheImage() {

        // Source for these methods, slightly modified:
        // https://stackoverflow.com/questions/9049143/android-share-intent-for-a-bitmap-is-it-possible-not-to-save-it-prior-sharing?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

        saveImageToCache();
        startShareActivity();


    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void saveImageToCache() {
        // save bitmap to cache directory
        try {

            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            fullImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void startShareActivity() {

        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(context, "no.ntnu.stud.dominih.groupten.switcheroo.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }

    }

}
