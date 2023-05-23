package com.test.myandroidassignment.views.weather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.myandroidassignment.R
import com.test.myandroidassignment.common.kelvinToCelsius
import com.test.myandroidassignment.common.unixTimestampToTimeString
import com.test.myandroidassignment.databinding.ItemHourlyBinding
import com.test.myandroidassignment.models.weatherlist.HourlyItem

class HourlyAdapter(
    private val hourlyDataList: List<HourlyItem> = arrayListOf(),
    var context: Context
) :
    RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHourlyBinding.inflate(inflater)
        return HourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val data = hourlyDataList[position]
        holder.binding.tvItemHourlyTime.text = data.dt.unixTimestampToTimeString()
        holder.binding.tvItemHumidityLevel.text = data.humidity.toString() + "%"
        holder.binding.tvItemTempLevel.text =
            data.temp.kelvinToCelsius().toString() + " " + context.resources.getString(
                R.string.degree_celsius_symbol
            )
        Glide.with(context)
            .load("http://openweathermap.org/img/w/${data.weather?.get(0)!!.icon}.png")
            .into(holder.binding.ivHourlyWeather)
    }

    override fun getItemCount(): Int {
        return hourlyDataList.size
    }

    inner class HourlyViewHolder(itemView: ItemHourlyBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: ItemHourlyBinding

        init {
            binding = itemView
        }
    }
}