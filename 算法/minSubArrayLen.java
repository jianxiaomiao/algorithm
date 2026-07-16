import java.util.Arrays;

public class minSubArrayLen {

    public static class Solution {
        /**
         * 长度最小的子数组：滑动窗口 (双指针法)
         * 时间复杂度: O(N) - left 和 right 指针均只向右移动，最多各遍历数组一次
         * 空间复杂度: O(1) - 仅需几个辅助指针变量
         */
        public int minSubArrayLen(int target, int[] nums) {
            if (nums == null || nums.length == 0) {
                return 0;
            }

            int minLength = Integer.MAX_VALUE; // 用最大值初始化，便于后面取 Math.min
            int sum = 0;                       // 记录当前窗口内所有元素的和
            int left = 0;                      // 窗口左指针

            // right 为窗口右指针，负责向右扩张
            for (int right = 0; right < nums.length; right++) {
                sum += nums[right]; // 1. 窗口右侧加入新元素

                // 2. 当窗口内元素和 >= target 时，尝试收缩窗口左侧，寻找最小长度
                while (sum >= target) {
                    // 更新最小长度 (当前窗口长度是 right - left + 1)
                    minLength = Math.min(minLength, right - left + 1);
                    
                    sum -= nums[left]; // 3. 窗口左侧抛弃元素
                    left++;            // 左指针右移，收缩窗口
                }
            }

            // 如果 minLength 没有被更新过，说明整个数组加起来也达不到 target，返回 0
            return minLength == Integer.MAX_VALUE ? 0 : minLength;
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int target = 7;
        int[] nums = {2, 3, 1, 2, 4, 3};

        System.out.println("========== 运行滑动窗口解法 ==========");
        System.out.println("输入数组: " + Arrays.toString(nums) + ", 目标值: " + target);
        
        int result = solution.minSubArrayLen(target, nums);
        
        System.out.println("满足条件最小子数组长度: " + result);
    }
}