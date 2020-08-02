package app.syam.twitter.common.item

import app.syam.twitter.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

class DividerItem : Item(){

    override fun bind(viewHolder: ViewHolder, position: Int) { }

    override fun getLayout(): Int = R.layout.item_divider

}