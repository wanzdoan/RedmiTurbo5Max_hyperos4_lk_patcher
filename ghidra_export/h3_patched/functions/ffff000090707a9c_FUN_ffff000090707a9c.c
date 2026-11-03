// ffff000090707a9c  size=96  FUN_ffff000090707a9c

void FUN_ffff000090707a9c(undefined8 param_1,long param_2)

{
  undefined8 uVar1;
  
  uVar1 = tpidr_el0;
  uVar1 = tpidrro_el0;
  *(undefined1 **)param_1 = &stack0xffffffffffffff80;
  tpidr_el0 = *(undefined8 *)(param_2 + 0x10);
  tpidrro_el0 = *(undefined8 *)(param_2 + 0x18);
  return;
}


