package com.github.shadowsocks.preference

import android.content.Context
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatEditText
import com.takisoft.preferencex.R

open class SSDPasswordToggleEditTextPreference @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.editTextPreferenceStyle, defStyleRes: Int = 0) : androidx.preference.EditTextPreference(context, attrs, defStyleAttr, defStyleRes) {
    val editText: EditText
    private var summaryHasText: CharSequence? = null
    private var summary: CharSequence? = null

    var passwordSubstituteLength: Int = 0
    private var passwordSubstitute: String? = null

    init {
        editText = AppCompatEditText(context, attrs)
        editText.id = android.R.id.edit

        val a = context.obtainStyledAttributes(attrs, R.styleable.AutoSummaryEditTextPreference, defStyleAttr, 0)
        summaryHasText = a.getText(R.styleable.AutoSummaryEditTextPreference_pref_summaryHasText)

        passwordSubstitute = a.getString(R.styleable.AutoSummaryEditTextPreference_pref_summaryPasswordSubstitute)
        passwordSubstituteLength = a.getInt(R.styleable.AutoSummaryEditTextPreference_pref_summaryPasswordSubstituteLength, 5)

        if (passwordSubstitute == null) {
            passwordSubstitute = "\u2022"
        }

        a.recycle()

        summary = super.getSummary()
    }

    override fun setText(text: String?) {
        val oldText = getText()
        super.setText(text)
        if (!TextUtils.equals(text, oldText)) {
            notifyChanged()
        }
    }

    /**
     * Returns the summary of this Preference. If no `pref_summaryHasText` is set, this will
     * be displayed if no value is set; otherwise the value will be used.
     *
     * @return The summary.
     */
    override fun getSummary(): CharSequence? {
        var text: CharSequence = text
        val hasText = !TextUtils.isEmpty(text)

        if (!hasText) {
            return summary
        } else {
            val inputType = editText.inputType

            if (inputType and InputType.TYPE_NUMBER_VARIATION_PASSWORD == InputType.TYPE_NUMBER_VARIATION_PASSWORD ||
                    inputType and InputType.TYPE_TEXT_VARIATION_PASSWORD == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                    inputType and InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD) {
                text = String(CharArray(if (passwordSubstituteLength > 0) passwordSubstituteLength else text.length)).replace(Regex("0"), passwordSubstitute?:"")
            }

            return if (summaryHasText != null) {
                String.format(summaryHasText!!.toString(), text)
            } else {
                text
            }
        }
    }

    /**
     * Sets the summary for this Preference with a CharSequence. If no `pref_summaryHasText`
     * is set, this will be displayed if no value is set; otherwise the value will be used.
     *
     * @param summary The summary for the preference.
     */
    override fun setSummary(summary: CharSequence?) {
        super.setSummary(summary)
        if (summary == null && this.summary != null) {
            this.summary = null
        } else if (summary != null && summary != this.summary) {
            this.summary = summary.toString()
        }
    }

    /**
     * Returns the summary for this Preference. This will be displayed if the preference
     * has a persisted value or the default value is set. If the summary
     * has a [String formatting][java.lang.String.format]
     * marker in it (i.e. "%s" or "%1$s"), then the current value will be substituted in its place.
     *
     * @return The picked summary.
     */
    fun getSummaryHasText(): CharSequence? {
        return summaryHasText
    }

    /**
     * Sets the summary for this Preference with a resource ID. This will be displayed if the
     * preference has a persisted value or the default value is set. If the summary
     * has a [String formatting][java.lang.String.format]
     * marker in it (i.e. "%s" or "%1$s"), then the current value will be substituted in its place.
     *
     * @param resId The summary as a resource.
     * @see .setSummaryHasText
     */
    fun setSummaryHasText(@StringRes resId: Int) {
        setSummaryHasText(context.getString(resId))
    }

    /**
     * Sets the summary for this Preference with a CharSequence. This will be displayed if
     * the preference has a persisted value or the default value is set. If the summary
     * has a [String formatting][java.lang.String.format]
     * marker in it (i.e. "%s" or "%1$s"), then the current value will be substituted in its place.
     *
     * @param summaryHasText The summary for the preference.
     */
    fun setSummaryHasText(summaryHasText: CharSequence?) {
        if (summaryHasText == null && this.summaryHasText != null) {
            this.summaryHasText = null
        } else if (summaryHasText != null && summaryHasText != this.summaryHasText) {
            this.summaryHasText = summaryHasText.toString()
        }

        notifyChanged()
    }

    /**
     * Returns the substitute characters to be used for displaying passwords in the summary.
     *
     * @return The substitute characters to be used for displaying passwords in the summary.
     */
    fun getPasswordSubstitute(): CharSequence? {
        return passwordSubstitute
    }

    /**
     * Sets the substitute characters to be used for displaying passwords in the summary.
     *
     * @param resId The substitute characters as a resource.
     * @see .setPasswordSubstitute
     */
    fun setPasswordSubstitute(@StringRes resId: Int) {
        setPasswordSubstitute(context.getString(resId))
    }

    /**
     * Sets the substitute characters to be used for displaying passwords in the summary.
     *
     * @param passwordSubstitute The substitute characters to be used for displaying passwords in
     * the summary.
     */
    fun setPasswordSubstitute(passwordSubstitute: String) {
        this.passwordSubstitute = passwordSubstitute
    }

}




