package com.artfullabs.footballgamestracker.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;
import android.os.AsyncTask;
import android.util.Log;

/**
 * @author Leonardo Derks
 * Clase con utlidiades para el proyecto
 * 
 */
public class Utilities {
	
	

	private static final String TAG = "FootballGamesTracker-Utilities";
	
	private static Twitter twitter = null;
	
	private static final int SECOND_MILLIS = 1000;
	private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
	private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
	private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
	
	
	/**
	 * Metodo que parsea la fecha
	 * en formato tweet (1s,h,m,fecha)
	 * @param duration
	 * @return
	 */
	public static String getTimeAgo(Calendar cal) {
		long time = cal.getTimeInMillis();
	    if (time < 1000000000000L) {
	        // if timestamp given in seconds, convert to millis
	        time *= 1000;
	    }

	    long now = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
	    if (time > now || time <= 0) {
	        return null;
	    }

	    // TODO: localize
	    final long diff = now - time;
	    if (diff < MINUTE_MILLIS) {
	        return "0s";
	    } else if (diff < 2 * MINUTE_MILLIS) {
	        return "1m";
	    } else if (diff < 50 * MINUTE_MILLIS) {
	        return diff / MINUTE_MILLIS + "m";
	    } else if (diff < 90 * MINUTE_MILLIS) {
	        return "1h";
	    } else if (diff < 24 * HOUR_MILLIS) {
	        return diff / HOUR_MILLIS + "h";
	    } else if (diff < 48 * HOUR_MILLIS) {
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d");
	        return dateFormat.format(cal.getTime());
	    } else {
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d");
	        return dateFormat.format(cal.getTime());
	    }
	}
	/**
	 * Metodo que se encarga de loggear en 
	 * logcat de tipo info
	 * @param msg
	 */
	public static void logInfo(String TAG, String msg)
	{
		Log.i(TAG, msg);
	}
	
	/**
	 * Metodo que se encarga de loggear en 
	 * logcat de tipo error
	 * @param msg
	 */
	public static void logError(String TAG, String msg)
	{
		Log.e(TAG, msg);
	}
	
	
	private class TwitterConnection extends AsyncTask {
		 
        @Override
        protected Object doInBackground(Object... arg0) {
            try {
				connect();
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return null;
        }
 
    }
 
    private void connect() throws TwitterException {
    	ConfigurationBuilder builder;
		Utilities.logInfo(TAG, "starting twitter integration");
		builder = new ConfigurationBuilder();
		builder.setApplicationOnlyAuthEnabled(true);
		
		builder.setOAuthConsumerKey(Constants.twitterAppKey).
			setOAuthConsumerSecret(Constants.twitterAppSecret);
		
        Twitter twitterConnect = new TwitterFactory(builder.build()).getInstance();

        // exercise & verify
        OAuth2Token token = twitterConnect.getOAuth2Token(); 
        this.twitter = twitterConnect;
    }
	
	/**
	 * Singleton para obener Twitter
	 * @return
	 * @throws TwitterException 
	 */
	public static Twitter getTwitter() throws TwitterException {
		if(twitter==null)
		{
			new Utilities().new TwitterConnection().execute();			
		}
		return twitter;
		
	}

}
