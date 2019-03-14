package com.example.agenciamento

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.FirebaseUser



class LoginActivity : AppCompatActivity() {

    var mAuth = FirebaseAuth.getInstance() //Conexão com o Firebase Auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Define o Listener no botão Login, chama signIn()
        loginButton.setOnClickListener{ view ->
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()
            signIn(view, email, password)
        }
    }



    fun signIn(view: View, email: String, password: String){
        showMessage(view,"Validando...")


        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
            if(task.isSuccessful){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("id", mAuth.currentUser?.email)
                startActivity(intent)

            }else{
                showMessage(view,"Error: ${task.exception?.message}")
            }
        })

    }

    fun showMessage(view:View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
    }

}
