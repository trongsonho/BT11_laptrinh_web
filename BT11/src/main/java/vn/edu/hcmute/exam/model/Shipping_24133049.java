package vn.edu.hcmute.exam.model;

public class Shipping_24133049 {
    private final String recipientName, phone, address, note;
    public Shipping_24133049(String recipientName, String phone, String address, String note) {
        this.recipientName = trim(recipientName); this.phone = trim(phone);
        this.address = trim(address); this.note = trim(note);
    }
    private static String trim(String value) { return value == null ? "" : value.trim(); }
    public String getRecipientName() { return recipientName; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getNote() { return note; }
    public void validate() {
        if (recipientName.isEmpty() || recipientName.length() > 100)
            throw new IllegalArgumentException("Họ tên người nhận bắt buộc, tối đa 100 ký tự.");
        if (!phone.matches("(?:0[0-9]{9,10}|\\+84[0-9]{9,10})"))
            throw new IllegalArgumentException("Số điện thoại phải gồm 10–11 chữ số bắt đầu bằng 0, hoặc +84 và 9–10 chữ số.");
        if (address.isEmpty() || address.length() > 500)
            throw new IllegalArgumentException("Địa chỉ nhận hàng bắt buộc, tối đa 500 ký tự.");
        if (note.length() > 1000) throw new IllegalArgumentException("Ghi chú tối đa 1000 ký tự.");
    }
}
