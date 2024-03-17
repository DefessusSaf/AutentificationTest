package com.example.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSignUp = findViewById<Button>(R.id.btn_sign_up)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val Name = findViewById<EditText>(R.id.etName)
        val Email = findViewById<EditText>(R.id.etEmail)
        val Password = findViewById<EditText>(R.id.etPassword)
        val ConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)

        auth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            val userName = Name.text.toString()
            val email = Email.text.toString()
            val password = Password.text.toString()
            val confirmPassword = ConfirmPassword.text.toString()

            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(applicationContext, "username is required", Toast.LENGTH_LONG).show()
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "email is required", Toast.LENGTH_LONG).show()
            }
            if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "password is required", Toast.LENGTH_LONG).show()
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(applicationContext, "confirm password is required", Toast.LENGTH_LONG).show()
            }
            if (password != confirmPassword) {
                Toast.makeText(applicationContext, "password not same", Toast.LENGTH_LONG).show()
            }

            registerUser(userName, email, password)
        }
    }

    private fun registerUser(userName:String, email:String, password:String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user: FirebaseUser? = auth.currentUser
                val userId: String = user!!.uid

                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

                val hashMap: HashMap<String, String> = HashMap()
                hashMap.put("userId", userId)
                hashMap.put("userName", userName)
                hashMap.put("profileImage", "")

                databaseReference.setValue(hashMap).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    }
                }

            }
        }
    }

}