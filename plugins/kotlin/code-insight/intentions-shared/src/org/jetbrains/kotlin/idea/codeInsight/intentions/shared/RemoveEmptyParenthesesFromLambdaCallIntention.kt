// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package org.jetbrains.kotlin.idea.codeInsight.intentions.shared

import com.intellij.codeInspection.CleanupLocalInspectionTool
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import org.jetbrains.kotlin.idea.base.resources.KotlinBundle
import org.jetbrains.kotlin.idea.codeinsight.api.classic.intentions.SelfTargetingRangeIntention
import org.jetbrains.kotlin.idea.codeinsight.api.classic.inspections.IntentionBasedInspection
import org.jetbrains.kotlin.idea.codeinsights.impl.base.applicators.RemoveEmptyParenthesesFromLambdaCallIntentionUtils
import org.jetbrains.kotlin.psi.KtValueArgumentList

@Suppress("DEPRECATION")
class RemoveEmptyParenthesesFromLambdaCallInspection : IntentionBasedInspection<KtValueArgumentList>(
    RemoveEmptyParenthesesFromLambdaCallIntention::class
), CleanupLocalInspectionTool

class RemoveEmptyParenthesesFromLambdaCallIntention : SelfTargetingRangeIntention<KtValueArgumentList>(
    KtValueArgumentList::class.java, KotlinBundle.lazyMessage("remove.unnecessary.parentheses.from.function.call.with.lambda")
) {
    override fun applicabilityRange(element: KtValueArgumentList): TextRange? =
        RemoveEmptyParenthesesFromLambdaCallIntentionUtils.applicabilityRange(element)

    override fun applyTo(element: KtValueArgumentList, editor: Editor?) =
        RemoveEmptyParenthesesFromLambdaCallIntentionUtils.applyTo(element)
}
