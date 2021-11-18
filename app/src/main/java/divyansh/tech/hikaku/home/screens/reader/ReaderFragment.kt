package divyansh.tech.hikaku.home.screens.reader

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.hikaku.databinding.FragmentReaderBinding
import java.io.File

@AndroidEntryPoint
class ReaderFragment: Fragment() {

    private lateinit var _binding: FragmentReaderBinding

    private val args by navArgs<ReaderFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReaderBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPDFView()
    }

    private fun setupPDFView() {
        val file = File(args.path)
        val uri = Uri.fromFile(file)
        _binding.pdfView.fromUri(uri).load()
    }
}