package com.adsandurl.fragment


import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adsandurl.R
import com.adsandurl.adapter.NewAdapter
import com.adsandurl.dataModel.NewChildren
import com.adsandurl.database.AppDatabase
import com.adsandurl.listener.OnBottomReachedListener
import com.adsandurl.listener.RecyclerViewClickListener
import com.adsandurl.util.AppClass
import com.adsandurl.util.Helper
import com.adsandurl.viewModel.DataViewModel
import com.wang.avi.AVLoadingIndicatorView


class NewFragment : Fragment() {

    private var tvReload: TextView? = null
    private var avlLoadingDots: AVLoadingIndicatorView? = null
    private var rvHot: RecyclerView? = null
    private var newAdapter: NewAdapter? = null
    private var dataViewModel: DataViewModel? = null
    private var isBottomError: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hot, container, false)
        tvReload = view.findViewById(R.id.tv_reload)
        avlLoadingDots = view.findViewById(R.id.avl_loading_dots)
        rvHot = view.findViewById(R.id.rv_hot)
        val db = AppDatabase.getDatabase(AppClass.getContext())

        rvHot!!.layoutManager = LinearLayoutManager(context)
        rvHot!!.itemAnimator = DefaultItemAnimator()
        dataViewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)
        avlLoadingDots!!.visibility = View.VISIBLE



        dataViewModel!!.getNewLiveData()
            .observe(this,
                Observer<List<NewChildren>> { data ->
                    if (newAdapter == null) {

                        newAdapter = NewAdapter(data, activity!!)

                        if (data.isNotEmpty()) {
                            avlLoadingDots!!.visibility = View.GONE
                            tvReload!!.visibility = View.GONE
                        }
                        //Bottom: Next Data
                        newAdapter!!.setOnBottomReachedListener(object :
                            OnBottomReachedListener {
                            override fun onBottomReached(position: Int) {

                                if ((!dataViewModel!!.isDataEnd.value!!) && (!isBottomError) == true) {
                                    newAdapter!!.footerVisibility(false, true)

                                    dataViewModel!!.getNextNewData()
                                }
                            }
                        })



                        newAdapter!!.setRecyclerViewClickListener(object :
                            RecyclerViewClickListener {
                            override fun onClick(id: Int, position: Int) {
                                if (id == R.id.iv_bottom_reload) {

                                    if (Helper.isNetworkAvailable(activity!!)) {
                                        isBottomError = false
                                        newAdapter!!.footerVisibility(false, true)
                                        newAdapter!!.notifyDataSetChanged()
                                        dataViewModel!!.getNextNewData()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            resources.getString(R.string.message_no_internet_connection),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        })

                        rvHot!!.adapter = newAdapter
                    } else {
                        newAdapter!!.setList(data)
                        avlLoadingDots!!.visibility = View.GONE
                    }
                })



        dataViewModel!!.isNetworkError.observe(this, object : Observer<Int> {
            override fun onChanged(@Nullable value: Int?) {
                if (value == 0) {
                    isBottomError = false
                    avlLoadingDots!!.visibility = View.GONE
                    tvReload!!.visibility = View.GONE
                    newAdapter!!.footerVisibility(false, false)
                    newAdapter!!.notifyDataSetChanged()
                } else if (value == 1) {
                    avlLoadingDots!!.visibility = View.GONE
                    if (db.dataDao().totalNewData() == 0) {
                        tvReload!!.visibility = View.VISIBLE
                    }

                } else if (value == 2) {
                    isBottomError = true
                    newAdapter!!.footerVisibility(true, false)
                    newAdapter!!.notifyDataSetChanged()
                }

            }
        })




        tvReload!!.setOnClickListener {
            if (Helper.isNetworkAvailable(activity!!)) {
                avlLoadingDots!!.visibility = View.VISIBLE
                tvReload!!.visibility = View.GONE
                dataViewModel!!.reloadNewData()
            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.message_no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_feeds, menu)
        val menuItem = menu.findItem(R.id.menu_search)

        val searchView = menuItem.getActionView() as SearchView
        searchView.setFocusable(false)
        searchView.setQueryHint("Search")
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {


            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                newAdapter!!.getFilter().filter(s)
                return false
            }
        })


    }

}

