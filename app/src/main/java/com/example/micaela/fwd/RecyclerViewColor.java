/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.micaela.fwd;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

public class RecyclerViewColor {

    public static int getViewHolderBackgroundColorFromInstance(Context context, int instanceNum) {

        int pos = (int) instanceNum % 2;


        switch (pos) {
            case 0:
                return ContextCompat.getColor(context, R.color.colorPrimaryLight);
            case 1:
                return ContextCompat.getColor(context, R.color.colorPrimaryDark);
            default:
                return Color.WHITE;
        }
    }
}
