package dev.lucasnlm.external

import android.app.Activity

class ReviewWrapper : IReviewWrapper {
    override fun startReviewPage(activity: Activity, appPackage: String) {
        // There's not review on FOSS build.
    }

    override fun startInAppReview(activity: Activity) {
        // There's not review on FOSS build.
    }
}
