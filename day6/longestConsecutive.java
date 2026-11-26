// 128. 最长连续序列
// 中等

// 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。

// 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。

 

// 示例 1：

// 输入：nums = [100,4,200,1,3,2]
// 输出：4
// 解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
// 示例 2：

// 输入：nums = [0,3,7,2,5,8,4,6,0,1]
// 输出：9
// 示例 3：

// 输入：nums = [1,0,1,2]
// 输出：3
 

// 提示：

// 0 <= nums.length <= 105
// -109 <= nums[i] <= 109

//我的想法：首先sort！log（n*n）的时间
//然后直接游标寻找，一个fast，一个slow--slow直接不变了，fast找后续可以组成连续序列的（相同数字跳过）
//[0,3,7,2,5,8,4,6,0,1] - [0,0,1,2,3,4,5,6,7,8]
//0-0,no; 0-1-...-8 ok
import java.util.HashSet;
import java.util.Set;

class Solution {
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();

        for (int num : nums) {
            set.add(num);
        }

        int maxLength = 0;
        int current;    // 当前正在检查的数字
        int length;     // 当前连续链的长度


        for (int num : set) {
            // TODO 1：判断 num 是否为连续链的起点
            if(set.contains(num - 1)){
                continue;
            }

            // TODO 2：如果是起点，初始化 current 和 length
            current = num;
            length = 1;
            // TODO 3：只要下一个数字存在，就继续向后计数
            while(set.contains(current + 1)){
                current++;
                length++;
            }
            // TODO 4：更新 maxLength
            if(maxLength < length){
                maxLength = length;
            }
        }

        return maxLength;
    }
}