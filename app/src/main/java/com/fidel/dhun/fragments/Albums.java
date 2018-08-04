package com.fidel.dhun.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fidel.dhun.R;
import com.fidel.dhun.SharedAlbum;
import com.fidel.dhun.adapter.AlbumRecyclerview;
import com.fidel.dhun.data.DatabaseSync;
import com.fidel.dhun.data.Songs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Albums#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Albums extends Fragment {

    private String SHARED;


    public static Albums newInstance() {

        Albums fragment = new Albums();
        return fragment;
    }

    ArrayList<Songs> albumList;
    RecyclerView AlbumsrecyclerView;
    private GridLayoutManager lLayout;

    public Albums() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Artist.
     */
    // TODO: Rename and change types and number of parameters
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view1 = inflater.inflate(R.layout.fragment_albums, container, false);
        AlbumsrecyclerView = (RecyclerView) view1.findViewById(R.id.albumsRecycler);


        albumList = new ArrayList<Songs>();
        DatabaseSync dbsync =  new DatabaseSync(getContext());
        albumList = dbsync.getAllContacts();
        Collections.sort(albumList, new Comparator<Songs>() {
            public int compare(Songs a, Songs b) {
                return a.getAlbum().compareTo(b.getAlbum());

            }
        });


        albumList.toArray();
        String Albumname = "dgfsfgwrthwr";
//int i =0;
        final ArrayList<Songs> tempArrayList = new ArrayList<Songs>();
        for (Songs s : albumList) {

            // if(i ==0)Albumname = s.getAlbum();


            if (!Albumname.equals(s.getAlbum())) {
                // Log.e("Demo Test",String.valueOf(s.getAlbum().contains(Albumname)));

                if (!s.getAlbum().contains(Albumname)) {
                    tempArrayList.add(s);
                    Albumname = s.getAlbum();
                }


            }

        }


        lLayout = new GridLayoutManager(getContext(), 3);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        AlbumsrecyclerView.setLayoutManager(lLayout);
        AlbumsrecyclerView.setHasFixedSize(true);
        AlbumsrecyclerView.setAdapter(new AlbumRecyclerview(getContext(), tempArrayList, 1, new AlbumRecyclerview.OnItemClickListener() {
            @Override
            public void onItemClick(Songs item, ImageView imageView) {


               // EventBus.getDefault().post(new GetMusicId(Integer.parseInt(String.valueOf(artistList.indexOf(item)))


                Intent sharedElem = new Intent(getActivity(), SharedAlbum.class);

                sharedElem.putExtra("activity","album");

                sharedElem.putExtra("shared", item.getArtistid());
                sharedElem.putExtra("imageuri", String.valueOf(item.getAlbumarturi()));
                sharedElem.putExtra("artistname", item.getAlbum());
                sharedElem.putExtra("artistid", item.getAlbumID());
                //   sharedElem.putExtra("list",tempArrayList);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), imageView, item.getArtistid());
                startActivity(sharedElem, optionsCompat.toBundle());
                // musicSrv.setSong(Integer.parseInt(String.valueOf(item.getID())));

               // musicSrv.playSong();
            }
        }));


        return view1;
    }


}
