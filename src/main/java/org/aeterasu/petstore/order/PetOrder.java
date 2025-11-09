package org.aeterasu.petstore.order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

public class PetOrder
{
    private long id = 0;
    private long petId = 0;
    private long quantity = 0;
    private String shipDate = "";
    private String status = "";
    private Boolean complete = false;

    public PetOrder(
        long id, 
        long petId,
        long quantity,
        String shipDate,
        String status,
        Boolean complete
    )
    {
        this.id = id;
        this.petId = petId;
        this.quantity = quantity;
        this.shipDate = shipDate;
        this.status = status;
        this.complete = complete;
    }

    public long getId()
    {
        return this.id;
    }

    public long getPetId()
    {
        return this.petId;
    }

    public long getQuantity()
    {
        return this.quantity;
    }

    public String getShipDate()
    {
        return this.shipDate;
    }

    public String getStatus()
    {
        return this.status;
    }

    public Boolean getComplete()
    {
        return this.complete;
    }

    public JSONObject getJson()
    {
        JSONObject result = new JSONObject()
            .put("id", this.id)
			.put("petId", this.petId)
			.put("quantity", this.quantity)
			.put("shipDate", this.shipDate)
            .put("status", this.status)
            .put("complete", this.complete);

        return result;
    }

    public static String getCurrentDataFormatted()
    {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return currentDateTime.format(formatter);
    }
}