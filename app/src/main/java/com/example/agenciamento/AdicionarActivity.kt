package com.example.agenciamento

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_adicionar.*

class AdicionarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar)

        submitDesafioAdd.setOnClickListener{
            //Cria a intent para retornar a MainActivity, retornando o nome e descrição do novo desafio
            val intentReturn = Intent()
            intentReturn.putExtra("desc", descAdd.text.toString())
            intentReturn.putExtra("nome", nomeAdd.text.toString())
            setResult(Activity.RESULT_OK, intentReturn)
            finish()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
