[hadoop@hadoop ~]$ ssh-keygen -t rsa
Generating public/private rsa key pair.
Enter file in which to save the key (/home/hadoop/.ssh/id_rsa): 
Created directory '/home/hadoop/.ssh'.
Enter passphrase (empty for no passphrase): 
Enter same passphrase again: 
Your identification has been saved in /home/hadoop/.ssh/id_rsa.
Your public key has been saved in /home/hadoop/.ssh/id_rsa.pub.
The key fingerprint is:
74:f9:ce:b1:71:2a:25:d6:cf:02:a3:20:6b:7e:08:6d hadoop@hadoop.master.test.com
[hadoop@hadoop ~]$ ls -a
.              .bash_logout   Desktop   .gconf   .gnome2          .gtkrc-1.2-gnome2  .metacity  .redhat  .thumbnails
..             .bash_profile  .dmrc     .gconfd  .gnome2_private  .ICEauthority      .mozilla   .ssh     .Trash
.bash_history  .bashrc        .eggcups  .gnome   .gstreamer-0.10  .icons             .nautilus  .themes  .xsession-errors
[hadoop@hadoop ~]$ cd .ssh
[hadoop@hadoop .ssh]$ ll
total 16
-rw------- 1 hadoop hadoop 1675 Dec 23 01:59 id_rsa
-rw-r--r-- 1 hadoop hadoop  411 Dec 23 01:59 id_rsa.pub

-----------------------
[hadoop@hadoop .ssh]$ cat id_rsa.pub >> authorized_keys
[hadoop@hadoop .ssh]$ ll
total 24
-rw-rw-r-- 1 hadoop hadoop  411 Dec 23 02:01 authorized_keys
-rw------- 1 hadoop hadoop 1675 Dec 23 01:59 id_rsa
-rw-r--r-- 1 hadoop hadoop  411 Dec 23 01:59 id_rsa.pub
[hadoop@hadoop .ssh]$ 
[hadoop@hadoop .ssh]$ chmod 700 authorized_keys 
[hadoop@hadoop .ssh]$ ll
total 24
-rwx------ 1 hadoop hadoop  411 Dec 23 02:23 authorized_keys
-rw------- 1 hadoop hadoop 1679 Dec 23 02:23 id_rsa
-rw-r--r-- 1 hadoop hadoop  411 Dec 23 02:23 id_rsa.pub
----------
[hadoop@hadoop .ssh]$ ssh localhost
The authenticity of host 'localhost (127.0.0.1)' can't be established.
RSA key fingerprint is 9c:b7:bf:50:2b:45:4e:10:8b:2b:58:08:f0:51:b3:7b.
Are you sure you want to continue connecting (yes/no)? yes
Warning: Permanently added 'localhost' (RSA) to the list of known hosts.
hadoop@localhost's password: 
Last login: Mon Dec 23 01:38:23 2013
[hadoop@hadoop ~]$ exit
Connection to localhost closed.
[hadoop@hadoop .ssh]$ 
[hadoop@hadoop .ssh]$ ssh localhost
Last login: Mon Dec 23 02:29:20 2013 from localhost.localdomain
