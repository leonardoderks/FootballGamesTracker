package com.artfullabs.footballgamestracker.Activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.URLEntity;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.artfullabs.footballgamestracker.R;
import com.artfullabs.footballgamestracker.Adapter.NewsCenterAdapter;
import com.artfullabs.footballgamestracker.Interface.RefreshListener;
import com.artfullabs.footballgamestracker.model.TimeLineItemVO;
import com.artfullabs.footballgamestracker.model.TweetVO;
import com.artfullabs.footballgamestracker.model.TwitterSettings;
import com.artfullabs.footballgamestracker.util.Constants;
import com.artfullabs.footballgamestracker.util.Utilities;

/**
 * @author Leonardo Derks
 * Clase que representa la vista del centro de noticias
 *
 */
public class NewsCenterFragment extends ListFragment implements RefreshListener{	

	private static final String TAG = "FootballGamesTracker-NewsCenterFragment";
	
	private NewsCenterAdapter newsCenterAdapter;
	
	private ConcurrentHashMap<String, TwitterSettings> twitterSettingsMap;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create a new TodoListAdapter for this ListActivity's ListView
		newsCenterAdapter = new NewsCenterAdapter(getActivity().getApplicationContext(),getActivity());
		twitterSettingsMap = new ConcurrentHashMap<String, TwitterSettings>(); 
		
		setListAdapter(newsCenterAdapter);
		
