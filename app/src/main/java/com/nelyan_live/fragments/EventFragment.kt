package com.nelyan_live.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.AppUtils
import com.nelyan_live.R
import com.nelyan_live.R.layout
import com.nelyan_live.adapter.MyEventAdapter
import com.nelyan_live.modals.EventModel
import com.nelyan_live.ui.Activity2Activity
import com.nelyan_live.ui.ActivityFragmentActivity
import com.nelyan_live.ui.CommunicationListner
import com.nelyan_live.utils.OpenActivity
import java.lang.RuntimeException
import java.util.*

class EventFragment : Fragment(), OnItemSelectedListener {

    private  var listner: CommunicationListner?= null

    lateinit  var mContext: Context
    var ivLocation: ImageView? = null
    var ivBack: ImageView? = null
    var title: TextView? = null
    var tvFilter: TextView? = null
    lateinit  var v: View
    var orderby: Spinner? = null
    var rc: RecyclerView? = null
    var datalist = ArrayList<EventModel>()


    override fun onResume() {
        super.onResume()
        if(listner!= null){
            listner!!.onFargmentActive(5)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(layout.fragment_event, container, false)
        mContext = requireActivity()
        ivBack = v.findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener {
            val fm = requireActivity()!!.supportFragmentManager
            val f = fm.findFragmentById(R.id.frame_container)
            fm.popBackStack()
        })

        ivLocation = v.findViewById(R.id.iv_bell)
        orderby = v.findViewById(R.id.orderby)
        rc = v.findViewById(R.id.rc_event)
        tvFilter = v.findViewById(R.id.tvFilter)
        tvFilter!!.setOnClickListener(View.OnClickListener {
            requireActivity().OpenActivity(ActivityFragmentActivity::class.java)
            //AppUtils.gotoFragment(mContext, ActivityFragment(), R.id.frame_container, false)
        })
        ivBack!!.setVisibility(View.VISIBLE)
        //            title.setVisibility(View.VISIBLE);
        ivLocation!!.setVisibility(View.VISIBLE)
        ivLocation!!.setImageResource(R.drawable.location_circle)
        val LM = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rc!!.setLayoutManager(LM)
        datalist.clear()
        datalist.add(EventModel(R.drawable.img_1, "Danse", "Marechal 20KM", "10.12.2020 To 11.12.2020", "11:00 AM 3 Year", "01:00 PM 5 Year", 450.80, "During the summer,many cities show movies in the park. Bring a blanket, snacks, a light sweater, and enjoy the magic of cinema"))
        datalist.add(EventModel(R.drawable.img_1, "Danse", "Marechal 20KM", "10.12.2020 To 11.12.2020", "11:00 AM 3 Year", "01:00 PM 5 Year", 450.80, "During the summer,many cities show movies in the park. Bring a blanket, snacks, a light sweater, and enjoy the magic of cinema"))
        val ad = MyEventAdapter(requireActivity(), datalist)
        //rc.setAdapter(null);
        rc!!.setAdapter(ad)
        ivLocation!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, Activity2Activity::class.java)
            intent.putExtra("event", "activity")
            startActivity(intent)
            /*Intent i= new Intent(getActivity(), Activity2Activity.class);
                    startActivity(i);*/
        })
        val genderlist = arrayOf<String?>(
                "", "Events in City",
                "Date Added",
                "Distance")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                requireActivity()!!, layout.customspinner, genderlist)

// Setting Adapter to the Spinner
        orderby!!.setAdapter(adapter)

// Setting OnItemClickListener to the Spinner
        orderby!!.setOnItemSelectedListener(this@EventFragment)
        return v
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is CommunicationListner){
            listner = context as CommunicationListner
        }else{

            throw RuntimeException("Home Fragment not Attched")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listner = null
    }

}