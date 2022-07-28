// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package org.jetbrains.kotlin.idea.k2.codeinsight.intentions

import org.jetbrains.kotlin.idea.base.resources.KotlinBundle
import org.jetbrains.kotlin.idea.codeinsight.api.applicators.*
import org.jetbrains.kotlin.idea.codeinsights.impl.base.applicators.ApplicabilityRanges
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.createExpressionByPattern

object RemoveSingleExpressionStringTemplateInspectionUtils {
    val applicator: KotlinApplicator<KtStringTemplateExpression, RemoveSingleExpressionStringTemplateIntention.Input> = applicator {
        familyAndActionName(KotlinBundle.lazyMessage("remove.single.expression.string.template"))
        isApplicableByPsi { stringTemplateExpression: KtStringTemplateExpression ->
            stringTemplateExpression.singleExpressionOrNull() != null
        }
        applyTo { stringTemplateExpression, input ->
            val newElement = if (input.isString) {
                input.expressionWithStringValue
            } else {
                KtPsiFactory(stringTemplateExpression).createExpressionByPattern(
                    pattern = "$0.$1()",
                    input.expressionWithStringValue,
                    "toString"
                )
            }
            stringTemplateExpression.replace(newElement)
        }
    }

    val applicabilityRange: KotlinApplicabilityRange<KtStringTemplateExpression> = ApplicabilityRanges.SELF

    val inputProvider: KotlinApplicatorInputProvider<KtStringTemplateExpression, RemoveSingleExpressionStringTemplateIntention.Input> =
        inputProvider { stringTemplateExpression: KtStringTemplateExpression ->
            val expression = stringTemplateExpression.singleExpressionOrNull() ?: return@inputProvider null
            RemoveSingleExpressionStringTemplateIntention.Input(expression, expression.getKtType()?.isString == true)
        }

    private fun KtStringTemplateExpression.singleExpressionOrNull() = children.singleOrNull()?.children?.firstOrNull() as? KtExpression
}

class RemoveSingleExpressionStringTemplateInspection() :
    AbstractKotlinApplicatorBasedInspection<KtStringTemplateExpression, RemoveSingleExpressionStringTemplateIntention.Input>(
        KtStringTemplateExpression::class
    ) {
    override fun getApplicator(): KotlinApplicator<KtStringTemplateExpression, RemoveSingleExpressionStringTemplateIntention.Input> =
        RemoveSingleExpressionStringTemplateInspectionUtils.applicator

    override fun getApplicabilityRange(): KotlinApplicabilityRange<KtStringTemplateExpression> =
        RemoveSingleExpressionStringTemplateInspectionUtils.applicabilityRange

    override fun getInputProvider(): KotlinApplicatorInputProvider<KtStringTemplateExpression, RemoveSingleExpressionStringTemplateIntention.Input> =
        RemoveSingleExpressionStringTemplateInspectionUtils.inputProvider
}

class RemoveSingleExpressionStringTemplateIntention :
    AbstractKotlinApplicatorBasedIntention<KtStringTemplateExpression, RemoveSingleExpressionStringTemplateIntention.Input>(
        KtStringTemplateExpression::class
    ) {
    data class Input(val expressionWithStringValue: KtExpression, val isString: Boolean) : KotlinApplicatorInput

    override fun getApplicator(): KotlinApplicator<KtStringTemplateExpression, Input> =
        RemoveSingleExpressionStringTemplateInspectionUtils.applicator

    override fun getApplicabilityRange(): KotlinApplicabilityRange<KtStringTemplateExpression> =
        RemoveSingleExpressionStringTemplateInspectionUtils.applicabilityRange

    override fun getInputProvider(): KotlinApplicatorInputProvider<KtStringTemplateExpression, Input> =
        RemoveSingleExpressionStringTemplateInspectionUtils.inputProvider
}