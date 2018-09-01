package io.skyhack.skyhack;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.cameraview.CameraView;
import com.google.android.cameraview.CameraViewImpl;
import com.google.android.flexbox.FlexboxLayout;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.rm.rmswitch.RMSwitch;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.maxHeight;
import static android.R.attr.maxWidth;

public class ProfileActivity extends AppCompatActivity {
    RelativeLayout logo_div,splash_cover,camera_pane,permission_camera,galary,click_pane,profile_menu_cov,interestPage;
    CardView data_div;
    ImageView ico_splash,done,camera_flip,click,flash;
    Button allow_camera;
    Animation anim;
    TextView page_tag,gender_tag,interest_button;
    EditText f_name,l_name,dob,aadhaar,occ;
    RMSwitch gender;
    Point screenSize;
    Animator animator;
    ObjectAnimator startAnim;
    CameraView cameraView;
    UCrop.Options options;
    CircularImageView profile;
    boolean profile_lp=false,camOn=false,galaryOn=false,isDP_added=false;
    String profile_url="",profile_path="";
    ProgressBar loading_profile;
    ToolTipsManager toolTip;
    Bitmap profile_dp=null;
    double diagonal;
    FlexboxLayout interest;
    ChipCloud inter;
    OkHttpClient client;
    boolean tags[]={false,false,false,false,false,false,false,false,false,false,false,false,false};
    int interestX=0;
    @Override
    protected void onPause() {
        super.onPause();
        if(camera_pane.getVisibility()== View.VISIBLE)
        {
            click.setVisibility(View.GONE);
            if(cameraView.isCameraOpened()){cameraView.stop();}
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(camera_pane.getVisibility()== View.VISIBLE)
        {
            if(checkPerm() && !cameraView.isCameraOpened()){cameraView.start();}
            new Handler().postDelayed(new Runnable() {@Override public void run()
            {
                click.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.click_grow);click.startAnimation(anim);
            }},500);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        diagonal=Math.sqrt((screenSize.x*screenSize.x) + (screenSize.y*screenSize.y));
        splash_cover=findViewById(R.id.splash_cover);
        logo_div=findViewById(R.id.logo_div);
        data_div=findViewById(R.id.data_div);
        profile_menu_cov=findViewById(R.id.profile_menu_cov);
        toolTip = new ToolTipsManager();
        client = new OkHttpClient();

        gender_tag=findViewById(R.id.gender_tag);
        gender_tag.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/above.ttf"));

        gender=findViewById(R.id.gender);
        gender.addSwitchObserver(new RMSwitch.RMSwitchObserver() {
            @Override
            public void onCheckStateChange(RMSwitch switchView, boolean isChecked) {
                if(isChecked){
                    gender_tag.setText(R.string.male);
                }
                else{
                    gender_tag.setText(R.string.female);
                }
            }
        });


        f_name=findViewById(R.id.f_name);
        f_name.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));

