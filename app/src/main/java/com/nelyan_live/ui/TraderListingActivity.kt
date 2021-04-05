package com.nelyan_live.ui


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.nelyan_live.R
import com.nelyan_live.adapter.TraderListingAdapter
import com.nelyan_live.utils.OpenActivity
import kotlinx.android.synthetic.main.fragment_trader_listing.*

class TraderListingActivity : AppCompatActivity(), View.OnClickListener, TraderListingAdapter.OnTraderItemClickListner {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_trader_listing)
        initalizeClicks()
        rv_traderListing.adapter = TraderListingAdapter(this, this)

    }

    private fun initalizeClicks() {
        ivBack.setOnClickListener(this)
        ivMap.setOnClickListener(this)
        tvFilter.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.ivMap -> {
                OpenActivity(HomeChildCareOnMapActivity::class.java)
            }
            R.id.tvFilter -> {
                OpenActivity(TraderFilterActivity::class.java)
            }
        }
    }

    override fun onTraderListItemClickListner(position: Int) {
        OpenActivity(TraderPublishActivty::class.java)
    }

    override fun onFavouriteItemClickListner(position: Int) {
        OpenActivity(FavouriteActivity::class.java)

    }

}