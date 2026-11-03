
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b80098c0(void)

{
  ulong uVar1;
  ulong uVar2;
  uint uVar3;
  
  FUN_ffff0000b80096c8(0xc440000);
  uVar3 = _DAT_0c440014;
  _DAT_0c440014 = _DAT_0c440014 & 0xfffffffd;
  do {
  } while ((uVar3 >> 2 & 1) != 0);
  uVar2 = scr_el3;
  scr_el3 = uVar2 | 1;
  InstructionSynchronizationBarrier();
  InstructionSynchronizationBarrier();
  uVar1 = UnkSytemRegRead(3,6,0xc,0xc,5);
  UnkSytemRegWrite(3,6,0xc,0xc,5,uVar1 | 9);
  InstructionSynchronizationBarrier();
  InstructionSynchronizationBarrier();
  UnkSytemRegWrite(3,6,0xc,0xc,7,1);
  InstructionSynchronizationBarrier();
  uVar1 = UnkSytemRegRead(3,4,0xc,9,5);
  UnkSytemRegWrite(3,4,0xc,9,5,uVar1 | 9);
  InstructionSynchronizationBarrier();
  InstructionSynchronizationBarrier();
  scr_el3 = uVar2;
  InstructionSynchronizationBarrier();
  InstructionSynchronizationBarrier();
  uVar1 = UnkSytemRegRead(3,0,0xc,0xc,5);
  UnkSytemRegWrite(3,0,0xc,0xc,5,uVar1 | 1);
  InstructionSynchronizationBarrier();
  InstructionSynchronizationBarrier();
  return;
}


