/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Piasy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.lxj.xpopup.util;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Util;

import java.io.File;

public abstract class ImageDownloadTarget implements Target<File> {

    private Request request;

    private final int width;
    private final int height;

    protected ImageDownloadTarget() {
        this(SIZE_ORIGINAL, SIZE_ORIGINAL);
    }

    private ImageDownloadTarget(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void onResourceReady(@NonNull File resource, Transition<? super File> transition) {
    }

    @Override
    public void onLoadCleared(Drawable placeholder) {
    }

    @Override
    public void onLoadStarted(Drawable placeholder) {
    }

    @Override
    public void onLoadFailed(Drawable errorDrawable) {
    }

    /**
     * Immediately calls the given callback with the sizes given in the constructor.
     *
     * @param cb {@inheritDoc}
     */
    @Override
    public final void getSize(@NonNull SizeReadyCallback cb) {
        if (!Util.isValidDimensions(width, height)) {
            throw new IllegalArgumentException(
                    "Width and height must both be > 0 or Target#SIZE_ORIGINAL, but given" + " width: "
                            + width + " and height: " + height + ", either provide dimensions in the constructor"
                            + " or call override()");
        }
        cb.onSizeReady(width, height);
    }

    @Override
    public void removeCallback(@NonNull SizeReadyCallback cb) {
        // Do nothing, we never retain a reference to the callback.
    }

    @Override
    public void setRequest(@Nullable Request request) {
        this.request = request;
    }

    @Override
    @Nullable
    public Request getRequest() {
        return request;
    }

    @Override
    public void onStart() {
        // Do nothing.
    }

    @Override
    public void onStop() {
        // Do nothing.
    }

    @Override
    public void onDestroy() {
        // Do nothing.
    }
}
