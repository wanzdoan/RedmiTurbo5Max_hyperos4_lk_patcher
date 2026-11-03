// ffff000090760fb8  size=80  FUN_ffff000090760fb8

void FUN_ffff000090760fb8(undefined8 *param_1)

{
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff00009077de54
              (s__s__d____s___log_fastboot_mode_l_ffff0000907f89d2,
               s_InitFastbootModelogHeader_ffff0000907e50ee,0x280,s_BLACKBOX_ffff0000907d9988);
    return;
  }
  if (DAT_ffff00009087bf14 == '\x01') {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v1__no_fastboot_m_ffff0000907fdd50,
               s_InitFastbootModelogHeader_ffff0000907e50ee,0x284);
    return;
  }
  *(undefined4 *)(param_1 + 1) = 0;
  *param_1 = 0x5e9f1d3b7a8c2e0f;
  *(undefined8 *)((long)param_1 + 0xc) = 0xac61000;
  *(undefined8 *)((long)param_1 + 0x14) = 0x40000000000;
  *(undefined8 *)((long)param_1 + 0x1c) = 0xbb800;
  FUN_ffff00009077eb88((long)param_1 + 0x24,0,0x28);
  FUN_ffff00009077eb88((long)param_1 + 0x4c,0,0x28);
  FUN_ffff00009077eb88((long)param_1 + 0x74,0,0x28);
  FUN_ffff00009077eb88((long)param_1 + 0x9c,0,0x28);
  FUN_ffff00009077eb88((long)param_1 + 0xc4,0,0x28);
  FUN_ffff00009077eb88((long)param_1 + 0xec,0,0x28);
  FUN_ffff00009077eb88((long)param_1 + 0x114,0,0x28);
  FUN_ffff00009077eb88((long)param_1 + 0x13c,0,0xf0);
  return;
}


