package com.sensenxu.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class sensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(sensitiveFilter.class);
    //替换符
    private static final String REPLACEMENT="***";
    //根节点
    private TrieNode rootNode = new TrieNode();
    @PostConstruct
    public void init(){
        try(
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        )
        {
                String keyword;
                while((keyword = reader.readLine())!= null){//得到每一行 如吸毒
                    //添加到前缀树
                    this.addKeyWord(keyword);
                }

        }catch (Exception e){
            logger.error("加载敏感词出错"+e.getMessage());
        }


    }
    private void addKeyWord(String keyword){
        TrieNode tempNode = rootNode;
        for(int i = 0; i < keyword.length(); i++){
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if(subNode == null){
                //如果没有，初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode); //上一级添加子结点
            }
            //指向子节点，进入下一轮循环
            tempNode = subNode; //添加过后的子结点作为新的"上一级"
            //设置结束标识
            if(i == keyword.length() - 1){
                tempNode.setKeywordEnd(true);

            }
        }
    }

    /**
     * 输入文本进行过滤
     * 返回过滤后的文本
     * @param text
     * @return
     */

    //过滤敏感词
    public String filter(String text){
        if(StringUtils.isBlank(text))return null;
        //指针1
        TrieNode tempNode = rootNode;
        //指针2
        int begin = 0;
        //指针3
        int position = 0;
        //结果，要持续追加
        StringBuilder sb = new StringBuilder();
        while(position < text.length()){
            char c = text.charAt(position);
            //跳过符号
            if(isSymbol(c)){
                //若指针1为根节点，将此符号计入结果，指针2走下下一步（即判断text下一个字符）
                if(tempNode == rootNode){
                    sb.append(c);//虽然你是特殊字符，但是你在敏感词前面
                    begin++;
                }
                //无论符号在开头还是在中间，指针3都会向下走一步
                position++;
                continue;
            }
            //走到这里代表不是符号，检查下级结点,当前结点变为下级结点
            tempNode = tempNode.getSubNode(c);
            if(tempNode == null) {
                //以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个位置
                position = ++begin;
                //重新指向根结点
                tempNode = rootNode;
            }else if(tempNode.isKeywordEnd){
                //发现敏感词，将begin-position字符串替换掉
                sb.append(REPLACEMENT);
                //进入下一个位置
                begin = ++position;
                //重新指向根结点
                tempNode = rootNode;
            }else{
                //检查下一个字符
                position++;
            }
        }
        //将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
     }
   private boolean isSymbol (Character c){
        //后一个是判断是否为东亚文字
        return !CharUtils.isAsciiAlphanumeric(c)&&(c < 0x2E80 || c > 0x9FFF);
   }


    private class TrieNode{
        //关键词结束的标识
        private boolean isKeywordEnd = false;
        //当前节点的子节点(下级字符，下级节点)
        private Map<Character,TrieNode> subNodes = new HashMap<>();


        //添加子节点
        public void addSubNode(Character c,TrieNode node){
            subNodes.put(c,node);
        }
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }


        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }
    }
}
