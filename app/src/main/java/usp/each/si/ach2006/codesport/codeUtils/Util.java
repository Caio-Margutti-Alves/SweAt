package usp.each.si.ach2006.codesport.codeUtils;

import android.content.Context;
import android.content.Intent;

public class Util {

	public static final String LAST_POSITION = "LAST_POSITION";
	public static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";

	public static final String CLOSE = "Close";
	public static final String PROFILE = "Profile";
	public static final String BUILDING = "Building";
	public static final String BOOK = "Book";
	public static final String PAINT = "Paint";
	public static final String CASE = "Case";
	public static final String SHOP = "Shop";
	public static final String PARTY = "Party";
	public static final String MOVIE = "Movie";

	//For the mark dialog
	public static final int MENU_VIEW_POINT = 0;
	public static final int MENU_ANIMAL = 1;
	public static final int MENU_WARNING = 2;
	public static final int MENU_CLOSURE = 3;
	public static final int MENU_EMERGENCY = 4;
	public static final int MENU_QUESTION = 5;

	//For the Maps Style dialog
	public static final int MENU_NORMAL = 0;
	public static final int MENU_HYBRID = 1;
	public static final int MENU_SATELLITE = 2;
	public static final int MENU_TERRAIN = 3;

	//For the Search Trails dialog
	public static final int MENU_0 = 0;
	public static final int MENU_1 = 1;
	public static final int MENU_2 = 2;
	public static final int MENU_3 = 3;
	public static final int MENU_4 = 4;

	public static boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	
	public static void share(Context context, String chatId, boolean isGroup) {
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.setType("text/plain");
		sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		// Add data to the intent, the receiving app will decide what to do with it.
		if (isGroup) {
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Join my group");
			sendIntent.putExtra(Intent.EXTRA_TEXT, "My group ID is " + chatId);

		} else {
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Invitation to chat");
			sendIntent.putExtra(Intent.EXTRA_TEXT, "My chat ID is " + chatId);
		}
		
		context.startActivity(Intent.createChooser(sendIntent, "Invite via"));
	}

    public static String fix(String in) {
        in = in.replaceAll("&quot;", "\"");
        in = in.replaceAll("&amp;", "&");
        in = in.replaceAll("&lt;", "<");
        in = in.replaceAll("&gt;", ">");
        return in;
    }

}
