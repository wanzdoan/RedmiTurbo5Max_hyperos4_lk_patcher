// ffff000090716104  size=172  FUN_ffff000090716104

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090716104(void)

{
  undefined8 uVar1;
  ulong uVar2;
  
  uVar1 = daif;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 | 0x80;
  }
  if ((_DAT_ffff00000c400000 & 1) == 0) {
    FUN_ffff0000907161b0();
  }
  else {
    FUN_ffff000090715fb8(&DAT_ffff00000c440000);
    do {
    } while ((_DAT_ffff00000c440014 >> 2 & 1) != 0);
    UnkSytemRegWrite(3,0,4,6,0,0xff);
    InstructionSynchronizationBarrier();
    InstructionSynchronizationBarrier();
    UnkSytemRegWrite(3,0,0xc,0xc,4,0);
    InstructionSynchronizationBarrier();
    InstructionSynchronizationBarrier();
    UnkSytemRegWrite(3,0,0xc,0xc,7,1);
    InstructionSynchronizationBarrier();
    _DAT_ffff00000c440000 = 1;
    _DAT_ffff00000c440004 = 0xff;
    _DAT_ffff00000c440014 = _DAT_ffff00000c440014 & 0xfffffffd;
  }
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 & 0xffffffffffffff7f;
  }
  return;
}


