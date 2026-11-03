// ffff000090761498  size=84  FUN_ffff000090761498

void FUN_ffff000090761498(undefined8 *param_1)

{
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff00009077e11c
              (s__s__d____s___log_normal_boot_log_ffff0000907dcfc0,
               s_InitNormalBootlogHeader_ffff0000908013f0,0x2b5,s_BLACKBOX_ffff0000907d9a1f);
    return;
  }
  if (DAT_ffff00009087bfac - 1 < 2) {
    FUN_ffff00009077e11c
              (s__s__d__blackbox_v1_v2__no_normal_ffff0000907d4e2c,
               s_InitNormalBootlogHeader_ffff0000908013f0,0x2b9);
    return;
  }
  *(undefined4 *)(param_1 + 1) = 0;
  *param_1 = 0xd3b5a7f9e1c2a4d6;
  *(undefined8 *)((long)param_1 + 0xc) = 0xb3b4800;
  *(undefined8 *)((long)param_1 + 0x14) = 0x40000000000;
  *(undefined8 *)((long)param_1 + 0x1c) = 0x12c000;
  FUN_ffff00009077ee50((long)param_1 + 0x24,0,0x50);
  FUN_ffff00009077ee50((long)param_1 + 0x74,0,0x50);
  FUN_ffff00009077ee50((long)param_1 + 0xc4,0,0x50);
  FUN_ffff00009077ee50((long)param_1 + 0x114,0,0x1e0);
  return;
}


