The following notes are for using Java with android studio.
NOTE: Some of the code snippets are from the Udacity android studio course.

Topics in these notes will be indicated with a "#" symbol and are listed below.
- #general
- #Life_Cycle
- #Toast
- #Layout_Types
- #HTTP_Requests
- #Recycler_Views
- #Menu_Items
- #Starting_New_Activities
- #implicit_intents
- #sqlite

#general
-------------------------------- General and Misc. ------------------------------
- set multiple values to an xml attribute using the | symbol.
  e.g. android:layout_gravity="start|center_vertical" (Sets the view to be vertically centered and aligned with the start of the parent)
--------------------------------- Life Cycle ------------------------------------
#Life_Cycle
The life cycle events are as follows and each method may be overriden within an AppCompatActivity class:
onCreate() -> onStart() -> onResume() -> onPause() -> onStop() -> onRestart() | onDestroy()

Note the following lifecycle events occur upon the rotation of the device:
    onPause() -> onStop() -> onDestroy() -> onCreate() -> onStart() -> onResume()

Save data between states using onSaveInstanceState():

-------------------------------- Toast ------------------------------------------
#Toast
Toast toast = Toast.makeText(MainActivity.this, "message", Toast.LENGTH_LONG);
toast.show();
toast.cancel(); //does automatically after a time period




#Layout_Types
-------------------------------- UI Layout Types ---------------------------------
- Linear Layout
  LinearLayout is a view group that aligns all children in a single direction, vertically or horizontally.

- Relative Layout
  RelativeLayout is a view group that displays child views in relative positions.

- Table Layout
TableLayout is a view that groups views into rows and columns.

- Absolute Layout
  AbsoluteLayout enables you to specify the exact location of its children.

- Frame Layout
  The FrameLayout is a placeholder on screen that you can use to display a single view.

- List View
  ListView is a view group that displays a list of scrollable items.

- Grid View:
  GridView is a ViewGroup that displays items in a two-dimensional, scrollable grid.

#HTTP_Requests
--------------------------------- HTTP Requests --------------------------------

1. Add the INTERNET permission to AndroidManifest.xml:
  <uses-permission android:name="android.permission.INTERNET" />

2. Build the URL using android.net.Uri and java.net.URL:

  //converts a android Uri to and java URL to be used in the HTTP request
  public static URL buildUrl(String githubSearchQuery) {
        // this creates a Uri that looks like: https://treytose.com?arg1=val1&arg2=val2
         Uri builtUri = Uri.parse("https://treytose.com").buildUpon()
                 .appendQueryParameter("arg1", "val1")
                 .appendQueryParameter("arg2", "val2")
                 .build();

        //convert the built Uri to a URL
         URL url = null;
         try {
             url = new URL(builtUri.toString());
         } catch (MalformedURLException e) {
             e.printStackTrace();
         }

         return url;
     }

3. Create an AsyncTask to execute the HTTP request.
NOTE: You CANNOT make an HTTP request on the main thread because it will make the UI lag and possibly crash.
  //The AsyncTask generics are used to determine what type of argument must be given and what type is returned.
  //  This AsyncTask takes a URL as an argument and returns a String (not sure what the middle one does)
  public class GithubQueryTask extends AsyncTask<URL, Void, String> {

       //This method is called before executing the main task
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           mLoadingIndicator.setVisibility(View.VISIBLE); //set loading icon to visible
       }

       // The main execution method. Multiple arguments may be given, "URL... params" is similar to args* in python.
       @Override
       protected String doInBackground(URL... params) {
           URL searchUrl = params[0];  //we know only 1 parameter will be given so get the first one.
           String githubSearchResults = null;
           try {
               githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl); // call the HTTP request method from this background thread.
           } catch (IOException e) {
               e.printStackTrace();
           }
           return githubSearchResults;  //The string of results of the HTTP request. This will be used in onPostExecute below
       }

       //onPostExecute, called after the main execution task.
       @Override
       protected void onPostExecute(String githubSearchResults) {
           mLoadingIndicator.setVisibility(View.INVISIBLE); // Hide loading icon
           if (githubSearchResults != null && !githubSearchResults.equals("")) {
               showJsonDataView();
               mSearchResultsTextView.setText(githubSearchResults);
           } else {
               // COMPLETED (16) Call showErrorMessage if the result is null in onPostExecute
               showErrorMessage();
           }
       }
   }

4. Call the HTTP request method from your AsyncTask:
  public static String getResponseFromHttpUrl(URL url) throws IOException {
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    try {
        InputStream in = urlConnection.getInputStream();

        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");

        boolean hasInput = scanner.hasNext();
        if (hasInput) {
            return scanner.next();
        } else {
            return null;
        }
    } finally {
        urlConnection.disconnect();
    }
  }
