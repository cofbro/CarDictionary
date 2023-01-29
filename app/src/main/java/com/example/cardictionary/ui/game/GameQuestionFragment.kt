package com.example.cardictionary.ui.game

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.cardictionary.data.adapter.SlideCardAdapter
import com.example.cardictionary.data.model3.Data
import com.example.cardictionary.data.network.Constants
import com.example.cardictionary.databinding.FragmentGameQuestionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameQuestionFragment : Fragment() {
    private val gameQuestionViewModel: GameQuestionViewModel by viewModels()
    private lateinit var binding: FragmentGameQuestionBinding
    private val slideCardAdapter = SlideCardAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameQuestionBinding.inflate(layoutInflater, container, false)

        gameQuestionViewModel.setJokeContent {
            slideCardAdapter.setDataJokePair(it)
            binding.recyclerView.apply {
                adapter = slideCardAdapter
                layoutManager = CardLayoutManager()
                val itemDecoration = ItemDecoration(requireContext(), gameQuestionViewModel)
                addItemDecoration(itemDecoration)

                val callback = SliderCardCallback(slideCardAdapter)
                val itemTouchHelper = ItemTouchHelper(callback)
                itemTouchHelper.attachToRecyclerView(this)

            }
        }


        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //binding.recyclerView.addOnItemTouchListener(ItemTouchListener(itemDecoration.getRectF()))
//        binding.image.setOnClickListener {
//            setJokeContent { data ->
//                slideCardAdapter.setDataJokePair(data)
//            }
//            ObjectAnimator.ofFloat(it, "rotation", 0f, 720f).apply {
//                interpolator = AccelerateDecelerateInterpolator()
//                duration = 1000
//                start()
//            }
//        }
    }





}