		if (newsCenterAdapter.getCount() == 0)
		{
			loadItems();
		}
		
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Make sure that the hosting Activity has implemented
		// the SelectionListener callback interface. We need this
		// because when an item in this ListFragment is selected, 
		// the hosting Activity's onItemSelected() method will be called.
		
//		try {
//
//			mCallback = (SelectionListener) activity;
//
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString()
//					+ " must implement SelectionListener");
//		}
	}

	// Note: ListFragments come with a default onCreateView() method.
	// For other Fragments you'll normally implement this method.
	// 	@Override
	//  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	//		Bundle savedInstanceState)

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// When using two-pane layout, configure the ListView to highlight the
		// selected list item
		
		if (isInTwoPaneMode()) {

			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		}

	}

	@Override
	public void onListItemClick(ListView l, View view, int position, long id) {

	
			Intent intent = new Intent();
			intent.setClass(this.getActivity(), TimeLineItemActivity.class);			
			intent.putExtra(Constants.TIME_LINE_ITEM,(Serializable)newsCenterAdapter.getItem(position));
			startActivity(intent);
	}

	// If there is a FeedFragment, then the layout is two-pane 
	private boolean isInTwoPaneMode() {

		return getFragmentManager().findFragmentById(R.id.content_frame) != null;

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();

		// Load saved ToDoItems, if necessary

		
	}

	/**
	 * Metodo que carga los feeds
	 */
	private void loadItems() {
		// TODO reemplazar por carga original
//		RssFeedVO rssFeedVO= new RssFeedVO(1,"wc2014","a 25 dias del mundial","ya brasil espera"
//				+ "lo que sera todo un acontecimiento","https://www.google.com","es.fifa.com",
//				new Date(),true);
//		newsCenterAdapter.add(rssFeedVO);
//		rssFeedVO= new RssFeedVO(1,"wc2014","se lesiona falcao","infortunadamente"
//				+ " falcao no podra asistir a la cita mundialista","https://www.marca.com","es.fifa.com",
//				new Date(),true);
//		newsCenterAdapter.add(rssFeedVO);
//		rssFeedVO= new RssFeedVO(1,"wc2014","Diego Costa decide","el delantero del "
//				+ "atletico madrid decide jugar con la seleccion de brasil","https://www.fifa.com","es.fifa.com",
//				new Date(),true);
//		newsCenterAdapter.add(rssFeedVO);
		
		
		try {
			reeadTwitterTimeLine();
		} catch (Exception e) {
			Utilities.logError(TAG, "error: " + e.getMessage());
			e.printStackTrace();
		}
		
		
	}

	/**
	 * Metodo que lee el time line de una
	 * cuenta de twitter
	 * @throws TembooException 
	 */
	private void reeadTwitterTimeLine() throws Exception {
		new RetreiveTimeLineItems().execute();	
			
	}
	
	public class RetreiveTimeLineItems extends AsyncTask<String,List<TweetVO> ,List<TweetVO>> {
		
		
		SharedPreferences settings = getActivity().
				getSharedPreferences(getString(R.string.shared_preferences_file),  Context.MODE_PRIVATE);
		
	    /* (non-Javadoc)
	     * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	     */
	    protected List<TweetVO>  doInBackground(String... urls) {
	    	
	    	List<TweetVO> tweets = new ArrayList<TweetVO>();
	    	
	    	//se cargan las cuentas de twitter.
			//TODO reemplazar por azure
//			String[] twitterAccounts = {"@lderks","@fifacom_es","@ccuenca24"};
			String[] twitterAccounts = {"@lderks"};
			
			//obtener por cada cuenta el ultimo ID leido
			
			
			try 
			{
				Twitter twitter = Utilities.getTwitter();
				for(String twitterAccount : twitterAccounts)
				{
					long maxId = 0;
					long sinceId = 0;
					maxId = settings.getLong(Constants.TWITTER_PREFIX_MAXID+twitterAccount,0);
					sinceId = settings.getLong(Constants.TWITTER_PREFIX_SINCEID+twitterAccount,0);
					Utilities.logInfo(TAG, "maxid "+maxId);
					Utilities.logInfo(TAG, "sinceId "+sinceId);
//					if(newsCenterAdapter.getCount()==0)
//					{
//						
//					}
//					else
//					{
//						TwitterSettings twitterSettings = twitterSettingsMap.get(twitterAccount);
//						maxId = twitterSettings.getMaxId();
//						sinceId = twitterSettings.getSinceId();
//					}
					Paging pag = null;
					 List<twitter4j.Status> newTweets = null;
					 List<twitter4j.Status> oldTweets = null;
					//es primera vez que se invoca
					if(maxId==0)
					{
						//se carga los ultimos tweets
						pag = new Paging(1,Constants.MAX_TWEETS_PER_REFRESH);
						newTweets = twitter.getUserTimeline(twitterAccount,pag);
						Utilities.logInfo(TAG, "entra por maxid == 0");
					}
					else
					{						
						pag = new Paging(1,Constants.MAX_TWEETS_PER_REFRESH,maxId);
						newTweets = twitter.getUserTimeline(twitterAccount,pag);
						Utilities.logInfo(TAG, "entra por maxid > 0");
					
						if(newsCenterAdapter.getCount()==0)
						{
							pag = new Paging(1,Constants.MAX_TWEETS_PER_REFRESH,sinceId,maxId);
							oldTweets = twitter.getUserTimeline(twitterAccount,pag);
							Utilities.logInfo(TAG, "entra por count == 0");
						}
											
					}
					if(newTweets!=null && newTweets.size()>0)
					{
						tweets.addAll(fillTweet(newTweets)); 						
						maxId = newTweets.get(0).getId();
						if(sinceId==0)
						{
							sinceId = newTweets.get(newTweets.size()-1).getId();
						}
						Utilities.logInfo(TAG, "tiene teets nuevos "+newTweets.size());
					}
					if(oldTweets!=null && oldTweets.size()>0)
					{
						tweets.addAll(fillTweet(oldTweets)); 
						Utilities.logInfo(TAG, "tiene teets viejos "+oldTweets.size());
					}      
					
					 //setea valores para refrescar apartir del ultimo
//					TwitterSettings twitterSettings = new TwitterSettings(sinceId, maxId);
//					twitterSettingsMap.put(twitterAccount, twitterSettings);
					Utilities.logInfo(TAG, "nuevo maxid "+maxId);
					Utilities.logInfo(TAG, "nuevo sinceId "+sinceId);
					SharedPreferences.Editor editor = settings.edit();
					editor.putLong(Constants.TWITTER_PREFIX_MAXID+twitterAccount, maxId);
					editor.putLong(Constants.TWITTER_PREFIX_SINCEID+twitterAccount, sinceId);
					editor.putLong(Constants.TWITTER_PREFIX_SINCEID+twitterAccount, sinceId);
					editor.commit();
//			        
				}
	    	}
            
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	    	return tweets;
	    }

		private List<TweetVO> fillTweet(List<twitter4j.Status> statuses) {
			List<TweetVO> tweets = new ArrayList<TweetVO>();
			for(twitter4j.Status status: statuses)
			{
				TweetVO tweetVO = new TweetVO();	            	
				tweetVO.setAccountImageLink(status.getUser().getProfileImageURL());
				tweetVO.setAccountName("@" + status.getUser().getScreenName());
				tweetVO.setCreationDate(status.getCreatedAt());
				tweetVO.setId(status.getId());	            	
				tweetVO.setImageLink(getImageLinkFromEntities(status.getMediaEntities()));	            	
				tweetVO.setLinks(getLinksFromEntities(status.getURLEntities()));
				tweetVO.setMainText(getOriginalText(status));
				tweetVO.setTitle(status.getUser().getName());
				String rtName = status.getRetweetedStatus()!=null ? 
						"RT @" + status.getRetweetedStatus().getUser().getScreenName() :
							"";
			
				tweetVO.setRetweetAccountName(rtName);
				tweets.add(tweetVO);
				
			}
			return tweets;
		}

	    /* (non-Javadoc)
	     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	     */
	    protected void onPostExecute(List<TweetVO> tweets ) {
//	    	long lastViewId = settings.getLong(Constants.TWITTER_LASTVIEW_ID,0);
	    	
	    	
	    
	    	for(TweetVO tweetVO : tweets)
	    	{	    		
	    		
	    		newsCenterAdapter.add(tweetVO);
	    		
	    	}
	    	Collections.sort(newsCenterAdapter.getmItems());
	    	setListPosition();
	    	
	    	newsCenterAdapter.notifyDataSetChanged();
	    	
	    	//getListView().smoothScrollToPosition(showPosition);
//	    	Utilities.logInfo(TAG, "postition to show "+showPosition);
	    	
	    	
//	    	getListView().smoothScrollToPosition(getListView().getCount()-1);
//	    	newsCenterAdapter.notifyDataSetChanged();
	    	
//	    	getListView().invalidate();
	    }
	    
	    
	    private void setListPosition() {
	    	long lastViewId = newsCenterAdapter.getCurrentId();
	    	Utilities.logInfo(TAG, "id de adapater "+lastViewId);
	    	if(lastViewId==0)
	    	{
	    		return;
	    	}
	    	int showPosition = 0;
	    	int position = 0;
	    	for(TimeLineItemVO timeLineItemVO : newsCenterAdapter.getmItems())
	    	{
	    		if(lastViewId!=0 && lastViewId==timeLineItemVO.getId())
	    		{
	    			showPosition = position;
	    			Utilities.logInfo(TAG, "postition to show "+showPosition);
	    			Utilities.logInfo(TAG, "id encontrado "+lastViewId);
	    		}
	    		position++;	    		
	    	}
	    	getListView().setSelection(showPosition);			
		}

		/**
	     * Metodo que devuelve el link de la primera imagen
	     * @param mediaEntities
	     * @return
	     */
	    private String getImageLinkFromEntities(MediaEntity[] mediaEntities)
	    {
	    	for(MediaEntity entiti : mediaEntities)
	    	{
	    		if(entiti.getType().equals(Constants.MEDIA_TYPE.PHOTO.getName()))
	    		{
	    			//TODO configurar la imagen pequeña y grande
	    			return entiti.getMediaURL();
	    		}
	    	}
	    	return null;
	    }
	    
	    /**
	     * obtiene los urls de las entidades
	     * @param urlEntities
	     * @return
	     */
	    private List<String> getLinksFromEntities(URLEntity[] urlEntities)
	    {
	    	List<String> links = new ArrayList<String>();
	    	for(URLEntity entity : urlEntities)
	    	{
	    		links.add(entity.getDisplayURL());
	    	}
	    	return links;
	    }
	    
	    /**
	     * metodo que busca en los retweets hasta encontrar el
	     * texto original y asi quitar la palabra retweet del tweet
	     * @param status
	     * @return
	     */
	    private String getOriginalText(twitter4j.Status status)
	    {
	    	if(status.isRetweet())
	    	{
	    		return getOriginalText(status.getRetweetedStatus());
	    	}
	    	else
	    	{
	    		return status.getText();
	    	}
			
	    }
	
	}

	/* (non-Javadoc)
	 * @see com.artfullabs.footballgamestracker.Interface.RefreshListener#refresh()
	 */
	@Override
	public void refresh() {
		try {
			Utilities.logInfo(TAG, "se presiona el boton de refresh");
			reeadTwitterTimeLine();
			
		} catch (Exception e) {
			Utilities.logError(TAG, "error: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onStop() {
		// TODO guardar aqui el ultimo id visto
		// usarlo solo si el id actual es 0, es decir, significa
		//que se entra a la app por primera vez y se debe parar
		//en el ultimo que vio en la session anterior
		super.onStop();
	}
	
	
}
