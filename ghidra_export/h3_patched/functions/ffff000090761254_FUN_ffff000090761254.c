// ffff000090761254  size=148  FUN_ffff000090761254

void FUN_ffff000090761254(undefined8 *param_1)

{
  *(undefined4 *)(param_1 + 1) = 0;
  *param_1 = 0xd3b5a7f9e1c2a4d6;
  *(undefined8 *)((long)param_1 + 0xc) = 0xb3b4800;
  *(undefined8 *)((long)param_1 + 0x14) = 0x40000000000;
  *(undefined8 *)((long)param_1 + 0x1c) = 0x12c000;
  FUN_ffff00009077eb88((long)param_1 + 0x24,0,0x50);
  FUN_ffff00009077eb88((long)param_1 + 0x74,0,0x50);
  FUN_ffff00009077eb88((long)param_1 + 0xc4,0,0x50);
  FUN_ffff00009077eb88((long)param_1 + 0x114,0,0x1e0);
  return;
}


