public class User {
    private String id;
    private String name;
    private String email;
    private String phone;
    private Role role;

    public User(String id, String name, String email, String phone, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
    
    }

    public static User create(String id,String name, String email, String phone, Role role) {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .phone(phone)
                .role(role)
                .build();
    }
}