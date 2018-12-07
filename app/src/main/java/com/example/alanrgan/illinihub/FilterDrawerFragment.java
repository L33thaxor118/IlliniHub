package com.example.alanrgan.illinihub;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.leanback.widget.HorizontalGridView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SearchView;

import com.example.alanrgan.illinihub.util.ButtonAdapter;
import com.example.alanrgan.illinihub.util.SortedArrayList;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterDrawerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterDrawerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterDrawerFragment extends Fragment implements ButtonAdapter.ClickListener {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";
  private Context activityContext;

  // TODO: Rename and change types of parameters
  private String mParam1 = "Hello";
  private String mParam2;
  private SortedArrayList<String> unselectedTags;
  private SortedArrayList<String> selectedTags;
  private ButtonAdapter unselectedTagAdapter;
  private ButtonAdapter selectedTagAdapter;
  private HorizontalGridView unselectedTagsContainer;
  private HorizontalGridView selectedTagsContainer;

  private OnFragmentInteractionListener mListener;

  public FilterDrawerFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment FilterDrawerFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static FilterDrawerFragment newInstance(String param1, String param2) {
    FilterDrawerFragment fragment = new FilterDrawerFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.filter_drawer, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    // UI component hooks need to be configured AFTER the fragment view has been created
    // At this point, we can query the parent view for the UI component we are looking for
    // with getView() and findViewById()
    unselectedTagsContainer = getView().findViewById(R.id.unselected_tag_container);
    selectedTagsContainer = getView().findViewById(R.id.selected_tag_container);

    unselectedTagsContainer.setHorizontalScrollBarEnabled(true);
    selectedTagsContainer.setHorizontalScrollBarEnabled(true);
    unselectedTagsContainer.setAdapter(unselectedTagAdapter);
    selectedTagsContainer.setAdapter(selectedTagAdapter);

    SearchView searchView = (SearchView) getView().findViewById(R.id.tag_search);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        unselectedTagAdapter.filter(newText);
        selectedTagAdapter.filter(newText);
        System.out.println("QUERY CHANGED");
        return false;
      }
    });
  }

  @Override
  public void onAttach(Context context) {
    unselectedTags = new SortedArrayList<>();
    selectedTags = new SortedArrayList<>();
    unselectedTags.insertSorted("Business");
    unselectedTags.insertSorted("Food");
    unselectedTags.insertSorted("Free");
    unselectedTags.insertSorted("GiveAway");
    unselectedTags.insertSorted("21+");
    unselectedTags.insertSorted("Tech Talk");
    unselectedTags.insertSorted("Ladies Night");
    unselectedTags.insertSorted("Study Group");

    unselectedTagAdapter = new ButtonAdapter(context, unselectedTags, this);
    selectedTagAdapter = new ButtonAdapter(context, selectedTags, this);

    super.onAttach(context);

    activityContext = context;
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
              + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public void onButtonAdapterItemClick(ButtonAdapter adapter, String item, int position) {
    if (adapter.equals(selectedTagAdapter)) {
      System.out.println("Clicked on Item");
      unselectedTags.insertSorted(selectedTags.get(position));
      selectedTags.remove(position);
    } else if (adapter.equals(unselectedTagAdapter)) {
      System.out.println("Clicked on Item");
      selectedTags.insertSorted(unselectedTags.get(position));
      unselectedTags.remove(position);
    }

    unselectedTagAdapter.notifyDataSetChanged();
    selectedTagAdapter.notifyDataSetChanged();
    mListener.updateFilter(selectedTags);
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    void updateFilter(List<String> tags);
  }
}
