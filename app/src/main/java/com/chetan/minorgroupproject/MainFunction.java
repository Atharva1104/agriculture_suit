package com.chetan.minorgroupproject;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.chetan.minorgroupproject.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;

public class MainFunction extends AppCompatActivity {

    //Creating variables
    TextView result, confidence,information;
    ImageView imageView;
    Button picture,select_picture,changeLang;
    int imageSize = 224;
    //Crop Information
    String[] cropInformation = {"Sowing Period : November\n" +
            "Yielding Period:  April\n" +
            "Scientific Name: Triticum\n" +
            "Wheat is the second most important staple food after rice consumed by 65% of the population in India.  Wheat is mostly consumed in the form of ‘chapati’ in our country for which bread wheat is cultivated in nearly 95 per cent of the cropped area.\n",
            "Sowing- April, May\n" +
                    "Yielding- December, January\n" +
                    "Growth time- 6-8 months\n" +
                    "Scientific name- Gossypium\n" +
                    "Cotton is a soft, fluffy staple fiber that grows in a boll, or protective case, around the seeds of the cotton plants of the genus Gossypium in the mallow family Malvaceae. The fiber is almost pure cellulose, and can contain minor percentages of waxes, fats, pectins, and water. Under natural conditions, the cotton bolls will increase the dispersal of the seeds.\n",
            "Sowing- July to mid August\n" +
                    "Yeilding- September to October\n" +
                    "Scientific name-Pennisetum glaucum\n" +
                    "India is the largest producer of pearl millet. India began growing pearl millet between 1500 and 1100 BCE.[6] It is currently unknown how it made its way to India.[6] Rajasthan is the highest-producing state in India. The first hybrid of pearl millet developed in India. Sajje is the local name of the Pearl Millet in Karnataka and is mostly grown in the semi-arid districts of North Karnataka. Sajje is milled and used for making flatbread called 'Sajje Rotti' and is eaten with Yennegai (stuffed brinjal) and yogurt.\n",
            "Sowing- Kharif jawar is sown in June to July whereas Rabi jawar is sown in October/November\n" +
                    "Yeilding- Kharif jawar is yielded in October/November  whereas Rabi jawar is yielded in February/March\n" +
                    "Growth period- 100-120 days(4 months) \n" +
                    "Scientific name- Sorghum bicolor\n" +
                    "Jowar is mainly concentrated in the peninsular and central India. Maharashtra, Karnataka, Andhra Pradesh, Madhya Pradesh, Gujarat, Rajasthan, Uttar Pradesh (the Bundelkhand region) and Tamil Nadu are the major jowar – growing states. Other states grow sorghum in small areas primarily for fodder. The sorghum grain is used primarily as human food in various forms, such as roti or bhakri (unleavened bread), or is cooked like rice. Sorghums are also malted, popped and several local preparations are made. Green and dried fodder is the most important roughage for feeding cattle throughout the country\n",

            "Sowing- Kharif maize is sown in June to July whereas Rabi maize is sown in October/November\n" +
                    "Yielding-Kharif maize is yielded in September/October  whereas Rabi jawar is yielded in January/February\n" +
                    "Growth period- 95 days \n" +
                    "Scientific name- Zea Mays\n" +
                    "In India, maize is the third most important food crops after rice and wheat. According to advance estimate its production is likely to be 22.23 M Tonnes (2012-13) mainly during Kharif season which covers 80% area. Maize in India, contributes nearly 9 % in the national food basket. The maize is cultivated throughout the year in all states of the country for various purposes including grain, fodder, green cobs, sweet corn, baby corn, pop corn in peri-urban areas.\n",
            "Sowing- June/July\n" +
                    "Yielding- September/October\n" +
                    "Growth period- 100-120 days(3-4 months) \n" +
                    "Scientific name- Vigna Radiata\n" +
                    "Mung bean (Vigna radiata) is a plant species of Fabaceae which is also known as green gram.[7] It is sometimes confused with black gram . Mung bean plants have a long history of being consumed by humans. The main consumed parts are the seeds and sprouts. The mature seeds provide an invaluable source of digestible protein for humans in places where meat is lacking or where people are mostly vegetarian.[39] Mung bean has a large market in Asia\n",
            "Sowing Period : June, July\n" +
                    "Yielding Period:  November, December\n" +
                    "Scientific Name: Oryza Sativa\n" +
                    "India is the world's second-largest producer of rice, and the largest exporter of rice in the world. Rice is one of the chief grains of India. Moreover, this country has the largest area under rice cultivation. Rice is mainly grown in rain-fed areas that receive heavy annual rainfall. That is why it is fundamentally a kharif crop in India.\n",
            "Sowing Period : Suru- January-February, Adsali- July-August and Pre-seasonal- October-November\n" +
                    "Yielding Period:  12-18 months to mature\n" +
                    "Scientific Name: Saccharum officinarum\n" +
                    "The plants are 2–6 m (6–20 ft) tall with stout, jointed, fibrous stalks that are rich in sucrose. Sugarcanes belong to the grass family, Poaceae, an economically important flowering plant family that includes maize, wheat, rice, and sorghum, and many forage crops. It is native to the warm temperate and tropical regions of India.  sugarcane is the world's largest crop by production quantity.\n",
            "Sowing Period : June, July for autumn winter crops, Summer crops in November\n" +
                    "Yielding Period:  60 days-100 days\n" +
                    "Scientific Name: Solanum lycopersicum\n" +
                    "The tomato is the edible berry of the plant Solanum lycopersicum commonly known as a tomato plant. Tomatoes are a significant source of umami flavor.[6] The tomato is consumed in diverse ways, raw or cooked, in many dishes, sauces, salads, and drinks. While tomatoes are fruits—botanically classified as berries—they are commonly used culinarily as a vegetable ingredient or side dish.\n",
            "Sowing Period : April, July \n" +
                    "Yielding Period:  7-9 Months\n" +
                    "Scientific Name: Curcuma longa\n" +
                    "Turmeric powder has a warm, bitter, black pepper-like flavor and earthy, mustard-like aroma.[\n" +
                    "Turmeric has been used in Asia for centuries and is a major part of Ayurveda, Siddha medicine, traditional Chinese medicine\n"

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main_function);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));


        result = findViewById(R.id.result);
