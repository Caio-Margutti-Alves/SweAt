package usp.each.si.ach2006.codesport.codeUtils.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.io.InputStream;

import usp.each.si.ach2006.codesport.R;
import usp.each.si.ach2006.codesport.models.user.User;

/**
 * Created by caioa_000 on 18/11/2015.
 */
public class CreateEventDialog {

    private Dialog dialog;

    public void showDialog(Activity activity, User user, String description){

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_information_event);

        AwesomeTextView text = (AwesomeTextView) dialog.findViewById(R.id.txt_description);
        text.setText(description);

        BootstrapButton btn_join = (BootstrapButton) dialog.findViewById(R.id.btn_join);
        BootstrapButton btn_cancel = (BootstrapButton) dialog.findViewById(R.id.btn_cancel);

        ImageView img_profile =  (ImageView) dialog.findViewById(R.id.img_profile);

        AwesomeTextView txt_name = (AwesomeTextView) dialog.findViewById(R.id.txt_name);
        AwesomeTextView txt_age = (AwesomeTextView) dialog.findViewById(R.id.txt_age);

        btn_join.setOnClickListener(lst_join);
        btn_cancel.setOnClickListener(lst_cancel);

        txt_name.setText(user.getFirstName() + " " + user.getLastName());
        txt_age.setText(String.valueOf(user.getAge()) + " years");

        if(user.getFacebookId()!= null){
            new LoadProfileImage(img_profile).execute(user.getFacebookPictureUrl());
        }else{
            new LoadProfileImage(img_profile).execute(user.getGooglePlusPictureUrl());
        }

        dialog.show();
    }


    Button.OnClickListener lst_join = new Button.OnClickListener() {
        public void onClick(View view) {

        }
    };

    Button.OnClickListener lst_cancel = new Button.OnClickListener() {
        public void onClick(View view) {
            dialog.dismiss();
        }
    };

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
