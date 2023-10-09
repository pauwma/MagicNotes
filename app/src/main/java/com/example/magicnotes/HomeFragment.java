package com.example.magicnotes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.magicnotes.Items.Note;
import com.example.magicnotes.Items.NoteDao;
import com.example.magicnotes.Utils.NotesAdapter;
import com.example.magicnotes.databinding.FragmentHomeBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements NotesAdapter.OnSelectionListener{

    private NotesAdapter notesAdapter;
    private NoteDao noteDao;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private boolean order;

    private ImageButton orderButton;
    private RecyclerView recyclerView;
    private AppBarLayout appBarLayout;

    private EditText searchBar;
    private Handler searchHandler = new Handler();
    private String lastQuery = "";

    enum OrderState {
        ASCENDING, DESCENDING, NONE
    }
    private OrderState titleOrderState = OrderState.NONE;
    private OrderState dateOrderState = OrderState.NONE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);

        searchBar = view.findViewById(R.id.barra);
        orderButton = view.findViewById(R.id.orderButton);
        appBarLayout = view.findViewById(R.id.appBarLayout);
        recyclerView = view.findViewById(R.id.notes_recycler_view);

        List<Note> notes = new ArrayList<>();
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        if (savedInstanceState != null) {
            titleOrderState = OrderState.valueOf(savedInstanceState.getString("titleOrderState", OrderState.NONE.name()));
            dateOrderState = OrderState.valueOf(savedInstanceState.getString("dateOrderState", OrderState.NONE.name()));
            order = savedInstanceState.getBoolean("order", true);
            Log.d("DEBUG", "" + order + titleOrderState+dateOrderState);

            updateNoteOrderName();
            updateNoteOrderDate();
            if(order){
                orderButton.setImageResource(R.drawable.dashboard);
                staggeredGridLayoutManager.setSpanCount(1);
            } else {
                orderButton.setImageResource(R.drawable.table_rows);
                staggeredGridLayoutManager.setSpanCount(2);
            }
        }
        notesAdapter = new NotesAdapter(notes);
        notesAdapter.setOnSelectionListener(this);
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);


        // Inicializa el Dao
        noteDao = AppDatabase.getDatabase(getContext()).noteDao();

        // Observa los cambios en la base de datos
        noteDao.getAllNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                // Actualiza el RecyclerView
                notesAdapter.updateNotes(notes);
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                searchHandler.removeCallbacksAndMessages(null);
                searchHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        searchNotes(s.toString());
                    }
                }, 500);
            }
        });

        // ! CHEAT DE NOTAS
        view.findViewById(R.id.profile_image_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        noteDao.insert(new Note(0, Note.NoteType.TEXT, "Lorem Ipsum", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas enim purus, tincidunt nec dui placerat, mollis elementum metus. Fusce suscipit purus et elementum porta. Nullam in libero dignissim ipsum faucibus porta. Duis ullamcorper justo id cursus ultrices. Aenean leo nisi, tincidunt pretium orci ac, iaculis vehicula metus. Vestibulum sit amet leo nisl. Vivamus et nisi velit. Phasellus nibh nibh, porttitor nec tellus quis, gravida feugiat enim. Aliquam quis maximus risus. Vestibulum pulvinar lacus lectus, nec vehicula enim hendrerit non.", "", null, 0, false));
                        noteDao.insert(new Note(1, Note.NoteType.TEXT, "Aliquam", "Donec et malesuada nulla. Morbi vestibulum lorem orci, eu pellentesque mauris euismod eget. In tempor urna lorem, vel egestas est finibus ut.", "", null, 0, false));
                        noteDao.insert(new Note(2, Note.NoteType.TEXT, "Blandit", "Duis suscipit condimentum tellus, vel rhoncus mauris cursus sit amet. ", "", null, 0, false));
                        noteDao.insert(new Note(3, Note.NoteType.TEXT, "Aenean", "enean nec ligula elementum, tristique felis eget, eleifend mauris. Vestibulum accumsan interdum risus, eu euismod erat cursus eu. Fusce quis consequat enim. Vestibulum egestas egestas nisi, quis auctor enim porta non. Fusce ante ex, ultricies sed dui nec, viverra fermentum justo.", "", null, 0, false));
                        noteDao.insert(new Note(4, Note.NoteType.TEXT, "Suspendisse rhoncus", "Suspendisse rhoncus, lectus non malesuada volutpat, enim risus eleifend leo, quis euismod ipsum neque eget nisl. Donec vel sagittis arcu. Nam dapibus fermentum porttitor. Suspendisse cursus lacinia felis, sed aliquam neque viverra non.", "", null, 0, false));
                        noteDao.insert(new Note(5, Note.NoteType.TEXT, "Mauris vestibulum", "Vivamus faucibus ligula ac nulla iaculis aliquam. Pellentesque velit mauris, cursus eget posuere eu, imperdiet ac neque. Aliquam placerat, sem eu maximus mollis, enim enim malesuada elit, ac cursus neque sapien sed erat. Phasellus vel malesuada tortor. Quisque efficitur ex et gravida fringilla. Vestibulum mattis leo ut vulputate ultrices.", "", null, 0, false));
                        noteDao.insert(new Note(6, Note.NoteType.TEXT, "Phasellus", "Phasellus lectus orci, porta tincidunt nisi sit amet, ornare posuere mi. Duis lacinia, ante et ultrices ullamcorper, ex lacus eleifend erat.", "", null, 0, false));
                        noteDao.insert(new Note(7, Note.NoteType.TEXT, "Class aptent taciti", "Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.", "", null, 0, false));
                        noteDao.insert(new Note(8, Note.NoteType.TEXT, "Ut eget accumsan mauris.", "Ut eget accumsan mauris. In hac habitasse platea dictumst. Suspendisse rutrum laoreet est, nec porta eros dignissim eu. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed volutpat facilisis scelerisque. Mauris eget sem bibendum, cursus odio at, dapibus nibh. Aenean porttitor ligula augue, a consectetur tortor posuere non.", "", null, 0, false));

                    }
                }).start();
            }
        });

        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "FAB Clicked"); // Debugging line
                Bundle bundle = new Bundle();
                bundle.putInt("note_id", -1);
                navController.navigate(R.id.noteFragment, bundle);
            }
        });

        view.findViewById(R.id.orderButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order) {
                    orderButton.setImageResource(R.drawable.dashboard);
                    staggeredGridLayoutManager.setSpanCount(1);
                    order = false;
                } else {
                    orderButton.setImageResource(R.drawable.table_rows);
                    staggeredGridLayoutManager.setSpanCount(2);
                    order = true;
                }
            }
        });

        view.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (Note e: notesAdapter.getSelectedNotes()) {
                            noteDao.delete(e);
                        }
                        getActivity().runOnUiThread(() -> {
                            notesAdapter.getSelectedNotes().clear();
                            notesAdapter.notifyDataSetChanged();
                            getView().findViewById(R.id.filterLayout).setVisibility(View.VISIBLE);
                            getView().findViewById(R.id.selectedLinearLayout).setVisibility(View.GONE);
                            onSelectionEnded();
                        });
                    }
                }).start();
            }
        });

        view.findViewById(R.id.nameOrderCardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (titleOrderState) {
                    case NONE:
                        titleOrderState = OrderState.ASCENDING;
                        break;
                    case ASCENDING:
                        titleOrderState = OrderState.DESCENDING;
                        break;
                    case DESCENDING:
                        titleOrderState = OrderState.NONE;
                        break;
                }
                updateNoteOrderName();
            }
        });

        view.findViewById(R.id.dateOrderCardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (dateOrderState) {
                    case NONE:
                        dateOrderState = OrderState.ASCENDING;
                        break;
                    case ASCENDING:
                        dateOrderState = OrderState.DESCENDING;
                        break;
                    case DESCENDING:
                        dateOrderState = OrderState.NONE;
                        break;
                }
                updateNoteOrderDate();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) { // Detectar un desplazamiento hacia arriba
                    appBarLayout.setExpanded(true, true);
                }
            }
        });

        notesAdapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Bundle bundle = new Bundle();
                bundle.putInt("note_id", note.getId());
                navController.navigate(R.id.noteFragment, bundle);
            }
        });

        notesAdapter.setOnNoteLongClickListener(new NotesAdapter.OnNoteLongClickListener() {
            @Override
            public void onNoteLongClick(View view, Note note) {
                Log.d("DEBUG", "Long Press detected");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("titleOrderState", titleOrderState.name());
        outState.putString("dateOrderState", dateOrderState.name());
        outState.putBoolean("order", order);
    }

    private void searchNotes(String query) {
        if(!query.equals(lastQuery)) {
            lastQuery = query;
            String searchQuery = "%" + query + "%";
            noteDao.searchNotesByTitle(searchQuery).observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
                @Override
                public void onChanged(List<Note> notes) {
                    notesAdapter.updateNotes(notes);
                }
            });
        }
    }
    private void updateNoteOrderName() {
        ImageView orderNameImageView = getView().findViewById(R.id.nameOrderImageView);
        ConstraintLayout nameOrderConstraintLayout = getView().findViewById(R.id.nameOrderConstraintLayout);
        MaterialCardView nameOrderCardView = getView().findViewById(R.id.nameOrderCardView);

        switch (dateOrderState){
            case ASCENDING:
                restartOrderDate();
            case DESCENDING:
                restartOrderDate();
        }

        switch (titleOrderState) {
            case ASCENDING:
                noteDao.getAllNotesOrderedByTitleAsc().observe(getViewLifecycleOwner(), notes -> {
                    notesAdapter.updateNotes(notes);
                });
                orderNameImageView.setImageResource(R.drawable.arrow_up);
                nameOrderCardView.setStrokeColor(ContextCompat.getColor(getContext(), R.color.primary_shadow));
                nameOrderConstraintLayout.setBackgroundResource(R.drawable.custom_button_primary);
                break;
            case DESCENDING:
                noteDao.getAllNotesOrderedByTitleDesc().observe(getViewLifecycleOwner(), notes -> {
                    notesAdapter.updateNotes(notes);
                });
                orderNameImageView.setImageResource(R.drawable.arrow_down);
                nameOrderCardView.setStrokeColor(ContextCompat.getColor(getContext(), R.color.primary_shadow));
                nameOrderConstraintLayout.setBackgroundResource(R.drawable.custom_button_primary);
                break;
            case NONE:
                noteDao.getAllNotes().observe(getViewLifecycleOwner(), notes -> {
                    notesAdapter.updateNotes(notes);
                });
                orderNameImageView.setImageResource(R.drawable.arrow_order);
                nameOrderCardView.setStrokeColor(ContextCompat.getColor(getContext(), R.color.outline));
                nameOrderConstraintLayout.setBackgroundResource(R.drawable.custom_button);
                break;
        }
    }
    private void updateNoteOrderDate() {
        ImageView orderDateImageView = getView().findViewById(R.id.dateOrderImageView);
        ConstraintLayout dateOrderConstraintLayout = getView().findViewById(R.id.dateOrderConstraintLayout);
        MaterialCardView dateOrderCardView = getView().findViewById(R.id.dateOrderCardView);

        switch (titleOrderState){
            case ASCENDING:
                restartOrderName();
            case DESCENDING:
                restartOrderName();
        }

        switch (dateOrderState) {
            case ASCENDING:
                noteDao.getAllNotesOrderedByTitleAsc().observe(getViewLifecycleOwner(), notes -> {
                    notesAdapter.updateNotes(notes);
                });
                orderDateImageView.setImageResource(R.drawable.arrow_up);
                dateOrderCardView.setStrokeColor(ContextCompat.getColor(getContext(), R.color.primary_shadow));
                dateOrderConstraintLayout.setBackgroundResource(R.drawable.custom_button_primary);
                break;
            case DESCENDING:
                noteDao.getAllNotesOrderedByTitleDesc().observe(getViewLifecycleOwner(), notes -> {
                    notesAdapter.updateNotes(notes);
                });
                orderDateImageView.setImageResource(R.drawable.arrow_down);
                dateOrderCardView.setStrokeColor(ContextCompat.getColor(getContext(), R.color.primary_shadow));
                dateOrderConstraintLayout.setBackgroundResource(R.drawable.custom_button_primary);
                break;
            case NONE:
                noteDao.getAllNotes().observe(getViewLifecycleOwner(), notes -> {
                    notesAdapter.updateNotes(notes);
                });
                orderDateImageView.setImageResource(R.drawable.arrow_order);
                dateOrderCardView.setStrokeColor(ContextCompat.getColor(getContext(), R.color.outline));
                dateOrderConstraintLayout.setBackgroundResource(R.drawable.custom_button);
                break;
        }
    }
    private void restartOrderDate(){
        ImageView orderDateImageView = getView().findViewById(R.id.dateOrderImageView);
        ConstraintLayout dateOrderConstraintLayout = getView().findViewById(R.id.dateOrderConstraintLayout);
        MaterialCardView dateOrderCardView = getView().findViewById(R.id.dateOrderCardView);
        orderDateImageView.setImageResource(R.drawable.arrow_order);
        dateOrderCardView.setStrokeColor(ContextCompat.getColor(getContext(), R.color.outline));
        dateOrderConstraintLayout.setBackgroundResource(R.drawable.custom_button);
    }
    private void restartOrderName(){
        ImageView orderNameImageView = getView().findViewById(R.id.nameOrderImageView);
        ConstraintLayout nameOrderConstraintLayout = getView().findViewById(R.id.nameOrderConstraintLayout);
        MaterialCardView nameOrderCardView = getView().findViewById(R.id.nameOrderCardView);
        orderNameImageView.setImageResource(R.drawable.arrow_order);
        nameOrderCardView.setStrokeColor(ContextCompat.getColor(getContext(), R.color.outline));
        nameOrderConstraintLayout.setBackgroundResource(R.drawable.custom_button);
    }

    @Override
    public void onSelectionStarted() {
        getView().findViewById(R.id.filterLayout).setVisibility(View.GONE);
        getView().findViewById(R.id.selectedLinearLayout).setVisibility(View.VISIBLE);
    }

    @Override
    public void onSelectionEnded() {
        getView().findViewById(R.id.filterLayout).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.selectedLinearLayout).setVisibility(View.GONE);
    }
}