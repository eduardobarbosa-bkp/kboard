package br.com.ebc.kboard.test.util

import org.springframework.context.support.ClassPathXmlApplicationContext

trait SpringTestBase {

  private var applicationContext:ClassPathXmlApplicationContext = null

  {
    applicationContext =  new ClassPathXmlApplicationContext(getApplicationContextName)
  }

  protected  def getApplicationContextName():String = { "classpath:spring/test-context.xml" }


  def getBean[T](clazz:Class[T]):T = {
    applicationContext.getBean(clazz)
  }

  def getBeanByName(clazzName:String):Any = {
    applicationContext.getBean(clazzName)
  }

}
