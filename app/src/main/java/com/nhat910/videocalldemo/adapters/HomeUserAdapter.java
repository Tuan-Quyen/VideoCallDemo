package com.nhat910.videocalldemo.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhat910.videocalldemo.R;
import com.nhat910.videocalldemo.holder.HomeUserHolder;
import com.nhat910.videocalldemo.interfaces.ItemClickListener;
import com.nhat910.videocalldemo.utils.AppUtils;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.users.model.QBUser;

import java.util.List;

public class HomeUserAdapter extends RecyclerView.Adapter<HomeUserHolder> {
    private List<QBUser> _data;
    private List<QBChatDialog> _dataDialog;
    private ItemClickListener.OnItemClick listener;

    public void setListener(ItemClickListener.OnItemClick listener) {
        this.listener = listener;
    }

    public HomeUserAdapter(List<QBUser> _data, List<QBChatDialog> _dataDialog) {
        this._data = _data;
        this._dataDialog = _dataDialog;
    }

    @NonNull
    @Override
    public HomeUserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_home, viewGroup, false);
        return new HomeUserHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeUserHolder homeUserHolder, int i) {
        homeUserHolder.tvUser.setText(_data.get(i).getFullName());
        homeUserHolder.imgName.setText(String.valueOf(_data.get(i).getFullName().charAt(0)).toUpperCase());
        setLastMessage(_data.get(i), homeUserHolder);
        homeUserHolder.clBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(homeUserHolder.getAdapterPosition());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setLastMessage(QBUser qbUser, HomeUserHolder homeUserHolder) {
        for (int i = 0; i < _dataDialog.size(); i++) {
            for (int j = 0; j < _dataDialog.get(i).getOccupants().size(); j++) {
                if (_dataDialog.get(i).getOccupants().get(j).equals(qbUser.getId()) && _dataDialog.get(i).getLastMessage() != null) {
                    homeUserHolder.tvLastMessage.setText(_dataDialog.get(i).getLastMessage() + " - " +
                            AppUtils.convertTime(homeUserHolder.itemView.getContext(), _dataDialog.get(i).getLastMessageDateSent()));
                    if (_dataDialog.get(i).getUnreadMessageCount() != 0) {
                        homeUserHolder.frUnread.setVisibility(View.VISIBLE);
                        homeUserHolder.tvUnread.setText(String.valueOf(_dataDialog.get(i).getUnreadMessageCount()));
                    } else {
                        homeUserHolder.frUnread.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    /*private void isOnline(QBUser qbUser, View view) {
        QBChatService.getInstance().getPingManager().setPingInterval(1);
        Performer<QBUser> qbUserPerformer = QBUsers.getUser(qbUser.getId());
        qbUserPerformer.performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                final QBPingManager pingManager = QBChatService.getInstance().getPingManager();
                pingManager.pingServer(new QBEntityCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid, Bundle bundle) {
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        view.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                view.setVisibility(View.GONE);
            }
        });
    }*/

    @Override
    public int getItemCount() {
        return _data != null ? _data.size() : 0;
    }
}
