package com.lxj.xpopup.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.lxj.xpopup.photoview.OnMatrixChangedListener;
import com.lxj.xpopup.photoview.PhotoView;

import java.io.File;

/**
 * 支持加载超长，超大的图片，你能OOM就算我输！！！
 * 注意：默认不支持超大超长图片加载，如8000x10000，传入bigImage为true时则使用SubsamplingScaleImageView加载大图；
 * SubsamplingScaleImageView虽然能加载超大图，但是不支持GIF
 */
public class SmartGlideImageLoader implements XPopupImageLoader {
    private int errImg;
    private boolean mBigImage;

    public SmartGlideImageLoader() {
    }

    public SmartGlideImageLoader(int errImgRes) {
        errImg = errImgRes;
    }

    public SmartGlideImageLoader(boolean bigImage, int errImgRes) {
        this(errImgRes);
        mBigImage = bigImage;
    }

    @Override
    public View loadImage(final int position, @NonNull final Object url, @NonNull ImageViewerPopupView popupView,
                          @NonNull final PhotoView snapshot, @NonNull final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        final View imageView = mBigImage ? buildBigImageView(popupView, progressBar, position)
                : buildPhotoView(popupView, snapshot, position);
        //支持超大图片，超长图片的加载
        final Context context = imageView.getContext();
        if (snapshot.getDrawable() != null && ((int)snapshot.getTag())==position ) {
            if (imageView instanceof PhotoView) {
                try {
                    ((PhotoView) imageView).setImageDrawable(snapshot.getDrawable().getConstantState().newDrawable());
                } catch (Exception e) { }
            } else {
                ((SubsamplingScaleImageView) imageView).setImage(ImageSource.bitmap(XPopupUtils.view2Bitmap(snapshot)));
            }
        }
        Glide.with(imageView).downloadOnly().load(url)
                .into(new ImageDownloadTarget() {
                    @Override
                    public void onLoadFailed(Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        progressBar.setVisibility(View.GONE);
                        if (imageView instanceof PhotoView) {
                            ((PhotoView) imageView).setImageResource(errImg);
                            ((PhotoView) imageView).setZoomable(false);
                        } else {
                            ((SubsamplingScaleImageView) imageView).setImage(ImageSource.resource(errImg));
                        }
                    }

                    @Override
                    public void onResourceReady(@NonNull File resource, Transition<? super File> transition) {
                        super.onResourceReady(resource, transition);
                        int maxW = XPopupUtils.getWindowWidth(context) * 2;
                        int maxH = XPopupUtils.getScreenHeight(context) * 2;

                        int[] size = XPopupUtils.getImageSize(resource);
                        //photo view加载
                        if (imageView instanceof PhotoView) {
                            progressBar.setVisibility(View.GONE);
                            ((PhotoView) imageView).setZoomable(true);
                            if (size[0] > maxW || size[1] > maxH) {
                                //TODO: 可能导致大图GIF展示不出来
                                ((PhotoView) imageView).setImageBitmap(XPopupUtils.getBitmap(resource, maxW, maxH));
                            } else {
                                Glide.with(imageView).load(resource).apply(new RequestOptions().error(errImg).override(size[0], size[1])).into(((PhotoView) imageView));
                            }
                        } else {
                            //大图加载
                            SubsamplingScaleImageView bigImageView = (SubsamplingScaleImageView) imageView;
                            if (size[1] * 1f / size[0] > XPopupUtils.getScreenHeight(context) * 1f / XPopupUtils.getWindowWidth(context)) {
//                                bigImageView.setScaleAndCenter(0.1f, new PointF(size[0]/2f, 0));
                                bigImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_START);
                            } else {
                                bigImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
                            }
                            bigImageView.setMaxScale(10f);
                            bigImageView.setDoubleTapZoomScale(3f);
                            bigImageView.setOnImageEventListener(new SSIVListener(bigImageView, progressBar, errImg));
                            Bitmap preview = XPopupUtils.getBitmap(resource, XPopupUtils.getWindowWidth(context), XPopupUtils.getScreenHeight(context));
                            bigImageView.setImage(ImageSource.uri(Uri.fromFile(resource)).dimensions(size[0], size[1]),
                                    ImageSource.cachedBitmap(preview));
                        }
                    }
                });
        return imageView;
    }

    private SubsamplingScaleImageView buildBigImageView(final ImageViewerPopupView popupView, ProgressBar progressBar, final int realPosition) {
        final SubsamplingScaleImageView ssiv = new SubsamplingScaleImageView(popupView.getContext());
        ssiv.setOnStateChangedListener(new SubsamplingScaleImageView.DefaultOnStateChangedListener() {
            @Override
            public void onCenterChanged(PointF newCenter, int origin) {
                super.onCenterChanged(newCenter, origin);
                //TODO 同步SubsamplingScaleImageView的滚动给snapshot
//                    Log.e("tag", "y: " + newCenter.y   + " vh: "+ ssiv.getMeasuredHeight()
//                    + "  dy: "+ (newCenter.y - ssiv.getMeasuredHeight()/2));
            }
        });
        ssiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupView.dismiss();
            }
        });
        if (popupView.longPressListener != null) {
            ssiv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    popupView.longPressListener.onLongPressed(popupView, realPosition);
                    return false;
                }
            });
        }
        return ssiv;
    }

    private PhotoView buildPhotoView(final ImageViewerPopupView popupView, final PhotoView snapshotView, final int realPosition) {
        final PhotoView photoView = new PhotoView(popupView.getContext());
        photoView.setZoomable(false);
        photoView.setOnMatrixChangeListener(new OnMatrixChangedListener() {
            @Override
            public void onMatrixChanged(RectF rect) {
                if (snapshotView != null) {
                    Matrix matrix = new Matrix();
                    photoView.getSuppMatrix(matrix);
                    snapshotView.setSuppMatrix(matrix);
                }
            }
        });
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupView.dismiss();
            }
        });
        if (popupView.longPressListener != null) {
            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    popupView.longPressListener.onLongPressed(popupView, realPosition);
                    return false;
                }
            });
        }
        return photoView;
    }

    @Override
    public void loadSnapshot(@NonNull Object uri, @NonNull final PhotoView snapshot) {
        Glide.with(snapshot).downloadOnly().load(uri)
                .into(new ImageDownloadTarget() {
                    @Override
                    public void onLoadFailed(Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }

                    @Override
                    public void onResourceReady(@NonNull File resource, Transition<? super File> transition) {
                        super.onResourceReady(resource, transition);
                        int maxW = XPopupUtils.getWindowWidth(snapshot.getContext());
                        int maxH = XPopupUtils.getScreenHeight(snapshot.getContext());
                        int[] size = XPopupUtils.getImageSize(resource);
                        if (size[0] > maxW || size[1] > maxH) {
                            //缩放加载
                            snapshot.setImageBitmap(XPopupUtils.getBitmap(resource, maxW, maxH));
                        } else {
                            Glide.with(snapshot).load(resource).apply(new RequestOptions().override(size[0], size[1])).into(snapshot);
                        }
                    }
                });
    }


    @Override
    public File getImageFile(@NonNull Context context, @NonNull Object uri) {
        try {
            return Glide.with(context).downloadOnly().load(uri).submit().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void destroy(int position, @NonNull Object object) {
        if (object instanceof SubsamplingScaleImageView) {
            SubsamplingScaleImageView ssiv = (SubsamplingScaleImageView) object;
//            ssiv.recycle();
        }

    }
}