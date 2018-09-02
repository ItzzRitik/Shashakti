package io.skyhack.skyhack;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SchemeActivity extends AppCompatActivity {

    RelativeLayout splash_cover;
    ImageView back;
    TextView head,details,apply;
    ProgressBar nextLoad;
    OkHttpClient client;
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fade_in,0);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        splash_cover=findViewById(R.id.splash_cover);
        back=findViewById(R.id.back);
        nextLoad=findViewById(R.id.nextLoad);
        head=findViewById(R.id.head);
        head.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2_bold.otf"));
        head.setText(getIntent().getStringExtra("name"));

        details=findViewById(R.id.details);
        details.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));
        details.setText(getIntent().getStringExtra("details"));

        apply=findViewById(R.id.apply);
        apply.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextLoading(true);
                RequestBody postBody = new FormBody.Builder()
                        .add("email", getIntent().getStringExtra("email"))
                        .add("aadhar", getIntent().getStringExtra("aadhaar"))
                        .add("name", getIntent().getStringExtra("name")).build();
                Log.i("apply",getIntent().getStringExtra("email"));
                Request request = new Request.Builder().url("https://nodeexercise-adityabhardwaj.c9users.io/apply").post(postBody).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.i("sign", e.getMessage());
                        call.cancel();
                    }
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                        if(response.isSuccessful() && Integer.parseInt(Objects.requireNonNull(response.body()).string())==1){
                            Log.i("apply","Application Successful");
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            nextLoading(false);
                                        }},800);
                                }
                            });
                        }
                        else {
                            Log.i("apply","Application Failed - "+(Objects.requireNonNull(response.body())).string());
                        }
                    }
                });
            }
        });

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in,0);
            }
        });
        client = new OkHttpClient();
        splash_cover.setVisibility(View.VISIBLE);
        viewed(getIntent().getStringExtra("name"));
    }
    public void viewed(String name){
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://nodeexercise-adityabhardwaj.c9users.io/schemeDetail").newBuilder();
        urlBuilder.addQueryParameter("name",name);
        Request request = new Request.Builder().url(urlBuilder.build().toString()).get()
                .addHeader("Content-Type", "application/json").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i("failure", e.getMessage());
                call.cancel();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String mMessage = Objects.requireNonNull(response.body()).string();
                if (response.isSuccessful()){
                    Log.i("success", mMessage);
                }
            }
        });
    }
    public void nextLoading(Boolean loading)
    {
        if(loading)
        {
            apply.setText("");
            scaleX(apply,35,150,new AnticipateInterpolator());
            apply.setBackgroundResource(R.drawable.signin_disabled);
            apply.setTextColor(Color.parseColor("#616161"));
            new Handler().postDelayed(new Runnable() {@Override public void run() {
                nextLoad.setVisibility(View.VISIBLE);apply.setText("╳");
            }},150);
        }
        else
        {
            nextLoad.setVisibility(View.GONE);apply.setText("");
            scaleX(apply,120,300,new OvershootInterpolator());
            apply.setBackgroundResource(R.drawable.signin_pressed);
            apply.setTextColor(Color.parseColor("#ffffff"));
            new Handler().postDelayed(new Runnable()
            {@Override public void run() {apply.setText("Done");}},150);
        }
    }
    public void scaleX(final View view,int x,int t, Interpolator interpolator)
    {
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredWidth(),(int)dptopx(x));anim.setInterpolator(interpolator);
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
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeight(),(int)dptopx(y));anim.setInterpolator(interpolator);
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
    public float dptopx(float num)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, num, getResources().getDisplayMetrics());
    }
}
