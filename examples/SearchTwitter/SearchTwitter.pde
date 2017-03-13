/*
 * Example inspired by the earlier tutorial by blprnt
 * See http://twitter4j.org/javadoc/ for the in-depth
 * documentation about the many thing you can do with
 * the twitter4j library
 */
import gohai.simpletweet.*;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

SimpleTweet simpletweet;
ArrayList<Status> tweets;

void setup() {
  size(500, 500);
  frameRate(0.5);
  simpletweet = new SimpleTweet(this);

  /*
   * Create a new Twitter app on https://apps.twitter.com/
   * then go to the tab "Keys and Access Tokens"
   * copy the consumer key and secret and fill the values in below
   * click the button to generate the access tokens for your account
   * copy and paste those values as well below
   */
  simpletweet.setOAuthConsumerKey("...");
  simpletweet.setOAuthConsumerSecret("...");
  simpletweet.setOAuthAccessToken("...");
  simpletweet.setOAuthAccessTokenSecret("...");

  tweets = search("#sun");
}

void draw() {
  background(0);
  Status current = tweets.get(frameCount % tweets.size());
  String message = current.getText();
  User user = current.getUser();
  String username = user.getScreenName();
  text(message + "by @" + username, 0, height/2);
}


ArrayList<Status> search(String keyword) {
  // request 100 results
  Query query = new Query(keyword);
  query.setCount(100);

  try {
    QueryResult result = simpletweet.twitter.search(query);
    ArrayList<Status> tweets = (ArrayList)result.getTweets();
    // return an ArrayList of Status objects
 	return tweets;
  } catch (TwitterException e) {
    println(e.getMessage());
    return new ArrayList<Status>();
  }
}