//        confidence = findViewById(R.id.confidence);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);
//        select_picture = findViewById(R.id.button2);
        information = findViewById(R.id.information);
        changeLang = findViewById(R.id.changeLanguage);


        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });
//        select_picture.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View view) {
//
//                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//
//                    Intent intent = new Intent(Intent.ACTION_PICK);
//                    intent.setType("image/*");
//                    startActivityForResult(intent, 1);
//                }else{
//                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
//                }
//            }
//        });
        picture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    //Request camera permission if we don't have it.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
    }


    //Setting up show language dialog box
    private void showChangeLanguageDialog() {

        final String[] listItems = {"English","हिंदी","मराठी"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainFunction.this);
        builder.setTitle("Choose Language..");
        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    setLocale("en");
                    recreate();
                }
                else if(i == 1){
                    setLocale("hi");
                    recreate();
                }
                else if(i == 2){
                    setLocale("mr");
                    recreate();
                }

                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();

    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);
    }

    //Classification of Images
    public void classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);


            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intvalues = new int[imageSize * imageSize];
            image.getPixels(intvalues,0,image.getWidth(),0,0,image.getWidth(),image.getHeight());
            int pixel = 0;
            for(int i = 0;i<imageSize;i++)
            {
                for(int j=0;j<imageSize;j++)
                {
                    int val = intvalues[pixel++]; //RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();


            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i=0;i<confidences.length;i++)
            {
                if(confidences[i]> maxConfidence)
                {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Wheat/गहू",
                    "Cotton/कापूस",
                    "Bajra/बाजरी",
                    "Jowar/ज्वारी",
                    "Maize/मका",
                    "Moong/मूग",
                    "Rice/तांदूळ",
                    "Sugarcane/ऊस",
                    "Tomato/टोमॅटो",
                    "Turmeric/हळद"};



            result.setText(classes[maxPos]);
            if(maxPos == 0){
                information.setText(cropInformation[maxPos]);
                information.setMovementMethod(new ScrollingMovementMethod());
            }
            else if(maxPos == 1){
                information.setText(cropInformation[maxPos]);
                information.setMovementMethod(new ScrollingMovementMethod());
            }
            else if(maxPos == 2){
                information.setText(cropInformation[maxPos]);
                information.setMovementMethod(new ScrollingMovementMethod());
            }
            else if(maxPos == 3){
                information.setText(cropInformation[maxPos]);
                information.setMovementMethod(new ScrollingMovementMethod());
            }
            else if(maxPos == 4){
                information.setText(cropInformation[maxPos]);
                information.setMovementMethod(new ScrollingMovementMethod());
            }
            else if(maxPos == 5){
                information.setText(cropInformation[maxPos]);
                information.setMovementMethod(new ScrollingMovementMethod());
            }
            else if(maxPos == 6){
                information.setText(cropInformation[maxPos]);
                information.setMovementMethod(new ScrollingMovementMethod());
            }
            else if(maxPos == 7){
                information.setText(cropInformation[maxPos]);
                information.setMovementMethod(new ScrollingMovementMethod());
            }
            else if(maxPos == 8){
                information.setText(cropInformation[maxPos]);
                information.setMovementMethod(new ScrollingMovementMethod());
            }
            else if(maxPos == 9){
                information.setText(cropInformation[maxPos]);
                information.setMovementMethod(new ScrollingMovementMethod());
            }










            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {

            Bitmap image =  (Bitmap) data.getExtras().get("data");
            int height = image.getHeight() , width = image.getWidth();
            int dimension = Math.min(width, height);
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            imageView.setImageBitmap(image);

            image = Bitmap.createScaledBitmap(image,imageSize,imageSize,false);
            classifyImage(image);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}