package com.example.myapplication_22apriltask2
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private var userList: List<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount() = userList.size

    fun setUserList(users: List<User>) {
        userList = users
        notifyDataSetChanged()
    }

    private suspend fun deleteUser(user: User, position: Int, context: Context) {
        userList = userList.filterIndexed { index, _ -> index != position }
        notifyDataSetChanged()

        // delete user from database
        AppDatabase.getDatabase(context).userDao().deleteUser(user)
    }




    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView? = itemView.findViewById(R.id.name_text_view)
        private val emailTextView: TextView? = itemView.findViewById(R.id.email_text_view)
        private val goalsTextView: TextView? = itemView.findViewById(R.id.goals_text_view)
        private val dobTextView: TextView? = itemView.findViewById(R.id.dob_text_view)
        private val dateTimeTextView: TextView? = itemView.findViewById(R.id.date_time_text_view)
        private val ratingBar: RatingBar? = itemView.findViewById(R.id.rating_bar)

        fun bind(user: User) {
            nameTextView?.text = user.name
            emailTextView?.text = user.email
            goalsTextView?.text = user.goalsStatus
            dobTextView?.text = user.dob
            dateTimeTextView?.text = user.timeDate
            ratingBar?.rating = user.rating
            Log.d("UserAdapter", "Binding user----: $user")
        }
    }
}
