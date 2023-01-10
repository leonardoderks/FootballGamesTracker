package com.artfullabs.footballgamestracker.util.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.artfullabs.footballgamestracker.R;
import com.artfullabs.footballgamestracker.util.Utilities;

/**
 * @author Leonardo Derks
 * clase que permite cargar la imagen
 * usando cache
 *
 */
public class ImageLoader {
	  
    MemoryCache memoryCache=new MemoryCache();
    FileCache fileCache;
    private Activity mconContext;
    private ProgressBar progressBar;
    private TextView textViewTwitterAcc;
    private int scaleImage;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService; 
  
    public ImageLoader(Context context){
        fileCache=new FileCache(context);
        executorService=Executors.newFixedThreadPool(5);
    }
  
    int stub_id = R.drawable.ic_launcher;
    public void DisplayImage(String url, int loader, ImageView imageView, Activity mContext, ProgressBar progressBar, TextView textViewTwitterAcc, int scale)
    {
    	this.scaleImage = scale;
    	this.mconContext = mContext;
    	this.progressBar = progressBar;
    	this.textViewTwitterAcc = textViewTwitterAcc;
        stub_id = loader;
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null)
        {	
        	Utilities.logInfo("imageloader","tiene bitmap");
            imageView.setImageBitmap(bitmap);
        }
        else
        {
        	Utilities.logInfo("imageloader","encola foto");
            queuePhoto(url, imageView);
            imageView.setImageResource(loader);
        }
    }
  
    private void queuePhoto(String url, ImageView imageView)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        Utilities.logInfo("imageloader","antes de ejecutar el hilo");
        executorService.submit(new PhotosLoader(p));
        Utilities.logInfo("imageloader","luego de ejecutar el hilo");
    }
  
    private Bitmap getBitmap(String url)
    {
        File f=fileCache.getFile(url);
  
        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null)
        {
        	Utilities.logInfo("imageloader","obtiene imagen de sd");
            return b;
        }
  
        //from web
        try {
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            Utilities.logInfo("imageloader","obtiene bitmap correctamente "+ bitmap);
            return bitmap;
        } catch (Exception ex){
           ex.printStackTrace();
           return null;
        }
    }
  
    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
  
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=this.scaleImage;
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            if(REQUIRED_SIZE>0)
            {
            	
	            int width_tmp=o.outWidth, height_tmp=o.outHeight;
	            int scale=1;
	            while(true){
	                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
	                    break;
	                width_tmp/=2;
	                height_tmp/=2;
	                scale*=2;
	            }
	            o2.inSampleSize=scale;
            }
  
            //decode with inSampleSize
            
            
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
  
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u;
            imageView=i;
        }
    }
  
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }
  
        @Override
        public void run() {
        	Utilities.logInfo("imageloader","entra a run de photosloaderr");
            if(imageViewReused(photoToLoad))
            {
            	Utilities.logInfo("imageloader","foto es reusable");
            	return;
            }
                
            Bitmap bmp=getBitmap(photoToLoad.url);
            memoryCache.put(photoToLoad.url, bmp);
            
            if(imageViewReused(photoToLoad))
            {
            	Utilities.logInfo("imageloader","la foto es reusable");
                return;
            }
            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
            Utilities.logInfo("imageloader","luego de inicializar bitmapdisplayer");
            Utilities.logInfo("imageloader"," contexto actividad "+ photoToLoad.imageView.getContext().getClass());
            //Activity a=(Activity)photoToLoad.imageView.getContext();
//            Activity a=(Activity)mconContext;
            Utilities.logInfo("imageloader","antes de onuitrhead actividad");            
            mconContext.runOnUiThread(bd);
            Utilities.logInfo("imageloader","ejecuta runonuitrhead");     
        }
    }
  
    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }
  
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
            {
            	Utilities.logInfo("imageloader","bitmap es reusable en imagedisplayer");
                return;
            }
            if(bitmap!=null)
            {
            	Utilities.logInfo("imageloader","coloca bitmap en bitmapdisplayer");
            	if(progressBar!=null)
            	{
            		progressBar.setVisibility(View.GONE);
            	}
            	photoToLoad.imageView.setVisibility(View.VISIBLE);
                photoToLoad.imageView.setImageBitmap(bitmap);
                if(textViewTwitterAcc!=null)
                {
                	textViewTwitterAcc.setVisibility(View.VISIBLE);
                }
            }
            else
            {	
            	Utilities.logInfo("imageloader","coloca loader en bitmapdisplayer");
                photoToLoad.imageView.setImageResource(stub_id);
            }
        }
    }
  
    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
  
}
