package com.magally.michiganmain;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.magally.michiganmain.Fragments.FeedFragment;
import com.magally.michiganmain.Fragments.NavigationDrawerFragment;
import com.magally.michiganmain.Fragments.ProfileFragment;
import com.magally.michiganmain.Fragments.QuestionsFragment;
import com.magally.michiganmain.Fragments.AnswersFragment;
import com.magally.michiganmain.Fragments.SearchFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private static final String FEED_FRAGMENT_TAG = "feed_fragment";
    private static final String PREGUNTAS_FRAGMENT_TAG = "preguntas_fragment";
    private static final String RESPUESTAS_FRAGMENT_TAG = "respuestas_fragment";
    private static final String BUSCAR_FRAGMENT_TAG = "buscar_fragment";
    private static final String PERFIL_FRAGMENT_TAG = "perfil_fragment";
    private SharedPreferences sharedPrefs;
    private String username;
    private ImageView zoomImageView;
    private ListView mListView;
    private Fragment objFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);
        username = sharedPrefs.getString("usuario", "");

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

       // zoomImageView = (ImageView)findViewById(R.id.expanded_image);

    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {

        objFragment = null;
        sharedPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);
        username = sharedPrefs.getString("usuario", "");
        String fragment_tag = null;
        switch (position){
            case 0:
                objFragment = new FeedFragment();
                fragment_tag = FEED_FRAGMENT_TAG;
                break;
            case 1:
                if(username.equals("")||username.equals(null)) {
                    Toast.makeText(this,"Debe iniciar sesión!", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    objFragment = new QuestionsFragment();
                    fragment_tag = PREGUNTAS_FRAGMENT_TAG;
                }
                break;
            case 2:
                if(username.equals("")||username.equals(null)) {
                    Toast.makeText(this,"Debe iniciar sesión!", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    objFragment = new AnswersFragment();
                    fragment_tag = RESPUESTAS_FRAGMENT_TAG;
                }
                break;
            case 3:
                objFragment = new SearchFragment();
                fragment_tag = BUSCAR_FRAGMENT_TAG;
                break;
            case 4:
                if(username.equals("")||username.equals(null)) {
                    Toast.makeText(this,"Debe iniciar sesión!", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    objFragment = new ProfileFragment();
                    fragment_tag = PERFIL_FRAGMENT_TAG;
                }
                break;
        }

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, objFragment, fragment_tag)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onBackPressed() {
        zoomImageView = (ImageView)findViewById(R.id.expanded_image);
        mListView = (ListView)findViewById(R.id.feedList);
        if(zoomImageView!= null && zoomImageView.getVisibility()==View.VISIBLE){
            zoomImageView.setVisibility(View.GONE);
            ((ArrayAdapter)mListView.getAdapter()).notifyDataSetChanged();
        }else if (objFragment instanceof FeedFragment){

            super.onBackPressed();
        }else{
            objFragment= new FeedFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, objFragment, FEED_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            MenuItem item = menu.findItem(R.id.action_sesion);
            sharedPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);
            username = sharedPrefs.getString("usuario", "");
            if(username.equals("")||username.equals(null)){
                item.setTitle("Iniciar Sesión");
                invalidateOptionsMenu();
            }else{
                item.setTitle("Cerrar Sesión");
                invalidateOptionsMenu();
            }

            restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        sharedPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);
        username = sharedPrefs.getString("usuario", "");
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_example){
            Intent iRep = new Intent(this, Reputacion.class);
            startActivity(iRep);
            return true;
        }

        if (id == R.id.action_sesion){
            Log.d("MainActivity", "Username: "+username);

            if(username.equals("")||username.equals(null)){
            Intent ises = new Intent(this, LoginActivity.class);
                ises.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ises);
            }else{
                sharedPrefs.edit().clear().apply();
                item.setTitle("Iniciar Sesión");
                invalidateOptionsMenu();
                Toast.makeText(getApplicationContext(),"Sesion Cerrada",Toast.LENGTH_LONG).show();
                ((FeedFragment)getSupportFragmentManager().findFragmentByTag(FEED_FRAGMENT_TAG)).updateFeed(0);
            }

            return true;
        }

        if (id == R.id.action_acerca_de){
            Intent iAcer = new Intent(this, AcercaDe.class);
            startActivity(iAcer);
            return true;
        }
        if (id == R.id.action_preferencias) {
            return true;
        }
        if (id == R.id.action_new_question) {
            if(username.equals("")||username.equals(null)) {
                Toast.makeText(this,"Debe iniciar sesión!", Toast.LENGTH_LONG).show();
            } else{
                Intent intent = new Intent(this, NewQuestion.class);
                startActivity(intent);
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
