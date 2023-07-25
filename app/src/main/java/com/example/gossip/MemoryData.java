package com.example.gossip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.example.gossip.messages.MessagesList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class MemoryData {
    public static void saveData (String data, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput ("datata.txt", Context.MODE_PRIVATE);
            fileOutputStream.write (data.getBytes());
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            }
        }
    public static void saveLastMsgTs (String data, String chatId,Context context) {
        try {
            if(context!=null){
                FileOutputStream fileOutputStream = context.openFileOutput (chatId+".txt", Context.MODE_PRIVATE);
                fileOutputStream.write (data.getBytes());
                fileOutputStream.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void saveName (String data, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput ("namee.txt", Context.MODE_PRIVATE);
            fileOutputStream.write (data.getBytes());
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void createProfilePicturesFolder(Context context) {
        try{
            File folder = new File(context.getFilesDir() , "ProfilePicturesFolder");
            if (!folder.exists()) {
                folder.mkdir();
            }
        }catch (Exception e){}

    }

    public static void saveProfilePicture(Bitmap bitmap, String Number, Context context) {
        createProfilePicturesFolder(context);

        try {
            String fileName = Number + ".jpeg";
            File file = new File(context.getFilesDir() + "/ProfilePicturesFolder", fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
//            Toast.makeText(context.getApplicationContext(),bitmap.toString(),Toast.LENGTH_SHORT).show();
            fileOutputStream.close();

            // Print the saved file path for reference
//            System.out.println("Profile picture saved: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getProfilePicture(String Number, Context context) {
        if(profilePictureExists(Number,context)){
            try {
                String fileName = Number + ".jpeg";
                File file = new File(context.getFilesDir() + "/ProfilePicturesFolder", fileName);
                FileInputStream fileInputStream = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                fileInputStream.close();
                return bitmap;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static boolean profilePictureExists(String Number, Context context) {
        String fileName = Number + ".jpeg";
        File file = new File(context.getFilesDir() + "/ProfilePicturesFolder", fileName);
        return file.exists();
    }
public static String getData (Context context) {
    String data = "";
    try {
        FileInputStream fis = context.openFileInput("datata.txt");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        data = sb.toString();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return data;
}


    public static String getName (Context context) {
        String data = "";
        try {
            FileInputStream fis = context.openFileInput("namee.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    public static String getLastMsgTs (Context context,String chatId) {
        String data = "";
        try {
            FileInputStream fis = context.openFileInput(chatId+".txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
//            Log.i("[];'",sb.toString());
            System.out.println(sb);
        } catch (IOException e) {
//            Log.i("MDerror",e.toString());
            e.printStackTrace();
        }
//        Log.i("storedData",data);
        return data;
    }

    public static void downloadImageAndSave(String imageRef, String number, Context context) {
//        Toast.makeText(context.getApplicationContext(), imageRef,Toast.LENGTH_SHORT).show();
        // Get a reference to the Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a StorageReference pointing to the image file
        StorageReference storageRef = storage.getReference().child(imageRef);

//        Toast.makeText(context.getApplicationContext(), storageRef.toString(),Toast.LENGTH_SHORT).show();


        storageRef.getBytes(5024*5024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap=BitmapFactory.decodeByteArray(bytes,0, bytes.length);
            }
        });
        Log.i("GHFJ",storageRef.toString());
        try {
            // Create a temporary file to store the downloaded image
            File localFile = File.createTempFile("temp_image", ".jpeg");

            // Download the image file from Firebase Storage to the temporary file
            storageRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                // Image downloaded successfully

                Log.i("GHFJ","Image downloaded successfully");

                // Decode the temporary file into a Bitmap object
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                // Save the bitmap as a profile picture using saveProfilePicture() function
                saveProfilePicture(bitmap, number, context);

                // Delete the temporary file
                localFile.delete();
                Toast.makeText(context.getApplicationContext(), "Profile picture saved successfully",Toast.LENGTH_SHORT).show();
//                System.out.println("Profile picture saved successfully");
            }).addOnFailureListener(exception -> {
                // Handle any errors that occurred during the download
//                System.out.println("Failed to download image: " + exception.getMessage());
                Toast.makeText(context.getApplicationContext(), exception.getMessage(),Toast.LENGTH_SHORT).show();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void storeMessageList(Context context, List<MessagesList> messageList) {
        File file = new File(context.getFilesDir(), "messageList.json");

        try {
            FileOutputStream fos = new FileOutputStream(file);
            Gson gson = new Gson();
            String json = gson.toJson(messageList);
            fos.write(json.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static List<MessagesList> retrieveMessageList(Context context) {
        File file = new File(context.getFilesDir(), "messageList.json");

        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();

            String json = stringBuilder.toString();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<MessagesList>>() {}.getType();
            return gson.fromJson(json, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void storeIndex(Context context, List<String> myList) {
        File file = new File(context.getFilesDir(), "myData.txt");

        try {
            FileOutputStream fos = new FileOutputStream(file);
            for (String item : myList) {
                fos.write(item.getBytes());
                fos.write("\n".getBytes()); // Add a newline after each item
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//
//
    public static List<String> retrieveIndex(Context context) {
        List<String> myList = new ArrayList<>();
        File file = new File(context.getFilesDir(), "myData.txt");

        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                myList.add(line);
            }

            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myList;
    }



    public static void deleteProfilePicturesFolder(Context context) {
        File folder = new File(context.getFilesDir(), "ProfilePicturesFolder");
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            folder.delete();
        }
    }
}

