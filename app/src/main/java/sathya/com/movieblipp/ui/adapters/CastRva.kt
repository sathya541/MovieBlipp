package sathya.com.movieblipp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import sathya.com.movieblipp.BuildConfig
import sathya.com.movieblipp.R
import sathya.com.movieblipp.data.model.Crew
import sathya.com.movieblipp.databinding.ItemCastBinding
import sathya.com.movieblipp.utils.Constants

class CastRva(val context: Context, val crewList: ArrayList<Crew>) :
    RecyclerView.Adapter<CastRva.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemCastBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cast, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {

            if (position == 0) {
                binding.spacingStart.visibility = View.VISIBLE
            } else if (position == Math.min(20, crewList.size) - 1) {
                binding.spacingEnd.visibility = View.VISIBLE
            } else {
                binding.spacingEnd.visibility = View.GONE
                binding.spacingStart.visibility = View.GONE
            }

            binding.castImage.load(BuildConfig.ORIGINAL_IMAGE_URL + crewList[position].profile_path) {
                placeholder(Constants.actorPlaceHolder[position % 4])
                error(Constants.actorPlaceHolder[position % 4])
            }

            binding.castName.text = crewList[position].name

            binding.root.setOnClickListener {
                val bundle = bundleOf(Constants.crew to crewList[position])
                it.findNavController()
                    .navigate(R.id.action_movieDetailsFragment_to_actorDetailsFragment, bundle)
            }
        }
    }

    override fun getItemCount(): Int = Math.min(20, crewList.size)

}