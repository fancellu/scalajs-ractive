package com.felstar.scalajs.ractive

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom._

import js.annotation.JSName

  @js.native
   trait Promise extends js.Object {   
   @JSName("then")
    def andThen[A,B](onFulfilled:js.Function1[A,B], onRejected:js.Function1[A,B]):Promise=js.native
   @JSName("then")    
    def andThen[A,B](onFulfilled: js.Function1[A,B]):Promise=js.native   
  }
  
  @js.native
   class Ractive extends js.Object {
    // instance methods
    def this(obj: js.Any) = this()
    def add(keypath:String,number:Int=1):Promise=js.native
    def animate(keypath:String,value:js.Any,options:js.Any):Promise=js.native
    def animate(keypath:String,value:js.Any):Promise=js.native
    def find(selector:String):org.scalajs.dom.raw.Element=js.native
    def findAll(selector:String,options:js.Any):js.Array[org.scalajs.dom.raw.Element]=js.native
    def findAll(selector:String):js.Array[org.scalajs.dom.raw.Element]=js.native
    def findComponent():org.scalajs.dom.raw.Element=js.native
    def findComponent(name:String):org.scalajs.dom.raw.Element=js.native
    def findComponents(name:String,options:js.Any):js.Array[org.scalajs.dom.raw.Element]=js.native
    def findContainer(name:String):org.scalajs.dom.raw.Element=js.native
    def findParent(name:String):org.scalajs.dom.raw.Element=js.native
    def fire(eventName:String,args:js.Any*):Unit=js.native
    def get():js.Any=js.native
    def get(keypath:String):js.Any=js.native
    def observe(keypath:String, callback:js.ThisFunction,options:js.Any):Cancellable=js.native
    def observe(keypath:String, callback:js.ThisFunction):Cancellable=js.native
    def observeOnce(keypath:String, callback:js.Any,options:js.Any):Cancellable=js.native
    def observeOnce(keypath:String, callback:js.Any):Cancellable=js.native
    def off(eventName:String,handler:js.Any):js.Any=js.native
    def off(eventName:String):js.Any=js.native
    def on(eventName:String,handler:js.ThisFunction):Cancellable=js.native
    def on(obj:js.Any):Cancellable=js.native
    def once(eventName:String,handler:js.Any):Cancellable=js.native
    def pop(keypath:String):Promise=js.native
    def push(keypath:String, value:js.Any*):Promise=js.native
    def render(target:js.Any):js.Any=js.native
    def reset(data:js.Any):Promise=js.native
    def resetPartial(name:String, partial:js.Any):js.Any=js.native
    def set(keypath:String,value:js.Any):Promise=js.native
    def set(obj:js.Any):Promise=js.native
    def shift(keypath:String):Promise=js.native
    def splice(keypath:String, index:Int, removeCount:Int, add:js.Any*):Promise=js.native
    def subtract(keypath:String,number:Int=1):Promise=js.native
    def teardown():Promise=js.native    
    def toggle(keypath:String):Promise=js.native
    def toHTML():String=js.native
    def unrender():js.Any=js.native
    def unshift(keypath:String):Promise=js.native
    def update():Promise=js.native
    def update(keypath:String):js.Any=js.native
    def updateModel():js.Any=js.native
    def updateModel(keypath:String,cascade:Boolean=false):js.Any=js.native
    // instance properties
    val container:js.Any=js.native
    val nodes:js.Dictionary[org.scalajs.dom.raw.Element]=js.native
    val transitions:js.Array[js.Function]=js.native
    val parent:js.Any=js.native
    val partials:js.Array[String]=js.native
    val root:js.Any=js.native
  }

  @js.native
   class Cancellable extends js.Object {
    def cancel:Unit =js.native      
   }
  
   @js.native
    object Ractive extends js.Object{    
     def extend(obj:js.Any):Ractive=js.native
     def getNodeInfo(node:org.scalajs.dom.raw.Element):NodeInfo=js.native     
     def parse(template:String):js.Any=js.native
     def parse(template:String,options:js.Any):js.Any=js.native
    }
   
   @js.native
   class NodeInfo extends js.Object {    
    val ractive:Ractive =js.native
    val keypath:String =js.native
    val indices:js.Array[js.Any] =js.native      
   }
