package com.artfullabs.footballgamestracker.Adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artfullabs.footballgamestracker.R;
import com.artfullabs.footballgamestracker.model.RssFeedVO;
import com.artfullabs.footballgamestracker.model.TimeLineItemVO;
import com.artfullabs.footballgamestracker.model.TweetVO;
import com.artfullabs.footballgamestracker.model.TwitterSettings;
import com.artfullabs.footballgamestracker.util.Utilities;
import com.artfullabs.footballgamestracker.util.image.ImageLoader;

/**
 * @author Leonardo Derks
 * Clase que permite pintar en la lista los feeds
 * Contiene el array con los objetos feeds y los enlaza con el 
 * componente grafico
 *
 */
public class NewsCenterAdapter extends BaseAdapter{
	
	

	

	private final Context mContext;
	/**
	 * lista de bojetos en el timeline
	 */
	private final List<TimeLineItemVO> mItems = new ArrayList<TimeLineItemVO>();
		
	

	private long currentId = 0;	

	private static final String TAG = "FootballGamesTracker-NewsCenterAdapter";
	private Activity activity;
	
	public NewsCenterAdapter(Context context, Activity activity) {
		this.activity = activity;
		mContext = context;
		
		

	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return mItems.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public TimeLineItemVO getItem(int pos) {
		return mItems.get(pos);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int pos) {
		return pos;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		
		final TimeLineItemVO timeLineItemVO = mItems.get(position);
		
		currentId = timeLineItemVO.getId();

		if(timeLineItemVO instanceof TweetVO)
		{
			return getTweetView((TweetVO)timeLineItemVO,convertView,parent);
		}
		else if(timeLineItemVO instanceof RssFeedVO)
		{
			return getFeedView((RssFeedVO)timeLineItemVO,convertView,parent);
		}
		
		return null;
		
		
	}
	
	/**
	 * Metodo que retorna una vista de tipo feed
	 * @param timeLineItemVO
	 * @param convertView
	 * @param parent
	 * @return
	 */
	private View getFeedView(RssFeedVO timeLineItemVO, View convertView,
			ViewGroup parent) {
		LayoutInflater viewInflator  = LayoutInflater.from(mContext);
		RelativeLayout itemLayout = (RelativeLayout) viewInflator.inflate(R.layout.feed_item, null);
				
	
		final TextView titleView = (TextView) itemLayout.findViewById(R.id.titleView);
		titleView.setText(timeLineItemVO.getTitle());	
		
		final TextView descriptionView = (TextView) itemLayout.findViewById(R.id.descriptionView);
		descriptionView.setText(timeLineItemVO.getMainText());			

		// Return the View you just created
		return itemLayout;
	}

	/**
	 * Metodo que obtiene una vista de tipo tweet
	 * @param timeLineItemVO
	 * @param convertView
	 * @param parent
	 * @return
	 */
	private View getTweetView(TweetVO timeLineItemVO, View convertView,
			ViewGroup parent) {
		LayoutInflater viewInflator  = LayoutInflater.from(mContext);
		RelativeLayout itemLayout = (RelativeLayout) viewInflator.inflate(R.layout.twitter_item, null);
		
		final TextView textViewTwitterName = (TextView) itemLayout.findViewById(R.id.textViewTwitterName);
		textViewTwitterName.setText(timeLineItemVO.getTitle());	
		
		final TextView editTextTwittDesc = (TextView) itemLayout.findViewById(R.id.editTextTwittDesc);
		editTextTwittDesc.setText(timeLineItemVO.getMainText());
		//editTextTwittDesc.setKeyListener(null);
	
		final TextView textViewTwitterRT = (TextView) itemLayout.findViewById(R.id.textViewTwitterAcc);
		textViewTwitterRT.setText(timeLineItemVO.getRetweetAccountName());
		
		final ProgressBar progressBar = (ProgressBar) itemLayout.findViewById(R.id.imgProgress);
		
		final TextView textViewTwitterScreen = (TextView) itemLayout.findViewById(R.id.textViewTwitterScreen);
		textViewTwitterScreen.setText(timeLineItemVO.getAccountName());
		
		final TextView textViewAgo = (TextView) itemLayout.findViewById(R.id.textViewAgo);
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		cal.setTime(timeLineItemVO.getCreationDate());
		String duration = Utilities.getTimeAgo(cal);
		
		//String duration = Utilities.toDuration(timeLineItemVO.getCreationDate().getTime());
		textViewAgo.setText(duration);
		
		
		//imagen
		int loader = R.drawable.ic_action_picture;
        
        // Imageview to show
        ImageView image = (ImageView) itemLayout.findViewById(R.id.imageViewTwittImage);
         
        // Image url
        String image_url = timeLineItemVO.getImageLink();
//        Utilities.logInfo(TAG, image_url);
         
        // ImageLoader class instance
        if(image_url!=null)
        {
	        ImageLoader imgLoader = new ImageLoader(mContext);	
	        image.setVisibility(View.INVISIBLE);
	        imgLoader.DisplayImage(image_url, loader, image, activity,progressBar,textViewTwitterRT,150);
        }
        else
        {
        	//logica para que quede el titulo de la cuenta
        	//debajo del texto cuando no hay imagen
        	itemLayout.removeView(image);
        	itemLayout.removeView(progressBar);
        	itemLayout.removeView(textViewTwitterRT);
        	RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
        			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        	lp.addRule(RelativeLayout.BELOW, editTextTwittDesc.getId());
        	lp.addRule(RelativeLayout.ALIGN_LEFT,editTextTwittDesc.getId());
        	lp.setMargins(0, 5, 0, 0);
        	textViewTwitterRT.setTextSize(10);
        	textViewTwitterRT.setVisibility(View.VISIBLE);
        	itemLayout.addView(textViewTwitterRT,lp);

        }
        
        //imagen icono cuenta
        String accountImage = timeLineItemVO.getAccountImageLink();
        if(accountImage!=null)
        {	        
	        ImageView accountImageView = (ImageView) itemLayout.findViewById(R.id.imageViewTwitterImage);
	        ImageLoader imgLoader = new ImageLoader(mContext);	
	        imgLoader.DisplayImage(accountImage, loader, accountImageView, activity,null,null,150);
        }
        
         
		// Return the View you just created
		return itemLayout;
	}

	/**
	 * Metodo que agrega un item a la lista
	 * @param item
	 */
	public void add(TimeLineItemVO item) {

		mItems.add(item);
		//notifyDataSetChanged();

	}
	
	public long getCurrentId() {
		return currentId;
	}

	public void setCurrentId(long currentId) {
		this.currentId = currentId;
	}
	
	public List<TimeLineItemVO> getmItems() {
		return mItems;
	}

	
	

}
