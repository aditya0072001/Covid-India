package tripathi.aditya.covid

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.cardview.widget.CardView
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class StateList : AppCompatActivity() {
    lateinit var pDialog:ProgressDialog
    lateinit var mylist:CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_list)
        val url="https://api.rootnet.in/covid19-in/stats/latest"
        mylist= findViewById(R.id.state_list) as CardView
        if(isNetworkAvailable()){
            AsyncTaskHandler().execute(url)
        }else{
            Toast.makeText(this,"Network is not available", Toast.LENGTH_SHORT).show()
        }
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
    inner class AsyncTaskHandler: AsyncTask<String, String, String>(){
        override fun doInBackground(vararg params: String?): String {
            val res:String
            val connection= URL("https://api.rootnet.in/covid19-in/stats/latest").openConnection() as HttpURLConnection
            try {
                connection.connect()
                res=connection.inputStream.use { it.reader().use { reader->reader.readText() } }
            }
            finally {
                connection.disconnect()
            }
            return  res
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            jsonResult(result)
            if(pDialog.isShowing()){
                pDialog.dismiss()
            }
        }

        private fun jsonResult(jsonString: String){
            val jsonObject= JSONObject(jsonString).getJSONObject("data").getJSONArray("regional")
            val list=ArrayList<StateData>()
            var i=0
            while (i<jsonObject.length()){
                val jsonObject1=jsonObject.getJSONObject(i)
                list.add(
                    StateData(
                        jsonObject1.getString("loc"),
                        jsonObject1.getInt("totalConfirmed"),
                        jsonObject1.getInt("deaths"),
                        jsonObject1.getInt("discharged")
                    )
                )
            i++
            }
           // val adapter=ListAdapter(this@StateList,list)
            //mylist.setAdapter(adapter)
        }

        override fun onPreExecute() {
            super.onPreExecute()
            pDialog= ProgressDialog(this@StateList)
            pDialog.setMessage("Please wait")
            pDialog.setCancelable(false)
            pDialog.show()
        }

    }
}
