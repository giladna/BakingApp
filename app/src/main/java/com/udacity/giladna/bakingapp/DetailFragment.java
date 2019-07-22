package com.udacity.giladna.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.giladna.bakingapp.model.Ingredient;
import com.udacity.giladna.bakingapp.model.Step;

import java.util.ArrayList;

import static com.udacity.giladna.bakingapp.DetailActivity.INTENT_INGREDIENTS;

public class DetailFragment extends Fragment {

    public static String INTENT_STEP = "step";
    public static String INTENT_POSITION = "stepNumber";
    public static String INTENT_STEP_LIST = "stepsList";
    public static String INTENT_TWO_PANE = "twoPane";


    private static final String LAST_POSITION = "last_position";
    private static final String IS_PLAYING = "is_playing";
    private static final String STEP_NUMBER = "step_number";

    private Step step;
    private SimpleExoPlayer player;
    PlayerView playerView;
    private long lastPosition;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> stepsList;
    private int stepNumber;
    private boolean isPlaying;
    private boolean twoPane = false;
    long curPosition;
    MediaSource mediaSource;

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stepNumber = -1;

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LAST_POSITION)) {
                lastPosition = savedInstanceState.getLong(LAST_POSITION);
            }

            if (savedInstanceState.containsKey(IS_PLAYING)) {
                isPlaying = savedInstanceState.getBoolean(IS_PLAYING);
            }

            if (savedInstanceState.containsKey(STEP_NUMBER)) {
                stepNumber = savedInstanceState.getInt(STEP_NUMBER);
            }
        }

        step = getArguments().getParcelable(INTENT_STEP);
        ingredients = getArguments().getParcelableArrayList(INTENT_INGREDIENTS);
        stepsList = getArguments().getParcelableArrayList(INTENT_STEP_LIST);
        stepNumber = getArguments().getInt(INTENT_POSITION);
        twoPane = getArguments().getBoolean(INTENT_TWO_PANE);
    }

    private void initializePlayer() {
        if (step != null) {
            if (player == null) {
                player = ExoPlayerFactory.newSimpleInstance(getActivity(), new DefaultTrackSelector());
                playerView.setPlayer(player);
            }
            playerView.setVisibility(View.VISIBLE);
            mediaSource = buildMediaSource(Uri.parse(step.getVideoURL()), new DefaultDataSourceFactory(getContext(), "User-Agent-BakingApp"));

            player.prepare(mediaSource);
            player.seekTo(lastPosition);
            player.setPlayWhenReady(true);
            isPlaying = true;
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
        playerView = rootView.findViewById(R.id.playerView);
        initializePlayer();
        if (ingredients != null) {
            (rootView.findViewById(R.id.recipe_detail)).setVisibility(View.GONE);
            playerView.setVisibility(View.GONE);
            int i = 0;
            populateReceipeDesc(rootView, i);
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
                        .placeholder(R.drawable.placeholder)
                        .into(imageView);

            } else {
                rootView.findViewById(R.id.no_thumbnail_tv).setVisibility(View.VISIBLE);
            }

            if (step.getVideoURL() != null && !step.getVideoURL().isEmpty()) {
                isPlaying = true;
                playerView.setPlayer(player);
            } else {
                isPlaying = false;
                playerView.setVisibility(View.GONE);
            }
            Button buttonNext = rootView.findViewById(R.id.next_step_btn);
            Button buttonPrev = rootView.findViewById(R.id.prev_step_btn);
            if (twoPane) {
                buttonNext.setVisibility(View.GONE);
                buttonPrev.setVisibility(View.GONE);

            } else {
                setButtonsVisibility(buttonNext, buttonPrev);
                buttonNext.setOnClickListener(v -> {
                    stopExoplayer();
                    stepNumber++;
                    handleButtonClick(rootView, thumbnailUrl, buttonNext, buttonPrev);
                });

                buttonPrev.setOnClickListener(v -> {
                    stopExoplayer();
                    stepNumber--;


                    handleButtonClick(rootView, thumbnailUrl, buttonNext, buttonPrev);
                });
            }

        }


        return rootView;
    }

    private void populateReceipeDesc(View rootView, int i) {
        for (Ingredient ingredient : ingredients) {
            String count = getColoredSpanned(++i + ") ", getString(R.string.indexColor));
            ((TextView) rootView.findViewById(R.id.recipe_desc_tv)).append(Html.fromHtml(count));
            ((TextView) rootView.findViewById(R.id.recipe_desc_tv)).append(Html.fromHtml(getColoredSpanned(ingredient.getQuantity() + " " + ingredient.getMeasure() + " ",  getString(R.string.quantityColor))));
            ((TextView) rootView.findViewById(R.id.recipe_desc_tv)).append(ingredient.getIngredient().substring(0, 1).toUpperCase() + ingredient.getIngredient().substring(1) + " \n\n");
        }
    }

    private void stopExoplayer() {
        if (player != null && isPlaying) {
            isPlaying = false;
            lastPosition = 0;
            player.stop();
        }
    }

    private void handleButtonClick(View rootView, String thumbnailUrl, Button buttonNext, Button buttonPrev) {
        setButtonsVisibility(buttonNext, buttonPrev);

        Step currStep = stepsList.get(stepNumber);
        step = currStep;
        String currStepThumbnailUrl = currStep.getThumbnailURL();
        if (!TextUtils.isEmpty(currStepThumbnailUrl) && (currStepThumbnailUrl.contains(".jpeg") || currStepThumbnailUrl.contains(".jpg") || currStepThumbnailUrl.contains("png"))) {

            ImageView imageView = rootView.findViewById(R.id.thumbnail_img);
            imageView.setVisibility(View.VISIBLE);

            Picasso.with(getContext())
                    .load(thumbnailUrl)
                    .error(R.drawable.error)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);

        } else {
            rootView.findViewById(R.id.no_thumbnail_tv).setVisibility(View.VISIBLE);
        }

        ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(currStep.getShortDescription());
        ((TextView) rootView.findViewById(R.id.recipe_desc_tv)).setText(currStep.getDescription());

        if (!TextUtils.isEmpty(currStep.getVideoURL())) {
            initializePlayer();
//            if (player == null) {
//                player = ExoPlayerFactory.newSimpleInstance(getActivity(), new DefaultTrackSelector());
//            }
//            mediaSource = buildMediaSource(Uri.parse(currStep.getVideoURL()), new DefaultDataSourceFactory(getContext(), "User-Agent-BakingApp"));
//            player.prepare(mediaSource);
//            player.setPlayWhenReady(true);

        } else {
            isPlaying = false;
            playerView.setVisibility(View.GONE);
        }
    }

    private void setButtonsVisibility(Button buttonNext, Button buttonPrev) {
        if (stepNumber == 0) {
            buttonNext.setVisibility(View.VISIBLE);
            buttonPrev.setVisibility(View.GONE);
        } else
        if (stepNumber + 1 == stepsList.size()) {
            buttonNext.setVisibility(View.GONE);
            buttonPrev.setVisibility(View.VISIBLE);
        } else {
            buttonNext.setVisibility(View.VISIBLE);
            buttonPrev.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();

    }

    private void releasePlayer() {
        if (player != null) {
            lastPosition = player.getCurrentPosition();
            isPlaying = player.getPlayWhenReady();

            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(LAST_POSITION, lastPosition);
        outState.putBoolean(IS_PLAYING, isPlaying);
        outState.putInt(STEP_NUMBER, stepNumber);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
    }
}
