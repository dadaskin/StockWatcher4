package com.adaskin.android.stockwatcher4.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adaskin.android.stockwatcher4.R;
import com.adaskin.android.stockwatcher4.models.AccountModel;
import com.adaskin.android.stockwatcher4.utilities.Constants;

public class AccountsAdapter extends ArrayAdapter<AccountModel> {

	public AccountsAdapter(Context context, List<AccountModel> accounts) {
		// Do something with default item
		
		super(context, 0, accounts);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_list_item, parent, false);
		}
		
		AccountModel account = getItem(position);
		TextView nameView = (TextView)convertView.findViewById(R.id.dialog_account_name_text_view);
		TextView colorView = (TextView)convertView.findViewById(R.id.dialog_account_color_field);
		if (account != null) {
			nameView.setText(account.getName());
			colorView.setBackgroundColor(account.getColor());
		}
		else {
			nameView.setText("Unknown");
			colorView.setBackgroundColor(Constants.ACCOUNT_UNKNOWN);
		}

		return convertView;
	}

	
	@Override
	public long getItemId(int position) {

		AccountModel item = getItem(position);
		if (item != null)
			return item.getName().hashCode();

		return -1L;
	}



	@Override
	public boolean hasStableIds() {
		return true;
	}

	



}
