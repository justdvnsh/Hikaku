package divyansh.tech.hikaku.home.screens.compare

import android.Manifest
import android.R.attr.path
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.hikaku.BuildConfig
import divyansh.tech.hikaku.common.EventObserver
import divyansh.tech.hikaku.databinding.FragmentCompareBinding
import divyansh.tech.hikaku.utils.PermissionChecker
import divyansh.tech.hikaku.utils.diff_match_patch
import timber.log.Timber
import java.io.File
import java.net.URI


@AndroidEntryPoint
class CompareFragment: Fragment() {

    private lateinit var _binding: FragmentCompareBinding
    val binding get() = _binding

    private val viewModel by viewModels<CompareViewModel>()

    private val args by navArgs<CompareFragmentArgs>()

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
        extractData()
        setupObservers()
    }

    private fun setupObservers() {

        viewModel.navigation.observe(
            viewLifecycleOwner,
            EventObserver {
                findNavController().navigate(it)
            }
        )
    }

    private fun extractData() {
        try {
            var parsedText = ""
            val reader = PdfReader(args.filePath)
            val n: Int = reader.numberOfPages
            for (i in 0 until n) {
                parsedText += PdfTextExtractor.getTextFromPage(reader, i + 1).trim() + "\n"
            }
            Timber.e("TEXT FROM DOC-> $parsedText")
            reader.close()
        } catch (e: Exception) {
            println(e)
        }
        Toast.makeText(requireContext(), args.filePath, Toast.LENGTH_SHORT).show()
    }

//    private fun compareResult() {
//        if (text1 == "") return
//        if (text2 == "") return
//        val diff = diff_match_patch()
//        diff.Diff_Timeout = 1F
//        diff.Diff_EditCost = 4
//        val d = diff.diff_main(text1, text2)
//        Timber.e("PRETTY HTML -> ${diff.diff_prettyHtml(d)}")
//        binding.webView.loadDataWithBaseURL(null, diff.diff_prettyHtml(d), "text/html","utf-8", null)
//    }
}