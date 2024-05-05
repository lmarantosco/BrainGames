package com.example.coroutinestest2

import android.R.attr.defaultValue
import android.graphics.Paint
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coroutinestest2.ui.HelperClasses.NewPolygonShape
import com.example.coroutinestest2.ui.theme.CoroutinesTest2Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Collections
import kotlin.random.Random
import kotlin.random.nextInt


class MainActivity : ComponentActivity() {

    var listOfDeferredTasks = ArrayList<Deferred<Any>>()
    var listOfJobs = ArrayList<Job>()
    var SECONDS_RUNNING : Int = 0
    var listOfNumbers = ArrayList<Numbers>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var scope = rememberCoroutineScope()
            CoroutinesTest2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    createDeferredTasks()
                    LaunchedEffect(key1 = true, block = {
                        scope.launch {
                            createJobTasks()
                        }
                    } )
//                    createUIBox()
                    selectGameToPlay()
                }
            }
        }
    }

    @Composable
    private fun selectGameToPlay()
    {
        var firstGameRunning = remember {
            mutableStateOf<Boolean>(false)
        }

        var secondGameRunning = remember {
            mutableStateOf<Boolean>(false)
        }

        var thirdGameRunning = remember {
            mutableStateOf<Boolean>(false)
        }

        var forthGameRunning = remember {
            mutableStateOf<Boolean>(false)
        }

        if ((firstGameRunning.value == false) && (secondGameRunning.value == false) && (thirdGameRunning.value == false) && (forthGameRunning.value == false))
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement = Arrangement.Center)
            {
                Text(
                    text = "SELECT GAME TO PLAY",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "GAME 1",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                    ,modifier = Modifier
                        .clickable {
                            firstGameRunning.value = true
                            secondGameRunning.value = false
                            thirdGameRunning.value = false
                            forthGameRunning.value = false
                        }
                        .background(color = randomCardColor())
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "GAME 2",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                    ,modifier = Modifier
                        .clickable {
                            firstGameRunning.value = false
                            secondGameRunning.value = true
                            thirdGameRunning.value = false
                            forthGameRunning.value = false
                        }
                        .background(color = randomCardColor())
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "GAME 3",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                    ,modifier = Modifier
                        .clickable {
                            firstGameRunning.value = false
                            secondGameRunning.value = false
                            thirdGameRunning.value = true
                            forthGameRunning.value = false
                        }
                        .background(color = randomCardColor())
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "GAME 4",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                    ,modifier = Modifier
                        .clickable {
                            firstGameRunning.value = false
                            secondGameRunning.value = false
                            thirdGameRunning.value = false
                            forthGameRunning.value = true
                        }
                        .background(color = randomCardColor())
                )
            }
        }
        else
        {
            intializeGame(firstGameRunning, secondGameRunning, thirdGameRunning , forthGameRunning)
        }


    }

    @Composable
    private fun intializeGame(
        firstGameRunning: MutableState<Boolean>,
        secondGameRunning: MutableState<Boolean>,
        thirdGameRunning : MutableState<Boolean>,
        forthGameRunning : MutableState<Boolean>
    ) {

        var screenWidth = resources.displayMetrics.widthPixels
        var screenHeight = resources.displayMetrics.heightPixels

        // FIRST GAME STATE VARIABLES

        var gameTime = remember {
            mutableStateOf<Long>(0)
        }

        var stageNumber = remember {
            mutableStateOf<Int>(0)
        }




        var stageRunning = remember {
            mutableStateOf<Boolean>(true)
        }

        var endOfRound = remember {
            mutableStateOf<Boolean>(false)
        }

        var numOfWins = remember {
            mutableStateOf<Int>(0)
        }

        var listOfCardNumbers = remember {
            mutableListOf<Int>(15, 40, 20, 10, 30)
        }

        var winningNumber = remember {
            mutableStateOf<Int>(chooseRandomNumber(listOfCardNumbers))
        }

        var totalGameScore = remember {
            mutableStateOf<Int>(0)
        }

        var numOfLifes = remember {
            mutableStateOf<Int>(3)
        }

        // SECOND GAME STATE VARIABLES
        var secondGameRoundEnded = remember{
            mutableStateOf<Boolean>(false)
        }

        var secondGameStageNumber = remember {
            mutableStateOf<Int>(1)
        }

        var numOfCards = remember {
            mutableStateOf<Int>(8)
        }

        var listOfMemoryNums = remember {
            mutableListOf<Int>()
        }

        var pairsScore = remember {
            mutableStateOf<Int>(0)
        }

        var secondGameNumOfLifes = remember {
            mutableStateOf<Int>(numOfCards.value)
        }

        //THRID GAME VARIABLES

        var thirdGameTime = remember {
            mutableStateOf<Long>(0)
        }

        var thirdStageNumber = remember {
            mutableStateOf<Int>(0)
        }


        var thirdEndOfRound = remember {
            mutableStateOf<Boolean>(false)
        }

        var thirdNumOfWins = remember {
            mutableStateOf<Int>(0)
        }

        var thirdListOfCardNumbers = remember {
            mutableListOf<Int>(15, 40, 20, 10, 30)
        }

        var thirdEquationAction = remember {
            mutableStateOf<Int>(0)
        }

        var thirdWinningNumber = remember {
            mutableStateOf<Int>(numExpression(20 , 0 , 0))
        }

        var thrirdWinningList = remember {
            mutableListOf<Int>(20 ,10 , 0 , 20)
        }

        var thirdTotalGameScore = remember {
            mutableStateOf<Int>(0)
        }

        var thirdNumOfLifes = remember {
            mutableStateOf<Int>(3)
        }

        //FORTH GAME VARIABLES
        var forthGameTime = remember {
            mutableStateOf<Long>(0)
        }

        var forthStageNumber = remember {
            mutableStateOf<Int>(0)
        }

        var forthEndOfRound = remember {
            mutableStateOf<Boolean>(false)
        }

        var forthNumOfWins = remember {
            mutableStateOf<Int>(0)
        }

        var forthItemList = remember {
            mutableListOf<Int>((Color.Red).toArgb() , (Color.Green).toArgb() , (Color.Yellow).toArgb() , (Color.Blue).toArgb() , (Color.White).toArgb())
        }

        var winningBox = remember {
            mutableStateOf<Int>(0)
        }

        var forthTotalGameScore = remember {
            mutableStateOf<Int>(0)
        }

        var forthNumOfLifes = remember {
            mutableStateOf<Int>(3)
        }

        var wordOrColor = remember {
            mutableStateOf<Int>(0)
        }

        var word = remember{
            mutableStateOf<String>("")
        }

        var color = remember{
            mutableStateOf<Color>(Color.White)
        }

        var forthShapeList = remember {
            mutableListOf<Shape>(RoundedCornerShape(10.dp) , RoundedCornerShape(10.dp), RoundedCornerShape(10.dp), RoundedCornerShape(10.dp) , RoundedCornerShape(10.dp))
        }

        var restartBoxColorsMethod = remember {
            mutableStateOf<Boolean>(false)
        }



        if (forthStageNumber.value == 0)
        {

            var itemsColoredDifferent = false
            while (!itemsColoredDifferent)
            {
                forthItemList[0] = randomCardColorModifier().toArgb()
                forthItemList[1] = randomCardColorModifier().toArgb()
                forthItemList[2] = randomCardColorModifier().toArgb()
                forthItemList[3] = randomCardColorModifier().toArgb()
                forthItemList[4] = randomCardColorModifier().toArgb()

                itemsColoredDifferent = isListUnique(forthItemList)
            }
            var wordColor : Int = 0
            wordColor = Random.nextInt(0 ,2)
            var scolor = randomCardColorModifier()
            var sword = randomCardColorToWord()
            while (sword == getColorName(Color(forthItemList.get(winningBox.value))))
            {
                sword = randomCardColorToWord()
            }
            while (scolor == Color(forthItemList.get(winningBox.value)))
            {
                scolor = randomCardColorModifier()
            }
            wordOrColor.value = wordColor
            color.value = scolor
            word.value = sword

            forthShapeList =  remember {
                mutableListOf<Shape>(generateRandomShape(
                    forthItemList.get(0),
                    sides = Random.nextInt(3, 10),
                    radius = Random.nextDouble(0.0, 10.0).toFloat()
                ),
                    generateRandomShape(
                        forthItemList.get(1),
                        sides = Random.nextInt(3, 10),
                        radius = Random.nextDouble(0.0, 10.0).toFloat()
                    ),
                    generateRandomShape(
                        forthItemList.get(2),
                        sides = Random.nextInt(3, 10),
                        radius = Random.nextDouble(0.0, 10.0).toFloat()
                    ),
                    generateRandomShape(
                        forthItemList.get(3),
                        sides = Random.nextInt(3, 10),
                        radius = Random.nextDouble(0.0, 10.0).toFloat()
                    ),
                    generateRandomShape(
                        forthItemList.get(4),
                        sides = Random.nextInt(3, 10),
                        radius = Random.nextDouble(0.0, 10.0).toFloat()
                    ))
            }
            restartBoxColorsMethod.value = true
        }

//        if (forthEndOfRound.value == true)
//        {
//            forthItemList[0] = randomCardColorModifier().toArgb()
//            forthItemList[1] = randomCardColorModifier().toArgb()
//            forthItemList[2] = randomCardColorModifier().toArgb()
//            forthItemList[3] = randomCardColorModifier().toArgb()
//            forthItemList[4] = randomCardColorModifier().toArgb()
//            var wordColor : Int = 0
//            wordColor = Random.nextInt(0 ,2)
//            var scolor = randomCardColorModifier()
//            var sword = randomCardColorToWord()
//            while (sword == getColorName(Color(forthItemList.get(winningBox.value))))
//            {
//                sword = randomCardColorToWord()
//            }
//            while (scolor == Color(forthItemList.get(winningBox.value)))
//            {
//                scolor = randomCardColorModifier()
//            }
//            wordOrColor.value = wordColor
//            color.value = scolor
//            word.value = sword
//
//            forthShapeList =  remember {
//                mutableListOf<Shape>(generateRandomShape(
//                    forthItemList.get(0),
//                    sides = Random.nextInt(3, 10),
//                    radius = Random.nextDouble(0.0, 10.0).toFloat()
//                ),
//                    generateRandomShape(
//                        forthItemList.get(1),
//                        sides = Random.nextInt(3, 10),
//                        radius = Random.nextDouble(0.0, 10.0).toFloat()
//                    ),
//                    generateRandomShape(
//                        forthItemList.get(2),
//                        sides = Random.nextInt(3, 10),
//                        radius = Random.nextDouble(0.0, 10.0).toFloat()
//                    ),
//                    generateRandomShape(
//                        forthItemList.get(3),
//                        sides = Random.nextInt(3, 10),
//                        radius = Random.nextDouble(0.0, 10.0).toFloat()
//                    ),
//                    generateRandomShape(
//                        forthItemList.get(4),
//                        sides = Random.nextInt(3, 10),
//                        radius = Random.nextDouble(0.0, 10.0).toFloat()
//                    ))
//            }
//        }

        var winningArrayList = ArrayList<Int>(4)


        //produce 20 stages
        LaunchedEffect(firstGameRunning)
        {
            while (firstGameRunning.value && endOfRound.value == false)
            {

                stageNumber.value = stageNumber.value + 1
                // Delay to simulate stage transitions

                while (endOfRound.value == false)
                {
                    updateTime(gameTime)
                }

            }
        }

        //produce 5 stages for the second game
//        LaunchedEffect(secondGameRunning)
//        {
//            while (secondGameRunning.value && secondGameRoundEnded.value == false)
//            {
//
//                stageNumber.value = stageNumber.value + 1
//                // Delay to simulate stage transitions
//
//                while (secondGameRoundEnded.value == false)
//                {
//                    updateTime(gameTime)
//                }
//
//            }
//        }

//        if (secondGameNumOfLifes.value == 0)
//        {
//            secondGameRunning.value = false
//        }

//        LaunchedEffect(key1 = secondGameRunning.value, block =
//        {
//            listOfMemoryNums = createListOfNumsForMemory(numOfCards.value)
//            listOfMemosCalculated.value = true
//        })

        //produce 10 stages
        LaunchedEffect(thirdGameRunning)
        {
            while (thirdGameRunning.value && thirdEndOfRound.value == false)
            {

                thirdStageNumber.value = thirdStageNumber.value + 1
                // Delay to simulate stage transitions

                while (thirdEndOfRound.value == false)
                {
                    updateTime(thirdGameTime)
                }

            }
        }

        //forth game
        //produce 10 stages
        LaunchedEffect(forthGameRunning)
        {
            while (forthGameRunning.value && forthEndOfRound.value == false)
            {

                // Delay to simulate stage transitions

                while (forthEndOfRound.value == false)
                {
                    updateTime(forthGameTime)
                }
   //             forthStageNumber.value = forthStageNumber.value + 1
            }
        }

        if (firstGameRunning.value == true)
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement = Arrangement.Top)
            {
                // Displaying game info
                if ((endOfRound.value == false) && (firstGameRunning.value == true)) {
                    // This calls the composable when the signal is set
                    Text("CLICK THE RIGHT BOX NUMBER")
                    Text("Stage: ${stageNumber.value}")
                    createFallingBoxes(
                        stageNumber,
                        firstGameRunning,
                        endOfRound,
                        numOfWins,
                        listOfCardNumbers,
                        winningNumber,
                        gameTime,
                        totalGameScore,
                        numOfLifes
                    )

                }
                if (endOfRound.value == true)
                {
                    stageNumber.value = stageNumber.value + 1
                    if ((stageNumber.value == 10) || (numOfLifes.value == 0))
                    {
//                        firstGameRunning.value = false
//                        secondGameRunning.value = true
                        selectGameToPlay()
                        firstGameRunning.value = false
                        secondGameRunning.value = false
                        thirdGameRunning.value = false
                        forthGameRunning.value = false
                    }
                    listOfCardNumbers = rearrangeNumValues(listOfCardNumbers)
                    winningNumber.value = chooseRandomNumber(listOfCardNumbers)
                    endOfRound.value = false
                }
            }
        }
        if (secondGameRunning.value == true)
        {
            if ((secondGameRoundEnded.value == false) && (secondGameRunning.value == true))
            {
                Column()
                {
                    Text("MEMORY GAME")

                    Text("Stage: ${secondGameStageNumber.value}")
                    Text(
                        text = "SCORE : ${pairsScore.value}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "LIFES : ${secondGameNumOfLifes.value}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    createMemoryBoard(
                        numOfCards = numOfCards.value * secondGameStageNumber.value,
                        listOfMemoryNums = listOfMemoryNums,
                        roundEnded = secondGameRoundEnded,
                        pairsScore,
                        secondGameNumOfLifes
                    )
                    Spacer(modifier = Modifier.height(200.dp))
                }
            }
            if (secondGameRoundEnded.value == true)
            {
                secondGameStageNumber.value = secondGameStageNumber.value + 1
                secondGameNumOfLifes.value = secondGameStageNumber.value * 8
                if ((secondGameStageNumber.value == 6) || (secondGameNumOfLifes.value == 0))
                {
                    firstGameRunning.value = false
                    secondGameRunning.value = false
                    thirdGameRunning.value = false
                    forthGameRunning.value = false
                    selectGameToPlay()
                }
                secondGameRunning.value = true
                secondGameRoundEnded.value = false
            }
        }
        if (thirdGameRunning.value == true)
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement = Arrangement.Top)
            {
                // Displaying game info
                if ((thirdEndOfRound.value == false) && (thirdGameRunning.value == true)) {
                    // This calls the composable when the signal is set
                    Text("CLICK THE RIGHT BOX NUMBER")
                    Text("Stage: ${thirdStageNumber.value}")
                    createEquationBoxes(
                        thirdStageNumber,
                        thirdGameRunning,
                        thirdEndOfRound,
                        thirdNumOfWins,
                        thirdListOfCardNumbers,
                        thirdWinningNumber,
                        thirdGameTime,
                        thirdTotalGameScore,
                        thirdNumOfLifes,
                        winningEqualtion = thrirdWinningList
                    )

                }
                if (thirdEndOfRound.value == true)
                {
                    thirdStageNumber.value = thirdStageNumber.value + 1
                    if ((thirdStageNumber.value == 10) || (thirdNumOfLifes.value ==0))
                    {
                        firstGameRunning.value = false
                        secondGameRunning.value = false
                        thirdGameRunning.value = false
                        forthGameRunning.value = false
                        selectGameToPlay()
//                        firstGameRunning.value = true
//                        secondGameRunning.value = false
//                        thirdGameRunning.value = false
                    }
                    thirdListOfCardNumbers = rearrangeNumValues(thirdListOfCardNumbers)
                    winningArrayList = produceNumberEquation(thirdListOfCardNumbers)
                    var randomWinningCard = Random.nextInt(0 , 5)
                    thrirdWinningList.set(0 , winningArrayList.get(0))
                    thrirdWinningList.set(1 , winningArrayList.get(1))
                    thrirdWinningList.set(2 , winningArrayList.get(2))
                    thrirdWinningList.set(3 , winningArrayList.get(3))
                    thirdWinningNumber.value = winningArrayList.get(3)
                    thirdListOfCardNumbers.set(randomWinningCard , thrirdWinningList.get(3))
                    thirdEndOfRound.value = false
                }
            }
        }
        if (forthGameRunning.value == true)
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement = Arrangement.Top)
            {
                // Displaying game info
                if ((forthEndOfRound.value == false) && (forthGameRunning.value == true)) {

                    // This calls the composable when the signal is set
                    Text("CLICK THE RIGHT BOX")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = if (wordOrColor.value == 0) "Click the name of the color specified by the word"
                    else "Click the color of the word")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Stage: ${forthStageNumber.value}")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("LIFES: ${forthNumOfLifes.value}")
                    Spacer(modifier = Modifier.height(10.dp))
                    if (wordOrColor.value == 0)
                    {
                        Text(
                            text = "${getColorName(Color(forthItemList.get(winningBox.value)))}",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = color.value
                        )
                    }
                    else
                    {
                        Text(
                            text = "${word.value}",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(forthItemList.get(winningBox.value))
                        )
                    }
                }
