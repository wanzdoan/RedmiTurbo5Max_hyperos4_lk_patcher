
void FUN_ffff0000be0070e0(undefined8 param_1,long param_2)

{
  undefined8 uVar1;
  
  uVar1 = tpidr_el0;
  uVar1 = tpidrro_el0;
  *(undefined1 **)param_1 = &stack0xffffffffffffff80;
  tpidr_el0 = *(undefined8 *)(param_2 + 0x10);
  tpidrro_el0 = *(undefined8 *)(param_2 + 0x18);
  return;
}


