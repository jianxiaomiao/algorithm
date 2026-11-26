// 3. 无重复字符的最长子串
// 中等
// 给定一个字符串 s ，请你找出其中不含有重复字符的 最长 子串 的长度。
// 示例 1:

// 输入: s = "abcabcbb"
// 输出: 3 
// 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。注意 "bca" 和 "cab" 也是正确答案。
// 示例 2:

// 输入: s = "bbbbb"
// 输出: 1
// 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
// 示例 3:

// 输入: s = "pwwkew"
// 输出: 3
// 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
//      请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 

// 提示：

// 0 <= s.length <= 105
// s 由英文字母、数字、符号和空格组成

//我的想法:在这道题是滑动窗口类型的题目
//我们使用一个队列,前面只允许退出,后面只允许进入
import java.util.ArrayDeque;
import java.util.Queue;

class Solution {
    public int lengthOfLongestSubstring(String s) {
        Queue<Character> queue = new ArrayDeque<>();
        int maxLen = 0;

        for (char c : s.toCharArray()) {
            while (queue.contains(c)) {
                queue.poll();
            }
            queue.offer(c);
            maxLen = Math.max(maxLen, queue.size());
        }

        return maxLen;
    }
}