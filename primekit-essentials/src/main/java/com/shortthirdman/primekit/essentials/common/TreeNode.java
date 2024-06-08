package com.shortthirdman.primekit.essentials.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreeNode {

    private int value;
    private TreeNode leftNode;
    private TreeNode rightNode;

    private TreeNode(int value) {
        this.value = value;
    }

    private TreeNode(int value, TreeNode leftNode, TreeNode rightNode) {
        this.value = value;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public static TreeNode createTreeNode(int value) {
        return new TreeNode(value);
    }

    public static TreeNode newTreeNode(int value, TreeNode leftNode, TreeNode rightNode) {
        return new TreeNode(value, leftNode, rightNode);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("TreeNode{");
        builder.append("value=").append(value);
        builder.append(", left=").append(leftNode.toString());
        builder.append(", right=").append(rightNode.toString());
        return builder.toString();
    }
}
