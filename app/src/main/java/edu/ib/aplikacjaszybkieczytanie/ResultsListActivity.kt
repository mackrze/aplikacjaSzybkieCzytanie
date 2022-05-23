package edu.ib.aplikacjaszybkieczytanie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import edu.ib.aplikacjaszybkieczytanie.fragment.PointFragment
import edu.ib.aplikacjaszybkieczytanie.fragment.RSVPFragment
import kotlinx.android.synthetic.main.activity_results_list.*

class ResultsListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results_list)

        val adapter = MyViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(RSVPFragment(),title = "RSVP")
        adapter.addFragment(PointFragment(),title = "Point")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }




    class MyViewPagerAdapter(manager:FragmentManager):FragmentPagerAdapter(manager)
    {
        private val fragmentList: MutableList<Fragment> = ArrayList()
        private val titleList: MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        fun addFragment(fragment:Fragment, title:String){
            fragmentList.add(fragment)
            titleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater as MenuInflater
        inflater.inflate(R.menu.menu3,menu)
        return true
    }

    fun backHomeOnClick(item: MenuItem) {
        val intent = Intent(this, MenuActivity::class.java)
        this.startActivity(intent)
    }

}