package com.artfullabs.footballgamestracker.util.image;

import java.io.File;

import com.artfullabs.footballgamestracker.util.Utilities;

import android.content.Context;

/**
 * @author Leonardo Derks
 * Clase que cuarda el cache en disco
 *
 */
public class FileCache {
	  
    private File cacheDir;
  
    public FileCache(Context context){
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"TempImages");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
  
    public File getFile(String url){
        String filename=String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        Utilities.logInfo("FileCache","file f" +  f != null ? f.getAbsolutePath() : "es nulo el file");
        return f;
  
    }
  
    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }
  
}
