package divyansh.tech.hikaku.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.daimajia.easing.linear.Linear
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.hikaku.common.EventObserver
import divyansh.tech.hikaku.databinding.FragmentHomeBinding
import divyansh.tech.hikaku.home.callbacks.HomeCallbacks
import divyansh.tech.hikaku.home.epoxy.EpoxyHomeController

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    val binding get() = _binding

    private val viewModel by viewModels<HomeViewModel>()

    private val controller by lazy {
        EpoxyHomeController(HomeCallbacks(viewModel))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.homeRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = controller.adapter
        }
    }

    private fun setupObservers() {

        viewModel.pdfLiveData.observe(
            viewLifecycleOwner,
            Observer {
                controller.setData(it)
            }
        )

        viewModel.navigation.observe(
            viewLifecycleOwner,
            EventObserver {
                findNavController().navigate(it)
            }
        )
    }
}