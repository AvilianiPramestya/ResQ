package com.tugasakhir.resq.rescuer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tugasakhir.resq.MainActivity
import com.tugasakhir.resq.R
import kotlinx.android.synthetic.main.activity_rescuer_signup.*


class SignUpRescuerActivity : AppCompatActivity() {

    private lateinit var actionBar: ActionBar
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rescuer_signup)

        actionBar = this.supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.title = "BUAT AKUN"
        actionBar.elevation = 0F

        firebaseAuth = FirebaseAuth.getInstance()

        button_signup_tosignin.setOnClickListener {
            val intent = Intent(this, SignInRescuerActivity::class.java)
            startActivity(intent)
            finish()
        }

        button_signup_finish.setOnClickListener {
            registerUser()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addNewRescuerAccount(email: String, password: String, name: String,
                                     phone: String, employeeId: String,
                                     instansi: String, division: String) {
        progressbar_signup.visibility = View.VISIBLE
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { p0 ->
                if (p0.isSuccessful) {
                    val rescuer = Rescuer(name, email, phone, instansi, division, employeeId, false)
                    FirebaseDatabase.getInstance().getReference("Rescuers")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(rescuer).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                progressbar_signup.visibility = View.GONE
                                Toast.makeText(
                                    this@SignUpRescuerActivity,
                                    "Akun berhasil dibuat", Toast.LENGTH_LONG
                                ).show()

                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()

                            } else {
                                progressbar_signup.visibility = View.GONE
                                Toast.makeText(
                                    this@SignUpRescuerActivity,
                                    task.exception?.message, Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                } else {
                    progressbar_signup.visibility = View.GONE
                    Toast.makeText(
                        this@SignUpRescuerActivity,
                        p0.exception?.message, Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun registerUser() {
        val name = edittext_signup_name.text.toString().trim()
        val email = edittext_signup_email.text.toString().trim()
        val phone = edittext_signup_phone.text.toString().trim()
        val employeeID = edittext_signup_employeenum.text.toString().trim()
        val password = edittext_signup_password.text.toString().trim()
        val repeatPassword = edittext_signup_repeat_password.text.toString().trim()
        val instansi = spinner_signup_instansi.selectedItem.toString()
        val division = spinner_signup_division.selectedItem.toString()

        if (name.isEmpty()) {
            edittext_signup_name.error = "Wajib diisi"
            edittext_signup_name.requestFocus()
            return
        }

        if (email.isEmpty()) {
            edittext_signup_email.error = "Wajib diisi"
            edittext_signup_email.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edittext_signup_email.error = "Input Email tidak sesuai"
            edittext_signup_email.requestFocus()
            return
        }

        if (phone.isEmpty()) {
            edittext_signup_phone.error = "Wajib diisi"
            edittext_signup_phone.requestFocus()
            return
        }

        if (employeeID.isEmpty()) {
            edittext_signup_employeenum.error = "Wajib diisi"
            edittext_signup_employeenum.requestFocus()
            return
        }

        if(password.length < 6) {
            edittext_signup_password.error = "Kata sandi minimal terdiri dari 6 karakter"
            edittext_signup_password.requestFocus()
            return
        }

        if (password.isEmpty()) {
            edittext_signup_password.error = "Wajib diisi"
            edittext_signup_password.requestFocus()
            return
        }

        if (repeatPassword.isEmpty()) {
            edittext_signup_repeat_password.error = "Wajib diisi"
            edittext_signup_repeat_password.requestFocus()
            return
        }

        if (repeatPassword != password) {
            edittext_signup_repeat_password.error = "Kata sandi tidak sesuai"
            edittext_signup_repeat_password.requestFocus()
            return
        }

        Log.wtf("SIGN  UP: ", email)
        Log.wtf("PASSWORD: ", password)
        addNewRescuerAccount(email, password, name, phone, employeeID, instansi, division)


    }
}