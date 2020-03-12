package com.crackerdr.catreddit;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_TEXT_USER = "textUser";

    private static final String TAG = "MainActivity";

    final Fragment fragmentFeed = new FeedFragment();
    final FragmentManager fm = getSupportFragmentManager();
    final Fragment fragmentCard = new CardFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: starting.");
        fm.beginTransaction().add(R.id.container_for_fragment, fragmentFeed, "1").commit();
        fm.beginTransaction().add(R.id.container_for_fragment, fragmentCard, "2").hide(fragmentCard).commit();

    }


}
