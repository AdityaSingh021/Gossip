package com.example.gossip;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.gossip.Status.StatusAdapter;
import com.example.gossip.Status.StatusGallery;
import com.example.gossip.Status.StatusModel;
import com.example.gossip.Status.StatusState;
import com.example.gossip.messages.MessagesAdapter;
import com.example.gossip.messages.MessagesList;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.siddydevelops.customlottiedialogbox.CustomLottieDialog;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {

    ProgressBar progressBar;
    ValueEventListener valListener;
    ShimmerFrameLayout shimmerFrameLayout;

    ValueEventListener valueEventListener;
    ImageView myStatusState;
    View view1;
    private RecyclerView recyclerView;
    private StatusAdapter adapter;
    public static List<StatusModel> statusList;
    private static final int REQUEST_READ_CONTACTS_PERMISSION = 1;

    public Uri filepath;
    public Bitmap bitmap;
    private ImageView addStatus;;
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
    SwipeRefreshLayout swipeRefreshLayout;
    com.example.gossip.EarthView global;
    private String chatKey="";
    CircleImageView my_story;
    public static String myName;
    public static String Mystatus;
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
    private DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");

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

//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(data!=null && data.getData()!=null && requestCode==1 && resultCode==RESULT_OK){
//            filepath=data.getData();
//            try{
//                InputStream inputStream=getActivity().getContentResolver().openInputStream(filepath);
//                bitmap= BitmapFactory.decodeStream(inputStream);
//                String fileName = "profile_picture.jpg";
//                FileOutputStream fos = getActivity().openFileOutput(fileName, MODE_PRIVATE);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                fos.close();
//
//                // Store the file path in SharedPreferences
//                SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putString("profile_picture_path", getActivity().getFilesDir() + "/" + fileName);
//                editor.apply();
//                my_dp.setImageBitmap(bitmap);
//                my_story.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
////                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_blank, container, false);
        setRetainInstance(true);
//        customLottieDialog = new CustomLottieDialog(getContext(), 	"LO04");
//        customLottieDialog.setLottieBackgroundColor("#000000");
//        customLottieDialog.setDialogLayoutDimensions(200,200);
//        customLottieDialog.setLoadingText("Loading...");
//        customLottieDialog.show();

        shimmerFrameLayout=view.findViewById(R.id.shimmer);
        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");
        mobile= MemoryData.getData(requireContext());
        Container.Mobile=mobile;
        recyclerView=view.findViewById(R.id.recyclerView);
        messagesRecyclerView=view.findViewById(R.id.messagesRecyclerView);
        ImageView search=view.findViewById(R.id.search);
        messagesRecyclerView.setHasFixedSize(true);
        recyclerView.setHasFixedSize(true);
        ImageView options=view.findViewById(R.id.Options);
        my_story=view.findViewById(R.id.my_story);
        addStatus=view.findViewById(R.id.addStatus);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messagesAdapter=new MessagesAdapter(messagesLists,getContext());
        messagesRecyclerView.setAdapter(messagesAdapter);
        myStatusState=view.findViewById(R.id.MyStatusState);
        shimmerFrameLayout.startShimmer();


        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAuthenticationId",MODE_PRIVATE);
        myName=sharedPreferences.getString("name","Unknown");
//        progressBar.setVisibility(View.VISIBLE);

        androidx.appcompat.widget.SearchView searchView=view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                messagesAdapter.filter(newText,messagesLists);
                return true;
            }
        });
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // This method is called when the user presses the search button
//                // (optional: you can perform any additional actions here)
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // This method is called whenever the user changes the text in the SearchView
//
//            }
//        });
        information=view.findViewById(R.id.information);
        information.setVisibility(View.INVISIBLE);


        swipeRefreshLayout = view.findViewById(R.id.myFragment);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                databaseReference.addValueEventListener(valListener);
                getStatus(my_story);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        my_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Mystatus!=null){
                    Intent i=new Intent(context,StatusGallery.class);
                    i.putExtra("Mobile",mobile);
                    i.putExtra("Name",myName);
                    i.putExtra("Index", -1);
                    context.startActivity(i);
                    Animatoo.INSTANCE.animateZoom(context);
                }
            }
        });
        if(statusList!=null){
            if(Mystatus!=null){
                Glide.with(getContext())
                        .load(Mystatus)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                RoundedBitmapDrawable circularDrawable = RoundedBitmapDrawableFactory.create(getResources(), ((BitmapDrawable) resource).getBitmap());
                                circularDrawable.setCircular(true);
                                my_story.setImageDrawable(circularDrawable);

                                return true;
                            }
                        })
                        .into(my_story);

            }
            adapter = new StatusAdapter(statusList,getContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
        }


        if(!messagesLists.isEmpty()){
            messagesAdapter.updateData(messagesLists);
        }else{
            retrievedList=MainActivity.retrievedList;
            if (retrievedList != null) {
                messagesAdapter.updateData(retrievedList);
            }
        }
        addStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU) {

                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_IMAGES)
                            == PackageManager.PERMISSION_GRANTED) {
                        Intent i = new Intent(Intent.ACTION_PICK);
                        i.setType("image/*");
                        startActivityForResult(Intent.createChooser(i, "Please select Image"), 1);
//                    openGallery();
                    } else {
//                        Toast.makeText(getContext(),"13asdad",Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                                123);
                    }
                }else{

                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Intent i = new Intent(Intent.ACTION_PICK);
                        i.setType("image/*");
                        startActivityForResult(Intent.createChooser(i, "Please select Image"), 1);
//                    openGallery();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                123);
                    }

                }


            }
        });

        ImageView camera=view.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(getContext(),addContact.class);
