package com.magally.michiganmain.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.magally.michiganmain.OtherUser;
import com.magally.michiganmain.Pregunta;
import com.magally.michiganmain.R;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.photoview.PhotoViewAttacher;


class CustomAdapter extends ArrayAdapter<Pregunta> {



    private Context mContext;
    private Pregunta pregunta;
    private static final String URL_REPUTATION = "http://192.168.1.113/michigan_server/question_rep.php";
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;


    CustomAdapter(Context context, Pregunta[] preguntas) {
        super(context, R.layout.feed_row,preguntas);
        mContext = context;
        mShortAnimationDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
    }


    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        LayoutInflater rowInflater =LayoutInflater.from(getContext());
        View customView = rowInflater.inflate(R.layout.feed_row, parent, false);
        ViewHolder v = new ViewHolder(customView);
        final Pregunta singlePregunta = getItem(position);
        final View rootView = parent.getChildAt(0);

        v.enunciadoTextView.setText(singlePregunta.getEnunciado());
        Log.d("GetFeedTask", singlePregunta.getFoto());

            Picasso.with(getContext())
                    .load(singlePregunta.getFoto())
                    .placeholder(R.drawable.silhouette)
                    .error(R.drawable.prueba)
                    .fit()
                    .centerCrop()
                    .into((ImageView)customView.findViewById(R.id.preguntaImg));

        v.preguntaImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(v,singlePregunta.getFoto(),parent);
            }
        });

        v.usuarioTextView.setText(singlePregunta.getUsername());
        v.repTV.setText(singlePregunta.getReputacion());
        v.numRespuestaTextView.setText(singlePregunta.getRespuestaCount()+ " Respuestas");
        v.repTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Asociar intent con la pregunta para sacar las respuestas que son
                Intent answersI = new Intent(getContext(), Answers.class);

                answersI.putExtra("pregunta_id",singlePregunta.getPreguntaID());
                mContext.startActivity(answersI);
            }
        });

        v.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getContext(),"+1",Toast.LENGTH_SHORT).show();
                updateRep("up",singlePregunta);
            }
        });

        v.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRep("down",singlePregunta);
                //Toast.makeText(getContext(), "-1", Toast.LENGTH_SHORT).show();
            }
        });

        v.usuarioTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Asociar intent con el usuario que es
                Intent otherUI = new Intent(getContext(), OtherUser.class);
                otherUI.putExtra("usuario",singlePregunta.getUsername());
                mContext.startActivity(otherUI);
                Toast.makeText(getContext(),"usuario",Toast.LENGTH_LONG).show();

            }
        });
        v.numRespuestaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Answers.class);
                intent.putExtra("pregunta_id", singlePregunta.getPreguntaID());
                mContext.startActivity(intent);
            }
        });
        return customView;

    }


    public static class ViewHolder{
        public final ImageView preguntaImg;
        public final TextView  enunciadoTextView, usuarioTextView, numRespuestaTextView, repTV;
        public final Button plusButton, minusButton;

        public ViewHolder(View view){
            preguntaImg = (ImageView)view.findViewById(R.id.preguntaImg);
            enunciadoTextView = (TextView)view.findViewById(R.id.enunciadoFeed);
            usuarioTextView = (TextView)view.findViewById(R.id.qUserTV);
            numRespuestaTextView = (TextView)view.findViewById(R.id.numRespuestasTV);
            plusButton = (Button)view.findViewById(R.id.plusBtn);
            minusButton = (Button)view.findViewById(R.id.minusBtn);
            repTV = (TextView)view.findViewById(R.id.repTV);


        }

    }

    private void updateRep(final String rep, Pregunta question){
        Log.d("CustomAdapter","in updateRep Method");
        this.pregunta = question;

        RequestParams params = new RequestParams();
        params.put("uid",pregunta.getUsername());
        params.put("qid",pregunta.getPreguntaID());
        if (rep.equals("up")){
            params.put("reputation","up");
        } else{
            params.put("reputation","down");
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(URL_REPUTATION,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try{
                    if(response.getInt("success")==1){
                        long reputacion = Long.valueOf(pregunta.getReputacion());
                        if(rep.equals("up")) {

                            pregunta.setReputacion(String.valueOf(++reputacion));
                            notifyDataSetChanged();
                        }else{
                            pregunta.setReputacion(String.valueOf(--reputacion));
                            notifyDataSetChanged();
                        }
                        //Toast.makeText(getContext(), "Reputacion actualizada", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getContext(), "No se pudo actualizar en estos momentos", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void zoomImageFromThumb(final View thumbView, String imageUri, ViewGroup parent) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        PhotoViewAttacher mAttacher;
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }
        View rootView = ((Activity)getContext()).findViewById(R.id.feed_container);
        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) rootView.findViewById(
                R.id.expanded_image);


        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);

        rootView.findViewById(R.id.feed_container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }
        mAttacher = new PhotoViewAttacher(expandedImageView);
        mAttacher.setAllowParentInterceptOnEdge(false);
        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

       Picasso.with(getContext()).invalidate(imageUri); // Delete thumbnail

        Picasso.with(getContext())  //Redownload full image
                .load(imageUri)
                .fit()
                .into(expandedImageView);
        //mAttacher.update();
        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.

        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }


}
