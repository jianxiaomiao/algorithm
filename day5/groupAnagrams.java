
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

// 给你一个字符串数组，请你将 字母异位词 组合在一起。可以按任意顺序返回结果列表。

// 示例 1:
// 输入: strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
// 输出: [["bat"],["nat","tan"],["ate","eat","tea"]]
// 解释：
// 在 strs 中没有字符串可以通过重新排列来形成 "bat"。
// 字符串 "nat" 和 "tan" 是字母异位词，因为它们可以重新排列以形成彼此。
// 字符串 "ate" ，"eat" 和 "tea" 是字母异位词，因为它们可以重新排列以形成彼此。
// 示例 2:
// 输入: strs = [""]
// 输出: [[""]]
// 示例 3:
// 输入: strs = ["a"]
// 输出: [["a"]]
// 提示：

// 1 <= strs.length <= 104
// 0 <= strs[i].length <= 100
// strs[i] 仅包含小写字母

//思路，从第一个开始一一比较，暴力匹配，时间n*n
//优化：排序，从a-z首字母排序，这样直接需要比较的从开头和数组立马对应开头的对比--只需要匹配对应的首字母，然后比较就行，因为有顺序->n*m
// ["eat", "tea", "tan", "ate", "nat", "bat"]-> ["ate", "bat", "eat", "nat", "tan", "tea"]
//ate - e - eat ok1; bat - t - tan no, tea no; eat - t - tan no, tea ok1;
//tan - n no; tea -a -aet ok1;
//需要额外判断对应的string已经有list;
class Solution {
   
    public List<List<String>> groupAnagrams(String[] strs) {
        HashMap<String, List<String>> map = new HashMap<String, List<String>>();
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            if (map.containsKey(key)) {
                map.get(key).add(str);
            } else {
                map.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
            }
        }
        
        return new ArrayList<>(map.values());
        
    }
}