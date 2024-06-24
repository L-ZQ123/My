package com.lzq.myweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.Request

import org.json.JSONObject
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val iv_search: ImageView =findViewById(R.id.iv_search)
        iv_search.setOnClickListener {
            //获取城市
            val edt_city: EditText =findViewById(R.id.edt_city)
            val str_city=edt_city.text.toString()
            if (str_city.isNotEmpty()){
                //发起网络请求
                sendrequestOKHttp(str_city)
            }else{
                edt_city.setError("cuowu")
            }
        }
    }
    private fun sendrequestOKHttp(city:String){
        //开启子线程
        thread {
            //建立一个客户端
            val client= OkHttpClient()
            //创建服务器端
            val request=Request.Builder().url(" http://v0.yiketianqi.com/free/v2030?city=$city&appid=96639816&appsecret=9XO6gz9g&hours=1\"").build()
            //建立一个连接
            val response=client.newCall(request).execute()
            //沿着连接去拿取所需的数据
            val responsedata=response.body?.string()
            Log.i("WeatherActivity",responsedata.toString())
            //要把数据给传出去
            if (responsedata!=null){
                passdata(responsedata)
            }else{
                Toast.makeText(this,"error", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun passdata(res:String){
        //去解析网络上
        val jsonObject= JSONObject(res)
        val city=jsonObject.optString("city")
        var data=""
        if (city.isNotEmpty()){
            val date=jsonObject.optString("data")
            val weather=jsonObject.optString("wea")
            val week=jsonObject.optString("week")
            val tem=jsonObject.optString("tem")
            val temhight=jsonObject.optString("tem_day")
            val temlow=jsonObject.optString("tem_nigth")
            val humidity=jsonObject.optString("humidity")
            val win_speed=jsonObject.optString("win_speed")
            data="当前日期\\t:$date\\n天气情况\\t$weather  \\n当前星期\\t:$week \\n实时温度:$tem \\n高温:$temhight \\n低温:$temlow \\n风力等级:$win_speed \\n空气湿度:$humidity"
        }else{
            data=""
        }
        //把数据显示出来
        show(data)
    }
    private fun show(data:String){
        val tv_weather: TextView =findViewById(R.id.tv_weather)
        runOnUiThread{
            if (data.isNotEmpty()){
                tv_weather.setText(data)
            }else{
                tv_weather.setError("error")
            }
        }
    }
    
}