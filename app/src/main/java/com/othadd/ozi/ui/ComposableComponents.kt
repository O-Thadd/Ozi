package com.othadd.ozi.ui

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.model.gaming.GamePrepOutcome
import com.othadd.ozi.domain.model.gaming.UserGameBrokeringState
import com.othadd.ozi.domain.model.gaming.UserGameState
import com.othadd.ozi.testDialogData
import com.othadd.ozi.testGamePrepDialogData
import com.othadd.ozi.testMenuData1
import com.othadd.ozi.testMenuItems
import com.othadd.ozi.testUser1
import com.othadd.ozi.testUsers1
import com.othadd.ozi.ui.model.DialogData
import com.othadd.ozi.ui.model.GamePrepDialogData
import com.othadd.ozi.ui.model.MenuData
import com.othadd.ozi.ui.theme.OziAviBlueBG_dark
import com.othadd.ozi.ui.theme.OziAviBlueBG_light
import com.othadd.ozi.ui.theme.OziAviGreenBG_dark
import com.othadd.ozi.ui.theme.OziAviGreenBG_light
import com.othadd.ozi.ui.theme.OziAviRedBG_dark
import com.othadd.ozi.ui.theme.OziAviRedBG_light
import com.othadd.ozi.ui.theme.OziComposeTheme
import com.othadd.oziX.R
import java.util.Calendar
import kotlin.math.roundToInt

