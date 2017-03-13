/* -*- mode: java; c-basic-offset: 2; indent-tabs-mode: nil -*- */

/*
 * Twitter4J library Copyright 2007 Yusuke Yamamoto
 * Wrapped for Processing by Gottfried Haider 2017.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * Distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gohai.simpletweet;

import processing.core.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import java.io.File;
import java.io.IOException;


/**
 *  Class for allowing easy access to the Twitter API via twitter4j
 */
public class SimpleTweet {

  /**
   *  Main twitter4j instance
   */
  public Twitter twitter;

  protected PApplet parent;
  protected String oAuthConsumerKey;
  protected String oAuthConsumerSecret;
  protected String oAuthAccessToken;
  protected String oAuthAccessTokenSecret;


  /**
   *  Create a new SimpleTweet instance
   *  @param parent typically use "this"
   */
  public SimpleTweet(PApplet parent) {
    this.parent = parent;
    createInstance();
  }

  /**
   *  Set the OAuth Consumer Key
   *  This is necessary to do before the Twitter API can be used
   *  To get this key it is necessary to register your application
   *  at https://apps.twitter.com/app/new
   *  @param key consumer key
   */
  public void setOAuthConsumerKey(String key) {
    oAuthConsumerKey = key;
    createInstance();
  }

  /**
   *  Set the OAuth Consumer Secret
   *  This is necessary to do before the Twitter API can be used
   *  To get this key it is necessary to register your application
   *  at https://apps.twitter.com/app/new
   *  @param secret consumer secret
   */
  public void setOAuthConsumerSecret(String secret) {
    oAuthConsumerSecret = secret;
    createInstance();
  }

  /**
   *  Set the OAuth Access Token
   *  This is necessary to do before the Twitter API can be used
   *  To get this key it is necessary to register your application
   *  at https://apps.twitter.com/app/new
   *  @param token access token
   */
  public void setOAuthAccessToken(String token) {
    oAuthAccessToken = token;
    createInstance();
  }

  /**
   *  Set the OAuth Access Token Secret
   *  This is necessary to do before the Twitter API can be used
   *  To get this key it is necessary to register your application
   *  at https://apps.twitter.com/app/new
   *  @param secret access token secret
   */
  public void setOAuthAccessTokenSecret(String secret) {
    oAuthAccessTokenSecret = secret;
    createInstance();
  }

  /**
   *  Send a tweet
   *  @param message message to send
   *  @return partial URL of the tweet (e.g. "mrgohai/status/841086396194529280")
   */
  public String tweet(String message) {
    try {
      StatusUpdate update = new StatusUpdate(message);
      Status status = twitter.updateStatus(update);

      User user = status.getUser();
      return user.getScreenName() + "/status/" + status.getId();
    } catch (TwitterException e) {
      System.err.println(e.getMessage());
      throw new RuntimeException("Error posting tweet");
    }
  }

  /**
   *  Tweet an image
   *  @param img PImage instance to attach
   *  @param update twitter4j StatusUpdate instance to use
   *  @return partial URL of the tweet (e.g. "mrgohai/status/841086396194529280")
   */
  public String tweetImage(PImage img, StatusUpdate update) {
    File temp;
 
    // write image into temporary file that gets deleted when the VM exits
    try {
      // make sure one pixel is somewhat transparent
      // to prevent Twitter from recompressing the image
      // to JPEG
      PImage copy = new PImage(img.width, img.height, PImage.ARGB);
      copy.copy(img, 0, 0, img.width, img.height, 0, 0, img.width, img.height);
      copy.set(0, 0, 254 << 24 | (copy.get(0, 0) & 0xffffff));
      temp = File.createTempFile("out", ".png");
      temp.deleteOnExit();
      copy.save(temp.getCanonicalPath());
    } catch (IOException e) {
      throw new RuntimeException("Error exporting image to PNG");
    }

    try {
      update.setMedia(temp);
      Status status = twitter.updateStatus(update);
      temp.delete();

      User user = status.getUser();
      return user.getScreenName() + "/status/" + status.getId();
    } catch (TwitterException e) {
      System.err.println(e.getMessage());
      throw new RuntimeException("Error posting tweet");
    }
  }

  /**
   *  Tweet an image
   *  @param img PImage instance to attach
   *  @param message message to send
   *  @return partial URL of the tweet (e.g. "mrgohai/status/841086396194529280")
   */
  public String tweetImage(PImage img, String message) {
    StatusUpdate update = new StatusUpdate(message);
    return tweetImage(img, update);
  }

  protected void createInstance() {
    ConfigurationBuilder cb = new ConfigurationBuilder();
    if (oAuthConsumerKey != null) {
      cb.setOAuthConsumerKey(oAuthConsumerKey);
    }
    if (oAuthConsumerSecret != null) {
      cb.setOAuthConsumerSecret(oAuthConsumerSecret);
    }
    if (oAuthAccessToken != null) {
      cb.setOAuthAccessToken(oAuthAccessToken);
    }
    if (oAuthAccessTokenSecret != null) {
      cb.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
    }
    twitter = new TwitterFactory(cb.build()).getInstance();
  }
}
