package com.example.voyaassessment.presentation.update

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.voyaassessment.R
import com.example.voyaassessment.data.model.remote.Categories
import com.example.voyaassessment.data.model.remote.FoodDetails
import com.example.voyaassessment.presentation.details.ChipsTextField
import com.example.voyaassessment.presentation.details.CreateFoodViewModel
import com.example.voyaassessment.utils.ApiResponse
import com.example.voyaassessment.utils.CustomLoadingBar
import com.example.voyaassessment.utils.Route
import com.example.voyaassessment.utils.SuccessDialog
import com.example.voyaassessment.utils.Tools

@Composable
fun EditFoodScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    createFoodViewModel: CreateFoodViewModel = hiltViewModel(),
    foodDetails: FoodDetails
) {
    val context = LocalContext.current
    // State for image selection and text fields
    var selectedImages by remember { mutableStateOf(listOf<Uri>()) }
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var calories by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCategory by remember { mutableStateOf("") }
    var categories by remember { mutableStateOf(emptyList<Categories.CategoriesData>()) }
    var categoryId by remember { mutableIntStateOf(Int.MAX_VALUE) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var tags by remember { mutableStateOf(listOf<String>()) }
    var isLoading by remember { mutableStateOf(false) }
    var showDialogSuccess by remember { mutableStateOf(false) }

    // Observe API responses
    val categoriesState by createFoodViewModel.categoriesState.collectAsState()
    val updateFoodResponse by createFoodViewModel.updateFoodState.collectAsState()


    val isButtonEnabled = name.text.isNotEmpty() &&
            description.text.isNotEmpty() &&
            tags.isNotEmpty() &&
            selectedCategory.isNotEmpty() &&
            selectedImages.isNotEmpty()


    val launcherCamera =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                val uri = Tools.saveImageToInternalStorage(context, it)
                selectedImages = selectedImages + uri
            }
        }

    val launcherGallery =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImages = selectedImages + it
            }
        }


    LaunchedEffect(Unit) {
        createFoodViewModel.getCategories()
    }

    // Prefill state when foodDetails is available.
    LaunchedEffect(foodDetails) {
        foodDetails.let { details ->
            if (name.text.isEmpty()) {
                name = TextFieldValue(details.data?.name ?: "")
            }
            if (description.text.isEmpty()) {
                description = TextFieldValue(details.data?.description ?: "")
            }
            if (calories.text.isEmpty()) {
                calories = TextFieldValue(details.data?.calories?.toString() ?: "")
            }
            if (selectedCategory.isEmpty()) {
                // Assuming details.data has a category name property
                selectedCategory = details.data?.category?.name ?: ""
                categoryId = details.data?.category?.id ?: 0
            }
            if (selectedImages.isEmpty()) {
                details.data?.foodImages?.let { imagesList ->
                    selectedImages = imagesList.mapNotNull { imageData ->
                        if (!imageData.imageUrl.isNullOrEmpty()) Uri.parse(imageData.imageUrl) else null
                    }
                }
            }
            if (tags.isEmpty()) {
                tags = details.data?.foodTags!!
            }
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(top = 40.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(R.drawable.arrow_square_right),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(25.dp)
                )
            }
            Text(
                text = stringResource(R.string.addnewfood),
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFF1D2433),
                modifier = Modifier.padding(start = 8.dp),
                fontSize = 20.sp,
            )
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { launcherCamera.launch(null) }
                    .padding(8.dp)
                    .height(90.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFFE4E7EC)),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.camera),
                        contentDescription = "Take photo"
                    )
                    Text(
                        text = "Take photo",
                        color = Color(0xFF1D2433),
                        modifier = Modifier.padding(top = 5.dp),
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { launcherGallery.launch("image/*") }
                    .padding(8.dp)
                    .height(90.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Gray),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.uploadsimple),
                        contentDescription = "Upload"
                    )
                    Text(
                        text = "Upload",
                        color = Color(0xFF1D2433),
                        modifier = Modifier.padding(top = 5.dp),
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display selected images
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(selectedImages) { uri ->
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .padding(8.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .fillMaxSize()
                    )
                    IconButton(
                        onClick = { selectedImages = selectedImages - uri },
                        modifier = Modifier
                            .size(25.dp)
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 5.dp, end = 5.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.closeselectedimage),
                            contentDescription = "Remove Image"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isLoading) {
            CustomLoadingBar(
                message = "Updating Food",
                imageResId = R.drawable.loading
            )
        }


        if (showDialogSuccess) {
            Dialog(
                onDismissRequest = { showDialogSuccess = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SuccessDialog(
                        title = (updateFoodResponse as ApiResponse.Success).data?.message.toString(),
                        subtitle = "",
                        buttonText = "Continue",
                        onButtonClick = {
                            navController.navigate(Route.FOOD_HOME_SCREEN) {
                                popUpTo(Route.FOOD_HOME_SCREEN) { inclusive = true }
                            }
                            showDialogSuccess = false
                        }
                    )

                }
            }
        }

        // loading indicator when updating food

        when (updateFoodResponse) {
            is ApiResponse.Failure -> {
                isLoading = false
                Tools.showToast(context, (updateFoodResponse as ApiResponse.Failure).message)
            }

            ApiResponse.Idle -> {
                isLoading = false
            }

            ApiResponse.Loading -> {
                isLoading = true
            }

            is ApiResponse.Success -> {
                isLoading = false
                showDialogSuccess = true
            }
        }


        // Name Field
        Text(
            text = "Name",
            color = Color(0xFF1D2433),
            fontFamily = FontFamily.SansSerif,
            fontSize = 15.sp
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter food name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Description Field
        Text(
            text = "Description",
            color = Color(0xFF1D2433),
            fontFamily = FontFamily.SansSerif,
            fontSize = 15.sp
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Enter food description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(105.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Category Dropdown
        Text(text = "Category")
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { isDropdownExpanded = !isDropdownExpanded }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_drop_down),
                            contentDescription = "Dropdown"
                        )
                    }
                }
            )
            when (val state = categoriesState) {
                is ApiResponse.Idle -> {}
                is ApiResponse.Loading -> {
                    CircularProgressIndicator()
                }

                is ApiResponse.Success -> {
                    categories = state.data?.data ?: emptyList()
                }

                is ApiResponse.Failure -> {

                }
            }
            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(onClick = {
                        selectedCategory = category.name.toString()
                        categoryId = category.id!!
                        isDropdownExpanded = false
                    }) {
                        Text(text = category.name.toString())
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Calories Field
        Text(text = "Calories")
        OutlinedTextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Enter number of Calories") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Tags label
        Text(text = "Tags")
        var typedText by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            ChipsTextField(
                tags = tags,
                typedText = typedText,
                onTextChange = { typedText = it },
                onTagAdd = { newTag ->
                    tags = tags + newTag
                    typedText = ""
                },
                onTagRemove = { tagToRemove ->
                    tags = tags - tagToRemove
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            Text(
                text = "Press enter once you've typed a tag",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                modifier = Modifier.padding(top = 4.dp)
            )
        }


        Button(
            onClick = {
                val files = selectedImages.mapNotNull { uri ->
                    Tools.uriToFile(context, uri)
                }
                foodDetails.data?.id?.let {
                    createFoodViewModel.updateFood(
                        it,
                        name = name.text,
                        description = description.text,
                        categoryId = categoryId,
                        calories = calories.text,
                        tags = tags,
                        imageFiles = files
                    )
                }
            },
            enabled = isButtonEnabled,
            colors = buttonColors(
                containerColor = if (isButtonEnabled) Color(0xFF0D6EFD) else Color(0xFF0E7F0FF),
                contentColor = Color.White
            ),
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(top = 15.dp)
        ) {
            Text(text = "Update Food")
        }
    }
}

@Preview
@Composable
private fun EditFoodScreenPreview() {
    val navController = rememberNavController()

}
