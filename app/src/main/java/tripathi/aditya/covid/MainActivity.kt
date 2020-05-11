package tripathi.aditya.covid

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {
    lateinit var pDialog:ProgressDialog
    var nD:Int=0
    var nR:Int=0
    var nA:Int=0
    internal lateinit var nDD: TextView
    internal lateinit var nRR:TextView
    internal  lateinit var nAA:TextView
    internal lateinit var stateOpen:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nDD = findViewById(R.id.death) as TextView
        nRR = findViewById(R.id.recovered) as TextView
        nAA= findViewById(R.id.active) as TextView
        stateOpen=findViewById(R.id.more) as ImageView
        val url="https://api.rootnet.in/covid19-in/stats/latest"
        if(isNetworkAvailable()){
            AsyncTaskHandler().execute(url)
        }else{
            Toast.makeText(this,"Network is not available",Toast.LENGTH_SHORT).show()
        }
        stateOpen.setOnClickListener{
            val intent = Intent(this, StateList::class.java)
            startActivity(intent)
        }
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    inner class AsyncTaskHandler:AsyncTask<String,String,String>(){
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
            val jsonObject=JSONObject(jsonString).getJSONObject("data").getJSONObject("summary")
            nA=jsonObject.getInt("total")
            nD=jsonObject.getInt("deaths")
            nR=jsonObject.getInt("discharged")
            nDD.setText(nD.toString())
            nRR.setText(nR.toString())
            nAA.setText(nA.toString())
        }

        override fun onPreExecute() {
            super.onPreExecute()
            pDialog= ProgressDialog(this@MainActivity)
            pDialog.setMessage("Please wait")
            pDialog.setCancelable(false)
            pDialog.show()
        }

    }
}
