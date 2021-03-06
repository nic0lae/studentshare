package com.fourty6et2.studentshare.fragments.buy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fourty6et2.studentshare.Config;
import com.fourty6et2.studentshare.Helpers;
import com.fourty6et2.studentshare.R;
import com.fourty6et2.studentshare.Section;
import com.fourty6et2.studentshare.StudentShareApi;
import com.fourty6et2.studentshare.activities.add.AddActivity;
import com.fourty6et2.studentshare.activities.add.IAddDelegate;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class BuySectionFragment extends Fragment implements IAddDelegate {

    private static final String BuyListTag = "BuyListFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_buy, container, false);

        BuyListFragment fragment = new BuyListFragment();
        getChildFragmentManager()
            .beginTransaction()
            .add(R.id.BuyListContainer, fragment, BuyListTag)
            .commit();

		return rootView;
	}

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            reloadData();
        }
    }

    @Override
    public void add(Activity a) {
        Intent i = new Intent(a, AddActivity.class);
        a.startActivityForResult(i, Section.AsEnum.Buy.ordinal());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data.getExtras().containsKey(AddActivity.DataType)) {

            int type = data.getExtras().getInt(AddActivity.DataType);
            String typeAsString = Helpers.getTypeAsStrignFromItemTypeAsInt(Helpers.getItemTypeFromInt(type));
            String phone = data.getExtras().getString(AddActivity.DataPhone);
            String email = data.getExtras().getString(AddActivity.DataEmail);
            String description = data.getExtras().getString(AddActivity.DataDescription);
            String price = data.getExtras().getString(AddActivity.DataPrice);

            StudentShareApi.AddSearch(Config.User.Id, typeAsString, phone, email, description, price, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    reloadData();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    reloadData();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    reloadData();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    if (statusCode != 200) {
                        Helpers.showNotificationBubble(R.string.BuySectionAddFailure, getActivity());
                    } else {
                        reloadData();
                    }
                }
            });
        }
    }

    private void reloadData() {
        BuyListFragment fragment = (BuyListFragment) getChildFragmentManager().findFragmentByTag(BuyListTag);

        if (fragment != null)
            fragment.reloadData();
    }
}
