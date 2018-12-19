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
@file:Suppress("unused")

package com.afollestad.materialdialogs.list

import android.support.annotation.ArrayRes
import android.support.annotation.CheckResult
import android.support.v7.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.R.attr
import com.afollestad.materialdialogs.assertOneSet
import com.afollestad.materialdialogs.internal.list.PlainListDialogAdapter
import com.afollestad.materialdialogs.utils.MDUtil.resolveDrawable
import com.afollestad.materialdialogs.utils.getStringArray

/** Gets the RecyclerView for a list dialog, if there is one. */
@CheckResult
fun MaterialDialog.getRecyclerView(): RecyclerView? {
  return this.view.contentLayout.recyclerView
}

/** A shortcut to [RecyclerView.getAdapter] on [getRecyclerView]. */
@CheckResult fun MaterialDialog.getListAdapter(): RecyclerView.Adapter<*>? {
  return getRecyclerView()?.adapter
}

/**
 * Sets a custom list adapter to render custom list content.
 *
 * Cannot be used in combination with message, input, and some other types of dialogs.
 */
@CheckResult fun MaterialDialog.customListAdapter(
  adapter: RecyclerView.Adapter<*>
): MaterialDialog {
  this.view.contentLayout.addRecyclerView(
      dialog = this,
      adapter = adapter
  )
  return this
}

/**
 * @param res The string array resource to populate the list with.
 * @param items The literal string array to populate the list with.
 * @param waitForPositiveButton When true, the [selection] listener won't be called until an item
 *    is selected and the positive action button is pressed. Defaults to true if the dialog has buttons.
 * @param selection A listener invoked when an item in the list is selected.
 */
@CheckResult fun MaterialDialog.listItems(
        @ArrayRes res: Int? = null,
        items: List<String>? = null,
        disabledIndices: IntArray? = null,
        waitForPositiveButton: Boolean = true,
        selection: ItemListener = null
): MaterialDialog {
  assertOneSet("listItems", items, res)
  val array = items ?: getStringArray(res)?.toList() ?: return this
  val adapter = getListAdapter()

  if (adapter is PlainListDialogAdapter) {
    adapter.replaceItems(array, selection)
    if (disabledIndices != null) {
      adapter.disableItems(disabledIndices)
    }
    return this
  }

  return customListAdapter(
      PlainListDialogAdapter(
          dialog = this,
          items = array,
          disabledItems = disabledIndices,
          waitForActionButton = waitForPositiveButton,
          selection = selection
      )
  )
}

internal fun MaterialDialog.getItemSelector() =
  resolveDrawable(context = context, attr = attr.md_item_selector)
