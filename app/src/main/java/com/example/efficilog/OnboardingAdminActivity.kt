package com.example.efficilog

import android.content.Intent
import android.widget.TextView
import android.widget.Button
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

class OnboardingAdminActivity : AppCompatActivity() {

    private lateinit var tvSkip: TextView
    private lateinit var btnBack: Button
    private lateinit var btnContinue: Button
    private lateinit var onboardingManager: OnboardingManager


    private var selectedRole: String? = null

    companion object {
        private const val TAG = "OnboardingAdmin"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_admin)

        onboardingManager = OnboardingManager(this)

        selectedRole = intent.getStringExtra("SELECTED_ROLE")
        Log.d("OnboardingAdmin", "OnboardingAdminActivity created with role: $selectedRole")

        // Initialize views
        initializeViews()
        setupClickListeners()

        // ✅ REMOVED: Don't automatically navigate - let user interact with the screen
        // navigateToMainActivity()
    }

    private fun initializeViews() {
        tvSkip = findViewById(R.id.tv_skip)
        btnBack = findViewById(R.id.btn_back)
        btnContinue = findViewById(R.id.btn_continue)
        Log.d("OnboardingAdmin", "Views initialized successfully")
    }

    private fun setupClickListeners() {
        tvSkip.setOnClickListener {
            Log.d("OnboardingAdmin", "Skip clicked - navigating to MainActivity")
            // Navigate to the main activity
            navigateToMainActivity()
        }

        btnBack.setOnClickListener {
            Log.d("OnboardingAdmin", "Back clicked - returning to role selection")
            // Navigate back to the role selection screen
            onBackPressed()
        }

        btnContinue.setOnClickListener {
            Log.d("OnboardingAdmin", "Continue clicked - navigating to completion")
            // Navigate to the next onboarding step
            navigateToCompletion() // Assuming this is the final step
        }
    }

    private fun navigateToCompletion() {
        try {
            val intent = Intent(this, OnboardingCompletionActivity::class.java)
            intent.putExtra("SELECTED_ROLE", selectedRole)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish() // Close this activity
        } catch (e: Exception) {
            Log.e("OnboardingAdmin", "Error navigating to completion", e)
            // If OnboardingCompletionActivity doesn't exist, go to MainActivity
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        try {
            // Save onboarding completion status
//            val sharedPref = getSharedPreferences("onboarding_prefs", MODE_PRIVATE)
//            with(sharedPref.edit()) {
//                putBoolean("onboarding_completed", true)
//                putString("user_role", selectedRole)
//                apply()
//            }

            onboardingManager.skipOnboarding()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        } catch (e: Exception) {
            Log.e("OnboardingAdmin", "Error navigating to MainActivity", e)
        }
    }

    override fun onBackPressed() {
        // Navigate back to the role selection screen
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
//package com.example.efficilog
//
//import android.R
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.Button
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.constraintlayout.widget.ConstraintLayout
//import kotlin.jvm.internal.Intrinsics
//
///* compiled from: OnboardingAdminActivity.kt */
//@Metadata(
//    d1 = ["\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\t\b\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u000b\u001a\u00020\u000c2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0014J\b\u0010\u000f\u001a\u00020\u000cH\u0002J\b\u0010\u0010\u001a\u00020\u000cH\u0002J\b\u0010\u0011\u001a\u00020\u000cH\u0002J\b\u0010\u0012\u001a\u00020\u000cH\u0002J\b\u0010\u0013\u001a\u00020\u000cH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082.¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0014"],
//    d2 = ["Lcom/example/efficilog/OnboardingAdminActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "<init>", "()V", "tvSkip", "Landroid/widget/TextView;", "btnBack", "Landroid/widget/Button;", "btnContinue", "selectedRole", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "initializeViews", "setupClickListeners", "navigateToCompletion", "navigateToMainActivity", "onBackPressed", "app_debug"],
//    k = 1,
//    mv = [2, 0, 0],
//    xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE
//) /* loaded from: classes4.dex */
//class OnboardingAdminActivity : AppCompatActivity() {
//    private var btnBack: Button? = null
//    private var btnContinue: Button? = null
//    private var selectedRole: String? = null
//    private var tvSkip: TextView? = null
//
//    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
//    public override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_onboarding_admin)
//        this.selectedRole = getIntent().getStringExtra("SELECTED_ROLE")
//        Log.d("OnboardingAdmin", "OnboardingAdminActivity created with role: " + this.selectedRole)
//        initializeViews()
//        setupClickListeners()
//    }
//
//    private fun initializeViews() {
//        this.tvSkip = findViewById<View?>(R.id.tv_skip) as TextView?
//        this.btnBack = findViewById<View?>(R.id.btn_back) as Button?
//        this.btnContinue = findViewById<View?>(R.id.btn_continue) as Button?
//        Log.d("OnboardingAdmin", "Views initialized successfully")
//    }
//
//    private fun setupClickListeners() {
//        var textView = this.tvSkip
//        var button: Button? = null
//        if (textView == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("tvSkip")
//            textView = null
//        }
//        textView!!.setOnClickListener(object : View.OnClickListener {
//            // from class: com.example.efficilog.OnboardingAdminActivity$$ExternalSyntheticLambda0
//            // android.view.View.OnClickListener
//            override fun onClick(view: View?) {
//                `m6460$r8$lambda$FcC7uy9SwSTFX04LEa4hS8v6bw`(this@OnboardingAdminActivity, view)
//            }
//        })
//        var button2 = this.btnBack
//        if (button2 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("btnBack")
//            button2 = null
//        }
//        button2!!.setOnClickListener(object : View.OnClickListener {
//            // from class: com.example.efficilog.OnboardingAdminActivity$$ExternalSyntheticLambda1
//            // android.view.View.OnClickListener
//            override fun onClick(view: View?) {
//                `$r8$lambda$JfDYOyvAel7eACXQ2zeM3PjkWWk`(this@OnboardingAdminActivity, view)
//            }
//        })
//        val button3 = this.btnContinue
//        if (button3 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("btnContinue")
//        } else {
//            button = button3
//        }
//        button!!.setOnClickListener(object : View.OnClickListener {
//            // from class: com.example.efficilog.OnboardingAdminActivity$$ExternalSyntheticLambda2
//            // android.view.View.OnClickListener
//            override fun onClick(view: View?) {
//                `$r8$lambda$oQmJ4JFCM2fPgpqQz0L2YNs9_IQ`(this@OnboardingAdminActivity, view)
//            }
//        })
//    }
//
//    private fun navigateToCompletion() {
//        try {
//            val intent: Intent = Intent(this, OnboardingCompletionActivity::class.java)
//            intent.putExtra("SELECTED_ROLE", this.selectedRole)
//            startActivity(intent)
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//            finish()
//        } catch (e: Exception) {
//            Log.e("OnboardingAdmin", "Error navigating to completion", e)
//            navigateToMainActivity()
//        }
//    }
//
//    private fun navigateToMainActivity() {
//        try {
//            val sharedPref = getSharedPreferences("onboarding_prefs", 0)
//            val `$this$navigateToMainActivity_u24lambda_u243` = sharedPref.edit()
//            `$this$navigateToMainActivity_u24lambda_u243`.putBoolean("onboarding_completed", true)
//            `$this$navigateToMainActivity_u24lambda_u243`.putString("user_role", this.selectedRole)
//            `$this$navigateToMainActivity_u24lambda_u243`.apply()
//            val intent = Intent(this, MainActivity::class.java)
//            intent.setFlags(268468224)
//            startActivity(intent)
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//            finish()
//        } catch (e: Exception) {
//            Log.e("OnboardingAdmin", "Error navigating to MainActivity", e)
//        }
//    }
//
//    // androidx.activity.ComponentActivity, android.app.Activity
//    override fun onBackPressed() {
//        super.onBackPressed()
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
//    }
//
//    companion object {
//        const val `$stable`: Int = 8
//
//        /* renamed from: $r8$lambda$FcC7uy9SwSTFX04LEa4h-S8v6bw */
//        fun `m6460$r8$lambda$FcC7uy9SwSTFX04LEa4hS8v6bw`(
//            onboardingAdminActivity: OnboardingAdminActivity,
//            view: View?
//        ) {
//            `setupClickListeners$lambda$0`(onboardingAdminActivity, view)
//        }
//
//        fun `$r8$lambda$JfDYOyvAel7eACXQ2zeM3PjkWWk`(
//            onboardingAdminActivity: OnboardingAdminActivity,
//            view: View?
//        ) {
//            `setupClickListeners$lambda$1`(onboardingAdminActivity, view)
//        }
//
//        fun `$r8$lambda$oQmJ4JFCM2fPgpqQz0L2YNs9_IQ`(
//            onboardingAdminActivity: OnboardingAdminActivity,
//            view: View?
//        ) {
//            `setupClickListeners$lambda$2`(onboardingAdminActivity, view)
//        }
//
//        fun `setupClickListeners$lambda$0`(`this$0`: OnboardingAdminActivity, it: View?) {
//            Intrinsics.checkNotNullParameter(`this$0`, "this$0")
//            Log.d("OnboardingAdmin", "Skip clicked - navigating to MainActivity")
//            `this$0`.navigateToMainActivity()
//        }
//
//        fun `setupClickListeners$lambda$1`(`this$0`: OnboardingAdminActivity, it: View?) {
//            Intrinsics.checkNotNullParameter(`this$0`, "this$0")
//            Log.d("OnboardingAdmin", "Back clicked - returning to role selection")
//            `this$0`.onBackPressed()
//        }
//
//        fun `setupClickListeners$lambda$2`(`this$0`: OnboardingAdminActivity, it: View?) {
//            Intrinsics.checkNotNullParameter(`this$0`, "this$0")
//            Log.d("OnboardingAdmin", "Continue clicked - navigating to completion")
//            `this$0`.navigateToCompletion()
//        }
//    }
//}