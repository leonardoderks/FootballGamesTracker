package com.artfullabs.footballgamestracker.model;

/**
 * @author Leonardo Derks
 * clase que almacena configuraciones
 * basicas del twitter
 *
 */
public class TwitterSettings {

	public TwitterSettings(long sinceId, long maxId) {
		super();
		this.sinceId = sinceId;
		this.maxId = maxId;
	}
	public long getSinceId() {
		return sinceId;
	}
	public void setSinceId(long sinceId) {
		this.sinceId = sinceId;
	}
	public long getMaxId() {
		return maxId;
	}
	public void setMaxId(long maxId) {
		this.maxId = maxId;
	}
	private long sinceId;
	private long maxId;

}
