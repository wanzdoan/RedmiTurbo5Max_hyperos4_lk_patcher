// ffff0000907611c0  size=144  FUN_ffff0000907611c0

void FUN_ffff0000907611c0(undefined8 *param_1)

{
  *(undefined4 *)(param_1 + 1) = 0;
  *param_1 = 0xa7e2c4b6d8f0a1d3;
  *(undefined8 *)((long)param_1 + 0xc) = 0xac10c00;
  *(undefined8 *)((long)param_1 + 0x14) = 0x40000000000;
  *(undefined8 *)((long)param_1 + 0x1c) = 0x10000;
  FUN_ffff00009077ee50((long)param_1 + 0x24,0,0x28);
  FUN_ffff00009077ee50((long)param_1 + 0x4c,0,0x28);
  FUN_ffff00009077ee50((long)param_1 + 0x74,0,0x28);
  FUN_ffff00009077ee50((long)param_1 + 0x9c,0,0xf0);
  return;
}


