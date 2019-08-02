package com.alc.travelmantics.model;

import java.io.Serializable;

/**
 * Created by Korir on 8/2/19.
 * amoskrr@gmail.com
 */
public class TravelDeal implements Serializable {
   private String id;
   private String title;
   private String description;
   private String price;

  public TravelDeal() {
  }

  private TravelDeal(Builder builder) {
    setId(builder.id);
    setTitle(builder.title);
    setDescription(builder.description);
    setPrice(builder.price);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public static final class Builder {
    private String id;
    private String title;
    private String description;
    private String price;

    private Builder() {
    }

    public Builder withId(String val) {
      id = val;
      return this;
    }

    public Builder withTitle(String val) {
      title = val;
      return this;
    }

    public Builder withDescription(String val) {
      description = val;
      return this;
    }

    public Builder withPrice(String val) {
      price = val;
      return this;
    }

    public TravelDeal build() {
      return new TravelDeal(this);
    }
  }
}
