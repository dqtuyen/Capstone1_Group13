package APIChatGPT.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.capstone1.R;

import java.util.List;

import APIChatGPT.model.MessageData;
import APIChatGPT.model.TypeTalking;
import APIChatGPT.util.AppUtil;


public class MessageDataAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context context;
    private List<MessageData> messageDataList;

    public MessageDataAdapter(Context context, List<MessageData> messageList) {
        this.context = context;
        this.messageDataList = messageList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chatbox_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chatbox_message_received, parent, false);
            ImageView imageView=view.findViewById(R.id.imgAvatar);

            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageData message = messageDataList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageDataList != null ? messageDataList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        MessageData message =  messageDataList.get(position);

        if (message.getTypeTalking()== TypeTalking.HUMAN) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }
    //Lớp này dùng để hiển thị giao diện và dữ liệu do người dùng hỏi OpenAI
    class SentMessageHolder extends RecyclerView.ViewHolder {
        private View view;
        TextView txtPrompt;
        TextView txtSendTime;

        SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txtPrompt=view.findViewById(R.id.txtPrompt);
            txtSendTime=view.findViewById(R.id.txtSendTime);
        }

        void bind(MessageData message) {
            txtPrompt.setText(message.getText());

            // Format the stored timestamp into a readable String using method.
            txtSendTime.setText(AppUtil.getTimeFormat(message.getCreated()));
        }
    }
    //Lớp này dùng để hiển thị giao diện và dữ liệu khi nhận được kết quả trả về từ OpenAI
    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        private View view;
        TextView txtName;
        TextView txtText;
        TextView txtCreated;

        ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txtName=view.findViewById(R.id.txtName);

            txtText=view.findViewById(R.id.txtText);

            txtCreated=view.findViewById(R.id.txtCreated);
        }

        void bind(MessageData message) {
            txtName.setText(message.getUserName());
            txtText.setText(message.getText());
            txtCreated.setText(AppUtil.getTimeFormat(message.getCreated()));
        }
    }
}