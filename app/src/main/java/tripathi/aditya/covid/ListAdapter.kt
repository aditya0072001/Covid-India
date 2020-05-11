package tripathi.aditya.covid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ListAdapter (val context: Context,val list:ArrayList<StateData>):BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view:View=LayoutInflater.from(context).inflate(R.layout.activity_state_list,parent,false)
        val loc=view.findViewById<TextView>(R.id.state_name)
        val ddactive=view.findViewById<TextView>(R.id.state_active)
        val dddeath=view.findViewById<TextView>(R.id.state_death)
        val ddrecovered=view.findViewById<TextView>(R.id.state_recovered)

        loc.text=list[position].loc.toString()
        ddactive.text=list[position].ddactive.toString()
        dddeath.text=list[position].dddeath.toString()
        ddrecovered.text=list[position].ddrecovered.toString()

        return  view
    }

    override fun getItem(position: Int): Any {
        return  position.toLong()
    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        return  list.size
    }

}