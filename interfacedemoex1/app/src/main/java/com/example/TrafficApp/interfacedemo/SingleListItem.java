package com.example.rasheduzzaman.interfacedemo;

/**
 * Created by Warlord on 10/27/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleListItem extends Activity{
    TextView ph,em,ha;
    LoginDataBaseAdapter loginDataBaseAdapter;
    ImageView pp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.single_list_item_view);
        ph=(TextView)findViewById(R.id.textView);
        em=(TextView)findViewById(R.id.textView2);
        ha=(TextView)findViewById(R.id.textView3);
        pp=(ImageView)findViewById(R.id.imageView);
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        TextView txtProduct = (TextView) findViewById(R.id.product_label);

        Intent i = getIntent();
        // getting attached intent data
        String username = i.getStringExtra("product");
        Cursor st=loginDataBaseAdapter.getPerson(username);
        st.moveToFirst();
        // displaying selected product name
        txtProduct.setText(username);
        em.setText(st.getString(3));
        ph.setText(st.getString(4));
        ha.setText(st.getString(5));
        if(st.getBlob(6)!=null) {
            byte[] bb = st.getBlob(6);

            //if(bb!=null)
           // Toast.makeText(SingleListItem.this, "Congrats -" + st.getColumnIndex("IMAGE"), Toast.LENGTH_LONG).show();

            pp.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0, bb.length));

        }
    }
}