package dk.shortify.adapter

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.flatdialoglibrary.dialog.FlatDialog
import dk.shortify.R
import dk.shortify.db.DB
import dk.shortify.db.History
import kotlinx.coroutines.*
import splitties.toast.toast

class HistoryAdapter (private val context : Context, val scope: LifecycleCoroutineScope) : RecyclerView.Adapter<HistoryAdapter.HistoryVH>(){
    var data = listOf<History>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryVH {
        val view = LayoutInflater.from(context).inflate(R.layout.history_item, parent , false)
        return HistoryVH(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: HistoryVH, position: Int) {
        holder.bind(data[position])
    }

    inner class HistoryVH(view : View) : RecyclerView.ViewHolder(view){
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

            itemView.setOnClickListener { view ->
                FlatDialog(view.context).apply {
                    setTitle(data.shorten)
                    setTitleColor(Color.parseColor("#C70BFC"))

                    setSubtitle(data.origin)
                    setSubtitleColor(Color.parseColor("#061D07"))

                    setFirstButtonText(view.context.getString(R.string.dup))
                    setSecondButtonText(view.context.getString(R.string.open))
                    setThirdButtonText(view.context.getString(R.string.delete))

                    setFirstButtonColor(Color.parseColor("#d3f6f3"))
                    setSecondButtonColor(Color.parseColor("#fee9b2"))
                    setThirdButtonColor(Color.parseColor("#fbd1b7"))

                    setFirstButtonTextColor(Color.parseColor("#000000"))
                    setSecondButtonTextColor(Color.parseColor("#000000"))
                    setThirdButtonTextColor(Color.parseColor("#000000"))

                    withFirstButtonListner {
                        (view.context.getSystemService(Activity.CLIPBOARD_SERVICE) as ClipboardManager)
                            .apply { setPrimaryClip(ClipData.newPlainText("url", data.shorten)) }

                        dismiss()
                        toast(R.string.clearComplete)
                    }

                    withSecondButtonListner {
                        view.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(data.shorten)))
                    }

                    withThirdButtonListner {
                        val dao = DB.get(view.context).historyDao()

                        scope.launch(Dispatchers.IO) {
                            runBlocking { dao.delete(data) }

                            launch(Dispatchers.Main) {
                                dao.getAll().observe(
                                    view.context as LifecycleOwner, Observer { history ->
                                        this@HistoryAdapter.data = history.reversed()
                                        this@HistoryAdapter.notifyDataSetChanged()
                                    }
                                )
                            }
                        }
                        dismiss()
                    }

                    setBackgroundColor(Color.parseColor("#DDFCA3"))
                    window?.attributes?.windowAnimations = R.style.DialogAnimation
                    setCancelable(true)
                }.show()
            }
        }
    }
}