--------------------------------------------------------------------------------

#Recycler_Views
------------------- Recycler Views -----------------------
1. Add the recycler view dependency in build.gradle:
  - Design View -> Palette -> Common -> RecyclerView -> (Click the Download button)

2. Add the recycler view to the xml file. Give it an ID:
    <android.support.v7.widget.RecyclerView
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:id="@+id/rv_example"></android.support.v7.widget.RecyclerView>

3. Create the Layout File that will be the view of each item in the RecyclerView:
  - res -> layout -> (right click layout) -> new -> Layout Resource File
  - Name the resource file and click OK

4. Create a new Java file to hold the RecyclerView adapter.
  - right click java folder -> new -> java class

5. Create the RecyclerView Adapter:
    // The RecyclerViewAdapter
    // NOTE: RecyclerView.Adapter takes generic argument of a RecyclerView.ViewHolder
    //        which is created as an inner class: RvViewHolder (see below)
-----------
    public class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvViewHolder> {

        private int numItems;

        public RvAdapter(int numItems) {
            this.numItems = numItems;
        }

        @NonNull
        @Override
        public RvAdapter.RvViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();

            // Converting the XML rv_layout file to a java View object which will be used for this recyclerView item
            // NOTE: false MUST be passed here or the RecyclerView will not work at all!
            View view = LayoutInflater.from(context).inflate(R.layout.rv_layout, viewGroup, false);

            // init a viewholder with this created view
            RvViewHolder rvViewHolder = new RvViewHolder(view);


            return rvViewHolder;
        }

        // called by the RecyclerView to get the data to display at a given position i
        @Override
        public void onBindViewHolder(@NonNull RvAdapter.RvViewHolder rvViewHolder, int i) {
            rvViewHolder.setText(Integer.toString(i));
        }

        @Override
        public int getItemCount() {
            return numItems;
        }


        class RvViewHolder extends RecyclerView.ViewHolder {

            TextView exampleTV;

            public RvViewHolder(@NonNull View itemView) {
                super(itemView);

                // here you are using the view (not the ViewHolder) to find one of its attributes with findViewById
                //  in this example we are getting the example text view and initting it.
                exampleTV = itemView.findViewById(R.id.tv_example);
            }

            public void setText(String text) {
                //set the text of the textView. Called from onBindViewHolder()
                exampleTV.setText(text);
            }
        }
    }
-----------
6. Initiate the RecyclerViewAdapter and set the RecyclerViews adapter to it in MainActivity.java:
-----------
    public class MainActivity extends AppCompatActivity {

      RvAdapter theAdapter;
      RecyclerView theRecyclerView;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);

          theRecyclerView = (RecyclerView) findViewById(R.id.rv_example);
          theAdapter = new RvAdapter(100); // init an adapter which will have 100 views (rows or columns)
          LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this); //This is IMPORTANT! or it will not show up
          theRecyclerView.setLayoutManager(llm);
          theRecyclerView.setHasFixedSize(true);

          //set the recyclerviews adapter to the created adapter
          theRecyclerView.setAdapter(theAdapter);
      }
    }

-- Basic Setup Complete --

7. Click Handling Steps:
  - Make your ViewHolder class implement OnClickListener
  - Implement the required OnClick() method
  - In the constructor of your viewholder, set the click listener of the view to the viewholder

  class TrackListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

      public TrackListHolder(@NonNull View itemView) {
          super(itemView);
          itemView.setOnClickListener(this); //set the on click listener
      }

      @Override
      public void onClick(View v) {
          int clickedPosition = getAdapterPosition();
          Toast trackToast = Toast.makeText(v.getContext(), "View " + clickedPosition + " clicked!", Toast.LENGTH_LONG);
          trackToast.show();
      }
  }


--------------------------------------------------------------------------------
#Menu_Items
---------------------------- Menu Items ----------------------------------------
1. Create a string resource for the name of the menu item in strings.xml:
     <string name="reset">Reset</string>

2. Create a menu resource file:
  - Right click on res -> new Android Resource File -> Set type to Menu -> Name the file (e.g. "main.xml" for MainActivity) -> Click OK

3. In your new menu xml file "main.xml" add an item to the Menu:
    <menu xmlns:android="http://schemas.android.com/apk/res/android" xmlns:app="http://schemas.android.com/apk/res-auto">
      <item
        android:title="@string/reset"
        android:id="@+id/action_refresh"
        android:orderInCategory="1" //sets this menu item as the first one to be shown
        app:showAsAction="ifRoom"> //shows this menu item only if there is space available
      </item>
    </menu>

