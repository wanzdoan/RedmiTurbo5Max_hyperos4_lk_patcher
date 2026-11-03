// ffff000090717364  size=36  FUN_ffff000090717364

void FUN_ffff000090717364(void)

{
  cntp_tval_el0 = 0;
  InstructionSynchronizationBarrier();
  cntp_ctl_el0 = 1;
  InstructionSynchronizationBarrier();
  return;
}


