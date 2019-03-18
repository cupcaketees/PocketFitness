package uk.ac.tees.cupcake.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.posts.SquareImagePostView;

public class GridImageAdapter extends ArrayAdapter<String> {

    private LayoutInflater mLayoutInflater;

    private int resourceLayout;
    
    private String mAppend;

    public GridImageAdapter(Context context, int layoutResource, String append, List<String> imgURLs) {
        super(context, layoutResource, imgURLs);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resourceLayout = layoutResource;
        this.mAppend = append;
    }

    private static class ViewHolder {
        SquareImagePostView imageView;
        ProgressBar mProgress;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(resourceLayout, parent, false);
            holder = new ViewHolder();
            holder.mProgress = convertView.findViewById(R.id.gridProgressBar);
            holder.imageView = convertView.findViewById(R.id.gridImageView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String imgURL = getItem(position);

        ImageLoader imageLoader = ImageLoader.getInstance();
        
        DisplayImageOptions options = new DisplayImageOptions
                .Builder()
                .cacheInMemory(true)
                .resetViewBeforeLoading(true)
                .build();
        
        imageLoader.displayImage(mAppend + imgURL, holder.imageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (holder.imageView != null) {
                    holder.imageView.setVisibility(View.INVISIBLE);
                }
                if (holder.mProgress != null) {
                    holder.mProgress.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (holder.mProgress != null) {
                    holder.mProgress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (holder.imageView != null) {
                    holder.imageView.setVisibility(View.VISIBLE);
                }
                if (holder.mProgress != null) {
                    holder.mProgress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if (holder.mProgress != null) {
                    holder.mProgress.setVisibility(View.GONE);
                }
            }
        });

        return convertView;
    }
}
