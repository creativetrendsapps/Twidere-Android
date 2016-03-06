/*
 * Twidere - Twitter client for Android
 *
 *  Copyright (C) 2012-2014 Mariotaku Lee <mariotaku.lee@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mariotaku.twidere.fragment.support;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;

import org.mariotaku.twidere.loader.support.TweetSearchLoader;
import org.mariotaku.twidere.model.AccountKey;
import org.mariotaku.twidere.model.ParcelableStatus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import edu.tsinghua.hotmobi.model.TimelineType;

/**
 * Created by mariotaku on 14/12/2.
 */
public class StatusesSearchFragment extends ParcelableStatusesFragment {

    @NonNull
    @Override
    @TimelineType
    protected String getTimelineType() {
        return TimelineType.SEARCH;
    }

    @Override
    protected Loader<List<ParcelableStatus>> onCreateStatusesLoader(final Context context,
                                                                    final Bundle args,
                                                                    final boolean fromUser) {
        setRefreshing(true);
        final AccountKey accountKey = args.getParcelable(EXTRA_ACCOUNT_KEY);
        final long maxId = args.getLong(EXTRA_MAX_ID, -1);
        final long sinceId = args.getLong(EXTRA_SINCE_ID, -1);
        final String query = args.getString(EXTRA_QUERY);
        final int tabPosition = args.getInt(EXTRA_TAB_POSITION, -1);
        final boolean makeGap = args.getBoolean(EXTRA_MAKE_GAP, true);
        if (query == null) throw new NullPointerException();
        return new TweetSearchLoader(getActivity(), accountKey, query, sinceId, maxId, getAdapterData(),
                getSavedStatusesFileArgs(), tabPosition, fromUser, makeGap);
    }

    @Override
    protected void fitSystemWindows(Rect insets) {
        super.fitSystemWindows(insets);
    }

    @Override
    protected String[] getSavedStatusesFileArgs() {
        final Bundle args = getArguments();
        if (args == null) return null;
        final long account_id = args.getLong(EXTRA_ACCOUNT_ID, -1);
        final String query = args.getString(EXTRA_QUERY);
        return new String[]{AUTHORITY_SEARCH_TWEETS, "account" + account_id, "query" + query};
    }


    @Override
    protected String getReadPositionTagWithArguments() {
        final Bundle args = getArguments();
        assert args != null;
        final int tabPosition = args.getInt(EXTRA_TAB_POSITION, -1);
        StringBuilder sb = new StringBuilder("search_");
        if (tabPosition < 0) return null;
        final String query = args.getString(EXTRA_QUERY);
        if (query == null) return null;
        final String encodedQuery;
        try {
            encodedQuery = URLEncoder.encode(query, "UTF-8").replaceAll("[^\\w\\d]", "_");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        sb.append(encodedQuery);
        return sb.toString();
    }

}
