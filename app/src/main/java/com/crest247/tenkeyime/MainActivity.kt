package com.crest247.tenkeyime

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.crest247.tenkeyime.ui.theme.TenKeyIMETheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
//		setContent {
//			TenKeyIMETheme {
//				// A surface container using the 'background' color from the theme
//				Surface(
//					modifier = Modifier.fillMaxSize(),
////					color = MaterialTheme.colorScheme.background
//				) {
////					Greeting("Android")
//					GreetingWithXmlLayout()
//				}
//			}
//		}
	}
}

@Composable
fun GreetingWithXmlLayout(modifier: Modifier = Modifier) {
	AndroidView(
		modifier = modifier,
		factory = { context: Context ->
			LayoutInflater.from(context).inflate(R.layout.activity_main, null) as LinearLayout
		}
	)
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
	Text(
		text = "Hello $name!",
		modifier = modifier
	)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	TenKeyIMETheme {
//		Greeting("Android")
		GreetingWithXmlLayout()
	}
}