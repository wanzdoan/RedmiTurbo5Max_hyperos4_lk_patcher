// ffff000090760f28  size=144  FUN_ffff000090760f28

void FUN_ffff000090760f28(undefined8 *param_1)

{
  *(undefined4 *)(param_1 + 1) = 0;
  *param_1 = 0xa7e2c4b6d8f0a1d3;
  *(undefined8 *)((long)param_1 + 0xc) = 0xac10c00;
  *(undefined8 *)((long)param_1 + 0x14) = 0x40000000000;
  *(undefined8 *)((long)param_1 + 0x1c) = 0x10000;
  FUN_ffff00009077eb88((long)param_1 + 0x24,0,0x28);
  FUN_ffff00009077eb88((long)param_1 + 0x4c,0,0x28);
  FUN_ffff00009077eb88((long)param_1 + 0x74,0,0x28);
  FUN_ffff00009077eb88((long)param_1 + 0x9c,0,0xf0);
  return;
}


