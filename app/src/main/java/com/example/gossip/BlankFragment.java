package com.example.gossip;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.gossip.messages.MessagesAdapter;
import com.example.gossip.messages.MessagesList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gossip.chat.Chat;
import com.example.gossip.messages.MessagesAdapter;
import com.example.gossip.messages.MessagesList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.siddydevelops.customlottiedialogbox.CustomLottieDialog;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {


    private static final int REQUEST_READ_CONTACTS_PERMISSION = 1;

    public Uri filepath;
    public Bitmap bitmap;
    private Bitmap temp;
    public FirebaseStorage storage = FirebaseStorage.getInstance();
    public com.google.android.material.bottomappbar.BottomAppBar bottomBar;
    private ImageView imageView;
    private ImageView imageView2;
    private RecyclerView messagesRecyclerView ;
    private RelativeLayout relativeLayout;
    public static String mobile;
    private String name;
    String lastMsg;
    private String lastMessage="";
    private int unseenMessages=0;
    com.example.gossip.EarthView global;
    private String chatKey="";
    CircleImageView my_story;
    public static String myName;
    private List<MessagesList> retrievedList;
//    public  List<String> ind;
    private boolean dataSet=false;
    private  MessagesAdapter messagesAdapter;
    private ImageView add;
    private int i=1;


    private Context context;
    private CircleImageView my_dp;
    public static String newUser_mobile;
//    private TreeSet<String> set;
    private List<MessagesList> messagesLists=new ArrayList<>();
    private DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public int bottomNavigationViewWidth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CustomLottieDialog customLottieDialog;
    TextView information;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public BlankFragment() {
        // Required empty public constructor
    }
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        fragmentContext = context;
//    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data!=null && data.getData()!=null && requestCode==1 && resultCode==RESULT_OK){
            filepath=data.getData();
            try{
                InputStream inputStream=getActivity().getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                String fileName = "profile_picture.jpg";
                FileOutputStream fos = getActivity().openFileOutput(fileName, MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();

                // Store the file path in SharedPreferences
                SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("profile_picture_path", getActivity().getFilesDir() + "/" + fileName);
                editor.apply();
                my_dp.setImageBitmap(bitmap);
                my_story.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
//                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_blank, container, false);

//        customLottieDialog = new CustomLottieDialog(getContext(), 	"LO04");
//        customLottieDialog.setLottieBackgroundColor("#000000");
//        customLottieDialog.setDialogLayoutDimensions(200,200);
//        customLottieDialog.setLoadingText("Loading...");
//        customLottieDialog.show();

        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");
        mobile= MemoryData.getData(requireContext());
//        databaseReference.child(mobile).child("Status").setValue(1);
//        Log.i("my mobile no",mobile);
//        imageView=view.findViewById(R.id.left_bottom_nav);
//        relativeLayout=view.findViewById(R.id.relativeLayout);
//        imageView2=view.findViewById(R.id.right_bottom_nav);
//        name=MemoryData.getName(requireContext());
        messagesRecyclerView=view.findViewById(R.id.messagesRecyclerView);
//        my_dp=view.findViewById(R.id.user_image);
//        add=view.findViewById(R.id.add);
        ImageView search=view.findViewById(R.id.search);
        messagesRecyclerView.setHasFixedSize(true);
        ImageView options=view.findViewById(R.id.Options);
        my_story=view.findViewById(R.id.my_story);
//        com.google.android.material.bottomappbar.BottomAppBar bottomBar = ((BottomNavigationPage) getActivity()).bottomBar;
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messagesAdapter=new MessagesAdapter(messagesLists,getContext());
        messagesRecyclerView.setAdapter(messagesAdapter);
        retrievedList = MemoryData.retrieveMessageList(getContext());
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAuthenticationId",MODE_PRIVATE);
        myName=sharedPreferences.getString("name","Unknown");

        information=view.findViewById(R.id.information);
        information.setVisibility(View.INVISIBLE);

        if (retrievedList != null) {
//            Toast.makeText(getContext(),"1",Toast.LENGTH_SHORT).show();
            messagesAdapter.updateData(retrievedList);
//            ind=MemoryData.retrieveIndex(getContext());
        }
        ImageView camera=view.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(getContext(),addContact.class);
//                startActivity(i);
                openCenteredDialog();
            }
        });
        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String filePath = prefs.getString("profile_picture_path", null);
        if (filePath != null) {
//            Toast.makeText(getContext(),"2",Toast.LENGTH_SHORT).show();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//            my_dp.setImageBitmap(bitmap);
            my_story.setImageBitmap(bitmap);
        }
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                customLottieDialog = new CustomLottieDialog(getContext(), 	"LO04");
//                customLottieDialog.setLottieBackgroundColor("#000000");
//                customLottieDialog.setDialogLayoutDimensions(200,200);
//                customLottieDialog.setLoadingText("Importing contacts...");
//                customLottieDialog.show();
                checkAndRequestContactsPermission();


            }
        });

        List<String> ListIndexes=new ArrayList<>();
