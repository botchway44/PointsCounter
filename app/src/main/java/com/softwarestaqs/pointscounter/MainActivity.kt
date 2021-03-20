package com.softwarestaqs.pointscounter

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.util.Log.VERBOSE
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.softwarestaqs.pointscounter.ui.theme.PointsCounterTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.random.Random

import androidx.compose.runtime.livedata.observeAsState

class NumberGameViewModel : ViewModel() {

   fun compareValues(isLeft : Boolean){
        if(isLeft && this._leftValue?.value!! > this._rightValue?.value!!){

            _points.value = _points?.value?.plus(1)

        }else if(!isLeft && this._leftValue?.value!! < this._rightValue?.value!!){
            _points.value = _points?.value?.minus(1)
        }

       this.seedValues()
    }
  private fun generateRandomNumber(): Int {
      return Random(100).nextInt(0,100);
    }

    fun seedValues(){
        this._leftValue.value = this.generateRandomNumber();
        this._rightValue.value = this.generateRandomNumber();
    }

    // LiveData holds state which is observed by the UI
    // (state flows down from ViewModel)
    private val _points = MutableLiveData(0)
    private val _leftValue = MutableLiveData(0)
    private val _rightValue = MutableLiveData(0)

    val points: LiveData<Int> = _points
    val leftValue: LiveData<Int> = _points
    val rightValue: LiveData<Int> = _points

}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
           MaterialTheme(


           ) {
              Scaffold(
                content = {
                    PointsCounterTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(color = MaterialTheme.colors.background) {
                            Scaffold() {
                                DefaultPreview()
                            }
                        }
                    }
                }
              )
           }
        }
    }
}

@Composable
fun TopAppBar(
    backgroundColor : Color,
){
    
    Row() {
        
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview( ) {

    val gameViewModel: NumberGameViewModel  = viewModel();
    gameViewModel.seedValues()

    PointsCounterTheme {



        Column(
            modifier  = Modifier.fillMaxHeight(),
            verticalArrangement =  Arrangement.SpaceBetween,
            horizontalAlignment =  Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement =  Arrangement.SpaceBetween,
                horizontalAlignment =  Alignment.CenterHorizontally,
            ) {

                Text( text = "Bigger Number Game", fontSize = 8.em )

                Text("Press the button with the larger number. If you get it right, you gain a point. If you get it wrong you loose a point.")
            }

//            Spacer( modifier = Modifier.padding(all = Dp(20f)))

           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(horizontal = 20.dp),
               horizontalArrangement =  Arrangement.SpaceBetween
           ) {
               Button(
                   
                   onClick = { gameViewModel.compareValues(true) }) {

                   val value : State<Int> = gameViewModel.leftValue.observeAsState(0)
                   Text(text = value.value.toString())
               }

               Button(
                   onClick = { gameViewModel.compareValues(false) }) {
                   val value : State<Int> = gameViewModel.rightValue.observeAsState(0)

                   Text(text = value.value.toString())
               }
           }

            val value : State<Int> = gameViewModel.rightValue.observeAsState(0)

            Text(text = "Points : $"+ value.value.toString())
        }
    }
}

