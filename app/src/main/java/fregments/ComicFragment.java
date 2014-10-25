package fregments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import roxy.xkcdcomics.MainActivity;
import roxy.xkcdcomics.R;
import tasks.ComicRequestAsyncTask;


public class ComicFragment extends Fragment {


    private static final String CURRENT_COMIC_URL = "http://xkcd.com/info.0.json";
    private static final String SPECIFIC_COMIC_URL = "http://xkcd.com/614/info.0.json";
    private static final String COMIC_NUMBER = "comic_number";

    private LayoutInflater mLayoutInflater;
    private String mRequestUrl;
    private Context mContext;
    private ViewGroup mViewGroup;

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

        View rootView = inflater.inflate(R.layout.comic_fragment, container, false);

        mRequestUrl = CURRENT_COMIC_URL;

        ComicRequestAsyncTask comicRequestAsyncTask = new ComicRequestAsyncTask(mLayoutInflater, rootView, mContext);
        comicRequestAsyncTask.execute(mRequestUrl);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(COMIC_NUMBER));
    }

    // Loading spinner
//    private void showProgressDialog()
//    {
//        mProgressDialog.setTitle("Loading...");
//        mProgressDialog.setMessage("Retrieving comic");
//        mProgressDialog.show();
//    }
//
//    private void dismissProgressDialog()
//    {
//        mProgressDialog.dismiss();
//    }

}
