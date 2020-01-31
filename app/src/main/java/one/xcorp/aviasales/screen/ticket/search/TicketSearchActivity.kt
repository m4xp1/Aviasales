package one.xcorp.aviasales.screen.ticket.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import one.xcorp.aviasales.R

class TicketSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_search)
    }

    companion object {

        fun newIntent(
            context: Context
        ) = Intent(context, TicketSearchActivity::class.java)
    }
}
