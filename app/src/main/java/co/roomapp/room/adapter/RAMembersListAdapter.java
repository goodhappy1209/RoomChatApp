package co.roomapp.room.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SearchView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import co.roomapp.room.R;
import java.util.ArrayList;

import co.roomapp.room.activity.RARoomsActivity;
import co.roomapp.room.activity.RATimelineActivity;
import co.roomapp.room.widget.RARoundImageView;
import co.stickylistheaders.StickyListHeadersAdapter;
import co.roomapp.room.model.RAMember;
import android.widget.ImageView;

public class RAMembersListAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {

    private final Context mContext;
    private ArrayList<RAMember> mMembers;
    private ArrayList<RAMember> mInvitedGuests;
    private int[] mSectionIndices;
    private String[] mSectionLetters;
    private LayoutInflater mInflater;
    private ArrayList<RAMember> m_onlineMembers;
    private ArrayList<RAMember> m_offlineMembers;

    public RAMembersListAdapter(Context context, ArrayList<RAMember> data, ArrayList<RAMember> invitedGuests) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mMembers = data;
        mInvitedGuests = invitedGuests;
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionTitles();
    }

    public void setMembers(ArrayList<RAMember> membersList, ArrayList<RAMember> invitedGuests) {

        this.mMembers = membersList;
        this.mInvitedGuests = invitedGuests;
        getSectionIndices();
        notifyDataSetChanged();
    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        m_onlineMembers = new ArrayList<RAMember>();
        m_offlineMembers = new ArrayList<RAMember>();

        sectionIndices.add(0);
        sectionIndices.add(1);
        int onlineCount = 0;
        int offlineCount = 0;
        for (int i = 0; i < mMembers.size(); i ++) {
            RAMember member = mMembers.get(i);
            if(member.getStatus().equals("online")) {
                m_onlineMembers.add(member);
                onlineCount ++;
            }
            else {
                m_offlineMembers.add(member);
                offlineCount ++;
            }
        }
        mMembers.clear();
        for (int i = 0; i < m_onlineMembers.size(); i ++)
            mMembers.add(m_onlineMembers.get(i));
        for (int i = 0; i < m_offlineMembers.size(); i ++)
            mMembers.add(m_offlineMembers.get(i));

        if(onlineCount > 0)
            sectionIndices.add(onlineCount+1);
        else
            sectionIndices.add(onlineCount+2);

        if((onlineCount > 0) && (offlineCount > 0))
            sectionIndices.add(mMembers.size()+1);
        else if((onlineCount > 0) && (offlineCount == 0))
            sectionIndices.add(onlineCount + 2);
        else if((onlineCount == 0) && (offlineCount > 0))
            sectionIndices.add(offlineCount + 2);
        else sectionIndices.add(3);

        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }

        return sections;
    }

    private String[] getSectionTitles() {
        String[] sectionTitles = new String[4];
        sectionTitles[0] = "search";
        sectionTitles[1] = "online";
        sectionTitles[2] = "offline";
        sectionTitles[3] = "Pending Invited Guests";
        return sectionTitles;
    }

    @Override
    public int getCount() {

        if((m_onlineMembers.size()>0)&&(m_offlineMembers.size()>0)&&(mInvitedGuests.size()>0))
            return m_offlineMembers.size()+m_onlineMembers.size()+mInvitedGuests.size()+1;
        if((m_onlineMembers.size()>0)&&(m_offlineMembers.size()==0)&&(mInvitedGuests.size()>0))
            return m_onlineMembers.size()+mInvitedGuests.size()+2;
        if((m_onlineMembers.size()==0)&&(m_offlineMembers.size()>0)&&(mInvitedGuests.size()>0))
            return m_offlineMembers.size()+mInvitedGuests.size()+2;
        if((m_onlineMembers.size()==0)&&(m_offlineMembers.size()==0)&&(mInvitedGuests.size()>0))
            return mInvitedGuests.size()+3;
        if((m_onlineMembers.size()>0)&&(m_offlineMembers.size()>0)&&(mInvitedGuests.size()==0))
            return m_offlineMembers.size()+m_onlineMembers.size()+2;
        if((m_onlineMembers.size()>0)&&(m_offlineMembers.size()==0)&&(mInvitedGuests.size()==0))
            return m_onlineMembers.size()+3;
        if((m_onlineMembers.size()==0)&&(m_offlineMembers.size()>0)&&(mInvitedGuests.size()==0))
            return m_offlineMembers.size()+3;
        if((m_onlineMembers.size()==0)&&(m_offlineMembers.size()==0)&&(mInvitedGuests.size()==0))
            return 4;
        return 4;
    }

    @Override
    public Object getItem(int position) {

        if(position == 0)
            return null;

        if((m_onlineMembers.size()>0)&&(position < m_onlineMembers.size()+1))
            return m_onlineMembers.get(position - 1);
        else if((m_onlineMembers.size()==0)&&(position==1))
            return null;

        int onlineCount = 1;
        int offlinecount = 1;

        if(m_offlineMembers.size()>0)
            offlinecount = m_offlineMembers.size();
        if(m_onlineMembers.size() > 0)
            onlineCount = m_onlineMembers.size();

        if(position - onlineCount - 1 < m_offlineMembers.size())
            return m_offlineMembers.get(position - onlineCount - 1);
        else if((m_offlineMembers.size() == 0)&&(position - onlineCount - 1 == 0))
            return null;

        if(mInvitedGuests.size()>0)
        {
            return mInvitedGuests.get(position - onlineCount - offlinecount - 1);
        }
        else
        {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(position!=0) {
            ViewHolder holder;
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.member_list_item_layout, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.photo = (RARoundImageView) convertView.findViewById(R.id.photo);
            holder.status = (ImageView) convertView.findViewById(R.id.online);
            convertView.setTag(holder);

            Object item = getItem(position);
            if(item != null)
            {
                RAMember member = (RAMember)item;
                if(member.getMemberJID() != null)
                {
                    if (member.getStatus().equals("online"))
                        holder.status.setImageResource(R.drawable.online);
                    else
                        holder.status.setImageResource(R.drawable.allnotification);
                    holder.text.setText(member.getUsername());
                }
                else
                {
                    holder.text.setText(member.getUsername());
                }
                return convertView;
            }
            else
            {
                convertView = mInflater.inflate(R.layout.member_list_blank_cell, null);
                return convertView;
            }
        }
        else
        {
            convertView = mInflater.inflate(R.layout.member_list_blank_cell, null);
            return convertView;
        }
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        if(position != 0) {
            HeaderViewHolder holder;
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.member_list_section_header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
            // set header text as first char in name
            int foundIndex = getSectionForPosition(position);
            if (foundIndex != -1) {
                String sectionTitle = mSectionLetters[foundIndex];
                holder.text.setText(sectionTitle);
            }
            return convertView;
        }
        else
        {
            convertView = mInflater.inflate(R.layout.roomlist_searchbar_cell, null);
            final SearchView searchView = (SearchView)convertView.findViewById(R.id.searchBar);
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    return false;
                }
            });

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchView.clearFocus();
                    if(mContext.getClass() == RATimelineActivity.class)
                    {
                        RATimelineActivity raTimelineActivity = (RATimelineActivity)mContext;
                        raTimelineActivity.searchMembersByQuery(query);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    System.out.println(newText);
                    return true;
                }
            });
        }
        return convertView;
    }

    /**
     * Remember that these have to be static, postion=1 should always return
     * the same Id that is.
     */
    @Override
    public long getHeaderId(int position) {

        if(position == 0)
            return 0;

        if(position == 1)
            return 1;

        RAMember member = (RAMember)getItem(position);
        if(member != null) {
            if (member.getMemberJID() != null) {
                if (member.getStatus().equals("online"))
                    return 1;
                else
                    return 2;
            } else {
                return 3;
            }
        }
        else
        {
            int found = -1;
            for (int i = 0; i < mSectionIndices.length; i++) {
                if (position < mSectionIndices[i]) {
                    found = i - 1;
                    break;
                }
            }
            if(found != -1)
                return found;
            else
                return mSectionIndices.length - 1;
        }
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }
        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {

        return (int)getHeaderId(position);
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    public void clear() {
        mMembers.clear();
        mInvitedGuests.clear();
        mSectionIndices = new int[0];
        mSectionLetters = new String[0];
        notifyDataSetChanged();
    }

    public void restore() {
//        mMembers = mContext.getResources().getStringArray(R.array.countries);
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionTitles();
        notifyDataSetChanged();
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
        RARoundImageView photo;
        ImageView status;
    }

}
