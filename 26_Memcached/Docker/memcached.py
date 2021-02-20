import sys
import memcache

args = sys.argv
mc = memcache.Client(['memcached:11211'], debug=0)

if args[1] == "w":
  mc.set(args[2], args[3])
elif args[1] == "r":
  value = mc.get(args[2])
  print(value)

mc.disconnect_all()
