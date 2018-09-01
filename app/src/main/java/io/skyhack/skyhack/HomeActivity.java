package io.skyhack.skyhack;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Interpolator;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tomergoldst.tooltips.ToolTipsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {
    RelativeLayout logo_div,splash_cover;
    ImageView ico_splash,menu,search;
    TextView page_tag;
    Animator animator;
    CardView data_div;
    ObjectAnimator startAnim;
    Point screenSize;
    ToolTipsManager toolTip;
    RecyclerView display;
    List<Schemes> schemes;
    WebView apply;
    double diagonal;
    OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        diagonal=Math.sqrt((screenSize.x*screenSize.x) + (screenSize.y*screenSize.y));
        splash_cover=findViewById(R.id.splash_cover);
        logo_div=findViewById(R.id.logo_div);
        data_div=findViewById(R.id.data_div);
        toolTip = new ToolTipsManager();
        client = new OkHttpClient();

        page_tag=findViewById(R.id.page_tag);
        page_tag.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));

        ico_splash=findViewById(R.id.ico_splash);
        ico_splash.setScaleType(ImageView.ScaleType.CENTER);

        menu=findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        search=findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        schemes = new ArrayList<>();
        display=findViewById(R.id.display);
        prepareSchemes();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,1);
        display.setLayoutManager(mLayoutManager);
        display.addItemDecoration(new GridSpacingItemDecoration(1,dptopx(10),true));
        display.setItemAnimator(new DefaultItemAnimator());

        apply=findViewById(R.id.apply);
        WebSettings settings = apply.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setLoadsImagesAutomatically(true);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(HomeActivity.this.getCacheDir().getPath());
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setGeolocationEnabled(true);
        settings.setSaveFormData(true);
        settings.setDomStorageEnabled(true);
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android "+ Build.VERSION.RELEASE+"; "+Build.MODEL+" Build/"+Build.ID+") AppleWebKit/537.36 (KHTML, like Gecko) Chrome/ Mobile Safari/537.36");

        apply.setWebChromeClient(new WebChromeClient());
        apply.setScrollbarFadingEnabled(true);
        apply.setWebViewClient(new WebViewClient(){
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){ }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){handler.proceed();}
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {}
            @Override
            public void onPageFinished(WebView view, String url){ }
            @Override
            public void onLoadResource(WebView view, String url) {}
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) { return false; }

        });
        //apply.loadUrl("https://goo.gl/forms/zVE1Eg8xqin4WU5f2");

        if(getIntent().getBooleanExtra("isProfile",false))
        {
            splash_cover.setVisibility(View.GONE);
            logo_div.setVisibility(View.VISIBLE);
            page_tag.setVisibility(View.VISIBLE);
            menu.setVisibility(View.VISIBLE);
            search.setVisibility(View.VISIBLE);
            scaleY(data_div,getIntent().getIntExtra("divHeight",0),0,new AccelerateDecelerateInterpolator());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AlphaAnimation anims = new AlphaAnimation(0,1);anims.setDuration(1000);
                    display.setVisibility(View.VISIBLE);display.startAnimation(anims);
                }},800);
        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // SignUp Animation

                    splash_cover.setVisibility(View.GONE);
                    logo_div.setVisibility(View.VISIBLE);

                    float CurrentX = ico_splash.getX();
                    float CurrentY = ico_splash.getY();
                    float FinalX = 0;
                    float FinalY = 35;
                    Path path = new Path();
                    path.moveTo(CurrentX, CurrentY);
                    path.quadTo(CurrentX*4/3, (CurrentY+FinalY)/4, FinalX, FinalY);

                    startAnim = ObjectAnimator.ofFloat(ico_splash, View.X, View.Y, path);
                    startAnim.setDuration(800);
                    startAnim.setInterpolator(new AccelerateDecelerateInterpolator());

                    AlphaAnimation anims = new AlphaAnimation(0.6f,1);
                    anims.setDuration(1000);
                    anims.setFillAfter(true);
                    anims.setInterpolator(new AccelerateDecelerateInterpolator());

                    logo_div.startAnimation(anims);
                    startAnim.start();
                    ico_splash.animate().scaleX(0f).scaleY(0f).setDuration(1000).start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scaleY(data_div,pxtodp(splash_cover.getHeight())-90,800,new AccelerateDecelerateInterpolator());
                            AlphaAnimation anims = new AlphaAnimation(1,0);anims.setDuration(700);anims.setFillAfter(true);
                            ico_splash.startAnimation(anims);
                        }},10);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AlphaAnimation anims = new AlphaAnimation(0,1);anims.setDuration(400);
                            page_tag.setVisibility(View.VISIBLE);page_tag.startAnimation(anims);
                            menu.setVisibility(View.VISIBLE);menu.startAnimation(anims);
                            search.setVisibility(View.VISIBLE);search.startAnimation(anims);
                        }},400);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AlphaAnimation anims = new AlphaAnimation(0,1);anims.setDuration(1000);
                            display.setVisibility(View.VISIBLE);display.startAnimation(anims);
                        }},800);

                }},1500);
        }
    }
    public void prepareSchemes(){

//        String arr="Rashtriya Bal Swasthya Karyakram (RBSK) is a new initiative aiming at early identification  and  early  intervention  for  children  from  birth  to  18  years to cover 4 ‘D’s viz. Defects at  birth,  Deficiencies,  Diseases,  Development  delays  including  disability. The workers now need not to worry about the food after the launch of Mukhyamantri Shramik Ann Sahayta Yojana in the state. The unregistered labourers can get theme registered at the counters to be set-up near Gandhi Maidan in Raipur to avail the scheme benefits. The labour department of the state is already running 24 programmes for the welfare of labourers in unorganized sector in the state. Mukhyamantri Shramik Ann Sahayta Yojana will soon be launched in other districts and towns of the state very soon. This is the first of its kind free lunch scheme in the entire country.";
//        schemes.add(new Schemes("Rashtriya Bal Swasthya Karyakram (RBSK)","10/09/2018", arr, BitmapFactory.decodeResource(getResources(), R.mipmap.rbsk)));
//        String arr1="Jal Kranti Abhiyan aims at turning one water scared village in each district of the country into water surplus village water through a holistic and in The unorganized workers not just work for their livelihood but they also help build the nation. Activities proposed under the campaign include rain water harvesting, recycling of waste water, micro irrigation for using water efficiently and mass awareness program. Along with it, a cadre of local water professional Jal Mitra will be created and they will be given training to create mass awareness";
//        schemes.add(new Schemes("Jal Kranti Abhiyan (JKA)","20/09/2018",arr1,BitmapFactory.decodeResource(getResources(), R.mipmap.jka)));
//        String arr2="Chhattisgarh state government has launched a new scheme in the state to facilitate people with Smartphone. To remove the digital inequality among the inhabitants, the state government has decided to distribute Smartphones among the citizens of the state for free. The scheme namely Sanchar Kranti Yojana aka SKY will be implemented soon across the state. The aim of this scheme is to provide free smartphones for the poor citizens and increase the mobile connectivity in the state. The government will distribute smartphones in two phases under Sanchar Kranti Yojana";
//        schemes.add(new Schemes("Sanchar Kranti Yojana (SKY)","30/09/2018",arr2, BitmapFactory.decodeResource(getResources(), R.mipmap.sky)));
//        String arr3 = "The state government has launched the free lunch scheme for the registered labourers in the unorganized sector in the state. The scheme will cover all the registered unorganized workers including the ones working in construction sector. The labour department of the state would provide food for free of cost to the labourers. As the scheme name suggests, this scheme has been launched by the state govt. of Chhattisgarh to improve the living and working conditions of the labourers who works in the state. This scheme will benefit the working class of people earning their wage on daily or contract basis";
//        schemes.add(new Schemes("Mukhyamantri Shramik Ann Sahaya Yojana (MSASY)","15/10/2018",arr3, BitmapFactory.decodeResource(getResources(), R.mipmap.msasy)));

        Request request = new Request.Builder().url("https://nodeexercise-adityabhardwaj.c9users.io/schemes").get()
                .addHeader("Content-Type", "application/json").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.w("failure", e.getMessage());
                call.cancel();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String mMessage = Objects.requireNonNull(response.body()).string();
                if (response.isSuccessful()){
                    try {
                        JSONArray postsArray = new JSONArray(mMessage);
                        for (int i = 0; i < postsArray.length(); i++) {
                            JSONObject pO = postsArray.getJSONObject(i);
                            schemes.add(new Schemes(pO.getString("name"),pO.getString("endDate"),pO.getString("description"), BitmapFactory.decodeResource(getResources(), R.mipmap.rbsk)));
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                display.setAdapter(new SchemeAdapter(HomeActivity.this,schemes));
                            }
                        });
                    }
                    catch (JSONException e) {
                        Log.w("error", e.toString());
                    }
                }
            }
        });
    }
    public void scaleX(final View view,int x,int t, Interpolator interpolator)
    {
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredWidth(),dptopx(x));anim.setInterpolator(interpolator);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = (Integer) valueAnimator.getAnimatedValue();
                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(t);anim.start();
    }
    public void scaleY(final View view,int y,int t, Interpolator interpolator)
    {
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeight(),dptopx(y));anim.setInterpolator(interpolator);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = (Integer) valueAnimator.getAnimatedValue();
                view.setLayoutParams(layoutParams);view.invalidate();
            }
        });
        anim.setDuration(t);anim.start();
    }
    public int dptopx(float dp)
    {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    public int pxtodp(float px)
    {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    public void vibrate(int ms)
    {
        ((Vibrator) Objects.requireNonNull(this.getSystemService(Context.VIBRATOR_SERVICE))).vibrate(ms);
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;
        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;
            if (includeEdge){
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;
                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }
}
