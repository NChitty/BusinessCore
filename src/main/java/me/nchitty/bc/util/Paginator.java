package me.nchitty.bc.util;

import me.nchitty.bc.business.Business;
import me.nchitty.bc.job.Job;
import me.nchitty.bc.player.Employee;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Paginator<E> {

    private final String formatPath;
    private final List<E> list;
    private int numberOfPages;
    private CommandSender recipient;
    private boolean console = false;


    public Paginator(String formatPath, List<E> list, CommandSender recipient) {
        this.formatPath = formatPath;
        this.list = list;
        numberOfPages = (int) Math.ceil(list.size()/5.0);
        this.recipient = recipient;
    }

    public List<Message> page(int pageNumber) {
        ArrayList<Message> messageList = new ArrayList<>();
        messageList.add(0, new Message(String.format("%s.header", formatPath), this.recipient).setOther(pageNumber, numberOfPages));
        int startIndex = (pageNumber - 1) * 5;
        int i = 1;
        E element = null;
            while (i < 6 && list.size() - 1 > startIndex + (i-1) ) {
                element = list.get(startIndex + (i - 1));
                if (element instanceof Business)
                    messageList.add(i,new Message(String.format("%s.format", formatPath), this.recipient, (Business) element));
                if (element instanceof Employee)
                    messageList.add(i,new Message(String.format("%s.format", formatPath), this.recipient, (Employee) element));
                if (element instanceof Job)
                    messageList.add(i,new Message(String.format("%s.format", formatPath), this.recipient, (Job) element));
                i++;
            }
        messageList.add(i, new Message(String.format("%s.footer", formatPath), this.recipient).setOther(pageNumber, numberOfPages));
        return messageList;
    }

}
