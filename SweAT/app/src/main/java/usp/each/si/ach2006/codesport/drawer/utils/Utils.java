package usp.each.si.ach2006.codesport.drawer.utils;

import android.content.Context;
import usp.each.si.ach2006.codesport.R;

public class Utils {

	//Set all the navigation icons and always to set "zero 0" for the item is a category
	public static int[] iconNavigation = new int[] {  /*R.drawable.ic_action_map,*/ R.drawable.ic_action_help,
            R.drawable.ic_action_person, R.drawable.ic_action_share};
	
	//get title of the item navigation
	public static String getTitleItem(Context context, int posicao){		
		String[] titulos = context.getResources().getStringArray(R.array.nav_menu_items);  
		return titulos[posicao];
	} 
	
}
