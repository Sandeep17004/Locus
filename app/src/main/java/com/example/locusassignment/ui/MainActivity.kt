package com.example.locusassignment.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locusassignment.R
import com.example.locusassignment.data.CommentModelClass
import com.example.locusassignment.data.CustomDataItem
import com.example.locusassignment.data.ItemDataType
import com.example.locusassignment.data.PhotoModelClass
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
    private lateinit var list: ArrayList<CustomDataItem>
    private val mainAdapter = MainAdapter(this)
    private lateinit var getCameraImage: ActivityResultLauncher<Uri>

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
                        updateRow(PhotoModelClass(imageUri = uri), null)
                    }
                }
            }
    }


    private fun observeViewModel() {
        mainViewModel.getItemList().observe(this) { response ->
            updateAdapter(ArrayList(response))
        }
    }

    private fun updateAdapter(response: ArrayList<CustomDataItem>?) {
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
        Toast.makeText(this, "deleted at $listPosition", Toast.LENGTH_SHORT).show()
    }

    private fun deleteDataFromAdapter() {
        updateRow(PhotoModelClass(imageUri = null), null)
    }

    private fun updateRow(
        photoModelClass: PhotoModelClass?,
        commentModelClass: CommentModelClass?
    ) {
        if (photoModelClass != null) {
            val newList = ArrayList(mainAdapter.currentList.toMutableList())
            if (photoModelClass.imageUri == null) {
                val customDataItem = CustomDataItem(
                    CustomDataItem.DataMap(
                        listOf(null)
                    ),
                    id = newList[editableListPosition].id,
                    title = newList[editableListPosition].title,
                    type = newList[editableListPosition].type
                )
                newList[editableListPosition] = customDataItem
                updateAdapter(newList)
            } else {
                val customDataItem = CustomDataItem(
                    CustomDataItem.DataMap(
                        listOf(
                            CustomDataItem.DataMap.OptionData(optionName = photoModelClass.imageUri.toString())
                        )
                    ),
                    id = newList[editableListPosition].id,
                    title = newList[editableListPosition].title,
                    type = newList[editableListPosition].type
                )

                newList[editableListPosition] = customDataItem
            }
            updateAdapter(newList)
        } else {
            if (commentModelClass != null) {
                val newList = ArrayList(mainAdapter.currentList.toMutableList())
                if (commentModelClass.isSelected) {
                    val customDataItem = CustomDataItem(
                        CustomDataItem.DataMap(
                            listOf(
                                CustomDataItem.DataMap.OptionData(
                                    isSelected = commentModelClass.isSelected,
                                    optionName = "", text = commentModelClass.comment.toString()
                                )
                            )
                        ),
                        id = newList[editableListPosition].id,
                        title = newList[editableListPosition].title,
                        type = newList[editableListPosition].type
                    )

                    newList[editableListPosition] = customDataItem
                } else {
                    val customDataItem = CustomDataItem(
                        CustomDataItem.DataMap(
                            listOf(
                                null
                            )
                        ),
                        id = newList[editableListPosition].id,
                        title = newList[editableListPosition].title,
                        type = newList[editableListPosition].type
                    )
                    newList[editableListPosition] = customDataItem
                }
                updateAdapter(newList)
            }
        }

    }

    override fun onProvideCommentSelected(listPosition: Int, isSwitchedOn: Boolean) {
        editableListPosition = listPosition
        updateRow(null, CommentModelClass(isSelected = isSwitchedOn))
    }

    override fun pickImageFromCamera(listPosition: Int) {
        editableListPosition = listPosition
        takeImage()
    }

    override fun onCommentTextChanged(listPosition: Int, comment: String) {
        val newList = ArrayList(mainAdapter.currentList)
        val optionData =
            CustomDataItem.DataMap.OptionData(optionName = "", isSelected = true, text = comment)
        newList[editableListPosition].dataMap.options = listOf(optionData)
        updateAdapter(newList)


        //updateRow(null, CommentModelClass(comment = comment, isSelected = true))
    }
}