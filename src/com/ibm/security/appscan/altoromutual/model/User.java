public Account[] getAccounts(){
    try {
        return DBUtil.getAccounts(username);
    } catch (SQLException e) {
        // Log the exception instead of printing the stack trace
        System.out.println("Error getting user accounts: " + e.getMessage());
        return null;
    }
}
