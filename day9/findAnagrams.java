// 438. 找到字符串中所有字母异位词
// 中等
// 给定两个字符串 s 和 p，找到 s 中所有 p 的 异位词 的子串，返回这些子串的起始索引。不考虑答案输出的顺序。
// 示例 1:

// 输入: s = "cbaebabacd", p = "abc"
// 输出: [0,6]
// 解释:
// 起始索引等于 0 的子串是 "cba", 它是 "abc" 的异位词。
// 起始索引等于 6 的子串是 "bac", 它是 "abc" 的异位词。
//  示例 2:

// 输入: s = "abab", p = "ab"
// 输出: [0,1,2]
// 解释:
// 起始索引等于 0 的子串是 "ab", 它是 "ab" 的异位词。
// 起始索引等于 1 的子串是 "ba", 它是 "ab" 的异位词。
// 起始索引等于 2 的子串是 "ab", 它是 "ab" 的异位词。
 

// 提示:

// 1 <= s.length, p.length <= 3 * 104
// s 和 p 仅包含小写字母

//我的想法:首先我们需要的是异位词,也就是字母都是全包含就行了
//我们使用滑动窗口,只要能够找到对应的字母,且不超过数量就一直滑动就好
//退出条件-匹配完成\匹配失败(匹配失败的时候滑动窗口需要重新找开头)

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

class Solution {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> numbers = new ArrayList<>();
        if (s.length() < p.length()) {
            return numbers;
        }

        int[] pCount = new int[26];
        for (char c : p.toCharArray()) {
            pCount[c - 'a']++;
        }

        int[] winCount = new int[26];
        Queue<Character> queue = new ArrayDeque<>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int idx = c - 'a';

            // 发现不在 p 中的字符，清空窗口并重置计数
            if (pCount[idx] == 0) {
                queue.clear();
                Arrays.fill(winCount, 0);
                continue;
            }

            // 当前字符数量超标时，从队头出队直到数量不超标
            while (winCount[idx] + 1 > pCount[idx]) {
                char out = queue.poll();
                winCount[out - 'a']--;
            }

            // 当前字符入队并更新计数
            queue.offer(c);
            winCount[idx]++;

            // 窗口长度等于 p.length() 时，说明匹配成功
            if (queue.size() == p.length()) {
                numbers.add(i - p.length() + 1);
            }
        }

        return numbers;
    }
}