package com.drstrange.app.ui.screens.courses

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.drstrange.app.data.SampleData
import com.drstrange.app.data.model.Course
import com.drstrange.app.data.model.CourseCategory
import com.drstrange.app.data.model.Difficulty
import com.drstrange.app.ui.theme.InfoBlue
import com.drstrange.app.ui.theme.MysticPurple
import com.drstrange.app.ui.theme.NeonTeal
import com.drstrange.app.ui.theme.SuccessGreen
import com.drstrange.app.ui.theme.WarningOrange

@Composable
fun CoursesScreen() {
    var selectedCategory by remember { mutableStateOf<CourseCategory?>(null) }
    val filteredCourses = if (selectedCategory != null) {
        SampleData.courses.filter { it.category == selectedCategory }
    } else {
        SampleData.courses
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Medical Courses",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${SampleData.courses.size} courses available",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            CategoryFilter(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )
        }

        items(filteredCourses) { course ->
            CourseCard(course = course)
        }
    }
}

@Composable
private fun CategoryFilter(
    selectedCategory: CourseCategory?,
    onCategorySelected: (CourseCategory?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            AssistChip(
                onClick = { onCategorySelected(null) },
                label = { Text("All") },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (selectedCategory == null)
                        MysticPurple.copy(alpha = 0.2f)
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
        items(CourseCategory.entries.toList()) { category ->
            AssistChip(
                onClick = { onCategorySelected(category) },
                label = { Text(category.displayName) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (selectedCategory == category)
                        MysticPurple.copy(alpha = 0.2f)
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}

@Composable
private fun CourseCard(course: Course) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = course.category.displayName,
                    style = MaterialTheme.typography.labelLarge,
                    color = MysticPurple
                )
                DifficultyBadge(difficulty = course.difficulty)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = course.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = course.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = course.duration,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Filled.Bookmark,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${course.lessons} lessons",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (course.enrolled && course.progress > 0f) {
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { course.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = NeonTeal,
                    trackColor = MaterialTheme.colorScheme.surface,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${(course.progress * 100).toInt()}% complete",
                    style = MaterialTheme.typography.labelSmall,
                    color = NeonTeal
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            FilledTonalButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayCircle,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (course.enrolled) "Continue" else "Enroll Now")
            }
        }
    }
}

@Composable
private fun DifficultyBadge(difficulty: Difficulty) {
    val color = when (difficulty) {
        Difficulty.BEGINNER -> SuccessGreen
        Difficulty.INTERMEDIATE -> WarningOrange
        Difficulty.ADVANCED -> InfoBlue
    }

    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = difficulty.displayName,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}
