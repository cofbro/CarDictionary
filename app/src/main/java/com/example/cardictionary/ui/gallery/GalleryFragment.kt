package com.example.cardictionary.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardictionary.R
import com.example.cardictionary.data.adapter.GalleryWrongTitleAdapter
import com.example.cardictionary.databinding.FragmentGalleryBinding
import com.example.cardictionary.ui.gallery.topic.RedoViewModel

class GalleryFragment : Fragment() {
    private val galleryViewModel: GalleryViewModel by activityViewModels()
    private val redoViewModel: RedoViewModel by activityViewModels()
    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryWrongTitleAdapter = GalleryWrongTitleAdapter()

        galleryViewModel.queryAllFromBackground()
        galleryViewModel.wrongTopicList.observe(viewLifecycleOwner) {
            Log.d("chy", "update")
            galleryWrongTitleAdapter.setSuitList(it)
            redoViewModel.setWrongTopicList(it)
        }
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.recyclerView.apply {
            galleryWrongTitleAdapter.setModel(galleryViewModel, binding)
            adapter = galleryWrongTitleAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        _binding!!.order.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            val menuInflater = popupMenu.menuInflater
            menuInflater.inflate(R.menu.order_menu, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener { item ->
                when (item!!.itemId) {
                    R.id.first_menu -> {
                        galleryWrongTitleAdapter.reversSuitList(false)
                        binding.order.text = "按答题时间由远到近"

                    }
                    R.id.second_menu -> {
                        galleryWrongTitleAdapter.reversSuitList(true)
                        binding.order.text = "按答题时间由近到远"
                    }
                }
                true
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.cleanAll.setOnClickListener {
            galleryViewModel.showTipDialog(requireContext())
        }

        binding.toggleBtn.setOnClickListener {
            val switch = it as SwitchCompat
            if (it.isChecked) {
                Log.d("chy","true")
                redoViewModel.setIsAutoDelete(true)
            } else {
                Log.d("chy","false")
                redoViewModel.setIsAutoDelete(false)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}