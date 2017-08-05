package com.example.rohan.miniproject.FragmentContainers;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.rohan.miniproject.Chat.ChatPreviews;
import com.example.rohan.miniproject.Profile.RealtimeUpdates;
import com.example.rohan.miniproject.R;


public class Tab3ContainerFragment extends BaseContainerFragment {
	
	private boolean mIsViewInited;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e("test", "tab 2 oncreateview");
		return inflater.inflate(R.layout.container_fragment, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e("test", "tab 2 container on activity created");
		if (!mIsViewInited) {
			mIsViewInited = true;
			initView();
		}
	}
	
	private void initView() {
		Log.e("test", "tab 2 init view");
		replaceFragmentstart(new ChatPreviews(), false);
	}

	String tag = "tag";

	public void replaceFragment(Fragment fragment, boolean addToBackStack) {
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_left,
				R.animator.slide_out_right, R.animator.slide_in_right);

		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.replace(R.id.container_framelayout, fragment,tag);
		transaction.commit();
		getChildFragmentManager().executePendingTransactions();
	}
	public void replaceFragmentstart(Fragment fragment, boolean addToBackStack) {
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		//transaction.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.replace(R.id.container_framelayout, fragment,tag);

		transaction.commit();
		getChildFragmentManager().executePendingTransactions();
	}

	/*public void onBack() {
		Fragment CurrentTab = getChildFragmentManager().findFragmentByTag(tag);
		boolean isPopFragment = false;


		isPopFragment = ((BaseContainerFragment) CurrentTab).popFragment();
		if (!isPopFragment) {
			//FragmentActivity.finish();
		}
	}
*/
}
