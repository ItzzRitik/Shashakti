package io.skyhack.skyhack;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    Animation anim;
    RelativeLayout login_div,social_div,logo_div,splash_cover,forget_pass,email_reset;
    ImageView ico_splash;
    TextView signin,forget_create;
    EditText email,pass,con_pass;
    int log=0;
    String buttonText="NEXT";
    ProgressBar nextLoad;
    @Override
    public void onBackPressed() {
        showKeyboard(email,false);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        splash_cover=findViewById(R.id.splash_cover);
        ico_splash=findViewById(R.id.ico_splash);
        logo_div=findViewById(R.id.logo_div);
        login_div=findViewById(R.id.login_div);
        social_div=findViewById(R.id.social_div);

        forget_create=findViewById(R.id.forget_create);
        forget_create.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));

        email=findViewById(R.id.email);
        email.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(isEmailValid(email.getText().toString()))
                {setButtonEnabled(true);}
                else{setButtonEnabled(false);}
            }
        });
        email.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if(log==0 &&isEmailValid(email.getText().toString())){performSignIn();}
                            return true;
                        default:break;
                    }
                }
                return false;
            }
        });
        email.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                newPageAnim();
                new Handler().postDelayed(new Runnable() {@Override public void run() {
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    finish();
                    LoginActivity.this.overridePendingTransition(0, 0);}},1000);
                return false;
            }
        });

        pass=findViewById(R.id.pass);
        pass.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                con_pass.setText("");
                if(pass.getText().length()>=6)
                {
                    pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_ok,0,0,0);
                    if(log==1){setButtonEnabled(true);}
                    else if(log==2){con_pass.setEnabled(true);}
                }
                else
                {
                    pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_nok,0,0,0);
                    if(log==1){setButtonEnabled(false);}
                    else if(log==2){con_pass.setText("");con_pass.setEnabled(false);}
                }
            }
        });
        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        con_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        pass.setSelection(pass.getText().length());
                        break;
                    case MotionEvent.ACTION_UP:
                        pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        con_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        pass.setSelection(pass.getText().length());
                        break;
                }
                return false;
            }
        });
        pass.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if(log==1)
                            {performSignIn();}
                            return true;
                        default:break;
                    }
                }
                return false;
            }
        });

        con_pass=findViewById(R.id.con_pass);
        con_pass.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));
        con_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(log==2)
                {
                    if(con_pass.getText().toString().equals(pass.getText().toString()) && con_pass.getText().length()>=6)
                    {con_pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.con_password_ok,0,0,0);setButtonEnabled(true);}
                    else{con_pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.con_password_nok,0,0,0);setButtonEnabled(false);}
                }
            }
        });
        con_pass.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if(log==2) {performSignIn();}
                            return true;
                        default:break;
                    }
                }
                return false;
            }
        });

        forget_pass = findViewById(R.id.forget_pass);
        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate(20);
            }
        });
        email_reset=findViewById(R.id.email_reset);
        email_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleY(social_div,80,300,new AccelerateDecelerateInterpolator());
                scaleY(login_div,48,300,new AccelerateDecelerateInterpolator());
                scaleY(forget_pass,0,300,new AccelerateDecelerateInterpolator());

                nextPad( 5,2);
                login_div.setPadding(0,(int)(10 * getResources().getDisplayMetrics().density),0,0);

                email_reset.setVisibility(View.GONE);email.setEnabled(true);
                pass.setText("");con_pass.setText("");
                signin.setText(getString(R.string.next));
                setButtonEnabled(true);vibrate(20);
                email.setVisibility(View.VISIBLE);
                pass.setVisibility(View.GONE);
                con_pass.setVisibility(View.GONE);
                log=0;

            }
        });


        signin=findViewById(R.id.signin);
        nextLoad=findViewById(R.id.nextLoad);
        signin.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/vdub.ttf"));
        signin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        signin.setBackgroundResource(R.drawable.signin_pressed);signin.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    case MotionEvent.ACTION_UP:
                        signin.setBackgroundResource(R.drawable.signin);signin.setTextColor(Color.parseColor("#ff611c"));
                        vibrate(20);
                        email.setEnabled(false);
                        performSignIn();
                        break;
                }
                return true;
            }
        });

        setButtonEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Splash Animation
                anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_trans);
                splash_cover.setVisibility(View.GONE);
                Animation anima = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_reveal);
                logo_div.setVisibility(View.VISIBLE);logo_div.startAnimation(anima);ico_splash.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {@Override public void run() {
                    new Handler().postDelayed(new Runnable() {@Override public void run() {
                        scaleY(login_div,48,400,new OvershootInterpolator());}},200);
                    scaleY(social_div,80,280,new AccelerateInterpolator());
                }},800);
            }},1500);
    }
    public void performSignIn()
    {
        if(log==0)
        {
            //Check server for existing account
            if((Math.random() <= 0.5))
            {
                //If Exists then ask password
                Log.e("", "SignIN");
                nextLoading(true);
                new Handler().postDelayed(new Runnable() {@Override public void run() {
                    scaleY(social_div,0,300,new AccelerateDecelerateInterpolator());
                    login_div.setPadding(0,0,0,0);
                    nextPad(8,4);
                    nextLoading(false);

                    //Ask Password
                    pass.setVisibility(View.VISIBLE);
                    con_pass.setVisibility(View.GONE);
                    email_reset.setVisibility(View.VISIBLE);
                    pass.requestFocus();
                    pass.setEnabled(true);
                    setButtonEnabled(false);
                    forget_create.setTextSize(13);
                    forget_create.setText(getResources().getString(R.string.forgot_pass));
                    scaleY(forget_pass,27,300,new OvershootInterpolator());
                    scaleY(login_div,98,300,new AccelerateDecelerateInterpolator());
                    log=1;
                }},1500);
            }
            else
            {
                //If Doesn't exist then ask signup
                Log.e("", "SignUP");
                nextLoading(true);
                new Handler().postDelayed(new Runnable() {@Override public void run() {
                    scaleY(social_div,0,300,new AccelerateDecelerateInterpolator());
                    login_div.setPadding(0,0,0,0);
                    nextPad(8,4);
                    nextLoading(false);

                    //Ask SignUp Details
                    pass.setVisibility(View.VISIBLE);
                    con_pass.setVisibility(View.VISIBLE);
                    email_reset.setVisibility(View.VISIBLE);
                    pass.requestFocus();
                    pass.setEnabled(true);
                    setButtonEnabled(false);
                    forget_create.setTextSize(14);
                    forget_create.setText(getResources().getString(R.string.login_create));
                    scaleY(forget_pass,30,300,new OvershootInterpolator());
                    scaleY(login_div,148,300,new AccelerateDecelerateInterpolator());
                    log=2;
                }},1500);
            }
        }
        else if(log==1)
        {
            //SignIn Initiate
            nextLoading(true);
            new Handler().postDelayed(new Runnable() {@Override public void run() {
                newPageAnim();nextLoading(false);}},1500);
            new Handler().postDelayed(new Runnable() {@Override public void run() {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                finish();
                LoginActivity.this.overridePendingTransition(0, 0);}},2500);
        }
        else if(log==2)
        {
            //SignUp Initiate
            nextLoading(true);
            new Handler().postDelayed(new Runnable() {@Override public void run() {
                newPageAnim();nextLoading(false);}},1500);
            new Handler().postDelayed(new Runnable() {@Override public void run() {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                finish();
                LoginActivity.this.overridePendingTransition(0, 0);}},2500);
        }
    }
    public void nextLoading(Boolean loading)
    {
        if(loading)
        {
            buttonText=signin.getText().toString();signin.setText("");
            scaleX(signin,30,150,new AnticipateInterpolator());
            signin.setBackgroundResource(R.drawable.signin_disabled);
            signin.setTextColor(Color.parseColor("#616161"));
            new Handler().postDelayed(new Runnable() {@Override public void run() {
                nextLoad.setVisibility(View.VISIBLE);signin.setText("╳");
            }},150);
        }
        else
        {
            nextLoad.setVisibility(View.GONE);signin.setText("");
            scaleX(signin,85,300,new OvershootInterpolator());
            new Handler().postDelayed(new Runnable()
            {@Override public void run() {signin.setText(buttonText);}},300);
        }
    }
    public void setButtonEnabled(Boolean what)
    {
        if(what) {
            signin.setBackgroundResource(R.drawable.signin);
            signin.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        else {
            signin.setBackgroundResource(R.drawable.signin_disabled);
            signin.setTextColor(getResources().getColor(R.color.disabled));
        }
        signin.setEnabled(what);
    }
    public static boolean isEmailValid(String emailStr)
    {
        return Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE) .matcher(emailStr).find();
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
    public void vibrate(int ms)
    {
        ((Vibrator) Objects.requireNonNull(this.getSystemService(Context.VIBRATOR_SERVICE))).vibrate(ms);
    }
    public void nextPad(int button,int loader){
        button = (int)(button * getResources().getDisplayMetrics().density);
        loader = (int)(loader * getResources().getDisplayMetrics().density);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) signin.getLayoutParams();
        lp.setMargins(0, 0, 0, button);
        signin.setLayoutParams(lp);
        lp = (RelativeLayout.LayoutParams) nextLoad.getLayoutParams();
        lp.setMargins(0, 0, (int)(-4.5 * getResources().getDisplayMetrics().density), loader);
        nextLoad.setLayoutParams(lp);
    }
    public void newPageAnim()
    {
        scaleY(social_div,0,300,new AccelerateDecelerateInterpolator());
        scaleY(login_div,0,300,new AccelerateDecelerateInterpolator());
        scaleY(forget_pass,0,300,new AccelerateDecelerateInterpolator());
        logo_div.setVisibility(View.VISIBLE);
        logo_div.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_hide));
        ico_splash.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_grow));
    }
    public void showKeyboard(View view,boolean what)
    {
        if(what)
        {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(),InputMethodManager.SHOW_FORCED, 0);
        }
        else
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
