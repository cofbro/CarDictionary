package com.example.cardictionary.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cardictionary.R
import com.example.cardictionary.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by viewModels()


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _binding!!.kemu1.setOnClickListener {
            val action = HomeFragmentDirections.actionNavHomeToChoiceFragment(1)
            findNavController().navigate(action)
        }

        _binding!!.km4.setOnClickListener {
            val action = HomeFragmentDirections.actionNavHomeToChoiceFragment(4)
            findNavController().navigate(action)
        }

        _binding!!.onlineExam.setOnClickListener {
            homeViewModel.showTipDialog(requireContext(), this, requireActivity())
        }

        _binding!!.km2.setOnClickListener {
            Toast.makeText(requireContext(), "科目二视频等待更新中", Toast.LENGTH_SHORT).show()
        }

        _binding!!.km3.setOnClickListener {
            Toast.makeText(requireContext(), "科目三视频等待更新中", Toast.LENGTH_SHORT).show()
        }

        _binding!!.searchView.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_gameQuestionFragment)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.loadTopScore(requireContext(), binding)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}