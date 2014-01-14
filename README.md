MapReduce Twitter code
====================

This code gives mappers and reducers for use with Apache Hadoop intended to be applied to data collected from the Twitter search or stream APIs. Expected input is one search API response per file or one json object per line if using the streaming API. [twitter-python](https://github.com/computermacgyver/twitter-python) gives example code to record the streaming api in the expected format. Most of this code was originally written for [an analysis of the extent to which multilingual Twitter users connect the Twitter mentions and retweets network](http://www.scotthale.net/pubs/?chi2014).

##Requirements
The following must be available on the classpath to compile and run:
* Douglas Crockford's [JSON-java](https://github.com/douglascrockford/JSON-java)
* [Hadoop](http://hadoop.apache.org/)
* Matt Sanford's [bindings for Chromium's Compact Language Detector library](https://github.com/mzsanford/cld) 

##Code
The following mappers are provided:
* GeotagMapper: output "username latitude longitude timezone-offset timezone-string location" for each tweet
* KeyTupleFilter: discard input that is not in a whitelist of known keys, while data for keys in the whitelist is unchanged
* TweetLanguageMapper: output "username tweet-language" pairs where tweet-language is the detected language of the tweet using CLD
* TweetLinkMapper: output "timezone link-domain" for each link in each tweet
* TweetTimeMapper: output "username tweet-datetime" for each tweet where tweet-datetime is the time the tweet was authored
* TwitterSearchMapper: To be used with input from the Search API
* UserDescriptionLanguageMapper: Output user,language pairs for each tweet, where the language is the detected language of the user description text in the user's profile
* UserMentionMapper: output "usernameA,usernameB 1" username pairs for every instance where usernameA mentions, retweets, or replies to usernameB

The following reducers are provided:
* MapReducer: Key is unchanged and the output value is a JSON map of all input values and their counts
* SumReducer: Key is unchanged and output value is the sum of all input values

Finally, there is a generic Java class to write a GraphML format (WriteGraph.java)

These can be connected is useful ways. For the article on Twitter and language referenced above, I used TweetLanguage (TweetLanguageMapper and MapReducer) to get a list of languages each user wrote tweets in and the relative frequency with which each language was used. I then used UserMentions (UserMentionMapper and SumReducer) to get the total number of times users mention/reply to each other. The output of this second job was filtered using the UserMentionsFilter (KeyTupleFilter and the built-in IdentityReducer) to only include users who had language data from the TweetLanguage job. Finally, the output of UserMentionsFilter and the TweetLanguage jobs were combined into a network graph using WriteGraph.java. The resulting network is available for download and is the network analyzed in the article.

The additional mappers and reducers were used in other projects or exploratory work, and I plan to continue building this repository with further Twitter/Hadoop related code.

##Reference
If you use this code in support of an academic publication, please cite:
   
    Hale, S. A. (2014) Global Connectivity and Multilinguals in the Twitter Network. 
    In Proceedings of the 2014 ACM Annual Conference on Human Factors in Computing Systems, 
    ACM (Montreal, Canada).

  
This code is released under the [GPLv2 license](http://www.gnu.org/licenses/gpl-2.0.html). Please [contact me](http://www.scotthale.net/blog/?page_id=9) if you wish to use the code in ways that the GPLv2 license does not permit.

More details, related code, and the original academic paper using this code is available at http://www.scotthale.net/pubs/?chi2014 .

