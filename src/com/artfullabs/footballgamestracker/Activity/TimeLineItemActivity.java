package com.artfullabs.footballgamestracker.Activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.artfullabs.footballgamestracker.R;
import com.artfullabs.footballgamestracker.R.drawable;
import com.artfullabs.footballgamestracker.R.id;
import com.artfullabs.footballgamestracker.R.layout;
import com.artfullabs.footballgamestracker.R.menu;
import com.artfullabs.footballgamestracker.model.TimeLineItemVO;
import com.artfullabs.footballgamestracker.util.Constants;
import com.artfullabs.footballgamestracker.util.Utilities;
import com.artfullabs.footballgamestracker.util.image.ImageLoader;

/**
 * @author Leonardo Derks
 * Activity que visualiza el item del timeline
 */
public class TimeLineItemActivity extends Activity {
	
	private static final String TAG = "FootballGamesTracker-NewsCenterAdapter";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_line_item);
		

		if (savedInstanceState == null) {
			TimeLineItemVO timeLineItemVO= (TimeLineItemVO) getIntent().getSerializableExtra(Constants.TIME_LINE_ITEM);
			setTitle(timeLineItemVO.getTitle());
			Utilities.logInfo(TAG,"tmeline vo que llega "+timeLineItemVO);
			Bundle args= new Bundle();
			args.putSerializable(Constants.TIME_LINE_ITEM, timeLineItemVO);
			PlaceholderFragment placeholderFragment = new PlaceholderFragment();
			placeholderFragment.setArguments(args);
			getFragmentManager().beginTransaction()
					.add(R.id.container, placeholderFragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_line_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		
		public PlaceholderFragment()
		{
			
		}

	
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
		
			View rootView = inflater.inflate(R.layout.fragment_time_line_item,
					container, false);
			Bundle bundle=getArguments();
			if(bundle!=null)
			{
				final TimeLineItemVO timeLineItemVO = (TimeLineItemVO) bundle.getSerializable(Constants.TIME_LINE_ITEM);
				Utilities.logInfo(TAG,"arguments es " + timeLineItemVO);
				TextView textViewItemDesc =  (TextView) rootView.findViewById(R.id.TextViewItemDesc);
				textViewItemDesc.setText(timeLineItemVO.getMainText());
				Linkify.addLinks(textViewItemDesc, Linkify.ALL);
//				textViewItemDesc.setMovementMethod(LinkMovementMethod.getInstance());
//				textViewItemDesc.setText(Html.fromHtml(timeLineItemVO.getMainText()));
				
				ImageView imageView = (ImageView) rootView.findViewById(R.id.imageViewItem);
				final String imageLink = timeLineItemVO.getImageLink();
				int loader = R.drawable.ic_action_picture;
				if(imageLink!=null)
		        {
			        ImageLoader imgLoader = new ImageLoader(getActivity());	
			        imageView.setVisibility(View.INVISIBLE);
			        imgLoader.DisplayImage(imageLink, loader, imageView, getActivity(),null,null,200);
			        final Activity thisTimeLineItemActivity = this.getActivity();
			      
			        imageView.setOnClickListener(new View.OnClickListener(){

			            @Override
			            public void onClick(View v){
			            	Intent intent = new Intent();
			    			intent.setClass(thisTimeLineItemActivity, ImageViewActivity.class);			
			    			intent.putExtra(Constants.URL_IMAGE,imageLink);			
			    			intent.putExtra(Constants.ITEM_TITLE,timeLineItemVO.getTitle());
			    			
			    			startActivity(intent);
			            }
			        });
		        }
		        else
		        {	
		        	imageView.setVisibility(View.GONE);
		        }
			}
			else
			{
				Utilities.logInfo(TAG,"arguments es nulo en fragmento");
			}
			
			return rootView;
		}
	}

}
