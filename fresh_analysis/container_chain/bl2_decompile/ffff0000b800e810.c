
void FUN_ffff0000b800e810(void)

{
  ulong uVar1;
  undefined8 uVar2;
  
  uVar1 = sctlr_el1;
  uVar2 = FUN_ffff0000b800e798(uVar1);
  UnkSytemRegWrite(3,0,2,1,0,uVar2);
  InstructionSynchronizationBarrier();
  uVar2 = FUN_ffff0000b800e798();
  UnkSytemRegWrite(3,0,2,1,1,uVar2);
  InstructionSynchronizationBarrier();
  sctlr_el1 = uVar1 & 0xffffffe737ffdfff | 0x80000000;
  InstructionSynchronizationBarrier();
  return;
}


