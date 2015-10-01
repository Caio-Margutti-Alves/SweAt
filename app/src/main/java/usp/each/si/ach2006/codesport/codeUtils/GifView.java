package usp.each.si.ach2006.codesport.codeUtils;

import android.content.Context;
import android.webkit.WebView;

/**
 * Created by caioa_000 on 20/09/2015.
 */
public class GifView extends WebView {

    public GifView(Context context, String path) { super(context); loadUrl(path); }

}