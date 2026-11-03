
void FUN_ffff0000b8008068(ulong param_1,long param_2,long param_3)

{
  ulong uVar1;
  
  uVar1 = cpacr_el1;
  if ((uVar1 & 0x300000) != 0) {
    FUN_ffff0000b8007ed4(param_2);
    param_1 = uVar1 & 0xffffffffffcfffff;
    cpacr_el1 = param_1;
    InstructionSynchronizationBarrier();
  }
  FUN_ffff0000b8007364(param_1,param_2 + 0x50,*(undefined8 *)(param_3 + 0x50));
  return;
}


