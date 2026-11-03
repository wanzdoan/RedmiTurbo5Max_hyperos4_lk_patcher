// ffff00009070872c  size=88  FUN_ffff00009070872c

void FUN_ffff00009070872c(ulong param_1,long param_2,long param_3)

{
  ulong uVar1;
  
  uVar1 = cpacr_el1;
  if ((uVar1 & 0x300000) != 0) {
    FUN_ffff000090708598(param_2);
    param_1 = uVar1 & 0xffffffffffcfffff;
    cpacr_el1 = param_1;
    InstructionSynchronizationBarrier();
  }
  FUN_ffff000090707a28(param_1,param_2 + 0x50,*(undefined8 *)(param_3 + 0x50));
  return;
}


