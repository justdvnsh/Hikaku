package divyansh.tech.hikaku.home.screens.compare

import android.Manifest
import android.R.attr.dial
import android.R.attr.path
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.hikaku.BuildConfig
import divyansh.tech.hikaku.common.EventObserver
import divyansh.tech.hikaku.databinding.FragmentCompareBinding
import divyansh.tech.hikaku.utils.PdfView
import divyansh.tech.hikaku.utils.PermissionChecker
import divyansh.tech.hikaku.utils.diff_match_patch
import io.github.lucasfsc.html2pdf.Html2Pdf
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
        setupObservers()
        setupWebView()
        setupFabButton()
    }

    private fun setupWebView() {
        Timber.e("HTML DATA -> ${args.html}")
        binding.webView.loadDataWithBaseURL(null, args.html, "text/html", "UTF-8", null)
    }

    private fun setupFabButton() {
        binding.download.setOnClickListener {
            val path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Stock Transfer"
            val fileName = "Test.pdf"
            val dir = File(path);
            if (!dir.exists())
                dir.mkdirs()
            val file = File(dir, fileName)
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("File Exported")
                .setMessage("Do you want to open the file ?")

            PdfView.createWebPdfJob(requireActivity(), binding.webView, file, fileName, object : PdfView.Callback {
                override fun onSuccess(path: String) {
                    dialog.setPositiveButton("Open") {dialog, button ->
                            viewModel.changeNavigation(
                                CompareFragmentDirections.actionCompareFragmentToReaderFragment(path)
                            )
                        }
                    dialog.setNegativeButton("Cancel") {dialog, _ ->
                        dialog.dismiss()
                    }
                    dialog.show()
                }

                override fun onFailure() {
                    // do something
                }

            })
        }
    }

    private fun setupObservers() {
        viewModel.navigation.observe(
            viewLifecycleOwner,
            EventObserver {
                findNavController().navigate(it)
            }
        )
    }
}