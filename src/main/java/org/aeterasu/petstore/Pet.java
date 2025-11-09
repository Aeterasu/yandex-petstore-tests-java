package org.aeterasu.petstore;

import org.json.JSONObject;

public class Pet
{
	private long id = 0;
	private String name = "";
	private String status = PetStatus.getStatusAvailable();
	
	public Pet(long id, String name, String status)
	{
		this.id = id;
		this.name = name;
		this.status = status;
	}

	public long getId()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
	}

	public String getStatus()
	{
		return this.status;
	}

	public JSONObject getJson()
	{
 		JSONObject result = new JSONObject()
			.put("id", this.id)
			.put("name", this.name)
			.put("status", this.status);
	   
	   return result;
	}
}