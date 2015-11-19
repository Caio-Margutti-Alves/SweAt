package usp.each.si.ach2006.codesport.codeUtils.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import java.io.InputStream;

import usp.each.si.ach2006.codesport.R;
import usp.each.si.ach2006.codesport.codeUtils.LoadProfileImage;
import usp.each.si.ach2006.codesport.codeUtils.Util;
import usp.each.si.ach2006.codesport.models.user.User;

/**
 * Created by caioa_000 on 18/11/2015.
 */
public class EventDialog {

    private Dialog dialog;

    public void showDialog(Activity activity, Marker marker, User user, String description){

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_information_event);

        AwesomeTextView txt_event = (AwesomeTextView) dialog.findViewById(R.id.txt_event);
        AwesomeTextView txt_description = (AwesomeTextView) dialog.findViewById(R.id.txt_description);

        BootstrapButton btn_join = (BootstrapButton) dialog.findViewById(R.id.btn_join);
        BootstrapButton btn_cancel = (BootstrapButton) dialog.findViewById(R.id.btn_cancel);

        ImageView img_profile =  (ImageView) dialog.findViewById(R.id.img_profile);

        ImageView img_marker_left =  (ImageView) dialog.findViewById(R.id.img_marker_left);
        ImageView img_marker_right =  (ImageView) dialog.findViewById(R.id.img_marker_right);

        AwesomeTextView txt_name = (AwesomeTextView) dialog.findViewById(R.id.txt_name);
        AwesomeTextView txt_age = (AwesomeTextView) dialog.findViewById(R.id.txt_age);

        btn_join.setOnClickListener(lst_join);
        btn_cancel.setOnClickListener(lst_cancel);

        txt_name.setText(user.getFirstName() + " " + user.getLastName());
        txt_age.setText(String.valueOf(user.getAge()) + " years");

        txt_event.setText(marker.getTitle());
        txt_description.setText(description);

        if(user.getFacebookId()!= null){
            new LoadProfileImage(img_profile).execute(user.getFacebookPictureUrl());
        }else{
            new LoadProfileImage(img_profile).execute(user.getGooglePlusPictureUrl());
        }

        int which = Integer.parseInt(marker.getSnippet());

        switch (which) {
            case Util.MENU_SOCCER:
                img_marker_left.setImageResource(R.drawable.icon_soccer);
                img_marker_right.setImageResource(R.drawable.icon_soccer);
                break;
            case Util.MENU_BASKET:
                img_marker_left.setImageResource(R.drawable.icon_basket);
                img_marker_right.setImageResource(R.drawable.icon_basket);
                break;
            case Util.MENU_VOLLEY:
                img_marker_left.setImageResource(R.drawable.icon_volley);
                img_marker_right.setImageResource(R.drawable.icon_volley);
                break;
            case Util.MENU_TENNIS:
                img_marker_left.setImageResource(R.drawable.icon_tennis);
                img_marker_right.setImageResource(R.drawable.icon_tennis);

                break;
            case Util.MENU_BASEBALL:
                img_marker_left.setImageResource(R.drawable.icon_baseball);
                img_marker_right.setImageResource(R.drawable.icon_baseball);
                break;
            case Util.MENU_FOOTBALL:
                img_marker_left.setImageResource(R.drawable.icon_football);
                img_marker_right.setImageResource(R.drawable.icon_football);
                break;
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

}
