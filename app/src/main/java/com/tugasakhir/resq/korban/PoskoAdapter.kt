package com.tugasakhir.resq.korban

import android.content.Intent
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tugasakhir.resq.R
import com.tugasakhir.resq.korban.view.PoskoDetailActivity
import com.tugasakhir.resq.rescuer.model.Posko
import kotlinx.android.synthetic.main.item_list_posko.view.*
import java.io.Serializable

class PoskoAdapter : RecyclerView.Adapter<PoskoAdapter.ViewHolder>() {

    private var posko: List<Posko?> = ArrayList()
    private var lat: String? = ""
    private var long: String? = ""
    var TAG = "LIST POSKO "

    fun setPosko(posko: ArrayList<Posko?>, lat: String?, long: String?) {
        this.posko = posko
        this.lat = lat
        this.long = long
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_posko, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = posko.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val context = holder.itemView.context
        Log.d(TAG, "MASUK adapter")
        Log.d(TAG, position.toString())

        val currentPosko = posko[position]
        holder.textview_lokasi_posko.text = currentPosko?.poskoName
        holder.textview_alamat_posko.text = currentPosko?.mapAddress
        holder.textview_kapasitas.text = currentPosko?.capacity.toString()
        holder.textview_jarak.text = currentPosko?.city

        val results = FloatArray(1)
        Location.distanceBetween(lat!!.toDouble(), long!!.toDouble(), currentPosko?.latitude!!.toDouble(), currentPosko?.longitude!!.toDouble(), results)

        val poskoDistance = results[0].toString().split(".")[0]
        var fixDistance = ""

        if (poskoDistance.length > 3) {
            fixDistance = (poskoDistance.toInt() / 1000).toString() + " km"
        } else {
            fixDistance = poskoDistance + " m"
        }

        holder.textview_jarak.text = fixDistance


        holder.itemView.posko_card.setOnClickListener {
            val intent = Intent((context), PoskoDetailActivity::class.java)
            intent.putExtra("EXTRA_POSKO", currentPosko as Serializable)
            intent.putExtra("EXTRA_LAT", lat)
            intent.putExtra("EXTRA_LONG", long)
            (context).startActivity(intent)
        }
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val textview_lokasi_posko: TextView = view.textview_lokasi_posko
        val textview_alamat_posko: TextView = view.textview_alamat_posko
        val textview_kapasitas: TextView = view.textview_kapasitas
        val textview_jarak: TextView = view.textview_jarak
    }
}