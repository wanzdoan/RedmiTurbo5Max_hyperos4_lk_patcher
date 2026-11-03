
ulong FUN_ffff0000be00356c(void)

{
  ulong uVar1;
  
  uVar1 = hcr_el2;
  hcr_el2 = uVar1 | 0x30000000000;
  InstructionSynchronizationBarrier();
  uVar1 = scr_el3;
  scr_el3 = uVar1 | 0x30000;
  InstructionSynchronizationBarrier();
  uVar1 = cnthctl_el2;
  cnthctl_el2 = uVar1 | 3;
  uVar1 = scr_el3;
  scr_el3 = uVar1 | 1;
  return uVar1 | 1;
}