@Composable
fun Avi(fg: Int = -1, bg: Int = -1, modifier: Modifier) {
    Surface(
        color = getAviBGColor(bg),
        shape = CircleShape,
        modifier = modifier
    ) {
        Box {
            if (fg != -1) {
                Image(
                    painter = getAviFGPainter(fg),
                    contentDescription = null,
                    modifier.padding(4.dp)
                )
            } else {
                Text(
                    text = "--",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Preview
@Composable
fun PrevAvi() {
    OziComposeTheme(darkTheme = true) {
        Avi(-1, -1, Modifier.size(80.dp))
    }
}

@Composable
fun getAviFGPainter(aviFG: Int): Painter {
    return painterResource(
        getFgResourceId(aviFG)
    )
}

fun getFgResourceId(aviFG: Int) = when (aviFG) {
    1 -> R.drawable.avi1
    2 -> R.drawable.avi2
    3 -> R.drawable.avi3
    4 -> R.drawable.avi4
    5 -> R.drawable.avi5
    6 -> R.drawable.avi6
    7 -> R.drawable.avi7
    8 -> R.drawable.avi8
    9 -> R.drawable.avi9
    10 -> R.drawable.avi10
    11 -> R.drawable.avi11
    12 -> R.drawable.avi12
    13 -> R.drawable.avi13
    14 -> R.drawable.avi14
    15 -> R.drawable.avi15
    16 -> R.drawable.avi16
    17 -> R.drawable.avi17
    18 -> R.drawable.avi18
    19 -> R.drawable.avi19
    20 -> R.drawable.avi20
    21 -> R.drawable.avi21
    22 -> R.drawable.avi22
    23 -> R.drawable.avi23
    24 -> R.drawable.avi24
    25 -> R.drawable.avi25
    26 -> R.drawable.avi26
    27 -> R.drawable.avi27
    28 -> R.drawable.avi28
    29 -> R.drawable.avi29
    30 -> R.drawable.avi30
    200 -> R.drawable.ic_group
    else -> R.drawable.ic_app_icon
}

fun getBgResourceId(aviBG: Int) = when (aviBG) {
    1 -> R.color.ozi_avi_bg_red
    2 -> R.color.ozi_avi_bg_blue
    3, 200 -> R.color.ozi_avi_bg_green
    else -> R.color.ozi_primary_color
}

fun getAviBGColor(aviBG: Int): Color {
    return when (aviBG) {
        1 -> OziAviRedBG_light
        2 -> OziAviBlueBG_light
        3 -> OziAviGreenBG_light
        else -> Color.LightGray
    }
}

fun getAviBGColorLight(aviBG: Int): Color {
    return when (aviBG) {
        1 -> OziAviRedBG_light
        2 -> OziAviBlueBG_light
        3 -> OziAviGreenBG_light
        else -> Color.LightGray
    }
}

fun getAviBGColorDark(aviBG: Int): Color {
    return when (aviBG) {
        1 -> OziAviRedBG_dark
        2 -> OziAviBlueBG_dark
        3 -> OziAviGreenBG_dark
        else -> Color.LightGray
    }
}


@Composable
fun ThemeSwitch(
    onPressed: () -> Unit,
    state: ThemeState
) {
    val transition = updateTransition(targetState = state, label = "switchStateTransition")
    val pxToMove = with(LocalDensity.current) { 32.dp.toPx().roundToInt() }
    val animatedOffset by transition.animateIntOffset(label = "intOffset") {
        when (it) {
            ThemeState.LIGHT -> IntOffset(0, 0)
            ThemeState.DARK -> IntOffset(pxToMove, 0)
        }
    }
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(50)
            )
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) {
                onPressed()
            }
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(50)
            )
            .requiredWidth(60.dp)
    ) {
        Box(
            modifier = Modifier
                .offset { animatedOffset }
                .background(
                    color = Color(0xFF252331),
                    shape = RoundedCornerShape(50)
                )
        ) {
            transition.AnimatedContent {
                when (it) {
                    ThemeState.LIGHT -> SunImage()
                    ThemeState.DARK -> MoonImage()
                }
            }
        }
    }
}

enum class ThemeState {
    LIGHT,
    DARK
}

@Composable
fun SunImage() {
    Image(
        painter = painterResource(R.drawable.sun),
        contentDescription = null,
        modifier = Modifier
            .padding(4.dp)
            .size(20.dp)
    )
}

@Composable
fun MoonImage() {
    Image(
        painter = painterResource(R.drawable.moon),
        contentDescription = null,
        modifier = Modifier
            .padding(4.dp)
            .size(20.dp)
    )
}

@Preview(showBackground = true, widthDp = 206, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun PrevThemeSwitch() {
    OziComposeTheme {
        ThemeSwitch(
            onPressed = { /*TODO*/ },
            state = ThemeState.LIGHT
        )
    }
}

@Composable
fun VerifiedIcon() {
    Icon(
        painter = painterResource(id = R.drawable.ic_verified),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun OziTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    leadingIcon: @Composable () -> Unit,
    placeHolder: @Composable () -> Unit,
    trailingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClicked: (() -> Unit)? = null,
    suggestions: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    LaunchedEffect(key1 = true){
        interactionSource.interactions.collect() {
            if (it is PressInteraction.Release){
                onClicked?.invoke()
            }
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            leadingIcon()

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                if (text.isEmpty()) {
                    placeHolder()
                }

                BasicTextField(
                    value = text,
                    onValueChange = { onTextChanged(it) },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
                    interactionSource = interactionSource,
                    keyboardOptions = if (suggestions)
                        KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences)
                    else
                        KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            trailingIcon()
        }
    }
}

@Preview
@Composable
fun PrevOziTextField() {
    OziComposeTheme {
        OziTextField(
            text = "",
            onTextChanged = { },
            leadingIcon = { /*TODO*/ },
            placeHolder = { /*TODO*/ },
            trailingIcon = { /*TODO*/ },
            suggestions = true
        )
    }
}

@Composable
fun OnBoardingTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    placeHolder: String,
    isError: Boolean = false,
    supportingText: String = "",
    forPassword: Boolean = false
) {
    var textHidden by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        textHidden = forPassword
    }

    TextField(
        value = text,
        onValueChange = { onTextChanged(it) },
        label = {
            Text(
                text = placeHolder,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        isError = isError,
        supportingText = {
            Text(
                text = supportingText,
                style = MaterialTheme.typography.labelMedium
            )
        },
        singleLine = true,
        trailingIcon = {
            if (forPassword) {
                Box(
                    modifier = Modifier.size(25.dp)
                ) {
                    if (textHidden) {
                        EyeHiddenIcon {
                            textHidden = !textHidden
                        }
                    } else {
                        EyeShownIcon {
                            textHidden = !textHidden
                        }
                    }
                }
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp),
        visualTransformation = if (textHidden) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true, widthDp = 412)
@Composable
fun PrevOnboardingTextField() {
    OziComposeTheme {
        Box(
        ) {
            OnBoardingTextField(
                text = "",
                onTextChanged = { },
                placeHolder = "Password",
                forPassword = true
            )
        }
    }
}

@Composable
fun EyeHiddenIcon(
    onTap: () -> Unit
) {
    Icon(
        painter = painterResource(id = R.drawable.eye_hidden),
        contentDescription = null,
        modifier = Modifier.clickable { onTap() }
    )
}

@Composable
fun EyeShownIcon(
    onTap: () -> Unit
) {
    Icon(
        painter = painterResource(id = R.drawable.eye_shown),
        contentDescription = null,
        modifier = Modifier.clickable { onTap() }
    )
}

@Composable
fun AviChooserDialog(
    onConfirmSelection: (Int, Int) -> Unit,
    onClose: () -> Unit
) {
    var selectedFg by remember { mutableIntStateOf(-1) }
    var selectedBg by remember { mutableIntStateOf(-1) }

    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier
                    .clickable { onClose() }
                    .align(Alignment.End)
                    .size(30.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Avi",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                for (i in 1..30) {
                    item {
                        SelectableAviFg(
                            aviFG = i,
                            i == selectedFg,
                            onSelected = {
                                selectedFg = i
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Background",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                for (i in 1..3) {
                    SelectableAviBg(
                        aviBg = i,
                        selected = i == selectedBg,
                        onSelected = { selectedBg = it },
                        modifier = Modifier.size(75.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            FilledTonalButton(
                onClick = {
                    onClose()
                    onConfirmSelection(selectedFg, selectedBg)
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = selectedFg != -1 && selectedBg != -1
            ) {
                Text(
                    text = "Confirm",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

//@Preview(showBackground = true, widthDp = 412)
@Composable
fun PrevAviChooser() {
    OziComposeTheme {
        AviChooserDialog({ _, _ -> }, { })
    }
}

@Composable
fun SelectableAviFg(
    aviFG: Int,
    selected: Boolean,
    onSelected: (Int) -> Unit
) {

    Box(
        modifier = Modifier
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                onSelected(aviFG)
            }
            .border(
                width = 2.dp,
                color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Icon(
            painter = getAviFGPainter(aviFG = aviFG),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

//@Preview
@Composable
fun PrevSelectableAviFg() {
    OziComposeTheme {
        SelectableAviFg(19, true, { })
    }
}


@Composable
fun SelectableAviBg(
    aviBg: Int,
    selected: Boolean,
    onSelected: (Int) -> Unit,
    modifier: Modifier
) {

    Surface(
        color = getAviBGColor(aviBg),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                onSelected(aviBg)
            }
            .border(
                width = 2.dp,
                color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
    ) {

    }
}

//@Preview
@Composable
fun PrevSelectableAviBg() {
    OziComposeTheme {
        SelectableAviBg(4, true, { }, Modifier.size(200.dp))
    }
}

@Composable
fun Dialog(
    dialogData: DialogData
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 6.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            dialogData.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge
                )
            }


            dialogData.body?.let {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (dialogData.button1 != null || dialogData.button2 != null) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            Row(
                horizontalArrangement = Arrangement.Center,
            ) {
                dialogData.button1?.let {
                    FilledTonalButton(onClick = it.second) {
                        Text(text = it.first)
                    }
                }

                dialogData.button2?.let {
                    Spacer(modifier = Modifier.width(32.dp))

                    FilledTonalButton(onClick = it.second) {
                        Text(text = it.first)
                    }
                }
            }
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PrevDialog() {
    OziComposeTheme {
        Dialog(dialogData = testDialogData)
    }
}

@Composable
fun UsersList(
    users: List<User>,
    selectedUsers: List<User>? = null,
    onUserClicked: (User, Boolean) -> Boolean,
    selectable: Boolean = false,
    selectOnClick: Boolean = false
    ) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users) {user ->
            User1(
                user = user,
                onUserClicked = onUserClicked,
                selectable = selectable,
                selected = if (selectedUsers == null) false else user in selectedUsers,
                selectOnClick = selectOnClick
            )
        }
    }
}

//@Preview
@Composable
fun PrevUsersList() {
    OziComposeTheme {
        Surface {
            UsersList(
                users = testUsers1,
                onUserClicked = { _, _ -> true }
            )
        }
    }
}



@Composable
fun User1(
    user: User,
    onUserClicked: (User, Boolean) -> Boolean,
    selectable: Boolean = false,
    selected: Boolean? = null,
    selectOnClick: Boolean = false
) {

    var actualSelected by remember(selected) { mutableStateOf(selected ?: false) }
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                if (selectable && selectOnClick) {
                    actualSelected = !actualSelected
                }
                val successful = onUserClicked(user, actualSelected)
                if (!successful) {
                    actualSelected = !actualSelected
                }
            }
            .padding(vertical = 4.dp)
    ) {
        Box {
            Avi(
                fg = user.aviFg,
                bg = user.aviBg,
                modifier = Modifier.size(40.dp)
            )

            if (actualSelected) {
                Image(
                    painter = painterResource(id = R.drawable.ic_selected),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.BottomEnd)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = user.username,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier.weight(1f)
        ) {
            if (user.verified) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_verified),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterStart)
                )
            }
        }

        if (user.online) {
            Chip(
                text = "online",
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

//@Preview(showBackground = true, widthDp = 412)
@Composable
fun PrevUser1() {
    OziComposeTheme {
        User1(
            user = testUser1,
            onUserClicked = { _, _ -> true},
            selectable = true,
            selected = null,
            selectOnClick = true
        )
    }
}


@Composable
fun User2(
    user: User,
    modifier: Modifier = Modifier
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(vertical = 4.dp)
    ) {
        Avi(
            fg = user.aviFg,
            bg = user.aviBg,
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = user.username,
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.width(8.dp))

            if (user.verified) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_verified),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .size(10.dp)
                )
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun PrevUser2() {
    OziComposeTheme {
        User2(
            user = testUser1
        )
    }
}


@Composable
private fun Chip(
    text: String,
    color: Color
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
        color = color,
    )
}

@Composable
fun GamePrepDialog(
    dialogData: GamePrepDialogData,
    postAlertDialog: (title: String, body: String?) -> Unit
) {
    val animationStartPoint = remember {
        val timeNow = Calendar.getInstance().timeInMillis
        val timeElapsed = timeNow - dialogData.state.requestCreationTime
        timeElapsed / 45_000f
    }

    val durationOfAnimation = remember {
        val timeNow = Calendar.getInstance().timeInMillis
        val finishTime = dialogData.state.requestCreationTime + 45_000
        val value = (finishTime - timeNow).toInt()
        if (value < 0) 0 else value
    }

    var startAnimation by remember {
        mutableStateOf(false)
    }

    val progress by animateFloatAsState(
        targetValue = if (startAnimation) 1f else animationStartPoint,
        label = "waiting progress animation",
        animationSpec = tween(durationMillis = durationOfAnimation, easing = LinearEasing),
        finishedListener = {  }
    )

    val thisUserIsHost by remember { mutableStateOf(dialogData.state.host.userId == dialogData.state.thisUserId) }
    val padding = remember { 16 }

    Surface(
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Column(
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Brokering game...",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = padding.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Host:",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = padding.dp)
                )

                GameParticipant(
                    user = dialogData.state.host,
                    brokeringState = UserGameBrokeringState.ACCEPTED,
                    host = true,
                    padding = padding
                )

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (dialogData.state.invitees.size == 1) "Invitee:" else "Invitees:",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = padding.dp)
                )


                for (user in dialogData.state.invitees) {
                    GameParticipant(
                        user = user,
                        brokeringState = when {
                            user.userId in dialogData.state.declinedInviteesIds -> UserGameBrokeringState.DECLINED
                            user.userId in dialogData.state.acceptedInviteesIds -> UserGameBrokeringState.ACCEPTED
                            user.gameState == UserGameState.PENDING_REQUEST.string -> UserGameBrokeringState.BUSY
                            user.gameState == UserGameState.PLAYING.string -> UserGameBrokeringState.PLAYING
                            else -> UserGameBrokeringState.PENDING
                        },
                        padding = padding
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (!thisUserIsHost) {
                    GameDialogButtonGroup(
                        accept = dialogData.accept,
                        decline = dialogData.decline,
                        accepted = dialogData.state.thisUserId in dialogData.state.acceptedInviteesIds,
                        enabled = dialogData.state.prepOutcome == null
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                val outcomeText = remember(key1 = dialogData.state.prepOutcome) {
                    when (dialogData.state.prepOutcome) {
                        GamePrepOutcome.STARTING_GAME -> "Starting game..."
                        GamePrepOutcome.CANCELLING_NO_HOST_RESPONSE -> "Host did not respond.\nCancelling..."
                        GamePrepOutcome.HOST_CANCELLED -> "Cancelling..."
                        GamePrepOutcome.PROMPTING_HOST -> "Waiting for host..."
                        GamePrepOutcome.ZERO_ACCEPTANCE -> "Cancelling..."
                        null -> ""
                    }
                }

                Text(
                    text = outcomeText,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            AnimatedVisibility(visible = thisUserIsHost && dialogData.state.prepOutcome == GamePrepOutcome.PROMPTING_HOST) {
                HostPrompt(
                    proceed = dialogData.proceed,
                    cancel = dialogData.cancel,
                    numberOfAcceptors = dialogData.state.acceptedInviteesIds.size
                )
            }
        }
    }

    LaunchedEffect(key1 = true){
        startAnimation = true
    }

    LaunchedEffect(key1 = dialogData.state.prepOutcome) {
        if (dialogData.state.prepOutcome == GamePrepOutcome.ZERO_ACCEPTANCE && thisUserIsHost) {
            val body =
                if (dialogData.state.invitees.size == 1) "Invitee not available or declined game request." else "Invitees not available or declined game request."
            postAlertDialog(body, null)
        }
    }
}

@Preview(widthDp = 412)
@Composable
fun PrevGamePrepDialog() {
    OziComposeTheme {
        GamePrepDialog(
            dialogData = testGamePrepDialogData,
            postAlertDialog = { _, _ -> }
        )
    }
}

@Composable
fun HostPrompt(
    proceed: () -> Unit,
    cancel: () -> Unit,
    numberOfAcceptors: Int
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 6.dp,
        tonalElevation = 6.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text =
            "Only $numberOfAcceptors ${if (numberOfAcceptors == 1) "invitee" else "invitees"} accepted"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
            ) {

                FilledTonalButton(
                    onClick = proceed,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color(0xFF008B00),
                        contentColor = Color.White
                    ),
                ) {
                    Text(text = "Proceed")
                }

                Spacer(modifier = Modifier.width(16.dp))

                FilledTonalButton(
                    onClick = cancel,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color(0xFF8B0000),
                        contentColor = Color.White
                    ),
                ) {
                    Text(text = "Cancel Game")
                }
            }

        }
    }
}


//@Preview
@Composable
fun PrevHostPrompt() {
    OziComposeTheme {
        HostPrompt(
            proceed = {  },
            cancel = {  },
            numberOfAcceptors = 3
        )
    }
}

@Composable
private fun GameDialogButtonGroup(
    accept: () -> Unit,
    decline: () -> Unit,
    accepted: Boolean,
    enabled: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        GameDialogAcceptButton(
            accept = accept,
            accepted = accepted,
            enabled = enabled
        )

        if (!accepted) {
            Spacer(modifier = Modifier.width(32.dp))
        }

        AnimatedVisibility(visible = !accepted) {
            FilledTonalButton(
                onClick = decline,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color(0xFF8B0000),
                    contentColor = Color.White
                ),
                enabled = enabled
            ) {
                Text(text = "Decline")
            }
        }
    }
}

@Composable
private fun GameDialogAcceptButton(
    accept: () -> Unit,
    accepted: Boolean,
    enabled: Boolean
) {
    var busy by remember(accepted, enabled) { mutableStateOf(false) }

    FilledTonalButton(
        onClick = {
            busy = true
            accept()
        },
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = Color(0xFF008B00),
            contentColor = Color.White
        ),
        enabled = !accepted && enabled
    ) {
        AnimatedContent(targetState = busy, label = "accept button busy animation") {
            if (it) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    color = Color.White,
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(text = if(accepted) "Accepted" else "Accept!")
            }

        }
    }
}

@Composable
fun GameParticipant(
    user: User,
    brokeringState: UserGameBrokeringState,
    host: Boolean = false,
    padding: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = padding.dp)
    ) {
        Avi(
            fg = user.aviFg,
            bg = user.aviBg,
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = user.username,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier.weight(1f)
        ) {
            if (user.verified) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_verified),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterStart)
                )
            }
        }

        if (!host) {
            when (brokeringState){
                UserGameBrokeringState.BUSY -> Chip("busy", Color.Yellow)
                UserGameBrokeringState.PLAYING -> Chip("currently playing", Color.Yellow)
                UserGameBrokeringState.PENDING -> Chip("pending", MaterialTheme.colorScheme.primary)
                UserGameBrokeringState.ACCEPTED -> Chip("accepted", MaterialTheme.colorScheme.tertiary)
                UserGameBrokeringState.DECLINED -> Chip("declined", Color.Red)
            }
        }
    }
}

