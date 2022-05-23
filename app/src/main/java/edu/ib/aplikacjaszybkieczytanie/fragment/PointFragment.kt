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

class PointFragment : Fragment() {

    private val firebaseRepo: FirebaseRepo =
        FirebaseRepo()

    private var pointList: List<ResultModel> = ArrayList()
    private val pointListAdapter: ResultListAdapter = ResultListAdapter(pointList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadPostData()
        fragmentRecView.layoutManager = LinearLayoutManager(this.context)
        fragmentRecView.adapter=pointListAdapter
    }

    fun loadPostData(){
        firebaseRepo.getResultsByMode("Point").addOnCompleteListener {
            if (it.isSuccessful) {
                pointList = it.result!!.toObjects(ResultModel::class.java)
                pointListAdapter.resultListItems = pointList
                pointListAdapter.notifyDataSetChanged()
            }
        }
    }


}