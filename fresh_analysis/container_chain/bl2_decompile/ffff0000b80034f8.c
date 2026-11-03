
void FUN_ffff0000b80034f8(void)

{
  ulong uVar1;
  
  uVar1 = currentel;
  if ((uVar1 & 0x3fffffffc) == 0xc) {
    vbar_el3 = (ulong)PTR_FUN_ffff0000b8108658 & 0xffffffffffff;
    InstructionSynchronizationBarrier();
    cntfrq_el0 = 13000000;
    InstructionSynchronizationBarrier();
    uVar1 = scr_el3;
    scr_el3 = uVar1 | 0x30000;
    InstructionSynchronizationBarrier();
    FUN_ffff0000b80098c0(uVar1 | 0x30000);
    FUN_ffff0000b803e88c();
    FUN_ffff0000b800e738();
    return;
  }
  return;
}