@Composable
fun MenuItem(
    menu: MenuData,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                onClick()
                menu.action()
            }
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        menu.iconResourceId?.let {
            Icon(
                tint = MaterialTheme.colorScheme.primary,
                painter = painterResource(id = it),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = menu.name,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

//@Preview(showBackground = true)
@Composable
fun PrevMenuItem() {
    OziComposeTheme {
        MenuItem(
            menu = testMenuData1,
            onClick = { }
        )
    }
}

@Composable
fun Menu(
    menuItems: List<MenuData>,
    onAnyItemClicked: () -> Unit,
    modifier: Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 6.dp,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(8.dp)
                .width(IntrinsicSize.Max)
        ) {
            for (menu in menuItems) {
                MenuItem(
                    menu = menu,
                    onClick = onAnyItemClicked
                )
            }
        }
    }
}

//@Preview
@Composable
fun PrevMenu() {
    OziComposeTheme {
        Menu(
            menuItems = testMenuItems,
            onAnyItemClicked = {  },
            modifier = Modifier
        )
    }
}

@Composable
fun MenuButtonIcon(
    onClick: (() -> Unit)
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        (1..3).forEach{ _ ->
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(4.dp)
            ) {  }
        }
    }
}

//@Preview
@Composable
fun PrevMenuButtonIcon() {
    OziComposeTheme {
        MenuButtonIcon { }
    }
}

@Composable
fun TopBar(
    title: String,
    onBackClicked: () -> Unit,
    subtitle: String? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .clickable { onBackClicked() }
                .align(Alignment.CenterStart)
        )

        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

//@Preview(widthDp = 412, showBackground = true)
@Composable
fun PrevTopBar() {
    OziComposeTheme {
        TopBar(
            title = "Explore",
            onBackClicked = { },
            subtitle = "Explore the world"
        )
    }
}

