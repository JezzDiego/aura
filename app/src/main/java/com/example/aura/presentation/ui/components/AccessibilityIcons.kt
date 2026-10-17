package com.example.aura.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Accessible
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.SignLanguage
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Elderly
import androidx.compose.material.icons.outlined.FamilyRestroom
import androidx.compose.material.icons.outlined.HighlightOff
import androidx.compose.material.icons.outlined.OndemandVideo
import androidx.compose.material.icons.outlined.OpenInFull
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.TableRestaurant
import androidx.compose.material.icons.outlined.Texture
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material.icons.outlined.Countertops
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.aura.domain.model.AccessibilityFilter
import com.example.aura.domain.model.AccessibilityGroup
import com.example.aura.domain.model.AccessibilityProfile
import com.example.aura.domain.model.FeatureCategory
import com.example.aura.domain.model.FeatureStatus
import com.example.aura.domain.model.ReviewChecklistItem

val AccessibilityFilter.icon: ImageVector
    get() = when (this) {
        AccessibilityFilter.STEP_FREE_ENTRANCE -> Icons.AutoMirrored.Filled.Accessible
        AccessibilityFilter.ADAPTED_RESTROOM -> Icons.Filled.Wc
        AccessibilityFilter.RESERVED_PARKING -> Icons.Filled.LocalParking
        AccessibilityFilter.WIDE_CORRIDORS -> Icons.Outlined.SwapHoriz
        AccessibilityFilter.LOWERED_COUNTER -> Icons.Outlined.Countertops
        AccessibilityFilter.LIBRAS_SERVICE -> Icons.Outlined.Translate
        AccessibilityFilter.ACCESSIBLE_VIDEO -> Icons.Outlined.OndemandVideo
        AccessibilityFilter.VISUAL_ALERT -> Icons.Outlined.Campaign
        AccessibilityFilter.TACTILE_FLOOR -> Icons.Outlined.Texture
        AccessibilityFilter.BRAILLE_MATERIALS -> Icons.Outlined.TouchApp
        AccessibilityFilter.GUIDE_DOG_WELCOME -> Icons.Filled.Pets
    }

/** Ícone usado nos chips de "Filtros Rápidos" (Figma). */
val AccessibilityFilter.quickIcon: ImageVector
    get() = when (this) {
        AccessibilityFilter.WIDE_CORRIDORS -> Icons.Outlined.OpenInFull
        AccessibilityFilter.LIBRAS_SERVICE -> Icons.Filled.SignLanguage
        else -> icon
    }

val AccessibilityGroup.icon: ImageVector
    get() = when (this) {
        AccessibilityGroup.PHYSICAL -> Icons.AutoMirrored.Filled.Accessible
        AccessibilityGroup.HEARING -> Icons.Filled.SignLanguage
        AccessibilityGroup.VISUAL -> Icons.Outlined.Visibility
    }

val FeatureCategory.icon: ImageVector
    get() = when (this) {
        FeatureCategory.ACCESS_ENTRY -> Icons.AutoMirrored.Outlined.DirectionsWalk
        FeatureCategory.INTERNAL_CIRCULATION -> Icons.Outlined.TableRestaurant
        FeatureCategory.ADAPTED_RESTROOM -> Icons.Filled.Wc
        FeatureCategory.COMMUNICATION -> Icons.Outlined.RecordVoiceOver
        FeatureCategory.SENSORY -> Icons.Outlined.Visibility
    }

val ReviewChecklistItem.icon: ImageVector
    get() = when (this) {
        ReviewChecklistItem.ENTRANCE_RAMP -> Icons.AutoMirrored.Filled.Accessible
        ReviewChecklistItem.ACCESSIBLE_RESTROOM -> Icons.Filled.Wc
        ReviewChecklistItem.LIBRAS_SERVICE -> Icons.Filled.SignLanguage
        ReviewChecklistItem.INTERNAL_CIRCULATION -> Icons.Outlined.OpenInFull
    }

val FeatureStatus.icon: ImageVector
    get() = when (this) {
        FeatureStatus.AVAILABLE -> Icons.Outlined.CheckCircle
        FeatureStatus.PARTIAL -> Icons.Outlined.WarningAmber
        FeatureStatus.UNAVAILABLE -> Icons.Outlined.HighlightOff
        FeatureStatus.UNKNOWN -> Icons.AutoMirrored.Outlined.HelpOutline
    }

val AccessibilityProfile.icon: ImageVector
    get() = when (this) {
        AccessibilityProfile.WHEELCHAIR -> Icons.AutoMirrored.Filled.Accessible
        AccessibilityProfile.REDUCED_MOBILITY -> Icons.AutoMirrored.Outlined.DirectionsWalk
        AccessibilityProfile.VISUAL -> Icons.Outlined.Visibility
        AccessibilityProfile.DEAF -> Icons.Filled.SignLanguage
        AccessibilityProfile.NEURODIVERGENT -> Icons.Outlined.Psychology
        AccessibilityProfile.ELDERLY -> Icons.Outlined.Elderly
        AccessibilityProfile.COMPANION -> Icons.Outlined.FamilyRestroom
    }
