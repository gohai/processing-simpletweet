/*
 * Press the mouse to post the current display window on Twitter
 * Graphics taken from Processing's RandomBook example
 */
import gohai.simpletweet.*;
SimpleTweet simpletweet;

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
}

void draw() {
  background(255);
  for (int i=0; i<100; i++) {
    float r = random(1.0);
    if(r < 0.2) {
      stroke(255); 
    } else {
      stroke(0); 
    }
    float sw = pow(random(1.0), 12);
    strokeWeight(sw * 260); 
    float x1 = random(-200, -100);
    float x2 = random(width+100, width+200);
    float y1 = random(-100, height+100);
    float y2 = random(-100, height+100);
    line(x1, y1, x2, y2);
  }
}

void mousePressed() {
  String tweet = simpletweet.tweetImage(get(), "Made with Processing");
  println("Posted " + tweet);
}
