package com.example.rasheduzzaman.interfacedemo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by Warlord on 10/22/2015.
 */
public class homepageactivity extends Activity {
    private static int LOAD_IMAGE_RESULTS = 1;
    public byte[] imgarray;
    ImageView pp;
    LoginDataBaseAdapter loginDataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);


        Button bt,bt1;
        final EditText un,pw,em,ph,ha;
        pp=(ImageView)findViewById(R.id.imageView);
        un=(EditText)findViewById(R.id.editText4);
        pw=(EditText)findViewById(R.id.editText5);
        em=(EditText)findViewById(R.id.editText6);
        ph=(EditText)findViewById(R.id.editText7);
        ha=(EditText)findViewById(R.id.editText8);
        bt=(Button)findViewById(R.id.button2);
        bt1=(Button)findViewById(R.id.button3);

        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
        Intent intent = getIntent();



        String uname= intent.getStringExtra("uname");



        Cursor st=loginDataBaseAdapter.getPerson(uname);
        st.moveToFirst();

       //pp.setImageBitmap(st.getBlob(6));
if(st.getBlob(6)!=null) {
    byte[] bb = st.getBlob(6);

    //if(bb!=null)
     //Toast.makeText(homepageactivity.this, "Congrats -"+st.getColumnIndex("IMAGE"), Toast.LENGTH_LONG).show();

    pp.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0, bb.length));

}
        final String i=st.getString(0);
        un.setText(st.getString(1));
        pw.setText(st.getString(2));
        em.setText(st.getString(3));
        ph.setText(st.getString(4));
        ha.setText(st.getString(5));

        bt.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String uned = un.getText().toString();
                String unpw = pw.getText().toString();
                String unem = em.getText().toString();
                String unph = ph.getText().toString();
                String unha = ha.getText().toString();

                loginDataBaseAdapter.updateEntry(i, uned, unpw, unem, unph, unha, imgarray);
                Toast.makeText(homepageactivity.this, "updated", Toast.LENGTH_LONG).show();


            }
        });




        bt1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
                startActivityForResult(i, LOAD_IMAGE_RESULTS);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            // Now we need to set the GUI ImageView data with data read from the picked file.
            Bitmap bm=BitmapFactory.decodeFile(imagePath);
            pp.setImageBitmap(bm);

            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte imageInByte[] = stream.toByteArray();
            imgarray =imageInByte;

        }
    }
}