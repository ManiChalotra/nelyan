package com.nelyan_live.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nelyan_live.R
import com.nelyan_live.adapter.FavoriteAdapter
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavouriteActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_favorite)
        initalizeClicks()

        recyclerview?.setLayoutManager(LinearLayoutManager(this))
        recyclerview.adapter = FavoriteAdapter(this)
    }

    private  fun initalizeClicks(){
        ivBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ivBack->{
                onBackPressed()
            }

        }
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
}