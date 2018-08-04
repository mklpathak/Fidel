package com.fidel.dhun.fragments;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.fidel.dhun.adapter.ArtistRecyclerView;
import com.fidel.dhun.data.ArtistInfo;
import com.fidel.dhun.data.ArtistPojo;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link Artist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Artist extends Fragment {




    public static Artist newInstance() {
        Artist fragment = new Artist();
        return fragment;
    }


    RecyclerView ArtistrecyclerView;
    private GridLayoutManager lLayout;


    public Artist() {
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
         final View view1=  inflater.inflate(R.layout.fragment_artist, container, false);
        ArtistrecyclerView = (RecyclerView) view1.findViewById(R.id.artistRecycler);



        //artistApi = ApiUtils.getArtist();




        //artistList = (ArrayList<Songs>) bundle1.getSerializable("songList");




       // DatabaseSync dbsync = new DatabaseSync(getContext());
        ArtistInfo artistSync = new ArtistInfo(getContext());














//        ArtistInfo artistInfo = new ArtistInfo(getActivity(), artistname );
//        artistInfo.getArtist();
        lLayout = new GridLayoutManager(getContext(), 3);


        ArtistrecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        ArtistrecyclerView.setLayoutManager(lLayout);
        ArtistrecyclerView.setHasFixedSize(true);
        if(artistSync.getAllArtist()!=null){

        ArtistrecyclerView.setAdapter(new ArtistRecyclerView(getContext(),artistSync.getAllArtist(),0, new ArtistRecyclerView.OnItemClickListener() {
            @Override public void onItemClick(ArtistPojo item, ImageView imageView) {


                //EventBus.getDefault().post(new GetMusicId(Integer.parseInt(String.valueOf(artistList.indexOf(item)))));
                Intent sharedElem = new Intent(getActivity(),SharedAlbum.class);
                sharedElem.putExtra("activity","artist");
                sharedElem.putExtra("shared",item.getName());
                sharedElem.putExtra("imageuri",String.valueOf(item.getImageuri()));
                sharedElem.putExtra("artistname",item.getName());
             //   sharedElem.putExtra("list",tempArrayList);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),imageView,item.getName());
                startActivity(sharedElem,optionsCompat.toBundle());
                // musicSrv.setSong(Integer.parseInt(String.valueOf(item.getID())));

//                musicSrv.playSong();
            }
        }));}



        return view1;
    }

//    private void loadArtist(String Artistname) {
//        artistApi.getArtist(Artistname).enqueue(new Callback<Example>() {
//            @Override
//            public void onResponse(Call<Example> call, final Response<Example> response) {
////                Log.e("Artist name Mukul","pathak " + response.body().getArtist().getImage().get(3).getText());
//
//
//                if(response.body().getArtist() != null) {
//
//                    if (!response.body().getArtist().getImage().get(3).getText().equals("")) {
//
//                        Picasso.with(getContext())
//                                .load(response.body().getArtist().getImage().get(3).getText())
//                                .into(new Target() {
//                                          @Override
//                                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                                              try {
//                                                  String root = Environment.getExternalStorageDirectory().toString();
//                                                  File myDir = new File(root + "/yourDirectory");
//
//                                                  if (!myDir.exists()) {
//                                                      myDir.mkdirs();
//                                                  }
//
//                                                  String name = response.body().getArtist().getName() + ".png";
//                                                  myDir = new File(myDir, name);
//                                                  FileOutputStream out = new FileOutputStream(myDir);
//                                                  bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
//
//                                                  out.flush();
//                                                  out.close();
//                                              } catch (Exception e) {
//                                                  // some action
//                                              }
//                                          }
//
//                                          @Override
//                                          public void onBitmapFailed(Drawable errorDrawable) {
//                                          }
//
//                                          @Override
//                                          public void onPrepareLoad(Drawable placeHolderDrawable) {
//                                          }
//                                      }
//                                );
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Example> call, Throwable t) {
//
//            }
//        });
//
//    }t


 public class BackgroundProcessing extends AsyncTask<Boolean,Boolean,Boolean>{
     @Override
     protected Boolean doInBackground(Boolean... booleans) {

         return null;
     }
 }
}
