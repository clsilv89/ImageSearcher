package com.caiosilva.tagsearcher

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.caiosilva.tagsearcher.data.model.Items
import com.example.myapplication.ui.theme.MyApplicationTheme

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val item: Items = getItemsFromParcel() ?: Items()
        setContent {
            MyApplicationTheme {
                SearchImageComponent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    items = item,
                    onLinkClicked = {
                        openWebPage(it)
                    },
                    onButtonClicked = {
                        shareInformation(it)
                    }
                )
            }
        }
    }

    private fun getItemsFromParcel(): Items? {
        return if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= 33)
            intent.getParcelableExtra("item", Items::class.java) else
            intent.getParcelableExtra("item")
    }

    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun shareInformation(item: Items) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, item.toString())
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}

@Composable
fun SearchImageComponent(
    modifier: Modifier,
    items: Items,
    onLinkClicked: (String) -> Unit = {},
    onButtonClicked: (Items) -> Unit = {}
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = items.media?.m.orEmpty(),
            contentDescription = items.description.orEmpty(),
        )
        Text(text = stringResource(R.string.title_tag, items.title.toString()))
        Text(
            text = stringResource(R.string.link_tag, items.link.toString()),
            modifier = Modifier.clickable {
                onLinkClicked(items.link.orEmpty())
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )
        Text(text = stringResource(R.string.description_tag, items.description.toString()))
        Text(text = stringResource(R.string.published_tag, items.published.toString()))
        Text(text = stringResource(R.string.author_tag, items.author.toString()))
        Button(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            onClick = { onButtonClicked(items) }) {
            Text(stringResource(R.string.share_information_tag))
        }
    }
}
