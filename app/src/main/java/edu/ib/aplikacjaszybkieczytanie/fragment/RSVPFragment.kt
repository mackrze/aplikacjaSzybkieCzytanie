package edu.ib.aplikacjaszybkieczytanie.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ib.aplikacjaszybkieczytanie.firebase.FirebaseRepo
import edu.ib.aplikacjaszybkieczytanie.R
import edu.ib.aplikacjaszybkieczytanie.adapter.ResultListAdapter
import edu.ib.aplikacjaszybkieczytanie.model.ResultModel
import kotlinx.android.synthetic.main.fragment.*

class RSVPFragment : Fragment() {

    private val firebaseRepo: FirebaseRepo =
        FirebaseRepo()

    private var rsvpList: List<ResultModel> = ArrayList()
    private val rsvpListAdapter: ResultListAdapter = ResultListAdapter(rsvpList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadPostData()
        fragmentRecView.layoutManager = LinearLayoutManager(this.context)
        fragmentRecView.adapter=rsvpListAdapter
    }

    fun loadPostData(){
        firebaseRepo.getResultsByMode("RSVP").addOnCompleteListener {
            if (it.isSuccessful) {
                rsvpList = it.result!!.toObjects(ResultModel::class.java)
                rsvpListAdapter.resultListItems = rsvpList
                rsvpListAdapter.notifyDataSetChanged()
            }
        }
    }

}