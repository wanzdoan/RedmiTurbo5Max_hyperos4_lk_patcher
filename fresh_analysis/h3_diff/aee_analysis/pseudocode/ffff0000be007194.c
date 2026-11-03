
undefined1  [16] FUN_ffff0000be007194(undefined8 param_1,undefined8 param_2)

{
  long lVar1;
  ulong uVar2;
  undefined1 auVar3 [16];
  undefined1 auVar4 [16];
  
  lVar1 = currentel;
  if (lVar1 == 4) {
    auVar4._8_8_ = param_2;
    auVar4._0_8_ = param_1;
    return auVar4;
  }
  if (lVar1 == 8) {
    elr_el2 = 0xffff0000be007224;
    spsr_el2 = 0x3c5;
  }
  else {
    uVar2 = cptr_el3;
    cptr_el3 = uVar2 & 0xfffffffffffffbff;
    uVar2 = scr_el3;
    scr_el3 = uVar2 | 0x400;
    elr_el3 = 0xffff0000be007224;
    spsr_el3 = 0x3c5;
  }
  cptr_el2 = 0x33ff;
  hcr_el2 = 0x80000000;
  sp_el1 = &stack0x00000000;
  FUN_ffff0000be00356c();
  auVar3._8_8_ = param_2;
  auVar3._0_8_ = param_1;
  InstructionSynchronizationBarrier();
  ExceptionReturn();
  return auVar3;
}


