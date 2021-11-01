package divyansh.tech.hikaku.home.screens.compare

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.hikaku.common.EventObserver
import divyansh.tech.hikaku.databinding.FragmentCompareBinding


@AndroidEntryPoint
class CompareFragment: Fragment() {

    private lateinit var _binding: FragmentCompareBinding
    val binding get() = _binding

    private val viewModel by viewModels<CompareViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompareBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {

        viewModel.buttonClickLiveData.observe(
            viewLifecycleOwner,
            EventObserver {
                var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
                chooseFile.type = "application/pdf"
                chooseFile = Intent.createChooser(chooseFile, "Choose a file")
                startActivityForResult(chooseFile, 1000)
            }
        )

        viewModel.navigation.observe(
            viewLifecycleOwner,
            EventObserver {
                findNavController().navigate(it)
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data!!.data
            val src: String? = uri?.path
            src?.let {
                Toast.makeText(requireContext(), "$src", Toast.LENGTH_SHORT).show()
            }
        }
    }

}