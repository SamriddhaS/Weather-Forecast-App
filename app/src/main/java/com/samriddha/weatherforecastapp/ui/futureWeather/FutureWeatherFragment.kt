package com.samriddha.weatherforecastapp.ui.futureWeather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.samriddha.weatherforecastapp.R
import com.samriddha.weatherforecastapp.ui.futureWeather.detailsFutureWeather.EPOCH_DATE_PASS_KEY
import com.samriddha.weatherforecastapp.utils.ApiException
import com.samriddha.weatherforecastapp.utils.LocationUtility
import com.samriddha.weatherforecastapp.utils.NoInternetException
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.future_weather_fragment.*
import kotlinx.android.synthetic.main.future_weather_fragment.groupLoading
import kotlinx.android.synthetic.main.future_weather_fragment.tvCurrentPlace
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class FutureWeatherFragment : Fragment() ,KodeinAware{

    override val kodein by closestKodein()

    private val viewModelFactory by instance<FutureWeatherViewModelFactory>()

    private lateinit var viewModel: FutureWeatherViewModel
    private lateinit var snackBar:Snackbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(FutureWeatherViewModel::class.java)

        if(LocationUtility.hasLocationPermission(requireContext()))
            bindUi()
        else
            Toast.makeText(requireContext(),"Please Goto Current Weather Section & Allow Required Permission",Toast.LENGTH_LONG).show()


    }

    private fun bindUi() {

        viewLifecycleOwner.lifecycleScope.launch {

            val job = async {

                try {
                    viewModel.updateWeatherData()
                }catch (e:NoInternetException){
                    if(futureProgressBar.visibility==View.VISIBLE) futureProgressBar.visibility = View.INVISIBLE
                    initSnackBar("Internet Not Available!!",true)
                    snackBar.show()
                }catch (e:ApiException){
                    if(futureProgressBar.visibility==View.VISIBLE) futureProgressBar.visibility = View.INVISIBLE
                    initSnackBar("Location Not Available,Try Different Location",false)
                    snackBar.show()                }
            }

            job.await()


            val futureWeatherData = viewModel.futureWeather.await()
            val locationData = viewModel.locationWeather.await()

            locationData.observe(viewLifecycleOwner, Observer {

                if (it==null) return@Observer
                updateLocation(it.name,it.region,it.country)
            })

            futureWeatherData.observe(viewLifecycleOwner, Observer {

                if (it==null) return@Observer

                futureProgressBar.visibility = View.GONE
                groupLoading.visibility = View.VISIBLE
                initRecyclerView(it.toFutureWeatherItems())

            })


        }
    }
    private fun initSnackBar(message:String,setAction:Boolean) {

        if (setAction){
            snackBar = Snackbar.make(snackLayoutFuture,message, Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction("Retry", View.OnClickListener {
                snackBar.dismiss()
                futureProgressBar.visibility = View.VISIBLE
                bindUi()
            })
        }else{
            snackBar = Snackbar.make(snackLayoutFuture,message, Snackbar.LENGTH_LONG)
        }

    }

    private fun updateLocation(location: String,region:String,country: String) {
        tvCurrentPlace.text = "$location,$region,$country"
    }

    private fun List<SimpleFutureWeatherData>.toFutureWeatherItems():List<FutureWeatherItem>{

        return this.map {
            FutureWeatherItem(it,viewModel.isMetric)
        }
    }

    private fun initRecyclerView(items:List<FutureWeatherItem>){

        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@FutureWeatherFragment.context,LinearLayoutManager.HORIZONTAL,false)
            adapter = groupAdapter
            setHasFixedSize(true)
        }

        groupAdapter.setOnItemClickListener{ item, view ->

            (item as? FutureWeatherItem)?.let {
                val epochDate = it.simpleFutureWeatherData.date
                val bundle = Bundle()
                bundle.putLong(EPOCH_DATE_PASS_KEY,epochDate)
                Navigation.findNavController(view).navigate(R.id.action_weekWeatherFragment_to_weekDetailsFragment,bundle)
            }
        }
    }

}