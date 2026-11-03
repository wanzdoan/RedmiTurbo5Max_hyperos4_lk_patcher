// ffff0000907087a0  size=88  FUN_ffff0000907087a0

void FUN_ffff0000907087a0(ulong param_1,long param_2,long param_3)

{
  ulong uVar1;
  
  uVar1 = cpacr_el1;
  if ((uVar1 & 0x300000) != 0) {
    FUN_ffff00009070860c(param_2);
    param_1 = uVar1 & 0xffffffffffcfffff;
    cpacr_el1 = param_1;
    InstructionSynchronizationBarrier();
  }
  FUN_ffff000090707a9c(param_1,param_2 + 0x50,*(undefined8 *)(param_3 + 0x50));
  return;
}


