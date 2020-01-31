package one.xcorp.aviasales.screen.ticket.route

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import one.xcorp.aviasales.R
import one.xcorp.aviasales.screen.ticket.search.TicketSearchActivity

class TicketRouteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_route)

        startActivity(TicketSearchActivity.newIntent(this))
    }
}
