package com.chidev.prototype;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chidev.prototype.checkList.CheckList;
import com.chidev.prototype.checkList.CheckListItemAdapter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CHECK_LIST_NAME = "checkListName";

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ArrayAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    public static ItemFragment newInstance(String checkListName) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(CHECK_LIST_NAME, checkListName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            String checkListName = getArguments().getString(CHECK_LIST_NAME);

            // use the checklist name to get items on list
            CheckList checkList = getArguments().getParcelable(checkListName);

            mAdapter = new CheckListItemAdapter(getActivity(), checkList.getItems(), this);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public ArrayAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String interactionType, String itemDescription, int position);
    }

    private CheckListItemAdapter.CheckListItemViewHolder currentItemHolder;

    public void currentlySelectedItem(CheckListItemAdapter.CheckListItemViewHolder holder) {
        if (currentItemHolder != null && currentItemHolder.position != holder.position) {
            currentItemHolder.editModeOff();
        }
        currentItemHolder = holder;
    }

    public void saveItemOnClickHandler(View view) {
        if (view.getTag() != null) {
            CheckListItemAdapter.CheckListItemViewHolder holder =
                    (CheckListItemAdapter.CheckListItemViewHolder) view.getTag();

            String newDescription = holder.editText.getText().toString();

            ((ListActivity) getActivity()).updateItemDescription(holder.position, newDescription);
            holder.editModeOff();
            mAdapter.notifyDataSetChanged();
        }
    }

    public void cancelItemOnClickHandler(View view) {
        if (view.getTag() != null) {
            CheckListItemAdapter.CheckListItemViewHolder holder =
                    (CheckListItemAdapter.CheckListItemViewHolder) view.getTag();
            holder.editText.setText(holder.titleText.getText().toString());
            holder.editModeOff();
            currentItemHolder = null;
        }
    }

    public void deleteItemOnClickHandler(View view) {
        if (view.getTag() != null) {
            CheckListItemAdapter.CheckListItemViewHolder holder =
                    (CheckListItemAdapter.CheckListItemViewHolder) view.getTag();
            holder.editModeOff();
            ((ListActivity) getActivity()).removeItem(holder.position);
            currentItemHolder = null;
            mAdapter.notifyDataSetChanged();
        }
    }
}
