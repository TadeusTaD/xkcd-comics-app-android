package fregments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Random;

import roxy.xkcdcomics.MainActivity;
import roxy.xkcdcomics.R;
import singletons.ButtonType;
import tasks.ComicRequestAsyncTask;


public class ComicFragment extends Fragment{

    // put constants in the utils file
    public static final String CURRENT_COMIC_URL = "http://xkcd.com/info.0.json";
    private static final String SPECIFIC_COMIC_URL = "http://xkcd.com/%d/info.0.json";
    private static final String COMIC_NUMBER = "comic_number";
    private static final int INITIAL_COMIC_NUMBER = 1;

    //xkcd web icon link http://xkcd.com/s/919f27.ico

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private View mRootView;
    private ViewGroup mViewGroup;
    private ProgressDialog  mProgressDialog;
    private ProgressBar mProgressBar;

    public static ComicFragment newInstance(int comicNumber) {
        ComicFragment fragment = new ComicFragment();
        Bundle args = new Bundle();
        args.putInt(COMIC_NUMBER, comicNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public ComicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mViewGroup = container;
        mContext = getActivity();
        mProgressDialog = new ProgressDialog(getActivity());

        mRootView = inflater.inflate(R.layout.comic_fragment, container, false);
        String requestUrl = CURRENT_COMIC_URL;

        ComicRequestAsyncTask comicRequestAsyncTask = new ComicRequestAsyncTask(mLayoutInflater, mRootView, mContext);
        comicRequestAsyncTask.execute(requestUrl);

        setButtonsOnClickListeners();

        mRootView.scrollTo(0, 0);
        return mRootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(COMIC_NUMBER));
    }

    private void setButtonsOnClickListeners(){
        TextView firstButton = (TextView)mRootView.findViewById(R.id.first_button);
        TextView prevButton = (TextView)mRootView.findViewById(R.id.prev_button);
        TextView randButton = (TextView)mRootView.findViewById(R.id.random_button);
        TextView nextButton = (TextView)mRootView.findViewById(R.id.next_button);
        TextView lastButton = (TextView)mRootView.findViewById(R.id.last_button);
        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int comicNumber = INITIAL_COMIC_NUMBER;
                getComicAsyncTask(comicNumber);
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int comicNumber = getCurrentComicNumber();
                if(comicNumber > INITIAL_COMIC_NUMBER) {
                    --comicNumber;
                    getComicAsyncTask(comicNumber);
                }
            }
        });

        randButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int comicNumber = randInt(INITIAL_COMIC_NUMBER, getLastComicNumber() );
                getComicAsyncTask(comicNumber);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int comicNumber = getCurrentComicNumber();
                if(getLastComicNumber() > comicNumber) {
                    ++comicNumber;
                    getComicAsyncTask(comicNumber);
                }
            }
        });

        lastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int comicNumber = getLastComicNumber();
                getComicAsyncTask(comicNumber);
            }
        });
    }

    private int getCurrentComicNumber(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        int comicNumber = preferences.getInt(MainActivity.COMIC_NUMBER_STRING, MainActivity.COMIC_NUMBER_DEFAULT);
        return comicNumber;
    }

    private int getLastComicNumber(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        int comicNumber = preferences.getInt(MainActivity.LAST_COMIC_NUMBER_STRING, MainActivity.COMIC_NUMBER_DEFAULT);
        return comicNumber;
    }

    private void getComicAsyncTask(int currentComic){
        Toast.makeText(mContext, String.valueOf(currentComic), Toast.LENGTH_SHORT).show();
        String url = String.format(SPECIFIC_COMIC_URL, currentComic);
        ComicRequestAsyncTask comicRequestAsyncTask = new ComicRequestAsyncTask(mLayoutInflater, mRootView, mContext);
        comicRequestAsyncTask.execute(url);
        mRootView.scrollTo(0, 0);
    }

    // put in a utils file
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

}