        l_name=findViewById(R.id.l_name);
        l_name.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));

        dob=findViewById(R.id.dob);
        dob.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));

        occ=findViewById(R.id.occ);
        occ.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));

        interestPage=findViewById(R.id.interestPage);
        interest_button=findViewById(R.id.interest_button);
        interest_button.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));
        interest_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        interest_button.setBackgroundResource(R.drawable.interest_pressed);interest_button.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    case MotionEvent.ACTION_UP:
                        interest_button.setBackgroundResource(R.drawable.interest);interest_button.setTextColor(Color.parseColor("#ff611c"));
                        vibrate(20);
                        if(interest_button.getText().equals(getString(R.string.interest))){
                            interestX=interest_button.getWidth();
                            selectInterest(true);
                        }
                        else {
                            selectInterest(false);
                        }
                        break;
                }
                return true;
            }
        });

        aadhaar=findViewById(R.id.aadhaar);
        aadhaar.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));

        page_tag=findViewById(R.id.page_tag);
        page_tag.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));

        ico_splash=findViewById(R.id.ico_splash);
        ico_splash.setScaleType(ImageView.ScaleType.CENTER);

        click_pane=findViewById(R.id.click_pane);
        galary= findViewById(R.id.galary);

        camera_pane=findViewById(R.id.camera_pane);
        loading_profile= findViewById(R.id.loading_profile);
        loading_profile.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.progress), PorterDuff.Mode.MULTIPLY);

        done=findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProfile();
                /*Intent home=new Intent(ProfileActivity.this,HomeActivity.class);
                home.putExtra("isProfile",true);
                home.putExtra("divHeight",pxtodp(data_div.getHeight()));
                ProfileActivity.this.startActivity(home);
                finish();
                ProfileActivity.this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);*/
            }
        });
        profile=findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profile_lp) {profile_lp=false;}
                else
                {
                    vibrate(20);
                    camera_pane.setVisibility(View.VISIBLE);
                    permission_camera.setVisibility(View.VISIBLE);
                    camOn=true;
                    final Animator animator = ViewAnimationUtils.createCircularReveal(camera_pane,dptopx(98),dptopx(260),0, (float)diagonal);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());animator.setDuration(500);animator.start();
                    if (checkPerm()) {
                        permission_camera.setVisibility(View.GONE);if(!cameraView.isCameraOpened()){cameraView.start();}
                    }
                    new Handler().postDelayed(new Runnable() {@Override public void run()
                    {
                        click.setVisibility(View.VISIBLE);
                        click.startAnimation(AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.click_grow));
                    }},500);
                    if(checkPerm())
                    {
                        new Handler().postDelayed(new Runnable() {@Override public void run()
                        {
                            ToolTip.Builder builder = new ToolTip.Builder(ProfileActivity.this, click,camera_pane, getString(R.string.open_galary), ToolTip.POSITION_ABOVE);
                            builder.setBackgroundColor(getResources().getColor(R.color.profile));
                            builder.setTextColor(getResources().getColor(R.color.profile_text));
                            builder.setGravity(ToolTip.GRAVITY_CENTER);
                            builder.setTextSize(15);
                            toolTip.show(builder.build());
                        }},1300);
                        new Handler().postDelayed(new Runnable() {@Override public void run() {toolTip.findAndDismiss(click);}},4000);
                    }
                }
            }
        });
        profile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                vibrate(35);
                return false;
            }
        });

        options=new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setShowCropFrame(false);
        options.setCropGridColumnCount(0);
        options.setCropGridRowCount(0);
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));

        cameraView=findViewById(R.id.cam);
        cameraView.setOnTurnCameraFailListener(new CameraViewImpl.OnTurnCameraFailListener() {
            @Override
            public void onTurnCameraFail(Exception e) {
                Toast.makeText(ProfileActivity.this, "Switch Camera Failed. Does you device has a front camera?",
                        Toast.LENGTH_SHORT).show();
            }
        });
        cameraView.setOnCameraErrorListener(new CameraViewImpl.OnCameraErrorListener() {
            @Override
            public void onCameraError(Exception e) {
                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        cameraView.setOnPictureTakenListener(new CameraViewImpl.OnPictureTakenListener() {
            @Override
            public void onPictureTaken(Bitmap result, int rotationDegrees) {
                if(cameraView.getFacing()!= CameraView.FACING_BACK)
                {
                    Matrix matrix = new Matrix();
                    matrix.postScale(-1, 1,result.getWidth()/2, result.getHeight()/2);
                    result= Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
                }
                vibrate(20);
                profile_path = MediaStore.Images.Media.insertImage(ProfileActivity.this.getContentResolver(), result, "Title", null);
                UCrop.of(Uri.parse(profile_path),Uri.parse(profile_url)).withOptions(options).withAspectRatio(1,1)
                        .withMaxResultSize(maxWidth, maxHeight).start(ProfileActivity.this);
            }
        });

        flash=findViewById(R.id.flash);
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate(20);
                if(cameraView.getFlash()!=CameraView.FLASH_ON) {
                    cameraView.setFlash(CameraView.FLASH_ON);
                    flash.setImageResource(R.drawable.flash_on);
                }
                else {
                    cameraView.setFlash(CameraView.FLASH_OFF);
                    flash.setImageResource(R.drawable.flash_off);
                }
                Toast.makeText(ProfileActivity.this, cameraView.getFlash()+"", Toast.LENGTH_SHORT).show();
            }
        });
        camera_flip=findViewById(R.id.camera_flip);
        camera_flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate(20);
                cameraView.switchCamera();
            }
        });
        click=findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cameraView.isCameraOpened()){
                    cameraView.takePicture();
                }
            }
        });
        click.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int cx=screenSize.x/2;
                int cy=screenSize.y-((int)(click.getY()));
                galaryOn=true;vibrate(35);
                animator = ViewAnimationUtils.createCircularReveal(galary,cx,cy,0,(float) diagonal);
                animator.setInterpolator(new AccelerateInterpolator());animator.setDuration(300);galary.setVisibility(View.VISIBLE);
                galary.startAnimation(AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.fade_out));
                animator.start();
                Intent intent = new Intent();
                intent.setType("image/*");intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                overridePendingTransition(R.anim.fade_in,0);
                cameraView.stop();
                return false;
            }
        });

        permission_camera=findViewById(R.id.permission_camera) ;
        allow_camera =findViewById(R.id.allow_camera);
        allow_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate(20);
                ActivityCompat.requestPermissions(ProfileActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        });

        interest = findViewById(R.id.interest);
        interest.setVisibility(View.GONE);
        ChipCloudConfig config = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.multi)
                .checkedChipColor(getResources().getColor(R.color.colorAccent))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(Color.parseColor("#efefef"))
                .uncheckedTextColor(Color.parseColor("#666666"))
                .useInsetPadding(true)
                .typeface(Typeface.createFromAsset(getAssets(), "fonts/above.ttf"));
        inter = new ChipCloud(ProfileActivity.this, interest,config);
        inter.addChip("Farming");
        inter.addChip("Dairy");
        inter.addChip("Livestock");
        inter.addChip("Sports");
        inter.addChip("Engineering");
        inter.addChip("Transportation");
        inter.addChip("Banking");
        inter.addChip("IT Services");
        inter.addChip("Manufacturing");
        inter.addChip("Education");
        inter.addChip("Construction");
        inter.addChip("Health Care");
        inter.addChip("Armed Forces");
        inter.setListener(new ChipListener() {
            @Override
            public void chipCheckedChange(int i, boolean b, boolean b1) {
                tags[i]=b;
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // SignUp Animation

                splash_cover.setVisibility(View.GONE);
                logo_div.setVisibility(View.VISIBLE);

                float CurrentX = ico_splash.getX();
                float CurrentY = ico_splash.getY();
                float FinalX = -20;
                float FinalY = 50;
                Path path = new Path();
                path.moveTo(CurrentX, CurrentY);
                path.quadTo(CurrentX*4/3, (CurrentY+FinalY)/4, FinalX, FinalY);

                startAnim = ObjectAnimator.ofFloat(ico_splash, View.X, View.Y, path);
                startAnim.setDuration(800);
                startAnim.setInterpolator(new AccelerateDecelerateInterpolator());

                AlphaAnimation anims = new AlphaAnimation(0.6f,1);
                anims.setDuration(1000);
                anims.setInterpolator(new AccelerateDecelerateInterpolator());

                logo_div.startAnimation(anims);
                startAnim.start();
                ico_splash.animate().scaleX(0.4f).scaleY(0.4f).setDuration(1000).start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scaleY(data_div,pxtodp(splash_cover.getHeight())-95,800,new AccelerateDecelerateInterpolator());
                    }},10);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AlphaAnimation anims = new AlphaAnimation(0,1);
                        anims.setDuration(600);
                        page_tag.setVisibility(View.VISIBLE);page_tag.startAnimation(anims);
                        done.setVisibility(View.VISIBLE);done.startAnimation(anims);
                    }},500);

            }},1500);
    }
    public void createProfile(){
        int tag=0;
        FormBody.Builder postBody = new FormBody.Builder()
                .add("email", getIntent().getStringExtra("email")+"")
                .add("firstname", f_name.getText().toString()+"")
                .add("lastname", l_name.getText().toString()+"")
                .add("gender", gender_tag.getText().toString()+"")
                .add("dob", dob.getText().toString()+"")
                .add("aadhar", aadhaar.getText().toString()+"")
                .add("occupation", occ.getText().toString()+"");
        for(int i=0;i<tags.length;i++){
            if(tags[i])
            postBody.add("tags["+(tag++)+"]", ""+inter.getLabel(i));
        }
        RequestBody formBody = postBody.build();


        Log.i("sign",postBody.toString());
        Request request = new Request.Builder().url("https://nodeexercise-adityabhardwaj.c9users.io/update").post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i("sign", e.getMessage());
                call.cancel();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                if(Integer.parseInt(Objects.requireNonNull(response.body()).string())==1 && response.isSuccessful()){

                }
                else{
                }
            }
        });
    }
    public void selectInterest(boolean open){
        if(open){
            scaleX(interestPage,pxtodp(f_name.getWidth()),300,new AccelerateDecelerateInterpolator());
            scaleY(interestPage,310,300,new AccelerateDecelerateInterpolator());
            interestPage.animate().translationY(-dptopx(10)).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(50).start();
            interest_button.animate().translationY(dptopx(260)).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(300).start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    interest_button.setText(R.string.interest_done);
                    interest.setVisibility(View.VISIBLE);
                }},100);
        }
        else {
            scaleX(interestPage,pxtodp(interestX),400,new AccelerateDecelerateInterpolator());
            scaleY(interestPage,pxtodp(interest_button.getHeight()),400,new AccelerateDecelerateInterpolator());
            interestPage.animate().translationY(0).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(400).start();
            interest_button.animate().translationY(dptopx(0)).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(400).start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    interest_button.setText(R.string.interest);
                    interest.setVisibility(View.GONE);
                }},100);
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

    @Override
    protected void onActivityResult(int requestCode, int resultcode, Intent intent) {
        super.onActivityResult(requestCode, resultcode, intent);
        if (requestCode == 1 && resultcode == RESULT_OK) {
            UCrop.of(Objects.requireNonNull(intent.getData()),Uri.parse(profile_url)).withOptions(options).withAspectRatio(1,1)
                    .withMaxResultSize(maxWidth, maxHeight).start(ProfileActivity.this);
        }
        if (requestCode == UCrop.REQUEST_CROP) {
            if(resultcode == RESULT_OK)
            {
                try {
                    final Uri resultUri = UCrop.getOutput(intent);
                    Bitmap bitmap= MediaStore.Images.Media.getBitmap(ProfileActivity.this.getContentResolver(), resultUri);
                    profile.setImageBitmap(bitmap);profile_dp=bitmap;isDP_added=true;
                    closeCam();
                    new Handler().postDelayed(new Runnable() {@Override public void run()
                    {
                        ToolTip.Builder builder = new ToolTip.Builder(ProfileActivity.this, profile,data_div, getString(R.string.remove_pic), ToolTip.POSITION_ABOVE);
                        builder.setBackgroundColor(getResources().getColor(R.color.profile));
                        builder.setTextColor(getResources().getColor(R.color.profile_text));
                        builder.setGravity(ToolTip.GRAVITY_CENTER);
                        builder.setTextSize(15);
                        toolTip.show(builder.build());vibrate(35);
                    }},1000);
                    new Handler().postDelayed(new Runnable() {@Override public void run() {toolTip.findAndDismiss(profile);}},3500);
                    new File(getRealPathFromURI(ProfileActivity.this,Uri.parse(profile_path))).delete();
                }
                catch (Exception e){}
            }
            else if (resultcode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(intent);
                Toast.makeText(ProfileActivity.this,getString(R.string.error)+cropError, Toast.LENGTH_LONG).show();
                new File(getRealPathFromURI(ProfileActivity.this,Uri.parse(profile_path))).delete();
            }
        }
    }
    public void closeCam()
    {
        int cy=(int)(profile.getY() + profile.getHeight() / 2);
        Animation anim = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.click_shrink);click.startAnimation(anim);
        animator = ViewAnimationUtils.createCircularReveal(camera_pane,ico_splash.getRight()/2,cy, ico_splash.getHeight()*141/100,0);
        animator.setInterpolator(new DecelerateInterpolator());animator.setDuration(500);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {camOn=false;click.setVisibility(View.GONE);}
            @Override
            public void onAnimationEnd(Animator animation) {camera_pane.setVisibility(View.GONE);click.setVisibility(View.GONE);}
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        new Handler().postDelayed(new Runnable() {@Override public void run() {animator.start();}},300);

    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(checkPerm())
                    {
                        permission_camera.setVisibility(View.GONE);
                        if(!cameraView.isCameraOpened()){cameraView.start();}
                        new Handler().postDelayed(new Runnable() {@Override public void run()
                        {
                            ToolTip.Builder builder = new ToolTip.Builder(ProfileActivity.this, click,camera_pane, getString(R.string.open_galary), ToolTip.POSITION_ABOVE);
                            builder.setBackgroundColor(getResources().getColor(R.color.profile));
                            builder.setTextColor(getResources().getColor(R.color.profile_text));
                            builder.setGravity(ToolTip.GRAVITY_CENTER);
                            builder.setTextSize(15);
                            toolTip.show(builder.build());
                        }},1300);
                        new Handler().postDelayed(new Runnable() {@Override public void run() {toolTip.findAndDismiss(click);}},4000);
                    }
                }
            }
        }
    }
    public boolean checkPerm(){
        return (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }
}
