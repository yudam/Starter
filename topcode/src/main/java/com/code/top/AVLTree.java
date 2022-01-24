package com.code.top;

/**
 * User: maodayu
 * Date: 2021/12/13 10:18
 * AVL树 : 平衡二叉树
 */
public class AVLTree {

    /*
     * 二叉搜索树的查找最大次数为二叉搜索树的最大层数
     * AVL树定义
     * 1. 每个节点的左右子节点的高度差为[-1,1]
     *  通过旋转降低树的高度，来减少查询
     *
     * ll: 左子树的左节点  -- 右旋
     * rr：右子树的右节点  -- 左旋
     * lr：左子树的右节点  -- 先左旋，在右旋
     * rl：右子树的左节点  -- 先右旋，在左旋
     */

    class Node {
        public int e;
        public Node left;
        public Node right;
        public int height;

        public Node(int val, int height) {
            this.e = val;
            this.height = height;
        }
    }

    public int getHeight(Node node) {
        if (node == null) return 0;
        return node.height;
    }

    public Node minNode(Node node) {
        if (node == null) return null;
        Node result = minNode(node.left);
        if (result == null) {
            result = node;
        }
        return result;
    }

    /*
     * 平衡因子
     */
    public int getBalFactory(Node node) {
        if (node == null) return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

    public boolean isBal(Node node) {
        if (node == null) return true;
        int factory = getBalFactory(node);
        if (factory < -1 || factory > 1) return false;
        return isBal(node.left) && isBal(node.right);
    }


    /*
     * 添加元素
     */
    public Node addNode(Node node, int value) {
        if (node == null) {
            return new Node(value, 1);
        }
        if (node.e > value) {
            node.left = addNode(node.left, value);
        } else {
            node.right = addNode(node.right, value);
        }
        node.height = Math.max(node.left.height, node.right.height) + 1;

        //右旋
        if (getBalFactory(node) > 1 && getBalFactory(node.left) > 0) {
            return rightRotate(node);
        }

        //左旋
        if (getBalFactory(node) < -1 && getBalFactory(node.right) < 0) {
            return leftRotate(node);
        }

        //lr
        if (getBalFactory(node) > 1 && getBalFactory(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        //rl
        if (getBalFactory(node) < 1 && getBalFactory(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    /*
     * 删除元素；和二叉搜索树基本一致，不过是在删除结束后，对树结构做平衡操作。
     */
    private Node remove(Node node, int e) {
        if (node == null) return null;

        Node rectNode = null;
        if (node.e > e) {
            node.left = remove(node.left, e);
            rectNode = node;
        } else if (node.e < e) {
            node.right = remove(node.right, e);
            rectNode = node;
        } else {
            if (node.left == null) {
                rectNode = node.right;
                node.right = null;
            } else if (node.right == null) {
                rectNode = node.left;
                node.left = null;
            } else {
                rectNode = minNode(node.right);
                rectNode.left = node.left;
                rectNode.right = remove(node.right, rectNode.e);
                node.left = node.right = null;
            }
        }

        rectNode.height = Math.max(rectNode.left.height, rectNode.right.height) + 1;

        //右旋
        if (getBalFactory(rectNode) > 1 && getBalFactory(rectNode.left) > 0) {
            return rightRotate(rectNode);
        }

        //左旋
        if (getBalFactory(rectNode) < -1 && getBalFactory(rectNode.right) < 0) {
            return leftRotate(rectNode);
        }

        //lr
        if (getBalFactory(rectNode) > 1 && getBalFactory(rectNode.left) < 0) {
            rectNode.left = leftRotate(rectNode.left);
            return rightRotate(rectNode);
        }

        //rl
        if (getBalFactory(rectNode) < 1 && getBalFactory(rectNode.right) > 0) {
            rectNode.right = rightRotate(rectNode.right);
            return leftRotate(rectNode);
        }
        return rectNode;
    }

    /*
     * 右旋:LL
     *
     */
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node temp = x.right;
        x.right = y;
        y.left = temp;
        y.height = Math.max(y.left.height, y.right.height) + 1;
        x.height = Math.max(x.left.height, x.right.height) + 1;
        return x;
    }

    /*
     * 左旋:rr  z-y-x
     */
    private Node leftRotate(Node y) {
        Node x = y.right;
        Node temp = x.left;
        x.left = y;
        y.right = temp;
        y.height = Math.max(y.left.height, y.right.height) + 1;
        x.height = Math.max(x.left.height, x.right.height) + 1;
        return x;
    }
}
