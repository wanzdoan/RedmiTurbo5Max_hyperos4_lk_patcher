// ffff0000907174a8  size=36  FUN_ffff0000907174a8

void FUN_ffff0000907174a8(void)

{
  cntp_tval_el0 = 0;
  InstructionSynchronizationBarrier();
  cntp_ctl_el0 = 1;
  InstructionSynchronizationBarrier();
  return;
}


