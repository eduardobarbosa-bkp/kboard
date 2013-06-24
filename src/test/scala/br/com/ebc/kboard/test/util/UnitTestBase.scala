package br.com.ebc.kboard.test.util

import org.scalatest.{FlatSpec, BeforeAndAfter}
import org.scalatest.matchers.ShouldMatchers


trait UnitTestBase extends FlatSpec with SpringTestBase with ShouldMatchers with BeforeAndAfter{

}
