import java.util.HashMap;
import java.util.Map;

public class lengthOfLongestSubstring {

    public static class Solution {
        /**
         * 无重复字符的最长子串：滑动窗口 + 哈希表记录位置
         * 时间复杂度: O(N) - 仅需遍历一次字符串
         * 空间复杂度: O(min(M, N)) - 存储字符位置的哈希表大小，M 为字符集大小（如 ASCII 128）
         */
        public int lengthOfLongestSubstring(String s) {
            if (s == null || s.length() == 0) {
                return 0;
            }

            int maxLength = 0;
            int left = 0; // 窗口左边界

            // key: 字符, value: 该字符最近一次出现的数组下标
            Map<Character, Integer> lastSeen = new HashMap<>();

            // right 为窗口右边界，从 0 开始遍历所有字符
            for (int right = 0; right < s.length(); right++) {
                char currentChar = s.charAt(right); // Java 获取字符串指定下标字符的写法

                // 如果当前字符在之前的窗口里出现过
                if (lastSeen.containsKey(currentChar)) {
                    // 左边界直接跳转到【上次出现位置的下一位】和【当前左边界】的最大值
                    // 为什么用 Math.max？因为上次出现的位置可能在当前 left 的左侧，此时 left 不能倒退
                    left = Math.max(left, lastSeen.get(currentChar) + 1);
                }

                // 记录/更新当前字符的最新下标
                lastSeen.put(currentChar, right);

                // 更新最大窗口长度
                maxLength = Math.max(maxLength, right - left + 1);
            }

            return maxLength;
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        String s1 = "abcabcbb";
        String s2 = "pwwkew";
        String s3 = "bbbbb";

        System.out.println("========== 运行最长无重复子串解法 ==========");
        System.out.println("输入: \"" + s1 + "\", 结果: " + solution.lengthOfLongestSubstring(s1));
        System.out.println("输入: \"" + s2 + "\", 结果: " + solution.lengthOfLongestSubstring(s2));
        System.out.println("输入: \"" + s3 + "\", 结果: " + solution.lengthOfLongestSubstring(s3));
    }
}