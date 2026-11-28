package day14;

import java.util.HashMap;
import java.util.Map;

// 41. 缺失的第一个正数
// 困难
// 提示
// 给你一个未排序的整数数组 nums ，请你找出其中没有出现的最小的正整数。

// 请你实现时间复杂度为 O(n) 并且只使用常数级别额外空间的解决方案。
 

// 示例 1：

// 输入：nums = [1,2,0]
// 输出：3
// 解释：范围 [1,2] 中的数字都在数组中。
// 示例 2：

// 输入：nums = [3,4,-1,1]
// 输出：2
// 解释：1 在数组中，但 2 没有。
// 示例 3：

// 输入：nums = [7,8,9,11,12]
// 输出：1
// 解释：最小的正数 1 没有出现。
 

// 提示：

// 1 <= nums.length <= 105
// -231 <= nums[i] <= 231 - 1

//我的想法:这道题是找没有出现的最小正数
//我的想法第一是使用 hashmap,然后判断一下就好,但是常数级别的额外空间需要额外考虑
class Solution {
    public int firstMissingPositive(int[] nums) {
    Map<Integer, Boolean> map = new HashMap<>();
    for (int num : nums) {
        if (num > 0) { // 只关心正整数
            map.put(num, true);
        }
    }
    // 长度为 n 的数组，缺失的最小正数一定在 [1, n + 1] 之间
    for (int i = 1; i <= nums.length; i++) {
        if (!map.containsKey(i)) {
            return i;
        }
    }
    return nums.length + 1;   
    }
}