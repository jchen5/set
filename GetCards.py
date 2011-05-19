import os

for i in xrange(1, 82):
	os.popen("wget http://www.setgame.com/images/setcards/small/%02d.gif" % i)
