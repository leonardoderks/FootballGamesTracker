package com.artfullabs.footballgamestracker.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.artfullabs.footballgamestracker.R;
import com.artfullabs.footballgamestracker.util.Constants;
import com.artfullabs.footballgamestracker.util.image.ImageLoader;
import com.artfullabs.footballgamestracker.util.image.ZoomFunctionality;

public class ImageViewActivity extends Activity {
	
	private static final String TAG = "FootballGamesTracker-ImageViewActivity";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			String url = getIntent().getStringExtra(Constants.URL_IMAGE);
			String title = getIntent().getStringExtra(Constants.ITEM_TITLE);
			setTitle(title);
			ZoomFunctionality img = new ZoomFunctionality(this);
			ImageLoader imgLoader = new ImageLoader(getApplicationContext());
			//imagen
			int loader = R.drawable.ic_action_picture;
			imgLoader.DisplayImage(url, loader, img, this,null,null,0);
			//img.setImageBitmap(bmp);
			img.setMaxZoom(4f);
			setContentView(img);			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_view, menu);
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


}
