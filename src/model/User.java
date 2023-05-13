package model;

public class User implements Comparable<model.User>{
        public String name;
        public String password;
        public int score;

        public User(String name, String password, int score) {
            this.name = name;
            this.password = password;
            this.score = score;
        }

        @Override
        public String toString() {
            return name + " " + password + " " + score;
        }

        @Override
        public int compareTo(model.User user) {
            return user.score - this.score;
        }
    }
