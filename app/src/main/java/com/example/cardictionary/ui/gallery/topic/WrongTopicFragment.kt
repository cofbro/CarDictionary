package com.example.cardictionary.ui.gallery.topic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.example.cardictionary.data.adapter.WrongTopicPagerAdapter
import com.example.cardictionary.databinding.FragmentWrongTopicBinding
import com.example.cardictionary.ui.gallery.GalleryViewModel
import com.google.android.material.tabs.TabLayoutMediator

class WrongTopicFragment : Fragment() {
    private lateinit var binding: FragmentWrongTopicBinding
    private val redoViewModel: RedoViewModel by activityViewModels()
    private val args: WrongTopicFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWrongTopicBinding.inflate(inflater, container, false)
        redoViewModel.locatedNum = args.located
        val wrongTopicPagerAdapter =
            WrongTopicPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        wrongTopicPagerAdapter.setPager2(listOf(RedoFragment(), ReciteFragment()))
        binding.viewPager2.apply {
            adapter = wrongTopicPagerAdapter
            isUserInputEnabled = false
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, postion ->
            when (postion) {
                0 -> tab.text = "答题"
                1 -> tab.text = "背题"
            }
        }.attach()



        return binding.root
    }

}