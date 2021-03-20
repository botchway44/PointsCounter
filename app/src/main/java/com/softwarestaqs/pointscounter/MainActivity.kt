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

import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.style.TextAlign
import java.util.*


/**
 * The NumberGameViewModel handles the logic of the Game
 * Comparisons, generators, and assignments.
 * */
class NumberGameViewModel : ViewModel() {

    /**
     * Comparator function for the button clicks
     * @param isLeft : Boolean
     */
    fun compareValues(isLeft : Boolean){

        //If Left is pressed and comparison is valid score a point
        if(isLeft && this._leftValue?.value!! > this._rightValue?.value!!)  _points.value = _points?.value?.plus(1)

        //If Right is pressed and comparison is valid score a point
        else if(!isLeft && this._leftValue?.value!! < this._rightValue?.value!!) _points.value = _points?.value?.plus(1)

        //Else deduct a point
        else _points.value = _points?.value?.minus(1)

       this.seedValues()
    }

    /***
     * A random Generator from the Java.util.* package
     */
    private fun generateRandomNumber(): Int {
        return Random().nextInt(100)
    }

    /***
     * Seeds the Left and right values with random numbers
     */
    fun seedValues(){
        this._leftValue.value = generateRandomNumber()
        this._rightValue.value = generateRandomNumber()
    }

    // LiveData holds state which is observed by the UI
    // (state flows down from ViewModel)
    private val _points = MutableLiveData(0)
    private val _leftValue = MutableLiveData(0)
    private val _rightValue = MutableLiveData(0)

    val points: LiveData<Int> = _points
    val leftValue: LiveData<Int> = _leftValue
    val rightValue: LiveData<Int> = _rightValue

}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
           MaterialTheme(

           ) {
              Scaffold(
                  topBar = { AppBar() },
                  bottomBar = {},
                    content = {
                        GameView()
                }
              )
           }
        }
    }
}

@Composable
fun AppBar(
){
    TopAppBar() {
        Row() {
            Text(text = "Bigger Number Game")
        }
    }
}

@Composable
fun GameView(
    gameViewModel: NumberGameViewModel  = viewModel()
){

    //Seed the game with random numbers
    gameViewModel.seedValues()

    PointsCounterTheme {

        Column(
            modifier  = Modifier.fillMaxHeight().padding(all = 4.dp),
            verticalArrangement =  Arrangement.SpaceBetween,
            horizontalAlignment =  Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement =  Arrangement.SpaceBetween,
                horizontalAlignment =  Alignment.CenterHorizontally,
            ) {

                Text( text = "Bigger Number Game", fontSize = 8.em ,textAlign = TextAlign.Center,)

                Text( modifier = Modifier.padding(horizontal = 10.dp) , textAlign = TextAlign.Center, text = "Press the button with the larger number. If you get it right, you gain a point. If you get it wrong you loose a point.")
            }

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

            val value : State<Int> = gameViewModel.points.observeAsState(0)

            Text(text = "Points : $"+ value.value.toString(), )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview( ) {

}

