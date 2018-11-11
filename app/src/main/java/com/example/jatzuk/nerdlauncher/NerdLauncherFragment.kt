package com.example.jatzuk.nerdlauncher

import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.util.*

class NerdLauncherFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_nerd_launcher, container, false)
        recyclerView = v.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        setupAdapter()
        return v
    }

    private fun setupAdapter() {
        val pm = activity!!.packageManager
        val activities = pm.queryIntentActivities(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0)
        activities.sortWith(Comparator { o1, o2 -> String.CASE_INSENSITIVE_ORDER.compare(o1?.loadLabel(pm)?.toString(), o2?.loadLabel(pm)?.toString()) })
        recyclerView.adapter = Adapter(activities)
        Log.i(TAG, activities!!.size.toString())
    }

    private inner class Adapter(private val activities: List<ResolveInfo>) : RecyclerView.Adapter<Adapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return Holder(LayoutInflater.from(activity).inflate(R.layout.list_item, parent, false))
        }

        override fun getItemCount() = activities.size

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(activities[position])
        }

        private inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val nameTextView = itemView.findViewById<TextView>(R.id.text_view)
            private val iconImageView = itemView.findViewById<ImageView>(R.id.icon_view)
            private lateinit var resolveInfo: ResolveInfo

            init {
                nameTextView.setOnClickListener {
                    val activityInfo = resolveInfo.activityInfo
                    startActivity(Intent(Intent.ACTION_MAIN).setClassName(activityInfo.packageName, activityInfo.name).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }
            }

            fun bind(resolveInfo: ResolveInfo) {
                this.resolveInfo = resolveInfo
                val pm = activity!!.packageManager
                nameTextView.text = this.resolveInfo.loadLabel(pm).toString()
                iconImageView.setImageDrawable(resolveInfo.loadIcon(pm))
            }
        }
    }

    companion object {
        private const val TAG = "NerdLauncherFragment"

        fun newInstance() = NerdLauncherFragment()
    }
}