package dev.anacoimbra.androidaugmentedreality

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.main_item.view.*

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.main_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = TYPES_COUNT

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    companion object {
        const val TYPES_COUNT = 4

        const val TYPE_OBJECT_PLACEMENT = 0
        const val TYPE_AUGMENTED_IMAGE = 1
        const val TYPE_AUGMENTED_FACE = 2
        const val TYPE_CLOUD_ANCHOR = 3
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
                }
                TYPE_AUGMENTED_FACE -> {
                    title.setText(R.string.title_augmented_face)
                    title.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        R.drawable.ic_augmented_face,
                        0,
                        0
                    )
                }
                TYPE_CLOUD_ANCHOR -> {
                    title.setText(R.string.title_cloud_anchor)
                    title.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        R.drawable.ic_cloud_anchor,
                        0,
                        0
                    )
                }
            }
            setOnClickListener {
            }
        }
    }
}