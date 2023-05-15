package kg.android.onemorestepapp.ui.stickers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import android.graphics.*
import kg.android.onemorestepapp.R
import kg.android.onemorestepapp.databinding.FragmentChestOpeningBinding
import kotlinx.android.synthetic.main.fragment_chest_opening.view.*
import android.animation.AnimatorSet

import android.animation.ObjectAnimator
import android.util.Base64

import android.view.animation.AccelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import kg.android.onemorestepapp.utils.ExtensionFunctions.hide
import kg.android.onemorestepapp.utils.ExtensionFunctions.show
import kg.android.onemorestepapp.viewmodels.ChestOpeningViewModel

@AndroidEntryPoint
class ChestOpeningFragment : Fragment() {
    private var _binding: FragmentChestOpeningBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChestOpeningViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChestOpeningBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ChestOpeningViewModel::class.java)

        binding.chestTop.setOnClickListener(View.OnClickListener {
            startAnimation()
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.chestOpeningResponse.observe(viewLifecycleOwner) { response ->
            if(response != null){
                binding.chestTop.hide()
                binding.chestBottom.hide()
                binding.gettingRewardTv.hide()
                binding.stickerLoadedTv.show()
                binding.uploadedStickerIv.show()
                if(response.alreadyHave) binding.stickerLoadedTv.text = getString(R.string.already_have_sticker)
                else binding.stickerLoadedTv.text = getString(R.string.new_sticker)
                val imageAsBytes: ByteArray = Base64.decode(response.sticker, Base64.DEFAULT)
                binding.uploadedStickerIv.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size))
            }
        }
    }

    private fun startAnimation(){
        var chestTopYStart = binding.chestTop.top
        var chestTopYEnd = binding.chestTop.top - 130

        val heightAnimator: ObjectAnimator = ObjectAnimator
            .ofFloat(binding.chestTop, "y", chestTopYStart.toFloat(), chestTopYEnd.toFloat())
            .setDuration(1500)
        heightAnimator.doOnEnd {
            binding.gettingRewardTv.show()
            viewModel.onEvent(ChestOpeningUiEvent.GetRandomSticker)

        }

        heightAnimator.interpolator = AccelerateInterpolator()

        val animatorSet = AnimatorSet()
        animatorSet

            .play(heightAnimator)

        animatorSet.start()
    }
}