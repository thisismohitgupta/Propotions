package com.socketmill.thisismohit.propotions;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


import org.json.JSONObject;

import java.util.Arrays;


public class Login extends AppCompatActivity {

    CallbackManager callbackManager;
    String email;
    ProgressBar progressBar;
    //com.socketmill.thisismohit.propotions.widget.GifMovieView gifMovieView;

    VideoView videoView;
    @Override
    protected void onStart() {
        super.onStart();
        toolbarNav NavController = new toolbarNav();
        NavController.checkIfLoggedIn(getApplicationContext(), MainActivity.class);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();


        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        progressBar = (ProgressBar) findViewById(R.id.marker_progress_login);
        if (progressBar.getVisibility() != View.VISIBLE) {
            Toast.makeText(getApplicationContext(), "its not visible ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "its visible ", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });


        String uilPath = "android.resource://" + getPackageName() + "/" + R.raw.untitled;
        videoView.setVideoURI(Uri.parse(uilPath));
        videoView.start();

        //gifMovieView = (com.socketmill.thisismohit.propotions.widget.GifMovieView) findViewById(R.id.gifMovieView);
        //gifMovieView.setMinimumWidth(getApplicationContext().getResources().getDisplayMetrics().widthPixels);
        //gifMovieView.setMinimumHeight(getApplicationContext().getResources().getDisplayMetrics().heightPixels);
        toolbarNav NavController = new toolbarNav();
        NavController.checkIfLoggedIn(getApplicationContext(), MainActivity.class);

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("user_friends", "email", "read_custom_friendlists"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(final LoginResult loginResult) {
                // App code

                final AccessToken accessToken = loginResult.getAccessToken();
                final Profile profile = Profile.getCurrentProfile();


                Toast.makeText(getApplicationContext(), "Login with facebook done next step initiated 1", Toast.LENGTH_SHORT).show();

                //Toast.makeText(getApplicationContext(),accessToken.getToken(), Toast.LENGTH_LONG).show();
                if (profile == null) {


                } else {

                    final String email2;
                    GraphRequest request = GraphRequest.newMeRequest(
                            accessToken,
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    // Application code
                                    Log.v("LoginActivity", response.toString());

                                    try {
                                        JSONObject graphObject = response.getJSONObject();


                                        email = graphObject.getString("email");


                                        Log.e("JSON", email);

                                        SendFeedBackJob newJob = new SendFeedBackJob(getApplicationContext());
                                        // newJob.activity = getParent() ;
                                        newJob.progressBar = progressBar;


                                        newJob.EMAIL = email;
                                        newJob.AccessToken = accessToken.getToken();

                                        newJob.invisible = View.INVISIBLE;

                                        newJob.show = View.VISIBLE;
                                        //newJob.context = getApplicationContext() ;


                                        Toast.makeText(getApplicationContext(), "Login with facebook done next step initiated", Toast.LENGTH_SHORT).show();

                                        newJob.execute();
                                        //login(profile.getId(), (profile.getId() + profile.getId()));
                                        //loginSuccessful();


                                    } catch (Exception e) {

//                                        Log.e("JSON", e.getMessage());


                                    }


                                }
                            });


                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "email");
                    request.setParameters(parameters);
                    request.executeAsync();


                }


            }

            @Override
            public void onCancel() {


                // App code
            }

            @Override
            public void onError(FacebookException exception) {


                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void login(String lowerCase, String password) {
        // TODO Auto-generated method stub
        ParseUser.logInInBackground(lowerCase, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                // TODO Auto-generated method stub
                if (e == null) {
                    loginSuccessful();
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                    loginUnSuccessful();
                }


            }
        });

    }

    protected void loginSuccessful() {
        // TODO Auto-generated method stub
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i);
    }

    protected void loginUnSuccessful() {
        // TODO Auto-generated method stub
        // Toast.makeText(getApplicationContext(), "Wrong Stuff", Toast.LENGTH_SHORT).show();
        //(Login.this, "Login", "Username or Password is invalid.", false);
    }

}
