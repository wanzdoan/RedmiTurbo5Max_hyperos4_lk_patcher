
void FUN_ffff0000be007560(void)

{
  ulong uVar1;
  
  vbar_el1 = 0xffff0000be000800;
  InstructionSynchronizationBarrier();
  uVar1 = currentel;
  if ((uVar1 & 0x3fffffff8) != 0) {
    FUN_ffff0000be007140();
  }
  uVar1 = daif;
  daif = uVar1 & 0xffffffffffffffbf;
  uVar1 = daif;
  daif = uVar1 & 0xfffffffffffffeff;
  FUN_ffff0000be01d138();
  return;
}


