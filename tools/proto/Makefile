

all: clean regs_pb.c regs_pb.cc regs_pb2.py Regs.java 

clean:
	rm regs_pb.[ch]* regs_pb2.py Regs.java registers_pb || true

regs_pb.c:
	protoc --plugin=$$(which protoc-gen-nanopb) --nanopb_out=. -o registers_pb regs.proto
	mv regs.pb.h regs_pb.h
	mv regs.pb.c regs_pb.c
	sed -i 's/regs[.]pb[.]h/regs_pb.h/' regs_pb.c

regs_pb.cc:
	protoc --cpp_out=. regs.proto
	mv regs.pb.h regs_pb.hh
	mv regs.pb.cc regs_pb.cc
	sed -i 's/regs[.]pb[.]h/regs_pb.hh/' regs_pb.cc

regs_pb2.py:
	protoc  --python_out=. regs.proto
	rm regs_pb2.pyc || true

Regs.java:
	protoc  --java_out=. regs.proto
	mv org/mindspy/protobufs/Regs.java ./
	rm org -fr

