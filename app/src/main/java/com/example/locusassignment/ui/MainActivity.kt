package com.example.locusassignment.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locusassignment.R
import com.example.locusassignment.data.ItemData
import com.example.locusassignment.databinding.ActivityMainBinding
import com.example.locusassignment.ui.adapter.ClickEventsHolder
import com.example.locusassignment.ui.adapter.MainAdapter
import com.example.locusassignment.ui.viewmodel.MainViewModel
import com.example.locusassignment.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), ClickEventsHolder {
    private lateinit var screenBinding: ActivityMainBinding
    private val mainViewModel by viewModel<MainViewModel>()
    private var tempUri: Uri? = null
    private var editableListPosition by Delegates.notNull<Int>()
    private lateinit var list: ArrayList<ItemData>
    private val mainAdapter by lazy { MainAdapter(this) }
    private val getCameraImage =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                tempUri?.let { uri ->
                    val newList = ArrayList(mainAdapter.currentList)
                    newList[editableListPosition].dataMap?.options = ArrayList()
                    newList[editableListPosition].dataMap?.options?.addAll(listOf(uri.toString()))
                    updateAdapter(newList)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenBinding =
            DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main)
                .apply {
                    rvLoadedData.apply {
                        layoutManager = LinearLayoutManager(
                            this@MainActivity,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        val itemDecorator =
                            DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
                        ContextCompat.getDrawable(
                            this@MainActivity,
                            R.drawable.custom_white_divider
                        )?.let {
                            itemDecorator.setDrawable(
                                it
                            )
                        }
                        addItemDecoration(itemDecorator)
                        adapter = mainAdapter
                    }
                }
        observeViewModel()
    }

    private fun observeViewModel() {
        mainViewModel.getItemList().observe(this) { response ->
            updateAdapter(response as ArrayList<ItemData>)
            // mainAdapter.submitList(response)
        }
    }

    private fun updateAdapter(response: ArrayList<ItemData>?) {
        list = ArrayList()
        if (response != null) {
            list.addAll(response)
        }
        mainAdapter.submitList(list.toList())

    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            this@MainActivity.getTempFileUri().let { uri ->
                tempUri = uri
                getCameraImage.launch(uri)
            }
        }
    }

    override fun onDeleteImageClicked(listPosition: Int) {
        /*  editableListPosition = listPosition
          deleteDataFromAdapter()*/
        Toast.makeText(this, "deleted at $listPosition", Toast.LENGTH_SHORT).show()
    }

    private fun deleteDataFromAdapter() {
        val newList = ArrayList(mainAdapter.currentList)
        newList[editableListPosition].dataMap?.options?.clear()
        updateAdapter(newList)
    }

    override fun onOptionSelected(listPosition: Int, groupPosition: Int) {

    }

    override fun onProvideCommentSelected(listPosition: Int) {
    }

    override fun pickImageFromCamera(listPosition: Int) {
        editableListPosition = listPosition
        takeImage()
    }
}