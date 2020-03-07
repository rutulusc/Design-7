// S30 Big N Problem #126 {Hard}
// 460. LFU Cache
// Time Complexity :O(1) for all operations
// Space Complexity :O(n)
// Did this code successfully run on Leetcode :Yes
// Any problem you faced while coding this :None


// Your code here along with comments explaining your approach
class LFUCache {
    
    class Node{
        int key; int val; int cnt;
        Node next; Node prev;
        public Node(int key, int val){
            this.key=key;
            this.val=val;
            this.cnt=1;
        }
    }
    
    class DLList{
        Node head; Node tail;
        int size;
        public DLList(){
            head=new Node(-1,-1);
            tail=new Node(-1,-1);
            head.next=tail;
            tail.prev=head;
        }
        public void addToHead(Node node){
            node.next=head.next;
            node.prev=head;
            head.next.prev=node; 
            head.next=node;
            size++;
        }
        public void removeNode(Node node){
            node.prev.next=node.next;
            node.next.prev=node.prev;
            size--;
        }
        public Node removeLast(){
            Node last=tail.prev;
            last.prev.next=tail;
            tail.prev=last.prev;
            size--;
            return last;
        }
    }
    
    HashMap<Integer, Node> map;
    HashMap<Integer, DLList> freq;
    int capacity; int min;
    int cacheSize; //to avoid confusion with capacity
    

    public LFUCache(int capacity) {
        map=new HashMap<>();
        freq=new HashMap<>();
        this.capacity=capacity;
        
    }
    
    public int get(int key) {
        Node node=map.get(key);
        if(node !=null){
            update(node);
            return node.val;
        }return -1;
    }
    
    public void put(int key, int value) {
        if(capacity==0) return;
        Node node;
        //key is already present in map
        if(map.containsKey(key)){
            node=map.get(key);
            node.val=value;
            update(node);  
        }else{//key is not present
            node=new Node(key,value);
            //capacity is full
            //we make the space
            if(cacheSize==capacity){
                DLList minList=freq.get(min);
                Node tailNode=minList.removeLast();
                map.remove(tailNode.key);
                cacheSize--;
            }
            //new node, so count is 1
            DLList li=freq.getOrDefault(1,new DLList());
            li.addToHead(node);
            freq.put(1,li);
            map.put(key,node);
            min=1;
            cacheSize++;
        }
        
    }
    
    //modify the freq hashmap
    public void update(Node node){
        DLList oldList=freq.get(node.cnt);
        oldList.removeNode(node);
        if(oldList.size==0 && node.cnt==min) min++;
        node.cnt++;
        DLList newList=freq.getOrDefault(node.cnt, new DLList());
        newList.addToHead(node);
        freq.put(node.cnt,newList);
    }
    
}

/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */