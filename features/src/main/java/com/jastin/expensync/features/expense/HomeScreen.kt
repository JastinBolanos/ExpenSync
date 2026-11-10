package com.jastin.expensync.features.expense

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import com.jastin.expensync.features.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }
    val searchFocusRequester = remember { FocusRequester() }
    val snackbarHostState = remember { SnackbarHostState() }

    BackHandler(enabled = showSearchBar) {
        showSearchBar = false
        viewModel.setSearchQuery("")
    }

    LaunchedEffect(showSearchBar) {
        if (showSearchBar) {
            kotlinx.coroutines.delay(120)
            searchFocusRequester.requestFocus()
        }
    }

    val fabInteractionSource = remember { MutableInteractionSource() }
    val isFabPressed by fabInteractionSource.collectIsPressedAsState()
    val fabScale by animateFloatAsState(
        targetValue = if (isFabPressed) 0.91f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "fab_scale"
    )

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    Scaffold(
        containerColor = DeepBgEnd,
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            // Floating Action Button in signature Gold-to-Copper logo gradient without tonal elevation stain
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .graphicsLayer {
                        scaleX = fabScale
                        scaleY = fabScale
                    }
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(LogoGold, LogoCopper)
                        )
                    )
                    .clickable(
                        interactionSource = fabInteractionSource,
                        indication = ripple(bounded = true, color = Color.Black.copy(alpha = 0.25f))
                    ) {
                        showBottomSheet = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nuevo Gasto",
                    tint = Color(0xFF030D0B),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { paddingValues ->
        // Multi-layered Atmospheric Background infused with the 4 Logo Colors
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val w = size.width
                    val h = size.height

                    // Layer 1: Deep Pine Night Foundation
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF061A17), // Deep Logo Pine Teal
                                Color(0xFF041411), // Midnight Pine
                                Color(0xFF020908)  // Pitch Shadow
                            )
                        )
                    )

                    // Layer 2: Top-Right Golden Amber & Terracotta Copper Ambient Bloom (Logo Top Curve)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                LogoGold.copy(alpha = 0.15f),
                                LogoCopper.copy(alpha = 0.08f),
                                Color.Transparent
                            ),
                            center = Offset(w * 0.90f, h * 0.10f),
                            radius = w * 0.85f
                        ),
                        center = Offset(w * 0.90f, h * 0.10f),
                        radius = w * 0.85f
                    )

                    // Layer 3: Mid-Left Slate Mineral & Pine Atmospheric Light (Logo Bottom Quadrants)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                LogoSlate.copy(alpha = 0.14f),
                                Color(0xFF0A2E28).copy(alpha = 0.10f),
                                Color.Transparent
                            ),
                            center = Offset(w * 0.08f, h * 0.42f),
                            radius = w * 0.70f
                        ),
                        center = Offset(w * 0.08f, h * 0.42f),
                        radius = w * 0.70f
                    )

                    // Layer 4: Subtle dynamic ribbon accent in bottom-right
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                LogoCopper.copy(alpha = 0.06f),
                                Color.Transparent
                            ),
                            center = Offset(w * 0.85f, h * 0.82f),
                            radius = w * 0.60f
                        ),
                        center = Offset(w * 0.85f, h * 0.82f),
                        radius = w * 0.60f
                    )
                }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    start = 18.dp,
                    end = 18.dp,
                    top = 16.dp,
                    bottom = 88.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header with Animated Takeover Search Bar & Frameless Search Action
                item(key = "header_takeover") {
                    AnimatedContent(
                        targetState = showSearchBar,
                        transitionSpec = {
                            if (targetState) {
                                // Opening search: Search bar slides in with spring from right, Header fades/slides left
                                (fadeIn(animationSpec = tween(220)) + slideInHorizontally(
                                    initialOffsetX = { fullWidth -> (fullWidth * 0.35f).toInt() },
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow)
                                ) + expandHorizontally(expandFrom = Alignment.End, animationSpec = tween(220)))
                                    .togetherWith(
                                        fadeOut(animationSpec = tween(150)) + slideOutHorizontally(
                                            targetOffsetX = { fullWidth -> -(fullWidth * 0.25f).toInt() },
                                            animationSpec = tween(150)
                                        )
                                    )
                            } else {
                                // Closing search: Header slides in from left, Search bar collapses right
                                (fadeIn(animationSpec = tween(220)) + slideInHorizontally(
                                    initialOffsetX = { fullWidth -> -(fullWidth * 0.25f).toInt() },
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow)
                                )).togetherWith(
                                    fadeOut(animationSpec = tween(150)) + slideOutHorizontally(
                                        targetOffsetX = { fullWidth -> (fullWidth * 0.35f).toInt() },
                                        animationSpec = tween(150)
                                    ) + shrinkHorizontally(shrinkTowards = Alignment.End, animationSpec = tween(180))
                                )
                            }
                        },
                        label = "header_search_switch"
                    ) { isSearching ->
                        if (isSearching) {
                            // Full Takeover Search Bar (Obsidian Pine Surface, Gradient Border & Bottom Living Beam)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFF08221D),
                                                Color(0xFF051714),
                                                Color(0xFF030D0B)
                                            )
                                        )
                                    )
                                    .border(
                                        width = 1.dp,
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                LogoGold.copy(alpha = 0.65f),
                                                LogoSlate.copy(alpha = 0.35f),
                                                LogoCopper.copy(alpha = 0.50f)
                                            )
                                        ),
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .drawBehind {
                                        val strokePx = 1.5.dp.toPx()
                                        drawLine(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    LogoGold.copy(alpha = 0.7f),
                                                    LogoCopper.copy(alpha = 0.6f),
                                                    Color.Transparent
                                                )
                                            ),
                                            start = Offset(size.width * 0.1f, size.height),
                                            end = Offset(size.width * 0.9f, size.height),
                                            strokeWidth = strokePx
                                        )
                                    }
                                    .padding(horizontal = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Back icon button to close search & restore header
                                IconButton(
                                    onClick = {
                                        showSearchBar = false
                                        viewModel.setSearchQuery("")
                                    },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Cerrar búsqueda",
                                        tint = LogoGold,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                // Search input field with auto-focus
                                BasicTextField(
                                    value = uiState.searchQuery,
                                    onValueChange = { viewModel.setSearchQuery(it) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .focusRequester(searchFocusRequester)
                                        .padding(horizontal = 4.dp),
                                    singleLine = true,
                                    cursorBrush = SolidColor(LogoGold),
                                    textStyle = TextStyle(
                                        color = TextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    decorationBox = { innerTextField ->
                                        Box(contentAlignment = Alignment.CenterStart) {
                                            if (uiState.searchQuery.isEmpty()) {
                                                Text(
                                                    text = "Buscar gasto...",
                                                    color = LogoSlate.copy(alpha = 0.8f),
                                                    fontSize = 15.sp
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }
                                )

                                // Clear search query button
                                AnimatedVisibility(
                                    visible = uiState.searchQuery.isNotEmpty(),
                                    enter = fadeIn(tween(140)) + scaleIn(tween(140)),
                                    exit = fadeOut(tween(100)) + scaleOut(tween(100))
                                ) {
                                    IconButton(
                                        onClick = { viewModel.setSearchQuery("") },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(22.dp)
                                                .clip(CircleShape)
                                                .background(Color.White.copy(alpha = 0.14f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Limpiar texto",
                                                tint = Color.White,
                                                modifier = Modifier.size(13.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            // Standard Header (Name & Frameless Search Action)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "ExpenSync",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Gestión de gastos inteligente",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    // Botón de búsqueda SIN CAJA (completamente integrado, sin borde ni fondo cuadrado)
                                    IconButton(
                                        onClick = { showSearchBar = true },
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = "Buscar",
                                            tint = TextSecondary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Professional Luxury Bank Card Hero with subtle floating animation
                item(key = "hero_luxury_card") {
                    LuxuryBankCard(
                        totalSpent = uiState.totalGroupSpent,
                        myNetBalance = uiState.myNetBalance,
                        transactionsCount = uiState.expenses.size
                    )
                }

                // Settlement Section
                item(key = "debts_section") {
                    DebtSummarySection(
                        debts = uiState.debts,
                        myNetBalance = uiState.myNetBalance
                    )
                }

                // Unified Transactions Header & Compact Filter Pills in Logo Colors
                item(key = "transactions_section_header") {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "TRANSACCIONES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            color = LogoSlate
                        )

                        // Compact Segmented Filter Pills with Logo Gold/Copper highlights
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(ExpenseFilter.entries) { filter ->
                                val isSelected = uiState.selectedFilter == filter
                                val tabBgColor by animateColorAsState(
                                    targetValue = if (isSelected) LogoGold.copy(alpha = 0.18f) else Color(0xFF09221E),
                                    animationSpec = tween(200),
                                    label = "tab_bg"
                                )
                                val tabTextColor by animateColorAsState(
                                    targetValue = if (isSelected) LogoGold else TextSecondary,
                                    animationSpec = tween(200),
                                    label = "tab_text"
                                )
                                val tabBorderColor by animateColorAsState(
                                    targetValue = if (isSelected) LogoGold.copy(alpha = 0.55f) else LogoSlate.copy(alpha = 0.22f),
                                    animationSpec = tween(200),
                                    label = "tab_border"
                                )
                                val tabScale by animateFloatAsState(
                                    targetValue = if (isSelected) 1.02f else 1f,
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                                    label = "tab_scale"
                                )

                                val tabShape = RoundedCornerShape(10.dp)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .graphicsLayer {
                                            scaleX = tabScale
                                            scaleY = tabScale
                                        }
                                        .clip(tabShape)
                                        .background(tabBgColor)
                                        .border(width = 0.8.dp, color = tabBorderColor, shape = tabShape)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = ripple(bounded = true, color = LogoGold.copy(alpha = 0.25f))
                                        ) {
                                            viewModel.setFilter(filter)
                                        }
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = filter.title,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        color = tabTextColor
                                    )
                                }
                            }
                        }
                    }
                }

                // Transactions Unified Feed connected with Logo Living Lines
                if (uiState.filteredExpenses.isEmpty()) {
                    item(key = "empty_state") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val emptyTitle = if (uiState.searchQuery.isNotEmpty()) "Sin resultados" else "No hay transacciones"
                                val emptySubtitle = if (uiState.searchQuery.isNotEmpty()) "No encontramos gastos que coincidan con \"${uiState.searchQuery}\"" else "Pulsa '+' para registrar un gasto"

                                Text(
                                    text = emptyTitle,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextSecondary
                                )
                                Text(
                                    text = emptySubtitle,
                                    fontSize = 13.sp,
                                    color = LogoSlate
                                )
                            }
                        }
                    }
                } else {
                    item(key = "transactions_stream") {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        ) {
                            uiState.filteredExpenses.forEachIndexed { index, expense ->
                                ExpenseRow(expense = expense)
                                if (index < uiState.filteredExpenses.lastIndex) {
                                    AnimatedLivingLine(
                                        colorA = LogoGold,
                                        colorB = LogoCopper,
                                        height = 1.dp,
                                        durationMillis = 2400 + index * 300,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showBottomSheet) {
            AddExpenseSheet(
                onDismiss = { showBottomSheet = false },
                onConfirm = { title, amount, paidBy, participants ->
                    viewModel.addExpense(
                        title = title,
                        amount = amount,
                        paidByUserId = paidBy,
                        participantsIds = participants
                    )
                    showBottomSheet = false
                }
            )
        }
    }
}