//        Log.i("Posid", String.valueOf(ind));
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity=getActivity();

        if (activity!=null && isAdded()) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot1) {

//                Toast.makeText(getContext(),"3",Toast.LENGTH_SHORT).show();
                    if (retrievedList != null) messagesAdapter.updateData(retrievedList);
                    final Long[] LastNode = {Long.valueOf(0)};
                    dataSet = true;
//                Log.i("mno","2");
                    databaseReference.child(mobile).child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int getChatCounts = (int) snapshot.getChildrenCount();
                            if (getChatCounts > 0) {
                                messagesLists.clear();
                                unseenMessages = 0;
                                lastMessage = "";
                                chatKey = "";
                                Log.i("mno", "4");
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    final String getKey = dataSnapshot1.getKey();
                                    chatKey = getKey;
                                    final String getmobile = snapshot.child(chatKey).child("user_2").getValue(String.class);
                                    String getName = snapshot1.child(mobile).child("contacts").child(getmobile).child("Name").getValue(String.class);
                                    if (dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")) {
                                        final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                        try {
                                            if (!MemoryData.profilePictureExists(getmobile, getContext())) {
                                                firebaseStorage = FirebaseStorage.getInstance();
                                                storageReference = firebaseStorage.getReference();
                                                StorageReference imgRef = storageReference.child(getmobile);

                                                imgRef.getBytes(5024 * 5024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                    @Override
                                                    public void onSuccess(byte[] bytes) {
                                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                        temp = bitmap;
                                                        if (bitmap != null)
                                                            MemoryData.saveProfilePicture(bitmap, getmobile, getContext());


                                                    }
                                                }).addOnFailureListener(exception -> {
                                                });
                                            }
                                        } catch (Exception e) {
                                        }

                                        final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);
                                        if (getUserOne.equals(mobile) && getUserTwo.equals(getmobile) || getUserOne.equals(getmobile) && getUserTwo.equals(mobile)) {
                                            if (dataSnapshot1.child("messages").getChildrenCount() == 0) {
//                                            if(retrievedList==null ){
                                                MessagesList messagesList = new MessagesList(getName, getmobile, "Say Hello..", temp, 0, chatKey, LastNode[0]);
                                                messagesLists.add(messagesList);
                                            } else {
                                                String savedChatId = "";
                                                String LastMessageIfNotSaved = "";
                                                for (DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()) {
                                                    final Long getMessageKey = Long.valueOf(chatDataSnapshot.child("timestamp").getValue(String.class));
                                                    Long getLastSeenMessage = Long.valueOf(getMessageKey);
                                                    LastNode[0] = getLastSeenMessage;
                                                    try {
                                                        getLastSeenMessage = Long.valueOf(MemoryData.getLastMsgTs(getContext(), getKey));
                                                    } catch (Exception e) {
                                                        getLastSeenMessage = getMessageKey;
                                                        LastMessageIfNotSaved = String.valueOf(getMessageKey);
                                                        savedChatId = getKey;
                                                    }
                                                    ;
                                                    lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                                    if (getMessageKey > getLastSeenMessage) {
                                                        unseenMessages++;
                                                    } else unseenMessages = 0;
                                                }
                                                MessagesList messagesList = new MessagesList(getName, getmobile, lastMessage, temp, unseenMessages, chatKey, LastNode[0]);
                                                int x = 0;
                                                if (messagesLists.isEmpty())
                                                    messagesLists.add(messagesList);
                                                else {
                                                    while (x < messagesLists.size() && LastNode[0] < messagesLists.get(x).getLastNode())
                                                        x++;
                                                    messagesLists.add(x, messagesList);
                                                }

                                                if (!LastMessageIfNotSaved.isEmpty() && context != null) {
                                                    MemoryData.saveLastMsgTs(LastMessageIfNotSaved, savedChatId, getContext());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (messagesLists != null ) {
//                            Toast.makeText(getContext(),"4",Toast.LENGTH_SHORT).show();
                                messagesAdapter.updateData(messagesLists);
                                try{
                                    MemoryData.storeMessageList(requireContext(), messagesLists);
                                }catch (Exception e){

                                }

                                retrievedList = messagesLists;
                            } else information.setVisibility(View.VISIBLE);
//                            customLottieDialog.dismiss();

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
//                    }

//                }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
//                messagesLists.clear();
                }
            });
//        Toast.makeText(getContext(),"view created",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        onDestroy();
//        onResume();
    }
    private Context fragmentContext;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);

    }
    public void openCenteredDialog() {
        // Create a Dialog instance
        Dialog dialog = new Dialog(getContext());

        // Set the layout for the Dialog
        dialog.setContentView(R.layout.add_friend);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        // Optionally set other properties for the Dialog, e.g., size, title, etc.
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setTitle("Your Dialog Title");
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        EditText searchUserName=dialog.findViewById(R.id.searchUserName);
        ImageView search=dialog.findViewById(R.id.search);
        TextView sendRequest=dialog.findViewById(R.id.sendRequest);
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName=searchUserName.getText().toString();
                if(!userName.isEmpty()){
                    databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot1:snapshot.getChildren()){
                                if(snapshot1.child("Name").getValue().equals(userName)){
                                    databaseReference.child(snapshot1.getKey()).child("FrndReq").child(mobile).child("Name").setValue(myName);
                                    databaseReference.child(mobile).child("ProfilePic").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            databaseReference.child(snapshot1.getKey()).child("FrndReq").child(mobile).child("profilePic").setValue(snapshot.getValue(String.class));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    dialog.dismiss();
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else Toast.makeText(getContext(),"Enter a Valid user name",Toast.LENGTH_SHORT).show();

            }

        });


        searchUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // This method is called before the text is changed.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // This method is called when the text is changing.
                // Check if the EditText has text, and hide the search icon if it does.
                if (charSequence.length() > 0) {
//                    editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    search.setVisibility(View.GONE);
                } else {
                    // If the EditText is empty, show the search icon.
//                    editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0);
                    search.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This method is called after the text has changed.
            }
        });

        // Show the Dialog
        dialog.show();
    }

    DatabaseReference databaseReference1;
    @SuppressLint("Range")
    private void importContacts() {
        CustomLottieDialog customLottieDialog = new CustomLottieDialog(getContext(), "LO04");
        customLottieDialog.setLottieBackgroundColor("#000000");
        customLottieDialog.setDialogLayoutDimensions(200, 200);
        customLottieDialog.setLoadingText("Loading...");

        // Start the background task to import contacts
        new ImportContactsTask(getContext(), customLottieDialog).execute();
    }
    private void checkAndRequestContactsPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},
                    REQUEST_READ_CONTACTS_PERMISSION);
        } else {
            // Permission has already been granted, so proceed with importing contacts
            importContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions are granted, so proceed with importing contacts
                importContacts();
            } else {
                // Handle the case when the user denies the permission request
                Toast.makeText(getContext(), "Permissions are required to import contacts.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}