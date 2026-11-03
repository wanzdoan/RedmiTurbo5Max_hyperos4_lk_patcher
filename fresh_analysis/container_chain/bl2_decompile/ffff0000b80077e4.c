
void FUN_ffff0000b80077e4(void)

{
  ulong uVar1;
  
  vbar_el1 = 0xffff0000b8000800;
  InstructionSynchronizationBarrier();
  uVar1 = currentel;
  if ((uVar1 & 0x3fffffff8) != 0) {
    FUN_ffff0000b80073c4();
  }
  uVar1 = daif;
  daif = uVar1 & 0xffffffffffffffbf;
  uVar1 = daif;
  daif = uVar1 & 0xfffffffffffffeff;
  FUN_ffff0000b800e154();
  return;
}


