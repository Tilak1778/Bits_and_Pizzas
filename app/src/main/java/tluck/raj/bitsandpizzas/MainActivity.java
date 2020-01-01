package tluck.raj.bitsandpizzas;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {

    private ShareActionProvider shareActionProvider;
    private String drawerList[];
    private ListView drawerListView;
    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle drawerToggle;
    private int currentposition=0;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("position",currentposition);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerLayout =findViewById(R.id.drawer_layout);
        drawerList=getResources().getStringArray(R.array.drawer_list);
        drawerListView=findViewById(R.id.drawer_List_layout);
        drawerListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,drawerList));
        drawerListView.setOnItemClickListener(new onDrawerItemClickListener());

        if(savedInstanceState==null)
            selectItem(0);
        else
        {
            currentposition=savedInstanceState.getInt("position");
            setActionBarTitle(currentposition);

        }



       drawerToggle =new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer,R.string.close_drawer){
           @Override
           public void onDrawerOpened(View drawerView) {
               super.onDrawerOpened(drawerView);
               invalidateOptionsMenu();
           }

           @Override
           public void onDrawerClosed(View drawerView) {
               super.onDrawerClosed(drawerView);
               invalidateOptionsMenu();

           }
       };

        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager fm =getSupportFragmentManager();
                Fragment frag=fm.findFragmentByTag("visible_fragment");

                if(frag instanceof topFragment)
                    currentposition=0;
                if(frag instanceof pizza_fragment)
                    currentposition=1;
                if(frag instanceof pasta_fragment)
                    currentposition=2;
                if(frag instanceof store_fragment)
                    currentposition=3;

                setActionBarTitle(currentposition);

                drawerListView.setItemChecked(currentposition,true);



            }
        });



    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean draweropen =drawerLayout.isDrawerOpen(drawerListView);
        menu.findItem(R.id.action_share).setVisible(!draweropen);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        MenuItem menuItem=menu.findItem(R.id.action_share);
       shareActionProvider=
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_TEXT,"hii");

        shareActionProvider.setShareIntent(intent);

        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(drawerToggle.onOptionsItemSelected(item)){

            return true;
        }


        switch (item.getItemId()){
            case R.id.create_order:
            {
                Intent intent=new Intent(this,orderActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.setting_option:{
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }


    private class onDrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



            selectItem(position);


        }
    }

    private void selectItem(int pos){

        Fragment fragment;
        currentposition=pos;

        switch (pos){
            case 0:
                fragment=new topFragment();
                break;
            case 1:
                fragment=new pizza_fragment();
                break;
            case 2:
                fragment=new pasta_fragment();
                break;
            case 3:
                fragment=new store_fragment();
                break;
            default: fragment=new topFragment();


        }

       FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frameLayout,fragment,"visible_fragment");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();

        setActionBarTitle(pos);
        DrawerLayout layout = findViewById(R.id.drawer_layout);
        layout.closeDrawer(drawerListView);


    }
    private void setActionBarTitle(int position) {
        String title;
        if (position == 0){
            title = getResources().getString(R.string.app_name);
        } else {
            title = drawerList[position];
        }
        getSupportActionBar().setTitle(title);
    }

}
