package com.caiosilva.tagsearcher

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.caiosilva.tagsearcher.data.model.Items
import com.caiosilva.tagsearcher.view.viewmodel.ImagesViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ImagesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                SearchImageComponent(
                    modifier = Modifier.fillMaxSize(),
                    viewModel = viewModel,
                    onClick = ::handleClickOnIntem
                )
                ShowLoading(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
                ShowError(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    private fun handleClickOnIntem(item: Items) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("item", item)

        startActivity(intent)
    }
}

@Composable
fun SearchImageComponent(
    modifier: Modifier = Modifier,
    viewModel: ImagesViewModel,
    onClick: (Items) -> Unit
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Column {
            Text(
                text = "Search for images",
                modifier = Modifier.padding(12.dp)
            )
            SearchComponent(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                viewModel = viewModel
            )
            ImageListComponent(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding),
                onClick = onClick
            )
        }
    }
}

@Composable
fun SearchComponent(
    modifier: Modifier = Modifier,
    viewModel: ImagesViewModel
) {
    val query by viewModel.query.collectAsState(initial = "")

    Column {
        TextField(
            value = query,
            onValueChange = { viewModel.updateQuery(it) },
            label = { Text("Enter search term") },
            modifier = modifier.padding(16.dp)
        )
    }
}

@Composable
fun ImageListComponent(
    modifier: Modifier = Modifier,
    viewModel: ImagesViewModel,
    onClick: (Items) -> Unit
) {
    val responseData by viewModel.responseData.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
    ) {
        items(responseData?.items?.size ?: 0) { index ->
            val image = responseData?.items?.get(index)
            Column {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClick.invoke(image ?: return@clickable)
                        },
                    model = image?.media?.m.orEmpty(),
                    contentDescription = responseData?.description.orEmpty(),
                )
            }
        }
    }
}

@Composable
fun ShowLoading(
    modifier: Modifier,
    viewModel: ImagesViewModel
) {
    val isLoading by viewModel.isLoading.collectAsState(false)
    if (isLoading) {
        Box(
            modifier = modifier
                .wrapContentSize(Alignment.Center)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = modifier
                    .wrapContentSize(Alignment.Center)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 16.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    text = "Loading..."
                )
                CircularProgressIndicator(
                )
            }
        }
    }
}

@Composable
fun ShowError(
    modifier: Modifier,
    viewModel: ImagesViewModel
) {
    val errorMessage by viewModel.errorMessage.collectAsState("")
    if (errorMessage.isNotEmpty()) {
        Box(
            modifier = modifier
                .wrapContentSize(Alignment.Center)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                text = errorMessage
            )
        }
    }
}