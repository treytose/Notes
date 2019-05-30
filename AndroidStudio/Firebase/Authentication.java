public class MainActivity extends AppCompatActivity {
    ImageButton mSendButton;
    EditText mMessageView;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDBReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private MessageAdapter mMessageAdapter;
    private RecyclerView mMessageRV;

    private String mUsername; //The currently signed in users username

    public static final int RC_SIGN_IN = 123; //actvitiy identifier for signing in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Firebase Init */
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDBReference = mFirebaseDatabase.getReference().child("messages");
        mAuth = FirebaseAuth.getInstance();

        /* UI Init */
        mMessageView = (EditText) findViewById(R.id.textInput);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mMessageRV = (RecyclerView) findViewById(R.id.messageRV);

        /* Recycler View init and setup */
        mMessageAdapter = new MessageAdapter();
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        llm.setStackFromEnd(true);
        mMessageRV.setLayoutManager(llm);
        mMessageRV.setHasFixedSize(true);
        mMessageRV.setAdapter(mMessageAdapter);

        /* Listeners */
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMessage messageObj = new ChatMessage(mUsername, mMessageView.getText().toString(), null);
                mMessagesDBReference.push().setValue(messageObj);
                mMessageView.setText("");
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    // user is signed in
                   // Toast.makeText(MainActivity.this, "You are signed in", Toast.LENGTH_SHORT).show();
                    onSignedInInit(user.getDisplayName());
                } else {
                    //user is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()
                                            ))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // After sign in process
        if(requestCode == RC_SIGN_IN) {
            if(resultCode == RESULT_OK) {
                // if the activity completed successfully here

            } else if(resultCode == RESULT_CANCELED) {
                //This is called if the user presses the back button, in this case we want to back out of the app

                finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDBReadListener();
        mMessageAdapter.clear();
    }

    // method for setup when a user signs in, called from the auth state listener
    private void onSignedInInit(String displayName) {
        mUsername = displayName;

        if(mChildEventListener == null) {
            attachDBReadListener();
        }

    }

    private void onSignedOutCleanup() {
        mUsername = "Anonymous";
        mMessageAdapter.clear();
        detachDBReadListener();
    }

    private void attachDBReadListener() {
        // This listener is attached to a specific section of the database and will update according to its various listeners
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                chatMessage.setMessageKey(dataSnapshot.getKey());
                mMessageAdapter.addMessage(chatMessage);
                mMessageAdapter.notifyDataSetChanged();
                mMessageRV.smoothScrollToPosition(mMessageAdapter.getItemCount()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                mMessageAdapter.removeMessage(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        //attach the listener to the message section of the database
        mMessagesDBReference.addChildEventListener(mChildEventListener);
    }

    private void detachDBReadListener() {
        if(mChildEventListener != null) {
            mMessagesDBReference.removeEventListener(mChildEventListener); //remove DB listener if user signs out
        }
        mChildEventListener = null;
    }


    /********** Menu Item **************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        if(itemID == R.id.sign_out) {
            // Signout user
            AuthUI.getInstance().signOut(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
