package com.artfullabs.footballgamestracker.model;


/**
 * @author Leonardo Derks
 * Clase que representa la tabla de RssFeeds
 *
 */
public class RssFeedVO extends TimeLineItemVO{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int tournamentID;
	private String code;	
	private String domain;	
	private boolean active;
	
	public int getTournamentID() {
		return tournamentID;
	}
	public void setTournamentID(int tournamentID) {
		this.tournamentID = tournamentID;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	@Override
	public int compareTo(Object item) {
		TimeLineItemVO timeLineItemVO = (TimeLineItemVO) item;
		return this.getCreationDate().compareTo(timeLineItemVO.getCreationDate());		
	}
	

}
