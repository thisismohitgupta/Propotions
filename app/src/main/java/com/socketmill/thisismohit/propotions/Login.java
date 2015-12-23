package com.socketmill.thisismohit.propotions;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Login extends AppCompatActivity {

    CallbackManager callbackManager;
    String email;


    ProgressBar progressBar;

    com.socketmill.thisismohit.propotions.widget.GifMovieView gifMovieView;
    private static LruCache<String, Bitmap> MemCache;


    private static SimpleDiskCache StringCache ;
    private static SimpleDiskCache bitmapCache ;

    SimpleDiskCache Bitmapcache ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();


        FacebookSdk.sdkInitialize(getApplicationContext());
        //final int maxMemorySize = (int)Runtime.getRuntime().maxMemory()/1024 ;
        //final int cacheSize = maxMemorySize /10 ;
        final int memClass = ((ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        final int cacheSize = 1024 * 1024 * memClass;
        Log.e("Error","memory allocated "+ String.valueOf(cacheSize));
        try {
            StringCache = SimpleDiskCache.open (getFilesDir(), 1, (long) cacheSize  / 3); // 10 MB
            //bitmapCache = SimpleDiskCache.open (getFilesDir(), 1, (long) cacheSize  ); // 10 MB
        } catch (IOException e) {

            Log.e("ERROR","FUCKED UP");
            e.printStackTrace();
        }




        setContentView(R.layout.activity_login);


        progressBar = (ProgressBar) findViewById(R.id.marker_progress_login);


        if (progressBar.getVisibility() != View.VISIBLE) {


            Toast.makeText(getApplicationContext(), "its not visible ", Toast.LENGTH_SHORT).show();

        } else {


            Toast.makeText(getApplicationContext(), "its visible ", Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.INVISIBLE);
        }
        gifMovieView = (com.socketmill.thisismohit.propotions.widget.GifMovieView) findViewById(R.id.gifMovieView);


        gifMovieView.setMinimumWidth(700);
        gifMovieView.setMinimumHeight(1000);

        toolbarNav NavController = new toolbarNav();
        NavController.checkIfLoggedIn(getApplicationContext(), MainActivity.class);

        // ProgressBar progree = (ProgressBar)findViewById(R.id.marker_progress_login);

        //progree.setVisibility(View.INVISIBLE);


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

                                        SendFeedBackJob newJob = new SendFeedBackJob();
                                        // newJob.activity = getParent() ;
                                        newJob.progressBar = progressBar;


                                        newJob.EMAIL = email;
                                        newJob.AccessToken = accessToken.getToken();

                                        newJob.invisible = View.INVISIBLE;

                                        newJob.show = View.VISIBLE;
                                        //newJob.context = getApplicationContext() ;


                                        Toast.makeText(getApplicationContext(), "Login with facebook done next step initiated", Toast.LENGTH_SHORT).show();

                                        newJob.execute();
                                        login(profile.getId(), (profile.getId() + profile.getId()));
                                        loginSuccessful();


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


    public static Bitmap getBitmapFromMemoryCache(String key) {



        try {
            return StringCache.getBitmap(key).getBitmap();
        } catch (Exception e) {



        }
        return  null;

    }

    public static void setBitmapMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {



            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] bitmapdata = bos.toByteArray();
                bos.close();
                InputStream bs = new ByteArrayInputStream(bitmapdata);
                StringCache.put(key, bs);
                bitmapdata = null ;
                bitmap = null ;
                bs.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static String getStringFromMemoryCache(String key) {

        String hello = null;
        try {
            hello = StringCache.getString(key).getString();
        } catch (Exception e) {



        }

        return hello;

    }

    public static void setStringMemoryCache(String key, String value)  {
        try {
            if (getStringFromMemoryCache(key) == null) {

                Log.e("ERROR", "name Set to cache");
                StringCache.put(key, value);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
