package pe.com.hatunsol.ferreterias.adapter;

/**
 * Created by Sistemas on 14/01/2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import pe.com.hatunsol.ferreterias.BuscarOperarioActivity;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapter_Clientes extends FragmentStatePagerAdapter {
    List<String> titles = new ArrayList<String>();
    //CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter_Clientes(FragmentManager fm, List<String> mTitles, int mNumbOfTabsumb) {
        super(fm);

        this.titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }



    public void addTab (String title) {
        this.titles.add(title);
        this.NumbOfTabs++;
        this.notifyDataSetChanged();


    }



    public void deleteTab (int position) {
        this.titles.remove(position);
        this.NumbOfTabs--;

        this.notifyDataSetChanged();


    }





    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
      if(position==0) // if the position is 0 we are returning the First tab
        {
            BuscarOperarioActivity tab1 = new BuscarOperarioActivity();

            return null;
        }
      else if(position==1)             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
      {
          BuscarOperarioActivity tab2 = new BuscarOperarioActivity();
          return null;
      }
        else if(position==2)             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            BuscarOperarioActivity tab2 = new BuscarOperarioActivity();
            return null;
        }
      else{
          BuscarOperarioActivity tab3 = new BuscarOperarioActivity();
          return null;
      }


    }




    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }



}