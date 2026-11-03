
void FUN_ffff0000907093bc(undefined8 *param_1)

{
  ulong uVar1;
  
  if (param_1 == (undefined8 *)0x0) {
    uVar1 = DAT_ffff00009083b000 | 0x80;
  }
  else {
    ttbr0_el1 = *param_1;
    InstructionSynchronizationBarrier();
    TLBI_ASIDE1(0);
    InstructionSynchronizationBarrier();
    uVar1 = DAT_ffff00009083b000;
  }
  tcr_el1 = uVar1;
  InstructionSynchronizationBarrier();
  return;
}


