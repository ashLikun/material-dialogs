/**
 * Designed and developed by Aidan Follestad (@afollestad)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.afollestad.materialdialogs.utils

import android.content.Context
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.annotation.*
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
object MDUtil {

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) fun resolveString(
          materialDialog: MaterialDialog,
          @StringRes res: Int? = null,
          @StringRes fallback: Int? = null,
          html: Boolean = false
  ): CharSequence? {
    return resolveString(
        context = materialDialog.windowContext,
        res = res,
        fallback = fallback,
        html = html
    )
  }

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) fun resolveString(
    context: Context,
    @StringRes res: Int? = null,
    @StringRes fallback: Int? = null,
    html: Boolean = false
  ): CharSequence? {
    val resourceId = res ?: (fallback ?: 0)
    if (resourceId == 0) return null
    val text = context.resources.getText(resourceId)
    if (html) {
      @Suppress("DEPRECATION")
      return Html.fromHtml(text.toString())
    }
    return text
  }

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) fun resolveDrawable(
          context: Context,
          @DrawableRes res: Int? = null,
          @AttrRes attr: Int? = null,
          fallback: Drawable? = null
  ): Drawable? {
    if (attr != null) {
      val a = context.theme.obtainStyledAttributes(intArrayOf(attr))
      try {
        var d = a.getDrawable(0)
        if (d == null && fallback != null) {
          d = fallback
        }
        return d
      } finally {
        a.recycle()
      }
    }
    if (res == null) return fallback
    return ContextCompat.getDrawable(context, res)
  }

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
  @ColorInt
  fun resolveColor(
    context: Context,
    @ColorRes res: Int? = null,
    @AttrRes attr: Int? = null
  ): Int {
    if (attr != null) {
      val a = context.theme.obtainStyledAttributes(intArrayOf(attr))
      try {
        return a.getColor(0, 0)
      } finally {
        a.recycle()
      }
    }
    return ContextCompat.getColor(context, res ?: 0)
  }

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) fun Int.isColorDark(threshold: Double = 0.5): Boolean {
    if (this == Color.TRANSPARENT) {
      return false
    }
    val darkness =
      1 - (0.299 * Color.red(this) + 0.587 * Color.green(this) + 0.114 * Color.blue(this)) / 255
    return darkness >= threshold
  }

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) fun <T : View> T.dimenPx(@DimenRes res: Int): Int {
    return context.resources.getDimensionPixelSize(res)
  }

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) fun isLandscape(context: Context) =
    context.resources.configuration.orientation == ORIENTATION_LANDSCAPE

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) fun EditText.textChanged(callback: (CharSequence) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable) = Unit

      override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
      ) = Unit

      override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
      ) = callback.invoke(s)
    })
  }
}
