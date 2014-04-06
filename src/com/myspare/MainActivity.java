package com.myspare;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener{
	 
    private String[] mSliderItems;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private LayoutInflater layoutInflator;
    Spinner spinnerDomainSelection;
    TextView tvDrawerTitle;
    private View viewCloseDrawer,viewOpenDrawer;
    int currentDomainSpinnerPosition = 0;
    String strHostUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        mTitle = "Browse";
 
        mSliderItems = getResources().getStringArray(R.array.sliderItem);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        layoutInflator = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Set the adapter for the list view
       
        //intilize view which set in actionbar when drawer is closed
        viewCloseDrawer = layoutInflator.inflate(R.layout.view_drawer_close, null);
        tvDrawerTitle = (TextView)viewCloseDrawer.findViewById(R.id.viewdrawer_close_tvtitle);
        tvDrawerTitle.setText(mTitle);
        
        //intilize view which set in actionbar when drawer is open
        viewOpenDrawer = layoutInflator.inflate(R.layout.view_drawer_open, null);
        spinnerDomainSelection = (Spinner)viewOpenDrawer.findViewById(R.id.viewdrawer_open_spinner);
        spinnerDomainSelection.setOnItemSelectedListener(MainActivity.this);
        
        
        
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mSliderItems));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
 
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {
 
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                //getSupportActionBar().setTitle(mTitle);
                                
                getSupportActionBar().setCustomView(viewCloseDrawer);
                getSupportActionBar().setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM);
                
            }
 
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                //getSupportActionBar().setTitle(mTitle);
                
                getSupportActionBar().setCustomView(viewOpenDrawer);
                getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM );
                
                
            }
        };
 
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
 
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        
 
 
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
 
        return super.onOptionsItemSelected(item);
    }
 
    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        
        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mSliderItems[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        
        Fragment fragment = null;
        String title = "";
        Fragment fragmentBrowse = new BrowseFragment();
        Fragment fragmentSearch = new SearchFragment();
        Fragment fragmentFavorite = new FavoriteFragment();
        if(position == 0){
        	fragment = fragmentBrowse;
        	title = "Browse";
        }else if(position == 1){
        	fragment = fragmentSearch;
        	title = "Search";
        }else{
        	fragment = fragmentFavorite;
        	title = "Favorite";
        }

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        
        setTitle(title);
    }
 
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        //getSupportActionBar().setTitle(mTitle);
        tvDrawerTitle.setText(mTitle);
    }
 
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		String str = (String)parent.getAdapter().getItem(position);
		
		if(currentDomainSpinnerPosition != position){
			currentDomainSpinnerPosition = position;
			mDrawerLayout.closeDrawers();
			selectItem(0);
			if(position == 0){
				strHostUrl = getString(R.string.hostCar);
			}else if(position == 1){
				strHostUrl = getString(R.string.hostParts);
			}else if(position == 2){
				strHostUrl = getString(R.string.hostBikes);
			}else if(position == 3){
				strHostUrl = getString(R.string.hostBoats);
			}else if(position == 4){
				strHostUrl = getString(R.string.hostTruck);
			}else if(position == 5){
				strHostUrl = getString(R.string.hostCarvans);
			}else{
				strHostUrl = getString(R.string.hostCar);
			}
			Toast.makeText(MainActivity.this, strHostUrl, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}