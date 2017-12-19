package softpro.bot;
/**
 * Created by Naseem on 11-12-2017.
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TrendingActivity extends AppCompatActivity {

    private WebView webView;
    private TextView textHere;
    private RelativeLayout loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);
        //setTitle("   LLinkify");
        webView=(WebView)findViewById(R.id.webView);
        textHere=(TextView)findViewById(R.id.textHere);
        loader=(RelativeLayout)findViewById(R.id.loader);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        String url = "https://www.youtube.com/feed/trending";
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                loader.setVisibility(View.INVISIBLE);
                webView.evaluateJavascript(
                        "(function() { return '<html>'+(document.getElementsByTagName('html')[0].innerHTML)+'</html>'; })();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String html) {
                                //String mhtml = html.substring(1, html.length() - 1).replace("\\u003C", "<").replace("\\n", "").replace("\\t", "").replace("\\", "");
                            }
                        });
            }

        });
    }
}