//                if (forthShapeList.size == 0)
//                {
//                    forthItemList[0] = randomCardColorModifier().toArgb()
//                    forthItemList[1] = randomCardColorModifier().toArgb()
//                    forthItemList[2] = randomCardColorModifier().toArgb()
//                    forthItemList[3] = randomCardColorModifier().toArgb()
//                    forthItemList[4] = randomCardColorModifier().toArgb()
//                    var wordColor : Int = 0
//                    wordColor = Random.nextInt(0 ,2)
//                    var scolor = randomCardColorModifier()
//                    var sword = randomCardColorToWord()
//                    while (sword == getColorName(Color(forthItemList.get(winningBox.value))))
//                    {
//                        sword = randomCardColorToWord()
//                    }
//                    while (scolor == Color(forthItemList.get(winningBox.value)))
//                    {
//                        scolor = randomCardColorModifier()
//                    }
//                    wordOrColor.value = wordColor
//                    color.value = scolor
//                    word.value = sword
//
//                    forthShapeList[0] = generateRandomShape(
//                        forthItemList.get(0),
//                        sides = Random.nextInt(3, 10),
//                        radius = Random.nextDouble(0.0, 10.0).toFloat()
//                    )
//
//                    forthShapeList[1] = generateRandomShape(
//                        forthItemList.get(1),
//                        sides = Random.nextInt(3, 10),
//                        radius = Random.nextDouble(0.0, 10.0).toFloat()
//                    )
//
//                    forthShapeList[2] = generateRandomShape(
//                        forthItemList.get(2),
//                        sides = Random.nextInt(3, 10),
//                        radius = Random.nextDouble(0.0, 10.0).toFloat()
//                    )
//
//                    forthShapeList[3] = generateRandomShape(
//                        forthItemList.get(3),
//                        sides = Random.nextInt(3, 10),
//                        radius = Random.nextDouble(0.0, 10.0).toFloat()
//                    )
//
//                    forthShapeList[4] = generateRandomShape(
//                        forthItemList.get(4),
//                        sides = Random.nextInt(3, 10),
//                        radius = Random.nextDouble(0.0, 10.0).toFloat()
//                    )
//                }
                if (restartBoxColorsMethod.value == true)
                {
                    createColorsBoxes(
                        forthStageNumber,
                        forthGameRunning,
                        forthEndOfRound,
                        forthNumOfWins,
                        listOfColors = forthItemList,
                        winningBox,
                        forthGameTime,
                        forthTotalGameScore,
                        forthNumOfLifes,
                        forthShapeList,
                        wordOrColor,
                        word,
                        color)
                }
                }
                if (forthEndOfRound.value == true)
                {
//                    forthStageNumber.value = forthStageNumber.value + 1
                    if ((forthStageNumber.value == 10) || (forthNumOfLifes.value == 0))
                    {
                        firstGameRunning.value = false
                        secondGameRunning.value = false
                        thirdGameRunning.value = false
                        forthGameRunning.value = false
                        selectGameToPlay()
                    }

                    var itemsColoredDifferent = false
                    while (!itemsColoredDifferent)
                    {
                        forthItemList[0] = randomCardColorModifier().toArgb()
                        forthItemList[1] = randomCardColorModifier().toArgb()
                        forthItemList[2] = randomCardColorModifier().toArgb()
                        forthItemList[3] = randomCardColorModifier().toArgb()
                        forthItemList[4] = randomCardColorModifier().toArgb()

                        itemsColoredDifferent = isListUnique(forthItemList)
                    }

                    forthShapeList[0] = generateRandomShape(
                        forthItemList.get(0),
                        sides = Random.nextInt(3, 10),
                        radius = Random.nextDouble(0.0, 10.0).toFloat()
                    )
                    forthShapeList[1] = generateRandomShape(
                        forthItemList.get(1),
                        sides = Random.nextInt(3, 10),
                        radius = Random.nextDouble(0.0, 10.0).toFloat()
                    )
                    forthShapeList[2] = generateRandomShape(
                        forthItemList.get(2),
                        sides = Random.nextInt(3, 10),
                        radius = Random.nextDouble(0.0, 10.0).toFloat()
                    )
                    forthShapeList[3] = generateRandomShape(
                        forthItemList.get(3),
                        sides = Random.nextInt(3, 10),
                        radius = Random.nextDouble(0.0, 10.0).toFloat()
                    )
                    forthShapeList[4] = generateRandomShape(
                        forthItemList.get(4),
                        sides = Random.nextInt(3, 10),
                        radius = Random.nextDouble(0.0, 10.0).toFloat()
                    )
                    winningBox.value = Random.nextInt(0 , 5)
                    var wordColor : Int = 0
                    wordColor = Random.nextInt(0 ,2)
                    var scolor = randomCardColorModifier()
                    var sword = randomCardColorToWord()
                    while (sword == getColorName(Color(forthItemList.get(winningBox.value))))
                    {
                        sword = randomCardColorToWord()
                    }
                    while (scolor == Color(forthItemList.get(winningBox.value)))
                    {
                        scolor = randomCardColorModifier()
                    }
                    wordOrColor.value = wordColor
                    color.value = scolor
                    word.value =sword
                    forthEndOfRound.value = false
                    restartBoxColorsMethod.value = true
                }
            }
    }

    private  fun createDeferredTasks()
    {
        //coroutine scope to manage coroutines
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val def1 : Deferred<String> = async {
                delay(500)
                println("inside deferrable 1")
                var result = "Defered 1"
                return@async result
            }
            listOfDeferredTasks.add(def1)
            val def2 : Deferred<String> = async {
                delay(500)
                println("inside deferrable 2")
                var result = "Defered 2"
                return@async result
            }
            listOfDeferredTasks.add(def2)
            val def3 : Deferred<String> = async {
                delay(500)
                println("inside deferrable 3")
                var result ="Deferred 3"
                return@async result
            }
            listOfDeferredTasks.add(def3)


            var results = listOfDeferredTasks.map {
                it.await()
            }

            results.forEach {
                println(it)
            }
        }
    }

    private suspend fun createJobTasks()
    {
        var job = Job()
        var scope = CoroutineScope(Dispatchers.IO + job)

        var job1 = scope.launch {
            delay(500)
            println("inside job1")
        }

        var job2 = scope.launch {
            delay(500)
            println("inside job2")
        }

        var job3 = scope.launch {
            delay(500)
            println("inside job1")
        }

        listOfJobs.add(job1)
        listOfJobs.add(job2)
        listOfJobs.add(job3)

        listOfJobs.map { it.join() }
    }

    @Composable
    private fun createUIBox()
    {
        var intOffset = remember { mutableStateOf(50) } // Changed to use Dp
        var alpha = remember { mutableStateOf(0.5f) } // Initialized with a Float

        val offsetAnimate = animateDpAsState(
            targetValue = intOffset.value.dp, // Using Dp
            animationSpec = TweenSpec(durationMillis = 500)
        )

        val alphaAnimate = animateFloatAsState(
            targetValue = alpha.value,
            animationSpec = TweenSpec(durationMillis = 500)
        )


        Row(horizontalArrangement = Arrangement.Center , verticalAlignment = Alignment.CenterVertically)
        {
            Box(contentAlignment = Alignment.Center, content = {
                Text(text = "Click Me" , fontSize = 20.sp , fontWeight = FontWeight.Bold
                , modifier = Modifier.clickable {
                    intOffset.value = 100
                        alpha.value = 1f
                    })
            }, modifier = Modifier
                .offset(x = offsetAnimate.value, y = offsetAnimate.value)
                .graphicsLayer(alpha = alphaAnimate.value))
        }
    }

    private suspend fun updateTime(gameTime : MutableState<Long>)
    {
        delay(100)
        gameTime.value = gameTime.value + 100
    }

    @Composable
    private fun createFallingBoxes(
        stageNumber: MutableState<Int>,
        gameRunning: MutableState<Boolean>,
        endOfRound: MutableState<Boolean>,
        numOfWins: MutableState<Int>,
        listOfCardNumbers: MutableList<Int>,
        winningNumber: MutableState<Int>,
        gameTime: MutableState<Long>,
        totalGameScore: MutableState<Int>,
        numOfLifes: MutableState<Int>,
    ) {
        var screenWidth = resources.displayMetrics.widthPixels
        var screenHeight = resources.displayMetrics.heightPixels

        var secondsOnGame = remember {
            mutableStateOf<Int>(0)
        }

        var box1YOffsetDp = remember {
            mutableStateOf<Int>(0)
        }

        var box2YOffsetDp = remember {
            mutableStateOf<Int>(0)
        }

        var box3YOffsetDp = remember {
            mutableStateOf<Int>(0)
        }

        var box4YOffsetDp = remember {
            mutableStateOf<Int>(0)
        }

        var box5YOffsetDp = remember {
            mutableStateOf<Int>(0)
        }

        var opacityA = remember {
            mutableStateOf<Float>(0f)
        }

        var opacityB = remember {
            mutableStateOf<Float>(0f)
        }

        var opacityC = remember {
            mutableStateOf<Float>(0f)
        }

        var opacityD = remember {
            mutableStateOf<Float>(0f)
        }

        var opacityE = remember {
            mutableStateOf<Float>(0f)
        }

        var rotationDegreesA = remember {
            mutableStateOf(0)
        }

        var rotationDegreesB = remember {
            mutableStateOf(0)
        }

        var rotationDegreesC = remember {
            mutableStateOf(0)
        }

        var rotationDegreesD = remember {
            mutableStateOf(0)
        }

        var rotationDegreesE = remember {
            mutableStateOf(0)
        }

        var winOrLoose = remember {
            mutableStateOf<Boolean>(false)
        }

        var cardColors = remember {
            mutableListOf<Color>(Color.White , Color.White , Color.White , Color.White , Color.White)
        }

//        LaunchedEffect(Unit) {
//            setCountdownTimer(secondsOnGame)
//        }
//        setCountdownTimer(secondsOnGame)

        //recalculate the y offset of the boxes based on the timer

        if (endOfRound.value == false)
        {
//            box1YOffsetDp.value = (secondsOnGame.value * randomYOffsetFactor(stageNumber.value + 1) * 100)
//            box2YOffsetDp.value = (secondsOnGame.value * randomYOffsetFactor(stageNumber.value + 1) * 100)
//            box3YOffsetDp.value = (secondsOnGame.value * randomYOffsetFactor(stageNumber.value + 1) * 100)
//            box4YOffsetDp.value = (secondsOnGame.value * randomYOffsetFactor(stageNumber.value + 1) * 100)
//            box5YOffsetDp.value = (secondsOnGame.value * randomYOffsetFactor(stageNumber.value + 1) * 100)
        }

        //round ends if boxes move out of the screen
        if (box1YOffsetDp.value >= screenHeight + 50)
        {
            box1YOffsetDp.value = 0
            box2YOffsetDp.value = 0
            box3YOffsetDp.value = 0
            box4YOffsetDp.value = 0
            box5YOffsetDp.value = 0
           numOfLifes.value = numOfLifes.value - 1
            endOfRound.value = true
        }

        //lost the game
        if (numOfLifes.value == 0)
        {
            endOfRound.value = true
        }


        val box1AnimateDpAsState by animateIntOffsetAsState(
            targetValue = IntOffset(0 , box1YOffsetDp.value), // Adjust the offset value as needed
            animationSpec = tween(durationMillis = 500))

        val box2AnimateDpAsState by animateIntOffsetAsState(
            targetValue = IntOffset(0 , box2YOffsetDp.value), // Adjust the offset value as needed
            animationSpec = tween(durationMillis = 500))

        val box3AnimateDpAsState by animateIntOffsetAsState(
            targetValue = IntOffset(0 , box3YOffsetDp.value), // Adjust the offset value as needed
            animationSpec = tween(durationMillis = 500))

        val box4AnimateDpAsState by animateIntOffsetAsState(
            targetValue = IntOffset(0 , box4YOffsetDp.value), // Adjust the offset value as needed
            animationSpec = tween(durationMillis = 500))

        val box5AnimateDpAsState by animateIntOffsetAsState(
            targetValue = IntOffset(0 , box5YOffsetDp.value), // Adjust the offset value as needed
            animationSpec = tween(durationMillis = 500))

//        var box1AnimateDpAsState = animateDpAsState(targetValue = box1YOffsetDp.value , animationSpec = tween(durationMillis = 500))
//        var box2AnimateDpAsState = animateDpAsState(targetValue = box2YOffsetDp.value , animationSpec = tween(durationMillis = 500))
//        var box3AnimateDpAsState = animateDpAsState(targetValue = box3YOffsetDp.value , animationSpec = tween(durationMillis = 500))
//        var box4AnimateDpAsState = animateDpAsState(targetValue = box4YOffsetDp.value , animationSpec = tween(durationMillis = 500))
//        var box5AnimateDpAsState = animateDpAsState(targetValue = box5YOffsetDp.value , animationSpec = tween(durationMillis = 500))

        Column(verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally)
        {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(10.dp, 10.dp)
            )
            {

                Card(modifier = Modifier
                    .width(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .height(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .offset { box1AnimateDpAsState }
                    .rotate(rotationDegreesA.value.toFloat())
                    .alpha(opacityA.value)
                    .clickable {
                        if (listOfCardNumbers.get(0) == winningNumber.value) {
                            endOfRound.value = true
                            winOrLoose.value = true
                            numOfWins.value = numOfWins.value + 1
                            totalGameScore.value += 1000
                        } else {
                            winOrLoose.value = false
                            numOfLifes.value = numOfLifes.value - 1
                            endOfRound.value = true
                        }
                    },
                    colors = CardDefaults.cardColors(cardColors.get(0)) )
                {Text(
                    text = listOfCardNumbers.get(0).toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )}
                Card(modifier = Modifier
                    .width(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .height(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .offset { box2AnimateDpAsState }
                    .rotate(rotationDegreesB.value.toFloat())
                    .alpha(opacityB.value)
                    .clickable {
                        if (listOfCardNumbers.get(1) == winningNumber.value) {
                            endOfRound.value = true
                            winOrLoose.value = true
                            numOfWins.value = numOfWins.value + 1
                            totalGameScore.value = totalGameScore.value + 1000
                        } else {
                            winOrLoose.value = false
                            numOfLifes.value = numOfLifes.value - 1
                            endOfRound.value = true
                        }
                    },
                    colors = CardDefaults.cardColors(cardColors.get(1))
                )
                {
                    Text(
                        text = listOfCardNumbers.get(1).toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Card(modifier = Modifier
                    .width(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .height(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .offset { box3AnimateDpAsState }
                    .rotate(rotationDegreesC.value.toFloat())
                    .alpha(opacityC.value)
                    .clickable {
                        if (listOfCardNumbers.get(2) == winningNumber.value) {
                            endOfRound.value = true
                            winOrLoose.value = true
                            numOfWins.value = numOfWins.value + 1
                            totalGameScore.value = totalGameScore.value + 1000
                        } else {
                            winOrLoose.value = false
                            numOfLifes.value = numOfLifes.value - 1
                            endOfRound.value = true
                        }
                    },
                    colors = CardDefaults.cardColors(cardColors.get(2))
                )
                {
                    Text(
                        text = listOfCardNumbers.get(2).toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Card(modifier = Modifier
                    .width(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .height(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .offset { box4AnimateDpAsState }
                    .rotate(rotationDegreesD.value.toFloat())
                    .alpha(opacityD.value)
                    .background(color = cardColors.get(3))
                    .clickable {
                        if (listOfCardNumbers.get(3) == winningNumber.value) {
                            endOfRound.value = true
                            winOrLoose.value = true
                            numOfWins.value = numOfWins.value + 1
                            totalGameScore.value = totalGameScore.value + 1000
                        } else {
                            winOrLoose.value = false
                            numOfLifes.value = numOfLifes.value - 1
                            endOfRound.value = true
                        }
                    }
                ,
                    colors = CardDefaults.cardColors(cardColors.get(3)) )
                {
                    Text(
                        text = listOfCardNumbers.get(3).toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Card(modifier = Modifier
                    .width(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .height(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .offset { box5AnimateDpAsState }
                    .rotate(rotationDegreesE.value.toFloat())
                    .alpha(opacityE.value)
                    .clickable {
                        if (listOfCardNumbers.get(4) == winningNumber.value) {
                            endOfRound.value = true
                            winOrLoose.value = true
                            numOfWins.value = numOfWins.value + 1
                            totalGameScore.value = totalGameScore.value + 1000
                        } else {
                            winOrLoose.value = false
                            numOfLifes.value = numOfLifes.value - 1
                            endOfRound.value = true
                        }
                    },
                    colors = CardDefaults.cardColors(cardColors.get(4))
                )
                {
                    Text(
                        text = listOfCardNumbers.get(4).toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
            Spacer(modifier = Modifier.height(200.dp))
            Text(
                text = winningNumber.value.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "Number of Wins : ${numOfWins.value}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Total Score : ${totalGameScore.value}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "LIVES : ${numOfLifes.value}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (gameTime.value % 3000 == 0L)
        {
            rotationDegreesA.value = rotationDegree().toInt()
            rotationDegreesB.value = rotationDegree().toInt()
            rotationDegreesC.value = rotationDegree().toInt()
            rotationDegreesD.value = rotationDegree().toInt()
            rotationDegreesE.value = rotationDegree().toInt()

            opacityA.value = randomOpacity()
            opacityB.value = randomOpacity()
            opacityC.value = randomOpacity()
            opacityD.value = randomOpacity()
            opacityE.value = randomOpacity()

            box1YOffsetDp.value += (randomYOffsetFactor(1) * 20 * (stageNumber.value + 1)).toInt()
            box2YOffsetDp.value += (randomYOffsetFactor(1) * 20 * (stageNumber.value + 1)).toInt()
            box3YOffsetDp.value += (randomYOffsetFactor(1) * 20 * (stageNumber.value + 1)).toInt()
            box4YOffsetDp.value += (randomYOffsetFactor(1) * 20 * (stageNumber.value + 1)).toInt()
            box5YOffsetDp.value += (randomYOffsetFactor(1) * 20 * (stageNumber.value + 1)).toInt()



            cardColors[0] = randomCardColor()
            cardColors[1] = randomCardColor()
            cardColors[2] = randomCardColor()
            cardColors[3] = randomCardColor()
            cardColors[4] = randomCardColor()


            rearrangeNumbersList(listOfCardNumbers)
        }
    }

    @Composable
    private fun createMemoryBoard(
        numOfCards: Int,
        listOfMemoryNums: MutableList<Int>,
        roundEnded: MutableState<Boolean>,
        pairsScore: MutableState<Int>,
        secondGameNumOfLifes: MutableState<Int>,
    )
    {
        var showContentMemoryList = remember {
            MutableList<MutableState<Boolean>>(numOfCards) { mutableStateOf(false) }
        }

        var openedContentMemoryList = remember {
            MutableList<MutableState<Boolean>>(numOfCards) { mutableStateOf(false) }
        }


        if (listOfMemoryNums.size != numOfCards)
        {
            listOfMemoryNums.clear()
            listOfMemoryNums.addAll(createListOfNumsForMemory(numOfCards))
        }

        var squareRoot = numOfCards / Math.sqrt(numOfCards.toDouble()).toInt()
        Column(verticalArrangement = Arrangement.SpaceEvenly , horizontalAlignment = Alignment.CenterHorizontally)
        {
            for (i in 0..(numOfCards / squareRoot) - 1)
            {
                Row(horizontalArrangement = Arrangement.SpaceEvenly)
                {
                    for (j in 0.. squareRoot - 1)
                    {
                        memoryCard(
                            memoNum = (i * squareRoot) + j,
                            numOfCards = numOfCards,
                            showContent = showContentMemoryList.get((i * squareRoot) + j),
                            listOfMemoryNums = listOfMemoryNums,
                            showContentMemoryList = showContentMemoryList,
                            openContentMemoryList = openedContentMemoryList,
                            pairsScore = pairsScore,
                            roundEnded = roundEnded,
                            numOfLifes = secondGameNumOfLifes)
                    }
                }
            }
        }
    }

    @Composable
    fun memoryCard(
        memoNum: Int,
        numOfCards: Int,
        showContent: MutableState<Boolean>,
        listOfMemoryNums: MutableList<Int>,
        showContentMemoryList: MutableList<MutableState<Boolean>>,
        openContentMemoryList: MutableList<MutableState<Boolean>>,
        pairsScore: MutableState<Int>,
        roundEnded: MutableState<Boolean>,
        numOfLifes: MutableState<Int>,
    )
    {
        var screenWidth = resources.displayMetrics.widthPixels
        var screenHeight = resources.displayMetrics.heightPixels
        val cardSize = (screenWidth / (6 * resources.displayMetrics.scaledDensity)).dp

        val scope = rememberCoroutineScope()

        // Ensure correct context for AnimatedVisibility
            Card(
                modifier = Modifier
                    .size(cardSize)
                    .border(1.dp, Color.Black)
                    .clickable {
                        if (openContentMemoryList.get(memoNum).value == false) {
                            showContentMemoryList[memoNum].value = true
                            showContent.value = !showContent.value
                            scope.launch {
                                delayMemoCardView(400)
                            }
                            if (checkForCorrectMemoryPair(
                                    showContentMemoryList,
                                    openContentMemoryList,
                                    listOfMemoryNums,
                                    memoNum,
                                    numOfLifes
                                )
                            ) {
                                pairsScore.value = pairsScore.value + 1000
                                //check that all cards have been opened
                                if (openContentMemoryList.all { it.value == true }) {
                                    roundEnded.value = true
                                }
                            }
                        }
                    }
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    AnimatedVisibility(
                        visible = showContent.value,
                        enter = expandIn(),
                        exit = shrinkOut(),
                    ) {
                        Text(
                            text = "${listOfMemoryNums.get(memoNum)}",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
    }

    private fun checkForCorrectMemoryPair(
        showContentMemoryList: MutableList<MutableState<Boolean>>,
        openContentMemoryList: MutableList<MutableState<Boolean>>,
        listOfMemoryNums: MutableList<Int>,
        memoNum: Int,
        numOfLifes: MutableState<Int>,
    ) : Boolean
    {
        //calculate if only two memoCards are open
        val scope = CoroutineScope(Dispatchers.Main)
        var correctPair : Boolean = false
        var memoCardsOpen : Int = 0
        var memoPair: Pair<Int?, Int?> = Pair(null, null)
        for (i in 0..listOfMemoryNums.size - 1)
        {
            showContentMemoryList[memoNum].value = true
            if ((openContentMemoryList.get(i).value == false) && (showContentMemoryList.get(i).value == true))
            {
//                if ((openContentMemoryList.get(i-3).value == false) && (showContentMemoryList.get(i-3).value == true))
//                {
//                    showContentMemoryList.get(i-3).value = false
//                }
//                if (i != memoNum)
//                {
//                    showContentMemoryList[i].value = false
//                }

                memoCardsOpen += 1
                if (memoCardsOpen == 1)
                {
                    memoPair = memoPair.copy(first = i , second = null)
                }
                if (memoCardsOpen == 2)
                {
                    memoPair = memoPair.copy(first = memoPair.first, second = i)
                }
            }
        }
        if (memoCardsOpen >= 1)
        {
            if ((memoPair.first != null) && (memoPair.second != null))
            {
                //check for some number in both open memo cards
                if (listOfMemoryNums.get(memoPair.first!!) == listOfMemoryNums.get(memoPair.second!!))
                {
                    correctPair = true
                    openContentMemoryList.get(memoPair.first!!).value = true
                    openContentMemoryList.get(memoPair.second!!).value = true
                }
                else
                {
                    scope.launch {
                        delayMemoCardView(400)
                        numOfLifes.value = numOfLifes.value -1
                        openContentMemoryList.get(memoPair.first!!).value = false
                        openContentMemoryList.get(memoPair.second!!).value = false
                        showContentMemoryList.get(memoPair.first!!).value = false
                        showContentMemoryList.get(memoPair.second!!).value = false
                    }
                }
            }
        }
        return correctPair
    }



    //HELPER PRIVATE METHODS

    private fun chooseRandomNumber(listOfCardNumbers: MutableList<Int>): Int {
        var randomIndex = kotlin.random.Random.nextInt(0 , 4)
        return listOfCardNumbers.get(randomIndex)
    }

    private fun rearrangeNumValues(listOfCardNumbers: MutableList<Int>) : MutableList<Int>
    {
        for (i in 0..listOfCardNumbers.size -1)
        {
            listOfCardNumbers[i] = kotlin.random.Random.nextInt(0 , 50)
        }
        return listOfCardNumbers
    }


    private fun randomYOffsetFactor(limit : Int) : Float
    {
        return kotlin.random.Random.nextDouble(0.0 , limit.toDouble()).toFloat()
    }

    private fun randomXOffsetFactor(start : Int , limit : Int) : Int
    {
        return kotlin.random.Random.nextInt(start , limit).toInt()
    }

    private fun rotationDegree() : Float
    {
        return kotlin.random.Random.nextInt(0 , 360).toFloat()
    }

    private fun randomOpacity() : Float
    {
        return kotlin.random.Random.nextDouble(0.0 , 1.0).toFloat()
    }

    private fun randomCardColor() : Color
    {
        var randomCounter = kotlin.random.Random.nextInt(0 , 4)
        var resultColor : Color = Color.White
        when (randomCounter)
        {
            0 ->
            {
                resultColor = Color.Gray
            }
            1 ->
            {
                resultColor = Color.LightGray
            }
            2 ->
            {
                resultColor = Color.Magenta
            }
            3 ->
            {
                resultColor = Color.White
            }
            4 ->
            {
                resultColor = Color.Blue
            }
            5 ->
            {
                resultColor = Color.Cyan
            }
            6 ->
            {
                resultColor = Color.Green
            }
            7 ->
            {
                resultColor = Color.Red
            }
        }
        return resultColor
    }

    private fun randomCardColorModifier() : Color
    {
        var randomCounter = kotlin.random.Random.nextInt(0 , 8)
        var resultColor : Color = Color.White
        when (randomCounter)
        {
            0 ->
            {
                resultColor = Color.Gray
            }
            1 ->
            {
                resultColor = Color.LightGray
            }
            2 ->
            {
                resultColor = Color.Magenta
            }
            3 ->
            {
                resultColor = Color.White
            }
            4 ->
            {
                resultColor = Color.Blue
            }
            5 ->
            {
                resultColor = Color.Cyan
            }
            6 ->
            {
                resultColor = Color.Green
            }
            7 ->
            {
                resultColor = Color.Red
            }
        }
        return resultColor
    }

    private fun randomCardColorToWord() : String
    {
        var randomCounter = kotlin.random.Random.nextInt(0 , 4)
        var resultColor : String = "White"
        when (randomCounter)
        {
            0 ->
            {
                resultColor = "Gray"
            }
            1 ->
            {
                resultColor = "Lightgray"
            }
            2 ->
            {
                resultColor = "Magenta"
            }
            3 ->
            {
                resultColor = "White"
            }
            4 ->
            {
                resultColor = "Blue"
            }
            5 ->
            {
                resultColor = "Cyan"
            }
            6 ->
            {
                resultColor = "Green"
            }
            7 ->
            {
                resultColor = "Red"
            }
        }
        return resultColor
    }

    private fun generateRandomShape(scolor : Int , sides : Int, radius : Float) : Shape
    {
        var randomScapeCounter : Int = 0
        var shape : Shape = RectangleShape
        randomScapeCounter = Random.nextInt(0 , 4)

        //values for roundrectshape
        // Define the radii for each corner (top-left, top-right, bottom-right, bottom-left)
        val cornerRadii = floatArrayOf(
            Random.nextInt(10 , 40).toFloat(), Random.nextInt(10 , 40).toFloat(), // Top-left corner
            Random.nextInt(10 , 40).toFloat(), Random.nextInt(10 , 40).toFloat(), // Top-right corner
            Random.nextInt(10 , 40).toFloat(), Random.nextInt(10 , 40).toFloat(), // Bottom-right corner
            Random.nextInt(10 , 40).toFloat(), Random.nextInt(10 , 40).toFloat()  // Bottom-left corner
        )

        var paint = Paint().apply {
            color = scolor
            style = Paint.Style.FILL // You can use STROKE to draw outlines
        }

        when (randomScapeCounter)
        {
            0 ->
            {
                shape =  RectangleShape
            }

            1 ->
            {
                shape = RoundedCornerShape(radius)
            }

            2 ->
            {
                shape = CircleShape
            }
            3 ->
            {
                shape = NewPolygonShape(sides , radius)
            }
        }
        return shape
    }

    private fun createListOfNumsForMemory(numOfCards : Int) : MutableList<Int>
    {
        var listOfNums = MutableList<Int>(numOfCards) { 0 }
        // Initialize ArrayList with n elements, all set to defaultValue
        // Initialize ArrayList with n elements, all set to defaultValue
        val firstArray = ArrayList<Int>(Collections.nCopies<Int>(numOfCards / 2, 0))
        for (i in 0..firstArray.size - 1)
        {
            firstArray.set(i , i)
        }
        firstArray.shuffle()
        val secondArray = ArrayList<Int>(Collections.nCopies<Int>(numOfCards / 2, 0))
        for (i in 0..secondArray.size - 1)
        {
            secondArray.set(i , i)
        }
        secondArray.shuffle()
        listOfNums.clear()
        listOfNums.addAll(firstArray)
        listOfNums.addAll(secondArray)


        return listOfNums
    }

    private suspend fun delayMemoCardView(delayFactor : Long)
    {
        delay(delayFactor)
    }

    private fun rearrangeNumbersList(numbers : MutableList<Int>)
    {
        numbers.shuffle()
    }

    private fun setCountdownTimer(seconds : MutableState<Int>)
    {
        val countDownTimer = object : CountDownTimer(Long.MAX_VALUE , 1000)
        {
            override fun onTick(p0: Long) {
                seconds.value += 1
            }

            override fun onFinish() {
            }

        }

        countDownTimer.start()
    }

    //THIRD GAME METHODS

    @Composable
    private fun createEquationBoxes(
        stageNumber: MutableState<Int>,
        gameRunning: MutableState<Boolean>,
        endOfRound: MutableState<Boolean>,
        numOfWins: MutableState<Int>,
        listOfCardNumbers: MutableList<Int>,
        winningNumber: MutableState<Int>,
        gameTime: MutableState<Long>,
        totalGameScore: MutableState<Int>,
        numOfLifes: MutableState<Int>,
        winningEqualtion : MutableList<Int>
    ) {
        var screenWidth = resources.displayMetrics.widthPixels
        var screenHeight = resources.displayMetrics.heightPixels

        var secondsOnGame = remember {
            mutableStateOf<Int>(0)
        }

        var box1YOffsetDp = remember {
            mutableStateOf<Int>(0)
        }

        var box1XOffsetDp = remember {
            mutableStateOf<Int>(0)
        }

        var box2YOffsetDp = remember {
            mutableStateOf<Int>(0)
        }

        var box2XOffsetDp = remember {
            mutableStateOf<Int>(0)
        }

        var box3YOffsetDp = remember {
            mutableStateOf<Int>(0)
        }

        var box3XOffsetDp = remember {
            mutableStateOf<Int>(0)
        }

        var box4YOffsetDp = remember {
            mutableStateOf<Int>(0)
        }

        var box4XOffsetDp = remember {
            mutableStateOf<Int>(0)
        }

        var box5YOffsetDp = remember {
            mutableStateOf<Int>(0)
        }

        var box5XOffsetDp = remember {
            mutableStateOf<Int>(0)
        }

        var opacityA = remember {
            mutableStateOf<Float>(0f)
        }

        var opacityB = remember {
            mutableStateOf<Float>(0f)
        }

        var opacityC = remember {
            mutableStateOf<Float>(0f)
        }

        var opacityD = remember {
            mutableStateOf<Float>(0f)
        }

        var opacityE = remember {
            mutableStateOf<Float>(0f)
        }

        var rotationDegreesA = remember {
            mutableStateOf(0)
        }

        var rotationDegreesB = remember {
            mutableStateOf(0)
        }

        var rotationDegreesC = remember {
            mutableStateOf(0)
        }

        var rotationDegreesD = remember {
            mutableStateOf(0)
        }

        var rotationDegreesE = remember {
            mutableStateOf(0)
        }

        var winOrLoose = remember {
            mutableStateOf<Boolean>(false)
        }

        var cardColors = remember {
            mutableListOf<Color>(Color.White , Color.White , Color.White , Color.White , Color.White)
        }

        var winningArrayList = ArrayList<Int>(5)

//        LaunchedEffect(Unit) {
//            setCountdownTimer(secondsOnGame)
//        }
//        setCountdownTimer(secondsOnGame)

        //recalculate the y offset of the boxes based on the timer

        if (endOfRound.value == false)
        {
//            box1YOffsetDp.value = (secondsOnGame.value * randomYOffsetFactor(stageNumber.value + 1) * 100)
//            box2YOffsetDp.value = (secondsOnGame.value * randomYOffsetFactor(stageNumber.value + 1) * 100)
//            box3YOffsetDp.value = (secondsOnGame.value * randomYOffsetFactor(stageNumber.value + 1) * 100)
//            box4YOffsetDp.value = (secondsOnGame.value * randomYOffsetFactor(stageNumber.value + 1) * 100)
//            box5YOffsetDp.value = (secondsOnGame.value * randomYOffsetFactor(stageNumber.value + 1) * 100)
        }

        //round ends if boxes move out of the screen
        if (box1YOffsetDp.value >= screenHeight + 50)
        {
            box1YOffsetDp.value = 0
            box2YOffsetDp.value = 0
            box3YOffsetDp.value = 0
            box4YOffsetDp.value = 0
            box5YOffsetDp.value = 0

            box1XOffsetDp.value = 0
            box2XOffsetDp.value = 0
            box3XOffsetDp.value = 0
            box4XOffsetDp.value = 0
            box5XOffsetDp.value = 0
            numOfLifes.value = numOfLifes.value -1
            endOfRound.value = true
        }

        //lost the game
        if (numOfLifes.value == 0)
        {
            endOfRound.value = true
        }

        var winingSymbol : Char = ' '
        when (winningEqualtion.get(2))
        {
            0 ->
            {
                winingSymbol = '+'
            }

            1 ->
            {
                winingSymbol = '*'
            }

            2 ->
            {
                winingSymbol = '/'
            }

            3 ->
            {
                winingSymbol = '-'
            }
        }

        val box1AnimateDpAsState by animateIntOffsetAsState(
            targetValue = IntOffset(box1XOffsetDp.value , box1YOffsetDp.value), // Adjust the offset value as needed
            animationSpec = tween(durationMillis = 500))

        val box2AnimateDpAsState by animateIntOffsetAsState(
            targetValue = IntOffset(box2XOffsetDp.value , box2YOffsetDp.value), // Adjust the offset value as needed
            animationSpec = tween(durationMillis = 500))

        val box3AnimateDpAsState by animateIntOffsetAsState(
            targetValue = IntOffset(box3XOffsetDp.value , box3YOffsetDp.value), // Adjust the offset value as needed
            animationSpec = tween(durationMillis = 500))

        val box4AnimateDpAsState by animateIntOffsetAsState(
            targetValue = IntOffset(box4XOffsetDp.value , box4YOffsetDp.value), // Adjust the offset value as needed
            animationSpec = tween(durationMillis = 500))

        val box5AnimateDpAsState by animateIntOffsetAsState(
            targetValue = IntOffset(box5XOffsetDp.value , box5YOffsetDp.value), // Adjust the offset value as needed
            animationSpec = tween(durationMillis = 500))

//        var box1AnimateDpAsState = animateDpAsState(targetValue = box1YOffsetDp.value , animationSpec = tween(durationMillis = 500))
//        var box2AnimateDpAsState = animateDpAsState(targetValue = box2YOffsetDp.value , animationSpec = tween(durationMillis = 500))
//        var box3AnimateDpAsState = animateDpAsState(targetValue = box3YOffsetDp.value , animationSpec = tween(durationMillis = 500))
//        var box4AnimateDpAsState = animateDpAsState(targetValue = box4YOffsetDp.value , animationSpec = tween(durationMillis = 500))
//        var box5AnimateDpAsState = animateDpAsState(targetValue = box5YOffsetDp.value , animationSpec = tween(durationMillis = 500))

        Column(verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally)
        {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(10.dp, 10.dp)
            )
            {

                Card(modifier = Modifier
                    .width(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .height(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .offset { box1AnimateDpAsState }
                    .alpha(opacityA.value)
                    .clickable {
                        if (listOfCardNumbers.get(0) == winningNumber.value) {
                            endOfRound.value = true
                            winOrLoose.value = true
                            numOfWins.value = numOfWins.value + 1
                            totalGameScore.value += 1000
                        } else {
                            winOrLoose.value = false
                            numOfLifes.value = numOfLifes.value - 1
                            endOfRound.value = true
                        }
                    },
                    colors = CardDefaults.cardColors(cardColors.get(0)) )
                {Text(
                    text = listOfCardNumbers.get(0).toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )}
                Card(modifier = Modifier
                    .width(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .height(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .offset { box2AnimateDpAsState }
                    .alpha(opacityB.value)
                    .clickable {
                        if (listOfCardNumbers.get(1) == winningNumber.value) {
                            endOfRound.value = true
                            winOrLoose.value = true
                            numOfWins.value = numOfWins.value + 1
                            totalGameScore.value = totalGameScore.value + 1000
                        } else {
                            winOrLoose.value = false
                            numOfLifes.value = numOfLifes.value - 1
                            endOfRound.value = true
                        }
                    },
                    colors = CardDefaults.cardColors(cardColors.get(1))
                )
                {
                    Text(
                        text = listOfCardNumbers.get(1).toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Card(modifier = Modifier
                    .width(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .height(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .offset { box3AnimateDpAsState }
                    .alpha(opacityC.value)
                    .clickable {
                        if (listOfCardNumbers.get(2) == winningNumber.value) {
                            endOfRound.value = true
                            winOrLoose.value = true
                            numOfWins.value = numOfWins.value + 1
                            totalGameScore.value = totalGameScore.value + 1000
                        } else {
                            winOrLoose.value = false
                            numOfLifes.value = numOfLifes.value - 1
                            endOfRound.value = true
                        }
                    },
                    colors = CardDefaults.cardColors(cardColors.get(2))
                )
                {
                    Text(
                        text = listOfCardNumbers.get(2).toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Card(modifier = Modifier
                    .width(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .height(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .offset { box4AnimateDpAsState }
                    .alpha(opacityD.value)
                    .background(color = cardColors.get(3))
                    .clickable {
                        if (listOfCardNumbers.get(3) == winningNumber.value) {
                            endOfRound.value = true
                            winOrLoose.value = true
                            numOfWins.value = numOfWins.value + 1
                            totalGameScore.value = totalGameScore.value + 1000
                        } else {
                            winOrLoose.value = false
                            numOfLifes.value = numOfLifes.value - 1
                            endOfRound.value = true
                        }
                    }
                    ,
                    colors = CardDefaults.cardColors(cardColors.get(3)) )
                {
                    Text(
                        text = listOfCardNumbers.get(3).toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Card(modifier = Modifier
                    .width(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .height(((screenWidth / 6) / resources.displayMetrics.scaledDensity).dp)
                    .offset { box5AnimateDpAsState }
                    .alpha(opacityE.value)
                    .clickable {
                        if (listOfCardNumbers.get(4) == winningNumber.value) {
                            endOfRound.value = true
                            winOrLoose.value = true
                            numOfWins.value = numOfWins.value + 1
                            totalGameScore.value = totalGameScore.value + 1000
                        } else {
                            winOrLoose.value = false
                            numOfLifes.value = numOfLifes.value - 1
                            endOfRound.value = true
                        }
                    },
                    colors = CardDefaults.cardColors(cardColors.get(4))
                )
                {
                    Text(
                        text = listOfCardNumbers.get(4).toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
            Spacer(modifier = Modifier.height(200.dp))
            Text(
                text = "${winningEqualtion.get(0)} ${winingSymbol.toString()} ${winningEqualtion.get(1)} =",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "Number of Wins : ${numOfWins.value}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Total Score : ${totalGameScore.value}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "LIVES : ${numOfLifes.value}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (gameTime.value % 3000 == 0L)
        {
            rotationDegreesA.value = rotationDegree().toInt()
            rotationDegreesB.value = rotationDegree().toInt()
            rotationDegreesC.value = rotationDegree().toInt()
            rotationDegreesD.value = rotationDegree().toInt()
            rotationDegreesE.value = rotationDegree().toInt()

            opacityA.value = randomOpacity()
            opacityB.value = randomOpacity()
            opacityC.value = randomOpacity()
            opacityD.value = randomOpacity()
            opacityE.value = randomOpacity()

            box1YOffsetDp.value += (randomYOffsetFactor(1) * 50 * (stageNumber.value + 1)).toInt()
            box2YOffsetDp.value += (randomYOffsetFactor(1) * 50 * (stageNumber.value + 1)).toInt()
            box3YOffsetDp.value += (randomYOffsetFactor(1) * 50 * (stageNumber.value + 1)).toInt()
            box4YOffsetDp.value += (randomYOffsetFactor(1) * 50 * (stageNumber.value + 1)).toInt()
            box5YOffsetDp.value += (randomYOffsetFactor(1) * 50 * (stageNumber.value + 1)).toInt()

            box1XOffsetDp.value = 0
            box2XOffsetDp.value = 0
            box3XOffsetDp.value = 0
            box4XOffsetDp.value = 0
            box5XOffsetDp.value = 0

            while ((box1XOffsetDp.value <= 0) || (box1XOffsetDp.value >= screenWidth))
                box1XOffsetDp.value += (randomYOffsetFactor(1) * randomXOffsetFactor(0 ,20) * (stageNumber.value + 1)).toInt()
            while ((box2XOffsetDp.value <= 0) || (box2XOffsetDp.value >= screenWidth))
                box2XOffsetDp.value += (randomYOffsetFactor(1) * randomXOffsetFactor(0 ,20) * (stageNumber.value + 1)).toInt()
            while ((box3XOffsetDp.value <= 0) || (box3XOffsetDp.value >= screenWidth))
                box3XOffsetDp.value += (randomYOffsetFactor(1) * randomXOffsetFactor(0 ,20) * (stageNumber.value + 1)).toInt()
            while ((box4XOffsetDp.value <= 0) || (box4XOffsetDp.value >= screenWidth))
                box4XOffsetDp.value += (randomYOffsetFactor(1) * randomXOffsetFactor(0 ,20) * (stageNumber.value + 1)).toInt()
            while ((box5XOffsetDp.value <= 0) || (box5XOffsetDp.value >= screenWidth))
                box5XOffsetDp.value += (randomYOffsetFactor(1) * randomXOffsetFactor(0 ,20) * (stageNumber.value + 1)).toInt()

            cardColors[0] = randomCardColor()
            cardColors[1] = randomCardColor()
            cardColors[2] = randomCardColor()
            cardColors[3] = randomCardColor()
            cardColors[4] = randomCardColor()

//            winningArrayList = produceNumberEquation(listOfCardNumbers)
//            winningEqualtion.add(0 , winningArrayList.get(0))
//            winningEqualtion.add(1 , winningArrayList.get(1))
//            winningEqualtion.add(2 , winningArrayList.get(2))
//            winningEqualtion.add(3 , winningArrayList.get(3))
//            winningNumber.value = winningArrayList.get(3)
//            rearrangeNumValues(listOfCardNumbers)
//            rearrangeNumbersList(listOfCardNumbers)
        }
    }

    @Composable
    private fun createColorsBoxes(
        stageNumber: MutableState<Int>,
        gameRunning: MutableState<Boolean>,
        endOfRound: MutableState<Boolean>,
        numOfWins: MutableState<Int>,
        listOfColors : MutableList<Int>,
        winningNumber: MutableState<Int>,
        gameTime: MutableState<Long>,
        totalGameScore: MutableState<Int>,
        numOfLifes: MutableState<Int>,
        forthShapeList : MutableList<Shape>,
        wordOrColor : MutableState<Int>,
        word : MutableState<String>,
        color : MutableState<Color>
    )
    {
        var screenHeight = resources.displayMetrics.heightPixels
        var screenWidth = resources.displayMetrics.widthPixels

        //boxes X Offset
        var box1XOffset = remember {
            mutableStateOf<Int>(0)
        }
        var box2XOffset = remember {
            mutableStateOf<Int>(0)
        }
        var box3XOffset = remember {
            mutableStateOf<Int>(0)
        }
        var box4XOffset = remember {
            mutableStateOf<Int>(0)
        }
        var box5XOffset = remember {
            mutableStateOf<Int>(0)
        }

        //boxes Y Offset
        var box1YOffset = remember {
            mutableStateOf<Int>(0)
        }
        var box2YOffset = remember {
            mutableStateOf<Int>(0)
        }
        var box3YOffset = remember {
            mutableStateOf<Int>(0)
        }
        var box4YOffset = remember {
            mutableStateOf<Int>(0)
        }
        var box5YOffset = remember {
            mutableStateOf<Int>(0)
        }

        //check if y offset is beyond the screenHeight
        if (box1YOffset.value >= screenHeight + 50)
        {

            box1XOffset.value = 0
            box2XOffset.value = 0
            box3XOffset.value = 0
            box4XOffset.value = 0
            box5XOffset.value = 0

            box1YOffset.value = 0
            box2YOffset.value = 0
            box3YOffset.value = 0
            box4YOffset.value = 0
            box5YOffset.value = 0

            endOfRound.value = true
            stageNumber.value = stageNumber.value + 1
            numOfLifes.value = numOfLifes.value - 1
        }

        if (endOfRound.value == true)
        {
            box1YOffset.value = 0
            box2YOffset.value = 0
            box3YOffset.value = 0
            box4YOffset.value = 0
            box5YOffset.value = 0

            box1XOffset.value = 0
            box2XOffset.value = 0
            box3XOffset.value = 0
            box4XOffset.value = 0
            box5XOffset.value = 0
        }

        //lost the game
        if (numOfLifes.value == 0)
        {
            endOfRound.value = true
        }

        //check that x offset is within constraints

        //offset animations for forth game

        var amimateBox1Offset = animateIntOffsetAsState(
            targetValue = IntOffset(
                box1XOffset.value,
                box1YOffset.value
            ),
            animationSpec = TweenSpec(durationMillis = 500)
        )

        var amimateBox2Offset = animateIntOffsetAsState(
            targetValue = IntOffset(
                box2XOffset.value,
                box2YOffset.value
            ),
            animationSpec = TweenSpec(durationMillis = 500)
        )

        var amimateBox3Offset = animateIntOffsetAsState(
            targetValue = IntOffset(
                box1XOffset.value,
                box1YOffset.value
            ),
            animationSpec = TweenSpec(durationMillis = 500)
        )

        var amimateBox4Offset = animateIntOffsetAsState(
            targetValue = IntOffset(
                box1XOffset.value,
                box1YOffset.value
            ),
            animationSpec = TweenSpec(durationMillis = 500)
        )


        var amimateBox5Offset = animateIntOffsetAsState(
            targetValue = IntOffset(
                box1XOffset.value,
                box1YOffset.value
            ),
            animationSpec = TweenSpec(durationMillis = 500)
        )

        Column(verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally)
        {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(10.dp, 10.dp)
            )
            {

                Card(
                    modifier = Modifier
                        .offset {
                            amimateBox1Offset.value
                        }
                        .size(((screenWidth / resources.displayMetrics.scaledDensity).dp / 6))
                        .background(
                            color = Color(listOfColors.get(0)),
                            shape = forthShapeList.get(0)
                        )
                        .clickable {
                            if (winningNumber.value == 0) {
                                numOfWins.value = numOfWins.value + 1
                                totalGameScore.value = totalGameScore.value + 1000
                                endOfRound.value = true
                                stageNumber.value = stageNumber.value + 1
                            } else {
                                numOfLifes.value = numOfLifes.value - 1
                                endOfRound.value = true
                                stageNumber.value = stageNumber.value + 1
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Color(listOfColors.get(0)), // Set the background color for the Card
                        contentColor = Color.Black // Set the default text color (ensure readability)
                    ),
                )
                {

                }

                Card(
                    modifier = Modifier
                        .offset {
                            amimateBox2Offset.value
                        }
                        .size(((screenWidth / resources.displayMetrics.scaledDensity).dp / 6))
                        .background(
                            color = Color(listOfColors.get(1)),
                            shape = forthShapeList.get(1)
                        )
                        .clickable {
                            if (winningNumber.value == 1) {
                                numOfWins.value = numOfWins.value + 1
                                totalGameScore.value = totalGameScore.value + 1000
                                endOfRound.value = true
                                stageNumber.value = stageNumber.value + 1
                            } else {
                                numOfLifes.value = numOfLifes.value - 1
                                endOfRound.value = true
                                stageNumber.value = stageNumber.value + 1
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Color(listOfColors.get(1)), // Set the background color for the Card
                        contentColor = Color.Black // Set the default text color (ensure readability)
                    ),
                )
                {

                }

                Card(
                    modifier = Modifier
                        .offset {
                            amimateBox3Offset.value
                        }
                        .size(((screenWidth / resources.displayMetrics.scaledDensity).dp / 6))
                        .background(
                            color = Color(listOfColors.get(2)),
                            shape = forthShapeList.get(2)
                        )
                        .clickable {
                            if (winningNumber.value == 2) {
                                numOfWins.value = numOfWins.value + 1
                                totalGameScore.value = totalGameScore.value + 1000
                                endOfRound.value = true
                                stageNumber.value = stageNumber.value + 1
                            } else {
                                numOfLifes.value = numOfLifes.value - 1
                                endOfRound.value = true
                                stageNumber.value = stageNumber.value + 1
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Color(listOfColors.get(2)), // Set the background color for the Card
                        contentColor = Color.Black // Set the default text color (ensure readability)
                    ),
                )
                {

                }

                Card(
                    modifier = Modifier
                        .offset {
                            amimateBox4Offset.value
                        }
                        .size(((screenWidth / resources.displayMetrics.scaledDensity).dp / 6))
                        .background(
                            color = Color(listOfColors.get(3)),
                            shape = forthShapeList.get(3)
                        )
                        .clickable {
                            if (winningNumber.value == 3) {
                                numOfWins.value = numOfWins.value + 1
                                totalGameScore.value = totalGameScore.value + 1000
                                endOfRound.value = true
                                stageNumber.value = stageNumber.value + 1
                            } else {
                                numOfLifes.value = numOfLifes.value - 1
                                endOfRound.value = true
                                stageNumber.value = stageNumber.value + 1
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Color(listOfColors.get(3)), // Set the background color for the Card
                        contentColor = Color.Black // Set the default text color (ensure readability)
                    ),
                )
                {

                }
                Card(
                    modifier = Modifier
                        .offset {
                            amimateBox5Offset.value
                        }
                        .size(((screenWidth / resources.displayMetrics.scaledDensity).dp / 6))
                        .background(
                            color = Color(listOfColors.get(4)),
                            shape = forthShapeList.get(4)
                        )
                        .clickable {
                            if (winningNumber.value == 4) {
                                numOfWins.value = numOfWins.value + 1
                                totalGameScore.value = totalGameScore.value + 1000
                                endOfRound.value = true
                                stageNumber.value = stageNumber.value + 1
                            } else {
                                numOfLifes.value = numOfLifes.value - 1
                                endOfRound.value = true
                                stageNumber.value = stageNumber.value + 1
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Color(listOfColors.get(4)), // Set the background color for the Card
                        contentColor = Color.Black // Set the default text color (ensure readability)
                    ),
                )
                {


                }
            }
        }

        if (gameTime.value % 3000 == 0L)
        {

            box1YOffset.value += (randomYOffsetFactor(1) * 30 * (stageNumber.value + 1)).toInt()
            box2YOffset.value += (randomYOffsetFactor(1) * 30 * (stageNumber.value + 1)).toInt()
            box3YOffset.value += (randomYOffsetFactor(1) * 30 * (stageNumber.value + 1)).toInt()
            box4YOffset.value += (randomYOffsetFactor(1) * 30 * (stageNumber.value + 1)).toInt()
            box5YOffset.value += (randomYOffsetFactor(1) * 30 * (stageNumber.value + 1)).toInt()



            while ((box1XOffset.value <= 0) || (box1XOffset.value >= screenWidth))
                box1XOffset.value += (randomYOffsetFactor(1) * randomXOffsetFactor(0 ,20) * (stageNumber.value + 1)).toInt()
            while ((box2XOffset.value <= 0) || (box2XOffset.value >= screenWidth))
                box2XOffset.value += (randomYOffsetFactor(1) * randomXOffsetFactor(0 ,20) * (stageNumber.value + 1)).toInt()
            while ((box3XOffset.value <= 0) || (box3XOffset.value >= screenWidth))
                box3XOffset.value += (randomYOffsetFactor(1) * randomXOffsetFactor(0 ,20) * (stageNumber.value + 1)).toInt()
            while ((box4XOffset.value <= 0) || (box4XOffset.value >= screenWidth))
                box4XOffset.value += (randomYOffsetFactor(1) * randomXOffsetFactor(0 ,20) * (stageNumber.value + 1)).toInt()
            while ((box5XOffset.value <= 0) || (box5XOffset.value >= screenWidth))
                box5XOffset.value += (randomYOffsetFactor(1) * randomXOffsetFactor(0 ,20) * (stageNumber.value + 1)).toInt()

        }
    }

    private fun numExpression(num1 : Int , num2 : Int , action : Int) : Int
    {
        var result : Int = 0
        when (action)
        {
            0 ->
            {
                result = num1 + num2
            }

            1 ->
            {
                result = num1 * num2
            }

            2 ->
            {
                result = num1 / num2
            }

            0 ->
            {
                result = num1 - num2
            }
        }
        return result
    }

    private fun numExpressionToString(num1 : Int , num2 : Int , action : Int) : String
    {
        var result : String = ""
        when (action)
        {
            0 ->
            {
                result = "${num1} + ${num2}"
            }

            1 ->
            {
                result = "${num1} * ${num2}"
            }

            2 ->
            {
                result = "${num1} / ${num2}"
            }

            3 ->
            {
                result = "${num1} - ${num2}"
            }
        }
        return result
    }

    private fun produceNumberEquation(listOfCardNumbers: MutableList<Int>) : ArrayList<Int>
    {
        var correctResult : Boolean = false
        var equationType : Int = 0
        var num1 : Int = 0
        var num2 : Int = 0
        var result : Int = 0
        var resultArrayList = ArrayList<Int>(4)
        while(correctResult == false)
        {
            while (num1 == num2)
            {
                num1 = listOfCardNumbers.get(kotlin.random.Random.nextInt(0 ,4).toInt())
                num2 = listOfCardNumbers.get(kotlin.random.Random.nextInt(0 ,4).toInt())
            }
            equationType = kotlin.random.Random.nextInt(0 ,4).toInt()
            when (equationType) {
                0 ->
                {
                    result = num1 + num2
                    correctResult = true
                }
                1 ->
                {
                    result = num1 * num2
                    correctResult = true
                }
                2 ->
                {
                    if (num2 != 0)
                    {
                        if ((num1 % num2) == 0)
                        {
                            correctResult = true
                        }
                        result = num1 / num2
                    }
                }
                3 ->
                {
                    result = num1 - num2
                    correctResult = true
                }
            }
        }
        resultArrayList.add(0 , num1)
        resultArrayList.add(1, num2)
        resultArrayList.add(2 , equationType)
        resultArrayList.add(3 , result)
        return resultArrayList
    }

    fun <T> isListUnique(list: List<T>): Boolean {
        val uniqueSet = list.toSet() // Convert list to set
        return uniqueSet.size == list.size // Check if set size equals list size
    }


    data class Numbers(var cardNumber : Int)

    fun getColorName(color: Color): String {
        return colorNameMap[color] ?: "Unknown Color"
    }

    val colorNameMap = mapOf(
        Color.Red to "Red",
        Color.Green to "Green",
        Color.Blue to "Blue",
        Color.Black to "Black",
        Color.White to "White",
        Color.Yellow to "Yellow",
        Color.Magenta to "Magenta",
        Color.Cyan to "Cyan",
        Color.Gray to "Gray",
        Color.LightGray to "Light Gray",
        // Add more common colors as needed
    )


}
