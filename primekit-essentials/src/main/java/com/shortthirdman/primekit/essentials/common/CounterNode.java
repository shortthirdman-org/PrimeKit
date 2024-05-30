package com.shortthirdman.primekit.essentials.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CounterNode {

    private int count;
    private TreeNode node;

    public CounterNode(TreeNode node, int count) {
        this.count = count;
        this.node = node;
    }

    private CounterNode(TreeNode left, TreeNode right, int count) {
        this.count = count;
        this.node = TreeNode.newTreeNode(count, left, right);
    }
}
