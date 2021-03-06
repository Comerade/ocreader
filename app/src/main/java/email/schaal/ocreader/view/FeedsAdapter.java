package email.schaal.ocreader.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import email.schaal.ocreader.database.model.Feed;
import email.schaal.ocreader.databinding.ListFeedBinding;
import email.schaal.ocreader.util.FaviconLoader;
import email.schaal.ocreader.util.FeedColors;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

import static email.schaal.ocreader.view.ItemsAdapter.VIEW_TYPE_ITEM;
import static email.schaal.ocreader.view.ItemsAdapter.VIEW_TYPE_LAST_ITEM;

/**
 * RecyclerView Adapter for Feeds
 */
public class FeedsAdapter extends RealmRecyclerViewAdapter<Feed, RecyclerView.ViewHolder> {
    private final FeedManageListener listener;

    public FeedsAdapter(Context context, Realm realm, FeedManageListener listener) {
        super(context, realm.where(Feed.class).findAllSorted(Feed.NAME), true);
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ITEM:
            case VIEW_TYPE_LAST_ITEM:
                ListFeedBinding binding = ListFeedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new FeedViewHolder(binding, listener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof FeedViewHolder) {
            Feed feed = getItem(position);
            ((FeedViewHolder)holder).bindFeed(feed);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }

    /**
     * ViewHolder displaying a Feed
     */
    private static class FeedViewHolder extends RecyclerView.ViewHolder implements FaviconLoader.FeedColorsListener {
        private final ListFeedBinding binding;
        private final FeedManageListener listener;

        FeedViewHolder(ListFeedBinding binding, FeedManageListener listener) {
            super(binding.getRoot());

            this.binding = binding;
            this.listener = listener;
        }

        void bindFeed(final Feed feed) {
            binding.textViewTitle.setText(feed.getName());

            binding.textViewFolder.setText(feed.getFolderTitle(itemView.getContext()));

            binding.deleteFeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    listener.deleteFeed(feed);
                }
            });

            if(feed.isConsideredFailed()) {
                binding.feedFailure.setVisibility(View.VISIBLE);
                binding.feedFailure.setText(feed.getLastUpdateError());
            } else {
                binding.feedFailure.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.showFeedDialog(feed);
                }
            });
            new FaviconLoader.Builder(binding.imageviewFavicon, feed).build().load(itemView.getContext(), this);
        }

        @Override
        public void onGenerated(@NonNull FeedColors feedColors) {

        }

        @Override
        public void onStart() {

        }
     }
}
