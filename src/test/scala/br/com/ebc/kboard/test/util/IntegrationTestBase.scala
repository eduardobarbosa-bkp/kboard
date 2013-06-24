package br.com.ebc.kboard.test.util

import org.scalatest.{BeforeAndAfter, GivenWhenThen, FeatureSpec}
import scala.io.{BufferedSource, Source}

trait IntegrationTestBase extends FeatureSpec with SpringTestBase with GivenWhenThen with BeforeAndAfter{

   def loadFileClassLoader(path:String):BufferedSource = {
     Source.fromURL(getClass.getClassLoader.getResource(path))
  }

}
