package astar;
public class Frontiera {
    public Node root;
    public Node tnil;
    String ftype;

    public Frontiera(String ftype) {
        tnil = new Node(null, null, null, 0);

        this.ftype = ftype;
        root = tnil;
        tnil.color = "black";
        root.color = "black";
    }

    public int compare(Node n1, Node n2){
        double c = n1.path_cost, cn = n2.path_cost;

        c = ftype == "astar" ? c + n1.stato.h() : c;
        cn = ftype == "astar" ? cn + n2.stato.h() : cn;

        return Double.compare(c, cn);
    }

    public boolean isEmpty() {
        return root == tnil;
    }

    public Node search(Node key) {
        return search_ric(key, root);
    }

    private Node search_ric(Node key, Node n) {
        if (n == tnil)return null;
        if(n.stato.equals(key.stato))return n;
        Node left = search_ric(key, n.left);
        if(left != tnil) return left;
        return search_ric(key, n.right);
    }

    private Node get_min(Node z) {
        if (z.left == tnil) return z;
        return get_min(z.left);
    }

    public void setRoot(Node n) {
        root = n;
        root.fparent = tnil;
        root.color = "black";
        if(root.left == null)root.left = tnil;
        if(root.right == null)root.right = tnil;
    }

    private void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right.fparent = x;
        y.fparent = x.fparent;
        if (x.fparent == tnil) root = y;
        else if (x == x.fparent.left) x.fparent.left = y;
        else x.fparent.right = y;
        y.right = x;
        x.fparent = y;
    }

    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left.fparent = x;
        y.fparent = x.fparent;
        if (x.fparent == tnil) root = y;
        else if (x == x.fparent.left) x.fparent.left = y;
        else x.fparent.right = y;
        y.left = x;
        x.fparent = y;
    }

    private void RBinsert_fixup(Node z) {
        if (z.fparent.color == "black" || z.fparent == tnil){
            if(z.fparent == tnil)z.color = "black";
            return;
        }
        Node y = z.fparent.fparent.right;
        if (z.fparent.fparent.right == z.fparent) y = z.fparent.fparent.left;

        if (y.color == "red") {//caso 1
            z.fparent.color = "black";
            y.color = "black";
            z.fparent.fparent.color = "red";
            z = z.fparent.fparent;
            RBinsert_fixup(z);
            return;
        }
        if (y == z.fparent.fparent.right) RRBinsert_fixup(z);
        else LRBinsert_fixup(z);
    }

    private void RRBinsert_fixup(Node z) {
        if (z.fparent.right == z) {
            z = z.fparent;
            leftRotate(z);
        }
        z.fparent.color = "black";
        z.fparent.fparent.color = "red";
        rightRotate(z.fparent.fparent);
        RBinsert_fixup(z);
    }

    private void LRBinsert_fixup(Node z) {
        if (z.fparent.left == z) {
            z = z.fparent;
            rightRotate(z);
        }
        z.fparent.color = "black";
        z.fparent.fparent.color = "red";
        leftRotate(z.fparent.fparent);
        RBinsert_fixup(z);
    }

    public void RBinsert(Node z) {
        Node x = root, y = tnil;
        while (x != tnil) {
            y = x;
            if(compare(z, x) < 0) x = x.left;
            else x = x.right;
        }
        z.fparent = y;
        if (y == tnil) root = z;
        else if (compare(z, y) < 0)y.left = z;
        else y.right = z;
        z.left = tnil;
        z.right = tnil;
        z.color = "red";
        RBinsert_fixup(z);
    }

    private void RBtransplant(Node z, Node x) {
        if (z.fparent == tnil)setRoot(x);
        else if (z.fparent.left == z) z.fparent.left = x;
        else z.fparent.right = x;
        x.fparent = z.fparent;
    }

    private boolean checkTree(Node z, int blacks){
        if(z == tnil)return blacks == 0;
        if(z.color == "red" && (z.left.color != "black" || z.right.color != "black"))return false;
        return checkTree(z.left, blacks - (z.color == "black" ? 1 : 0)) && checkTree(z.right, blacks - (z.color == "black" ? 1 : 0));
    }

    public boolean checkTree(){
        int blacks = 0;
        Node x = root;
        if (x.color != "black" || tnil.color != "black")return false;
        while (x != tnil) {
            blacks += x.color == "black" ? 1 : 0;
            x = x.left;
        }
        return checkTree(root, blacks);
    }

    private void RBdelete_fixup(Node z) {
        if (z.color == "red" || z.fparent == tnil)z.color = "black";
        else if (z.fparent.left == z)RRBdelete_fixup(z);
        else LRBdelete_fixup(z);
    }

    private void RRBdelete_fixup(Node z) {
        Node w = z.fparent.right;
        //caso 1
        if(w == tnil)RBdelete_fixup(z.fparent);
        else if (w.color == "red") {
            w.color = "black";
            z.fparent.color = "red";
            leftRotate(z.fparent);
            RBdelete_fixup(z);
        }else if (w.right.color == "black" && w.left.color == "black") {
            w.color = "red";
            RBdelete_fixup(z.fparent);
        }else {
            if (w.right.color == "black") {
                w.left.color = "black";
                w.color = "red";
                rightRotate(w);
                w = w.fparent;
            }
            w.color = z.fparent.color;
            z.fparent.color = "black";
            w.right.color = "black";
            leftRotate(w.fparent);
        }
    }

    private void LRBdelete_fixup(Node z) {
        Node w = z.fparent.left;
        if (w.color == "red") {
            w.color = "black";
            z.fparent.color = "red";
            rightRotate(z.fparent);
            RBdelete_fixup(z);
        }else if (w.right.color == "black" && w.left.color == "black") {// caso 2
            w.color = "red";
            RBdelete_fixup(z.fparent);
        }else {
            if (w.left.color == "black") {
                w.right.color = "black";
                w.color = "red";
                leftRotate(w);
                w = z.fparent.left;
            }
            w.color = z.fparent.color;
            z.fparent.color = "black";
            w.left.color = "black";
            rightRotate(w.fparent);
        }
    }

    public Node popMinRoot() {
        Node todelete = get_min(root);
        Node temp = todelete;
        RBdelete(todelete);
        return temp;
    }

    public void RBdelete(Node z){
        String orig_color = z.color;
        Node x;
        if(z.right == tnil){
            x = z.left;
            RBtransplant(z, z.left);
        }
        else if(z.left == tnil){
            x = z.right;
            RBtransplant(z, z.right);
        }
        else{
            Node y = get_min(z.right);
            x = y.right;
            orig_color = y.color;
            //if(y.fparent == z)x.fparent = y;
            if(y.fparent != z){
                RBtransplant(y, y.right);
                y.right = z.right;
                y.right.fparent = y;
            }
            RBtransplant(z, y);
            y.left = z.left;
            y.left.fparent = y;
            y.color = z.color;
        }
        if(orig_color == "black")RBdelete_fixup(x);
    }
}