package com.felstar.scalajs

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom._

import js.annotation.JSName

package object ractive{
  def toAnyAny(f: =>Any)= (x:Any)=>f
  //def toRactiveHandler( f: Ractive=>Any)= (ra:Ractive)=>f(ra)
  def toRactiveHandler( f: Ractive=>Any)= ((ra:Ractive)=>f(ra)):js.ThisFunction
  
  implicit class RactivePimp(val ra:Ractive) extends AnyVal{
    def onR(eventName:String,f: Ractive=>Any):js.Any=ra.on(eventName,f)
    def offR(eventName:String,f: Ractive=>Any):js.Any=ra.off(eventName,f)
  }
}