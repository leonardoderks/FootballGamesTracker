package com.artfullabs.footballgamestracker.model;

import com.artfullabs.footballgamestracker.util.Constants.MEDIA_TYPE;

/**
 * @author Leonardo Derks
 * clase que representa un tweet en el timeline
 */
public class TweetVO extends TimeLineItemVO {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MEDIA_TYPE mediaType;
	private String accountImageLink;
	private String retweetAccountName;
	
	private String video;
	private String accountName;
	
	public String getRetweetAccountName() {
		return retweetAccountName;
	}
	public void setRetweetAccountName(String retweetAccountName) {
		this.retweetAccountName = retweetAccountName;
	}
	
	public MEDIA_TYPE getMediaType() {
		return mediaType;
	}
	public void setMediaType(MEDIA_TYPE mediaType) {
		this.mediaType = mediaType;
	}
	
	public String getAccountImageLink() {
		return accountImageLink;
	}
	public void setAccountImageLink(String accountImageLink) {
		this.accountImageLink = accountImageLink;
	}
	
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	@Override
	public int compareTo(Object item) {
		TimeLineItemVO timeLineItemVO = (TimeLineItemVO) item;
		return timeLineItemVO.getCreationDate().compareTo(this.getCreationDate());		
	}


}
