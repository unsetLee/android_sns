package com.example.myapplication

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.myapplication.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.item_detail.view.*

class DetailviewFragment: Fragment() {

    var firestore : FirebaseFirestore? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        firestore = FirebaseFirestore.getInstance()

        var view = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_detail, container, false)

        view.detailviewfragment_recyclerview.adapter = DetailRecyclerviewAdapter()
        view.detailviewfragment_recyclerview.layoutManager = LinearLayoutManager(activity)

        return view
    }

    inner class DetailRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        val contentDTOs : ArrayList<ContentDTO>
        val contentUidList : ArrayList<String>

        init{
            println("초기화 전")
            contentDTOs = ArrayList()
            contentUidList = ArrayList()
            println("초기화 후")
            var uid = FirebaseAuth.getInstance().currentUser?.uid


            //변경이 일어날 때 여기만 따로 들어가서
            firestore?.collection("images")?.orderBy("timestamp", Query.Direction.DESCENDING)?.addSnapshotListener{querySnapshot, firebaseFirestoreException ->
                contentDTOs.clear()
                print("clear")
                for(snapshot in querySnapshot!!.documents){
                    var item = snapshot.toObject(ContentDTO::class.java)

                    contentDTOs.add(item!!)
                    contentUidList.add(snapshot.id)
                }

                notifyDataSetChanged()
            }

        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) { // 결합
            val viewHolder = (holder as CustomViewHolder).itemView

            viewHolder.detailviewitem_profile_textview.text = contentDTOs!![position].userId

            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl).into(viewHolder.detailviewitem_imageview_content)

            viewHolder.detailviewitem_explain_textview.text = contentDTOs!![position].explain

            viewHolder.detailviewitem_favoritecounter_text.text = "좋아요 " + contentDTOs!![position].favoriteCount + "개"
            // Int여서 toString을 넣어야하는데  뒤에 "개"가 있으므로 tostring 필요없음

            //이미지는 glide로 가져오는 것임
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

            var view = LayoutInflater.from(parent?.context).inflate(R.layout.item_detail, parent, false)

            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View?): RecyclerView.ViewHolder(view)
    }
}