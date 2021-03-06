package tasks;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.DateFormatSymbols;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import fregments.ComicFragment;
import models.Comic;
import roxy.xkcdcomics.MainActivity;
import roxy.xkcdcomics.R;

public class ComicRequestAsyncTask extends AsyncTask<String, Void, String> {

    private LayoutInflater mLayoutInfrater;
    private Context mContext;

    private Comic mComic;
    private View mView;
    private String mTaskURL;
    private ProgressDialog mProgressDialog;

    public ComicRequestAsyncTask(LayoutInflater layoutInflater, View view,  Context context)
    {
        mLayoutInfrater = layoutInflater;
        mView = view;
        mContext = context;
    }

    @Override
    protected void onPreExecute(){
       mProgressDialog = new ProgressDialog(mContext);
       showProgressDialog();
    }

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();

            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                mTaskURL = uri[0];
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(final String result) {
        Gson gson = new Gson();
        mComic = gson.fromJson(result.toString(), Comic.class);
        setUpComicFragment();

        dismissProgressDialog();
        mView.scrollTo(0,0);
    }

    private void setUpComicFragment(){
        CustomComicHolder  comicHolder;
        if(mView == null)
        {
            mView = mLayoutInfrater.inflate(R.layout.comic_fragment, null);
        }
        comicHolder = new CustomComicHolder();
        comicHolder.comicTitle = (TextView)mView.findViewById(R.id.comic_title);
        comicHolder.comicImage = (ImageView)mView.findViewById(R.id.comic_image);
        comicHolder.comicHoverText = (TextView)mView.findViewById(R.id.comic_hover_text);
        comicHolder.comicTranscriptText = (TextView)mView.findViewById(R.id.comic_transcript_text);
        comicHolder.comicNumDate = (TextView)mView.findViewById(R.id.comic_number_text);
        mView.setTag(comicHolder);


        Comic comic = mComic;
        comicHolder.comicTitle.setText(comic.getTitle());

        if(comic.getAlt() != null && comic.getAlt() != "" ) {
            comicHolder.comicHoverText.setText(mContext.getString(R.string.comic_hover_text_title) + comic.getAlt());
        } else {
            comicHolder.comicHoverText.setVisibility(comicHolder.comicHoverText.GONE);
        }
        if(comic.getTranscript() != null && comic.getTranscript() != "") {
            comicHolder.comicTranscriptText.setText(mContext.getString(R.string.comic_transcript_text_title) + comic.getTranscript());
        } else {
            comicHolder.comicTranscriptText.setVisibility(comicHolder.comicTranscriptText.GONE);
        }

        String month = new DateFormatSymbols().getMonths()[comic.getMonth()-1];
        String numDate = Integer.toString(comic.getNum()) + " | " + month + " " + Integer.toString(comic.getDay()) + ", " + Integer.toString(comic.getYear());
        comicHolder.comicNumDate.setText(numDate);

        //Picasso.with(mContext).load(comic.getImg()).resize(comicHolder.comicImage.getWidth(), comicHolder.comicImage.getHeight()).centerInside().into(comicHolder.comicImage);
        //Point size = getWindowSize();
        //Picasso.with(mContext).load(comic.getImg()).resize(size.x, size.y).centerInside().into(comicHolder.comicImage);
        Picasso.with(mContext).load(comic.getImg()).into(comicHolder.comicImage);

        //stores the comic value in the shared resources
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(MainActivity.COMIC_NUMBER_STRING, comic.getNum()).apply();

        if(mTaskURL == ComicFragment.CURRENT_COMIC_URL){
            editor.putInt(MainActivity.LAST_COMIC_NUMBER_STRING, comic.getNum()).apply();
        }
    }

    private class CustomComicHolder {
        TextView comicNumDate;
        TextView comicTitle;
        TextView comicHoverText;
        TextView comicTranscriptText;
        ImageView comicImage;
    }

    private Point getWindowSize(){
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    //Loading spinner
    private void showProgressDialog()
    {
        mProgressDialog.setTitle("Loading...");
        mProgressDialog.setMessage("Retrieving comic");
        mProgressDialog.show();
    }

    private void dismissProgressDialog()
    {
        mProgressDialog.dismiss();
    }

}
