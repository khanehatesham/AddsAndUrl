package com.adsandurl.fragment


import android.os.Bundle
import android.util.Log
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
import com.adsandurl.adapter.HotAdapter
import com.adsandurl.dataModel.HotChildren
import com.adsandurl.database.AppDatabase
import com.adsandurl.listener.OnBottomReachedListener
import com.adsandurl.listener.RecyclerViewClickListener
import com.adsandurl.util.AppClass
import com.adsandurl.util.Helper
import com.adsandurl.viewModel.DataViewModel
import com.wang.avi.AVLoadingIndicatorView


class HotFragment : Fragment() {

    private var tvReload: TextView? = null
    private var avlLoadingDots: AVLoadingIndicatorView? = null
    private var rvHot: RecyclerView? = null
    private var hotAdapter: HotAdapter? = null
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



        dataViewModel!!.getHotLiveDat()
            .observe(this,
                Observer<List<HotChildren>> { data ->
                    if (hotAdapter == null) {

                        hotAdapter = HotAdapter(data, activity!!)

                        if (data.isNotEmpty()) {
                            avlLoadingDots!!.visibility = View.GONE
                            tvReload!!.visibility = View.GONE
                        }
                        //Bottom: Next Data
                        hotAdapter!!.setOnBottomReachedListener(object :
                            OnBottomReachedListener {
                            override fun onBottomReached(position: Int) {

                                Log.d("bottom==", "bottom")
                                if ((!dataViewModel!!.isDataEnd.value!!) && (!isBottomError) == true) {
                                    Log.d("bottom==", "bottom1")
                                    hotAdapter!!.footerVisibility(false, true)

                                    dataViewModel!!.getNextHotData()
                                }
                            }
                        })



                        hotAdapter!!.setRecyclerViewClickListener(object :
                            RecyclerViewClickListener {
                            override fun onClick(id: Int, position: Int) {
                                if (id == R.id.iv_bottom_reload) {

                                    if (Helper.isNetworkAvailable(activity!!)) {
                                        isBottomError = false
                                        hotAdapter!!.footerVisibility(false, true)
                                        hotAdapter!!.notifyDataSetChanged()
                                        dataViewModel!!.getNextHotData()
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

                        rvHot!!.adapter = hotAdapter
                    } else {
                        hotAdapter!!.setList(data)
                        avlLoadingDots!!.visibility = View.GONE
                    }
                })



        dataViewModel!!.isNetworkError.observe(this, object : Observer<Int> {
            override fun onChanged(@Nullable value: Int?) {
                if (value == 0) {
                    isBottomError = false
                    avlLoadingDots!!.visibility = View.GONE
                    tvReload!!.visibility = View.GONE
                    hotAdapter!!.footerVisibility(false, false)
                    hotAdapter!!.notifyDataSetChanged()
                } else if (value == 1) {
                    avlLoadingDots!!.visibility = View.GONE
                    if (db.dataDao().totalData() == 0) {
                        tvReload!!.visibility = View.VISIBLE
                    }

                } else if (value == 2) {
                    isBottomError = true
                    hotAdapter!!.footerVisibility(true, false)
                    hotAdapter!!.notifyDataSetChanged()
                }

            }
        })




        tvReload!!.setOnClickListener {
            if (Helper.isNetworkAvailable(activity!!)) {
                avlLoadingDots!!.visibility = View.VISIBLE
                tvReload!!.visibility = View.GONE
                dataViewModel!!.reloadHotData()
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
                hotAdapter!!.getFilter().filter(s)
                return false
            }
        })


    }

}

