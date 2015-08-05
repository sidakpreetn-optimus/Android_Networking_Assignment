package com.example.twitterflickrdemo;

/**
 * @author optimus158
 * 
 *         Class for extracting the image URL from the JSON Object from Flickr
 */
public class FlickrJSONImage {
	String id;
	String owner;
	String secret;
	String server;
	String farm;
	String title;

	FlickrJSONImage(String id, String owner, String secret, String server,
			String farm, String title) {
		this.id = id;
		this.owner = owner;
		this.secret = secret;
		this.server = server;
		this.farm = farm;
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getFarm() {
		return farm;
	}

	public void setFarm(String farm) {
		this.farm = farm;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
