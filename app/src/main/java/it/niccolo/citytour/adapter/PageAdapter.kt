package it.niccolo.citytour.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import it.niccolo.citytour.fragment.LocationFragment
import it.niccolo.citytour.fragment.PlacesFragment

@Suppress("DEPRECATION")
class PagerAdapter(fm : FragmentManager, private val numOfTabs : Int) : FragmentPagerAdapter(fm) {

    override fun getCount() : Int {
        return numOfTabs
    }

    override fun getItem(position: Int) : Fragment {
        return when(position) {
            0 -> LocationFragment()
            1 -> PlacesFragment()
            else -> Fragment()
        }
    }

}