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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import dagger.hilt.android.AndroidEntryPoint
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

    private lateinit var readExternalStoragePermissionChecker: PermissionChecker
    private lateinit var manageExternalStoragePermissionChecker: PermissionChecker

    private var text1: String = ""
    private var text2: String = ""

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
                readExternalStoragePermissionChecker = PermissionChecker(
                    requireActivity(), binding.root,
                    101,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    "Read external storage access is required to read pdfs from sdcard."
                )

                if (readExternalStoragePermissionChecker.checkPermission()) {
                    when (it) {
                        CompareViewModel.BUTTONS.FIRST -> openFileChooser(1000)
                        CompareViewModel.BUTTONS.SECOND -> openFileChooser(1001)
                        else -> {}
                    }
                } else {
                    readExternalStoragePermissionChecker.requestPermission()
                }
            }
        )

        viewModel.navigation.observe(
            viewLifecycleOwner,
            EventObserver {
                findNavController().navigate(it)
            }
        )
    }

    private fun openFileChooser(code: Int) {
        var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
        chooseFile.type = "application/pdf"
        chooseFile = Intent.createChooser(chooseFile, "Choose a file")
        startActivityForResult(chooseFile, code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            getDoc(CompareViewModel.BUTTONS.FIRST, data)
        }
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            getDoc(CompareViewModel.BUTTONS.SECOND, data)
        }
    }

    private fun getDoc(button: CompareViewModel.BUTTONS, data: Intent?) {
        val uri: Uri? = data!!.data
        val src: String? = uri?.path?.substringAfter(":")
        src?.let {
            try {
                var parsedText = ""
                val reader = PdfReader("file:///$it")
                val n: Int = reader.numberOfPages
                for (i in 0 until n) {
                    parsedText += PdfTextExtractor.getTextFromPage(reader, i + 1).trim() + "\n"
                }
                Timber.e("TEXT FROM DOC-> $parsedText")
                if (button == CompareViewModel.BUTTONS.FIRST) text1 = parsedText
                if (button == CompareViewModel.BUTTONS.SECOND) text2 = parsedText
                compareResult()
                reader.close()
            } catch (e: Exception) {
                println(e)
            }
            Toast.makeText(requireContext(), "$src", Toast.LENGTH_SHORT).show()
        }
    }

    private fun compareResult() {
        if (text1 == "") return
        if (text2 == "") return
        val diff = diff_match_patch()
        diff.Diff_Timeout = 1F
        diff.Diff_EditCost = 4
        val d = diff.diff_main(text1, text2)
        Timber.e("PRETTY HTML -> ${diff.diff_prettyHtml(d)}")
        binding.webView.loadDataWithBaseURL(null, diff.diff_prettyHtml(d), "text/html","utf-8", null)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (readExternalStoragePermissionChecker.onRequestPermissionsResult(requestCode, grantResults)) {
//            openFileChooser()
        }
        if (manageExternalStoragePermissionChecker.onRequestPermissionsResult(requestCode, grantResults)) {
//            openFileChooser()
        }
    }
}