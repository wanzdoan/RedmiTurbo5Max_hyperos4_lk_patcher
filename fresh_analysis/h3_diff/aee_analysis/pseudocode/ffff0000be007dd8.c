
void FUN_ffff0000be007dd8(ulong param_1,long param_2,long param_3)

{
  ulong uVar1;
  
  uVar1 = cpacr_el1;
  if ((uVar1 & 0x300000) != 0) {
    FUN_ffff0000be007c44(param_2);
    param_1 = uVar1 & 0xffffffffffcfffff;
    cpacr_el1 = param_1;
    InstructionSynchronizationBarrier();
  }
  FUN_ffff0000be0070e0(param_1,param_2 + 0x50,*(undefined8 *)(param_3 + 0x50));
  return;
}


