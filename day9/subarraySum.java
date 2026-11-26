// 560. 和为 K 的子数组
// 中等
// 给你一个整数数组 nums 和一个整数 k ，请你统计并返回 该数组中和为 k 的子数组的个数 。

// 子数组是数组中元素的连续非空序列。

// 示例 1：

// 输入：nums = [1,1,1], k = 2
// 输出：2
// 示例 2：

// 输入：nums = [1,2,3], k = 3
// 输出：2

// 提示：

// 1 <= nums.length <= 2 * 104
// -1000 <= nums[i] <= 1000
// -107 <= k <= 107

//我的想法:使用滑动窗口

import java.util.HashMap;
import java.util.Map;

class Solution {
    public int subarraySum(int[] nums, int k) {
        // key: 前缀和, value: 该前缀和出现的次数
        Map<Integer, Integer> prefixCount = new HashMap<>();
        prefixCount.put(0, 1);

        int count = 0;
        int preSum = 0;

        for (int num : nums) {
            preSum += num;
            // 若存在前缀和为 preSum - k，说明中间这段子数组的和为 k
            count += prefixCount.getOrDefault(preSum - k, 0);
            prefixCount.put(preSum, prefixCount.getOrDefault(preSum, 0) + 1);
        }

        return count;
    }
}