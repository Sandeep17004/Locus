package com.example.locusassignment.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locusassignment.R
import com.example.locusassignment.data.helper.CommentTypeHelperClass
import com.example.locusassignment.data.CustomItemData
import com.example.locusassignment.data.helper.PhotoTypeHelperClass
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
    private lateinit var list: ArrayList<CustomItemData>
    private val mainAdapter = MainAdapter(this)
    private lateinit var getCameraImage: ActivityResultLauncher<Uri>


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.submit -> {
                Log.d("allUserData", mainAdapter.currentList.toString())
            }
        }
        return super.onOptionsItemSelected(item)
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
        getCameraImage =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                if (isSuccess) {
                    tempUri?.let { uri ->
                        updateRow(PhotoTypeHelperClass(imageUri = uri), null)
                    }
                }
            }
    }


    private fun observeViewModel() {
        mainViewModel.getItemList().observe(this) { response ->
            updateAdapter(ArrayList(response))
        }
    }

    private fun updateAdapter(response: ArrayList<CustomItemData>?) {
        mainAdapter.submitList(response)
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
        editableListPosition = listPosition
        deleteDataFromAdapter()
    }

    private fun deleteDataFromAdapter() {
        updateRow(PhotoTypeHelperClass(imageUri = null), null)
    }

    private fun updateRow(
        photoTypeHelperClass: PhotoTypeHelperClass?,
        commentTypeHelperClass: CommentTypeHelperClass?
    ) {
        val newList = ArrayList(mainAdapter.currentList.toMutableList())
        if (photoTypeHelperClass != null) {
            if (photoTypeHelperClass.imageUri == null) {
                val customItemData = CustomItemData(
                    CustomItemData.DataMap(
                        listOf(null)
                    ),
                    id = newList[editableListPosition].id,
                    title = newList[editableListPosition].title,
                    type = newList[editableListPosition].type
                )
                newList[editableListPosition] = customItemData

            } else {
                val customItemData = CustomItemData(
                    CustomItemData.DataMap(
                        listOf(
                            CustomItemData.DataMap.OptionData(optionName = photoTypeHelperClass.imageUri.toString())
                        )
                    ),
                    id = newList[editableListPosition].id,
                    title = newList[editableListPosition].title,
                    type = newList[editableListPosition].type
                )

                newList[editableListPosition] = customItemData
            }

        } else {
            if (commentTypeHelperClass != null) {
                if (commentTypeHelperClass.isSelected) {
                    val customItemData = CustomItemData(
                        CustomItemData.DataMap(
                            listOf(
                                CustomItemData.DataMap.OptionData(
                                    isSelected = commentTypeHelperClass.isSelected,
                                    optionName = "",
                                    text = commentTypeHelperClass.comment.toString()
                                )
                            )
                        ),
                        id = newList[editableListPosition].id,
                        title = newList[editableListPosition].title,
                        type = newList[editableListPosition].type
                    )
                    newList[editableListPosition] = customItemData
                } else {
                    val customItemData = CustomItemData(
                        CustomItemData.DataMap(
                            listOf(
                                null
                            )
                        ),
                        id = newList[editableListPosition].id,
                        title = newList[editableListPosition].title,
                        type = newList[editableListPosition].type
                    )
                    newList[editableListPosition] = customItemData
                }
            }
        }
        updateAdapter(newList)
    }

    override fun onProvideCommentSelected(listPosition: Int, isSwitchedOn: Boolean) {
        editableListPosition = listPosition
        updateRow(null, CommentTypeHelperClass(isSelected = isSwitchedOn))
    }

    override fun pickImageFromCamera(listPosition: Int) {
        editableListPosition = listPosition
        takeImage()
    }

    override fun onCommentTextChanged(listPosition: Int, comment: String) {
        val newList = ArrayList(mainAdapter.currentList)
        val optionData =
            CustomItemData.DataMap.OptionData(optionName = "", isSelected = true, text = comment)
        newList[editableListPosition].dataMap.options = listOf(optionData)
        updateAdapter(newList)
    }
}