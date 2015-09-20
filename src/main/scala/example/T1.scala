package example
import org.scalajs.dom
import scalajs.js.annotation.JSExport
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import dom.ext.Ajax
import scalajs.js
import js.Dynamic.literal
import com.felstar.scalajs.ractive._

@JSExport
object T1 extends {

  @JSExport
  def main() = {

    implicit class ToDynamicArray(dyn:js.Dynamic){      
      def toArray=dyn.asInstanceOf[js.Array[js.Dynamic]]
      def foreach(f: js.Dynamic=>Unit)=toArray.foreach(f)
      def map[B](f: js.Dynamic=>B)=toArray.map(f)
    }

    def celsius(kelvins: js.Dynamic) = {kelvins.asInstanceOf[Double] - 273.15}
    
    def formatTemp(x:Any)=scala.util.Try{x.toString.toDouble.formatted("%.2f")}.getOrElse("0")
    
    val area:js.ThisFunction={
      (ra:Ractive)=>
        val width=ra.get("width").asInstanceOf[Double]
        val height=ra.get("height").asInstanceOf[Double]
        (width*height).toInt
    }
    
    def weatherForecast(ra2:Ractive): Unit = {
      
      val city=ra2.get("city")
      
      if (city.toString == "") return;
     
      val fXHR = Ajax.get(s"http://api.openweathermap.org/data/2.5/forecast/daily?q=$city&cnt=7")

      fXHR.onSuccess {
        case xhr =>
          if (xhr.status == 200) {
            val json = js.JSON.parse(
              xhr.responseText)
              
              def dt2DateString(dt:js.Dynamic)={
              val date=new js.Date(dt.asInstanceOf[Double].toLong*1000)
              // Can't do SimpleDateFormat from Scala.js, bah
               s"${date.getDate}/${date.getMonth+1}/${date.getFullYear}"
              }
              
              val dateTemps=json.list.map{el=> (dt2DateString(el.dt),celsius(el.temp.max),celsius(el.temp.min))}
                            
              val indexedDateTemps=for {
                ((_,temp_max,temp_min),idx)<-dateTemps.zipWithIndex                
              } yield ((idx,temp_max),(idx,temp_min))
              
              val highs=indexedDateTemps.map(_._1)
              val lows=indexedDateTemps.map(_._2)

              val wrapper = ra2.find( ".svg-wrapper" );
              val (width,height)=(wrapper.clientWidth,wrapper.clientHeight)
              
              val xscale=width/dateTemps.size
              val yscale=(temp:Double)=>height-((temp+10.0)/52*height)
              
              def scaleTemp(idx:Int, temp:Double)=
                literal(x=idx*xscale,y=yscale(temp),value=temp)             
              
              val dates=for {
                ((dateString,high,low),idx)<-dateTemps.zipWithIndex
              } yield literal(
                  dateString=literal(x=idx*xscale,value=dateString),
                  high=scaleTemp(idx,high),
                  low=scaleTemp(idx,low))

              ra2.set("zeroY",yscale(0))
              ra2.set("clientHeight",height)              
              ra2.set("lineWidth",width)
              
              val shape=highs++lows.reverse  
              
              val svgPath=shape.map{case (idx,temp)=>s"${idx*xscale},${yscale(temp)}"}
              
                // for some reason it does not animate the svgPath. So just using set for now.
              //ra2.animate("dates",literal(dates=dates,svgPath=svgPath),literal(easing= "easeOut",duration=3000))
              ra2.set("dates",literal(dates=dates,svgPath=svgPath))              
          }
      }
    }
    
    val rvals = literal(
      el = "#container",
      template = "#template",         
      computed=literal(area= area),     
      data = literal(
          name = "world", 
          width=0,
          height=100,
          lineWidth=0,
          checkbox = true,                    
          format= formatTemp _)
        )
    val ra = new Ractive(rvals)
    
    println(js.JSON.stringify(ra.nodes)) // to show we can get all elements with an id    
    
    def weatherNow(city: String): Unit = {
      if (city == "") return

      val fXHR = Ajax.get(s"http://api.openweathermap.org/data/2.5/weather?q=$city")

      fXHR.onSuccess {
        case xhr =>
          if (xhr.status == 200) {
            val json = js.JSON.parse(
              xhr.responseText)
            val country = json.sys.country.toString
            val weather = json.weather.pop().main.toString
            
            val min = formatTemp(celsius(json.main.temp_min))
            val max = formatTemp(celsius(json.main.temp_max))
            val humid = json.main.humidity.toString        
            ra.set(literal(country=country,weather=weather,min=min,max=max,humid=humid))
          }
      }
    }
    
    ra.observe("city", (_: Ractive, newVal: String) => weatherNow(newVal.toString))
    ra.observe("city", (ra: Ractive) => weatherForecast(ra))
    
    ra.set("city", "London")

    // calling jstime2 in JS, magic 
    (1 to 10).map(x => js.Dynamic.global.jstimes2(x)).map(x => ra.set("width", x))
    ra.add("width", 10)

    val current = ra.get("width").asInstanceOf[Number]

    val ok: Any => Unit = x => println(s"ok $x")
    val failed: Any => Unit = x => println(s"failed $x")

    def animate(ra:Ractive)={
     val animprom = ra.animate("width", 100 + current.intValue(), literal(duration = 2000))
     animprom.andThen(ok) // should say ok 130
     animprom.andThen((_: Int) + 1).andThen(ok) // should say ok 131 (we add 1 to 130, then print out)
     animprom.andThen((x: Int) => ra.animate("width", x - 100, literal(duration = 2000))).andThen { toAnyAny(println("Finished shrinking")) }  
    }
    
    animate(ra)
    
    ra.onR("animate",animate)

    // ra.on("increment", (ra:Ractive)=>ra.add("width")) // telling it to expect a ractive
    // ra.on("increment", toRactiveHandler(_.add("width"))) // doing same with converter

    //    ra.onR("increment",_.add("width")) // same with pimp    
    //    ra.onR("decrement", _.subtract("width"))

    // in one go
    val cancellable = ra.on {
      literal(
        increment = toRactiveHandler(_.add("width")),
        decrement = toRactiveHandler(_.subtract("width")))
    }
    
     // using an inplace template, twice, with append
    {
      val rvals = literal(
      el = "#container2",
      template = "<b>In place template. Greeting={{greeting}}</b><br/>",
      data = literal(greeting = "hello"),
      append=true)
      new Ractive(rvals)
      
      rvals.data.greeting="world"
      new Ractive(rvals)
    }    
  }
}