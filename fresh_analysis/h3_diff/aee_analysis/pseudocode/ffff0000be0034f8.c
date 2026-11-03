
void FUN_ffff0000be0034f8(void)

{
  ulong uVar1;
  
  uVar1 = currentel;
  if ((uVar1 & 0x3fffffffc) == 0xc) {
    vbar_el3 = (ulong)PTR_FUN_ffff0000be0f54e8 & 0xffffffffffff;
    InstructionSynchronizationBarrier();
    cntfrq_el0 = 13000000;
    InstructionSynchronizationBarrier();
    FUN_ffff0000be01923c((ulong)PTR_FUN_ffff0000be0f54e8 & 0xffffffffffff);
    FUN_ffff0000be047c50();
    FUN_ffff0000be03673c();
    FUN_ffff0000be01d658();
    FUN_ffff0000be0191a0();
    FUN_ffff0000be04992c();
    FUN_ffff0000be0258a0();
    FUN_ffff0000be02587c();
    FUN_ffff0000be032714();
    return;
  }
  return;
}


