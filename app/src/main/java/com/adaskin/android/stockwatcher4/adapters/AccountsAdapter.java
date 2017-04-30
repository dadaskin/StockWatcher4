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

public class AccountsAdapter extends ArrayAdapter<AccountModel> {

	public AccountsAdapter(Context context, int index, List<AccountModel> accounts) {
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
		nameView.setText(account.getName());

		TextView colorView = (TextView)convertView.findViewById(R.id.dialog_account_color_field);
		colorView.setBackgroundColor(account.getColor());
		
		return convertView;
	}

	
	@Override
	public long getItemId(int position) {
		return getItem(position).getName().hashCode();
	}



	@Override
	public boolean hasStableIds() {
		return true;
	}

	



}
