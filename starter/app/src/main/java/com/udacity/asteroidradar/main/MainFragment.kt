package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated"
        }
        ViewModelProvider(this, MainViewModel.Factory(activity.application))
            .get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val adapter = MainAdapter(MainAdapter.OnClicKListener {
            viewModel.displayDetails(it)
        })
        binding.asteroidRecycler.adapter = adapter

        setHasOptionsMenu(true)

        viewModel.asteroidList.observe(viewLifecycleOwner, Observer {
            it.apply {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToSelectedAsteroid.observe(requireActivity(), Observer {
            if (it != null) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroidDetailsComplete()
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

//    companion object {
//        @SuppressLint("NewApi")
//        fun checkConnectivity() : Boolean {
//            var result = false
//            val connectivityManager =
////                contex.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
//                val networkCapabilities = connectivityManager.activeNetwork ?: return false
//                val activeNetwork =
//                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
//                result = when {
//                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//                    else -> false
//                }
//            } else {
//                connectivityManager.run {
//                    connectivityManager.activeNetworkInfo?.run {
//                        result = when (type) {
//                            ConnectivityManager.TYPE_WIFI -> true
//                            ConnectivityManager.TYPE_MOBILE -> true
//                            else -> false
//                        }
//                    }
//                }
//            }
//
//            return result
//        }
//    }
}
