package com.daya.flickr;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.Toast;

import com.daya.flickr.flickrhelpers.FlickrjActivity;

public class MainActivity extends FragmentActivity implements
		TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

	private TabHost mTabHost;
	private ViewPager mViewPager;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, MainActivity.TabInfo>();
	private PagerAdapter mPagerAdapter;

	private class TabInfo {
		private String tag;
		private Class<?> clss;
		private Bundle args;
		private Fragment fragment;

		TabInfo(String tag, Class<?> clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}

	}

	class TabFactory implements TabContentFactory {

		private final Context mContext;

		public TabFactory(Context context) {
			mContext = context;
		}

		@Override
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		// TODO Auto-generated method stub

		if (menuItem.getItemId() == R.id.action_settings) {
			Toast.makeText(MainActivity.this, "Logging in...",
					Toast.LENGTH_SHORT).show();

			Intent intent = new Intent(MainActivity.this, FlickrjActivity.class);
			startActivity(intent);

		}
		return true;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Inflate the layout
		setContentView(R.layout.activity_main);

		ActionBar actionBar = getActionBar();
		actionBar.setLogo(R.drawable.ic_launcher);
		actionBar.setHomeButtonEnabled(true);

		// Initialise the TabHost
		this.initialiseTabHost(savedInstanceState);
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); // set
																				// tab
																				// as
																				// per
																				// saved
																				// state
		}
		// Intialise ViewPager
		this.intialiseViewPager();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("tab", mTabHost.getCurrentTabTag()); // save the tab
																// selected
		super.onSaveInstanceState(outState);
	}

	private void intialiseViewPager() {

		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, Tab1Fragment.class.getName()));
		fragments.add(Fragment.instantiate(this, Tab2Fragment.class.getName()));
		this.mPagerAdapter = new PagerAdapter(
				super.getSupportFragmentManager(), fragments);
		this.mViewPager = (ViewPager) super.findViewById(R.id.viewpager);
		this.mViewPager.setAdapter(this.mPagerAdapter);
		this.mViewPager.setOnPageChangeListener(this);
	}

	private void initialiseTabHost(Bundle args) {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;
		MainActivity.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Tab1").setIndicator("Photos list"),
				(tabInfo = new TabInfo("Tab1", Tab1Fragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		MainActivity.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Tab2").setIndicator("Upload photo"),
				(tabInfo = new TabInfo("Tab2", Tab2Fragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		mTabHost.setOnTabChangedListener(this);
	}

	private static void AddTab(MainActivity activity, TabHost tabHost,
			TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(activity.new TabFactory(activity));
		tabHost.addTab(tabSpec);
	}

	@Override
	public void onTabChanged(String tag) {
		int pos = this.mTabHost.getCurrentTab();
		if (pos <= 1)
			this.mViewPager.setCurrentItem(pos);

		System.out.println("----on tab change called----" + pos);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		this.mTabHost.setCurrentTab(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}
}
