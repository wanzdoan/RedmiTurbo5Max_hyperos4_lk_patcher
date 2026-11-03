// ffff000090709430  size=64  FUN_ffff000090709430

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090709430(undefined8 *param_1)

{
  ulong uVar1;
  
  if (param_1 == (undefined8 *)0x0) {
    uVar1 = _DAT_ffff00009083b000 | 0x80;
  }
  else {
    ttbr0_el1 = *param_1;
    InstructionSynchronizationBarrier();
    TLBI_ASIDE1(0);
    InstructionSynchronizationBarrier();
    uVar1 = _DAT_ffff00009083b000;
  }
  tcr_el1 = uVar1;
  InstructionSynchronizationBarrier();
  return;
}


