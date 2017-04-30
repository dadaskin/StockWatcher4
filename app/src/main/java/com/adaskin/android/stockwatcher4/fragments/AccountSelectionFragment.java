package com.adaskin.android.stockwatcher4.fragments;

import java.util.LinkedList;
import java.util.List;

import com.adaskin.android.stockwatcher4.R;
import com.adaskin.android.stockwatcher4.adapters.AccountsAdapter;
import com.adaskin.android.stockwatcher4.models.AccountModel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class AccountSelectionFragment extends DialogFragment {

	private AlertOkListener alertOkListener;
	
	// An interface to be implemented on the hosting activity for the OK button
	public interface AlertOkListener {
		public void onOkClick(int position);
	}

	// Required empty constructor
	public AccountSelectionFragment() {
	}
	
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		try {
			alertOkListener = (AlertOkListener)activity;
		} catch (ClassCastException e) {
            // The hosting activity does not implemented the interface AlertOkListener
            throw new ClassCastException(activity.toString() + " must implement AlertOkListener");
		}
	}
	
	private ListView mLv;
	private Dialog mDlg;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        int accountColor = bundle.getInt("color");
        int index = AccountModel.getBlockColorIndex(accountColor);
        
        CharSequence[] accountNameArray = new CharSequence[0];
		accountNameArray = AccountModel.getBlockAccountNameList().toArray(accountNameArray); 
        
		LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflator.inflate(R.layout.account_list, null);
		mLv = (ListView)layout.findViewById(R.id.account_list);

		mLv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		AccountsAdapter accountsAdapter = new AccountsAdapter(getActivity(), index, getAccountModelList());
		mLv.setAdapter(accountsAdapter);
		mLv.setItemChecked(index, true);
		
		Button okButton = (Button)layout.findViewById(R.id.account_list_ok_button);
		okButton.setOnClickListener(mOkListener); 
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Select Account:");
		builder.setView(layout);
				
		mDlg = builder.create();
		mDlg.show();
        return mDlg;
	}
	
	private View.OnClickListener mOkListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = mLv.getCheckedItemPosition();
			alertOkListener.onOkClick(position);
			
			mDlg.dismiss();
			mDlg.cancel();
		}
	};
	

	private List<AccountModel> getAccountModelList() {
		CharSequence[] nameArray = new CharSequence[0];
		nameArray = AccountModel.getBlockAccountNameList().toArray(nameArray);
		
        Integer[] colorArray = new Integer[0];
        colorArray = AccountModel.getBlockAccountColorList().toArray(colorArray);
        
        List<AccountModel> accountList = new LinkedList<AccountModel>();
        for (int i=0; i< nameArray.length; i++) {
        	String name = nameArray[i].toString();
        	int color = colorArray[i].intValue();
        	accountList.add(new AccountModel(name,color));
        }
		
        return  accountList;
	}
	
	
	
}
