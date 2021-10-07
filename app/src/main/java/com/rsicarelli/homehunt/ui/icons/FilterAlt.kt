package com.rsicarelli.homehunt.ui.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Rounded.FilterAlt: ImageVector
    get() {
        if (_filterAlt != null) {
            return _filterAlt!!
        }
        _filterAlt = materialIcon(name = "Rounded.FilterAlt") {
            materialPath {
                moveTo(4.21f, 5.61f)
                curveTo(6.23f, 8.2f, 10.0f, 13.0f, 10.0f, 13.0f)
                verticalLineToRelative(5.0f)
                curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
                horizontalLineToRelative(0.0f)
                curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
                verticalLineToRelative(-5.0f)
                curveToRelative(0.0f, 0.0f, 3.77f, -4.8f, 5.79f, -7.39f)
                curveTo(20.3f, 4.95f, 19.83f, 4.0f, 19.0f, 4.0f)
                horizontalLineTo(5.0f)
                curveTo(4.17f, 4.0f, 3.7f, 4.95f, 4.21f, 5.61f)
                close()
            }
        }
        return _filterAlt!!
    }

private var _filterAlt: ImageVector? = null
