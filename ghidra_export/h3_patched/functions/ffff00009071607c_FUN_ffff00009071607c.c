// ffff00009071607c  size=124  FUN_ffff00009071607c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009071607c(void)

{
  uint uVar1;
  
  FUN_ffff000090715fb8(&DAT_ffff00000c440000);
  uVar1 = _DAT_ffff00000c440014;
  _DAT_ffff00000c440014 = _DAT_ffff00000c440014 & 0xfffffffd;
  do {
  } while ((uVar1 >> 2 & 1) != 0);
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
  return;
}


