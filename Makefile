

.PHONY: tools clean Regs.java

all: clean tools Regs.java

clean:
	rm src/main/java/org/mindspy/protobufs/Regs.java  || true

tools:
	$(MAKE) -C $@

Regs.java:
	mv tools/proto/Regs.java src/main/java/org/mindspy/protobufs/