4. In MainActivity.java, override onCreateOptionsMenu():
    @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.main, menu); //inflate the new menu resource you created
         return true; //returning true will display the menu
     }

5.  Override onOptionsItemSelected:

    //This method is called when a menu item is selected.
    // You must get the item's id and compare it to the ID's in your resource file to see which menu item was selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == R.id.action_refresh) {
            /* do what needs to be done when the action_refresh menu item was clicked here */
            return true; //return true means we have handled it
        }

        return super.onOptionsItemSelected(item); //return the default boolean if we did not handle the selection
    }

--------------------------------------------------------------------------------
#Starting_New_Activities

1. Create a new activity using the wizard:
  - File -> New -> Activity -> Empty (or other template)

2. Create an intent:
  Intent itent = new Intent(MainActivity.this, NewActivity.class);  //create the intent using current context and the newly created Activity
  startActivity(intent); //changes to the new activity

3. (Optional) Pass data with the intent to the new activity using the putExtra() method:
  In the initial parent activity:
    intent.putExtra("Name", "John Doe");
    startActivity(intent);

  In the child activity:
    Intent intent = getIntent(); //retrieves the intent that started this activity
    if(intent.hasExtra("Name")) {
      String name = intent.getStringExtra("Name");
    }

--------------------------------------------------------------------------------
#implicit_intents

Implicit intents are used for when you do not know which specific app will need to be opened,
but you need to open something such as a webpage or a calling application.
Implicit intents allow android to decide which app to use.

Opening a webpage using an implicit intent:
  public void openWebPage(String uriString) {
       Uri uri = Uri.parse(uriString);
       Intent intent = new Intent(Intent.ACTION_VIEW, uri);
       if (intent.resolveActivity(getPackageManager()) != null) {
           startActivity(intent);
       }
   }

--------------------------------------------------------------------------------
#sqlite

1. Create a class to make a "contract" to define the schema for your database:
/* Contract classes are used for defining the schema of your SQLITE database */
public class Contract {

    private Contract() {} //make a private constructor so this class cannot be instantiated

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "theUserTable";
        public static final String COLUMN_NAME_FIRSTNAME = "firstname";
        public static final String COLUMN_NAME_LASTNAME = "lastname";

    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserEntry.COLUMN_NAME_FIRSTNAME + " TEXT," +
                    UserEntry.COLUMN_NAME_LASTNAME + " TEXT)";


    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;

}


2. Create a database Helper class:
public class DBHelper extends SQLiteOpenHelper {

  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "data.db";

  public DBHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
      db.execSQL(Contract.SQL_CREATE_ENTRIES);
      Log.d("MSG: ", "CALLED onCreate()");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL(Contract.SQL_DELETE_ENTRIES);
      onCreate(db);
  }
}


3.  Storing Data in the database:
  DBHelper dbHelper = new DBHelper(MainActivity.this);
  SQLiteDatabase db = dbHelper.getWritableDatabase();
  ContentValues values = new ContentValues();
  values.put(Contract.UserEntry.COLUMN_NAME_FIRSTNAME, firstName.getText().toString());
  values.put(Contract.UserEntry.COLUMN_NAME_LASTNAME, lastName.getText().toString());

  long newRodId = db.insert(Contract.UserEntry.TABLE_NAME, null, values);


4. Retreive Items from the database:
DBHelper dbHelper = new DBHelper(ViewDBActivity.this);
      SQLiteDatabase db = dbHelper.getReadableDatabase();

     String[] projection = {
             BaseColumns._ID,
             Contract.UserEntry.COLUMN_NAME_FIRSTNAME,
             Contract.UserEntry.COLUMN_NAME_LASTNAME
     };

     Cursor cursor = db.query(
             Contract.UserEntry.TABLE_NAME,   //Table to query
             projection,                      //Array of columns to return (null returns all)
             null,                   // The columns for the WHERE clause
             null,                // The values for the WHERE clause
             null,                    // dont group rows
             null,                    // dont filter by row groups
             null);                   // how to sort

      /* Retrieve all of the items from the cursor */
      List firstNames = new ArrayList<>();
      List lastNames = new ArrayList<>();

      // iterates through all results
      while(cursor.moveToNext()) {
          String firstName = cursor.getString(cursor.getColumnIndex(Contract.UserEntry.COLUMN_NAME_FIRSTNAME));
          String lastName = cursor.getString(cursor.getColumnIndex(Contract.UserEntry.COLUMN_NAME_LASTNAME));

          firstNames.add(firstName);
          lastNames.add(lastName);
      }

      cursor.close();

--------------------------------------------------------------------------------
