// ffff0000907610dc  size=148  FUN_ffff0000907610dc

void FUN_ffff0000907610dc(undefined8 *param_1)

{
  *(undefined4 *)(param_1 + 1) = 0;
  *param_1 = 0x3b9d5f1e7c8a2e4f;
  *(undefined8 *)((long)param_1 + 0xc) = 0xa440800;
  *(undefined8 *)((long)param_1 + 0x14) = 0x40000000000;
  *(undefined8 *)((long)param_1 + 0x1c) = 0xc8000;
  FUN_ffff00009077ee50((long)param_1 + 0x24,0,0x50);
  FUN_ffff00009077ee50((long)param_1 + 0x74,0,0x50);
  FUN_ffff00009077ee50((long)param_1 + 0xc4,0,0x50);
  FUN_ffff00009077ee50((long)param_1 + 0x114,0,0x1e0);
  return;
}


