package dk.shortify.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dk.shortify.R
import dk.shortify.db.History

class HistoryAdapter (private val context : Context) : RecyclerView.Adapter<HistoryAdapter.HistoryVH>(){
    var data = listOf<History>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryVH {
        val view = LayoutInflater.from(context).inflate(R.layout.history_item, parent , false)
        return HistoryVH(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: HistoryVH, position: Int) {
        holder.bind(data[position])
    }

    class HistoryVH(view : View) : RecyclerView.ViewHolder(view){
        private val img : ImageView = view.findViewById(R.id.img_type)
        private val tvShorten : TextView = view.findViewById(R.id.tv_shorten)
        private val tvOrigin : TextView = view.findViewById(R.id.tv_origin)

        fun bind(data : History) {
            //img
            when(data.type){
                0 -> img.setImageResource(R.drawable.vgdlogo)
                1 -> img.setImageResource(R.drawable.isgdlogo)
                2 -> img.setImageResource(R.drawable.me2)
                3 -> img.setImageResource(R.drawable.codelogo)
            }

            //shorten
            with(data.shorten){
                tvShorten.text = substring(indexOf("//") + 2)
            }

            //original
            tvOrigin.text = data.origin

            itemView.setOnClickListener {

            }
        }
    }
}
