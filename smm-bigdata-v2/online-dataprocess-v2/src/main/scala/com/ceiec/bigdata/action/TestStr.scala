package com.ceiec.bigdata.action

import com.ceiec.bigdata.entity.twitter.TwitterRoot

object TestStr {

  def main(args: Array[String]): Unit = {
    val twitterRoot: TwitterRoot = new TwitterRoot
//    twitterRoot.setStreaming(2)
    var nb = twitterRoot.getStreaming
      if(nb != null && (nb ==1 ||nb ==0)){

      }else{
        nb = 0
      }
    println(nb)
  }
}
