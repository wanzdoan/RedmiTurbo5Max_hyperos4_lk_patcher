// ffff0000907610dc  size=80  FUN_ffff0000907610dc

void FUN_ffff0000907610dc(undefined8 *param_1)

{
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff00009077de54
              (s__s__d____s___log_recovery_mode_l_ffff0000907db428,
               s_InitRecoveryModelogHeader_ffff0000907fa4be,0x29a,s_BLACKBOX_ffff0000907d9988);
    return;
  }
  if (DAT_ffff00009087bf14 == '\x01') {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v1__no_recovery_m_ffff0000907d355d,
               s_InitRecoveryModelogHeader_ffff0000907fa4be,0x29e);
    return;
  }
  *(undefined4 *)(param_1 + 1) = 0;
  *param_1 = 0xc4a8e6d2f3b5d9a;
  *(undefined8 *)((long)param_1 + 0xc) = 0xb00ac00;
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


