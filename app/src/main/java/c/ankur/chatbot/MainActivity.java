package c.ankur.chatbot;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.getstream.sdk.chat.StreamChat;
import com.getstream.sdk.chat.enums.FilterObject;
import com.getstream.sdk.chat.rest.User;
import com.getstream.sdk.chat.rest.core.Client;
import com.getstream.sdk.chat.viewmodel.ChannelListViewModel;

import java.util.HashMap;

import c.ankur.chatbot.databinding.ActivityMainBinding;

import static com.getstream.sdk.chat.enums.Filters.and;
import static com.getstream.sdk.chat.enums.Filters.eq;
import static com.getstream.sdk.chat.enums.Filters.in;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_CHANNEL_TYPE = "com.example.chattutorial.CHANNEL_TYPE";
    public static final String EXTRA_CHANNEL_ID = "com.example.chattutorial.CHANNEL_ID";


    @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            // setup the client using the example API key
            // normal you would call init in your Application class and not the activity
            StreamChat.init("b67pax5b2wdq", this.getApplicationContext());
            Client client = StreamChat.getInstance(this.getApplication());
            HashMap<String, Object> extraData = new HashMap<>();
            extraData.put("name", "Paranoid Android");
            extraData.put("image", "https://bit.ly/2TIt8NR");
            User currentUser = new User("muddy-king-8", extraData);
            // User token is typically provided by your server when the user authenticates
            client.setUser(currentUser, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoibXVkZHkta2luZy04In0.4DvY7fe9Ajyy-UFd4dQe0yabPNwkCdSyODTcmcrDIls");

            // we're using data binding in this example
            ActivityMainBinding binding =
                    DataBindingUtil.setContentView(this, R.layout.activity_main);
            // Specify the current activity as the lifecycle owner.
            binding.setLifecycleOwner(this);

            // most the business logic for chat is handled in the ChannelListViewModel view model
            ChannelListViewModel viewModel = ViewModelProviders.of(this).get(ChannelListViewModel.class);
            binding.setViewModel(viewModel);
            binding.channelList.setViewModel(viewModel, this);

            // query all channels of type messaging
            FilterObject filter = and(eq("type", "messaging"), in("members", "muddy-king-8"));
            viewModel.setChannelFilter(filter);

            // click handlers for clicking a user avatar or channel
            binding.channelList.setOnChannelClickListener(channel -> {
                // open the channel activity
                Intent intent = new Intent(MainActivity.this, ChannelActivity.class);
                intent.putExtra(EXTRA_CHANNEL_TYPE, channel.getType());
                intent.putExtra(EXTRA_CHANNEL_ID, channel.getId());
                startActivity(intent);

            });
            binding.channelList.setOnUserClickListener(user -> {
                // open your user profile
            });

        }
    }
