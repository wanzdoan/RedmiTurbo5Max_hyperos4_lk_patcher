
undefined1  [16] FUN_ffff0000be007140(void)

{
  ulong uVar1;
  undefined1 auVar2 [16];
  
  uVar1 = scr_el3;
  scr_el3 = uVar1 | 0x400;
  hcr_el2 = 0x80000000;
  cptr_el2 = 0x33ff;
  uVar1 = cptr_el3;
  cptr_el3 = uVar1 & 0xfffffffffffffbff;
  cpacr_el1 = 0x300000;
  sp_el1 = &stack0x00000000;
  elr_el3 = 0xffff0000be007224;
  spsr_el3 = 0x3c5;
  InstructionSynchronizationBarrier();
  ExceptionReturn();
  auVar2._8_8_ = 0x400;
  auVar2._0_8_ = 0x3c5;
  return auVar2;
}


