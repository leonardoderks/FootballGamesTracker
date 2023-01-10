package com.artfullabs.footballgamestracker.util.image;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Leonardo Derks
 * Clase con utilideades para manejo de imagenes
 *
 */
public class Utils {
	
    /**
     * Metodo que permite copiar un Stream de bytes
     * @param is
     * @param os
     */
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}
