package com.shortthirdman.sharedlibs.common;

public class CounterNode {
    private int count;
    private TreeNode node;

    public CounterNode(TreeNode node, int count) {
        this.count = count;
        this.node = node;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public TreeNode getNode() {
        return node;
    }

    public void setNode(TreeNode node) {
        this.node = node;
    }
}
