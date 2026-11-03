package miuix.animation.utils;

/* JADX INFO: loaded from: classes2.dex */
public class LinkNode {
    public LinkNode next;

    public void addToTail(LinkNode linkNode) {
        while (this != linkNode) {
            LinkNode linkNode2 = this.next;
            if (linkNode2 == null) {
                this.next = linkNode;
                return;
            }
            this = linkNode2;
        }
    }

    public LinkNode remove() {
        LinkNode linkNode = this.next;
        this.next = null;
        return linkNode;
    }

    public LinkNode destroy() {
        while (remove() != null) {
        }
        return null;
    }

    public int size() {
        int i = 0;
        while (true) {
            this = this.next;
            if (this == null) {
                return i;
            }
            i++;
        }
    }
}