//                startActivity(i);
                openCenteredDialog();
            }
        });

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportContactsDialog();
//                customLottieDialog = new CustomLottieDialog(getContext(), 	"LO04");
//                customLottieDialog.setLottieBackgroundColor("#000000");
//                customLottieDialog.setDialogLayoutDimensions(200,200);
//                customLottieDialog.setLoadingText("Importing contacts...");
//                customLottieDialog.show();
//                OpenDialogBox openDialogBox=new OpenDialogBox();
//                openDialogBox.ImportContactsDialog(getContext(),"Import Contacts","#FFFFFF","Importing contacts");



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

        view1=view;
        if (activity!=null && isAdded()) {
            valListener=databaseReference.addValueEventListener(new ValueEventListener() {
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
//                                Log.i("mno", "4");
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
//                                    Log.i("mno", "5");
                                    final String getKey = dataSnapshot1.getKey();
                                    chatKey = getKey;
                                    final String getmobile = snapshot.child(chatKey).child("user_2").getValue(String.class);
                                    String getName = snapshot1.child(mobile).child("contacts").child(getmobile).child("Name").getValue(String.class);
                                    if (dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")) {
                                        final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                        final String profilePic= snapshot1.child(getmobile).child("profilePic").getValue(String.class);

//                                        try {
////                                            if (!MemoryData.profilePictureExists(getmobile, getContext())) {
//                                                firebaseStorage = FirebaseStorage.getInstance();
//                                                storageReference = firebaseStorage.getReference();
//                                                StorageReference imgRef = storageReference.child("ProfilePictures").child(getmobile);
//
//                                                imgRef.getBytes(5024 * 5024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                                                    @Override
//                                                    public void onSuccess(byte[] bytes) {
//                                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                                                        temp = bitmap;
//                                                        if (bitmap != null)
//                                                            try{
//                                                                MemoryData.saveProfilePicture(bitmap, getmobile, getContext());
//                                                            }catch (Exception e){}
//
//
//
//                                                    }
//                                                }).addOnFailureListener(exception -> {
//                                                });
////                                            }
//                                        } catch (Exception e) {
//                                        }

                                        final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);
                                        if (getUserOne.equals(mobile) && getUserTwo.equals(getmobile) || getUserOne.equals(getmobile) && getUserTwo.equals(mobile)) {
                                            if (dataSnapshot1.child("messages").getChildrenCount() == 0) {
//                                            if(retrievedList==null ){
                                                MessagesList messagesList = new MessagesList(getName, getmobile, "Say Hello..", profilePic, 0, chatKey, LastNode[0]);
                                                messagesLists.add(messagesList);
                                            } else {
                                                String savedChatId = "";
                                                String LastMessageIfNotSaved = "";
                                                for (DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()) {
                                                    final Long getMessageKey = Long.valueOf(chatDataSnapshot.child("timestamp").getValue(String.class));
                                                    Long getLastSeenMessage = Long.valueOf(getMessageKey)-1;
                                                    LastNode[0] = getLastSeenMessage;
                                                    try {
                                                        getLastSeenMessage = Long.valueOf(MemoryData.getLastMsgTs(getContext(), getKey));
                                                    } catch (Exception e) {
                                                        getLastSeenMessage = getMessageKey-1;
                                                        LastMessageIfNotSaved = String.valueOf(getMessageKey);
                                                        savedChatId = getKey;
                                                    }
                                                    ;
//                                                    try{
                                                        lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                                        if(lastMessage==null || lastMessage.isEmpty()) {
                                                            try{
                                                                if(chatDataSnapshot.child("mobile").getValue(String.class).equals(mobile)) lastMessage = "\uD83D\uDCF7 You: shared a photo";
                                                                else {
                                                                    String oppoMobile=chatDataSnapshot.child("mobile").getValue(String.class);
                                                                    lastMessage = "\uD83D\uDCF7 " + snapshot1.child(mobile).child("contacts").child(oppoMobile).child("Name").getValue(String.class) + ": shared a photo";
                                                                }
                                                            }catch(Exception e){
                                                                lastMessage = "image is shared";
                                                            }

//
                                                        }
//                                                    }catch (Exception e){
//                                                        lastMessage = "image";
//                                                    }

                                                    if (getMessageKey > getLastSeenMessage) {
                                                        unseenMessages++;
                                                    } else unseenMessages = 0;
                                                }
                                                MessagesList messagesList = new MessagesList(getName, getmobile, lastMessage, profilePic, unseenMessages, chatKey, LastNode[0]);
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
//                                if(MainActivity.firstTime!=null) swipeRefreshLayout.setRefreshing(true);
                            } else information.setVisibility(View.VISIBLE);
//                            customLottieDialog.dismiss();
                            swipeRefreshLayout.setRefreshing(false);

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
//        onResume();
        super.onPause();

//        super.onPause();
//        onDestroy();
        onResume();
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
        dialog.getWindow().setGravity(Gravity.TOP);
        EditText searchUserName=dialog.findViewById(R.id.searchUserName);
        ImageView search=dialog.findViewById(R.id.search);
        TextView sendRequest=dialog.findViewById(R.id.sendRequest);
        TextView warning=dialog.findViewById(R.id.warning);
        warning.setVisibility(View.GONE);
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String userName=searchUserName.getText().toString();
//                if(!userName.isEmpty() && !userName.equals(myName)){
//                    databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            final int[] check = {0};
//                            for(DataSnapshot snapshot1:snapshot.getChildren()){
//                                if(snapshot1.child("Name").getValue().equals(userName)){
//                                    databaseReference.child(mobile).child("contacts").child(snapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            if(snapshot.exists()){
//                                                Toast.makeText(getContext(),"Already a friend.",Toast.LENGTH_SHORT).show();
//                                                dialog.dismiss();
//
//                                            }else{
//                                                databaseReference.child(snapshot1.getKey()).child("FrndReq").child(mobile).child("Name").setValue(myName);
//
//                                                databaseReference.child(mobile).child("ProfilePic").addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                        databaseReference.child(snapshot1.getKey()).child("FrndReq").child(mobile).child("profilePic").setValue(snapshot.getValue(String.class));
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                                    }
//                                                });
//
//
//
//                                                check[0] =1;
//                                                Toast.makeText(getContext(),"Request sent",Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    });
//                                    dialog.dismiss();
//                                    break;
//                                }
//                            }
//                            if(check[0]==0){
//                                warning.setVisibility(View.VISIBLE);
//                                warning.setText("Warning : User does not exist.");
//                                searchUserName.setText("");
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }else Toast.makeText(getContext(),"Enter a Valid user name",Toast.LENGTH_SHORT).show();

                MainActivity.sendRequest(searchUserName.getText().toString(),getContext(),warning,dialog);
                searchUserName.setText("");
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
        CustomLottieDialog customLottieDialog = new CustomLottieDialog(getContext(), R.raw.animation_contacts);
        customLottieDialog.setLottieBackgroundColor("#000000");
        customLottieDialog.setDialogLayoutDimensions(200, 200);
        customLottieDialog.setLoadingText("    ○ ○ ○ ○ ○   ");
        customLottieDialog.setLoadingTextColor("#FFFFFF");

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


    public void getStatus(View view){
         statusList = new ArrayList<>();
//         List<StatusState> statusState=MainActivity.statusState;
        long currTime=System.currentTimeMillis();

//        recyclerView = view.findViewById(R.id.recyclerView);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean state=true;
                statusList.clear();
//                Log.i("checkingxxx",snapshot.child(mobile).child("Status").child("MyStatus").child("1").getValue(String.class)+"aa");

                for(DataSnapshot snapshot1:snapshot.child(mobile).child("Status").child("MyStatus").getChildren()){


//                    Log.i("testingThis",Mystatus);
                    long time=currTime;
                    try {
                        time=snapshot1.child("statusTime").getValue(Long.class);
                    }catch (Exception e){

                    };
                    long timeDifferenceMillis=currTime-time;
//                    Mystatus=snapshot1.child("statusImage").getValue(String.class);
//                    Log.i("mystatus",Mystatus);
                    if(Mystatus==null) Mystatus=snapshot1.child("statusImage").getValue(String.class);
                    if(timeDifferenceMillis<= TimeUnit.DAYS.toMillis(1) && Mystatus!=null) {
//                        if(Mystatus==null) Mystatus=snapshot1.child("statusImage").getValue(String.class);
                        Log.i("mystatus",Mystatus+"    aaaaa");
                    try{
                        Glide.with(requireContext())
                                .load(Mystatus)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        RoundedBitmapDrawable circularDrawable = RoundedBitmapDrawableFactory.create(getResources(), ((BitmapDrawable) resource).getBitmap());
                                        circularDrawable.setCircular(true);
                                        my_story.setImageDrawable(circularDrawable);

                                        return true;
                                    }
                                })
                                .into(my_story);
                        myStatusState.setVisibility(View.VISIBLE);
                        state=false;
                        if(snapshot.child(mobile).child("Status").child("MyStatus").child("viewed").getValue(String.class).equals("Yes")) myStatusState.setImageResource(R.drawable.status_circle_grey);
                        else myStatusState.setImageResource(R.drawable.status_cicrcle);
                    }catch (Exception e){}}
                    else{
                        state=false;
                        FirebaseStorage storage=FirebaseStorage.getInstance();
                        StorageReference deleter=storage.getReference().child("Status").child(mobile).child(snapshot1.getKey());
                        deleter.delete();
                        databaseReference.child(mobile).child("Status").child("MyStatus").child(snapshot1.getKey()).removeValue();
                        myStatusState.setVisibility(View.GONE);
//                        my_story
                    }

//                    Picasso.get().load(Mystatus).into(my_story);

                    break;
                }if(state) {
                    Bitmap bitmap;
                    if(Container.getMyImage()!=null) bitmap=Container.getMyImage();
                    else bitmap =MemoryData.getProfilePicture(MainActivity.mobile,getContext());
                    my_story.setImageBitmap(bitmap);
                    myStatusState.setVisibility(View.GONE);
                }

                for(DataSnapshot snapshot1:snapshot.child(mobile).child("Status").child("Others").getChildren()){
                    boolean shouldRemove=true;
                    for(DataSnapshot dataSnapshot:snapshot.child(Objects.requireNonNull(snapshot1.getKey())).child("Status").child("MyStatus").getChildren()){
                        long time=currTime;
                        try{
                            time=dataSnapshot.child("statusTime").getValue(Long.class);
                        }catch (Exception e){};

                        long timeDifferenceMillis=currTime-time;
                        String imageUrl=dataSnapshot.child("statusImage").getValue(String.class);
                        if(timeDifferenceMillis<= TimeUnit.DAYS.toMillis(1) && imageUrl!=null) {
                            shouldRemove=false;
                            DataSnapshot ref = snapshot.child(mobile).child("contacts").child(snapshot1.getKey());
                            String Name = ref.child("Name").getValue(String.class);
                            String chatId = ref.child("ChatId").getValue(String.class);
                            boolean viewed=false;
                            if(Objects.requireNonNull(snapshot1.child("viewed").getValue(String.class)).equals("No")) viewed=true;
                            statusList.add(0, new StatusModel(imageUrl, Name, snapshot1.getKey(), chatId,viewed));
//                        Log.i("checkingxxx",Name);
                            break;
                        }
                    }
                    if(shouldRemove) databaseReference.child(mobile).child("Status").child("Others").child(snapshot1.getKey()).removeValue();

                }
                Container.statusList=statusList;
                adapter = new StatusAdapter(statusList,getContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(adapter);

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    // uploading status ---------------------------------------------->


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data!=null && data.getData()!=null && requestCode==1 && resultCode==RESULT_OK){
            filepath=data.getData();
            try{
                InputStream inputStream=context.getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                String fileName = "profile_picture.jpg";
                my_story.setImageBitmap(bitmap);

                uploadtofirebase();
//                SharedPreferences prefs = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putString("profile_picture_path", getApplicationContext().getFilesDir() + "/" + fileName);
//                editor.apply();
//                Mydp=findViewById(R.id.Mydp);
//                Mydp.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
//                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void uploadtofirebase(){
//        ProgressDialog dialog=new ProgressDialog(this);
//        dialog.setTitle("File Uploader");
//        dialog.show();
        CustomLottieDialog customLottieDialog = new CustomLottieDialog(getContext(), "LO04");
        customLottieDialog.setLottieBackgroundColor("#000000");
        customLottieDialog.setDialogLayoutDimensions(500, 500);
        customLottieDialog.setLoadingText("Loading...");
        customLottieDialog.show();

        DatabaseReference ref=databaseReference.child(mobile).child("Status").child("MyStatus").push();
        String imageId=ref.getKey();
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference uploader=storage.getReference().child("Status").child(mobile).child(imageId);
        if(filepath!=null){
            uploader.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                if (!Login.this.isFinishing() && dialog != null) {
//                dialog.dismiss();
//                }
                    uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String statusUrl=uri.toString();
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ref.child("statusImage").setValue(statusUrl);
                                    ref.child("statusTime").setValue(System.currentTimeMillis());
//                                    String StatusId=newStatusRef.getKey();
//                                    newStatusRef.setValue(statusUrl);
//                                    Toast.makeText(getContext(),"Uploading your status.",Toast.LENGTH_SHORT).show();
                                    for(DataSnapshot dataSnapshot:snapshot.child(mobile).child("contacts").getChildren()){
//                                        databaseReference.child("Status").child("Others").child(mobile).
                                        databaseReference.child(Objects.requireNonNull(dataSnapshot.getKey())).child("Status").child("Others").child(mobile).child("viewed").setValue("No");
//                                        databaseReference.child(dataSnapshot.getKey()).child("Status").child("Others").child(mobile).setValue("");
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
//                        profilePicLink=uri.toString();
//                        Toast.makeText(getApplicationContext(),profilePicLink,Toast.LENGTH_SHORT).show();

                        }
                    });
//                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Image uploaded",Toast.LENGTH_SHORT).show();
                    customLottieDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("failureMessage",e.getMessage());
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void ImportContactsDialog() {
        // Create a Dialog instance
        Dialog dialog = new Dialog(getContext());

        // Set the layout for the Dialog
        dialog.setContentView(R.layout.import_contacts);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        // Optionally set other properties for the Dialog, e.g., size, title, etc.
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setTitle("Your Dialog Title");
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        TextView importContacts=dialog.findViewById(R.id.Import);
        importContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(),"Importing contacts",Toast.LENGTH_SHORT).show();
                checkAndRequestContactsPermission();
                dialog.dismiss();
            }
        });
        // Show the Dialog
        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        getStatus(view1);
//        databaseReference.child("Users").child(mobile).child("State").setValue(1);
    }

    @Override
    public void onStop() {
        super.onStop();
//        databaseReference.removeEventListener(valueEventListener);
//        databaseReference.child("Users").child(mobile).child("State").setValue(0);
    }




}