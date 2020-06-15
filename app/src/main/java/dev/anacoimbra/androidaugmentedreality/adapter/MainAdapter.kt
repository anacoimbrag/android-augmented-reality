package dev.anacoimbra.androidaugmentedreality.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.anacoimbra.androidaugmentedreality.R
import dev.anacoimbra.androidaugmentedreality.activity.AugmentedImageActivity
import dev.anacoimbra.androidaugmentedreality.activity.CloudAnchorActivity
import dev.anacoimbra.androidaugmentedreality.activity.NoArActivity
import dev.anacoimbra.androidaugmentedreality.activity.ObjectPlacementActivity
import kotlinx.android.synthetic.main.main_item.view.*

class MainAdapter(private val hasAr: Boolean = false) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.main_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = typesCount

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    private val typesCount: Int
        get() = if (hasAr) 4 else 1

    private val typeNoAr: Int
        get() = if (hasAr) 2 else 0

    companion object {
        private const val TYPE_OBJECT_PLACEMENT = 0
        private const val TYPE_AUGMENTED_IMAGE = 1
        private const val TYPE_CLOUD_ANCHOR = 3
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() = with(itemView) {
            when (adapterPosition) {
                TYPE_OBJECT_PLACEMENT -> {
                    title.setText(R.string.title_object_placement)
                    title.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        R.drawable.ic_object_placement,
                        0,
                        0
                    )
                    title.setOnClickListener {
                        ObjectPlacementActivity.start(context)
                    }
                }
                TYPE_AUGMENTED_IMAGE -> {
                    title.setText(R.string.title_augmented_image)
                    title.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        R.drawable.ic_augmented_image,
                        0,
                        0
                    )
                    title.setOnClickListener {
                        AugmentedImageActivity.start(context)
                    }
                }
                typeNoAr -> {
                    title.setText(R.string.title_no_ar)
                    title.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        R.drawable.ic_no_ar,
                        0,
                        0
                    )
                    title.setOnClickListener {
                        NoArActivity.start(context)
                    }
                }
                TYPE_CLOUD_ANCHOR -> {
                    title.setText(R.string.title_cloud_anchor)
                    title.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        R.drawable.ic_cloud_anchor,
                        0,
                        0
                    )
                    title.setOnClickListener {
                        CloudAnchorActivity.start(context)
                    }
                }
            }
        }
    }
}