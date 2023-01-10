package com.artfullabs.footballgamestracker.util;

/**
 * @author Leonardo Derks
 * Clase con constantes de la aplicacion
 *
 */
public final class Constants {
	
	public static final String tembooAccountName = "artfullabs" ;
	public static final String tembooAppKey = "8ad35404cf3247f2ae3ebd4b93adc1b1";
	public static final String tembooAppName = "footballgamestracker";
	
	public static final String twitterAccountName = "FootballGamesTK" ;
	public static final String twitterAppKey = "dYz1L7ucKbGEBjjyfdiGEUY5F";
	public static final String twitterAppSecret = "dOhsmediQFXoh841iODE7G20l4t8tipNlKEzmDtyamwdgiJCna";
	public static final String twitterAppName = "FootballGamesTracker";
	
	public static final String TIME_LINE_ITEM = "TIME_LINE_ITEM";
	public static final String URL_IMAGE = "URL_IMAGE";
	public static final String ITEM_TITLE = "ITEM_TITLE";
	
	public static final String TWITTER_PREFIX_MAXID = "TWITTER_MAXID_";
	public static final String TWITTER_PREFIX_SINCEID = "TWITTER_SINCEID_";
	public static final String TWITTER_LASTVIEW_ID = "TWITTER_LASTVIEW_ID";
	public static final int MAX_TWEETS_PER_REFRESH = 7;
	
	
	
	
	/**
	 * Opciones del menu
	 */
	public enum MENU_OPTIONS {
		
		NEWS_CENTER(0),
		RESULTS(1),
		STANDINGS(2),
		SCHEDULE(3),
		FAVORITES(4);	
		
		private int index;
	
		private MENU_OPTIONS(int ordinal) {
			index = ordinal;
		}
		
		public int getIndex()
		{
			return index;
		}
		
	}
	
	/**
	 * Tipo de media entity
	 * que viene en un item de time Line
	 *
	 */
	public enum MEDIA_TYPE
	{
		PHOTO("photo"),
		VIDEO("video");
		
		private String name;

		private MEDIA_TYPE(String name) {
			this.name = name;
		}
		
		public String getName()
		{
			return this.name;
		}
		
		
	}

}
