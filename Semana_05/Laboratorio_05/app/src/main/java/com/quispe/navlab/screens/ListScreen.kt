package com.quispe.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.quispe.navlab.navigation.Screen
import com.quispe.navlab.ui.theme.AcademicPurple
import com.quispe.navlab.ui.theme.CardWhite
import com.quispe.navlab.ui.theme.LilacBackground
import com.quispe.navlab.ui.theme.LilacContainer
import com.quispe.navlab.ui.theme.SoftPinkBg
import com.quispe.navlab.ui.theme.TextPrimary
import com.quispe.navlab.ui.theme.TextSecondary

data class Student(
    val id: Int,
    val name: String,
    val career: String,
    val initials: String,
    val badgeColor: Color
)

val studentList = listOf(
    Student(1, "Juan León", "Ingeniería de Sistemas", "JL", LilacContainer),
    Student(2, "María García", "Arquitectura", "MG", SoftPinkBg),
    Student(3, "Carlos Pérez", "Medicina", "CP", LilacContainer),
    Student(4, "Ana López", "Derecho", "AL", SoftPinkBg),
    Student(5, "Luis Ramírez", "Administración", "LR", LilacContainer)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController) {
    Scaffold(
        containerColor = LilacBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Directorio de Alumnos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = AcademicPurple
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CardWhite
                )
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 16.dp,
                bottom = padding.calculateBottomPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(studentList, key = { it.id }) { student ->
                StudentCardItem(
                    student = student,
                    onClick = {
                        navController.navigate(
                            Screen.Detail.createRoute(student.id)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun StudentCardItem(
    student: Student,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular Avatar with Initials
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(student.badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = student.initials,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AcademicPurple
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Student Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = student.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = student.career,
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }

            // Navigation Arrow
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = AcademicPurple.copy(alpha = 0.7f),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}