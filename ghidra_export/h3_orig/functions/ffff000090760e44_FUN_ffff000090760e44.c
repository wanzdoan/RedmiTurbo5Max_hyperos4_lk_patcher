// ffff000090760e44  size=148  FUN_ffff000090760e44

void FUN_ffff000090760e44(undefined8 *param_1)

{
  *(undefined4 *)(param_1 + 1) = 0;
  *param_1 = 0x3b9d5f1e7c8a2e4f;
  *(undefined8 *)((long)param_1 + 0xc) = 0xa440800;
  *(undefined8 *)((long)param_1 + 0x14) = 0x40000000000;
  *(undefined8 *)((long)param_1 + 0x1c) = 0xc8000;
  FUN_ffff00009077eb88((long)param_1 + 0x24,0,0x50);
  FUN_ffff00009077eb88((long)param_1 + 0x74,0,0x50);
  FUN_ffff00009077eb88((long)param_1 + 0xc4,0,0x50);
  FUN_ffff00009077eb88((long)param_1 + 0x114,0,0x1e0);
  return;
}


