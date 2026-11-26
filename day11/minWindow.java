package day11;
// 给定两个字符串 s 和 t，长度分别是 m 和 n，返回 s 中的 最短窗口 子串，使得该子串包含 t 中的每一个字符（包括重复字符）。如果没有这样的子串，返回空字符串 ""。

// 测试用例保证答案唯一。
// 示例 1：

// 输入：
// String s = "ADOBECODEBANC";
// String t = "ABC";
// 输出："BANC"
// 解释：最小覆盖子串 "BANC" 包含来自字符串 t 的 'A'、'B' 和 'C'。
// 示例 2：

// 输入：
// String s = "a";
// String t = "a";

// 输出："a"
// 解释：整个字符串 s 是最小覆盖子串。
// 示例 3:

// 输入:
// String s = "a";
// String t = "aa";

// 输出: ""
// 解释: t 中两个字符 'a' 均应包含在 s 的子串中，
// 因此没有符合条件的子字符串，返回空字符串。

// 提示：

// m == s.length
// n == t.length
// 1 <= m, n <= 105
// s 和 t 由英文字母组成

// 进阶：你能设计一个在 O(m + n) 时间内解决此问题的算法吗？

//我的想法:首先判断铁矿,比如s 长度< t,那么直接 return ""
//然后进行后续判断,我们首先使用一个 hashmap,存 s 的字符,然后先初筛一遍包含全部 t 的字符,不包含那肯定也挂了
//然后我们就开始苦逼的找子串了,我的想法是从开头开始找匹配的字符,然后后续找到多的,判断前面的能不能丢,这个能不能丢的判断就很重要了
//我先穷举想想哈,a-dob-ec-,这里就是一个了->然后a-dob-ec-odeb-,这里发现给前面砍头没有办法凑够,->a-dob-ec-odeb-a,这时候前面的 ba 都出现了,砍头->ec-odeb-a-nc-,这里 c 出现了,砍头
//->deb-a-nc-,然后前面的 de 是不需要的,可以去掉,最后得到b-anc-
//那么我们就需要遍历这个 s->然后记录下来现在的子串里面的t 的字符的顺序,这里是 abc,后面发现 ab 重复了就可以砍头
//也就是子串记录里面存在前后相同的部分可以去掉开头!
class Solution {
    public String minWindow(String s, String t) {
        if (s.length() < t.length()) {
            return "";
        }

        int[] need = new int[128];
        int required = 0; // t 中不同字符的种类数
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            if (need[c] == 0) {
                required++;
            }
            need[c]++;
        }

        int[] window = new int[128];
        int left = 0;
        int valid = 0; // 当前窗口中数量已达标的字符种类数
        int start = 0;
        int minLen = Integer.MAX_VALUE;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (need[c] > 0) {
                window[c]++;
                if (window[c] == need[c]) {
                    valid++;
                }
            }

            // 当窗口已完全覆盖 t 时，尝试收缩左边界（砍头）
            while (valid == required) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    start = left;
                }

                char d = s.charAt(left);
                left++;
                if (need[d] > 0) {
                    if (window[d] == need[d]) {
                        valid--;
                    }
                    window[d]--;
                }
            }
        }

        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }
}
