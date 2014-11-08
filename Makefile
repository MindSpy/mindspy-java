
.PHONY: clean

all: proto

clean:
	rm src/main/java/org/mindspy/protobufs/Proto.java || true

proto: src/main/java/org/mindspy/protobufs/Proto.java

src/main/java/org/mindspy/protobufs/Proto.java: lib/proto/mindspy.proto lib/proto/mindspy.options
	$(MAKE) -C lib/proto Proto.java
	mv lib/proto/Proto.java src/main/java/org/mindspy/protobufs/


