package softpro.bot;
/**
 * Created by Naseem on 11-12-2017.
 */
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.bumptech.glide.Glide;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<LinkModel> links = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter linksAdapter;
    private ImageView imgThumbnail;
    private TextView titleText;
    private RelativeLayout loader;
    private BootstrapButton gobtn;
    private BootstrapEditText urltext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gobtn = (BootstrapButton) findViewById(R.id.gobtn);
        imgThumbnail = (ImageView) findViewById(R.id.imgThumbnail);
        titleText = (TextView) findViewById(R.id.titleText);
        loader = (RelativeLayout) findViewById(R.id.loader);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        urltext = (BootstrapEditText) findViewById(R.id.urltext);
        gobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doClick(urltext.getText().toString());
            }
        });

        Intent receivedIntent = getIntent();
        String receivedAction = receivedIntent.getAction();
        String receivedType = receivedIntent.getType();
        //In Case The User Directly Shares The Link Via Our App
        if (receivedAction.equals(Intent.ACTION_SEND)) {
            if (receivedType.startsWith("text/")) {
                //Handle Sent Link
                //Get The Recieved Text
                doClick(receivedIntent.getStringExtra(Intent.EXTRA_TEXT));
            }
        } else if (receivedAction.equals(Intent.ACTION_MAIN)) {
            //If App Has Been Launched Directly From The Launcher
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    //Inflating The Menu With An Option To Directly Copy The Link From Clipboard
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.paste:
                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                if (cm.hasPrimaryClip()) {
                    if (cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        ClipData cd = cm.getPrimaryClip();
                        String str = cd.getItemAt(0).getText().toString();
                        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                        urltext.setText(str);
                    } else {
                        Toast.makeText(this, "Sorry. We Don't Support That Mime Type", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Sorry Nothing Found To Paste", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void doClick(final String url) {
        loader.setVisibility(View.VISIBLE);
        loader.animate().alpha(1.0f).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                loader.setVisibility(View.VISIBLE);
                fetchData(url);
            }
        }).start();
    }

    public void fetchData(String urll) {
        //Using A WebView Internally To Load The Page And The Get The Source Code From The Page.
        //Its Just A Work Around, No Direct Approach Worked
        WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.clearHistory();
        webView.clearFormData();
        String url = "http://shouvikmitra.fanclub.rocks/utilities/getvideo.php?videoid=" + urll + "&type=Download";
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                view.evaluateJavascript(
                        "(function() { return '<html>'+(document.getElementsByTagName('html')[0].innerHTML)+'</html>'; })();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String html) {
                                //Parsing HTML And Selcting Only The Required Fields Using JSOUP.
                                String mhtml = html.substring(1, html.length() - 1).replace("\\u003C", "<").replace("\\n", "").replace("\\t", "").replace("\\", "");
                                Document doc = Jsoup.parse(mhtml);
                                Elements uls = doc.body().getElementsByClass("dl-list");
                                final Element info = doc.body().getElementById("info");

                                int index = -1;
                                for (Element ul : uls) {
                                    Elements lis = ul.select("li");
                                    index++;//<ul>
                                    if (index == 0) {
                                        links.add(new LinkModel(null, null, null, null, null, Constants.AVAILABLE));
                                    }
                                    if (index == 1) {
                                        links.add(new LinkModel(null, null, null, null, null, Constants.SEPERATED));
                                    }
                                    for (Element li : lis) {
                                        Element type = li.getElementsByClass("btn btn-default btn-type").first();
                                        Element download = li.getElementsByClass("btn btn-primary btn-download").first();
                                        Element quality = li.getElementsByClass("label label-warning").first();
                                        String link1 = type.attr("href");
                                        String text1 = type.text();
                                        String link2 = "http://shouvikmitra.fanclub.rocks/utilities/" + download.attr("href");
                                        String text2 = download.text();
                                        String text3 = quality.text();
                                        links.add(new LinkModel(text1, link1, text2, link2, text3, Constants.NORMAL));
                                        System.out.println(links.get(links.size() - 1).toString());
                                    }

                                }

                                linksAdapter = new LinksAdapter(MainActivity.this, links);
                                //Just To Give It An Animated Feel
                                loader.animate().alpha(0.0f).setDuration(1000)
                                        .withStartAction(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (info != null) {
                                                    Element img = info.select("img").first();
                                                    String imgSrc = "http://shouvikmitra.fanclub.rocks/utilities/" + img.attr("src");
                                                    Element text = info.select("p").first();
                                                    String title = text.text();
                                                    ///Using Glide To Load The Image Async
                                                    Glide.with(getApplicationContext())
                                                            .load(imgSrc)
                                                            .into(imgThumbnail);
                                                    titleText.setText(title);
                                                }
                                                recyclerView.setAdapter(linksAdapter);
                                            }
                                        })
                                        .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        loader.setVisibility(View.GONE);
                                    }
                                }).start();
                                // code here
                            }
                        });
            }

        });
    }

}
