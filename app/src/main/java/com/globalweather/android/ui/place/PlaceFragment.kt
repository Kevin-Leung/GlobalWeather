package com.globalweather.android.ui.place

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.ViewModelProvider
import com.globalweather.android.MainActivity
import com.globalweather.android.R
import com.globalweather.android.databinding.FragmentPlaceBinding
import com.globalweather.android.logic.model.Place
import com.globalweather.android.ui.weather.WeatherActivity

class PlaceFragment : Fragment(R.layout.fragment_place) {

    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java)}
    private lateinit var adapter: PlaceAdapter

    private lateinit var placeViewModel:PlaceViewModel
    private var _binding: FragmentPlaceBinding? = null //点FragmentPlaceBinding可以直接进去xml

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        placeViewModel = ViewModelProvider(this).get(PlaceViewModel::class.java)
        _binding = FragmentPlaceBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity is MainActivity && viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }

        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        adapter = PlaceAdapter(this, viewModel.placeList)
        binding.recyclerView.adapter = adapter

        //监听搜索框内容的变化情况：每当内容发生变化，获取新的内容，传递给PlaceViewModel中的searchPlaces()
        binding.searchPlaceEdit.addTextChangedListener { text: Editable? ->
            val content = text.toString()
            if(content.isNotEmpty()){
                viewModel.searchPlaces(content)
            }else{
                binding.recyclerView.visibility = View.GONE
                binding.bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result->
            val places = result.getOrNull()
            if(places!=null){
                binding.recyclerView.visibility = View.VISIBLE
                binding.bgImageView.visibility=View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(activity,"cannot find any places",Toast.LENGTH_LONG).show()
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}