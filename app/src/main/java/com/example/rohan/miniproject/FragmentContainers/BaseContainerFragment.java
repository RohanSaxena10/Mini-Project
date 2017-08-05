package com.example.rohan.miniproject.FragmentContainers;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;

import com.example.rohan.miniproject.R;


public class BaseContainerFragment extends Fragment {

	public void replaceFragment(Fragment fragment, boolean addToBackStack) {
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_left,
				R.animator.slide_out_right, R.animator.slide_in_right);
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.replace(R.id.container_framelayout, fragment);
		transaction.commit();
		getChildFragmentManager().executePendingTransactions();
	}
	
	public boolean popFragment() {
		Log.e("test", "pop fragment: " + getChildFragmentManager().getBackStackEntryCount());
		boolean isPop = false;
		if (getChildFragmentManager().getBackStackEntryCount() > 0) {
			isPop = true;
			getChildFragmentManager().popBackStack();
		}
		return isPop;
	}
	
}
