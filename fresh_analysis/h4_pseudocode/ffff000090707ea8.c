
void FUN_ffff000090707ea8(void)

{
  ulong uVar1;
  
  vbar_el1 = 0xffff000090700800;
  InstructionSynchronizationBarrier();
  uVar1 = currentel;
  if ((uVar1 & 0x3fffffff8) != 0) {
    FUN_ffff000090707a88();
  }
  uVar1 = daif;
  daif = uVar1 & 0xffffffffffffffbf;
  uVar1 = daif;
  daif = uVar1 & 0xfffffffffffffeff;
  FUN_ffff00009071c498();
  return;
}


