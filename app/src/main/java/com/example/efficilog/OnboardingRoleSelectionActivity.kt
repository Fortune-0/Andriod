package com.example.efficilog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup

import android.widget.TextView
import android.content.Intent
import android.util.Log

class OnboardingRoleSelectionActivity : AppCompatActivity() {

    private lateinit var radioGroup: RadioGroup
    private lateinit var rbAdministrator: RadioButton
    private lateinit var rbStaff: RadioButton
    private lateinit var btnContinue: Button
    private lateinit var btnBack: Button
    private lateinit var tvSkip: TextView
    private lateinit var ivAdminIndicator: ImageView
    private lateinit var ivStaffIndicator: ImageView
    private lateinit var onboardingManager: OnboardingManager


    // Track selected role
    private var selectedRole: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_role_selection)

        onboardingManager = OnboardingManager(this)

        initializeViews()
        setupClickListeners()
        setupRadioGroupListener()

        // Initially disable continue button
        updateContinueButton()
    }

    private fun initializeViews() {
        radioGroup = findViewById(R.id.rg_user_role)
        rbAdministrator = findViewById(R.id.rb_administrator)
        rbStaff = findViewById(R.id.rb_staff)
        btnContinue = findViewById(R.id.btn_continue)
        btnBack = findViewById(R.id.btn_back)
        tvSkip = findViewById(R.id.skip)
        ivAdminIndicator = findViewById(R.id.iv_admin_indicator)
        ivStaffIndicator = findViewById(R.id.iv_staff_indicator)

        Log.d("OnboardingRole", "Views initialized")
    }

    private fun setupClickListeners() {
        btnContinue.setOnClickListener {
            Log.d("OnboardingRole", "Continue clicked, selected role: $selectedRole")
            if (selectedRole.isNotEmpty()) {
                navigateToFeatureScreen()
            } else {
                Toast.makeText(this, "Please select a role to continue", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            Log.d("OnboardingRole", "Back clicked")
            onBackPressed()
        }

        tvSkip.setOnClickListener {
            Log.d("OnboardingRole", "Skip clicked")
            skipOnboarding()
        }

        // Simplified approach - use the RadioButton's built-in behavior
        rbAdministrator.setOnCheckedChangeListener { _, isChecked ->
            Log.d("OnboardingRole", "Administrator checked: $isChecked")
            if (isChecked) {
                selectedRole = "Administrator"
                updateSelection()
            }
        }

        rbStaff.setOnCheckedChangeListener { _, isChecked ->
            Log.d("OnboardingRole", "Staff checked: $isChecked")
            if (isChecked) {
                selectedRole = "Staff"
                updateSelection()
            }
        }
    }

    private fun setupRadioGroupListener() {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            Log.d("OnboardingRole", "RadioGroup changed, checkedId: $checkedId")
            when (checkedId) {
                R.id.rb_administrator -> {
                    selectedRole = "Administrator"
                    updateSelection()
                }
                R.id.rb_staff -> {
                    selectedRole = "Staff"
                    updateSelection()
                }
                else -> {
                    selectedRole = ""
                    updateSelection()
                }
            }
        }
    }

    private fun updateSelection() {
        Log.d("OnboardingRole", "Updating selection for role: $selectedRole")

        // Reset all indicators
        ivAdminIndicator.visibility = View.GONE
        ivStaffIndicator.visibility = View.GONE

        // Show indicator for selected role
        when (selectedRole) {
            "Administrator" -> {
                ivAdminIndicator.visibility = View.VISIBLE
                Log.d("OnboardingRole", "Admin indicator shown")
            }
            "Staff" -> {
                ivStaffIndicator.visibility = View.VISIBLE
                Log.d("OnboardingRole", "Staff indicator shown")
            }
        }

        updateContinueButton()
    }

    private fun updateContinueButton() {
        val isRoleSelected = selectedRole.isNotEmpty()
        btnContinue.isEnabled = isRoleSelected
        btnContinue.alpha = if (isRoleSelected) 1.0f else 0.5f
        Log.d("OnboardingRole", "Continue button enabled: $isRoleSelected")
    }

    private fun navigateToFeatureScreen() {
        Log.d("OnboardingRole", "Navigating to feature screen for role: $selectedRole")

        try {
            val intent = when (selectedRole) {
                "Administrator" -> {
                    Log.d("OnboardingRole", "Creating intent for OnboardingAdminActivity")
                    Intent(this, OnboardingAdminActivity::class.java)
                }
                "Staff" -> {
                    Log.d("OnboardingRole", "Creating intent for OnboardingStaffActivity")
                    Intent(this, OnboardingStaffActivity::class.java)
                }
                else -> {
                    Log.e("OnboardingRole", "Invalid role selected: $selectedRole")
                    Toast.makeText(this, "Invalid role selected", Toast.LENGTH_SHORT).show()
                    return
                }
            }

            // Pass the selected role to the next activity
            intent.putExtra("SELECTED_ROLE", selectedRole)
            Log.d("OnboardingRole", "Starting activity with intent: ${intent.component?.className}")

            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

            Log.d("OnboardingRole", "Activity started successfully")

        } catch (e: Exception) {
            Log.e("OnboardingRole", "Error navigating to feature screen", e)
            Toast.makeText(this, "Error: Unable to navigate. ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun skipOnboarding() {
        Log.d("OnboardingRole", "Skipping onboarding")

//        val sharedPref = getSharedPreferences("onboarding_prefs", MODE_PRIVATE)
//        with(sharedPref.edit()) {
//            putBoolean("onboarding_completed", true)
//            apply()
//        }

        onboardingManager.skipOnboarding()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

//        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        Log.d("OnboardingRole", "Back pressed")
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
//package com.example.efficilog
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.RadioButton
//import android.widget.RadioGroup
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//
//class OnboardingRoleSelectionActivity : AppCompatActivity() {
//
//    private lateinit var radioGroup: RadioGroup
//    private lateinit var rbAdministrator: RadioButton
//    private lateinit var rbStaff: RadioButton
//    private lateinit var btnContinue: Button
//    private lateinit var btnBack: Button
//    private lateinit var tvSkip: TextView
//    private lateinit var ivAdminIndicator: ImageView
//    private lateinit var ivStaffIndicator: ImageView
//
//    // Track selected role
//    private var selectedRole: String = ""
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_onboarding_role_selection)
//
//        initializeViews()
//        setupClickListeners()
//        setupRadioGroupListener()
//    }
//
//    private fun initializeViews() {
//        radioGroup = findViewById(R.id.rg_user_role)
//        rbAdministrator = findViewById(R.id.rb_administrator)
//        rbStaff = findViewById(R.id.rb_staff)
//        btnContinue = findViewById(R.id.btn_continue)
//        btnBack = findViewById(R.id.btn_back)
//        tvSkip = findViewById(R.id.skip)
//        ivAdminIndicator = findViewById(R.id.iv_admin_indicator)
//        ivStaffIndicator = findViewById(R.id.iv_staff_indicator)
//
//        // Initially disable continue button until user selects a role
//        btnContinue.isEnabled = false
//        btnContinue.alpha = 0.5f
//    }
//
//    private fun setupClickListeners() {
//        btnContinue.setOnClickListener {
//            if (selectedRole.isNotEmpty()) {
//                navigateToFeatureScreen()
//            } else {
//                Toast.makeText(this, "Please select a role to continue", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        btnBack.setOnClickListener {
//            onBackPressed()
//        }
//
//        tvSkip.setOnClickListener {
//            skipOnboarding()
//        }
//
//        // Add manual click listeners for RadioButtons as backup
//        rbAdministrator.setOnClickListener {
//            if (!rbAdministrator.isChecked) {
//                rbAdministrator.isChecked = true
//                rbStaff.isChecked = false
//                updateSelection("Administrator")
//            }
//        }
//
//        rbStaff.setOnClickListener {
//            if (!rbStaff.isChecked) {
//                rbStaff.isChecked = true
//                rbAdministrator.isChecked = false
//                updateSelection("Staff")
//            }
//        }
//    }
//
//    private fun setupRadioGroupListener() {
//        radioGroup.setOnCheckedChangeListener { _, checkedId ->
//            when (checkedId) {
//                R.id.rb_administrator -> updateSelection("Administrator")
//                R.id.rb_staff -> updateSelection("Staff")
//                else -> updateSelection("")
//            }
//        }
//    }
//
//    private fun updateSelection(role: String) {
//        selectedRole = role
//
//        // Reset all indicators
//        ivAdminIndicator.visibility = View.GONE
//        ivStaffIndicator.visibility = View.GONE
//
//        // Show indicator for selected role
//        when (role) {
//            "Administrator" -> ivAdminIndicator.visibility = View.VISIBLE
//            "Staff" -> ivStaffIndicator.visibility = View.VISIBLE
//        }
//
//        // Enable/disable continue button
//        btnContinue.isEnabled = selectedRole.isNotEmpty()
//        btnContinue.alpha = if (selectedRole.isNotEmpty()) 1.0f else 0.5f
//    }
//
//    private fun navigateToFeatureScreen() {
//        val intent = when (selectedRole) {
//            "Administrator" -> Intent(this, OnboardingAdminActivity::class.java)
//            "Staff" -> Intent(this, OnboardingStaffActivity::class.java)
//            else -> return
//        }
//
//        // Pass the selected role to the next activity
//        intent.putExtra("SELECTED_ROLE", selectedRole)
//        startActivity(intent)
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//    }
//
//    private fun skipOnboarding() {
//        val sharedPref = getSharedPreferences("onboarding_prefs", MODE_PRIVATE)
//        with(sharedPref.edit()) {
//            putBoolean("onboarding_completed", true)
//            apply()
//        }
//
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//
//    override fun onBackPressed() {
//        super.onBackPressed()
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
//    }
//}

//package com.example.efficilog
//
//import android.R
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.Button
//import android.widget.CompoundButton
//import android.widget.ImageView
//import android.widget.RadioButton
//import android.widget.RadioGroup
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.constraintlayout.widget.ConstraintLayout
//import kotlin.jvm.internal.Intrinsics
//
///* compiled from: OnboardingRoleSelectionActivity.kt */
//@Metadata(
//    d1 = ["\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B\t\b\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0014J\b\u0010\u0017\u001a\u00020\u0014H\u0002J\b\u0010\u0018\u001a\u00020\u0014H\u0002J\b\u0010\u0019\u001a\u00020\u0014H\u0002J\b\u0010\u001a\u001a\u00020\u0014H\u0002J\b\u0010\u001b\u001a\u00020\u0014H\u0002J\b\u0010\u001c\u001a\u00020\u0014H\u0002J\b\u0010\u001d\u001a\u00020\u0014H\u0002J\b\u0010\u001e\u001a\u00020\u0014H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000c\u001a\u00020\rX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001f"],
//    d2 = ["Lcom/example/efficilog/OnboardingRoleSelectionActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "<init>", "()V", "radioGroup", "Landroid/widget/RadioGroup;", "rbAdministrator", "Landroid/widget/RadioButton;", "rbStaff", "btnContinue", "Landroid/widget/Button;", "btnBack", "tvSkip", "Landroid/widget/TextView;", "ivAdminIndicator", "Landroid/widget/ImageView;", "ivStaffIndicator", "selectedRole", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "initializeViews", "setupClickListeners", "setupRadioGroupListener", "updateSelection", "updateContinueButton", "navigateToFeatureScreen", "skipOnboarding", "onBackPressed", "app_debug"],
//    k = 1,
//    mv = [2, 0, 0],
//    xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE
//) /* loaded from: classes4.dex */
//class OnboardingRoleSelectionActivity : AppCompatActivity() {
//    private var btnBack: Button? = null
//    private var btnContinue: Button? = null
//    private var ivAdminIndicator: ImageView? = null
//    private var ivStaffIndicator: ImageView? = null
//    private var radioGroup: RadioGroup? = null
//    private var rbAdministrator: RadioButton? = null
//    private var rbStaff: RadioButton? = null
//    private var selectedRole = ""
//    private var tvSkip: TextView? = null
//
//    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
//    public override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_onboarding_role_selection)
//        initializeViews()
//        setupClickListeners()
//        setupRadioGroupListener()
//        updateContinueButton()
//    }
//
//    private fun initializeViews() {
//        this.radioGroup = findViewById<View?>(R.id.rg_user_role) as RadioGroup?
//        this.rbAdministrator = findViewById<View?>(R.id.rb_administrator) as RadioButton?
//        this.rbStaff = findViewById<View?>(R.id.rb_staff) as RadioButton?
//        this.btnContinue = findViewById<View?>(R.id.btn_continue) as Button?
//        this.btnBack = findViewById<View?>(R.id.btn_back) as Button?
//        this.tvSkip = findViewById<View?>(R.id.skip) as TextView?
//        this.ivAdminIndicator = findViewById<View?>(R.id.iv_admin_indicator) as ImageView?
//        this.ivStaffIndicator = findViewById<View?>(R.id.iv_staff_indicator) as ImageView?
//        Log.d("OnboardingRole", "Views initialized")
//    }
//
//    private fun setupClickListeners() {
//        var button = this.btnContinue
//        var radioButton: RadioButton? = null
//        if (button == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("btnContinue")
//            button = null
//        }
//        button!!.setOnClickListener(object : View.OnClickListener {
//            // from class: com.example.efficilog.OnboardingRoleSelectionActivity$$ExternalSyntheticLambda0
//            // android.view.View.OnClickListener
//            override fun onClick(view: View?) {
//                `$r8$lambda$9WUiDFJ4vUahGRoyL7Ql_yf1sCs`(this@OnboardingRoleSelectionActivity, view)
//            }
//        })
//        var button2 = this.btnBack
//        if (button2 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("btnBack")
//            button2 = null
//        }
//        button2!!.setOnClickListener(object : View.OnClickListener {
//            // from class: com.example.efficilog.OnboardingRoleSelectionActivity$$ExternalSyntheticLambda1
//            // android.view.View.OnClickListener
//            override fun onClick(view: View?) {
//                `$r8$lambda$JEgxByNYVSwAyqRjsDi22eViiX0`(this@OnboardingRoleSelectionActivity, view)
//            }
//        })
//        var textView = this.tvSkip
//        if (textView == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("tvSkip")
//            textView = null
//        }
//        textView!!.setOnClickListener(object : View.OnClickListener {
//            // from class: com.example.efficilog.OnboardingRoleSelectionActivity$$ExternalSyntheticLambda2
//            // android.view.View.OnClickListener
//            override fun onClick(view: View?) {
//                `m6461$r8$lambda$0tllocvzOvhP0rlWmFQks6p8n4`(
//                    this@OnboardingRoleSelectionActivity,
//                    view
//                )
//            }
//        })
//        var radioButton2 = this.rbAdministrator
//        if (radioButton2 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("rbAdministrator")
//            radioButton2 = null
//        }
//        radioButton2!!.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
//            // from class: com.example.efficilog.OnboardingRoleSelectionActivity$$ExternalSyntheticLambda3
//            // android.widget.CompoundButton.OnCheckedChangeListener
//            override fun onCheckedChanged(compoundButton: CompoundButton?, z: Boolean) {
//                `m6463$r8$lambda$Q0tCEdRq4T7Kot4xhwIcQswtnw`(
//                    this@OnboardingRoleSelectionActivity,
//                    compoundButton,
//                    z
//                )
//            }
//        })
//        val radioButton3 = this.rbStaff
//        if (radioButton3 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("rbStaff")
//        } else {
//            radioButton = radioButton3
//        }
//        radioButton!!.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
//            // from class: com.example.efficilog.OnboardingRoleSelectionActivity$$ExternalSyntheticLambda4
//            // android.widget.CompoundButton.OnCheckedChangeListener
//            override fun onCheckedChanged(compoundButton: CompoundButton?, z: Boolean) {
//                `m6462$r8$lambda$A55OI5sas1WRjGvdkR2AcJrXE`(
//                    this@OnboardingRoleSelectionActivity,
//                    compoundButton,
//                    z
//                )
//            }
//        })
//    }
//
//    private fun setupRadioGroupListener() {
//        var radioGroup = this.radioGroup
//        if (radioGroup == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("radioGroup")
//            radioGroup = null
//        }
//        radioGroup!!.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
//            // from class: com.example.efficilog.OnboardingRoleSelectionActivity$$ExternalSyntheticLambda5
//            // android.widget.RadioGroup.OnCheckedChangeListener
//            override fun onCheckedChanged(radioGroup2: RadioGroup?, i: Int) {
//                `m6464$r8$lambda$UGu2bTQXIMYlAplGhcuD9lu9eA`(
//                    this@OnboardingRoleSelectionActivity,
//                    radioGroup2,
//                    i
//                )
//            }
//        })
//    }
//
//    private fun updateSelection() {
//        Log.d("OnboardingRole", "Updating selection for role: " + this.selectedRole)
//        var imageView = this.ivAdminIndicator
//        var imageView2: ImageView? = null
//        if (imageView == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("ivAdminIndicator")
//            imageView = null
//        }
//        imageView!!.setVisibility(8)
//        var imageView3 = this.ivStaffIndicator
//        if (imageView3 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("ivStaffIndicator")
//            imageView3 = null
//        }
//        imageView3!!.setVisibility(8)
//        val str: String? = this.selectedRole
//        if (Intrinsics.areEqual(str, OnboardingManager.ROLE_ADMINISTRATOR)) {
//            val imageView4 = this.ivAdminIndicator
//            if (imageView4 == null) {
//                Intrinsics.throwUninitializedPropertyAccessException("ivAdminIndicator")
//            } else {
//                imageView2 = imageView4
//            }
//            imageView2!!.setVisibility(0)
//            Log.d("OnboardingRole", "Admin indicator shown")
//        } else if (Intrinsics.areEqual(str, OnboardingManager.ROLE_STAFF)) {
//            val imageView5 = this.ivStaffIndicator
//            if (imageView5 == null) {
//                Intrinsics.throwUninitializedPropertyAccessException("ivStaffIndicator")
//            } else {
//                imageView2 = imageView5
//            }
//            imageView2!!.setVisibility(0)
//            Log.d("OnboardingRole", "Staff indicator shown")
//        }
//        updateContinueButton()
//    }
//
//    private fun updateContinueButton() {
//        val isRoleSelected = this.selectedRole.length > 0
//        var button = this.btnContinue
//        var button2: Button? = null
//        if (button == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("btnContinue")
//            button = null
//        }
//        button!!.setEnabled(isRoleSelected)
//        val button3 = this.btnContinue
//        if (button3 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("btnContinue")
//        } else {
//            button2 = button3
//        }
//        button2!!.setAlpha(if (isRoleSelected) 1.0f else 0.5f)
//        Log.d("OnboardingRole", "Continue button enabled: " + isRoleSelected)
//    }
//
//    private fun navigateToFeatureScreen() {
//        val intent: Intent?
//        Log.d("OnboardingRole", "Navigating to feature screen for role: " + this.selectedRole)
//        val str: String? = this.selectedRole
//        if (Intrinsics.areEqual(str, OnboardingManager.ROLE_ADMINISTRATOR)) {
//            intent = Intent(this, OnboardingAdminActivity::class.java)
//        } else if (!Intrinsics.areEqual(str, OnboardingManager.ROLE_STAFF)) {
//            Log.e("OnboardingRole", "Invalid role selected: " + this.selectedRole)
//            return
//        } else {
//            intent = Intent(this, OnboardingStaffActivity::class.java)
//        }
//        intent!!.putExtra("SELECTED_ROLE", this.selectedRole)
//        startActivity(intent)
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//    }
//
//    private fun skipOnboarding() {
//        Log.d("OnboardingRole", "Skipping onboarding")
//        val sharedPref = getSharedPreferences("onboarding_prefs", 0)
//        val `$this$skipOnboarding_u24lambda_u246` = sharedPref.edit()
//        `$this$skipOnboarding_u24lambda_u246`.putBoolean("onboarding_completed", true)
//        `$this$skipOnboarding_u24lambda_u246`.apply()
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//
//    // androidx.activity.ComponentActivity, android.app.Activity
//    override fun onBackPressed() {
//        Log.d("OnboardingRole", "Back pressed")
//        super.onBackPressed()
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
//    }
//
//    companion object {
//        const val `$stable`: Int = 8
//
//        /* renamed from: $r8$lambda$0tllocvzOvhP0-rlWmFQks6p8n4 */
//        fun `m6461$r8$lambda$0tllocvzOvhP0rlWmFQks6p8n4`(
//            onboardingRoleSelectionActivity: OnboardingRoleSelectionActivity,
//            view: View?
//        ) {
//            `setupClickListeners$lambda$2`(onboardingRoleSelectionActivity, view)
//        }
//
//        fun `$r8$lambda$9WUiDFJ4vUahGRoyL7Ql_yf1sCs`(
//            onboardingRoleSelectionActivity: OnboardingRoleSelectionActivity,
//            view: View?
//        ) {
//            `setupClickListeners$lambda$0`(onboardingRoleSelectionActivity, view)
//        }
//
//        /* renamed from: $r8$lambda$A-55OI5sas1WRjGvdkR2AcJ-rXE */
//        fun `m6462$r8$lambda$A55OI5sas1WRjGvdkR2AcJrXE`(
//            onboardingRoleSelectionActivity: OnboardingRoleSelectionActivity,
//            compoundButton: CompoundButton?,
//            z: Boolean
//        ) {
//            `setupClickListeners$lambda$4`(onboardingRoleSelectionActivity, compoundButton, z)
//        }
//
//        fun `$r8$lambda$JEgxByNYVSwAyqRjsDi22eViiX0`(
//            onboardingRoleSelectionActivity: OnboardingRoleSelectionActivity,
//            view: View?
//        ) {
//            `setupClickListeners$lambda$1`(onboardingRoleSelectionActivity, view)
//        }
//
//        /* renamed from: $r8$lambda$Q0tCEdRq4T7Kot4x-hwIcQswtnw */
//        fun `m6463$r8$lambda$Q0tCEdRq4T7Kot4xhwIcQswtnw`(
//            onboardingRoleSelectionActivity: OnboardingRoleSelectionActivity,
//            compoundButton: CompoundButton?,
//            z: Boolean
//        ) {
//            `setupClickListeners$lambda$3`(onboardingRoleSelectionActivity, compoundButton, z)
//        }
//
//        /* renamed from: $r8$lambda$UGu2bTQXIMYlAplGhcuD9lu9-eA */
//        fun `m6464$r8$lambda$UGu2bTQXIMYlAplGhcuD9lu9eA`(
//            onboardingRoleSelectionActivity: OnboardingRoleSelectionActivity,
//            radioGroup: RadioGroup?,
//            i: Int
//        ) {
//            `setupRadioGroupListener$lambda$5`(onboardingRoleSelectionActivity, radioGroup, i)
//        }
//
//        fun `setupClickListeners$lambda$0`(`this$0`: OnboardingRoleSelectionActivity, it: View?) {
//            Intrinsics.checkNotNullParameter(`this$0`, "this$0")
//            Log.d("OnboardingRole", "Continue clicked, selected role: " + `this$0`.selectedRole)
//            if (`this$0`.selectedRole.length > 0) {
//                `this$0`.navigateToFeatureScreen()
//            } else {
//                Toast.makeText(`this$0`, "Please select a role to continue", 0).show()
//            }
//        }
//
//        fun `setupClickListeners$lambda$1`(`this$0`: OnboardingRoleSelectionActivity, it: View?) {
//            Intrinsics.checkNotNullParameter(`this$0`, "this$0")
//            Log.d("OnboardingRole", "Back clicked")
//            `this$0`.onBackPressed()
//        }
//
//        fun `setupClickListeners$lambda$2`(`this$0`: OnboardingRoleSelectionActivity, it: View?) {
//            Intrinsics.checkNotNullParameter(`this$0`, "this$0")
//            Log.d("OnboardingRole", "Skip clicked")
//            `this$0`.skipOnboarding()
//        }
//
//        fun `setupClickListeners$lambda$3`(
//            `this$0`: OnboardingRoleSelectionActivity,
//            compoundButton: CompoundButton?,
//            isChecked: Boolean
//        ) {
//            Intrinsics.checkNotNullParameter(`this$0`, "this$0")
//            Log.d("OnboardingRole", "Administrator checked: " + isChecked)
//            if (isChecked) {
//                `this$0`.selectedRole = OnboardingManager.ROLE_ADMINISTRATOR
//                `this$0`.updateSelection()
//            }
//        }
//
//        fun `setupClickListeners$lambda$4`(
//            `this$0`: OnboardingRoleSelectionActivity,
//            compoundButton: CompoundButton?,
//            isChecked: Boolean
//        ) {
//            Intrinsics.checkNotNullParameter(`this$0`, "this$0")
//            Log.d("OnboardingRole", "Staff checked: " + isChecked)
//            if (isChecked) {
//                `this$0`.selectedRole = OnboardingManager.ROLE_STAFF
//                `this$0`.updateSelection()
//            }
//        }
//
//        fun `setupRadioGroupListener$lambda$5`(
//            `this$0`: OnboardingRoleSelectionActivity,
//            group: RadioGroup?,
//            checkedId: Int
//        ) {
//            Intrinsics.checkNotNullParameter(`this$0`, "this$0")
//            Log.d("OnboardingRole", "RadioGroup changed, checkedId: " + checkedId)
//            if (checkedId == R.id.rb_administrator) {
//                `this$0`.selectedRole = OnboardingManager.ROLE_ADMINISTRATOR
//                `this$0`.updateSelection()
//            } else if (checkedId == R.id.rb_staff) {
//                `this$0`.selectedRole = OnboardingManager.ROLE_STAFF
//                `this$0`.updateSelection()
//            } else {
//                `this$0`.selectedRole = ""
//                `this$0`.updateSelection()
//            }
//        }
//    }
//}