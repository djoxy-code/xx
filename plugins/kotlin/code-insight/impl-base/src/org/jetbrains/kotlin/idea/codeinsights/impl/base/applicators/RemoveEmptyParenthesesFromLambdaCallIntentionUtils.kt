// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package org.jetbrains.kotlin.idea.codeinsights.impl.base.applicators

import com.intellij.openapi.util.TextRange
import org.jetbrains.kotlin.idea.base.psi.getLineNumber
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtQualifiedExpression
import org.jetbrains.kotlin.psi.KtValueArgumentList
import org.jetbrains.kotlin.psi.psiUtil.getPrevSiblingIgnoringWhitespaceAndComments

object RemoveEmptyParenthesesFromLambdaCallIntentionUtils {
    private fun isApplicable(list: KtValueArgumentList): Boolean = applicabilityRange(list) != null

    fun applyTo(list: KtValueArgumentList) = list.delete()

    fun applyToIfApplicable(list: KtValueArgumentList) {
        if (isApplicable(list)) {
            applyTo(list)
        }
    }

    fun applicabilityRange(list: KtValueArgumentList): TextRange? {
        if (list.arguments.isNotEmpty()) return null
        val parent = list.parent as? KtCallExpression ?: return null
        if (parent.calleeExpression?.text == KtTokens.SUSPEND_KEYWORD.value) return null
        val singleLambdaArgument = parent.lambdaArguments.singleOrNull() ?: return null
        if (list.getLineNumber(start = false) != singleLambdaArgument.getLineNumber(start = true)) return null
        val prev = list.getPrevSiblingIgnoringWhitespaceAndComments()
        if (prev is KtCallExpression || (prev as? KtQualifiedExpression)?.selectorExpression is KtCallExpression) return null
        return list.textRange
    }
}