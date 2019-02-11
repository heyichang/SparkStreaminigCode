package com.ceiec.bigdata.action;
import java.util.LinkedList;
import java.util.Queue;

public class AhoCorasick {
    class TrieNode {
        TrieNode[] children;
        TrieNode fail;
        boolean isWord;

        TrieNode() {
            children = new TrieNode[26];
            fail = null;
            isWord = false;
        }
    }

    TrieNode root;

    AhoCorasick() {
        root = new TrieNode();
    }

    void bulidTrie(String[] patterns) {
        for (String pattern : patterns) {
            TrieNode cur = root;
            for (int i = 0; i < pattern.length(); i++) {
                if (cur.children[pattern.charAt(i) - 'a'] == null)
                    cur.children[pattern.charAt(i) - 'a'] = new TrieNode();
                cur = cur.children[pattern.charAt(i) - 'a'];
            }
            cur.isWord = true;
        }
    }

    void core() {
        Queue<TrieNode> queue = new LinkedList<TrieNode>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TrieNode cur = queue.poll();
            for (int i = 0; i < 26; i++) {
                if (cur.children[i] != null) {
                    if (cur == root) cur.children[i].fail = root;
                    else {
                        TrieNode tmp = cur.fail;
                        while (tmp != null) {
                            if (tmp.children[i] != null) {
                                cur.children[i].fail = tmp.children[i];
                                break;
                            }
                            tmp = tmp.fail;
                        }
                        if (tmp == null) cur.children[i].fail = root;
                    }
                    queue.add(cur.children[i]);
                }
            }
        }
    }

    int query(String text) {
        int res = 0;
        TrieNode pre = root;
        for (int i = 0; i < text.length(); i++) {
            int index = text.charAt(i) - 'a';
            while (pre.children[index] == null && pre != root) pre = pre.fail;
            if (pre == root && pre.children[index] == null) continue;
            pre = pre.children[index];
            TrieNode tmp = pre;
            while (tmp != root && tmp.isWord) {
                res++;
                tmp.isWord = false;
                tmp = tmp.fail;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        AhoCorasick ac = new AhoCorasick();
        String[] patterns = new String[]{"he", "she", "chin", "china"};
        ac.bulidTrie(patterns);
        ac.core();
        int ans = ac.query("China Antonin Scalia never left — for now");
        System.out.println(ans);
    }
}
