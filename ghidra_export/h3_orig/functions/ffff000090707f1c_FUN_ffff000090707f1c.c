// ffff000090707f1c  size=64  FUN_ffff000090707f1c

void FUN_ffff000090707f1c(void)

{
  ulong uVar1;
  
  vbar_el1 = 0xffff000090700800;
  InstructionSynchronizationBarrier();
  uVar1 = currentel;
  if ((uVar1 & 0x3fffffff8) != 0) {
    FUN_ffff000090707afc();
  }
  uVar1 = daif;
  daif = uVar1 & 0xffffffffffffffbf;
  uVar1 = daif;
  daif = uVar1 & 0xfffffffffffffeff;
  FUN_ffff00009071c354();
  return;
}


