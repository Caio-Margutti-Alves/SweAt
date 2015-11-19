package usp.each.si.ach2006.codesport.codeUtils.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.maps.model.Marker;

import usp.each.si.ach2006.codesport.R;
import usp.each.si.ach2006.codesport.codeUtils.LoadProfileImage;
import usp.each.si.ach2006.codesport.codeUtils.Util;
import usp.each.si.ach2006.codesport.models.user.User;

/**
 * Created by caioa_000 on 18/11/2015.
 */
public class DateDialog {

    private Dialog dialog;
    TimeDialog time_dialog;
    Activity activity;
    Marker marker;

    public void showDialog(Activity activity, Marker marker){

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_pick_date);

        this.activity = activity;
        this.marker = marker;

        AwesomeTextView txt_event = (AwesomeTextView) dialog.findViewById(R.id.txt_event);

        BootstrapButton btn_join = (BootstrapButton) dialog.findViewById(R.id.btn_ok);
        BootstrapButton btn_cancel = (BootstrapButton) dialog.findViewById(R.id.btn_cancel);

        ImageView img_marker_left =  (ImageView) dialog.findViewById(R.id.img_marker_left);
        AwesomeTextView txt_x =  (AwesomeTextView) dialog.findViewById(R.id.txt_x);

        btn_join.setOnClickListener(lst_ok);
        btn_cancel.setOnClickListener(lst_cancel);

        txt_x.setOnClickListener(lst_text_x);

        txt_event.setText(marker.getTitle());

        int which = Integer.parseInt(marker.getSnippet());

        switch (which) {
            case Util.MENU_SOCCER:
                img_marker_left.setImageResource(R.drawable.icon_soccer);
                break;
            case Util.MENU_BASKET:
                img_marker_left.setImageResource(R.drawable.icon_basket);
                break;
            case Util.MENU_VOLLEY:
                img_marker_left.setImageResource(R.drawable.icon_volley);
                break;
            case Util.MENU_TENNIS:
                img_marker_left.setImageResource(R.drawable.icon_tennis);
                break;
            case Util.MENU_BASEBALL:
                img_marker_left.setImageResource(R.drawable.icon_baseball);
                break;
            case Util.MENU_FOOTBALL:
                img_marker_left.setImageResource(R.drawable.icon_football);
                break;
        }

        dialog.show();
    }


    Button.OnClickListener lst_ok = new Button.OnClickListener() {
        public void onClick(View view) {
            dialog.dismiss();
            time_dialog = new TimeDialog();
            time_dialog.showDialog(activity, marker);
        }
    };

    Button.OnClickListener lst_cancel = new Button.OnClickListener() {
        public void onClick(View view) {
            dialog.dismiss();
        }
    };

    AwesomeTextView.OnClickListener lst_text_x = new AwesomeTextView.OnClickListener() {
        public void onClick(View view) {
            dialog.dismiss();
        }
    };

}
