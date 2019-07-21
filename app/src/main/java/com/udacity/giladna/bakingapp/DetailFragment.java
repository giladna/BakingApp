package com.udacity.giladna.bakingapp;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.squareup.picasso.Picasso;
import com.udacity.giladna.bakingapp.model.Ingredient;
import com.udacity.giladna.bakingapp.model.Step;

import java.util.ArrayList;


public class DetailFragment extends Fragment {


    private Step step;
    private SimpleExoPlayer simpleExoPlayer;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> stepsList;
    private int position;
    private boolean videoplaying;
    private boolean twoPane = false;
    long curPosition;
    MediaSource mediaSource;

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        step = getArguments().getParcelable("step");
        ingredients = getArguments().getParcelableArrayList("ingredients");
        stepsList = getArguments().getParcelableArrayList("stepsList");
        position = getArguments().getInt("position");
        twoPane = getArguments().getBoolean("twoPane");


        if (step != null) {

            if (simpleExoPlayer == null) {
                simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), new DefaultTrackSelector());
            }

            mediaSource = buildMediaSource(Uri.parse(step.getVideoURL()), new DefaultDataSourceFactory(getContext(), "User-Agent-BakingApp"));
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    private MediaSource buildMediaSource(Uri uri, DefaultDataSourceFactory dataSourceFactory) {
        return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        if (ingredients != null) {
            (rootView.findViewById(R.id.recipe_detail)).setVisibility(View.GONE);
            (rootView.findViewById(R.id.playerView)).setVisibility(View.GONE);
            int i = 0;
            for (Ingredient ingredient : ingredients) {
                String count = getColoredSpanned(++i + ") ", "#800000");
                ((TextView) rootView.findViewById(R.id.recipe_desc_tv)).append(Html.fromHtml(count));
                ((TextView) rootView.findViewById(R.id.recipe_desc_tv)).append(Html.fromHtml(getColoredSpanned(ingredient.getQuantity() + " " + ingredient.getMeasure() + " ",  "#000080")));
                ((TextView) rootView.findViewById(R.id.recipe_desc_tv)).append(ingredient.getIngredient().substring(0, 1).toUpperCase() + ingredient.getIngredient().substring(1) + " \n\n");
            }
        }

        if (step != null) {

            ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(step.getShortDescription());
            ((TextView) rootView.findViewById(R.id.recipe_desc_tv)).setText(step.getDescription());
            String thumbnailUrl = step.getThumbnailURL();
            if (!TextUtils.isEmpty(thumbnailUrl) && (thumbnailUrl.contains(".jpeg") || thumbnailUrl.contains(".jpg") || thumbnailUrl.contains("png"))) {


                ImageView imageView = rootView.findViewById(R.id.thumbnail_img);
                imageView.setVisibility(View.VISIBLE);

                Picasso.with(getContext())
                        .load(thumbnailUrl)
                        .error(R.drawable.error)
                        .placeholder(R.drawable.error)
                        .into(imageView);

            } else {
                rootView.findViewById(R.id.no_thumbnail_tv).setVisibility(View.VISIBLE);
            }

            if (step.getVideoURL() != null && !step.getVideoURL().isEmpty()) {
                videoplaying = true;
                ((PlayerView) rootView.findViewById(R.id.playerView)).setPlayer(simpleExoPlayer);
            } else {
                videoplaying = false;

                (rootView.findViewById(R.id.playerView)).setVisibility(View.GONE);
            }
            Button buttonNext = rootView.findViewById(R.id.next_step_btn);
            Button buttonPrev = rootView.findViewById(R.id.prev_step_btn);
            if (twoPane) {
                buttonNext.setVisibility(View.GONE);
                buttonPrev.setVisibility(View.GONE);

            } else {
                if (position == 0) {
                    buttonNext.setVisibility(View.VISIBLE);
                    buttonPrev.setVisibility(View.GONE);
                } else
                if (position + 1 == stepsList.size()) {
                    buttonNext.setVisibility(View.GONE);
                    buttonPrev.setVisibility(View.VISIBLE);
                } else {
                    buttonNext.setVisibility(View.VISIBLE);
                    buttonPrev.setVisibility(View.VISIBLE);
                }


                buttonNext.setOnClickListener(v -> {

                    if (videoplaying) {
                        simpleExoPlayer.stop();
                    }
                    position++;

                    if (position  == 0) {
                        (rootView.findViewById(R.id.next_step_btn)).setVisibility(View.VISIBLE);
                        (rootView.findViewById(R.id.prev_step_btn)).setVisibility(View.GONE);

                    } else if (position + 1 == stepsList.size()) {
                        (rootView.findViewById(R.id.next_step_btn)).setVisibility(View.GONE);
                        (rootView.findViewById(R.id.prev_step_btn)).setVisibility(View.VISIBLE);

                    } else {
                        (rootView.findViewById(R.id.next_step_btn)).setVisibility(View.VISIBLE);
                        (rootView.findViewById(R.id.prev_step_btn)).setVisibility(View.VISIBLE);

                    }

                    Step currStep = stepsList.get(position);
                    String currStepThumbnailUrl = currStep.getThumbnailURL();
                    if (!TextUtils.isEmpty(currStepThumbnailUrl) && (currStepThumbnailUrl.contains(".jpeg") || currStepThumbnailUrl.contains(".jpg") || currStepThumbnailUrl.contains("png"))) {

                        ImageView imageView = rootView.findViewById(R.id.thumbnail_img);
                        imageView.setVisibility(View.VISIBLE);

                        Picasso.with(getContext())
                                .load(thumbnailUrl)
                                .error(R.drawable.error)
                                .placeholder(R.drawable.error)
                                .into(imageView);

                    } else {
                        rootView.findViewById(R.id.no_thumbnail_tv).setVisibility(View.VISIBLE);
                    }

                    ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(currStep.getShortDescription());
                    ((TextView) rootView.findViewById(R.id.recipe_desc_tv)).setText(currStep.getDescription());

                    if (!TextUtils.isEmpty(currStep.getVideoURL())) {
                        if (simpleExoPlayer == null) {
                            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), new DefaultTrackSelector());
                        }
                        mediaSource = buildMediaSource(Uri.parse(currStep.getVideoURL()), new DefaultDataSourceFactory(getContext(), "User-Agent-BakingApp"));
                        simpleExoPlayer.prepare(mediaSource);
                        simpleExoPlayer.setPlayWhenReady(true);
                        videoplaying = true;
                        ((PlayerView) rootView.findViewById(R.id.playerView)).setPlayer(simpleExoPlayer);
                        (rootView.findViewById(R.id.playerView)).setVisibility(View.VISIBLE);
                    } else {
                        videoplaying = false;
                        (rootView.findViewById(R.id.playerView)).setVisibility(View.GONE);
                    }
                });

                buttonPrev.setOnClickListener(v -> {

                    if (videoplaying) {
                        simpleExoPlayer.stop();
                    }
                    position--;

                    if (position  == 0) {
                        (rootView.findViewById(R.id.next_step_btn)).setVisibility(View.VISIBLE);
                        (rootView.findViewById(R.id.prev_step_btn)).setVisibility(View.GONE);

                    } else if (position + 1 == stepsList.size()) {
                        (rootView.findViewById(R.id.next_step_btn)).setVisibility(View.GONE);
                        (rootView.findViewById(R.id.prev_step_btn)).setVisibility(View.VISIBLE);

                    } else {
                        (rootView.findViewById(R.id.next_step_btn)).setVisibility(View.VISIBLE);
                        (rootView.findViewById(R.id.prev_step_btn)).setVisibility(View.VISIBLE);

                    }

                    Step currStep = stepsList.get(position);
                    String currStepThumbnailUrl = currStep.getThumbnailURL();
                    if (!TextUtils.isEmpty(currStepThumbnailUrl) && (currStepThumbnailUrl.contains(".jpeg") || currStepThumbnailUrl.contains(".jpg") || currStepThumbnailUrl.contains("png"))) {

                        ImageView imageView = rootView.findViewById(R.id.thumbnail_img);
                        imageView.setVisibility(View.VISIBLE);

                        Picasso.with(getContext())
                                .load(thumbnailUrl)
                                .error(R.drawable.error)
                                .placeholder(R.drawable.error)
                                .into(imageView);

                    } else {
                        rootView.findViewById(R.id.no_thumbnail_tv).setVisibility(View.VISIBLE);
                    }


                    ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(currStep.getShortDescription());
                    ((TextView) rootView.findViewById(R.id.recipe_desc_tv)).setText(currStep.getDescription());

                    if (!TextUtils.isEmpty(currStep.getVideoURL())) {
                        if (simpleExoPlayer == null) {
                            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), new DefaultTrackSelector());
                        }

                        mediaSource = buildMediaSource(Uri.parse(currStep.getVideoURL()), new DefaultDataSourceFactory(getContext(), "User-Agent-BakingApp"));
                        simpleExoPlayer.prepare(mediaSource);
                        simpleExoPlayer.setPlayWhenReady(true);
                        videoplaying = true;
                        ((PlayerView) rootView.findViewById(R.id.playerView)).setPlayer(simpleExoPlayer);
                        (rootView.findViewById(R.id.playerView)).setVisibility(View.VISIBLE);
                    } else {
                        videoplaying = false;
                        (rootView.findViewById(R.id.playerView)).setVisibility(View.GONE);
                    }
                });
            }

        }


        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();

    }


    @Override
    public void onStop() {
        super.onStop();
        if (simpleExoPlayer != null) {
            curPosition = simpleExoPlayer.getCurrentPosition();
            simpleExoPlayer.stop();
        }
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.seekTo(curPosition);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }


}
