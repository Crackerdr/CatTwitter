package com.crackerdr.catreddit;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import models.Entry;
import models.Feed;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class FeedFragment extends Fragment {

    private static final String TAG = "MainActivity";

    private static final String BASE_URL = "https://www.reddit.com/r/";

    private Button btnRefreshFeed;
    private MediaPlayer mMediaPlayer;
    private TextView mEmptyStateTextView;
    private ImageView mEmptyStateImageView;

    public FeedFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.feed_fragment, container, false);
        Log.d(TAG, "onCreate: starting.");


        btnRefreshFeed = (Button) rootView.findViewById(R.id.btnRefreshFeed);
        FloatingActionButton fab = rootView.findViewById(R.id.fab);

        init();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMediaPlayer != null) {
                    releaseMediaPlayer();
                } else {
                    playSound(getActivity(), R.raw.cat_sound);
                }
            }
        });
        return rootView;
    }


    private void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        CatAPI catAPI = retrofit.create(CatAPI.class);

        Call<Feed> call = catAPI.getFeed();

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                //Log.d(TAG, "onResponse: feed: " + response.body().toString());
                //  Log.d(TAG, "onResponse: Server Response: " + response.toString());

                List<Entry> entrys = response.body().getEntrys();

                // Log.d(TAG, "onResponse: entrys: " + response.body().getEntrys());

//                Log.d(TAG, "onResponse: title: " + entrys.get(0).getTitle());
                final ArrayList<CatCard> mCardList = new ArrayList<>();
                int size = mCardList.size();
                for (int i = 0; i < entrys.size(); i++) {
                    ExtractXML extractXML1 = new ExtractXML(entrys.get(i).getContent(), "<a href=");
                    List<String> postContent = extractXML1.start();

                    if (!postContent.get(3).contains("i.redd.it")) {
                        continue;
                    }

                    ExtractXML extractXML2 = new ExtractXML(entrys.get(i).getContent(), "<span><a href=");

                    try {
                        postContent.add(postContent.get(3));
//                        postContent.add(extractXML2.start().get(0));
                    } catch (NullPointerException e) {
                        postContent.add(null);
                        Log.e(TAG, "onResponse: NullPointerException(thumbnail):" + e.getMessage());
                    } catch (IndexOutOfBoundsException e) {
                        postContent.add(null);
                        Log.e(TAG, "onResponse: IndexOutOfBoundsException(thumbnail):" + e.getMessage());
                    }


                    int lastPosition = postContent.size() - 1;
                    try {
                        mCardList.add(new CatCard(
                                entrys.get(i).getTitle(),
                                postContent.get(0),
                                postContent.get(lastPosition),
                                entrys.get(i).getId()
                        ));
                    } catch (NullPointerException e) {
                        mCardList.add(new CatCard(
                                entrys.get(i).getTitle(),
                                postContent.get(0),
                                postContent.get(lastPosition),
                                entrys.get(i).getId()
                        ));
                        Log.e(TAG, "onResponse: NullPointerException: " + e.getMessage());
                    }

                }

                ListView listView = (ListView) getActivity().findViewById(R.id.list_view);
                mEmptyStateTextView = (TextView) getActivity().findViewById(R.id.empty_view);
                mEmptyStateImageView = (ImageView) getActivity().findViewById(R.id.empty_view_image);
                listView.setEmptyView(mEmptyStateImageView);
                listView.setEmptyView(mEmptyStateTextView);
                CardAdapter cardAdapter = new CardAdapter(getActivity(), R.layout.item_container, mCardList);
                listView.setAdapter(cardAdapter);


                btnRefreshFeed = (Button) getActivity().findViewById(R.id.btnRefreshFeed);
                btnRefreshFeed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCardList.clear();
                        init();
                        cardAdapter.notifyDataSetChanged();
                    }

                });
                View loadingIndicator = getActivity().findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.GONE);

            }


            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG, "onFailure: Unable to retrieve RSS: " + t.getMessage());
                Toast.makeText(getActivity(), "An Error Occured", Toast.LENGTH_SHORT).show();

                View loadingIndicator = getActivity().findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.GONE);

                // Update empty state with no connection error message
                mEmptyStateTextView.setText(R.string.no_internet_connection);
                mEmptyStateImageView.setImageResource(R.drawable.sad_cat);

            }

        });

    }


    public void playSound(Context context, int soundFileResId) {

        mMediaPlayer = MediaPlayer.create(context, soundFileResId);

        mMediaPlayer.start();

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mMediaPlayer.release();
                mMediaPlayer = null;

            }
        });

    }

    private void releaseMediaPlayer() {

        if (mMediaPlayer != null) {

            mMediaPlayer.release();
            mMediaPlayer = null;

        }
    }


}