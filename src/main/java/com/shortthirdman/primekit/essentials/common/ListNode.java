package com.shortthirdman.primekit.essentials.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListNode {

    private int value;

    private ListNode next;

    public ListNode(int x) {
        this.value = x;
        this.next = null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ListNode{");
        sb.append("value=").append(value);
        sb.append(", next=").append(next);
        sb.append('}');
        return sb.toString();
    }
}
