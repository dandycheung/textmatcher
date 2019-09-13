package com.santalu.textmatcher

import android.text.Editable
import android.text.TextWatcher
import com.santalu.textmatcher.rule.Rule

/**
 * Created by fatih.santalu on 9/9/2019
 *
 * Simple text watcher matches appropriate targets according to given @Rule
 */

class TextMatcher(
  val rules: List<Rule>,
  val action: OnMatchListener
) : TextWatcher {

  internal var isMatchingEnabled = true

  override fun afterTextChanged(text: Editable?) {
    if (text.isNullOrEmpty()) return

    // customize visual style of targets
    rules.forEach {
      it.applyStyle(text)
    }
  }

  override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
  }

  override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
    if (!isMatchingEnabled) return

    rules.forEach {
      if (text.isNullOrEmpty()) return action(it, null)

      // find closest target's boundaries from selection
      val targetStart = it.getTargetStart(text, start)
      val targetEnd = it.getTargetEnd(text, start)
      val target = text.substring(targetStart, targetEnd)

      if (it.isMatches(target)) {
        action(it, target)
      } else {
        action(it, null)
      }
    }
  }
}